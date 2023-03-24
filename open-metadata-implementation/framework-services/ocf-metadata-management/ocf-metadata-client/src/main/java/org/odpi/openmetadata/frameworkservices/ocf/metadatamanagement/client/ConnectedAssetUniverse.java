/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest.AssetResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;


/**
 * ConnectedAssetUniverse is the OMAS client library implementation of the Open Connector Framework
 * (OCF) AssetUniverse object.  AssetUniverse provides read-only access to the properties known
 * about an asset.  ConnectedAssetUniverse configures AssetUniverse (and its dependent objects)
 * with the information necessary to populate the AssetUniverse contents from the open metadata
 * repositories.
 * 
 * All of ConnectedAssetUniverse's work is done in the constructors.  They extract basic information
 * about the asset and push objects to the super class to retrieve the more detailed properties.
 * These properties are only retrieved on demand.
 */
public class ConnectedAssetUniverse extends AssetUniverse
{
    private static final long    serialVersionUID = 1L;

    private final int MAX_CACHE_SIZE = 100;

    private static final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private static final RESTExceptionHandler    restExceptionHandler    = new RESTExceptionHandler();

    /**
     * Private constructor used by the create() static factory methods for instantiation
     * of ConnectedAssetUniverse objects.
     *
     * @param serviceName calling service
     * @param remoteServerName  name of the server.
     * @param omasServerURL  url used to call the server.
     * @param userId  userId of user making request.
     * @param assetGUID  unique id for asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem retrieving the asset properties from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private ConnectedAssetUniverse(AssetResponse    assetResponse,
                                   OCFRESTClient    restClient,
                                   String           serviceName,
                                   String           remoteServerName,
                                   String           omasServerURL,
                                   String           userId,
                                   String           assetGUID) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        super(assetResponse.getAsset());

        this.processAssetResponse(serviceName,
                                  remoteServerName,
                                  omasServerURL,
                                  userId,
                                  assetGUID,
                                  restClient,
                                  assetResponse);
    }

    /**
     * Static factory method used for creating a ConnectedAssetUniverse object
     * without authentication.
     *
     * @param serviceName calling service
     * @param remoteServerName  name of the server.
     * @param omasServerURL  url used to call the server.
     * @param userId  userId of user making request.
     * @param assetGUID  unique id for asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem retrieving the asset properties from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public static ConnectedAssetUniverse create(String      serviceName,
                                                String      remoteServerName,
                                                String      omasServerURL,
                                                String      userId,
                                                String      assetGUID) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        OCFRESTClient restClient = new OCFRESTClient(remoteServerName, omasServerURL);
        AssetResponse assetResponse = getAssetSummary(serviceName, remoteServerName, omasServerURL, restClient, userId, assetGUID);

        return new ConnectedAssetUniverse(assetResponse,
                                          restClient,
                                          serviceName,
                                          remoteServerName,
                                          omasServerURL,
                                          userId,
                                          assetGUID);
    }

    /**
     * Static factory method used for creating a ConnectedAssetUniverse object where a userId and password
     * of the local calling server are embedded in the HTTP request.
     *
     * @param serviceName calling service
     * @param remoteServerName  name of the server.
     * @param localServerUserId userId of the local server.
     * @param localServerPassword password of the local server.
     * @param omasServerURL  url used to call the server.
     * @param userId  userId of user making request.
     * @param assetGUID  unique id for asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem retrieving the asset properties from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public static ConnectedAssetUniverse create(String      serviceName,
                                                String      remoteServerName,
                                                String      localServerUserId,
                                                String      localServerPassword,
                                                String      omasServerURL,
                                                String      userId,
                                                String      assetGUID) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        OCFRESTClient restClient = new OCFRESTClient(remoteServerName, omasServerURL, localServerUserId, localServerPassword);
        AssetResponse assetResponse = getAssetSummary(serviceName, remoteServerName, omasServerURL, restClient, userId, assetGUID);

        return new ConnectedAssetUniverse(assetResponse,
                                          restClient,
                                          serviceName,
                                          remoteServerName,
                                          omasServerURL,
                                          userId,
                                          assetGUID);
    }

    /**
     * Static factory method used for creating a ConnectedAssetUniverse object.
     *
     * @param serviceName calling service
     * @param remoteServerName  name of the server.
     * @param omasServerURL  url used to call the server.
     * @param userId  userId of user making request.
     * @param assetGUID  unique id for asset.
     * @param restClient client for calling rest APIs
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem retrieving the asset properties from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public static ConnectedAssetUniverse create(String          serviceName,
                                                String          remoteServerName,
                                                String          omasServerURL,
                                                String          userId,
                                                String          assetGUID,
                                                OCFRESTClient   restClient) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        AssetResponse assetResponse = getAssetSummary(serviceName, remoteServerName, omasServerURL, restClient, userId, assetGUID);

        return new ConnectedAssetUniverse(assetResponse,
                                          restClient,
                                          serviceName,
                                          remoteServerName,
                                          omasServerURL,
                                          userId,
                                          assetGUID);
    }

    /**
     * Static factory method used by EgeriaConnectedAssetProperties.refresh() to instantiate a ConnectedAssetUniverse object
     * with no authentication information being attached to the HTTP requests.
     *
     * @param serviceName calling service
     * @param remoteServerName  name of the server.
     * @param omasServerURL  url used to call the server.
     * @param userId  userId of user making request.
     * @param assetGUID  unique id for asset.
     * @param connectionGUID  unique id for connection used to access asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem retrieving the asset properties from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public static ConnectedAssetUniverse create(String      serviceName,
                                                String      remoteServerName,
                                                String      omasServerURL,
                                                String      userId,
                                                String      assetGUID,
                                                String      connectionGUID) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        OCFRESTClient restClient = new OCFRESTClient(remoteServerName, omasServerURL);
        AssetResponse assetResponse = getConnectedAssetSummary(serviceName, remoteServerName, omasServerURL, restClient, userId, assetGUID, connectionGUID);

        return new ConnectedAssetUniverse(assetResponse,
                                          restClient,
                                          serviceName,
                                          remoteServerName,
                                          omasServerURL,
                                          userId,
                                          assetGUID);
    }

    /**
     * Static factory method used by EgeriaConnectedAssetProperties.refresh() to instantiate a ConnectedAssetUniverse object
     * where a usedId and password of the local calling server are embedded in the HTTP request.
     *
     * @param serviceName calling service
     * @param remoteServerName  name of the server.
     * @param localServerUserId userId of the local server.
     * @param localServerPassword password of the local server.
     * @param omasServerURL  url used to call the server.
     * @param userId  userId of user making request.
     * @param assetGUID  unique id for asset.
     * @param connectionGUID  unique id for connection used to access asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem retrieving the asset properties from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public static ConnectedAssetUniverse create(String      serviceName,
                                                String      remoteServerName,
                                                String      localServerUserId,
                                                String      localServerPassword,
                                                String      omasServerURL,
                                                String      userId,
                                                String      assetGUID,
                                                String      connectionGUID) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        OCFRESTClient restClient = new OCFRESTClient(remoteServerName, omasServerURL, localServerUserId, localServerPassword);
        AssetResponse assetResponse = getConnectedAssetSummary(serviceName, remoteServerName, omasServerURL, restClient, userId, assetGUID, connectionGUID);

        return new ConnectedAssetUniverse(assetResponse,
                                          restClient,
                                          serviceName,
                                          remoteServerName,
                                          omasServerURL,
                                          userId,
                                          assetGUID);
    }

    /**
     * Extract the returned properties from AssetResponse and set up the superclass.
     *
     * @param serviceName calling service
     * @param remoteServerName server to call.
     * @param omasServerURL url root of the remote server.
     * @param userId userId of calling user.
     * @param assetGUID unique identifier of the asset.
     * @param restClient client to issue REST call.
     * @param assetResponse response from the server covering the basic asset properties and the
     *                      counts of objects attached to it.
     */
    private void  processAssetResponse(String           serviceName,
                                       String           remoteServerName,
                                       String           omasServerURL,
                                       String           userId,
                                       String           assetGUID,
                                       OCFRESTClient    restClient,
                                       AssetResponse    assetResponse)
    {
        if (assetResponse.getExternalIdentifierCount() > 0)
        {
            super.externalIdentifiers = new ConnectedExternalIdentifiers(serviceName,
                                                                         remoteServerName,
                                                                         userId,
                                                                         omasServerURL,
                                                                         assetGUID,
                                                                         assetResponse.getExternalIdentifierCount(),
                                                                         MAX_CACHE_SIZE,
                                                                         restClient);
        }

        if (assetResponse.getRelatedMediaReferenceCount() > 0)
        {
            super.relatedMediaReferences = new ConnectedRelatedMediaReferences(serviceName,
                                                                               remoteServerName,
                                                                               userId,
                                                                               omasServerURL,
                                                                               assetGUID,
                                                                               assetResponse.getRelatedMediaReferenceCount(),
                                                                               MAX_CACHE_SIZE,
                                                                               restClient);
        }

        if (assetResponse.getNoteLogsCount() > 0)
        {
            super.noteLogs = new ConnectedNoteLogs(serviceName,
                                                   remoteServerName,
                                                   userId,
                                                   omasServerURL,
                                                   assetGUID,
                                                   assetResponse.getNoteLogsCount(),
                                                   MAX_CACHE_SIZE,
                                                   restClient);
        }

        if (assetResponse.getExternalReferencesCount() > 0)
        {
            super.externalReferences = new ConnectedExternalReferences(serviceName,
                                                                       remoteServerName,
                                                                       userId,
                                                                       omasServerURL,
                                                                       assetGUID,
                                                                       assetResponse.getExternalReferencesCount(),
                                                                       MAX_CACHE_SIZE,
                                                                       restClient);
        }

        if (assetResponse.getConnectionCount() > 0)
        {
            super.connections = new ConnectedConnections(serviceName,
                                                         remoteServerName,
                                                         userId,
                                                         omasServerURL,
                                                         assetGUID,
                                                         assetResponse.getConnectionCount(),
                                                         MAX_CACHE_SIZE,
                                                         restClient);
        }

        if (assetResponse.getLicenseCount() > 0)
        {
            super.licenses = new ConnectedLicenses(serviceName,
                                                   remoteServerName,
                                                   userId,
                                                   omasServerURL,
                                                   assetGUID,
                                                   assetResponse.getLicenseCount(),
                                                   MAX_CACHE_SIZE,
                                                   restClient);
        }

        if (assetResponse.getCertificationCount() > 0)
        {
            super.certifications = new ConnectedCertifications(serviceName,
                                                               remoteServerName,
                                                               userId,
                                                               omasServerURL,
                                                               assetGUID,
                                                               assetResponse.getCertificationCount(),
                                                               MAX_CACHE_SIZE,
                                                               restClient);
        }

        super.feedback = new ConnectedFeedback(serviceName,
                                               remoteServerName,
                                               userId,
                                               omasServerURL,
                                               assetGUID,
                                               assetResponse.getCommentCount(),
                                               assetResponse.getLikeCount(),
                                               assetResponse.getRatingsCount(),
                                               assetResponse.getInformalTagCount(),
                                               MAX_CACHE_SIZE,
                                               restClient);

        if (assetResponse.getKnownLocationsCount() > 0)
        {
            super.knownLocations = new ConnectedLocations(serviceName,
                                                          remoteServerName,
                                                          userId,
                                                          omasServerURL,
                                                          assetGUID,
                                                          assetResponse.getKnownLocationsCount(),
                                                          MAX_CACHE_SIZE,
                                                          restClient);
        }

        super.lineage = new ConnectedAssetLineage(serviceName,
                                                  remoteServerName,
                                                  userId,
                                                  omasServerURL,
                                                  assetGUID,
                                                  MAX_CACHE_SIZE,
                                                  restClient);

        if (assetResponse.getRelatedAssetCount() > 0)
        {
            super.relatedAssets = new ConnectedRelatedAssets(serviceName,
                                                             remoteServerName,
                                                             userId,
                                                             omasServerURL,
                                                             assetGUID,
                                                             assetResponse.getRelatedAssetCount(),
                                                             MAX_CACHE_SIZE,
                                                             restClient);
        }

        if (assetResponse.getSchemaType() != null)
        {
            super.schema = this.getSchemaType(serviceName,
                                              remoteServerName,
                                              omasServerURL,
                                              userId,
                                              assetResponse.getSchemaType(),
                                              restClient);
        }
    }

