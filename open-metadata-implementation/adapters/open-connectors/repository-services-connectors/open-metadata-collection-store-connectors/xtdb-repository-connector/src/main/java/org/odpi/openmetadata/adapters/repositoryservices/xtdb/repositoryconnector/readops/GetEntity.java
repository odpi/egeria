/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.Constants;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityDetailMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityProxyMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntitySummaryMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.IXtdb;
import xtdb.api.IXtdbDatasource;
import xtdb.api.XtdbDocument;

import java.io.IOException;
import java.util.Date;

/**
 * Base class to retrieve entity information.
 */
public class GetEntity extends AbstractReadOperation {

    private static final Logger log = LoggerFactory.getLogger(GetEntity.class);

    private final String guid;

    /**
     * Default constructor ensures that a consistent datasource is created and used for the entirety of
     * the operation, and further ensures it is fully-closed afterwards irrespective of any exceptions.
     * @param xtdb connectivity to XTDB
     * @param guid of the entity to retrieve
     * @param asOfTime (optional) validity time for which to open the datasource
     */
    public GetEntity(XTDBOMRSRepositoryConnector xtdb, String guid, Date asOfTime) {
        super(xtdb, asOfTime);
        this.guid = guid;
    }

    /**
     * Retrieve the requested entity as a summary from the XTDB repository.
     * @return EntitySummary as it existed at the specified point in time
     * @throws RepositoryErrorException if any issue closing an open XTDB resource
     */
    public EntitySummary asSummary() throws RepositoryErrorException {
        final String methodName = "getEntitySummary";
        EntitySummary es;
        IXtdb xtdbAPI = xtdb.getXtdbAPI();
        try (IXtdbDatasource db = asOfTime == null ? xtdbAPI.openDB() : xtdbAPI.openDB(asOfTime)) {
            es = summaryByGuid(xtdb, db, guid);
        } catch (IOException e) {
            throw new RepositoryErrorException(XTDBErrorCode.CANNOT_CLOSE_RESOURCE.getMessageDefinition(),
                                               this.getClass().getName(), methodName, e);
        }
        return es;
    }

    /**
     * Retrieve the requested entity as a proxy from the XTDB repository.
     * @return EntityProxy as it existed at the specified point in time
     * @throws RepositoryErrorException if any issue closing an open XTDB resource
     */
    public EntityProxy asProxy() throws RepositoryErrorException {
        final String methodName = "getEntityProxy";
        EntityProxy ep;
        IXtdb xtdbAPI = xtdb.getXtdbAPI();
        try (IXtdbDatasource db = asOfTime == null ? xtdbAPI.openDB() : xtdbAPI.openDB(asOfTime)) {
            ep = proxyByGuid(xtdb, db, guid);
        } catch (IOException e) {
            throw new RepositoryErrorException(XTDBErrorCode.CANNOT_CLOSE_RESOURCE.getMessageDefinition(),
                                               this.getClass().getName(), methodName, e);
        }
        return ep;
    }

    /**
     * Retrieve the requested entity as a detailed object from the XTDB repository.
     * @return EntityDetail as it existed at the specified point in time
     * @throws RepositoryErrorException if any issues closing an open XTDB resource
     * @throws EntityProxyOnlyException if the retrieved entity is only a proxy
     */
    public EntityDetail asDetail() throws RepositoryErrorException, EntityProxyOnlyException {
        final String methodName = "getEntityDetail";
        EntityDetail ed;
        IXtdb xtdbAPI = xtdb.getXtdbAPI();
        try (IXtdbDatasource db = asOfTime == null ? xtdbAPI.openDB() : xtdbAPI.openDB(asOfTime)) {
            ed = detailByGuid(xtdb, db, guid);
        } catch (IOException e) {
            throw new RepositoryErrorException(XTDBErrorCode.CANNOT_CLOSE_RESOURCE.getMessageDefinition(),
                                               this.getClass().getName(), methodName, e);
        }
        return ed;
    }

    /**
     * Translate the provided Egeria unique identifier into an Egeria entity.
     * @param xtdb connectivity to XTDB
     * @param db from which to retrieve the details
     * @param guid unique identifier of the Egeria entity
     * @return EntitySummary
     */
    public static EntitySummary summaryByGuid(XTDBOMRSRepositoryConnector xtdb, IXtdbDatasource db, String guid) {
        return summaryByRef(xtdb, db, EntitySummaryMapping.getReference(guid));
    }

