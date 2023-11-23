/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.Constants;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.IXtdb;
import xtdb.api.IXtdbDatasource;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * Base class that all search operations that retrieve entities should implement.
 */
public abstract class AbstractRelationshipSearchOperation extends AbstractSearchOperation {

    private static final Logger log = LoggerFactory.getLogger(AbstractRelationshipSearchOperation.class);

    /**
     * {@inheritDoc}
     */
    protected AbstractRelationshipSearchOperation(XTDBOMRSRepositoryConnector xtdb,
                                                  String typeGUID,
                                                  int fromElement,
                                                  List<InstanceStatus> limitResultsByStatus,
                                                  Date asOfTime,
                                                  String sequencingProperty,
                                                  SequencingOrder sequencingOrder,
                                                  int pageSize,
                                                  String userId) {
        super(xtdb,
                typeGUID,
                fromElement,
                limitResultsByStatus,
                null,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize,
                userId);
    }

    /**
     * Create a new search operation.
     * @param xtdb connectivity to XTDB
     * @param typeGUID unique identifier of a type definition by which to limit results
     * @param fromElement starting element for paged results
     * @param limitResultsByStatus list of statuses by which to limit results
     * @param existingDB the already-opened point-in-time from which to retrieve results
     * @param sequencingProperty the name of the property by which to sort results (only applicable when sorting by property)
     * @param sequencingOrder the mechanism to use for sorting results
     * @param pageSize the number of results to include per page
     * @param userId executing the search
     */
    protected AbstractRelationshipSearchOperation(XTDBOMRSRepositoryConnector xtdb,
                                                  String typeGUID,
                                                  int fromElement,
                                                  List<InstanceStatus> limitResultsByStatus,
                                                  IXtdbDatasource existingDB,
                                                  String sequencingProperty,
                                                  SequencingOrder sequencingOrder,
                                                  int pageSize,
                                                  String userId) {
        super(xtdb,
                typeGUID,
                fromElement,
                limitResultsByStatus,
                null,
                existingDB,
                sequencingProperty,
                sequencingOrder,
                pageSize,
                userId);
    }

    /**
     * Public interface through which to execute the search operation, which should populate the 'xtdbResults'
     * protected member of this class.
     * @return {@code List<Relationship>} list of results in Egeria form
     * @throws TypeErrorException if a requested type for searching is not known to the repository
     * @throws RepositoryErrorException on any error with the read operation, either closing the datasource or timing out
     */
    public List<Relationship> getResults() throws TypeErrorException, RepositoryErrorException {
        if (existingDB != null) {
            try {
                Collection<List<?>> xtdbResults = runQuery(existingDB);
                log.debug(Constants.FOUND_RESULTS, xtdbResults);
                return translateResults(xtdbResults, existingDB);
            } catch (TimeoutException e) {
                throw new RepositoryTimeoutException(XTDBErrorCode.QUERY_TIMEOUT.getMessageDefinition(xtdb.getRepositoryName()),
                                                     this.getClass().getName(), this.getClass().getName(), e);
            }
        } else {
            List<Relationship> results;
            IXtdb xtdbAPI = xtdb.getXtdbAPI();
            try (IXtdbDatasource db = asOfTime == null ? xtdbAPI.openDB() : xtdbAPI.openDB(asOfTime)) {
                Collection<List<?>> xtdbResults = runQuery(db);
                log.debug(Constants.FOUND_RESULTS, xtdbResults);
                results = translateResults(xtdbResults, db);
            } catch (IOException e) {
                throw new RepositoryErrorException(XTDBErrorCode.CANNOT_CLOSE_RESOURCE.getMessageDefinition(),
                                                   this.getClass().getName(), this.getClass().getName(), e);
            } catch (TimeoutException e) {
                throw new RepositoryTimeoutException(XTDBErrorCode.QUERY_TIMEOUT.getMessageDefinition(""),
                                                     this.getClass().getName(), this.getClass().getName(), e);
            }
            return results;
        }
    }

    /**
     * Translate the set of XTDB document IDs into a list of Egeria Relationships.
     * @param xtdbResults collection of XT unique IDs to translate to full Egeria representations
     * @param db opened datasource from which to retrieve the full Egeria representations
     * @return {@code List<Relationship>} list of Egeria representation of the results
     */
    private List<Relationship> translateResults(Collection<List<?>> xtdbResults,
                                                IXtdbDatasource db) {
        final String methodName = "translateResults";
        List<Relationship> results = null;
        if (xtdbResults != null) {
            results = new ArrayList<>();
            for (List<?> xtdbResult : xtdbResults) {
                String docRef = (String) xtdbResult.get(0);
                Relationship relationship = GetRelationship.byRef(xtdb, db, docRef);
                if (relationship == null) {
                    xtdb.logProblem(this.getClass().getName(),
                                    methodName,
                                    XTDBAuditCode.MAPPING_FAILURE,
                                    null,
                                    "relationship",
                                    docRef,
                                    "cannot be translated to Relationship");
                } else {
                    results.add(relationship);
                }
            }
        }
        return results;
    }

}
