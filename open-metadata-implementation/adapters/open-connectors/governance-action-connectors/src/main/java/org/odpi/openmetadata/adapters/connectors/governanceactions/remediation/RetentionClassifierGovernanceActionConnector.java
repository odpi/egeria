/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.NumericPropertyParser;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.StatusIdentifier;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.RetentionBasis;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * RetentionClassifierGovernanceActionConnector sets a Retention classification on the assets supplied as newAsset
 * action targets, recording the steward supplied as the steward action target, the retention basis and status, and
 * the dates to archive and delete the asset's data.  The values come from the configuration properties of the
 * service's connection, and a request parameter overrides the configuration property of the same name.
 * If at least one asset is classified, the output guard is classification-assigned.
 * If no steward is supplied, the output guard is missing-steward and no asset is changed.
 * If there are no newAsset action targets that are assets, the output guard is no-targets-detected.
 * If a value can not be used, or an asset can not be classified, the output guard is classification-failed.
 */
public class RetentionClassifierGovernanceActionConnector extends GeneralGovernanceActionService
{
    /**
     * Confidence recorded in the Retention classification.
     */
    private static final int confidence = 100;

    /**
     * Default number of milliseconds before the data should be archived (five years).
     */
    private static final long defaultTimeToArchive = 31556952000L * 5;

    /**
     * Default number of milliseconds before the data should be deleted (fifteen years).
     */
    private static final long defaultTimeToDelete = 31556952000L * 15;


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

        List<String>              outputGuards = new ArrayList<>();
        CompletionStatus          completionStatus;
        AuditLogMessageDefinition messageDefinition = null;

        try
        {
            /*
             * The values are retrieved first so that a value that can not be used is reported before any
             * asset is changed.
             */
            int  statusIdentifier = this.getIntProperty(RetentionClassifierRequestParameter.STATUS_IDENTIFIER.getName(),
                                                        StatusIdentifier.VALIDATED.getOrdinal());
            int  basisIdentifier  = this.getIntProperty(RetentionClassifierRequestParameter.BASIS_IDENTIFIER.getName(),
                                                        RetentionBasis.REGULATED_LIFETIME.getOrdinal());
            long timeToArchive    = this.getLongProperty(RetentionClassifierRequestParameter.RETENTION_TIME_TO_ARCHIVE.getName(),
                                                         defaultTimeToArchive);
            long timeToDelete     = this.getLongProperty(RetentionClassifierRequestParameter.RETENTION_TIME_TO_DELETE.getName(),
                                                         defaultTimeToDelete);

            List<ActionTargetElement> actionTargets = governanceContext.getActionTargetElements();
            OpenMetadataElement       steward       = this.getSteward(actionTargets);
            List<OpenMetadataElement> assets        = this.getNewAssets(actionTargets);

            if (assets.isEmpty())
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_TARGETS.getMessageDefinition(governanceServiceName);

                completionStatus = RetentionClassifierGuard.NO_TARGETS.getCompletionStatus();
                outputGuards.add(RetentionClassifierGuard.NO_TARGETS.getName());
            }
            else if (steward == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_STEWARD.getMessageDefinition(governanceServiceName);

                completionStatus = RetentionClassifierGuard.MISSING_STEWARD.getCompletionStatus();
                outputGuards.add(RetentionClassifierGuard.MISSING_STEWARD.getName());
            }
            else
            {
                Date              now           = new Date();
                Date              dateToArchive = new Date(now.getTime() + timeToArchive);
                Date              dateToDelete  = new Date(now.getTime() + timeToDelete);
                ElementProperties properties    = this.getRetentionProperties(statusIdentifier,
                                                                              basisIdentifier,
                                                                              steward,
                                                                              dateToArchive,
                                                                              dateToDelete);

                for (OpenMetadataElement asset : assets)
                {
                    logRecord(methodName,
                              GovernanceActionConnectorsAuditCode.SETTING_RETENTION.getMessageDefinition(governanceServiceName,
                                                                                                         asset.getElementGUID(),
                                                                                                         dateToArchive.toString(),
                                                                                                         dateToDelete.toString()));

                    governanceContext.getOpenMetadataStore().classifyMetadataElementInStore(asset.getElementGUID(),
                                                                                            OpenMetadataType.RETENTION_CLASSIFICATION.typeName,
                                                                                            governanceContext.getOpenMetadataStore().getMetadataSourceOptions(),
                                                                                            new NewElementProperties(properties));
                }

                completionStatus = RetentionClassifierGuard.CLASSIFICATION_ASSIGNED.getCompletionStatus();
                outputGuards.add(RetentionClassifierGuard.CLASSIFICATION_ASSIGNED.getName());
            }

            if (messageDefinition != null)
            {
                logRecord(methodName, messageDefinition);
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards, null, null, messageDefinition);
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
     * Return the element supplied as the steward action target.
     *
     * @param actionTargets action targets passed to the service
     * @return steward element or null
     */
    private OpenMetadataElement getSteward(List<ActionTargetElement> actionTargets)
    {
        if (actionTargets != null)
        {
            for (ActionTargetElement actionTarget : actionTargets)
            {
                if ((actionTarget != null) &&
                        (actionTarget.getTargetElement() != null) &&
                        (ActionTarget.STEWARD.getName().equals(actionTarget.getActionTargetName())))
                {
                    return actionTarget.getTargetElement();
                }
            }
        }

        return null;
    }


