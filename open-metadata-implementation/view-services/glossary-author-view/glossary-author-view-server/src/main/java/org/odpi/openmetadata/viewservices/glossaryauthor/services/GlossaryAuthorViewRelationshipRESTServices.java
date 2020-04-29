/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.services;


import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.viewservices.glossaryauthor.handlers.RelationshipHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The GlossaryAuthorViewRelationshipRESTServices provides the org.odpi.openmetadata.viewservices.glossaryauthor.services implementation of the Glossary Author Open Metadata
 * View Service (OMVS). This interface provides view Relationship authoring interfaces for subject area experts.
 */

public class GlossaryAuthorViewRelationshipRESTServices extends BaseGlossaryAuthorView {
    private static String className = GlossaryAuthorViewRelationshipRESTServices.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger(className);

    /**
     * Default constructor
     */
    public GlossaryAuthorViewRelationshipRESTServices() {

    }

    /**
     * Create a Hasa is the relationship between a spine object and a spine attribute.
     * Note that this method does not error if the relationship ends are not spine objects or spine attributes.
     * This allows the user to create terms then make them spine objects and spine attributes at a later stage.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @return response, when successful contains the created Hasa
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createTermHASARelationship(String serverName, String userId, Hasa termHASARelationship)
    {
        String                     restAPIName = "createTermHASARelationship";
        RESTCallToken              token       = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response    = null;
        AuditLog                   auditLog    = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Hasa createdTermHASARelationship = handler.createTermHASARelationship(userId, termHASARelationship);
            response = new TermHASARelationshipResponse(createdTermHASARelationship);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a Term HAS A relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the HAS A relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getTermHASARelationship(String serverName, String userId, String guid)
     {

        String restAPIName = "getTermHASARelationship";
         RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
         SubjectAreaOMASAPIResponse response = null;
         AuditLog auditLog = null;

         // should not be called without a supplied relationship - the calling layer should not allow this.
         try {
             auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
             RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
             Hasa updatedTermHASARelationship = handler.getTermHASARelationship(userId, guid);
             response = new TermHASARelationshipResponse(updatedTermHASARelationship);
         }  catch (Throwable error) {
             response = getResponseForError(error, auditLog, restAPIName);
         }
         restCallLogger.logRESTCallReturn(token, response.toString());
         return response;
    }

    /**
     * Update a Hasa is the relationship between a spine object and a spine attribute.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated Hasa
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateTermHASARelationship(String serverName, String userId, Hasa termHASARelationship, boolean isReplace)
     {
        String restAPIName = "updateTermHASARelationship";
         RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
         SubjectAreaOMASAPIResponse response = null;
         AuditLog auditLog = null;

         // should not be called without a supplied relationship - the calling layer should not allow this.
         try {
             auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
             RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
             if (isReplace) {
                 Hasa updatedTermHASARelationship = handler.replaceTermHASARelationship(userId, termHASARelationship);
                 response = new TermHASARelationshipResponse(updatedTermHASARelationship);
             } else {
                 Hasa updatedTermHASARelationship = handler.updateTermHASARelationship(userId, termHASARelationship);
                 response = new TermHASARelationshipResponse(updatedTermHASARelationship);
             }

         }  catch (Throwable error) {
             response = getResponseForError(error, auditLog, restAPIName);
         }
         restCallLogger.logRESTCallReturn(token, response.toString());
         return response;
    }

    /**
     * Delete a Term HAS A relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the HAS A relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteTermHASARelationship(String serverName, String userId, String guid, Boolean isPurge)
     {
        String restAPIName = "deleteTermHASARelationship";

         RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
         SubjectAreaOMASAPIResponse response = null;
         AuditLog auditLog = null;

         // should not be called without a supplied relationship - the calling layer should not allow this.
         try {
             auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
             RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
             if (isPurge) {
                 handler.purgeTermHASARelationship(userId, guid);
                 response = new VoidResponse();
             } else {
                 Hasa deletedTermHASARelationship = handler.deleteTermHASARelationship(userId, guid);
                 response = new TermHASARelationshipResponse(deletedTermHASARelationship);
             }
         }  catch (Throwable error) {
             response = getResponseForError(error, auditLog, restAPIName);
         }
         restCallLogger.logRESTCallReturn(token, response.toString());
         return response;
    }

    /**
     * Restore a Term HAS A relationship.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li> 
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreTermHASARelationship(String serverName, String userId, String guid)
     {
        String restAPIName = "restoreTermHASARelationship";
         RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
         SubjectAreaOMASAPIResponse response = null;
         AuditLog auditLog = null;

         // should not be called without a supplied relationship - the calling layer should not allow this.
         try {
             auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
             RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
             Hasa restoredTermHASARelationship = handler.restoreTermHASARelationship(userId, guid);
             response = new TermHASARelationshipResponse(restoredTermHASARelationship);
         }  catch (Throwable error) {
             response = getResponseForError(error, auditLog, restAPIName);
         }
         restCallLogger.logRESTCallReturn(token, response.toString());
         return response;
    }
    /**
     * Create a Related Term is a link between two similar Terms.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param relatedTerm          the Related Term relationship
     * @return response, when successful contains the created RelatedTerm
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createRelatedTerm(String serverName, String userId, RelatedTerm relatedTerm)
    {
        String restAPIName = "createRelatedTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            RelatedTerm createdRelatedTerm = handler.createRelatedTerm(userId, relatedTerm);
            response = new RelatedTermResponse(createdRelatedTerm);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get  a Related Term is a link between two similar Terms.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the related term relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getRelatedTerm(String serverName, String userId, String guid)
    {

        String restAPIName = "getRelatedTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            RelatedTerm updatedRelatedTerm = handler.getRelatedTerm(userId, guid);
            response = new RelatedTermResponse(updatedRelatedTerm);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a Related Term is a link between two similar Terms.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param relatedTerm the HASA relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated RelatedTerm
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateRelatedTerm(String serverName, String userId, RelatedTerm relatedTerm, boolean isReplace)
    {
        String restAPIName = "updateRelatedTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                RelatedTerm updatedRelatedTerm = handler.replaceRelatedTerm(userId,relatedTerm);
                response = new RelatedTermResponse(updatedRelatedTerm);
            } else {
                RelatedTerm updatedRelatedTerm = handler.updateRelatedTerm(userId,relatedTerm);
                response = new RelatedTermResponse(updatedRelatedTerm);
            }

        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a Related Term is a link between two similar Terms.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the RelatedTerm relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteRelatedTerm(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteRelatedTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeRelatedTerm(userId, guid);
                response = new VoidResponse();
            } else {
                RelatedTerm deletedRelatedTerm = handler.deleteRelatedTerm(userId, guid);
                response = new RelatedTermResponse(deletedRelatedTerm);
            }
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore Related Term is a link between two similar Terms.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreRelatedTerm(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreRelatedTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            RelatedTerm restoredRelatedTerm = handler.restoreRelatedTerm(userId, guid);
            response = new RelatedTermResponse(restoredRelatedTerm);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Create a synonym relationship, which is a link between glossary terms that have the same meaning.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param synonym              the synonym relationship
     * @return response, when successful contains the created synonym
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createSynonym(String serverName, String userId, Synonym synonym)
    {
        String restAPIName = "createSynonym";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Synonym createdSynonym = handler.createSynonymRelationship(userId, synonym);
            response = new SynonymRelationshipResponse(createdSynonym);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a synonym relationship, which is a link between glossary terms that have the same meaning.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Synonym relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getSynonym(String serverName, String userId, String guid)
    {

        String restAPIName = "getSynonym";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Synonym updatedSynonym = handler.getSynonymRelationship(userId, guid);
            response = new SynonymRelationshipResponse(updatedSynonym);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a  synonym relationship, which is a link between glossary terms that have the same meaning.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param synonym the synonym relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated synonym
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateSynonym(String serverName, String userId, Synonym synonym, boolean isReplace)
    {
        String restAPIName = "updateSynonym";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                Synonym updatedSynonym = handler.replaceSynonymRelationship(userId,synonym);
                response = new SynonymRelationshipResponse(updatedSynonym);
            } else {
                Synonym updatedSynonym = handler.updateSynonymRelationship(userId,synonym);
                response = new SynonymRelationshipResponse(updatedSynonym);
            }

        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a synonym relationship, which is a link between glossary terms that have the same meaning.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Synonym relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteSynonym(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteSynonym";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeSynonymRelationship(userId, guid);
                response = new VoidResponse();
            } else {
                Synonym deletedSynonym = handler.deleteSynonymRelationship(userId, guid);
                response = new SynonymRelationshipResponse(deletedSynonym);
            }
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore  synonym relationship, which is a link between glossary terms that have the same meaning.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreSynonym(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreSynonym";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Synonym restoredSynonym = handler.restoreSynonymRelationship(userId, guid);
            response = new SynonymRelationshipResponse(restoredSynonym);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Create an antonym relationship, which is a link between glossary terms that have the opposite meanings.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param antonym          the antonym relationship
     * @return response, when successful contains the created antonym
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createAntonym(String serverName, String userId, Antonym antonym)
    {
        String restAPIName = "createAntonym";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Antonym createdAntonym = handler.createAntonymRelationship(userId, antonym);
            response = new AntonymRelationshipResponse(createdAntonym);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a Antonym relationship, which is a link between glossary terms that have the same meaning.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Antonym relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getAntonym(String serverName, String userId, String guid)
    {

        String restAPIName = "getAntonym";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Antonym updatedAntonym = handler.getAntonymRelationship(userId, guid);
            response = new AntonymRelationshipResponse(updatedAntonym);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a  antonym relationship, which is a link between glossary terms that have the opposite meanings.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param antonym the antonym relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated antonym
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateAntonym(String serverName, String userId, Antonym antonym, boolean isReplace)
    {
        String restAPIName = "updateAntonym";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                Antonym updatedAntonym = handler.replaceAntonymRelationship(userId,antonym);
                response = new AntonymRelationshipResponse(updatedAntonym);
            } else {
                Antonym updatedAntonym = handler.updateAntonymRelationship(userId,antonym);
                response = new AntonymRelationshipResponse(updatedAntonym);
            }

        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a Antonym relationship, which is a link between glossary terms that have the same meaning.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Antonym relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteAntonym(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteAntonym";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeAntonymRelationship(userId, guid);
                response = new VoidResponse();
            } else {
                Antonym deletedAntonym = handler.deleteAntonymRelationship(userId, guid);
                response = new AntonymRelationshipResponse(deletedAntonym);
            }
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore Antonym relationship, which is a link between glossary terms that have the same meaning.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreAntonym(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreAntonym";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Antonym restoredAntonym = handler.restoreAntonymRelationship(userId, guid);
            response = new AntonymRelationshipResponse(restoredAntonym);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Create a translation relationship, which is a link between glossary terms to provide different natural language translation of the same concept.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param categoryAnchor          the categoryAnchor relationship
     * @return response, when successful contains the created categoryAnchor
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createTranslation(String serverName, String userId, Translation categoryAnchor)
    {
        String restAPIName = "createTranslation";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Translation createdTranslation = handler.createTranslationRelationship(userId, categoryAnchor);
            response = new TranslationRelationshipResponse(createdTranslation);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a translation relationship, which is a link between glossary terms to provide different natural language translation of the same concept.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Translation relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getTranslation(String serverName, String userId, String guid)
    {

        String restAPIName = "getTranslation";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Translation updatedTranslation = handler.getTranslationRelationship(userId, guid);
            response = new TranslationRelationshipResponse(updatedTranslation);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a translation relationship, which is a link between glossary terms to provide different natural language translation of the same concept.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param categoryAnchor the categoryAnchor relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated categoryAnchor
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateTranslation(String serverName, String userId, Translation categoryAnchor, boolean isReplace)
    {
        String restAPIName = "updateTranslation";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                Translation updatedTranslation = handler.replaceTranslationRelationship(userId,categoryAnchor);
                response = new TranslationRelationshipResponse(updatedTranslation);
            } else {
                Translation updatedTranslation = handler.updateTranslationRelationship(userId,categoryAnchor);
                response = new TranslationRelationshipResponse(updatedTranslation);
            }

        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a translation relationship, which is a link between glossary terms to provide different natural language translation of the same concept.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Translation relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteTranslation(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteTranslation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeTranslationRelationship(userId, guid);
                response = new VoidResponse();
            } else {
                Translation deletedTranslation = handler.deleteTranslationRelationship(userId, guid);
                response = new TranslationRelationshipResponse(deletedTranslation);
            }
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore a translation relationship, which is a link between glossary terms to provide different natural language translation of the same concept.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreTranslation(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreTranslation";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Translation restoredTranslation = handler.restoreTranslationRelationship(userId, guid);
            response = new TranslationRelationshipResponse(restoredTranslation);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Create a usedInContext relationship, which is a link between glossary terms, where one describes the context where the other one is valid to use.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param usedInContext          the usedInContext relationship
     * @return response, when successful contains the created usedInContext
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createUsedInContext(String serverName, String userId, UsedInContext usedInContext)
    {
        String restAPIName = "createUsedInContext";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            UsedInContext createdUsedInContext = handler.createUsedInContextRelationship(userId, usedInContext);
            response = new UsedInContextRelationshipResponse(createdUsedInContext);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a usedInContext relationship, which is a link between glossary terms, where one describes the context where the other one is valid to use.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the UsedInContext relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getUsedInContext(String serverName, String userId, String guid)
    {

        String restAPIName = "getUsedInContext";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            UsedInContext updatedUsedInContext = handler.getUsedInContextRelationship(userId, guid);
            response = new UsedInContextRelationshipResponse(updatedUsedInContext);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a usedInContext relationship, which is a link between glossary terms, where one describes the context where the other one is valid to use.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param usedInContext the usedInContext relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated usedInContext
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateUsedInContext(String serverName, String userId, UsedInContext usedInContext, boolean isReplace)
    {
        String restAPIName = "updateUsedInContext";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                UsedInContext updatedUsedInContext = handler.replaceUsedInContextRelationship(userId,usedInContext);
                response = new UsedInContextRelationshipResponse(updatedUsedInContext);
            } else {
                UsedInContext updatedUsedInContext = handler.updateUsedInContextRelationship(userId,usedInContext);
                response = new UsedInContextRelationshipResponse(updatedUsedInContext);
            }

        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a usedInContext relationship, which is a link between glossary terms, where one describes the context where the other one is valid to use.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the UsedInContext relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteUsedInContext(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteUsedInContext";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeUsedInContextRelationship(userId, guid);
                response = new VoidResponse();
            } else {
                UsedInContext deletedUsedInContext = handler.deleteUsedInContextRelationship(userId, guid);
                response = new UsedInContextRelationshipResponse(deletedUsedInContext);
            }
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore a usedInContext relationship, which is a link between glossary terms, where one describes the context where the other one is valid to use.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreUsedInContext(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreUsedInContext";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            UsedInContext restoredUsedInContext = handler.restoreUsedInContextRelationship(userId, guid);
            response = new UsedInContextRelationshipResponse(restoredUsedInContext);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Create a preferredTerm relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param preferredTerm          the preferredTerm relationship
     * @return response, when successful contains the created preferredTerm
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createPreferredTerm(String serverName, String userId, PreferredTerm preferredTerm)
    {
        String restAPIName = "createPreferredTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            PreferredTerm createdPreferredTerm = handler.createPreferredTermRelationship(userId, preferredTerm);
            response = new PreferredTermRelationshipResponse(createdPreferredTerm);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a preferredTerm relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the PreferredTerm relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getPreferredTerm(String serverName, String userId, String guid)
    {

        String restAPIName = "getPreferredTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            PreferredTerm updatedPreferredTerm = handler.getPreferredTermRelationship(userId, guid);
            response = new PreferredTermRelationshipResponse(updatedPreferredTerm);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a preferredTerm relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param preferredTerm the preferredTerm relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated preferredTerm
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updatePreferredTerm(String serverName, String userId, PreferredTerm preferredTerm, boolean isReplace)
    {
        String restAPIName = "updatePreferredTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                PreferredTerm updatedPreferredTerm = handler.replacePreferredTermRelationship(userId,preferredTerm);
                response = new PreferredTermRelationshipResponse(updatedPreferredTerm);
            } else {
                PreferredTerm updatedPreferredTerm = handler.updatePreferredTermRelationship(userId,preferredTerm);
                response = new PreferredTermRelationshipResponse(updatedPreferredTerm);
            }

        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a preferredTerm relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the PreferredTerm relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deletePreferredTerm(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deletePreferredTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgePreferredTermRelationship(userId, guid);
                response = new VoidResponse();
            } else {
                PreferredTerm deletedPreferredTerm = handler.deletePreferredTermRelationship(userId, guid);
                response = new PreferredTermRelationshipResponse(deletedPreferredTerm);
            }
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore a preferredTerm relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restorePreferredTerm(String serverName, String userId, String guid)
    {
        String restAPIName = "restorePreferredTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            PreferredTerm restoredPreferredTerm = handler.restorePreferredTermRelationship(userId, guid);
            response = new PreferredTermRelationshipResponse(restoredPreferredTerm);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Create a ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param validValue          the validValue relationship
     * @return response, when successful contains the created validValue
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createValidValue(String serverName, String userId, ValidValue validValue)
    {
        String restAPIName = "createValidValue";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ValidValue createdValidValue = handler.createValidValueRelationship(userId, validValue);
            response = new ValidValueRelationshipResponse(createdValidValue);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ValidValue relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getValidValue(String serverName, String userId, String guid)
    {

        String restAPIName = "getValidValue";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ValidValue updatedValidValue = handler.getValidValueRelationship(userId, guid);
            response = new ValidValueRelationshipResponse(updatedValidValue);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param validValue the validValue relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated validValue
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateValidValue(String serverName, String userId, ValidValue validValue, boolean isReplace)
    {
        String restAPIName = "updateValidValue";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                ValidValue updatedValidValue = handler.replaceValidValueRelationship(userId,validValue);
                response = new ValidValueRelationshipResponse(updatedValidValue);
            } else {
                ValidValue updatedValidValue = handler.updateValidValueRelationship(userId,validValue);
                response = new ValidValueRelationshipResponse(updatedValidValue);
            }

        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ValidValue relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteValidValue(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteValidValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeValidValueRelationship(userId, guid);
                response = new VoidResponse();
            } else {
                ValidValue deletedValidValue = handler.deleteValidValueRelationship(userId, guid);
                response = new ValidValueRelationshipResponse(deletedValidValue);
            }
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore a ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreValidValue(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreValidValue";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ValidValue restoredValidValue = handler.restoreValidValueRelationship(userId, guid);
            response = new ValidValueRelationshipResponse(restoredValidValue);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Create a replacementTerm relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param replacementTerm              the replacementTerm relationship
     * @return response, when successful contains the created replacementTerm
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createReplacementTerm(String serverName, String userId, ReplacementTerm replacementTerm)
    {
        String restAPIName = "createReplacementTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ReplacementTerm createdReplacementTerm = handler.createReplacementTermRelationship(userId, replacementTerm);
            response = new ReplacementRelationshipResponse(createdReplacementTerm);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a replacementTerm relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ReplacementTerm relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getReplacementTerm(String serverName, String userId, String guid)
    {

        String restAPIName = "getReplacementTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ReplacementTerm updatedReplacementTerm = handler.getReplacementTermRelationship(userId, guid);
            response = new ReplacementRelationshipResponse(updatedReplacementTerm);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a replacementTerm relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param replacementTerm the replacementTerm relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated replacementTerm
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateReplacementTerm(String serverName, String userId, ReplacementTerm replacementTerm, boolean isReplace)
    {
        String restAPIName = "updateReplacementTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                ReplacementTerm updatedReplacementTerm = handler.replaceReplacementTermRelationship(userId,replacementTerm);
                response = new ReplacementRelationshipResponse(updatedReplacementTerm);
            } else {
                ReplacementTerm updatedReplacementTerm = handler.updateReplacementTermRelationship(userId,replacementTerm);
                response = new ReplacementRelationshipResponse(updatedReplacementTerm);
            }

        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a replacementTerm relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ReplacementTerm relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteReplacementTerm(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteReplacementTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeReplacementTermRelationship(userId, guid);
                response = new VoidResponse();
            } else {
                ReplacementTerm deletedReplacementTerm = handler.deleteReplacementTermRelationship(userId, guid);
                response = new ReplacementRelationshipResponse(deletedReplacementTerm);
            }
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore a replacementTerm relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreReplacementTerm(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreReplacementTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ReplacementTerm restoredReplacementTerm = handler.restoreReplacementTermRelationship(userId, guid);
            response = new ReplacementRelationshipResponse(restoredReplacementTerm);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Create a termTYPEDBYRelationship relationship, which is a link between a spine attribute and its type.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termTYPEDBYRelationship          the termTYPEDBYRelationship relationship
     * @return response, when successful contains the created termTYPEDBYRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createTermTYPEDBYRelationship(String serverName, String userId, TypedBy termTYPEDBYRelationship)
    {
        String restAPIName = "createTermTYPEDBYRelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            TypedBy createdTermTYPEDBYRelationship = handler.createTermTYPEDBYRelationship(userId, termTYPEDBYRelationship);
            response = new TermTYPEDBYRelationshipResponse(createdTermTYPEDBYRelationship);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a termTYPEDBYRelationship relationship, which is a link between a spine attribute and its type.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the TypedBy relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getTermTYPEDBYRelationship(String serverName, String userId, String guid)
    {

        String restAPIName = "getTermTYPEDBYRelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            TypedBy updatedTermTYPEDBYRelationship = handler.getTermTYPEDBYRelationship(userId, guid);
            response = new TermTYPEDBYRelationshipResponse(updatedTermTYPEDBYRelationship);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a termTYPEDBYRelationship relationship, which is a link between a spine attribute and its type.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termTYPEDBYRelationship the termTYPEDBYRelationship relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated termTYPEDBYRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateTermTYPEDBYRelationship(String serverName, String userId, TypedBy termTYPEDBYRelationship, boolean isReplace)
    {
        String restAPIName = "updateTermTYPEDBYRelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                TypedBy updatedTermTYPEDBYRelationship = handler.replaceTermTYPEDBYRelationship(userId, termTYPEDBYRelationship);
                response = new TermTYPEDBYRelationshipResponse(updatedTermTYPEDBYRelationship);
            } else {
                TypedBy updatedTermTYPEDBYRelationship = handler.updateTermTYPEDBYRelationship(userId, termTYPEDBYRelationship);
                response = new TermTYPEDBYRelationshipResponse(updatedTermTYPEDBYRelationship);
            }

        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a termTYPEDBYRelationship relationship, which is a link between a spine attribute and its type.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the TypedBy relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteTermTYPEDBYRelationship(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteTermTYPEDBYRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeTermTYPEDBYRelationship(userId, guid);
                response = new VoidResponse();
            } else {
                TypedBy deletedTermTYPEDBYRelationship = handler.deleteTermTYPEDBYRelationship(userId, guid);
                response = new TermTYPEDBYRelationshipResponse(deletedTermTYPEDBYRelationship);
            }
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore a termTYPEDBYRelationship relationship, which is a link between a spine attribute and its type.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreTermTYPEDBYRelationship(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreTermTYPEDBYRelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            TypedBy restoredTermTYPEDBYRelationship = handler.restoreTypedByRelationship(userId, guid);
            response = new TermTYPEDBYRelationshipResponse(restoredTermTYPEDBYRelationship);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
    
    /**
     * Create iSARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param ISARelationship      the ISA Relationship
     * @return response, when successful contains the created Isa
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createIsaRelationship(String serverName, String userId, Isa ISARelationship)
    {
        String restAPIName = "createIsaRelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Isa createdISARelationship = handler.createIsaRelationship(userId, ISARelationship);
            response = new TermISARelationshipResponse(createdISARelationship);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get iSARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ISA Relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getISARelationship(String serverName, String userId, String guid)
    {

        String restAPIName = "getISARelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Isa updatedISARelationship = handler.getIsaRelationship(userId, guid);
            response = new TermISARelationshipResponse(updatedISARelationship);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update iSARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param ISARelationship the ISA Relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated Isa
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateISARelationship(String serverName, String userId, Isa ISARelationship, boolean isReplace)
    {
        String restAPIName = "updateISARelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                Isa updatedISARelationship = handler.replaceIsaRelationship(userId, ISARelationship);
                response = new TermISARelationshipResponse(updatedISARelationship);
            } else {
                Isa updatedISARelationship = handler.updateIsaRelationship(userId, ISARelationship);
                response = new TermISARelationshipResponse(updatedISARelationship);
            }

        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete iSARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ISA Relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteISARelationship(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteISARelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeIsaRelationship(userId, guid);
                response = new VoidResponse();
            } else {
                Isa deletedISARelationship = handler.deleteIsaRelationship(userId, guid);
                response = new TermISARelationshipResponse(deletedISARelationship);
            }
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore iSARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreISARelationship(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreISARelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Isa restoredISARelationship = handler.restoreIsaRelationship(userId, guid);
            response = new TermISARelationshipResponse(restoredISARelationship);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Create a termISATypeOFRelationship relationship, which is an inheritance relationship between two spine objects.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termISATypeOFRelationship          the termISATypeOFRelationship relationship
     * @return response, when successful contains the created termISATypeOFRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createTermISATypeOFRelationship(String serverName, String userId, IsaTypeOf termISATypeOFRelationship)
    {
        String restAPIName = "createTermISATypeOFRelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            IsaTypeOf createdTermISATypeOFRelationship = handler.createTermISATypeOFRelationship(userId, termISATypeOFRelationship);
            response = new TermISATYPEOFRelationshipResponse(createdTermISATypeOFRelationship);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a termISATypeOFRelationship relationship, which is an inheritance relationship between two spine objects.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the IsaTypeOf relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getTermISATypeOFRelationship(String serverName, String userId, String guid)
    {

        String restAPIName = "getTermISATypeOFRelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            IsaTypeOf updatedTermISATypeOFRelationship = handler.getTermISATypeOFRelationship(userId, guid);
            response = new TermISATYPEOFRelationshipResponse(updatedTermISATypeOFRelationship);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a termISATypeOFRelationship relationship, which is an inheritance relationship between two spine objects.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termISATypeOFRelationship the termISATypeOFRelationship relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated termISATypeOFRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateTermISATypeOFRelationship(String serverName, String userId, IsaTypeOf termISATypeOFRelationship, boolean isReplace)
    {
        String restAPIName = "updateTermISATypeOFRelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                IsaTypeOf updatedTermISATypeOFRelationship = handler.replaceTermISATypeOFRelationship(userId, termISATypeOFRelationship);
                response = new TermISATYPEOFRelationshipResponse(updatedTermISATypeOFRelationship);
            } else {
                IsaTypeOf updatedTermISATypeOFRelationship = handler.updateTermISATypeOFRelationship(userId, termISATypeOFRelationship);
                response = new TermISATYPEOFRelationshipResponse(updatedTermISATypeOFRelationship);
            }

        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a termISATypeOFRelationship relationship, which is an inheritance relationship between two spine objects.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the IsaTypeOf relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteTermISATypeOFRelationship(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteTermISATypeOFRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeTermISATypeOFRelationship(userId, guid);
                response = new VoidResponse();
            } else {
                IsaTypeOf deletedTermISATypeOFRelationship = handler.deleteTermISATypeOFRelationship(userId, guid);
                response = new TermISATYPEOFRelationshipResponse(deletedTermISATypeOFRelationship);
            }
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore a termISATypeOFRelationship relationship, which is an inheritance relationship between two spine objects.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreTermISATypeOFRelationship(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreTermISATypeOFRelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            IsaTypeOf restoredTermISATypeOFRelationship = handler.restoreIsaTypeOfRelationship(userId, guid);
            response = new TermISATYPEOFRelationshipResponse(restoredTermISATypeOFRelationship);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Create a termCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termCategorization   the termCategorization relationship
     * @return response, when successful contains the created termCategorization
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createTermCategorization(String serverName, String userId, Categorization termCategorization)
    {
        String restAPIName = "createTermCategorization";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Categorization createdTermCategorization = handler.createTermCategorizationRelationship(userId, termCategorization);
            response = new TermCategorizationRelationshipResponse(createdTermCategorization);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a termCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the TermCategorization relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getTermCategorization(String serverName, String userId, String guid)
    {

        String restAPIName = "getTermCategorization";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Categorization updatedTermCategorization = handler.getTermCategorizationRelationship(userId, guid);
            response = new TermCategorizationRelationshipResponse(updatedTermCategorization);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a termCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termCategorization the termCategorization relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated termCategorization
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateTermCategorization(String serverName, String userId, Categorization termCategorization, boolean isReplace)
    {
        String restAPIName = "updateTermCategorization";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                Categorization updatedTermCategorization = handler.replaceTermCategorizationRelationship(userId, termCategorization);
                response = new TermCategorizationRelationshipResponse(updatedTermCategorization);
            } else {
                Categorization updatedTermCategorization = handler.updateTermCategorizationRelationship(userId, termCategorization);
                response = new TermCategorizationRelationshipResponse(updatedTermCategorization);
            }

        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a termCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the TermCategorization relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteTermCategorization(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteTermCategorization";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeTermCategorizationRelationship(userId, guid);
                response = new VoidResponse();
            } else {
                Categorization deletedTermCategorization = handler.deleteTermCategorizationRelationship(userId, guid);
                response = new TermCategorizationRelationshipResponse(deletedTermCategorization);
            }
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore a termCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreTermCategorization(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreTermCategorization";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Categorization restoredTermCategorization = handler.restoreTermCategorizationRelationship(userId, guid);
            response = new TermCategorizationRelationshipResponse(restoredTermCategorization);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Create a termAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     *
     * Terms created using the Glossary Author OMVS cannot be created without a glossary and there can only be one glossary associated with a
     * Term. This method is to allow glossaries to be associated with Terms that have not been created via the Glossary Author OMVS (or the Subject Area OMAS)
     * or to recreate the TermAnchorRelationship relationship if it has been purged.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termAnchor          the termAnchor relationship
     * @return response, when successful contains the created termAnchor
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createTermAnchor(String serverName, String userId, TermAnchor termAnchor)
    {
        String restAPIName = "createTermAnchor";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            TermAnchor createdTermAnchor = handler.createTermAnchorRelationship(userId, termAnchor);
            response = new TermAnchorRelationshipResponse(createdTermAnchor);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a termAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the TermAnchor relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getTermAnchor(String serverName, String userId, String guid)
    {

        String restAPIName = "getTermAnchor";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            TermAnchor updatedTermAnchor = handler.getTermAnchorRelationship(userId, guid);
            response = new TermAnchorRelationshipResponse(updatedTermAnchor);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a termAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termAnchor the termAnchor relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated termAnchor
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateTermAnchor(String serverName, String userId, TermAnchor termAnchor, boolean isReplace)
    {
        String restAPIName = "updateTermAnchor";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                TermAnchor updatedTermAnchor = handler.replaceTermAnchorRelationship(userId, termAnchor);
                response = new TermAnchorRelationshipResponse(updatedTermAnchor);
            } else {
                TermAnchor updatedTermAnchor = handler.updateTermAnchorRelationship(userId, termAnchor);
                response = new TermAnchorRelationshipResponse(updatedTermAnchor);
            }

        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a termAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the TermAnchor relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteTermAnchor(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteTermAnchor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeTermAnchorRelationship(userId, guid);
                response = new VoidResponse();
            } else {
                TermAnchor deletedTermAnchor = handler.deleteTermAnchorRelationship(userId, guid);
                response = new TermAnchorRelationshipResponse(deletedTermAnchor);
            }
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore a termAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreTermAnchor(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreTermAnchor";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            TermAnchor restoredTermAnchor = handler.restoreTermAnchorRelationship(userId, guid);
            response = new TermAnchorRelationshipResponse(restoredTermAnchor);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Create a categoryAnchor Relationship. A relationship between a Glossary and a Category. This relationship allows Categories to be owned by a Glossary.
     *
     * Categories created using the Glossary Author OMVS cannot be created without a glossary and there can only be one glossary associated with a
     * Term. This method is to allow glossaries to be associated with Categories that have not been created via the Glossary Author OMVS (or the Subject Area OMAS)
     * or to recreate the CategoryAnchorRelationship relationship if it has been purged.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param categoryAnchor       the categoryAnchor relationship
     * @return response, when successful contains the created categoryAnchor
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createCategoryAnchor(String serverName, String userId, CategoryAnchor categoryAnchor)
    {
        String restAPIName = "createCategoryAnchor";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            CategoryAnchor createdCategoryAnchor = handler.createCategoryAnchorRelationship(userId, categoryAnchor);
            response = new CategoryAnchorRelationshipResponse(createdCategoryAnchor);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a categoryAnchor Relationship. A relationship between a Glossary and a Category. This relationship allows Categories to be owned by a Glossary.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the CategoryAnchor relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getCategoryAnchor(String serverName, String userId, String guid)
    {

        String restAPIName = "getCategoryAnchor";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            CategoryAnchor updatedCategoryAnchor = handler.getCategoryAnchorRelationship(userId, guid);
            response = new CategoryAnchorRelationshipResponse(updatedCategoryAnchor);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a categoryAnchor Relationship. A relationship between a Glossary and a Category. This relationship allows Categories to be owned by a Glossary.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param categoryAnchor the categoryAnchor relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated categoryAnchor
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateCategoryAnchor(String serverName, String userId, CategoryAnchor categoryAnchor, boolean isReplace)
    {
        String restAPIName = "updateCategoryAnchor";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                CategoryAnchor updatedCategoryAnchor = handler.replaceCategoryAnchorRelationship(userId, categoryAnchor);
                response = new CategoryAnchorRelationshipResponse(updatedCategoryAnchor);
            } else {
                CategoryAnchor updatedCategoryAnchor = handler.updateCategoryAnchorRelationship(userId, categoryAnchor);
                response = new CategoryAnchorRelationshipResponse(updatedCategoryAnchor);
            }

        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a categoryAnchor Relationship. A relationship between a Glossary and a Category. This relationship allows Categories to be owned by a Glossary.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the CategoryAnchor relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteCategoryAnchor(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteCategoryAnchor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeCategoryAnchorRelationship(userId, guid);
                response = new VoidResponse();
            } else {
                CategoryAnchor deletedCategoryAnchor = handler.deleteCategoryAnchorRelationship(userId, guid);
                response = new CategoryAnchorRelationshipResponse(deletedCategoryAnchor);
            }
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore a categoryAnchor Relationship. A relationship between a Glossary and a Category. This relationship allows Categories to be owned by a Glossary.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreCategoryAnchor(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreCategoryAnchor";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            CategoryAnchor restoredCategoryAnchor = handler.restoreCategoryAnchorRelationship(userId, guid);
            response = new CategoryAnchorRelationshipResponse(restoredCategoryAnchor);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
    /**
     * Create a project scope relationship, which is a link between the project content and the project.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param projectScope       the projectScope relationship
     * @return response, when successful contains the created projectScope
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createProjectScope(String serverName, String userId, ProjectScope projectScope)
    {
        String restAPIName = "createProjectScope";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ProjectScope createdProjectScope = handler.createProjectScopeRelationship(userId, projectScope);
            response = new ProjectScopeRelationshipResponse(createdProjectScope);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a project scope relationship, which is a link between the project content and the project.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ProjectScope relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getProjectScope(String serverName, String userId, String guid)
    {

        String restAPIName = "getProjectScope";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ProjectScope updatedProjectScope = handler.getProjectScopeRelationship(userId, guid);
            response = new ProjectScopeRelationshipResponse(updatedProjectScope);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a project scope relationship, which is a link between the project content and the project.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param projectScope the projectScope relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated projectScope
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateProjectScope(String serverName, String userId, ProjectScope projectScope, boolean isReplace)
    {
        String restAPIName = "updateProjectScope";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                ProjectScope updatedProjectScope = handler.replaceProjectScopeRelationship(userId, projectScope);
                response = new ProjectScopeRelationshipResponse(updatedProjectScope);
            } else {
                ProjectScope updatedProjectScope = handler.updateProjectScopeRelationship(userId, projectScope);
                response = new ProjectScopeRelationshipResponse(updatedProjectScope);
            }

        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a project scope relationship, which is a link between the project content and the project.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ProjectScope relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteProjectScope(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteProjectScope";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeProjectScopeRelationship(userId, guid);
                response = new VoidResponse();
            } else {
                ProjectScope deletedProjectScope = handler.deleteProjectScopeRelationship(userId, guid);
                response = new ProjectScopeRelationshipResponse(deletedProjectScope);
            }
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore a project scope relationship, which is a link between the project content and the project.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreProjectScope(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreProjectScope";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ProjectScope restoredProjectScope = handler.restoreProjectScopeRelationship(userId, guid);
            response = new ProjectScopeRelationshipResponse(restoredProjectScope);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
    /**
     * Get a SemanticAssignment relationship,  Links a glossary term to another element such as an asset or schema element to define its meaning.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the SemanticAssignment relationship to get
     * @return response which when successful contains the SemanticAssignment relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getSemanticAssignmentRelationship(String serverName, String userId, String guid)
     {
         String restAPIName = "getSemanticAssignmentRelationship";
         RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
         SubjectAreaOMASAPIResponse response = null;
         AuditLog auditLog = null;

         // should not be called without a supplied relationship - the calling layer should not allow this.
         try {
             auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
             RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
             SemanticAssignment updatedSemanticAssignment = handler.getSemanticAssignmentRelationship(userId, guid);
             response = new SemanticAssignementRelationshipResponse(updatedSemanticAssignment);
         }  catch (Throwable error) {
             response = getResponseForError(error, auditLog, restAPIName);
         }
         restCallLogger.logRESTCallReturn(token, response.toString());
         return response;
    }
}