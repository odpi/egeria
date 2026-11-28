/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.verification;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TypeCasterGovernanceActionConnector outputs the type of its single action target as its guard so that it can be
 * used as a step in a governance action process that branches down different paths depending on the type of the
 * element it is working on.
 * The guard is the name of the action target's type unless the candidateTypeNames property is set.  This comes from
 * the configuration properties of the connector's connection and/or the request parameters.  The value in the
 * request parameters overrides the value in the configuration properties.  When it is set, the guard is the first of
 * the candidate type names that the action target's type, or one of its super types, matches.  This means a process
 * can branch on a super type, such as Asset, and is not affected by the introduction of new subtypes.
 * If the action target does not match any of the candidate types, the output guard is type-not-listed.
 * If no action targets are passed the output guard is no-targets-detected, and if more than one is passed, the output
 * guard is multiple-targets-detected.  Action targets whose element could not be retrieved are ignored.
 */
public class TypeCasterGovernanceActionConnector extends GeneralGovernanceActionService
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

        List<String> candidateTypeNames = this.getCandidateTypeNames();

        try
        {
            List<ActionTargetElement> actionTargets = this.getTypedActionTargets();

            String                    guardName;
            CompletionStatus          completionStatus;
            AuditLogMessageDefinition messageDefinition;

            if (actionTargets.isEmpty())
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_TARGETS.getMessageDefinition(governanceServiceName);
                guardName         = TypeCasterGuard.NO_TARGETS.getName();
                completionStatus  = TypeCasterGuard.NO_TARGETS.getCompletionStatus();
            }
            else if (actionTargets.size() > 1)
            {
                List<String> actionTargetGUIDs = new ArrayList<>();

                for (ActionTargetElement actionTarget : actionTargets)
                {
                    actionTargetGUIDs.add(actionTarget.getActionTargetGUID());
                }

                messageDefinition = GovernanceActionConnectorsAuditCode.MULTIPLE_TARGETS.getMessageDefinition(governanceServiceName,
                                                                                                              Integer.toString(actionTargets.size()),
                                                                                                              actionTargetGUIDs.toString());
                guardName         = TypeCasterGuard.MULTIPLE_TARGETS.getName();
                completionStatus  = TypeCasterGuard.MULTIPLE_TARGETS.getCompletionStatus();
            }
            else
            {
                ActionTargetElement actionTarget = actionTargets.get(0);
                OpenMetadataElement element      = actionTarget.getTargetElement();
                String              typeName     = element.getType().getTypeName();

                guardName = this.getMatchingTypeName(element, typeName, candidateTypeNames);

                if (guardName == null)
                {
                    messageDefinition = GovernanceActionConnectorsAuditCode.TYPE_NOT_LISTED.getMessageDefinition(governanceServiceName,
                                                                                                                 actionTarget.getActionTargetGUID(),
                                                                                                                 typeName,
                                                                                                                 candidateTypeNames.toString());
                    guardName         = TypeCasterGuard.TYPE_NOT_LISTED.getName();
                    completionStatus  = TypeCasterGuard.TYPE_NOT_LISTED.getCompletionStatus();
                }
                else
                {
                    messageDefinition = GovernanceActionConnectorsAuditCode.TYPE_CAST.getMessageDefinition(governanceServiceName,
                                                                                                           guardName,
                                                                                                           actionTarget.getActionTargetGUID(),
                                                                                                           typeName);
                    completionStatus  = CompletionStatus.ACTIONED;
                }
            }

            logRecord(methodName, messageDefinition);

            List<String> outputGuards = new ArrayList<>();

            outputGuards.add(guardName);

            governanceContext.recordCompletionStatus(completionStatus, outputGuards, null, null, messageDefinition);
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
     * Return the names of the candidate types from the request parameters or, if they are not set there, the
     * configuration properties.  The value may be a comma separated list or, in the configuration properties, a list of values.
     *
     * @return list of type names or null
     */
    private List<String> getCandidateTypeNames()
    {
        final String propertyName = TypeCasterRequestParameter.CANDIDATE_TYPE_NAMES.getName();

        Map<String, String> requestParameters       = governanceContext.getRequestParameters();
        Map<String, Object> configurationProperties = connectionBean.getConfigurationProperties();

        List<String> candidateTypeNames = null;

        if ((requestParameters != null) && (requestParameters.get(propertyName) != null))
        {
            candidateTypeNames = getArrayValue(requestParameters.get(propertyName));
        }
        else if (configurationProperties != null)
        {
            candidateTypeNames = getArrayValue(configurationProperties.get(propertyName));
        }

        if ((candidateTypeNames == null) || (candidateTypeNames.isEmpty()))
        {
            return null;
        }

        return candidateTypeNames;
    }


    /**
     * Return the action targets whose element, along with its type, has been retrieved.
     *
     * @return list of action targets - empty if there are none
     */
    private List<ActionTargetElement> getTypedActionTargets()
    {
        List<ActionTargetElement> typedActionTargets = new ArrayList<>();

        if (governanceContext.getActionTargetElements() != null)
        {
            for (ActionTargetElement actionTarget : governanceContext.getActionTargetElements())
            {
                if ((actionTarget != null) &&
                    (actionTarget.getTargetElement() != null) &&
                    (actionTarget.getTargetElement().getType() != null) &&
                    (actionTarget.getTargetElement().getType().getTypeName() != null))
                {
                    typedActionTargets.add(actionTarget);
                }
            }
        }

        return typedActionTargets;
    }


    /**
     * Return the type name to use as the guard for an element.  This is the element's own type name when there are
     * no candidate types, otherwise it is the first candidate type that the element's type, or one of its super
     * types, matches.
     *
     * @param element element to describe
     * @param typeName name of the element's type
     * @param candidateTypeNames ordered list of candidate type names, or null
     * @return type name or null if the element does not match any of the candidate types
     */
    private String getMatchingTypeName(OpenMetadataElement element,
                                       String              typeName,
                                       List<String>        candidateTypeNames)
    {
        if (candidateTypeNames == null)
        {
            return typeName;
        }

        for (String candidateTypeName : candidateTypeNames)
        {
            if (propertyHelper.isTypeOf(element, candidateTypeName))
            {
                return candidateTypeName;
            }
        }

        return null;
    }
}
