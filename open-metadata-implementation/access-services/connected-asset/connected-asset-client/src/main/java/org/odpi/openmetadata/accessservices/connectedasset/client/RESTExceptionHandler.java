/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.client;

import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.UnrecognizedAssetGUIDException;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.UnrecognizedConnectionGUIDException;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.UnrecognizedGUIDException;
import org.odpi.openmetadata.accessservices.connectedasset.rest.ConnectedAssetOMASAPIResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Map;

/**
 * RESTExceptionHandler detects exceptions encoded a REST response and throws the appropriate Java exception.
 */
class RESTExceptionHandler
{
    /**
     * Default constructor
     */
    RESTExceptionHandler()
    {
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
     * Throw an UnrecognizedAssetGUIDException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws UnrecognizedConnectionGUIDException encoded exception from the server
     */
    void detectAndThrowUnrecognizedConnectionGUIDException(String                        methodName,
                                                           ConnectedAssetOMASAPIResponse restResult) throws UnrecognizedConnectionGUIDException
    {
        final String   exceptionClassName = UnrecognizedConnectionGUIDException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String connectionGUID = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  guidObject = exceptionProperties.get("connectionGUID");

                if (guidObject != null)
                {
                    connectionGUID = (String)guidObject;
                }
            }
            throw new UnrecognizedConnectionGUIDException(restResult.getRelatedHTTPCode(),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          restResult.getExceptionErrorMessage(),
                                                          restResult.getExceptionSystemAction(),
                                                          restResult.getExceptionUserAction(),
                                                          connectionGUID);
        }
    }



    /**
     * Throw an UnrecognizedGUIDException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws UnrecognizedGUIDException encoded exception from the server
     */
    void detectAndThrowUnrecognizedGUIDException(String                        methodName,
                                                 ConnectedAssetOMASAPIResponse restResult) throws UnrecognizedGUIDException
    {
        final String   exceptionClassName = UnrecognizedAssetGUIDException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String guid = null;
            String guidType = null;

            Map<String, Object>   exceptionProperties = restResult.getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  guidObject = exceptionProperties.get("guid");
                Object  guidTypeObject = exceptionProperties.get("guidType");

                if (guidObject != null)
                {
                    guid = (String)guidObject;
                }

                if (guidTypeObject != null)
                {
                    guidType = (String)guidTypeObject;
                }
            }
            throw new UnrecognizedGUIDException(restResult.getRelatedHTTPCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                restResult.getExceptionErrorMessage(),
                                                restResult.getExceptionSystemAction(),
                                                restResult.getExceptionUserAction(),
                                                guid,
                                                guidType);
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
