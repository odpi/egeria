/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.dino.api.ffdc;

import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;



import java.net.ConnectException;

public class DinoExceptionHandler {


    /**
     * Method for capturing an exception into a REST response.
     *
     * Set the exception information into the response.
     * This exception capture handler strips off the messageId from the front of the message as
     * the message is intended for user-consumption.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     */
    public static void captureCheckedException(FFDCResponse            response,
                                               OCFCheckedExceptionBase error,
                                               String                  exceptionClassName)

    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        if (error.getReportedCaughtException() != null)
        {
            response.setExceptionCausedBy(error.getReportedCaughtException().getClass().getName());
        }
        response.setActionDescription(error.getReportingActionDescription());
        String fullErrorMessage = error.getReportedErrorMessage();
        String errorMessageId = error.getReportedErrorMessageId();
        String trimmedErrorMessage = fullErrorMessage.substring(errorMessageId.length()+1);
        response.setExceptionErrorMessage(trimmedErrorMessage);
        response.setExceptionErrorMessageId(error.getReportedErrorMessageId());
        response.setExceptionErrorMessageParameters(error.getReportedErrorMessageParameters());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
        response.setExceptionProperties(null);
    }


    /*
     * Mapping functions for OMRS (back-end) repository services exceptions
     */

    public static DinoViewServiceException mapOMRSUserNotAuthorizedException(String                     className,
                                                                            String                     methodName,
                                                                            UserNotAuthorizedException repositoryException)
    {
        String userId = repositoryException.getUserId();
        return new DinoViewServiceException(DinoViewErrorCode.USER_NOT_AUTHORIZED.getMessageDefinition(userId),
                                           className,
                                           methodName);
    }



    public static DinoViewServiceException mapOMRSInvalidParameterException(String className, String methodName, InvalidParameterException repositoryException)
    {
        String parameterName = repositoryException.getReportedErrorMessageParameters()[0];
        if (parameterName.equals("searchCriteria"))
            parameterName = "Search Text";
        return new DinoViewServiceException(DinoViewErrorCode.INVALID_PARAMETER.getMessageDefinition(methodName, parameterName),
                                           className,
                                           methodName);
    }



    /*
     * Mapping functions for OCF (connector framework) exceptions
     */

    public static DinoViewServiceException mapOCFInvalidParameterException(String className,
                                                                           String methodName,
                                                                           org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException ocfException)
    {
        String ocfMessage = ocfException.getReportedErrorMessage();
        return new DinoViewServiceException(DinoViewErrorCode.INVALID_PARAMETER.getMessageDefinition(methodName, ocfMessage),
                                            className,
                                            methodName);
    }


    public static DinoViewServiceException mapOCFUserNotAuthorizedException(String className,
                                                                            String methodName,
                                                                            String userName,
                                                                            org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  ocfException)
    {
        return new DinoViewServiceException(DinoViewErrorCode.USER_NOT_AUTHORIZED.getMessageDefinition(methodName, userName),
                                            className,
                                            methodName);
    }


    public static DinoViewServiceException mapOCFPropertyServerException(String className,
                                                                         String methodName,
                                                                         String platformName,
                                                                         org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException ocfException)
    {
        /*
         * This error is caught if a platform services client tries to contact a platform that is not running.
         * In this case the exception has relatedHTTPCode 503 and a cause -> cause (i.e. double nested) that is an
         * Exception and has a (further) cause which is a ConnectException with message containing "Connection refused".
         * This is not nice but the exceptions are nested deeply and we need to dig to find the cause.
         */

        if (ocfException.getCause() != null)
        {
            Exception cause1 = (Exception)(ocfException.getCause());
            if (cause1.getCause() != null)
            {
                Exception cause2 = (Exception)(cause1.getCause());
                if (cause2.getCause() != null && cause2.getCause() instanceof ConnectException)
                {
                    String message = cause2.getCause().getMessage();
                    if (message.contains("Connection refused"))
                    {
                        return new DinoViewServiceException(DinoViewErrorCode.PLATFORM_NOT_AVAILABLE.getMessageDefinition(methodName, platformName),
                                                            className,
                                                            methodName);
                    }
                }
            }
        }

        /* In any other scenario, take the message from the OCF exception */
        String exceptionMessage = ocfException.getReportedErrorMessage();
        return new DinoViewServiceException(DinoViewErrorCode.UNKNOWN_ERROR.getMessageDefinition(methodName, exceptionMessage),
                                            className,
                                            methodName);
    }


    /*
     * Mapping functions for OMAG Server Configuration exceptions
     */

    public static DinoViewServiceException mapOMAGInvalidParameterException(String className,
                                                                            String methodName,
                                                                            OMAGInvalidParameterException omagException)
    {
        if (omagException != null)
        {
            String omagMessage = omagException.getReportedErrorMessage();
            return new DinoViewServiceException(DinoViewErrorCode.INVALID_PARAMETER.getMessageDefinition(methodName, omagMessage),
                                                className,
                                                methodName);
        }
        else
        {
            return new DinoViewServiceException(DinoViewErrorCode.UNKNOWN_ERROR.getMessageDefinition(methodName),
                                                className,
                                                methodName);
        }

    }


    public static DinoViewServiceException mapOMAGNotAuthorizedException(String className,
                                                                         String methodName,
                                                                         String userName,
                                                                         OMAGNotAuthorizedException omagException)
    {

            return new DinoViewServiceException(DinoViewErrorCode.USER_NOT_AUTHORIZED.getMessageDefinition(methodName, userName),
                                                className,
                                                methodName);

    }


    public static DinoViewServiceException mapOMAGConfigurationErrorException(String className,
                                                                              String methodName,
                                                                              String serverName,
                                                                              OMAGConfigurationErrorException omagException)
    {

            return new DinoViewServiceException(DinoViewErrorCode.COULD_NOT_RETRIEVE_SERVER_CONFIGURATION.getMessageDefinition(methodName, serverName),
                                                className,
                                                methodName);

    }

    /*
     * Mapping functions for InvalidParameterException and similar - as thrown by DiscoveryConfigurationClient
     */

    public static DinoViewServiceException mapInvalidParameterException(String className,
                                                                        String methodName,
                                                                        org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException exception)
    {
        if (exception != null)
        {
            String message = exception.getReportedErrorMessage();
            return new DinoViewServiceException(DinoViewErrorCode.INVALID_PARAMETER.getMessageDefinition(methodName, message),
                                                className,
                                                methodName);
        }
        else
        {
            return new DinoViewServiceException(DinoViewErrorCode.UNKNOWN_ERROR.getMessageDefinition(methodName),
                                                className,
                                                methodName);
        }

    }

    public static DinoViewServiceException mapUserNotAuthorizedException(String                         className,
                                                                         String                     methodName,
                                                                         org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException repositoryException)
    {
        String userId = repositoryException.getUserId();
        return new DinoViewServiceException(DinoViewErrorCode.USER_NOT_AUTHORIZED.getMessageDefinition(userId),
                                            className,
                                            methodName);
    }
}
