/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.DetectUtils;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;


/**
 * SubjectAreaImpl is the OMAS client library implementation of the Subject Area OMAS.
 * This interface provides glossary authoring interface for subject area experts.
 */
public class SubjectAreaGlossaryImpl extends SubjectAreaBaseImpl implements SubjectAreaGlossary {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaGlossaryImpl.class);

    private static final String className = SubjectAreaGlossaryImpl.class.getName();

    private static final String BASE_URL = SubjectAreaImpl.SUBJECT_AREA_BASE_URL + "glossaries";


    /**
     * Constructor for no authentication.
     *
     * @param serverName            name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException there is a problem creating the client-side components to issue any
     *                                                                                    REST API calls.
     */
    public SubjectAreaGlossaryImpl(String serverName, String serverPlatformURLRoot) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
    }

   @Override
    public Glossary createGlossary(String userId, Glossary suppliedGlossary) throws MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException, UnrecognizedGUIDException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException, PropertyServerException {
        final String methodName = "createGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String urlTemplate = BASE_URL;
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, urlTemplate, suppliedGlossary);
        Glossary glossary = DetectUtils.detectAndReturnGlossary(className, methodName, response);
        // that the returned nodeType matches the requested one
        if (suppliedGlossary.getNodeType() != null && !suppliedGlossary.getNodeType().equals(glossary.getNodeType())) {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.UNEXPECTED_NODETYPE;

            ExceptionMessageDefinition messageDefinition = errorCode.getMessageDefinition();
            String propertyName = "NodeType";
            String propertyValue = suppliedGlossary.getNodeType().name();
            messageDefinition.setMessageParameters(propertyName,propertyValue);
            throw new InvalidParameterException(
                    messageDefinition,
                    className,
                    methodName,
                    propertyName,
                    propertyValue);

        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return glossary;
    }

    @Override
    public Glossary getGlossaryByGuid(String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException, UnexpectedResponseException, PropertyServerException {
        final String methodName = "getCategoryByGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        final String urlTemplate = BASE_URL + "/%s";
        SubjectAreaOMASAPIResponse response = getByIdRESTCall(userId, guid, methodName, urlTemplate);
        Glossary glossary = DetectUtils.detectAndReturnGlossary(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return glossary;
    }

    @Override
    public List<Line> getGlossaryRelationships(String userId, String guid,
                                               Date asOfTime,
                                               int offset,
                                               int pageSize,
                                               SequencingOrder sequencingOrder,
                                               String sequencingProperty) throws
                                                                          UserNotAuthorizedException,
                                                                          InvalidParameterException,
                                                                          FunctionNotSupportedException,
                                                                          UnexpectedResponseException,
                                                                          MetadataServerUncontactableException, PropertyServerException {
        final String methodName = "getGlossaryRelationships";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        List<Line> relationships = getRelationships(BASE_URL, userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return relationships;
    }

    @Override
    public List<Glossary> findGlossary(String userId,
                                       String searchCriteria,
                                       Date asOfTime,
                                       int offset,
                                       int pageSize,
                                       SequencingOrder sequencingOrder,
                                       String sequencingProperty) throws
                                                                  MetadataServerUncontactableException,
                                                                  UserNotAuthorizedException,
                                                                  InvalidParameterException,
                                                                  FunctionNotSupportedException,
                                                                  UnexpectedResponseException, PropertyServerException {

        final String methodName = "findGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = findRESTCall(userId,
                                                           methodName,
                                                           BASE_URL,
                                                           searchCriteria,
                                                           asOfTime,
                                                           offset,
                                                           pageSize,
                                                           sequencingOrder,
                                                           sequencingProperty);
        List<Glossary> glossaries = DetectUtils.detectAndReturnGlossaries(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return glossaries;
    }

    @Override
    public Glossary replaceGlossary(String userId, String guid, Glossary suppliedGlossary) throws
                                                                                           UnexpectedResponseException,
                                                                                           UserNotAuthorizedException,
                                                                                           InvalidParameterException,
                                                                                           MetadataServerUncontactableException,
                                                                                           PropertyServerException, FunctionNotSupportedException {
        final String methodName = "replaceGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        Glossary glossary = updateGlossary(userId, guid, suppliedGlossary, true);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return glossary;
    }

    @Override
    public Glossary updateGlossary(String userId, String guid, Glossary suppliedGlossary) throws UnexpectedResponseException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 InvalidParameterException,
                                                                                                 MetadataServerUncontactableException,
                                                                                                 PropertyServerException,
                                                                                                 FunctionNotSupportedException {
        final String methodName = "updateGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        Glossary glossary = updateGlossary(userId, guid, suppliedGlossary, false);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return glossary;

    }

    @Override
    public Glossary deleteGlossary(String userId, String guid) throws InvalidParameterException,
                                                                      MetadataServerUncontactableException,
                                                                      UserNotAuthorizedException,
                                                                      UnrecognizedGUIDException,
                                                                      FunctionNotSupportedException,
                                                                      UnexpectedResponseException,
                                                                      EntityNotDeletedException, PropertyServerException {
        final String methodName = "deleteGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        final String urlTemplate = BASE_URL + "/%s?isPurge=false";
        SubjectAreaOMASAPIResponse response = deleteEntityRESTCall(userId, guid, methodName, urlTemplate);
        Glossary glossary = DetectUtils.detectAndReturnGlossary(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return glossary;
    }

    @Override
    public void purgeGlossary(String userId, String guid) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 MetadataServerUncontactableException,
                                                                 UnrecognizedGUIDException,
                                                                 EntityNotPurgedException,
                                                                 UnexpectedResponseException,
                                                                 FunctionNotSupportedException, PropertyServerException {
        final String methodName = "purgeGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = BASE_URL + "/%s?isPurge=true";
        purgeEntityRESTCall(userId, guid, methodName, urlTemplate);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Update Glossary.
     * <p>
     * If the caller has chosen to incorporate the glossary name in their Glossary Terms qualified name, renaming the glossary will cause those
     * qualified names to mismatch the Glossary name.
     *
     * @param userId           userId under which the request is performed
     * @param guid             of the glossary to update
     * @param suppliedGlossary Glossary to be updated
     * @param isReplace        flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return the updated glossary.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    private Glossary updateGlossary(String userId, String guid, Glossary suppliedGlossary, boolean isReplace) throws
                                                                                                              UserNotAuthorizedException,
                                                                                                              InvalidParameterException,
                                                                                                              MetadataServerUncontactableException,
                                                                                                              UnexpectedResponseException,
                                                                                                              PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        final String urlTemplate = BASE_URL + "/%s?isReplace=%b";
        SubjectAreaOMASAPIResponse response = putRESTCall(userId, guid, isReplace, methodName, urlTemplate, suppliedGlossary);

        Glossary glossary = DetectUtils.detectAndReturnGlossary(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return glossary;
    }

    @Override
    public Glossary restoreGlossary(String userId, String guid) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       MetadataServerUncontactableException,
                                                                       UnrecognizedGUIDException,
                                                                       FunctionNotSupportedException,
                                                                       UnexpectedResponseException, PropertyServerException {
        final String methodName = "restoreGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        final String urlTemplate = BASE_URL + "/%s";
        SubjectAreaOMASAPIResponse response = restoreRESTCall(userId, guid, methodName, urlTemplate);

        Glossary glossary = DetectUtils.detectAndReturnGlossary(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return glossary;
    }
}
