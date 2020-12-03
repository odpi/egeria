/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.serverauthor.api.ffdc;

import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;

/**
 * The Server Author Exception handler maps OMAG exceptions that have been produced by the admin client into
 * Server author Exceptions.
 */
public class ServerAuthorExceptionHandler {


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
     * Mapping functions for OMAG (back-end) admin services exceptions
     */

    /**
     * Map an OMAGNotAuthorizedException to a ServerAuthorViewServiceException which is more consumable / meaningful to the UI
     * @param className the name of the calling class
     * @param methodName the name of the operation being requested
     * @param omagException supplied OMAGNotAuthorizedException
     * @return mapped Server author View Exception
     */
    public static ServerAuthorViewServiceException mapOMAGUserNotAuthorizedException(String            className,
                                                                                     String                     methodName,
                                                                                     OMAGNotAuthorizedException omagException)
    {
        return new ServerAuthorViewServiceException(ServerAuthorViewErrorCode.USER_NOT_AUTHORIZED.getMessageDefinition(),
                                           className,
                                           methodName);
    }

    /**
     * Map an OMAGInvalidParameterException to a ServerAuthorViewServiceException which is more consumable / meaningful to the UI
     * @param className the name of the calling class
     * @param methodName the name of the operation being requested
     * @param omagException supplied OMAGInvalidParameterException
     * @return mapped Server author View Exception
     */
    public static ServerAuthorViewServiceException mapOMAGInvalidParameterException(String className, String methodName, OMAGInvalidParameterException omagException)
    {
        String parameterName = omagException.getReportedErrorMessageParameters()[0];
        return new ServerAuthorViewServiceException(ServerAuthorViewErrorCode.INVALID_PARAMETER.getMessageDefinition(methodName, parameterName),
                                           className,
                                           methodName);
    }

    /**
     * Map an OMAG configuration exception to a ServerAuthorViewServiceException
     * @param className the name of the calling class
     * @param methodName the name of the operation being requested
     * @param error the OMAGConfigurationErrorException error to be mapped
     * @return ServerAuthorViewServiceException Server Author View Service Exception
     */
    public static ServerAuthorViewServiceException mapOMAGConfigurationErrorException(String className, String methodName, OMAGConfigurationErrorException error) {
        return new ServerAuthorViewServiceException(ServerAuthorViewErrorCode.CONFIG_ERROR.getMessageDefinition(),
                                                    className,
                                                    methodName);
    }
}
