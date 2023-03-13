/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;

import org.odpi.openmetadata.commonservices.multitenant.OMAGServerServiceInstance;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageServerErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.handlers.OpenLineageHandler;


/**
 * OpenLineageInstance maintains the instance information needed to execute queries on a Open lineage server.
 */
public class OpenLineageServerInstance extends OMAGServerServiceInstance {


    private OpenLineageHandler openLineageHandler;

    /**
     * Constructor where REST Services used.
     *
     * @param serverName         name of this server
     * @param serviceName        name of this service
     * @param openLineageHandler
     */
    public OpenLineageServerInstance(String serverName,
                                     String serviceName,
                                     int maxPageSize,
                                     OpenLineageHandler openLineageHandler) {
        super(serverName, serviceName, maxPageSize);
        this.openLineageHandler = openLineageHandler;
    }


    /**
     * Return the OpenLineageHandler.
     *
     * @return OpenLineageHandler.
     */
    OpenLineageHandler getOpenLineageHandler() throws OpenLineageException {
        final String methodName = "getOpenLineageHandler";
        if (this.openLineageHandler == null) {
            OpenLineageServerErrorCode errorCode = OpenLineageServerErrorCode.OPEN_LINEAGE_HANDLER_NOT_INSTANTIATED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);
            throw new OpenLineageException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        return openLineageHandler;
    }

}
