/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.client;

import org.odpi.openmetadata.accessservices.connectedasset.ffdc.ConnectedAssetErrorCode;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.connectedasset.rest.ConnectedAssetOMASAPIResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


/**
 * ConnectedAsset is the OMAS client library implementation of the ConnectedAsset OMAS.
 * ConnectedAsset provides the metadata for the ConnectedAssetProperties API that is
 * supported by all Open Connector Framework (OCF)
 * connectors.   It provides access to the metadata about the Asset that the connector is linked to.
 */
public class ConnectedAsset extends AssetUniverse
{
    /*
     * The URL of the server where OMAS is active
     */
    private String                    omasServerURL;


    /**
     * Default Constructor used once a connector is created.
     *
     * @param omasServerURL  unique id for the connector instance
     * @param userId  String - userId of user making request.
     * @param assetGUID  String - unique id for asset.
     *
     * @return AssetUniverse - a comprehensive collection of properties about the asset.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedAssetGUIDException the assetGUID is not recognized
     * @throws PropertyServerException There is a problem retrieving the asset properties from
     *                                   the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public ConnectedAsset(String   omasServerURL,
                          String   userId,
                          String   assetGUID) throws UnrecognizedAssetGUIDException,
                                                     InvalidParameterException,
                                                     PropertyServerException,
                                                     UserNotAuthorizedException
    {
        super();

        this.omasServerURL = omasServerURL;

        super.assetBean = this.getAssetBasics(userId, assetGUID);
        super.externalIdentifiers = this.getNewExternalIdentifiers(userId, assetGUID);
        super.relatedMediaReferences = this.getNewRelatedMediaReferences(userId, assetGUID);
        super.noteLogs = this.getNewNoteLogs(userId, assetGUID);
        super.externalReferences = this.getNewExternalReferences(userId, assetGUID);
        super.connections = this.getNewConnections(userId, assetGUID);
        super.licenses = this.getNewLicenses(userId, assetGUID);
        super.certifications = this.getNewCertifications(userId, assetGUID);
        super.meanings = this.getNewMeanings(userId, assetGUID);
        super.schema = this.getNewSchema(userId, assetGUID);
        super.analysis = this.getNewAnalysis(userId, assetGUID);
        super.feedback = this.getNewFeedback(userId, assetGUID);
        super.knownLocations = this.getNewKnownLocations(userId, assetGUID);
        super.lineage = this.getNewLineage(userId, assetGUID);
        super.relatedAssets = this.getNewRelatedAssets(userId, assetGUID);
    }



    /**
     * Returns the basic information about the asset.
     *
     * @param userId     String   userId of user making request.
     * @param assetGUID  String   unique id for asset.
     *
     * @return a bean with the basic properties about the asset.
     * @throws InvalidParameterException the GUID is null or invalid.
     * @throws UnrecognizedAssetGUIDException the GUID is not recognized by the property server.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private Asset getAssetBasics(String   userId,
                                 String   assetGUID) throws InvalidParameterException,
                                                            UnrecognizedAssetGUIDException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        return null;
    }


    private AssetExternalIdentifiers  getNewExternalIdentifiers(String   userId,
                                                                String   assetGUID)
    {
        return null;
    }


    private AssetRelatedMediaReferences  getNewRelatedMediaReferences(String   userId,
                                                                      String   assetGUID)
    {
        return null;
    }

    private AssetNoteLogs  getNewNoteLogs(String   userId,
                                          String   assetGUID)
    {
        return null;
    }

    private AssetExternalReferences  getNewExternalReferences(String   userId,
                                                              String   assetGUID)
    {
        return null;
    }

    private AssetConnections  getNewConnections(String   userId,
                                                String   assetGUID)
    {
        return null;
    }

    private AssetLicenses  getNewLicenses(String   userId,
                                          String   assetGUID)
    {
        return null;
    }

    private AssetCertifications  getNewCertifications(String   userId,
                                                      String   assetGUID)
    {
        return null;
    }

    private AssetMeanings  getNewMeanings(String   userId,
                                          String   assetGUID)
    {
        return null;
    }

    private AssetSchema  getNewSchema(String   userId,
                                      String   assetGUID)
    {
        return null;
    }

    private AssetAnnotations  getNewAnalysis(String   userId,
                                             String   assetGUID)
    {
        return null;
    }

    private AssetFeedback  getNewFeedback(String   userId,
                                          String   assetGUID)
    {
        return null;
    }

    private AssetLocations  getNewKnownLocations(String   userId,
                                                 String   assetGUID)
    {
        return null;
    }

    private AssetLineage  getNewLineage(String   userId,
                                        String   assetGUID)
    {
        return null;
    }

    private RelatedAssets  getNewRelatedAssets(String   userId,
                                               String   assetGUID)
    {
        return null;
    }


    /**
     * Issue a POST REST call that returns a CountResponse object.  This is typically a create
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return Object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private Object callRESTCall(String    methodName,
                                Class     returnClass,
                                String    urlTemplate,
                                Object    requestBody,
                                Object... params) throws PropertyServerException
    {
        try
        {
            RestTemplate restTemplate = new RestTemplate();

            return restTemplate.postForObject(urlTemplate, requestBody, returnClass, params);
        }
        catch (Throwable error)
        {
            ConnectedAssetErrorCode errorCode = ConnectedAssetErrorCode.CLIENT_SIDE_REST_API_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                     omasServerURL,
                                                                                                     error.getMessage());

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction(),
                                              error);
        }
    }


    /**
     * Throw an InvalidParameterException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws InvalidParameterException encoded exception from the server
     */
    private void detectAndThrowInvalidParameterException(String                        methodName,
                                                         ConnectedAssetOMASAPIResponse restResult) throws InvalidParameterException
    {
        final String   exceptionClassName = InvalidParameterException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String paramName = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  nameObject = exceptionProperties.get("parameterName");

                if (nameObject != null)
                {
                    paramName = (String)nameObject;
                }
            }
            throw new InvalidParameterException(restResult.getRelatedHTTPCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                restResult.getExceptionErrorMessage(),
                                                restResult.getExceptionSystemAction(),
                                                restResult.getExceptionUserAction(),
                                                paramName);
        }
    }


    /**
     * Throw an PropertyServerException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws PropertyServerException encoded exception from the server
     */
    private void detectAndThrowPropertyServerException(String                        methodName,
                                                       ConnectedAssetOMASAPIResponse restResult) throws PropertyServerException
    {
        final String   exceptionClassName = PropertyServerException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new PropertyServerException(restResult.getRelatedHTTPCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              restResult.getExceptionErrorMessage(),
                                              restResult.getExceptionSystemAction(),
                                              restResult.getExceptionUserAction());
        }
    }


    /**
     * Throw an UnrecognizedAssetGUIDException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws UnrecognizedAssetGUIDException encoded exception from the server
     */
    private void detectAndThrowUnrecognizedAssetGUIDException(String                        methodName,
                                                              ConnectedAssetOMASAPIResponse restResult) throws UnrecognizedAssetGUIDException
    {
        final String   exceptionClassName = UnrecognizedAssetGUIDException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String assetGUID = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  guidObject = exceptionProperties.get("assetGUID");

                if (guidObject != null)
                {
                    assetGUID = (String)guidObject;
                }
            }
            throw new UnrecognizedAssetGUIDException(restResult.getRelatedHTTPCode(),
                                                     this.getClass().getName(),
                                                     methodName,
                                                     restResult.getExceptionErrorMessage(),
                                                     restResult.getExceptionSystemAction(),
                                                     restResult.getExceptionUserAction(),
                                                     assetGUID);
        }
    }


    /**
     * Throw an UserNotAuthorizedException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called.
     * @param restResult  response from UserNotAuthorizedException encoded exception from the server.
     *
     * @throws UserNotAuthorizedException encoded exception from the server
     */
    private void detectAndThrowUserNotAuthorizedException(String                        methodName,
                                                          ConnectedAssetOMASAPIResponse restResult) throws UserNotAuthorizedException
    {
        final String   exceptionClassName = UserNotAuthorizedException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String userId = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  userIdObject = exceptionProperties.get("userId");

                if (userIdObject != null)
                {
                    userId = (String)userIdObject;
                }
            }

            throw new UserNotAuthorizedException(restResult.getRelatedHTTPCode(),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 restResult.getExceptionErrorMessage(),
                                                 restResult.getExceptionSystemAction(),
                                                 restResult.getExceptionUserAction(),
                                                 userId);
        }
    }
}
