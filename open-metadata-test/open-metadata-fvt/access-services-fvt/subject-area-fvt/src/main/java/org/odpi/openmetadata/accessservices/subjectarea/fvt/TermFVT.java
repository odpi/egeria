/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaTerm;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Confidence;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Confidentiality;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Criticality;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Retention;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.ConfidenceLevel;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.ConfidentialityLevel;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.CriticalityLevel;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.RetentionBasis;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.GovernanceActions;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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
        String glossaryGuid = glossary.getSystemAttributes().getGUID();
        Term term1 = createTerm(DEFAULT_TEST_TERM_NAME, glossaryGuid);
        FVTUtils.validateNode(term1);
        System.out.println("Create a term2 using glossary guid");
        Term term2 = createTerm(DEFAULT_TEST_TERM_NAME, glossaryGuid);
        FVTUtils.validateNode(term2);
        System.out.println("Create a term2 using glossary guid");

        Term termForUpdate = new Term();
        termForUpdate.setName(DEFAULT_TEST_TERM_NAME_UPDATED);
        System.out.println("Get term1");
        String guid = term1.getSystemAttributes().getGUID();
        Term gotTerm = getTermByGUID(guid);
        FVTUtils.validateNode(gotTerm);
        System.out.println("Update term1");
        Term updatedTerm = updateTerm(guid, termForUpdate);
        FVTUtils.validateNode(updatedTerm);
        System.out.println("Get term1 again");
        gotTerm = getTermByGUID(guid);
        FVTUtils.validateNode(gotTerm);
        System.out.println("Delete term1");
        gotTerm = deleteTerm(guid);
        System.out.println("Restore term1");
        FVTUtils.validateNode(gotTerm);
        gotTerm = restoreTerm(guid);
        FVTUtils.validateNode(gotTerm);
        System.out.println("Delete term1 again");
        gotTerm = deleteTerm(guid);
        FVTUtils.validateNode(gotTerm);
        System.out.println("Purge term1");
        purgeTerm(guid);
        System.out.println("Create term3 with governance actions");
        GovernanceActions governanceActions = createGovernanceActions();
        Term term3 = createTermWithGovernanceActions(DEFAULT_TEST_TERM_NAME, glossaryGuid,governanceActions);
        FVTUtils.validateNode(term3);
        if (!governanceActions.getConfidence().getLevel().equals(term3.getGovernanceActions().getConfidence().getLevel())){
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Governance actions confidence not returned  as expected", "", "");
        }
        if (!governanceActions.getConfidentiality().getLevel().equals(term3.getGovernanceActions().getConfidentiality().getLevel())) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Governance actions confidentiality not returned  as expected", "", "");
        }
        if (!governanceActions.getRetention().getBasis().equals(term3.getGovernanceActions().getRetention().getBasis())) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Governance actions retention not returned  as expected", "", "");
        }
        if (!governanceActions.getCriticality().getLevel().equals(term3.getGovernanceActions().getCriticality().getLevel())) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Governance actions criticality not returned  as expected", "", "");
        }
        GovernanceActions governanceActions2 = create2ndGovernanceActions();
        System.out.println("Update term3 with and change governance actions");
        Term term3ForUpdate = new Term();
        term3ForUpdate.setName(DEFAULT_TEST_TERM_NAME_UPDATED);
        term3ForUpdate.setGovernanceActions(governanceActions2);

        Term updatedTerm3 = updateTerm(term3.getSystemAttributes().getGUID(), term3ForUpdate);
        FVTUtils.validateNode(updatedTerm3);
        if (!governanceActions2.getConfidence().getLevel().equals(updatedTerm3.getGovernanceActions().getConfidence().getLevel())){
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Governance actions confidence not returned  as expected", "", "");
        }
        if (!governanceActions2.getConfidentiality().getLevel().equals(updatedTerm3.getGovernanceActions().getConfidentiality().getLevel())) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Governance actions confidentiality not returned  as expected", "", "");
        }
        if (!(updatedTerm3.getGovernanceActions().getRetention()==null)) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Governance actions retention not null as expected", "", "");
        }
        if (!(updatedTerm3.getGovernanceActions().getCriticality().getLevel()==null)) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Governance actions criticality not returned  as expected", "", "");
        }

        System.out.println("create terms to find");
        Term termForFind1 = getTermForInput("abc",glossaryGuid);
        termForFind1.setQualifiedName("yyy");
        termForFind1 = issueCreateTerm(termForFind1);
        FVTUtils.validateNode(termForFind1);
        Term termForFind2 = createTerm("yyy",glossaryGuid);
        FVTUtils.validateNode(termForFind2);
        Term termForFind3 = createTerm("zzz",glossaryGuid);
        FVTUtils.validateNode(termForFind3);
        Term termForFind4 = createTerm("This is a Term with spaces in name",glossaryGuid);
        FVTUtils.validateNode(termForFind4);

        List<Term>  results = findTerms("zzz");
        if (results.size() !=1 ) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected 1 back on the find got " +results.size(), "", "");
        }
        results = findTerms("yyy");
        if (results.size() !=2 ) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected 2 back on the find got " +results.size(), "", "");
        }
        //soft delete a term and check it is not found
        Term deleted4 = deleteTerm(termForFind2.getSystemAttributes().getGUID());
        FVTUtils.validateNode(deleted4);
        results = findTerms("yyy");
        if (results.size() !=1 ) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected 1 back on the find got " +results.size(), "", "");
        }

       // search for a term with a name with spaces in
        results = findTerms("This is a Term with spaces in name");
        if (results.size() !=1 ) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected 1 back on the find got " +results.size(), "", "");
        }
    }

    public  Term createTerm(String termName, String glossaryGuid) throws SubjectAreaCheckedExceptionBase
    {
        Term term = getTermForInput(termName, glossaryGuid);
        return issueCreateTerm(term);
    }

    private Term issueCreateTerm(Term term) throws MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException {
        Term newTerm = subjectAreaTerm.createTerm(serverName,FVTConstants.USERID, term);
        if (newTerm != null)
        {
            System.out.println("Created Term " + newTerm.getName() + " with guid " + newTerm.getSystemAttributes().getGUID());
        }
        return newTerm;
    }

    private Term getTermForInput(String termName, String glossaryGuid) {
        Term term = new Term();
        term.setName(termName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        term.setGlossary(glossarySummary);
        return term;
    }

    public  Term createTermWithGovernanceActions(String termName, String glossaryGuid,GovernanceActions governanceActions) throws SubjectAreaCheckedExceptionBase
    {
        Term term = getTermForInput(termName, glossaryGuid);
        term.setGovernanceActions(governanceActions);
        Term newTerm = issueCreateTerm(term);
        return newTerm;
    }

    private GovernanceActions createGovernanceActions() {
        GovernanceActions governanceActions = new GovernanceActions();
        Confidentiality confidentiality = new Confidentiality();
        confidentiality.setLevel(ConfidentialityLevel.Confidential);
        governanceActions.setConfidentiality(confidentiality);

        Confidence confidence = new Confidence();
        confidence.setLevel(ConfidenceLevel.Authoritative);
        governanceActions.setConfidence(confidence);

        Criticality criticality = new Criticality();
        criticality.setLevel(CriticalityLevel.Catastrophic);
        governanceActions.setCriticality(criticality);

        Retention retention = new Retention();
        retention.setBasis(RetentionBasis.ProjectLifetime);
        governanceActions.setRetention(retention);
        return governanceActions;
    }
    private GovernanceActions create2ndGovernanceActions() {
        GovernanceActions governanceActions = new GovernanceActions();
        Confidentiality confidentiality = new Confidentiality();
        confidentiality.setLevel(ConfidentialityLevel.Internal);
        governanceActions.setConfidentiality(confidentiality);

        Confidence confidence = new Confidence();
        confidence.setLevel(ConfidenceLevel.AdHoc);
        governanceActions.setConfidence(confidence);
        // remove this classification level
        Criticality criticality = new Criticality();
        criticality.setLevel(null);
        governanceActions.setCriticality(criticality);
        // remove retention by nulling it
        governanceActions.setRetention(null);
        return governanceActions;
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
    public List<Term> findTerms(String criteria) throws SubjectAreaCheckedExceptionBase
    {
        List<Term> terms = subjectAreaTerm.findTerm(
                serverName,
                FVTConstants.USERID,
                criteria,
                null,
        0,
         0,
     null,
                null);
        return terms;
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
    public Term restoreTerm(String guid) throws SubjectAreaCheckedExceptionBase
    {
        Term restoredTerm = subjectAreaTerm.restoreTerm(serverName,FVTConstants.USERID, guid);
        if (restoredTerm != null)
        {
            System.out.println("Restored Term " + restoredTerm.getName());
        }
        return restoredTerm;
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

    public List<Line> getTermRelationships(Term term) throws UserNotAuthorizedException, UnexpectedResponseException, InvalidParameterException, FunctionNotSupportedException, MetadataServerUncontactableException {
        return subjectAreaTerm.getTermRelationships(serverName,FVTConstants.USERID,
                term.getSystemAttributes().getGUID(),
                null,
                0,
                0,
                null,
                null);
    }

    public List<Line> getTermRelationships(Term term, Date asOfTime, int offset, int pageSize, SequencingOrder sequenceOrder, String sequenceProperty) throws UserNotAuthorizedException, UnexpectedResponseException, InvalidParameterException, FunctionNotSupportedException, MetadataServerUncontactableException {
        return subjectAreaTerm.getTermRelationships(serverName,FVTConstants.USERID,
                term.getSystemAttributes().getGUID(),
                asOfTime,
                offset,
                pageSize,
                sequenceOrder,
                sequenceProperty);
    }
}
