/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.EntityDetailResponse;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.EntityDetailsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.RelationshipsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.CanonicalGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.CanonicalTaxonomy;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Taxonomy;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.GlossaryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaGlossaryRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * SubjectAreaGlossaryHandler manages Glossary objects from the property server.  It runs server-side in the subject Area
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
public class SubjectAreaGlossaryHandler extends SubjectAreaHandler {
    private static final Class clazz = SubjectAreaGlossaryHandler.class;
    private static final String className = clazz.getName();
    private static final Logger log = LoggerFactory.getLogger(clazz);

    /**
     * Construct the Subject Area Glossary Handler
     * needed to operate within a single server instance.
     *
     * @param serviceName             name of the consuming service
     * @param serverName              name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     * @param oMRSAPIHelper           omrs API helper
     * @param errorHandler            handler for repository service errors
     */
    public SubjectAreaGlossaryHandler(String serviceName,
                                      String serverName,
                                      InvalidParameterHandler invalidParameterHandler,
                                      OMRSRepositoryHelper repositoryHelper,
                                      RepositoryHandler repositoryHandler,
                                      OMRSAPIHelper oMRSAPIHelper,
                                      RepositoryErrorHandler errorHandler) {
        super(serviceName, serverName,invalidParameterHandler,repositoryHelper,repositoryHandler,oMRSAPIHelper,errorHandler);
    }


