/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.server;

import org.odpi.openmetadata.accessservices.informationview.assets.DatabaseContextHandler;
import org.odpi.openmetadata.accessservices.informationview.context.DataViewContextBuilder;
import org.odpi.openmetadata.accessservices.informationview.context.ReportContextBuilder;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.PropertyServerException;
import org.odpi.openmetadata.accessservices.informationview.registration.RegistrationHandler;
import org.odpi.openmetadata.accessservices.informationview.reports.DataViewHandler;
import org.odpi.openmetadata.accessservices.informationview.reports.ReportHandler;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;

/**
 * InformationViewInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the InformationViewAdmin class.
 */
class InformationViewInstanceHandler
{
    private static InformationViewServicesInstanceMap   instanceMap = new InformationViewServicesInstanceMap();

    /**
     * Default constructor registers the access service
     */
    InformationViewInstanceHandler() {
        new InformationViewOMASRegistration();
    }


    /**
     * Retrieve the report creator for the access service.
     *
     * @param serverName name of the server tied to the request
     * @return reportCreator for exclusive use by the requested instance
     * @throws PropertyServerException no instance for this server
     */
    ReportHandler getReportCreator(String serverName) throws PropertyServerException {
        InformationViewServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getReportHandler();
        } else {
            final String methodName = "getReportCreator";
            throwError(serverName, methodName);
            return null;
        }
    }


    /**
     * Retrieve the report data view creator for the access service.
     *
     * @param serverName name of the server tied to the request
     * @return reportCreator for exclusive use by the requested instance
     * @throws PropertyServerException no instance for this server
     */
     DataViewHandler getDataViewHandler(String serverName) throws PropertyServerException {
        InformationViewServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getDataViewHandler();
        } else {
            final String methodName = "getDataViewHandler";
            throwError(serverName, methodName);
            return null;
        }
    }
    /**
     * Retrieve the handler for retrieving assets details for the access service.
     *
     * @param serverName name of the server tied to the request
     * @return assetsHandler for exclusive use by the requested instance
     * @throws PropertyServerException no instance for this server
     */
     DatabaseContextHandler getAssetContextHandler(String serverName) throws PropertyServerException {
        InformationViewServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getContextBuilders().getDatabaseContextHandler();
        } else {
            final String methodName = "getDatabaseContextHandler";
            throwError(serverName, methodName);
            return null;
        }
    }

    /**
     *
     * @param serverName name of the server tied to the request
     * @return registration handler for exclusive use by the requested instance
     * @throws PropertyServerException
     */
     RegistrationHandler getRegistrationHandler(String serverName) throws PropertyServerException {
        InformationViewServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getRegistrationHandler();
        } else {
            final String methodName = "getRegistrationHandler";
            throwError(serverName, methodName);
            return null;
        }
    }


    ReportContextBuilder getReportContextBuilder(String serverName){
        InformationViewServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getContextBuilders().getReportContextBuilder();
        } else {
            final String methodName = "getReportContextBuilder";
            throwError(serverName, methodName);
            return null;
        }
    }


        DataViewContextBuilder getDataViewContextBuilder(String serverName){
        InformationViewServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getContextBuilders().getDataViewContextBuilder();
        } else {
            final String methodName = "getReportContextBuilder";
            throwError(serverName, methodName);
            return null;
        }
    }


    private void throwError(String serverName, String methodName) throws PropertyServerException {
        InformationViewErrorCode errorCode = InformationViewErrorCode.SERVICE_NOT_INITIALIZED;
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

        throw new PropertyServerException(this.getClass().getName(),
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


}
