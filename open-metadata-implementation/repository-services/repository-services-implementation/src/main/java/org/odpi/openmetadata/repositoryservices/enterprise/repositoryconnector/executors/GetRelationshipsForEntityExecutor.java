/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.EnterpriseOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.RelationshipsAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.Date;
import java.util.List;


/**
 * GetRelationshipsForEntityExecutor is the executor for the getRelationshipsForEntity request.
 * This request can be issued in parallel - the call to each request potentially running in a different thread.
 */
public class GetRelationshipsForEntityExecutor extends PageableRepositoryExecutorBase
{
    private final String                   entityGUID;
    private final RelationshipsAccumulator accumulator;


    /**
     * Create the executor.  The parameters provide the parameters for issuing the requests and
     * combining the results.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier for the entity.
     * @param relationshipTypeGUID String GUID of the type of relationship required (null for all).
     * @param fromRelationshipElement the starting element number of the relationships to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize  the maximum number of result classifications that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @param localMetadataCollectionId unique identifier for the local repository - may be null if no local repository
     * @param auditLog logging destination
     * @param repositoryValidator validator for resulting relationships
     * @param methodName calling method
     */
    public GetRelationshipsForEntityExecutor(String                  userId,
                                             String                  entityGUID,
                                             String                  relationshipTypeGUID,
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
             entityGUID,
             relationshipTypeGUID,
             fromRelationshipElement,
             limitResultsByStatus,
             asOfTime,
             sequencingProperty,
             sequencingOrder,
             pageSize,
             new RelationshipsAccumulator(localMetadataCollectionId, auditLog, repositoryValidator),
             methodName);
    }


    /**
     * Create the executor.  The parameters provide the parameters for issuing the requests and
     * combining the results.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier for the entity.
     * @param relationshipTypeGUID String GUID of the type of relationship required (null for all).
     * @param fromRelationshipElement the starting element number of the relationships to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize  the maximum number of result classifications that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @param accumulator captures results and exceptions
     * @param methodName calling method
     */
    private GetRelationshipsForEntityExecutor(String                  userId,
                                              String                  entityGUID,
                                              String                  relationshipTypeGUID,
                                              int                     fromRelationshipElement,
                                              List<InstanceStatus>    limitResultsByStatus,
                                              Date                    asOfTime,
                                              String                  sequencingProperty,
                                              SequencingOrder sequencingOrder,
                                              int                     pageSize,
                                              RelationshipsAccumulator accumulator,
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

        this.entityGUID = entityGUID;
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
        return new GetRelationshipsForEntityExecutor(userId,
                                                     entityGUID,
                                                     instanceTypeGUID,
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
            List<Relationship> results = metadataCollection.getRelationshipsForEntity(userId,
                                                                                      entityGUID,
                                                                                      instanceTypeGUID,
                                                                                      startingElement,
                                                                                      limitResultsByStatus,
                                                                                      asOfTime,
                                                                                      sequencingProperty,
                                                                                      sequencingOrder,
                                                                                      pageSize);

            accumulator.addRelationships(results, metadataCollectionId);
        }
        catch (InvalidParameterException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (EntityNotKnownException error)
        {
            /*
             * The entity is not known in the remote system so convert this to a null response.
             */
            accumulator.addRelationships(null, metadataCollectionId);
        }
        catch (FunctionNotSupportedException error)
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
        catch (TypeErrorException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (PagingErrorException error)
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
     * Return the results of the combined requests.
     *
     * @param repositoryConnector enterprise connector
     * @return Relationships list.  Null means no relationships associated with the entity.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws PropertyErrorException the sequencing property is not valid for the attached classifications.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public List<Relationship>  getResults(EnterpriseOMRSRepositoryConnector repositoryConnector) throws InvalidParameterException,
                                                                                                        TypeErrorException,
                                                                                                        RepositoryErrorException,
                                                                                                        EntityNotKnownException,
                                                                                                        PropertyErrorException,
                                                                                                        PagingErrorException,
                                                                                                        FunctionNotSupportedException,
                                                                                                        UserNotAuthorizedException
    {
        if (accumulator.resultsReturned())
        {
            return accumulator.getResults(repositoryConnector);
        }

        handleCommonPagingRequestExceptions();

        accumulator.throwCapturedEntityNotKnownException();

        return null;
    }
}
