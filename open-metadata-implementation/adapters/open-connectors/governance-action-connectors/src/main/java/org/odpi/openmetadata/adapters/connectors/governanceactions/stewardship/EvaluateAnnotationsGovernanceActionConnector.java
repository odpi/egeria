/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.remediation.RetentionClassifierGuard;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ToDoType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.SurveyActionTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * EvaluateAnnotationsGovernanceActionConnector is currently a placeholder for a governance action service
 * that will look through the annotations from a survey report and set up guards to drive actions that
 * process the different types.
 */
public class EvaluateAnnotationsGovernanceActionConnector extends GeneralGovernanceActionService
{
    /**
     * Default constructor
     */
    public EvaluateAnnotationsGovernanceActionConnector()
    {
    }


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

        try
        {
            List<String>              outputGuards      = new ArrayList<>();
            CompletionStatus          completionStatus  = null;
            AuditLogMessageDefinition messageDefinition = null;
            ActionTargetElement       surveyReport      = null;
            String                    stewardGUID       = null;

            if (governanceContext.getActionTargetElements() != null)
            {
                for (ActionTargetElement actionTargetElement : governanceContext.getActionTargetElements())
                {
                    if (actionTargetElement != null)
                    {
                        if (SurveyActionTarget.SURVEY_REPORT.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            surveyReport = actionTargetElement;
                        }
                        else if (ActionTarget.STEWARD.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            stewardGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                    }
                }
            }

            if (surveyReport == null)
            {
                outputGuards.add(EvaluateAnnotationsGuard.NO_SURVEY_REPORT.getName());
                completionStatus = EvaluateAnnotationsGuard.NO_SURVEY_REPORT.getCompletionStatus();
            }
            else if (stewardGUID == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_STEWARD.getMessageDefinition(governanceServiceName);
                outputGuards.add(RetentionClassifierGuard.MISSING_STEWARD.getName());
                completionStatus = RetentionClassifierGuard.MISSING_STEWARD.getCompletionStatus();
            }
            else
            {
                /*
                 * Search the survey report looking for request for action annotations
                 */
                int startFrom = 0;
                List<RelatedMetadataElement> reportedAnnotations = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(surveyReport.getActionTargetGUID(),
                                                                                                                                       1,
                                                                                                                                       OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                                                                       startFrom,
                                                                                                                                       governanceContext.getMaxPageSize());
                while (reportedAnnotations != null)
                {
                    for (RelatedMetadataElement reportedAnnotation : reportedAnnotations)
                    {
                        if (reportedAnnotation != null)
                        {
                            if (reportedAnnotation.getElement().getType().getTypeName().equals(OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION.typeName))
                            {
                                governanceContext.openToDo(governanceServiceName + ":" + connectorInstanceId,
                                                           ToDoType.REQUEST_FOR_ACTION.getDescription(),
                                                           "Follow the link for the request for action to discover the issue and suggested remedy.",
                                                           ToDoType.REQUEST_FOR_ACTION.getName(),
                                                           0,
                                                           null,
                                                           stewardGUID,
                                                           reportedAnnotation.getElement().getElementGUID(),
                                                           ToDoType.REQUEST_FOR_ACTION.getActionTargetName());
                            }
                        }
                    }

                    startFrom = startFrom + governanceContext.getMaxPageSize();
                    reportedAnnotations = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(surveyReport.getActionTargetGUID(),
                                                                                                              1,
                                                                                                              OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                                              startFrom,
                                                                                                              governanceContext.getMaxPageSize());
                }
            }

            if (messageDefinition != null)
            {
                auditLog.logMessage(methodName, messageDefinition);
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards);
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
