/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.ErrorMessageCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops.AbstractReadOperation;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn.*;
import xtdb.api.*;
import xtdb.api.tx.Transaction;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.model.PersistenceLayer;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.model.search.XTDBQuery;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Provides all connectivity and API-based interaction with a XTDB back-end.
 */
public class XTDBOMRSRepositoryConnector extends OMRSRepositoryConnector {

    private static final Logger log = LoggerFactory.getLogger(XTDBOMRSRepositoryConnector.class);

    private static final String SYNC = "Synchronously";
    private static final String ASYNC = "Asynchronously";

    private IXtdb xtdbAPI = null;
    private boolean luceneConfigured = false;
    private boolean synchronousIndex = true;
    private boolean luceneRegexes = true;

    /**
     * Default constructor used by the OCF Connector Provider.
     */
    public XTDBOMRSRepositoryConnector() {
        // nothing to do (yet)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMetadataCollectionId(String metadataCollectionId) {
        final String methodName = "setMetadataCollectionId";
        super.metadataCollectionId = metadataCollectionId;
        if (metadataCollectionId != null) {
            try {
                metadataCollection = new XTDBOMRSMetadataCollection(this,
                                                                    super.serverName,
                                                                    repositoryHelper,
                                                                    repositoryValidator,
                                                                    metadataCollectionId,
                                                                    auditLog);
            } catch (Exception e) {
                throw new OMRSLogicErrorException(OMRSErrorCode.NULL_METADATA_COLLECTION.getMessageDefinition(super.serverName),
                        this.getClass().getName(),
                        methodName,
                        e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public synchronized void start() throws ConnectorCheckedException {

        super.start();
        final String methodName = "start";

        auditLog.logMessage(methodName, XTDBAuditCode.REPOSITORY_NODE_STARTING.getMessageDefinition());

        // Retrieve the configuration from the configurationProperties, and serialise it directly into a .json file
        File configFile = null;
        Map<?, ?> configMap = null;
        Map<String, Object> configProperties = connectionDetails.getConfigurationProperties();
        if (configProperties != null && !configProperties.isEmpty()) {
            if (configProperties.containsKey(XTDBOMRSRepositoryConnectorProvider.XTDB_CONFIG)) {
                // JSON-style configuration
                Object xtdbCfg = configProperties.get(XTDBOMRSRepositoryConnectorProvider.XTDB_CONFIG);
                if (xtdbCfg instanceof Map) {
                    Map<String, Object> xtdbConfig = (Map<String, Object>) xtdbCfg;
                    // Dynamically set whether Lucene is configured or not based on the presence of its configuration in
                    // the configurationProperties
                    luceneConfigured = xtdbConfig.containsKey(Constants.XTDB_LUCENE);
                    // If Lucene is configured, inject the custom analyzers and indexers required by Egeria
                    if (luceneConfigured) {
                        Object luceneCfg = xtdbConfig.get(Constants.XTDB_LUCENE);
                        if (luceneCfg instanceof Map) {
                            Map<String, Object> luceneConfig = (Map<String, Object>) luceneCfg;
                            Map<String, String> indexer = new HashMap<>();
                            indexer.put("xtdb/module", "xtdb.lucene.egeria/->egeria-indexer");
                            luceneConfig.put("indexer", indexer);
                            Map<String, String> analyzer = new HashMap<>();
                            analyzer.put("xtdb/module", "xtdb.lucene.egeria/->ci-analyzer");
                            luceneConfig.put("analyzer", analyzer);
                            // Override the Lucene configuration with these injected customizations
                            xtdbConfig.put(Constants.XTDB_LUCENE, luceneConfig);
                        }
                    }
                    configMap = xtdbConfig;
                }
            }
            if (configProperties.containsKey(XTDBOMRSRepositoryConnectorProvider.XTDB_CONFIG_EDN)) {
                // EDN-style configuration
                try {
                    configFile = File.createTempFile(serverName, ".edn", new File("./"));
                    String xtdbCfg = (String) configProperties.get(XTDBOMRSRepositoryConnectorProvider.XTDB_CONFIG_EDN);
                    luceneConfigured = xtdbCfg.contains(Constants.XTDB_LUCENE);
                    BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
                    writer.write(xtdbCfg);
                    writer.close();
                } catch (IOException e) {
                    auditLog.logException(methodName, XTDBAuditCode.CANNOT_READ_CONFIGURATION.getMessageDefinition(e.getClass().getName()), e);
                    throw new ConnectorCheckedException(XTDBErrorCode.CANNOT_READ_CONFIGURATION.getMessageDefinition(repositoryName),
                                                        this.getClass().getName(), methodName, e);
                }
            }
            if (configProperties.containsKey(XTDBOMRSRepositoryConnectorProvider.SYNCHRONOUS_INDEX)) {
                Object syncIdx = configProperties.get(XTDBOMRSRepositoryConnectorProvider.SYNCHRONOUS_INDEX);
                if (syncIdx instanceof Boolean) {
                    synchronousIndex = (Boolean) syncIdx;
                }
            }
            if (configProperties.containsKey(XTDBOMRSRepositoryConnectorProvider.LUCENE_REGEXES)) {
                Object luceneReg = configProperties.get(XTDBOMRSRepositoryConnectorProvider.LUCENE_REGEXES);
                if (luceneReg instanceof Boolean) {
                    luceneRegexes = (Boolean) luceneReg;
                }
            }
        }

        try {

            if (configMap == null && configFile == null) {
                // If no configuration options were specified, we will start an in-memory node
                auditLog.logMessage(methodName, XTDBAuditCode.REPOSITORY_NODE_STARTING_NO_CONFIG.getMessageDefinition());
                xtdbAPI = IXtdb.startNode();
            } else if (configMap != null) {
                // If the map (JSON-style) is populated, use that to start the server
                auditLog.logMessage(methodName, XTDBAuditCode.REPOSITORY_NODE_STARTING_WITH_CONFIG.getMessageDefinition());
                log.debug("Starting XTDB with configuration: {}", configMap);
                xtdbAPI = IXtdb.startNode(configMap);
            } else {
                // Otherwise, fall-back to configuring based on the EDN-style config
                auditLog.logMessage(methodName, XTDBAuditCode.REPOSITORY_NODE_STARTING_WITH_CONFIG.getMessageDefinition());
                log.debug("Starting XTDB with configuration: {}", configFile);
                xtdbAPI = IXtdb.startNode(configFile);
                Files.delete(Paths.get(configFile.getCanonicalPath()));
            }
            Map<Keyword, ?> details = xtdbAPI.status();
            log.info("xtdb config details: {}", details);
            Object version = details.get(Constants.XTDB_VERSION);
            long persistenceVersion = PersistenceLayer.getVersion(xtdbAPI);
            boolean emptyDataStore = isDataStoreEmpty();
            if (persistenceVersion == -1 && emptyDataStore) {
                // If there is no persistence layer defined, and there is no metadata stored yet, mark the
                // version per this connector
                PersistenceLayer.setVersion(xtdbAPI, PersistenceLayer.LATEST_VERSION);
            } else if (persistenceVersion != PersistenceLayer.LATEST_VERSION) {
                // Otherwise, there is something in the data store already (the persistence layer details, and / or
                // pre-existing metadata), so if the versions do not match we must exit to ensure integrity of the data
                xtdbAPI.close();
                throw new ConnectorCheckedException(XTDBErrorCode.PERSISTENCE_LAYER_MISMATCH.getMessageDefinition("" + persistenceVersion, "" + PersistenceLayer.LATEST_VERSION),
                                                    this.getClass().getName(), methodName);
            }
            List<String> opts = new ArrayList<>();
            opts.add(synchronousIndex ? "synchronous indexing" : "asynchronous indexing");
            if (luceneConfigured) {
                opts.add("Lucene text index");
                if (luceneRegexes)
                    opts.add("Lucene regexes");
            }
            auditLog.logMessage(methodName,
                                XTDBAuditCode.REPOSITORY_SERVICE_STARTED.getMessageDefinition(
                            version == null ? "<null>" : version.toString(),
                            String.join(", ", opts)));
        } catch (Exception e) {
            auditLog.logException(methodName, XTDBAuditCode.FAILED_REPOSITORY_STARTUP.getMessageDefinition(e.getClass().getName()), e);
            throw new ConnectorCheckedException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                                this.getClass().getName(), methodName, e);
        }

        // Ensure the latest version of the transaction functions is committed to the repository at
        // every startup (these should all be idempotent operations if the functions already exist)
        Transaction.Builder tx = Transaction.builder();
        AddEntityProxy.create(tx);
        AddEntity.create(tx);
        UpdateEntityStatus.create(tx);
        UpdateEntityProperties.create(tx);
        UndoEntityUpdate.create(tx);
        RestoreEntity.create(tx);
        ClassifyEntityDetail.create(tx);
        ClassifyEntityProxy.create(tx);
        DeclassifyEntityDetail.create(tx);
        DeclassifyEntityProxy.create(tx);
        UpdateEntityDetailClassification.create(tx);
        UpdateEntityProxyClassification.create(tx);
        AddRelationship.create(tx);
        UpdateRelationshipStatus.create(tx);
        UpdateRelationshipProperties.create(tx);
        UndoRelationshipUpdate.create(tx);
        RestoreRelationship.create(tx);
        DeleteRelationship.create(tx);
        PurgeRelationship.create(tx);
        DeleteEntity.create(tx);
        PurgeEntity.create(tx);
        ReLinkRelationship.create(tx);
        ReIdentifyEntity.create(tx);
        ReIdentifyRelationship.create(tx);
        ReTypeEntity.create(tx);
        ReTypeRelationship.create(tx);
        ReHomeEntity.create(tx);
        ReHomeRelationship.create(tx);
        SaveEntityReferenceCopy.create(tx);
        SaveClassificationReferenceCopyEntityDetail.create(tx);
        SaveClassificationReferenceCopyEntityProxy.create(tx);
        SaveRelationshipReferenceCopy.create(tx);
        PurgeClassificationReferenceCopyEntityDetail.create(tx);
        PurgeClassificationReferenceCopyEntityProxy.create(tx);
        // Null for the timeout here means use the default (which is therefore configurable directly by
        // the XTDB configurationProperties of the connector)
        Transaction txn = tx.build();
        log.info("Adding transaction functions: {}", txn);
        TransactionInstant instant = xtdbAPI.submitTx(txn);
        xtdbAPI.awaitTx(instant, null);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void disconnect() throws ConnectorCheckedException {

        final String methodName = "disconnect";
        super.disconnect();

        // Ready the embedded XTDB node for GC
        try {
            this.xtdbAPI.close();
        } catch (IOException e) {
            if (auditLog != null)
                auditLog.logException(methodName, XTDBAuditCode.FAILED_REPOSITORY_SHUTDOWN.getMessageDefinition(e.getClass().getName()), e);
            throw new ConnectorCheckedException(XTDBErrorCode.FAILED_DISCONNECT.getMessageDefinition(),
                                                this.getClass().getName(), methodName, e);
        }
        if (auditLog != null)
            auditLog.logMessage(methodName, XTDBAuditCode.REPOSITORY_SERVICE_SHUTDOWN.getMessageDefinition(getServerName()));

    }

    /**
     * Indicates whether Lucene is configured in the connector (true) or not (false).
     * @return boolean
     */
    public boolean isLuceneConfigured() {
        return luceneConfigured;
    }

    /**
     * Indicates whether the connector expects all regular expressions to be Lucene-compatible (true) or not (false).
     * @return boolean
     */
    public boolean expectsLuceneRegexes() {
        return luceneRegexes;
    }

    /**
     * Log a problem with the connector, preferring the audit log so long as it is available and only falling
     * back to debug-level logging if it is not.
     * @param className where the problem occurred
     * @param methodName where the problem occurred
     * @param code describing the problem
     * @param cause the exception that triggered the problem (if any)
     * @param params providing additional details about the problem
     */
    public void logProblem(String className, String methodName, XTDBAuditCode code, Exception cause, String... params) {
        String location = className + "::" + methodName;
        if (auditLog != null) {
            if (cause != null) {
                auditLog.logException(location, code.getMessageDefinition(params), cause);
            } else {
                auditLog.logMessage(location, code.getMessageDefinition(params));
            }
        } else {
            log.error("No audit log available -- problem during {}: {}", location, code.getMessageDefinition(params), cause);
        }
    }

    /**
     * Checks whether the data store is currently empty.
     * @return true of the data store is empty (has no metadata stored), otherwise false
     */
    public boolean isDataStoreEmpty() {
        XTDBQuery                   query      = new XTDBQuery();
        List<IPersistentCollection> conditions = new ArrayList<>();
        conditions.add(PersistentVector.create(XTDBQuery.DOC_ID, Keyword.intern(InstanceAuditHeaderMapping.METADATA_COLLECTION_ID), Symbol.intern("_")));
        query.addConditions(conditions);
        IPersistentMap q = query.getQuery();
        q = q.assoc(Keyword.intern("limit"), 1);
        log.debug(Constants.QUERY_WITH, q);
        Collection<List<?>> results = xtdbAPI.db().query(q);
        return results == null || results.isEmpty();
    }

    /**
     * Validate that the commit was persisted, or throw an exception if it failed.
     * @param instant giving the commit point
     * @param methodName that made the commit
     * @throws Exception on any error
     */
    public void validateCommit(TransactionInstant instant, String methodName) throws Exception {
        if (synchronousIndex) {
            if (!xtdbAPI.hasTxCommitted(instant)) {
                Exception e = ErrorMessageCache.get(instant.getId());
                if (e != null) {
                    throw e;
                } else {
                    throw new RepositoryErrorException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                                       this.getClass().getName(),
                                                       methodName);
                }
            }
        }
    }

    /**
     * Validates that the commit was persisted (if synchronous), throwing an exception if it failed, and
     * also retrieves and returns the detailed entity that resulted from the transaction. Note that if the
     * operation is configured to be asynchronous, this will ALWAYS return null for the entity details.
     * @param docId of the entity within XTDB itself (i.e. prefixed)
     * @param instant giving the commit point of the transaction
     * @param methodName that made the commit
     * @return EntityDetail result of the committed transaction (synchronous) or null (asynchronous)
     * @throws Exception on any error
     */
    public EntityDetail getResultingEntity(String docId,
                                           TransactionInstant instant,
                                           String methodName) throws Exception {
        validateCommit(instant, methodName);
        if (synchronousIndex) {
            EntityDetail ed;
            try (IXtdbDatasource db = xtdbAPI.openDB(instant)) {
                XtdbDocument result = AbstractReadOperation.getXtdbObjectByReference(db, docId);
                EntityDetailMapping edm = new EntityDetailMapping(this, result);
                ed = edm.toEgeria();
            } catch (IOException e) {
                throw new RepositoryErrorException(XTDBErrorCode.CANNOT_CLOSE_RESOURCE.getMessageDefinition(),
                                                   this.getClass().getName(), methodName, e);
            }
            return ed;
        } else {
            // For async write we will ALWAYS return null, as there cannot be any consistent idea
            // of what the object looks like before the write itself has completed
            return null;
        }
    }

    /**
     * Validates that the commit was persisted (if synchronous), throwing an exception if it failed, and
     * also retrieves and returns the summary entity that resulted from the transaction. Note that if the
     * operation is configured to be asynchronous, this will ALWAYS return null for the entity details.
     * @param docId of the entity within XTDB itself (i.e. prefixed)
     * @param instant giving the commit point of the transaction
     * @param methodName that made the commit
     * @return EntitySummary result of the committed transaction (synchronous) or null (asynchronous)
     * @throws Exception on any error
     */
    public EntitySummary getResultingEntitySummary(String docId,
                                                   TransactionInstant instant,
                                                   String methodName) throws Exception {
        validateCommit(instant, methodName);
        if (synchronousIndex) {
            EntitySummary es;
            try (IXtdbDatasource db = xtdbAPI.openDB(instant)) {
                XtdbDocument result = AbstractReadOperation.getXtdbObjectByReference(db, docId);
                EntitySummaryMapping esm = new EntitySummaryMapping(this, result);
                es = esm.toEgeria();
            } catch (IOException e) {
                throw new RepositoryErrorException(XTDBErrorCode.CANNOT_CLOSE_RESOURCE.getMessageDefinition(),
                                                   this.getClass().getName(), methodName, e);
            }
            return es;
        } else {
            // For async write we will ALWAYS return null, as there cannot be any consistent idea
            // of what the object looks like before the write itself has completed
            return null;
        }
    }

    /**
     * Validates that the commit was persisted (if synchornous), throwing an exception if it failed, and
     * also retrieves and returns the detailed relationship that resulted from the transaction. Note that if
     * the operation is configured to be asynchronous, this will ALWAYS return null for the relationship detials.
     * @param docId of the relationship within XTDB itself (i.e. prefixed)
     * @param instant giving the commit point of the transaction
     * @param methodName that made the commit
     * @return Relationship result of the committed transaction (synchronous) or null (asynchronous)
     * @throws Exception on any error
     */
    public Relationship getResultingRelationship(String docId,
                                                 TransactionInstant instant,
                                                 String methodName) throws Exception {
        validateCommit(instant, methodName);
        if (synchronousIndex) {
            Relationship r;
            try (IXtdbDatasource db = xtdbAPI.openDB(instant)) {
                XtdbDocument result = AbstractReadOperation.getXtdbObjectByReference(db, docId);
                RelationshipMapping rm = new RelationshipMapping(this, result, db);
                r = rm.toEgeria();
            } catch (IOException e) {
                throw new RepositoryErrorException(XTDBErrorCode.CANNOT_CLOSE_RESOURCE.getMessageDefinition(),
                                                   this.getClass().getName(), methodName, e);
            }
            return r;
        } else {
            // For async write we will ALWAYS return null, as there cannot be any consistent idea
            // of what the object looks like before the write itself has completed
            return null;
        }
    }

    /**
     * Run multiple statements through XTDB as a single transaction.
     * @param statements the transaction to submit
     * @return TransactionInstant transaction details
     */
    public TransactionInstant runTx(Transaction statements) {
        if (log.isDebugEnabled())
            log.debug("{} transacting with: {}", synchronousIndex ? SYNC : ASYNC, statements);
        TransactionInstant tx = xtdbAPI.submitTx(statements);
        // Null for the timeout here means use the default (which is therefore configurable directly by the XTDB
        // configurationProperties of the connector)
        if (synchronousIndex) {
            return xtdbAPI.awaitTx(tx, null);
        } else {
            return tx;
        }
    }

    /**
     * Retrieve the XTDB API directly.
     * NOTE: This should only be used in very exceptional circumstances where direct access to the API
     * is needed (e.g. for testing purposes). Use any other method where possible.
     * @return IXtdb
     */
    public IXtdb getXtdbAPI() {
        return xtdbAPI;
    }

    /**
     * Default equality comparison.
     * @param o object to compare against
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        XTDBOMRSRepositoryConnector that = (XTDBOMRSRepositoryConnector) o;
        return luceneConfigured == that.luceneConfigured && synchronousIndex == that.synchronousIndex && luceneRegexes == that.luceneRegexes && Objects.equals(xtdbAPI, that.xtdbAPI);
    }

    /**
     * Default hash calculation.
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), xtdbAPI, luceneConfigured, synchronousIndex, luceneRegexes);
    }

}
