/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops;

import clojure.lang.IPersistentMap;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.Constants;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.RelationshipMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.model.search.TextConditionBuilder;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.model.search.XTDBQuery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.ICursor;
import xtdb.api.IXtdbDatasource;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * Base class that all search operations should implement.
 */
public abstract class AbstractSearchOperation extends AbstractReadOperation {

    private static final Logger log = LoggerFactory.getLogger(AbstractSearchOperation.class);

    private final OMRSRepositoryHelper repositoryHelper;

    protected final String typeGUID;
    protected final int fromElement;
    protected final List<InstanceStatus> limitResultsByStatus;
    protected final SearchClassifications matchClassifications;
    protected final String sequencingProperty;
    protected final SequencingOrder sequencingOrder;
    protected final int pageSize;
    protected final String userId;

    /**
     * Create a new search operation.
     * @param xtdb connectivity to XTDB
     * @param typeGUID unique identifier of a type definition by which to limit results
     * @param fromElement starting element for paged results
     * @param limitResultsByStatus list of statuses by which to limit results
     * @param matchClassifications classification criteria by which to limit results
     * @param asOfTime the point-in-time for which to retrieve results
     * @param sequencingProperty the name of the property by which to sort results (only applicable when sorting by property)
     * @param sequencingOrder the mechanism to use for sorting results
     * @param pageSize the number of results to include per page
     * @param userId executing the search
     */
    protected AbstractSearchOperation(XTDBOMRSRepositoryConnector xtdb,
                                      String typeGUID,
                                      int fromElement,
                                      List<InstanceStatus> limitResultsByStatus,
                                      SearchClassifications matchClassifications,
                                      Date asOfTime,
                                      String sequencingProperty,
                                      SequencingOrder sequencingOrder,
                                      int pageSize,
                                      String userId) {
        super(xtdb, asOfTime);
        this.repositoryHelper = xtdb.getRepositoryHelper();
        this.typeGUID = typeGUID;
        this.fromElement = fromElement;
        this.limitResultsByStatus = limitResultsByStatus;
        this.matchClassifications = matchClassifications;
        this.sequencingProperty = sequencingProperty;
        this.sequencingOrder = sequencingOrder;
        this.pageSize = pageSize;
        this.userId = userId;
    }

    /**
     * Create a new search operation.
     * @param xtdb connectivity to XTDB
     * @param typeGUID unique identifier of a type definition by which to limit results
     * @param fromElement starting element for paged results
     * @param limitResultsByStatus list of statuses by which to limit results
     * @param matchClassifications classification criteria by which to limit results
     * @param existingDB the already-opened point-in-time from which to retrieve results
     * @param sequencingProperty the name of the property by which to sort results (only applicable when sorting by property)
     * @param sequencingOrder the mechanism to use for sorting results
     * @param pageSize the number of results to include per page
     * @param userId executing the search
     */
    protected AbstractSearchOperation(XTDBOMRSRepositoryConnector xtdb,
                                      String typeGUID,
                                      int fromElement,
                                      List<InstanceStatus> limitResultsByStatus,
                                      SearchClassifications matchClassifications,
                                      IXtdbDatasource existingDB,
                                      String sequencingProperty,
                                      SequencingOrder sequencingOrder,
                                      int pageSize,
                                      String userId) {
        super(xtdb, existingDB);
        this.repositoryHelper = xtdb.getRepositoryHelper();
        this.typeGUID = typeGUID;
        this.fromElement = fromElement;
        this.limitResultsByStatus = limitResultsByStatus;
        this.matchClassifications = matchClassifications;
        this.sequencingProperty = sequencingProperty;
        this.sequencingOrder = sequencingOrder;
        this.pageSize = pageSize;
        this.userId = userId;
    }

