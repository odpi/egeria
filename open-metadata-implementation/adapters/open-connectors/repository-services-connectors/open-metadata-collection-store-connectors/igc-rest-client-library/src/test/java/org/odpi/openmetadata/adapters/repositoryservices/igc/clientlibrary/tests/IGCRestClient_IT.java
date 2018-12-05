/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.tests;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115.*;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearch;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchCondition;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Test suite for IGCRestClient.
 * <br><br>
 * Since the testing of the client depends on some metadata actually being available, this suite
 * relies on an environment that has been populated with the Coco Pharmaceuticals samples publicly
 * available (and automatically loadable) from:
 * <a href="https://github.com/cmgrote/ibm-infosvr-samples">https://github.com/cmgrote/ibm-infosvr-samples</a>
 */
public class IGCRestClient_IT {

    private IGCRestClient igcrest;
    private final String bigTermRid    = "6662c0f2.e1b1ec6c.00263pfv9.8dicae6.ms2vnv.944jep7aa2u7e0epjv35l";
    private final String bigTermName   = "Department Code";
    private final String multiPageRid  = "6662c0f2.e1b1ec6c.00263v5ln.ako8t79.5ve76f.urbfgjnj98q0168uijncd";
    private final String multiPageName = "Internal";

    /**
     * Configures connection and registers POJOs.
     */
    @BeforeSuite
    public void setUp() {
        IGCRestClient.disableSslVerification();

        igcrest = new IGCRestClient(
                "https://infosvr.vagrant.ibm.com:9446",
                IGCRestClient.encodeBasicAuth("isadmin", "isadmin"));
        igcrest.registerPOJO(Term.class);
        igcrest.registerPOJO(Category.class);
        igcrest.registerPOJO(DatabaseColumn.class);
        igcrest.registerPOJO(DataFileField.class);
    }

    /**
     * Retrieves an asset by its RID and ensures it is of the expected class.
     */
    @Test
    public void getAssetById() {
        Reference term = igcrest.getAssetById(bigTermRid);
        assertTrue(term.getClass() == Term.class);
        assertTrue(((Term)term).getName().equals(bigTermName));
    }

    /**
     * Retrieves all pages of results from an asset known to have multiple pages of relationships, and ensures
     * that regardless of the paging mechanism used to retrieve them the results are equivalent.
     */
    @Test
    public void getAllPagesEquivalence() {
        // Retrieve an asset we know has multiple pages of assigned_terms
        Term term = (Term)igcrest.getAssetById(multiPageRid);
        System.out.println(term);
        assertTrue(term.getAssignedTerms().hasMorePages());
        // Retrieve all the pages via igcrest directly
        ArrayList<Reference> r1 = igcrest.getAllPages(term.getAssignedTerms().getItems(), term.getAssignedTerms().getPaging());
        // Retrieve all the pages from the relationship property itself (which should mean it has no more pages)
        term.getAssignedTerms().getAllPages(igcrest);
        assertFalse(term.getAssignedTerms().hasMorePages());
        ArrayList<Reference> r2 = term.getAssignedTerms().getItems();
        System.out.println("r1: " + r1.toString());
        System.out.println("r2: " + r2.toString());
        // The two should be identical (easiest to do a string comparison)
        assertTrue(r1.toString().equals(r2.toString()));
        assertTrue(r1.size() == r2.size());
        assertTrue(r1.size() == 14);
    }

    /**
     * Runs a simple {@link IGCSearch} and ensures the result is of the expected class.
     */
    @Test
    public void simpleTermSearch() {
        IGCSearch igcSimple = new IGCSearch("term");
        igcSimple.addProperty("modified_on");
        ReferenceList searchResult2 = igcrest.search(igcSimple);
        assertTrue(searchResult2.getItems().size() > 0);
        assertTrue(searchResult2.getItems().get(0).getClass() == Term.class);
    }

