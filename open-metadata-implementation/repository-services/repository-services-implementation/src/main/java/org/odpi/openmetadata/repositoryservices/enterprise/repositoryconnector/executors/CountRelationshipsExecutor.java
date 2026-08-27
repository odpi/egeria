/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.EndMatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.CountAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.Date;
import java.util.List;


/**
 * CountRelationshipsExecutor is the executor for the countRelationships request.
 * This request can be issued in parallel - the call to each request potentially running in a different thread.
 * It sums the counts returned by each repository in the cohort; it does not deduplicate reference copies of the
 * same relationship held by more than one repository, since doing so would require fetching every matching
 * relationship.
 */
public class CountRelationshipsExecutor extends PageableRepositoryExecutorBase
{
    private final SearchProperties matchProperties;
    private final List<String>     instanceSubtypeGUIDs;
    private final boolean          skipSubtypes;
    private final List<String>     end1EntityGUIDs;
    private final String           end1EntityTypeGUID;
    private final List<String>     end2EntityGUIDs;
    private final String           end2EntityTypeGUID;
    private final EndMatchCriteria endMatchCriteria;
    private final CountAccumulator accumulator;

    /**
     * Create the executor.  The parameters provide the parameters for issuing the requests and
     * combining the results.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param relationshipSubtypeGUIDs optional list of the unique identifiers (guids) for subtypes of the
     *                                 relationshipTypeGUID to include in (or, if skipSubtypes is true, exclude from) the search results.
     *                                 Null means all subtypes.
     * @param skipSubtypes if true, relationshipSubtypeGUIDs is treated as the list of subtypes to exclude from the
     *                     search results rather than the only subtypes to include.  Ignored if relationshipSubtypeGUIDs is null.
     * @param end1EntityGUIDs optional list of entity guids used to match end 1 of the relationships.
     * @param end1EntityTypeGUID optional unique identifier of the type that the entity at end 1 must
     *                           belong to.  Subtypes of the named type match too.  This is independent of
     *                           end1EntityGUIDs: supplying the type on its own, with the guids left null,
     *                           asks for the relationships that start at any entity of that type.
     * @param end2EntityGUIDs optional list of entity guids used to match end 2 of the relationships.
     * @param end2EntityTypeGUID optional unique identifier of the type that the entity at end 2 must
     *                           belong to.  Subtypes of the named type match too.  This is independent of
     *                           end2EntityGUIDs: supplying the type on its own, with the guids left null,
     *                           asks for the relationships that end at any entity of that type.
     * @param endMatchCriteria criteria for matching the ends of the relationships.
     * @param matchProperties Optional list of relationship property conditions to match.
     * @param fromRelationshipElement not used - the count is not affected by paging.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty not used - the count is not affected by sequencing.
     * @param sequencingOrder not used - the count is not affected by sequencing.
     * @param pageSize not used - the count is not affected by paging.
     * @param localMetadataCollectionId unique identifier for the local repository - may be null if no local repository
     * @param auditLog logging destination
     * @param repositoryValidator validator for resulting relationships
     * @param methodName calling method
     */
    public CountRelationshipsExecutor(String                  userId,
                                      String                  relationshipTypeGUID,
                                      List<String>            relationshipSubtypeGUIDs,
                                      boolean                 skipSubtypes,
                                      List<String>            end1EntityGUIDs,
                                      String                  end1EntityTypeGUID,
                                      List<String>            end2EntityGUIDs,
                                      String                  end2EntityTypeGUID,
                                      EndMatchCriteria        endMatchCriteria,
                                      SearchProperties        matchProperties,
                                      int                     fromRelationshipElement,
                                      List<InstanceStatus>    limitResultsByStatus,
                                      Date                    asOfTime,
                                      String                  sequencingProperty,
                                      SequencingOrder         sequencingOrder,
                                      int                     pageSize,
                                      String                  localMetadataCollectionId,
                                      AuditLog                auditLog,
                                      OMRSRepositoryValidator repositoryValidator,
                                      String                  methodName)
    {
        this(userId,
             relationshipTypeGUID,
             relationshipSubtypeGUIDs,
             skipSubtypes,
             end1EntityGUIDs,
             end1EntityTypeGUID,
             end2EntityGUIDs,
             end2EntityTypeGUID,
             endMatchCriteria,
             matchProperties,
             fromRelationshipElement,
             limitResultsByStatus,
             asOfTime,
             sequencingProperty,
             sequencingOrder,
             pageSize,
             new CountAccumulator(localMetadataCollectionId, auditLog, repositoryValidator),
             methodName);
    }


