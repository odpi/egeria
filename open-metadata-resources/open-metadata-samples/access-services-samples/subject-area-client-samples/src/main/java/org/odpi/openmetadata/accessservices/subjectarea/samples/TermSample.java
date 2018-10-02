/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.samples;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaTerm;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Sample to call subject area term client API
 */
public class TermSample {
    private static final String USERID = " Fred";

    private static final String DEFAULT_TEST_GLOSSARY_NAME  = "Test Glossary for term sample";
    private static final String DEFAULT_TEST_TERM_NAME  = "Test term A";
    private static final String DEFAULT_TEST_TERM_NAME_UPDATED = "Test term A updated";
    private static SubjectAreaTerm subjectAreaTerm = null;

    public static void main(String args[]) {
        SubjectArea subjectArea = null;
        try {
            String url =RunAllSamples.getUrl(args);
            initialiseTermSample(url);
            System.out.println("Create a glossary");
            GlossarySample.initialiseGlossarySample(url);
            Glossary glossary = GlossarySample.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
            System.out.println("Create a term1 using glossary name");
            Term term1 = TermSample.createTermWithGlossaryName(DEFAULT_TEST_TERM_NAME, DEFAULT_TEST_GLOSSARY_NAME);
            System.out.println("Create a term2 using glossary guid");
            Term term2 = TermSample.createTermWithGlossaryGuid(DEFAULT_TEST_TERM_NAME, glossary.getSystemAttributes().getGUID());

            Term termForUpdate = new Term();
            termForUpdate.setName(DEFAULT_TEST_TERM_NAME_UPDATED);

            if (term1 != null) {
                System.out.println("Get the term1");
                String guid = term1.getSystemAttributes().getGUID();
                Term gotTerm = getTermByGUID( guid);
                System.out.println("Update the term1");
                Term updatedTerm = updateTerm( guid, termForUpdate);
                System.out.println("Get the term1 again");
                gotTerm = getTermByGUID( guid);
                System.out.println("Delete the term1");
                gotTerm = deleteTerm( guid);
                System.out.println("Purge a term1");
            }
        } catch (SubjectAreaCheckedExceptionBase e) {
            System.out.println("ERROR: " +e.getErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        } catch (IOException e) {
            System.out.println("Error getting user input");
        }
    }

    public static Term createTermWithGlossaryGuid(String termName,String glossaryGuid) throws SubjectAreaCheckedExceptionBase {
        Term term = new Term();
        term.setName(termName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        term.setGlossary(glossarySummary);
        Term newTerm = subjectAreaTerm.createTerm(USERID, term);
        if (newTerm != null) {
            System.out.println("Created Term " + newTerm.getName() + " with guid " + newTerm.getSystemAttributes().getGUID());
        }
        return newTerm;
    }

    public static Term createTermWithGlossaryName(String termName,String glossaryName) throws SubjectAreaCheckedExceptionBase {
        Term term = new Term();
        term.setName(termName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setName(glossaryName);
        term.setGlossary(glossarySummary);
        Term newTerm = subjectAreaTerm.createTerm(USERID, term);
        if (newTerm != null) {
            System.out.println("Created Term " + newTerm.getName() + " with guid " + newTerm.getSystemAttributes().getGUID());
        }
        return newTerm;
    }

    public static Term getTermByGUID(String guid) throws SubjectAreaCheckedExceptionBase {
        Term term = subjectAreaTerm.getTermByGuid(USERID, guid);
        if (term != null) {
            System.out.println("Got Term " + term.getName() + " with guid " + term.getSystemAttributes().getGUID() + " and status " + term.getSystemAttributes().getStatus());
        }
        return term;
    }

    public static Term updateTerm(String guid, Term term) throws SubjectAreaCheckedExceptionBase {
        Term updatedTerm = subjectAreaTerm.updateTerm(USERID, guid, term);
        if (updatedTerm != null) {
            System.out.println("Updated Term name to " + updatedTerm.getName());
        }
        return updatedTerm;
    }

    public static Term deleteTerm(String guid) throws SubjectAreaCheckedExceptionBase {
        Term deletedTerm = subjectAreaTerm.deleteTerm(USERID, guid);
        if (deletedTerm != null) {
            System.out.println("Deleted Term name is " + deletedTerm.getName());
        }
        return deletedTerm;
    }
    public static void purgeTerm(String guid) throws SubjectAreaCheckedExceptionBase {
        subjectAreaTerm.purgeTerm(USERID, guid);
        System.out.println("Purge succeeded");
    }
    /**
     * Call this to initialise the glossary sample
     * @param url
     * @throws InvalidParameterException
     */
    public static void initialiseTermSample(String url) throws InvalidParameterException {
        subjectAreaTerm = new SubjectAreaImpl(url).getSubjectAreaTerm();
    }
}
