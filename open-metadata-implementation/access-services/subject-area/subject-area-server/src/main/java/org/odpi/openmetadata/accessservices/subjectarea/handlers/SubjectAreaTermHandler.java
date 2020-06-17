/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria term. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse2;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.TermMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.TermAnchorMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * SubjectAreaTermHandler manages Term objects from the property server.  It runs server-side in the subject Area
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
public class SubjectAreaTermHandler extends SubjectAreaHandler {
    private static final String className = SubjectAreaTermHandler.class.getName();

    /**
     * Construct the Subject Area Term Handler
     * needed to operate within a single server instance.
     *
     * @param oMRSAPIHelper           omrs API helper
     */
    public SubjectAreaTermHandler(OMRSAPIHelper oMRSAPIHelper) {
        super(oMRSAPIHelper);
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
    public SubjectAreaOMASAPIResponse2<Term> createTerm(String userId, Term suppliedTerm) {
        final String methodName = "createTerm";
        SubjectAreaOMASAPIResponse2<Term> response = new SubjectAreaOMASAPIResponse2<>();

        String createdTermGuid = null;
        try {
            InputValidator.validateNodeType(className, methodName, suppliedTerm.getNodeType(), NodeType.Term);
            // need to check we have a name
            final String suppliedTermName = suppliedTerm.getName();
            if (suppliedTermName == null || suppliedTermName.equals("")) {
                ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITHOUT_NAME.getMessageDefinition();
                throw new InvalidParameterException(messageDefinition, className, methodName, "Name", null);
            } else {
                TermMapper termMapper = mappersFactory.get(TermMapper.class);
                EntityDetail termEntityDetail = termMapper.map(suppliedTerm);
                GlossarySummary suppliedGlossary = suppliedTerm.getGlossary();

                String glossaryGuid = validateGlossarySummaryDuringCreation(userId, methodName, suppliedGlossary);
                createdTermGuid = oMRSAPIHelper.callOMRSAddEntity(methodName, userId, termEntityDetail);
                if (createdTermGuid != null) {
                    TermAnchor termAnchor = new TermAnchor();
                    termAnchor.setGlossaryGuid(glossaryGuid);
                    termAnchor.setTermGuid(createdTermGuid);
                    TermAnchorMapper termAnchorMapper = mappersFactory.get(TermAnchorMapper.class);
                    Relationship relationship = termAnchorMapper.map(termAnchor);
                    oMRSAPIHelper.callOMRSAddRelationship(methodName, userId, relationship);
                    response = getTermByGuid(userId, createdTermGuid);
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
    public SubjectAreaOMASAPIResponse2<Term> getTermByGuid(String userId, String guid) {
        final String methodName = "getTermByGuid";
        SubjectAreaOMASAPIResponse2<Term> response = new SubjectAreaOMASAPIResponse2<>();

        try {
            Optional<EntityDetail> entityDetail = oMRSAPIHelper.callOMRSGetEntityByGuid(userId, guid, TERM_TYPE_NAME, methodName);
            if (entityDetail.isPresent()) {
                TermMapper termMapper = mappersFactory.get(TermMapper.class);
                Term term = termMapper.map(entityDetail.get());
                setGlossary(userId, term, methodName);
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
    public SubjectAreaOMASAPIResponse2<Term> findTerm(String userId, FindRequest findRequest) {

        final String methodName = "findTerm";
        SubjectAreaOMASAPIResponse2<Term> response = new SubjectAreaOMASAPIResponse2<>();

        // If no search criteria is supplied then we return all terms, this should not be too many
        try {
            List<Term> foundTerms = findEntities(userId, TERM_TYPE_NAME, findRequest, TermMapper.class, methodName);
            if (foundTerms != null) {
                for (Term term : foundTerms) {
                    setGlossary(userId, term, methodName);
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


    private void setGlossary(String userId, Term term, String methodName) throws SubjectAreaCheckedException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException,
                                                                                 InvalidParameterException
    {
        final String guid = term.getSystemAttributes().getGUID();
        List<Relationship> relationships = oMRSAPIHelper.getRelationshipsByType(userId, guid, TERM_TYPE_NAME, TERM_ANCHOR_RELATIONSHIP_NAME, methodName);
        if (CollectionUtils.isNotEmpty(relationships)) {
            for (Relationship relationship : relationships) {
                TermAnchorMapper termAnchorMapper = mappersFactory.get(TermAnchorMapper.class);
                TermAnchor termAnchor = termAnchorMapper.map(relationship);
                GlossarySummary glossarySummary = getGlossarySummary(methodName, userId, termAnchor);
                if (glossarySummary != null) {
                    term.setGlossary(glossarySummary);
                    break;
                }
            }
        }
        // return the Term without a Glossary summary as we have not got one.
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

    public SubjectAreaOMASAPIResponse2<Line> getTermRelationships(String userId, String guid, FindRequest findRequest) {
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
    public SubjectAreaOMASAPIResponse2<Term> updateTerm(String userId, String guid, Term suppliedTerm, boolean isReplace) {
        final String methodName = "updateTerm";
        SubjectAreaOMASAPIResponse2<Term> response = new SubjectAreaOMASAPIResponse2<>();

        try {
            InputValidator.validateNodeType(className, methodName, suppliedTerm.getNodeType(), NodeType.Term, NodeType.Activity);

            response = getTermByGuid(userId, guid);
            if (response.getHead() != null) {
                Term currentTerm = response.getHead();

                Set<String> currentClassificationNames = currentTerm.getClassifications()
                        .stream()
                        .map(x -> x.getClassificationName())
                        .collect(Collectors.toSet());

                if (isReplace)
                    replaceAttributes(currentTerm, suppliedTerm);
                else
                    updateAttributes(currentTerm, suppliedTerm);

                Date termFromTime = suppliedTerm.getEffectiveFromTime();
                Date termToTime = suppliedTerm.getEffectiveToTime();
                currentTerm.setEffectiveFromTime(termFromTime);
                currentTerm.setEffectiveToTime(termToTime);
                // always update the governance actions for a replace or an update
                currentTerm.setGovernanceActions(suppliedTerm.getGovernanceActions());

                TermMapper termMapper = mappersFactory.get(TermMapper.class);
                EntityDetail updateEntityDetail = termMapper.map(currentTerm);
                oMRSAPIHelper.callOMRSUpdateEntity(methodName, userId, updateEntityDetail);

                if (CollectionUtils.isNotEmpty(updateEntityDetail.getClassifications())) {

                    for (Classification classification : updateEntityDetail.getClassifications()) {
                        oMRSAPIHelper.callOMRSClassifyEntity(methodName, userId, guid, classification);
                        currentClassificationNames.remove(classification.getName());
                    }

                    for (String deClassifyName : currentClassificationNames) {
                        oMRSAPIHelper.callOMRSDeClassifyEntity(methodName, userId, guid, deClassifyName);
                    }
                }

                    response = getTermByGuid(userId, guid);
            }

        } catch (SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException e) {
            response.setExceptionInfo(e, className);
        }

        return response;
    }

    private void replaceAttributes(Term currentTerm, Term newTerm) {
        currentTerm.setName(newTerm.getName());
        currentTerm.setQualifiedName(newTerm.getQualifiedName());
        currentTerm.setDescription(newTerm.getDescription());
        currentTerm.setAbbreviation(newTerm.getAbbreviation());
        currentTerm.setExamples(newTerm.getExamples());
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
    public SubjectAreaOMASAPIResponse2<Term> deleteTerm(String userId, String guid, Boolean isPurge) {
        final String methodName = "deleteTerm";
        SubjectAreaOMASAPIResponse2<Term> response = new SubjectAreaOMASAPIResponse2<>();
        try {
            if (isPurge) {
                oMRSAPIHelper.callOMRSPurgeEntity(methodName, userId, TERM_TYPE_NAME, guid);
            } else {
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
    public SubjectAreaOMASAPIResponse2<Term> restoreTerm(String userId, String guid) {
        final String methodName = "restoreTerm";
        SubjectAreaOMASAPIResponse2<Term> response = new SubjectAreaOMASAPIResponse2<>();
        try {
            this.oMRSAPIHelper.callOMRSRestoreEntity(methodName, userId, guid);
            response = getTermByGuid(userId, guid);
        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }
}