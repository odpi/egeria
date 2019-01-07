/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaTerm;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;

import java.io.IOException;
import java.util.Date;

/**
 * FVT resource to call subject area term client API
 */
public class TermFVT
{
    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Test Glossary for term FVT";
    private static final String DEFAULT_TEST_TERM_NAME = "Test term A";
    private static final String DEFAULT_TEST_TERM_NAME_UPDATED = "Test term A updated";
    private SubjectAreaTerm subjectAreaTerm = null;
    private GlossaryFVT glossaryFVT =null;
    private String serverName = null;

    public static void main(String args[])
    {
        try
        {
            String url = RunAllFVT.getUrl(args);
            runit(url);
        } catch (IOException e1)
        {
            System.out.println("Error getting user input");
        } catch (SubjectAreaCheckedExceptionBase e)
        {
            System.out.println("ERROR: " + e.getErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        }

    }
    public TermFVT(String url,String serverName) throws InvalidParameterException
    {
        subjectAreaTerm = new SubjectAreaImpl(serverName,url).getSubjectAreaTerm();
        System.out.println("Create a glossary");
        glossaryFVT = new GlossaryFVT(url,serverName);
        this.serverName=serverName;
    }
    public static void runit(String url) throws SubjectAreaCheckedExceptionBase
    {
        TermFVT fvt =new TermFVT(url,FVTConstants.SERVER_NAME1);
        fvt.run();
        TermFVT fvt2 =new TermFVT(url,FVTConstants.SERVER_NAME2);
        fvt2.run();
    }

    public void run() throws SubjectAreaCheckedExceptionBase
    {
        Glossary glossary= glossaryFVT.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
        System.out.println("Create a term1 using glossary name");
        Term term1 = createTermWithGlossaryGuid(DEFAULT_TEST_TERM_NAME, glossary.getSystemAttributes().getGUID());
        FVTUtils.validateNode(term1);
        System.out.println("Create a term2 using glossary guid");
        Term term2 = createTermWithGlossaryGuid(DEFAULT_TEST_TERM_NAME, glossary.getSystemAttributes().getGUID());
        FVTUtils.validateNode(term2);
        Term termForUpdate = new Term();
        termForUpdate.setName(DEFAULT_TEST_TERM_NAME_UPDATED);
        System.out.println("Get the term1");
        String guid = term1.getSystemAttributes().getGUID();
        Term gotTerm = getTermByGUID(guid);
        FVTUtils.validateNode(gotTerm);
        System.out.println("Update the term1");
        Term updatedTerm = updateTerm(guid, termForUpdate);
        FVTUtils.validateNode(updatedTerm);
        System.out.println("Get the term1 again");
        gotTerm = getTermByGUID(guid);
        FVTUtils.validateNode(gotTerm);
        System.out.println("Delete the term1");
        gotTerm = deleteTerm(guid);
        FVTUtils.validateNode(gotTerm);
        System.out.println("Purge a term1");
        purgeTerm(guid);
    }

    public  Term createTermWithGlossaryGuid(String termName, String glossaryGuid) throws SubjectAreaCheckedExceptionBase
    {
        Term term = new Term();
        term.setName(termName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        term.setGlossary(glossarySummary);
        Term newTerm = subjectAreaTerm.createTerm(serverName,FVTConstants.USERID, term);
        if (newTerm != null)
        {
            System.out.println("Created Term " + newTerm.getName() + " with guid " + newTerm.getSystemAttributes().getGUID());
        }
        return newTerm;
    }


    public Term getTermByGUID(String guid) throws SubjectAreaCheckedExceptionBase
    {
        Term term = subjectAreaTerm.getTermByGuid(serverName,FVTConstants.USERID, guid);
        if (term != null)
        {
            System.out.println("Got Term " + term.getName() + " with guid " + term.getSystemAttributes().getGUID() + " and status " + term.getSystemAttributes().getStatus());
        }
        return term;
    }

    public Term updateTerm(String guid, Term term) throws SubjectAreaCheckedExceptionBase
    {
        Term updatedTerm = subjectAreaTerm.updateTerm(serverName,FVTConstants.USERID, guid, term);
        if (updatedTerm != null)
        {
            System.out.println("Updated Term name to " + updatedTerm.getName());
        }
        return updatedTerm;
    }
    public Term updateTermToFuture(String guid, Term term) throws SubjectAreaCheckedExceptionBase
    {
        long now = new Date().getTime();

       term.setEffectiveFromTime(new Date(now+6*1000*60*60*24));
       term.setEffectiveToTime(new Date(now+7*1000*60*60*24));

        Term updatedTerm = subjectAreaTerm.updateTerm(serverName,FVTConstants.USERID, guid, term);
        if (updatedTerm != null)
        {
            System.out.println("Updated Term name to " + updatedTerm.getName());
        }
        return updatedTerm;
    }

    public Term deleteTerm(String guid) throws SubjectAreaCheckedExceptionBase
    {
        Term deletedTerm = subjectAreaTerm.deleteTerm(serverName,FVTConstants.USERID, guid);
        if (deletedTerm != null)
        {
            System.out.println("Deleted Term name is " + deletedTerm.getName());
        }
        return deletedTerm;
    }

    public void purgeTerm(String guid) throws SubjectAreaCheckedExceptionBase
    {
        subjectAreaTerm.purgeTerm(serverName,FVTConstants.USERID, guid);
        System.out.println("Purge succeeded");
    }
}
