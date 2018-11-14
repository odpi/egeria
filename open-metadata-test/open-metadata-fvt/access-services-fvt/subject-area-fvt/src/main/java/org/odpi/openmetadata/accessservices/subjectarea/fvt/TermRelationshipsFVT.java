/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaTerm;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Antonym;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.RelatedTermRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Synonym;

import java.io.IOException;

/**
 * FVT resource to call subject area term client API
 */
public class TermRelationshipsFVT
{
    private static final String USERID = "Fred";

    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Test Glossary for relationships FVT";
    private static final String DEFAULT_TEST_TERM_NAME = "Test term A1";
    private static final String DEFAULT_TEST_TERM_NAME2 = "Test term B1";
    private static final String DEFAULT_TEST_TERM_NAME3 = "Test term C1";
    private static SubjectAreaRelationship subjectAreaRelationship = null;
    private static SubjectAreaTerm subjectAreaTerm  =null;

    public static void main(String args[])
    {
        SubjectArea subjectArea = null;
        try
        {
            String url = RunAllFVT.getUrl(args);
            initialiseTermRelationshipsFVT(url);
            System.out.println("Create a glossary");
            GlossaryFVT.initialiseGlossaryFVT(url);
            TermFVT.initialiseTermFVT(url);
            Glossary glossary = GlossaryFVT.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
            System.out.println("Create a term1 using glossary guid");
            Term term1 = TermFVT.createTermWithGlossaryGuid(DEFAULT_TEST_TERM_NAME, glossary.getSystemAttributes().getGUID());
            System.out.println("Create a term2 using glossary guid");
            Term term2 = TermFVT.createTermWithGlossaryGuid(DEFAULT_TEST_TERM_NAME2, glossary.getSystemAttributes().getGUID());
            System.out.println("Create a term3 using glossary guid");
            Term term3 = TermFVT.createTermWithGlossaryGuid(DEFAULT_TEST_TERM_NAME3, glossary.getSystemAttributes().getGUID());

            if (term1 != null && term2 !=null)
            {
                Synonym synonym = new Synonym();
                synonym.setDescription("ddd");
                synonym.setExpression("Ex");
                synonym.setSource("source");
                synonym.setSteward("Stew");
                synonym.setEntity1Guid(term1.getSystemAttributes().getGUID());
                synonym.setEntity2Guid(term2.getSystemAttributes().getGUID());
                Synonym createdSynonym = subjectAreaRelationship.createSynonymRelationship(USERID, synonym);
                System.out.println("Created Synonym " + createdSynonym);
                String guid = createdSynonym.getGuid();

                subjectAreaRelationship.getSynonymRelationship(USERID,guid);
                System.out.println("Got Synonym " + createdSynonym);
                subjectAreaRelationship.deleteSynonymRelationship(USERID,guid);
                System.out.println("Soft deleted Synonym with guid="+guid );
                subjectAreaRelationship.purgeSynonymRelationship(USERID,guid);
                System.out.println("Hard deleted Synonym with guid="+guid );
            }
            if (term1 != null && term3 !=null)
            {
                Antonym antonym = new Antonym();
                antonym.setDescription("ddd");
                antonym.setExpression("Ex");
                antonym.setSource("source");
                antonym.setSteward("Stew");
                antonym.setEntity1Guid(term1.getSystemAttributes().getGUID());
                antonym.setEntity2Guid(term3.getSystemAttributes().getGUID());
                Antonym createdAntonym = subjectAreaRelationship.createAntonymRelationship(USERID, antonym);
                System.out.println("Created Antonym " + createdAntonym);
                String guid = createdAntonym.getGuid();

                subjectAreaRelationship.getAntonymRelationship(USERID,guid);
                System.out.println("Got Antonym " + createdAntonym);
                subjectAreaRelationship.deleteAntonymRelationship(USERID,guid);
                System.out.println("Soft deleted Antonym with guid="+guid );
                subjectAreaRelationship.purgeAntonymRelationship(USERID,guid);
                System.out.println("Hard deleted Antonym with guid="+guid );
            }
            if (term1 != null && term3 !=null)
            {
                RelatedTermRelationship relatedterm = new RelatedTermRelationship();
                relatedterm.setDescription("ddd");
                relatedterm.setExpression("Ex");
                relatedterm.setSource("source");
                relatedterm.setSteward("Stew");
                relatedterm.setEntity1Guid(term1.getSystemAttributes().getGUID());
                relatedterm.setEntity2Guid(term3.getSystemAttributes().getGUID());
                RelatedTermRelationship createdRelatedTermRelationship = subjectAreaRelationship.createRelatedTermRelationship(USERID, relatedterm);
                System.out.println("Created RelatedTermRelationship " + createdRelatedTermRelationship);
                String guid = createdRelatedTermRelationship.getGuid();

                subjectAreaRelationship.getRelatedTermRelationship(USERID,guid);
                System.out.println("Got RelatedTermRelationship " + createdRelatedTermRelationship);
                subjectAreaRelationship.deleteRelatedTermRelationship(USERID,guid);
                System.out.println("Soft deleted RelatedTermRelationship with guid="+guid );
                subjectAreaRelationship.purgeRelatedTermRelationship(USERID,guid);
                System.out.println("Hard deleted RelatedTermRelationship with guid="+guid );
            }

        } catch (SubjectAreaCheckedExceptionBase e)
        {
            System.out.println("ERROR: " + e.getErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        } catch (IOException e)
        {
            System.out.println("Error getting user input");
        }
    }

    /**
     * Create Term associated with a glossary identified with a guid
     * @param termName name of the Term to create
     * @param glossaryGuid the guid of the Glossary to associate the Term with
     * @return Term the created term
     * @throws SubjectAreaCheckedExceptionBase error
     */
    public static Term createTermWithGlossaryGuid(String termName, String glossaryGuid) throws SubjectAreaCheckedExceptionBase
    {
        Term term = new Term();
        term.setName(termName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        term.setGlossary(glossarySummary);
        Term newTerm = subjectAreaTerm.createTerm(USERID, term);
        if (newTerm != null)
        {
            System.out.println("Created Term " + newTerm.getName() + " with guid " + newTerm.getSystemAttributes().getGUID());
        }
        return newTerm;
    }

    /**
     * Call this to initialise the relationship FVT
     *
     * @param url supplied base url for the subject area OMAS
     * @throws InvalidParameterException a parameter is null or an invalid value.
     */
    public static void initialiseTermRelationshipsFVT(String url) throws InvalidParameterException
    {
        subjectAreaRelationship = new SubjectAreaImpl(url).getSubjectAreaRelationship();
    }
}
