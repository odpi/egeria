/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetsearch.admin.handlers;

import org.odpi.openmetadata.accessservices.assetcatalog.AssetCatalog;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.userinterface.common.ffdc.DependantServerNotAvailableException;
import org.odpi.openmetadata.userinterface.common.ffdc.UserInterfaceErrorCode;
import org.odpi.openmetadata.viewservices.assetsearch.admin.registration.AssetSearchViewRegistration;
import org.odpi.openmetadata.viewservices.assetsearch.admin.serviceinstances.AssetSearchViewServicesInstance;


/**
 * AssetSearchViewRegistration registers the view service with the UI Server administration services.
 * This registration must be driven once at server start up. The UI Server administration services
 * then use this registration information as confirmation that there is an implementation of this
 * view service in the server and it can be configured and used.
 */
public class AssetSearchViewInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public AssetSearchViewInstanceHandler() {
        super(ViewServiceDescription.ASSET_SEARCH.getViewServiceName());
        AssetSearchViewRegistration.registerViewService();
    }
    /**
     * Return the Asset Search view's official view Service Name
     *
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return String the service name
     * @throws DependantServerNotAvailableException no available instance for the requested server
     */
    public String  getViewServiceName(String serverName, String userId, String serviceOperationName) throws DependantServerNotAvailableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        AssetSearchViewServicesInstance instance = (AssetSearchViewServicesInstance)
                super.getServerServiceInstance(userId,
                        serverName,
                        serviceOperationName);

        if (instance != null) {
            return instance.getViewServiceName();
        } else {
            final String methodName = "getViewServiceName";

            UserInterfaceErrorCode errorCode    = UserInterfaceErrorCode.SERVICE_NOT_INITIALIZED;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, ViewServiceDescription.SUBJECT_AREA.getViewServiceName(),methodName);

            throw new DependantServerNotAvailableException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction()
                    );
        }
    }

    /**
     * This serverName has an associated metadata server. This call returns that metadata servers's name.
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return String Metadata server name
     * @throws DependantServerNotAvailableException Metadata server uncontactable
     */
    public String getMetadataServerName(String serverName, String userId, String serviceOperationName) throws DependantServerNotAvailableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        AssetSearchViewServicesInstance instance = (AssetSearchViewServicesInstance)
                super.getServerServiceInstance(userId,
                        serverName,
                        serviceOperationName);

        if (instance != null) {
            return instance.getMetadataServerName();
        } else {
            final String methodName = "getMetadataServerURL";

            UserInterfaceErrorCode errorCode   = UserInterfaceErrorCode.SERVICE_NOT_INITIALIZED;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, ViewServiceDescription.SUBJECT_AREA.getViewServiceName(),methodName);

            throw new DependantServerNotAvailableException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }
    /**
     * This serverName has an associated metadata server. This call returns that metadata servers's URL.
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return String Metadata server URL
     * @throws DependantServerNotAvailableException Metadata server uncontactable
     */
    public String getMetadataServerURL(String serverName, String userId, String serviceOperationName ) throws DependantServerNotAvailableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        AssetSearchViewServicesInstance instance = (AssetSearchViewServicesInstance)
                super.getServerServiceInstance(userId,
                        serverName,
                        serviceOperationName);

        if (instance != null) {
            return instance.getMetadataServerURL();
        } else {
            final String methodName = "getMetadataServerURL";

            UserInterfaceErrorCode errorCode    = UserInterfaceErrorCode.SERVICE_NOT_INITIALIZED;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, ViewServiceDescription.SUBJECT_AREA.getViewServiceName(),methodName);

            throw new DependantServerNotAvailableException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }


    /**
     * Get the asset search client
     * @param serverName local UI server name
     * @param userId user id
     * @param serviceOperationName service operation name
     * @return asset search client
     */
    public AssetCatalog getAssetCatalog(String serverName, String userId, String serviceOperationName) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, DependantServerNotAvailableException {
        AssetSearchViewServicesInstance instance = getAssetSearchViewServicesInstance(userId,serverName,serviceOperationName);
        return instance.getAssetCatalogClient();
    }
    /**
     *
     * @param userId local server userid
     * @param serverName name of the server that the request is for
     * @param serviceOperationName service operation - usually the top level rest call
     * @return OpenLineageViewServicesInstance instance for this tenant to use.
     * @throws InvalidParameterException
     * @throws PropertyServerException
     * @throws UserNotAuthorizedException
     * @throws DependantServerNotAvailableException
     */
    private AssetSearchViewServicesInstance getAssetSearchViewServicesInstance(String userId, String serverName, String serviceOperationName)
            throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException,
            DependantServerNotAvailableException {
        AssetSearchViewServicesInstance instance = (AssetSearchViewServicesInstance)
                super.getServerServiceInstance(userId,
                        serverName,
                        serviceOperationName);

        if (instance == null) {

            final String methodName = "getAssetSearchViewServicesInstance";
                
            UserInterfaceErrorCode errorCode = UserInterfaceErrorCode.SERVICE_NOT_INITIALIZED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, ViewServiceDescription.SUBJECT_AREA.getViewServiceName(), methodName);

            throw new DependantServerNotAvailableException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        return instance;
    }
    
}
