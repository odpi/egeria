/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.server;

import org.odpi.openmetadata.accessservices.assetowner.metadataelements.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.DataField;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;


/**
 * AssetOwnerInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the AssetOwnerAdmin class.
 */
class AssetOwnerInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    AssetOwnerInstanceHandler()
    {
        super(AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceFullName());

        AssetOwnerRegistration.registerAccessService();
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
    ReferenceableHandler<ReferenceableElement> getReferenceableHandler(String userId,
                                                                       String serverName,
                                                                       String serviceOperationName) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        AssetOwnerServicesInstance instance = (AssetOwnerServicesInstance)super.getServerServiceInstance(userId,
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
    AssetHandler<AssetElement> getAssetHandler(String userId,
                                               String serverName,
                                               String serviceOperationName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        AssetOwnerServicesInstance instance = (AssetOwnerServicesInstance)super.getServerServiceInstance(userId,
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
    SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> getSchemaAttributeHandler(String userId,
                                                                                                String serverName,
                                                                                                String serviceOperationName) throws InvalidParameterException,
                                                                                                                                    UserNotAuthorizedException,
                                                                                                                                    PropertyServerException
    {
        AssetOwnerServicesInstance instance = (AssetOwnerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                         serverName,
                                                                                                         serviceOperationName);

        if (instance != null)
        {
            return instance.getSchemaAttributeHandler();
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
    SchemaTypeHandler<SchemaTypeElement> getSchemaTypeHandler(String userId,
                                                              String serverName,
                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        AssetOwnerServicesInstance instance = (AssetOwnerServicesInstance)super.getServerServiceInstance(userId,
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
    FilesAndFoldersHandler<FileSystemElement, FolderElement, FileElement> getFilesAndFoldersHandler(String userId,
                                                                                                    String serverName,
                                                                                                    String serviceOperationName) throws InvalidParameterException,
                                                                                                                                        UserNotAuthorizedException,
                                                                                                                                        PropertyServerException
    {
        AssetOwnerServicesInstance instance = (AssetOwnerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                         serverName,
                                                                                                         serviceOperationName);

        if (instance != null)
        {
            return instance.getFilesAndFoldersHandler();
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
    ValidValuesHandler<ValidValueElement> getValidValuesHandler(String userId,
                                                                String serverName,
                                                                String serviceOperationName) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        AssetOwnerServicesInstance instance = (AssetOwnerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                         serverName,
                                                                                                         serviceOperationName);

        if (instance != null)
        {
            return instance.getValidValuesHandler();
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
    public DiscoveryAnalysisReportHandler<DiscoveryAnalysisReport> getDiscoveryAnalysisReportHandler(String userId,
                                                                                                     String serverName,
                                                                                                     String serviceOperationName) throws InvalidParameterException,
                                                                                                                                         UserNotAuthorizedException,
                                                                                                                                         PropertyServerException
    {

        AssetOwnerServicesInstance instance = (AssetOwnerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                         serverName,
                                                                                                         serviceOperationName);

        if (instance != null)
        {
            return instance.getDiscoveryAnalysisReportHandler();
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
    public AnnotationHandler<Annotation> getAnnotationHandler(String userId,
                                                              String serverName,
                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        AssetOwnerServicesInstance instance = (AssetOwnerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                         serverName,
                                                                                                         serviceOperationName);

        if (instance != null)
        {
            return instance.getAnnotationHandler();
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
    public DataFieldHandler<DataField> getDataFieldHandler(String userId,
                                                           String serverName,
                                                           String serviceOperationName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        AssetOwnerServicesInstance instance = (AssetOwnerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                         serverName,
                                                                                                         serviceOperationName);

        if (instance != null)
        {
            return instance.getDataFieldHandler();
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
    public ConnectionHandler<ConnectionElement> getConnectionHandler(String userId,
                                                                     String serverName,
                                                                     String serviceOperationName) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        AssetOwnerServicesInstance instance = (AssetOwnerServicesInstance)super.getServerServiceInstance(userId,
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
    public ConnectorTypeHandler<ConnectorTypeElement> getConnectorTypeHandler(String userId,
                                                                              String serverName,
                                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        AssetOwnerServicesInstance instance = (AssetOwnerServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

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
    public EndpointHandler<EndpointElement> getEndpointHandler(String userId,
                                                               String serverName,
                                                               String serviceOperationName) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        AssetOwnerServicesInstance instance = (AssetOwnerServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getEndpointHandler();
        }

        return null;
    }




    /**
     * Retrieve the specific converter for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return converter for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    ReferenceableHandler<RelatedElement> getRelatedElementHandler(String userId,
                                                                  String serverName,
                                                                  String serviceOperationName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        AssetOwnerServicesInstance instance = (AssetOwnerServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getRelatedElementHandler();
        }

        return null;
    }


    /**
     * Retrieve a specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    ExternalReferenceHandler<ExternalReferenceElement> getExternalReferencesHandler(String userId,
                                                                                    String serverName,
                                                                                    String serviceOperationName) throws InvalidParameterException,
                                                                                                                        UserNotAuthorizedException,
                                                                                                                        PropertyServerException
    {
        AssetOwnerServicesInstance instance = (AssetOwnerServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getExternalReferencesHandler();
        }

        return null;
    }

    

    /**
     * Retrieve a specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    LicenseHandler<LicenseTypeElement> getLicenseTypeHandler(String userId,
                                                             String serverName,
                                                             String serviceOperationName) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        AssetOwnerServicesInstance instance = (AssetOwnerServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getLicenseTypeHandler();
        }

        return null;
    }


    /**
     * Retrieve a specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    CertificationHandler<CertificationTypeElement> getCertificationTypeHandler(String userId,
                                                                               String serverName,
                                                                               String serviceOperationName) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        AssetOwnerServicesInstance instance = (AssetOwnerServicesInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getCertificationTypeHandler();
        }

        return null;
    }


}
