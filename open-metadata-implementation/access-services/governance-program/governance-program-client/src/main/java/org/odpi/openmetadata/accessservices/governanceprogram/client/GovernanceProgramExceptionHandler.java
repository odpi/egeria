/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client;


import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.GovernanceProgramErrorCode;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomain;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.List;
import java.util.Map;

/**
 * GovernanceProgramExceptionHandler provides the client-side interface for the Governance Program Open Metadata Access Service (OMAS).
 * This client, manages all of the interaction with an open metadata repository.  It is initialized with the URL
 * of the server that is running the Open Metadata Access Services.  This server is responsible for locating and
 * managing the governance program definitions exchanged with this client.
 */
public class GovernanceProgramExceptionHandler
{
    private String            omasServerURL;  /* Initialized in constructor */


    /**
     * Create a new GovernanceProgramExceptionHandler.
     *
     * @param newServerURL  the network address of the server running the OMAS REST services
     */
    public GovernanceProgramExceptionHandler(String   newServerURL)
    {
        omasServerURL = newServerURL;
    }


    /**
     * Throw an exception if a server URL has not been supplied on the constructor.
     *
     * @param methodName  name of the method making the call.
     * @throws PropertyServerException the server URL is not set
     */
    public  void validateOMASServerURL(String methodName) throws PropertyServerException
    {
        if (omasServerURL == null)
        {
            /*
             * It is not possible to retrieve a connection without knowledge of where the OMAS Server is located.
             */
            GovernanceProgramErrorCode errorCode    = GovernanceProgramErrorCode.SERVER_URL_NOT_SPECIFIED;
            String                     errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

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
     * @param userId  user name to validate
     * @param methodName  name of the method making the call.
     * @throws UserNotAuthorizedException the userId is null
     */
    public  void validateUserId(String userId,
                                String methodName) throws UserNotAuthorizedException
    {
        if (userId == null)
        {
            GovernanceProgramErrorCode errorCode    = GovernanceProgramErrorCode.NULL_USER_ID;
            String                     errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new UserNotAuthorizedException(errorCode.getHTTPErrorCode(),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 errorMessage,
                                                 errorCode.getSystemAction(),
                                                 errorCode.getUserAction(),
                                                 null);
        }
    }


    /**
     * Throw an exception if the supplied guid is null
     *
     * @param guid  unique identifier to validate
     * @param guidParameter  name of the parameter that passed the guid.
     * @param expectedTypeName name of the type for the instance that should have been retrieved.
     * @param methodName  name of the method making the call.
     * @throws UnrecognizedGUIDException the guid is null
     */
    public  void validateGUID(String guid,
                              String guidParameter,
                              String expectedTypeName,
                              String methodName) throws UnrecognizedGUIDException
    {
        if (guid == null)
        {
            GovernanceProgramErrorCode errorCode    = GovernanceProgramErrorCode.NULL_GUID;
            String                 errorMessage     = errorCode.getErrorMessageId()
                                                    + errorCode.getFormattedErrorMessage(guidParameter,
                                                                                         methodName);

            throw new UnrecognizedGUIDException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                expectedTypeName,
                                                null);
        }
    }


    /**
     * Throw an exception if the supplied name is null
     *
     * @param name  unique name to validate
     * @param nameParameter name of the parameter that passed the name.
     * @param methodName name of the method making the call.
     * @throws InvalidParameterException the guid is null
     */
    public  void validateName(String name,
                              String nameParameter,
                              String methodName) throws InvalidParameterException
    {
        if (name == null)
        {
            GovernanceProgramErrorCode errorCode    = GovernanceProgramErrorCode.NULL_NAME;
            String                 errorMessage     = errorCode.getErrorMessageId()
                                                    + errorCode.getFormattedErrorMessage(nameParameter,
                                                                                         methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                nameParameter);
        }
    }


    /**
     * Throw an exception if the supplied GovernanceDomain enum is null
     *
     * @param governanceDomain domain enum to validate
     * @param nameParameter - name of the parameter that passed the name.
     * @param methodName - name of the method making the call.
     * @throws InvalidParameterException the guid is null
     */
    public  void validateGovernanceDomain(GovernanceDomain governanceDomain,
                                          String           nameParameter,
                                          String           methodName) throws InvalidParameterException
    {
        if (governanceDomain == null)
        {
            GovernanceProgramErrorCode errorCode    = GovernanceProgramErrorCode.NULL_ENUM;
            String                 errorMessage     = errorCode.getErrorMessageId()
                                                    + errorCode.getFormattedErrorMessage(nameParameter, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                nameParameter);
        }
    }



    /**
     * Throw an UnrecognizedGUIDException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     * @throws UnrecognizedGUIDException encoded exception from the server
     */
    public  void detectAndThrowUnrecognizedGUIDException(String                           methodName,
                                                         GovernanceProgramOMASAPIResponse restResult) throws UnrecognizedGUIDException
    {
        final String   exceptionClassName = UnrecognizedGUIDException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String guid = null;
            String expectedTypeName = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  property = exceptionProperties.get("guid");

                if (property != null)
                {
                    guid = (String)property;
                }

                property = exceptionProperties.get("expectedTypeName");

                if (property != null)
                {
                    expectedTypeName = (String)property;
                }
            }

            throw new UnrecognizedGUIDException(restResult.getRelatedHTTPCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                restResult.getExceptionErrorMessage(),
                                                restResult.getExceptionSystemAction(),
                                                restResult.getExceptionUserAction(),
                                                expectedTypeName,
                                                guid);
        }
    }


    /**
     * Throw an InvalidParameterException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     * @throws InvalidParameterException encoded exception from the server
     */
    public  void detectAndThrowInvalidParameterException(String                           methodName,
                                                         GovernanceProgramOMASAPIResponse restResult) throws InvalidParameterException
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
     * Throw an EmployeeNumberNotUniqueException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     * @throws EmployeeNumberNotUniqueException encoded exception from the server
     */
    public  void detectAndThrowEmployeeNumberNotUniqueException(String                           methodName,
                                                                GovernanceProgramOMASAPIResponse restResult) throws EmployeeNumberNotUniqueException
    {
        final String   exceptionClassName = EmployeeNumberNotUniqueException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            List<EntityDetail> duplicateProfiles = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  duplicateProfilesObject = exceptionProperties.get("duplicateProfiles");

                if (duplicateProfilesObject != null)
                {
                    duplicateProfiles = (List<EntityDetail>)duplicateProfilesObject;
                }
            }
            throw new EmployeeNumberNotUniqueException(restResult.getRelatedHTTPCode(),
                                                       this.getClass().getName(),
                                                       methodName,
                                                       restResult.getExceptionErrorMessage(),
                                                       restResult.getExceptionSystemAction(),
                                                       restResult.getExceptionUserAction(),
                                                       duplicateProfiles);
        }
    }


    /**
     * Throw an AppointmentIdNotUniqueException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     * @throws AppointmentIdNotUniqueException encoded exception from the server
     */
    public  void detectAndThrowAppointmentIdNotUniqueException(String                           methodName,
                                                               GovernanceProgramOMASAPIResponse restResult) throws AppointmentIdNotUniqueException
    {
        final String   exceptionClassName = EmployeeNumberNotUniqueException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            List<EntityDetail> duplicatesPosts = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  duplicateProfilesObject = exceptionProperties.get("duplicatePosts");

                if (duplicateProfilesObject != null)
                {
                    duplicatesPosts = (List<EntityDetail>)duplicateProfilesObject;
                }
            }
            throw new AppointmentIdNotUniqueException(restResult.getRelatedHTTPCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      restResult.getExceptionErrorMessage(),
                                                      restResult.getExceptionSystemAction(),
                                                      restResult.getExceptionUserAction(),
                                                      duplicatesPosts);
        }
    }


    /**
     * Throw an PropertyServerException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     * @throws PropertyServerException encoded exception from the server
     */
    public  void detectAndThrowPropertyServerException(String                           methodName,
                                                       GovernanceProgramOMASAPIResponse restResult) throws PropertyServerException
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
     * Throw an UserNotAuthorizedException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from UserNotAuthorizedException encoded exception from the server
     * @throws UserNotAuthorizedException encoded exception from the server
     */
    public  void detectAndThrowUserNotAuthorizedException(String                           methodName,
                                                          GovernanceProgramOMASAPIResponse restResult) throws UserNotAuthorizedException
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


    /**
     * Handles a failed REST call.
     *
     * @param methodName the name of the method issuing the call.
     * @param error the exception that was returned from the REST call.
     * @throws PropertyServerException the exception that will wrap the original exception
     */
    public void handleRESTRequestFailure(String    methodName,
                                         Throwable error) throws PropertyServerException
    {
        GovernanceProgramErrorCode errorCode = GovernanceProgramErrorCode.CLIENT_SIDE_REST_API_ERROR;
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