    /**
     * Returns the basic information about the asset.  The connection guid allows the short description for the
     * asset to be filled out.
     *
     * @param serviceName calling service
     * @param remoteServerName  name of the server.
     * @param omasServerURL  url used to call the server.
     * @param restClient client to call REST API
     * @param userId     String   userId of user making request.
     * @param assetGUID  String   unique id for asset.
     * @param connectionGUID  unique id for connection used to access asset.
     *
     * @return a bean with the basic properties about the asset.
     * @throws InvalidParameterException the asset GUID is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private static AssetResponse getConnectedAssetSummary(String        serviceName,
                                                          String        remoteServerName,
                                                          String        omasServerURL,
                                                          OCFRESTClient restClient,
                                                          String        userId,
                                                          String        assetGUID,
                                                          String        connectionGUID) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String   methodName = "getConnectedAssetSummary";
        final String   urlTemplate = "/servers/{0}/open-metadata/framework-services/{1}/connected-asset/users/{2}/assets/{3}/via-connection/{4}";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, remoteServerName, methodName);

        AssetResponse  restResult = null;

        try
        {
            restResult = restClient.callOCFAssetGetRESTCall(methodName,
                                                            omasServerURL + urlTemplate,
                                                            remoteServerName,
                                                            serviceName,
                                                            userId,
                                                            assetGUID,
                                                            connectionGUID);

            restExceptionHandler.detectAndThrowInvalidParameterException(restResult);
            restExceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
            restExceptionHandler.detectAndThrowPropertyServerException(restResult);
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            restExceptionHandler.handleUnexpectedException(error, methodName, remoteServerName, omasServerURL);
        }

        return restResult;
    }


    /**
     * Returns the basic information about the asset.  Note shortDescription is null in the returned asset because
     * there is no linked connection object.
     *
     * @param serviceName calling service
     * @param remoteServerName  name of the server.
     * @param omasServerURL  url used to call the server.
     * @param restClient client to call REST API
     * @param userId     String   userId of user making request.
     * @param assetGUID  String   unique id for asset.
     *
     * @return a bean with the basic properties about the asset.
     * @throws InvalidParameterException the asset GUID is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private static AssetResponse getAssetSummary(String        serviceName,
                                                 String        remoteServerName,
                                                 String        omasServerURL,
                                                 OCFRESTClient restClient,
                                                 String        userId,
                                                 String        assetGUID) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String   methodName = "getAssetSummary";
        final String   urlTemplate = "/servers/{0}/open-metadata/framework-services/{1}/connected-asset/users/{2}/assets/{3}";
        
        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, remoteServerName, methodName);

        AssetResponse  restResult = null;

        try
        {
            restResult = restClient.callOCFAssetGetRESTCall(methodName,
                                                            omasServerURL + urlTemplate,
                                                            remoteServerName,
                                                            serviceName,
                                                            userId,
                                                            assetGUID);

            restExceptionHandler.detectAndThrowInvalidParameterException(restResult);
            restExceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
            restExceptionHandler.detectAndThrowPropertyServerException(restResult);
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            restExceptionHandler.handleUnexpectedException(error, methodName, remoteServerName, omasServerURL);
        }

        return restResult;
    }


    /**
     * Based on the type of bean passed, return the appropriate type of AssetSchemaType.
     *
     * @param serviceName calling service
     * @param remoteServerName  name of the server.
     * @param omasServerURL  url used to call the server.
     * @param userId     String   userId of user making request.
     * @param bean schema type bean that has the properties for the schema type.
     * @param restClient client to call REST API

     * @return subtype of SchemaType
     */
    private SchemaType getSchemaType(String        serviceName,
                                     String        remoteServerName,
                                     String        omasServerURL,
                                     String        userId,
                                     SchemaType    bean,
                                     OCFRESTClient restClient)
    {
        if (bean == null)
        {
            return null;
        }
        else if (bean instanceof ComplexSchemaType)
        {
            return new ConnectedNestedSchemaType((ComplexSchemaType)bean,
                                                 serviceName,
                                                 remoteServerName,
                                                 omasServerURL,
                                                 userId,
                                                 MAX_CACHE_SIZE,
                                                 restClient);
        }
        else if (bean instanceof APISchemaType)
        {
            return new ConnectedDeployedAPISchemaType((APISchemaType) bean,
                                                      serviceName,
                                                      remoteServerName,
                                                      omasServerURL,
                                                      userId,
                                                      MAX_CACHE_SIZE,
                                                      restClient);
        }
        else
        {
            return bean;
        }
    }
}
