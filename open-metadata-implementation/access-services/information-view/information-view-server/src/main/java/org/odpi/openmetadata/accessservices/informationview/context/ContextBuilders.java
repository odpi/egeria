/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.context;

import org.odpi.openmetadata.accessservices.informationview.assets.DatabaseContextHandler;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

public class ContextBuilders {

    private ReportContextBuilder reportContextBuilder;
    private DatabaseContextHandler databaseContextHandler;
    private DataViewContextBuilder dataViewContextBuilder;

    public ContextBuilders(OMRSRepositoryConnector enterpriseConnector, OMEntityDao entityDao, OMRSAuditLog auditLog){
        reportContextBuilder = new ReportContextBuilder(enterpriseConnector, entityDao, auditLog);
        databaseContextHandler = new DatabaseContextHandler(enterpriseConnector, entityDao, auditLog );
        dataViewContextBuilder = new DataViewContextBuilder(enterpriseConnector, entityDao, auditLog);
    }

    public ReportContextBuilder getReportContextBuilder() {
        return reportContextBuilder;
    }

    public void setReportContextBuilder(ReportContextBuilder reportContextBuilder) {
        this.reportContextBuilder = reportContextBuilder;
    }

    public DatabaseContextHandler getDatabaseContextHandler() {
        return databaseContextHandler;
    }

    public void setDatabaseContextHandler(DatabaseContextHandler databaseContextHandler) {
        this.databaseContextHandler = databaseContextHandler;
    }

    public DataViewContextBuilder getDataViewContextBuilder() {
        return dataViewContextBuilder;
    }

    public void setDataViewContextBuilder(DataViewContextBuilder dataViewContextBuilder) {
        this.dataViewContextBuilder = dataViewContextBuilder;
    }

    @Override
    public String toString() {
        return "{" +
                "reportContextBuilder=" + reportContextBuilder +
                ", databaseContextHandler=" + databaseContextHandler +
                ", dataViewContextBuilder=" + dataViewContextBuilder +
                '}';
    }
}