    /**
     * Interface that must be implemented to actually execute the query logic.
     * @param db the datasource against which to run the query
     * @return {@code Collection<List<?>>} of internal XT references (IDs) that match the query
     * @throws TypeErrorException if a requested type for searching is not known to the repository
     * @throws TimeoutException if the query times out
     * @throws RepositoryErrorException if there is any issue iterating through the results
     */
    protected abstract Collection<List<?>> runQuery(IXtdbDatasource db) throws TypeErrorException, TimeoutException, RepositoryErrorException;

    /**
     * Search XTDB based on the provided parameters, using an already-opened point-in-time view of the database (should
     * work across both Entities and Relationships).
     * @param db already opened point-in-time view of the database
     * @param category to limit the search to either entities or relationships (required)
     * @param typeGuid to limit the search by type (optional)
     * @param subtypeGuids to limit the search to a set of subtypes (optional)
     * @param matchProperties by which to limit the results (optional)
     * @param fromElement starting element for paging
     * @param limitResultsByStatus by which to limit results (optional)
     * @param matchClassifications by which to limit entity results (must be null for relationships) (optional)
     * @param sequencingProperty by which to order the results (required if sequencingOrder involves a property)
     * @param sequencingOrder by which to order results (optional, will default to GUID)
     * @param pageSize maximum number of results per page
     * @param namespace by which to qualify the matchProperties
     * @param userId of the user running the query
     * @return {@code Collection<List<?>>} list of the XTDB document references that match
     * @throws TypeErrorException if a requested type for searching is not known to the repository
     * @throws RepositoryErrorException if there is any issue iterating through the results
     */
    protected Collection<List<?>> searchXtdb(IXtdbDatasource db,
                                             TypeDefCategory category,
                                             String typeGuid,
                                             List<String> subtypeGuids,
                                             SearchProperties matchProperties,
                                             int fromElement,
                                             List<InstanceStatus> limitResultsByStatus,
                                             SearchClassifications matchClassifications,
                                             String sequencingProperty,
                                             SequencingOrder sequencingOrder,
                                             int pageSize,
                                             String namespace,
                                             String userId) throws TypeErrorException, RepositoryErrorException {
        XTDBQuery query = new XTDBQuery();
        updateQuery(query,
                category,
                typeGuid,
                subtypeGuids,
                matchProperties,
                limitResultsByStatus,
                matchClassifications,
                sequencingProperty,
                sequencingOrder,
                namespace,
                userId);
        IPersistentMap q = query.getQuery();
        log.debug(Constants.QUERY_WITH, q);
        Collection<List<?>> results;
        try (ICursor<List<?>> searchCursor = db.openQuery(q)) {
            results = deduplicateAndPage(searchCursor, fromElement, pageSize);
        } catch (IOException e) {
            throw new RepositoryErrorException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                               this.getClass().getName(), this.getClass().getName(), e);
        }
        return results;
    }

    /**
     * Update the provided query with the specified parameters.
     * @param query into which to add conditions
     * @param category to limit the search to either entities or relationships (required)
     * @param typeGuid to limit the search by type (optional)
     * @param subtypeGuids to limit the search to a set of subtypes (optional)
     * @param matchProperties by which to limit the results (optional)
     * @param limitResultsByStatus by which to limit results (optional)
     * @param matchClassifications by which to limit entity results (must be null for relationships) (optional)
     * @param sequencingProperty by which to order the results (required if sequencingOrder involves a property)
     * @param sequencingOrder by which to order results (optional, will default to GUID)
     * @param namespace by which to qualify the matchProperties
     * @param userId of the user running the query
     * @throws TypeErrorException if a requested type for searching is not known to the repository
     */
    protected void updateQuery(XTDBQuery query,
                               TypeDefCategory category,
                               String typeGuid,
                               List<String> subtypeGuids,
                               SearchProperties matchProperties,
                               List<InstanceStatus> limitResultsByStatus,
                               SearchClassifications matchClassifications,
                               String sequencingProperty,
                               SequencingOrder sequencingOrder,
                               String namespace,
                               String userId) throws TypeErrorException {
        // Note that we will put the property search criteria first to optimise the search, which can more than double
        // the speed for very broad scenarios (where no type limiter is specified, or only Referenceable)
        Set<String> completeTypeSet = getCompleteSetOfTypeNamesForSearch(userId, typeGuid, subtypeGuids, namespace);
        query.addPropertyConditions(matchProperties, namespace, completeTypeSet, xtdb, xtdb.isLuceneConfigured(), xtdb.expectsLuceneRegexes());
        query.addTypeCondition(category, typeGuid, subtypeGuids);
        query.addClassificationConditions(matchClassifications, completeTypeSet, xtdb, xtdb.isLuceneConfigured(), xtdb.expectsLuceneRegexes());
        query.addSequencing(sequencingOrder, sequencingProperty, namespace, completeTypeSet, xtdb);
        // Note: we will always limit by 'e', even if the TypeDefCategory indicates this is a relationship as these
        // operations only ever return a single type of instance (entity or relationship), and 'e' is therefore used
        // generally to represent either
        query.addStatusLimiters(limitResultsByStatus, XTDBQuery.DOC_ID);
    }

    /**
     * Search all text properties in XTDB based on the provided parameters, using an already-opened point-in-time view
     * of the database (should work across both Entities and Relationships).
     * @param db already opened point-in-time view of the database
     * @param category to limit the search to either entities or relationships (required)
     * @param typeGuid to limit the search by type (optional)
     * @param searchCriteria by which to limit the results (required, must be a Java regular expression)
     * @param fromElement starting element for paging
     * @param limitResultsByStatus by which to limit results (optional)
     * @param matchClassifications by which to limit entity results (must be null for relationships) (optional)
     * @param sequencingProperty by which to order the results (required if sequencingOrder involves a property)
     * @param sequencingOrder by which to order results (optional, will default to GUID)
     * @param pageSize maximum number of results per page
     * @param namespace by which to qualify the matchProperties
     * @param userId of the user running the query
     * @return {@code Collection<List<?>>} list of the XTDB document references that match
     * @throws TypeErrorException if a requested type for searching is not known to the repository
     * @throws RepositoryErrorException if there is any issue iterating through the results
     */
    protected Collection<List<?>> searchXtdbText(IXtdbDatasource db,
                                                 TypeDefCategory category,
                                                 String typeGuid,
                                                 String searchCriteria,
                                                 int fromElement,
                                                 List<InstanceStatus> limitResultsByStatus,
                                                 SearchClassifications matchClassifications,
                                                 String sequencingProperty,
                                                 SequencingOrder sequencingOrder,
                                                 int pageSize,
                                                 String namespace,
                                                 String userId) throws TypeErrorException, RepositoryErrorException {
        XTDBQuery query = new XTDBQuery();
        updateTextQuery(query,
                category,
                typeGuid,
                searchCriteria,
                limitResultsByStatus,
                matchClassifications,
                sequencingProperty,
                sequencingOrder,
                namespace,
                userId);
        IPersistentMap q = query.getQuery();
        log.debug(Constants.QUERY_WITH, q);
        Collection<List<?>> results;
        try (ICursor<List<?>> searchCursor = db.openQuery(q)) {
            results = deduplicateAndPage(searchCursor, fromElement, pageSize);
        } catch (IOException e) {
            throw new RepositoryErrorException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                               this.getClass().getName(), this.getClass().getName(), e);
        }
        return results;
    }

    /**
     * Update the provided query with the specified parameters for a free-form text search across all text fields.
     * @param query into which to add conditions
     * @param category to limit the search to either entities or relationships (required)
     * @param typeGuid to limit the search by type (optional)
     * @param searchCriteria defining the textual regular expression to use to match against all text fields
     * @param limitResultsByStatus by which to limit results (optional)
     * @param matchClassifications by which to limit entity results (must be null for relationships) (optional)
     * @param sequencingProperty by which to order the results (required if sequencingOrder involves a property)
     * @param sequencingOrder by which to order results (optional, will default to GUID)
     * @param namespace by which to qualify the sequencing property (if any)
     * @param userId of the user running the query
     * @throws TypeErrorException if a requested type for searching is not known to the repository
     */
    private void updateTextQuery(XTDBQuery query,
                                 TypeDefCategory category,
                                 String typeGuid,
                                 String searchCriteria,
                                 List<InstanceStatus> limitResultsByStatus,
                                 SearchClassifications matchClassifications,
                                 String sequencingProperty,
                                 SequencingOrder sequencingOrder,
                                 String namespace,
                                 String userId) throws TypeErrorException {
        // Note that we will put the search string criteria first to optimise the search, which can more than double
        // the speed for very broad scenarios (where no type limiter is specified, or only Referenceable)
        Set<String> completeTypeSet = getCompleteSetOfTypeNamesForSearch(userId, typeGuid, null, namespace);
        if (xtdb.isLuceneConfigured()) {
            query.addConditions(TextConditionBuilder.buildWildcardLuceneCondition(searchCriteria, xtdb, completeTypeSet, namespace, xtdb.expectsLuceneRegexes()));
        } else {
            query.addConditions(TextConditionBuilder.buildWildcardTextCondition(searchCriteria, xtdb, completeTypeSet, namespace, false, xtdb.expectsLuceneRegexes()));
        }
        query.addTypeCondition(category, typeGuid, null);
        query.addClassificationConditions(matchClassifications, completeTypeSet, xtdb, xtdb.isLuceneConfigured(), xtdb.expectsLuceneRegexes());
        query.addSequencing(sequencingOrder, sequencingProperty, namespace, completeTypeSet, xtdb);
        // Note: we will always limit by 'e', even if the TypeDefCategory indicates this is a relationship as these
        // operations only ever return a single type of instance (entity or relationship), and 'e' is therefore used
        // generally to represent either
        query.addStatusLimiters(limitResultsByStatus, XTDBQuery.DOC_ID);
    }

    /**
     * Retrieve the complete list of type names that have been requested by the search.
     * @param userId of the user running the query
     * @param typeGuid provided to the search, to limit by type
     * @param subtypeGuids provided to the search, to limit to a set of subtypes
     * @param namespace by which properties will be qualified (allowing us to see whether the types should be for entities or relationships)
     * @return {@code Set<String>} of the names of all types and subtypes to include in the search
     * @throws TypeErrorException if a requested type for searching is not known to the repository
     */
    private Set<String> getCompleteSetOfTypeNamesForSearch(String userId,
                                                           String typeGuid,
                                                           List<String> subtypeGuids,
                                                           String namespace) throws TypeErrorException {
        final String methodName = "getCompleteListOfTypeNamesForSearch";
        Set<String> complete = new HashSet<>();
        if (namespace != null) {
            if (subtypeGuids != null && !subtypeGuids.isEmpty()) {
                // If subtypes were specified, we can short-circuit to only considering those (and logic is the same
                // across entity types and relationship types)
                for (String subtypeGuid : subtypeGuids) {
                    String typeDefName = repositoryHelper.getTypeDef(xtdb.getRepositoryName(), "subtypeGuids", subtypeGuid, methodName).getName();
                    addAllSubtypesToSet(complete, typeDefName);
                }
            } else if (typeGuid != null) {
                // Otherwise we need to consider all sub-types of the provided typeGuid
                String typeDefName = repositoryHelper.getTypeDef(xtdb.getRepositoryName(), "typeGuid", typeGuid, methodName).getName();
                addAllSubtypesToSet(complete, typeDefName);
            } else {
                // Otherwise we need to consider all types of the provided kind
                if (RelationshipMapping.RELATIONSHIP_PROPERTIES_NS.equals(namespace)) {
                    // We need all relationship types
                    try {
                        List<TypeDef> typeDefinitions = xtdb.getMetadataCollection().findTypeDefsByCategory(userId, TypeDefCategory.RELATIONSHIP_DEF);
                        if (typeDefinitions != null) {
                            for (TypeDef typeDef : typeDefinitions) {
                                String typeDefName = typeDef.getName();
                                addAllSubtypesToSet(complete, typeDefName);
                            }
                        }
                    } catch (InvalidParameterException | RepositoryErrorException | UserNotAuthorizedException e) {
                        xtdb.logProblem(this.getClass().getName(),
                                        methodName,
                                        XTDBAuditCode.UNEXPECTED_RUNTIME_ERROR,
                                        e,
                                        "unable to retrieve relationship typedefs",
                                        e.getClass().getName());
                    }
                } else {
                    // Otherwise we need all entity types
                    String typeDefName = "OpenMetadataRoot";
                    addAllSubtypesToSet(complete, typeDefName);
                }
            }
        }
        return complete;
    }

    /**
     * Add the names of all subtypes of the provided typeDefName to the provided set of subtypes.
     * @param subtypes to update with names of subtypes
     * @param typeDefName for which to retrieve all subtypes
     */
    private void addAllSubtypesToSet(Set<String> subtypes, String typeDefName) {
        subtypes.add(typeDefName);  // add the typedef itself, and then its subtypes
        List<String> subtypesList = repositoryHelper.getSubTypesOf(xtdb.getRepositoryName(), typeDefName);
        if (subtypesList != null) {
            subtypes.addAll(subtypesList);
        }
    }

    /**
     * De-duplicate and return only the selected page of results from the provided collection of XTDB query results.
     * @param results from a XTDB query
     * @param fromElement starting point for the page
     * @param pageSize number of elements to include in the page
     * @return {@code Collection<List<?>>} of only the single page of results specified
     */
    protected Collection<List<?>> deduplicateAndPage(ICursor<List<?>> results,
                                                     int fromElement,
                                                     int pageSize) {

        List<List<?>> pageOfResults  = new ArrayList<>();
        if (results != null && results.hasNext()) {

            Set<List<?>> skippedResults = new HashSet<>();
            int currentIndex = 0;
            // 0 as a pageSize means ALL pages -- so we should return every result that we found (up to the maximum
            // number of results allowed by the connector)
            pageSize = pageSize > 0 ? pageSize : xtdb.getMaxPageSize();
            int lastResultIndex = (fromElement + pageSize);

            while (results.hasNext()) {

                List<?> next = results.next();
                if (currentIndex >= lastResultIndex) {
                    // If we are at / beyond the last index, break out of the loop
                    break;
                } else if (currentIndex >= fromElement) {
                    // TODO: for large page sizes we may be able to optimise the following, if the assumption
                    //  that the bag of results returned will always have duplicate values next to each other
                    //  (then we only need to compare to the last result to see if we should skip it or not,
                    //  rather than a 'contains' against a List)
                    if (!pageOfResults.contains(next)) {
                        // Otherwise, only add this result if it is at or beyond the starting point (fromElement) and
                        // our list of results does not already contain this result (in which case, also increment our
                        // current index for the number of results we have captured)
                        pageOfResults.add(next);
                        currentIndex++;
                    }
                } else if (!skippedResults.contains(next)) {
                    // Otherwise, remember that we have are skipping this result and increment the current index
                    // accordingly just this once (necessary to skip only the correct number of results, when the
                    // fromElement is not 0)
                    skippedResults.add(next);
                    currentIndex++;
                }
                // In any other scenario, it is a result that has already been included or already been skipped,
                // so we do not need to increment our index or do anything with the result -- just move on to the
                // next one

            }

        }

        return pageOfResults;

    }

}
