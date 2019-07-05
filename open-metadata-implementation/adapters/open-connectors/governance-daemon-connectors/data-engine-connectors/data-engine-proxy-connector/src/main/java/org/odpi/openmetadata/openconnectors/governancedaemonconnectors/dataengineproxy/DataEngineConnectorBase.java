/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineImpl;
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class DataEngineConnectorBase extends ConnectorBase implements DataEngineInterface {

    private static final Logger log = LoggerFactory.getLogger(DataEngineConnectorBase.class);

    private DataEngineImpl dataEngineClient;

    public DataEngineConnectorBase() { super(); }

    /**
     * Call made by the ConnectorProvider to initialize the Connector with the base services.
     *
     * @param connectorInstanceId   unique id for the connector instance useful for messages etc
     * @param connectionProperties   POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String               connectorInstanceId,
                           ConnectionProperties connectionProperties) {
        super.initialize(connectorInstanceId, connectionProperties);
        Map<String, Object> omasProperties = this.connectionBean.getSecuredProperties();
        if (omasProperties != null) {
            // Setup the Data Engine OMAS connection based on the secured client sent through from the connector's
            // configuration and initialization (from DataEngineProxyOperationalServices)
            dataEngineClient = (DataEngineImpl) omasProperties.getOrDefault("dataEngineOMASClient", null);
        }
    }

    /**
     * Register the provided SoftwareServiceCapbility as a Data Engine.
     *
     * @param dataEngine
     * @param userId
     * @return String - the GUID of the registered Data Engine
     */
    public String registerDataEngine(SoftwareServerCapability dataEngine, String userId) {
        final String methodName = "registerDataEngine";
        try {
            return dataEngineClient.createSoftwareServerCapability(userId, dataEngine);
        } catch (InvalidParameterException | PropertyServerException e) {
            DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.OMAS_CONNECTION_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
            throw new OCFRuntimeException(
                    errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction()
            );
        } catch (UserNotAuthorizedException e) {
            DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.USER_NOT_AUTHORIZED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
            throw new OCFRuntimeException(
                    errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction()
            );
        }
    }

    /**
     * Register the provided SoftwareServiceCapbility as a Data Engine.
     *
     * @param dataEngine
     * @return String - the GUID of the registered Data Engine
     */
    public String registerDataEngine(SoftwareServerCapability dataEngine) {
        return registerDataEngine(dataEngine, null);
    }

    /**
     * Send the provided process to the Data Engine OMAS.
     *
     * @param process
     * @param userId
     * @return String - the GUID of the process that was created
     */
    public String sendProcess(Process process, String userId) {
        final String methodName = "sendProcess";
        try {
            return dataEngineClient.createProcess(userId, process);
        } catch (InvalidParameterException | PropertyServerException e) {
            DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.OMAS_CONNECTION_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
            throw new OCFRuntimeException(
                    errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction()
            );
        } catch (UserNotAuthorizedException e) {
            DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.USER_NOT_AUTHORIZED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
            throw new OCFRuntimeException(
                    errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction()
            );
        }
    }

    /**
     * Send the provided process to the Data Engine OMAS.
     *
     * @param process
     * @return String - the GUID of the process that was created
     */
    public String sendProcess(Process process) {
        return sendProcess(process, null);
    }

    /**
     * Send the provided list of LineageMappings to the Data Engine OMAS.
     *
     * @param lineageMappingList
     * @param userId
     */
    public void sendLineageMappings(List<LineageMapping> lineageMappingList, String userId) {
        final String methodName = "sendLineageMappings";
        try {
            dataEngineClient.addLineageMappings(userId, lineageMappingList);
        } catch (InvalidParameterException | PropertyServerException e) {
            DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.OMAS_CONNECTION_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
            throw new OCFRuntimeException(
                    errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction()
            );
        } catch (UserNotAuthorizedException e) {
            DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.USER_NOT_AUTHORIZED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
            throw new OCFRuntimeException(
                    errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction()
            );
        }
    }

    /**
     * Send the provided list of LineageMappings to the Data Engine OMAS.
     *
     * @param lineageMappingList
     */
    public void sendLineageMappings(List<LineageMapping> lineageMappingList) {
        sendLineageMappings(lineageMappingList, null);
    }

}
