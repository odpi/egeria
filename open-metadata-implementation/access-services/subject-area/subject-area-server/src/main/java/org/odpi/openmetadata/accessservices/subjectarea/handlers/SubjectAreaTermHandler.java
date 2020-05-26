/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria term. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.EntityDetailResponse;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.EntityDetailsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.GlossarySummaryResponse;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.RelationshipsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.GovernanceActions;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Activity;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.TermMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.TermAnchorMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaGlossaryRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaTermRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.TypeGuids;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * SubjectAreaTermHandler manages Term objects from the property server.  It runs server-side in the subject Area
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
public class SubjectAreaTermHandler extends SubjectAreaHandler{
    private static final Class clazz = SubjectAreaTermHandler.class;
    private static final String className = clazz.getName();
    private static final Logger log = LoggerFactory.getLogger(clazz);

    /**
     * Construct the Subject Area Term Handler
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
    public SubjectAreaTermHandler(String serviceName,
                                  String serverName,
                                  InvalidParameterHandler invalidParameterHandler,
                                  OMRSRepositoryHelper repositoryHelper,
                                  RepositoryHandler repositoryHandler,
                                  OMRSAPIHelper oMRSAPIHelper,
                                  RepositoryErrorHandler errorHandler) {
        super(serviceName, serverName,invalidParameterHandler,repositoryHelper,repositoryHandler,oMRSAPIHelper);
    }

    /**
     * Take an entityDetail response and map it to the appropriate Term orientated response
     * @param response entityDetailResponse
     * @return Term response containing the appropriate Term object.
     */
    @Override
    protected SubjectAreaOMASAPIResponse getResponse( SubjectAreaOMASAPIResponse response) {
        EntityDetailResponse entityDetailResponse = (EntityDetailResponse) response;
        EntityDetail entityDetail = entityDetailResponse.getEntityDetail();
        TermMapper termMapper = new TermMapper(oMRSAPIHelper);

        try {
            Term term= termMapper.mapEntityDetailToNode(entityDetail);
            if (term.getNodeType()==NodeType.Activity) {
                Activity activity = (Activity)term;
                response = new TermResponse(activity);
            } else {
                response = new TermResponse(term);
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }

        return response;
    }

    /**
     * Create a Term. There are specializations of terms that can also be created using this operation.
     * To create a specialization, you should specify a nodeType other than Term in the supplied term.
     * <p>
     * Terms with the same name can be confusing. Best practise is to createTerms that have unique names.
     * This Create call does not police that term names are unique. So it is possible to create Terms with the same name as each other.
     *
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     * <li>Taxonomy to create a Taxonomy </li>
     * <li>CanonicalTerm to create a canonical term </li>
     * <li>TaxonomyAndCanonicalTerm to create a term that is both a taxonomy and a canonical term </li>
     * <li>Term to create a term that is not a taxonomy or a canonical term</li>
     * </ul>
     *
     * @param glossaryHandler  glossary handler
     * @param userId           unique identifier for requesting user, under which the request is performed
     * @param suppliedTerm Term to create
     * @return response, when successful contains the created term.
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
    public SubjectAreaOMASAPIResponse createTerm( SubjectAreaGlossaryHandler glossaryHandler, String userId, Term suppliedTerm) {
        final String methodName = "createTerm";
        SubjectAreaOMASAPIResponse response = null;
        Glossary associatedGlossary = null;

        SubjectAreaTermRESTServices termRESTServices = new SubjectAreaTermRESTServices();
        termRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        if (response == null) {
            try {
                // TODO Activity
                InputValidator.validateNodeType(className, methodName, suppliedTerm.getNodeType(), NodeType.Term);
                SubjectAreaGlossaryRESTServices glossaryRESTServices = new SubjectAreaGlossaryRESTServices();
                glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

                // need to check we have a name
                final String suppliedTermName = suppliedTerm.getName();
                if (suppliedTermName == null || suppliedTermName.equals("")) {
                    ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITHOUT_NAME.getMessageDefinition();
                    throw new InvalidParameterException(messageDefinition,
                                                        className,
                                                        methodName,
                                                        "Name",
                                                        null);                }
                TermMapper termMapper = new TermMapper(oMRSAPIHelper);
                EntityDetail suppliedTermEntityDetail = termMapper.mapNodeToEntityDetail(suppliedTerm);
                GlossarySummary suppliedGlossary = suppliedTerm.getGlossary();

                SubjectAreaOMASAPIResponse glossaryResponse = validateGlossarySummaryDuringCreation(glossaryHandler, userId, methodName, suppliedGlossary);
                if (glossaryResponse.getResponseCategory().equals(ResponseCategory.Category.Glossary)) {
                    // store the associated glossary
                    associatedGlossary = ((GlossaryResponse) glossaryResponse).getGlossary();
                    response = oMRSAPIHelper.callOMRSAddEntity(methodName, userId, suppliedTermEntityDetail);
                    if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                        EntityDetailResponse entityDetailResponse = (EntityDetailResponse) response;
                        EntityDetail createdTermEntityDetail = entityDetailResponse.getEntityDetail();
                        String termGuid = createdTermEntityDetail.getGUID();
                        // Knit the Term to the supplied glossary
                        String glossaryGuid = associatedGlossary.getSystemAttributes().getGUID();
                        TermAnchor termAnchor = new TermAnchor();
                        termAnchor.setGlossaryGuid(glossaryGuid);
                        termAnchor.setTermGuid(termGuid);
                        TermAnchorMapper termAnchorMapper = new TermAnchorMapper(oMRSAPIHelper);
                        Relationship relationship = termAnchorMapper.mapLineToRelationship(termAnchor);
                        response = oMRSAPIHelper.callOMRSAddRelationship(methodName, userId, relationship);
                        if (response.getResponseCategory().equals(ResponseCategory.OmrsRelationship)) {
                            // the relationship creation was successful so get the Term to return.
                            response = getTermByGuid(userId, termGuid);
                        }
                    }
                } else {
                    // error
                    response = glossaryResponse;
                }
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Get a term by guid.
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the term to get
     * @return response which when successful contains the term with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse getTermByGuid(String userId, String guid) {
        final String methodName = "getTermByGuid";
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaTermRESTServices termRESTServices = new SubjectAreaTermRESTServices();
        termRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

        if (response == null) {
            try {
                InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
                response = oMRSAPIHelper.callOMRSGetEntityByGuid(methodName, userId, guid);
                if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                    EntityDetailResponse entityDetailResponse = (EntityDetailResponse) response;
                    EntityDetail gotEntityDetail = entityDetailResponse.getEntityDetail();
                    TermMapper termMapper = new TermMapper(oMRSAPIHelper);
                    Term gotTerm = (Term) termMapper.mapEntityDetailToNode(gotEntityDetail);
                    String anchorTypeGuid = TypeGuids.getTermAnchorTypeGuid();

                    response = oMRSAPIHelper.callGetRelationshipsForEntity(methodName, userId, guid, anchorTypeGuid, 0, null, null, null, 0);
                    if (response.getResponseCategory().equals(ResponseCategory.OmrsRelationships)) {
                        RelationshipsResponse relationshipsResponse = (RelationshipsResponse) response;
                        List<Relationship> glossaryRelationships = relationshipsResponse.getRelationships();
                        if (glossaryRelationships.iterator().hasNext()) {
                            Relationship glossaryRelationship = glossaryRelationships.iterator().next();
                            TermAnchor termAnchor = (TermAnchor) new TermAnchorMapper(oMRSAPIHelper).mapRelationshipToLine(glossaryRelationship);
                            response = SubjectAreaUtils.getGlossarySummaryForTerm(methodName, userId, oMRSAPIHelper, termAnchor, gotTerm);
                            if (response.getResponseCategory().equals(ResponseCategory.GlossarySummary)) {
                                GlossarySummaryResponse glossarySummaryResponse = (GlossarySummaryResponse) response;
                                GlossarySummary glossarySummary = glossarySummaryResponse.getGlossarySummary();
                                gotTerm.setGlossary(glossarySummary);
                                response = new TermResponse(gotTerm);
                            } else if (response == null) {
                                // there is no effective glossary
                                response = new TermResponse(gotTerm);
                            }
                        } else {
                            // return the Term without a Glossary summary as we have not got one.
                            response = new TermResponse(gotTerm);
                        }
                    }
                }

            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", Response=" + response);
        }
        return response;
    }


    /**
     * Find Term
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param searchCriteria     String expression matching Term property values. If not specified then all terms are returned.
     * @param asOfTime           the terms returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is no limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of Terms meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a find was issued but the repository does not implement find functionality in some way.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse findTerm(String userId,
                                                   String searchCriteria,
                                                   Date asOfTime,
                                                   Integer offset,
                                                   Integer pageSize,
                                                   org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                   String sequencingProperty) {

        final String methodName = "findTerm";
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaTermRESTServices termRESTServices = new SubjectAreaTermRESTServices();
        termRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

        /*
         * If no search criteria is supplied then we return all terms, this should not be too many.
         */
        if (searchCriteria == null) {
            response = oMRSAPIHelper.getEntitiesByType(oMRSAPIHelper, methodName, userId, "GlossaryTerm", asOfTime, offset, pageSize);
        } else {
            response = oMRSAPIHelper.findEntitiesByPropertyValue(methodName, userId, "GlossaryTerm", searchCriteria, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty, methodName);
        }
        if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetails)) {
            EntityDetailsResponse entityDetailsResponse = (EntityDetailsResponse) response;
            List<EntityDetail> entityDetails = entityDetailsResponse.getEntityDetails();

            List<Term> terms = new ArrayList<>();
            if (entityDetails == null) {
                response = new TermsResponse(terms);
            } else {
                for (EntityDetail entityDetail : entityDetails) {
                    // call the getTerm so that the TermSummary and other parts are populated.
                    response = getTermByGuid(userId, entityDetail.getGUID());
                    if (response.getResponseCategory() == ResponseCategory.Term) {
                        TermResponse termResponse = (TermResponse) response;
                        Term term = termResponse.getTerm();
                        terms.add(term);
                    } else {
                        break;
                    }
                }
                if (response.getResponseCategory() == ResponseCategory.Term) {
                    response = new TermsResponse(terms);
                }
            }
        }
        return response;
    }
    /**
     * Get Term relationships
     *
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
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException   Function not supported.</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getTermRelationships(String userId, String guid,
                                                               Date asOfTime,
                                                               Integer offset,
                                                               Integer pageSize,
                                                               org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                               String sequencingProperty
                                                              ) {
        String methodName = "getTermRelationships";
        return getRelationshipsFromGuid(methodName, userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
    }

    /**
     * Update a Term
     * <p>
     * If the caller has chosen to incorporate the term name in their Term Terms or Categories qualified name, renaming the term will cause those
     * qualified names to mismatch the Term name.
     * If the caller has chosen to incorporate the term qualifiedName in their Term Terms or Categories qualified name, changing the qualified name of the term will cause those
     * qualified names to mismatch the Term name.
     * Status is not updated using this call.
     *

     * @param userId           unique identifier for requesting user, under which the request is performed
     * @param guid             guid of the term to update
     * @param suppliedTerm term to be updated
     * @param isReplace        flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    public SubjectAreaOMASAPIResponse updateTerm(String userId, String guid, Term suppliedTerm, boolean isReplace) {
        final String methodName = "updateTerm";
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaTermRESTServices termRESTServices = new SubjectAreaTermRESTServices();
        termRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        TermMapper termMapper = new TermMapper(oMRSAPIHelper);
        try {

            InputValidator.validateNodeType(className, methodName, suppliedTerm.getNodeType(), NodeType.Term, NodeType.Activity);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

            response = getTermByGuid(userId, guid);
            if (response.getResponseCategory().equals(ResponseCategory.Term)) {
                Term originalTerm = ((TermResponse) response).getTerm();
                Term updateTerm = new Term();
                updateTerm.setSystemAttributes(originalTerm.getSystemAttributes());
                if (isReplace) {
                    // copy over attributes
                    updateTerm.setName(suppliedTerm.getName());
                    updateTerm.setQualifiedName(suppliedTerm.getQualifiedName());
                    updateTerm.setDescription(suppliedTerm.getDescription());
                    updateTerm.setAbbreviation(suppliedTerm.getAbbreviation());
                    updateTerm.setExamples(suppliedTerm.getExamples());
                    updateTerm.setUsage(suppliedTerm.getUsage());
                    updateTerm.setObjectIdentifier(suppliedTerm.isObjectIdentifier());
                    updateTerm.setSpineAttribute(suppliedTerm.isSpineAttribute());
                    updateTerm.setSpineObject(suppliedTerm.isSpineObject());
                    updateTerm.setAdditionalProperties(suppliedTerm.getAdditionalProperties());
                    updateTerm.setAdditionalProperties(suppliedTerm.getAdditionalProperties());
                    //TODO handle other classifications
                } else
                {
                    // copy over attributes if specified
                    if (suppliedTerm.getName() == null)
                    {
                        updateTerm.setName(originalTerm.getName());
                    } else
                    {
                        updateTerm.setName(suppliedTerm.getName());
                    }
                    if (suppliedTerm.getQualifiedName() == null)
                    {
                        updateTerm.setQualifiedName(originalTerm.getQualifiedName());
                    } else
                    {
                        updateTerm.setQualifiedName(suppliedTerm.getQualifiedName());
                    }
                    if (suppliedTerm.getDescription() == null)
                    {
                        updateTerm.setDescription(originalTerm.getDescription());
                    } else
                    {
                        updateTerm.setDescription(suppliedTerm.getDescription());
                    }
                    if (suppliedTerm.getUsage() == null)
                    {
                        updateTerm.setUsage(originalTerm.getUsage());
                    } else
                    {
                        updateTerm.setUsage(suppliedTerm.getUsage());
                    }
                    if (suppliedTerm.getAbbreviation() == null)
                    {
                        updateTerm.setAbbreviation(originalTerm.getAbbreviation());
                    } else
                    {
                        updateTerm.setAbbreviation(suppliedTerm.getAbbreviation());
                    }
                    if (suppliedTerm.getAdditionalProperties() == null)
                    {
                        updateTerm.setAdditionalProperties(originalTerm.getAdditionalProperties());
                    } else
                    {
                        updateTerm.setAdditionalProperties(suppliedTerm.getAdditionalProperties());
                    }
                    if (suppliedTerm.getExamples() == null)
                    {
                        updateTerm.setExamples(originalTerm.getExamples());
                    } else
                    {
                        updateTerm.setExamples(suppliedTerm.getExamples());
                    }
                    //TODO handle other classifications
                }

                // always update the governance actions for a replace or an update
                GovernanceActions suppliedGovernanceActions = suppliedTerm.getGovernanceActions();
                updateTerm.setGovernanceActions(suppliedGovernanceActions);
                Date termFromTime = suppliedTerm.getEffectiveFromTime();
                Date termToTime = suppliedTerm.getEffectiveToTime();
                updateTerm.setEffectiveFromTime(termFromTime);
                updateTerm.setEffectiveToTime(termToTime);

                EntityDetail updateEntityDetail = termMapper.mapNodeToEntityDetail(updateTerm);
                response = oMRSAPIHelper.callOMRSUpdateEntityProperties(methodName, userId, updateEntityDetail);
                if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail))
                {
                    if (updateEntityDetail.getClassifications() !=null && !updateEntityDetail.getClassifications().isEmpty()) {
                        for (Classification classification : updateEntityDetail.getClassifications()) {
                            response = oMRSAPIHelper.callOMRSClassifyEntity(methodName, userId,guid,classification.getName(),classification.getProperties());
                            if (!response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)){
                                break;
                            }
                        }
                    }
                    /*
                     * The update might involve removing a classification. Check to see if there was a governance actions existing
                     * classification, that we now need to remove because it was null in the requested GovernanceActions
                     */
                    if (updateEntityDetail.getClassifications()!=null && !updateEntityDetail.getClassifications().isEmpty()) {
                        Set<String> currentClassificationNames = updateEntityDetail.getClassifications().stream().map(x -> x.getName()).collect(Collectors.toSet());
                        if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                            if (originalTerm.getGovernanceActions().getRetention() != null && !currentClassificationNames.contains("Retention")) {
                                response = oMRSAPIHelper.callOMRSDeClassifyEntity(methodName, userId, guid, "Retention");
                            }
                        }
                        if (!response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                            if (originalTerm.getGovernanceActions().getCriticality() != null && !currentClassificationNames.contains("Criticality")) {
                                response = oMRSAPIHelper.callOMRSDeClassifyEntity(methodName, userId, guid, "Criticality");
                            }
                        }
                        if (!response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                            if (originalTerm.getGovernanceActions().getConfidence() != null && !currentClassificationNames.contains("Confidence")) {
                                response = oMRSAPIHelper.callOMRSDeClassifyEntity(methodName, userId, guid, "Confidence");
                            }
                        }
                        if (!response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                            if (originalTerm.getGovernanceActions().getConfidentiality() != null && !currentClassificationNames.contains("Confidentility")) {
                                response = oMRSAPIHelper.callOMRSDeClassifyEntity(methodName, userId, guid, "Confidentility");
                            }
                        }
                    }

                    if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                        response = getTermByGuid(userId, guid);
                    }
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
     * Delete a Term instance
     * <p>
     * The deletion of a term is only allowed if there is no term content (i.e. no terms or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the term instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the term will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the term to be deleted.
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the term was not deleted.</li>
     * <li> EntityNotPurgedException             a hard delete was issued but the term was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteTerm(String userId, String guid, Boolean isPurge) {
        final String methodName = "deleteTerm";
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaTermRESTServices termRESTServices = new SubjectAreaTermRESTServices();
        termRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        if (response ==null)
        {
            OMRSRepositoryHelper repositoryHelper = this.oMRSAPIHelper.getOMRSRepositoryHelper();
            try
            {
                InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

                //TODO get source properly
                String source = oMRSAPIHelper.getServiceName();
                String typeDefName = "GlossaryTerm";
                String typeDefGuid = repositoryHelper.getTypeDefByName(source, typeDefName).getGUID();
                if (isPurge)
                {
                    response = oMRSAPIHelper.callOMRSPurgeEntity(methodName, userId, typeDefName, typeDefGuid, guid);
                } else
                {
                    response = oMRSAPIHelper.callOMRSDeleteEntity(methodName, userId, typeDefName, typeDefGuid, guid);
                    if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail))
                    {
                        EntityDetailResponse entityDetailResponse = (EntityDetailResponse)response;
                        EntityDetail entityDetail = entityDetailResponse.getEntityDetail();
                        TermMapper termMapper = new TermMapper(oMRSAPIHelper);
                        Term term = (Term) termMapper.mapEntityDetailToNode(entityDetail);
                        response = new TermResponse(term);
                    }
                }
            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }


    /**
     * Restore a Term
     * <p>
     * Restore allows the deleted Term to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the term to restore
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
    public SubjectAreaOMASAPIResponse restoreTerm(String userId, String guid) {
        final String methodName = "restoreTerm";
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaTermRESTServices termRESTServices = new SubjectAreaTermRESTServices();
        termRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        if (response == null) {
            try {

                InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
                response = this.oMRSAPIHelper.callOMRSRestoreEntity(methodName, userId, guid);
                if (response.getResponseCategory() == ResponseCategory.OmrsEntityDetail) {
                    response = getTermByGuid(userId, guid);
                }
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }


}
