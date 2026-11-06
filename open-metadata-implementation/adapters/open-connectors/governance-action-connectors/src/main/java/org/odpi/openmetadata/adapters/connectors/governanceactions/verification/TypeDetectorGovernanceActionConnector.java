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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TypeDetectorGovernanceActionConnector checks the type of the elements supplied as action targets so that it can be
 * used as a step in a governance action process that filters out action targets of the wrong type.
 * The acceptableTypeNames come from the configuration properties of the connector's connection and/or the request
 * parameters.  The value in the request parameters overrides the value in the configuration properties.
 * An element is acceptable if its type, or one of its super types, is one of the acceptable types.
 * The action targets are passed on to the next step of a governance action process, whichever guard is
 * produced, so the output guard is type-detected only if all of the action targets are acceptable.
 * If at least one action target is not acceptable, the output guard is type-not-detected.
 * If no acceptable type names are supplied, the output guard is missing-acceptable-type-names.
 * If no action targets are passed the output guard is no-targets-detected.
 */
public class TypeDetectorGovernanceActionConnector extends GeneralGovernanceActionService
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

        List<String>              acceptableTypeNames = this.getAcceptableTypeNames();
        TypeDetectorGuard         guard;
        AuditLogMessageDefinition messageDefinition   = null;

        try
        {
            if ((governanceContext.getActionTargetElements() == null) || (governanceContext.getActionTargetElements().isEmpty()))
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_TARGETS.getMessageDefinition(governanceServiceName);
                guard = TypeDetectorGuard.NO_TARGETS;
            }
            else if (acceptableTypeNames == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_ACCEPTABLE_TYPE_NAMES.getMessageDefinition(governanceServiceName);
                guard = TypeDetectorGuard.MISSING_ACCEPTABLE_TYPE_NAMES;
            }
            else
            {
                List<String> unacceptableElements = this.getUnacceptableElements(acceptableTypeNames);

                if (unacceptableElements.isEmpty())
                {
                    guard = TypeDetectorGuard.TYPE_DETECTED;
                }
                else
                {
                    messageDefinition = GovernanceActionConnectorsAuditCode.UNACCEPTABLE_TYPE.getMessageDefinition(governanceServiceName,
                                                                                                                   unacceptableElements.toString(),
                                                                                                                   acceptableTypeNames.toString());
                    guard = TypeDetectorGuard.TYPE_NOT_DETECTED;
                }
            }

            if (messageDefinition != null)
            {
                logRecord(methodName, messageDefinition);
            }

            List<String> outputGuards = new ArrayList<>();

            outputGuards.add(guard.getName());

            governanceContext.recordCompletionStatus(guard.getCompletionStatus(), outputGuards, null, null, messageDefinition);
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
     * Return the names of the acceptable types from the request parameters or, if they are not set there, the
     * configuration properties.  The value may be a comma separated list or, in the configuration properties, a list of values.
     *
     * @return list of type names or null
     */
    private List<String> getAcceptableTypeNames()
    {
        final String propertyName = TypeDetectorRequestParameter.ACCEPTABLE_TYPE_NAMES.getName();

        Map<String, String> requestParameters       = governanceContext.getRequestParameters();
        Map<String, Object> configurationProperties = connectionBean.getConfigurationProperties();

        List<String> acceptableTypeNames = null;

        if ((requestParameters != null) && (requestParameters.get(propertyName) != null))
        {
            acceptableTypeNames = getArrayValue(requestParameters.get(propertyName));
        }
        else if (configurationProperties != null)
        {
            acceptableTypeNames = getArrayValue(configurationProperties.get(propertyName));
        }

        if ((acceptableTypeNames == null) || (acceptableTypeNames.isEmpty()))
        {
            return null;
        }

        return acceptableTypeNames;
    }


    /**
     * Return a description of each action target that is not of an acceptable type.
     *
     * @param acceptableTypeNames names of the acceptable types
     * @return list of element descriptions - empty if all action targets are acceptable
     */
    private List<String> getUnacceptableElements(List<String> acceptableTypeNames)
    {
        List<String> unacceptableElements = new ArrayList<>();

        for (ActionTargetElement actionTarget : governanceContext.getActionTargetElements())
        {
            if (actionTarget != null)
            {
                OpenMetadataElement element = actionTarget.getTargetElement();

                if ((element == null) || (! propertyHelper.isTypeOf(element, acceptableTypeNames)))
                {
                    unacceptableElements.add(actionTarget.getActionTargetGUID() + " (" + this.getTypeName(element) + ")");
                }
            }
        }

        return unacceptableElements;
    }


    /**
     * Return the name of the type of an element.
     *
     * @param element element to query
     * @return type name or null
     */
    private String getTypeName(OpenMetadataElement element)
    {
        if ((element == null) || (element.getType() == null))
        {
            return null;
        }

        return element.getType().getTypeName();
    }
}
