/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ConnectionElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ConnectorTypeElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.EndpointElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.FileSystemElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SchemaAttributeElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SchemaTypeElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SoftwareCapabilityElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ValidValueElement;


class DataManagerInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    DataManagerInstanceHandler()
    {
        super(AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceFullName());

        DataManagerOMASRegistration.registerAccessService();
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
    ElementStubConverter<ElementStub> getElementStubConverter(String userId,
                                                              String serverName,
                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        DataManagerServicesInstance instance = (DataManagerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                           serverName,
                                                                                                           serviceOperationName);

        if (instance != null)
        {
            return instance.getElementStubConverter();
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
    SoftwareCapabilityHandler<SoftwareCapabilityElement> getSoftwareServerCapabilityHandler(String userId,
                                                                                            String serverName,
                                                                                            String serviceOperationName) throws InvalidParameterException,
                                                                                                                                UserNotAuthorizedException,
                                                                                                                                PropertyServerException
    {
        DataManagerServicesInstance instance = (DataManagerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                           serverName,
                                                                                                           serviceOperationName);

        if (instance != null)
        {
            return instance.getDataManagerIntegratorHandler();
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
    RelationalDataHandler<DatabaseElement,
                          DatabaseSchemaElement,
                          DatabaseTableElement,
                          DatabaseViewElement,
                          DatabaseColumnElement,
                          SchemaTypeElement> getRelationalDataHandler(String userId,
                                                                      String serverName,
                                                                      String serviceOperationName) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        DataManagerServicesInstance instance = (DataManagerServicesInstance)super.getServerServiceInstance(userId,
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
    FilesAndFoldersHandler<FileSystemElement,
                                  FileFolderElement,
                                  DataFileElement> getFilesAndFoldersHandler(String userId,
                                                                             String serverName,
                                                                             String serviceOperationName) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException
    {

        DataManagerServicesInstance instance = (DataManagerServicesInstance)super.getServerServiceInstance(userId,
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
    AssetHandler<TopicElement> getTopicHandler(String userId,
                                               String serverName,
                                               String serviceOperationName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {

        DataManagerServicesInstance instance = (DataManagerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                           serverName,
                                                                                                           serviceOperationName);

        if (instance != null)
        {
            return instance.getTopicHandler();
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
    EventTypeHandler<EventTypeElement> getEventTypeHandler(String userId,
                                                           String serverName,
                                                           String serviceOperationName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {

        DataManagerServicesInstance instance = (DataManagerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                           serverName,
                                                                                                           serviceOperationName);

        if (instance != null)
        {
            return instance.getEventTypeHandler();
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
    AssetHandler<APIElement> getAPIHandler(String userId,
                                           String serverName,
                                           String serviceOperationName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {

        DataManagerServicesInstance instance = (DataManagerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                           serverName,
                                                                                                           serviceOperationName);

        if (instance != null)
        {
            return instance.getAPIHandler();
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
    APIOperationHandler<APIOperationElement> getAPIOperationHandler(String userId,
                                                                    String serverName,
                                                                    String serviceOperationName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {

        DataManagerServicesInstance instance = (DataManagerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                           serverName,
                                                                                                           serviceOperationName);

        if (instance != null)
        {
            return instance.getAPIOperationHandler();
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
    APIParameterListHandler<APIParameterListElement> getAPIParameterListHandler(String userId,
                                                                                String serverName,
                                                                                String serviceOperationName) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException
    {

        DataManagerServicesInstance instance = (DataManagerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                           serverName,
                                                                                                           serviceOperationName);

        if (instance != null)
        {
            return instance.getAPIParameterListHandler();
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
    AssetHandler<FormElement> getFormHandler(String userId,
                                             String serverName,
                                             String serviceOperationName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {

        DataManagerServicesInstance instance = (DataManagerServicesInstance) super.getServerServiceInstance(userId,
                                                                                                            serverName,
                                                                                                            serviceOperationName);

        if (instance != null)
        {
            return instance.getFormHandler();
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
    AssetHandler<ReportElement> getReportHandler(String userId,
                                                 String serverName,
                                                 String serviceOperationName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {

        DataManagerServicesInstance instance = (DataManagerServicesInstance) super.getServerServiceInstance(userId,
                                                                                                            serverName,
                                                                                                            serviceOperationName);

        if (instance != null)
        {
            return instance.getReportHandler();
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
    AssetHandler<QueryElement> getQueryHandler(String userId,
                                               String serverName,
                                               String serviceOperationName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {

        DataManagerServicesInstance instance = (DataManagerServicesInstance) super.getServerServiceInstance(userId,
                                                                                                            serverName,
                                                                                                            serviceOperationName);

        if (instance != null)
        {
            return instance.getQueryHandler();
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
    DisplayDataContainerHandler<DataContainerElement, SchemaTypeElement> getDisplayDataContainerHandler(String userId,
                                                                                                        String serverName,
                                                                                                        String serviceOperationName) throws InvalidParameterException,
                                                                                                                                            UserNotAuthorizedException,
                                                                                                                                            PropertyServerException
    {

        DataManagerServicesInstance instance = (DataManagerServicesInstance) super.getServerServiceInstance(userId,
                                                                                                            serverName,
                                                                                                            serviceOperationName);

        if (instance != null)
        {
            return instance.getDisplayDataContainerHandler();
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

        DataManagerServicesInstance instance = (DataManagerServicesInstance) super.getServerServiceInstance(userId,
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
    SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> getSchemaAttributeHandler(String userId,
                                                                                                String serverName,
                                                                                                String serviceOperationName) throws InvalidParameterException,
                                                                                                                                    UserNotAuthorizedException,
                                                                                                                                    PropertyServerException
    {

        DataManagerServicesInstance instance = (DataManagerServicesInstance) super.getServerServiceInstance(userId,
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
    ConnectionHandler<ConnectionElement> getConnectionHandler(String userId,
                                                              String serverName,
                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {

        DataManagerServicesInstance instance = (DataManagerServicesInstance) super.getServerServiceInstance(userId,
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
    ConnectorTypeHandler<ConnectorTypeElement> getConnectorTypeHandler(String userId,
                                                                       String serverName,
                                                                       String serviceOperationName) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {

        DataManagerServicesInstance instance = (DataManagerServicesInstance) super.getServerServiceInstance(userId,
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
    EndpointHandler<EndpointElement> getEndpointHandler(String userId,
                                                        String serverName,
                                                        String serviceOperationName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {

        DataManagerServicesInstance instance = (DataManagerServicesInstance) super.getServerServiceInstance(userId,
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
    ReferenceableHandler<RelatedElementStub> getRelatedElementHandler(String userId,
                                                                      String serverName,
                                                                      String serviceOperationName) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {

        DataManagerServicesInstance instance = (DataManagerServicesInstance) super.getServerServiceInstance(userId,
                                                                                                            serverName,
                                                                                                            serviceOperationName);

        if (instance != null)
        {
            return instance.getRelatedElementHandler();
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
    ValidValuesHandler<ValidValueSetElement> getValidValuesSetHandler(String userId,
                                                                      String serverName,
                                                                      String serviceOperationName) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        DataManagerServicesInstance instance = (DataManagerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                           serverName,
                                                                                                           serviceOperationName);

        if (instance != null)
        {
            return instance.getValidValuesSetHandler();
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
    ValidValuesHandler<ValidValueElement> getValidValueHandler(String userId,
                                                               String serverName,
                                                               String serviceOperationName) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        DataManagerServicesInstance instance = (DataManagerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                           serverName,
                                                                                                           serviceOperationName);

        if (instance != null)
        {
            return instance.getValidValuesHandler();
        }

        return null;
    }
}
