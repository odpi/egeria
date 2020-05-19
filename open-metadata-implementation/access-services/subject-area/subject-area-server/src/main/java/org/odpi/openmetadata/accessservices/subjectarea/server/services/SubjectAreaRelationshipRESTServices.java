/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * TheSubjectAreaTermRESTServices provides the server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS) for Terms.  This interface provides relationship authoring interfaces for subject area experts.
 */

public class SubjectAreaRelationshipRESTServices extends SubjectAreaRESTServicesInstance
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaRelationshipRESTServices.class);
    private static final String className = SubjectAreaRelationshipRESTServices.class.getName();
    static private SubjectAreaInstanceHandler instanceHandler = new SubjectAreaInstanceHandler();


    /**
     * Default constructor
     */
    public SubjectAreaRelationshipRESTServices()
    {
        //SubjectAreaRESTServicesInstance registers this omas.
    }

    public SubjectAreaRelationshipRESTServices(OMRSAPIHelper oMRSAPIHelper)
    {
        this.oMRSAPIHelper = oMRSAPIHelper;
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
        String restAPIName = "createTermHASARelationship";
        return createLine(serverName, restAPIName, userId, Hasa.class.getName(), termHASARelationship);
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
        return getLine(serverName, restAPIName, userId, Hasa.class.getName(), guid);
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
        return updateLine(serverName, restAPIName, userId, Hasa.class.getName(), termHASARelationship, isReplace);
    }

    /**
     * Delete a Term HAS A relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the HAS A relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete, the response contains the deleted relationship
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
        return deleteLine(serverName, restAPIName, userId, Hasa.class.getName(), guid, isPurge);
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
        return restoreLine(serverName, restAPIName, userId, Hasa.class.getName(), guid);
    }

    /**
     * Create a RelatedTerm. A Related Term is a link between two similar Terms.
     *
     * @param serverName                          serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                              userId under which the request is performed
     * @param relatedTermRelationship the RelatedTerm relationship
     * @return response, when successful contains the restored Related Term relationship
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
    public SubjectAreaOMASAPIResponse createRelatedTerm(String serverName, String userId, RelatedTerm relatedTermRelationship)
     {
        String restAPIName = "createRelatedTerm";
        return createLine(serverName,restAPIName, userId,RelatedTerm.class.getName(),relatedTermRelationship);
    }

    /**
     * Get a related Term relationship.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the related term relationship to get
     * @return response which when successful contains the related term relationship with the requested guid
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
        return getLine(serverName,restAPIName, userId,RelatedTerm.class.getName(),guid);
    }

    /**
     * Update a Related Term relationship. A Related Term is a link between two similar Terms.
     * <p>
     *
     * @param serverName              serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                  userId under which the request is performed
     * @param relatedTermRelationship the related term  relationship
     * @param isReplace               flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    public SubjectAreaOMASAPIResponse updateRelatedTerm(String serverName, String userId, RelatedTerm relatedTermRelationship, boolean isReplace)
     {
        String restAPIName = "updateRelatedTerm";
        return updateLine(serverName,restAPIName, userId,RelatedTerm.class.getName(),relatedTermRelationship,isReplace);
    }

    /**
     * Delete a Related Term relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Related term relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
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
        return deleteLine(serverName,restAPIName, userId,RelatedTerm.class.getName(),guid,isPurge);
    }
    /**
     * Restore a related Term relationship.
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
        return restoreLine(serverName, restAPIName, userId, RelatedTerm.class.getName(), guid);
    }

    /**
     * Create a synonym relationship, which is a link between glossary terms that have the same meaning.
     * <p>
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param synonym    the Synonym relationship
     * @return response, when successful contains the created synonym relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function is not supported.
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createSynonym(String serverName, String userId, Synonym synonym)
     {
        String restAPIName = "createSynonym";
       return createLine(serverName,restAPIName, userId,Synonym.class.getName(),synonym);
    }

    /**
     * Get a synonym relationship, which is a link between glossary terms that have the same meaning.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the termCategorization relationship to get
     * @return response which when successful contains the termCategorization relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getSynonymRelationship(String serverName, String userId, String guid)
     {
        String restAPIName = "getSynonymRelationship";
        return getLine(serverName, restAPIName, userId, Synonym.class.getName(), guid);
    }

    /**
     * Update a Synonym relationship which is a link between glossary terms that have the same meaning
     * <p>
     *
     * @param serverName          serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId              userId under which the request is performed
     * @param synonym the Synonym relationship
     * @param isReplace           flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated SynonymRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> FunctionNotSupportedException        Function is not supported.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateSynonymRelationship(String serverName, String userId, Synonym synonym, boolean isReplace)
     {
        String restAPIName = "updateSynonymRelationship";
        return updateLine(serverName,restAPIName, userId,Synonym.class.getName(),synonym,isReplace);
    }

    /**
     * Delete a Synonym relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Synonym relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function is not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse deleteSynonymRelationship(String serverName, String userId, String guid, Boolean isPurge)
     {
        String restAPIName = "deleteSynonymRelationship";
        return deleteLine(serverName, restAPIName, userId, Synonym.class.getName(), guid,isPurge);
    }
    /**
     * Restore a synonym relationship.
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
     * <li> FunctionNotSupportedException        Function is not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreSynonym(String serverName, String userId, String guid)
     {
        String restAPIName = "restoreSynonym";
        return restoreLine(serverName, restAPIName, userId, Synonym.class.getName(), guid);
    }
    /**
     * Create an antonym relationship, which is a link between glossary terms that have the opposite meaning.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param antonym    the Antonym relationship
     * @return response, when successful contains the created antonym relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createAntonym(String serverName, String userId, Antonym antonym)
     {
        String restAPIName = "createAntonym";
        return createLine(serverName,restAPIName, userId,Antonym.class.getName(),antonym);
    }

    /**
     * Get a antonym relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the related term relationship to get
     * @return response which when successful contains the antonym relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getAntonymRelationship(String serverName, String userId, String guid)
     {
        String restAPIName = "getAntonymRelationship";
        return getLine(serverName,restAPIName, userId,Antonym.class.getName(),guid);
    }

    /**
     * Update a Antonym relationship which is a link between glossary terms that have the opposite meaning
     * <p>
     *
     * @param serverName          serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId              userId under which the request is performed
     * @param antonym the Antonym relationship
     * @param isReplace           flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated AntonymRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateAntonymRelationship(String serverName, String userId, Antonym antonym, boolean isReplace)
     {
        String restAPIName = "updateAntonymRelationship";
        return updateLine(serverName,restAPIName, userId,Antonym.class.getName(),antonym,isReplace);
    }

    /**
     * Delete a Antonym relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Antonym relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function is not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse deleteAntonymRelationship(String serverName, String userId, String guid, Boolean isPurge)
     {
        String restAPIName = "deleteAntonymRelationship";
        return deleteLine(serverName,restAPIName, userId,Antonym.class.getName(),guid,isPurge);
    }
    /**
     * Restore an antonym relationship.
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
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> FunctionNotSupportedException        Function is not supported.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreAntonym(String serverName, String userId, String guid)
     {
        String restAPIName = "restoreAntonym";
        return restoreLine(serverName, restAPIName, userId, Antonym.class.getName(), guid);
    }
    /**
     * Create a translationRelationship relationship, which is a link between glossary terms to provide different natural language translationRelationship of the same concept.
     *
     * @param serverName  serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId      userId under which the request is performed
     * @param translationRelationship the Translation relationship
     * @return response, when successful contains the created translationRelationship relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createTranslation(String serverName, String userId, Translation translationRelationship)
     {
        String restAPIName = "createTranslation";
        return createLine(serverName,restAPIName, userId,Translation.class.getName(),translationRelationship);
    }

    /**
     * Get a translation relationshiptranslation relationship, which is a link between glossary terms to provide different natural language translation of the same concept.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the translation relationship to get
     * @return response which when successful contains the translation relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getTranslationRelationship(String serverName, String userId, String guid)
     {
        String restAPIName = "getTranslationRelationship";
        return getLine(serverName,restAPIName, userId,Translation.class.getName(),guid);
    }

    /**
     * Update a Translation relationship translation relationship, which is a link between glossary terms to provide different natural language translation of the same concept.
     * <p>
     *
     * @param serverName              serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                  userId under which the request is performed
     * @param translationRelationship the Translation relationship
     * @param isReplace               flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated TranslationRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateTranslationRelationship(String serverName, String userId, Translation translationRelationship, boolean isReplace)
     {
        String restAPIName = "updateTranslationRelationship";
        return updateLine(serverName,restAPIName, userId,Translation.class.getName(),translationRelationship,isReplace);
    }

    /**
     * Delete a Translation relationship, which is a link between glossary terms to provide different natural language translation of the same concept.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Translation relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function is not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse deleteTranslationRelationship(String serverName, String userId, String guid, Boolean isPurge)
     {
        String restAPIName = "deleteTranslationRelationship";
        return deleteLine(serverName,restAPIName, userId,Translation.class.getName(),guid,isPurge);
    }
    /**
     * Restore a translation relationship.
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
        return restoreLine(serverName, restAPIName, userId,Translation.class.getName(), guid);
    }
    /**
     * Create a usedInContextRelationship relationship, which is a link between glossary terms, where one describes the context where the other one is valid to use.
     * <p>
     *
     * @param serverName    serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId        userId under which the request is performed
     * @param usedInContextRelationship the UsedInContext relationship
     * @return response, when successful contains the created usedInContextRelationship relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createUsedInContext(String serverName, String userId, UsedInContext usedInContextRelationship)
     {
        String restAPIName = "createUsedInContext";
        return createLine(serverName,restAPIName, userId,UsedInContext.class.getName(),usedInContextRelationship);
    }

    /**
     * Get a usedInContext relationship,  which is a link between glossary terms, where one describes the context where the other one is valid to use.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the usedInContext relationship to get
     * @return response which when successful contains the usedInContext relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getUsedInContextRelationship(String serverName, String userId, String guid)
     {
        String restAPIName = "getUsedInContextRelationship";
        return getLine(serverName,restAPIName, userId,UsedInContext.class.getName(),guid);
    }

    /**
     * Update a UsedInContext relationship which is a link between glossary terms, where one describes the context where the other one is valid to use.
     * <p>
     *
     * @param serverName                serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                    userId under which the request is performed
     * @param usedInContextRelationship the UsedInContext relationship
     * @param isReplace                 flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated UsedInContextRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateUsedInContextRelationship(String serverName, String userId, UsedInContext usedInContextRelationship, boolean isReplace)
     {
        String restAPIName = "updateUsedInContextRelationship";
        return updateLine(serverName, restAPIName, userId, UsedInContext.class.getName(), usedInContextRelationship, isReplace);
    }

    /**
     * Delete a UsedInContext relationship which is a link between glossary terms, where one describes the context where the other one is valid to use.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the UsedInContext relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
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

    public SubjectAreaOMASAPIResponse deleteUsedInContextRelationship(String serverName, String userId, String guid, Boolean isPurge)
     {
        String restAPIName = "deleteUsedInContextRelationship";
        return deleteLine(serverName,restAPIName, userId,UsedInContext.class.getName(),guid,isPurge);
    }
    /**
     * Restore a used in context relationship.
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
        return restoreLine(serverName, restAPIName, userId, UsedInContext.class.getName(), guid);
    }

    /**
     * Create a preferredTermRelationship relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     *
     * @param serverName    serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId        userId under which the request is performed
     * @param preferredTermRelationship the preferred Term relationship
     * @return response, when successful contains the created preferredTermRelationship relationship
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
    public SubjectAreaOMASAPIResponse createPreferredTerm(String serverName, String userId, PreferredTerm preferredTermRelationship)
     {
        String restAPIName = "createPreferredTerm";
        return createLine(serverName, restAPIName, userId,PreferredTerm.class.getName(), preferredTermRelationship);
    }

    /**
     * Get a preferredTerm relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the preferredTerm relationship to get
     * @return response which when successful contains the preferredTerm relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getPreferredTermRelationship(String serverName, String userId, String guid)
     {
        String restAPIName = "getPreferredTermRelationship";
        return getLine(serverName, restAPIName, userId,PreferredTerm.class.getName(), guid);
    }

    /**
     * Update a PreferredTerm relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     * <p>
     *
     * @param serverName                serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                    userId under which the request is performed
     * @param preferredTermRelationship the PreferredTerm relationship
     * @param isReplace                 flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated PreferredTermRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> FunctionNotSupportedException        Function not supported
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updatePreferredTermRelationship(String serverName, String userId, PreferredTerm preferredTermRelationship, boolean isReplace)
     {
        String restAPIName = "updatePreferredTermRelationship";
        return updateLine(serverName, restAPIName, userId, PreferredTerm.class.getName(), preferredTermRelationship, isReplace);
    }

    /**
     * Delete a PreferredTerm relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the PreferredTerm relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
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

    public SubjectAreaOMASAPIResponse deletePreferredTermRelationship(String serverName, String userId, String guid, Boolean isPurge)
     {
        String restAPIName = "deletePreferredTermRelationship";
        return deleteLine(serverName, restAPIName, userId,PreferredTerm.class.getName(), guid,isPurge);
    }
    /**
     * Restore a preferred term relationship.
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
        return restoreLine(serverName, restAPIName, userId, PreferredTerm.class.getName(), guid);
    }

    /**
     * Create a validValueRelationship relationship, which is a link between glossary terms that have the same meaning.
     * <p>
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param validValueRelationship the ValidValue relationship
     * @return response, when successful contains the created validValueRelationship relationship
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
    public SubjectAreaOMASAPIResponse createValidValue(String serverName, String userId, ValidValue validValueRelationship)
     {
        String restAPIName = "createValidValue";
        return createLine(serverName, restAPIName, userId,ValidValue.class.getName(), validValueRelationship);
    }

    /**
     * Get a validValue relationship, which is a link between glossary terms that have the same meaning.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the validValue relationship to get
     * @return response which when successful contains the validValue relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getValidValueRelationship(String serverName, String userId, String guid)
     {
        String restAPIName = "getValidValueRelationship";
        return getLine(serverName, restAPIName, userId,ValidValue.class.getName(),guid);
    }

    /**
     * Update a ValidValue relationship which is a link between glossary terms that have the same meaning
     * <p>
     *
     * @param serverName             serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                 userId under which the request is performed
     * @param validValueRelationship the ValidValue relationship
     * @param isReplace              flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated ValidValueRelationship
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
    public SubjectAreaOMASAPIResponse updateValidValueRelationship(String serverName, String userId, ValidValue validValueRelationship, boolean isReplace)
     {
        String restAPIName = "updateValidValueRelationship";
        return updateLine(serverName, restAPIName, userId,ValidValue.class.getName(),validValueRelationship,isReplace);
    }

    /**
     * Delete a ValidValue relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ValidValue relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
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

    public SubjectAreaOMASAPIResponse deleteValidValueRelationship(String serverName, String userId, String guid, Boolean isPurge)
     {
        String restAPIName = "deleteValidValueRelationship";
        return deleteLine(serverName, restAPIName, userId,ValidValue.class.getName(),guid,isPurge);
    }
    /**
     * Restore a valid value relationship.
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
        return restoreLine(serverName, restAPIName, userId, ValidValue.class.getName(), guid);
    }

    /**
     * Create a replacementTermRelationship relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     *
     * @param serverName      serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId          userId under which the request is performed
     * @param replacementTermRelationship the ReplacementTerm relationship
     * @return response, when successful contains the created replacementTermRelationship relationship
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
    public SubjectAreaOMASAPIResponse createReplacementTerm(String serverName, String userId, ReplacementTerm replacementTermRelationship)
     {
        String restAPIName = "createReplacementTerm";
        return createLine(serverName, restAPIName, userId,ReplacementTerm.class.getName(), replacementTermRelationship);
    }

    /**
     * Get a replacementTerm relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the replacementTerm relationship to get
     * @return response which when successful contains the replacementTerm relationship with the requested guid
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
        return getLine(serverName, restAPIName, userId,ReplacementTerm.class.getName(),guid);
    }

    /**
     * Update a ReplacementTerm relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     * @param serverName                  serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                      userId under which the request is performed
     * @param replacementTermRelationship the ReplacementTerm relationship
     * @param isReplace                   flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated ReplacementRelationship
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
    public SubjectAreaOMASAPIResponse updateReplacementTerm(String serverName, String userId, ReplacementTerm replacementTermRelationship, boolean isReplace)
     {
        String restAPIName = "updateReplacementTerm";
        return updateLine(serverName, restAPIName, userId,ReplacementTerm.class.getName(), replacementTermRelationship,isReplace);
    }

    /**
     * Delete a ReplacementTerm relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ReplacementTerm relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
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
        return deleteLine(serverName, restAPIName, userId,ReplacementTerm.class.getName(), guid,isPurge);
    }
    /**
     * Restore a replacement term relationship.
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
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreReplacementTerm(String serverName, String userId, String guid)
     {
        String restAPIName = "restoreReplacementTerm";
        return restoreLine(serverName, restAPIName, userId, ReplacementTerm.class.getName(), guid);
    }
    /**
     * Create a termTYPEDBYRelationship relationship, which is a link between a spine attribute and its type.
     * <p>
     *
     * @param serverName              serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                  userId under which the request is performed
     * @param termTYPEDBYRelationship the TypedBy relationship
     * @return response, when successful contains the created termTYPEDBYRelationship relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createTermTYPEDBYRelationship(String serverName, String userId, TypedBy termTYPEDBYRelationship)
     {
        String restAPIName = "createTermTYPEDBYRelationship";
        return createLine(serverName, restAPIName, userId, TypedBy.class.getName(), termTYPEDBYRelationship);
    }

    /**
     * Get a termTYPEDBYRelationship relationship, which is a link between a spine attribute and its type.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the termTYPEDBYRelationship relationship to get
     * @return response which when successful contains the termTYPEDBYRelationship relationship with the requested guid
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
        return getLine(serverName, restAPIName, userId, TypedBy.class.getName(), guid);
    }

    /**
     * Update a TypedBy relationship, which is a link between a spine attribute and its type.
     * <p>
     *
     * @param serverName                          serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                              userId under which the request is performed
     * @param termTYPEDBYRelationship the TypedBy relationship
     * @param isReplace                           flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated TypedBy
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
        return updateLine(serverName, restAPIName, userId, TypedBy.class.getName(), termTYPEDBYRelationship, isReplace);
    }

    /**
     * Delete a TypedBy relationship, which is a link between a spine attribute and its type.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the TypedBy relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
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
        return deleteLine(serverName, restAPIName, userId, TypedBy.class.getName(), guid, isPurge);
    }
    /**
     * Restore a replacement term relationship.
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
        return restoreLine(serverName, restAPIName, userId, TypedBy.class.getName(), guid);
    }
    /**
     * Create a iSARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param serverName      serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId          userId under which the request is performed
     * @param iSARelationship the Isa relationship
     * @return response, when successful contains the created iSARelationship relationship
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
    public SubjectAreaOMASAPIResponse createISARelationship(String serverName, String userId, Isa iSARelationship)
     {
        String restAPIName = "createISARelationship";
        return createLine(serverName, restAPIName, userId, Isa.class.getName(), iSARelationship);
    }

    /**
     * Get a iSARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the iSARelationship relationship to get
     * @return response which when successful contains the iSARelationship relationship with the requested guid
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
        return getLine(serverName, restAPIName, userId, Isa.class.getName(), guid);
    }

    /**
     * Update a Isa relationship, which is a link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param serverName                  serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                      userId under which the request is performed
     * @param iSARelationship the Isa relationship
     * @param isReplace                   flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    public SubjectAreaOMASAPIResponse updateISARelationship(String serverName, String userId, Isa iSARelationship, boolean isReplace)
     {
        String restAPIName = "updateISARelationship";
        return updateLine(serverName, restAPIName, userId, Isa.class.getName(), iSARelationship, isReplace);
    }

    /**
     * Delete a Isa relationship, which is a link between a more general glossary term and a more specific definition.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Isa relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
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
        return deleteLine(serverName, restAPIName, userId, Isa.class.getName(), guid, isPurge);
    }
    /**
     * Restore a is a relationship.
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
        return restoreLine(serverName, restAPIName, userId, Isa.class.getName(), guid);
    }
    /**
     * Create a termISATypeOFRelationship relationship, which is an inheritance relationship between two spine objects.
     * <p>
     *
     * @param serverName                serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                    userId under which the request is performed
     * @param termISATypeOFRelationship the IsaTypeOf relationship
     * @return response, when successful contains the created termISATypeOFRelationship relationship
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
    public SubjectAreaOMASAPIResponse createTermISATypeOFRelationship(String serverName, String userId, IsaTypeOf termISATypeOFRelationship)
     {
        String restAPIName = "createTermISATypeOFRelationship";
        return createLine(serverName, restAPIName, userId, IsaTypeOf.class.getName(), termISATypeOFRelationship);
    }

    /**
     * Get a termISATypeOFRelationship relationship, which is an inheritance relationship between two spine objects.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the termISATypeOFRelationship relationship to get
     * @return response which when successful contains the termISATypeOFRelationship relationship with the requested guid
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
        return getLine(serverName, restAPIName, userId, IsaTypeOf.class.getName(), guid);
    }

    /**
     * Update a IsaTypeOf relationship, which is an inheritance relationship between two spine objects.
     * <p>
     *
     * @param serverName                            serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                                userId under which the request is performed
     * @param termISATypeOFRelationship the IsaTypeOf relationship
     * @param isReplace                             flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated IsaTypeOf
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
        return updateLine(serverName, restAPIName, userId, IsaTypeOf.class.getName(), termISATypeOFRelationship, isReplace);
    }

    /**
     * Delete a IsaTypeOf relationship, which is an inheritance relationship between two spine objects.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the IsaTypeOf relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse deleteIsATypeOfRelationship(String serverName, String userId, String guid, Boolean isPurge)
     {
        String restAPIName = "deleteIsATypeOfRelationship";
        return deleteLine(serverName, restAPIName, userId, IsaTypeOf.class.getName(), guid, isPurge);
    }
    /**
     * Restore a is IsaTypeOf relationship.
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
        return restoreLine(serverName, restAPIName, userId, IsaTypeOf.class.getName(), guid);
    }
    /**
     *  Create a termCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termCategorizationRelationship the TermCategorizationRelationship relationship
     * @return response, when successful contains the created termCategorizationRelationship relationship
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
    public SubjectAreaOMASAPIResponse createTermCategorizationRelationship(String serverName, String userId, Categorization termCategorizationRelationship)
     {
        String restAPIName = "createTermCategorizationRelationship";
        return createLine(serverName, restAPIName, userId, Categorization.class.getName(), termCategorizationRelationship);
    }

    /**
     * Get a termCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the termCategorizationRelationship relationship to get
     * @return response which when successful contains the termCategorizationRelationship relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
     public SubjectAreaOMASAPIResponse getTermCategorizationRelationship(String serverName,String userId,String guid)   {
        String restAPIName = "getTermCategorizationRelationship";
         return getLine(serverName, restAPIName, userId, Categorization.class.getName(), guid);
     }

    /**
     * Update a termCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termCategorizationRelationship   the termCategorization relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    public SubjectAreaOMASAPIResponse updateTermCategorizationRelationship(String serverName, String userId, Categorization termCategorizationRelationship, Boolean isReplace)  {
        String restAPIName = "updateTermCategorizationRelationship";
        return updateLine(serverName, restAPIName, userId, Categorization.class.getName(), termCategorizationRelationship, isReplace);
    }


    /**
     * Delete a TermCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermCategorizationRelationship relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
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
     public SubjectAreaOMASAPIResponse deleteTermCategorizationRelationship(String serverName,String userId, String guid, Boolean isPurge)  {
        String restAPIName = "deleteTermCategorizationRelationship";
         return deleteLine(serverName, restAPIName, userId, Categorization.class.getName(), guid, isPurge);
     }
    /**
     * Restore a TermCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * Restore allows the deleted TermCategorization Relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the TermCategorization Relationship to delete
     * @return response which when successful contains the restored TermCategorization
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreTermCategorizationRelationship( String serverName,  String userId, String guid)
     {
        String restAPIName = "restoreTermCategorizationRelationship";
        return restoreLine(serverName, restAPIName, userId, Categorization.class.getName(), guid);
    }

    /**
     *  Create a termAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     * Terms created using the Subject Area OMAS cannot be created without a glossary and there can only be one glossary associated with a
     * Term. This method is to allow glossaries to be associated with Terms that have not been created via the Subject Area OMAS or to recreate
     * the TermAnchor relationship if it has been purged.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termAnchorRelationship the TermAnchorRelationship relationship
     * @return response, when successful contains the created termAnchorRelationship relationship
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
    public SubjectAreaOMASAPIResponse createTermAnchorRelationship(String serverName, String userId, TermAnchor termAnchorRelationship)
     {
        String restAPIName = "createTermAnchorRelationship";
        return createLine(serverName, restAPIName, userId, TermAnchor.class.getName(), termAnchorRelationship);
    }

    /**
     * Get a termAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the termAnchor Relationship to get
     * @return response which when successful contains the termAnchorRelationship relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse getTermAnchorRelationship(String serverName,String userId,String guid)   {
        String restAPIName = "getTermAnchorRelationship";
        return getLine(serverName, restAPIName, userId, TermAnchor.class.getName(), guid);
    }

    /**
     * Delete a TermAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermAnchorRelationship relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
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
    public SubjectAreaOMASAPIResponse deleteTermAnchorRelationship(String serverName,String userId, String guid, Boolean isPurge)  {
        String restAPIName = "deleteTermAnchorRelationship";
        return deleteLine(serverName, restAPIName, userId, TermAnchor.class.getName(), guid, isPurge);
    }
    /**
     * Restore a TermAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     *
     * Restore allows the deleted TermAnchor Relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Term Anchor Relationship to delete
     * @return response which when successful contains the restored TermAnchor
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreTermAnchorRelationship( String serverName,  String userId, String guid)
     {
        String restAPIName = "restoreTermAnchorRelationship";
        return restoreLine(serverName, restAPIName, userId, TermAnchor.class.getName(), guid);
    }
    /**
     *  Create a categoryAnchor Relationship. A relationship between a Glossary and a Category. This relationship allows terms to be owned by a Glossary.
     * Categories created using the Subject Area OMAS cannot be created without a glossary and there can only be one glossary associated with a
     * Category. This method is to allow glossaries to be associated with Categories that have not been created via the Subject Area OMAS.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param categoryAnchorRelationship the CategoryAnchorRelationship relationship
     * @return response, when successful contains the created categoryAnchorRelationship relationship
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
    public SubjectAreaOMASAPIResponse createCategoryAnchorRelationship(String serverName, String userId, CategoryAnchor categoryAnchorRelationship)
     {
        String restAPIName = "createCategoryAnchorRelationship";
        return createLine(serverName, restAPIName, userId, CategoryAnchor.class.getName(), categoryAnchorRelationship);
    }

    /**
     * Get a categoryAnchor Relationship. A relationship between a Glossary and a Category. This relationship allows terms to be owned by a Glossary.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the categoryAnchor Relationship to get
     * @return response which when successful contains the categoryAnchorRelationship relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse getCategoryAnchorRelationship(String serverName,String userId,String guid)   {
        String restAPIName = "getCategoryAnchorRelationship";
        return getLine(serverName, restAPIName, userId, CategoryAnchor.class.getName(), guid);
    }

    /**
     * Delete a CategoryAnchor Relationship. A relationship between a Glossary and a Category. This relationship allows terms to be owned by a Glossary.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the CategoryAnchorRelationship relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
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
    public SubjectAreaOMASAPIResponse deleteCategoryAnchorRelationship(String serverName,String userId, String guid, Boolean isPurge)  {
        String restAPIName = "deleteCategoryAnchorRelationship";
        return deleteLine(serverName, restAPIName, userId, CategoryAnchor.class.getName(), guid, isPurge);
    }
    /**
     * Restore a CategoryAnchor Relationship. A relationship between a Glossary and a Category. This relationship allows terms to be owned by a Glossary.
     *
     * Restore allows the deleted CategoryAnchor Relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Category Anchor Relationship to delete
     * @return response which when successful contains the restored CategoryAnchor
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreCategoryAnchorRelationship( String serverName,  String userId, String guid)
     {
        String restAPIName = "restoreCategoryAnchorRelationship";
        return restoreLine(serverName, restAPIName, userId, CategoryAnchor.class.getName(), guid);
    }
    /**
     * Create a projectScope relationship, which is a link between the project content and the project.
     * <p>
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param projectScope    the Synonym relationship
     * @return response, restored projectScope relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function is not supported.
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createProjectScopeRelationship(String serverName, String userId, ProjectScope projectScope)
    {
        String restAPIName = "createProjectScope";
        return createLine(serverName, restAPIName, userId, ProjectScope.class.getName(), projectScope);
    }

    /**
     * Get a projectScope relationship, which is a link between the project content and the project.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the termCategorization relationship to get
     * @return response which when successful contains the termCategorization relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getProjectScopeRelationship(String serverName, String userId, String guid)
    {
        String restAPIName = "getProjectScopeRelationship";
        return getLine(serverName, restAPIName, userId, ProjectScope.class.getName(), guid);
    }

    /**
     * Update a ProjectScope relationship which is a link between the project content and the project.
     * <p>
     *
     * @param serverName          serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId              userId under which the request is performed
     * @param projectScope the ProjectScope relationship
     * @param isReplace           flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated ProjectScopeRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> FunctionNotSupportedException        Function is not supported.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateProjectScopeRelationship(String serverName, String userId, ProjectScope projectScope, boolean isReplace)
    {
        String restAPIName = "updateProjectScopeRelationship";
        return updateLine(serverName, restAPIName, userId, ProjectScope.class.getName(), projectScope, isReplace);
    }

    /**
     * Delete a ProjectScope relationship, which is a link between the project content and the project.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ProjectScope relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function is not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse deleteProjectScopeRelationship(String serverName, String userId, String guid, Boolean isPurge)
    {
        String restAPIName = "deleteProjectScopeRelationship";
        return deleteLine(serverName, restAPIName, userId, ProjectScope.class.getName(), guid, isPurge);
    }
    /**
     * Restore a projectScope relationship, which is a link between the project content and the project.
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
     * <li> FunctionNotSupportedException        Function is not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreProjectScopeRelationship(String serverName, String userId, String guid)
    {
        String restAPIName = "restoreProjectScopeRelationship";
        return restoreLine(serverName, restAPIName, userId, ProjectScope.class.getName(), guid);
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
        return restoreLine(serverName, restAPIName, userId,SemanticAssignment.class.getName(), guid);
    }


}