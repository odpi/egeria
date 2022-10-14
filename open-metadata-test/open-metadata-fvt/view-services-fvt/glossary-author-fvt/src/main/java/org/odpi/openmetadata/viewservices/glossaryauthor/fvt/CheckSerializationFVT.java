/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.fvt;
import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.GlossaryAuthorViewRestClient;
import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.relationships.GlossaryAuthorViewRelationshipsClient;
import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.term.GlossaryAuthorViewTermClient;
import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.glossarys.GlossaryAuthorViewGlossaryClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GenericResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

import java.io.IOException;
import java.util.List;
/**
 * FVT to call Glossary Author View API for checking serialization.
 */

public class CheckSerializationFVT {
    private final String userId;
    private final GlossaryAuthorViewRelationshipsClient glossaryAuthorViewRelationship;
    private final GlossaryAuthorViewTermClient glossaryAuthorViewTerm;
    private final GlossaryAuthorViewGlossaryClient glossaryAuthorViewGlossary;
    private static Logger log = LoggerFactory.getLogger(CheckSerializationFVT.class);


    private static final String HAS_A = "has-as";
    private static final String RELATED_TERM = "related-terms";
    private static final String SYNONYM = "synonyms";
    private static final String ANTONYM = "antonyms";
    private static final String TRANSLATION = "translations";
    private static final String USED_IN_CONTEXT = "used-in-contexts";
    private static final String PREFERRED_TERM = "preferred-terms";
    private static final String VALID_VALUE = "valid-values";
    private static final String REPLACEMENT_TERM = "replacement-terms";
    private static final String TYPED_BY = "typed-bys";
    private static final String IS_A = "is-as";
    private static final String IS_A_TYPE_OF_DEPRECATED = "is-a-type-of-deprecateds";
    private static final String IS_A_TYPE_OF = "is-a-type-ofs";
    private static final String TERM_CATEGORIZATION = "term-categorizations";
    private static final String SEMANTIC_ASSIGNMENT = "semantic-assignments";
    private static final String TERM_ANCHOR = "term-anchor";
    private static final String CATEGORY_ANCHOR = "category-anchor";
    private static final String PROJECT_SCOPE = "project-scopes";
    private static final String CATEGORY_HIERARCHY_LINK = "category-hierarchy-links";


    public CheckSerializationFVT(String url, String serverName, String userId) throws InvalidParameterException {
        this.userId = userId;

        GlossaryAuthorViewRestClient client = new GlossaryAuthorViewRestClient(serverName, url);
        this.glossaryAuthorViewTerm = new GlossaryAuthorViewTermClient(client);
        this.glossaryAuthorViewGlossary = new GlossaryAuthorViewGlossaryClient(client);
        this.glossaryAuthorViewRelationship = new GlossaryAuthorViewRelationshipsClient(client);
    }

