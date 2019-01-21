/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaTerm;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;

import java.io.IOException;

/**
 * FVT resource to call subject area client APIs to test the effectivity dates
 */
public class EffectiveDatesFVT
{

    private static final String DEFAULT_TEST_PAST_GLOSSARY_NAME = "Test past Glossary for term FVT";
    private static final String DEFAULT_TEST_FUTURE_GLOSSARY_NAME = "Test future Glossary for term FVT";
    private static final String DEFAULT_TEST_TERM_NAME = "Test term A";
    private SubjectAreaTerm subjectAreaTerm = null;
    private GlossaryFVT glossaryFVT =null;
    private TermFVT termFVT=null;
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
    public EffectiveDatesFVT(String url, String serverName) throws InvalidParameterException
    {
        subjectAreaTerm = new SubjectAreaImpl(serverName,url).getSubjectAreaTerm();
        System.out.println("Create a glossary");
        glossaryFVT = new GlossaryFVT(url,serverName);
        termFVT= new TermFVT(url,serverName);
        this.serverName=serverName;
    }
    public static void runit(String url) throws SubjectAreaCheckedExceptionBase
    {
        EffectiveDatesFVT fvt =new EffectiveDatesFVT(url,FVTConstants.SERVER_NAME1);
        fvt.run();
        EffectiveDatesFVT fvt2 =new EffectiveDatesFVT(url,FVTConstants.SERVER_NAME2);
        fvt2.run();
    }

    public void run() throws SubjectAreaCheckedExceptionBase
    {
        try
        {
            Glossary pastgloss = glossaryFVT.createPastToGlossary(DEFAULT_TEST_PAST_GLOSSARY_NAME);
            FVTUtils.validateNode(pastgloss);
        } catch (InvalidParameterException e) {
            System.out.println("Expected creation of a Glossary with to in the past failed");
        }
        try
        {
            Glossary pastgloss =glossaryFVT.createPastFromGlossary(DEFAULT_TEST_PAST_GLOSSARY_NAME);
            FVTUtils.validateNode(pastgloss);
        } catch (InvalidParameterException e) {
            System.out.println("Expected creation of a Glossary with from in the past failed");
        }
        try
        {
           glossaryFVT.createInvalidEffectiveDateGlossary(DEFAULT_TEST_PAST_GLOSSARY_NAME);
        } catch (InvalidParameterException e) {
            System.out.println("Expected creation of a Glossary with invalid Effectivity dates failed");
        }
        Glossary futureGloss =glossaryFVT.createFutureGlossary(DEFAULT_TEST_FUTURE_GLOSSARY_NAME);
        FVTUtils.validateNode(futureGloss);
        Term term5 =termFVT.createTerm(DEFAULT_TEST_TERM_NAME, futureGloss.getSystemAttributes().getGUID());
        FVTUtils.validateNode(term5);
        if (term5.getGlossary()==null) {
            // error
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Term expected an associated future Glossary,  ", "", "");
        }

        Term gotTerm5 = termFVT.getTermByGUID(term5.getSystemAttributes().getGUID());
        FVTUtils.validateNode(gotTerm5);
        if (gotTerm5.getGlossary()==null) {
            // error
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Term expected an associated future Glossary,  ", "", "");
        }
        // update the term so that its effective dates not longer are compatible with the glossary
        Term futureTerm = termFVT.updateTermToFuture(gotTerm5.getSystemAttributes().getGUID(),term5);
        FVTUtils.validateNode(futureTerm);
        if (futureTerm.getGlossary()!=null) {
            // error
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Term expected associated future Glossary,  ", "", "");
        }
        futureTerm = termFVT.getTermByGUID(term5.getSystemAttributes().getGUID());
        FVTUtils.validateNode(futureTerm);
        if (futureTerm.getGlossary()==null) {
            // error
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Term expected no associated future Glossary,  ", "", "");
        }
    }

}