    /**
     * Return the assets supplied as newAsset action targets.  These are the elements to classify.
     *
     * @param actionTargets action targets passed to the service
     * @return list of assets (may be empty)
     */
    private List<OpenMetadataElement> getNewAssets(List<ActionTargetElement> actionTargets)
    {
        List<OpenMetadataElement> assets = new ArrayList<>();

        if (actionTargets != null)
        {
            for (ActionTargetElement actionTarget : actionTargets)
            {
                if ((actionTarget != null) &&
                        (actionTarget.getTargetElement() != null) &&
                        (ActionTarget.NEW_ASSET.getName().equals(actionTarget.getActionTargetName())) &&
                        (propertyHelper.isTypeOf(actionTarget.getTargetElement(), OpenMetadataType.ASSET.typeName)))
                {
                    assets.add(actionTarget.getTargetElement());
                }
            }
        }

        return assets;
    }


    /**
     * Return the value of a property as an int.  The request parameter is used if it is set, otherwise the
     * configuration property.  A blank value is treated as not set.
     *
     * @param propertyName name of the property
     * @param defaultValue value to use if the property is not set
     * @return int value
     * @throws org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFRuntimeException the value is not a whole number
     */
    private int getIntProperty(String propertyName,
                               int    defaultValue)
    {
        String requestParameter = this.getRequestParameter(propertyName);

        if (requestParameter != null)
        {
            return NumericPropertyParser.getIntRequestParameter(propertyName, requestParameter);
        }

        Object configurationProperty = this.getConfigurationProperty(propertyName);

        if (configurationProperty != null)
        {
            return NumericPropertyParser.getIntConfigurationProperty(propertyName, configurationProperty);
        }

        return defaultValue;
    }


    /**
     * Return the value of a property as a long.  The request parameter is used if it is set, otherwise the
     * configuration property.  A blank value is treated as not set.
     *
     * @param propertyName name of the property
     * @param defaultValue value to use if the property is not set
     * @return long value
     * @throws org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFRuntimeException the value is not a whole number
     */
    private long getLongProperty(String propertyName,
                                 long   defaultValue)
    {
        String requestParameter = this.getRequestParameter(propertyName);

        if (requestParameter != null)
        {
            return NumericPropertyParser.getLongRequestParameter(propertyName, requestParameter);
        }

        Object configurationProperty = this.getConfigurationProperty(propertyName);

        if (configurationProperty != null)
        {
            return NumericPropertyParser.getLongConfigurationProperty(propertyName, configurationProperty);
        }

        return defaultValue;
    }


    /**
     * Return the value of a request parameter.  A blank value is treated as not set.
     *
     * @param propertyName name of the request parameter
     * @return value or null
     */
    private String getRequestParameter(String propertyName)
    {
        Map<String, String> requestParameters = governanceContext.getRequestParameters();

        if ((requestParameters != null) &&
                (requestParameters.get(propertyName) != null) &&
                (! requestParameters.get(propertyName).isBlank()))
        {
            return requestParameters.get(propertyName);
        }

        return null;
    }


    /**
     * Return the value of a configuration property.  A blank value is treated as not set.
     *
     * @param propertyName name of the configuration property
     * @return value or null
     */
    private Object getConfigurationProperty(String propertyName)
    {
        Map<String, Object> configurationProperties = connectionBean.getConfigurationProperties();

        if ((configurationProperties != null) &&
                (configurationProperties.get(propertyName) != null) &&
                (! configurationProperties.get(propertyName).toString().isBlank()))
        {
            return configurationProperties.get(propertyName);
        }

        return null;
    }


    /**
     * Return the properties for the Retention classification.
     *
     * @param statusIdentifier status of the classification
     * @param basisIdentifier basis on which the retention dates are set
     * @param steward element describing the steward
     * @param dateToArchive date when the data should be archived
     * @param dateToDelete date when the data should be deleted
     * @return element properties for the Retention classification
     */
    private ElementProperties getRetentionProperties(int                 statusIdentifier,
                                                     int                 basisIdentifier,
                                                     OpenMetadataElement steward,
                                                     Date                dateToArchive,
                                                     Date                dateToDelete)
    {
        ElementProperties elementProperties = propertyHelper.addIntProperty(null,
                                                                            OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                                            statusIdentifier);
        elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                          OpenMetadataProperty.CONFIDENCE.name,
                                                          confidence);
        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.STEWARD.name,
                                                             steward.getElementGUID());

        if (steward.getType() != null)
        {
            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                 steward.getType().getTypeName());
        }

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                             OpenMetadataProperty.GUID.name);
        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.SOURCE.name,
                                                             governanceServiceName);
        elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                          OpenMetadataProperty.RETENTION_BASIS.name,
                                                          basisIdentifier);
        elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                           OpenMetadataProperty.ARCHIVE_AFTER.name,
                                                           dateToArchive);
        elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                           OpenMetadataProperty.DELETE_AFTER.name,
                                                           dateToDelete);

        return elementProperties;
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

            outputGuards.add(RetentionClassifierGuard.CLASSIFICATION_FAILED.getName());

            governanceContext.recordCompletionStatus(RetentionClassifierGuard.CLASSIFICATION_FAILED.getCompletionStatus(),
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
