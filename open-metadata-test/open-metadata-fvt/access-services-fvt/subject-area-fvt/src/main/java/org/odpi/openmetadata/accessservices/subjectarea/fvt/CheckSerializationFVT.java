/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.glossaries.SubjectAreaGlossaryClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.terms.SubjectAreaTermClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaRelationshipClients;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.io.IOException;
import java.util.List;

public class CheckSerializationFVT {
    private final String userId;
    private final SubjectAreaRelationshipClients subjectAreaRelationship;
    private final SubjectAreaNodeClient<Term> subjectAreaTerm;
    private final SubjectAreaNodeClient<Glossary> subjectAreaGlossary;

    public CheckSerializationFVT(String url, String serverName, String userId) throws InvalidParameterException {
        this.userId = userId;

        SubjectAreaRestClient client = new SubjectAreaRestClient(serverName, url);
        this.subjectAreaTerm = new SubjectAreaTermClient<>(client);
        this.subjectAreaGlossary = new SubjectAreaGlossaryClient<>(client);
        this.subjectAreaRelationship = new SubjectAreaRelationship(client);
    }

    public static void main(String[] args) {
        try {
            String url = RunAllFVTOn2Servers.getUrl(args);
            runWith2Servers(url);
        } catch (IOException e1) {
            System.out.println("Error getting user input");
        } catch (SubjectAreaFVTCheckedException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            System.out.println("ERROR: " + e.getReportedErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        }

    }

    public static void runWith2Servers(String url) throws  SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        runIt(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        runIt(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
    }

    public static void runIt(String url, String serverName, String userId) throws InvalidParameterException, SubjectAreaFVTCheckedException, PropertyServerException, UserNotAuthorizedException {
        System.out.println("CheckSerializationFVT runIt started");
        CheckSerializationFVT fvt = new CheckSerializationFVT(url, serverName, userId);
        fvt.run();
        System.out.println("CheckSerializationFVT runIt stopped");
    }

    private void run() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        Glossary glossary = new Glossary();
        glossary.setName("Test");
        Glossary gotGlossary = subjectAreaGlossary.create(userId, glossary);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(gotGlossary.getSystemAttributes().getGUID());

        Term termOne = new Term();
        termOne.setName("Test_term_a");
        termOne.setGlossary(glossarySummary);
        Term termTwo = new Term();
        termTwo.setName("Test_term_b");
        termTwo.setGlossary(glossarySummary);

        Term gotTermOne = subjectAreaTerm.create(userId, termOne);
        Term gotTermTwo = subjectAreaTerm.create(userId, termTwo);
        String oneTermGuid = gotTermOne.getSystemAttributes().getGUID();
        String twoTermGuid = gotTermTwo.getSystemAttributes().getGUID();
        checkChildrenSerialization(oneTermGuid, twoTermGuid);

        subjectAreaTerm.delete(userId, oneTermGuid);
        subjectAreaTerm.delete(userId, twoTermGuid);
        subjectAreaGlossary.delete(userId, gotGlossary.getSystemAttributes().getGUID());
    }

    public void checkChildrenSerialization(String oneTermGuid, String twoTermGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        List<Relationship> termAnchors = subjectAreaTerm.getAllRelationships(userId, oneTermGuid);
        checkCastChild(termAnchors.get(0), TermAnchor.class);
        System.out.println("TermAnchor is ok.");

        createHasA(oneTermGuid, twoTermGuid);
        List<Relationship> hasAList = subjectAreaTerm.getAllRelationships(userId, oneTermGuid);
        hasAList.removeIf(line -> line instanceof TermAnchor);
        HasA hasA = checkCastChild(hasAList.get(0), HasA.class);
        subjectAreaRelationship.hasA().delete(userId, hasA.getGuid());
        System.out.println("HasA is ok.");

        createIsA(oneTermGuid, twoTermGuid);
        List<Relationship> isAList = subjectAreaTerm.getAllRelationships(userId, oneTermGuid);
        isAList.removeIf(line -> line instanceof TermAnchor);
        IsA isA = checkCastChild(isAList.get(0), IsA.class);
        subjectAreaRelationship.isA().delete(userId, isA.getGuid());
        System.out.println("IsA is ok.");

        createRelatedTerm(oneTermGuid,twoTermGuid);
        List<Relationship> relatedTerms = subjectAreaTerm.getAllRelationships(userId, oneTermGuid);
        relatedTerms.removeIf(line -> line instanceof TermAnchor);
        RelatedTerm relatedTerm = checkCastChild(relatedTerms.get(0), RelatedTerm.class);
        subjectAreaRelationship.relatedTerm().delete(userId, relatedTerm.getGuid());
        System.out.println("RelatedTerm is ok.");

        createTranslation(oneTermGuid, twoTermGuid);
        List<Relationship> translations = subjectAreaTerm.getAllRelationships(userId, oneTermGuid);
        translations.removeIf(line -> line instanceof TermAnchor);
        Translation translation = checkCastChild(translations.get(0), Translation.class);
        subjectAreaRelationship.translation().delete(userId, translation.getGuid());
        System.out.println("Translation is ok.");

        createPreferredTerm(oneTermGuid, twoTermGuid);
        List<Relationship> preferredTerms = subjectAreaTerm.getAllRelationships(userId, oneTermGuid);
        preferredTerms.removeIf(line -> line instanceof TermAnchor);
        PreferredTerm preferredTerm = checkCastChild(preferredTerms.get(0), PreferredTerm.class);
        subjectAreaRelationship.preferredTerm().delete(userId, preferredTerm.getGuid());
        System.out.println("PreferredTerm is ok.");

        createSynonym(oneTermGuid, twoTermGuid);
        List<Relationship> synonyms = subjectAreaTerm.getAllRelationships(userId, oneTermGuid);
        synonyms.removeIf(line -> line instanceof TermAnchor);
        Synonym synonym = checkCastChild(synonyms.get(0), Synonym.class);
        subjectAreaRelationship.synonym().delete(userId, synonym.getGuid());
        System.out.println("Synonym is ok.");
    }

    private  <L extends Relationship, ForCast extends Relationship>ForCast checkCastChild(L line, Class<ForCast> lClass) throws SubjectAreaFVTCheckedException {
        try {
            return (ForCast) line;
        } catch (ClassCastException e) {
            throw new SubjectAreaFVTCheckedException("The class " + lClass.getSimpleName()
                    + "was deserialized incorrectly, so it cannot be cast");
        }
    }

    private void createHasA(String oneTermGuid, String twoTermGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        HasA hasA = new HasA();
        hasA.getEnd1().setNodeGuid(oneTermGuid);
        hasA.getEnd2().setNodeGuid(twoTermGuid);
        subjectAreaRelationship.hasA().create(userId, hasA);
    }

    private void createIsA(String oneTermGuid, String twoTermGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        IsA isA = new IsA();
        isA.getEnd1().setNodeGuid(oneTermGuid);
        isA.getEnd2().setNodeGuid(twoTermGuid);
        subjectAreaRelationship.isA().create(userId, isA);
    }

    private void createRelatedTerm(String oneTermGuid, String twoTermGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        RelatedTerm relatedTerm = new RelatedTerm();
        relatedTerm.getEnd1().setNodeGuid(oneTermGuid);
        relatedTerm.getEnd2().setNodeGuid(twoTermGuid);
        subjectAreaRelationship.relatedTerm().create(userId, relatedTerm);
    }

    private void createTranslation(String oneTermGuid, String twoTermGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Translation translation = new Translation();
        translation.getEnd1().setNodeGuid(oneTermGuid);
        translation.getEnd2().setNodeGuid(twoTermGuid);
        subjectAreaRelationship.translation().create(userId, translation);
    }

    private void createPreferredTerm(String oneTermGuid, String twoTermGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        PreferredTerm preferredTerm = new PreferredTerm();
        preferredTerm.getEnd1().setNodeGuid(oneTermGuid);
        preferredTerm.getEnd2().setNodeGuid(twoTermGuid);
        subjectAreaRelationship.preferredTerm().create(userId, preferredTerm);
    }

    private void createSynonym(String oneTermGuid, String twoTermGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Synonym synonym = new Synonym();
        synonym.getEnd1().setNodeGuid(oneTermGuid);
        synonym.getEnd2().setNodeGuid(twoTermGuid);
        subjectAreaRelationship.synonym().create(userId, synonym);
    }
}