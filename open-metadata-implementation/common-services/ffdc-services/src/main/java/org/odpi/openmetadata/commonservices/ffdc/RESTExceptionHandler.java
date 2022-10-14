/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc;

import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
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
    private static final MessageFormatter messageFormatter = new MessageFormatter();

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
        InvalidParameterException error =
                new InvalidParameterException(OMAGCommonErrorCode.NO_REQUEST_BODY.getMessageDefinition(userId,
                                                                                                       methodName,
                                                                                                       serverName),
                                              this.getClass().getName(),
                                              methodName,
                                              "requestBody");

        log.error("No rest body supplied", error);
        throw error;
    }


    /**
     * Manage an unexpected exception
     *
     * @param parameterName name of null parameter - use to help locate where the problem is
     * @param methodName method that caught the exception
     * @throws InvalidParameterException exception to report error
     */
    public void handleMissingValue(String      parameterName,
                                   String      methodName) throws InvalidParameterException
    {
        InvalidParameterException error =
                new InvalidParameterException(OMAGCommonErrorCode.NULL_OBJECT.getMessageDefinition(parameterName,
                                                                                                   methodName),
                                              this.getClass().getName(),
                                              methodName,
                                              parameterName);

        log.error("Missing parameter", error);
        throw error;
    }

    /**
     * Manage an unexpected exception
     *
     * @param expectedClassName name of expected properties class
     * @param methodName method that caught the exception
     * @throws InvalidParameterException exception to report error
     */
    public void handleInvalidPropertiesObject(String expectedClassName,
                                              String methodName) throws InvalidParameterException
    {
        InvalidParameterException error =
                new InvalidParameterException(OMAGCommonErrorCode.INVALID_PROPERTIES_OBJECT.getMessageDefinition(methodName, expectedClassName),
                                              this.getClass().getName(),
                                              methodName,
                                              expectedClassName);

        log.error("Wrong properties class", error);
        throw error;
    }



    /**
     * Manage a bad type name
     *
     * @param subTypeName subtype that does not match
     * @param superTypeName expected (super) type
     * @param serviceName calling service
     * @param methodName method that caught the exception
     * @throws InvalidParameterException exception to report error
     */
    public void handleBadType(String subTypeName,
                              String superTypeName,
                              String serviceName,
                              String methodName) throws InvalidParameterException
    {
        InvalidParameterException error =
                new InvalidParameterException(OMAGCommonErrorCode.BAD_SUB_TYPE_NAME.getMessageDefinition(subTypeName,
                                                                                                         methodName,
                                                                                                         serviceName,
                                                                                                         superTypeName),
                                              this.getClass().getName(),
                                              methodName,
                                              subTypeName);

        log.error("Missing parameter", error);
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
                                                 FFDCResponse restResult) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String invalidParameterExceptionClassName  = InvalidParameterException.class.getName();
        final String propertyServerExceptionClassName    = PropertyServerException.class.getName();
        final String userNotAuthorizedExceptionClassName = UserNotAuthorizedException.class.getName();

        if (restResult != null)
        {
            String exceptionClassName = restResult.getExceptionClassName();

            if (exceptionClassName != null)
            {
                log.error("FFDC Response: {}", restResult.toString());
                if (exceptionClassName.equals(invalidParameterExceptionClassName))
                {
                    this.throwInvalidParameterException(restResult);
                }
                else if (exceptionClassName.equals(userNotAuthorizedExceptionClassName))
                {
                    this.throwUserNotAuthorizedException(restResult);
                }
                else if (exceptionClassName.equals(propertyServerExceptionClassName))
                {
                    this.throwPropertyServerException(restResult);
                }
                else
                {
                    this.throwUnexpectedException(methodName, restResult);
                }
            }
            else
            {
                if (log.isDebugEnabled())
                {
                    log.debug("FFDC good Response: {}", restResult.toString());
                }
            }
        }
    }


    /**
     * Throw an InvalidParameterException if it is encoded in the REST response.
     *
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws InvalidParameterException encoded exception from the server
     */
    public void detectAndThrowInvalidParameterException(FFDCResponse restResult) throws InvalidParameterException
    {
        final String   exceptionClassName = InvalidParameterException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            this.throwInvalidParameterException(restResult);
        }
    }


    /**
     * Throw an InvalidParameterException if it is encoded in the REST response.
     *
     * @param methodName  calling method.
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws InvalidParameterException encoded exception from the server
     */
    @Deprecated
    public void detectAndThrowInvalidParameterException(String methodName, FFDCResponseBase restResult) throws InvalidParameterException
    {
        this.detectAndThrowInvalidParameterException(restResult);
    }


    /**
     * Throw an UserNotAuthorizedException if it is encoded in the REST response.
     *
     * @param methodName  calling method.
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws UserNotAuthorizedException encoded exception from the server
     */
    @Deprecated
    public void detectAndThrowUserNotAuthorizedException(String methodName, FFDCResponseBase restResult) throws UserNotAuthorizedException
    {
        this.detectAndThrowUserNotAuthorizedException(restResult);
    }


    /**
     * Throw an PropertyServerException if it is encoded in the REST response.
     *
     * @param methodName  calling method.
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws PropertyServerException encoded exception from the server
     */
    @Deprecated
    public void detectAndThrowPropertyServerException(String methodName, FFDCResponseBase restResult) throws PropertyServerException
    {
        this.detectAndThrowPropertyServerException(restResult);
    }


    /**
     * Throw an InvalidParameterException if it is encoded in the REST response.
     *
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws InvalidParameterException encoded exception from the server
     */
    private void throwInvalidParameterException(FFDCResponse restResult) throws InvalidParameterException
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
                                                                        restResult.getActionDescription(),
                                                                        restResult.getExceptionErrorMessage(),
                                                                        restResult.getExceptionErrorMessageId(),
                                                                        restResult.getExceptionErrorMessageParameters(),
                                                                        restResult.getExceptionSystemAction(),
                                                                        restResult.getExceptionUserAction(),
                                                                        restResult.getExceptionCausedBy(),
                                                                        parameterName,
                                                                        restResult.getExceptionProperties());

        log.error("Detected Invalid Parameter Exception in REST Response", error);
        throw error;
    }


    /**
     * Throw a PropertyServerException if it is encoded in the REST response.
     *
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws PropertyServerException encoded exception from the server
     */
    public void detectAndThrowPropertyServerException(FFDCResponse restResult) throws PropertyServerException
    {
        final String   exceptionClassName = PropertyServerException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            this.throwPropertyServerException(restResult);
        }
    }


    /**
     * Throw a PropertyServerException if it is encoded in the REST response.
     *
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws PropertyServerException encoded exception from the server
     */
    private void throwPropertyServerException(FFDCResponse restResult) throws PropertyServerException
    {
        PropertyServerException error = new PropertyServerException(restResult.getRelatedHTTPCode(),
                                                                    this.getClass().getName(),
                                                                    restResult.getActionDescription(),
                                                                    restResult.getExceptionErrorMessage(),
                                                                    restResult.getExceptionErrorMessageId(),
                                                                    restResult.getExceptionErrorMessageParameters(),
                                                                    restResult.getExceptionSystemAction(),
                                                                    restResult.getExceptionUserAction(),
                                                                    restResult.getExceptionCausedBy(),
                                                                    restResult.getExceptionProperties());

        log.error("Property Server Error Exception returned by REST Call", error);
        throw error;
    }


    /**
     * Throw an UserNotAuthorizedException if it is encoded in the REST response.
     *
     * @param restResult  response from UserNotAuthorizedException encoded exception from the server.
     *
     * @throws UserNotAuthorizedException encoded exception from the server
     */
    public void detectAndThrowUserNotAuthorizedException(FFDCResponse restResult) throws UserNotAuthorizedException
    {
        final String   exceptionClassName = UserNotAuthorizedException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            this.throwUserNotAuthorizedException(restResult);
        }
    }


    /**
     * Throw an UserNotAuthorizedException if it is encoded in the REST response.
     *
     * @param restResult  response from UserNotAuthorizedException encoded exception from the server.
     *
     * @throws UserNotAuthorizedException encoded exception from the server
     */
    private void throwUserNotAuthorizedException(FFDCResponse restResult) throws UserNotAuthorizedException
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
                                                                          restResult.getActionDescription(),
                                                                          restResult.getExceptionErrorMessage(),
                                                                          restResult.getExceptionErrorMessageId(),
                                                                          restResult.getExceptionErrorMessageParameters(),
                                                                          restResult.getExceptionSystemAction(),
                                                                          restResult.getExceptionUserAction(),
                                                                          restResult.getExceptionCausedBy(),
                                                                          userId,
                                                                          restResult.getExceptionProperties());

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
        log.error("Unexpected runtime exception returned from REST Call", error);

        throw new PropertyServerException(OMAGCommonErrorCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                                              serverName,
                                                                                                              serverURL,
                                                                                                              error.getMessage()),
                                          this.getClass().getName(),
                                          methodName,
                                          error);
    }


    /**
     * Manage an unexpected exception
     *
     * @param methodName  name of the method called.
     * @param restResult  response from the encoded exception from the server.
     * @throws PropertyServerException wrapping exception for the caught exception
     */
    private void throwUnexpectedException(String       methodName,
                                          FFDCResponse restResult) throws PropertyServerException
    {
        log.error(methodName + " returned unexpected exception", restResult);

        throw new PropertyServerException(
                OMAGCommonErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(restResult.getExceptionClassName(),
                                                                              methodName,
                                                                              restResult.getExceptionErrorMessage()),
                this.getClass().getName(),
                methodName);
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     */
    protected  void captureCheckedException(FFDCResponse            response,
                                            OCFCheckedExceptionBase error,
                                            String                  exceptionClassName)
    {
        this.captureCheckedException(response, error, exceptionClassName, null);
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     * @param exceptionProperties map of properties stored in the exception to help with diagnostics
     */
    protected  void captureCheckedException(FFDCResponse            response,
                                            OCFCheckedExceptionBase error,
                                            String                  exceptionClassName,
                                            Map<String, Object>     exceptionProperties)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        if (error.getReportedCaughtException() != null)
        {
            response.setExceptionCausedBy(error.getReportedCaughtException().getClass().getName());
        }
        response.setActionDescription(error.getReportingActionDescription());
        response.setExceptionErrorMessage(error.getReportedErrorMessage());
        response.setExceptionErrorMessageId(error.getReportedErrorMessageId());
        response.setExceptionErrorMessageParameters(error.getReportedErrorMessageParameters());
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
    public  void captureExceptions(FFDCResponse response,
                                   Exception    error,
                                   String       methodName)
    {
        this.captureExceptions(response, error, methodName, null);
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response
     * @param methodName calling method
     * @param auditLog log location for recording an unexpected exception
     */
    public  void captureExceptions(FFDCResponse response,
                                   Exception    error,
                                   String       methodName,
                                   AuditLog     auditLog)
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
            String message = error.getMessage();

            if (message == null)
            {
                message = "null";
            }

            ExceptionMessageDefinition messageDefinition = OMAGCommonErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                         methodName,
                                                                                                                         message);

            response.setRelatedHTTPCode(messageDefinition.getHttpErrorCode());
            response.setExceptionClassName(PropertyServerException.class.getName());
            response.setExceptionCausedBy(error.getClass().getName());
            response.setActionDescription(methodName);
            response.setExceptionErrorMessage(messageFormatter.getFormattedMessage(messageDefinition));
            response.setExceptionErrorMessageId(messageDefinition.getMessageId());
            response.setExceptionErrorMessageParameters(messageDefinition.getMessageParams());
            response.setExceptionSystemAction(messageDefinition.getSystemAction());
            response.setExceptionUserAction(messageDefinition.getUserAction());
            response.setExceptionProperties(null);


            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      OMAGCommonAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(), methodName, message),
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
    public  void captureInvalidParameterException(FFDCResponse              response,
                                                  InvalidParameterException error)
    {
        Map<String, Object>  exceptionProperties = error.getRelatedProperties();

        String  parameterName = error.getParameterName();

        if (parameterName != null)
        {
            if (exceptionProperties == null)
            {
                exceptionProperties = new HashMap<>();
            }

            exceptionProperties.put("parameterName", parameterName);
        }

        if (exceptionProperties != null)
        {
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
    public  void capturePropertyServerException(FFDCResponse            response,
                                                PropertyServerException error)
    {
        captureCheckedException(response, error, PropertyServerException.class.getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    public  void captureUserNotAuthorizedException(FFDCResponse               response,
                                                   UserNotAuthorizedException error)
    {
        Map<String, Object>  exceptionProperties = error.getRelatedProperties();

        String  userId = error.getUserId();

        if (userId != null)
        {
            if (exceptionProperties == null)
            {
                exceptionProperties = new HashMap<>();
            }

            exceptionProperties.put("userId", userId);
        }

        if (exceptionProperties != null)
        {
            captureCheckedException(response, error, UserNotAuthorizedException.class.getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, UserNotAuthorizedException.class.getName());
        }
    }
}
