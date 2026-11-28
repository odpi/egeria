/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OriginSeekerGovernanceActionConnector uses the lineage relationships to determine the origin of the asset that is passed
 * as the newAsset action target.  It follows each lineage path, collecting the AssetOrigin classifications.  If there is only one, it is
 * added to the action target asset and the output guard is set to origin-assigned.  If there are multiple origin's identified they are
 * added to the detectedOrigins request parameter and the output guard is multiple-origins-detected.
 * If no AssetOrigin classifications are found, the output guard is no-origins-detected.
 * Note: This implementation currently only follows the LineageMapping, DataFlow, ControlFlow and ProcessCall relationships attached
 * directly to the elements in the lineage graph, it needs extending to support lineage mapping between ports and schema attributes.
 */
public class OriginSeekerGovernanceActionConnector extends GeneralGovernanceActionService
{
    private static final String detectedOriginsProperty   = "detectedOrigins";
    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();

    /*
     * The lineage relationships that are followed upstream from the asset.
     */
    private static final List<String> lineageRelationshipTypeNames = List.of(OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName,
                                                                             OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                             OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName,
                                                                             OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName);


    /**
     * Indicates that the governance action service is completely configured and can begin processing.
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

        List<String>        outputGuards = new ArrayList<>();
        CompletionStatus    completionStatus;
        Map<String, String> newRequestParameters = new HashMap<>();

        governanceContext.getOpenMetadataStore().setForLineage(true);

        try
        {
            List<ActionTargetElement> newAssetElements = new ArrayList<>();

            if (governanceContext.getActionTargetElements() != null)
            {
                for (ActionTargetElement actionTargetElement : governanceContext.getActionTargetElements())
                {
                    if ((actionTargetElement != null) && (actionTargetElement.getActionTargetName() != null))
                    {
                        if (actionTargetElement.getActionTargetName().equals(ActionTarget.NEW_ASSET.getName()))
                        {
                            newAssetElements.add(actionTargetElement);
                        }
                    }
                }
            }

            if (newAssetElements.isEmpty())
            {
                completionStatus = OriginSeekerGuard.NO_TARGETS.getCompletionStatus();
                outputGuards.add(OriginSeekerGuard.NO_TARGETS.getName());
            }
            else if (newAssetElements.size() == 1)
            {
                completionStatus = seekOrigin(newAssetElements.get(0), newRequestParameters, outputGuards);
            }
            else
            {
                /*
                 * Multiple action targets to supply.  This governance action does not support multiple action targets because the
                 * result of the origin search could be different for each action target and so it would be difficult to automate the response.
                 */
                completionStatus = OriginSeekerGuard.MULTIPLE_TARGETS.getCompletionStatus();
                outputGuards.add(OriginSeekerGuard.MULTIPLE_TARGETS.getName());
            }

            if (newRequestParameters.isEmpty())
            {
                newRequestParameters = null;
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards, newRequestParameters, null);
        }
        catch (OMFCheckedExceptionBase error)
        {
            this.recordFailure(error);

            throw new ConnectorCheckedException(error.getReportedErrorMessage(), error);
        }
        catch (Exception error)
        {
            this.recordFailure(error);

            throw new ConnectorCheckedException(GovernanceActionConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    private CompletionStatus seekOrigin(ActionTargetElement actionTarget,
                                        Map<String, String> newRequestParameters,
                                        List<String>        outputGuards) throws Exception
    {
        CompletionStatus completionStatus = null;
        OpenMetadataElement targetElement = actionTarget.getTargetElement();

        if (propertyHelper.isTypeOf(targetElement, OpenMetadataType.ASSET.typeName))
        {
            /*
             * Check that the AssetOrigin classification is not already set - this is an error if it is.
             */
            AttachedClassification existingAssetOriginClassification = propertyHelper.getClassification(targetElement, OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName);

            if (existingAssetOriginClassification != null)
            {
                completionStatus = OriginSeekerGuard.ORIGIN_ALREADY_ASSIGNED.getCompletionStatus();
                outputGuards.add(OriginSeekerGuard.ORIGIN_ALREADY_ASSIGNED.getName());
            }

            if (completionStatus == null)
            {
                /*
                 * No current AssetOrigin classification is present so ok to begin seeking the origin through the lineage.
                 * This method returns a list of origin classifications detected from walking the lineage tree.
                 * The returned list has been deduplicated.
                 */
                List<String> coveredEntityGUIDs = new ArrayList<>();
                coveredEntityGUIDs.add(targetElement.getElementGUID());

                List<ElementProperties> originClassifications = this.getOrigins(targetElement, coveredEntityGUIDs);

                if (originClassifications == null)
                {
                    /*
                     * No origin classifications have been detected which means the guard needs to be set so that a manual assignment
                     * can be initiated.
                     */
                    outputGuards.add(OriginSeekerGuard.NO_ORIGINS_DETECTED.getName());
                    completionStatus = OriginSeekerGuard.NO_ORIGINS_DETECTED.getCompletionStatus();
                }
                else if (originClassifications.size() == 1)
                {
                    /*
                     * A single origin has been found, so it is ok to add it to the action target asset.
                     */
                    governanceContext.getOpenMetadataStore().classifyMetadataElementInStore(targetElement.getElementGUID(),
                                                                                            OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName,
                                                                                            governanceContext.getOpenMetadataStore().getMetadataSourceOptions(),
                                                                                            new NewElementProperties(originClassifications.get(0)));

                    outputGuards.add(OriginSeekerGuard.ORIGIN_ASSIGNED.getName());
                    completionStatus = OriginSeekerGuard.ORIGIN_ASSIGNED.getCompletionStatus();
                }
                else /* multiple origins to choose from */
                {
                    /*
                     * There are multiple possible origin classifications to use.  This is going to need a manual assignment and so
                     * the different origin values are added to the request parameters that will be added to the request parameters to
                     * make it easier for the steward to understand the origins found in the lineage.
                     */
                    String jsonString = OBJECT_WRITER.writeValueAsString(originClassifications);

                    newRequestParameters.put(detectedOriginsProperty, jsonString);

                    outputGuards.add(OriginSeekerGuard.MULTIPLE_ORIGINS_DETECTED.getName());
                    completionStatus = OriginSeekerGuard.MULTIPLE_ORIGINS_DETECTED.getCompletionStatus();
                }
            }
        }
        else
        {
            completionStatus = OriginSeekerGuard.TARGET_NOT_ASSET.getCompletionStatus();
            outputGuards.add(OriginSeekerGuard.TARGET_NOT_ASSET.getName());
        }

        return completionStatus;
    }


    /**
     * Return the origin classifications found by walking upstream through the lineage graph from the supplied element.
     * Each branch of the graph is followed until an element with an origin classification is found.
     *
     * @param asset element to start from
     * @param coveredEntityGUIDs unique identifiers of the elements that have already been visited
     * @return deduplicated list of origin classification properties, or null if none are found
     * @throws InvalidParameterException invalid parameter
     * @throws UserNotAuthorizedException the service is no longer active
     * @throws PropertyServerException problem with the metadata store
     */
    private List<ElementProperties> getOrigins(OpenMetadataElement  asset,
                                               List<String>         coveredEntityGUIDs) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        List<ElementProperties> results = new ArrayList<>();

        /*
         * Explore each branch in the lineage map.
         */
        for (OpenMetadataElement nextAsset : this.getUpstreamElements(asset.getElementGUID()))
        {
            /*
             * Some lineage graphs are circular so the covered entity guids prevents the same element from being processed twice.
             */
            if (! coveredEntityGUIDs.contains(nextAsset.getElementGUID()))
            {
                coveredEntityGUIDs.add(nextAsset.getElementGUID());

                /*
                 * If we find an origin classification on this asset we stop traversing the lineage graph.
                 */
                AttachedClassification existingAssetOriginClassification = propertyHelper.getClassification(nextAsset, OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName);

                if (existingAssetOriginClassification == null)
                {
                    /*
                     * No origin classification so it must look further back in the lineage graph.
                     */
                    List<ElementProperties> upstreamResults = getOrigins(nextAsset, coveredEntityGUIDs);

                    if ((upstreamResults != null) && (! upstreamResults.isEmpty()))
                    {
                        /*
                         * Now it is necessary to merge and deduplicate the results.
                         */
                        for (ElementProperties upstreamResult : upstreamResults)
                        {
                            if (upstreamResult != null)
                            {
                                if (! results.contains(upstreamResult))
                                {
                                    results.add(upstreamResult);
                                }
                            }
                        }
                    }
                }
                else
                {
                    /*
                     * There is an origin classification so process it.
                     */
                    if (existingAssetOriginClassification.getClassificationProperties() != null)
                    {
                        if (! results.contains(existingAssetOriginClassification.getClassificationProperties()))
                        {
                            results.add(existingAssetOriginClassification.getClassificationProperties());
                        }
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }

        return results;
    }


    /**
     * Return the elements that are one step upstream of the supplied element in the lineage graph.  Each type of
     * lineage relationship is retrieved on its own, page by page, so that the lineage relationships are not lost
     * among the element's other relationships, however many there are.
     * Note this is only working with lineage relationships attached directly to the element.  It would need
     * extending to work with lineage between ports and schema elements.
     *
     * @param elementGUID unique identifier of the element
     * @return list of upstream elements (may be empty)
     * @throws InvalidParameterException invalid parameter
     * @throws UserNotAuthorizedException the service is no longer active
     * @throws PropertyServerException problem with the metadata store
     */
    private List<OpenMetadataElement> getUpstreamElements(String elementGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        List<OpenMetadataElement> upstreamElements = new ArrayList<>();

        OpenMetadataStore store    = governanceContext.getOpenMetadataStore();
        int               pageSize = governanceContext.getMaxPageSize();

        for (String relationshipTypeName : lineageRelationshipTypeNames)
        {
            int startFrom = 0;

            /*
             * Retrieving from end 2 means it is working upstream on the lineage relationships.
             */
            RelatedMetadataElementList lineageLinks = store.getRelatedMetadataElements(elementGUID, 2, relationshipTypeName, startFrom, pageSize);

            while ((lineageLinks != null) && (lineageLinks.getElementList() != null) && (! lineageLinks.getElementList().isEmpty()))
            {
                for (RelatedMetadataElement lineageLink : lineageLinks.getElementList())
                {
                    if ((lineageLink != null) && (lineageLink.getElement() != null))
                    {
                        upstreamElements.add(lineageLink.getElement());
                    }
                }

                if ((pageSize <= 0) || (lineageLinks.getElementList().size() < pageSize))
                {
                    break;
                }

                startFrom    = startFrom + pageSize;
                lineageLinks = store.getRelatedMetadataElements(elementGUID, 2, relationshipTypeName, startFrom, pageSize);
            }
        }

        return upstreamElements;
    }


    /**
     * Record the origin-seeking-failed guard, so that a governance action process can respond to the failure.
     * The exception is still thrown by the caller.
     *
     * @param error exception that stopped the service
     */
    private void recordFailure(Exception error)
    {
        try
        {
            List<String> outputGuards = new ArrayList<>();

            outputGuards.add(OriginSeekerGuard.ORIGIN_SEEKING_FAILED.getName());

            governanceContext.recordCompletionStatus(OriginSeekerGuard.ORIGIN_SEEKING_FAILED.getCompletionStatus(),
                                                     outputGuards,
                                                     null,
                                                     null,
                                                     error.getMessage());
        }
        catch (Exception recordError)
        {
            /*
             * The exception from the service is more important, and is thrown by the caller.
             */
        }
    }
}
