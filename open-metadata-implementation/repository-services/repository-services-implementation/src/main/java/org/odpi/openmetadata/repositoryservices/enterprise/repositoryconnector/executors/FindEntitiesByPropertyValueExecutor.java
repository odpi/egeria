/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.EntitiesAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.Date;
import java.util.List;

/**
 * FindEntitiesByPropertyValueExecutor is the executor for the findEntitiesByPropertyValue request.
 * This request can be issued in parallel - the call to each request potentially running in a different thread.
 */
public class FindEntitiesByPropertyValueExecutor extends PageableEntityRepositoryExecutorBase
{
    private final String       searchString;
    private final boolean      startsWith;
    private final boolean      endsWith;
    private final boolean      ignoreCase;
    private final List<String> limitResultsByClassification;


    /**
     * Create the executor.  The parameters provide the parameters for issuing the requests and
     * combining the results.
     *
     * @param userId                       unique identifier for requesting user.
     * @param entityTypeGUID               GUID of the type of entity to search for. Null means all types will
     *                                     be searched (could be slow so not recommended).
     * @param searchString                 String expression contained in any of the property values within the entities
     *                                     of the supplied type.
     * @param startsWith                   true if the search should be for strings that start with the search string
     * @param endsWith                     true if the search should be for strings that end with the search string
     * @param ignoreCase                   true if the search should be case-insensitive
     * @param fromEntityElement            the starting element number of the entities to return.
     *                                     This is used when retrieving elements
     *                                     beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus         By default, entities in all statuses are returned.  However, it is possible
     *                                     to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                                     status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime                     Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty           String name of the property that is to be used to sequence the results.
     *                                     Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder              Enum defining how the results should be ordered.
     * @param pageSize                     the maximum number of result entities that can be returned on this request.  Zero means
     *                                     unrestricted return results size.
     * @param localMetadataCollectionId    unique identifier for the local repository - may be null if no local repository
     * @param auditLog                     logging destination
     * @param repositoryValidator          validator for resulting relationships
     * @param methodName                   calling method
     */
    public FindEntitiesByPropertyValueExecutor(String                  userId,
                                               String                  entityTypeGUID,
                                               String                  searchString,
                                               boolean                 startsWith,
                                               boolean                 endsWith,
                                               boolean                 ignoreCase,
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
                                               String methodName)
    {
        this(userId,
             entityTypeGUID,
             searchString,
             startsWith,
             endsWith,
             ignoreCase,
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
     * @param userId                       unique identifier for requesting user.
     * @param entityTypeGUID               GUID of the type of entity to search for. Null means all types will
     *                                     be searched (could be slow so not recommended).
     * @param searchString                 String expression contained in any of the property values within the entities
     *                                     of the supplied type.
     * @param startsWith                   true if the search should be for strings that start with the search string
     * @param endsWith                     true if the search should be for strings that end with the search string
     * @param ignoreCase                   true if the search should be case-insensitive
     * @param fromEntityElement            the starting element number of the entities to return.
     *                                     This is used when retrieving elements
     *                                     beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus         By default, entities in all statuses are returned.  However, it is possible
     *                                     to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                                     status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime                     Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty           String name of the property that is to be used to sequence the results.
     *                                     Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder              Enum defining how the results should be ordered.
     * @param pageSize                     the maximum number of result entities that can be returned on this request.  Zero means
     *                                     unrestricted return results size.
     * @param accumulator                  location for results and returned exceptions
     * @param methodName                   calling method
     */
    private FindEntitiesByPropertyValueExecutor(String               userId,
                                                String               entityTypeGUID,
                                                String               searchString,
                                                boolean              startsWith,
                                                boolean              endsWith,
                                                boolean              ignoreCase,
                                                int                  fromEntityElement,
                                                List<InstanceStatus> limitResultsByStatus,
                                                List<String>         limitResultsByClassification,
                                                Date                 asOfTime,
                                                String               sequencingProperty,
                                                SequencingOrder      sequencingOrder,
                                                int                  pageSize,
                                                EntitiesAccumulator accumulator,
                                                String methodName)
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

        this.searchString                 = searchString;
        this.startsWith                   = startsWith;
        this.endsWith                     = endsWith;
        this.ignoreCase                   = ignoreCase;
        this.limitResultsByClassification = limitResultsByClassification;
    }


    /**
     * Return a clone of this executor with the same command parameters and accumulator instance.
     * This is used when setting up the parallel execution of the work.  Each clone executes
     * the calls to a single open metadata repository.
     *
     * @return clone of this executor
     */
    @Override
    public CloneableRepositoryExecutor getClone()
    {
        return new FindEntitiesByPropertyValueExecutor(userId,
                                                       instanceTypeGUID,
                                                       searchString,
                                                       startsWith,
                                                       endsWith,
                                                       ignoreCase,
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
     * @param metadataCollection   metadata collection object for the repository
     * @return boolean true means that the required results have been achieved
     */
    @Override
    public boolean issueRequestToRepository(String metadataCollectionId,
                                            OMRSMetadataCollection metadataCollection)
    {
        try
        {
            /*
             * Issue the request
             */
            List<EntityDetail> results = metadataCollection.findEntitiesByPropertyValue(userId,
                                                                                        instanceTypeGUID,
                                                                                        searchString,
                                                                                        startsWith,
                                                                                        endsWith,
                                                                                        ignoreCase,
                                                                                        startingElement,
                                                                                        limitResultsByStatus,
                                                                                        limitResultsByClassification,
                                                                                        asOfTime,
                                                                                        sequencingProperty,
                                                                                        sequencingOrder,
                                                                                        pageSize);

            accumulator.addEntities(results, metadataCollectionId);
        }
        catch (FunctionNotSupportedException error)
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
        catch (RepositoryErrorException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (UserNotAuthorizedException error)
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
}
