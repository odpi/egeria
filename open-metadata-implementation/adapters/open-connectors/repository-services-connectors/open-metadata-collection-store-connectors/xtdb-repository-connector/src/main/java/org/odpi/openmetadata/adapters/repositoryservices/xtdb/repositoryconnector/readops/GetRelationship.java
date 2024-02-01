/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.Constants;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.RelationshipMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.IXtdb;
import xtdb.api.IXtdbDatasource;
import xtdb.api.XtdbDocument;

import java.io.IOException;
import java.util.Date;

/**
 * Base class to retrieve relationship information.
 */
public class GetRelationship extends AbstractReadOperation {

    private static final Logger log = LoggerFactory.getLogger(GetRelationship.class);

    private final String guid;

    /**
     * Default constructor ensures that a consistent datasource is created and used for the entirety of
     * the operation, and further ensures it is fully-closed afterwards irrespective of any exceptions.
     * @param xtdb connectivity to XTDB
     * @param guid of the relationship to retrieve
     * @param asOfTime (optional) validity time for which to open the datasource
     */
    public GetRelationship(XTDBOMRSRepositoryConnector xtdb, String guid, Date asOfTime) {
        super(xtdb, asOfTime);
        this.guid = guid;
    }

    /**
     * Retrieve the requested relationship from the XTDB repository.
     * @return Relationship as it existed at the specified point in time
     * @throws RepositoryErrorException if any issue closing an open XTDB resource
     */
    public Relationship execute() throws RepositoryErrorException {
        final String methodName = "getRelationship";
        Relationship r;
        // Since a relationship involves not only the relationship object, but also some details from each proxy,
        // we will open a database up-front to re-use for multiple queries (try-with to ensure it is closed after).
        IXtdb xtdbAPI = xtdb.getXtdbAPI();
        try (IXtdbDatasource db = asOfTime == null ? xtdbAPI.openDB() : xtdbAPI.openDB(asOfTime)) {
            r = byRef(xtdb, db, RelationshipMapping.getReference(guid));
        } catch (IOException e) {
            throw new RepositoryErrorException(XTDBErrorCode.CANNOT_CLOSE_RESOURCE.getMessageDefinition(),
                                               this.getClass().getName(), methodName, e);
        }
        return r;
    }

    /**
     * Translate the provided XTDB document reference into an Egeria relationship.
     * @param xtdb connectivity to XTDB
     * @param db from which to retrieve the details
     * @param ref reference to the relationship document
     * @return Relationship
     */
    public static Relationship byRef(XTDBOMRSRepositoryConnector xtdb, IXtdbDatasource db, String ref) {
        XtdbDocument xtdbDoc = getXtdbObjectByReference(db, ref);
        if (log.isDebugEnabled())
            log.debug(Constants.FOUND_RESULTS, xtdbDoc == null ? null : xtdbDoc.toMap());
        if (xtdbDoc != null) {
            RelationshipMapping rm = new RelationshipMapping(xtdb, xtdbDoc, db);
            return rm.toEgeria();
        }
        return null;
    }

}
