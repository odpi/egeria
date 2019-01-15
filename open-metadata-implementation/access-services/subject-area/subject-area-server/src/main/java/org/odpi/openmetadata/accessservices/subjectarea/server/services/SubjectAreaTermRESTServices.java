/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermAnchor.TermAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship.TermISATypeOFRelationshipMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaBeansToAccessOMRS;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.IconSummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.TermMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.TypeGuids;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.accessservices.subjectarea.validators.RestValidator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * TheSubjectAreaTermRESTServices provides the server-side implementation of the SubjectAreaDefinition Open Metadata
 * Access Service (OMAS) for Terms.  This interface provides term authoring interfaces for subject area experts.
 */

public class SubjectAreaTermRESTServices extends SubjectAreaRESTServicesInstance
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaTermRESTServices.class);

    private static final String className = SubjectAreaTermRESTServices.class.getName();

    /**
     * Default constructor
     */
    public SubjectAreaTermRESTServices()
    {
        //SubjectAreaRESTServicesInstance registers this omas.
    }
    public SubjectAreaTermRESTServices(OMRSAPIHelper oMRSAPIHelper)
    {
        this.oMRSAPIHelper =oMRSAPIHelper;
    }

    /**
     * Create a Term
     * <p>
     * The name needs to be specified - as this is the main identifier for the term. The name should be unique for canonical glossaries. This API does not police the uniqueness in this case.
     * <p>
     * The qualifiedName can be specified and will be honoured. If it is specified then the caller may wish to ensure that it is
     * unique. If this qualifiedName is not specified then one will be generated as GlossaryTerm concatinated with the the guid.
     * <p>
     * Failure to create the Terms classifications, link to its glossary or its icon, results in the create failing and the term being deleted
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId       userId
     * @param suppliedTerm term to create
     * @return response, when successful contains the created term.
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
    public SubjectAreaOMASAPIResponse createTerm(String serverName, String userId, Term suppliedTerm)
    {
        final String methodName = "createTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }

        SubjectAreaOMASAPIResponse response = null;
        GlossaryTerm glossaryTerm = null;
        Glossary associatedGlossary = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS termRESTServices= initializeAPI(serverName, userId,suppliedTerm.getEffectiveFromTime(),suppliedTerm.getEffectiveToTime(), methodName);
            InputValidator.validateNodeType(className, methodName, suppliedTerm.getNodeType(), NodeType.Term);
            SubjectAreaGlossaryRESTServices glossaryRESTServices = new SubjectAreaGlossaryRESTServices();
            glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
            glossaryTerm = TermMapper.mapTermToOMRSBean(suppliedTerm);
            // need to check we have a name
            final String suppliedTermName = suppliedTerm.getName();
            if (suppliedTermName == null || suppliedTermName.equals(""))
            {
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITHOUT_NAME;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName);
                log.error(errorMessage);
                throw new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
            }

            // should we remove this restriction?
            if (suppliedTerm.getProjects() != null && !suppliedTerm.getProjects().isEmpty())
            {
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITH_PROJECTS;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName, suppliedTermName);
                log.error(errorMessage);
                throw new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
            }

            GlossarySummary suppliedGlossary = suppliedTerm.getGlossary();

            SubjectAreaOMASAPIResponse glossaryResponse = RestValidator.validateGlossarySummaryDuringCreation(serverName,userId,methodName, suppliedGlossary, glossaryRESTServices);
            if (glossaryResponse.getResponseCategory().equals(ResponseCategory.Category.Glossary))
            {
                // store the associated glossary
                associatedGlossary = ((GlossaryResponse) glossaryResponse).getGlossary();
                GlossaryTerm newGlossaryTerm = termRESTServices.createGlossaryTerm(userId, glossaryTerm);
                String termGuid = newGlossaryTerm.getSystemAttributes().getGUID();
                // Knit the Term to the supplied glossary
                String glossaryGuid = associatedGlossary.getSystemAttributes().getGUID();
                TermAnchor termAnchor = new TermAnchor();
                termAnchor.setEntity1Guid(glossaryGuid);
                termAnchor.setEntity2Guid(termGuid);
                termRESTServices.createTermAnchorRelationship(userId, termAnchor);
                response = getTermByGuid(serverName,userId, termGuid);
            } else
            {
                // error
                response = glossaryResponse;
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
        } catch (ClassificationException e)
        {
            response = OMASExceptionToResponse.convertClassificationException(e);
        } catch (StatusNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertStatusNotsupportedException(e);
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
     * Get a Term
     *
     * @param serverName  serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the term to get
     * @return response which when successful contains the term with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getTermByGuid(String serverName, String userId, String guid)
    {
        final String methodName = "getTermByGuid";
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
            GlossaryTerm glossaryTerm = subjectAreaOmasREST.getGlossaryTermById(userId, guid);
            Term gotTerm = TermMapper.mapOMRSBeantoTerm(glossaryTerm);
            List<Classification> classifications = glossaryTerm.getClassifications();
            // set the GlossaryTerm classifications into the Node
            gotTerm.setClassifications(classifications);

            String mediaReferenceTypeGuid = TypeGuids.getMediaReferenceTypeGuid();
            // At some stage we may wish to consider paging this request - for now assuing that the number of related media is likely to be low.
            // get the related media relationships
            Set<Line> mediaRelationships = subjectAreaOmasREST.getGlossaryRelationships(userId, guid, mediaReferenceTypeGuid, 0, null, null, null, 0);
            // get the icons from the related media relationships
            Set<IconSummary> icons =  SubjectAreaUtils.getIconSummarySet(userId,subjectAreaOmasREST,mediaRelationships);
            if (icons != null) {
                gotTerm.setIcons(icons);
            }
            String anchorTypeGuid = TypeGuids.getTermAnchorTypeGuid();
            Set<Line> glossaryRelationships = subjectAreaOmasREST.getGlossaryRelationships(userId, guid, anchorTypeGuid, 0, null, null, null, 0);
            if ( glossaryRelationships.iterator().hasNext()) {
                Line glossaryRelationship = glossaryRelationships.iterator().next();
                if (SubjectAreaUtils.isEffective(glossaryRelationship))
                {
                    GlossarySummary glossarySummary = SubjectAreaUtils.getGlossarySummaryForTerm(userId, subjectAreaOmasREST, glossaryRelationship);
                    gotTerm.setGlossary(glossarySummary);
                }
            }
            response = new TermResponse(gotTerm);

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
            //should not occur TODO appropriate error/log
        }


        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", Response=" + response);
        }
        return response;
    }
    /*
     * Get Term relationships
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the term to get
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is not limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return the relationships associated with the requested Term guid
     *
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException   Function not supported
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    public  SubjectAreaOMASAPIResponse getTermRelationships(String serverName, String userId,String guid,
                                                           Date asOfTime,
                                                           Integer offset,
                                                           Integer pageSize,
                                                           org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                           String sequencingProperty
    ) {
        final String methodName = "getTermRelationships";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        SubjectAreaOMASAPIResponse response =null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            // if offset or pagesize were not supplied then default them, so they can be converted to primitives.
            if (offset ==null) {
                offset = new Integer(0);
            }
            if (pageSize == null) {
                pageSize = new Integer(0);
            }
            SequencingOrder omrsSequencingOrder =  SubjectAreaUtils.convertOMASToOMRSSequencingOrder(sequencingOrder);
            Set<Line> omrsTermRelationships = subjectAreaOmasREST.getGlossaryTermRelationships(
                    userId,
                    guid,
                    null,
                    offset,
                    asOfTime,
                    sequencingProperty,
                    omrsSequencingOrder,
                    pageSize);
            List<Line> termRelationships = SubjectAreaUtils.convertOMRSLinesToOMASLines(omrsTermRelationships);
            //TODO change the follow to a while loop . refactor to a method so it can be reused for find.

            int sizeToGet = 0;
            if (omrsTermRelationships.size() > termRelationships.size()) {
                sizeToGet = omrsTermRelationships.size() - termRelationships.size();
            }

            while (sizeToGet >0) {

                // there are more relationships we need to get
                omrsTermRelationships = subjectAreaOmasREST.getGlossaryTermRelationships(
                        userId,
                        guid,
                        null,
                        offset + sizeToGet,
                        asOfTime,
                        sequencingProperty,
                        omrsSequencingOrder,
                        sizeToGet);
                if (omrsTermRelationships != null) {
                    sizeToGet = getSizeToGet(omrsTermRelationships, termRelationships);
                    offset = +sizeToGet;
                }
            }
            response =new TermRelationshipsResponse(termRelationships);
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
            //should not occur as it is issued when a type guid is supplied - we supply null. TODO appropriate error/log
        }


        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", Response=" + response);
        }
        return response;
    }

    private int getSizeToGet(Set<Line> omrsTermRelationships, List<Line> termRelationships) {
        int sizeToGet =0;
        List<Line> moreTermRelationships = SubjectAreaUtils.convertOMRSLinesToOMASLines(omrsTermRelationships);
        if ( moreTermRelationships !=null && moreTermRelationships.size() >0) {
            for (Line moreTermRelationship : moreTermRelationships) {
                termRelationships.add(moreTermRelationship);
            }
            sizeToGet = omrsTermRelationships.size() - moreTermRelationships.size();

        }
        return sizeToGet;
    }

    /**
     * Update a Term
     * <p>
     * Status is not updated using this call.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId       userId under which the request is performed
     * @param guid         guid of the term to update
     * @param suppliedTerm term to be updated
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateTerm(String serverName, String userId, String guid, Term suppliedTerm, boolean isReplace)
    {
        final String methodName = "updateTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }

        SubjectAreaOMASAPIResponse response = null;

        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, suppliedTerm.getEffectiveFromTime(),suppliedTerm.getEffectiveToTime(),methodName);
            InputValidator.validateNodeType(className, methodName, suppliedTerm.getNodeType(), NodeType.Term);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            response = getTermByGuid(serverName,userId, guid);
            if (response.getResponseCategory().equals(ResponseCategory.Term))
            {
                org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term originalTerm = ((TermResponse) response).getTerm();
                if (originalTerm.getSystemAttributes() != null)
                {
                    Status status = originalTerm.getSystemAttributes().getStatus();
                    SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.GLOSSARY_UPDATE_FAILED_ON_DELETED_GLOSSARY);
                }
                if (suppliedTerm.getSystemAttributes() != null)
                {
                    Status status = suppliedTerm.getSystemAttributes().getStatus();
                    SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.STATUS_UPDATE_TO_DELETED_NOT_ALLOWED);
                }
                Term updateTerm = originalTerm;
                if (isReplace)
                {
                    // copy over attributes
                    updateTerm.setName(suppliedTerm.getName());
                    updateTerm.setQualifiedName(suppliedTerm.getQualifiedName());
                    updateTerm.setDescription(suppliedTerm.getDescription());
                    updateTerm.setUsage(suppliedTerm.getUsage());
                    updateTerm.setAbbreviation(suppliedTerm.getAbbreviation());
                    updateTerm.setExamples(suppliedTerm.getExamples());
                    updateTerm.setSpineObject(suppliedTerm.isSpineObject());
                    updateTerm.setSpineAttribute(suppliedTerm.isSpineAttribute());
                    updateTerm.setObjectIdentifier(suppliedTerm.isObjectIdentifier());
                    updateTerm.setAdditionalProperties(suppliedTerm.getAdditionalProperties());
                    //TODO handle governance classifications and other classifications
                } else
                {
                    // copy over attributes if specified
                    if (suppliedTerm.getName() != null)
                    {
                        updateTerm.setName(suppliedTerm.getName());
                    }
                    if (suppliedTerm.getQualifiedName() != null)
                    {
                        updateTerm.setQualifiedName(suppliedTerm.getQualifiedName());
                    }
                    if (suppliedTerm.getDescription() != null)
                    {
                        updateTerm.setDescription(suppliedTerm.getDescription());
                    }
                    if (suppliedTerm.getUsage() != null)
                    {
                        updateTerm.setUsage(suppliedTerm.getUsage());
                    }
                    if (suppliedTerm.getAbbreviation() != null)
                    {
                        updateTerm.setAbbreviation(suppliedTerm.getAbbreviation());
                    }
                    if (suppliedTerm.getAdditionalProperties() != null)
                    {
                        updateTerm.setAdditionalProperties(suppliedTerm.getAdditionalProperties());
                    }
                    if (suppliedTerm.getExamples() != null)
                    {
                        updateTerm.setExamples(suppliedTerm.getExamples());
                    }
                    if (suppliedTerm.isSpineObject())
                    {
                        updateTerm.setSpineObject(suppliedTerm.isSpineObject());
                    }
                    if (suppliedTerm.isSpineObject())
                    {
                        updateTerm.setSpineAttribute(suppliedTerm.isSpineAttribute());
                    }
                    if (suppliedTerm.isObjectIdentifier())
                    {
                        updateTerm.setObjectIdentifier(suppliedTerm.isObjectIdentifier());
                    }
                    //TODO handle governance classifications and other classifications
                }
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm generatedTerm = null;

                generatedTerm = TermMapper.mapTermToOMRSBean(updateTerm);
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm updatedGeneratedTerm = null;

                updatedGeneratedTerm = subjectAreaOmasREST.updateGlossaryTerm(userId, generatedTerm);

                org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term updatedTerm = TermMapper.mapOMRSBeantoTerm(updatedGeneratedTerm);
                response = new TermResponse(updatedTerm);
            }
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }  catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ",response=" + response);
        }
        return response;

    }


    /**
     * Delete a Term instance
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the term instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the term will not exist after the operation.
     * when not successful the following Exception responses can occur
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId  userId under which the request is performed
     * @param guid    guid of the term to be deleted.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the term was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the term was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteTerm(String serverName, String userId, String guid, Boolean isPurge)
    {
        final String methodName = "deleteTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", guid=" + guid);
        }

        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            if (isPurge)
            {
                subjectAreaOmasREST.purgeGlossaryTermByGuid(userId, guid);
                response = new VoidResponse();
            } else
            {
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm deletedGeneratedTerm = null;
                EntityDetail entityDetail =subjectAreaOmasREST.deleteGlossaryTermByGuid(userId, guid);
                deletedGeneratedTerm = org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTermMapper.mapOmrsEntityDetailToGlossaryTerm(entityDetail);
                org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term deletedTerm = TermMapper.mapOMRSBeantoTerm(deletedGeneratedTerm);
                response = new TermResponse(deletedTerm);

            }
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (GUIDNotPurgedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
        } catch (EntityNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertEntityNotDeletedException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }
    /**
     * Restore a Term
     *
     * Restore allows the deleted Term to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the term to delete
     * @return response which when successful contains the restored term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreTerm(String serverName, String userId, String guid)
    {
        final String methodName = "restoreTerm";
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
            org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm restoredGeneratedTerm = null;

            EntityDetail omrsEntity = this.oMRSAPIHelper.callOMRSRestoreEntity(userId, guid);
            restoredGeneratedTerm = org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTermMapper.mapOmrsEntityDetailToGlossaryTerm(omrsEntity);
            org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term restoredTerm = TermMapper.mapOMRSBeantoTerm(restoredGeneratedTerm);
            response = new TermResponse(restoredTerm);
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
}
