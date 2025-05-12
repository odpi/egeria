/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.verification;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.governanceaction.VerificationGovernanceActionService;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;

import java.util.ArrayList;
import java.util.List;


/**
 * VerifyAssetGovernanceActionConnector evaluates an asset to be sure it has zones, an origin and
 * an owner.
 */
public class VerifyAssetGovernanceActionConnector extends VerificationGovernanceActionService
{
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

        List<String>     outputGuards     = new ArrayList<>();
        CompletionStatus completionStatus;

        try
        {
            if (governanceContext.getActionTargetElements() == null)
            {
                completionStatus = VerifyAssetGuard.NO_TARGETS_DETECTED.getCompletionStatus();
                outputGuards.add(VerifyAssetGuard.NO_TARGETS_DETECTED.getName());
            }
            else if (governanceContext.getActionTargetElements().size() == 1)
            {
                ActionTargetElement actionTarget = governanceContext.getActionTargetElements().get(0);

                OpenMetadataElement targetElement = actionTarget.getTargetElement();

                if (propertyHelper.isTypeOf(targetElement, OpenMetadataType.ASSET.typeName))
                {
                    if (targetElement.getClassifications() == null)
                    {
                        outputGuards.add(VerifyAssetGuard.NO_ZONES.getName());
                        outputGuards.add(VerifyAssetGuard.NO_OWNER.getName());
                        outputGuards.add(VerifyAssetGuard.NO_ORIGIN.getName());
                    }
                    else
                    {
                        boolean noZones = true;
                        boolean noOwner = true;
                        boolean noOrigin = true;

                        for (AttachedClassification classification : targetElement.getClassifications())
                        {
                            if (classification != null)
                            {
                                if (propertyHelper.isTypeOf(classification, OpenMetadataType.ASSET_ZONE_MEMBERSHIP_CLASSIFICATION.typeName))
                                {
                                    noZones = false;
                                }
                                else if (propertyHelper.isTypeOf(classification, OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName))
                                {
                                    noOwner = false;
                                }
                                else if (propertyHelper.isTypeOf(classification, OpenMetadataType.ASSET_ORIGIN_CLASSIFICATION.typeName))
                                {
                                    noOrigin = false;
                                }
                            }
                        }

                        if (noZones)
                        {
                            outputGuards.add(VerifyAssetGuard.NO_ZONES.getName());
                        }
                        else
                        {
                            outputGuards.add(VerifyAssetGuard.ZONES_ASSIGNED.getName());
                        }

                        if (noOwner)
                        {
                            outputGuards.add(VerifyAssetGuard.NO_OWNER.getName());
                        }
                        else
                        {
                            outputGuards.add(VerifyAssetGuard.OWNER_ASSIGNED.getName());
                        }

                        if (noOrigin)
                        {
                            outputGuards.add(VerifyAssetGuard.NO_ORIGIN.getName());
                        }
                        else
                        {
                            outputGuards.add(VerifyAssetGuard.ORIGIN_ASSIGNED.getName());
                        }
                    }

                    completionStatus = CompletionStatus.ACTIONED;
                }
                else
                {
                    completionStatus = VerifyAssetGuard.TARGET_NOT_ASSET.getCompletionStatus();
                    outputGuards.add(VerifyAssetGuard.TARGET_NOT_ASSET.getName());
                }
            }
            else
            {
                /*
                 * Multiple action targets to supply.  This governance action does not support multiple action targets because the
                 * result of the verification could be different for each action target and so it would be difficult to automate the response.
                 */
                completionStatus = VerifyAssetGuard.MULTIPLE_TARGETS_DETECTED.getCompletionStatus();
                outputGuards.add(VerifyAssetGuard.MULTIPLE_TARGETS_DETECTED.getName());
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards, null, null);
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

}