    /**
     * Take an entityDetail response and map it to the appropriate glossary orientated response
     *
     * @param response entityDetailResponse
     * @return glossary response containing the appropriate glossary object.
     */
    @Override
    protected SubjectAreaOMASAPIResponse getResponse(SubjectAreaOMASAPIResponse response) {
        EntityDetailResponse entityDetailResponse = (EntityDetailResponse) response;
        EntityDetail entityDetail = entityDetailResponse.getEntityDetail();
        GlossaryMapper glossaryMapper = new GlossaryMapper(oMRSAPIHelper);
        try {
            Glossary glossary = glossaryMapper.mapEntityDetailToNode(entityDetail);
            if (glossary.getNodeType() == NodeType.TaxonomyAndCanonicalGlossary) {
                CanonicalTaxonomy canonicalTaxonomy = (CanonicalTaxonomy) glossary;
                response = new GlossaryResponse(canonicalTaxonomy);
            } else if (glossary.getNodeType() == NodeType.Taxonomy) {
                Taxonomy taxonomy = (Taxonomy) glossary;
                response = new GlossaryResponse(taxonomy);
            } else if (glossary.getNodeType() == NodeType.CanonicalGlossary) {
                CanonicalGlossary canonicalGlossary = (CanonicalGlossary) glossary;
                response = new GlossaryResponse(canonicalGlossary);
            } else {
                response = new GlossaryResponse(glossary);
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }

        return response;
    }

    /**
     * Create a Glossary. There are specializations of glossaries that can also be created using this operation.
     * To create a specialization, you should specify a nodeType other than Glossary in the supplied glossary.
     * <p>
     * Glossaries with the same name can be confusing. Best practise is to createGlossaries that have unique names.
     * This Create call does not police that glossary names are unique. So it is possible to create Glossaries with the same name as each other.
     *
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     * <li>Taxonomy to create a Taxonomy </li>
     * <li>CanonicalGlossary to create a canonical glossary </li>
     * <li>TaxonomyAndCanonicalGlossary to create a glossary that is both a taxonomy and a canonical glossary </li>
     * <li>Glossary to create a glossary that is not a taxonomy or a canonical glossary</li>
     * </ul>
     *
     * @param userId           unique identifier for requesting user, under which the request is performed
     * @param suppliedGlossary Glossary to create
     * @return response, when successful contains the created glossary.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li>ClassificationException              Error processing a classification.</li>
     * <li>StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createGlossary(String userId, Glossary suppliedGlossary) {
        final String methodName = "createGlossary";
        SubjectAreaOMASAPIResponse response = null;

        SubjectAreaGlossaryRESTServices glossaryRESTServices = new SubjectAreaGlossaryRESTServices();
        glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        try {
            InputValidator.validateNodeType(className, methodName, suppliedGlossary.getNodeType(), NodeType.Glossary, NodeType.Taxonomy, NodeType.TaxonomyAndCanonicalGlossary, NodeType.CanonicalGlossary);
            final String suppliedGlossaryName = suppliedGlossary.getName();

            // need to check we have a name
            if (suppliedGlossaryName == null || suppliedGlossaryName.equals("")) {
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_CREATE_WITHOUT_NAME;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName);
                log.error(errorMessage);
                throw new org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
            } else {
                GlossaryMapper glossaryMapper = new GlossaryMapper(oMRSAPIHelper);
                EntityDetail glossaryEntityDetail = glossaryMapper.mapNodeToEntityDetail(suppliedGlossary);
                response = oMRSAPIHelper.callOMRSAddEntity(methodName, userId, glossaryEntityDetail);
                if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                    EntityDetailResponse entityDetailResponse = (EntityDetailResponse) response;
                    EntityDetail entityDetail = entityDetailResponse.getEntityDetail();
                    response = getGlossaryByGuid(userId, entityDetail.getGUID());
                }
            }
        } catch (org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        return response;
    }

    /**
     * Get a glossary by guid.
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the glossary to get
     * @return response which when successful contains the glossary with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse getGlossaryByGuid(String userId, String guid) {
        final String methodName = "getGlossaryByGuid";
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaGlossaryRESTServices glossaryRESTServices = new SubjectAreaGlossaryRESTServices();
        glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

        try {
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            response = oMRSAPIHelper.callOMRSGetEntityByGuid(methodName, userId, guid);
            if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                response = getResponse(response);
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        return response;
    }

    /**
     * Find Glossary
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param searchCriteria     String expression matching Glossary property values. If not specified then all glossaries are returned.
     * @param asOfTime           the glossaries returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is no limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
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
    public SubjectAreaOMASAPIResponse findGlossary(String userId,
                                                   String searchCriteria,
                                                   Date asOfTime,
                                                   Integer offset,
                                                   Integer pageSize,
                                                   org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                   String sequencingProperty) {

        final String methodName = "findGlossary";
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaGlossaryRESTServices glossaryRESTServices = new SubjectAreaGlossaryRESTServices();
        glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

        /*
         * If no search criteria is supplied then we return all glossaries, this should not be too many.
         */
        if (searchCriteria == null) {
            response = oMRSAPIHelper.getEntitiesByType(oMRSAPIHelper, methodName, userId, "Glossary", asOfTime, offset, pageSize);
        } else {
            response = oMRSAPIHelper.findEntitiesByPropertyValue(methodName, userId, "Glossary", searchCriteria, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty, methodName);
        }
        if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetails)) {
            EntityDetailsResponse entityDetailsResponse = (EntityDetailsResponse) response;
            List<EntityDetail> entityDetails = entityDetailsResponse.getEntityDetails();

            List<Glossary> glossaries = new ArrayList<>();
            if (entityDetails == null) {
                response = new GlossariesResponse(glossaries);
            } else {
                for (EntityDetail entityDetail : entityDetails) {
                    // call the getGlossary so that the GlossarySummary and other parts are populated.
                    response = getGlossaryByGuid(userId, entityDetail.getGUID());
                    if (response.getResponseCategory() == ResponseCategory.Glossary) {
                        GlossaryResponse glossaryResponse = (GlossaryResponse) response;
                        Glossary glossary = glossaryResponse.getGlossary();
                        glossaries.add(glossary);
                    } else {
                        break;
                    }
                }
                if (response.getResponseCategory() == ResponseCategory.Glossary) {
                    response = new GlossariesResponse(glossaries);
                }
            }
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
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException   Function not supported.</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getGlossaryRelationships(String userId, String guid,
                                                               Date asOfTime,
                                                               Integer offset,
                                                               Integer pageSize,
                                                               org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                               String sequencingProperty
                                                              ) {
        String methodName = "getGlossaryRelationships";
        return getRelationshipsFromGuid(methodName, userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
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
     * @param userId           unique identifier for requesting user, under which the request is performed
     * @param guid             guid of the glossary to update
     * @param suppliedGlossary glossary to be updated
     * @param isReplace        flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    public SubjectAreaOMASAPIResponse updateGlossary(String userId, String guid, Glossary suppliedGlossary, boolean isReplace) {
        final String methodName = "updateGlossary";
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaGlossaryRESTServices glossaryRESTServices = new SubjectAreaGlossaryRESTServices();
        glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

        try {

            InputValidator.validateNodeType(className, methodName, suppliedGlossary.getNodeType(), NodeType.Glossary, NodeType.Taxonomy, NodeType.TaxonomyAndCanonicalGlossary, NodeType.CanonicalGlossary);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

            response = getGlossaryByGuid(userId, guid);
            if (response.getResponseCategory().equals(ResponseCategory.Glossary)) {
                org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary originalGlossary = ((GlossaryResponse) response).getGlossary();
                Glossary updateGlossary = originalGlossary;
                if (isReplace) {
                    // copy over attributes
                    updateGlossary.setName(suppliedGlossary.getName());
                    updateGlossary.setQualifiedName(suppliedGlossary.getQualifiedName());
                    updateGlossary.setDescription(suppliedGlossary.getDescription());
                    updateGlossary.setUsage(suppliedGlossary.getUsage());
                    updateGlossary.setAdditionalProperties(suppliedGlossary.getAdditionalProperties());
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
                }
                Date termFromTime = suppliedGlossary.getEffectiveFromTime();
                Date termToTime = suppliedGlossary.getEffectiveToTime();
                updateGlossary.setEffectiveFromTime(termFromTime);
                updateGlossary.setEffectiveToTime(termToTime);
                GlossaryMapper glossaryMapper = new GlossaryMapper(oMRSAPIHelper);
                EntityDetail entityDetail = glossaryMapper.mapNodeToEntityDetail(updateGlossary);
                String glossaryGuid = entityDetail.getGUID();
                response = oMRSAPIHelper.callOMRSUpdateEntityProperties(methodName, userId, entityDetail);
                if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                    response = getGlossaryByGuid(userId, glossaryGuid);
                }
            }
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
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the glossary to be deleted.
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the glossary was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the glossary was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteGlossary(String userId, String guid, Boolean isPurge) {
        final String methodName = "deleteGlossary";
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaGlossaryRESTServices glossaryRESTServices = new SubjectAreaGlossaryRESTServices();
        glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        try {
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            // do not delete if there is glossary content (terms or categories)
            // look for all glossary content that is not deleted.
            List<InstanceStatus> statusList = new ArrayList<>();
            statusList.add(InstanceStatus.ACTIVE);
            OMRSRepositoryHelper repositoryHelper = this.oMRSAPIHelper.getOMRSRepositoryHelper();
            //TODO get source properly
            String source = oMRSAPIHelper.getServiceName();
            String glossaryTypeDefName = "Glossary";
            String glossaryTypeDefGuid = repositoryHelper.getTypeDefByName(source, glossaryTypeDefName).getGUID();

            if (isPurge) {
                oMRSAPIHelper.callOMRSPurgeEntity(methodName, userId, glossaryTypeDefName, glossaryTypeDefGuid, guid);
                response = new VoidResponse();
            } else {
                String termAnchorGuid = repositoryHelper.getTypeDefByName(methodName, "TermAnchor").getGUID();
                String categoryAnchorGuid = repositoryHelper.getTypeDefByName(methodName, "TermAnchor").getGUID();

                // if this is a not a purge then attempt to get terms and categories, as we should not delete if there are any
                response = oMRSAPIHelper.callGetRelationshipsForEntity(methodName, userId, guid, termAnchorGuid, 0, statusList, null, null, null, 1);
                if (response.getResponseCategory().equals(ResponseCategory.OmrsRelationships)) {
                    RelationshipsResponse termAnchorRelationshipsResponse = (RelationshipsResponse) response;
                    List<Relationship> termRelationships = termAnchorRelationshipsResponse.getRelationships();
                    response = oMRSAPIHelper.callGetRelationshipsForEntity(methodName, userId, guid, categoryAnchorGuid, 0, statusList, null, null, null, 1);
                    if (response.getResponseCategory().equals(ResponseCategory.OmrsRelationships)) {
                        RelationshipsResponse categoryAnchorRelationshipsResponse = (RelationshipsResponse) response;
                        List<Relationship> categoryRelationships = categoryAnchorRelationshipsResponse.getRelationships();
                        if (((termRelationships == null) || termRelationships.isEmpty()) && (categoryRelationships == null || categoryRelationships.isEmpty())) {
                            response = oMRSAPIHelper.callOMRSDeleteEntity(methodName, userId, glossaryTypeDefName, glossaryTypeDefGuid, guid);
                            if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                                response = getResponse(response);
                            }
                        } else {
                            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_CONTENT_PREVENTED_DELETE;
                            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName, guid);
                            log.error(errorMessage);
                            InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
                            response = new InvalidParameterExceptionResponse(e);
                        }
                    }
                }
            }

        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        return response;
    }


    /**
     * Restore a Glossary
     * <p>
     * Restore allows the deleted Glossary to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
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
    public SubjectAreaOMASAPIResponse restoreGlossary(String userId, String guid) {
        final String methodName = "restoreGlossary";
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaGlossaryRESTServices glossaryRESTServices = new SubjectAreaGlossaryRESTServices();
        glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        try {
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            response = this.oMRSAPIHelper.callOMRSRestoreEntity(methodName, userId, guid);
            if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                response = getGlossaryByGuid(userId, guid);
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        return response;
    }

}
