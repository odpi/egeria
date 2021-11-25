/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;


/**
 * GetEntityProxyExecutor provides the executor for the isEntityProxyKnown and getEntityProxy methods.
 * The Entity is received from metadata repositories until the entity from the home repository is retrieved.
 * The classifications are saved from each retrieval and the latest version of the entity.
 *
 * When the home repository's version is retrieved, it switches to phase two where calls to getHomeClassifications are
 * made to the remaining repositories.  This is to pick up any stray classifications.
 *
 * The two phases are used to take advantage of an optimization in the OMRSRESTRepositoryConnector which remembers if a
 * remote repository does not support a call.
 * The getHomeClassifications methods are only supported by repositories that support the the ability to home classifications on
 * entities from other repositories.  This means that the phase two calls will only go remote if the remote repository
 * supports the getHomeClassifications method.
 */
public class GetEntityProxyExecutor extends GetEntitySummaryExecutor
{
    private EntityProxy entityProxy;
    private String localMetadataCollectionId;

    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier (guid) for the entity.
     * @param localMetadataCollectionId id of local metadata collection
     * @param auditLog logging destination
     * @param methodName calling method
     */
    public GetEntityProxyExecutor(String   userId,
                                  String   entityGUID,
                                  String   localMetadataCollectionId,
                                  AuditLog auditLog,
                                  String   methodName)
    {
        super(userId, entityGUID, auditLog, methodName);
        this.localMetadataCollectionId = localMetadataCollectionId;
    }

    /**
     * Perform the required action for the supplied repository.
     * Create requests occur in the first repository that accepts the call.
     * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
     * there are no positive results from any repository.
     *
     * @param metadataCollectionId unique identifier for the metadata collection for the repository
     * @param metadataCollection metadata collection object for the repository
     * @return boolean true means that the required results have been achieved
     */
    @Override
    public boolean issueRequestToRepository(String                 metadataCollectionId,
                                            OMRSMetadataCollection metadataCollection) {

        if(!localMetadataCollectionId.equals(metadataCollectionId)){
            // skip remote collections, execute on local only
            return false;
        }

        try
        {
            entityProxy = metadataCollection.getEntityProxy(userId, entityGUID);
            return true;
        }
        catch (InvalidParameterException error)
        {
            accumulator.captureException(error);
        }
        catch (RepositoryErrorException error)
        {
            accumulator.captureException(error);
        }
        catch (UserNotAuthorizedException error)
        {
            accumulator.captureException(error);
        }
        catch (Exception error)
        {
            accumulator.captureGenericException(methodName, metadataCollectionId, error);
        }

        return false;
    }

    /**
     * Return the header, classifications and properties of a specific entity.
     *
     * @return EntityProxy structure.
     */
    public EntityProxy getEntityProxy()
    {
        return entityProxy;
    }

    /**
     * Return the header, classifications and properties of a specific entity.
     *
     * @return EntityProxy structure.
     */
    public EntityProxy isEntityProxyKnown()
    {
        return entityProxy;
    }
}
