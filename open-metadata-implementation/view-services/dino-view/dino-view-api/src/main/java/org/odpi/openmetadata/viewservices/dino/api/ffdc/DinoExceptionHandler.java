/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.dino.api.ffdc;

import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
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

    public static DinoViewServiceException mapOMRSRepositoryErrorException(String                   className,
                                                                          String                   methodName,
                                                                          RepositoryErrorException repositoryException)
    {

        String serverName;
        switch (repositoryException.getReportedErrorMessageId()) {
            case "OMAG-MULTI-TENANT-404-001":        // platform is contactable, but repository server is not available
                serverName = repositoryException.getReportedErrorMessageParameters()[0];
                return new DinoViewServiceException(DinoViewErrorCode.REPOSITORY_NOT_AVAILABLE.getMessageDefinition(methodName,serverName),
                                                   className,
                                                   methodName);

            case "OMRS-REST-API-503-006":           // platform is not contactable, suspect wrong platform URL
                serverName = repositoryException.getReportedErrorMessageParameters()[1];
                return new DinoViewServiceException(DinoViewErrorCode.PLATFORM_NOT_AVAILABLE.getMessageDefinition(methodName,serverName),
                                                   className,
                                                   methodName);

            default:
                /*
                 * This is a non-specific repository error - so just pass on the message from the original exception
                 */
                String message = repositoryException.getReportedErrorMessage();
                return new DinoViewServiceException(DinoViewErrorCode.REPOSITORY_ERROR.getMessageDefinition(methodName,message),
                                                   className,
                                                   methodName);

        }

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

    public static DinoViewServiceException mapOMRSEntityNotKnownException(String className,
                                                                         String methodName,
                                                                         String repositoryServerName,
                                                                         boolean enterpriseOption,
                                                                         EntityNotKnownException repositoryException)
    {
        String entityGUID = repositoryException.getReportedErrorMessageParameters()[0];
        if (! enterpriseOption)
        {
            /*
             * Because this was a non-enterprise operation extract the server from the exception.
             * It should be identical to the repositoryServerName parameter passed by the caller, but
             * it's better to present the actual source of the exception.
             */
            String reportingServerName = repositoryException.getReportedErrorMessageParameters()[3];
            return new DinoViewServiceException(DinoViewErrorCode.ENTITY_NOT_KNOWN_IN_REPOSITORY.getMessageDefinition(methodName, entityGUID, reportingServerName),
                                               className,
                                               methodName);
        }
        else
        {
            /* Because this was for an enterprise retrieval, do not rely on the server name in the exception - it
             * could be from any of the repository servers that responded. Instead, use the repositoryServerName
             * parameter from the caller.
             */
            return new DinoViewServiceException(DinoViewErrorCode.ENTITY_NOT_KNOWN_IN_ENTERPRISE.getMessageDefinition(methodName, entityGUID, repositoryServerName),
                                               className,
                                               methodName);
        }
    }

    public static DinoViewServiceException mapOMRSEntityProxyOnlyException(String className,
                                                                          String methodName,
                                                                          EntityProxyOnlyException repositoryException)
    {
        /*
         * The positional parameters to this exception's message vary depending on which
         * connector raised the original error code. Rather than trying to unpick this
         * just include the original exception's formatted message.
         */
        String  exceptionMessage = repositoryException.getReportedErrorMessage();
        return new DinoViewServiceException(DinoViewErrorCode.ENTITY_PROXY_ONLY.getMessageDefinition(methodName, exceptionMessage),
                                           className,
                                           methodName);
    }

    public static DinoViewServiceException mapOMRSRelationshipNotKnownException(String className,
                                                                               String methodName,
                                                                               String repositoryServerName,
                                                                               boolean enterpriseOption,
                                                                               RelationshipNotKnownException repositoryException)
    {
        String relationshipGUID = repositoryException.getReportedErrorMessageParameters()[0];
        if (! enterpriseOption)
        {
            /*
             * Because this was a non-enterprise operation extract the server from the exception.
             * It should be identical to the repositoryServerName parameter passed by the caller, but
             * it's better to present the actual source of the exception.
             */
            String reportingServerName = repositoryException.getReportedErrorMessageParameters()[2];
            return new DinoViewServiceException(DinoViewErrorCode.RELATIONSHIP_NOT_KNOWN_IN_REPOSITORY.getMessageDefinition(methodName,
                                                                                                                          relationshipGUID,
                                                                                                                          reportingServerName),
                                               className,
                                               methodName);
        }
        else
        {
            /* Because this was for an enterprise retrieval, do not rely on the server name in the exception - it
             * could be from any of the repository servers that responded. Instead, use the repositoryServerName
             * parameter from the caller.
             */
            return new DinoViewServiceException(DinoViewErrorCode.RELATIONSHIP_NOT_KNOWN_IN_ENTERPRISE.getMessageDefinition(methodName,
                                                                                                                          relationshipGUID,
                                                                                                                          repositoryServerName),
                                               className,
                                               methodName);
        }
    }



    public static DinoViewServiceException mapOMRSTypeErrorException(String className, String methodName, TypeErrorException repositoryException)
    {
        /*
         * In the case of a PagingErrorException just wrap the existing message
         */
        String exceptionMessage = repositoryException.getReportedErrorMessage();
        return new DinoViewServiceException(DinoViewErrorCode.TYPE_ERROR.getMessageDefinition(methodName, exceptionMessage),
                                           className,
                                           methodName);
    }

    public static DinoViewServiceException mapOMRSPropertyErrorException(String className, String methodName, PropertyErrorException repositoryException)
    {
        /*
         * In the case of a PropertyErrorException there are many variations arising from different
         * error codes. These include at least the following:
         *  BAD_PROPERTY_FOR_TYPE
         *  NO_PROPERTIES_FOR_TYPE
         *  BAD_PROPERTY_TYPE
         *  NULL_PROPERTY_NAME_FOR_INSTANCE
         *  NULL_PROPERTY_VALUE_FOR_INSTANCE
         *  NULL_PROPERTY_TYPE_FOR_INSTANCE
         *  NO_NEW_PROPERTIES
         *  BAD_PROPERTY_FOR_TYPE
         *  BAD_PROPERTY_FOR_INSTANCE
         *  NULL_PROPERTY_NAME
         *  and more...
         *
         * Rather than attempting to cover them all and pick the salient parameters
         * out of their varying positions in the message parameters list, it is preferred to simply
         * include the message from the original exception - which wll have been quite comprehensively
         * formatted with a mixture of repository, category, type, property name, value, etc.
         *
         * To achieve this, the Dino error code for a property exception is very simple and accepts
         * the original message.
         */
        String exceptionMessage = repositoryException.getReportedErrorMessage();
        return new DinoViewServiceException(DinoViewErrorCode.PROPERTY_ERROR.getMessageDefinition(methodName, exceptionMessage),
                                           className,
                                           methodName);
    }


    public static DinoViewServiceException mapOMRSPagingErrorException(String className, String methodName, PagingErrorException repositoryException)
    {
        /*
         * In the case of a PagingErrorException just wrap the existing message
         */
        String exceptionMessage = repositoryException.getReportedErrorMessage();
        return new DinoViewServiceException(DinoViewErrorCode.PAGING_ERROR.getMessageDefinition(methodName, exceptionMessage),
                                           className,
                                           methodName);
    }


    public static DinoViewServiceException mapOMRSFunctionNotSupportedException(String className, String methodName, FunctionNotSupportedException repositoryException)
    {
        String repositoryName = repositoryException.getReportedErrorMessageParameters()[2];
        return new DinoViewServiceException(DinoViewErrorCode.FUNCTION_NOT_SUPPORTED_ERROR.getMessageDefinition(methodName, repositoryName ),
                                           className,
                                           methodName);
    }


    /*
     * Mapping functions for OCF (connector franework) exceptions
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
                                                                            org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  ocfException)
    {
        String ocfMessage = ocfException.getReportedErrorMessage();
        return new DinoViewServiceException(DinoViewErrorCode.INVALID_PARAMETER.getMessageDefinition(methodName, ocfMessage),
                                            className,
                                            methodName);
    }

    public static DinoViewServiceException mapOCFPropertyServerException(String className,
                                                                         String methodName,
                                                                         org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException ocfException)
    {
        String ocfMessage = ocfException.getReportedErrorMessage();
        return new DinoViewServiceException(DinoViewErrorCode.INVALID_PARAMETER.getMessageDefinition(methodName, ocfMessage),
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
        String omagMessage = omagException.getReportedErrorMessage();
        return new DinoViewServiceException(DinoViewErrorCode.INVALID_PARAMETER.getMessageDefinition(methodName, omagMessage),
                                            className,
                                            methodName);
    }

    public static DinoViewServiceException mapOMAGNotAuthorizedException(String className,
                                                                         String methodName,
                                                                         OMAGNotAuthorizedException omagException)
    {
        String omagMessage = omagException.getReportedErrorMessage();
        return new DinoViewServiceException(DinoViewErrorCode.INVALID_PARAMETER.getMessageDefinition(methodName, omagMessage),
                                            className,
                                            methodName);
    }

    public static DinoViewServiceException mapOMAGConfigurationErrorException(String className,
                                                                              String methodName,
                                                                              OMAGConfigurationErrorException omagException)
    {
        String omagMessage = omagException.getReportedErrorMessage();
        return new DinoViewServiceException(DinoViewErrorCode.INVALID_PARAMETER.getMessageDefinition(methodName, omagMessage),
                                            className,
                                            methodName);
    }
}
