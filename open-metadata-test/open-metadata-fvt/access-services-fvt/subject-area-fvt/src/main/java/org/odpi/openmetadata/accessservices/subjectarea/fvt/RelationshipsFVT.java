/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaTerm;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;

import java.io.IOException;

/**
 * FVT resource to call subject area term client API
 */
public class RelationshipsFVT
{
    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Test Glossary for relationships FVT";
    private static final String DEFAULT_TEST_TERM_NAME = "Test term A1";
    private static final String DEFAULT_TEST_TERM_NAME2 = "Test term B1";
    private static final String DEFAULT_TEST_TERM_NAME3 = "Test term C1";
    private SubjectAreaRelationship subjectAreaRelationship = null;
    private GlossaryFVT glossaryFVT =null;
    private TermFVT termFVT =null;
    private String url = null;

    public RelationshipsFVT(String url) throws InvalidParameterException
    {
        this.url=url;
        subjectAreaRelationship = new SubjectAreaImpl(FVTConstants.SERVER_NAME1,url).getSubjectAreaRelationship();
        termFVT = new TermFVT(url,FVTConstants.SERVER_NAME1);
        glossaryFVT = new GlossaryFVT(url,FVTConstants.SERVER_NAME1);
    }

    public static void main(String args[])
    {
        SubjectArea subjectArea = null;
        String url = null;
        try
        {
            url = RunAllFVT.getUrl(args);
            RelationshipsFVT fvt =new RelationshipsFVT(url);
            fvt.run();
        } catch (IOException e1)
        {
            System.out.println("Error getting user input");
        } catch (SubjectAreaCheckedExceptionBase e)
        {
            System.out.println("ERROR: " + e.getErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        }

    }
    public static void runit(String url) throws SubjectAreaCheckedExceptionBase
    {
        RelationshipsFVT fvt =new  RelationshipsFVT(url);
        fvt.run();
    }
    public void run() throws SubjectAreaCheckedExceptionBase
    {
        SubjectArea subjectArea = null;
        System.out.println("Create a glossary");

        Glossary glossary = glossaryFVT.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
        System.out.println("Create a term1 using glossary guid");
        Term term1 = termFVT.createTermWithGlossaryGuid(DEFAULT_TEST_TERM_NAME, glossary.getSystemAttributes().getGUID());
        System.out.println("Create a term2 using glossary guid");
        Term term2 = termFVT.createTermWithGlossaryGuid(DEFAULT_TEST_TERM_NAME2, glossary.getSystemAttributes().getGUID());
        System.out.println("Create a term3 using glossary guid");
        Term term3 = termFVT.createTermWithGlossaryGuid(DEFAULT_TEST_TERM_NAME3, glossary.getSystemAttributes().getGUID());

        if (term1 != null && term2 != null)
        {
            Synonym synonym = new Synonym();
            synonym.setDescription("ddd");
            synonym.setExpression("Ex");
            synonym.setSource("source");
            synonym.setSteward("Stew");
            synonym.setSynonym1Guid(term1.getSystemAttributes().getGUID());
            synonym.setSynonym2Guid(term2.getSystemAttributes().getGUID());
            Synonym createdSynonym = subjectAreaRelationship.createSynonymRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, synonym);
            System.out.println("Created Synonym " + createdSynonym);
            String guid = createdSynonym.getGuid();

            subjectAreaRelationship.getSynonymRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Got Synonym " + createdSynonym);

            Synonym updateSynonym = new Synonym();
            updateSynonym.setDescription("ddd2");
            updateSynonym.setGuid(createdSynonym.getGuid());
            Synonym updatedSynonym = subjectAreaRelationship.updateSynonymRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, updateSynonym);
            if (!updatedSynonym.getDescription().equals(updateSynonym.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym update description not as expected", "", "");
            }
            if (!updatedSynonym.getSource().equals(createdSynonym.getSource()))
            {
                new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym update source not as expected", "", "");
            }
            if (!updatedSynonym.getExpression().equals(createdSynonym.getExpression()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym update expression not as expected", "", "");
            }
            if (!updatedSynonym.getSteward().equals(createdSynonym.getSteward()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym update steward not as expected", "", "");
            }
            if (!updatedSynonym.getSynonym1Guid().equals(createdSynonym.getSynonym1Guid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym update end 1 not as expected", "", "");
            }
            if (!updatedSynonym.getSynonym2Guid().equals(createdSynonym.getSynonym2Guid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym update end 2 not as expected", "", "");
            }
            System.out.println("Updated Synonym " + createdSynonym);
            Synonym replaceSynonym = new Synonym();
            replaceSynonym.setDescription("ddd3");
            replaceSynonym.setGuid(createdSynonym.getGuid());
            Synonym replacedSynonym = subjectAreaRelationship.replaceSynonymRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, replaceSynonym);
            if (!replacedSynonym.getDescription().equals(replaceSynonym.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym replace description not as expected", "", "");
            }
            if (replacedSynonym.getSource() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym replace source not as expected", "", "");
            }
            if (replacedSynonym.getExpression() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym replace expression not as expected", "", "");
            }
            if (replacedSynonym.getSteward() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym replace steward not as expected", "", "");
            }
            if (!replacedSynonym.getSynonym1Guid().equals(createdSynonym.getSynonym1Guid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym replace end 1 not as expected", "", "");
            }
            if (!replacedSynonym.getSynonym2Guid().equals(createdSynonym.getSynonym2Guid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym replace end 2 not as expected", "", "");
            }
            System.out.println("Replaced Synonym " + createdSynonym);
            subjectAreaRelationship.deleteSynonymRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Soft deleted Synonym with guid=" + guid);
            subjectAreaRelationship.purgeSynonymRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Hard deleted Synonym with guid=" + guid);
        }
        if (term1 != null && term3 != null)
        {
            Antonym antonym = new Antonym();
            antonym.setDescription("ddd");
            antonym.setExpression("Ex");
            antonym.setSource("source");
            antonym.setSteward("Stew");
            antonym.setAntonym1Guid(term1.getSystemAttributes().getGUID());
            antonym.setAntonym2Guid(term3.getSystemAttributes().getGUID());
            Antonym createdAntonym = subjectAreaRelationship.createAntonymRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, antonym);
            System.out.println("Created Antonym " + createdAntonym);
            String guid = createdAntonym.getGuid();

            subjectAreaRelationship.getAntonymRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Got Antonym " + createdAntonym);
            Antonym updateAntonym = new Antonym();
            updateAntonym.setDescription("ddd2");
            updateAntonym.setGuid(createdAntonym.getGuid());
            Antonym updatedAntonym = subjectAreaRelationship.updateAntonymRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, updateAntonym);
            if (!updatedAntonym.getDescription().equals(updateAntonym.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym update description not as expected", "", "");
            }
            if (!updatedAntonym.getSource().equals(createdAntonym.getSource()))
            {
                new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym update source not as expected", "", "");
            }
            if (!updatedAntonym.getExpression().equals(createdAntonym.getExpression()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym update expression not as expected", "", "");
            }
            if (!updatedAntonym.getSteward().equals(createdAntonym.getSteward()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym update steward not as expected", "", "");
            }
            if (!updatedAntonym.getAntonym1Guid().equals(createdAntonym.getAntonym1Guid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym update end 1 not as expected", "", "");
            }
            if (!updatedAntonym.getAntonym2Guid().equals(createdAntonym.getAntonym2Guid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym update end 2 not as expected", "", "");
            }
            System.out.println("Updated Antonym " + createdAntonym);
            Antonym replaceAntonym = new Antonym();
            replaceAntonym.setDescription("ddd3");
            replaceAntonym.setGuid(createdAntonym.getGuid());
            Antonym replacedAntonym = subjectAreaRelationship.replaceAntonymRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, replaceAntonym);
            if (!replacedAntonym.getDescription().equals(replaceAntonym.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym replace description not as expected", "", "");
            }
            if (replacedAntonym.getSource() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym replace source not as expected", "", "");
            }
            if (replacedAntonym.getExpression() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym replace expression not as expected", "", "");
            }
            if (replacedAntonym.getSteward() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym replace steward not as expected", "", "");
            }
            if (!replacedAntonym.getAntonym1Guid().equals(createdAntonym.getAntonym1Guid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym replace end 1 not as expected", "", "");
            }
            if (!replacedAntonym.getAntonym2Guid().equals(createdAntonym.getAntonym2Guid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym replace end 2 not as expected", "", "");
            }
            System.out.println("Replaced Antonym " + createdAntonym);


            subjectAreaRelationship.deleteAntonymRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Soft deleted Antonym with guid=" + guid);
            subjectAreaRelationship.purgeAntonymRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Hard deleted Antonym with guid=" + guid);
        }
        if (term1 != null && term3 != null)
        {
            RelatedTerm relatedterm = new RelatedTerm();
            relatedterm.setDescription("ddd");
            relatedterm.setExpression("Ex");
            relatedterm.setSource("source");
            relatedterm.setSteward("Stew");
            relatedterm.setRelatedTerm1Guid(term1.getSystemAttributes().getGUID());
            relatedterm.setRelatedTerm2Guid(term3.getSystemAttributes().getGUID());
            RelatedTerm createdRelatedTerm = subjectAreaRelationship.createRelatedTerm(FVTConstants.SERVER_NAME1,FVTConstants.USERID, relatedterm);
            System.out.println("Created RelatedTerm " + createdRelatedTerm);
            String guid = createdRelatedTerm.getGuid();

            subjectAreaRelationship.getRelatedTerm(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Got RelatedTerm " + createdRelatedTerm);
            RelatedTerm updateRelatedTerm = new RelatedTerm();
            updateRelatedTerm.setDescription("ddd2");
            updateRelatedTerm.setGuid(createdRelatedTerm.getGuid());
            RelatedTerm updatedRelatedTerm = subjectAreaRelationship.updateRelatedTerm(FVTConstants.SERVER_NAME1,FVTConstants.USERID, updateRelatedTerm);
            if (!updatedRelatedTerm.getDescription().equals(updateRelatedTerm.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm update description not as expected", "", "");
            }
            if (!updatedRelatedTerm.getSource().equals(createdRelatedTerm.getSource()))
            {
                new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm update source not as expected", "", "");
            }
            if (!updatedRelatedTerm.getExpression().equals(createdRelatedTerm.getExpression()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm update expression not as expected", "", "");
            }
            if (!updatedRelatedTerm.getSteward().equals(createdRelatedTerm.getSteward()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm update steward not as expected", "", "");
            }
            if (!updatedRelatedTerm.getRelatedTerm1Guid().equals(createdRelatedTerm.getRelatedTerm1Guid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm update end 1 not as expected", "", "");
            }
            if (!updatedRelatedTerm.getRelatedTerm2Guid().equals(createdRelatedTerm.getRelatedTerm2Guid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm update end 2 not as expected", "", "");
            }
            System.out.println("Updated RelatedTerm " + createdRelatedTerm);
            RelatedTerm replaceRelatedTerm = new RelatedTerm();
            replaceRelatedTerm.setDescription("ddd3");
            replaceRelatedTerm.setGuid(createdRelatedTerm.getGuid());
            RelatedTerm replacedRelatedTerm = subjectAreaRelationship.replaceRelatedTerm(FVTConstants.SERVER_NAME1,FVTConstants.USERID, replaceRelatedTerm);
            if (!replacedRelatedTerm.getDescription().equals(replaceRelatedTerm.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm replace description not as expected", "", "");
            }
            if (replacedRelatedTerm.getSource() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm replace source not as expected", "", "");
            }
            if (replacedRelatedTerm.getExpression() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm replace expression not as expected", "", "");
            }
            if (replacedRelatedTerm.getSteward() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm replace steward not as expected", "", "");
            }
            if (!replacedRelatedTerm.getRelatedTerm1Guid().equals(createdRelatedTerm.getRelatedTerm1Guid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm replace end 1 not as expected", "", "");
            }
            if (!replacedRelatedTerm.getRelatedTerm2Guid().equals(createdRelatedTerm.getRelatedTerm2Guid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm replace end 2 not as expected", "", "");
            }
            System.out.println("Replaced RelatedTerm " + createdRelatedTerm);

            subjectAreaRelationship.deleteRelatedTerm(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Soft deleted RelatedTerm with guid=" + guid);
            subjectAreaRelationship.purgeRelatedTerm(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Hard deleted RelatedTerm with guid=" + guid);
        }
        if (term1 != null && term3 != null)
        {
            TermHASARelationship hasaRelationshipASARelationship = new TermHASARelationship();
            hasaRelationshipASARelationship.setDescription("ddd");
            hasaRelationshipASARelationship.setSource("source");
            hasaRelationshipASARelationship.setSteward("Stew");
            hasaRelationshipASARelationship.setOwningTermGuid(term1.getSystemAttributes().getGUID());
            hasaRelationshipASARelationship.setOwnedTermGuid(term3.getSystemAttributes().getGUID());
            TermHASARelationship createdHASATerm = subjectAreaRelationship.createTermHASARelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, hasaRelationshipASARelationship);
            System.out.println("Created TermHASARelationship " + createdHASATerm);
            String guid = createdHASATerm.getGuid();

            subjectAreaRelationship.getTermHASARelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Got TermHASARelationship " + createdHASATerm);
            TermHASARelationship updateHASATerm = new TermHASARelationship();
            updateHASATerm.setDescription("ddd2");
            updateHASATerm.setGuid(createdHASATerm.getGuid());
            TermHASARelationship updatedHASATerm = subjectAreaRelationship.updateTermHASARelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, updateHASATerm);
            if (!updatedHASATerm.getDescription().equals(updateHASATerm.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship update description not as expected", "", "");
            }
            if (!updatedHASATerm.getSource().equals(createdHASATerm.getSource()))
            {
                new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship update source not as expected", "", "");
            }
            if (!updatedHASATerm.getSteward().equals(createdHASATerm.getSteward()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship update steward not as expected", "", "");
            }
            if (!updatedHASATerm.getOwningTermGuid().equals(createdHASATerm.getOwningTermGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship update end 1 not as expected", "", "");
            }
            if (!updatedHASATerm.getOwnedTermGuid().equals(createdHASATerm.getOwnedTermGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship update end 2 not as expected", "", "");
            }
            System.out.println("Updated HASARelationship " + createdHASATerm);
            TermHASARelationship replaceHASATerm = new TermHASARelationship();
            replaceHASATerm.setDescription("ddd3");
            replaceHASATerm.setGuid(createdHASATerm.getGuid());
            TermHASARelationship replacedHASATerm = subjectAreaRelationship.replaceTermHASARelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, replaceHASATerm);
            if (!replacedHASATerm.getDescription().equals(replaceHASATerm.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship replace description not as expected", "", "");
            }
            if (replacedHASATerm.getSource() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship replace source not as expected", "", "");
            }
            if (replacedHASATerm.getSteward() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship replace steward not as expected", "", "");
            }
            if (!replacedHASATerm.getOwningTermGuid().equals(createdHASATerm.getOwningTermGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship replace end 1 not as expected", "", "");
            }
            if (!replacedHASATerm.getOwnedTermGuid().equals(createdHASATerm.getOwnedTermGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship replace end 2 not as expected", "", "");
            }
            System.out.println("Replaced HASARelationship " + createdHASATerm);

            subjectAreaRelationship.deleteTermHASARelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Soft deleted TermHASARelationship with guid=" + guid);
            subjectAreaRelationship.purgeTermHASARelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Hard deleted TermHASARelationship with guid=" + guid);
        }
        if (term1 != null && term2 != null)
        {
            Translation translation = new Translation();
            translation.setDescription("ddd");
            translation.setExpression("Ex");
            translation.setSource("source");
            translation.setSteward("Stew");
            translation.setTranslation1Guid(term1.getSystemAttributes().getGUID());
            translation.setTranslation2Guid(term2.getSystemAttributes().getGUID());
            Translation createdTranslation = subjectAreaRelationship.createTranslationRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, translation);
            System.out.println("Created Translation " + createdTranslation);
            String guid = createdTranslation.getGuid();

            subjectAreaRelationship.getTranslationRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Got Translation " + createdTranslation);

            Translation updateTranslation = new Translation();
            updateTranslation.setDescription("ddd2");
            updateTranslation.setGuid(createdTranslation.getGuid());
            Translation updatedTranslation = subjectAreaRelationship.updateTranslationRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, updateTranslation);
            if (!updatedTranslation.getDescription().equals(updateTranslation.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation update description not as expected", "", "");
            }
            if (!updatedTranslation.getSource().equals(createdTranslation.getSource()))
            {
                new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation update source not as expected", "", "");
            }
            if (!updatedTranslation.getExpression().equals(createdTranslation.getExpression()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation update expression not as expected", "", "");
            }
            if (!updatedTranslation.getSteward().equals(createdTranslation.getSteward()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation update steward not as expected", "", "");
            }
            if (!updatedTranslation.getTranslation1Guid().equals(createdTranslation.getTranslation1Guid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation update end 1 not as expected", "", "");
            }
            if (!updatedTranslation.getTranslation2Guid().equals(createdTranslation.getTranslation2Guid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation update end 2 not as expected", "", "");
            }
            System.out.println("Updated Translation " + createdTranslation);
            Translation replaceTranslation = new Translation();
            replaceTranslation.setDescription("ddd3");
            replaceTranslation.setGuid(createdTranslation.getGuid());
            Translation replacedTranslation = subjectAreaRelationship.replaceTranslationRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, replaceTranslation);
            if (!replacedTranslation.getDescription().equals(replaceTranslation.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation replace description not as expected", "", "");
            }
            if (replacedTranslation.getSource() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation replace source not as expected", "", "");
            }
            if (replacedTranslation.getExpression() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation replace expression not as expected", "", "");
            }
            if (replacedTranslation.getSteward() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation replace steward not as expected", "", "");
            }
            if (!replacedTranslation.getTranslation1Guid().equals(createdTranslation.getTranslation1Guid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation replace end 1 not as expected", "", "");
            }
            if (!replacedTranslation.getTranslation2Guid().equals(createdTranslation.getTranslation2Guid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation replace end 2 not as expected", "", "");
            }
            System.out.println("Replaced Translation " + createdTranslation);
            subjectAreaRelationship.deleteTranslationRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Soft deleted Translation with guid=" + guid);
            subjectAreaRelationship.purgeTranslationRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Hard deleted Translation with guid=" + guid);
        }
        if (term1 != null && term2 != null)
        {
            UsedInContext usedInContext = new UsedInContext();
            usedInContext.setDescription("ddd");
            usedInContext.setExpression("Ex");
            usedInContext.setSource("source");
            usedInContext.setSteward("Stew");
            usedInContext.setContextGuid(term1.getSystemAttributes().getGUID());
            usedInContext.setTermInContextGuid(term2.getSystemAttributes().getGUID());
            UsedInContext createdUsedInContext = subjectAreaRelationship.createUsedInContextRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, usedInContext);
            System.out.println("Created UsedInContext " + createdUsedInContext);
            String guid = createdUsedInContext.getGuid();

            subjectAreaRelationship.getUsedInContextRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Got UsedInContext " + createdUsedInContext);

            UsedInContext updateUsedInContext = new UsedInContext();
            updateUsedInContext.setDescription("ddd2");
            updateUsedInContext.setGuid(createdUsedInContext.getGuid());
            UsedInContext updatedUsedInContext = subjectAreaRelationship.updateUsedInContextRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, updateUsedInContext);
            if (!updatedUsedInContext.getDescription().equals(updateUsedInContext.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext update description not as expected", "", "");
            }
            if (!updatedUsedInContext.getSource().equals(createdUsedInContext.getSource()))
            {
                new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext update source not as expected", "", "");
            }
            if (!updatedUsedInContext.getExpression().equals(createdUsedInContext.getExpression()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext update expression not as expected", "", "");
            }
            if (!updatedUsedInContext.getSteward().equals(createdUsedInContext.getSteward()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext update steward not as expected", "", "");
            }
            if (!updatedUsedInContext.getContextGuid().equals(createdUsedInContext.getContextGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext update end 1 not as expected", "", "");
            }
            if (!updatedUsedInContext.getTermInContextGuid().equals(createdUsedInContext.getTermInContextGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext update end 2 not as expected", "", "");
            }
            System.out.println("Updated UsedInContext " + createdUsedInContext);
            UsedInContext replaceUsedInContext = new UsedInContext();
            replaceUsedInContext.setDescription("ddd3");
            replaceUsedInContext.setGuid(createdUsedInContext.getGuid());
            UsedInContext replacedUsedInContext = subjectAreaRelationship.replaceUsedInContextRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, replaceUsedInContext);
            if (!replacedUsedInContext.getDescription().equals(replaceUsedInContext.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext replace description not as expected", "", "");
            }
            if (replacedUsedInContext.getSource() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext replace source not as expected", "", "");
            }
            if (replacedUsedInContext.getExpression() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext replace expression not as expected", "", "");
            }
            if (replacedUsedInContext.getSteward() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext replace steward not as expected", "", "");
            }
            if (!replacedUsedInContext.getContextGuid().equals(createdUsedInContext.getContextGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext replace end 1 not as expected", "", "");
            }
            if (!replacedUsedInContext.getTermInContextGuid().equals(createdUsedInContext.getTermInContextGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext replace end 2 not as expected", "", "");
            }
            System.out.println("Replaced UsedInContext " + createdUsedInContext);
            subjectAreaRelationship.deleteUsedInContextRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Soft deleted UsedInContext with guid=" + guid);
            subjectAreaRelationship.purgeUsedInContextRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Hard deleted UsedInContext with guid=" + guid);
        }
        if (term1 != null && term2 != null)
        {
            PreferredTerm preferredTerm = new PreferredTerm();
            preferredTerm.setDescription("ddd");
            preferredTerm.setExpression("Ex");
            preferredTerm.setSource("source");
            preferredTerm.setSteward("Stew");
            preferredTerm.setAlternateTermGuid(term1.getSystemAttributes().getGUID());
            preferredTerm.setPreferredTermGuid(term2.getSystemAttributes().getGUID());
            PreferredTerm createdPreferredTerm = subjectAreaRelationship.createPreferredTermRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, preferredTerm);
            System.out.println("Created PreferredTerm " + createdPreferredTerm);
            String guid = createdPreferredTerm.getGuid();

            subjectAreaRelationship.getPreferredTermRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Got PreferredTerm " + createdPreferredTerm);

            PreferredTerm updatePreferredTerm = new PreferredTerm();
            updatePreferredTerm.setDescription("ddd2");
            updatePreferredTerm.setGuid(createdPreferredTerm.getGuid());
            PreferredTerm updatedPreferredTerm = subjectAreaRelationship.updatePreferredTermRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, updatePreferredTerm);
            if (!updatedPreferredTerm.getDescription().equals(updatePreferredTerm.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm update description not as expected", "", "");
            }
            if (!updatedPreferredTerm.getSource().equals(createdPreferredTerm.getSource()))
            {
                new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm update source not as expected", "", "");
            }
            if (!updatedPreferredTerm.getExpression().equals(createdPreferredTerm.getExpression()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm update expression not as expected", "", "");
            }
            if (!updatedPreferredTerm.getSteward().equals(createdPreferredTerm.getSteward()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm update steward not as expected", "", "");
            }
            if (!updatedPreferredTerm.getAlternateTermGuid().equals(createdPreferredTerm.getAlternateTermGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm update end 1 not as expected", "", "");
            }
            if (!updatedPreferredTerm.getPreferredTermGuid().equals(createdPreferredTerm.getPreferredTermGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm update end 2 not as expected", "", "");
            }
            System.out.println("Updated PreferredTerm " + createdPreferredTerm);
            PreferredTerm replacePreferredTerm = new PreferredTerm();
            replacePreferredTerm.setDescription("ddd3");
            replacePreferredTerm.setGuid(createdPreferredTerm.getGuid());
            PreferredTerm replacedPreferredTerm = subjectAreaRelationship.replacePreferredTermRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, replacePreferredTerm);
            if (!replacedPreferredTerm.getDescription().equals(replacePreferredTerm.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm replace description not as expected", "", "");
            }
            if (replacedPreferredTerm.getSource() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm replace source not as expected", "", "");
            }
            if (replacedPreferredTerm.getExpression() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm replace expression not as expected", "", "");
            }
            if (replacedPreferredTerm.getSteward() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm replace steward not as expected", "", "");
            }
            if (!replacedPreferredTerm.getAlternateTermGuid().equals(createdPreferredTerm.getAlternateTermGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm replace end 1 not as expected", "", "");
            }
            if (!replacedPreferredTerm.getPreferredTermGuid().equals(createdPreferredTerm.getPreferredTermGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm replace end 2 not as expected", "", "");
            }
            System.out.println("Replaced PreferredTerm " + createdPreferredTerm);
            subjectAreaRelationship.deletePreferredTermRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Soft deleted PreferredTerm with guid=" + guid);
            subjectAreaRelationship.purgePreferredTermRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Hard deleted PreferredTerm with guid=" + guid);
        }
        if (term1 != null && term2 != null)
        {
            ValidValue validValue = new ValidValue();
            validValue.setDescription("ddd");
            validValue.setExpression("Ex");
            validValue.setSource("source");
            validValue.setSteward("Stew");
            validValue.setTermGuid(term1.getSystemAttributes().getGUID());
            validValue.setValidValueGuid(term2.getSystemAttributes().getGUID());
            ValidValue createdValidValue = subjectAreaRelationship.createValidValueRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, validValue);
            System.out.println("Created ValidValue " + createdValidValue);
            String guid = createdValidValue.getGuid();

            subjectAreaRelationship.getValidValueRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Got ValidValue " + createdValidValue);

            ValidValue updateValidValue = new ValidValue();
            updateValidValue.setDescription("ddd2");
            updateValidValue.setGuid(createdValidValue.getGuid());
            ValidValue updatedValidValue = subjectAreaRelationship.updateValidValueRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, updateValidValue);
            if (!updatedValidValue.getDescription().equals(updateValidValue.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue update description not as expected", "", "");
            }
            if (!updatedValidValue.getSource().equals(createdValidValue.getSource()))
            {
                new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue update source not as expected", "", "");
            }
            if (!updatedValidValue.getExpression().equals(createdValidValue.getExpression()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue update expression not as expected", "", "");
            }
            if (!updatedValidValue.getSteward().equals(createdValidValue.getSteward()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue update steward not as expected", "", "");
            }
            if (!updatedValidValue.getValidValueGuid().equals(createdValidValue.getValidValueGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue update end 1 not as expected", "", "");
            }
            if (!updatedValidValue.getTermGuid().equals(createdValidValue.getTermGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue update end 2 not as expected", "", "");
            }
            System.out.println("Updated ValidValue " + createdValidValue);
            ValidValue replaceValidValue = new ValidValue();
            replaceValidValue.setDescription("ddd3");
            replaceValidValue.setGuid(createdValidValue.getGuid());
            ValidValue replacedValidValue = subjectAreaRelationship.replaceValidValueRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, replaceValidValue);
            if (!replacedValidValue.getDescription().equals(replaceValidValue.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue replace description not as expected", "", "");
            }
            if (replacedValidValue.getSource() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue replace source not as expected", "", "");
            }
            if (replacedValidValue.getExpression() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue replace expression not as expected", "", "");
            }
            if (replacedValidValue.getSteward() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue replace steward not as expected", "", "");
            }
            if (!replacedValidValue.getValidValueGuid().equals(createdValidValue.getValidValueGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue replace end 1 not as expected", "", "");
            }
            if (!replacedValidValue.getTermGuid().equals(createdValidValue.getTermGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue replace end 2 not as expected", "", "");
            }
            System.out.println("Replaced ValidValue " + createdValidValue);
            subjectAreaRelationship.deleteValidValueRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Soft deleted ValidValue with guid=" + guid);
            subjectAreaRelationship.purgeValidValueRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Hard deleted ValidValue with guid=" + guid);
        }
        if (term1 != null && term2 != null)
        {
            ReplacementTerm replacementTerm = new ReplacementTerm();
            replacementTerm.setDescription("ddd");
            replacementTerm.setExpression("Ex");
            replacementTerm.setSource("source");
            replacementTerm.setSteward("Stew");
            replacementTerm.setReplacedTermGuid(term1.getSystemAttributes().getGUID());
            replacementTerm.setReplacementTermGuid(term2.getSystemAttributes().getGUID());
            ReplacementTerm createdReplacementTerm = subjectAreaRelationship.createReplacementTermRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, replacementTerm);
            System.out.println("Created ReplacementTerm " + createdReplacementTerm);
            String guid = createdReplacementTerm.getGuid();

            subjectAreaRelationship.getReplacementTermRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Got ReplacementTerm " + createdReplacementTerm);

            ReplacementTerm updateReplacementTerm = new ReplacementTerm();
            updateReplacementTerm.setDescription("ddd2");
            updateReplacementTerm.setGuid(createdReplacementTerm.getGuid());
            ReplacementTerm updatedReplacementTerm = subjectAreaRelationship.updateReplacementTermRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, updateReplacementTerm);
            if (!updatedReplacementTerm.getDescription().equals(updateReplacementTerm.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm update description not as expected", "", "");
            }
            if (!updatedReplacementTerm.getSource().equals(createdReplacementTerm.getSource()))
            {
                new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm update source not as expected", "", "");
            }
            if (!updatedReplacementTerm.getExpression().equals(createdReplacementTerm.getExpression()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm update expression not as expected", "", "");
            }
            if (!updatedReplacementTerm.getSteward().equals(createdReplacementTerm.getSteward()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm update steward not as expected", "", "");
            }
            if (!updatedReplacementTerm.getReplacedTermGuid().equals(createdReplacementTerm.getReplacedTermGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm update end 1 not as expected", "", "");
            }
            if (!updatedReplacementTerm.getReplacementTermGuid().equals(createdReplacementTerm.getReplacementTermGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm update end 2 not as expected", "", "");
            }
            System.out.println("Updated ReplacementTerm " + createdReplacementTerm);
            ReplacementTerm replaceReplacementTerm = new ReplacementTerm();
            replaceReplacementTerm.setDescription("ddd3");
            replaceReplacementTerm.setGuid(createdReplacementTerm.getGuid());
            ReplacementTerm replacedReplacementTerm = subjectAreaRelationship.replaceReplacementTermRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, replaceReplacementTerm);
            if (!replacedReplacementTerm.getDescription().equals(replaceReplacementTerm.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm replace description not as expected", "", "");
            }
            if (replacedReplacementTerm.getSource() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm replace source not as expected", "", "");
            }
            if (replacedReplacementTerm.getExpression() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm replace expression not as expected", "", "");
            }
            if (replacedReplacementTerm.getSteward() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm replace steward not as expected", "", "");
            }
            if (!replacedReplacementTerm.getReplacedTermGuid().equals(createdReplacementTerm.getReplacedTermGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm replace end 1 not as expected", "", "");
            }
            if (!replacedReplacementTerm.getReplacementTermGuid().equals(createdReplacementTerm.getReplacementTermGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm replace end 2 not as expected", "", "");
            }
            System.out.println("Replaced ReplacementTerm " + createdReplacementTerm);
            subjectAreaRelationship.deleteReplacementTermRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Soft deleted ReplacementTerm with guid=" + guid);
            subjectAreaRelationship.purgeReplacementTermRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Hard deleted ReplacementTerm with guid=" + guid);
        }
        if (term1 != null && term2 != null)
        {
            TermTYPEDBYRelationship termTYPEDBYRelationship = new TermTYPEDBYRelationship();
            termTYPEDBYRelationship.setDescription("ddd");
            termTYPEDBYRelationship.setSource("source");
            termTYPEDBYRelationship.setSteward("Stew");
            termTYPEDBYRelationship.setAttributeGuid(term1.getSystemAttributes().getGUID());
            termTYPEDBYRelationship.setTypeGuid(term2.getSystemAttributes().getGUID());
            TermTYPEDBYRelationship createdTermTYPEDBYRelationship = subjectAreaRelationship.createTermTYPEDBYRelationshipRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, termTYPEDBYRelationship);
            System.out.println("Created TermTYPEDBYRelationship " + createdTermTYPEDBYRelationship);
            String guid = createdTermTYPEDBYRelationship.getGuid();

            subjectAreaRelationship.getTermTYPEDBYRelationshipRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Got TermTYPEDBYRelationship " + createdTermTYPEDBYRelationship);

            TermTYPEDBYRelationship updateTermTYPEDBYRelationship = new TermTYPEDBYRelationship();
            updateTermTYPEDBYRelationship.setDescription("ddd2");
            updateTermTYPEDBYRelationship.setGuid(createdTermTYPEDBYRelationship.getGuid());
            TermTYPEDBYRelationship updatedTermTYPEDBYRelationship = subjectAreaRelationship.updateTermTYPEDBYRelationshipRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, updateTermTYPEDBYRelationship);
            if (!updatedTermTYPEDBYRelationship.getDescription().equals(updateTermTYPEDBYRelationship.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship update description not as expected", "", "");
            }
            if (!updatedTermTYPEDBYRelationship.getSource().equals(createdTermTYPEDBYRelationship.getSource()))
            {
                new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship update source not as expected", "", "");
            }
            if (!updatedTermTYPEDBYRelationship.getSteward().equals(createdTermTYPEDBYRelationship.getSteward()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship update steward not as expected", "", "");
            }
            if (!updatedTermTYPEDBYRelationship.getAttributeGuid().equals(createdTermTYPEDBYRelationship.getAttributeGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship update end 1 not as expected", "", "");
            }
            if (!updatedTermTYPEDBYRelationship.getTypeGuid().equals(createdTermTYPEDBYRelationship.getTypeGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship update end 2 not as expected", "", "");
            }
            System.out.println("Updated TermTYPEDBYRelationship " + createdTermTYPEDBYRelationship);
            TermTYPEDBYRelationship replaceTermTYPEDBYRelationship = new TermTYPEDBYRelationship();
            replaceTermTYPEDBYRelationship.setDescription("ddd3");
            replaceTermTYPEDBYRelationship.setGuid(createdTermTYPEDBYRelationship.getGuid());
            TermTYPEDBYRelationship replacedTermTYPEDBYRelationship = subjectAreaRelationship.replaceTermTYPEDBYRelationshipRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, replaceTermTYPEDBYRelationship);
            if (!replacedTermTYPEDBYRelationship.getDescription().equals(replaceTermTYPEDBYRelationship.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship replace description not as expected", "", "");
            }
            if (replacedTermTYPEDBYRelationship.getSource() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship replace source not as expected", "", "");
            }
            if (replacedTermTYPEDBYRelationship.getSteward() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship replace steward not as expected", "", "");
            }
            if (!replacedTermTYPEDBYRelationship.getAttributeGuid().equals(createdTermTYPEDBYRelationship.getAttributeGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship replace end 1 not as expected", "", "");
            }
            if (!replacedTermTYPEDBYRelationship.getTypeGuid().equals(createdTermTYPEDBYRelationship.getTypeGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship replace end 2 not as expected", "", "");
            }
            System.out.println("Replaced TermTYPEDBYRelationship " + createdTermTYPEDBYRelationship);
            subjectAreaRelationship.deleteTermTYPEDBYRelationshipRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Soft deleted TermTYPEDBYRelationship with guid=" + guid);
            subjectAreaRelationship.purgeTermTYPEDBYRelationshipRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Hard deleted TermTYPEDBYRelationship with guid=" + guid);
        }
        if (term1 != null && term2 != null)
        {
            ISARelationship isa = new ISARelationship();
            isa.setDescription("ddd");
            isa.setExpression("Ex");
            isa.setSource("source");
            isa.setSteward("Stew");
            isa.setSpecialisedTermGuid(term1.getSystemAttributes().getGUID());
            isa.setTermGuid(term2.getSystemAttributes().getGUID());
            ISARelationship createdIsa = subjectAreaRelationship.createIsaRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, isa);
            System.out.println("Created Isa " + createdIsa);
            String guid = createdIsa.getGuid();

            subjectAreaRelationship.getIsaRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Got Isa " + createdIsa);

            ISARelationship updateIsa = new ISARelationship();
            updateIsa.setDescription("ddd2");
            updateIsa.setGuid(createdIsa.getGuid());
            ISARelationship updatedIsa = subjectAreaRelationship.updateIsaRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, updateIsa);
            if (!updatedIsa.getDescription().equals(updateIsa.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa update description not as expected", "", "");
            }
            if (!updatedIsa.getSource().equals(createdIsa.getSource()))
            {
                new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa update source not as expected", "", "");
            }
            if (!updatedIsa.getExpression().equals(createdIsa.getExpression()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa update expression not as expected", "", "");
            }
            if (!updatedIsa.getSteward().equals(createdIsa.getSteward()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa update steward not as expected", "", "");
            }
            if (!updatedIsa.getTermGuid().equals(createdIsa.getTermGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa update end 1 not as expected", "", "");
            }
            if (!updatedIsa.getSpecialisedTermGuid().equals(createdIsa.getSpecialisedTermGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa update end 2 not as expected", "", "");
            }
            System.out.println("Updated Isa " + createdIsa);
            ISARelationship replaceIsa = new ISARelationship();
            replaceIsa.setDescription("ddd3");
            replaceIsa.setGuid(createdIsa.getGuid());
            ISARelationship replacedIsa = subjectAreaRelationship.replaceIsaRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, replaceIsa);
            if (!replacedIsa.getDescription().equals(replaceIsa.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa replace description not as expected", "", "");
            }
            if (replacedIsa.getSource() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa replace source not as expected", "", "");
            }
            if (replacedIsa.getExpression() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa replace expression not as expected", "", "");
            }
            if (replacedIsa.getSteward() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa replace steward not as expected", "", "");
            }
            if (!replacedIsa.getSpecialisedTermGuid().equals(createdIsa.getSpecialisedTermGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa replace end 1 not as expected", "", "");
            }
            if (!replacedIsa.getTermGuid().equals(createdIsa.getTermGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa replace end 2 not as expected", "", "");
            }
            System.out.println("Replaced Isa " + createdIsa);
            subjectAreaRelationship.deleteIsaRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Soft deleted Isa with guid=" + guid);
            subjectAreaRelationship.purgeIsaRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Hard deleted Isa with guid=" + guid);
        }
        if (term1 != null && term2 != null)
        {
            TermISATypeOFRelationship TermISATypeOFRelationship = new TermISATypeOFRelationship();
            TermISATypeOFRelationship.setDescription("ddd");
            TermISATypeOFRelationship.setSource("source");
            TermISATypeOFRelationship.setSteward("Stew");
            TermISATypeOFRelationship.setSubTypeGuid(term1.getSystemAttributes().getGUID());
            TermISATypeOFRelationship.setSuperTypeGuid(term2.getSystemAttributes().getGUID());
            TermISATypeOFRelationship createdTermISATypeOFRelationship = subjectAreaRelationship.createTermISATypeOFRelationshipRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, TermISATypeOFRelationship);
            System.out.println("Created TermISATypeOFRelationship " + createdTermISATypeOFRelationship);
            String guid = createdTermISATypeOFRelationship.getGuid();

            subjectAreaRelationship.getTermISATypeOFRelationshipRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Got TermISATypeOFRelationship " + createdTermISATypeOFRelationship);

            TermISATypeOFRelationship updateTermISATypeOFRelationship = new TermISATypeOFRelationship();
            updateTermISATypeOFRelationship.setDescription("ddd2");
            updateTermISATypeOFRelationship.setGuid(createdTermISATypeOFRelationship.getGuid());
            TermISATypeOFRelationship updatedTermISATypeOFRelationship = subjectAreaRelationship.updateTermISATypeOFRelationshipRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, updateTermISATypeOFRelationship);
            if (!updatedTermISATypeOFRelationship.getDescription().equals(updateTermISATypeOFRelationship.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship update description not as expected", "", "");
            }
            if (!updatedTermISATypeOFRelationship.getSource().equals(createdTermISATypeOFRelationship.getSource()))
            {
                new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship update source not as expected", "", "");
            }
                      if (!updatedTermISATypeOFRelationship.getSteward().equals(createdTermISATypeOFRelationship.getSteward()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship update steward not as expected", "", "");
            }
            if (!updatedTermISATypeOFRelationship.getSubTypeGuid().equals(createdTermISATypeOFRelationship.getSubTypeGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship update end 1 not as expected", "", "");
            }
            if (!updatedTermISATypeOFRelationship.getSuperTypeGuid().equals(createdTermISATypeOFRelationship.getSuperTypeGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship update end 2 not as expected", "", "");
            }
            System.out.println("Updated TermISATypeOFRelationship " + createdTermISATypeOFRelationship);
            TermISATypeOFRelationship replaceTermISATypeOFRelationship = new TermISATypeOFRelationship();
            replaceTermISATypeOFRelationship.setDescription("ddd3");
            replaceTermISATypeOFRelationship.setGuid(createdTermISATypeOFRelationship.getGuid());
            TermISATypeOFRelationship replacedTermISATypeOFRelationship = subjectAreaRelationship.replaceTermISATypeOFRelationshipRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, replaceTermISATypeOFRelationship);
            if (!replacedTermISATypeOFRelationship.getDescription().equals(replaceTermISATypeOFRelationship.getDescription()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship replace description not as expected", "", "");
            }
            if (replacedTermISATypeOFRelationship.getSource() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship replace source not as expected", "", "");
            }
                       if (replacedTermISATypeOFRelationship.getSteward() != null)
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship replace steward not as expected", "", "");
            }
            if (!replacedTermISATypeOFRelationship.getSuperTypeGuid().equals(createdTermISATypeOFRelationship.getSuperTypeGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship replace end 1 not as expected", "", "");
            }
            if (!replacedTermISATypeOFRelationship.getSubTypeGuid().equals(createdTermISATypeOFRelationship.getSubTypeGuid()))
            {
                throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship replace end 2 not as expected", "", "");
            }
            System.out.println("Replaced TermISATypeOFRelationship " + createdTermISATypeOFRelationship);
            subjectAreaRelationship.deleteTermISATypeOFRelationshipRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Soft deleted TermISATypeOFRelationship with guid=" + guid);
            subjectAreaRelationship.purgeTermISATypeOFRelationshipRelationship(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
            System.out.println("Hard deleted TermISATypeOFRelationship with guid=" + guid);
        }
    }
}
