/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;


import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PeerDuplicateLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * QualifiedNamePeerDuplicateGovernanceActionConnector checks the qualified name to determine the duplicates of the entity that is passed
 * as an action target.  Every other element of the same type with the same qualified name is linked to the action target with a
 * PeerDuplicateLink relationship, and both elements are given the KnownDuplicate classification.
 * Duplicates that are already linked to the action target are not linked again, so the service can be rerun safely.
 * If at least one new duplicate is linked, the output guard is duplicate-detected.
 * If all the duplicates are already linked, the output guard is duplicate-already-assigned.
 * If there are no duplicates, the output guard is no-duplication-detected.
 * If the action target does not have a qualified name, the output guard is invalid-action-target-type.
 */
public class QualifiedNamePeerDuplicateGovernanceActionConnector extends GeneralGovernanceActionService
{
    /**
     * Value used in the PeerDuplicateLink relationships to show that the duplicate is usable.
     */
    private static final int statusIdentifier = 1;


    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * <p>
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException a problem within the governance action service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        super.start();

        QualifiedNamePeerDuplicateGuard guard;
        AuditLogMessageDefinition       messageDefinition = null;

        try
        {
            List<ActionTargetElement> actionTargets = governanceContext.getActionTargetElements();

            if ((actionTargets == null) || (actionTargets.isEmpty()))
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_TARGETS.getMessageDefinition(governanceServiceName);
                guard = QualifiedNamePeerDuplicateGuard.NO_TARGETS_DETECTED;
            }
            else if (actionTargets.size() == 1)
            {
                ActionTargetElement actionTarget  = actionTargets.get(0);
                OpenMetadataElement targetElement = null;

                if (actionTarget != null)
                {
                    targetElement = actionTarget.getTargetElement();
                }

                String qualifiedName = this.getQualifiedName(targetElement, methodName);

                if (qualifiedName == null)
                {
                    String actionTargetGUID = null;

                    if (actionTarget != null)
                    {
                        actionTargetGUID = actionTarget.getActionTargetGUID();
                    }

                    messageDefinition = GovernanceActionConnectorsAuditCode.NO_QUALIFIED_NAME.getMessageDefinition(governanceServiceName,
                                                                                                                   actionTargetGUID);
                    guard = QualifiedNamePeerDuplicateGuard.INVALID_ACTION_TARGET_TYPE;
                }
                else
                {
                    guard = this.linkPeerDuplicates(targetElement, qualifiedName, methodName);
                }
            }
            else
            {
                guard = QualifiedNamePeerDuplicateGuard.MULTIPLE_TARGETS_DETECTED;
            }

            if (messageDefinition != null)
            {
                logRecord(methodName, messageDefinition);
            }

            List<String> outputGuards = new ArrayList<>();

            outputGuards.add(guard.getName());

