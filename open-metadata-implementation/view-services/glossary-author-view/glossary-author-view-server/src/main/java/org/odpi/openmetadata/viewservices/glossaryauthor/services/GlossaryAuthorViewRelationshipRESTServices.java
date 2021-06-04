/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.services;


import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.viewservices.glossaryauthor.handlers.RelationshipHandler;


/**
 * The GlossaryAuthorViewRelationshipRESTServices provides the org.odpi.openmetadata.viewservices.glossaryauthor.services implementation of the Glossary Author Open Metadata
 * View Service (OMVS). This interface provides view Relationship authoring interfaces for subject area experts.
 */

public class GlossaryAuthorViewRelationshipRESTServices extends BaseGlossaryAuthorView {
    private static String className = GlossaryAuthorViewRelationshipRESTServices.class.getName();
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
     * @param termHasARelationship the HasA relationship
     * @return response, when successful contains the created Hasa
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<HasA> createTermHasARelationship(String serverName, String userId, HasA termHasARelationship)
    {
        String                     restAPIName = "createTermHasARelationship";
        RESTCallToken              token       = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<HasA> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog                   auditLog    = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            HasA createdTermHasARelationship = handler.createTermHasARelationship(userId, termHasARelationship);
            response.addResult(createdTermHasARelationship);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a Term HAS A relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the HasA relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<HasA> getTermHasARelationship(String serverName, String userId, String guid)
     {

        String restAPIName = "getTermHasARelationship";
         RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
         SubjectAreaOMASAPIResponse<HasA> response = new SubjectAreaOMASAPIResponse<>();
         AuditLog auditLog = null;

         // should not be called without a supplied relationship - the calling layer should not allow this.
         try {
             auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
             RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
             HasA updatedTermHasARelationship = handler.getTermHasARelationship(userId, guid);
             response.addResult(updatedTermHasARelationship);
         }  catch (Exception exception) {
             response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * @param guid       guid of the HasA relationship
     * @param termHasARelationship the HasA relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated Hasa
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<HasA> updateTermHasARelationship(String serverName, String userId, String guid, HasA termHasARelationship, boolean isReplace)
     {
        String restAPIName = "updateTermHasARelationship";
         RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
         SubjectAreaOMASAPIResponse<HasA> response = new SubjectAreaOMASAPIResponse<>();
         AuditLog auditLog = null;

         // should not be called without a supplied relationship - the calling layer should not allow this.
         try {
             auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
             RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
             if (isReplace) {
                 HasA updatedTermHasARelationship = handler.replaceTermHasARelationship(userId, guid, termHasARelationship);
                 response.addResult(updatedTermHasARelationship);
             } else {
                 HasA updatedTermHasARelationship = handler.updateTermHasARelationship(userId, guid, termHasARelationship);
                 response.addResult(updatedTermHasARelationship);
             }

         }  catch (Exception exception) {
             response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<HasA> deleteTermHasARelationship(String serverName, String userId, String guid, boolean isPurge)
     {
        String restAPIName = "deleteTermHasARelationship";

         RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
         SubjectAreaOMASAPIResponse<HasA> response = new SubjectAreaOMASAPIResponse<>();
         AuditLog auditLog = null;

         // should not be called without a supplied relationship - the calling layer should not allow this.
         try {
             auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
             RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
             if (isPurge) {
                 handler.purgeTermHasARelationship(userId, guid);
             } else {
                 handler.deleteTermHasARelationship(userId, guid);
             }
         }  catch (Exception exception) {
             response = getResponseForException(exception, auditLog, className, restAPIName);
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
    public SubjectAreaOMASAPIResponse<HasA> restoreTermHasARelationship(String serverName, String userId, String guid)
     {
        String restAPIName = "restoreTermHasARelationship";
         RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
         SubjectAreaOMASAPIResponse<HasA> response = new SubjectAreaOMASAPIResponse<>();
         AuditLog auditLog = null;

         // should not be called without a supplied relationship - the calling layer should not allow this.
         try {
             auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
             RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
             HasA restoredTermHasARelationship = handler.restoreTermHasARelationship(userId, guid);
             response.addResult(restoredTermHasARelationship);
         }  catch (Exception exception) {
             response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<RelatedTerm> createRelatedTerm(String serverName, String userId, RelatedTerm relatedTerm)
    {
        String restAPIName = "createRelatedTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<RelatedTerm> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            RelatedTerm createdRelatedTerm = handler.createRelatedTerm(userId, relatedTerm);
            response.addResult(createdRelatedTerm);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<RelatedTerm> getRelatedTerm(String serverName, String userId, String guid)
    {

        String restAPIName = "getRelatedTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<RelatedTerm> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            RelatedTerm updatedRelatedTerm = handler.getRelatedTerm(userId, guid);
            response.addResult(updatedRelatedTerm);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * @param guid       guid of the related term relationship
     * @param relatedTerm the HasA relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated RelatedTerm
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<RelatedTerm> updateRelatedTerm(String serverName, String userId, String guid, RelatedTerm relatedTerm, boolean isReplace)
    {
        String restAPIName = "updateRelatedTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<RelatedTerm> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                RelatedTerm updatedRelatedTerm = handler.replaceRelatedTerm(userId, guid, relatedTerm);
                response.addResult(updatedRelatedTerm);
            } else {
                RelatedTerm updatedRelatedTerm = handler.updateRelatedTerm(userId, guid, relatedTerm);
                response.addResult(updatedRelatedTerm);
            }

        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<RelatedTerm> deleteRelatedTerm(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteRelatedTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<RelatedTerm> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeRelatedTerm(userId, guid);
            } else {
                handler.deleteRelatedTerm(userId, guid);
            }
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<RelatedTerm> restoreRelatedTerm(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreRelatedTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<RelatedTerm> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            RelatedTerm restoredRelatedTerm = handler.restoreRelatedTerm(userId, guid);
            response.addResult(restoredRelatedTerm);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Synonym> createSynonym(String serverName, String userId, Synonym synonym)
    {
        String restAPIName = "createSynonym";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Synonym> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Synonym createdSynonym = handler.createSynonymRelationship(userId, synonym);
            response.addResult(createdSynonym);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Synonym> getSynonym(String serverName, String userId, String guid)
    {

        String restAPIName = "getSynonym";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Synonym> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Synonym updatedSynonym = handler.getSynonymRelationship(userId, guid);
            response.addResult(updatedSynonym);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * @param guid       guid of the Synonym relationship
     * @param synonym the synonym relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated synonym
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Synonym> updateSynonym(String serverName, String userId, String guid, Synonym synonym, boolean isReplace)
    {
        String restAPIName = "updateSynonym";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Synonym> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                Synonym updatedSynonym = handler.replaceSynonymRelationship(userId, guid, synonym);
                response.addResult(updatedSynonym);
            } else {
                Synonym updatedSynonym = handler.updateSynonymRelationship(userId, guid, synonym);
                response.addResult(updatedSynonym);
            }

        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Synonym> deleteSynonym(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteSynonym";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Synonym> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeSynonymRelationship(userId, guid);
            } else {
                handler.deleteSynonymRelationship(userId, guid);
            }
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Synonym> restoreSynonym(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreSynonym";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Synonym> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Synonym restoredSynonym = handler.restoreSynonymRelationship(userId, guid);
            response.addResult(restoredSynonym);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Antonym> createAntonym(String serverName, String userId, Antonym antonym)
    {
        String restAPIName = "createAntonym";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Antonym> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Antonym createdAntonym = handler.createAntonymRelationship(userId, antonym);
            response.addResult(createdAntonym);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Antonym> getAntonym(String serverName, String userId, String guid)
    {

        String restAPIName = "getAntonym";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Antonym> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Antonym updatedAntonym = handler.getAntonymRelationship(userId, guid);
            response.addResult(updatedAntonym);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * @param guid       guid of the Antonym relationship
     * @param antonym the antonym relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated antonym
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Antonym> updateAntonym(String serverName, String userId, String guid, Antonym antonym, boolean isReplace)
    {
        String restAPIName = "updateAntonym";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Antonym> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                Antonym updatedAntonym = handler.replaceAntonymRelationship(userId, guid, antonym);
                response.addResult(updatedAntonym);
            } else {
                Antonym updatedAntonym = handler.updateAntonymRelationship(userId, guid, antonym);
                response.addResult(updatedAntonym);
            }

        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Antonym> deleteAntonym(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteAntonym";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Antonym> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeAntonymRelationship(userId, guid);
            } else {
                handler.deleteAntonymRelationship(userId, guid);
            }
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Antonym> restoreAntonym(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreAntonym";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Antonym> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Antonym restoredAntonym = handler.restoreAntonymRelationship(userId, guid);
            response.addResult(restoredAntonym);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Translation> createTranslation(String serverName, String userId, Translation categoryAnchor)
    {
        String restAPIName = "createTranslation";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Translation> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Translation createdTranslation = handler.createTranslationRelationship(userId, categoryAnchor);
            response.addResult(createdTranslation);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Translation> getTranslation(String serverName, String userId, String guid)
    {

        String restAPIName = "getTranslation";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Translation> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Translation updatedTranslation = handler.getTranslationRelationship(userId, guid);
            response.addResult(updatedTranslation);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * @param guid       guid of the Translation relationship
     * @param categoryAnchor the categoryAnchor relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated categoryAnchor
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Translation> updateTranslation(String serverName, String userId, String guid, Translation categoryAnchor, boolean isReplace)
    {
        String restAPIName = "updateTranslation";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Translation> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                Translation updatedTranslation = handler.replaceTranslationRelationship(userId, guid, categoryAnchor);
                response.addResult(updatedTranslation);
            } else {
                Translation updatedTranslation = handler.updateTranslationRelationship(userId, guid, categoryAnchor);
                response.addResult(updatedTranslation);
            }

        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Translation> deleteTranslation(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteTranslation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Translation> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeTranslationRelationship(userId, guid);
            } else {
                handler.deleteTranslationRelationship(userId, guid);
            }
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Translation> restoreTranslation(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreTranslation";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Translation> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Translation restoredTranslation = handler.restoreTranslationRelationship(userId, guid);
            response.addResult(restoredTranslation);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<UsedInContext> createUsedInContext(String serverName, String userId, UsedInContext usedInContext)
    {
        String restAPIName = "createUsedInContext";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<UsedInContext> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            UsedInContext createdUsedInContext = handler.createUsedInContextRelationship(userId, usedInContext);
            response.addResult(createdUsedInContext);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<UsedInContext> getUsedInContext(String serverName, String userId, String guid)
    {

        String restAPIName = "getUsedInContext";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<UsedInContext> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            UsedInContext updatedUsedInContext = handler.getUsedInContextRelationship(userId, guid);
            response.addResult(updatedUsedInContext);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * @param guid       guid of the UsedInContext relationship
     * @param usedInContext the usedInContext relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated usedInContext
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<UsedInContext> updateUsedInContext(String serverName, String userId, String guid, UsedInContext usedInContext, boolean isReplace)
    {
        String restAPIName = "updateUsedInContext";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<UsedInContext> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                UsedInContext updatedUsedInContext = handler.replaceUsedInContextRelationship(userId, guid, usedInContext);
                response.addResult(updatedUsedInContext);
            } else {
                UsedInContext updatedUsedInContext = handler.updateUsedInContextRelationship(userId, guid, usedInContext);
                response.addResult(updatedUsedInContext);
            }

        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<UsedInContext> deleteUsedInContext(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteUsedInContext";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<UsedInContext> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeUsedInContextRelationship(userId, guid);
            } else {
                 handler.deleteUsedInContextRelationship(userId, guid);
            }
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<UsedInContext> restoreUsedInContext(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreUsedInContext";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<UsedInContext> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            UsedInContext restoredUsedInContext = handler.restoreUsedInContextRelationship(userId, guid);
            response.addResult(restoredUsedInContext);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<PreferredTerm> createPreferredTerm(String serverName, String userId, PreferredTerm preferredTerm)
    {
        String restAPIName = "createPreferredTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<PreferredTerm> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            PreferredTerm createdPreferredTerm = handler.createPreferredTermRelationship(userId, preferredTerm);
            response.addResult(createdPreferredTerm);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<PreferredTerm> getPreferredTerm(String serverName, String userId, String guid)
    {

        String restAPIName = "getPreferredTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<PreferredTerm> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            PreferredTerm updatedPreferredTerm = handler.getPreferredTermRelationship(userId, guid);
            response.addResult(updatedPreferredTerm);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * @param guid       guid of the PreferredTerm relationship
     * @param preferredTerm the preferredTerm relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated preferredTerm
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<PreferredTerm> updatePreferredTerm(String serverName, String userId, String guid, PreferredTerm preferredTerm, boolean isReplace)
    {
        String restAPIName = "updatePreferredTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<PreferredTerm> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                PreferredTerm updatedPreferredTerm = handler.replacePreferredTermRelationship(userId, guid, preferredTerm);
                response.addResult(updatedPreferredTerm);
            } else {
                PreferredTerm updatedPreferredTerm = handler.updatePreferredTermRelationship(userId, guid, preferredTerm);
                response.addResult(updatedPreferredTerm);
            }

        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<PreferredTerm> deletePreferredTerm(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deletePreferredTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<PreferredTerm> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgePreferredTermRelationship(userId, guid);
            } else {
                handler.deletePreferredTermRelationship(userId, guid);
            }
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<PreferredTerm> restorePreferredTerm(String serverName, String userId, String guid)
    {
        String restAPIName = "restorePreferredTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<PreferredTerm> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            PreferredTerm restoredPreferredTerm = handler.restorePreferredTermRelationship(userId, guid);
            response.addResult(restoredPreferredTerm);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<ValidValue> createValidValue(String serverName, String userId, ValidValue validValue)
    {
        String restAPIName = "createValidValue";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<ValidValue> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ValidValue createdValidValue = handler.createValidValueRelationship(userId, validValue);
            response.addResult(createdValidValue);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<ValidValue> getValidValue(String serverName, String userId, String guid)
    {

        String restAPIName = "getValidValue";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<ValidValue> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ValidValue updatedValidValue = handler.getValidValueRelationship(userId, guid);
            response.addResult(updatedValidValue);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * @param guid       guid of the ValidValue relationship
     * @param validValue the validValue relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated validValue
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<ValidValue> updateValidValue(String serverName, String userId, String guid, ValidValue validValue, boolean isReplace)
    {
        String restAPIName = "updateValidValue";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<ValidValue> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                ValidValue updatedValidValue = handler.replaceValidValueRelationship(userId, guid, validValue);
                response.addResult(updatedValidValue);
            } else {
                ValidValue updatedValidValue = handler.updateValidValueRelationship(userId, guid, validValue);
                response.addResult(updatedValidValue);
            }

        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<ValidValue> deleteValidValue(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteValidValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<ValidValue> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeValidValueRelationship(userId, guid);
            } else {
                handler.deleteValidValueRelationship(userId, guid);
            }
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<ValidValue> restoreValidValue(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreValidValue";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<ValidValue> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ValidValue restoredValidValue = handler.restoreValidValueRelationship(userId, guid);
            response.addResult(restoredValidValue);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<ReplacementTerm> createReplacementTerm(String serverName, String userId, ReplacementTerm replacementTerm)
    {
        String restAPIName = "createReplacementTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<ReplacementTerm> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ReplacementTerm createdReplacementTerm = handler.createReplacementTermRelationship(userId, replacementTerm);
            response.addResult(createdReplacementTerm);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<ReplacementTerm> getReplacementTerm(String serverName, String userId, String guid)
    {

        String restAPIName = "getReplacementTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<ReplacementTerm> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ReplacementTerm updatedReplacementTerm = handler.getReplacementTermRelationship(userId, guid);
            response.addResult(updatedReplacementTerm);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * @param guid                 guid of the ReplacementTerm relationship
     * @param replacementTerm the replacementTerm relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated replacementTerm
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<ReplacementTerm> updateReplacementTerm(String serverName, String userId, String guid, ReplacementTerm replacementTerm, boolean isReplace)
    {
        String restAPIName = "updateReplacementTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<ReplacementTerm> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isReplace) {
                ReplacementTerm updatedReplacementTerm = handler.replaceReplacementTermRelationship(userId, guid, replacementTerm);
                response.addResult(updatedReplacementTerm);
            } else {
                ReplacementTerm updatedReplacementTerm = handler.updateReplacementTermRelationship(userId, guid, replacementTerm);
                response.addResult(updatedReplacementTerm);
            }

        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<ReplacementTerm> deleteReplacementTerm(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteReplacementTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<ReplacementTerm> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeReplacementTermRelationship(userId, guid);
            } else {
                handler.deleteReplacementTermRelationship(userId, guid);
            }
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<ReplacementTerm> restoreReplacementTerm(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreReplacementTerm";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<ReplacementTerm> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ReplacementTerm restoredReplacementTerm = handler.restoreReplacementTermRelationship(userId, guid);
            response.addResult(restoredReplacementTerm);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<TypedBy> createTermTYPEDBYRelationship(String serverName, String userId, TypedBy termTYPEDBYRelationship)
    {
        String restAPIName = "createTermTYPEDBYRelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<TypedBy> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            TypedBy createdTermTYPEDBYRelationship = handler.createTermTYPEDBYRelationship(userId, termTYPEDBYRelationship);
            response.addResult(createdTermTYPEDBYRelationship);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<TypedBy> getTermTYPEDBYRelationship(String serverName, String userId, String guid)
    {

        String restAPIName = "getTermTYPEDBYRelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<TypedBy> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            TypedBy updatedTermTYPEDBYRelationship = handler.getTermTYPEDBYRelationship(userId, guid);
            response.addResult(updatedTermTYPEDBYRelationship);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * @param guid       guid of the TypedBy relationship
     * @param termTYPEDBYRelationship the termTYPEDBYRelationship relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated termTYPEDBYRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<TypedBy> updateTermTYPEDBYRelationship(String serverName, String userId, String guid, TypedBy termTYPEDBYRelationship, boolean isReplace)
    {
        String restAPIName = "updateTermTYPEDBYRelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<TypedBy> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            TypedBy updatedTermTYPEDBYRelationship;
            if (isReplace) {
                updatedTermTYPEDBYRelationship = handler.replaceTermTYPEDBYRelationship(userId, guid, termTYPEDBYRelationship);
            } else {
                updatedTermTYPEDBYRelationship = handler.updateTermTYPEDBYRelationship(userId, guid, termTYPEDBYRelationship);
            }

            response.addResult(updatedTermTYPEDBYRelationship);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<TypedBy> deleteTermTYPEDBYRelationship(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteTermTYPEDBYRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<TypedBy> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeTermTYPEDBYRelationship(userId, guid);
            } else {
                handler.deleteTermTYPEDBYRelationship(userId, guid);
            }
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<TypedBy> restoreTermTYPEDBYRelationship(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreTermTYPEDBYRelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<TypedBy> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            TypedBy restoredTermTYPEDBYRelationship = handler.restoreTypedByRelationship(userId, guid);
            response.addResult(restoredTermTYPEDBYRelationship);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * @param IsARelationship      the IsA Relationship
     * @return response, when successful contains the created Isa
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<IsA> createIsaRelationship(String serverName, String userId, IsA IsARelationship)
    {
        String restAPIName = "createIsaRelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<IsA> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            IsA createdIsARelationship = handler.createIsaRelationship(userId, IsARelationship);
            response.addResult(createdIsARelationship);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get iSARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the IsA Relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<IsA> getIsARelationship(String serverName, String userId, String guid)
    {

        String restAPIName = "getIsARelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<IsA> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            IsA updatedIsARelationship = handler.getIsaRelationship(userId, guid);
            response.addResult(updatedIsARelationship);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * @param guid       guid of the IsA Relationship
     * @param IsARelationship the IsA Relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated Isa
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<IsA> updateIsARelationship(String serverName, String userId, String guid, IsA IsARelationship, boolean isReplace)
    {
        String restAPIName = "updateIsARelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<IsA> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            IsA updatedIsARelationship;
            if (isReplace) {
                updatedIsARelationship = handler.replaceIsaRelationship(userId, guid, IsARelationship);
            } else {
                updatedIsARelationship = handler.updateIsaRelationship(userId, guid, IsARelationship);
            }
            response.addResult(updatedIsARelationship);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete iSARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the IsA Relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<IsA> deleteIsARelationship(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteIsARelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<IsA> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeIsaRelationship(userId, guid);
            } else {
                handler.deleteIsaRelationship(userId, guid);
            }
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<IsA> restoreIsARelationship(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreIsARelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<IsA> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            IsA restoredIsARelationship = handler.restoreIsaRelationship(userId, guid);
            response.addResult(restoredIsARelationship);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Create a termIsATypeOfRelationship relationship, which is an inheritance relationship between two spine objects.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termIsATypeOfRelationship          the termIsATypeOfRelationship relationship
     * @return response, when successful contains the created termIsATypeOfRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<IsATypeOf> createTermIsATypeOfRelationship(String serverName, String userId, IsATypeOf termIsATypeOfRelationship)
    {
        String restAPIName = "createTermIsATypeOfRelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<IsATypeOf> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            IsATypeOf createdTermIsATypeOfRelationship = handler.createTermIsATypeOfRelationship(userId, termIsATypeOfRelationship);
            response.addResult(createdTermIsATypeOfRelationship);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a termIsATypeOfRelationship relationship, which is an inheritance relationship between two spine objects.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the IsaTypeOf relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<IsATypeOf> getTermIsATypeOfRelationship(String serverName, String userId, String guid)
    {

        String restAPIName = "getTermIsATypeOfRelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<IsATypeOf> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            IsATypeOf updatedTermIsATypeOfRelationship = handler.getTermIsATypeOfRelationship(userId, guid);
            response.addResult(updatedTermIsATypeOfRelationship);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a termIsATypeOfRelationship relationship, which is an inheritance relationship between two spine objects.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param guid       guid of the IsaTypeOf relationship
     * @param termIsATypeOfRelationship the termIsATypeOfRelationship relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated termIsATypeOfRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<IsATypeOf> updateTermIsATypeOfRelationship(String serverName, String userId, String guid, IsATypeOf termIsATypeOfRelationship, boolean isReplace)
    {
        String restAPIName = "updateTermIsATypeOfRelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<IsATypeOf> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            IsATypeOf updatedTermIsATypeOfRelationship;
            if (isReplace) {
                updatedTermIsATypeOfRelationship = handler.replaceTermIsATypeOfRelationship(userId, guid, termIsATypeOfRelationship);
            } else {
                updatedTermIsATypeOfRelationship = handler.updateTermIsATypeOfRelationship(userId, guid, termIsATypeOfRelationship);
            }

            response.addResult(updatedTermIsATypeOfRelationship);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a termIsATypeOfRelationship relationship, which is an inheritance relationship between two spine objects.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the IsaTypeOf relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<IsATypeOf> deleteTermIsATypeOfRelationship(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteTermIsATypeOfRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<IsATypeOf> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeTermIsATypeOfRelationship(userId, guid);
            } else {
                handler.deleteTermIsATypeOfRelationship(userId, guid);
            }
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore a termIsATypeOfRelationship relationship, which is an inheritance relationship between two spine objects.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<IsATypeOf> restoreTermIsATypeOfRelationship(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreTermIsATypeOfRelationship";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<IsATypeOf> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            IsATypeOf restoredTermIsATypeOfRelationship = handler.restoreIsaTypeOfRelationship(userId, guid);
            response.addResult(restoredTermIsATypeOfRelationship);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Categorization> createTermCategorization(String serverName, String userId, Categorization termCategorization)
    {
        String restAPIName = "createTermCategorization";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Categorization> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Categorization createdTermCategorization = handler.createTermCategorizationRelationship(userId, termCategorization);
            response.addResult(createdTermCategorization);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Categorization> getTermCategorization(String serverName, String userId, String guid)
    {

        String restAPIName = "getTermCategorization";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Categorization> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Categorization updatedTermCategorization = handler.getTermCategorizationRelationship(userId, guid);
            response.addResult(updatedTermCategorization);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * @param guid       guid of the TermCategorization relationship
     * @param termCategorization the termCategorization relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated termCategorization
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Categorization> updateTermCategorization(String serverName, String userId, String guid, Categorization termCategorization, boolean isReplace)
    {
        String restAPIName = "updateTermCategorization";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Categorization> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Categorization updatedTermCategorization;
            if (isReplace) {
                updatedTermCategorization = handler.replaceTermCategorizationRelationship(userId, guid, termCategorization);
            } else {
                updatedTermCategorization = handler.updateTermCategorizationRelationship(userId, guid, termCategorization);
            }

            response.addResult(updatedTermCategorization);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Categorization> deleteTermCategorization(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteTermCategorization";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Categorization> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeTermCategorizationRelationship(userId, guid);
            } else {
                handler.deleteTermCategorizationRelationship(userId, guid);
            }
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Categorization> restoreTermCategorization(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreTermCategorization";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<Categorization> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            Categorization restoredTermCategorization = handler.restoreTermCategorizationRelationship(userId, guid);
            response.addResult(restoredTermCategorization);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<TermAnchor> createTermAnchor(String serverName, String userId, TermAnchor termAnchor)
    {
        String restAPIName = "createTermAnchor";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<TermAnchor> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            TermAnchor createdTermAnchor = handler.createTermAnchorRelationship(userId, termAnchor);
            response.addResult(createdTermAnchor);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<TermAnchor> getTermAnchor(String serverName, String userId, String guid)
    {

        String restAPIName = "getTermAnchor";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<TermAnchor> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            TermAnchor updatedTermAnchor = handler.getTermAnchorRelationship(userId, guid);
            response.addResult(updatedTermAnchor);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a termAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param guid       guid of the TermAnchor relationship
     * @param termAnchor the termAnchor relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated termAnchor
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<TermAnchor> updateTermAnchor(String serverName, String userId, String guid, TermAnchor termAnchor, boolean isReplace)
    {
        String restAPIName = "updateTermAnchor";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<TermAnchor> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            TermAnchor updatedTermAnchor;
            if (isReplace) {
                updatedTermAnchor = handler.replaceTermAnchorRelationship(userId, guid, termAnchor);
            } else {
                updatedTermAnchor = handler.updateTermAnchorRelationship(userId, guid, termAnchor);
            }

            response.addResult(updatedTermAnchor);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<TermAnchor> deleteTermAnchor(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteTermAnchor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<TermAnchor> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeTermAnchorRelationship(userId, guid);
            } else {
                handler.deleteTermAnchorRelationship(userId, guid);
            }
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<TermAnchor> restoreTermAnchor(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreTermAnchor";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<TermAnchor> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            TermAnchor restoredTermAnchor = handler.restoreTermAnchorRelationship(userId, guid);
            response.addResult(restoredTermAnchor);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<CategoryAnchor> createCategoryAnchor(String serverName, String userId, CategoryAnchor categoryAnchor)
    {
        String restAPIName = "createCategoryAnchor";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<CategoryAnchor> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            CategoryAnchor createdCategoryAnchor = handler.createCategoryAnchorRelationship(userId, categoryAnchor);
            response.addResult(createdCategoryAnchor);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<CategoryAnchor> getCategoryAnchor(String serverName, String userId, String guid)
    {

        String restAPIName = "getCategoryAnchor";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<CategoryAnchor> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            CategoryAnchor updatedCategoryAnchor = handler.getCategoryAnchorRelationship(userId, guid);
            response.addResult(updatedCategoryAnchor);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * @param guid       guid of the CategoryAnchor relationship
     * @param categoryAnchor the categoryAnchor relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated categoryAnchor
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<CategoryAnchor> updateCategoryAnchor(String serverName, String userId, String guid, CategoryAnchor categoryAnchor, boolean isReplace)
    {
        String restAPIName = "updateCategoryAnchor";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<CategoryAnchor> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            CategoryAnchor updatedCategoryAnchor;
            if (isReplace) {
                updatedCategoryAnchor = handler.replaceCategoryAnchorRelationship(userId, guid, categoryAnchor);
            } else {
                updatedCategoryAnchor = handler.updateCategoryAnchorRelationship(userId, guid, categoryAnchor);
            }

            response.addResult(updatedCategoryAnchor);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<CategoryAnchor> deleteCategoryAnchor(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteCategoryAnchor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<CategoryAnchor> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeCategoryAnchorRelationship(userId, guid);
            } else {
                handler.deleteCategoryAnchorRelationship(userId, guid);
            }
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<CategoryAnchor> restoreCategoryAnchor(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreCategoryAnchor";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<CategoryAnchor> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            CategoryAnchor restoredCategoryAnchor = handler.restoreCategoryAnchorRelationship(userId, guid);
            response.addResult(restoredCategoryAnchor);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<ProjectScope> createProjectScope(String serverName, String userId, ProjectScope projectScope)
    {
        String restAPIName = "createProjectScope";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<ProjectScope> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ProjectScope createdProjectScope = handler.createProjectScopeRelationship(userId, projectScope);
            response.addResult(createdProjectScope);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<ProjectScope> getProjectScope(String serverName, String userId, String guid)
    {
        String restAPIName = "getProjectScope";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<ProjectScope> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ProjectScope updatedProjectScope = handler.getProjectScopeRelationship(userId, guid);
            response.addResult(updatedProjectScope);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * @param guid       guid of the ProjectScope relationship
     * @param projectScope the projectScope relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated projectScope
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<ProjectScope> updateProjectScope(String serverName, String userId, String guid, ProjectScope projectScope, boolean isReplace)
    {
        String restAPIName = "updateProjectScope";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<ProjectScope> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ProjectScope updatedProjectScope;
            if (isReplace) {
                updatedProjectScope = handler.replaceProjectScopeRelationship(userId, guid, projectScope);
            } else {
                updatedProjectScope = handler.updateProjectScopeRelationship(userId, guid, projectScope);
            }

            response.addResult(updatedProjectScope);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<ProjectScope> deleteProjectScope(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteProjectScope";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<ProjectScope> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeProjectScopeRelationship(userId, guid);
            } else {
                handler.deleteProjectScopeRelationship(userId, guid);
            }
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<ProjectScope> restoreProjectScope(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreProjectScope";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<ProjectScope> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            ProjectScope restoredProjectScope = handler.restoreProjectScopeRelationship(userId, guid);
            response.addResult(restoredProjectScope);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Create a CategoryHierarchyLink Relationship. A relationship between two categories used to create nested categories.
     *
     * @param serverName            serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                userId under which the request is performed
     * @param categoryHierarchyLink the CategoryHierarchyLink relationship
     * @return response, when successful contains the created categoryHierarchyLink relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<CategoryHierarchyLink> createCategoryHierarchyLink(String serverName, String userId, CategoryHierarchyLink categoryHierarchyLink) {
        String restAPIName = "createCategoryHierarchyLink";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<CategoryHierarchyLink> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            CategoryHierarchyLink createdCategoryHierarchyLink = handler.createCategoryHierarchyLink(userId, categoryHierarchyLink);
            response.addResult(createdCategoryHierarchyLink);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Get a CategoryHierarchyLink Relationship. A relationship between two categories used to create nested categories.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the CategoryHierarchyLink Relationship to get
     * @return response which when successful contains the CategoryHierarchyLink relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<CategoryHierarchyLink> getCategoryHierarchyLink(String serverName, String userId, String guid) {
        String restAPIName = "getCategoryHierarchyLink";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<CategoryHierarchyLink> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            CategoryHierarchyLink categoryHierarchyLink = handler.getCategoryHierarchyLink(userId, guid);
            response.addResult(categoryHierarchyLink);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
    /**
     * Update a CategoryHierarchyLink Relationship. A relationship between two categories used to create nested categories.
     *
     * @param serverName            serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                userId under which the request is performed
     * @param guid                  guid of the CategoryHierarchyLink relationship
     * @param categoryHierarchyLink the CategoryHierarchyLink relationship
     * @param isReplace             flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated categoryHierarchyLink
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<CategoryHierarchyLink> updateCategoryHierarchyLink(String serverName, String userId, String guid, CategoryHierarchyLink categoryHierarchyLink, Boolean isReplace) {
        String restAPIName = "updateCategoryHierarchyLink";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<CategoryHierarchyLink> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            CategoryHierarchyLink updatedCategoryHierarchyLink;
            if (isReplace) {
                updatedCategoryHierarchyLink = handler.replaceCategoryHierarchyLink(userId, guid, categoryHierarchyLink);
            } else {
                updatedCategoryHierarchyLink = handler.updateCategoryHierarchyLink(userId, guid, categoryHierarchyLink);
            }

            response.addResult(updatedCategoryHierarchyLink);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;    }

    /**
     * Delete a CategoryHierarchyLink Relationship. A relationship between two categories used to create nested categories.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the CategoryHierarchyLink relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<CategoryHierarchyLink> deleteCategoryHierarchyLink(String serverName, String userId, String guid, Boolean isPurge) {
        String restAPIName = "deleteCategoryHierarchyLink";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<CategoryHierarchyLink> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            if (isPurge) {
                handler.purgeCategoryHierarchyLink(userId, guid);
            } else {
                handler.deleteCategoryHierarchyLink(userId, guid);
            }
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore a CategoryHierarchyLink Relationship. A relationship between two categories used to create nested categories.
     * <p>
     * Restore allows the deleted CategoryHierarchyLink Relationship to be made active again. Restore allows deletes to be undone.
     * Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the CategoryHierarchyLink Relationship to delete
     * @return response which when successful contains the restored CategoryHierarchyLink
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<CategoryHierarchyLink> restoreCategoryHierarchyLink(String serverName, String userId, String guid) {
        String restAPIName = "restoreCategoryHierarchyLink";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<CategoryHierarchyLink> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied relationship - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
            CategoryHierarchyLink restoredCategoryHierarchyLink = handler.restoreCategoryHierarchyLink(userId, guid);
            response.addResult(restoredCategoryHierarchyLink);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<SemanticAssignment> getSemanticAssignmentRelationship(String serverName, String userId, String guid)
     {
         String restAPIName = "getSemanticAssignmentRelationship";
         RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
         SubjectAreaOMASAPIResponse<SemanticAssignment> response = new SubjectAreaOMASAPIResponse<>();
         AuditLog auditLog = null;

         // should not be called without a supplied relationship - the calling layer should not allow this.
         try {
             auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
             RelationshipHandler handler = instanceHandler.getRelationshipHandler(serverName, userId, restAPIName);
             SemanticAssignment updatedSemanticAssignment = handler.getSemanticAssignmentRelationship(userId, guid);
             response.addResult(updatedSemanticAssignment);
         }  catch (Exception exception) {
             response = getResponseForException(exception, auditLog, className, restAPIName);
         }
         restCallLogger.logRESTCallReturn(token, response.toString());
         return response;
    }
}