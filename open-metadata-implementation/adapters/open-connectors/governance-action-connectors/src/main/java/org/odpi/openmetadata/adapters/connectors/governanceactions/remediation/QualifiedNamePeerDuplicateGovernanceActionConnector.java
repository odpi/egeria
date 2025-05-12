/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;


import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.governanceaction.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.governanceaction.RemediationGovernanceActionService;
import org.odpi.openmetadata.frameworks.governanceaction.controls.Guard;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.MatchCriteria;
import org.odpi.openmetadata.frameworks.openmetadata.search.PrimitiveTypeCategory;
import org.odpi.openmetadata.frameworks.openmetadata.search.PrimitiveTypePropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyCondition;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * QualifiedNamePeerDuplicateGovernanceActionConnector checks the qualified name to determine the duplicates of the entity that is passed
 * as an action target.
 */
public class QualifiedNamePeerDuplicateGovernanceActionConnector extends RemediationGovernanceActionService
{
    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * <p>
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        final String methodName = "start";

        super.start();

        List<String> outputGuards = new ArrayList<>();
        CompletionStatus completionStatus = CompletionStatus.INVALID;

        try
        {
            if (governanceContext.getActionTargetElements() == null)
            {
                outputGuards.add(Guard.NO_TARGETS_DETECTED.getName());
                completionStatus = Guard.NO_TARGETS_DETECTED.getCompletionStatus();
            }
            else if (governanceContext.getActionTargetElements().size() == 1)
            {
                ActionTargetElement actionTarget = governanceContext.getActionTargetElements().get(0);
                OpenMetadataElement targetElement = actionTarget.getTargetElement();

                OpenMetadataStore store = governanceContext.getOpenMetadataStore();
                store.setForDuplicateProcessing(true);
                store.setForLineage(true);

                String qualifiedName = targetElement.getElementProperties().getPropertyValueMap().get(OpenMetadataProperty.QUALIFIED_NAME.name).valueAsString();
                SearchProperties searchProperties = getSearchProperties(qualifiedName);
                List<OpenMetadataElement> elements = store.findMetadataElements(targetElement.getType().getTypeId(),
                                                                                null,
                                                                                searchProperties,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                0,
                                                                                0);

                if (elements != null)
                {
                    String targetElementGUID = targetElement.getElementGUID();
                    if (elements.size() == 1 && elements.get(0).getElementGUID().equalsIgnoreCase(targetElementGUID))
                    {
                        outputGuards.add(QualifiedNamePeerDuplicateGuard.NO_DUPLICATION_DETECTED.getName());
                        completionStatus = QualifiedNamePeerDuplicateGuard.NO_DUPLICATION_DETECTED.getCompletionStatus();
                    }
                    for (OpenMetadataElement duplicateAsset : elements)
                    {
                        String duplicateAssetGUID = duplicateAsset.getElementGUID();
                        if (duplicateAssetGUID.equalsIgnoreCase(targetElementGUID))
                        {
                            continue;
                        }

                        governanceContext.linkElementsAsPeerDuplicates(targetElementGUID,
                                                                       duplicateAssetGUID,
                                                                       1,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       true);
                        outputGuards.add(QualifiedNamePeerDuplicateGuard.DUPLICATE_ASSIGNED.getName());
                        completionStatus = QualifiedNamePeerDuplicateGuard.DUPLICATE_ASSIGNED.getCompletionStatus();
                        break;
                    }
                }
            }
            else
            {
                outputGuards.add(Guard.MULTIPLE_TARGETS_DETECTED.getName());
                completionStatus = Guard.MULTIPLE_TARGETS_DETECTED.getCompletionStatus();
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards);
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
        primitivePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());

        condition.setProperty(OpenMetadataProperty.QUALIFIED_NAME.name);
        condition.setOperator(PropertyComparisonOperator.EQ);
        condition.setValue(primitivePropertyValue);

        conditions.add(condition);

        searchProperties.setConditions(conditions);
        searchProperties.setMatchCriteria(MatchCriteria.ALL);
        return searchProperties;
    }
}
