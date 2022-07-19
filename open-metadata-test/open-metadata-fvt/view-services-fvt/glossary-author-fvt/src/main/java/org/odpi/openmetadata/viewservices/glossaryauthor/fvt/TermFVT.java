/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.fvt;

import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.GlossaryAuthorViewRestClient;
import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.term.GlossaryAuthorViewTermClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Confidence;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Confidentiality;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Criticality;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Retention;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.ConfidenceLevel;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.CriticalityLevel;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.RetentionBasis;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.GovernanceClassifications;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
//import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * FVT resource to call Glossary Author View term API
 */
public class TermFVT {
    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Test Glossary for term FVT";
    private static final String DEFAULT_TEST_TERM_NAME = "Test term A";
    private static final String DEFAULT_TEST_TERM_NAME_UPDATED = "Test term A updated";
    private static final String DEFAULT_TEST_TERM_LIST = "Test term Number ";
    private SubjectAreaNodeClient<Term> subjectAreaTerm = null;
    private GlossaryAuthorViewTermClient glossaryAuthorViewTermClient = null;
    private GlossaryFVT glossaryFVT =null;
    private CategoryFVT categoryFVT =null;
    private SubjectAreaDefinitionCategoryFVT subjectAreaFVT =null;
    private String userId =null;
    private int existingTermCount = 0;
    private static Logger log = LoggerFactory.getLogger(TermFVT.class);

    /*
     * Keep track of all the created guids in this set, by adding create and restore guids and removing when deleting.
     * At the end of the test it will delete any remaining guids.
     *
     * Note this FVT is called by other FVTs. Who ever constructs the FVT should run deleteRemainingTerms.
     */
    private Set<String> createdTermsSet = new HashSet<>();

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
    public TermFVT(String url,String serverName,String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        GlossaryAuthorViewRestClient client = new GlossaryAuthorViewRestClient(serverName, url);
        //subjectAreaTerm = new SubjectAreaTermClient<>(client);
        this.userId=userId;

        glossaryAuthorViewTermClient = new GlossaryAuthorViewTermClient(client);

        if (log.isDebugEnabled()) {
            log.debug("Create a glossary");
        }
        glossaryFVT = new GlossaryFVT(url,serverName,userId);
        categoryFVT = new CategoryFVT(url, serverName,userId);
        //optionKey

        subjectAreaFVT = new SubjectAreaDefinitionCategoryFVT(url, serverName,userId);

        existingTermCount = findTerms("").size();
        if (log.isDebugEnabled()) {
            log.debug("existingTermCount " + existingTermCount);
        }
    }


