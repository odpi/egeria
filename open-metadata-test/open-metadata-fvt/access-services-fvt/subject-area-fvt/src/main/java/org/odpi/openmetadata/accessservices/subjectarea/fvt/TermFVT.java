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
    private String userId =null;

    public static void main(String args[])
    {
        try
        {
            String url = RunAllFVT.getUrl(args);
            runWith2Servers(url);
        } catch (IOException e1)
        {
            System.out.println("Error getting user input");
        } catch (SubjectAreaCheckedExceptionBase e)
        {
            System.out.println("ERROR: " + e.getErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        }

    }
    public TermFVT(String url,String serverName,String userId) throws SubjectAreaCheckedExceptionBase
    {
        subjectAreaTerm = new SubjectAreaImpl(serverName,url).getSubjectAreaTerm();
        System.out.println("Create a glossary");
        glossaryFVT = new GlossaryFVT(url,serverName,userId);
        this.serverName=serverName;
        this.userId=userId;
    }
    public static void runWith2Servers(String url) throws SubjectAreaCheckedExceptionBase
    {
        TermFVT fvt =new TermFVT(url,FVTConstants.SERVER_NAME1,FVTConstants.USERID);
        fvt.run();
        TermFVT fvt2 =new TermFVT(url,FVTConstants.SERVER_NAME2,FVTConstants.USERID);
        fvt2.run();
    }

    public static void runIt(String url, String serverName, String userId) throws SubjectAreaCheckedExceptionBase {
        TermFVT fvt =new TermFVT(url,serverName,userId);
        fvt.run();
    }

    public void run() throws SubjectAreaCheckedExceptionBase
    {
        Glossary glossary= glossaryFVT.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
        System.out.println("Create a term1");
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
        Term term = results.get(0);
        long now = new Date().getTime();
        Date fromTermTime = new Date(now+6*1000*60*60*24);
        Date toTermTime = new Date(now+7*1000*60*60*24);

        term.setEffectiveFromTime(fromTermTime);
        term.setEffectiveToTime(toTermTime);
        Term updatedFutureTerm = updateTerm(term.getSystemAttributes().getGUID(),term);
        if (updatedFutureTerm.getEffectiveFromTime().getTime()!=fromTermTime.getTime()) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected term from time to update", "", "");
        }
        if (updatedFutureTerm.getEffectiveToTime().getTime()!=toTermTime.getTime()) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected term to time to update", "", "");
        }
        Date fromGlossaryTime = new Date(now+8*1000*60*60*24);
        Date toGlossaryTime = new Date(now+9*1000*60*60*24);
        glossary.setEffectiveFromTime(fromGlossaryTime);
        glossary.setEffectiveToTime(toGlossaryTime);
        Glossary updatedFutureGlossary= glossaryFVT.updateGlossary(glossaryGuid,glossary);

        if (updatedFutureGlossary.getEffectiveFromTime().getTime()!=fromGlossaryTime.getTime()) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected glossary from time to update", "", "");
        }
        if (updatedFutureGlossary.getEffectiveToTime().getTime()!=toGlossaryTime.getTime()) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected glossary to time to update", "", "");
        }

        Term newTerm = getTermByGUID(term.getSystemAttributes().getGUID());

        GlossarySummary glossarySummary =  newTerm.getGlossary();

        if (glossarySummary.getFromEffectivityTime().getTime()!=fromGlossaryTime.getTime()) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected glossary summary from time to update", "", "");
        }
        if (glossarySummary.getToEffectivityTime().getTime()!=toGlossaryTime.getTime()) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected glossary summary to time to update", "", "");
        }

        if (glossarySummary.getRelationshipguid() ==null) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected glossary summary non null relationship", "", "");
        }
        if (glossarySummary.getFromRelationshipEffectivityTime() !=null) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected glossary summary null relationship from time", "", "");
        }
        if (glossarySummary.getToRelationshipEffectivityTime() !=null) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected glossary summary null relationship to time", "", "");
        }
        Term term5 = new Term();
        term5.setSpineObject(true);
        term5.setName("Term5");
        glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        term5.setGlossary(glossarySummary);
        Term createdTerm5 = issueCreateTerm(term5);
        if (createdTerm5.isSpineObject() == false) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected isSpineObject to be true ", "", "");
        }
        Term term6 = new Term();
        term6.setSpineAttribute(true);
        term6.setName("Term6");
        glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        term6.setGlossary(glossarySummary);
        Term createdTerm6 = issueCreateTerm(term6);
        if (createdTerm6.isSpineAttribute() == false) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected isSpineAttribute to be true ", "", "");
        }
        Term term7 = new Term();
        term7.setObjectIdentifier(true);
        term7.setName("Term7");
        glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        term7.setGlossary(glossarySummary);
        Term createdTerm7 = issueCreateTerm(term7);
        if (createdTerm7.isObjectIdentifier() == false) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Expected isObjectIdentifier to be true ", "", "");
        }

    }

    public  Term createTerm(String termName, String glossaryGuid) throws SubjectAreaCheckedExceptionBase
    {
        Term term = getTermForInput(termName, glossaryGuid);
        return issueCreateTerm(term);
    }

    public Term issueCreateTerm(Term term) throws MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException {
        Term newTerm = subjectAreaTerm.createTerm(this.userId, term);
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
        confidentiality.setLevel(6);
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
        confidentiality.setLevel(5);
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
        Term term = subjectAreaTerm.getTermByGuid(this.userId, guid);
        if (term != null)
        {
            System.out.println("Got Term " + term.getName() + " with guid " + term.getSystemAttributes().getGUID() + " and status " + term.getSystemAttributes().getStatus());
        }
        return term;
    }
    public List<Term> findTerms(String criteria) throws SubjectAreaCheckedExceptionBase
    {
        List<Term> terms = subjectAreaTerm.findTerm(
                this.userId,
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
        Term updatedTerm = subjectAreaTerm.updateTerm(this.userId, guid, term);
        if (updatedTerm != null)
        {
            System.out.println("Updated Term name to " + updatedTerm.getName());
        }
        return updatedTerm;
    }
    public Term restoreTerm(String guid) throws SubjectAreaCheckedExceptionBase
    {
        Term restoredTerm = subjectAreaTerm.restoreTerm(this.userId, guid);
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

        Term updatedTerm = subjectAreaTerm.updateTerm(this.userId, guid, term);
        if (updatedTerm != null)
        {
            System.out.println("Updated Term name to " + updatedTerm.getName());
        }
        return updatedTerm;
    }

    public Term deleteTerm(String guid) throws SubjectAreaCheckedExceptionBase
    {
        Term deletedTerm = subjectAreaTerm.deleteTerm(this.userId, guid);
        if (deletedTerm != null)
        {
            System.out.println("Deleted Term name is " + deletedTerm.getName());
        }
        return deletedTerm;
    }

    public void purgeTerm(String guid) throws SubjectAreaCheckedExceptionBase
    {
        subjectAreaTerm.purgeTerm(this.userId, guid);
        System.out.println("Purge succeeded");
    }

    public List<Line> getTermRelationships(Term term) throws UserNotAuthorizedException, UnexpectedResponseException, InvalidParameterException, FunctionNotSupportedException, MetadataServerUncontactableException {
        return subjectAreaTerm.getTermRelationships(this.userId,
                term.getSystemAttributes().getGUID(),
                null,
                0,
                0,
                null,
                null);
    }

    public List<Line> getTermRelationships(Term term, Date asOfTime, int offset, int pageSize, SequencingOrder sequenceOrder, String sequenceProperty) throws UserNotAuthorizedException, UnexpectedResponseException, InvalidParameterException, FunctionNotSupportedException, MetadataServerUncontactableException {
        return subjectAreaTerm.getTermRelationships(this.userId,
                term.getSystemAttributes().getGUID(),
                asOfTime,
                offset,
                pageSize,
                sequenceOrder,
                sequenceProperty);
    }
}
