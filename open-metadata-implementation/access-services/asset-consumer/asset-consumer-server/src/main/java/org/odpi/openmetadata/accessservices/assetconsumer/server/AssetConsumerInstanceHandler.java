/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.server;

import org.odpi.openmetadata.accessservices.assetconsumer.elements.*;
import org.odpi.openmetadata.accessservices.assetconsumer.handlers.LoggingHandler;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.MetadataElement;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.MetadataRelationship;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;


/**
 * AssetConsumerInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the AssetConsumerAdmin class.
 */
class AssetConsumerInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    AssetConsumerInstanceHandler()
    {
        super(AccessServiceDescription.ASSET_CONSUMER_OMAS.getAccessServiceFullName());

        AssetConsumerRegistration.registerAccessService();
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    LoggingHandler getLoggingHandler(String userId,
                                     String serverName,
                                     String serviceOperationName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        AssetConsumerServicesInstance instance = (AssetConsumerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                               serverName,
                                                                                                               serviceOperationName);

        if (instance != null)
        {
            return instance.getLoggingHandler();
        }

        return null;
    }



    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    AssetHandler<Asset> getAssetHandler(String userId,
                                        String serverName,
                                        String serviceOperationName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {

        AssetConsumerServicesInstance instance = (AssetConsumerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                               serverName,
                                                                                                               serviceOperationName);

        if (instance != null)
        {
            return instance.getAssetHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    ReferenceableHandler<MetadataElement> getMetadataElementHandler(String userId,
                                                                    String serverName,
                                                                    String serviceOperationName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {

        AssetConsumerServicesInstance instance = (AssetConsumerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                               serverName,
                                                                                                               serviceOperationName);

        if (instance != null)
        {
            return instance.getMetadataElementHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    ReferenceableHandler<MetadataRelationship> getMetadataRelationshipHandler(String userId,
                                                                              String serverName,
                                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {

        AssetConsumerServicesInstance instance = (AssetConsumerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                               serverName,
                                                                                                               serviceOperationName);

        if (instance != null)
        {
            return instance.getMetadataRelationshipHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    CommentHandler<OpenMetadataAPIDummyBean> getCommentHandler(String userId,
                                                               String serverName,
                                                               String serviceOperationName) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {

        AssetConsumerServicesInstance instance = (AssetConsumerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                               serverName,
                                                                                                               serviceOperationName);

        if (instance != null)
        {
            return instance.getCommentHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    ConnectionHandler<OpenMetadataAPIDummyBean> getConnectionHandler(String userId,
                                                                     String serverName,
                                                                     String serviceOperationName) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        AssetConsumerServicesInstance instance = (AssetConsumerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                               serverName,
                                                                                                               serviceOperationName);

        if (instance != null)
        {
            return instance.getConnectionHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    GlossaryTermHandler<MeaningElement> getGlossaryTermHandler(String userId,
                                                               String serverName,
                                                               String serviceOperationName) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        AssetConsumerServicesInstance instance = (AssetConsumerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                               serverName,
                                                                                                               serviceOperationName);

        if (instance != null)
        {
            return instance.getGlossaryTermHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    InformalTagHandler<InformalTagElement> getInformalTagHandler(String userId,
                                                                 String serverName,
                                                                 String serviceOperationName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        AssetConsumerServicesInstance instance = (AssetConsumerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                               serverName,
                                                                                                               serviceOperationName);

        if (instance != null)
        {
            return instance.getInformalTagHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    LikeHandler<OpenMetadataAPIDummyBean> getLikeHandler(String userId,
                                                         String serverName,
                                                         String serviceOperationName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        AssetConsumerServicesInstance instance = (AssetConsumerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                               serverName,
                                                                                                               serviceOperationName);

        if (instance != null)
        {
            return instance.getLikeHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    RatingHandler<OpenMetadataAPIDummyBean> getRatingHandler(String userId,
                                                             String serverName,
                                                             String serviceOperationName) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        AssetConsumerServicesInstance instance = (AssetConsumerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                               serverName,
                                                                                                               serviceOperationName);

        if (instance != null)
        {
            return instance.getRatingHandler();
        }

        return null;
    }
}
