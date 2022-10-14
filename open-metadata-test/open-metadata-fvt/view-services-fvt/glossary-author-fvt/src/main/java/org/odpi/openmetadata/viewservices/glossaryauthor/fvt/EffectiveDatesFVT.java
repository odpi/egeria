/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * FVT resource to call Glossary Author View APIs to test the effectivity dates
 */
public class EffectiveDatesFVT
{

    private static final String DEFAULT_TEST_PAST_GLOSSARY_NAME = "Test past Glossary for term FVT";
    private static final String DEFAULT_TEST_FUTURE_GLOSSARY_NAME = "Test future Glossary for term FVT";
    private static final String DEFAULT_TEST_TERM_NAME = "Test term A";
    private GlossaryFVT glossaryFVT =null;
    private TermFVT termFVT=null;
    private static Logger log = LoggerFactory.getLogger(EffectiveDatesFVT.class);

    public static void main(String args[])
    {
        try
        {
            String url = RunAllFVTOn2Servers.getUrl(args);
            runWith2Servers(url);
        } catch (IOException e1)
        {
            System.out.println("Error getting user input");
        } catch (GlossaryAuthorFVTCheckedException e) {
            log.error("ERROR: " + e.getMessage() );
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            log.error("ERROR: " + e.getReportedErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        }

    }
    public EffectiveDatesFVT(String url, String serverName,String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        if (log.isDebugEnabled()) {
            log.debug("Create a glossary");
        }
        glossaryFVT = new GlossaryFVT(url,serverName,userId);
        termFVT= new TermFVT(url,serverName,userId);
    }
    public void deleteRemaining() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, GlossaryAuthorFVTCheckedException {
        termFVT.deleteRemainingTerms();
        glossaryFVT.deleteRemainingGlossaries();
    }
    public static void runWith2Servers(String url) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        runIt(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        runIt(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
    }
    synchronized public static void runIt(String url, String serverName, String userId) throws InvalidParameterException, GlossaryAuthorFVTCheckedException, PropertyServerException, UserNotAuthorizedException {
        try
        {
            System.out.println("EffectiveDatesFVT runIt started");
            EffectiveDatesFVT fvt = new EffectiveDatesFVT(url, serverName, userId);
            fvt.run();
            fvt.deleteRemaining();
            System.out.println("EffectiveDatesFVT runIt stopped");
        }
        catch (Exception error) {
            log.error("The FVT Encountered an Exception", error);
            throw error;
        }
    }

    public void run() throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        long now = new Date().getTime();

        try
        {
            glossaryFVT.createPastToGlossary(DEFAULT_TEST_PAST_GLOSSARY_NAME);
        } catch (InvalidParameterException e) {
            log.error("Expected creation of a Glossary with to in the past failed");
        }
        try
        {
            glossaryFVT.createPastFromGlossary(DEFAULT_TEST_PAST_GLOSSARY_NAME);
        } catch (InvalidParameterException e) {
            log.error("Expected creation of a Glossary with from in the past failed");
        }
        try
        {
           glossaryFVT.createInvalidEffectiveDateGlossary(DEFAULT_TEST_PAST_GLOSSARY_NAME);
        } catch (InvalidParameterException e) {
            log.error("Expected creation of a Glossary with invalid Effectivity dates failed");
        }
        Glossary futureGloss = glossaryFVT.createFutureGlossary(DEFAULT_TEST_FUTURE_GLOSSARY_NAME);
        FVTUtils.validateNode(futureGloss);
        Term term5 =termFVT.createTerm(DEFAULT_TEST_TERM_NAME, futureGloss.getSystemAttributes().getGUID());
        FVTUtils.validateNode(term5);
        checkTermGlossaryEffectivity(futureGloss, term5);

        Term gotTerm5 = termFVT.getTermByGUID(term5.getSystemAttributes().getGUID());
        FVTUtils.validateNode(gotTerm5);
        checkTermGlossaryEffectivity(futureGloss, gotTerm5);

        // update the term so that its effective dates not longer are compatible with the glossary
        Term futureTerm = termFVT.updateTermToFuture(now,gotTerm5.getSystemAttributes().getGUID(), term5);
        FVTUtils.validateNode(futureTerm);
        checkTermGlossaryEffectivity(futureGloss, futureTerm);
        futureTerm = termFVT.getTermByGUID(term5.getSystemAttributes().getGUID());
        FVTUtils.validateNode(futureTerm);
        checkTermGlossaryEffectivity(futureGloss, futureTerm);
    }
    private void checkTermGlossaryEffectivity(Glossary glossary, Term term) throws GlossaryAuthorFVTCheckedException {
        if (term.getGlossary()==null) {
            // error always expect a glossary
            throw new GlossaryAuthorFVTCheckedException("ERROR: Term expected associated future Glossary");
        }
        Long glossaryFrom =glossary.getEffectiveFromTime();
        Long termGlossaryFrom =term.getGlossary().getFromEffectivityTime();
        Long glossaryTo =glossary.getEffectiveToTime();
        Long termGlossaryTo =term.getGlossary().getToEffectivityTime();

        if (glossaryFrom == null && termGlossaryFrom != null) {
            // error
            throw new GlossaryAuthorFVTCheckedException("ERROR: Term's Glossary fromTime not null but glossaries is null");
        }
        if (glossaryFrom != null && termGlossaryFrom == null) {
            // error
            throw new GlossaryAuthorFVTCheckedException("ERROR: Term's Glossary fromTime  null but glossaries is not null");
        }
        if (glossaryTo == null && termGlossaryTo != null) {
            // error
            throw new GlossaryAuthorFVTCheckedException("ERROR: Term's Glossary toTime not null but glossaries is null");
        }
        if (glossaryTo != null && termGlossaryTo == null) {
            // error
            throw new GlossaryAuthorFVTCheckedException("ERROR: Term's Glossary toTime null but glossaries is not null");
        }
        if (glossaryFrom != null && termGlossaryFrom != null) {
            if (glossaryFrom.longValue() != termGlossaryFrom.longValue()) {
                // error
                throw new GlossaryAuthorFVTCheckedException("ERROR: Term's Glossary fromTime " + termGlossaryFrom.longValue() + " does not match the glossaries " + glossaryFrom.longValue());
            }

        }

        if (glossaryTo != null && termGlossaryTo != null) {
            if (glossaryTo.longValue() != termGlossaryTo.longValue()) {
                // error
                throw new GlossaryAuthorFVTCheckedException("ERROR: Term's Glossary toTime " + termGlossaryTo.longValue() + " does not match the glossaries " + glossaryTo.longValue());
            }

        }
    }
}
