/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * AnchorsZoneResynchronizerGovernanceActionConnector corrects the governance zones held in Anchors classifications.
 * An Anchors classification records the zones that govern its element - the element's own if it has a
 * ZoneMembership classification, otherwise its anchor's - so that searches can leave out the elements in zones
 * the caller can not read.  The metadata access server keeps them up to date as zones change, but they can be out
 * of date in a repository populated before it did, and for elements loaded from an open metadata archive.
 * Each correction is sent to the repository named in the classification header, so reference copies of elements
 * from other members of the cohort are corrected through the member that owns them.
 * <br><br>
 * With action targets, each target is checked, along with the members of any target that is an anchor.  Without
 * them, every element in the repository is checked.  The service completes with anchors-zones-corrected if it
 * corrected any classification, and anchors-zones-up-to-date if there was nothing to correct.  If any element
 * could not be checked or corrected, the other targets are still checked and the service completes with
 * anchors-zones-resync-failed.  Running it until it
 * reports anchors-zones-up-to-date shows that searches filtered by governance zone (on by default, controlled by
 * the pushDownZoneFilter property of the Open Metadata Access Security Connector) return every element each user
 * may read.
 */
public class AnchorsZoneResynchronizerGovernanceActionConnector extends GeneralGovernanceActionService
{
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

        List<String> outputGuards = new ArrayList<>();

        try
        {
            OpenMetadataStore     store   = governanceContext.getOpenMetadataStore();
            MetadataSourceOptions options = new MetadataSourceOptions(store.getMetadataSourceOptions());

            /*
             * Elements with a Memento classification are included, and duplicates are processed one by one,
             * since every Anchors classification needs to be correct.
             */
            options.setForLineage(true);
            options.setForDuplicateProcessing(true);

            long                      corrected      = 0;
            AuditLogMessageDefinition failureMessage = null;

            if ((governanceContext.getActionTargetElements() == null) || (governanceContext.getActionTargetElements().isEmpty()))
            {
                final String scope = "every element in the repository";

                logRecord(methodName,
                          GovernanceActionConnectorsAuditCode.CHECKING_ANCHORS_ZONES.getMessageDefinition(governanceServiceName, scope));

                try
                {
                    corrected = store.resynchronizeAnchorsZoneMembership(null, options);
                }
                catch (PropertyServerException error)
                {
                    failureMessage = this.logIncompleteCheck(scope, error, methodName);
                }
            }
            else
            {
                for (ActionTargetElement actionTarget : governanceContext.getActionTargetElements())
                {
                    if ((actionTarget != null) && (actionTarget.getTargetElement() != null))
                    {
                        String scope = "element " + actionTarget.getTargetElement().getElementGUID();

                        logRecord(methodName,
                                  GovernanceActionConnectorsAuditCode.CHECKING_ANCHORS_ZONES.getMessageDefinition(governanceServiceName, scope));

                        /*
                         * A failure on one target does not stop the others being checked.
                         */
                        try
                        {
                            corrected += store.resynchronizeAnchorsZoneMembership(actionTarget.getTargetElement().getElementGUID(), options);
                        }
                        catch (InvalidParameterException | PropertyServerException error)
                        {
                            failureMessage = this.logIncompleteCheck(scope, error, methodName);
                        }
                    }
                }
            }

            AuditLogMessageDefinition messageDefinition = GovernanceActionConnectorsAuditCode.ANCHORS_ZONES_CORRECTED.getMessageDefinition(governanceServiceName,
                                                                                                                                          Long.toString(corrected));
            logRecord(methodName, messageDefinition);

            if (failureMessage != null)
            {
                outputGuards.add(AnchorsZoneResynchronizerGuard.RESYNC_FAILED.getName());

                governanceContext.recordCompletionStatus(AnchorsZoneResynchronizerGuard.RESYNC_FAILED.getCompletionStatus(),
                                                         outputGuards,
                                                         null,
                                                         null,
                                                         failureMessage);
                return;
            }

            AnchorsZoneResynchronizerGuard guard = (corrected > 0) ? AnchorsZoneResynchronizerGuard.ZONES_CORRECTED
                                                                   : AnchorsZoneResynchronizerGuard.ZONES_UP_TO_DATE;

            outputGuards.add(guard.getName());

            governanceContext.recordCompletionStatus(guard.getCompletionStatus(), outputGuards, null, null, messageDefinition);
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


    /**
     * Log that some of the Anchors classifications in scope could not be checked or corrected.
     *
     * @param scope description of the elements being checked
     * @param error exception returned by the metadata access server
     * @param methodName calling method
     * @return the message that was logged
     */
    private AuditLogMessageDefinition logIncompleteCheck(String                  scope,
                                                         OMFCheckedExceptionBase error,
                                                         String                  methodName)
    {
        AuditLogMessageDefinition messageDefinition = GovernanceActionConnectorsAuditCode.ANCHORS_ZONES_NOT_CORRECTED.getMessageDefinition(governanceServiceName,
                                                                                                                                              scope,
                                                                                                                                              error.getReportedErrorMessage());
        logExceptionRecord(methodName, messageDefinition, error);

        return messageDefinition;
    }


    /**
     * Record that the service failed, so that a governance action process can follow the failure guard.
     *
     * @param error the exception that stopped the service
     */
    private void recordFailure(Exception error)
    {
        try
        {
            List<String> outputGuards = new ArrayList<>();

            outputGuards.add(AnchorsZoneResynchronizerGuard.RESYNC_FAILED.getName());

            governanceContext.recordCompletionStatus(AnchorsZoneResynchronizerGuard.RESYNC_FAILED.getCompletionStatus(),
                                                     outputGuards,
                                                     null,
                                                     null,
                                                     error.getMessage());
        }
        catch (Exception recordError)
        {
            /*
             * The original error is reported by the caller.
             */
        }
    }
}
