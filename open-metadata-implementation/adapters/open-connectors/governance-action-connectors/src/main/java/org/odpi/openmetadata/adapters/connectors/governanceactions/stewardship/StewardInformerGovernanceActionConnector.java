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
import org.odpi.openmetadata.frameworks.openmetadata.refdata.AssignmentType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

/**
 * StewardInformerGovernanceActionConnector creates a to do for a steward using the template identified by the
 * templateGUID from the request parameters or the configuration properties of the connector's connection.
 * The request parameters provide the values of the placeholders in the template.  Each run creates a new to do, so its
 * qualified name is generated from the name of the service and the unique identifier of the engine action.
 * The to do is assigned to the action target called steward and sponsored by the optional action target called sponsor.
 * All other action targets are attached to the to do to guide the steward.
 * If the to do is created, the output guard is steward-informed and the to do is added as the newAsset action target.
 * If no template is supplied, the output guard is missing-template.
 * If there is no steward, the output guard is missing-steward.
 */
public class StewardInformerGovernanceActionConnector extends GeneralGovernanceActionService
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
            String                templateGUID        = getProperty(StewardInformerRequestParameter.TEMPLATE_GUID.getName(), null);
            String                stewardGUID         = null;
            String                sponsorGUID         = null;
            List<NewActionTarget> actionTargets       = new ArrayList<>();
            List<NewActionTarget> outputActionTargets = null;

            if (governanceContext.getActionTargetElements() != null)
            {
                for (ActionTargetElement actionTarget : governanceContext.getActionTargetElements())
                {
                    if (actionTarget != null)
                    {
                        if (ActionTarget.STEWARD.getName().equals(actionTarget.getActionTargetName()))
                        {
                            stewardGUID = actionTarget.getActionTargetGUID();
                        }
                        else if (ActionTarget.SPONSOR.getName().equals(actionTarget.getActionTargetName()))
                        {
                            sponsorGUID = actionTarget.getActionTargetGUID();
                        }
                        else
                        {
                            actionTargets.add(actionTarget);
                        }
                    }
                }
            }

            StewardInformerGuard      guard;
            AuditLogMessageDefinition messageDefinition;

            if ((templateGUID == null) || (templateGUID.isBlank()))
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_TEMPLATE_GUID.getMessageDefinition(governanceServiceName);
                guard = StewardInformerGuard.MISSING_TEMPLATE;
            }
            else if (stewardGUID == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_STEWARD.getMessageDefinition(governanceServiceName);
                guard = StewardInformerGuard.MISSING_STEWARD;
            }
            else
            {
                String qualifiedName = governanceServiceName + "::" + governanceContext.getEngineActionGUID();
                String toDoGUID      = this.createToDo(templateGUID.trim(), qualifiedName, stewardGUID, sponsorGUID, actionTargets);

                messageDefinition = GovernanceActionConnectorsAuditCode.NEW_ASSET_CREATED.getMessageDefinition(governanceServiceName,
                                                                                                               OpenMetadataType.TO_DO.typeName,
                                                                                                               qualifiedName,
                                                                                                               toDoGUID);

                NewActionTarget newActionTarget = new NewActionTarget();

                newActionTarget.setActionTargetGUID(toDoGUID);
                newActionTarget.setActionTargetName(ActionTarget.NEW_ASSET.name);

                outputActionTargets = new ArrayList<>();
                outputActionTargets.add(newActionTarget);

                guard = StewardInformerGuard.STEWARD_INFORMED;
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
     * Create the to do from the template and link it to the steward, sponsor, service and action targets.
     * These are the same relationships that are created when a to do is opened without a template.
     *
     * @param templateGUID unique identifier of the template
     * @param qualifiedName unique name for the to do
     * @param stewardGUID unique identifier of the steward
     * @param sponsorGUID unique identifier of the sponsor, or null
     * @param actionTargets elements to attach to the to do
     * @return unique identifier of the to do
     * @throws InvalidParameterException the template is invalid
     * @throws UserNotAuthorizedException the service is not authorized to create the to do
     * @throws PropertyServerException a problem with the metadata store
     */
    private String createToDo(String                templateGUID,
                              String                qualifiedName,
                              String                stewardGUID,
                              String                sponsorGUID,
                              List<NewActionTarget> actionTargets) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        OpenMetadataStore openMetadataStore = governanceContext.getOpenMetadataStore();

        String toDoGUID = openMetadataStore.createMetadataElementFromTemplate(OpenMetadataType.TO_DO.typeName,
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
                                                                              stewardGUID,
                                                                              OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                              null,
                                                                              true);

        for (NewActionTarget actionTarget : actionTargets)
        {
            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                           toDoGUID,
                                                           actionTarget.getActionTargetGUID(),
                                                           null,
                                                           null,
                                                           propertyHelper.addStringProperty(null,
                                                                                            OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                                                            actionTarget.getActionTargetName()));
        }

        if (sponsorGUID != null)
        {
            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                           sponsorGUID,
                                                           toDoGUID,
                                                           null,
                                                           null,
                                                           propertyHelper.addStringProperty(null,
                                                                                            OpenMetadataProperty.ASSIGNMENT_TYPE.name,
                                                                                            AssignmentType.SPONSOR.getDisplayName()));
        }

        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName,
                                                       governanceContext.getConnectorGUID(),
                                                       toDoGUID,
                                                       null,
                                                       null,
                                                       null);

        return toDoGUID;
    }
}
