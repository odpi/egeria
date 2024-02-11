/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.server;

import org.odpi.openmetadata.commonservices.multitenant.OMAGServerServiceInstance;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.ffdc.LineageWarehouseException;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.ffdc.LineageWarehouseErrorCode;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.handlers.LineageWarehouseHandler;


/**
 * OpenLineageInstance maintains the instance information needed to execute queries on a Open lineage server.
 */
public class LineageWarehouseServerInstance extends OMAGServerServiceInstance {


    private LineageWarehouseHandler lineageWarehouseHandler;

    /**
     * Constructor where REST Services used.
     *
     * @param serverName         name of this server
     * @param serviceName        name of this service
     * @param lineageWarehouseHandler
     */
    public LineageWarehouseServerInstance(String serverName,
                                          String serviceName,
                                          int maxPageSize,
                                          LineageWarehouseHandler lineageWarehouseHandler) {
        super(serverName, serviceName, maxPageSize);
        this.lineageWarehouseHandler = lineageWarehouseHandler;
    }


    /**
     * Return the OpenLineageHandler.
     *
     * @return OpenLineageHandler.
     */
    LineageWarehouseHandler getOpenLineageHandler() throws LineageWarehouseException
    {
        final String methodName = "getOpenLineageHandler";
        if (this.lineageWarehouseHandler == null) {
            LineageWarehouseErrorCode errorCode    = LineageWarehouseErrorCode.OPEN_LINEAGE_HANDLER_NOT_INSTANTIATED;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);
            throw new LineageWarehouseException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction());
        }
        return lineageWarehouseHandler;
    }

}