    /**
     * Runs a match-any-criteria {@link IGCSearch} and ensures the results are as expected.
     */
    @Test
    public void searchWithOr() {
        IGCSearchCondition cond1 = new IGCSearchCondition("name", "=", "Street Number");
        IGCSearchCondition cond2 = new IGCSearchCondition("name", "=", "City");
        IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet();
        igcSearchConditionSet.addCondition(cond1);
        igcSearchConditionSet.addCondition(cond2);
        igcSearchConditionSet.setMatchAnyCondition(true);
        IGCSearch igcSearch = new IGCSearch("term", igcSearchConditionSet);
        ReferenceList searchResult = igcrest.search(igcSearch);
        System.out.println(searchResult.getItems());
        for (Reference asset : searchResult.getItems()) {
            assertTrue(asset.getClass() == Term.class);
            assertTrue(asset.getName().equals("Street Number") || asset.getName().equals("City"));
        }
    }

    /**
     * Retrieves an asset by RID and then iterates through its relationships to get the additional
     * details of the relationships.
     */
    @Test
    public void getAssetDetailsOnRelationships() {
        // Starting with a term we know has multiple relationships
        Term term = (Term)igcrest.getAssetById(bigTermRid);
        term.getAssignedAssets().getAllPages(igcrest);
        System.out.println(term.getAssignedAssets());
        assertTrue(term.getAssignedAssets().getItems().size() == 6);
        for (Reference ref : term.getAssignedAssets().getItems()) {
            MainObject detailed = (MainObject) ref;
            System.out.println("Before: " + detailed);
            assertTrue(detailed.getAssignedToTerms() == null);
            detailed = (MainObject)ref.getAssetDetails(igcrest);
            System.out.println("After: " + detailed);
            Reference relation = detailed.getAssignedToTerms().getItems().get(0);
            assertTrue(relation.getClass() == Term.class);
        }
    }

    /**
     * Retrieves an asset by RID, then its full set of relationship details, and then iterates through
     * each related asset to further retrieve that related assets' relationships.
     */
    @Test
    public void getFullAssetDetailsThenPerRelationshipDetails() {
        Term term = (Term)igcrest.getAssetById(bigTermRid);
        term.getFullAssetDetails(igcrest);
        for (Reference ref : term.getAssignedAssets().getItems()) {
            MainObject detailed = (MainObject) ref.getAssetDetails(igcrest);
            assertTrue(detailed.getAssignedToTerms().getItems() != null);
            assertTrue(detailed.getAssignedToTerms().getItems().size() > 0);
            Reference relation = detailed.getAssignedToTerms().getItems().get(0);
            assertTrue(relation.getClass() == Term.class);
        }
    }

    /**
     * Retrieves an asset by RID, then iterates through the relationships to retrieve only the additional
     * context information for each related asset.
     */
    @Test
    public void populateOnlyTheContext() {
        Term term = (Term)igcrest.getAssetById(bigTermRid);
        term.getAssignedAssets().getAllPages(igcrest);
        System.out.println(term.getAssignedAssets());
        assertTrue(term.getAssignedAssets().getItems().size() == 6);
        for (Reference ref : term.getAssignedAssets().getItems()) {
            MainObject detailed = (MainObject) ref;
            System.out.println("Before: " + detailed);
            assertTrue(detailed.getContext().size() == 0);
            assertTrue(detailed.populateContext(igcrest));
            System.out.println("After: " + detailed);
            assertTrue(detailed.getContext().size() > 0);
        }
    }

    /**
     * Retrieves an asset by RID, then iterates through the relationships to retrieve only a defined subset
     * of properties for each related asset.
     */
    @Test
    public void populateOnlyASubsetOfProperties() {
        String[] properties = {"assigned_to_terms"};
        Term term = (Term)igcrest.getAssetById(bigTermRid);
        term.getAssignedAssets().getAllPages(igcrest);
        System.out.println(term.getAssignedAssets());
        assertTrue(term.getAssignedAssets().getItems().size() == 6);
        for (Reference ref : term.getAssignedAssets().getItems()) {
            MainObject detailed = (MainObject) ref;
            System.out.println("Before: " + detailed);
            assertTrue(detailed.getAssignedToTerms() == null);
            detailed = (MainObject) detailed.getAssetWithSubsetOfProperties(igcrest, properties);
            System.out.println("After: " + detailed);
            assertTrue(detailed.getAssignedToTerms().getItems().size() > 0);
        }
    }

