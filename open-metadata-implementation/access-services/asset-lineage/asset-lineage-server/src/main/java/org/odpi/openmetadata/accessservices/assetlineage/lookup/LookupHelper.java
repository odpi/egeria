/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.lookup;


import org.odpi.openmetadata.accessservices.assetlineage.contentmanager.EntitiesCreatorHelper;
import org.odpi.openmetadata.accessservices.assetlineage.events.DatabaseColumnSource;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class LookupHelper {

    private static final Logger log = LoggerFactory.getLogger(LookupHelper.class);
    private OMRSRepositoryConnector enterpriseConnector;
    private EntitiesCreatorHelper entitiesCreatorHelper;
    private OMRSAuditLog auditLog;

    public LookupHelper(OMRSRepositoryConnector enterpriseConnector, EntitiesCreatorHelper entitiesCreatorHelper, OMRSAuditLog auditLog) {
        this.enterpriseConnector = enterpriseConnector;
        this.auditLog = auditLog;
        this.entitiesCreatorHelper = entitiesCreatorHelper;
    }

    public EntityDetail lookupDatabaseColumn(DatabaseColumnSource source) throws EntityNotKnownException, UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {
        log.debug(MessageFormat.format("lookup database column {0}", source));
        EndpointLookup endpointLookup = new EndpointLookup(enterpriseConnector, entitiesCreatorHelper, null, auditLog);
        DatabaseLookup databaseLookup = new DatabaseLookup(enterpriseConnector, entitiesCreatorHelper, endpointLookup, auditLog);
        DatabaseSchemaLookup databaseSchemaLookup = new DatabaseSchemaLookup(enterpriseConnector, entitiesCreatorHelper, databaseLookup, auditLog);
        TableLookup tableLookup = new TableLookup(enterpriseConnector, entitiesCreatorHelper, databaseSchemaLookup, auditLog);
        ColumnLookup columnLookup = new ColumnLookup(enterpriseConnector, entitiesCreatorHelper, tableLookup, auditLog);
        return columnLookup.lookupEntity(source);
    }


}




