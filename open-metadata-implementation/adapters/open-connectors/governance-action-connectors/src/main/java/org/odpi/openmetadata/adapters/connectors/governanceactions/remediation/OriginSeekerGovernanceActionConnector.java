/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.governanceaction.RemediationGovernanceActionService;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OriginSeekerGovernanceActionConnector uses the lineage mapping relationships to determine the origin of the asset that is passed
 * as an action target.  It follows each lineage path, collecting the AssetOrigin classifications.  If there is only one, it is
 * added to the action target asset and the output guard is set to origin-assigned.  If there are multiple origin's identified they are
 * added to the detectedOrigins request parameter and the output guard is multiple-origins-detected.
 * If no AssetOrigin classifications are found, the output guard is no-origins-detected.
 * Note: This implementation currently only follows LineageMapping links between assets, it needs extending to support lineage mapping
 * between ports and schema attributes.
 */
public class OriginSeekerGovernanceActionConnector extends RemediationGovernanceActionService
{
    private static final String assetOriginClassification = "AssetOrigin";
    private static final String detectedOriginsProperty   = "detectedOrigins";
    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();

    private final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Indicates that the governance action service is completely configured and can begin processing.
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

        List<String>          outputGuards = new ArrayList<>();
        CompletionStatus      completionStatus = null;
        Map<String, String>   newRequestParameters = null;

        try
        {
            if (governanceContext.getActionTargetElements() == null)
            {
                completionStatus = OriginSeekerGuard.NO_TARGETS_DETECTED.getCompletionStatus();
                outputGuards.add(OriginSeekerGuard.NO_TARGETS_DETECTED.getName());
            }
            else if (governanceContext.getActionTargetElements().size() == 1)
            {
                ActionTargetElement actionTarget = governanceContext.getActionTargetElements().get(0);

                OpenMetadataElement targetElement = actionTarget.getTargetElement();

                if (propertyHelper.isTypeOf(targetElement, OpenMetadataType.ASSET.typeName))
                {
                    /*
                     * Check that the AssetOrigin classification is not already set - this is an error if it is.
                     */
                    AttachedClassification existingAssetOriginClassification = this.getAssetOriginClassification(targetElement);

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
                            governanceContext.classifyMetadataElement(targetElement.getElementGUID(),
                                                                      assetOriginClassification,
                                                                      false,
                                                                      false,
                                                                      originClassifications.get(0),
                                                                      new Date());

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
                            newRequestParameters = new HashMap<>();

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
            }
            else
            {
                /*
                 * Multiple action targets to supply.  This governance action does not support multiple action targets because the
                 * result of the origin search could be different for each action target and so it would be difficult to automate the response.
                 */
                completionStatus = OriginSeekerGuard.MULTIPLE_TARGETS_DETECTED.getCompletionStatus();
                outputGuards.add(OriginSeekerGuard.MULTIPLE_TARGETS_DETECTED.getName());
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards, newRequestParameters, null);
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
     * Return the AssetOrigin classification from an asset entity (if set).
     *
     * @param asset asset element to check
     * @return null or located AssetOrigin classification
     */
    private AttachedClassification getAssetOriginClassification(OpenMetadataElement asset)
    {
        List<AttachedClassification> existingClassifications = asset.getClassifications();

        if (existingClassifications != null)
        {
            for (AttachedClassification existingClassification : existingClassifications)
            {
                if (existingClassification != null)
                {
                    if (assetOriginClassification.equals(existingClassification.getClassificationName()))
                    {
                        return existingClassification;
                    }
                }
            }
        }

        return null;
    }


    /**
     * Extract the path name located in the properties of the supplied asset metadata element (either a FileFolder or DataFile).
     * It looks first in the linked connection endpoint.  If this is not available then the qualified name of the asset is used.
     *
     * @param asset metadata element
     * @return pathname
     */
    private List<ElementProperties> getOrigins(OpenMetadataElement  asset,
                                               List<String>         coveredEntityGUIDs) throws Exception
    {
        final String lineageMappingRelationshipName = "LineageMapping";
        final String dataFlowRelationshipName = "DataFlow";
        final String controlFlowRelationshipName = "ControlFlow";
        final String processCallRelationshipName = "ProcessCall";

        List<ElementProperties> results = new ArrayList<>();

        String[] relationshipArray = { lineageMappingRelationshipName,
                                       dataFlowRelationshipName,
                                       controlFlowRelationshipName,
                                       processCallRelationshipName };
        List<String> lineageRelationships = Arrays.asList(relationshipArray);

        /*
         * The lineage is explored by repeatedly retrieving the lineage from the metadata store.
         */
        OpenMetadataStore store = governanceContext.getOpenMetadataStore();

        /*
         * Retrieving from end 2 means it is working upstream on the lineage relationships.
         * Note this is only working with lineage relationships between Assets.  It would need
         * extending to work with lineage between ports and schema elements.
         */
        List<RelatedMetadataElement> lineageLinks = store.getRelatedMetadataElements(asset.getElementGUID(),
                                                                                     2,
                                                                                     null,
                                                                                     0,
                                                                                     0);

        if ((lineageLinks != null) && (! lineageLinks.isEmpty()))
        {
            /*
             * Explore each branch in the lineage map.
             */
            for (RelatedMetadataElement lineageLink : lineageLinks)
            {
                if (lineageLink != null)
                {
                    String relationshipName = lineageLink.getType().getTypeName();

                    if (lineageRelationships.contains(relationshipName))
                    {
                        OpenMetadataElement nextAsset = lineageLink.getElement();

                        /*
                         * Some lineage graphs are circular so the covered entity guids prevents the same element from being processed twice.
                         */
                        if (! coveredEntityGUIDs.contains(nextAsset.getElementGUID()))
                        {
                            coveredEntityGUIDs.add(nextAsset.getElementGUID());

                            /*
                             * If we find an origin classification on this asset we stop traversing the lineage graph.
                             */
                            AttachedClassification existingAssetOriginClassification = this.getAssetOriginClassification(nextAsset);

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
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }

        return results;
    }
}
