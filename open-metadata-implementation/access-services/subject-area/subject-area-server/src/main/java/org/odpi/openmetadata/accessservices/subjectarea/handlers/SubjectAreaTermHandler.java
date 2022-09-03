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
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;

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
     * @param genericHandler generic handler
     * @param maxPageSize    maximum page size
     */
    public SubjectAreaTermHandler(OpenMetadataAPIGenericHandler genericHandler, int maxPageSize) {
        super(genericHandler, maxPageSize);
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
     * @param userId              unique identifier for requesting user, under which the request is performed
     * @param relationshipHandler relationship handler
     * @param suppliedTerm        Term to create
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
    public SubjectAreaOMASAPIResponse<Term> createTerm(String userId, SubjectAreaRelationshipHandler relationshipHandler, Term suppliedTerm) {
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
                validateCategoriesDuringCreation(userId, methodName, suppliedCategorysummaries);

                Date effectiveFrom = null;
                Date effectiveTo = null;

                if (suppliedTerm.getEffectiveFromTime() != null) {
                    effectiveFrom = new Date(suppliedTerm.getEffectiveFromTime());
                }
                if (suppliedTerm.getEffectiveToTime() != null) {
                    effectiveTo = new Date(suppliedTerm.getEffectiveToTime());
                }

                GlossaryTermBuilder builder = new GlossaryTermBuilder(suppliedTerm.getQualifiedName(),
                                                                      suppliedTerm.getName(),
                                                                      suppliedTerm.getSummary(),
                                                                      suppliedTerm.getDescription(),
                                                                      suppliedTerm.getExamples(),
                                                                      suppliedTerm.getAbbreviation(),
                                                                      suppliedTerm.getUsage(),
                                                                      suppliedTerm.getAdditionalProperties(),
                                                                      suppliedTerm.getExtendedProperties(),
                                                                      null,
                                                                      genericHandler.getRepositoryHelper(),
                                                                      genericHandler.getServiceName(),
                                                                      genericHandler.getServerName());


                builder.setEffectivityDates(effectiveFrom, effectiveTo);
                builder.setAnchors(userId, glossaryGuid, methodName);

                createdTermGuid = genericHandler.createBeanInRepository(userId,
                                                                        null,
                                                                        null,
                                                                        OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_GUID,
                                                                        OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                                                        builder,
                                                                        null,
                                                                        methodName);
                if (createdTermGuid != null) {

                    TermAnchor termAnchor = new TermAnchor();

                    termAnchor.getEnd1().setNodeGuid(glossaryGuid);
                    termAnchor.getEnd2().setNodeGuid(createdTermGuid);

                    relationshipHandler.createRelationship(methodName, userId, TermAnchorMapper.class, termAnchor);
                    response = getTermByGuid(userId, createdTermGuid);
                    if (response.getRelatedHTTPCode() == 200) {
                        if (suppliedCategorysummaries != null && suppliedCategorysummaries.size() > 0) {
                            for (CategorySummary categorySummary : suppliedCategorysummaries) {
                                Categorization categorization = new Categorization();
                                categorization.getEnd1().setNodeGuid(categorySummary.getGuid());
                                categorization.getEnd2().setNodeGuid(createdTermGuid);
                                // we expect that the created term has a from time of now or the supplied value.
                                // set the relationship from value to the same
                                categorization.setEffectiveFromTime(suppliedTerm.getEffectiveFromTime());
                                categorization.setEffectiveToTime(suppliedTerm.getEffectiveToTime());

                                // TODO check error
                                relationshipHandler.createRelationship(methodName, userId, TermCategorizationMapper.class, categorization);

                                if (response.getRelatedHTTPCode() != 200) {
                                    break;
                                }
                            }
                        }
                    }
                    // TODO set classifications
                    EntityDetail entityDetail = termMapper.map(suppliedTerm);
                    List<Classification> classifications =entityDetail.getClassifications();
                    if (classifications != null) {
                        for (Classification classification : classifications) {
                            String classificationTypeName = classification.getName();
                            TypeDef typeDef = genericHandler.getRepositoryHelper().getTypeDefByName(methodName, classificationTypeName);
                            if (typeDef != null) {
                                genericHandler.setClassificationInRepository(userId,
                                                                             null,
                                                                             null,
                                                                             createdTermGuid,
                                                                             "guid",
                                                                             OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                                                             typeDef.getGUID(),
                                                                             classificationTypeName,
                                                                             classification.getProperties(),
                                                                             true,
                                                                             false,
                                                                             false,
                                                                             null,
                                                                             methodName);
                            } else {
                                //TODO Error invalid classification
                            }
                        }
                    }
                    response = getTermByGuid(userId, createdTermGuid);
                }
            }
        } catch (SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            //if the entity is created, but subsequently an error occurred while creating the relationship
            if (createdTermGuid != null) {
                deleteTerm(userId, createdTermGuid);
            }
            response.setExceptionInfo(e, className);
        }

        return response;
    }

    /**
     * This method validates that any Categories supplied to a Term create exist.
     *
     * @param userId             userId under which the request is performed
     * @param methodName         method making the call
     * @param suppliedCategories categories to validate.
     * @throws PropertyServerException     something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException  the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException   one of the parameters is null or invalid
     * @throws SubjectAreaCheckedException standard exception Subject Area OMAS services
     */
    protected void validateCategoriesDuringCreation(String userId,
                                                    String methodName,
                                                    List<CategorySummary> suppliedCategories) throws UserNotAuthorizedException,
                                                                                                     PropertyServerException,
                                                                                                     InvalidParameterException,
                                                                                                     SubjectAreaCheckedException {
        /*
         * If there are categories supplied then they need to specify a guid that is exists and is for a Category or
         * a child of Category.
         */
        if (suppliedCategories != null) {
            for (CategorySummary categorySummary : suppliedCategories) {
                String guid = categorySummary.getGuid();
                // find by guid, expect an exception if the category is not accessible
                genericHandler.getEntityFromRepository(userId,
                                                       guid,
                                                       "guid",
                                                       OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                                       null,
                                                       null,
                                                       false,
                                                       false,
                                                       null,
                                                       methodName);
            }
        }
    }

    /**
     * Get a term by guid.
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
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Term> getTermByGuid(String userId, String guid) {
        final String methodName = "getTermByGuid";
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();

        try {
            EntityDetail entityDetail = genericHandler.getEntityFromRepository(userId,
                                                                               guid,
                                                                               "guid",
                                                                               OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                                                               null,
                                                                               null,
                                                                               false,
                                                                               false,
                                                                               null,
                                                                               methodName);

            TermMapper termMapper = mappersFactory.get(TermMapper.class);
            Term term = termMapper.map(entityDetail);
            setSummaryObjects(userId, term, methodName);
            response.addResult(term);

        } catch (SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }


    /**
     * Find Term
     *
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param findRequest {@link FindRequest}
     * @param exactValue  a boolean, which when set means that only exact matches will be returned, otherwise matches that start with the search criteria will be returned.
     * @param ignoreCase  a boolean, which when set means that case will be ignored, if not set that case will be respected
     * @return A list of Terms meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a find was issued but the repository does not implement find functionality in some way.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Term> findTerm(String userId, FindRequest findRequest, boolean exactValue, boolean ignoreCase) {

        final String methodName = "findTerm";
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();

        // If no search criteria is supplied then we return all terms, this should not be too many
        try {
            List<Term> foundTerms = findNodes(userId, OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME, OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_GUID, findRequest, exactValue, ignoreCase, TermMapper.class, methodName);
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
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param term       Term on which to set the summary objects
     * @param methodName rest API
     * @throws SubjectAreaCheckedException
     * @throws PropertyServerException
     * @throws UserNotAuthorizedException
     * @throws InvalidParameterException
     */
    private void setSummaryObjects(String userId, Term term, String methodName) throws SubjectAreaCheckedException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException,
                                                                                       InvalidParameterException {
        final String guid = term.getSystemAttributes().getGUID();

        List<Relationship> termAnchorRelationships =
                getRelationshipsForEntityByType(methodName,
                                                userId,
                                                guid,
                                                new FindRequest(),
                                                OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                                OpenMetadataAPIMapper.TERM_ANCHOR_TYPE_GUID,
                                                OpenMetadataAPIMapper.TERM_ANCHOR_TYPE_NAME,
                                                OpenMetadataAPIMapper.GLOSSARY_TYPE_NAME);
        if (CollectionUtils.isNotEmpty(termAnchorRelationships)) {
            for (Relationship relationship : termAnchorRelationships) {
                TermAnchor termAnchor = (TermAnchor)relationship;
                GlossarySummary glossarySummary = getGlossarySummary(methodName, userId, termAnchor);
                if (glossarySummary != null) {
                    term.setGlossary(glossarySummary);
                }
            }
        }
        List<Relationship> termCategorizationRelationships =
                getRelationshipsForEntityByType(methodName,
                                                userId,
                                                guid,
                                                new FindRequest(),
                                                OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                                OpenMetadataAPIMapper.TERM_CATEGORIZATION_TYPE_GUID,
                                                OpenMetadataAPIMapper.TERM_CATEGORIZATION_TYPE_NAME,
                                                OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME
                                               );

        if (CollectionUtils.isNotEmpty(termCategorizationRelationships)) {
            List<CategorySummary> categorySummaryList = new ArrayList<>();
            for (Relationship relationship : termCategorizationRelationships) {
                Categorization categorization = (Categorization)relationship;
                if (categorization != null) {
                    CategorySummary categorySummary = getCategorySummary(methodName, userId, categorization);
                    if (categorySummary != null) {
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
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        guid
     * @param findRequest {@link FindRequest}
     * @return the relationships associated with the requested Term guid
     * <p>
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException   Function not supported.</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Relationship> getTermRelationships(String userId, String guid, FindRequest findRequest) {
        String methodName = "getTermRelationships";
        return getAllRelationshipsForEntity(methodName, userId, guid, findRequest, OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME);
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
     * @param userId       unique identifier for requesting user, under which the request is performed
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
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata repository service.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Term> updateTerm(String userId, String guid, Term suppliedTerm, SubjectAreaRelationshipHandler relationshipHandler, boolean isReplace) {
        final String methodName = "updateTerm";
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();

        try {
            InputValidator.validateNodeType(className, methodName, suppliedTerm.getNodeType(), NodeType.Term, NodeType.Activity);

            response = getTermByGuid(userId, guid);
            if (response.head().isPresent()) {
                Term storedTerm = response.head().get();
                TermMapper termMapper = mappersFactory.get(TermMapper.class);

                EntityDetail suppliedEntity = termMapper.map(suppliedTerm);
                EntityDetail storedEntity = termMapper.map(storedTerm);

                Date effectiveFrom = null;

                if (suppliedTerm.getEffectiveFromTime() != null) {
                    effectiveFrom = new Date(suppliedTerm.getEffectiveFromTime());
                }

                genericHandler.updateBeanInRepository(userId,
                                                      null,
                                                      null,
                                                      guid,
                                                      "guid",
                                                      OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_GUID,
                                                      OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                                      false,
                                                      false,
                                                      suppliedEntity.getProperties(),
                                                      !isReplace,
                                                      null,
                                                      methodName);

                // the update properties should not have updated the classifications so we can use
                Set<String> storedClassificationNames = getStoredClassificationNames(storedTerm);

                // always update the governance actions for a replace or an update

                List<Classification> suppliedClassifications = suppliedEntity.getClassifications();
                List<Classification> storedClassifications = storedEntity.getClassifications();
                Map<String, Classification> storedClassificationMap = null;

                if ((storedClassifications != null) && (!storedClassifications.isEmpty())) {
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
                            String classificationTypeName = suppliedClassification.getName();
                            String classificationTypeGUID = null;
                            TypeDef typeDef = genericHandler.getRepositoryHelper().getTypeDefByName(genericHandler.getServiceName(),
                                                                                classificationTypeName);
                            if (typeDef != null) {
                                classificationTypeGUID = typeDef.getGUID();
                            }

                            boolean isMergeUpdate;

                            if ((storedClassificationMap == null) || (!storedClassificationMap.containsKey(classificationTypeName))) {
                                isMergeUpdate = false;
                            } else {
                                isMergeUpdate = true;
                            }

                            genericHandler.setClassificationInRepository(userId,
                                                                         null,
                                                                         null,
                                                                         guid,
                                                                         "guid",
                                                                         OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                                                         classificationTypeGUID,
                                                                         classificationTypeName,
                                                                         suppliedClassification.getProperties(),
                                                                         isMergeUpdate,
                                                                         false,
                                                                         false,
                                                                         null,
                                                                         methodName);

                            storedClassificationNames.remove(suppliedClassification.getName());
                        }
                    }

                    for (String deClassifyName : storedClassificationNames) {
                        String classificationTypeGUID = null;
                        TypeDef typeDef = genericHandler.getRepositoryHelper().getTypeDefByName(genericHandler.getServiceName(),
                                                                                                deClassifyName);
                        if (typeDef != null) {
                            classificationTypeGUID = typeDef.getGUID();
                        }
                        genericHandler.removeClassificationFromRepository(userId,
                                                           null,
                                                           null,
                                                           guid,
                                                           "guid",
                                                           OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                                           classificationTypeGUID,
                                                           deClassifyName,
                                                           false,
                                                           false,
                                                           null,
                                                           methodName);


                    }
                }
                List<CategorySummary> suppliedCategories = suppliedTerm.getCategories();
                if (suppliedCategories == null && !isReplace) {
                    // in the update case with null categories supplied then do not change anything.
                } else {
                    replaceCategories(userId, guid, suppliedTerm, relationshipHandler, methodName);
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
     *
     * @param userId       unique identifier for requesting user, under which the request is performed
     * @param guid         guid of the term to update
     * @param suppliedTerm term to be updated
     * @param relationshipHandler relationship handler
     * @param methodName   API name
     * @throws UserNotAuthorizedException  the requesting user is not authorized to issue this request.
     * @throws PropertyServerException     reporting errors when connecting to a metadata repository to retrieve properties about the connection and/or connector.
     * @throws SubjectAreaCheckedException reporting errors found when using the Subject Area OMAS services.
     * @throws InvalidParameterException   one of the parameters is null or invalid.
     */
    private void replaceCategories(String userId, String guid, Term suppliedTerm, SubjectAreaRelationshipHandler relationshipHandler, String methodName) throws UserNotAuthorizedException, PropertyServerException, SubjectAreaCheckedException, InvalidParameterException {
        Set<String> deleteCategorizationGuidSet = new HashSet<>();
        SubjectAreaOMASAPIResponse<Relationship> relationshipResponse = getTermRelationships(userId, guid, new FindRequest());
        List<Relationship> relationships = relationshipResponse.results();
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
            for (String guidToDelete : deleteCategorizationGuidSet) {
                    relationshipHandler.deleteRelationship(methodName, userId, TermCategorizationMapper.class,guidToDelete);
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

        genericHandler.linkElementToElement(userId,
                                            null,
                                            null,
                                            categoryGuid,
                                            "guid end1",
                                            OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                            suppliedTerm.getSystemAttributes().getGUID(),
                                            "guid end2",
                                            OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                            false,
                                            false,
                                            OpenMetadataAPIMapper.TERM_CATEGORIZATION_TYPE_GUID,
                                            OpenMetadataAPIMapper.TERM_CATEGORIZATION_TYPE_NAME,
                                            (InstanceProperties) null,
                                            null,
                                            null,
                                            null,
                                            methodName);


    }

    private Set<String> getStoredClassificationNames(Term currentTerm) {
        Set<String> currentClassificationNames = currentTerm.getClassifications()
                .stream()
                .map(x -> x.getClassificationName())
                .collect(Collectors.toSet());

        GovernanceClassifications currentActions = currentTerm.getGovernanceClassifications();
        if (currentActions != null) {
            if (currentActions.getConfidence() != null)
                currentClassificationNames.add(currentActions.getConfidence().getClassificationName());
            if (currentActions.getConfidentiality() != null)
                currentClassificationNames.add(currentActions.getConfidentiality().getClassificationName());
            if (currentActions.getRetention() != null)
                currentClassificationNames.add(currentActions.getRetention().getClassificationName());
            if (currentActions.getCriticality() != null)
                currentClassificationNames.add(currentActions.getCriticality().getClassificationName());
        }
        return currentClassificationNames;
    }


    /**
     * Delete a Term instance
     * <p>
     * The deletion of a term is only allowed if there is no term content (i.e. no terms or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional.
     * <p>
     * A soft delete means that the term instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the term will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the term to be deleted.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the term was not deleted.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Term> deleteTerm(String userId, String guid) {
        final String methodName = "deleteTerm";
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
        try {
            genericHandler.deleteBeanInRepository(userId,
                                                  null,
                                                  null,
                                                  guid,
                                                  "guid",
                                                  OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_GUID,
                                                  OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                                  null,
                                                  null,
                                                  false,
                                                  false,
                                                  null,
                                                  methodName);


        } catch (PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Restore a Term
     * <p>
     * Restore allows the deleted Term to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the term to restore
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
            genericHandler.getRepositoryHandler().restoreEntity(userId,
                                                                null,
                                                                null,
                                                                guid,
                                                                methodName);
            response = getTermByGuid(userId, guid);
        } catch (UserNotAuthorizedException |  PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Get the Categories categorizing this Term. The server has a maximum page size defined, the number of Categories returned is limited by that maximum page size.
     *
     * @param userId          unique identifier for requesting user, under which the request is performed
     * @param guid            guid of the category to get terms
     * @param categoryHandler category handler
     * @param startingFrom    the starting element number for this set of results.  This is used when retrieving elements
     * @param pageSize        the maximum number of elements that can be returned on this request.
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
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();

        if (pageSize == null) {
            pageSize = maxPageSize;
        }
        if (startingFrom == null) {
            startingFrom = 0;
        }
        SubjectAreaOMASAPIResponse<Term> thisTermResponse = getTermByGuid(userId, guid);
        if (thisTermResponse.getRelatedHTTPCode() == 200) {
            try {
                Set<String> specificMatchPropertyNames = new HashSet();

                // specify the names of string attributes for this type that we want to match against
                specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);
                specificMatchPropertyNames.add(OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME);
                specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);

                List<EntityDetail> entities = genericHandler.getAttachedFilteredEntities(userId,
                                                                                         guid,
                                                                                         "guid",
                                                                                         OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                                                                         OpenMetadataAPIMapper.TERM_CATEGORIZATION_TYPE_NAME,
                                                                                         OpenMetadataAPIMapper.TERM_CATEGORIZATION_TYPE_GUID,
                                                                                         1,      // get the categories
                                                                                         specificMatchPropertyNames,
                                                                                         "", // no search criteria
                                                                                         startingFrom,
                                                                                         false,
                                                                                         false,
                                                                                         pageSize,
                                                                                         null, // any date
                                                                                         methodName);
                if (entities != null) {
                    Set<Category> categories = new HashSet<>();
                    for (EntityDetail entity : entities) {
                        SubjectAreaOMASAPIResponse<Category> categoryResponse = categoryHandler.getCategoryByGuid(userId, entity.getGUID());
                        if (categoryResponse.getRelatedHTTPCode() == 200) {
                            categories.add(categoryResponse.results().get(0));
                        } else {
                            response = categoryResponse;
                            break;
                        }
                    }
                    if (response.getRelatedHTTPCode() == 200) {
                        response.addAllResults(categories);
                    }
                }

            } catch (PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
                response.setExceptionInfo(e, className);
            }
        }

        return response;
    }
}