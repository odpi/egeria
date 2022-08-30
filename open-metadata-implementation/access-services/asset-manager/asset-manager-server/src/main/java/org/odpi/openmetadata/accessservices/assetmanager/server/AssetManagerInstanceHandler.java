/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.handlers.ConnectionExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.handlers.DataAssetExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.handlers.ExternalReferenceExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.handlers.GlossaryExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.handlers.ProcessExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.handlers.SchemaExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

class AssetManagerInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    AssetManagerInstanceHandler()
    {
        super(AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName());

        AssetManagerOMASRegistration.registerAccessService();
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
    SoftwareCapabilityHandler<SoftwareCapabilityElement> getAssetManagerHandler(String userId,
                                                                                String serverName,
                                                                                String serviceOperationName) throws InvalidParameterException,
                                                                                                                                UserNotAuthorizedException,
                                                                                                                                PropertyServerException
    {
        AssetManagerServicesInstance instance = (AssetManagerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                           serverName,
                                                                                                           serviceOperationName);

        if (instance != null)
        {
            return instance.getAssetManagerIntegratorHandler();
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
    ExternalIdentifierHandler<MetadataCorrelationHeader, ElementHeader> getExternalIdentifierHandler(String userId,
                                                                                                     String serverName,
                                                                                                     String serviceOperationName) throws InvalidParameterException,
                                                                                                                                         UserNotAuthorizedException,
                                                                                                                                         PropertyServerException
    {
        AssetManagerServicesInstance instance = (AssetManagerServicesInstance)super.getServerServiceInstance(userId,
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
    ConnectionExchangeHandler getConnectionExchangeHandler(String userId,
                                                           String serverName,
                                                           String serviceOperationName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        AssetManagerServicesInstance instance = (AssetManagerServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getConnectionExchangeHandler();
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
    DataAssetExchangeHandler getDataAssetExchangeHandler(String userId,
                                                         String serverName,
                                                         String serviceOperationName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        AssetManagerServicesInstance instance = (AssetManagerServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getDataAssetExchangeHandler();
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
    ExternalReferenceExchangeHandler getExternalReferenceExchangeHandler(String userId,
                                                                         String serverName,
                                                                         String serviceOperationName) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        AssetManagerServicesInstance instance = (AssetManagerServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getExternalReferenceExchangeHandler();
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
    GlossaryExchangeHandler getGlossaryExchangeHandler(String userId,
                                                       String serverName,
                                                       String serviceOperationName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        AssetManagerServicesInstance instance = (AssetManagerServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGlossaryExchangeHandler();
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
    ProcessExchangeHandler getProcessExchangeHandler(String userId,
                                                     String serverName,
                                                     String serviceOperationName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        AssetManagerServicesInstance instance = (AssetManagerServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getProcessExchangeHandler();
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
    SchemaExchangeHandler getSchemaExchangeHandler(String userId,
                                                   String serverName,
                                                   String serviceOperationName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        AssetManagerServicesInstance instance = (AssetManagerServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getSchemaExchangeHandler();
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
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    AssetHandler<GovernanceActionProcessElement> getGovernanceActionProcessHandler(String userId,
                                                                                   String serverName,
                                                                                   String serviceOperationName) throws InvalidParameterException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       PropertyServerException
    {
        AssetManagerServicesInstance instance = (AssetManagerServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceActionProcessHandler();
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
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    GovernanceActionTypeHandler<GovernanceActionTypeElement> getGovernanceActionTypeHandler(String userId,
                                                                                            String serverName,
                                                                                            String serviceOperationName) throws InvalidParameterException,
                                                                                                                                UserNotAuthorizedException,
                                                                                                                                PropertyServerException
    {
        AssetManagerServicesInstance instance = (AssetManagerServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceActionTypeHandler();
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
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    GovernanceActionHandler<GovernanceActionElement> getGovernanceActionHandler(String userId,
                                                                                String serverName,
                                                                                String serviceOperationName) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException
    {
        AssetManagerServicesInstance instance = (AssetManagerServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceActionHandler();
        }

        return null;
    }
}
