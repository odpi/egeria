/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.server;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.ReportCreator;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.PropertyServerException;

/**
 * InformationViewInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the AssetCatalogAdmin class.
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
    ReportCreator getReportCreator(String serverName) throws PropertyServerException {
        InformationViewServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getReportCreator();
        } else {
            final String methodName = "getReportCreator";

            InformationViewErrorCode errorCode    = InformationViewErrorCode.SERVICE_NOT_INITIALIZED;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

            throw new PropertyServerException(this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }
}
