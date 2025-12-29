/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.dataengineer.server;


import org.odpi.openmetadata.adapters.connectors.productmanager.tabulardatasets.OpenMetadataDataSetConnectorBase;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.TabularDataSetReportResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularDataSetReport;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The DataEngineerRESTServices provides the server-side implementation of the Data Engineer Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class DataEngineerRESTServices extends TokenController
{
    private static final DataEngineerInstanceHandler instanceHandler = new DataEngineerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(DataEngineerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public DataEngineerRESTServices()
    {
    }


    /**
     * Returns details of a tabular data set including the data rows that match the startFromRow and maxRowCount values.
     * If maxRowCount=0 then no data records are returned.
     *
     * @param serverName     name of called server
     * @param tabularDataSetGUID     unique identifier of tabular dat set asset
     * @param startFromRow    cursor position in the data set
     * @param maxRowCount maximum number of rows to return.  0 means no rows.
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public TabularDataSetReportResponse getTabularDataSetReport(String serverName,
                                                                String tabularDataSetGUID,
                                                                int    startFromRow,
                                                                int    maxRowCount)
    {
        final String methodName = "getTabularDataSetReport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TabularDataSetReportResponse response = new TabularDataSetReportResponse();
        AuditLog                     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient connectedAssetClient = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = connectedAssetClient.getConnectorForAsset(userId, tabularDataSetGUID, auditLog);

            if (connector instanceof OpenMetadataDataSetConnectorBase openMetadataDataSetConnectorBase)
            {
                openMetadataDataSetConnectorBase.setLocalEnvironment(userId, serverName, instanceHandler.getServiceName());
            }

            if (connector instanceof ReadableTabularDataSource readableTabularDataSource)
            {
                connector.start();

                TabularDataSetReport tabularDataSetReport = new TabularDataSetReport();

                tabularDataSetReport.setRecordCount(readableTabularDataSource.getRecordCount());
                tabularDataSetReport.setTableName(readableTabularDataSource.getTableName());
                tabularDataSetReport.setTableDescription(readableTabularDataSource.getTableDescription());
                tabularDataSetReport.setColumnDescriptions(readableTabularDataSource.getColumnDescriptions());

                if (maxRowCount > 0)
                {
                    Map<String, List<String>> tabularDataRows = new HashMap<>();

                    for (int i = startFromRow; ((i < startFromRow + maxRowCount) &&
                                                (i < startFromRow + tabularDataSetReport.getRecordCount())); i++)
                    {
                        List<String> dataRecord = readableTabularDataSource.readRecord(i);

                        if (dataRecord != null)
                        {
                            tabularDataRows.put(String.valueOf(i), dataRecord);
                        }
                    }

                    tabularDataSetReport.setDataRecords(tabularDataRows);
                }

                response.setTabularDataSetReport(tabularDataSetReport);

                connector.disconnect();
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
