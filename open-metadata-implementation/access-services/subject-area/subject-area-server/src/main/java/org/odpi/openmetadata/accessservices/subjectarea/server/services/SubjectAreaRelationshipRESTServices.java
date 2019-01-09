/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationshipMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaBeansToAccessOMRS;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Synonym;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.SynonymMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * TheSubjectAreaTermRESTServices provides the server-side implementation of the SubjectAreaDefinition Open Metadata
 * Access Service (OMAS) for Terms.  This interface provides relationship authoring interfaces for subject area experts.
 */

public class SubjectAreaRelationshipRESTServices extends SubjectAreaRESTServicesInstance
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaRelationshipRESTServices.class);
    private static final String className = SubjectAreaRelationshipRESTServices.class.getName();

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
     * Create a TermHASARelationship is the relationship between a spine object and a spine attribute.
     * Note that this method does not error if the relationship ends are not spine objects or spine attributes.
     * This allows the user to create terms then make them spine objects and spine attributes at a later stage.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @return response, when successful contains the created TermHASARelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createTermHASARelationship(String serverName, String userId, TermHASARelationship termHASARelationship)
    {
        final String methodName = "createTermHASARelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }

        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,termHASARelationship.getEffectiveFromTime(),termHASARelationship.getEffectiveToTime(), methodName);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationship gennedRelationship = HasAMapper.mapTermHASARelationshipToOMRSRelationshipBean(termHASARelationship);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationship createdGennedRelationship = subjectAreaOmasREST.createTermHASARelationshipRelationship(userId, gennedRelationship);
            TermHASARelationship createdRelationship = HasAMapper.mapOMRSRelationshipBeanToTermHASARelationship(createdGennedRelationship);
            response = new TermHASARelationshipResponse(createdRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
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
        final String methodName = "getTermHASARelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationship relationshipBean = subjectAreaOmasREST.getTermHASARelationshipRelationshipByGuid(userId, guid);
            TermHASARelationship termHASARelationship = HasAMapper.mapOMRSRelationshipBeanToTermHASARelationship(relationshipBean);
            response = new TermHASARelationshipResponse(termHASARelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Update a TermHASARelationship is the relationship between a spine object and a spine attribute.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created TermHASARelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateTermHASARelationship(String serverName, String userId, TermHASARelationship termHASARelationship, boolean isReplace)
    {
        final String methodName = "updateTermHASARelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        String relationshipGuid = termHASARelationship.getGuid();
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,termHASARelationship.getEffectiveFromTime(),termHASARelationship.getEffectiveToTime(), methodName);

            InputValidator.validateGUIDNotNull(className, methodName, relationshipGuid, "termGuid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationship existingRelationship = subjectAreaOmasREST.getTermHASARelationshipRelationshipByGuid(userId, relationshipGuid);

            if (isReplace)
            {
                // use the relationship as supplied
            } else
            {
                // copy over existing relationships attributes if they were not supplied
                if (termHASARelationship.getDescription() == null)
                {
                    termHASARelationship.setDescription(existingRelationship.getDescription());
                }
                if (termHASARelationship.getSource() == null)
                {
                    termHASARelationship.setSource(existingRelationship.getSource());
                }
                if (termHASARelationship.getStatus() == null)
                {
                    termHASARelationship.setStatus(existingRelationship.getStatus());
                }
                if (termHASARelationship.getSteward() == null)
                {
                    termHASARelationship.setSteward(existingRelationship.getSteward());
                }
            }
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationship gennedRelationship = HasAMapper.mapTermHASARelationshipToOMRSRelationshipBean(termHASARelationship);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationship updatedGennedRelationship = subjectAreaOmasREST.updateTermHASARelationshipRelationship(userId, gennedRelationship);
            TermHASARelationship updatedRelationship = HasAMapper.mapOMRSRelationshipBeanToTermHASARelationship(updatedGennedRelationship);
            response = new TermHASARelationshipResponse(updatedRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
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
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteTermHASARelationship(String serverName, String userId, String guid, Boolean isPurge)
    {
        final String methodName = "deleteTermHASARelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            if (isPurge)
            {
                subjectAreaOmasREST.purgeRelationshipById(userId, guid, "TermHASARelationship");
                response = new VoidResponse();
            } else
            {
                Relationship omrsRelationship = subjectAreaOmasREST.deleteRelationshipById(userId, guid, "TermHASARelationship");
                org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationship deletedRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationshipMapper.mapOmrsRelationshipToTermHASARelationship(omrsRelationship);
                TermHASARelationship deletedRelationship = HasAMapper.mapOMRSRelationshipBeanToTermHASARelationship(deletedRelationshipBean);
                response = new TermHASARelationshipResponse(deletedRelationship);
            }
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (RelationshipNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertRelationshipNotDeletedException(e);
        } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotPurgedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
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
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreTermHASARelationship(String serverName, String userId, String guid)
    {
        final String methodName = "restoreTermHASARelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            Relationship omrsRelationship = this.oMRSAPIHelper.callOMRSRestoreRelationship(userId, guid);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationship restoredRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationshipMapper.mapOmrsRelationshipToTermHASARelationship(omrsRelationship);
            TermHASARelationship restoredRelationship = HasAMapper.mapOMRSRelationshipBeanToTermHASARelationship(restoredRelationshipBean);
            response = new TermHASARelationshipResponse(restoredRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotDeletedException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }


    /**
     * Create a RelatedTerm. A Related Term is a link between two similar Terms.
     *
     * @param serverName                          serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                              userId under which the request is performed
     * @param relatedTermRelationship the RelatedTerm relationship
     * @return response, when successful contains the created Related Term relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createRelatedTerm(String serverName, String userId, RelatedTerm relatedTermRelationship)
    {
        final String methodName = "RelatedTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        RelatedTerm createdRelatedTerm = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,relatedTermRelationship.getEffectiveFromTime(),relatedTermRelationship.getEffectiveToTime(), methodName);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm gennedRelationship = RelatedTermMapper.mapRelatedTermToOMRSRelationshipBean(relatedTermRelationship);

            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm createdGennedRelationship = subjectAreaOmasREST.createRelatedTermRelationship(userId, gennedRelationship);
            RelatedTerm createdRelationship = RelatedTermMapper.mapOMRSRelationshipBeanToRelatedTerm(createdGennedRelationship);

            response = new RelatedTermResponse(createdRelationship);

        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
        final String methodName = "getRelatedTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm relationshipBean = subjectAreaOmasREST.getRelatedTermRelationshipByGuid(userId, guid);
            RelatedTerm relatedterm = RelatedTermMapper.mapOMRSRelationshipBeanToRelatedTerm(relationshipBean);
            response = new RelatedTermResponse(relatedterm);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Update a Related Term relationship. A Related Term is a link between two similar Terms.
     * <p>
     *
     * @param serverName              serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                  userId under which the request is performed
     * @param relatedTermRelationship the related term  relationship
     * @param isReplace               flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created RelatedTerm
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateRelatedTerm(String serverName, String userId, RelatedTerm relatedTermRelationship, boolean isReplace)
    {
        final String methodName = "updateRelatedTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        RelatedTerm createdRelatedTerm = null;
        String relationshipGuid = relatedTermRelationship.getGuid();
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,relatedTermRelationship.getEffectiveFromTime(),relatedTermRelationship.getEffectiveToTime(), methodName);
            InputValidator.validateGUIDNotNull(className, methodName, relationshipGuid, "termGuid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm existingRelationship = subjectAreaOmasREST.getRelatedTermRelationshipByGuid(userId, relationshipGuid);

            if (isReplace)
            {
                // use the relationship as supplied
            } else
            {
                // copy over existing relationships attributes if they were not supplied
                if (relatedTermRelationship.getDescription() == null)
                {
                    relatedTermRelationship.setDescription(existingRelationship.getDescription());
                }
                if (relatedTermRelationship.getSource() == null)
                {
                    relatedTermRelationship.setSource(existingRelationship.getSource());
                }
                if (relatedTermRelationship.getStatus() == null)
                {
                    relatedTermRelationship.setStatus(existingRelationship.getStatus());
                }
                if (relatedTermRelationship.getSteward() == null)
                {
                    relatedTermRelationship.setSteward(existingRelationship.getSteward());
                }
                if (relatedTermRelationship.getExpression() == null)
                {
                    relatedTermRelationship.setExpression(existingRelationship.getExpression());
                }
            }
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm gennedRelationship = RelatedTermMapper.mapRelatedTermToOMRSRelationshipBean(relatedTermRelationship);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm updatedGennedRelationship = subjectAreaOmasREST.updateRelatedTermRelationship(userId, gennedRelationship);
            RelatedTerm updatedRelationship = RelatedTermMapper.mapOMRSRelationshipBeanToRelatedTerm(updatedGennedRelationship);
            response = new RelatedTermResponse(updatedRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Delete a Related Term relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Related term relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the related term relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse deleteRelatedTerm(String serverName, String userId, String guid, Boolean isPurge)
    {
        final String methodName = "deleteRelatedTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            if (isPurge)
            {

                subjectAreaOmasREST.purgeRelationshipById(userId, guid, "RelatedTerm");
                response = new VoidResponse();

            } else
            {
                Relationship omrsRelationship = subjectAreaOmasREST.deleteRelationshipById(userId, guid, "RelatedTerm");
                org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm deletedRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTermMapper.mapOmrsRelationshipToRelatedTerm(omrsRelationship);
                RelatedTerm deletedRelationship = RelatedTermMapper.mapOMRSRelationshipBeanToRelatedTerm(deletedRelationshipBean);
                response = new RelatedTermResponse(deletedRelationship);
            }
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (RelationshipNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertRelationshipNotDeletedException(e);
       } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotPurgedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreRelatedTerm(String serverName, String userId, String guid)
    {
        final String methodName = "restoreRelatedTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            Relationship omrsRelationship = this.oMRSAPIHelper.callOMRSRestoreRelationship(userId, guid);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm restoredRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTermMapper.mapOmrsRelationshipToRelatedTerm(omrsRelationship);
            RelatedTerm restoredRelationship = RelatedTermMapper.mapOMRSRelationshipBeanToRelatedTerm(restoredRelationshipBean);
            response = new RelatedTermResponse(restoredRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotDeletedException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Create a synonymRelationship relationship, which is a link between glossary terms that have the same meaning.
     * <p>
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param synonymRelationship    the Synonym relationship
     * @return response, when successful contains the created synonymRelationship relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createSynonym(String serverName, String userId, Synonym synonymRelationship)
    {
        final String methodName = "createSynonym";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        Synonym createdSynonym = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,synonymRelationship.getEffectiveFromTime(),synonymRelationship.getEffectiveToTime(), methodName);
            // tolerate more than one synonymRelationship between the same terms. They may have different expressions and other attributes.
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym gennedRelationship = SynonymMapper.mapSynonymToOMRSRelationshipBean(synonymRelationship);

            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym createdGennedRelationship = subjectAreaOmasREST.createSynonymRelationship(userId, gennedRelationship);
            Synonym createdRelationship = SynonymMapper.mapOMRSRelationshipBeanToSynonym(createdGennedRelationship);
            response = new SynonymRelationshipResponse(createdRelationship);

        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Get a synonym relationship, which is a link between glossary terms that have the same meaning.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the synonym relationship to get
     * @return response which when successful contains the synonym relationship with the requested guid
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
        final String methodName = "getSynonymRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym relationshipBean = subjectAreaOmasREST.getSynonymRelationshipByGuid(userId, guid);
            Synonym synonym = SynonymMapper.mapOMRSRelationshipBeanToSynonym(relationshipBean);
            response = new SynonymRelationshipResponse(synonym);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Update a Synonym relationship which is a link between glossary terms that have the same meaning
     * <p>
     *
     * @param serverName          serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId              userId under which the request is performed
     * @param synonymRelationship the Synonym relationship
     * @param isReplace           flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created SynonymRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateSynonymRelationship(String serverName, String userId, Synonym synonymRelationship, boolean isReplace)
    {
        final String methodName = "updateSynonymRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        String relationshipGuid = synonymRelationship.getGuid();
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,synonymRelationship.getEffectiveFromTime(),synonymRelationship.getEffectiveToTime(), methodName);
            InputValidator.validateGUIDNotNull(className, methodName, relationshipGuid, "relationshipGuid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym existingRelationship = subjectAreaOmasREST.getSynonymRelationshipByGuid(userId, relationshipGuid);

            if (isReplace)
            {
                // use the relationship as supplied
            } else
            {
                // copy over existing relationships attributes if they were not supplied
                if (synonymRelationship.getDescription() == null)
                {
                    synonymRelationship.setDescription(existingRelationship.getDescription());
                }
                if (synonymRelationship.getSource() == null)
                {
                    synonymRelationship.setSource(existingRelationship.getSource());
                }
                if (synonymRelationship.getStatus() == null)
                {
                    synonymRelationship.setStatus(existingRelationship.getStatus());
                }
                if (synonymRelationship.getSteward() == null)
                {
                    synonymRelationship.setSteward(existingRelationship.getSteward());
                }
                if (synonymRelationship.getExpression() == null)
                {
                    synonymRelationship.setExpression(existingRelationship.getExpression());
                }
            }
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym gennedRelationship = SynonymMapper.mapSynonymToOMRSRelationshipBean(synonymRelationship);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym updatedGennedRelationship = subjectAreaOmasREST.updateSynonymRelationship(userId, gennedRelationship);
            Synonym updatedRelationship = SynonymMapper.mapOMRSRelationshipBeanToSynonym(updatedGennedRelationship);
            response = new SynonymRelationshipResponse(updatedRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Delete a Synonym relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Synonym relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the Synonym relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse deleteSynonymRelationship(String serverName, String userId, String guid, Boolean isPurge)
    {
        final String methodName = "deleteSynonymRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            if (isPurge)
            {

                subjectAreaOmasREST.purgeRelationshipById(userId, guid, "Synonym");
                response = new VoidResponse();

            } else
            {
                Relationship omrsRelationship = subjectAreaOmasREST.deleteRelationshipById(userId, guid, "Synonym");
                org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym deletedRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.SynonymMapper.mapOmrsRelationshipToSynonym(omrsRelationship);
                Synonym deletedRelationship = SynonymMapper.mapOMRSRelationshipBeanToSynonym(deletedRelationshipBean);
                response = new SynonymRelationshipResponse(deletedRelationship);
            }
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (RelationshipNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertRelationshipNotDeletedException(e);
       } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotPurgedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreSynonym(String serverName, String userId, String guid)
    {
        final String methodName = "restoreSynonym";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            Relationship omrsRelationship = this.oMRSAPIHelper.callOMRSRestoreRelationship(userId, guid);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym restoredRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.SynonymMapper.mapOmrsRelationshipToSynonym(omrsRelationship);
            Synonym restoredRelationship = SynonymMapper.mapOMRSRelationshipBeanToSynonym(restoredRelationshipBean);
            response = new SynonymRelationshipResponse(restoredRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotDeletedException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }
    /**
     * Create an antonymRelationship relationship, which is a link between glossary terms that have the opposite meaning.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param antonymRelationship    the Antonym relationship
     * @return response, when successful contains the created antonymRelationship relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createAntonym(String serverName, String userId, Antonym antonymRelationship)
    {
        final String methodName = "createAntonym";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, antonymRelationship.getEffectiveFromTime(),antonymRelationship.getEffectiveToTime(),methodName);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym gennedRelationship = AntonymMapper.mapAntonymToOMRSRelationshipBean(antonymRelationship);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym createdGennedRelationship = subjectAreaOmasREST.createAntonymRelationship(userId, gennedRelationship);
            Antonym createdRelationship = AntonymMapper.mapOMRSRelationshipBeanToAntonym(createdGennedRelationship);
            response = new AntonymRelationshipResponse(createdRelationship);

        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
        final String methodName = "getAntonymRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym relationshipBean = subjectAreaOmasREST.getAntonymRelationshipByGuid(userId, guid);
            Antonym antonym = AntonymMapper.mapOMRSRelationshipBeanToAntonym(relationshipBean);
            response = new AntonymRelationshipResponse(antonym);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Update a Antonym relationship which is a link between glossary terms that have the opposite meaning
     * <p>
     *
     * @param serverName          serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId              userId under which the request is performed
     * @param antonymRelationship the Antonym relationship
     * @param isReplace           flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created AntonymRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateAntonymRelationship(String serverName, String userId, Antonym antonymRelationship, boolean isReplace)
    {
        final String methodName = "updateAntonymRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        String relationshipGuid = antonymRelationship.getGuid();
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,antonymRelationship.getEffectiveFromTime(),antonymRelationship.getEffectiveToTime(), methodName);
            InputValidator.validateGUIDNotNull(className, methodName, relationshipGuid, "guid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym existingRelationship = subjectAreaOmasREST.getAntonymRelationshipByGuid(userId, relationshipGuid);

            if (isReplace)
            {
                // use the relationship as supplied
            } else
            {
                // copy over existing relationships attributes if they were not supplied
                if (antonymRelationship.getDescription() == null)
                {
                    antonymRelationship.setDescription(existingRelationship.getDescription());
                }
                if (antonymRelationship.getSource() == null)
                {
                    antonymRelationship.setSource(existingRelationship.getSource());
                }
                if (antonymRelationship.getStatus() == null)
                {
                    antonymRelationship.setStatus(existingRelationship.getStatus());
                }
                if (antonymRelationship.getSteward() == null)
                {
                    antonymRelationship.setSteward(existingRelationship.getSteward());
                }
                if (antonymRelationship.getExpression() == null)
                {
                    antonymRelationship.setExpression(existingRelationship.getExpression());
                }
            }
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym gennedRelationship = AntonymMapper.mapAntonymToOMRSRelationshipBean(antonymRelationship);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym updatedGennedRelationship = subjectAreaOmasREST.updateAntonymRelationship(userId, gennedRelationship);
            Antonym updatedRelationship = AntonymMapper.mapOMRSRelationshipBeanToAntonym(updatedGennedRelationship);
            response = new AntonymRelationshipResponse(updatedRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Delete a Antonym relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Antonym relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the Antonym relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse deleteAntonymRelationship(String serverName, String userId, String guid, Boolean isPurge)
    {
        final String methodName = "deleteAntonymRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            if (isPurge)
            {
                subjectAreaOmasREST.purgeRelationshipById(userId, guid, "Antonym");
                response = new VoidResponse();

            } else
            {

                Relationship omrsRelationship = subjectAreaOmasREST.deleteRelationshipById(userId, guid, "Antonym");
                org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym deletedRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.AntonymMapper.mapOmrsRelationshipToAntonym(omrsRelationship);
                Antonym deletedRelationship = AntonymMapper.mapOMRSRelationshipBeanToAntonym(deletedRelationshipBean);
                response = new AntonymRelationshipResponse(deletedRelationship);
            }
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (RelationshipNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertRelationshipNotDeletedException(e);
       } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotPurgedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreAntonym(String serverName, String userId, String guid)
    {
        final String methodName = "restoreAntonym";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            Relationship omrsRelationship = this.oMRSAPIHelper.callOMRSRestoreRelationship(userId, guid);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym restoredRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.AntonymMapper.mapOmrsRelationshipToAntonym(omrsRelationship);
            Antonym restoredRelationship = AntonymMapper.mapOMRSRelationshipBeanToAntonym(restoredRelationshipBean);
            response = new AntonymRelationshipResponse(restoredRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotDeletedException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createTranslation(String serverName, String userId, Translation translationRelationship)
    {
        final String methodName = "Translation";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,translationRelationship.getEffectiveFromTime(),translationRelationship.getEffectiveToTime(), methodName);

            // tolerate more than one translationRelationship between the same terms. They may have different expressions and other attributes.

            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Translation.Translation gennedRelationship = TranslationMapper.mapTranslationToOMRSRelationshipBean(translationRelationship);

            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Translation.Translation createdGennedRelationship = subjectAreaOmasREST.createTranslationRelationship(userId, gennedRelationship);
            Translation createdRelationship = TranslationMapper.mapOMRSRelationshipBeanToTranslation(createdGennedRelationship);
            response = new TranslationRelationshipResponse(createdRelationship);

        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
        final String methodName = "getTranslationRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Translation.Translation relationshipBean = subjectAreaOmasREST.getTranslationRelationshipByGuid(userId, guid);
            Translation translation = TranslationMapper.mapOMRSRelationshipBeanToTranslation(relationshipBean);
            response = new TranslationRelationshipResponse(translation);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Update a Translation relationship translation relationship, which is a link between glossary terms to provide different natural language translation of the same concept.
     * <p>
     *
     * @param serverName              serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                  userId under which the request is performed
     * @param translationRelationship the Translation relationship
     * @param isReplace               flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created TranslationRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateTranslationRelationship(String serverName, String userId, Translation translationRelationship, boolean isReplace)
    {
        final String methodName = "updateTranslationRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        String relationshipGuid = translationRelationship.getGuid();
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,translationRelationship.getEffectiveFromTime(),translationRelationship.getEffectiveToTime(), methodName);
            InputValidator.validateGUIDNotNull(className, methodName, relationshipGuid, "relationshipGuid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Translation.Translation existingRelationship = subjectAreaOmasREST.getTranslationRelationshipByGuid(userId, relationshipGuid);

            if (isReplace)
            {
                // use the relationship as supplied
            } else
            {
                // copy over existing relationships attributes if they were not supplied
                if (translationRelationship.getDescription() == null)
                {
                    translationRelationship.setDescription(existingRelationship.getDescription());
                }
                if (translationRelationship.getSource() == null)
                {
                    translationRelationship.setSource(existingRelationship.getSource());
                }
                if (translationRelationship.getStatus() == null)
                {
                    translationRelationship.setStatus(existingRelationship.getStatus());
                }
                if (translationRelationship.getSteward() == null)
                {
                    translationRelationship.setSteward(existingRelationship.getSteward());
                }
                if (translationRelationship.getExpression() == null)
                {
                    translationRelationship.setExpression(existingRelationship.getExpression());
                }
            }
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Translation.Translation gennedRelationship = TranslationMapper.mapTranslationToOMRSRelationshipBean(translationRelationship);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Translation.Translation updatedGennedRelationship = subjectAreaOmasREST.updateTranslationRelationship(userId, gennedRelationship);
            Translation updatedRelationship = TranslationMapper.mapOMRSRelationshipBeanToTranslation(updatedGennedRelationship);
            response = new TranslationRelationshipResponse(updatedRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Delete a Translation relationshiptranslation relationship, which is a link between glossary terms to provide different natural language translation of the same concept.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Translation relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the Translation relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse deleteTranslationRelationship(String serverName, String userId, String guid, Boolean isPurge)
    {
        final String methodName = "deleteTranslationRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            if (isPurge)
            {
                subjectAreaOmasREST.purgeRelationshipById(userId, guid, "Translation");
                response = new VoidResponse();

            } else
            {
                Relationship omrsRelationship = subjectAreaOmasREST.deleteRelationshipById(userId, guid, "Translation");
                org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Translation.Translation deletedRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Translation.TranslationMapper.mapOmrsRelationshipToTranslation(omrsRelationship);
                Translation deletedRelationship = TranslationMapper.mapOMRSRelationshipBeanToTranslation(deletedRelationshipBean);
                response = new TranslationRelationshipResponse(deletedRelationship);
            }
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (RelationshipNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertRelationshipNotDeletedException(e);
       } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotPurgedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreTranslation(String serverName, String userId, String guid)
    {
        final String methodName = "restoreTranslation";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            Relationship omrsRelationship = this.oMRSAPIHelper.callOMRSRestoreRelationship(userId, guid);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Translation.Translation restoredRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Translation.TranslationMapper.mapOmrsRelationshipToTranslation(omrsRelationship);
            Translation restoredRelationship = TranslationMapper.mapOMRSRelationshipBeanToTranslation(restoredRelationshipBean);
            response = new TranslationRelationshipResponse(restoredRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotDeletedException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createUsedInContext(String serverName, String userId, UsedInContext usedInContextRelationship)
    {
        final String methodName = "UsedInContext";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        UsedInContext createdUsedInContext = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,usedInContextRelationship.getEffectiveFromTime(),usedInContextRelationship.getEffectiveToTime(), methodName);
            // tolerate more than one usedInContextRelationship between the same terms. They may have different expressions and other attributes.
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext.UsedInContext gennedRelationship = UsedInContextMapper.mapUsedInContextToOMRSRelationshipBean(usedInContextRelationship);

            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext.UsedInContext createdGennedRelationship = subjectAreaOmasREST.createUsedInContextRelationship(userId, gennedRelationship);
            UsedInContext createdRelationship = UsedInContextMapper.mapOMRSRelationshipBeanToUsedInContext(createdGennedRelationship);
            response = new UsedInContextRelationshipResponse(createdRelationship);

        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
        final String methodName = "getUsedInContextRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext.UsedInContext relationshipBean = subjectAreaOmasREST.getUsedInContextRelationshipByGuid(userId, guid);
            UsedInContext usedInContext = UsedInContextMapper.mapOMRSRelationshipBeanToUsedInContext(relationshipBean);
            response = new UsedInContextRelationshipResponse(usedInContext);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Update a UsedInContext relationship which is a link between glossary terms, where one describes the context where the other one is valid to use.
     * <p>
     *
     * @param serverName                serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                    userId under which the request is performed
     * @param usedInContextRelationship the UsedInContext relationship
     * @param isReplace                 flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created UsedInContextRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateUsedInContextRelationship(String serverName, String userId, UsedInContext usedInContextRelationship, boolean isReplace)
    {
        final String methodName = "updateUsedInContextRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        String relationshipGuid = usedInContextRelationship.getGuid();
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,usedInContextRelationship.getEffectiveFromTime(),usedInContextRelationship.getEffectiveToTime(), methodName);
            InputValidator.validateGUIDNotNull(className, methodName, relationshipGuid, "relationshipGuid");

            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext.UsedInContext existingRelationship = subjectAreaOmasREST.getUsedInContextRelationshipByGuid(userId, relationshipGuid);

            if (isReplace)
            {
                // use the relationship as supplied
            } else
            {
                // copy over existing relationships attributes if they were not supplied
                if (usedInContextRelationship.getDescription() == null)
                {
                    usedInContextRelationship.setDescription(existingRelationship.getDescription());
                }
                if (usedInContextRelationship.getSource() == null)
                {
                    usedInContextRelationship.setSource(existingRelationship.getSource());
                }
                if (usedInContextRelationship.getStatus() == null)
                {
                    usedInContextRelationship.setStatus(existingRelationship.getStatus());
                }
                if (usedInContextRelationship.getSteward() == null)
                {
                    usedInContextRelationship.setSteward(existingRelationship.getSteward());
                }
                if (usedInContextRelationship.getExpression() == null)
                {
                    usedInContextRelationship.setExpression(existingRelationship.getExpression());
                }
            }
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext.UsedInContext gennedRelationship = UsedInContextMapper.mapUsedInContextToOMRSRelationshipBean(usedInContextRelationship);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext.UsedInContext updatedGennedRelationship = subjectAreaOmasREST.updateUsedInContextRelationship(userId, gennedRelationship);
            UsedInContext updatedRelationship = UsedInContextMapper.mapOMRSRelationshipBeanToUsedInContext(updatedGennedRelationship);
            response = new UsedInContextRelationshipResponse(updatedRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Delete a UsedInContext relationship which is a link between glossary terms, where one describes the context where the other one is valid to use.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the UsedInContext relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the UsedInContext relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse deleteUsedInContextRelationship(String serverName, String userId, String guid, Boolean isPurge)
    {
        final String methodName = "deleteUsedInContextRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            if (isPurge)
            {
                subjectAreaOmasREST.purgeRelationshipById(userId, guid, "UsedInContext");
                response = new VoidResponse();

            } else
            {
                Relationship omrsRelationship = subjectAreaOmasREST.deleteRelationshipById(userId, guid, "UsedInContext");
                org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext.UsedInContext deletedRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext.UsedInContextMapper.mapOmrsRelationshipToUsedInContext(omrsRelationship);
                UsedInContext deletedRelationship = UsedInContextMapper.mapOMRSRelationshipBeanToUsedInContext(deletedRelationshipBean);
                response = new UsedInContextRelationshipResponse(deletedRelationship);
            }
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (RelationshipNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertRelationshipNotDeletedException(e);
       } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotPurgedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreUsedInContext(String serverName, String userId, String guid)
    {
        final String methodName = "restoreUsedInContext";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            Relationship omrsRelationship = this.oMRSAPIHelper.callOMRSRestoreRelationship(userId, guid);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext.UsedInContext restoredRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext.UsedInContextMapper.mapOmrsRelationshipToUsedInContext(omrsRelationship);
            UsedInContext restoredRelationship = UsedInContextMapper.mapOMRSRelationshipBeanToUsedInContext(restoredRelationshipBean);
            response = new UsedInContextRelationshipResponse(restoredRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotDeletedException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createPreferredTerm(String serverName, String userId, PreferredTerm preferredTermRelationship)
    {
        final String methodName = "PreferredTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        PreferredTerm createdPreferredTerm = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, preferredTermRelationship.getEffectiveFromTime(),preferredTermRelationship.getEffectiveToTime(),methodName);
            // tolerate more than one preferredTermRelationship between the same terms. They may have different expressions and other attributes.

            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.PreferredTerm.PreferredTerm gennedRelationship = PreferredTermMapper.mapPreferredTermToOMRSRelationshipBean(preferredTermRelationship);

            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.PreferredTerm.PreferredTerm createdGennedRelationship = subjectAreaOmasREST.createPreferredTermRelationship(userId, gennedRelationship);
            PreferredTerm createdRelationship = PreferredTermMapper.mapOMRSRelationshipBeanToPreferredTerm(createdGennedRelationship);
            response = new PreferredTermRelationshipResponse(createdRelationship);

        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
        final String methodName = "getPreferredTermRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.PreferredTerm.PreferredTerm relationshipBean = subjectAreaOmasREST.getPreferredTermRelationshipByGuid(userId, guid);
            PreferredTerm preferredTerm = PreferredTermMapper.mapOMRSRelationshipBeanToPreferredTerm(relationshipBean);
            response = new PreferredTermRelationshipResponse(preferredTerm);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Update a PreferredTerm relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     * <p>
     *
     * @param serverName                serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                    userId under which the request is performed
     * @param preferredTermRelationship the PreferredTerm relationship
     * @param isReplace                 flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created PreferredTermRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> FunctionNotSupportedException        Function not supported
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updatePreferredTermRelationship(String serverName, String userId, PreferredTerm preferredTermRelationship, boolean isReplace)
    {
        final String methodName = "updatePreferredTermRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        String relationshipGuid = preferredTermRelationship.getGuid();
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,preferredTermRelationship.getEffectiveFromTime(),preferredTermRelationship.getEffectiveToTime(), methodName);
            InputValidator.validateGUIDNotNull(className, methodName, relationshipGuid, "relationshipGuid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.PreferredTerm.PreferredTerm existingRelationship = subjectAreaOmasREST.getPreferredTermRelationshipByGuid(userId, relationshipGuid);

            if (isReplace)
            {
                // use the relationship as supplied
            } else
            {
                // copy over existing relationships attributes if they were not supplied
                if (preferredTermRelationship.getDescription() == null)
                {
                    preferredTermRelationship.setDescription(existingRelationship.getDescription());
                }
                if (preferredTermRelationship.getSource() == null)
                {
                    preferredTermRelationship.setSource(existingRelationship.getSource());
                }
                if (preferredTermRelationship.getStatus() == null)
                {
                    preferredTermRelationship.setStatus(existingRelationship.getStatus());
                }
                if (preferredTermRelationship.getSteward() == null)
                {
                    preferredTermRelationship.setSteward(existingRelationship.getSteward());
                }
                if (preferredTermRelationship.getExpression() == null)
                {
                    preferredTermRelationship.setExpression(existingRelationship.getExpression());
                }
            }
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.PreferredTerm.PreferredTerm gennedRelationship = PreferredTermMapper.mapPreferredTermToOMRSRelationshipBean(preferredTermRelationship);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.PreferredTerm.PreferredTerm updatedGennedRelationship = subjectAreaOmasREST.updatePreferredTermRelationship(userId, gennedRelationship);
            PreferredTerm updatedRelationship = PreferredTermMapper.mapOMRSRelationshipBeanToPreferredTerm(updatedGennedRelationship);
            response = new PreferredTermRelationshipResponse(updatedRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Delete a PreferredTerm relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the PreferredTerm relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the PreferredTerm relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse deletePreferredTermRelationship(String serverName, String userId, String guid, Boolean isPurge)
    {
        final String methodName = "deletePreferredTermRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            if (isPurge)
            {
                subjectAreaOmasREST.purgeRelationshipById(userId, guid, "PreferredTerm");
                response = new VoidResponse();

            } else
            {
                Relationship omrsRelationship = subjectAreaOmasREST.deleteRelationshipById(userId, guid, "PreferredTerm");
                org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.PreferredTerm.PreferredTerm deletedRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.PreferredTerm.PreferredTermMapper.mapOmrsRelationshipToPreferredTerm(omrsRelationship);
                PreferredTerm deletedRelationship = PreferredTermMapper.mapOMRSRelationshipBeanToPreferredTerm(deletedRelationshipBean);
                response = new PreferredTermRelationshipResponse(deletedRelationship);
            }
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (RelationshipNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertRelationshipNotDeletedException(e);
       } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotPurgedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restorePreferredTerm(String serverName, String userId, String guid)
    {
        final String methodName = "restorePreferredTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            Relationship omrsRelationship = this.oMRSAPIHelper.callOMRSRestoreRelationship(userId, guid);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.PreferredTerm.PreferredTerm restoredRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.PreferredTerm.PreferredTermMapper.mapOmrsRelationshipToPreferredTerm(omrsRelationship);
            PreferredTerm restoredRelationship = PreferredTermMapper.mapOMRSRelationshipBeanToPreferredTerm(restoredRelationshipBean);
            response = new PreferredTermRelationshipResponse(restoredRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotDeletedException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createValidValue(String serverName, String userId, ValidValue validValueRelationship)
    {
        final String methodName = "ValidValue";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        ValidValue createdValidValue = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,validValueRelationship.getEffectiveFromTime(),validValueRelationship.getEffectiveToTime(), methodName);
            // tolerate more than one validValueRelationship between the same terms. They may have different expressions and other attributes.
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ValidValue.ValidValue gennedRelationship = ValidValueMapper.mapValidValueToOMRSRelationshipBean(validValueRelationship);

            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ValidValue.ValidValue createdGennedRelationship = subjectAreaOmasREST.createValidValueRelationship(userId, gennedRelationship);
            ValidValue createdRelationship = ValidValueMapper.mapOMRSRelationshipBeanToValidValue(createdGennedRelationship);
            response = new ValidValueRelationshipResponse(createdRelationship);

        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
        final String methodName = "getValidValueRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ValidValue.ValidValue relationshipBean = subjectAreaOmasREST.getValidValueRelationshipByGuid(userId, guid);
            ValidValue validValue = ValidValueMapper.mapOMRSRelationshipBeanToValidValue(relationshipBean);
            response = new ValidValueRelationshipResponse(validValue);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Update a ValidValue relationship which is a link between glossary terms that have the same meaning
     * <p>
     *
     * @param serverName             serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                 userId under which the request is performed
     * @param validValueRelationship the ValidValue relationship
     * @param isReplace              flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created ValidValueRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateValidValueRelationship(String serverName, String userId, ValidValue validValueRelationship, boolean isReplace)
    {
        final String methodName = "updateValidValueRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        String relationshipGuid = validValueRelationship.getGuid();
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, relationshipGuid, "relationshipGuid");

            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ValidValue.ValidValue existingRelationship = subjectAreaOmasREST.getValidValueRelationshipByGuid(userId, relationshipGuid);

            if (isReplace)
            {
                // use the relationship as supplied
            } else
            {
                // copy over existing relationships attributes if they were not supplied
                if (validValueRelationship.getDescription() == null)
                {
                    validValueRelationship.setDescription(existingRelationship.getDescription());
                }
                if (validValueRelationship.getSource() == null)
                {
                    validValueRelationship.setSource(existingRelationship.getSource());
                }
                if (validValueRelationship.getStatus() == null)
                {
                    validValueRelationship.setStatus(existingRelationship.getStatus());
                }
                if (validValueRelationship.getSteward() == null)
                {
                    validValueRelationship.setSteward(existingRelationship.getSteward());
                }
                if (validValueRelationship.getExpression() == null)
                {
                    validValueRelationship.setExpression(existingRelationship.getExpression());
                }
            }
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ValidValue.ValidValue gennedRelationship = ValidValueMapper.mapValidValueToOMRSRelationshipBean(validValueRelationship);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ValidValue.ValidValue updatedGennedRelationship = subjectAreaOmasREST.updateValidValueRelationship(userId, gennedRelationship);
            ValidValue updatedRelationship = ValidValueMapper.mapOMRSRelationshipBeanToValidValue(updatedGennedRelationship);
            response = new ValidValueRelationshipResponse(updatedRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Delete a ValidValue relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ValidValue relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the ValidValue relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse deleteValidValueRelationship(String serverName, String userId, String guid, Boolean isPurge)
    {
        final String methodName = "deleteValidValueRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            if (isPurge)
            {
                subjectAreaOmasREST.purgeRelationshipById(userId, guid, "ValidValue");
                response = new VoidResponse();

            } else
            {
                Relationship omrsRelationship = subjectAreaOmasREST.deleteRelationshipById(userId, guid, "ValidValue");
                org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ValidValue.ValidValue deletedRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ValidValue.ValidValueMapper.mapOmrsRelationshipToValidValue(omrsRelationship);
                ValidValue deletedRelationship = ValidValueMapper.mapOMRSRelationshipBeanToValidValue(deletedRelationshipBean);
                response = new ValidValueRelationshipResponse(deletedRelationship);
            }
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (RelationshipNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertRelationshipNotDeletedException(e);
       } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotPurgedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreValidValue(String serverName, String userId, String guid)
    {
        final String methodName = "restoreValidValue";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            Relationship omrsRelationship = this.oMRSAPIHelper.callOMRSRestoreRelationship(userId, guid);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ValidValue.ValidValue restoredRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ValidValue.ValidValueMapper.mapOmrsRelationshipToValidValue(omrsRelationship);
            ValidValue restoredRelationship = ValidValueMapper.mapOMRSRelationshipBeanToValidValue(restoredRelationshipBean);
            response = new ValidValueRelationshipResponse(restoredRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotDeletedException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createReplacementTerm(String serverName, String userId, ReplacementTerm replacementTermRelationship)
    {
        final String methodName = "ReplacementTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        ReplacementTerm createdReplacementTerm = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,replacementTermRelationship.getEffectiveFromTime(),replacementTermRelationship.getEffectiveToTime(), methodName);
            // tolerate more than one replacementTermRelationship between the same terms. They may have different expressions and other attributes.
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReplacementTerm.ReplacementTerm gennedRelationship = TermReplacementMapper.mapReplacementTermToOMRSRelationshipBean(replacementTermRelationship);

            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReplacementTerm.ReplacementTerm createdGennedRelationship = subjectAreaOmasREST.createReplacementTermRelationship(userId, gennedRelationship);
            ReplacementTerm createdRelationship = TermReplacementMapper.mapOMRSRelationshipBeanToReplacementTerm(createdGennedRelationship);
            response = new ReplacementRelationshipResponse(createdRelationship);

        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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

    public SubjectAreaOMASAPIResponse getReplacementTermRelationship(String serverName, String userId, String guid)
    {
        final String methodName = "getReplacementTermRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReplacementTerm.ReplacementTerm relationshipBean = subjectAreaOmasREST.getReplacementTermRelationshipByGuid(userId, guid);
            ReplacementTerm replacementTerm = TermReplacementMapper.mapOMRSRelationshipBeanToReplacementTerm(relationshipBean);
            response = new ReplacementRelationshipResponse(replacementTerm);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Update a ReplacementTerm relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     * @param serverName                  serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                      userId under which the request is performed
     * @param replacementTermRelationship the ReplacementTerm relationship
     * @param isReplace                   flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created ReplacementTermRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateReplacementTermRelationship(String serverName, String userId, ReplacementTerm replacementTermRelationship, boolean isReplace)
    {
        final String methodName = "updateReplacementTermRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        String relationshipGuid = replacementTermRelationship.getGuid();
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,replacementTermRelationship.getEffectiveFromTime(),replacementTermRelationship.getEffectiveToTime(), methodName);
            InputValidator.validateGUIDNotNull(className, methodName, relationshipGuid, "relationshipGuid");

            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReplacementTerm.ReplacementTerm existingRelationship = subjectAreaOmasREST.getReplacementTermRelationshipByGuid(userId, relationshipGuid);

            if (isReplace)
            {
                // use the relationship as supplied
            } else
            {
                // copy over existing relationships attributes if they were not supplied
                if (replacementTermRelationship.getDescription() == null)
                {
                    replacementTermRelationship.setDescription(existingRelationship.getDescription());
                }
                if (replacementTermRelationship.getSource() == null)
                {
                    replacementTermRelationship.setSource(existingRelationship.getSource());
                }
                if (replacementTermRelationship.getStatus() == null)
                {
                    replacementTermRelationship.setStatus(existingRelationship.getStatus());
                }
                if (replacementTermRelationship.getSteward() == null)
                {
                    replacementTermRelationship.setSteward(existingRelationship.getSteward());
                }
                if (replacementTermRelationship.getExpression() == null)
                {
                    replacementTermRelationship.setExpression(existingRelationship.getExpression());
                }
            }
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReplacementTerm.ReplacementTerm gennedRelationship = TermReplacementMapper.mapReplacementTermToOMRSRelationshipBean(replacementTermRelationship);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReplacementTerm.ReplacementTerm updatedGennedRelationship = subjectAreaOmasREST.updateReplacementTermRelationship(userId, gennedRelationship);
            ReplacementTerm updatedRelationship = TermReplacementMapper.mapOMRSRelationshipBeanToReplacementTerm(updatedGennedRelationship);
            response = new ReplacementRelationshipResponse(updatedRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Delete a ReplacementTerm relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ReplacementTerm relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the ReplacementTerm relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse deleteReplacementTermRelationship(String serverName, String userId, String guid, Boolean isPurge)
    {
        final String methodName = "deleteReplacementTermRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            if (isPurge)
            {
                subjectAreaOmasREST.purgeRelationshipById(userId, guid, "ReplacementTerm");
                response = new VoidResponse();

            } else
            {
                Relationship omrsRelationship = subjectAreaOmasREST.deleteRelationshipById(userId, guid, "ReplacementTerm");
                org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReplacementTerm.ReplacementTerm deletedRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReplacementTerm.ReplacementTermMapper.mapOmrsRelationshipToReplacementTerm(omrsRelationship);
                ReplacementTerm deletedRelationship = TermReplacementMapper.mapOMRSRelationshipBeanToReplacementTerm(deletedRelationshipBean);
                response = new ReplacementRelationshipResponse(deletedRelationship);
            }
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (RelationshipNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertRelationshipNotDeletedException(e);
       } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotPurgedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreReplacementTerm(String serverName, String userId, String guid)
    {
        final String methodName = "restoreReplacementTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            Relationship omrsRelationship = this.oMRSAPIHelper.callOMRSRestoreRelationship(userId, guid);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReplacementTerm.ReplacementTerm restoredRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReplacementTerm.ReplacementTermMapper.mapOmrsRelationshipToReplacementTerm(omrsRelationship);
            ReplacementTerm restoredRelationship = TermReplacementMapper.mapOMRSRelationshipBeanToReplacementTerm(restoredRelationshipBean);
            response = new ReplacementRelationshipResponse(restoredRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotDeletedException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }
    /**
     * Create a termTYPEDBYRelationship relationship, which is a link between a spine attribute and its type.
     * <p>
     *
     * @param serverName              serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                  userId under which the request is performed
     * @param termTYPEDBYRelationship the TermTYPEDBYRelationship relationship
     * @return response, when successful contains the created termTYPEDBYRelationship relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createTermTYPEDBYRelationship(String serverName, String userId, TermTYPEDBYRelationship termTYPEDBYRelationship)
    {
        final String methodName = "TermTYPEDBYRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, termTYPEDBYRelationship.getEffectiveFromTime(),termTYPEDBYRelationship.getEffectiveToTime(),methodName);
            // tolerate more than one termTYPEDBYRelationship between the same terms. They may have different expressions and other attributes.
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationship gennedRelationship = TypedByMapper.mapTermTYPEDBYRelationshipToOMRSRelationshipBean(termTYPEDBYRelationship);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationship createdGennedRelationship = subjectAreaOmasREST.createTermTYPEDBYRelationshipRelationship(userId, gennedRelationship);
            TermTYPEDBYRelationship createdRelationship = TypedByMapper.mapOMRSRelationshipBeanToTermTYPEDBYRelationship(createdGennedRelationship);
            response = new TermTYPEDBYRelationshipResponse(createdRelationship);

        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
        final String methodName = "getTermTYPEDBYRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationship relationshipBean = subjectAreaOmasREST.getTermTYPEDBYRelationshipRelationshipByGuid(userId, guid);
            TermTYPEDBYRelationship termTYPEDBYRelationship = TypedByMapper.mapOMRSRelationshipBeanToTermTYPEDBYRelationship(relationshipBean);
            response = new TermTYPEDBYRelationshipResponse(termTYPEDBYRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Update a TermTYPEDBYRelationship relationship, which is a link between a spine attribute and its type.
     * <p>
     *
     * @param serverName                          serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                              userId under which the request is performed
     * @param termTYPEDBYRelationshipRelationship the TermTYPEDBYRelationship relationship
     * @param isReplace                           flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created TermTYPEDBYRelationshipRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification</li>
     * <li> StatusNotSupportedException          A status value is not supported</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateTermTYPEDBYRelationship(String serverName, String userId, TermTYPEDBYRelationship termTYPEDBYRelationshipRelationship, boolean isReplace)
    {
        final String methodName = "updateTermTYPEDBYRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        String relationshipGuid = termTYPEDBYRelationshipRelationship.getGuid();
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,termTYPEDBYRelationshipRelationship.getEffectiveFromTime(),termTYPEDBYRelationshipRelationship.getEffectiveToTime(), methodName);
            InputValidator.validateGUIDNotNull(className, methodName, relationshipGuid, "relationshipGuid");

            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationship existingRelationship = subjectAreaOmasREST.getTermTYPEDBYRelationshipRelationshipByGuid(userId, relationshipGuid);

            if (isReplace)
            {
                // use the relationship as supplied
            } else
            {
                // copy over existing relationships attributes if they were not supplied
                if (termTYPEDBYRelationshipRelationship.getDescription() == null)
                {
                    termTYPEDBYRelationshipRelationship.setDescription(existingRelationship.getDescription());
                }
                if (termTYPEDBYRelationshipRelationship.getSource() == null)
                {
                    termTYPEDBYRelationshipRelationship.setSource(existingRelationship.getSource());
                }
                if (termTYPEDBYRelationshipRelationship.getStatus() == null)
                {
                    termTYPEDBYRelationshipRelationship.setStatus(existingRelationship.getStatus());
                }
                if (termTYPEDBYRelationshipRelationship.getSteward() == null)
                {
                    termTYPEDBYRelationshipRelationship.setSteward(existingRelationship.getSteward());
                }

            }
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationship gennedRelationship = TypedByMapper.mapTermTYPEDBYRelationshipToOMRSRelationshipBean(termTYPEDBYRelationshipRelationship);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationship updatedGennedRelationship = subjectAreaOmasREST.updateTermTYPEDBYRelationshipRelationship(userId, gennedRelationship);
            TermTYPEDBYRelationship updatedRelationship = TypedByMapper.mapOMRSRelationshipBeanToTermTYPEDBYRelationship(updatedGennedRelationship);
            response = new TermTYPEDBYRelationshipResponse(updatedRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Delete a TermTYPEDBYRelationship relationship, which is a link between a spine attribute and its type.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the TermTYPEDBYRelationship relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the TermTYPEDBYRelationship relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse deleteTermTYPEDBYRelationship(String serverName, String userId, String guid, Boolean isPurge)
    {
        final String methodName = "deleteTermTYPEDBYRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            if (isPurge)
            {
                subjectAreaOmasREST.purgeRelationshipById(userId, guid, "TermTYPEDBYRelationship");
                response = new VoidResponse();

            } else
            {
                Relationship omrsRelationship = subjectAreaOmasREST.deleteRelationshipById(userId, guid, "TermTYPEDBYRelationship");
                org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationship deletedRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationshipMapper.mapOmrsRelationshipToTermTYPEDBYRelationship(omrsRelationship);
                TermTYPEDBYRelationship deletedRelationship = TypedByMapper.mapOMRSRelationshipBeanToTermTYPEDBYRelationship(deletedRelationshipBean);
                response = new TermTYPEDBYRelationshipResponse(deletedRelationship);
            }
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (RelationshipNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertRelationshipNotDeletedException(e);
        } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotPurgedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreTermTYPEDBYRelationship(String serverName, String userId, String guid)
    {
        final String methodName = "restoreTermTYPEDBYRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            Relationship omrsRelationship = this.oMRSAPIHelper.callOMRSRestoreRelationship(userId, guid);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationship restoredRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationshipMapper.mapOmrsRelationshipToTermTYPEDBYRelationship(omrsRelationship);
            TermTYPEDBYRelationship restoredRelationship = TypedByMapper.mapOMRSRelationshipBeanToTermTYPEDBYRelationship(restoredRelationshipBean);
            response = new TermTYPEDBYRelationshipResponse(restoredRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotDeletedException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }
    /**
     * Create a iSARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param serverName      serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId          userId under which the request is performed
     * @param iSARelationship the ISARelationship relationship
     * @return response, when successful contains the created iSARelationship relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createISARelationship(String serverName, String userId, ISARelationship iSARelationship)
    {
        final String methodName = "ISARelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        ISARelationship createdISARelationship = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,iSARelationship.getEffectiveFromTime(),iSARelationship.getEffectiveFromTime(), methodName);
            // tolerate more than one iSARelationship between the same terms. They may have different expressions and other attributes.

            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ISARelationship.ISARelationship gennedRelationship = IsaMapper.mapISARelationshipToOMRSRelationshipBean(iSARelationship);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ISARelationship.ISARelationship createdGennedRelationship = subjectAreaOmasREST.createISARelationshipRelationship(userId, gennedRelationship);
            ISARelationship createdRelationship = IsaMapper.mapOMRSRelationshipBeanToISARelationship(createdGennedRelationship);
            response = new TermISARelationshipResponse(createdRelationship);

        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
        final String methodName = "getISARelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ISARelationship.ISARelationship relationshipBean = subjectAreaOmasREST.getISARelationshipRelationshipByGuid(userId, guid);
            ISARelationship iSARelationship = IsaMapper.mapOMRSRelationshipBeanToISARelationship(relationshipBean);
            response = new TermISARelationshipResponse(iSARelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Update a ISARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param serverName                  serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                      userId under which the request is performed
     * @param iSARelationshipRelationship the ISARelationship relationship
     * @param isReplace                   flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created ISARelationshipRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateISARelationship(String serverName, String userId, ISARelationship iSARelationshipRelationship, boolean isReplace)
    {
        final String methodName = "updateISARelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        String relationshipGuid = iSARelationshipRelationship.getGuid();
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,iSARelationshipRelationship.getEffectiveFromTime(),iSARelationshipRelationship.getEffectiveToTime(), methodName);
            InputValidator.validateGUIDNotNull(className, methodName, relationshipGuid, "relationshipGuid");

            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ISARelationship.ISARelationship existingRelationship = subjectAreaOmasREST.getISARelationshipRelationshipByGuid(userId, relationshipGuid);

            if (isReplace)
            {
                // use the relationship as supplied
            } else
            {
                // copy over existing relationships attributes if they were not supplied
                if (iSARelationshipRelationship.getDescription() == null)
                {
                    iSARelationshipRelationship.setDescription(existingRelationship.getDescription());
                }
                if (iSARelationshipRelationship.getSource() == null)
                {
                    iSARelationshipRelationship.setSource(existingRelationship.getSource());
                }
                if (iSARelationshipRelationship.getStatus() == null)
                {
                    iSARelationshipRelationship.setStatus(existingRelationship.getStatus());
                }
                if (iSARelationshipRelationship.getSteward() == null)
                {
                    iSARelationshipRelationship.setSteward(existingRelationship.getSteward());
                }
                if (iSARelationshipRelationship.getExpression() == null)
                {
                    iSARelationshipRelationship.setExpression(existingRelationship.getExpression());
                }
            }
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ISARelationship.ISARelationship gennedRelationship = IsaMapper.mapISARelationshipToOMRSRelationshipBean(iSARelationshipRelationship);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ISARelationship.ISARelationship updatedGennedRelationship = subjectAreaOmasREST.updateISARelationshipRelationship(userId, gennedRelationship);
            ISARelationship updatedRelationship = IsaMapper.mapOMRSRelationshipBeanToISARelationship(updatedGennedRelationship);
            response = new TermISARelationshipResponse(updatedRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Delete a ISARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ISARelationship relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the ISARelationship relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse deleteISARelationship(String serverName, String userId, String guid, Boolean isPurge)
    {
        final String methodName = "deleteISARelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            if (isPurge)
            {
                subjectAreaOmasREST.purgeRelationshipById(userId, guid, "ISARelationship");
                response = new VoidResponse();

            } else
            {
                Relationship omrsRelationship = subjectAreaOmasREST.deleteRelationshipById(userId, guid, "ISARelationship");
                org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ISARelationship.ISARelationship deletedRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ISARelationship.ISARelationshipMapper.mapOmrsRelationshipToISARelationship(omrsRelationship);
                ISARelationship deletedRelationship = IsaMapper.mapOMRSRelationshipBeanToISARelationship(deletedRelationshipBean);
                response = new TermISARelationshipResponse(deletedRelationship);
            }
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (RelationshipNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertRelationshipNotDeletedException(e);
       } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotPurgedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreISARelationship(String serverName, String userId, String guid)
    {
        final String methodName = "restoreISARelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            Relationship omrsRelationship = this.oMRSAPIHelper.callOMRSRestoreRelationship(userId, guid);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ISARelationship.ISARelationship deletedRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ISARelationship.ISARelationshipMapper.mapOmrsRelationshipToISARelationship(omrsRelationship);
            ISARelationship deletedRelationship = IsaMapper.mapOMRSRelationshipBeanToISARelationship(deletedRelationshipBean);
            response = new TermISARelationshipResponse(deletedRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotDeletedException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }
    /**
     * Create a termISATypeOFRelationship relationship, which is an inheritance relationship between two spine objects.
     * <p>
     *
     * @param serverName                serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                    userId under which the request is performed
     * @param termISATypeOFRelationship the TermISATypeOFRelationship relationship
     * @return response, when successful contains the created termISATypeOFRelationship relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createTermISATypeOFRelationship(String serverName, String userId, TermISATypeOFRelationship termISATypeOFRelationship)
    {
        final String methodName = "TermISATypeOFRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        TermISATypeOFRelationship createdTermISATypeOFRelationship = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,termISATypeOFRelationship.getEffectiveFromTime(),termISATypeOFRelationship.getEffectiveToTime(), methodName);
            // tolerate more than one termISATypeOFRelationship between the same terms. They may have different expressions and other attributes.
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship.TermISATypeOFRelationship gennedRelationship = IsaTypeOfMapper.mapTermISATypeOFRelationshipToOMRSRelationshipBean(termISATypeOFRelationship);

            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship.TermISATypeOFRelationship createdGennedRelationship = subjectAreaOmasREST.createTermISATypeOFRelationshipRelationship(userId, gennedRelationship);
            TermISATypeOFRelationship createdRelationship = IsaTypeOfMapper.mapOMRSRelationshipBeanToTermISATypeOFRelationship(createdGennedRelationship);
            response = new TermISATYPEOFRelationshipResponse(createdRelationship);

        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
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
        final String methodName = "getTermISATypeOFRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship.TermISATypeOFRelationship relationshipBean = subjectAreaOmasREST.getTermISATypeOFRelationshipRelationshipByGuid(userId, guid);
            TermISATypeOFRelationship termISATypeOFRelationship = IsaTypeOfMapper.mapOMRSRelationshipBeanToTermISATypeOFRelationship(relationshipBean);
            response = new TermISATYPEOFRelationshipResponse(termISATypeOFRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Update a TermISATypeOFRelationship relationship, which is an inheritance relationship between two spine objects.
     * <p>
     *
     * @param serverName                            serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                                userId under which the request is performed
     * @param termISATypeOFRelationshipRelationship the TermISATypeOFRelationship relationship
     * @param isReplace                             flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created TermISATypeOFRelationshipRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateTermISATypeOFRelationship(String serverName, String userId, TermISATypeOFRelationship termISATypeOFRelationshipRelationship, boolean isReplace)
    {
        final String methodName = "updateTermISATypeOFRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        String relationshipGuid = termISATypeOFRelationshipRelationship.getGuid();
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,termISATypeOFRelationshipRelationship.getEffectiveFromTime(),termISATypeOFRelationshipRelationship.getEffectiveToTime(), methodName);
            InputValidator.validateGUIDNotNull(className, methodName, relationshipGuid, "relationshipGuid");

            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship.TermISATypeOFRelationship existingRelationship = subjectAreaOmasREST.getTermISATypeOFRelationshipRelationshipByGuid(userId, relationshipGuid);

            if (isReplace)
            {
                // use the relationship as supplied
            } else
            {
                // copy over existing relationships attributes if they were not supplied
                if (termISATypeOFRelationshipRelationship.getDescription() == null)
                {
                    termISATypeOFRelationshipRelationship.setDescription(existingRelationship.getDescription());
                }
                if (termISATypeOFRelationshipRelationship.getSource() == null)
                {
                    termISATypeOFRelationshipRelationship.setSource(existingRelationship.getSource());
                }
                if (termISATypeOFRelationshipRelationship.getStatus() == null)
                {
                    termISATypeOFRelationshipRelationship.setStatus(existingRelationship.getStatus());
                }
                if (termISATypeOFRelationshipRelationship.getSteward() == null)
                {
                    termISATypeOFRelationshipRelationship.setSteward(existingRelationship.getSteward());
                }
            }
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship.TermISATypeOFRelationship gennedRelationship = IsaTypeOfMapper.mapTermISATypeOFRelationshipToOMRSRelationshipBean(termISATypeOFRelationshipRelationship);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship.TermISATypeOFRelationship updatedGennedRelationship = subjectAreaOmasREST.updateTermISATypeOFRelationshipRelationship(userId, gennedRelationship);
            TermISATypeOFRelationship updatedRelationship = IsaTypeOfMapper.mapOMRSRelationshipBeanToTermISATypeOFRelationship(updatedGennedRelationship);
            response = new TermISATYPEOFRelationshipResponse(updatedRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Delete a TermISATypeOFRelationship relationship, which is an inheritance relationship between two spine objects.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the TermISATypeOFRelationship relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the TermISATypeOFRelationship relationship with the requested guid
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
        final String methodName = "deleteIsATypeOfRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            if (isPurge)
            {
                subjectAreaOmasREST.purgeRelationshipById(userId, guid, "TermISATypeOFRelationship");
                response = new VoidResponse();

            } else
            {
                Relationship omrsRelationship = subjectAreaOmasREST.deleteRelationshipById(userId, guid, "TermISATypeOFRelationship");
                org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship.TermISATypeOFRelationship deletedRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship.TermISATypeOFRelationshipMapper.mapOmrsRelationshipToTermISATypeOFRelationship(omrsRelationship);
                TermISATypeOFRelationship deletedRelationship = IsaTypeOfMapper.mapOMRSRelationshipBeanToTermISATypeOFRelationship(deletedRelationshipBean);
                response = new TermISATYPEOFRelationshipResponse(deletedRelationship);
            }
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (RelationshipNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertRelationshipNotDeletedException(e);
       } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotPurgedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }
    /**
     * Restore a is TermISATypeOFRelationship relationship.
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
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreTermISATypeOF(String serverName, String userId, String guid)
    {
        final String methodName = "restoreTermISATypeOF";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            Relationship omrsRelationship = this.oMRSAPIHelper.callOMRSRestoreRelationship(userId, guid);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship.TermISATypeOFRelationship deletedRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship.TermISATypeOFRelationshipMapper.mapOmrsRelationshipToTermISATypeOFRelationship(omrsRelationship);
            TermISATypeOFRelationship deletedRelationship = IsaTypeOfMapper.mapOMRSRelationshipBeanToTermISATypeOFRelationship(deletedRelationshipBean);
            response = new TermISATYPEOFRelationshipResponse(deletedRelationship);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (GUIDNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotDeletedException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
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
        final String methodName = "getSemanticAssignmentRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);

            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SemanticAssignment.SemanticAssignment relationshipBean = subjectAreaOmasREST.getSemanticAssignmentRelationshipByGuid(userId, guid);
            SemanticAssignment semanticAssignment = SemanticAssignmentMapper.mapOMRSRelationshipBeanToSemanticAssignment(relationshipBean);
            response = new SemanticAssignementRelationshipResponse(semanticAssignment);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }
}