    public static void main(String[] args) {
        try {
            String url = RunAllFVTOn2Servers.getUrl(args);
            runWith2Servers(url);
        } catch (IOException e1) {
            System.out.println("Error getting user input");
        } catch (GlossaryAuthorFVTCheckedException e) {
            log.error("ERROR: " + e.getMessage());
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            log.error("ERROR: " + e.getReportedErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        }

    }

    public static void runWith2Servers(String url) throws  GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        runIt(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        runIt(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
    }

    public static void runIt(String url, String serverName, String userId) throws InvalidParameterException, GlossaryAuthorFVTCheckedException, PropertyServerException, UserNotAuthorizedException {
        try
        {
            System.out.println("CheckSerializationFVT runIt started");
            CheckSerializationFVT fvt = new CheckSerializationFVT(url, serverName, userId);
            fvt.run();
            System.out.println("CheckSerializationFVT runIt stopped");
        }
        catch (Exception error) {
            log.error("The FVT Encountered an Exception", error);
            throw error;
        }
    }

    private void run() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException  {
        Glossary glossary = new Glossary();
        glossary.setName("Test");
        Glossary gotGlossary = glossaryAuthorViewGlossary.create(userId, glossary);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(gotGlossary.getSystemAttributes().getGUID());

        Term termOne = new Term();
        termOne.setName("Test_term_a");
        termOne.setGlossary(glossarySummary);
        Term termTwo = new Term();
        termTwo.setName("Test_term_b");
        termTwo.setGlossary(glossarySummary);

        Term gotTermOne = glossaryAuthorViewTerm.create(userId, termOne);
        Term gotTermTwo = glossaryAuthorViewTerm.create(userId, termTwo);
        String oneTermGuid = gotTermOne.getSystemAttributes().getGUID();
        String twoTermGuid = gotTermTwo.getSystemAttributes().getGUID();
        checkChildrenSerialization(oneTermGuid, twoTermGuid);

        glossaryAuthorViewTerm.delete(userId, oneTermGuid);
        glossaryAuthorViewTerm.delete(userId, twoTermGuid);
        glossaryAuthorViewGlossary.delete(userId, gotGlossary.getSystemAttributes().getGUID());
    }

    public void checkChildrenSerialization(String oneTermGuid, String twoTermGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException {
        List<Relationship> termAnchors = glossaryAuthorViewTerm.getAllRelationships(userId, oneTermGuid);
        checkCastChild(termAnchors.get(0), TermAnchor.class);
        if (log.isDebugEnabled()) {
            log.debug("TermAnchor is ok.");
        }

        createHasA(oneTermGuid, twoTermGuid);
        List<Relationship> hasAList = glossaryAuthorViewTerm.getAllRelationships(userId, oneTermGuid);
        hasAList.removeIf(line -> line instanceof TermAnchor);
        HasA hasA = checkCastChild(hasAList.get(0), HasA.class);

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, HasA.class);
        ParameterizedTypeReference<GenericResponse<HasA>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        glossaryAuthorViewRelationship.deleteRel(userId, hasA.getGuid(), type, HAS_A);

        if (log.isDebugEnabled()) {
            log.debug("HasA is ok.");
        }

        createIsA(oneTermGuid, twoTermGuid);
        List<Relationship> isAList = glossaryAuthorViewTerm.getAllRelationships(userId, oneTermGuid);
        isAList.removeIf(line -> line instanceof TermAnchor);
        IsA isA = checkCastChild(isAList.get(0), IsA.class);
        resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, IsA.class);
        ParameterizedTypeReference<GenericResponse<IsA>> typeIsA = ParameterizedTypeReference.forType(resolvableType.getType());

        glossaryAuthorViewRelationship.deleteRel(userId, isA.getGuid(),typeIsA, IS_A);
        if (log.isDebugEnabled()) {
            log.debug("IsA is ok.");
        }

        createRelatedTerm(oneTermGuid,twoTermGuid);
        List<Relationship> relatedTerms = glossaryAuthorViewTerm.getAllRelationships(userId, oneTermGuid);
        relatedTerms.removeIf(line -> line instanceof TermAnchor);
        RelatedTerm relatedTerm = checkCastChild(relatedTerms.get(0), RelatedTerm.class);

        resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, RelatedTerm.class);
        ParameterizedTypeReference<GenericResponse<RelatedTerm>> typeRT = ParameterizedTypeReference.forType(resolvableType.getType());

        glossaryAuthorViewRelationship.deleteRel(userId, relatedTerm.getGuid(),typeRT, RELATED_TERM);
        if (log.isDebugEnabled()) {
            log.debug("RelatedTerm is ok.");
        }

        createTranslation(oneTermGuid, twoTermGuid);
        List<Relationship> translations = glossaryAuthorViewTerm.getAllRelationships(userId, oneTermGuid);
        translations.removeIf(line -> line instanceof TermAnchor);
        Translation translation = checkCastChild(translations.get(0), Translation.class);

        resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Translation.class);
        ParameterizedTypeReference<GenericResponse<Translation>> typeT = ParameterizedTypeReference.forType(resolvableType.getType());

        glossaryAuthorViewRelationship.deleteRel(userId, translation.getGuid(),typeT,TRANSLATION);
        if (log.isDebugEnabled()) {
            log.debug("Translation is ok.");
        }

        createPreferredTerm(oneTermGuid, twoTermGuid);
        List<Relationship> preferredTerms = glossaryAuthorViewTerm.getAllRelationships(userId, oneTermGuid);
        preferredTerms.removeIf(line -> line instanceof TermAnchor);
        PreferredTerm preferredTerm = checkCastChild(preferredTerms.get(0), PreferredTerm.class);

        resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, PreferredTerm.class);
        ParameterizedTypeReference<GenericResponse<PreferredTerm>> typePT = ParameterizedTypeReference.forType(resolvableType.getType());


        glossaryAuthorViewRelationship.deleteRel(userId, preferredTerm.getGuid(),typePT,PREFERRED_TERM);
        if (log.isDebugEnabled()) {
            log.debug("PreferredTerm is ok.");
        }

        createSynonym(oneTermGuid, twoTermGuid);
        List<Relationship> synonyms = glossaryAuthorViewTerm.getAllRelationships(userId, oneTermGuid);
        synonyms.removeIf(line -> line instanceof TermAnchor);
        Synonym synonym = checkCastChild(synonyms.get(0), Synonym.class);
        resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Synonym.class);
        ParameterizedTypeReference<GenericResponse<Synonym>> typeS = ParameterizedTypeReference.forType(resolvableType.getType());

        glossaryAuthorViewRelationship.deleteRel(userId, synonym.getGuid(), typeS,SYNONYM);
        if (log.isDebugEnabled()) {
            log.debug("Synonym is ok.");
        }
    }

    private  <L extends Relationship, ForCast extends Relationship>ForCast checkCastChild(L line, Class<ForCast> lClass) throws GlossaryAuthorFVTCheckedException {
        try {
            return (ForCast) line;
        } catch (ClassCastException e) {
            throw new GlossaryAuthorFVTCheckedException("The class " + lClass.getSimpleName()
                    + "was deserialized incorrectly, so it cannot be cast");
        }
    }

    private void createHasA(String oneTermGuid, String twoTermGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        HasA hasA = new HasA();
        hasA.getEnd1().setNodeGuid(oneTermGuid);
        hasA.getEnd2().setNodeGuid(twoTermGuid);

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, HasA.class);
        ParameterizedTypeReference<GenericResponse<HasA>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        glossaryAuthorViewRelationship.createRel(userId,hasA, type, HAS_A);
    }

    private void createIsA(String oneTermGuid, String twoTermGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        IsA isA = new IsA();
        isA.getEnd1().setNodeGuid(oneTermGuid);
        isA.getEnd2().setNodeGuid(twoTermGuid);

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, IsA.class);
        ParameterizedTypeReference<GenericResponse<IsA>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        glossaryAuthorViewRelationship.createRel(userId, isA,type, IS_A);
    }

    private void createRelatedTerm(String oneTermGuid, String twoTermGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        RelatedTerm relatedTerm = new RelatedTerm();
        relatedTerm.getEnd1().setNodeGuid(oneTermGuid);
        relatedTerm.getEnd2().setNodeGuid(twoTermGuid);

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, RelatedTerm.class);
        ParameterizedTypeReference<GenericResponse<RelatedTerm>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        glossaryAuthorViewRelationship.createRel(userId, relatedTerm,type, RELATED_TERM);

    }

    private void createTranslation(String oneTermGuid, String twoTermGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Translation translation = new Translation();
        translation.getEnd1().setNodeGuid(oneTermGuid);
        translation.getEnd2().setNodeGuid(twoTermGuid);

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Translation.class);
        ParameterizedTypeReference<GenericResponse<Translation>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        glossaryAuthorViewRelationship.createRel(userId, translation,type, TRANSLATION);

    }

    private void createPreferredTerm(String oneTermGuid, String twoTermGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        PreferredTerm preferredTerm = new PreferredTerm();
        preferredTerm.getEnd1().setNodeGuid(oneTermGuid);
        preferredTerm.getEnd2().setNodeGuid(twoTermGuid);
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, PreferredTerm.class);
        ParameterizedTypeReference<GenericResponse<PreferredTerm>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        glossaryAuthorViewRelationship.createRel(userId, preferredTerm,type, PREFERRED_TERM);
    }

    private void createSynonym(String oneTermGuid, String twoTermGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Synonym synonym = new Synonym();
        synonym.getEnd1().setNodeGuid(oneTermGuid);
        synonym.getEnd2().setNodeGuid(twoTermGuid);
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Synonym.class);
        ParameterizedTypeReference<GenericResponse<Synonym>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        glossaryAuthorViewRelationship.createRel(userId, synonym,type, SYNONYM);
    }
}