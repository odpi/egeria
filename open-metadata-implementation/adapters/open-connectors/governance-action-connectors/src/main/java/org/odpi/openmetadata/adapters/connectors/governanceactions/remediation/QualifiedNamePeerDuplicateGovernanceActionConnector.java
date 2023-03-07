/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;


import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.governanceaction.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.governanceaction.RemediationGovernanceActionService;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.MatchCriteria;
import org.odpi.openmetadata.frameworks.governanceaction.search.PrimitiveTypeCategory;
import org.odpi.openmetadata.frameworks.governanceaction.search.PrimitiveTypePropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyCondition;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * QualifiedNamePeerDuplicateGovernanceActionConnector checks the qualified name to determine the duplicates of the entity that is passed
 * as an action target.
 */
public class QualifiedNamePeerDuplicateGovernanceActionConnector extends RemediationGovernanceActionService
{
    private static final String QUALIFIED_NAME_PROPERTY = "qualifiedName";

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
                completionStatus = CompletionStatus.FAILED;
                outputGuards.add(QualifiedNamePeerDuplicateGovernanceActionProvider.NO_TARGETS_DETECTED_GUARD);
            }
            else if (governanceContext.getActionTargetElements().size() == 1)
            {
                ActionTargetElement actionTarget = governanceContext.getActionTargetElements().get(0);
                OpenMetadataElement targetElement = actionTarget.getTargetElement();

                OpenMetadataClient store = (OpenMetadataClient) governanceContext.getOpenMetadataStore();

                String qualifiedName = targetElement.getElementProperties().getPropertyValueMap().get(QUALIFIED_NAME_PROPERTY).valueAsString();
                SearchProperties searchProperties = getSearchProperties(qualifiedName);
                List<OpenMetadataElement> elements = store.findMetadataElements(targetElement.getType().getTypeId(),
                                                                                null,
                                                                                searchProperties,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                false,
                                                                                true,
                                                                                new Date(),
                                                                                0,
                                                                                0);

                if (elements != null)
                {
                    String targetElementGUID = targetElement.getElementGUID();
                    if (elements.size() == 1 && elements.get(0).getElementGUID().equalsIgnoreCase(targetElementGUID))
                    {
                        outputGuards.add(QualifiedNamePeerDuplicateGovernanceActionProvider.NO_DUPLICATION_DETECTED_GUARD);
                    }
                    for (OpenMetadataElement duplicateAsset : elements)
                    {
                        String duplicateAssetGUID = duplicateAsset.getElementGUID();
                        if (duplicateAssetGUID.equalsIgnoreCase(targetElementGUID))
                        {
                            continue;
                        }

                        store.linkElementsAsPeerDuplicates(targetElementGUID,
                                                           duplicateAssetGUID,
                                                           1,
                                                           null,
                                                           null,
                                                           null,
                                                           null,
                                                           null,
                                                           true);
                        outputGuards.add(QualifiedNamePeerDuplicateGovernanceActionProvider.DUPLICATE_ASSIGNED_GUARD);
                        completionStatus = CompletionStatus.ACTIONED;
                        break;
                    }
                }
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards);
        }
        catch (OCFCheckedExceptionBase error)
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
        primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());

        condition.setProperty(QUALIFIED_NAME_PROPERTY);
        condition.setOperator(PropertyComparisonOperator.EQ);
        condition.setValue(primitivePropertyValue);

        conditions.add(condition);

        searchProperties.setConditions(conditions);
        searchProperties.setMatchCriteria(MatchCriteria.ALL);
        return searchProperties;
    }
}
