/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.client;

import org.odpi.openmetadata.accessservices.connectedasset.ffdc.ConnectedAssetErrorCode;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.connectedasset.rest.AssetResponse;
import org.odpi.openmetadata.accessservices.connectedasset.rest.ConnectedAssetOMASAPIResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.*;
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


    /*
     * Current counts of all of the attached elements
     */
    private int   annotationCount            = 0;
    private int   certificationCount         = 0;
    private int   commentCount               = 0;
    private int   connectionCount            = 0;
    private int   externalIdentifierCount    = 0;
    private int   externalReferencesCount    = 0;
    private int   informalTagCount           = 0;
    private int   licenseCount               = 0;
    private int   likeCount                  = 0;
    private int   knownLocationsCount        = 0;
    private int   meaningsCount              = 0;
    private int   noteLogsCount              = 0;
    private int   ratingsCount               = 0;
    private int   relatedAssetCount          = 0;
    private int   relatedMediaReferenceCount = 0;
    private int   schemaCount                = 0;


    /**
     * Constructor used by Asset Consumer OMAS and Connected AssetProperties.refresh().
     *
     * @param omasServerURL  url used to call the server.
     * @param userId  userId of user making request.
     * @param assetGUID  unique id for asset.
     *
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

        AssetResponse assetResponse = this.getAssetSummary(userId, assetGUID);

        super.assetBean = assetResponse.getAsset();

        if (assetResponse.getExternalIdentifierCount() > 0)
        {
            super.externalIdentifiers = new ConnectedAssetExternalIdentifiers(userId,
                                                                              omasServerURL,
                                                                              assetGUID,
                                                                              this,
                                                                              assetResponse.getExternalIdentifierCount(),
                                                                              100);
        }
        super.relatedMediaReferences = this.getNewRelatedMediaReferences(userId, assetGUID);
        super.noteLogs = this.getNewNoteLogs(userId, assetGUID);
        super.externalIdentifiers = this.getNewExternalIdentifiers(userId, assetGUID);
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
    private AssetResponse getAssetSummary(String   userId,
                                          String   assetGUID) throws InvalidParameterException,
                                                                     UnrecognizedAssetGUIDException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String   methodName = "getAssetSummary";
        final String   urlTemplate = "/open-metadata/access-services/connected-asset/users/{0}/assets/by-connection/{1}";

        validateOMASServerURL(methodName);

        AssetResponse  restResult = null;

        try
        {
            RestTemplate restTemplate = new RestTemplate();

            restResult = restTemplate.getForObject(urlTemplate, AssetResponse.class, userId, assetGUID);
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

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowUnrecognizedAssetGUIDException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        this.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult;
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

    private AssetSchemaType getNewSchema(String   userId,
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
     * Throw an exception if a server URL has not been supplied on the constructor.
     *
     * @param methodName  name of the method making the call.
     *
     * @throws PropertyServerException the server URL is not set
     */
    void validateOMASServerURL(String methodName) throws PropertyServerException
    {
        if (omasServerURL == null)
        {
            /*
             * It is not possible to retrieve a connection without knowledge of where the OMAS Server is located.
             */
            ConnectedAssetErrorCode errorCode    = ConnectedAssetErrorCode.SERVER_URL_NOT_SPECIFIED;
            String                  errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param userId      user name to validate
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the userId is null
     */
    void validateUserId(String userId,
                        String methodName) throws InvalidParameterException
    {
        if (userId == null)
        {
            ConnectedAssetErrorCode errorCode   = ConnectedAssetErrorCode.NULL_USER_ID;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                "userId");
        }
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param guid           unique identifier to validate
     * @param guidParameter  name of the parameter that passed the guid.
     * @param methodName     name of the method making the call.
     *
     * @throws InvalidParameterException the guid is null
     */
    void validateGUID(String guid,
                      String guidParameter,
                      String methodName) throws InvalidParameterException
    {
        if (guid == null)
        {
            ConnectedAssetErrorCode errorCode    = ConnectedAssetErrorCode.NULL_GUID;
            String                  errorMessage = errorCode.getErrorMessageId()
                                  + errorCode.getFormattedErrorMessage(guidParameter, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                guidParameter);
        }
    }


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return Object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    Object callGetRESTCall(String    methodName,
                           Class     returnClass,
                           String    urlTemplate,
                           Object... params) throws PropertyServerException
    {
        try
        {
            RestTemplate  restTemplate = new RestTemplate();

            return restTemplate.getForObject(urlTemplate, returnClass, params);
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
    void detectAndThrowInvalidParameterException(String                        methodName,
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
    void detectAndThrowPropertyServerException(String                        methodName,
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
    void detectAndThrowUnrecognizedAssetGUIDException(String                        methodName,
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
    void detectAndThrowUserNotAuthorizedException(String                        methodName,
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
