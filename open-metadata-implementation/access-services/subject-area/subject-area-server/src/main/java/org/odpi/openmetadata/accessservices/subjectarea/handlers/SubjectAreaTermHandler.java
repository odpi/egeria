/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria term. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.GovernanceClassifications;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.RelationshipType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Categorization;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.CategoryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.TermMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.TermAnchorMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.TermCategorizationMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;

import java.util.*;
import java.util.stream.Collectors;


/**
 * SubjectAreaTermHandler manages Term objects from the property server.  It runs server-side in the subject Area
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
public class SubjectAreaTermHandler extends SubjectAreaHandler {
    private static final String className = SubjectAreaTermHandler.class.getName();
    private final TermAnchorMapper termAnchorMapper;
    private final TermCategorizationMapper termCategorizationMapper;
    private final CategoryMapper categoryMapper;

    /**
     * Construct the Subject Area Term Handler
     * needed to operate within a single server instance.
     *
     * @param oMRSAPIHelper           omrs API helper
     * @param maxPageSize             maximum page size
     */
    public SubjectAreaTermHandler(OMRSAPIHelper oMRSAPIHelper, int maxPageSize) {
        super(oMRSAPIHelper, maxPageSize);
        termAnchorMapper = mappersFactory.get(TermAnchorMapper.class);
        termCategorizationMapper = mappersFactory.get(TermCategorizationMapper.class);
        categoryMapper = mappersFactory.get(CategoryMapper.class);
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
     * @param userId           unique identifier for requesting user, under which the request is performed
     * @param suppliedTerm     Term to create
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
    public SubjectAreaOMASAPIResponse<Term> createTerm(String userId, Term suppliedTerm) {
        final String methodName = "createTerm";
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();

        String createdTermGuid = null;
        try {
            InputValidator.validateNodeType(className, methodName, suppliedTerm.getNodeType(), NodeType.Term);
            // need to check we have a name
            final String suppliedTermName = suppliedTerm.getName();
            if (suppliedTermName == null || suppliedTermName.equals("")) {
                ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITHOUT_NAME.getMessageDefinition();
                throw new InvalidParameterException(messageDefinition, className, methodName, "Name", null);
            } else {
                setUniqueQualifiedNameIfBlank(suppliedTerm);
                TermMapper termMapper = mappersFactory.get(TermMapper.class);
                EntityDetail termEntityDetail = termMapper.map(suppliedTerm);
                GlossarySummary suppliedGlossary = suppliedTerm.getGlossary();
                List<CategorySummary> suppliedCategorysummaries = suppliedTerm.getCategories();

                String glossaryGuid = validateGlossarySummaryDuringCreation(userId, methodName, suppliedGlossary);
                validateCategoriesDuringCreation(userId,methodName,suppliedCategorysummaries);
                InstanceProperties instanceProperties = termEntityDetail.getProperties();
                if (instanceProperties == null ) {
                    instanceProperties = new InstanceProperties();
                }
                if (instanceProperties.getEffectiveFromTime() == null) {
                    instanceProperties.setEffectiveFromTime(new Date());
                    termEntityDetail.setProperties(instanceProperties);
                }
                createdTermGuid = oMRSAPIHelper.callOMRSAddEntity(methodName, userId, termEntityDetail);
                if (createdTermGuid != null) {
                    TermAnchor termAnchor = new TermAnchor();
                    // we expect that the created term has a from time of now or the supplied value.
                    // set the relationship from value to the same
                    termAnchor.setEffectiveFromTime(instanceProperties.getEffectiveFromTime().getTime());
                    if (instanceProperties.getEffectiveToTime() != null) {
                        termAnchor.setEffectiveToTime(instanceProperties.getEffectiveToTime().getTime());
                    }

                    termAnchor.getEnd1().setNodeGuid(glossaryGuid);
                    termAnchor.getEnd2().setNodeGuid(createdTermGuid);

                    org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship relationship = termAnchorMapper.map(termAnchor);
                    oMRSAPIHelper.callOMRSAddRelationship(methodName, userId, relationship);
                    response = getTermByGuid(userId, createdTermGuid);
                    if (response.getRelatedHTTPCode() == 200) {
                        if (suppliedCategorysummaries != null && suppliedCategorysummaries.size() >0) {
                            for (CategorySummary categorySummary : suppliedCategorysummaries) {
                                Categorization categorization = new Categorization();
                                categorization.getEnd1().setNodeGuid(categorySummary.getGuid());
                                categorization.getEnd2().setNodeGuid(createdTermGuid);
                                // we expect that the created term has a from time of now or the supplied value.
                                // set the relationship from value to the same
                                categorization.setEffectiveFromTime(instanceProperties.getEffectiveFromTime().getTime());
                                if (instanceProperties.getEffectiveToTime() != null) {
                                    categorization.setEffectiveToTime(instanceProperties.getEffectiveToTime().getTime());
                                }
                                relationship = termCategorizationMapper.map(categorization);
                                oMRSAPIHelper.callOMRSAddRelationship(methodName, userId, relationship);
                                response = getTermByGuid(userId, createdTermGuid);
                                if (response.getRelatedHTTPCode() != 200) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            //if the entity is created, but subsequently an error occurred while creating the relationship
            if (createdTermGuid != null) {
                deleteTerm(userId, createdTermGuid, false);
                deleteTerm(userId, createdTermGuid, true);
            }
            response.setExceptionInfo(e, className);
        }

        return response;
    }
    /**
     * This method validates that any Categories supplied to a Term create exist.
     *
     * @param userId           userId under which the request is performed
     * @param methodName       method making the call
     * @param suppliedCategories categories to validate.

     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws SubjectAreaCheckedException standard exception Subject Area OMAS services
     */
    protected void validateCategoriesDuringCreation(String userId,
                                                    String methodName,
                                                    List<CategorySummary> suppliedCategories) throws UserNotAuthorizedException,
                                                                                                    PropertyServerException,
                                                                                                    InvalidParameterException,
                                                                                                    SubjectAreaCheckedException
    {
        /*
         * If there are categories supplied then they need to specify a guid that is exists and is for a Category or
         * a child of Category.
         */
        if (suppliedCategories != null) {
            for (CategorySummary categorySummary:suppliedCategories) {
                String guid = categorySummary.getGuid();
                // find by guid
                Optional<EntityDetail> entityDetail = oMRSAPIHelper.callOMRSGetEntityByGuid(userId, guid, CATEGORY_TYPE_NAME, methodName);
                if (!entityDetail.isPresent()) {
                    ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.  TERM_CREATE_WITH_BAD_CATEGORIES.getMessageDefinition();
                    throw new InvalidParameterException(
                            messageDefinition,
                            className,
                            methodName,
                            "categories",
                            null);
                }
            }
        }
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
    public SubjectAreaOMASAPIResponse<Term> getTermByGuid(String userId, String guid) {
        final String methodName = "getTermByGuid";
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();

        try {
            Optional<EntityDetail> entityDetail = oMRSAPIHelper.callOMRSGetEntityByGuid(userId, guid, TERM_TYPE_NAME, methodName);
            if (entityDetail.isPresent()) {
                TermMapper termMapper = mappersFactory.get(TermMapper.class);
                Term term = termMapper.map(entityDetail.get());
                setSummaryObjects(userId, term, methodName);
                response.addResult(term);
            }
        } catch (SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }


    /**
     * Find Term
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param findRequest        {@link FindRequest}
     * @return A list of Terms meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a find was issued but the repository does not implement find functionality in some way.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Term> findTerm(String userId, FindRequest findRequest) {

        final String methodName = "findTerm";
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();

        // If no search criteria is supplied then we return all terms, this should not be too many
        try {
            List<Term> foundTerms = findEntities(userId, TERM_TYPE_NAME, findRequest, TermMapper.class, methodName);
            if (foundTerms != null) {
                for (Term term : foundTerms) {
                    setSummaryObjects(userId, term, methodName);
                    response.addResult(term);
                }
            } else {
                return response;
            }
        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | PropertyServerException | InvalidParameterException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Set the summary objects into the Term. This means if we find a relationship to a Glossary (TermAnchor) or a relationship
     * to a Category (TermCategorization) then represent those relationships are summary objects.
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param term               Term on which to set the summary objects
     * @param methodName         rest API
     * @throws SubjectAreaCheckedException
     * @throws PropertyServerException
     * @throws UserNotAuthorizedException
     * @throws InvalidParameterException
     */
    private void setSummaryObjects(String userId, Term term, String methodName) throws SubjectAreaCheckedException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException,
                                                                                       InvalidParameterException
    {
        final String guid = term.getSystemAttributes().getGUID();
        List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship> termAnchorRelationships = oMRSAPIHelper.getRelationshipsByType(userId, guid, TERM_TYPE_NAME, TERM_ANCHOR_RELATIONSHIP_NAME, methodName);
        if (CollectionUtils.isNotEmpty(termAnchorRelationships)) {
            for (org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship relationship : termAnchorRelationships) {
                TermAnchor termAnchor = termAnchorMapper.map(relationship);
                GlossarySummary glossarySummary = getGlossarySummary(methodName, userId, termAnchor);
                if (glossarySummary != null) {
                    term.setGlossary(glossarySummary);
                }
            }
        }
        List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship> termCategorizationRelationships = oMRSAPIHelper.getRelationshipsByType(userId, guid, TERM_TYPE_NAME, TERM_CATEGORIZATION_RELATIONSHIP_NAME, methodName);
        if (CollectionUtils.isNotEmpty(termCategorizationRelationships)) {
            List<CategorySummary> categorySummaryList = new ArrayList<>();
            for (org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship relationship : termCategorizationRelationships) {
                Categorization categorization = termCategorizationMapper.map(relationship);
                if (categorization !=null) {
                    CategorySummary categorySummary = getCategorySummary(methodName, userId, categorization);
                    if (categorySummary !=null) {
                        categorySummaryList.add(categorySummary);
                    }
                }
            }
            if (categorySummaryList.size() > 0) {
                term.setCategories(categorySummaryList);
            }
        }
    }

    /**
     * Get Term relationships
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param guid               guid
     * @param findRequest        {@link FindRequest}
     * @return the relationships associated with the requested Term guid
     *
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException   Function not supported.</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Relationship> getTermRelationships(String userId, String guid, FindRequest findRequest) {
        String methodName = "getTermRelationships";
        return getAllRelationshipsForEntity(methodName, userId, guid, findRequest);
    }

    /**
     * Update a Term
     * <p>
     * If the caller has chosen to incorporate the term name in their Term Terms or Categories qualified name, renaming the term will cause those
     * qualified names to mismatch the Term name.
     * If the caller has chosen to incorporate the term qualifiedName in their Term Terms or Categories qualified name, changing the qualified name of the term will cause those
     * qualified names to mismatch the Term name.
     * Status is not updated using this call.
     * The Categories categorising this Term can be amended using this call; this means that the termCategorization relationships are removed and/or added in this call.
     * For an update (rather than a replace) with no categories supplied, no changes are made to the termCategorizations; otherwise the
     * supplied categorizing Categories will replace the existing ones.
     *
     * @param userId           unique identifier for requesting user, under which the request is performed
     * @param guid             guid of the term to update
     * @param suppliedTerm     term to be updated
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
    public SubjectAreaOMASAPIResponse<Term> updateTerm(String userId, String guid, Term suppliedTerm, boolean isReplace) {
        final String methodName = "updateTerm";
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();

        try {
            InputValidator.validateNodeType(className, methodName, suppliedTerm.getNodeType(), NodeType.Term, NodeType.Activity);

            response = getTermByGuid(userId, guid);
            if (response.head().isPresent()) {
                Term currentTerm = response.head().get();
                checkReadOnly(methodName, currentTerm, "update");
                Set<String> currentClassificationNames = getCurrentClassificationNames(currentTerm);

                if (isReplace)
                    replaceAttributes(currentTerm, suppliedTerm);
                else
                    updateAttributes(currentTerm, suppliedTerm);

                Long termFromTime = suppliedTerm.getEffectiveFromTime();
                Long termToTime = suppliedTerm.getEffectiveToTime();
                currentTerm.setEffectiveFromTime(termFromTime);
                currentTerm.setEffectiveToTime(termToTime);
                // always update the governance actions for a replace or an update
                currentTerm.setGovernanceClassifications(suppliedTerm.getGovernanceClassifications());

                TermMapper termMapper = mappersFactory.get(TermMapper.class);
                EntityDetail forUpdate = termMapper.map(currentTerm);
                Optional<EntityDetail> updatedEntity = oMRSAPIHelper.callOMRSUpdateEntity(methodName, userId, forUpdate);
                if (updatedEntity.isPresent()) {
                    List<Classification> suppliedClassifications = forUpdate.getClassifications();
                    List<Classification> storedClassifications = updatedEntity.get().getClassifications();
                    Map<String, Classification> storedClassificationMap = null;

                    if ((storedClassifications != null) && (! storedClassifications.isEmpty())) {
                        storedClassificationMap = new HashMap<>();
                        for (Classification storedClassification : storedClassifications) {
                            if (storedClassification != null) {
                                storedClassificationMap.put(storedClassification.getName(), storedClassification);
                            }
                        }
                    }

                    if (CollectionUtils.isNotEmpty(suppliedClassifications)) {
                        for (Classification suppliedClassification : suppliedClassifications) {
                            if (suppliedClassification != null) {
                                if ((storedClassificationMap == null) || (! storedClassificationMap.keySet().contains(suppliedClassification.getName()))) {
                                    oMRSAPIHelper.callOMRSClassifyEntity(methodName, userId, guid, suppliedClassification);
                                } else {
                                    oMRSAPIHelper.callOMRSUpdateClassification(methodName, userId, guid, storedClassificationMap.get(suppliedClassification.getName()), suppliedClassification.getProperties());
                                }
                                currentClassificationNames.remove(suppliedClassification.getName());
                            }
                        }

                        for (String deClassifyName : currentClassificationNames) {
                            oMRSAPIHelper.callOMRSDeClassifyEntity(methodName, userId, guid, deClassifyName);
                        }
                    }
                    List<CategorySummary> suppliedCategories = suppliedTerm.getCategories();
                    if (suppliedCategories==null && !isReplace) {
                        // in the update case with null categories supplied then do not change anything.
                    } else {
                        replaceCategories(userId, guid, suppliedTerm, methodName);
                    }
                }
                response = getTermByGuid(userId, guid);
            }

        } catch (SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            response = new SubjectAreaOMASAPIResponse<>();
            response.setExceptionInfo(e, className);
        }

        return response;
    }

    /**
     * Update the Categories sub-object of Term. Replace the categories with those supplied. This means that the termCategorization relationships are removed and
     * added as per the request.
     * @param userId           unique identifier for requesting user, under which the request is performed
     * @param guid             guid of the term to update
     * @param suppliedTerm     term to be updated
     * @param methodName       API name
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws PropertyServerException              reporting errors when connecting to a metadata repository to retrieve properties about the connection and/or connector.
     * @throws SubjectAreaCheckedException          reporting errors found when using the Subject Area OMAS services.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     */
    private void replaceCategories(String userId, String guid, Term suppliedTerm, String methodName) throws UserNotAuthorizedException, PropertyServerException, SubjectAreaCheckedException, InvalidParameterException {
        Set<String> deleteCategorizationGuidSet = new HashSet<>();
        SubjectAreaOMASAPIResponse<Relationship> relationshipResponse = getTermRelationships(userId, guid, new FindRequest());
        List<Relationship> relationships= relationshipResponse.results();
        /*
         * The supplied categories may not be completely filled out.
         * we will accept a guid (i.e. that of the category) and ignore the rest.
         */
        for (Relationship relationship : relationships) {
            if (relationship.getRelationshipType().equals(RelationshipType.TermCategorization)) {
                deleteCategorizationGuidSet.add(relationship.getGuid());
            }
        }

        // always replace the categories if categories are supplied
        // delete any existing categorizations
        if (deleteCategorizationGuidSet != null && deleteCategorizationGuidSet.size() > 0) {
            for (String categorizationGuidToDelete : deleteCategorizationGuidSet) {
                String typeDefGuid = termCategorizationMapper.getTypeDefGuid();
                oMRSAPIHelper.callOMRSDeleteRelationship(methodName, userId, typeDefGuid, termCategorizationMapper.getTypeName(), categorizationGuidToDelete);
            }
        }
        // add any supplied ones
        List<CategorySummary> suppliedCategories = suppliedTerm.getCategories();
        if (suppliedCategories != null && suppliedCategories.size() > 0) {
            for (CategorySummary categorySummary : suppliedCategories) {
                addCategorizationRelationship(userId, suppliedTerm, methodName, categorySummary.getGuid());
            }
        }
    }

    private void addCategorizationRelationship(String userId, Term suppliedTerm, String methodName, String categoryGuid) throws SubjectAreaCheckedException, PropertyServerException, UserNotAuthorizedException, InvalidParameterException {
        Optional<EntityDetail> entityDetail = oMRSAPIHelper.callOMRSGetEntityByGuid(userId, categoryGuid, categoryMapper.getTypeName(), methodName);
        if (entityDetail.isPresent()) {
            Categorization categorization = new Categorization();
            categorization.getEnd1().setNodeGuid(entityDetail.get().getGUID());
            categorization.getEnd2().setNodeGuid(suppliedTerm.getSystemAttributes().getGUID());
            oMRSAPIHelper.callOMRSAddRelationship(methodName, userId, termCategorizationMapper.map(categorization));
        }
    }

    private Set<String> getCurrentClassificationNames(Term currentTerm) {
        Set<String> currentClassificationNames = currentTerm.getClassifications()
                .stream()
                .map(x -> x.getClassificationName())
                .collect(Collectors.toSet());

        GovernanceClassifications currentActions = currentTerm.getGovernanceClassifications();
        if (currentActions != null) {
            if (currentActions.getConfidence()!=null)
                currentClassificationNames.add(currentActions.getConfidence().getClassificationName());
            if (currentActions.getConfidentiality()!=null)
                currentClassificationNames.add(currentActions.getConfidentiality().getClassificationName());
            if (currentActions.getRetention()!=null)
                currentClassificationNames.add(currentActions.getRetention().getClassificationName());
            if (currentActions.getCriticality()!=null)
                currentClassificationNames.add(currentActions.getCriticality().getClassificationName());
        }
        return currentClassificationNames;
    }

    private void replaceAttributes(Term currentTerm, Term newTerm) {
        currentTerm.setName(newTerm.getName());
        currentTerm.setQualifiedName(newTerm.getQualifiedName());
        currentTerm.setDescription(newTerm.getDescription());
        currentTerm.setAbbreviation(newTerm.getAbbreviation());
        currentTerm.setExamples(newTerm.getExamples());
        currentTerm.setSummary(newTerm.getSummary());
        currentTerm.setUsage(newTerm.getUsage());
        currentTerm.setObjectIdentifier(newTerm.isObjectIdentifier());
        currentTerm.setSpineAttribute(newTerm.isSpineAttribute());
        currentTerm.setSpineObject(newTerm.isSpineObject());
        currentTerm.setAdditionalProperties(newTerm.getAdditionalProperties());
        currentTerm.setClassifications(newTerm.getClassifications());
    }

    private void updateAttributes(Term currentTerm, Term newTerm) {
        if (newTerm.getName() != null) {
            currentTerm.setName(newTerm.getName());
        }
        if (newTerm.getQualifiedName() != null) {
            currentTerm.setQualifiedName(newTerm.getQualifiedName());
        }
        if (newTerm.getDescription() != null) {
            currentTerm.setDescription(newTerm.getDescription());
        }
        if (newTerm.getUsage() != null) {
            currentTerm.setUsage(newTerm.getUsage());
        }
        if (newTerm.getSummary() != null) {
            currentTerm.setSummary(newTerm.getSummary());
        }
        if (newTerm.getAbbreviation() != null) {
            currentTerm.setAbbreviation(newTerm.getAbbreviation());
        }
        if (newTerm.getAdditionalProperties() != null) {
            currentTerm.setAdditionalProperties(newTerm.getAdditionalProperties());
        }
        if (newTerm.getExamples() != null) {
            currentTerm.setExamples(newTerm.getExamples());
        }

        if (newTerm.getClassifications() != null) {
            currentTerm.setClassifications(newTerm.getClassifications());
        }
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
    public SubjectAreaOMASAPIResponse<Term> deleteTerm(String userId, String guid, Boolean isPurge) {
        final String methodName = "deleteTerm";
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
        try {
            if (isPurge) {
                oMRSAPIHelper.callOMRSPurgeEntity(methodName, userId, TERM_TYPE_NAME, guid);
            } else {
                response = getTermByGuid(userId, guid);
                if (response.head().isPresent()) {
                    Term currentTerm = response.head().get();
                    checkReadOnly(methodName, currentTerm, "delete");
                }
                oMRSAPIHelper.callOMRSDeleteEntity(methodName, userId, TERM_TYPE_NAME, guid);
            }
        } catch (SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException e) {
            response.setExceptionInfo(e, className);
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
    public SubjectAreaOMASAPIResponse<Term> restoreTerm(String userId, String guid) {
        final String methodName = "restoreTerm";
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
        try {
            this.oMRSAPIHelper.callOMRSRestoreEntity(methodName, userId, guid);
            response = getTermByGuid(userId, guid);
        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }
    /**
     * Get the Categories categorizing this Term. The server has a maximum page size defined, the number of Categories returned is limited by that maximum page size.
     *
     * @param userId       unique identifier for requesting user, under which the request is performed
     * @param guid         guid of the category to get terms
     * @param categoryHandler  category handler
     * @param startingFrom the starting element number for this set of results.  This is used when retrieving elements
     * @param pageSize     the maximum number of elements that can be returned on this request.
     * @return A list of categories categorizing this Term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Category> getTermCategories(String userId, String guid, SubjectAreaCategoryHandler categoryHandler, Integer startingFrom, Integer pageSize) {
        final String methodName = "getTermCategories";
        SubjectAreaOMASAPIResponse<Category>  response = getRelatedNodesForEnd2(methodName, userId, guid, TERM_CATEGORIZATION_RELATIONSHIP_NAME, CategoryMapper.class, startingFrom, pageSize);
        List<Category> allCategories = new ArrayList<>();
        // the categories we get back from the mappers only map the parts from the entity. They do not set the parentCategory or the anchor.
        if (response.getRelatedHTTPCode() == 200 && response.results() !=null && response.results().size() >0) {
            for (Category mappedCategory: response.results()) {
                SubjectAreaOMASAPIResponse<Category> categoryResponse = categoryHandler.getCategoryByGuid(userId, mappedCategory.getSystemAttributes().getGUID());
                if (categoryResponse.getRelatedHTTPCode() == 200) {
                    allCategories.add(categoryResponse.results().get(0));
                } else {
                    response = categoryResponse;
                    break;
                }
            }
        }
        if (response.getRelatedHTTPCode() == 200) {
            response = new SubjectAreaOMASAPIResponse<>();
            response.addAllResults(allCategories);
        }
        return response;
    }
}