    /**
     * Checks that workflow is not enabled in the environment.
     */
    @Test
    public void isWorkflowEnabled() {
        assertFalse(igcrest.isWorkflowEnabled());
    }

    /**
     * Retrieves an asset by RID, then iterates through its relationships: for each relationship, confirms that
     * its identity is the same whether it came from the relationship navigation or when retrieved directly by RID.
     */
    @Test
    public void identityMatching() {
        Term term = (Term)igcrest.getAssetById(bigTermRid);
        term.getAssignedAssets().getAllPages(igcrest);
        System.out.println(term.getAssignedAssets());
        assertTrue(term.getAssignedAssets().getItems().size() == 6);
        for (Reference ref : term.getAssignedAssets().getItems()) {
            MainObject detailed = (MainObject) ref;
            assertTrue(detailed.populateContext(igcrest));
            Identity identityOfRelatedAsset = detailed.getIdentity(igcrest);
            System.out.println("Related: " + identityOfRelatedAsset);
            MainObject directly = (MainObject) igcrest.getAssetById(detailed.getId());
            Identity identityOfAssetDirectly = directly.getIdentity(igcrest);
            System.out.println("Directly: " + identityOfAssetDirectly);
            assertTrue(identityOfRelatedAsset.equals(identityOfAssetDirectly));
            assertTrue(identityOfAssetDirectly.equals(identityOfRelatedAsset));
            assertTrue(!identityOfAssetDirectly.equals(term));
        }
    }

    /**
     * Retrieves an asset by RID, then retrieves its parent (container). Confirms the identity is the same when
     * retrieving the parent (container) directly as well as when retrieving via getParentContext().
     */
    @Test
    public void parentIdentityMatching() {
        Term term = (Term)igcrest.getAssetById(bigTermRid);
        System.out.println("Term's parent identity: " + term.getIdentity(igcrest).getParentIdentity());
        Category category = (Category)term.getParentCategory();
        System.out.println("Parent category's identity: " + category.getIdentity(igcrest));
        assertTrue(term.getIdentity(igcrest).getParentIdentity().equals(category.getIdentity(igcrest)));
    }

    /**
     * Tests retrieving the value of a property by its name, rather than a direct member method of the class.
     */
    @Test
    public void dynamicPropertyRetrieval() {
        Term term = (Term)igcrest.getAssetById(bigTermRid);
        System.out.println(term);
        String name = (String) term.getPropertyByName("name");
        assertTrue(name.equals(bigTermName));
    }

    /**
     * Tests data type checking methods {@link Reference#isReferenceList}, {@link Reference#isReference},
     * {@link Reference#isSimpleType} and object-specific equality checking (eg. {@link Category#isCategory(Object)}
     */
    @Test
    public void dataTypeChecks() {
        Term term = (Term)igcrest.getAssetById(bigTermRid);
        System.out.println(term);
        assertTrue(Term.isTerm(term));
        assertFalse(Label.isLabel(term));
        assertTrue(term.isReferenceList("assigned_assets"));
        assertTrue(Reference.isReferenceList(term.getAssignedAssets()));
        assertTrue(term.isSimpleType("short_description"));
        assertTrue(Reference.isSimpleType((Object)term.getTheName()));
        assertTrue(Reference.isSimpleType((Object)term.getStatus()));
        assertTrue(term.isReference("parent_category"));
        assertTrue(Reference.isReference(term.getParentCategory()));
        assertTrue(Category.isCategory(term.getParentCategory()));
    }

    /**
     * Cleanly shuts down connection (closes session) to IGC.
     */
    @AfterSuite
    public void tearDown() {
        igcrest.disconnect();
    }

}
