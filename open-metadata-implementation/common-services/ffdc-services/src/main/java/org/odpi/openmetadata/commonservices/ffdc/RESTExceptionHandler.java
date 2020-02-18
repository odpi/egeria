/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc;

import org.odpi.openmetadata.commonservices.ffdc.auditlog.OMAGCommonAuditCode;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * RESTExceptionHandler converts standard exceptions to REST responses.  These responses ensure exception objects
 * (with their stack traces) are not serialized over REST APIs.
 *
 * This class does use developer logging (SLF4J) for components that are not mature enough to have implemented
 * FFDC.  For mature components, this logging is superfluous.
 */
public class RESTExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(RESTExceptionHandler.class);

    /**
     * Constructor
     */
    public RESTExceptionHandler()
    {
    }


    /**
     * Manage an unexpected exception
     *
     * @param userId calling user
     * @param methodName method that caught the exception
     * @param serverName name of the server being called
     * @throws InvalidParameterException exception to report error
     */
    public void handleNoRequestBody(String      userId,
                                    String      methodName,
                                    String      serverName) throws InvalidParameterException
    {
        OMAGCommonErrorCode errorCode = OMAGCommonErrorCode.NO_REQUEST_BODY;
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(userId,
                                                                                                 methodName,
                                                                                                 serverName);

        InvalidParameterException error = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                                        this.getClass().getName(),
                                                                        methodName,
                                                                        errorMessage,
                                                                        errorCode.getSystemAction(),
                                                                        errorCode.getUserAction(),
                                                                        "requestBody");

        log.error("No rest body supplied", error);
        throw error;
    }


    /**
     * Throw an exception if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    public void detectAndThrowStandardExceptions(String           methodName,
                                                 FFDCResponseBase restResult) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String   invalidParameterExceptionClassName = InvalidParameterException.class.getName();
        final String   propertyServerExceptionClassName = PropertyServerException.class.getName();
        final String   userNotAuthorizedExceptionClassName = UserNotAuthorizedException.class.getName();

        if (restResult != null)
        {
            log.error("FFDC Response: {}", restResult.toString());
            String exceptionClassName = restResult.getExceptionClassName();

            if (exceptionClassName != null)
            {
                if (exceptionClassName.equals(invalidParameterExceptionClassName))
                {
                    this.throwInvalidParameterException(methodName, restResult);
                }
                else if (exceptionClassName.equals(userNotAuthorizedExceptionClassName))
                {
                    this.throwUserNotAuthorizedException(methodName, restResult);
                }
                else if (exceptionClassName.equals(propertyServerExceptionClassName))
                {
                    this.throwPropertyServerException(methodName, restResult);
                }
                else
                {
                    this.throwUnexpectedException(methodName, restResult);
                }
            }
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
    public void detectAndThrowInvalidParameterException(String           methodName,
                                                        FFDCResponseBase restResult) throws InvalidParameterException
    {
        final String   exceptionClassName = InvalidParameterException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            this.throwInvalidParameterException(methodName, restResult);
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
    private void throwInvalidParameterException(String           methodName,
                                                FFDCResponseBase restResult) throws InvalidParameterException
    {
        String parameterName = null;

        Map<String, Object>   exceptionProperties = restResult.getExceptionProperties();

        if (exceptionProperties != null)
        {
            Object  nameObject = exceptionProperties.get("parameterName");

            if (nameObject != null)
            {
                parameterName = (String)nameObject;
            }
        }

        InvalidParameterException error = new InvalidParameterException(restResult.getRelatedHTTPCode(),
                                                                        this.getClass().getName(),
                                                                        methodName,
                                                                        restResult.getExceptionErrorMessage(),
                                                                        restResult.getExceptionSystemAction(),
                                                                        restResult.getExceptionUserAction(),
                                                                        parameterName);
        log.error("Detected Invalid Parameter Exception in REST Response", error);
        throw error;
    }


    /**
     * Throw a PropertyServerException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws PropertyServerException encoded exception from the server
     */
    public void detectAndThrowPropertyServerException(String           methodName,
                                                      FFDCResponseBase restResult) throws PropertyServerException
    {
        final String   exceptionClassName = PropertyServerException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            this.throwPropertyServerException(methodName, restResult);
        }
    }


    /**
     * Throw a PropertyServerException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws PropertyServerException encoded exception from the server
     */
    private void throwPropertyServerException(String           methodName,
                                              FFDCResponseBase restResult) throws PropertyServerException
    {
        PropertyServerException error = new PropertyServerException(restResult.getRelatedHTTPCode(),
                                                                    this.getClass().getName(),
                                                                    methodName,
                                                                    restResult.getExceptionErrorMessage(),
                                                                    restResult.getExceptionSystemAction(),
                                                                    restResult.getExceptionUserAction());

        log.error("Property Server Error Exception returned by REST Call", error);
        throw error;
    }


    /**
     * Throw an UserNotAuthorizedException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called.
     * @param restResult  response from UserNotAuthorizedException encoded exception from the server.
     *
     * @throws UserNotAuthorizedException encoded exception from the server
     */
    public void detectAndThrowUserNotAuthorizedException(String           methodName,
                                                         FFDCResponseBase restResult) throws UserNotAuthorizedException
    {
        final String   exceptionClassName = UserNotAuthorizedException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            this.throwUserNotAuthorizedException(methodName, restResult);
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
    private void throwUserNotAuthorizedException(String           methodName,
                                                 FFDCResponseBase restResult) throws UserNotAuthorizedException
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

        UserNotAuthorizedException error = new UserNotAuthorizedException(restResult.getRelatedHTTPCode(),
                                                                          this.getClass().getName(),
                                                                          methodName,
                                                                          restResult.getExceptionErrorMessage(),
                                                                          restResult.getExceptionSystemAction(),
                                                                          restResult.getExceptionUserAction(),
                                                                          userId);
        log.error("User Not Authorized Exception", error);
        throw error;
    }


    /**
     * Manage an unexpected exception
     *
     * @param error unexpected exception
     * @param methodName method that caught the exception
     * @param serverName name of the server being called
     * @param serverURL platform URL
     * @throws PropertyServerException wrapping exception for the caught exception
     */
    public void handleUnexpectedException(Throwable   error,
                                          String      methodName,
                                          String      serverName,
                                          String      serverURL) throws PropertyServerException
    {
        OMAGCommonErrorCode errorCode = OMAGCommonErrorCode.CLIENT_SIDE_REST_API_ERROR;
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                 serverName,
                                                                                                 serverURL,
                                                                                                 error.getMessage());

        log.error("Unexpected runtime exception returned from REST Call", error);

        throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction(),
                                          error);
    }


    /**
     * Manage an unexpected exception
     *
     * @param methodName  name of the method called.
     * @param restResult  response from the encoded exception from the server.
     * @throws PropertyServerException wrapping exception for the caught exception
     */
    protected void throwUnexpectedException(String           methodName,
                                            FFDCResponseBase restResult) throws PropertyServerException
    {
        OMAGCommonErrorCode errorCode = OMAGCommonErrorCode.UNEXPECTED_EXCEPTION;
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(restResult.getExceptionClassName(),
                                                                                                 methodName,
                                                                                                 restResult.getExceptionErrorMessage());

        log.error(methodName + " returned unexpected exception", restResult);

        throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          restResult.getExceptionSystemAction(),
                                          restResult.getExceptionUserAction());
    }


    /**
     * Throw an exception if the supplied guid returned an entity of the wrong type
     *
     * @param guid  unique identifier of entity
     * @param methodName  name of the method making the call.
     * @param actualType  type of retrieved entity
     * @param expectedType  type the entity should be
     * @throws InvalidParameterException the guid is for the wrong type of object
     */
    public void handleWrongTypeForGUIDException(String guid,
                                                String methodName,
                                                String actualType,
                                                String expectedType) throws InvalidParameterException
    {
        OMAGCommonErrorCode errorCode = OMAGCommonErrorCode.INSTANCE_WRONG_TYPE_FOR_GUID;
        String              errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                              guid,
                                                                                                              actualType,
                                                                                                              expectedType);

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                            this.getClass().getName(),
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            expectedType);

    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     */
    protected  void captureCheckedException(FFDCResponseBase        response,
                                            OCFCheckedExceptionBase error,
                                            String                  exceptionClassName)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     * @param exceptionProperties map of properties stored in the exception to help with diagnostics
     */
    protected  void captureCheckedException(FFDCResponseBase             response,
                                            OCFCheckedExceptionBase      error,
                                            String                       exceptionClassName,
                                            Map<String, Object>          exceptionProperties)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
        response.setExceptionProperties(exceptionProperties);
    }



    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response
     * @param methodName calling method
     */
    public  void captureThrowable(FFDCResponseBase             response,
                                  Throwable                    error,
                                  String                       methodName)
    {
        this.captureThrowable(response, error, methodName, null);
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response
     * @param methodName calling method
     * @param auditLog log location for recording an unexpected exception
     */
    public  void captureThrowable(FFDCResponseBase             response,
                                  Throwable                    error,
                                  String                       methodName,
                                  OMRSAuditLog                 auditLog)
    {
        log.error("Exception from " + methodName + " being packaged for return on REST call", error);

        if (error instanceof PropertyServerException)
        {
            capturePropertyServerException(response, (PropertyServerException)error);
        }
        else if (error instanceof UserNotAuthorizedException)
        {
            captureUserNotAuthorizedException(response, (UserNotAuthorizedException)error);
        }
        else if (error instanceof InvalidParameterException)
        {
            captureInvalidParameterException(response, (InvalidParameterException)error);
        }
        else
        {
            OMAGCommonErrorCode errorCode = OMAGCommonErrorCode.UNEXPECTED_EXCEPTION;

            String message = error.getMessage();

            if (message == null)
            {
                message = "null";
            }
            response.setRelatedHTTPCode(errorCode.getHTTPErrorCode());
            response.setExceptionClassName(error.getClass().getName());
            response.setExceptionErrorMessage(errorCode.getFormattedErrorMessage(error.getClass().getName(), methodName, message));
            response.setExceptionSystemAction(errorCode.getSystemAction());
            response.setExceptionUserAction(errorCode.getUserAction());
            response.setExceptionProperties(null);

            if (auditLog != null)
            {
                OMAGCommonAuditCode auditCode;

                auditCode = OMAGCommonAuditCode.UNEXPECTED_EXCEPTION;
                auditLog.logException(methodName,
                                      auditCode.getLogMessageId(),
                                      auditCode.getSeverity(),
                                      auditCode.getFormattedLogMessage(error.getClass().getName(), methodName, message),
                                      null,
                                      auditCode.getSystemAction(),
                                      auditCode.getUserAction(),
                                      error);
            }
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    public  void captureInvalidParameterException(FFDCResponseBase          response,
                                                  InvalidParameterException error)
    {
        String  parameterName = error.getParameterName();

        if (parameterName != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("parameterName", parameterName);
            captureCheckedException(response, error, InvalidParameterException.class.getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, InvalidParameterException.class.getName());
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    public  void capturePropertyServerException(FFDCResponseBase        response,
                                                PropertyServerException error)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(PropertyServerException.class.getName());
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    public  void captureUserNotAuthorizedException(FFDCResponseBase           response,
                                                   UserNotAuthorizedException error)
    {
        String  userId = error.getUserId();

        if (userId != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("userId", userId);
            response.setExceptionProperties(exceptionProperties);
        }

        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(UserNotAuthorizedException.class.getName());
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error    returned response.
     */
    public void captureRepositoryErrorException(FFDCResponseBase         response,
                                                RepositoryErrorException error)
    {
        setOMRSExceptionInformationInResponse(response, error);
    }


    /**
     * Set up the standard fields from an OMRS exception.
     *
     * @param response response to fill out
     * @param error error to process
     */
    private void setOMRSExceptionInformationInResponse(FFDCResponseBase response, OMRSCheckedExceptionBase error)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(error.getClass().getName());
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }
}
