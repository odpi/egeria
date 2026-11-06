/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

/**
 * IncidentReporterGovernanceActionConnector creates an incident report using the template identified by the
 * templateGUID from the request parameters or the configuration properties of the connector's connection.
 * The request parameters provide the values of the placeholders in the template.  Each run creates a new incident
 * report, so its qualified name is generated from the name of the service and the unique identifier of the engine action.
 * The service is linked to the report as its originator.  The action targets are linked to the report as its impacted
 * resources.  This relationship can only be attached to Referenceables, so any other action target is skipped.
 * If the incident report is created, the output guard is incident-reported and the report is added as the newAsset action target.
 * If no template is supplied, the output guard is missing-template.
 */
public class IncidentReporterGovernanceActionConnector extends GeneralGovernanceActionService
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

        try
        {
            String                    templateGUID        = getProperty(IncidentReporterRequestParameter.TEMPLATE_GUID.getName(), null);
            List<NewActionTarget>     outputActionTargets = null;
            IncidentReporterGuard     guard;
            AuditLogMessageDefinition messageDefinition;

            if ((templateGUID == null) || (templateGUID.isBlank()))
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_TEMPLATE_GUID.getMessageDefinition(governanceServiceName);
                guard = IncidentReporterGuard.MISSING_TEMPLATE;
            }
            else
            {
                String qualifiedName      = governanceServiceName + "::" + governanceContext.getEngineActionGUID();
                String incidentReportGUID = this.createIncidentReport(templateGUID.trim(), qualifiedName);

                messageDefinition = GovernanceActionConnectorsAuditCode.NEW_ASSET_CREATED.getMessageDefinition(governanceServiceName,
                                                                                                               OpenMetadataType.INCIDENT_REPORT.typeName,
                                                                                                               qualifiedName,
                                                                                                               incidentReportGUID);

                NewActionTarget newActionTarget = new NewActionTarget();

                newActionTarget.setActionTargetGUID(incidentReportGUID);
                newActionTarget.setActionTargetName(ActionTarget.NEW_ASSET.name);

                outputActionTargets = new ArrayList<>();
                outputActionTargets.add(newActionTarget);

                guard = IncidentReporterGuard.INCIDENT_REPORTED;
            }

            logRecord(methodName, messageDefinition);

            List<String> outputGuards = new ArrayList<>();

            outputGuards.add(guard.getName());

            governanceContext.recordCompletionStatus(guard.getCompletionStatus(), outputGuards, null, outputActionTargets, messageDefinition);
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
     * Create the incident report from the template and link it to the service and the action targets.
     * The service is the originator of the report, as it is when an incident report is created without a template.
     *
     * @param templateGUID unique identifier of the template
     * @param qualifiedName unique name for the incident report
     * @return unique identifier of the incident report
     * @throws InvalidParameterException the template is invalid
     * @throws UserNotAuthorizedException the service is not authorized to create the incident report
     * @throws PropertyServerException a problem with the metadata store
     */
    private String createIncidentReport(String templateGUID,
                                        String qualifiedName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        OpenMetadataStore openMetadataStore = governanceContext.getOpenMetadataStore();

        String incidentReportGUID = openMetadataStore.createMetadataElementFromTemplate(OpenMetadataType.INCIDENT_REPORT.typeName,
                                                                                        null,
                                                                                        true,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        templateGUID,
                                                                                        propertyHelper.addStringProperty(null,
                                                                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                         qualifiedName),
                                                                                        null,
                                                                                        governanceContext.getRequestParameters(),
                                                                                        governanceContext.getConnectorGUID(),
                                                                                        OpenMetadataType.REPORT_ORIGINATOR_RELATIONSHIP.typeName,
                                                                                        null,
                                                                                        true);

        if (governanceContext.getActionTargetElements() != null)
        {
            for (ActionTargetElement actionTarget : governanceContext.getActionTargetElements())
            {
                if ((actionTarget != null) &&
                        (propertyHelper.isTypeOf(actionTarget.getTargetElement(), OpenMetadataType.REFERENCEABLE.typeName)))
                {
                    openMetadataStore.createRelatedElementsInStore(OpenMetadataType.IMPACTED_RESOURCE_RELATIONSHIP.typeName,
                                                                   actionTarget.getActionTargetGUID(),
                                                                   incidentReportGUID,
                                                                   null,
                                                                   null,
                                                                   null);
                }
            }
        }

        return incidentReportGUID;
    }
}
