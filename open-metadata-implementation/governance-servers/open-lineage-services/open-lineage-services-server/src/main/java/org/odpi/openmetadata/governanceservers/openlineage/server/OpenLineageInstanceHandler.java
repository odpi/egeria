/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;


import org.odpi.openmetadata.governanceservers.openlineage.eventprocessors.GraphConstructor;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.exceptions.PropertyServerException;


class OpenLineageInstanceHandler
{
    private static OpenLineageServicesInstanceMap   instanceMap = new OpenLineageServicesInstanceMap();


    public GraphConstructor graphConstructor(String serverName) throws PropertyServerException {
        OpenLineageServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getGraphConstructor();
        } else {
            final String methodName = "graphConstructor";
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
