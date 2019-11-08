/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.governanceservers.openlineage.exception.OpenLineageErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.services.GraphQueryingServices;

class OpenLineageInstanceHandler
{
    private static OpenLineageServicesInstanceMap   instanceMap = new OpenLineageServicesInstanceMap();

    public GraphQueryingServices queryHandler(String serverName) throws PropertyServerException {
        OpenLineageServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getGraphQueryingServices();
        } else {
            final String methodName = "queryHandler";
            throwError(serverName, methodName);
            return null;
        }
    }

    private void throwError(String serverName, String methodName) throws PropertyServerException {
        OpenLineageErrorCode errorCode = OpenLineageErrorCode.SERVICE_NOT_INITIALIZED;
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

        throw new PropertyServerException(this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

}
