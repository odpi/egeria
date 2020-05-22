/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaTerm;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
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
    private String userId = null;

    public static void main(String args[])
    {
        try
        {
            String url = RunAllFVT.getUrl(args);
            runWith2Servers(url);
        } catch (IOException e1)
        {
            System.out.println("Error getting user input");
        } catch (SubjectAreaCheckedException e)
        {
            System.out.println("ERROR: " + e.getErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        } catch (SubjectAreaFVTCheckedException e) {
            System.out.println("ERROR: " + e.getMessage() );
        }

    }
    public EffectiveDatesFVT(String url, String serverName,String userId) throws SubjectAreaCheckedException
    {
        subjectAreaTerm = new SubjectAreaImpl(serverName,url).getSubjectAreaTerm();
        System.out.println("Create a glossary");
        glossaryFVT = new GlossaryFVT(url,serverName,userId);
        termFVT= new TermFVT(url,serverName,userId);
        this.serverName=serverName;
        this.userId=userId;
    }
    public static void runWith2Servers(String url) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException
    {
        EffectiveDatesFVT fvt =new EffectiveDatesFVT(url,FVTConstants.SERVER_NAME1,FVTConstants.USERID);
        fvt.run();
        EffectiveDatesFVT fvt2 =new EffectiveDatesFVT(url,FVTConstants.SERVER_NAME2,FVTConstants.USERID);
        fvt2.run();
    }

    public void run() throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException
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
            throw new SubjectAreaFVTCheckedException("ERROR: Term with no effectivity constraints expected an associated future Glossary");
        }

        Term gotTerm5 = termFVT.getTermByGUID(term5.getSystemAttributes().getGUID());
        FVTUtils.validateNode(gotTerm5);
        if (gotTerm5.getGlossary()==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Term with no effectivity constraints expected an associated future Glossary");
        }
        // update the term so that its effective dates not longer are compatible with the glossary
        Term futureTerm = termFVT.updateTermToFuture(gotTerm5.getSystemAttributes().getGUID(),term5);
        FVTUtils.validateNode(futureTerm);
        if (futureTerm.getGlossary()!=null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Term expected associated future Glossary");
        }
        futureTerm = termFVT.getTermByGUID(term5.getSystemAttributes().getGUID());
        FVTUtils.validateNode(futureTerm);
        if (futureTerm.getGlossary()==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Term expected no associated future Glossary");
        }
    }
}