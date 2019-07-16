/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;


import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.DatabaseColumnSource;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class LookupHelper {

    private static final Logger log = LoggerFactory.getLogger(LookupHelper.class);
    private OMRSRepositoryConnector enterpriseConnector;
    private OMEntityDao omEntityDao;
    private OMRSAuditLog auditLog;
    private EndpointLookup endpointLookup;
    private DatabaseLookup databaseLookup;
    private DatabaseSchemaLookup databaseSchemaLookup;
    private TableLookup tableLookup;
    private ColumnLookup columnLookup;

    public LookupHelper(OMRSRepositoryConnector enterpriseConnector, OMEntityDao omEntityDao, OMRSAuditLog auditLog) {
        this.enterpriseConnector = enterpriseConnector;
        this.auditLog = auditLog;
        this.omEntityDao = omEntityDao;
        endpointLookup = new EndpointLookup(enterpriseConnector, omEntityDao, null, auditLog);
        databaseLookup = new DatabaseLookup(enterpriseConnector, omEntityDao, endpointLookup, auditLog);
        databaseSchemaLookup = new DatabaseSchemaLookup(enterpriseConnector, omEntityDao, databaseLookup, auditLog);
        tableLookup = new TableLookup(enterpriseConnector, omEntityDao, databaseSchemaLookup, auditLog);
        columnLookup = new ColumnLookup(enterpriseConnector, omEntityDao, tableLookup, auditLog);
    }

    public EntityDetail lookupDatabaseColumn(DatabaseColumnSource source) {
        if(log.isDebugEnabled()) {
            log.debug(MessageFormat.format("lookup database column {0}", source));
        }
        return columnLookup.lookupEntity(source);
    }


}




