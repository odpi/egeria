/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.tex.api.ffdc;

import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;

public class TexExceptionHandler {


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

    public static TexViewServiceException mapOMRSUserNotAuthorizedException(String                     className,
                                                                            String                     methodName,
                                                                            UserNotAuthorizedException repositoryException)
    {
        String userId = repositoryException.getUserId();
        return new TexViewServiceException(TexViewErrorCode.USER_NOT_AUTHORIZED.getMessageDefinition(userId),
                                           className,
                                           methodName);
    }

    public static TexViewServiceException mapOMRSRepositoryErrorException(String                   className,
                                                                          String                   methodName,
                                                                          RepositoryErrorException repositoryException)
    {

        String serverName;
        String reportedErrorMessageId = repositoryException.getReportedErrorMessageId();
        if (reportedErrorMessageId != null)
        {
            switch (reportedErrorMessageId)
            {
                case "OMAG-MULTI-TENANT-404-001":        // platform is contactable, but repository server is not available
                    serverName = repositoryException.getReportedErrorMessageParameters()[0];
                    return new TexViewServiceException(TexViewErrorCode.REPOSITORY_NOT_AVAILABLE.getMessageDefinition(methodName, serverName),
                                                       className,
                                                       methodName);

                case "OMRS-REST-API-503-006":           // platform is not contactable, suspect wrong platform URL
                    serverName = repositoryException.getReportedErrorMessageParameters()[1];
                    return new TexViewServiceException(TexViewErrorCode.PLATFORM_NOT_AVAILABLE.getMessageDefinition(methodName, serverName),
                                                       className,
                                                       methodName);
            }
        }

        /*
         * This is a non-specific repository error - so just pass on the message from the original exception
         */
        String message = repositoryException.getReportedErrorMessage();
        return new TexViewServiceException(TexViewErrorCode.REPOSITORY_ERROR.getMessageDefinition(methodName, message),
                                           className,
                                           methodName);

    }

    public static TexViewServiceException mapOMRSInvalidParameterException(String className, String methodName, InvalidParameterException repositoryException)
    {
        String parameterName = repositoryException.getReportedErrorMessageParameters()[0];
        if (parameterName.equals("searchCriteria"))
            parameterName = "Search Text";
        return new TexViewServiceException(TexViewErrorCode.INVALID_PARAMETER.getMessageDefinition(methodName, parameterName),
                                           className,
                                           methodName);
    }


}
