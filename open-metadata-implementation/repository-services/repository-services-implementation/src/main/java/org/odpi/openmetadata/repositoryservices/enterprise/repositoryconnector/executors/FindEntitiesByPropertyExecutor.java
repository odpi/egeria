/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.EntitiesAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.Date;
import java.util.List;

/**
 * FindEntitiesByPropertyExecutor is the executor for the findEntitiesByProperty request.
 * This request can be issued in parallel - the call to each request potentially running in a different thread.
 */
public class FindEntitiesByPropertyExecutor extends PageableEntityRepositoryExecutorBase
{
    private final InstanceProperties matchProperties;
    private final MatchCriteria      matchCriteria;
    private final List<String>       limitResultsByClassification;


    /**
     * Create the executor.  The parameters provide the parameters for issuing the requests and
     * combining the results.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param matchProperties Optional list of entity properties to match (contains wildcards).
     * @param matchCriteria Enum defining how the match properties should be matched to the entities in the repository.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @param localMetadataCollectionId unique identifier for the local repository - may be null if no local repository
     * @param auditLog logging destination
     * @param repositoryValidator validator for resulting relationships
     * @param methodName calling method
     */
    public FindEntitiesByPropertyExecutor(String                  userId,
                                          String                  entityTypeGUID,
                                          InstanceProperties      matchProperties,
                                          MatchCriteria           matchCriteria,
                                          int                     fromEntityElement,
                                          List<InstanceStatus>    limitResultsByStatus,
                                          List<String>            limitResultsByClassification,
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
             entityTypeGUID,
             matchProperties,
             matchCriteria,
             fromEntityElement,
             limitResultsByStatus,
             limitResultsByClassification,
             asOfTime,
             sequencingProperty,
             sequencingOrder,
             pageSize,
             new EntitiesAccumulator(localMetadataCollectionId, auditLog, repositoryValidator),
             methodName);
    }


    /**
     * Create the executor.  The parameters provide the parameters for issuing the requests and
     * combining the results.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param matchProperties Optional list of entity properties to match (contains wildcards).
     * @param matchCriteria Enum defining how the match properties should be matched to the entities in the repository.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @param accumulator location for results and returned exceptions
     * @param methodName calling method
     */
    private FindEntitiesByPropertyExecutor(String               userId,
                                           String               entityTypeGUID,
                                           InstanceProperties   matchProperties,
                                           MatchCriteria        matchCriteria,
                                           int                  fromEntityElement,
                                           List<InstanceStatus> limitResultsByStatus,
                                           List<String>         limitResultsByClassification,
                                            Date                asOfTime,
                                           String               sequencingProperty,
                                           SequencingOrder      sequencingOrder,
                                           int                  pageSize,
                                           EntitiesAccumulator accumulator,
                                           String               methodName)
    {
        super(userId,
              entityTypeGUID,
              fromEntityElement,
              limitResultsByStatus,
              asOfTime,
              sequencingProperty,
              sequencingOrder,
              pageSize,
              accumulator,
              methodName);

        this.matchProperties = matchProperties;
        this.matchCriteria = matchCriteria;
        this.limitResultsByClassification = limitResultsByClassification;
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
        return new FindEntitiesByPropertyExecutor(userId,
                                                  instanceTypeGUID,
                                                  matchProperties,
                                                  matchCriteria,
                                                  startingElement,
                                                  limitResultsByStatus,
                                                  limitResultsByClassification,
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
            List<EntityDetail> results = metadataCollection.findEntitiesByProperty(userId,
                                                                                   instanceTypeGUID,
                                                                                   matchProperties,
                                                                                   matchCriteria,
                                                                                   startingElement,
                                                                                   limitResultsByStatus,
                                                                                   limitResultsByClassification,
                                                                                   asOfTime,
                                                                                   sequencingProperty,
                                                                                   sequencingOrder,
                                                                                   pageSize);

            accumulator.addEntities(results, metadataCollectionId);
        }
        catch (InvalidParameterException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (FunctionNotSupportedException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (TypeErrorException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (PropertyErrorException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (PagingErrorException error)
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
        catch (Exception error)
        {
            accumulator.captureGenericException(methodName, metadataCollectionId, error);
        }

        return false;
    }
}