    private ViewServiceConfig retrieveGlossaryAuthorConfig() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
         return glossaryAuthorViewTermClient.getGlossaryAuthViewServiceConfig(userId);

    }


    public static void runWith2Servers(String url) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        runIt(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        runIt(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
    }

    public static void runIt(String url, String serverName, String userId) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        try {
            System.out.println("TermFVT runIt started");
            TermFVT fvt =new TermFVT(url,serverName,userId);
            fvt.run();
            fvt.deleteRemaining();
            System.out.println("TermFVT runIt stopped");
        }
        catch (Exception error) {
            log.error("The FVT Encountered an Exception", error);
            throw error;
        }
    }
    public static int getTermCount(String url, String serverName, String userId) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, GlossaryAuthorFVTCheckedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        TermFVT fvt = new TermFVT(url, serverName, userId);
        return fvt.findTerms("").size();
    }

    public void run() throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        Glossary glossary= glossaryFVT.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
        if (log.isDebugEnabled()) {
            log.debug("Create a term1");
        }
        String glossaryGuid = glossary.getSystemAttributes().getGUID();
        Term term1 = createTerm(DEFAULT_TEST_TERM_NAME, glossaryGuid);
        FVTUtils.validateNode(term1);
        if (log.isDebugEnabled()) {
            log.debug("Create a term1 using glossary userId");
        }
        Term term2 = createTerm(DEFAULT_TEST_TERM_NAME, glossaryGuid);
        FVTUtils.validateNode(term2);
        if (log.isDebugEnabled()) {
            log.debug("Create a term2 using glossary userId");
        }

        FindRequest findRequest = new FindRequest();
        findRequest.setSearchCriteria("");
        List<Term> results = glossaryFVT.getTerms(glossaryGuid, findRequest);
        if (results.size() != 2) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 2 back on getGlossaryTerms " + results.size());
        }
        findRequest.setPageSize(1);
        results = glossaryFVT.getTerms(glossaryGuid, findRequest);
        if (results.size() != 1) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 1 back on getGlossaryTerms with page size 1, got " + results.size());
        }

        Term termForUpdate = new Term();
        termForUpdate.setName(DEFAULT_TEST_TERM_NAME_UPDATED);
        if (log.isDebugEnabled()) {
            log.debug("Get term1");
        }
        String guid = term1.getSystemAttributes().getGUID();
        Term gotTerm = getTermByGUID(guid);
        FVTUtils.validateNode(gotTerm);
        if (log.isDebugEnabled()) {
            log.debug("Update term1");
        }
        Term updatedTerm = updateTerm(guid, termForUpdate);
        FVTUtils.validateNode(updatedTerm);
        if (log.isDebugEnabled()) {
            log.debug("Get term1 again");
        }
        gotTerm = getTermByGUID(guid);
        FVTUtils.validateNode(gotTerm);
        if (log.isDebugEnabled()) {
            log.debug("Delete term1");
        }
        deleteTerm(guid);
        if (log.isDebugEnabled()) {
            log.debug("Restore term1");
        }
        //FVTUtils.validateNode(gotTerm);
        gotTerm = restoreTerm(guid);
        FVTUtils.validateNode(gotTerm);
        if (log.isDebugEnabled()) {
            log.debug("Delete term1 again");
        }
        deleteTerm(guid);
        //FVTUtils.validateNode(gotTerm);
        if (log.isDebugEnabled()) {
            log.debug("Create term3 with governance actions");
        }
        GovernanceClassifications governanceClassifications = createGovernanceClassifications();
        Term term3 = createTermWithGovernanceClassifications(DEFAULT_TEST_TERM_NAME, glossaryGuid, governanceClassifications);
        FVTUtils.validateNode(term3);
        if (!governanceClassifications.getConfidence().getLevel().equals(term3.getGovernanceClassifications().getConfidence().getLevel())){
            throw new GlossaryAuthorFVTCheckedException("ERROR: Governance actions confidence not returned as expected");
        }
        if (!governanceClassifications.getConfidentiality().getLevel().equals(term3.getGovernanceClassifications().getConfidentiality().getLevel())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Governance actions confidentiality not returned as expected");
        }
        if (!governanceClassifications.getRetention().getBasis().equals(term3.getGovernanceClassifications().getRetention().getBasis())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Governance actions retention not returned as expected");
        }
        if (!governanceClassifications.getCriticality().getLevel().equals(term3.getGovernanceClassifications().getCriticality().getLevel())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Governance actions criticality not returned  as expected. ");
        }
        GovernanceClassifications governanceClassifications2 = create2ndGovernanceClassifications();
        if (log.isDebugEnabled()) {
            log.debug("Update term3 with and change governance actions");
        }
        Term term3ForUpdate = new Term();
        term3ForUpdate.setName(DEFAULT_TEST_TERM_NAME_UPDATED);
        term3ForUpdate.setGovernanceClassifications(governanceClassifications2);

        Term updatedTerm3 = updateTerm(term3.getSystemAttributes().getGUID(), term3ForUpdate);
        FVTUtils.validateNode(updatedTerm3);
        if (!governanceClassifications2.getConfidence().getLevel().equals(updatedTerm3.getGovernanceClassifications().getConfidence().getLevel())){
            throw new GlossaryAuthorFVTCheckedException("ERROR: Governance actions confidence not returned  as expected");
        }
        if (!governanceClassifications2.getConfidentiality().getLevel().equals(updatedTerm3.getGovernanceClassifications().getConfidentiality().getLevel())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Governance actions confidentiality not returned  as expected");
        }
        if (updatedTerm3.getGovernanceClassifications().getRetention() !=null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Governance actions retention not null as expected");
        }
        // https://github.com/odpi/egeria/issues/3457  the below line when uncommented causes an error with the graph repo.
