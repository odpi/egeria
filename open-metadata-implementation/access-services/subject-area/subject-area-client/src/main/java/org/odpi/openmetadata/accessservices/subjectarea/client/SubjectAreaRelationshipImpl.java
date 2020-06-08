/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.DetectUtils;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * SubjectAreaImpl is the OMAS client library implementation of the Subject Area OMAS.
 * This interface provides relationship authoring interface for subject area experts.
 */
public class SubjectAreaRelationshipImpl extends SubjectAreaBaseImpl implements SubjectAreaRelationship {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaRelationshipImpl.class);

    private static final String className = SubjectAreaRelationshipImpl.class.getName();
    private static final String BASE_RELATIONSHIPS_URL = SubjectAreaImpl.SUBJECT_AREA_BASE_URL + "relationships";
    private static final String HASA = "/has-as";
    private static final String RELATED_TERM = "/related-terms";
    private static final String SYNONYM = "/synonyms";
    private static final String ANTONYM = "/antonyms";
    private static final String TRANSLATION = "/translations";
    private static final String USED_IN_CONTEXT = "/used-in-contexts";
    private static final String PREFERRED_TERM = "/preferred-terms";
    private static final String VALID_VALUE = "/valid-values";
    private static final String REPLACEMENT_TERM = "/replacement-terms";
    private static final String TYPED_BY = "/typed-bys";
    private static final String IS_A = "/is-as";
    private static final String IS_A_TYPE_OF = "/is-a-type-ofs";
    private static final String TERM_CATEGORIZATION = "/term-categorizations";
    private static final String SEMANTIC_ASSIGNMENT = "/semantic-assignments";
    private static final String TERM_ANCHOR = "/term-anchor";
    private static final String CATEGORY_ANCHOR = "/category-anchor";
    private static final String PROJECT_SCOPE = "/project-scopes";

    // urls to use when creating types of relationships
    private static final String BASE_RELATIONSHIPS_HASA_URL = BASE_RELATIONSHIPS_URL + HASA;
    private static final String BASE_RELATIONSHIPS_RELATEDTERM_URL = BASE_RELATIONSHIPS_URL + RELATED_TERM;
    private static final String BASE_RELATIONSHIPS_SYNONYM_URL = BASE_RELATIONSHIPS_URL + SYNONYM;
    private static final String BASE_RELATIONSHIPS_ANTONYM_URL = BASE_RELATIONSHIPS_URL + ANTONYM;
    private static final String BASE_RELATIONSHIPS_TRANSLATION_URL = BASE_RELATIONSHIPS_URL + TRANSLATION;
    private static final String BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL = BASE_RELATIONSHIPS_URL + USED_IN_CONTEXT;
    private static final String BASE_RELATIONSHIPS_PREFERRED_TERM_URL = BASE_RELATIONSHIPS_URL + PREFERRED_TERM;
    private static final String BASE_RELATIONSHIPS_VALID_VALUE_URL = BASE_RELATIONSHIPS_URL + VALID_VALUE;
    private static final String BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL = BASE_RELATIONSHIPS_URL + REPLACEMENT_TERM;
    private static final String BASE_RELATIONSHIPS_TYPED_BY_URL = BASE_RELATIONSHIPS_URL + TYPED_BY;
    private static final String BASE_RELATIONSHIPS_IS_A_URL = BASE_RELATIONSHIPS_URL + IS_A;
    private static final String BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL = BASE_RELATIONSHIPS_URL + IS_A_TYPE_OF;
    private static final String BASE_RELATIONSHIPS_TERM_CATEGORIZATION_URL = BASE_RELATIONSHIPS_URL + TERM_CATEGORIZATION;
    private static final String BASE_RELATIONSHIPS_SEMANTIC_ASSIGNMENT_URL = BASE_RELATIONSHIPS_URL + SEMANTIC_ASSIGNMENT;
    private static final String BASE_RELATIONSHIPS_TERM_ANCHOR_URL = BASE_RELATIONSHIPS_URL + TERM_ANCHOR;
    private static final String BASE_RELATIONSHIPS_CATEGORY_ANCHOR_URL = BASE_RELATIONSHIPS_URL + CATEGORY_ANCHOR;
    private static final String BASE_RELATIONSHIPS_PROJECT_SCOPE_URL = BASE_RELATIONSHIPS_URL + PROJECT_SCOPE;

    /**
     * Constructor for no authentication.
     *
     * @param serverName            name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException there is a problem creating the client-side components to issue any
     *                                                                                    REST API calls.
     */
    public SubjectAreaRelationshipImpl(String serverName, String serverPlatformURLRoot) throws
                                                                                        org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
    }

    @Override
    public Hasa createTermHASARelationship(String userId, Hasa hasa) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            MetadataServerUncontactableException,
                                                                            UnexpectedResponseException,
                                                                            UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException,
                                                                            PropertyServerException,
                                                                            FunctionNotSupportedException {
        final String methodName = "createTermHASARelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, BASE_RELATIONSHIPS_HASA_URL, hasa);
        Hasa createdTermHASARelationship = DetectUtils.detectAndReturnTermHASARelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdTermHASARelationship;

    }

    @Override
    public Hasa getTermHASARelationship(String userId, String guid) throws InvalidParameterException,
                                                                           MetadataServerUncontactableException,
                                                                           UserNotAuthorizedException,
                                                                           UnexpectedResponseException,
                                                                           UnrecognizedGUIDException,
                                                                           PropertyServerException,
                                                                           FunctionNotSupportedException {
        final String methodName = "getTermHASARelationship";
        SubjectAreaOMASAPIResponse response = getRelationship(userId, guid, methodName, BASE_RELATIONSHIPS_HASA_URL);
        Hasa gotTermHASARelationship = DetectUtils.detectAndReturnTermHASARelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermHASARelationship;
    }

    @Override
    public Hasa updateTermHASARelationship(String userId, Hasa hasa) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateTermHASARelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, hasa.getGuid(), BASE_RELATIONSHIPS_HASA_URL, hasa, false);
        Hasa updatedTermHASARelationship = DetectUtils.detectAndReturnTermHASARelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTermHASARelationship;
    }

    @Override
    public Hasa replaceTermHASARelationship(String userId, Hasa hasa) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             MetadataServerUncontactableException,
                                                                             UnexpectedResponseException,
                                                                             UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "replaceTermHASARelationship";
        SubjectAreaOMASAPIResponse response = updateRelationship(userId, hasa.getGuid(), BASE_RELATIONSHIPS_HASA_URL, hasa, true);
        Hasa updatedTermHASARelationship = DetectUtils.detectAndReturnTermHASARelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTermHASARelationship;
    }
    @Override
    public Hasa deleteTermHASARelationship(String userId, String guid) throws
                                                                       InvalidParameterException,
                                                                       MetadataServerUncontactableException,
                                                                       UserNotAuthorizedException,
                                                                       UnrecognizedGUIDException,
                                                                       FunctionNotSupportedException,
                                                                       RelationshipNotDeletedException,
                                                                       UnexpectedResponseException,
                                                                       PropertyServerException {
        final String methodName = "deleteTermHASARelationship";
        final String urlTemplate = BASE_RELATIONSHIPS_HASA_URL;
        SubjectAreaOMASAPIResponse response = deleteRelationship(userId, guid, methodName, urlTemplate);
        Hasa hasa = DetectUtils.detectAndReturnTermHASARelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return hasa;
    }


    @Override
    public void purgeTermHASARelationship(String userId, String guid) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             UnrecognizedGUIDException,
                                                                             MetadataServerUncontactableException,
                                                                             UnexpectedResponseException,
                                                                             RelationshipNotPurgedException,
                                                                             FunctionNotSupportedException,
                                                                             PropertyServerException {
        final String methodName = "purgeTermHASARelationship";
        final String urlTemplate = BASE_RELATIONSHIPS_HASA_URL;
        purgeRelationship(userId, guid, methodName, urlTemplate);
    }

    @Override
    public Hasa restoreTermHASARelationship(String userId, String guid) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               MetadataServerUncontactableException,
                                                                               UnexpectedResponseException,
                                                                               UnrecognizedGUIDException,
                                                                               PropertyServerException,
                                                                               FunctionNotSupportedException {
        final String methodName = "restoreTermHASARelationship";
        final String urlTemplate = BASE_RELATIONSHIPS_HASA_URL;
        SubjectAreaOMASAPIResponse response = restoreRelationship(userId, guid, methodName, urlTemplate);
        Hasa hasa = DetectUtils.detectAndReturnTermHASARelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return hasa;
    }

    @Override

    public RelatedTerm createRelatedTerm(String userId, RelatedTerm relatedTermRelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "createTermRelatedARelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, BASE_RELATIONSHIPS_RELATEDTERM_URL, relatedTermRelationship);
        RelatedTerm createdRelatedTerm = DetectUtils.detectAndReturnRelatedTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdRelatedTerm;
    }
    @Override
    public RelatedTerm getRelatedTerm(String userId, String guid) throws InvalidParameterException,
                                                                         MetadataServerUncontactableException,
                                                                         UserNotAuthorizedException,
                                                                         UnexpectedResponseException,
                                                                         UnrecognizedGUIDException,
                                                                         PropertyServerException,
                                                                         FunctionNotSupportedException {
        final String methodName = "getRelatedTerm";
        SubjectAreaOMASAPIResponse response = getRelationship(userId, guid, methodName, BASE_RELATIONSHIPS_RELATEDTERM_URL);
        RelatedTerm gotRelatedTerm = DetectUtils.detectAndReturnRelatedTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotRelatedTerm;
    }

    @Override
    public RelatedTerm updateRelatedTerm(String userId, RelatedTerm termRelatedTerm) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateRelatedTerm";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, termRelatedTerm.getGuid(), BASE_RELATIONSHIPS_RELATEDTERM_URL, termRelatedTerm, false);
        RelatedTerm relatedTermRelationship = DetectUtils.detectAndReturnRelatedTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return relatedTermRelationship;
    }

    @Override
    public RelatedTerm replaceRelatedTerm(String userId, RelatedTerm termRelatedTerm) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "replaceRelatedTerm";
        SubjectAreaOMASAPIResponse response = updateRelationship(userId, termRelatedTerm.getGuid(), BASE_RELATIONSHIPS_RELATEDTERM_URL, termRelatedTerm, true);
        RelatedTerm relatedTermRelationship = DetectUtils.detectAndReturnRelatedTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return relatedTermRelationship;
    }

    @Override
    public RelatedTerm restoreRelatedTerm(String userId, String guid) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             MetadataServerUncontactableException,
                                                                             UnexpectedResponseException,
                                                                             UnrecognizedGUIDException,
                                                                             PropertyServerException,
                                                                             FunctionNotSupportedException {
        final String methodName = "restoreRelatedTerm";
        String url = BASE_RELATIONSHIPS_RELATEDTERM_URL;
        SubjectAreaOMASAPIResponse response = restoreRelationship(userId, guid, methodName, url);
        RelatedTerm gotRelatedTerm = DetectUtils.detectAndReturnRelatedTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotRelatedTerm;
    }
    @Override
    public RelatedTerm deleteRelatedTerm(String userId, String guid) throws
                                                                     InvalidParameterException,
                                                                     MetadataServerUncontactableException,
                                                                     UserNotAuthorizedException,
                                                                     UnrecognizedGUIDException,
                                                                     FunctionNotSupportedException,
                                                                     RelationshipNotDeletedException,
                                                                     UnexpectedResponseException,
                                                                     PropertyServerException {
        final String methodName = "deleteRelatedTerm";
        final String url = BASE_RELATIONSHIPS_RELATEDTERM_URL;
        SubjectAreaOMASAPIResponse response = deleteRelationship(userId, guid, methodName, url);
        RelatedTerm gotRelatedTerm = DetectUtils.detectAndReturnRelatedTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotRelatedTerm;
    }

    @Override
    public void purgeRelatedTerm(String userId, String guid) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    UnrecognizedGUIDException,
                                                                    MetadataServerUncontactableException,
                                                                    UnexpectedResponseException,
                                                                    RelationshipNotPurgedException,
                                                                    PropertyServerException,
                                                                    FunctionNotSupportedException {
        final String methodName = "purgeRelatedTerm";
        final String url = BASE_RELATIONSHIPS_RELATEDTERM_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    @Override
    public Synonym createSynonymRelationship(String userId, Synonym synonym) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "createSynonymRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, BASE_RELATIONSHIPS_SYNONYM_URL, synonym);
        Synonym createdSynonym = DetectUtils.detectAndReturnSynonym(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdSynonym;
    }
    @Override
    public Synonym getSynonymRelationship(String userId, String guid) throws InvalidParameterException,
                                                                             MetadataServerUncontactableException,
                                                                             UserNotAuthorizedException,
                                                                             UnexpectedResponseException,
                                                                             UnrecognizedGUIDException,
                                                                             PropertyServerException,
                                                                             FunctionNotSupportedException {
        final String methodName = "getSynonymRelationship";
        SubjectAreaOMASAPIResponse response = getRelationship(userId, guid, methodName, BASE_RELATIONSHIPS_SYNONYM_URL);
        Synonym gotSynonym = DetectUtils.detectAndReturnSynonym(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotSynonym;
    }

    @Override
    public Synonym updateSynonymRelationship(String userId, Synonym synonymRelationship) throws InvalidParameterException,
                                                                                                MetadataServerUncontactableException,
                                                                                                UserNotAuthorizedException,
                                                                                                UnexpectedResponseException,
                                                                                                UnrecognizedGUIDException,
                                                                                                PropertyServerException,
                                                                                                FunctionNotSupportedException {
        final String methodName = "updateSynonymRelationship";


        SubjectAreaOMASAPIResponse response = updateRelationship(userId, synonymRelationship.getGuid(), BASE_RELATIONSHIPS_SYNONYM_URL, synonymRelationship, false);
        Synonym updatedSynonymRelationship = DetectUtils.detectAndReturnSynonym(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedSynonymRelationship;
    }

    @Override
    public Synonym replaceSynonymRelationship(String userId, Synonym synonymRelationship) throws InvalidParameterException,
                                                                                                 MetadataServerUncontactableException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 UnexpectedResponseException,
                                                                                                 UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateSynonymRelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, synonymRelationship.getGuid(), BASE_RELATIONSHIPS_SYNONYM_URL, synonymRelationship, true);
        Synonym updatedSynonymRelationship = DetectUtils.detectAndReturnSynonym(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedSynonymRelationship;
    }

    @Override
    public Synonym deleteSynonymRelationship(String userId, String guid) throws
                                                                         InvalidParameterException,
                                                                         MetadataServerUncontactableException,
                                                                         UnrecognizedGUIDException,
                                                                         UserNotAuthorizedException,
                                                                         FunctionNotSupportedException,
                                                                         RelationshipNotDeletedException,
                                                                         UnexpectedResponseException,
                                                                         PropertyServerException {
        final String methodName = "deleteSynonymRelationship";
        String url = BASE_RELATIONSHIPS_SYNONYM_URL;
        SubjectAreaOMASAPIResponse response = deleteRelationship(userId, guid, methodName, url);
        Synonym gotSynonym = DetectUtils.detectAndReturnSynonym(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotSynonym;
    }

    @Override
    public void purgeSynonymRelationship(String userId, String guid) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            UnrecognizedGUIDException,
                                                                            MetadataServerUncontactableException,
                                                                            UnexpectedResponseException,
                                                                            RelationshipNotPurgedException,
                                                                            PropertyServerException,
                                                                            FunctionNotSupportedException {
        final String methodName = "purgeSynonymRelationship";
        String url = BASE_RELATIONSHIPS_SYNONYM_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }
    @Override
    public Synonym restoreSynonymRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UnexpectedResponseException,
                                                                                 UnrecognizedGUIDException,
                                                                                 PropertyServerException,
                                                                                 FunctionNotSupportedException {
        final String methodName = "restoreSynonymRelationship";
        final String url = BASE_RELATIONSHIPS_SYNONYM_URL;
        SubjectAreaOMASAPIResponse response = restoreRelationship(userId, guid, methodName, url);
        Synonym gotSynonym = DetectUtils.detectAndReturnSynonym(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotSynonym;
    }

    @Override
    public Antonym createAntonymRelationship(String userId, Antonym antonym) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "createAntonymRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, BASE_RELATIONSHIPS_ANTONYM_URL, antonym);
        Antonym createdAntonym = DetectUtils.detectAndReturnAntonym(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdAntonym;
    }

    @Override
    public Antonym getAntonymRelationship(String userId, String guid) throws InvalidParameterException,
                                                                             MetadataServerUncontactableException,
                                                                             UserNotAuthorizedException,
                                                                             UnexpectedResponseException,
                                                                             UnrecognizedGUIDException,
                                                                             PropertyServerException,
                                                                             FunctionNotSupportedException {
        final String methodName = "getAntonymRelationship";
        SubjectAreaOMASAPIResponse response = getRelationship(userId, guid, methodName, BASE_RELATIONSHIPS_ANTONYM_URL);
        Antonym gotAntonym = DetectUtils.detectAndReturnAntonym(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotAntonym;
    }

    @Override
    public Antonym updateAntonymRelationship(String userId, Antonym antonymRelationship) throws InvalidParameterException,
                                                                                                MetadataServerUncontactableException,
                                                                                                UserNotAuthorizedException,
                                                                                                UnexpectedResponseException,
                                                                                                UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateAntonymRelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, antonymRelationship.getGuid(), BASE_RELATIONSHIPS_ANTONYM_URL, antonymRelationship, false);
        Antonym updatedAntonymRelationship = DetectUtils.detectAndReturnAntonym(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedAntonymRelationship;
    }

    @Override
    public Antonym replaceAntonymRelationship(String userId, Antonym antonymRelationship) throws InvalidParameterException,
                                                                                                 MetadataServerUncontactableException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 UnexpectedResponseException,
                                                                                                 UnrecognizedGUIDException,
                                                                                                 PropertyServerException,
                                                                                                 FunctionNotSupportedException {
        final String methodName = "updateAntonymRelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, antonymRelationship.getGuid(), BASE_RELATIONSHIPS_ANTONYM_URL, antonymRelationship, true);
        Antonym updatedAntonymRelationship = DetectUtils.detectAndReturnAntonym(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedAntonymRelationship;
    }
    @Override
    public Antonym deleteAntonymRelationship(String userId, String guid) throws
                                                                         InvalidParameterException,
                                                                         MetadataServerUncontactableException,
                                                                         UserNotAuthorizedException,
                                                                         UnrecognizedGUIDException,
                                                                         FunctionNotSupportedException,
                                                                         RelationshipNotDeletedException,
                                                                         UnexpectedResponseException,
                                                                         PropertyServerException {
        final String methodName = "deleteAntonymRelationship";
        final String url = BASE_RELATIONSHIPS_ANTONYM_URL;
        SubjectAreaOMASAPIResponse response = deleteRelationship(userId, guid, methodName, url);
        Antonym gotAntonym = DetectUtils.detectAndReturnAntonym(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotAntonym;
    }
    @Override
    public void purgeAntonymRelationship(String userId, String guid) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            UnrecognizedGUIDException,
                                                                            MetadataServerUncontactableException,
                                                                            UnexpectedResponseException,
                                                                            RelationshipNotPurgedException,
                                                                            PropertyServerException,
                                                                            FunctionNotSupportedException {
        final String methodName = "purgeAntonymRelationship";
        final String url = BASE_RELATIONSHIPS_ANTONYM_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }
    @Override
    public Antonym restoreAntonymRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UnexpectedResponseException,
                                                                                 UnrecognizedGUIDException,
                                                                                 PropertyServerException,
                                                                                 FunctionNotSupportedException {
        final String methodName = "restoreAntonymRelationship";
        final String url = BASE_RELATIONSHIPS_ANTONYM_URL;
        SubjectAreaOMASAPIResponse response = restoreRelationship(userId, guid, methodName, url);
        Antonym gotAntonym = DetectUtils.detectAndReturnAntonym(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotAntonym;
    }
    @Override
    public Translation createTranslationRelationship(String userId, Translation translation) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    MetadataServerUncontactableException,
                                                                                                    UnexpectedResponseException,
                                                                                                    UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "createTranslationRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, BASE_RELATIONSHIPS_TRANSLATION_URL, translation);

        Translation createdTranslation = DetectUtils.detectAndReturnTranslation(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdTranslation;
    }

    @Override
    public Translation getTranslationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                     MetadataServerUncontactableException,
                                                                                     UserNotAuthorizedException,
                                                                                     UnexpectedResponseException,
                                                                                     UnrecognizedGUIDException,
                                                                                     PropertyServerException,
                                                                                     FunctionNotSupportedException {
        final String methodName = "getTranslationRelationship";
        SubjectAreaOMASAPIResponse response = getRelationship(userId, guid, methodName, BASE_RELATIONSHIPS_TRANSLATION_URL);
        Translation gotTranslation = DetectUtils.detectAndReturnTranslation(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTranslation;
    }
    @Override
    public Translation updateTranslationRelationship(String userId, Translation translationRelationship) throws InvalidParameterException,
                                                                                                                MetadataServerUncontactableException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                UnexpectedResponseException,
                                                                                                                UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateTranslationRelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, translationRelationship.getGuid(), BASE_RELATIONSHIPS_TRANSLATION_URL, translationRelationship, false);
        Translation updatedTranslationRelationship = DetectUtils.detectAndReturnTranslation(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTranslationRelationship;
    }
    @Override
    public Translation replaceTranslationRelationship(String userId, Translation translationRelationship) throws InvalidParameterException,
                                                                                                                 MetadataServerUncontactableException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 UnexpectedResponseException,
                                                                                                                 UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateTranslationRelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, translationRelationship.getGuid(), BASE_RELATIONSHIPS_TRANSLATION_URL, translationRelationship, true);
        Translation updatedTranslationRelationship = DetectUtils.detectAndReturnTranslation(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTranslationRelationship;
    }
    @Override
    public Translation deleteTranslationRelationship(String userId, String guid) throws
                                                                                 InvalidParameterException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UserNotAuthorizedException,
                                                                                 UnrecognizedGUIDException,
                                                                                 FunctionNotSupportedException,
                                                                                 RelationshipNotDeletedException,
                                                                                 UnexpectedResponseException, PropertyServerException {
        final String methodName = "deleteTranslationRelationship";
        final String url = BASE_RELATIONSHIPS_TRANSLATION_URL;
        SubjectAreaOMASAPIResponse response = deleteRelationship(userId, guid, methodName, url);
        Translation gotTranslation = DetectUtils.detectAndReturnTranslation(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTranslation;
    }
    @Override
    public void purgeTranslationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                RelationshipNotPurgedException,
                                                                                UnrecognizedGUIDException,
                                                                                MetadataServerUncontactableException,
                                                                                UnexpectedResponseException,
                                                                                PropertyServerException,
                                                                                FunctionNotSupportedException {
        final String methodName = "purgeTranslationRelationship";
        final String url = BASE_RELATIONSHIPS_TRANSLATION_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }
    @Override
    public Translation restoreTranslationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         MetadataServerUncontactableException,
                                                                                         UnexpectedResponseException,
                                                                                         UnrecognizedGUIDException,
                                                                                         PropertyServerException,
                                                                                         FunctionNotSupportedException {
        final String methodName = "restoreTranslationRelationship";
        final String url = BASE_RELATIONSHIPS_TRANSLATION_URL;
        SubjectAreaOMASAPIResponse response = restoreRelationship(userId, guid, methodName, url);
        Translation gotTranslation = DetectUtils.detectAndReturnTranslation(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTranslation;
    }

    @Override
    public UsedInContext createUsedInContextRelationship(String userId, UsedInContext usedInContext) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            MetadataServerUncontactableException,
                                                                                                            UnexpectedResponseException,
                                                                                                            UnrecognizedGUIDException,
                                                                                                            PropertyServerException,
                                                                                                            FunctionNotSupportedException {
        final String methodName = "createUsedInContextRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL, usedInContext);
        UsedInContext createdUsedInContext = DetectUtils.detectAndReturnUsedInContext(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdUsedInContext;
    }
    @Override
    public UsedInContext getUsedInContextRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                         MetadataServerUncontactableException,
                                                                                         UserNotAuthorizedException,
                                                                                         UnexpectedResponseException,
                                                                                         UnrecognizedGUIDException,
                                                                                         PropertyServerException, FunctionNotSupportedException {
        final String methodName = "getUsedInContextRelationship";
        SubjectAreaOMASAPIResponse response = getRelationship(userId, guid, methodName, BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL);
        UsedInContext gotUsedInContext = DetectUtils.detectAndReturnUsedInContext(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotUsedInContext;
    }
    @Override
    public UsedInContext updateUsedInContextRelationship(String userId, UsedInContext usedInContextRelationship) throws InvalidParameterException,
                                                                                                                        MetadataServerUncontactableException,
                                                                                                                        UserNotAuthorizedException,
                                                                                                                        UnexpectedResponseException,
                                                                                                                        UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateUsedInContextRelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, usedInContextRelationship.getGuid(), BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL, usedInContextRelationship, false);
        UsedInContext updatedUsedInContextRelationship = DetectUtils.detectAndReturnUsedInContext(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedUsedInContextRelationship;
    }
    @Override
    public UsedInContext replaceUsedInContextRelationship(String userId, UsedInContext usedInContextRelationship) throws InvalidParameterException,
                                                                                                                         MetadataServerUncontactableException,
                                                                                                                         UserNotAuthorizedException,
                                                                                                                         UnexpectedResponseException,
                                                                                                                         UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateUsedInContextRelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, usedInContextRelationship.getGuid(), BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL, usedInContextRelationship, true);
        UsedInContext updatedUsedInContextRelationship = DetectUtils.detectAndReturnUsedInContext(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedUsedInContextRelationship;
    }
    @Override
    public UsedInContext deleteUsedInContextRelationship(String userId, String guid) throws
                                                                                     InvalidParameterException,
                                                                                     MetadataServerUncontactableException,
                                                                                     UserNotAuthorizedException,
                                                                                     UnrecognizedGUIDException,
                                                                                     FunctionNotSupportedException,
                                                                                     RelationshipNotDeletedException,
                                                                                     UnexpectedResponseException,
                                                                                     PropertyServerException {
        final String methodName = "deleteUsedInContextRelationship";
        final String url = BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL;
        SubjectAreaOMASAPIResponse response = deleteRelationship(userId, guid, methodName, url);
        UsedInContext gotUsedInContext = DetectUtils.detectAndReturnUsedInContext(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotUsedInContext;
    }

    @Override
    public void purgeUsedInContextRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  RelationshipNotPurgedException,
                                                                                  UnrecognizedGUIDException,
                                                                                  MetadataServerUncontactableException,
                                                                                  UnexpectedResponseException,
                                                                                  PropertyServerException,
                                                                                  FunctionNotSupportedException {
        final String methodName = "purgeUsedInContextRelationship";
        final String url = BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }
    @Override
    public UsedInContext restoreUsedInContextRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             MetadataServerUncontactableException,
                                                                                             UnexpectedResponseException,
                                                                                             UnrecognizedGUIDException,
                                                                                             PropertyServerException,
                                                                                             FunctionNotSupportedException {
        final String methodName = "restoreUsedInContextRelationship";
        final String url = BASE_RELATIONSHIPS_USED_IN_CONTEXT_URL;
        SubjectAreaOMASAPIResponse response = restoreRelationship(userId, guid, methodName, url);
        UsedInContext gotUsedInContext = DetectUtils.detectAndReturnUsedInContext(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotUsedInContext;
    }
    @Override
    public PreferredTerm createPreferredTermRelationship(String userId, PreferredTerm preferredTerm) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "createPreferredTermRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, BASE_RELATIONSHIPS_PREFERRED_TERM_URL, preferredTerm);
        PreferredTerm createdPreferredTerm = DetectUtils.detectAndReturnPreferredTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdPreferredTerm;
    }
    @Override
    public PreferredTerm getPreferredTermRelationship(String userId, String guid) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, UnexpectedResponseException, UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "getPreferredTermRelationship";
        SubjectAreaOMASAPIResponse response = getRelationship(userId, guid, methodName, BASE_RELATIONSHIPS_PREFERRED_TERM_URL);
        PreferredTerm gotPreferredTerm = DetectUtils.detectAndReturnPreferredTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotPreferredTerm;
    }
    @Override
    public PreferredTerm updatePreferredTermRelationship(String userId, PreferredTerm preferredTermRelationship) throws InvalidParameterException,
                                                                                                                        MetadataServerUncontactableException,
                                                                                                                        UserNotAuthorizedException,
                                                                                                                        UnexpectedResponseException,
                                                                                                                        UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updatePreferredTermRelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, preferredTermRelationship.getGuid(), BASE_RELATIONSHIPS_PREFERRED_TERM_URL, preferredTermRelationship, false);
        PreferredTerm updatedPreferredTermRelationship = DetectUtils.detectAndReturnPreferredTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedPreferredTermRelationship;
    }

    @Override
    public PreferredTerm replacePreferredTermRelationship(String userId, PreferredTerm preferredTermRelationship) throws InvalidParameterException,
                                                                                                                         MetadataServerUncontactableException,
                                                                                                                         UserNotAuthorizedException,
                                                                                                                         UnexpectedResponseException,
                                                                                                                         UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updatePreferredTermRelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, preferredTermRelationship.getGuid(), BASE_RELATIONSHIPS_PREFERRED_TERM_URL, preferredTermRelationship, true);
        PreferredTerm updatedPreferredTermRelationship = DetectUtils.detectAndReturnPreferredTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedPreferredTermRelationship;
    }
    @Override
    public PreferredTerm deletePreferredTermRelationship(String userId, String guid) throws
                                                                                     InvalidParameterException,
                                                                                     MetadataServerUncontactableException,
                                                                                     UserNotAuthorizedException,
                                                                                     UnrecognizedGUIDException,
                                                                                     FunctionNotSupportedException,
                                                                                     RelationshipNotDeletedException,
                                                                                     UnexpectedResponseException,
                                                                                     PropertyServerException {
        final String methodName = "deletePreferredTermRelationship";
        final String url = BASE_RELATIONSHIPS_PREFERRED_TERM_URL;
        SubjectAreaOMASAPIResponse response = deleteRelationship(userId, guid, methodName, url);
        PreferredTerm gotPreferredTerm = DetectUtils.detectAndReturnPreferredTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotPreferredTerm;
    }
    @Override
    public void purgePreferredTermRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  RelationshipNotPurgedException,
                                                                                  UnrecognizedGUIDException,
                                                                                  MetadataServerUncontactableException,
                                                                                  UnexpectedResponseException,
                                                                                  PropertyServerException,
                                                                                  FunctionNotSupportedException {
        final String methodName = "purgePreferredTermRelationship";
        final String url = BASE_RELATIONSHIPS_PREFERRED_TERM_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }
    @Override
    public PreferredTerm restorePreferredTermRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             MetadataServerUncontactableException,
                                                                                             UnexpectedResponseException,
                                                                                             UnrecognizedGUIDException,
                                                                                             PropertyServerException,
                                                                                             FunctionNotSupportedException {
        final String methodName = "restorePreferredTermRelationship";
        final String url = BASE_RELATIONSHIPS_PREFERRED_TERM_URL;
        SubjectAreaOMASAPIResponse response = restoreRelationship(userId, guid, methodName, url);
        PreferredTerm gotPreferredTerm = DetectUtils.detectAndReturnPreferredTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotPreferredTerm;
    }

    @Override
    public ValidValue createValidValueRelationship(String userId, ValidValue validValue) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                MetadataServerUncontactableException,
                                                                                                UnexpectedResponseException,
                                                                                                UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "createValidValueRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, BASE_RELATIONSHIPS_VALID_VALUE_URL, validValue);

        ValidValue createdValidValue = DetectUtils.detectAndReturnValidValue(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdValidValue;
    }
    @Override
    public ValidValue getValidValueRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                   MetadataServerUncontactableException,
                                                                                   UserNotAuthorizedException,
                                                                                   UnexpectedResponseException,
                                                                                   UnrecognizedGUIDException,
                                                                                   PropertyServerException,
                                                                                   FunctionNotSupportedException {
        final String methodName = "getValidValueRelationship";
        SubjectAreaOMASAPIResponse response = getRelationship(userId, guid, methodName, BASE_RELATIONSHIPS_VALID_VALUE_URL);
        ValidValue gotValidValue = DetectUtils.detectAndReturnValidValue(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotValidValue;
    }
    @Override
    public ValidValue updateValidValueRelationship(String userId, ValidValue validValueRelationship) throws InvalidParameterException,
                                                                                                            MetadataServerUncontactableException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            UnexpectedResponseException,
                                                                                                            UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateValidValueRelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, validValueRelationship.getGuid(), BASE_RELATIONSHIPS_VALID_VALUE_URL, validValueRelationship, false);
        ValidValue updatedValidValueRelationship = DetectUtils.detectAndReturnValidValue(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedValidValueRelationship;
    }
    @Override
    public ValidValue replaceValidValueRelationship(String userId, ValidValue validValueRelationship) throws InvalidParameterException,
                                                                                                             MetadataServerUncontactableException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             UnexpectedResponseException,
                                                                                                             UnrecognizedGUIDException,
                                                                                                             PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateValidValueRelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, validValueRelationship.getGuid(), BASE_RELATIONSHIPS_VALID_VALUE_URL, validValueRelationship, true);
        ValidValue updatedValidValueRelationship = DetectUtils.detectAndReturnValidValue(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedValidValueRelationship;
    }

    @Override
    public ValidValue deleteValidValueRelationship(String userId, String guid) throws
                                                                               InvalidParameterException,
                                                                               MetadataServerUncontactableException,
                                                                               UserNotAuthorizedException,
                                                                               UnrecognizedGUIDException,
                                                                               FunctionNotSupportedException,
                                                                               RelationshipNotDeletedException,
                                                                               UnexpectedResponseException,
                                                                               PropertyServerException {
        final String methodName = "deleteValidValueRelationship";
        final String url = BASE_RELATIONSHIPS_VALID_VALUE_URL;
        SubjectAreaOMASAPIResponse response = deleteRelationship(userId, guid, methodName, url);
        ValidValue gotValidValue = DetectUtils.detectAndReturnValidValue(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotValidValue;
    }
    @Override
    public void purgeValidValueRelationship(String userId, String guid) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               RelationshipNotPurgedException,
                                                                               UnrecognizedGUIDException,
                                                                               MetadataServerUncontactableException,
                                                                               UnexpectedResponseException,
                                                                               PropertyServerException,
                                                                               FunctionNotSupportedException {
        final String methodName = "purgeValidValueRelationship";
        final String url = BASE_RELATIONSHIPS_VALID_VALUE_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }
    @Override
    public ValidValue restoreValidValueRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       MetadataServerUncontactableException,
                                                                                       UnexpectedResponseException,
                                                                                       UnrecognizedGUIDException,
                                                                                       PropertyServerException,
                                                                                       FunctionNotSupportedException {
        final String methodName = "restoreValidValueRelationship";
        final String url = BASE_RELATIONSHIPS_VALID_VALUE_URL;
        SubjectAreaOMASAPIResponse response = restoreRelationship(userId, guid, methodName, url);
        ValidValue gotValidValue = DetectUtils.detectAndReturnValidValue(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotValidValue;
    }
    @Override
    public ReplacementTerm createReplacementTermRelationship(String userId, ReplacementTerm replacementTerm) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "createReplacementTermRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL, replacementTerm);
        ReplacementTerm createdReplacementTerm = DetectUtils.detectAndReturnReplacementTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdReplacementTerm;
    }

    @Override
    public ReplacementTerm getReplacementTermRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                             MetadataServerUncontactableException,
                                                                                             UserNotAuthorizedException,
                                                                                             UnexpectedResponseException,
                                                                                             UnrecognizedGUIDException,
                                                                                             PropertyServerException,
                                                                                             FunctionNotSupportedException {
        final String methodName = "getReplacementTermRelationship";
        SubjectAreaOMASAPIResponse response = getRelationship(userId, guid, methodName, BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL);
        ReplacementTerm gotReplacementTerm = DetectUtils.detectAndReturnReplacementTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotReplacementTerm;
    }
    @Override
    public ReplacementTerm updateReplacementTermRelationship(String userId, ReplacementTerm replacementTermRelationship) throws InvalidParameterException,
                                                                                                                                MetadataServerUncontactableException,
                                                                                                                                UserNotAuthorizedException,
                                                                                                                                UnexpectedResponseException,
                                                                                                                                UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateReplacementTermRelationship";


        SubjectAreaOMASAPIResponse response = updateRelationship(userId, replacementTermRelationship.getGuid(), BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL, replacementTermRelationship, false);
        ReplacementTerm updatedReplacementTermRelationship = DetectUtils.detectAndReturnReplacementTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedReplacementTermRelationship;
    }
    @Override
    public ReplacementTerm replaceReplacementTermRelationship(String userId, ReplacementTerm replacementTermRelationship) throws InvalidParameterException,
                                                                                                                                 MetadataServerUncontactableException,
                                                                                                                                 UserNotAuthorizedException,
                                                                                                                                 UnexpectedResponseException,
                                                                                                                                 UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateReplacementTermRelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, replacementTermRelationship.getGuid(), BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL, replacementTermRelationship, true);
        ReplacementTerm updatedReplacementTermRelationship = DetectUtils.detectAndReturnReplacementTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedReplacementTermRelationship;
    }
    @Override
    public ReplacementTerm deleteReplacementTermRelationship(String userId, String guid) throws
                                                                                         InvalidParameterException,
                                                                                         MetadataServerUncontactableException,
                                                                                         UserNotAuthorizedException,
                                                                                         UnrecognizedGUIDException,
                                                                                         FunctionNotSupportedException,
                                                                                         RelationshipNotDeletedException,
                                                                                         UnexpectedResponseException, PropertyServerException {
        final String methodName = "deleteReplacementTermRelationship";
        final String url = BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL;
        SubjectAreaOMASAPIResponse response = deleteRelationship(userId, guid, methodName, url);
        ReplacementTerm gotReplacementTerm = DetectUtils.detectAndReturnReplacementTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotReplacementTerm;
    }
    @Override
    public void purgeReplacementTermRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    RelationshipNotPurgedException,
                                                                                    UnrecognizedGUIDException,
                                                                                    MetadataServerUncontactableException,
                                                                                    UnexpectedResponseException,
                                                                                    PropertyServerException,
                                                                                    FunctionNotSupportedException{
        final String methodName = "purgeReplacementTermRelationship";
        final String url = BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }
    @Override

    public ReplacementTerm restoreReplacementTermRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 MetadataServerUncontactableException,
                                                                                                 UnexpectedResponseException,
                                                                                                 UnrecognizedGUIDException,
                                                                                                 PropertyServerException,
                                                                                                 FunctionNotSupportedException {
        final String methodName = "restoreReplacementTermRelationship";
        final String url = BASE_RELATIONSHIPS_REPLACEMENT_TERM_URL;
        SubjectAreaOMASAPIResponse response = restoreRelationship(userId, guid, methodName, url);
        ReplacementTerm gotReplacementTerm = DetectUtils.detectAndReturnReplacementTerm(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotReplacementTerm;
    }
    @Override
    public TypedBy createTermTYPEDBYRelationship(String userId, TypedBy termTYPEDBYRelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "createTermTYPEDBYRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, BASE_RELATIONSHIPS_TYPED_BY_URL, termTYPEDBYRelationship);

        TypedBy createdTermTYPEDBYRelationship = DetectUtils.detectAndReturnTermTYPEDBYRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdTermTYPEDBYRelationship;
    }

    @Override
    public TypedBy getTermTYPEDBYRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UserNotAuthorizedException,
                                                                                 UnexpectedResponseException,
                                                                                 UnrecognizedGUIDException,
                                                                                 PropertyServerException,
                                                                                 FunctionNotSupportedException {
        final String methodName = "getTermTYPEDBYRelationship";
        SubjectAreaOMASAPIResponse response = getRelationship(userId, guid, methodName, BASE_RELATIONSHIPS_TYPED_BY_URL);
        TypedBy gotTermTYPEDBYRelationship = DetectUtils.detectAndReturnTermTYPEDBYRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermTYPEDBYRelationship;
    }

    @Override
    public TypedBy updateTermTYPEDBYRelationship(String userId, TypedBy termTYPEDBYRelationship) throws InvalidParameterException,
                                                                                                        MetadataServerUncontactableException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        UnexpectedResponseException,
                                                                                                        UnrecognizedGUIDException,
                                                                                                        PropertyServerException,
                                                                                                        FunctionNotSupportedException {
        final String methodName = "updateTermTYPEDBYRelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, termTYPEDBYRelationship.getGuid(), BASE_RELATIONSHIPS_TYPED_BY_URL, termTYPEDBYRelationship, false);
        TypedBy updatedTermTYPEDBYRelationship = DetectUtils.detectAndReturnTermTYPEDBYRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTermTYPEDBYRelationship;
    }

    @Override
    public TypedBy replaceTermTYPEDBYRelationship(String userId, TypedBy termTYPEDBYRelationship) throws InvalidParameterException,
                                                                                                         MetadataServerUncontactableException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         UnexpectedResponseException,
                                                                                                         UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateTermTYPEDBYRelationship";


        SubjectAreaOMASAPIResponse response = updateRelationship(userId, termTYPEDBYRelationship.getGuid(), BASE_RELATIONSHIPS_TYPED_BY_URL, termTYPEDBYRelationship, true);
        TypedBy updatedTermTYPEDBYRelationship = DetectUtils.detectAndReturnTermTYPEDBYRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTermTYPEDBYRelationship;
    }

    @Override
    public TypedBy deleteTermTYPEDBYRelationship(String userId, String guid) throws
                                                                             InvalidParameterException,
                                                                             MetadataServerUncontactableException,
                                                                             UserNotAuthorizedException,
                                                                             UnrecognizedGUIDException,
                                                                             FunctionNotSupportedException,
                                                                             RelationshipNotDeletedException,
                                                                             UnexpectedResponseException,
                                                                             PropertyServerException {
        final String methodName = "deleteTermTYPEDBYRelationship";
        final String url = BASE_RELATIONSHIPS_TYPED_BY_URL;
        SubjectAreaOMASAPIResponse response = deleteRelationship(userId, guid, methodName, url);
        TypedBy gotTermTYPEDBYRelationship = DetectUtils.detectAndReturnTermTYPEDBYRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermTYPEDBYRelationship;
    }

    @Override
    public void purgeTermTYPEDBYRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                RelationshipNotPurgedException,
                                                                                UnrecognizedGUIDException,
                                                                                MetadataServerUncontactableException,
                                                                                UnexpectedResponseException,
                                                                                PropertyServerException,
                                                                                FunctionNotSupportedException {
        final String methodName = "purgeTermTYPEDBYRelationship";
        final String url = BASE_RELATIONSHIPS_TYPED_BY_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    @Override
    public TypedBy restoreTypedByRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UnexpectedResponseException,
                                                                                 UnrecognizedGUIDException,
                                                                                 PropertyServerException,
                                                                                 FunctionNotSupportedException {
        final String methodName = "restoreTermTYPEDBYRelationship";
        final String url = BASE_RELATIONSHIPS_TYPED_BY_URL;
        SubjectAreaOMASAPIResponse response = restoreRelationship(userId, guid, methodName, url);
        TypedBy gotTermTYPEDBYRelationship = DetectUtils.detectAndReturnTermTYPEDBYRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermTYPEDBYRelationship;
    }

    @Override
    public Isa createIsaRelationship(String userId, Isa isa) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "createIsaRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, BASE_RELATIONSHIPS_IS_A_URL, isa);
        Isa createdIsa = DetectUtils.detectAndReturnISARelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdIsa;
    }

    @Override
    public Isa getIsaRelationship(String userId, String guid) throws InvalidParameterException,
                                                                     MetadataServerUncontactableException,
                                                                     UserNotAuthorizedException,
                                                                     UnexpectedResponseException,
                                                                     UnrecognizedGUIDException,
                                                                     PropertyServerException,
                                                                     FunctionNotSupportedException {
        final String methodName = "getIsaRelationship";
        SubjectAreaOMASAPIResponse response = getRelationship(userId, guid, methodName, BASE_RELATIONSHIPS_IS_A_URL);
        Isa gotIsa = DetectUtils.detectAndReturnISARelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotIsa;
    }

    @Override
    public Isa updateIsaRelationship(String userId, Isa isaRelationship) throws InvalidParameterException,
                                                                                MetadataServerUncontactableException,
                                                                                UserNotAuthorizedException,
                                                                                UnexpectedResponseException,
                                                                                UnrecognizedGUIDException,
                                                                                PropertyServerException,
                                                                                FunctionNotSupportedException {
        final String methodName = "updateIsaRelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, isaRelationship.getGuid(), BASE_RELATIONSHIPS_IS_A_URL, isaRelationship, false);
        Isa updatedIsaRelationship = DetectUtils.detectAndReturnISARelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedIsaRelationship;
    }

    @Override
    public Isa replaceIsaRelationship(String userId, Isa isaRelationship) throws InvalidParameterException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UserNotAuthorizedException,
                                                                                 UnexpectedResponseException,
                                                                                 UnrecognizedGUIDException,
                                                                                 PropertyServerException,
                                                                                 FunctionNotSupportedException {
        final String methodName = "updateIsaRelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, isaRelationship.getGuid(), BASE_RELATIONSHIPS_IS_A_URL, isaRelationship, true);
        Isa updatedIsaRelationship = DetectUtils.detectAndReturnISARelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedIsaRelationship;
    }

    @Override
    public Isa deleteIsaRelationship(String userId, String guid) throws
                                                                 InvalidParameterException,
                                                                 MetadataServerUncontactableException,
                                                                 UserNotAuthorizedException,
                                                                 UnrecognizedGUIDException,
                                                                 FunctionNotSupportedException,
                                                                 RelationshipNotDeletedException,
                                                                 UnexpectedResponseException, PropertyServerException {
        final String methodName = "deleteIsaRelationship";
        final String url = BASE_RELATIONSHIPS_IS_A_URL;
        SubjectAreaOMASAPIResponse response = deleteRelationship(userId, guid, methodName, url);
        Isa gotIsa = DetectUtils.detectAndReturnISARelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotIsa;
    }


    @Override
    public void purgeIsaRelationship(String userId, String guid) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        RelationshipNotPurgedException,
                                                                        UnrecognizedGUIDException,
                                                                        MetadataServerUncontactableException,
                                                                        UnexpectedResponseException,
                                                                        PropertyServerException,
                                                                        FunctionNotSupportedException {
        final String methodName = "purgeIsaRelationship";
        final String url = BASE_RELATIONSHIPS_IS_A_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    @Override
    public Isa restoreIsaRelationship(String userId, String guid) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         MetadataServerUncontactableException,
                                                                         UnexpectedResponseException,
                                                                         UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "restoreIsaRelationship";
        final String url = BASE_RELATIONSHIPS_IS_A_URL;
        SubjectAreaOMASAPIResponse response = restoreRelationship(userId, guid, methodName, url);
        Isa gotIsa = DetectUtils.detectAndReturnISARelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotIsa;
    }

    @Override
    public IsaTypeOf createTermISATypeOFRelationship(String userId, IsaTypeOf isaTypeOf) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "createTermISATypeOFRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL, isaTypeOf);
        IsaTypeOf createdTermISATypeOFRelationship = DetectUtils.detectAndReturnTermISATypeOFRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdTermISATypeOFRelationship;
    }

    @Override
    public IsaTypeOf getTermISATypeOFRelationship(String userId, String guid) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, UnexpectedResponseException, UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "getTermISATypeOFRelationship";
        SubjectAreaOMASAPIResponse response = getRelationship(userId, guid, methodName, BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL);
        IsaTypeOf gotTermISATypeOFRelationship = DetectUtils.detectAndReturnTermISATypeOFRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermISATypeOFRelationship;
    }
    @Override
    public IsaTypeOf updateTermISATypeOFRelationship(String userId, IsaTypeOf isATypeOf) throws InvalidParameterException,
                                                                                                MetadataServerUncontactableException,
                                                                                                UserNotAuthorizedException,
                                                                                                UnexpectedResponseException,
                                                                                                UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateTermISATypeOFRelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, isATypeOf.getGuid(), BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL, isATypeOf, false);
        IsaTypeOf updatedTermISATypeOFRelationship = DetectUtils.detectAndReturnTermISATypeOFRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTermISATypeOFRelationship;
    }

    @Override
    public IsaTypeOf replaceTermISATypeOFRelationship(String userId, IsaTypeOf isATypeOf) throws InvalidParameterException,
                                                                                                 MetadataServerUncontactableException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 UnexpectedResponseException,
                                                                                                 UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateTermISATypeOFRelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, isATypeOf.getGuid(), BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL, isATypeOf, true);
        IsaTypeOf updatedTermISATypeOFRelationship = DetectUtils.detectAndReturnTermISATypeOFRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTermISATypeOFRelationship;
    }
    @Override
    public IsaTypeOf deleteTermISATypeOFRelationship(String userId, String guid) throws
                                                                                 InvalidParameterException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UserNotAuthorizedException,
                                                                                 UnrecognizedGUIDException,
                                                                                 FunctionNotSupportedException,
                                                                                 RelationshipNotDeletedException,
                                                                                 UnexpectedResponseException,
                                                                                 PropertyServerException {
        final String methodName = "deleteTermISATypeOFRelationship";
        final String url = BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL;
        SubjectAreaOMASAPIResponse response = deleteRelationship(userId, guid, methodName, url);
        IsaTypeOf gotTermISATypeOFRelationship = DetectUtils.detectAndReturnTermISATypeOFRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermISATypeOFRelationship;
    }
    @Override
    public void purgeTermISATypeOFRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  RelationshipNotPurgedException,
                                                                                  UnrecognizedGUIDException,
                                                                                  MetadataServerUncontactableException,
                                                                                  UnexpectedResponseException,
                                                                                  PropertyServerException,
                                                                                  FunctionNotSupportedException {
        final String methodName = "purgeTermISATypeOFRelationship";
        final String url = BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    @Override

    public IsaTypeOf restoreIsaTypeOfRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     MetadataServerUncontactableException,
                                                                                     UnexpectedResponseException,
                                                                                     UnrecognizedGUIDException,
                                                                                     PropertyServerException,
                                                                                     FunctionNotSupportedException {
        final String methodName = "restoreTermISATypeOFRelationship";
        final String url = BASE_RELATIONSHIPS_IS_A_TYPE_OF_URL;
        SubjectAreaOMASAPIResponse response = restoreRelationship(userId, guid, methodName, url);
        IsaTypeOf gotTermISATypeOFRelationship = DetectUtils.detectAndReturnTermISATypeOFRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermISATypeOFRelationship;
    }

    private SubjectAreaOMASAPIResponse getRelationship(String userId, String guid, String methodName, String base_url) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        final String urlTemplate = base_url + "/%s";

        SubjectAreaOMASAPIResponse response = getByIdRESTCall(userId, guid, methodName, urlTemplate);
        return response;
    }

    private SubjectAreaOMASAPIResponse deleteRelationship(String userId, String guid, String methodName, String base_url)
    throws InvalidParameterException,
           MetadataServerUncontactableException,
           UserNotAuthorizedException,
           FunctionNotSupportedException,
           RelationshipNotDeletedException,
           UnrecognizedGUIDException,
           PropertyServerException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = base_url + "/%s?isPurge=false";
        return deleteRelationshipRESTCall(userId, guid, methodName, urlTemplate);

    }

    private SubjectAreaOMASAPIResponse restoreRelationship(String userId, String guid, String methodName, String base_url) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = base_url + "/%s";
        return restoreRESTCall(userId, guid, methodName, urlTemplate);

    }

    private void purgeRelationship(String userId, String guid, String methodName, String base_url) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, RelationshipNotPurgedException, UnexpectedResponseException, UnrecognizedGUIDException,  FunctionNotSupportedException, PropertyServerException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = base_url + "/%s?isPurge=true";
        SubjectAreaOMASAPIResponse response = purgeRelationshipRESTCall(userId, guid, methodName, urlTemplate);
        DetectUtils.detectVoid(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    @Override
    public Categorization createTermCategorizationRelationship(String userId, Categorization categorization) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    MetadataServerUncontactableException,
                                                                                                                    UnexpectedResponseException,
                                                                                                                    UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {

        final String methodName = "createTermCategorizationRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, BASE_RELATIONSHIPS_TERM_CATEGORIZATION_URL, categorization);

        Categorization createdTermCategorizationRelationship = DetectUtils.detectAndReturnTermCategorizationRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdTermCategorizationRelationship;

    }

    @Override
    public Categorization getTermCategorizationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                               MetadataServerUncontactableException,
                                                                                               UserNotAuthorizedException,
                                                                                               UnexpectedResponseException,
                                                                                               UnrecognizedGUIDException,
                                                                                               PropertyServerException,
                                                                                               FunctionNotSupportedException {
        final String methodName = "getTermCategorizationRelationship";
        SubjectAreaOMASAPIResponse response = getRelationship(userId, guid, methodName, BASE_RELATIONSHIPS_TERM_CATEGORIZATION_URL);
        Categorization gotTermCategorizationRelationship = DetectUtils.detectAndReturnTermCategorizationRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermCategorizationRelationship;
    }

    @Override
    public Categorization updateTermCategorizationRelationship(String userId, Categorization termCategorizationRelationship) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateTermCategorizationRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = updateRelationship(userId, termCategorizationRelationship.getGuid(), BASE_RELATIONSHIPS_TERM_CATEGORIZATION_URL, termCategorizationRelationship, false);
        Categorization updatedTermCategorizationRelationship = DetectUtils.detectAndReturnTermCategorizationRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTermCategorizationRelationship;
    }

    @Override
    public Categorization replaceTermCategorizationRelationship(String userId, Categorization termCategorizationRelationship) throws InvalidParameterException,
                                                                                                                                     UserNotAuthorizedException,
                                                                                                                                     MetadataServerUncontactableException,
                                                                                                                                     UnexpectedResponseException,
                                                                                                                                     UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "replaceTermCategorizationRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, termCategorizationRelationship.getGuid(), BASE_RELATIONSHIPS_TERM_CATEGORIZATION_URL, termCategorizationRelationship, true);
        Categorization updatedTermCategorizationRelationship = DetectUtils.detectAndReturnTermCategorizationRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTermCategorizationRelationship;
    }

    @Override
    public Categorization deleteTermCategorizationRelationship(String userId, String guid) throws
                                                                                           InvalidParameterException,
                                                                                           MetadataServerUncontactableException,
                                                                                           UserNotAuthorizedException,
                                                                                           UnrecognizedGUIDException,
                                                                                           FunctionNotSupportedException,
                                                                                           RelationshipNotDeletedException,
                                                                                           UnexpectedResponseException,
                                                                                           PropertyServerException {
        final String methodName = "deleteTermCategorizationRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String url = BASE_RELATIONSHIPS_TERM_CATEGORIZATION_URL;
        SubjectAreaOMASAPIResponse response = deleteRelationship(userId, guid, methodName, url);
        Categorization gotTermCategorizationRelationship = DetectUtils.detectAndReturnTermCategorizationRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermCategorizationRelationship;
    }

    @Override
    public void purgeTermCategorizationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       RelationshipNotPurgedException,
                                                                                       UnrecognizedGUIDException,
                                                                                       MetadataServerUncontactableException,
                                                                                       UnexpectedResponseException,
                                                                                       PropertyServerException,
                                                                                       FunctionNotSupportedException {
        final String methodName = "purgeTermCategorizationRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String url = BASE_RELATIONSHIPS_TERM_CATEGORIZATION_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    @Override
    public Categorization restoreTermCategorizationRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   MetadataServerUncontactableException,
                                                                                                   UnexpectedResponseException,
                                                                                                   UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "restoreTermCategorizationRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String url = BASE_RELATIONSHIPS_TERM_CATEGORIZATION_URL;
        SubjectAreaOMASAPIResponse response = restoreRelationship(userId, guid, methodName, url);
        Categorization gotTermCategorizationRelationship = DetectUtils.detectAndReturnTermCategorizationRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermCategorizationRelationship;
    }

    @Override
    public TermAnchor createTermAnchorRelationship(String userId, TermAnchor termAnchor) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                MetadataServerUncontactableException,
                                                                                                UnexpectedResponseException,
                                                                                                UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {

        final String methodName = "createTermAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, BASE_RELATIONSHIPS_TERM_ANCHOR_URL, termAnchor);

        TermAnchor createdTermAnchorRelationship = DetectUtils.detectAndReturnTermAnchorRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdTermAnchorRelationship;

    }

    @Override
    public TermAnchor getTermAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                   MetadataServerUncontactableException,
                                                                                   UserNotAuthorizedException,
                                                                                   UnexpectedResponseException,
                                                                                   UnrecognizedGUIDException,
                                                                                   PropertyServerException,
                                                                                   FunctionNotSupportedException {
        final String methodName = "getTermAnchorRelationship";
        SubjectAreaOMASAPIResponse response = getRelationship(userId, guid, methodName, BASE_RELATIONSHIPS_TERM_ANCHOR_URL);
        TermAnchor gotTermAnchorRelationship = DetectUtils.detectAndReturnTermAnchorRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermAnchorRelationship;
    }

    @Override
    public TermAnchor replaceTermAnchorRelationship(String userId, TermAnchor termCategorizationRelationship) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     MetadataServerUncontactableException,
                                                                                                                     UnexpectedResponseException,
                                                                                                                     UnrecognizedGUIDException,
                                                                                                                     PropertyServerException,
                                                                                                                     FunctionNotSupportedException {
        final String methodName = "replaceTermAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, termCategorizationRelationship.getGuid(), BASE_RELATIONSHIPS_TERM_ANCHOR_URL, termCategorizationRelationship, true);
        TermAnchor updatedTermAnchorRelationship = DetectUtils.detectAndReturnTermAnchorRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedTermAnchorRelationship;
    }

    @Override
    public TermAnchor deleteTermAnchorRelationship(String userId, String guid) throws
                                                                               InvalidParameterException,
                                                                               MetadataServerUncontactableException,
                                                                               UserNotAuthorizedException,
                                                                               UnrecognizedGUIDException,
                                                                               FunctionNotSupportedException,
                                                                               RelationshipNotDeletedException,
                                                                               UnexpectedResponseException,
                                                                               PropertyServerException {
        final String methodName = "deleteTermAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String url = BASE_RELATIONSHIPS_TERM_ANCHOR_URL;
        SubjectAreaOMASAPIResponse response = deleteRelationship(userId, guid, methodName, url);
        TermAnchor gotTermAnchorRelationship = DetectUtils.detectAndReturnTermAnchorRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermAnchorRelationship;
    }

    @Override
    public void purgeTermAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               RelationshipNotPurgedException,
                                                                               UnrecognizedGUIDException,
                                                                               MetadataServerUncontactableException,
                                                                               UnexpectedResponseException,
                                                                               PropertyServerException,
                                                                               FunctionNotSupportedException {
        final String methodName = "purgeTermAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String url = BASE_RELATIONSHIPS_TERM_ANCHOR_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    @Override
    public TermAnchor restoreTermAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       MetadataServerUncontactableException,
                                                                                       UnexpectedResponseException,
                                                                                       UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "restoreTermAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String url = BASE_RELATIONSHIPS_TERM_ANCHOR_URL;
        SubjectAreaOMASAPIResponse response = restoreRelationship(userId, guid, methodName, url);
        TermAnchor gotTermAnchorRelationship = DetectUtils.detectAndReturnTermAnchorRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotTermAnchorRelationship;
    }

    @Override
    public CategoryAnchor createCategoryAnchorRelationship(String userId, CategoryAnchor categoryAnchor) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                MetadataServerUncontactableException,
                                                                                                                UnexpectedResponseException,
                                                                                                                UnrecognizedGUIDException,
                                                                                                                PropertyServerException,
                                                                                                                FunctionNotSupportedException {

        final String methodName = "createCategoryAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, BASE_RELATIONSHIPS_CATEGORY_ANCHOR_URL, categoryAnchor);

        CategoryAnchor createdCategoryAnchorRelationship = DetectUtils.detectAndReturnCategoryAnchorRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdCategoryAnchorRelationship;

    }

    @Override
    public CategoryAnchor getCategoryAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                           MetadataServerUncontactableException,
                                                                                           UserNotAuthorizedException,
                                                                                           UnexpectedResponseException,
                                                                                           UnrecognizedGUIDException,
                                                                                           PropertyServerException,
                                                                                           FunctionNotSupportedException {
        final String methodName = "getCategoryAnchorRelationship";
        SubjectAreaOMASAPIResponse response = getRelationship(userId, guid, methodName, BASE_RELATIONSHIPS_CATEGORY_ANCHOR_URL);
        CategoryAnchor gotCategoryAnchorRelationship = DetectUtils.detectAndReturnCategoryAnchorRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotCategoryAnchorRelationship;
    }

    @Override
    public CategoryAnchor updateCategoryAnchorRelationship(String userId, CategoryAnchor termCategorizationRelationship) throws InvalidParameterException,
                                                                                                                                UserNotAuthorizedException,
                                                                                                                                MetadataServerUncontactableException,
                                                                                                                                UnexpectedResponseException,
                                                                                                                                UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateCategoryAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, termCategorizationRelationship.getGuid(), BASE_RELATIONSHIPS_CATEGORY_ANCHOR_URL, termCategorizationRelationship, false);
        CategoryAnchor updatedCategoryAnchorRelationship = DetectUtils.detectAndReturnCategoryAnchorRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedCategoryAnchorRelationship;
    }

    @Override
    public CategoryAnchor replaceCategoryAnchorRelationship(String userId, CategoryAnchor termCategorizationRelationship) throws InvalidParameterException,
                                                                                                                                 UserNotAuthorizedException,
                                                                                                                                 MetadataServerUncontactableException,
                                                                                                                                 UnexpectedResponseException,
                                                                                                                                 UnrecognizedGUIDException,
                                                                                                                                 PropertyServerException,
                                                                                                                                 FunctionNotSupportedException {
        final String methodName = "replaceCategoryAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, termCategorizationRelationship.getGuid(), BASE_RELATIONSHIPS_CATEGORY_ANCHOR_URL, termCategorizationRelationship, true);
        CategoryAnchor updatedCategoryAnchorRelationship = DetectUtils.detectAndReturnCategoryAnchorRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedCategoryAnchorRelationship;
    }

    @Override
    public CategoryAnchor deleteCategoryAnchorRelationship(String userId, String guid) throws
                                                                                       InvalidParameterException,
                                                                                       MetadataServerUncontactableException,
                                                                                       UserNotAuthorizedException,
                                                                                       UnrecognizedGUIDException,
                                                                                       FunctionNotSupportedException,
                                                                                       RelationshipNotDeletedException,
                                                                                       UnexpectedResponseException, PropertyServerException {
        final String methodName = "deleteCategoryAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String url = BASE_RELATIONSHIPS_CATEGORY_ANCHOR_URL;
        SubjectAreaOMASAPIResponse response = deleteRelationship(userId, guid, methodName, url);
        CategoryAnchor gotCategoryAnchorRelationship = DetectUtils.detectAndReturnCategoryAnchorRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotCategoryAnchorRelationship;
    }

    @Override
    public void purgeCategoryAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   RelationshipNotPurgedException,
                                                                                   UnrecognizedGUIDException,
                                                                                   MetadataServerUncontactableException,
                                                                                   UnexpectedResponseException,
                                                                                   PropertyServerException,
                                                                                   FunctionNotSupportedException {
        final String methodName = "purgeCategoryAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String url = BASE_RELATIONSHIPS_CATEGORY_ANCHOR_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }
    @Override
    public CategoryAnchor restoreCategoryAnchorRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               MetadataServerUncontactableException,
                                                                                               UnexpectedResponseException,
                                                                                               UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "restoreCategoryAnchorRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String url = BASE_RELATIONSHIPS_CATEGORY_ANCHOR_URL;
        SubjectAreaOMASAPIResponse response = restoreRelationship(userId, guid, methodName, url);
        CategoryAnchor gotCategoryAnchorRelationship = DetectUtils.detectAndReturnCategoryAnchorRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotCategoryAnchorRelationship;
    }

    @Override
    public ProjectScope createProjectScopeRelationship(String userId, ProjectScope projectScope) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "createProjectScopeRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, BASE_RELATIONSHIPS_PROJECT_SCOPE_URL, projectScope);
        ProjectScope createdProjectScope = DetectUtils.detectAndReturnProjectScope(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return createdProjectScope;
    }

    @Override
    public ProjectScope getProjectScopeRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                       MetadataServerUncontactableException,
                                                                                       UserNotAuthorizedException,
                                                                                       UnexpectedResponseException,
                                                                                       UnrecognizedGUIDException,
                                                                                       PropertyServerException,
                                                                                       FunctionNotSupportedException {
        final String methodName = "getProjectScopeRelationship";
        SubjectAreaOMASAPIResponse response = getRelationship(userId, guid, methodName, BASE_RELATIONSHIPS_PROJECT_SCOPE_URL);
        ProjectScope gotProjectScope = DetectUtils.detectAndReturnProjectScope(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotProjectScope;
    }
    @Override
    public ProjectScope updateProjectScopeRelationship(String userId, ProjectScope projectScopeRelationship) throws InvalidParameterException,
                                                                                                                    MetadataServerUncontactableException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    UnexpectedResponseException,
                                                                                                                    UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateProjectScopeRelationship";
        SubjectAreaOMASAPIResponse response = updateRelationship(userId, projectScopeRelationship.getGuid(), BASE_RELATIONSHIPS_PROJECT_SCOPE_URL, projectScopeRelationship, false);
        ProjectScope updatedProjectScopeRelationship = DetectUtils.detectAndReturnProjectScope(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedProjectScopeRelationship;
    }

    @Override
    public ProjectScope replaceProjectScopeRelationship(String userId, ProjectScope projectScopeRelationship) throws InvalidParameterException,
                                                                                                                     MetadataServerUncontactableException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     UnexpectedResponseException,
                                                                                                                     UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateProjectScopeRelationship";

        SubjectAreaOMASAPIResponse response = updateRelationship(userId, projectScopeRelationship.getGuid(), BASE_RELATIONSHIPS_PROJECT_SCOPE_URL, projectScopeRelationship, true);
        ProjectScope updatedProjectScopeRelationship = DetectUtils.detectAndReturnProjectScope(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return updatedProjectScopeRelationship;
    }

    @Override
    public ProjectScope deleteProjectScopeRelationship(String userId, String guid) throws
                                                                                   InvalidParameterException,
                                                                                   MetadataServerUncontactableException,
                                                                                   UnrecognizedGUIDException,
                                                                                   UserNotAuthorizedException,
                                                                                   FunctionNotSupportedException,
                                                                                   RelationshipNotDeletedException,
                                                                                   UnexpectedResponseException, PropertyServerException {
        final String methodName = "deleteProjectScopeRelationship";
        String url = BASE_RELATIONSHIPS_PROJECT_SCOPE_URL;
        SubjectAreaOMASAPIResponse response = deleteRelationship(userId, guid, methodName, url);
        ProjectScope gotProjectScope = DetectUtils.detectAndReturnProjectScope(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotProjectScope;
    }

    @Override
    public void purgeProjectScopeRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 RelationshipNotPurgedException,
                                                                                 UnrecognizedGUIDException,
                                                                                 MetadataServerUncontactableException,
                                                                                 UnexpectedResponseException,
                                                                                 PropertyServerException,
                                                                                 FunctionNotSupportedException {
        final String methodName = "purgeProjectScopeRelationship";
        String url = BASE_RELATIONSHIPS_PROJECT_SCOPE_URL;
        purgeRelationship(userId, guid, methodName, url);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    @Override
    public ProjectScope restoreProjectScopeRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           MetadataServerUncontactableException,
                                                                                           UnexpectedResponseException,
                                                                                           UnrecognizedGUIDException, PropertyServerException, FunctionNotSupportedException,
                                                                                           PropertyServerException,
                                                                                           FunctionNotSupportedException {
        final String methodName = "restoreProjectScopeRelationship";
        String url = BASE_RELATIONSHIPS_PROJECT_SCOPE_URL;
        SubjectAreaOMASAPIResponse response = restoreRelationship(userId, guid, methodName, url);
        ProjectScope gotProjectScope = DetectUtils.detectAndReturnProjectScope(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotProjectScope;
    }

    @Override
    public SemanticAssignment getSemanticAssignmentRelationship(String userId, String guid) throws InvalidParameterException,
                                                                                                   MetadataServerUncontactableException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   UnexpectedResponseException,
                                                                                                   UnrecognizedGUIDException,
                                                                                                   PropertyServerException,
                                                                                                   FunctionNotSupportedException {
        final String methodName = "getSemanticAssignmentRelationship";
        SubjectAreaOMASAPIResponse response = getRelationship(userId, guid, methodName, BASE_RELATIONSHIPS_SEMANTIC_ASSIGNMENT_URL);
        SemanticAssignment gotSemanticAssignmentRelationship = DetectUtils.detectAndReturnSemanticAssignmentRelationship(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return gotSemanticAssignmentRelationship;
    }

    /**
     * Update Relationship.
     *
     * @param userId      userId under which the request is performed
     * @param guid        unique identifier for the relationship
     * @param baseUrl     omasServerUrl to build the rest call on
     * @param requestBody requestBody String representation of the relationship
     * @param isReplace   flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return the updated term.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws UnrecognizedGUIDException,           PropertyServerException, FunctionNotSupportedException            the supplied guid was not recognised
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    private SubjectAreaOMASAPIResponse updateRelationship(String userId, String guid, String baseUrl, Object requestBody, boolean isReplace) throws UserNotAuthorizedException, InvalidParameterException, PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        final String urlTemplate = baseUrl + "/%s?isReplace=%b";
        SubjectAreaOMASAPIResponse response = putRESTCall(userId, guid, isReplace, methodName, urlTemplate, requestBody);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }
}