            governanceContext.recordCompletionStatus(guard.getCompletionStatus(), outputGuards, null, null, messageDefinition);
        }
        catch (OMFCheckedExceptionBase error)
        {
            throw new ConnectorCheckedException(error.getReportedErrorMessage(), error);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(GovernanceActionConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Return the qualified name of the action target's element.
     *
     * @param targetElement element to query
     * @param methodName calling method
     * @return qualified name or null if the element does not have one
     */
    private String getQualifiedName(OpenMetadataElement targetElement,
                                    String              methodName)
    {
        if ((targetElement == null) || (targetElement.getElementGUID() == null) || (targetElement.getType() == null))
        {
            return null;
        }

        return propertyHelper.getStringProperty(governanceServiceName,
                                                OpenMetadataProperty.QUALIFIED_NAME.name,
                                                targetElement.getElementProperties(),
                                                methodName);
    }


    /**
     * Link each element with the same type and qualified name as the action target to the action target as a peer duplicate.
     * Elements that are already linked to the action target are skipped, and the KnownDuplicate classification is only
     * added to the elements that do not have it already.
     *
     * @param targetElement element from the action target
     * @param qualifiedName qualified name of the action target
     * @param methodName calling method
     * @return guard describing the result
     * @throws InvalidParameterException a parameter passed to the open metadata store is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to work with the elements
     * @throws PropertyServerException a problem in the open metadata store
     */
    private QualifiedNamePeerDuplicateGuard linkPeerDuplicates(OpenMetadataElement targetElement,
                                                               String              qualifiedName,
                                                               String              methodName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        OpenMetadataStore store = governanceContext.getOpenMetadataStore();

        store.setForDuplicateProcessing(true);
        store.setForLineage(true);

        String targetElementGUID = targetElement.getElementGUID();

        List<OpenMetadataElement> duplicates = this.getDuplicates(store, targetElement, qualifiedName);

        if (duplicates.isEmpty())
        {
            return QualifiedNamePeerDuplicateGuard.NO_DUPLICATION_DETECTED;
        }

        Set<String> linkedPeerDuplicateGUIDs = this.getLinkedPeerDuplicateGUIDs(store, targetElementGUID);

        ClassificationExplorerClient classificationExplorerClient = governanceContext.getClassificationExplorerClient();
        MakeAnchorOptions            makeAnchorOptions            = new MakeAnchorOptions(classificationExplorerClient.getMetadataSourceOptions());

        boolean newDuplicateLinked = false;

        for (OpenMetadataElement duplicate : duplicates)
        {
            String duplicateGUID = duplicate.getElementGUID();

            if (! linkedPeerDuplicateGUIDs.contains(duplicateGUID))
            {
                PeerDuplicateLinkProperties peerDuplicateLinkProperties = new PeerDuplicateLinkProperties();

                peerDuplicateLinkProperties.setStatusIdentifier(statusIdentifier);
                peerDuplicateLinkProperties.setSource(governanceServiceName);

                classificationExplorerClient.linkElementsAsPeerDuplicates(targetElementGUID,
                                                                          duplicateGUID,
                                                                          peerDuplicateLinkProperties,
                                                                          makeAnchorOptions);

                logRecord(methodName,
                          GovernanceActionConnectorsAuditCode.PEER_DUPLICATE_LINKED.getMessageDefinition(governanceServiceName,
                                                                                                         targetElementGUID,
                                                                                                         duplicateGUID,
                                                                                                         qualifiedName));
                newDuplicateLinked = true;
            }

            if (! propertyHelper.isClassified(duplicate, OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName))
            {
                classificationExplorerClient.setKnownDuplicateClassification(duplicateGUID, null, makeAnchorOptions);
            }
        }

        if (! propertyHelper.isClassified(targetElement, OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName))
        {
            classificationExplorerClient.setKnownDuplicateClassification(targetElementGUID, null, makeAnchorOptions);
        }

        if (newDuplicateLinked)
        {
            return QualifiedNamePeerDuplicateGuard.DUPLICATE_ASSIGNED;
        }

        return QualifiedNamePeerDuplicateGuard.DUPLICATE_ALREADY_ASSIGNED;
    }


    /**
     * Return the other elements of the same type as the action target that have the same qualified name.
     *
     * @param store open metadata store
     * @param targetElement element from the action target
     * @param qualifiedName qualified name of the action target
     * @return list of duplicates - empty if there are none
     * @throws InvalidParameterException a parameter passed to the open metadata store is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to retrieve the elements
     * @throws PropertyServerException a problem in the open metadata store
     */
    private List<OpenMetadataElement> getDuplicates(OpenMetadataStore   store,
                                                    OpenMetadataElement targetElement,
                                                    String              qualifiedName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        List<OpenMetadataElement> duplicates = new ArrayList<>();

        List<OpenMetadataElement> elements = store.findMetadataElements(targetElement.getType().getTypeName(),
                                                                        null,
                                                                        this.getSearchProperties(qualifiedName),
                                                                        null,
                                                                        0,
                                                                        0);

        if (elements != null)
        {
            for (OpenMetadataElement element : elements)
            {
                if ((element != null) && (element.getElementGUID() != null) && (! element.getElementGUID().equals(targetElement.getElementGUID())))
                {
                    duplicates.add(element);
                }
            }
        }

        return duplicates;
    }


    /**
     * Return the unique identifiers of the elements that are already linked to the action target as peer duplicates.
     *
     * @param store open metadata store
     * @param targetElementGUID unique identifier of the action target's element
     * @return set of unique identifiers
     * @throws InvalidParameterException a parameter passed to the open metadata store is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to retrieve the elements
     * @throws PropertyServerException a problem in the open metadata store
     */
    private Set<String> getLinkedPeerDuplicateGUIDs(OpenMetadataStore store,
                                                    String            targetElementGUID) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        Set<String> linkedPeerDuplicateGUIDs = new HashSet<>();

        RelatedMetadataElementList peerDuplicates = store.getRelatedMetadataElements(targetElementGUID,
                                                                                     0,
                                                                                     OpenMetadataType.PEER_DUPLICATE_LINK.typeName,
                                                                                     0,
                                                                                     0);

        if ((peerDuplicates != null) && (peerDuplicates.getElementList() != null))
        {
            for (RelatedMetadataElement peerDuplicate : peerDuplicates.getElementList())
            {
                if ((peerDuplicate != null) && (peerDuplicate.getElement() != null))
                {
                    linkedPeerDuplicateGUIDs.add(peerDuplicate.getElement().getElementGUID());
                }
            }
        }

        return linkedPeerDuplicateGUIDs;
    }


    /**
     * Build up property parameters for a search.
     *
     * @param qualifiedName name to search for
     * @return search properties
     */
    private SearchProperties getSearchProperties(String qualifiedName)
    {
        SearchProperties searchProperties = new SearchProperties();
        List<PropertyCondition> conditions = new ArrayList<>();
        PropertyCondition condition = new PropertyCondition();
        PrimitiveTypePropertyValue primitivePropertyValue = new PrimitiveTypePropertyValue();

        primitivePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
        primitivePropertyValue.setPrimitiveValue(qualifiedName);
        primitivePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getDisplayName());

        condition.setProperty(OpenMetadataProperty.QUALIFIED_NAME.name);
        condition.setOperator(PropertyComparisonOperator.EQ);
        condition.setValue(primitivePropertyValue);

        conditions.add(condition);

        searchProperties.setConditions(conditions);
        searchProperties.setMatchCriteria(MatchCriteria.ALL);
        return searchProperties;
    }
}
