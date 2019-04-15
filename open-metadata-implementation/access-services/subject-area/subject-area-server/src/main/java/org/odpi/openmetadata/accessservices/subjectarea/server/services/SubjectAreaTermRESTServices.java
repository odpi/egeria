/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.EntityDetailResponse;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.EntityDetailsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.GlossarySummaryResponse;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.RelationshipsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.GovernanceActions;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermAnchorRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.TermMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.TermAnchorMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.TypeGuids;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.accessservices.subjectarea.validators.RestValidator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


/**
 * The SubjectAreaTermRESTServices provides the server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS) for Terms.  This interface provides term authoring interfaces for subject area experts.
 */

public class SubjectAreaTermRESTServices extends SubjectAreaRESTServicesInstance
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaTermRESTServices.class);

    private static final String className = SubjectAreaTermRESTServices.class.getName();

    public static final Set<String> SUBJECT_AREA_TERM_CLASSIFICATIONS= new HashSet(Arrays.asList(
            // spine objects
            SpineObject.class.getSimpleName(),
            SpineAttribute.class.getSimpleName(),
            ObjectIdentifier.class.getSimpleName(),
            //governance actions
            Confidentiality.class.getSimpleName(),
            Confidence.class.getSimpleName(),
            Criticality.class.getSimpleName(),
            Retention.class.getSimpleName(),
            // dictionary
            AbstractConcept.class.getSimpleName(),
            ActivityDescription.class.getSimpleName(),
            DataValue.class.getSimpleName(),
            // context
            ContextDefinition.class.getSimpleName()));

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

        Glossary associatedGlossary = null;

        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response = initializeAPI(serverName, userId, methodName);
        if (response == null) {
            try {
                // TODO Activity
                InputValidator.validateNodeType(className, methodName, suppliedTerm.getNodeType(), NodeType.Term);
                SubjectAreaGlossaryRESTServices glossaryRESTServices = new SubjectAreaGlossaryRESTServices();
                glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

                // need to check we have a name
                final String suppliedTermName = suppliedTerm.getName();
                if (suppliedTermName == null || suppliedTermName.equals("")) {
                    SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITHOUT_NAME;
                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName);
                    log.error(errorMessage);
                    throw new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
                }
                TermMapper termMapper = new TermMapper(oMRSAPIHelper);
                EntityDetail suppliedTermEntityDetail = termMapper.mapNodeToEntityDetail(suppliedTerm);
                GlossarySummary suppliedGlossary = suppliedTerm.getGlossary();

                SubjectAreaOMASAPIResponse glossaryResponse = RestValidator.validateGlossarySummaryDuringCreation(serverName, userId, methodName, suppliedGlossary, glossaryRESTServices);
                if (glossaryResponse.getResponseCategory().equals(ResponseCategory.Category.Glossary)) {
                    // store the associated glossary
                    associatedGlossary = ((GlossaryResponse) glossaryResponse).getGlossary();
                    response = oMRSAPIHelper.callOMRSAddEntity(userId, suppliedTermEntityDetail);
                    if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                        EntityDetailResponse entityDetailResponse = (EntityDetailResponse) response;
                        EntityDetail createdTermEntityDetail = entityDetailResponse.getEntityDetail();
                        String termGuid = createdTermEntityDetail.getGUID();
                        // Knit the Term to the supplied glossary
                        String glossaryGuid = associatedGlossary.getSystemAttributes().getGUID();
                        TermAnchorRelationship termAnchor = new TermAnchorRelationship();
                        termAnchor.setGlossaryGuid(glossaryGuid);
                        termAnchor.setTermGuid(termGuid);
                        TermAnchorMapper termAnchorMapper = new TermAnchorMapper(oMRSAPIHelper);
                        Relationship relationship = termAnchorMapper.mapLineToRelationship(termAnchor);
                        response = oMRSAPIHelper.callOMRSAddRelationship(userId, relationship);
                        if (response.getResponseCategory().equals(ResponseCategory.OmrsRelationship)) {
                            // the relationship creation was successful so get the Term to return.
                            response = getTermByGuid(serverName, userId, termGuid);
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
        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response = initializeAPI(serverName, userId, methodName);
        if (response == null) {
            try {
                InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
                response = oMRSAPIHelper.callOMRSGetEntityByGuid(userId, guid);
                if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                    EntityDetailResponse entityDetailResponse = (EntityDetailResponse) response;
                    EntityDetail gotEntityDetail = entityDetailResponse.getEntityDetail();
                    TermMapper termMapper = new TermMapper(oMRSAPIHelper);
                    Term gotTerm = (Term) termMapper.mapEntityDetailToNode(gotEntityDetail);
                    String anchorTypeGuid = TypeGuids.getTermAnchorTypeGuid();

                    response = oMRSAPIHelper.callGetRelationshipsForEntity(userId, guid, anchorTypeGuid, 0, null, null, null, 0);
                    if (response.getResponseCategory().equals(ResponseCategory.OmrsRelationships)) {
                        RelationshipsResponse relationshipsResponse = (RelationshipsResponse) response;
                        List<Relationship> glossaryRelationships = relationshipsResponse.getRelationships();
                        if (glossaryRelationships.iterator().hasNext()) {
                            Relationship glossaryRelationship = glossaryRelationships.iterator().next();
                            TermAnchorRelationship termAnchor = (TermAnchorRelationship) new TermAnchorMapper(oMRSAPIHelper).mapRelationshipToLine(glossaryRelationship);
                            response = SubjectAreaUtils.getGlossarySummaryForTerm(userId, oMRSAPIHelper, termAnchor,gotTerm);
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
        return  getRelationshipsFromGuid(serverName, userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
    }
    /**
     * Find Term
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param searchCriteria String expression matching Term property values (this does not include the GlossarySummary content). When not specified, all terms are returned.
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is no limit to the page size
     * @param sequencingOrder the sequencing order for the results.
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
    public  SubjectAreaOMASAPIResponse findTerm(String serverName, String userId,
                                                String searchCriteria,
                                                Date asOfTime,
                                                Integer offset,
                                                Integer pageSize,
                                                org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                String sequencingProperty) {

        final String methodName = "findTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }

        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response = initializeAPI(serverName, userId, methodName);
        if (response == null)
        {
            response = OMRSAPIHelper.findEntitiesByType(oMRSAPIHelper, serverName, userId, "GlossaryTerm", searchCriteria, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty, methodName);
            if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetails))
            {
                EntityDetailsResponse entityDetailsResponse = (EntityDetailsResponse) response;
                List<EntityDetail> entitydetails = entityDetailsResponse.getEntityDetails();
                List<Term> terms = new ArrayList<>();
                for (EntityDetail entityDetail : entitydetails)
                {
                    // call the getTerm so that the GlossarySummary and other parts are populated.
                    response = getTermByGuid(serverName,userId,entityDetail.getGUID());
                    if (response.getResponseCategory() == ResponseCategory.Term) {
                        TermResponse termResponse = (TermResponse)response;
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
     * @param serverName   serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId       userId under which the request is performed
     * @param guid         guid of the term to update
     * @param suppliedTerm term to be updated
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated. The GovernanceAction content is always replaced.
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

        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response = initializeAPI(serverName, userId, methodName);
        if (response ==null) {
            TermMapper termMapper = new TermMapper(oMRSAPIHelper);
            try
            {
                InputValidator.validateNodeType(className, methodName, suppliedTerm.getNodeType(), NodeType.Term);
                InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
                response = getTermByGuid(serverName, userId, guid);
                if (response.getResponseCategory().equals(ResponseCategory.Term))
                {
                    org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term originalTerm = ((TermResponse) response).getTerm();
                    Term updateTerm = new Term();
                    updateTerm.setSystemAttributes(originalTerm.getSystemAttributes());
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
                    response = oMRSAPIHelper.callOMRSUpdateEntityProperties(userId, updateEntityDetail);
                    if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail))
                    {
                        if (updateEntityDetail.getClassifications() !=null && !updateEntityDetail.getClassifications().isEmpty()) {
                            for (Classification classification : updateEntityDetail.getClassifications()) {
                                response = oMRSAPIHelper.callOMRSClassifyEntity(userId,guid,classification.getName(),classification.getProperties());
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
                                    response = oMRSAPIHelper.callOMRSDeClassifyEntity(userId, guid, "Retention");
                                }
                            }
                            if (!response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                                if (originalTerm.getGovernanceActions().getCriticality() != null && !currentClassificationNames.contains("Criticality")) {
                                    response = oMRSAPIHelper.callOMRSDeClassifyEntity(userId, guid, "Criticality");
                                }
                            }
                            if (!response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                                if (originalTerm.getGovernanceActions().getConfidence() != null && !currentClassificationNames.contains("Confidence")) {
                                    response = oMRSAPIHelper.callOMRSDeClassifyEntity(userId, guid, "Confidence");
                                }
                            }
                            if (!response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                                if (originalTerm.getGovernanceActions().getConfidentiality() != null && !currentClassificationNames.contains("Confidentility")) {
                                    response = oMRSAPIHelper.callOMRSDeClassifyEntity(userId, guid, "Confidentility");
                                }
                            }
                        }

                        if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                            response = getTermByGuid(serverName, userId, guid);
                        }
                    }
                }
            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
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


        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response = initializeAPI(serverName, userId, methodName);
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
                    response = oMRSAPIHelper.callOMRSPurgeEntity(userId, typeDefName, typeDefGuid, guid);
                } else
                {
                    response = oMRSAPIHelper.callOMRSDeleteEntity(userId, typeDefName, typeDefGuid, guid);
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
        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response = initializeAPI(serverName, userId, methodName);
        if (response == null) {
            try {

                InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
                response = this.oMRSAPIHelper.callOMRSRestoreEntity(userId, guid);
                if (response.getResponseCategory() == ResponseCategory.OmrsEntityDetail) {
                    response = getTermByGuid(serverName, userId, guid);
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
