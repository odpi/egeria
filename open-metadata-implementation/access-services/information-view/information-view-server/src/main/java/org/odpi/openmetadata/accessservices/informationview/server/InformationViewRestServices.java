/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.server;

import org.odpi.openmetadata.accessservices.informationview.assets.DatabaseContextHandler;
import org.odpi.openmetadata.accessservices.informationview.events.*;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.InformationViewUncheckedExceptionBase;
import org.odpi.openmetadata.accessservices.informationview.registration.RegistrationHandler;
import org.odpi.openmetadata.accessservices.informationview.reports.DataViewHandler;
import org.odpi.openmetadata.accessservices.informationview.reports.ReportHandler;
import org.odpi.openmetadata.accessservices.informationview.responses.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.List;


public class InformationViewRestServices {

    InformationViewInstanceHandler  instanceHandler = new InformationViewInstanceHandler();

    /**
     *
     * @param serverName
     * @param userId
     * @param requestBody  - structure of the report
     * @return
     */
    public VoidResponse submitReport(String serverName,
                                     String userId,
                                     ReportRequestBody requestBody) {

        VoidResponse  response = new VoidResponse();

        try {
            ReportHandler reportCreator = instanceHandler.getReportCreator(serverName);
            reportCreator.submitReportModel(requestBody);
        }
        catch (InformationViewUncheckedExceptionBase e) {
           handleErrorResponse(e);
        }

        return response;
    }

    /**
     *
     * @param serverName
     * @param userId
     * @param requestBody - representation of data view
     * @return
     */
    public InformationViewOMASAPIResponse submitDataView(String serverName,
                                                         String userId,
                                                         DataViewRequestBody requestBody) {

        VoidResponse response = new VoidResponse();

        try {
            DataViewHandler dataViewHandler = instanceHandler.getDataViewHandler(serverName);
            dataViewHandler.createDataView(requestBody);
        }
        catch (InformationViewUncheckedExceptionBase e) {
            handleErrorResponse(e);
        }

        return response;
    }

    /**
     *
     * @param serverName
     * @param userId
     * @param startFrom
     * @param pageSize
     * @return
     */
    public InformationViewOMASAPIResponse getDatabases(String serverName,
                                                       String userId,
                                                       int startFrom,
                                                       int pageSize) {

        DatabaseListResponse response = new DatabaseListResponse();
        try {
            DatabaseContextHandler databaseContextHandler = instanceHandler.getAssetContextHandler(serverName);
            List<DatabaseSource> databases = databaseContextHandler.getDatabases(startFrom, pageSize);
            response.setDatabasesList(databases);
        }
        catch (InformationViewUncheckedExceptionBase e) {
            handleErrorResponse(e);
        }
        return response;
    }

    /**
     *
     * @param serverName
     * @param userId
     * @param databaseGuid - guid of the database entity
     * @param startFrom
     * @param pageSize
     * @return
     */
    public InformationViewOMASAPIResponse getTablesForDatabase(String serverName,
                                                               String userId,
                                                               String databaseGuid,
                                                               int startFrom,
                                                               int pageSize) {

        TableListResponse response = new TableListResponse();

        try {
            DatabaseContextHandler databaseContextHandler = instanceHandler.getAssetContextHandler(serverName);
            List<TableSource> tables = databaseContextHandler.getTables(databaseGuid, startFrom, pageSize);
            response.setTableList(tables);
        }
        catch (InformationViewUncheckedExceptionBase e) {
            handleErrorResponse(e);
        }

        return response;
    }

    public InformationViewOMASAPIResponse getTableContext(String serverName,
                                                                String userId,
                                                                String tableGuid) {

        TableContextResponse response = new TableContextResponse();
        try {
            DatabaseContextHandler databaseContextHandler = instanceHandler.getAssetContextHandler(serverName);
            List<TableContextEvent> tables = databaseContextHandler.getTableContext(tableGuid);
            response.setTableContexts(tables);
        }
        catch (InformationViewUncheckedExceptionBase e) {
            handleErrorResponse(e);
        }
        return response;
    }

    public InformationViewOMASAPIResponse getTableColumns(String serverName,
                                                                String userId,
                                                                String tableGuid,
                                                                int startFrom,
                                                                int pageSize) {
        TableColumnsResponse response = new TableColumnsResponse();

        try {
            DatabaseContextHandler databaseContextHandler = instanceHandler.getAssetContextHandler(serverName);
            List<TableColumn> columns = databaseContextHandler.getTableColumns(tableGuid, startFrom, pageSize);
            response.setTableColumns(columns);
        }
        catch (InformationViewUncheckedExceptionBase e) {
            handleErrorResponse(e);
        }

        return response;
    }

     public InformationViewOMASAPIResponse registerExternalTool(String serverName,
                                                                String userId,
                                                                RegistrationRequestBody requestBody) {
         RegistrationResponse response = new RegistrationResponse();
         RegistrationHandler registrationHandler = instanceHandler.getRegistrationHandler(serverName);
         try {
             EntityDetail entityDetail  = registrationHandler.registerTool(requestBody);
             response.setGuid(entityDetail.getGUID());
         } catch (InformationViewUncheckedExceptionBase e) {
             handleErrorResponse(e);
         }
         return response;
    }




    private InformationViewOMASAPIResponse handleErrorResponse(InformationViewUncheckedExceptionBase e) {
        VoidResponse  response = new VoidResponse();
        response.setExceptionClassName(e.getReportingClassName());
        response.setExceptionErrorMessage(e.getReportedErrorMessage());
        response.setRelatedHTTPCode(e.getHttpErrorCode());
        response.setExceptionUserAction(e.getReportedUserAction());
        return response;
    }

}
