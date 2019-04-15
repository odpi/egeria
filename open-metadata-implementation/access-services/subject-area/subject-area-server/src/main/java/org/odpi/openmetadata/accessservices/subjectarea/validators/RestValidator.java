/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.validators;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.MetadataServerUncontactableException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.responses.OMASExceptionToResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaCategoryRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaGlossaryRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaTermRESTServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Validator methods used for rest
 */
public class RestValidator {
    private static final Logger log = LoggerFactory.getLogger(RestValidator.class);

    private static final String className = SubjectAreaTermRESTServices.class.getName();
    /**
     * Throw an exception if a org.odpi.openmetadata.accessservices.subjectarea.server URL has not been supplied on the constructor.
     *
     * @param className - name of the class making the call.
     * @param methodName - name of the method making the call.
     * @param omasServerURL - omas server url.
     * @throws MetadataServerUncontactableException not able to communicate with the metadata server.
     */
    static public void validateOMASServerURLNotNull( String className,String methodName, String omasServerURL) throws MetadataServerUncontactableException
    {
        if (omasServerURL == null)
        {
            /*
             * It is not possible to retrieve a connection without knowledge of where the OMAS Server is located.
             */
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.SERVER_URL_NOT_SPECIFIED;
            String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }




    /**
     * This method validated for creation.
     * TODO need another method for update where we could use the relationship guid.
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId  userId under which the request is performed
     * @param methodName method making the call
     * @param suppliedGlossary glossary to validate against.
     * @param glossaryRESTServices rest services for glossary.

     * @return SubjectAreaOMASAPIResponse this response is of type ResponseCategory.Category.Glossary if successful, otherwise there is an error response.
     */
    static public SubjectAreaOMASAPIResponse validateGlossarySummaryDuringCreation(String serverName,
                                                                                   String userId,
                                                                                   String methodName,
                                                                                   GlossarySummary suppliedGlossary,
                                                                                   SubjectAreaGlossaryRESTServices glossaryRESTServices

    ) {
        SubjectAreaOMASAPIResponse response = null;
        String guid =null;
        String relationshipGuid =null;
        /*
         * There needs to be an associated glossary supplied
         * The glossary could be of NodeType Glossary, Taxonomy , Canonical glossary or canonical and taxonomy.
         * The Glossary summary contains 4 identifying fields. We only require one of these fields to be supplied.
         * If more than one is supplied then we look for a glossary matching the supplied guid then matching the name.
         * Note if a relationship guid is supplied - then we reject this request - as the relationship cannot exist before one of its ends exists.
         */

        if (suppliedGlossary == null ) {
            // error - glossary is mandatory
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CREATE_WITHOUT_GLOSSARY;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(className,
                    methodName);
            log.error(errorMessage);
            InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } else
        {
            guid = suppliedGlossary.getGuid();
            relationshipGuid = suppliedGlossary.getRelationshipguid();
            if (relationshipGuid != null)
            {
                // glossary relationship cannot exist before the Term exists.
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CREATE_WITH_GLOSSARY_RELATIONSHIP;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName, relationshipGuid);
                log.error(errorMessage);
                InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
            if (response == null)
            {
                if (guid == null)
                {
                    // error -  glossary guid is mandatory
                    SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CREATE_WITHOUT_GLOSSARY;
                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName);
                    log.error(errorMessage);
                    InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
                    response = OMASExceptionToResponse.convertInvalidParameterException(e);
                } else
                {
                    // find by guid
                    response = glossaryRESTServices.getGlossaryByGuid(serverName, userId, guid);
                    // set error code in case we failed
                    SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CREATE_WITH_NON_EXISTANT_GLOSSARY_GUID;
                    if (response.getResponseCategory()!= ResponseCategory.Glossary) {
                        // glossary relationship cannot exist before the Term exists.
                        String errorMessage = errorCode.getErrorMessageId()
                                + errorCode.getFormattedErrorMessage(className,
                                methodName, relationshipGuid);
                        log.error(errorMessage);
                        InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                className,
                                methodName,
                                errorMessage,
                                errorCode.getSystemAction(),
                                errorCode.getUserAction());
                        response = OMASExceptionToResponse.convertInvalidParameterException(e);
                    }
                }
            }
        }
        return response;
    }
    /**
     * This method validated for creation.
     * TODO need another method for update where we could use the relationship guid.
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId  userId under which the request is performed
     * @param methodName - method making the call
     * @param suppliedCategory - category to validate against.
     * @param categoryRESTServices - rest services for glossary.

     * @return SubjectAreaOMASAPIResponse this response is of type ResponseCategory.Category.Caregory if successful, otherwise there is an error response.
     */
    static public SubjectAreaOMASAPIResponse validateCategorySummaryDuringCreation(String serverName, String userId,String methodName,
                                                                                   CategorySummary suppliedCategory,
                                                                                   SubjectAreaCategoryRESTServices categoryRESTServices

    ) {
        SubjectAreaOMASAPIResponse response = null;
        String guid =null;
        String relationshipGuid =null;
        String name = null;

        /*
             * There needs to be an associated glossary supplied
             * The glossary could be of NodeType Glossary, Taxonomy , Canonical glossary or canonical and taxonomy.
             * The Glossary summary contains 4 identifying fields. We only require one of these fields to be supplied.
             * If more than one is supplied then we look for a glossary matching the supplied guid then matching the name.
             * Note if a relationship guid is supplied - then we reject this request - as the relationship cannot exist before one of its ends exists.
             */

        if (suppliedCategory != null ) {
            guid = suppliedCategory.getGuid();
            relationshipGuid = suppliedCategory.getRelationshipguid();
            name = suppliedCategory.getName();

            if (relationshipGuid !=null) {

                // glossary relationship cannot exist before the Term exists.
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CREATE_WITH_CATEGORY_RELATIONSHIP;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(className,
                        methodName, relationshipGuid);
                log.error(errorMessage);
                InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                        className,
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }

            if (guid==null) {
                // error -  if a category is supplied it needs a valid guid in it
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CREATE_WITH_NON_EXISTANT_CATEGORY_GUID;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(className,
                        methodName);
                log.error(errorMessage);
                InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                        className,
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }

            if (response ==null && guid!=null ) {
                response = categoryRESTServices.getCategory(serverName, userId,guid);
                if (!response.getResponseCategory().equals(ResponseCategory.Category.Category)) {
                    // category not found
                    SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CREATE_WITH_NON_EXISTANT_CATEGORY_GUID;
                    String errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage(className,
                            methodName, name);
                    log.error(errorMessage);
                    InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            className,
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                    response = OMASExceptionToResponse.convertInvalidParameterException(e);
                }
            }
        }
        return response;
    }
}