    /**
     * Translate the provided XTDB document reference into an Egeria entity.
     * @param xtdb connectivity to XTDB
     * @param db from which to retrieve the details
     * @param ref internal XTDB reference to the entity document
     * @return EntitySummary
     */
    public static EntitySummary summaryByRef(XTDBOMRSRepositoryConnector xtdb, IXtdbDatasource db, String ref) {
        XtdbDocument xtdbDoc = getXtdbObjectByReference(db, ref);
        if (log.isDebugEnabled())
            log.debug(Constants.FOUND_RESULTS, xtdbDoc == null ? null : xtdbDoc.toMap());
        if (xtdbDoc != null) {
            EntitySummaryMapping esm = new EntitySummaryMapping(xtdb, xtdbDoc);
            return esm.toEgeria();
        }
        return null;
    }

    /**
     * Translate the provided Egeria unique identifier into an Egeria entity.
     * @param xtdb connectivity to XTDB
     * @param db from which to retrieve the details
     * @param guid unique identifier of the Egeria entity
     * @return EntityProxy
     */
    public static EntityProxy proxyByGuid(XTDBOMRSRepositoryConnector xtdb, IXtdbDatasource db, String guid) {
        return proxyByRef(xtdb, db, EntityProxyMapping.getReference(guid));
    }

    /**
     * Translate the provided XTDB document reference into an Egeria entity.
     * @param xtdb connectivity to XTDB
     * @param db from which to retrieve the details
     * @param ref internal XTDB reference to the entity document
     * @return EntityProxy
     */
    public static EntityProxy proxyByRef(XTDBOMRSRepositoryConnector xtdb, IXtdbDatasource db, String ref) {
        XtdbDocument xtdbDoc = getXtdbObjectByReference(db, ref);
        if (log.isDebugEnabled())
            log.debug(Constants.FOUND_RESULTS, xtdbDoc == null ? null : xtdbDoc.toMap());
        if (xtdbDoc != null) {
            return EntityProxyMapping.getFromDoc(xtdb, xtdbDoc);
        }
        return null;
    }

    /**
     * Translate the provided Egeria unique identifier into an Egeria entity.
     * @param xtdb connectivity to XTDB
     * @param db from which to retrieve the details
     * @param guid unique identifier of the Egeria entity
     * @return EntityDetail
     * @throws EntityProxyOnlyException if the retrieved entity is only a proxy
     */
    public static EntityDetail detailByGuid(XTDBOMRSRepositoryConnector xtdb,
                                            IXtdbDatasource db,
                                            String guid) throws EntityProxyOnlyException {
        return detailByRef(xtdb, db, EntityDetailMapping.getReference(guid));
    }

    /**
     * Translate the provided XTDB document reference into an Egeria entity.
     * @param xtdb connectivity to XTDB
     * @param db from which to retrieve the details
     * @param ref reference to the entity document
     * @return EntityDetail
     * @throws EntityProxyOnlyException if the retrieved entity is only a proxy
     */
    public static EntityDetail detailByRef(XTDBOMRSRepositoryConnector xtdb,
                                           IXtdbDatasource db,
                                           String ref) throws EntityProxyOnlyException {
        final String methodName = "detailByRef";
        XtdbDocument xtdbDoc = getXtdbObjectByReference(db, ref);
        if (log.isDebugEnabled())
            log.debug(Constants.FOUND_RESULTS, xtdbDoc == null ? null : xtdbDoc.toMap());
        if (xtdbDoc != null) {
            if (EntityProxyMapping.isOnlyAProxy(xtdbDoc)) {
                throw new EntityProxyOnlyException(XTDBErrorCode.ENTITY_PROXY_ONLY.getMessageDefinition(
                        ref, xtdb.getRepositoryName()),
                                                   GetEntity.class.getName(), methodName);
            }
            EntityDetailMapping edm = new EntityDetailMapping(xtdb, xtdbDoc);
            return edm.toEgeria();
        }
        return null;
    }

}
