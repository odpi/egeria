/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTermReferences;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Referenceable.Referenceable;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.GlossaryTermToGlossary.AnchorReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.GlossaryTermToGlossaryCategory.CategoriesReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.GlossaryTermToReferenceable.AssignedElementsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToRelatedMedia.RelatedMediaReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermAnchor.TermAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaBeansToAccessOMRS;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.AssetSummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.IconSummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Antonym;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.RelatedTermRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Synonym;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermHASARelationship;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.TermMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.AntonymMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.HasAMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.RelatedTermMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.SynonymMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.accessservices.subjectarea.validators.RestValidator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * TheSubjectAreaTermRESTServices provides the server-side implementation of the SubjectAreaDefinition Open Metadata
 * Access Service (OMAS) for Terms.  This interface provides relationship authoring interfaces for subject area experts.
 */

public class SubjectAreaRelationshipRESTServices extends SubjectAreaRESTServices
{

    private static final Logger log = LoggerFactory.getLogger(SubjectAreaRelationshipRESTServices.class);

    private static final String className = SubjectAreaRelationshipRESTServices.class.getName();

    /**
     * Default constructor
     */
    public SubjectAreaRelationshipRESTServices()
    {
        //SubjectAreaRESTServices registers this omas.
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
     * <li> FunctionNotSupportedException        Function not supported
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createTerm(String userId, Term suppliedTerm)
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
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateNodeType(className, methodName, suppliedTerm.getNodeType(), NodeType.Term);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
        SubjectAreaGlossaryRESTServices glossaryRESTServices = new SubjectAreaGlossaryRESTServices();
        if (response == null)
        {
            service.setOMRSAPIHelper(this.oMRSAPIHelper);
            SubjectAreaRelationshipRESTServices termRESTServices = new SubjectAreaRelationshipRESTServices();
            termRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

            glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

            try
            {
                glossaryTerm = TermMapper.mapTermToOMRSBean(suppliedTerm);
            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }
        // need to check we have a name
        final String suppliedTermName = suppliedTerm.getName();
        if (response == null && (suppliedTermName == null || suppliedTermName.equals("")))
        {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITHOUT_NAME;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName);
            log.error(errorMessage);
            InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }

        // should we remove this restriction?
        if (response == null && (suppliedTerm.getCategories() != null && !suppliedTerm.getCategories().isEmpty()))
        {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITH_CATEGORIES;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName, suppliedTermName);
            log.error(errorMessage);
            InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
            response = OMASExceptionToResponse.convertInvalidParameterException(e);

        }
        // should we remove this restriction?
        if (response == null && (suppliedTerm.getProjects() != null && !suppliedTerm.getProjects().isEmpty()))
        {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITH_PROJECTS;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName, suppliedTermName);
            log.error(errorMessage);
            InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
            response = OMASExceptionToResponse.convertInvalidParameterException(e);

        }
        // We do not allow terms to be associated with assets in this OMAS
        // there is no setAssets on Term - but in theory a Term returned from a get could be fed into a create - which should be rejected.
        if (response == null && (suppliedTerm.getAssets() != null && !suppliedTerm.getAssets().isEmpty()))
        {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITH_ASSETS;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName, suppliedTermName);
            log.error(errorMessage);
            InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
            response = OMASExceptionToResponse.convertInvalidParameterException(e);

        }
        GlossarySummary suppliedGlossary = suppliedTerm.getGlossary();
        if (response == null)
        {

            SubjectAreaOMASAPIResponse glossaryResponse = RestValidator.validateGlossarySummaryDuringCreation(methodName, suppliedGlossary, glossaryRESTServices, userId);
            if (glossaryResponse.getResponseCategory().equals(ResponseCategory.Category.Glossary))
            {
                // store the associated glossary
                associatedGlossary = ((GlossaryResponse) glossaryResponse).getGlossary();
            } else
            {
                // error
                response = glossaryResponse;
            }

        }
        GlossaryTerm newGlossaryTerm = null;
        List<Classification> classifications = new ArrayList<>();
        String termGuid = null;

        if (response == null)
        {
            try
            {
                newGlossaryTerm = service.createGlossaryTerm(userId, glossaryTerm);
                termGuid = newGlossaryTerm.getSystemAttributes().getGUID();
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
            }
        }
        if (response == null)
        {
            // Knit the Term to the supplied glossary
            String glossaryGuid = associatedGlossary.getSystemAttributes().getGUID();
            TermAnchor termAnchor = new TermAnchor();
            termAnchor.setEntity1Guid(glossaryGuid);
            termAnchor.setEntity2Guid(termGuid);

            try
            {
                service.createTermAnchorRelationship(userId, termAnchor);
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
        }
        if (response == null)
        {
            // We could perform other relationship creation here. I suggest not - and we encourage users to use relationship creation API
            response = getTermByGuid(userId, termGuid);
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
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the term to get
     * @return response which when successful contains the term with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getTermByGuid(String userId, String guid)
    {
        final String methodName = "getTermByGuid";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response == null)
        {
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
            subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
            GlossaryTerm glossaryTerm = null;
            try
            {
                glossaryTerm = subjectAreaOmasREST.getGlossaryTermById(userId, guid);
                Term gotTerm = TermMapper.mapOMRSBeantoTerm(glossaryTerm);
                List<Classification> classifications = glossaryTerm.getClassifications();
                // set the GlossaryTerm classifications into the Node
                gotTerm.setClassifications(classifications);

                Set<Line> termRelationships = subjectAreaOmasREST.getGlossaryTermRelationships(userId, guid);
                GlossaryTermReferences glossaryTermReferences = new GlossaryTermReferences(guid, termRelationships);
                if (response == null)
                {
                    // set icon
                    Set<RelatedMediaReference> relatedMediaReferenceSet = glossaryTermReferences.getRelatedMediaReferences();
                    Set<IconSummary> icons = SubjectAreaUtils.getIconSummaries(userId, relatedMediaReferenceSet);
                    if (icons != null)
                    {
                        gotTerm.setIcons(icons);
                    }
                    // set assets
                    Set<AssetSummary> assets = new HashSet();
                    Set<AssignedElementsReference> assignedElementsReferences = glossaryTermReferences.getAssignedElementsReferences();
                    if (assignedElementsReferences != null && !assignedElementsReferences.isEmpty())
                    {
                        for (AssignedElementsReference assignedElementsReference : assignedElementsReferences)
                        {
                            Referenceable referenceable = assignedElementsReference.getReferenceable();
                            AssetSummary asset = new AssetSummary();
                            asset.setGuid(referenceable.getSystemAttributes().getGUID());
                            asset.setQualifiedName(referenceable.getQualifiedName());
                            assets.add(asset);
                        }
                        gotTerm.setAssets(assets);
                    }

                    // set categories
                    Set<CategorySummary> categories = new HashSet();
                    Set<CategoriesReference> categoriesReferences = glossaryTermReferences.getCategoriesReferences();
                    if (categoriesReferences != null && !categoriesReferences.isEmpty())
                    {
                        for (CategoriesReference categoryReference : categoriesReferences)
                        {
                            CategorySummary category = new CategorySummary();
                            category.setGuid(categoryReference.getRelatedEndGuid());
                            category.setRelationshipguid(categoryReference.getRelationshipGuid());
                            category.setQualifiedName(categoryReference.getGlossaryCategory().getQualifiedName());
                            category.setName(categoryReference.getGlossaryCategory().getDisplayName());
                            categories.add(category);
                        }

                        gotTerm.setCategories(categories);
                    }


                    // set terms
//                    Set<TermSummary> terms = glossaryTermReferences.getTermSummaries();
//                    gotTerm.setTerms(terms);


                    AnchorReference anchorReference = glossaryTermReferences.getAnchorReference();
                    if (anchorReference != null)
                    {

                        //get the glossary - we need this for the name and qualified name
                        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary glossary = subjectAreaOmasREST.getGlossaryById(userId, anchorReference.getRelatedEndGuid());
                        // set glossary summary
                        GlossarySummary glossarySummary = new GlossarySummary();
                        glossarySummary.setName(glossary.getDisplayName());
                        glossarySummary.setQualifiedName(glossary.getQualifiedName());
                        glossarySummary.setGuid(anchorReference.getRelatedEndGuid());
                        glossarySummary.setRelationshipguid(anchorReference.getRelationshipGuid());
                        glossarySummary.setRelationshipType(anchorReference.getRelationship_Type());
                        gotTerm.setGlossary(glossarySummary);
                    }

                    response = new TermResponse(gotTerm);
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
            } catch (FunctionNotSupportedException e)
            {
                response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
            }
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", Response=" + response);
        }
        return response;
    }

    /**
     * Update a Term
     * <p>
     * Status is not updated using this call.
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of the term to update
     * @param suppliedTerm term to be updated
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateTerm(String userId, String guid, Term suppliedTerm, boolean isReplace)
    {
        final String methodName = "updateTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateNodeType(className, methodName, suppliedTerm.getNodeType(), NodeType.Term);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response == null)
        {
            SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
            service.setOMRSAPIHelper(this.oMRSAPIHelper);
            response = getTermByGuid(userId, guid);
            if (response.getResponseCategory().equals(ResponseCategory.Term))
            {
                Term originalTerm = ((TermResponse) response).getTerm();
                if (originalTerm.getSystemAttributes() != null)
                {
                    Status status = originalTerm.getSystemAttributes().getStatus();
                    SubjectAreaOMASAPIResponse deleteCheckResponse = SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.GLOSSARY_UPDATE_FAILED_ON_DELETED_GLOSSARY);
                    if (deleteCheckResponse != null)
                    {
                        response = deleteCheckResponse;
                    }
                }
                if (suppliedTerm.getSystemAttributes() != null)
                {
                    Status status = suppliedTerm.getSystemAttributes().getStatus();
                    SubjectAreaOMASAPIResponse deleteCheckResponse = SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.STATUS_UPDATE_TO_DELETED_NOT_ALLOWED);
                    if (deleteCheckResponse != null)
                    {
                        response = deleteCheckResponse;
                    }
                }
                if (response != null)
                {
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
                        if (suppliedTerm.getExamples()!=null)
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
                    GlossaryTerm generatedTerm = null;
                    try
                    {
                        generatedTerm = TermMapper.mapTermToOMRSBean(updateTerm);
                        GlossaryTerm updatedGeneratedTerm = null;
                        try
                        {
                            updatedGeneratedTerm = service.updateGlossaryTerm(userId, generatedTerm);
                        } catch (MetadataServerUncontactableException e)
                        {
                            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
                        } catch (UserNotAuthorizedException e)
                        {
                            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
                        } catch (UnrecognizedGUIDException e)
                        {
                            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
                        }
                        Term updatedTerm = TermMapper.mapOMRSBeantoTerm(updatedGeneratedTerm);
                        response = new TermResponse(updatedTerm);
                    } catch (InvalidParameterException e)
                    {
                        response = OMASExceptionToResponse.convertInvalidParameterException(e);
                    }
                }
            }
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
    public SubjectAreaOMASAPIResponse deleteTerm(String userId, String guid, Boolean isPurge)
    {
        final String methodName = "deleteTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response == null)
        {
            SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
            service.setOMRSAPIHelper(oMRSAPIHelper);
            if (isPurge)
            {
                try
                {
                    service.purgeGlossaryTermByGuid(userId, guid);
                    response = new VoidResponse();
                } catch (MetadataServerUncontactableException e)
                {
                    response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
                } catch (UserNotAuthorizedException e)
                {
                    response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
                } catch (InvalidParameterException e)
                {
                    response = OMASExceptionToResponse.convertInvalidParameterException(e);
                } catch (UnrecognizedGUIDException e)
                {
                    response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
                } catch (EntityNotDeletedException e)
                {
                    response = OMASExceptionToResponse.convertEntityNotDeletedException(e);
                } catch (GUIDNotPurgedException e)
                {
                    response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
                }

            } else
            {
                GlossaryTerm deletedGeneratedTerm = null;
                try
                {
                    EntityDetail entityDetail = service.deleteGlossaryTermByGuid(userId, guid);
                    deletedGeneratedTerm = org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTermMapper.mapOmrsEntityDetailToGlossaryTerm(entityDetail);
                    Term deletedTerm = TermMapper.mapOMRSBeantoTerm(deletedGeneratedTerm);
                    response = new TermResponse(deletedTerm);
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
                }
            }
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }

    /**
     * Create a TermHASARelationship is the relationship between a spine object and a spine attribute.
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termGuid             guid of the term on which the relationship should be created
     * @param termHASARelationship the HASA relationship
     * @return response, when successful contains the created TermHASARelationship
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
    public SubjectAreaOMASAPIResponse createTermHASARelationship(String userId, String termGuid, TermHASARelationship termHASARelationship)
    {
        final String methodName = "createTermHASARelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId, ",TermGuid=" + termGuid);
        }
        SubjectAreaOMASAPIResponse response = null;
        TermHASARelationship createdTermHASARelationship = null;
        try
        {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, termGuid, "termGuid");
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response == null)
        {
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
            subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
            try
            {
                GlossaryTerm glossaryTerm = subjectAreaOmasREST.getGlossaryTermById(userId, termGuid);
                Term gotTerm = TermMapper.mapOMRSBeantoTerm(glossaryTerm);
                // TODO error if not a term or subclass
                if (gotTerm.isSpineObject())
                {
                    // TODO error HASA relationship need to be created from the object end.
                }
                GlossaryTerm glossaryTermOtherEnd = subjectAreaOmasREST.getGlossaryTermById(userId, termHASARelationship.getEntity2Guid());
                Term otherTerm = TermMapper.mapOMRSBeantoTerm(glossaryTermOtherEnd);
                // TODO error if not a term or subclass
                if (otherTerm.isSpineAttribute())
                {
                    // TODO error
                }
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
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the HAS A relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getTermHASARelationship(String userId, String guid)  {
        final String methodName = "getTermHASARelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
            subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);





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
        return response;
    }
    /**
     * Update a TermHASARelationship is the relationship between a spine object and a spine attribute.
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created TermHASARelationship
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
    public SubjectAreaOMASAPIResponse updateTermHASARelationship(String userId, TermHASARelationship termHASARelationship,boolean isReplace)
    {
        final String methodName = "updateTermHASARelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        TermHASARelationship createdTermHASARelationship = null;
        String relationshipGuid = termHASARelationship.getGuid();
        try
        {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, relationshipGuid, "termGuid");
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response == null)
        {
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
            subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
            try
            {
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
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the HAS A relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
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

    public SubjectAreaOMASAPIResponse deleteTermHASARelationship(String userId, String guid, Boolean isPurge)  {
        final String methodName = "deleteTermHASARelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
            subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
            if (isPurge)
            {
                try
                {
                    subjectAreaOmasREST.purgeRelationshipById(userId, guid,"TermHASARelationship");
                    response = new VoidResponse();
                } catch (RelationshipNotDeletedException e)
                {
                    response = OMASExceptionToResponse.convertRelationshipNotDeletedException(e);
                } catch (GUIDNotPurgedException e)
                {
                    response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
                }

            } else
            {
                GlossaryTerm deletedGeneratedTerm = null;
                try
                {
                    Relationship omrsRelationship = subjectAreaOmasREST.deleteRelationshipById(userId,guid,"TermHASARelationship");
                    org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationship deletedRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationshipMapper.mapOmrsRelationshipToTermHASARelationship(omrsRelationship);
                    TermHASARelationship deletedRelationship = HasAMapper.mapOMRSRelationshipBeanToTermHASARelationship(deletedRelationshipBean);
                    response = new TermHASARelationshipResponse(deletedRelationship);
                } catch (FunctionNotSupportedException e)
                {
                    response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
                }
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
        }
        return response;
    }

    /**
     * Create a RelatedTermRelationship. A Related Term is a link between two similar Terms.
     *
     * @param userId               userId under which the request is performed
     * @param termGuid             guid of the term on which the relationship should be created
     * @param relatedTermRelationshipRelationship the RelatedTermRelationship relationship
     * @return response, when successful contains the created Related Term relationship
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
    public SubjectAreaOMASAPIResponse createRelatedTerm(String userId, String termGuid, RelatedTermRelationship relatedTermRelationshipRelationship)
    {
        final String methodName = "RelatedTermRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId, ",TermGuid=" + termGuid);
        }
        SubjectAreaOMASAPIResponse response = null;
        RelatedTermRelationship createdRelatedTermRelationship = null;
        try
        {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, termGuid, "termGuid");
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response == null)
        {
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
            subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
            try
            {
                GlossaryTerm glossaryTerm = subjectAreaOmasREST.getGlossaryTermById(userId, termGuid);
                Term gotTerm = TermMapper.mapOMRSBeantoTerm(glossaryTerm);
                // error if not a term or subclass
                GlossaryTerm glossaryTermOtherEnd = subjectAreaOmasREST.getGlossaryTermById(userId, relatedTermRelationshipRelationship.getEntity2Guid());
                Term otherTerm = TermMapper.mapOMRSBeantoTerm(glossaryTermOtherEnd);
                // error if not a term or subclass
                org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm gennedRelationship = RelatedTermMapper.mapRelatedTermToOMRSRelationshipBean(relatedTermRelationshipRelationship);

                org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm createdGennedRelationship = subjectAreaOmasREST.createRelatedTermRelationship(userId, gennedRelationship);
                RelatedTermRelationship createdRelationship = RelatedTermMapper.mapOMRSRelationshipBeanToRelatedTerm(createdGennedRelationship);

                response = new RelatedTermRelationshipResponse(createdRelationship);

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
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the related term relationship to get
     * @return response which when successful contains the related term relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getRelatedTermRelationship(String userId, String guid)  {
        final String methodName = "getRelatedTermRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
            subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm relationshipBean = subjectAreaOmasREST.getRelatedTermRelationshipByGuid(userId, guid);
            RelatedTermRelationship relatedterm =RelatedTermMapper.mapOMRSRelationshipBeanToRelatedTerm(relationshipBean);
            response = new RelatedTermRelationshipResponse(relatedterm);
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
        return response;
    }

    /**
     * Update a Related Term relationship. A Related Term is a link between two similar Terms.
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param relatedTermRelationship the related term  relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created RelatedTermRelationship
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
    public SubjectAreaOMASAPIResponse updateRelatedTermRelationship(String userId, RelatedTermRelationship relatedTermRelationship,boolean isReplace)
    {
        final String methodName = "updateRelatedTermRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        RelatedTermRelationship createdRelatedTermRelationship = null;
        String relationshipGuid = relatedTermRelationship.getGuid();
        try
        {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, relationshipGuid, "termGuid");
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response == null)
        {
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
            subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
            try
            {
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
                RelatedTermRelationship updatedRelationship = RelatedTermMapper.mapOMRSRelationshipBeanToRelatedTerm(updatedGennedRelationship);
                response = new RelatedTermRelationshipResponse(updatedRelationship);
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
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Related term relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
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

    public SubjectAreaOMASAPIResponse deleteRelatedTermRelationship(String userId, String guid, Boolean isPurge)  {
        final String methodName = "deleteRelatedTermRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
            subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
            if (isPurge)
            {
                try
                {
                    subjectAreaOmasREST.purgeRelationshipById(userId, guid,"RelatedTerm");
                    response = new VoidResponse();
                } catch (RelationshipNotDeletedException e)
                {
                    response = OMASExceptionToResponse.convertRelationshipNotDeletedException(e);
                } catch (GUIDNotPurgedException e)
                {
                    response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
                }

            } else
            {
                try
                {
                    Relationship omrsRelationship = subjectAreaOmasREST.deleteRelationshipById(userId,guid,"RelatedTerm");
                    org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm deletedRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTermMapper.mapOmrsRelationshipToRelatedTerm(omrsRelationship);
                    RelatedTermRelationship deletedRelationship = RelatedTermMapper.mapOMRSRelationshipBeanToRelatedTerm(deletedRelationshipBean);
                    response = new RelatedTermRelationshipResponse(deletedRelationship);
                } catch (FunctionNotSupportedException e)
                {
                    response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
                }
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
        }
        return response;
    }
    /**
     *  Create a synonym relationship, which is a link between glossary terms that have the same meaning.
     *
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termGuid             guid of the term on which the relationship should be created
     * @param synonym the Synonym relationship
     * @return response, when successful contains the created synonym relationship
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
    public SubjectAreaOMASAPIResponse createSynonym(String userId, String termGuid, Synonym synonym)
    {
        final String methodName = "Synonym";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId, ",TermGuid=" + termGuid);
        }
        SubjectAreaOMASAPIResponse response = null;
        Synonym createdSynonym = null;
        try
        {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, termGuid, "termGuid");
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        // tolerate more than one synonym between the same terms. They may have different expressions and other attributes.

        if (response == null)
        {
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
            subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
            try
            {
                GlossaryTerm glossaryTerm = subjectAreaOmasREST.getGlossaryTermById(userId, termGuid);
                Term gotTerm = TermMapper.mapOMRSBeantoTerm(glossaryTerm);

                GlossaryTerm glossaryTermOtherEnd = null;
                // error if not a term or subclass
                glossaryTermOtherEnd = subjectAreaOmasREST.getGlossaryTermById(userId, synonym.getEntity2Guid());
                if(glossaryTermOtherEnd.getSystemAttributes().getGUID().equals(termGuid)) {
                    glossaryTermOtherEnd = subjectAreaOmasREST.getGlossaryTermById(userId, synonym.getEntity1Guid());
                }

                Term otherTerm = TermMapper.mapOMRSBeantoTerm(glossaryTermOtherEnd);
                // error if not a term or subclass
                org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym gennedRelationship = SynonymMapper.mapSynonymToOMRSRelationshipBean(synonym);

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
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }
    /**
     * Get a synonym relationship
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the related term relationship to get
     * @return response which when successful contains the synonym relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getSynonymRelationship(String userId, String guid)  {
        final String methodName = "getSynonymRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
            subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym relationshipBean = subjectAreaOmasREST.getSynonymRelationshipByGuid(userId, guid);
            Synonym synonym =SynonymMapper.mapOMRSRelationshipBeanToSynonym(relationshipBean);
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
        return response;
    }
    /**
     * Update a Synonym relationship which is a link between glossary terms that have the same meaning
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param synonymRelationship the Synonym relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created SynonymRelationship
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
    public SubjectAreaOMASAPIResponse updateSynonymRelationship(String userId, Synonym synonymRelationship,boolean isReplace)
    {
        final String methodName = "updateRSynonymRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        Synonym createdSynonym = null;
        String relationshipGuid = synonymRelationship.getGuid();
        try
        {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, relationshipGuid, "termGuid");
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response == null)
        {
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
            subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
            try
            {
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
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Synonym relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
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

    public SubjectAreaOMASAPIResponse deleteSynonymRelationship(String userId, String guid, Boolean isPurge)  {
        final String methodName = "deleteSynonymRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
            subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
            if (isPurge)
            {
                try
                {
                    subjectAreaOmasREST.purgeRelationshipById(userId, guid,"Synonym");
                    response = new VoidResponse();
                } catch (RelationshipNotDeletedException e)
                {
                    response = OMASExceptionToResponse.convertRelationshipNotDeletedException(e);
                } catch (GUIDNotPurgedException e)
                {
                    response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
                }

            } else
            {
                try
                {
                    Relationship omrsRelationship = subjectAreaOmasREST.deleteRelationshipById(userId,guid,"Synonym");
                    org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym deletedRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.SynonymMapper.mapOmrsRelationshipToSynonym(omrsRelationship);
                    Synonym deletedRelationship = SynonymMapper.mapOMRSRelationshipBeanToSynonym(deletedRelationshipBean);
                    response = new SynonymRelationshipResponse(deletedRelationship);
                } catch (FunctionNotSupportedException e)
                {
                    response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
                }
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
        }
        return response;
    }
    /**
     *  Create an antonym relationship, which is a link between glossary terms that have the opposite meaning.
     *
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termGuid             guid of the term on which the relationship should be created
     * @param antonym the Antonym relationship
     * @return response, when successful contains the created antonym relationship
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
    public SubjectAreaOMASAPIResponse createAntonym(String userId, String termGuid, Antonym antonym)
    {
        final String methodName = "createAntonym";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId, ",TermGuid=" + termGuid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, termGuid, "termGuid");
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response == null)
        {
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
            subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
            try
            {
                GlossaryTerm glossaryTerm = subjectAreaOmasREST.getGlossaryTermById(userId, termGuid);
                Term gotTerm = TermMapper.mapOMRSBeantoTerm(glossaryTerm);
                // TODO error if not a term or subclass
                GlossaryTerm glossaryTermOtherEnd = subjectAreaOmasREST.getGlossaryTermById(userId, antonym.getEntity2Guid());

                // error if not a term or subclass
                glossaryTermOtherEnd = subjectAreaOmasREST.getGlossaryTermById(userId, antonym.getEntity2Guid());
                if(glossaryTermOtherEnd.getSystemAttributes().getGUID().equals(termGuid)) {
                    glossaryTermOtherEnd = subjectAreaOmasREST.getGlossaryTermById(userId, antonym.getEntity1Guid());
                }
                String glossaryTermOtherEndGuid = glossaryTermOtherEnd.getSystemAttributes().getGUID();

                Term otherTerm = TermMapper.mapOMRSBeantoTerm(glossaryTermOtherEnd);
                // TODO error if not a term or subclass
                org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym gennedRelationship = AntonymMapper.mapAntonymToOMRSRelationshipBean(antonym);

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
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the related term relationship to get
     * @return response which when successful contains the antonym relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getAntonymRelationship(String userId, String guid)  {
        final String methodName = "getAntonymRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
            subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
            org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym relationshipBean = subjectAreaOmasREST.getAntonymRelationshipByGuid(userId, guid);
            Antonym antonym =AntonymMapper.mapOMRSRelationshipBeanToAntonym(relationshipBean);
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
        return response;
    }
    /**
     * Update a Antonym relationship which is a link between glossary terms that have the opposite meaning
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param AntonymRelationship the Antonym relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created AntonymRelationship
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
    public SubjectAreaOMASAPIResponse updateAntonymRelationship(String userId, Antonym AntonymRelationship,boolean isReplace)
    {
        final String methodName = "updateRAntonymRelationship";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        Antonym createdAntonym = null;
        String relationshipGuid = AntonymRelationship.getGuid();
        try
        {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, relationshipGuid, "termGuid");
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response == null)
        {
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
            subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
            try
            {
                org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym existingRelationship = subjectAreaOmasREST.getAntonymRelationshipByGuid(userId, relationshipGuid);

                if (isReplace)
                {
                    // use the relationship as supplied
                } else
                {
                    // copy over existing relationships attributes if they were not supplied
                    if (AntonymRelationship.getDescription() == null)
                    {
                        AntonymRelationship.setDescription(existingRelationship.getDescription());
                    }
                    if (AntonymRelationship.getSource() == null)
                    {
                        AntonymRelationship.setSource(existingRelationship.getSource());
                    }
                    if (AntonymRelationship.getStatus() == null)
                    {
                        AntonymRelationship.setStatus(existingRelationship.getStatus());
                    }
                    if (AntonymRelationship.getSteward() == null)
                    {
                        AntonymRelationship.setSteward(existingRelationship.getSteward());
                    }
                    if (AntonymRelationship.getExpression() == null)
                    {
                        AntonymRelationship.setExpression(existingRelationship.getExpression());
                    }
                }
                org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym gennedRelationship = AntonymMapper.mapAntonymToOMRSRelationshipBean(AntonymRelationship);
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
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Antonym relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
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

    public SubjectAreaOMASAPIResponse deleteAntonymRelationship(String userId, String guid, Boolean isPurge)  {
        final String methodName = "deleteAntonymRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
            subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
            if (isPurge)
            {
                try
                {
                    subjectAreaOmasREST.purgeRelationshipById(userId, guid,"Antonym");
                    response = new VoidResponse();
                } catch (RelationshipNotDeletedException e)
                {
                    response = OMASExceptionToResponse.convertRelationshipNotDeletedException(e);
                } catch (GUIDNotPurgedException e)
                {
                    response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
                }

            } else
            {
                try
                {
                    Relationship omrsRelationship = subjectAreaOmasREST.deleteRelationshipById(userId,guid,"Antonym");
                    org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym deletedRelationshipBean = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.AntonymMapper.mapOmrsRelationshipToAntonym(omrsRelationship);
                    Antonym deletedRelationship = AntonymMapper.mapOMRSRelationshipBeanToAntonym(deletedRelationshipBean);
                    response = new AntonymRelationshipResponse(deletedRelationship);
                } catch (FunctionNotSupportedException e)
                {
                    response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
                }
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
        }
        return response;
    }
}