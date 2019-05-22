/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.AssetResponse;
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
    private final int MAX_CACHE_SIZE = 100;

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private RESTExceptionHandler    restExceptionHandler    = new RESTExceptionHandler();


    /**
     * Constructor used by Asset Consumer OMAS for getExtendedProperties() with no authentication
     * information being attached to the HTTP requests.  The calling user of the specific
     * request flows as a property in the URL.
     *
     * @param remoteServerName  name of the server.
     * @param omasServerURL  url used to call the server.
     * @param userId  userId of user making request.
     * @param assetGUID  unique id for asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem retrieving the asset properties from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public ConnectedAssetUniverse(String   remoteServerName,
                                  String   omasServerURL,
                                  String   userId,
                                  String   assetGUID) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        super();

        RESTClient    restClient    = new RESTClient(remoteServerName, omasServerURL);
        AssetResponse assetResponse = this.getAssetSummary(remoteServerName, omasServerURL, restClient, userId, assetGUID);

        this.processAssetResponse(remoteServerName, omasServerURL, userId, assetGUID, restClient, assetResponse);
    }



    /**
     * Constructor used by Asset Consumer OMAS for getExtendedProperties() where a userId and password
     * of the local calling server are embedded in the HTTP requests.  The calling user of the specific
     * request flows as a property in the URL.
     *
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
    public ConnectedAssetUniverse(String   remoteServerName,
                                  String   localServerUserId,
                                  String   localServerPassword,
                                  String   omasServerURL,
                                  String   userId,
                                  String   assetGUID) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        super();

        RESTClient    restClient = new RESTClient(remoteServerName, omasServerURL, localServerUserId, localServerPassword);
        AssetResponse assetResponse = this.getAssetSummary(remoteServerName, omasServerURL, restClient, userId, assetGUID);

        this.processAssetResponse(remoteServerName, omasServerURL, userId, assetGUID, restClient, assetResponse);
    }


    /**
     * Constructor used by Connected Asset OMAS for related asset properties.
     *
     * @param remoteServerName  name of the server.
     * @param omasServerURL  url used to call the server.
     * @param userId  userId of user making request.
     * @param assetGUID  unique id for asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem retrieving the asset properties from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public ConnectedAssetUniverse(String      remoteServerName,
                                  String      omasServerURL,
                                  String      userId,
                                  String      assetGUID,
                                  RESTClient  restClient) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        super();

        AssetResponse assetResponse = this.getAssetSummary(remoteServerName, omasServerURL, restClient, userId, assetGUID);

        this.processAssetResponse(remoteServerName, omasServerURL, userId, assetGUID, restClient, assetResponse);
    }


    /**
     * Constructor used by ConnectedAssetProperties.refresh() with no authentication
     * information being attached to the HTTP requests.  The calling user of the specific
     * request flows as a property in the URL.
     *
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
    public ConnectedAssetUniverse(String   remoteServerName,
                                  String   omasServerURL,
                                  String   userId,
                                  String   assetGUID,
                                  String   connectionGUID) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        super();

        RESTClient restClient = new RESTClient(remoteServerName, omasServerURL);
        AssetResponse assetResponse = this.getConnectedAssetSummary(remoteServerName, omasServerURL, restClient, userId, assetGUID, connectionGUID);

        this.processAssetResponse(remoteServerName, omasServerURL, userId, assetGUID, restClient, assetResponse);
    }


    /**
     * Constructor used by ConnectedAssetProperties.refresh() with no authentication
     * information being attached to the HTTP requests.  The calling user of the specific
     * request flows as a property in the URL.
     *
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
    public ConnectedAssetUniverse(String   remoteServerName,
                                  String   localServerUserId,
                                  String   localServerPassword,
                                  String   omasServerURL,
                                  String   userId,
                                  String   assetGUID,
                                  String   connectionGUID) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        super();

        RESTClient restClient = new RESTClient(remoteServerName, omasServerURL, localServerUserId, localServerPassword);
        AssetResponse assetResponse = this.getConnectedAssetSummary(remoteServerName, omasServerURL, restClient, userId, assetGUID, connectionGUID);

        this.processAssetResponse(remoteServerName, omasServerURL, userId, assetGUID, restClient, assetResponse);
    }


    /**
     * Extract the returned properties from AssetResponse and set up the superclass.
     *
     * @param remoteServerName server to call.
     * @param omasServerURL url root of the remote server.
     * @param userId userId of calling user.
     * @param assetGUID unique identifier of the asset.
     * @param restClient client to issue REST call.
     * @param assetResponse response from the server covering the basic asset properties and the
     *                      counts of objects attached to it.
     */
    private void  processAssetResponse(String           remoteServerName,
                                       String           omasServerURL,
                                       String           userId,
                                       String           assetGUID,
                                       RESTClient       restClient,
                                       AssetResponse    assetResponse)
    {
        super.assetBean = assetResponse.getAsset();

        if (assetResponse.getExternalIdentifierCount() > 0)
        {
            super.externalIdentifiers = new ConnectedAssetExternalIdentifiers(remoteServerName,
                                                                              userId,
                                                                              omasServerURL,
                                                                              assetGUID,
                                                                              this,
                                                                              assetResponse.getExternalIdentifierCount(),
                                                                              MAX_CACHE_SIZE,
                                                                              restClient);
        }

        if (assetResponse.getRelatedMediaReferenceCount() > 0)
        {
            super.relatedMediaReferences = new ConnectedAssetRelatedMediaReferences(remoteServerName,
                                                                                    userId,
                                                                                    omasServerURL,
                                                                                    assetGUID,
                                                                                    this,
                                                                                    assetResponse.getRelatedMediaReferenceCount(),
                                                                                    MAX_CACHE_SIZE,
                                                                                    restClient);
        }

        if (assetResponse.getNoteLogsCount() > 0)
        {
            super.noteLogs = new ConnectedAssetNoteLogs(remoteServerName,
                                                        userId,
                                                        omasServerURL,
                                                        assetGUID,
                                                        this,
                                                        assetResponse.getNoteLogsCount(),
                                                        MAX_CACHE_SIZE,
                                                        restClient);
        }

        if (assetResponse.getExternalReferencesCount() > 0)
        {
            super.externalReferences = new ConnectedAssetExternalReferences(remoteServerName,
                                                                            userId,
                                                                            omasServerURL,
                                                                            assetGUID,
                                                                            this,
                                                                            assetResponse.getExternalReferencesCount(),
                                                                            MAX_CACHE_SIZE,
                                                                            restClient);
        }

        if (assetResponse.getConnectionCount() > 0)
        {
            super.connections = new ConnectedAssetConnections(remoteServerName,
                                                              userId,
                                                              omasServerURL,
                                                              assetGUID,
                                                              this,
                                                              assetResponse.getConnectionCount(),
                                                              MAX_CACHE_SIZE,
                                                              restClient);
        }

        if (assetResponse.getLicenseCount() > 0)
        {
            super.licenses = new ConnectedAssetLicenses(remoteServerName,
                                                        userId,
                                                        omasServerURL,
                                                        assetGUID,
                                                        this,
                                                        assetResponse.getLicenseCount(),
                                                        MAX_CACHE_SIZE,
                                                        restClient);
        }

        if (assetResponse.getCertificationCount() > 0)
        {
            super.certifications = new ConnectedAssetCertifications(remoteServerName,
                                                                    userId,
                                                                    omasServerURL,
                                                                    assetGUID,
                                                                    this,
                                                                    assetResponse.getCertificationCount(),
                                                                    MAX_CACHE_SIZE,
                                                                    restClient);
        }

        super.feedback = new ConnectedAssetFeedback(remoteServerName,
                                                    userId,
                                                    omasServerURL,
                                                    assetGUID,
                                                    this,
                                                    assetResponse.getCommentCount(),
                                                    assetResponse.getLikeCount(),
                                                    assetResponse.getRatingsCount(),
                                                    assetResponse.getInformalTagCount(),
                                                    MAX_CACHE_SIZE,
                                                    restClient);

        if (assetResponse.getKnownLocationsCount() > 0)
        {
            super.knownLocations = new ConnectedAssetLocations(remoteServerName,
                                                               userId,
                                                               omasServerURL,
                                                               assetGUID,
                                                               this,
                                                               assetResponse.getKnownLocationsCount(),
                                                               MAX_CACHE_SIZE,
                                                               restClient);
        }

        super.lineage = new ConnectedAssetLineage(remoteServerName,
                                                  userId,
                                                  omasServerURL,
                                                  assetGUID,
                                                  this,
                                                  MAX_CACHE_SIZE,
                                                  restClient);

        if (assetResponse.getRelatedAssetCount() > 0)
        {
            super.relatedAssets = new ConnectedAssetRelatedAssets(remoteServerName,
                                                                  userId,
                                                                  omasServerURL,
                                                                  assetGUID,
                                                                  this,
                                                                  assetResponse.getRelatedAssetCount(),
                                                                  MAX_CACHE_SIZE,
                                                                  restClient);
        }

        if (assetResponse.getSchemaType() != null)
        {
            super.schema = this.getAssetSchemaType(remoteServerName, omasServerURL, userId, assetResponse.getSchemaType(), restClient);
        }
    }


    /**
     * Returns the basic information about the asset.  The connection guid allows the short description for the
     * asset to be filled out.
     *
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
    private AssetResponse getConnectedAssetSummary(String     remoteServerName,
                                                   String     omasServerURL,
                                                   RESTClient restClient,
                                                   String     userId,
                                                   String     assetGUID,
                                                   String     connectionGUID) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String   methodName = "getConnectedAssetSummary";
        final String   urlTemplate = "/servers/{0}/open-metadata/common-services/ocf/connected-asset/users/{1}/assets/{2}/via-connection/{3}";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, remoteServerName, methodName);

        AssetResponse  restResult = null;

        try
        {
            restResult = restClient.callAssetGetRESTCall(methodName, urlTemplate, remoteServerName, userId, assetGUID, connectionGUID);

            restExceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
            restExceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
            restExceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException error)
        {
            throw error;
        }
        catch (Throwable error)
        {
            restExceptionHandler.handleUnexpectedException(error, methodName, remoteServerName, omasServerURL);
        }

        return restResult;
    }


    /**
     * Returns the basic information about the asset.  Note shortDescription is null in the returned asset because
     * there is no linked connection object.
     *
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
    private AssetResponse getAssetSummary(String     remoteServerName,
                                          String     omasServerURL,
                                          RESTClient restClient,
                                          String     userId,
                                          String     assetGUID) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String   methodName = "getAssetSummary";
        final String   urlTemplate = "/servers/{0}/open-metadata/common-services/ocf/connected-asset/users/{1}/assets/{2}";
        
        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, remoteServerName, methodName);

        AssetResponse  restResult = null;

        try
        {
            restResult = restClient.callAssetGetRESTCall(methodName, urlTemplate, remoteServerName, userId, assetGUID);

            restExceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
            restExceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
            restExceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException error)
        {
            throw error;
        }
        catch (Throwable error)
        {
            restExceptionHandler.handleUnexpectedException(error, methodName, remoteServerName, omasServerURL);
        }

        return restResult;
    }


    /**
     * Based on the type of bean passed, return the appropriate type of AssetSchemaType.
     *
     * @param remoteServerName  name of the server.
     * @param omasServerURL  url used to call the server.
     * @param userId     String   userId of user making request.
     * @param bean schema type bean that has the properties for the schema type.
     * @param restClient client to call REST API

     * @return subtype of AssetSchemaType
     */
    AssetSchemaType    getAssetSchemaType(String     remoteServerName,
                                          String     omasServerURL,
                                          String     userId,
                                          SchemaType bean,
                                          RESTClient restClient)
    {
        if (bean == null)
        {
            return null;
        }
        else if (bean instanceof ComplexSchemaType)
        {
            return new ConnectedAssetComplexSchemaType(remoteServerName,
                                                       omasServerURL,
                                                       userId,
                                                       this,
                                                       MAX_CACHE_SIZE,
                                                       (ComplexSchemaType)bean,
                                                       restClient);
        }
        else if (bean instanceof MapSchemaType)
        {
            return new ConnectedAssetMapSchemaType(remoteServerName,
                                                   omasServerURL,
                                                   userId,
                                                   this,
                                                   (MapSchemaType)bean,
                                                   restClient);
        }
        else if (bean instanceof PrimitiveSchemaType)
        {
            return new AssetPrimitiveSchemaType(this, (PrimitiveSchemaType) bean);
        }
        else if (bean instanceof BoundedSchemaType)
        {
            return new ConnectedAssetBoundedSchemaType(remoteServerName,
                                                       omasServerURL,
                                                       userId,
                                                       this,
                                                       (BoundedSchemaType)bean,
                                                       restClient);
        }
        else
        {
            return new AssetSchemaType(this, bean);
        }
    }
    
}
