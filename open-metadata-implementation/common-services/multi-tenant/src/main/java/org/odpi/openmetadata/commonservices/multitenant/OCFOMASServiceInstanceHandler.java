/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.*;

public abstract class OCFOMASServiceInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Constructor
     *
     * @param accessServiceDescription a collection of descriptive strings for the OMAS
     */
    public OCFOMASServiceInstanceHandler(AccessServiceDescription accessServiceDescription)
    {
        super(accessServiceDescription);
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public AssetHandler getAssetHandler(String userId,
                                        String serverName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {

        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId, serverName);

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
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public CertificationHandler getCertificationHandler(String userId,
                                                        String serverName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {

        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId, serverName);

        if (instance != null)
        {
            return instance.getCertificationHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public CommentHandler getCommentHandler(String userId,
                                            String serverName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {

        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId, serverName);

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
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public ConnectionHandler getConnectionHandler(String userId,
                                                  String serverName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId, serverName);

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
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public ConnectorTypeHandler getConnectorTypeHandler(String userId,
                                                        String serverName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId, serverName);

        if (instance != null)
        {
            return instance.getConnectorTypeHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public EndpointHandler getEndpointHandler(String userId,
                                              String serverName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId, serverName);

        if (instance != null)
        {
            return instance.getEndpointHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public ExternalIdentifierHandler getExternalIdentifierHandler(String userId,
                                                                  String serverName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId, serverName);

        if (instance != null)
        {
            return instance.getExternalIdentifierHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public ExternalReferenceHandler getExternalReferenceHandler(String userId,
                                                                String serverName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId, serverName);

        if (instance != null)
        {
            return instance.getExternalReferenceHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public InformalTagHandler getInformalTagHandler(String userId,
                                                    String serverName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId, serverName);

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
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public LicenseHandler getLicenseHandler(String userId,
                                            String serverName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId, serverName);

        if (instance != null)
        {
            return instance.getLicenseHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public LikeHandler getLikeHandler(String userId,
                                      String serverName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId, serverName);

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
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public LocationHandler getLocationHandler(String userId,
                                              String serverName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId, serverName);

        if (instance != null)
        {
            return instance.getLocationHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public MeaningHandler getMeaningHandler(String userId,
                                            String serverName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId, serverName);

        if (instance != null)
        {
            return instance.getMeaningHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public NoteHandler getNoteHandler(String userId,
                                      String serverName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId, serverName);

        if (instance != null)
        {
            return instance.getNoteHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public NoteLogHandler getNoteLogHandler(String userId,
                                            String serverName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId, serverName);

        if (instance != null)
        {
            return instance.getNoteLogHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public RatingHandler getRatingHandler(String userId,
                                          String serverName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId, serverName);

        if (instance != null)
        {
            return instance.getRatingHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public RelatedMediaHandler getRelatedMediaHandler(String userId,
                                                      String serverName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId, serverName);

        if (instance != null)
        {
            return instance.getRelatedMediaHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public SchemaTypeHandler getSchemaTypeHandler(String userId,
                                                  String serverName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId, serverName);

        if (instance != null)
        {
            return instance.getSchemaTypeHandler();
        }

        return null;
    }
}