    /**
     * Create the executor.  The parameters provide the parameters for issuing the requests and
     * combining the results.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param relationshipSubtypeGUIDs optional list of the unique identifiers (guids) for subtypes of the
     *                                 relationshipTypeGUID to include in (or, if skipSubtypes is true, exclude from) the search results.
     *                                 Null means all subtypes.
     * @param skipSubtypes if true, relationshipSubtypeGUIDs is treated as the list of subtypes to exclude from the
     *                     search results rather than the only subtypes to include.  Ignored if relationshipSubtypeGUIDs is null.
     * @param end1EntityGUIDs optional list of entity guids used to match end 1 of the relationships.
     * @param end1EntityTypeGUID optional unique identifier of the type that the entity at end 1 must
     *                           belong to.  Subtypes of the named type match too.  This is independent of
     *                           end1EntityGUIDs: supplying the type on its own, with the guids left null,
     *                           asks for the relationships that start at any entity of that type.
     * @param end2EntityGUIDs optional list of entity guids used to match end 2 of the relationships.
     * @param end2EntityTypeGUID optional unique identifier of the type that the entity at end 2 must
     *                           belong to.  Subtypes of the named type match too.  This is independent of
     *                           end2EntityGUIDs: supplying the type on its own, with the guids left null,
     *                           asks for the relationships that end at any entity of that type.
     * @param endMatchCriteria criteria for matching the ends of the relationships.
     * @param matchProperties Optional list of relationship property conditions to match.
     * @param fromRelationshipElement not used - the count is not affected by paging.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty not used - the count is not affected by sequencing.
     * @param sequencingOrder not used - the count is not affected by sequencing.
     * @param pageSize not used - the count is not affected by paging.
     * @param accumulator captures results and exceptions
     * @param methodName calling method
     */
    private CountRelationshipsExecutor(String                  userId,
                                       String                  relationshipTypeGUID,
                                       List<String>            relationshipSubtypeGUIDs,
                                       boolean                 skipSubtypes,
                                       List<String>            end1EntityGUIDs,
                                       String                  end1EntityTypeGUID,
                                       List<String>            end2EntityGUIDs,
                                       String                  end2EntityTypeGUID,
                                       EndMatchCriteria        endMatchCriteria,
                                       SearchProperties        matchProperties,
                                       int                     fromRelationshipElement,
                                       List<InstanceStatus>    limitResultsByStatus,
                                       Date                    asOfTime,
                                       String                  sequencingProperty,
                                       SequencingOrder         sequencingOrder,
                                       int                     pageSize,
                                       CountAccumulator        accumulator,
                                       String                  methodName)
    {
        super(userId,
              relationshipTypeGUID,
              fromRelationshipElement,
              limitResultsByStatus,
              sequencingProperty,
              sequencingOrder,
              pageSize,
              asOfTime,
              accumulator,
              methodName);

        this.matchProperties = matchProperties;
        this.instanceSubtypeGUIDs = relationshipSubtypeGUIDs;
        this.skipSubtypes = skipSubtypes;
        this.end1EntityGUIDs = end1EntityGUIDs;
        this.end1EntityTypeGUID = end1EntityTypeGUID;
        this.end2EntityGUIDs = end2EntityGUIDs;
        this.end2EntityTypeGUID = end2EntityTypeGUID;
        this.endMatchCriteria = endMatchCriteria;

        this.accumulator = accumulator;
    }


    /**
     * Return a clone of this executor with the same command parameters and accumulator instance.
     * This is used when setting up the parallel execution of the work.  Each clone executes
     * the calls to a single open metadata repository.
     *
     * @return clone of this executor
     */
    public CloneableRepositoryExecutor getClone()
    {
        return new CountRelationshipsExecutor(userId,
                                              instanceTypeGUID,
                                              instanceSubtypeGUIDs,
                                              skipSubtypes,
                                              end1EntityGUIDs,
                                              end1EntityTypeGUID,
                                              end2EntityGUIDs,
                                              end2EntityTypeGUID,
                                              endMatchCriteria,
                                              matchProperties,
                                              startingElement,
                                              limitResultsByStatus,
                                              asOfTime,
                                              sequencingProperty,
                                              sequencingOrder,
                                              pageSize,
                                              accumulator,
                                              methodName);
    }


    /**
     * Perform the required action for the supplied repository.
     *
     * @param metadataCollectionId unique identifier for the metadata collection for the repository
     * @param metadataCollection metadata collection object for the repository
     * @return boolean true means that the required results have been achieved
     */
    public boolean issueRequestToRepository(String                 metadataCollectionId,
                                            OMRSMetadataCollection metadataCollection)
    {
        try
        {
            /*
             * Issue the request
             */
            long result = metadataCollection.countRelationships(userId,
                                                                 instanceTypeGUID,
                                                                 instanceSubtypeGUIDs,
                                                                 skipSubtypes,
                                                                 end1EntityGUIDs,
                                                                 end1EntityTypeGUID,
                                                                 end2EntityGUIDs,
                                                                 end2EntityTypeGUID,
                                                                 endMatchCriteria,
                                                                 matchProperties,
                                                                 startingElement,
                                                                 limitResultsByStatus,
                                                                 asOfTime,
                                                                 sequencingProperty,
                                                                 sequencingOrder,
                                                                 pageSize);

            accumulator.addCount(result, metadataCollectionId);
        }
        catch (FunctionNotSupportedException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (RepositoryErrorException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (UserNotAuthorizedException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (TypeErrorException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (PagingErrorException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (PropertyErrorException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (InvalidParameterException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (Exception error)
        {
            accumulator.captureGenericException(methodName, metadataCollectionId, error);
        }

        return false;
    }


    /**
     * Return the results of the combined requests - the sum of the counts returned by all repositories that
     * responded successfully.
     *
     * @return count of relationships matching the supplied criteria.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public long getResults() throws InvalidParameterException,
                                    TypeErrorException,
                                    RepositoryErrorException,
                                    PropertyErrorException,
                                    PagingErrorException,
                                    FunctionNotSupportedException,
                                    UserNotAuthorizedException
    {
        if (accumulator.resultsReturned())
        {
            return accumulator.getResults();
        }

        handleCommonPagingRequestExceptions();

        return 0L;
    }
}
