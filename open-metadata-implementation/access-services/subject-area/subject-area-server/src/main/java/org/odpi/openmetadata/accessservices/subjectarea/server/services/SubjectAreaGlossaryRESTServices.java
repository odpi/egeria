/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.CanonicalVocabulary.CanonicalVocabulary;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Taxonomy.Taxonomy;
import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaBeansToAccessOMRS;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.IconSummary;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.GlossaryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.TypeGuids;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectAreaDefinition Open Metadata
 * Assess Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 */

public class SubjectAreaGlossaryRESTServices extends SubjectAreaRESTServicesInstance
{
    // these guids need to match the archive types. I did not want to introduce a dependancy on the archive types to get them.
    public static final String GLOSSARY_TYPE_GUID = "36f66863-9726-4b41-97ee-714fd0dc6fe4";
    public static final String TERM_ANCHOR_RELATIONSHIP_GUID = "1d43d661-bdc7-4a91-a996-3239b8f82e56";
    public static final String CATEGORY_ANCHOR_RELATIONSHIP_GUID = "c628938e-815e-47db-8d1c-59bb2e84e028";
    public static final String CATEGORY_HIERARCHY_LINK_GUID = "71e4b6fb-3412-4193-aff3-a16eccd87e8e";
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaGlossaryRESTServices.class);
    private static final String className = SubjectAreaGlossaryRESTServices.class.getName();

    /**
     * Default constructor
     */
    public SubjectAreaGlossaryRESTServices() {
        super();
    }
    public SubjectAreaGlossaryRESTServices(OMRSAPIHelper oMRSAPIHelper)
    {
        this.oMRSAPIHelper =oMRSAPIHelper;
    }


    /**
     * Create a Glossary. There are specializations of glossaries that can also be created using this operation.
     * To create a specialization, you should specify a nodeType other than Glossary in the supplied glossary.
     *
     * Glossaries with the same name can be confusing. Best practise is to createGlossaries that have unique names.
     * This Create call does not police that glossary names are unique. So it is possible to create Glossaries with the same name as each other.
     *
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     *     <li>Taxonomy to create a Taxonomy </li>
     *     <li>CanonicalGlossary to create a canonical glossary </li>
     *     <li>TaxonomyAndCanonicalGlossary to create a glossary that is both a taxonomy and a canonical glossary </li>
     *     <li>Glossary to create a glossary that is not a taxonomy or a canonical glossary</li>
     * </ul>
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param suppliedGlossary Glossary to create
     * @return response, when successful contains the created glossary.
     * when not successful the following Exception responses can occur
     *  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     *  MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     *  InvalidParameterException            one of the parameters is null or invalid.
     *  UnrecognizedGUIDException            the supplied guid was not recognised
     *  ClassificationException              Error processing a classification
     *  StatusNotSupportedException          A status value is not supported
     */
    public SubjectAreaOMASAPIResponse createGlossary(String serverName, String userId, org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary suppliedGlossary) {
        final String methodName = "createGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response =null;
        try {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId,suppliedGlossary.getEffectiveFromTime(),suppliedGlossary.getEffectiveToTime(), methodName);
            SubjectAreaGlossaryRESTServices glossaryRESTServices = new SubjectAreaGlossaryRESTServices();
            glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
            org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary generatedGlossary = null;
            InputValidator.validateNodeType( className,methodName,suppliedGlossary.getNodeType(), NodeType.Glossary,NodeType.Taxonomy,NodeType.TaxonomyAndCanonicalGlossary,NodeType.CanonicalGlossary);
            final String suppliedGlossaryName = suppliedGlossary.getName();
            generatedGlossary = GlossaryMapper.mapGlossaryToOMRSBean(suppliedGlossary);
            // need to check we have a name
            if (suppliedGlossaryName == null || suppliedGlossaryName.equals("")) {
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_CREATE_WITHOUT_NAME;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(className,
                        methodName);
                log.error(errorMessage);
                throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                        className,
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            } else {
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary newGeneratedGlossary =  subjectAreaOmasREST.createGlossary(userId, generatedGlossary);
                org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary newGlossary = GlossaryMapper.mapOMRSBeantoGlossary(newGeneratedGlossary);
                String glossaryGuid  = newGlossary.getSystemAttributes().getGUID();
                if (suppliedGlossary.getNodeType() == NodeType.Taxonomy || suppliedGlossary.getNodeType() == NodeType.TaxonomyAndCanonicalGlossary)
                {
                    List< org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification> classifications = new ArrayList<>();
                    classifications.add(new Taxonomy());
                    subjectAreaOmasREST.addGlossaryCategoryClassifications(userId, glossaryGuid, classifications);
                }
                if (suppliedGlossary.getNodeType() == NodeType.CanonicalGlossary || suppliedGlossary.getNodeType() == NodeType.TaxonomyAndCanonicalGlossary)
                {
                    List< org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification> classifications = new ArrayList<>();
                    classifications.add(new CanonicalVocabulary());
                    subjectAreaOmasREST.addGlossaryCategoryClassifications(userId, glossaryGuid, classifications);
                }
               if (suppliedGlossary.getNodeType()== NodeType.CanonicalGlossary) {
                   newGlossary.setNodeType(NodeType.CanonicalGlossary);
               } else if (suppliedGlossary.getNodeType()== NodeType.Taxonomy) {
                   newGlossary.setNodeType(NodeType.Taxonomy);
               } else if (suppliedGlossary.getNodeType()== NodeType.TaxonomyAndCanonicalGlossary) {
                   newGlossary.setNodeType(NodeType.TaxonomyAndCanonicalGlossary);
               }

                   response = new GlossaryResponse(newGlossary);
            }
        } catch (MetadataServerUncontactableException e) {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UserNotAuthorizedException e) {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (ClassificationException e) {
            response = OMASExceptionToResponse.convertClassificationException(e);
        } catch (StatusNotSupportedException e) {
            response = OMASExceptionToResponse.convertStatusNotsupportedException(e);
        } catch (UnrecognizedGUIDException e) {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response =" + response);
        }
        return response;
    }

    /**
     * Get a glossary by guid.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the glossary to get
     * @return response which when successful contains the glossary with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse getGlossaryByGuid(String serverName, String userId, String guid) {
        final String methodName = "getGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary generatedGlossary = subjectAreaOmasREST.getGlossaryById(userId, guid);
            org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary gotGlossary = GlossaryMapper.mapOMRSBeantoGlossary(generatedGlossary);
            List<org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification> classifications = generatedGlossary.getClassifications();
            // set the org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary classifications into the Node
            gotGlossary.setClassifications(classifications);

            String mediaReferenceTypeGuid = TypeGuids.getMediaReferenceTypeGuid();
            // At some stage we may wish to consider paging this request - for now assuing that the number of related media is likely to be low.
            // get the related media relationships
            Set<Line> glossaryRelationships = subjectAreaOmasREST.getGlossaryRelationships(userId, guid, mediaReferenceTypeGuid, 0, null, null, null, 0);
            // get the icons from the related media relationships
            Set<IconSummary> icons =  SubjectAreaUtils.getIconSummarySet(userId,subjectAreaOmasREST,glossaryRelationships);
            if (icons != null) {
                gotGlossary.setIcons(icons);
            }
            response = new GlossaryResponse(gotGlossary);
        } catch (MetadataServerUncontactableException e) {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UserNotAuthorizedException e) {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UnrecognizedGUIDException e) {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        }
        if (log.isDebugEnabled()) {
                log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }
    /**
     * Find Glossary
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param searchCriteria String expression matching Glossary property values.
     * @param asOfTime the glossaries returned as they were at this time. null indicates at the current time.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is no limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of Glossaries meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a find was issued but the repository does not implement find functionality in some way.</li>
     * </ul>
     */
    public  SubjectAreaOMASAPIResponse findGlossary(String serverName, String userId,
                                                String searchCriteria,
                                                Date asOfTime,
                                                Integer offset,
                                                Integer pageSize,
                                                org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                String sequencingProperty) {

        final String methodName = "findGlossary";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        List<Glossary> glossaries = null;
        SubjectAreaOMASAPIResponse response =null;
        try
        {

                // initialise omrs API helper with the right instance based on the server name
                initializeAPI(serverName, userId, methodName);
                List<EntityDetail> entitydetails = OMRSAPIHelper.findEntitiesByType(oMRSAPIHelper,serverName, userId,"Glossary", searchCriteria, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty, methodName);
                if (entitydetails !=null) {
                    glossaries= new ArrayList<>();
                    for (EntityDetail entityDetail : entitydetails) {
                        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary generatedGlossary = org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.GlossaryMapper.mapOmrsEntityDetailToGlossary(entityDetail);
                        Glossary glossary = GlossaryMapper.mapOMRSBeantoGlossary(generatedGlossary);
                        glossaries.add(glossary);
                    }
                }
                response =new GlossariesResponse(glossaries);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (FunctionNotSupportedException e)
        {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", Response=" + response);
        }
        return response;
    }
    /*
     * Get Glossary relationships
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
     * @return the relationships associated with the requested Glossary guid
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

    public  SubjectAreaOMASAPIResponse getGlossaryRelationships(String serverName, String userId,String guid,
                                                            Date asOfTime,
                                                            Integer offset,
                                                            Integer pageSize,
                                                            org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                            String sequencingProperty
    ) {
        final String methodName = "getGlossaryRelationships";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        SubjectAreaOMASAPIResponse response =null;
        try {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            // check that the guid is that of a Glossary by getting the guid
            subjectAreaOmasREST.getGlossaryById(userId, guid);
            response = getRelationshipsFromGuid(serverName,userId,guid,asOfTime,offset,pageSize,sequencingOrder,sequencingProperty);
        } catch (MetadataServerUncontactableException e) {
            OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UserNotAuthorizedException e) {
            OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UnrecognizedGUIDException e) {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", Response=" + response);
        }
        return response;
    }

    /**
     * Update a Glossary
     * <p>
     * If the caller has chosen to incorporate the glossary name in their Glossary Terms or Categories qualified name, renaming the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * If the caller has chosen to incorporate the glossary qualifiedName in their Glossary Terms or Categories qualified name, changing the qualified name of the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * Status is not updated using this call.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId           unique identifier for requesting user, under which the request is performed
     * @param guid             guid of the glossary to update
     * @param suppliedGlossary glossary to be updated
     * @param isReplace flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateGlossary(String serverName, String userId, String guid, Glossary suppliedGlossary, boolean isReplace) {
        final String methodName = "updateGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
         SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateNodeType(className, methodName, suppliedGlossary.getNodeType(), NodeType.Glossary, NodeType.Taxonomy, NodeType.TaxonomyAndCanonicalGlossary, NodeType.CanonicalGlossary);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

            response = getGlossaryByGuid(serverName,userId, guid);
            if (response.getResponseCategory().equals(ResponseCategory.Glossary)) {
                org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary originalGlossary = ((GlossaryResponse) response).getGlossary();
                if (originalGlossary.getSystemAttributes() != null) {
                    Status status = originalGlossary.getSystemAttributes().getStatus();
                    SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.GLOSSARY_UPDATE_FAILED_ON_DELETED_GLOSSARY);
                }
                if (suppliedGlossary.getSystemAttributes() != null) {
                    Status status = suppliedGlossary.getSystemAttributes().getStatus();
                    SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.STATUS_UPDATE_TO_DELETED_NOT_ALLOWED);
                }

                Glossary updateGlossary = originalGlossary;
                if (isReplace) {
                    // copy over attributes
                    updateGlossary.setName(suppliedGlossary.getName());
                    updateGlossary.setQualifiedName(suppliedGlossary.getQualifiedName());
                    updateGlossary.setDescription(suppliedGlossary.getDescription());
                    updateGlossary.setUsage(suppliedGlossary.getUsage());
                    updateGlossary.setAdditionalProperties(suppliedGlossary.getAdditionalProperties());
                    //TODO handle governance classifications and other classifications
                } else {
                    // copy over attributes if specified
                    if (suppliedGlossary.getName() != null) {
                        updateGlossary.setName(suppliedGlossary.getName());
                    }
                    if (suppliedGlossary.getQualifiedName() != null) {
                        updateGlossary.setQualifiedName(suppliedGlossary.getQualifiedName());
                    }
                    if (suppliedGlossary.getDescription() != null) {
                        updateGlossary.setDescription(suppliedGlossary.getDescription());
                    }
                    if (suppliedGlossary.getUsage() != null) {
                        updateGlossary.setUsage(suppliedGlossary.getUsage());
                    }
                    if (suppliedGlossary.getAdditionalProperties() != null) {
                        updateGlossary.setAdditionalProperties(suppliedGlossary.getAdditionalProperties());
                    }
                    //TODO handle governance classifications and other classifications
                }
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary generatedGlossary = null;

                generatedGlossary = GlossaryMapper.mapGlossaryToOMRSBean(updateGlossary);
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary updatedGeneratedGlossary = null;

                updatedGeneratedGlossary =subjectAreaOmasREST.updateGlossary(userId, generatedGlossary);

                org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary updatedGlossary = GlossaryMapper.mapOMRSBeantoGlossary(updatedGeneratedGlossary);
                response = new GlossaryResponse(updatedGlossary);
            }
        } catch (MetadataServerUncontactableException e) {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UserNotAuthorizedException e) {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (UnrecognizedGUIDException e) {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ",response=" + response);
        }
        return response;

    }

    /**
     * Delete a Glossary instance
     * <p>
     * The deletion of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the glossary instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the glossary will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId  unique identifier for requesting user, under which the request is performed
     * @param guid    guid of the glossary to be deleted.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the glossary was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the glossary was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteGlossary(String serverName, String userId, String guid, Boolean isPurge) {
        final String methodName = "deleteGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", guid=" + guid);
        }

        SubjectAreaOMASAPIResponse response = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            // do not delete if there is glossary content (terms or categories)
            // look for all glossary content that is not deleted.
            List<InstanceStatus> statusList = new ArrayList<>();
            statusList.add(InstanceStatus.ACTIVE);

            if (isPurge) {
                // go ahead with the purge, this will only work if the guid is soft deleted.
                subjectAreaOmasREST.purgeGlossaryByGuid(userId, guid);
                response = new VoidResponse();
            } else {
                // if this is a not a purge then attempt to get terms and categories, as we should not delete if there are any
                List<Relationship> terms = oMRSAPIHelper.callGetRelationshipsForEntity(userId, guid, TERM_ANCHOR_RELATIONSHIP_GUID, 0, statusList, null, null, null, 1);
                List<Relationship> categories = oMRSAPIHelper.callGetRelationshipsForEntity(userId, guid, CATEGORY_ANCHOR_RELATIONSHIP_GUID, 0, statusList, null, null, null, 1);
                if (((terms == null) || terms.isEmpty()) &&
                        (categories == null || categories.isEmpty())) {

                    org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary deletedGeneratedGlossary = null;

                    EntityDetail entityDetail = subjectAreaOmasREST.deleteGlossaryByGuid(userId, guid);
                    deletedGeneratedGlossary = org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.GlossaryMapper.mapOmrsEntityDetailToGlossary(entityDetail);
                    org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary deletedGlossary = GlossaryMapper.mapOMRSBeantoGlossary(deletedGeneratedGlossary);
                    response = new GlossaryResponse(deletedGlossary);
                } else {
                    SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_CONTENT_PREVENTED_DELETE;
                    String errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage(className,
                            methodName, guid);
                    log.error(errorMessage);
                    InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            className,
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                    response = new InvalidParameterExceptionResponse(e);
                }
            }
        } catch (MetadataServerUncontactableException e) {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UserNotAuthorizedException e) {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (FunctionNotSupportedException e) {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (UnrecognizedGUIDException e) {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (GUIDNotPurgedException e)
        {
            response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
        } catch (EntityNotDeletedException e)
        {
            response = OMASExceptionToResponse.convertEntityNotDeletedException(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }
    /**
     * Restore a Glossary
     *
     * Restore allows the deleted Glossary to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the glossary to restore
     * @return response which when successful contains the restored glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreGlossary(String serverName, String userId, String guid)
    {
        final String methodName = "restoreGlossary";
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
            org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary restoredGeneratedGlossary = null;

            EntityDetail omrsEntity = this.oMRSAPIHelper.callOMRSRestoreEntity(userId, guid);
            restoredGeneratedGlossary = org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.GlossaryMapper.mapOmrsEntityDetailToGlossary(omrsEntity);
            org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary restoredGlossary = GlossaryMapper.mapOMRSBeantoGlossary(restoredGeneratedGlossary);
            response = new GlossaryResponse(restoredGlossary);
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