//        if (updatedTerm3.getGovernanceActions().getCriticality().getLevel() !=null) {
//            throw new SubjectAreaFVTCheckedException("ERROR: Governance actions criticality not returned as expected. It is " + updatedTerm3.getGovernanceActions().getCriticality().getLevel().getName());
//        }
        String spacedTermName = "This is a Term with spaces in name";
        int allcount  = glossaryAuthorViewTermClient.findAll(userId).size();
        int yyycount = findTerms("yyy").size();
        int zzzcount = findTerms("zzz").size();
        int spacedTermcount = findTerms( spacedTermName).size();

        if (log.isDebugEnabled()) {
            log.debug("create terms to find");
        }
        Term termForFind1 = getTermForInput("abc",glossaryGuid);
        termForFind1.setDescription("yyy");
        termForFind1 = issueCreateTerm(termForFind1);
        FVTUtils.validateNode(termForFind1);
        Term termForFind2 = createTerm("yyy",glossaryGuid);
        FVTUtils.validateNode(termForFind2);
        Term termForFind3 = createTerm("zzz",glossaryGuid);
        FVTUtils.validateNode(termForFind3);
        Term termForFind4 = createTerm("This is a Term with spaces in name",glossaryGuid);
        FVTUtils.validateNode(termForFind4);

        results = findTerms("zzz");
        if (results.size() !=zzzcount+1 ) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: zzz Expected " + zzzcount+1+ " back on the find got " +results.size());
        }
        results = findTerms("yyy");
        if (results.size() !=yyycount + 2) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: yyy Expected " + yyycount+1 + " back on the find got " +results.size());
        }
        results = findTerms(null); //it's find all terms
        if (results.size() !=allcount + 4 ) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: allcount Expected " + allcount + 4 + " back on the find got " +results.size());
        }

        results = glossaryAuthorViewTermClient.findAll(userId); //it's find all terms
        if (results.size() !=allcount + 4 ) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: allcount2 Expected " + allcount + 4 + " back on the find got " +results.size());
        }
        //soft delete a term and check it is not found
        deleteTerm(termForFind2.getSystemAttributes().getGUID());
        //FVTUtils.validateNode(deleted4);
        results = findTerms("yyy");
        if (results.size() !=yyycount +1 ) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: yyy2 Expected " +yyycount +1  + " back on the find got " +results.size());
        }

       // search for a term with a name with spaces in
        results = findTerms(spacedTermName);
        if (results.size() != spacedTermcount +1 ) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected spaced " + spacedTermcount+1 + " back on the find got "  +results.size());
        }

        Term term = termForFind4;
        // we need to find a term that has our glossary so we can see how the summary changes when we change the
        // glossary effectivity
        long now = new Date().getTime();
        long fromTermTime = new Date(now+6*1000*60*60*24).getTime();
        long toTermTime = new Date(now+7*1000*60*60*24).getTime();

        term.setEffectiveFromTime(fromTermTime);
        term.setEffectiveToTime(toTermTime);
        Term updatedFutureTerm = updateTerm(term.getSystemAttributes().getGUID(), term);
        if (updatedFutureTerm.getEffectiveFromTime().longValue()!=fromTermTime) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected term from time to update");
        }
        if (updatedFutureTerm.getEffectiveToTime().longValue() !=toTermTime) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected term to time to update");
        }
        long fromGlossaryTime = new Date(now+8*1000*60*60*24).getTime();
        long toGlossaryTime = new Date(now+9*1000*60*60*24).getTime();
        glossary.setEffectiveFromTime(fromGlossaryTime);
        glossary.setEffectiveToTime(toGlossaryTime);
        Glossary updatedFutureGlossary= glossaryFVT.updateGlossary(glossaryGuid, glossary);

        if (updatedFutureGlossary.getEffectiveFromTime().longValue()!= fromGlossaryTime) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected glossary from time to update");
        }
        if (updatedFutureGlossary.getEffectiveToTime().longValue()!= toGlossaryTime) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected glossary to time to update");
        }

        Term newTerm = getTermByGUID(term.getSystemAttributes().getGUID());

        GlossarySummary glossarySummary =  newTerm.getGlossary();

        if (glossarySummary.getFromEffectivityTime().longValue()!= fromGlossaryTime) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected from glossary summary time "+glossarySummary.getFromEffectivityTime() + " to equal " +fromGlossaryTime);
        }
        if (glossarySummary.getToEffectivityTime().longValue() != toGlossaryTime) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected to glossary summary time "+glossarySummary.getToEffectivityTime() + " to equal " + toGlossaryTime);
        }

        if (glossarySummary.getRelationshipguid() == null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected glossary summary non null relationship");
        }
        if (glossarySummary.getFromRelationshipEffectivityTime() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected glossary summary null relationship from time");
        }
        if (glossarySummary.getToRelationshipEffectivityTime() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected glossary summary null relationship to time");
        }
        Term term5 = new Term();
        term5.setSpineObject(true);
        term5.setName("Term5");
        glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        term5.setGlossary(glossarySummary);
        Term createdTerm5 = issueCreateTerm(term5);
        if (createdTerm5.isSpineObject() == false) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected isSpineObject to be true ");
        }
        Term term6 = new Term();
        term6.setSpineAttribute(true);
        term6.setName("Term6");
        glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        term6.setGlossary(glossarySummary);
        Term createdTerm6 = issueCreateTerm(term6);
        if (createdTerm6.isSpineAttribute() == false) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected isSpineAttribute to be true ");
        }
        Term term7 = new Term();
        term7.setObjectIdentifier(true);
        term7.setName("Term7");
        glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        term7.setGlossary(glossarySummary);
        Term createdTerm7 = issueCreateTerm(term7);
        if (createdTerm7.isObjectIdentifier() == false) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected isObjectIdentifier to be true ");
        }
        // make sure there is a term with the name
         createTerm(DEFAULT_TEST_TERM_NAME, glossaryGuid);

        Term termForUniqueQFN2= createTerm(DEFAULT_TEST_TERM_NAME, glossaryGuid);
        if (termForUniqueQFN2 == null || termForUniqueQFN2.getQualifiedName().length() == 0) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected qualified name to be set");
        }

        // test categories

        Category cat1 = categoryFVT.createCategoryWithGlossaryGuid("cat1", glossaryGuid);

        Category cat2 = subjectAreaFVT.createSubjectAreaDefinitionWithGlossaryGuid("cat2", glossaryGuid);
        Category cat3 = categoryFVT.createCategoryWithGlossaryGuid("cat3",glossaryGuid);
        CategorySummary cat1Summary = new CategorySummary();
        cat1Summary.setGuid(cat1.getSystemAttributes().getGUID());
        CategorySummary cat2Summary = new CategorySummary();
        cat2Summary.setGuid(cat2.getSystemAttributes().getGUID());
        CategorySummary cat3Summary = new CategorySummary();
        cat3Summary.setGuid(cat3.getSystemAttributes().getGUID());

        List<CategorySummary> suppliedCategories = new ArrayList<>();
        suppliedCategories.add(cat1Summary);

        Term term4cats = getTermForInput(DEFAULT_TEST_TERM_NAME,glossaryGuid);
        Term createdTerm4cats =issueCreateTerm(term4cats);
        if (createdTerm4cats.getCategories() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected null categories created when none were requested");
        }

        term4cats = getTermForInput(DEFAULT_TEST_TERM_NAME,glossaryGuid);
        term4cats.setCategories(suppliedCategories);
        createdTerm4cats =issueCreateTerm(term4cats);
        if (createdTerm4cats.getCategories().size() != 1) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 1 categories returned");
        }
        if (!createdTerm4cats.getCategories().get(0).getGuid().equals(cat1Summary.getGuid())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected response category guid to match the requested category guid.");
        }
        if (cat1.getSystemAttributes().getGUID() != null) log.error("guid is null 1" + cat1.getSystemAttributes().getGUID());

        if (categoryFVT.getTerms(cat1.getSystemAttributes().getGUID()) == null) log.error("Its null");
        if (categoryFVT.getTerms(cat1.getSystemAttributes().getGUID()).size() != 1) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected the category to have 1 term.");
        }

        suppliedCategories.add(cat2Summary);
        term4cats.setCategories(suppliedCategories);
        Term createdTerm4cats2 =issueCreateTerm(term4cats);
        if (createdTerm4cats2.getCategories().size() != 2) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 2 categories returned");
        }
        List<Category> categories = getCategoriesAPI(createdTerm4cats2.getSystemAttributes().getGUID(),0,5);
        if (categories.size() !=2) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 2 categories returned on get Categories API call");
        }

        // update with null categories should change nothing
        createdTerm4cats2.setCategories(null);
        Term updatedTerm4cats2 = updateTerm(createdTerm4cats2.getSystemAttributes().getGUID(),createdTerm4cats2);
        if (updatedTerm4cats2.getCategories().size() != 2) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 2 categories returned");
        }
        if (getCategoriesAPI(updatedTerm4cats2.getSystemAttributes().getGUID(),0,5).size() !=2) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 2 categories returned on get Categories API call after update");
        }
        if (getCategoriesAPI(updatedTerm4cats2.getSystemAttributes().getGUID(),1,5).size() !=1) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 1 categories returned on get Categories API call after update startingFrom 1");
        }
        if (getCategoriesAPI(updatedTerm4cats2.getSystemAttributes().getGUID(),0,1).size() !=1) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 1 categories returned on get Categories API call after update pageSize 1");
        }

        // replace categories with null
        createdTerm4cats.setCategories(null);
        Term replacedTerm4cats = replaceTerm(createdTerm4cats.getSystemAttributes().getGUID(), createdTerm4cats);
