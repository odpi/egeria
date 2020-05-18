/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.*;

import java.util.List;

/**
 * Provide access to the common handlers for OMAS's that use the OCF beans.
 */
public abstract class OCFOMASServiceInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Constructor
     *
     * @param serviceName name of the service
     */
    public OCFOMASServiceInstanceHandler(String serviceName)
    {
        super(serviceName);
    }


    /**
     * Return the service's official name
     *
     * @param callingServiceURLName url fragment that indicates the service name
     * @return String name
     */
    public String  getServiceName(String callingServiceURLName)
    {
        final String assetOwnerURLName      = "asset-owner";
        final String assetConsumerURLName   = "asset-consumer";
        final String discoveryEngineURLName = "discovery-engine";

        String callingServiceName;

        if (assetOwnerURLName.equals(callingServiceURLName))
        {
            callingServiceName = AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceFullName();
        }
        else if (assetConsumerURLName.equals(callingServiceURLName))
        {
            callingServiceName = AccessServiceDescription.ASSET_CONSUMER_OMAS.getAccessServiceFullName();
        }
        else if (discoveryEngineURLName.equals(callingServiceURLName))
        {
            callingServiceName = AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceFullName();
        }
        else
        {
            callingServiceName = callingServiceURLName;
        }

        return callingServiceName;
    }


    /**
     * Get the instance for a specific service.
     *
     * @param userId calling user
     * @param serverName name of this server
     * @param callingServiceURLName url fragment that indicates the service name
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return specific service instance
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    private OMASServiceInstance getCallingServiceInstance(String  userId,
                                                          String  serverName,
                                                          String  callingServiceURLName,
                                                          String  serviceOperationName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        return (OMASServiceInstance)platformInstanceMap.getServiceInstance(userId,
                                                                           serverName,
                                                                           this.getServiceName(callingServiceURLName),
                                                                           serviceOperationName);
    }


    /**
     * Get the supportedZones for a specific service. This is used in services that are shared by different
     * access services.
     *
     * @param userId calling user
     * @param serverName name of this server
     * @param callingServiceURLName url fragment that indicates the service name
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return list of governance zones
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public List<String> getSupportedZones(String  userId,
                                          String  serverName,
                                          String  callingServiceURLName,
                                          String  serviceOperationName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        OMASServiceInstance callingServiceInstance = this.getCallingServiceInstance(userId,
                                                                                    serverName,
                                                                                    callingServiceURLName,
                                                                                    serviceOperationName);
        if (callingServiceInstance != null)
        {
            return callingServiceInstance.getSupportedZones();
        }

        return null;
    }


    /**
     * Get the defaultZones for a specific service.  This is used in services that are shared by different
     * access services.
     *
     * @param userId calling user
     * @param serverName name of this server
     * @param callingServiceURLName url fragment that indicates the service name
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return list of governance zones
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public List<String> getDefaultZones(String  userId,
                                        String  serverName,
                                        String  callingServiceURLName,
                                        String  serviceOperationName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        OMASServiceInstance callingServiceInstance = this.getCallingServiceInstance(userId,
                                                                                    serverName,
                                                                                    callingServiceURLName,
                                                                                    serviceOperationName);
        if (callingServiceInstance != null)
        {
            return callingServiceInstance.getDefaultZones();
        }

        return null;
    }


    /**
     * Get the publish for a specific service.  This is used in services that are shared by different
     * access services.
     *
     * @param userId calling user
     * @param serverName name of this server
     * @param callingServiceURLName url fragment that indicates the service name
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return list of governance zones
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public List<String> getPublishZones(String  userId,
                                        String  serverName,
                                        String  callingServiceURLName,
                                        String  serviceOperationName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        OMASServiceInstance callingServiceInstance = this.getCallingServiceInstance(userId,
                                                                                    serverName,
                                                                                    callingServiceURLName,
                                                                                    serviceOperationName);
        if (callingServiceInstance != null)
        {
            return callingServiceInstance.getPublishZones();
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
    public AssetHandler getAssetHandler(String userId,
                                        String serverName,
                                        String serviceOperationName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {

        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
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
    public CertificationHandler getCertificationHandler(String userId,
                                                        String serverName,
                                                        String serviceOperationName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {

        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

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
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public CommentHandler getCommentHandler(String userId,
                                            String serverName,
                                            String serviceOperationName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {

        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
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
    public ConnectionHandler getConnectionHandler(String userId,
                                                  String serverName,
                                                  String serviceOperationName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
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
    public ConnectorTypeHandler getConnectorTypeHandler(String userId,
                                                        String serverName,
                                                        String serviceOperationName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

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
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public EndpointHandler getEndpointHandler(String userId,
                                              String serverName,
                                              String serviceOperationName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

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
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public ExternalIdentifierHandler getExternalIdentifierHandler(String userId,
                                                                  String serverName,
                                                                  String serviceOperationName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

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
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public ExternalReferenceHandler getExternalReferenceHandler(String userId,
                                                                String serverName,
                                                                String serviceOperationName) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

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
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public FileSystemHandler getFileSystemHandler(String userId,
                                                  String serverName,
                                                  String serviceOperationName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {

        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

        if (instance != null)
        {
            return instance.getFileSystemHandler();
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
    public GlossaryTermHandler getGlossaryTermHandler(String userId,
                                                      String serverName,
                                                      String serviceOperationName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
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
    public InformalTagHandler getInformalTagHandler(String userId,
                                                    String serverName,
                                                    String serviceOperationName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
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
    public LicenseHandler getLicenseHandler(String userId,
                                            String serverName,
                                            String serviceOperationName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

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
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public LikeHandler getLikeHandler(String userId,
                                      String serverName,
                                      String serviceOperationName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
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
    public LocationHandler getLocationHandler(String userId,
                                              String serverName,
                                              String serviceOperationName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

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
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public MeaningHandler getMeaningHandler(String userId,
                                            String serverName,
                                            String serviceOperationName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

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
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public NoteHandler getNoteHandler(String userId,
                                      String serverName,
                                      String serviceOperationName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

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
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public NoteLogHandler getNoteLogHandler(String userId,
                                            String serverName,
                                            String serviceOperationName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

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
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public RatingHandler getRatingHandler(String userId,
                                          String serverName,
                                          String serviceOperationName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

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
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public ReferenceableHandler getReferenceableHandler(String userId,
                                                        String serverName,
                                                        String serviceOperationName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

        if (instance != null)
        {
            return instance.getReferenceableHandler();
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
    public RelatedMediaHandler getRelatedMediaHandler(String userId,
                                                      String serverName,
                                                      String serviceOperationName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

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
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public RelationalDataHandler getRelationalDataHandler(String userId,
                                                          String serverName,
                                                          String serviceOperationName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

        if (instance != null)
        {
            return instance.getRelationalDataHandler();
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
    public SchemaTypeHandler getSchemaTypeHandler(String userId,
                                                  String serverName,
                                                  String serviceOperationName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

        if (instance != null)
        {
            return instance.getSchemaTypeHandler();
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
    public SoftwareServerCapabilityHandler getSoftwareServerCapabilityHandler(String userId,
                                                                              String serverName,
                                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

        if (instance != null)
        {
            return instance.getSoftwareServerCapabilityHandler();
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
    public ValidValuesHandler getValidValuesHandler(String userId,
                                                    String serverName,
                                                    String serviceOperationName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

        if (instance != null)
        {
            return instance.getValidValuesHandler();
        }

        return null;
    }
}