//        System.out.println("DDebug msg " + replacedTerm4cats.getCategories().toString());
        if (replacedTerm4cats.getCategories() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected replace with null to get rid of the categorizations.");
        }
        List<Category> cats = getCategoriesAPI(replacedTerm4cats.getSystemAttributes().getGUID(),0,5);
        if (cats ==null || cats.size() != 0) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Use API call to check replace with null to get rid of the categorizations.");
        }
        // update term to gain 2 categories
        createdTerm4cats.setCategories(suppliedCategories);
        updatedTerm4cats2 = updateTerm(createdTerm4cats.getSystemAttributes().getGUID(),createdTerm4cats);
        if (updatedTerm4cats2.getCategories().size() != 2) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected update to gain 2 categorizations.");
        }
        if (getCategoriesAPI(updatedTerm4cats2.getSystemAttributes().getGUID(),0,5).size() !=2) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Use API call to check update to gain 2 categorizations");
        }
        testCategorizedTermsWithSearchCriteria();

        List<CategorySummary> supplied3Categories = new ArrayList<>();
        supplied3Categories.add(cat1Summary);
        supplied3Categories.add(cat2Summary);
        supplied3Categories.add(cat3Summary);
        updatedTerm4cats2.setCategories(supplied3Categories);
        updatedTerm4cats2 = updateTerm(createdTerm4cats.getSystemAttributes().getGUID(), updatedTerm4cats2);
        if (updatedTerm4cats2.getCategories().size() != 3) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected update to have 3 categorizations.");
        }

        // clean up
        categoryFVT.deleteCategory(cat1Summary.getGuid());
        categoryFVT.deleteCategory(cat2Summary.getGuid());
        categoryFVT.deleteCategory(cat3Summary.getGuid());
        deleteTerm(createdTerm4cats.getSystemAttributes().getGUID());
        deleteTerm(createdTerm4cats2.getSystemAttributes().getGUID());
    }

    public  Term createTerm(String termName, String glossaryGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        Term term = getTermForInput(termName, glossaryGuid);
        return issueCreateTerm(term);
    }

    public Term issueCreateTerm(Term term) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        Term newTerm = glossaryAuthorViewTermClient.create(this.userId, term);
        if (newTerm != null)
        {
            String guid = newTerm.getSystemAttributes().getGUID();
            if (log.isDebugEnabled()) {
                log.debug("Created Term " + newTerm.getName() + " with guid " + guid);
            }
            createdTermsSet.add(guid);
        }
        return newTerm;
    }

    public Term getTermForInput(String termName, String glossaryGuid) {
        Term term = new Term();
        term.setName(termName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        term.setGlossary(glossarySummary);
        return term;
    }

    public  Term createTermWithGovernanceClassifications(String termName, String glossaryGuid, GovernanceClassifications governanceClassifications) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        Term term = getTermForInput(termName, glossaryGuid);
        term.setGovernanceClassifications(governanceClassifications);
        Term newTerm = issueCreateTerm(term);
        return newTerm;
    }

    private GovernanceClassifications createGovernanceClassifications() {
        GovernanceClassifications governanceClassifications = new GovernanceClassifications();
        Confidentiality confidentiality = new Confidentiality();
        confidentiality.setLevel(6);
        governanceClassifications.setConfidentiality(confidentiality);

        Confidence confidence = new Confidence();
        confidence.setLevel(ConfidenceLevel.Authoritative);
        governanceClassifications.setConfidence(confidence);

        Criticality criticality = new Criticality();
        criticality.setLevel(CriticalityLevel.Catastrophic);
        governanceClassifications.setCriticality(criticality);

        Retention retention = new Retention();
        retention.setBasis(RetentionBasis.ProjectLifetime);
        governanceClassifications.setRetention(retention);
        return governanceClassifications;
    }
    private GovernanceClassifications create2ndGovernanceClassifications() {
        GovernanceClassifications governanceClassifications = new GovernanceClassifications();
        Confidentiality confidentiality = new Confidentiality();
        confidentiality.setLevel(5);
        governanceClassifications.setConfidentiality(confidentiality);

        Confidence confidence = new Confidence();
        confidence.setLevel(ConfidenceLevel.AdHoc);
        governanceClassifications.setConfidence(confidence);
        // remove this classification level
        Criticality criticality = new Criticality();
        criticality.setLevel(null);
        governanceClassifications.setCriticality(criticality);
        // remove retention by nulling it
        governanceClassifications.setRetention(null);
        return governanceClassifications;
    }


    public Term getTermByGUID(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        Term term = glossaryAuthorViewTermClient.getByGUID(this.userId, guid);
        if (term != null)
        {
            if (log.isDebugEnabled()) {
                log.debug("Got Term " + term.getName() + " with userId " + term.getSystemAttributes().getGUID() + " and status " + term.getSystemAttributes().getStatus());
            }
        }
        return term;
    }
    public List<Term> findTerms(String criteria) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        FindRequest findRequest = new FindRequest();
        findRequest.setSearchCriteria(criteria);
        List<Term> terms = glossaryAuthorViewTermClient.find(this.userId, findRequest,false,true);
        return terms;
    }

    public Term updateTerm(String guid, Term term) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        Term updatedTerm = glossaryAuthorViewTermClient.update(this.userId, guid, term,false);
        if (updatedTerm != null)
        {
            if (log.isDebugEnabled()) {
                log.debug("Updated Term name to " + updatedTerm.getName());
            }
        }
        return updatedTerm;
    }
    public Term replaceTerm(String guid, Term term) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        Term updatedTerm = glossaryAuthorViewTermClient.update(this.userId, guid, term, true);
        if (updatedTerm != null)
        {
            if (log.isDebugEnabled()) {
                log.debug("Replaced Term name to " + updatedTerm.getName());
            }
        }
        return updatedTerm;
    }
    public Term restoreTerm(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        Term restoredTerm = glossaryAuthorViewTermClient.restore(this.userId, guid);
        if (restoredTerm != null)
        {
            if (log.isDebugEnabled()) {
                log.debug("Restored Term " + restoredTerm.getName());
            }
            createdTermsSet.add(guid);
        }
        return restoredTerm;
    }

    public Term updateTermToFuture(long now, String guid, Term term) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {

        term.setEffectiveFromTime(new Date(now+1001*60*60*24).getTime());
        term.setEffectiveToTime(new Date(now+1999*60*60*24).getTime());

        Term updatedTerm = glossaryAuthorViewTermClient.update(this.userId, guid, term);
        if (updatedTerm != null)
        {
            if (log.isDebugEnabled()) {
                log.debug("Updated Term name to " + updatedTerm.getName());
            }
        }
        return updatedTerm;
    }

    public void deleteTerm(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
            glossaryAuthorViewTermClient.delete(this.userId, guid);
            createdTermsSet.remove(guid);
            if (log.isDebugEnabled()) {
                log.debug("Delete succeeded");
            }
    }

    public List<Relationship> getTermRelationships(Term term) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        return glossaryAuthorViewTermClient.getAllRelationships(this.userId, term.getSystemAttributes().getGUID());
    }

    public List<Relationship> getTermRelationships(Term term, Date asOfTime, int offset, int pageSize, SequencingOrder sequenceOrder, String sequenceProperty) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        FindRequest findRequest = new FindRequest();
        findRequest.setAsOfTime(asOfTime);
        findRequest.setStartingFrom(offset);
        findRequest.setPageSize(pageSize);
        findRequest.setSequencingOrder(sequenceOrder);
        findRequest.setSequencingProperty(sequenceProperty);
        //term.
        return glossaryAuthorViewTermClient.getRelationships(this.userId, term.getSystemAttributes().getGUID(),findRequest);
    }
    void deleteRemaining() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, GlossaryAuthorFVTCheckedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        deleteRemainingTerms();
        glossaryFVT.deleteRemainingGlossaries();
    }
    void deleteRemainingTerms() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        Iterator<String> iter =  createdTermsSet.iterator();
        while (iter.hasNext()) {
            String guid = iter.next();
            iter.remove();
            deleteTerm(guid);
        }
        List<Term> terms = findTerms("");
        if (terms.size() != existingTermCount) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected " +existingTermCount + " Terms to be found, got " + terms.size());
        }
    }
    public List<Category> getCategoriesAPI(String termGuid,int startingFrom, int pageSize) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        FindRequest findRequest = new FindRequest();
        findRequest.setPageSize(pageSize);
        findRequest.setStartingFrom(startingFrom);
        return glossaryAuthorViewTermClient.getCategories(userId, termGuid, findRequest);
    }

    private void testCategorizedTermsWithSearchCriteria() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        if (log.isDebugEnabled()) {
            log.debug("Create a glossary");
        }
        Glossary glossary = glossaryFVT.createGlossary("Glossary name for CategorizedTermsWithSearchCriteria");
        String glossaryGuid = glossary.getSystemAttributes().getGUID();
        if (log.isDebugEnabled()) {
            log.debug("Create a ttt");
        }
        Category category = categoryFVT.createCategoryWithGlossaryGuid("ttt", glossary.getSystemAttributes().getGUID());
        String parentGuid = category.getSystemAttributes().getGUID();
        // create 20 children
        List<CategorySummary> categories = new ArrayList<>();
        CategorySummary catSummary = new CategorySummary();
        catSummary.setGuid(parentGuid);
        categories.add(catSummary);
        Set<String> termGuids = new HashSet<>();

        for (int i=0;i<10;i++) {

            Term term1 = getTermForInput("tt" + i, glossaryGuid);
            term1.setCategories(categories);
            Term createdTerm1 = issueCreateTerm(term1);
            termGuids.add(createdTerm1.getSystemAttributes().getGUID());
            if (createdTerm1.getCategories().size() != 1) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 1 category created");
            }
            Term term2 = getTermForInput("ss" + i, glossaryGuid);
            term2.setCategories(categories);
            Term createdTerm2 = issueCreateTerm(term2);
            termGuids.add(createdTerm2.getSystemAttributes().getGUID());
            if (createdTerm2.getCategories().size() != 1) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 2 category created");
            }
        }
        FindRequest findRequest = new FindRequest();
        if ( categoryFVT.getTerms(parentGuid, findRequest).size() != 20) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 20 terms associated with the category ");
        }
        if ( glossaryFVT.getTerms(glossaryGuid, findRequest).size() != 20) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 20 terms associated with the category ");
        }
        findRequest.setSearchCriteria("tt3");
        int count =  categoryFVT.getTerms(parentGuid, findRequest).size();
        if (count !=1) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 1 categorized term, got " + count);
        }
        count =   glossaryFVT.getTerms(glossaryGuid, findRequest).size();
        if (count !=1) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 1 glossary term, got " + count);
        }

        findRequest.setSearchCriteria("tt");
        count =  categoryFVT.getTerms(parentGuid, findRequest).size();
        if (count !=10) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 10 category terms, got " + count);
        }
        count =  glossaryFVT.getTerms(glossaryGuid, findRequest).size();
        if (count !=10) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 10 glossary terms, got " + count);
        }

        findRequest.setPageSize(5);
        List<Term> terms = categoryFVT.getTerms(parentGuid, findRequest);
        count = terms.size();
        if (count !=5) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 5 terms with tt,got " + count);
        }
        count = glossaryFVT.getTerms(glossaryGuid, findRequest).size();
        if (count !=5) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 5 glossary terms with tt, got " + count);
        }
        findRequest.setSearchCriteria("ss");
        findRequest.setPageSize(20);
        if ( categoryFVT.getTerms(parentGuid, findRequest).size() !=10) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 10 terms for ss");
        }
        List<Term> tenTerms = glossaryFVT.getTerms(glossaryGuid, findRequest);
        count = tenTerms.size();
        if (count !=10) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 10 glossary terms for ss, got " + count);
        }
        Set<String> tenTermNames = tenTerms.stream()
                .map(term ->term.getName()).collect(Collectors.toSet());
        count = tenTermNames.size();
        if (count !=10) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 10 glossary terms distinct names ss, got " + count);
        }
        findRequest.setPageSize(5);

        List<Term> firstFiveCategoryTerms =categoryFVT.getTerms(parentGuid, findRequest);
        count =  firstFiveCategoryTerms.size();
        if (count !=5) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 5 terms for ss, got " + count);
        }
        Set<String> firstFiveCategoryTermsNames = firstFiveCategoryTerms.stream()
                .map(term ->term.getName()).collect(Collectors.toSet());
        count =  firstFiveCategoryTermsNames.size();
        if (count !=5) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 5 distinct term names for ss, got " + count);
        }
        findRequest.setStartingFrom(5);
        List<Term> secondFiveCategoryTerms =categoryFVT.getTerms(parentGuid, findRequest);
        count =  secondFiveCategoryTerms.size();
        if (count !=5) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 5 category terms for ss for 2nd page, got " + count);
        }
        Set<String> secondFiveCategoryTermsNames = secondFiveCategoryTerms.stream()
                .map(term ->term.getName()).collect(Collectors.toSet());
        count =  secondFiveCategoryTermsNames.size();
        if (count !=5) {
            throw new GlossaryAuthorFVTCheckedException("ERROR:  Expected 5 category term names for ss for 2nd page " + count);
        }
        Set<String> totalFiveCategoryTermsNames = firstFiveCategoryTermsNames;
        totalFiveCategoryTermsNames.addAll(secondFiveCategoryTermsNames);
        count = totalFiveCategoryTermsNames.size();
        if (count !=10) {
            throw new GlossaryAuthorFVTCheckedException("ERROR:  Expected 10 different category term names for ss from first 2 pages " + count);
        }

        //cleanup
        for (String termGuid: termGuids) {
            deleteTerm(termGuid);
        }
        categoryFVT.deleteCategory(parentGuid);
        glossaryFVT.deleteGlossary(glossaryGuid);

    }
}
