/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.fvt;

import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.GlossaryAuthorViewRestClient;
import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.category.GlossaryAuthorViewCategoryClient;
import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.relationships.GlossaryAuthorViewRelationshipsClient;
import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.term.GlossaryAuthorViewTermClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GenericResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

import java.io.IOException;
import java.util.List;

/**
 * FVT resource to call Glossary author view relationships API
 */
public class RelationshipsFVT {
    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Test Glossary for relationships FVT";
    private static final String DEFAULT_TEST_TERM_NAME = "Test term A1";
    private static final String DEFAULT_TEST_TERM_NAME2 = "Test term B1";
    private static final String DEFAULT_TEST_TERM_NAME3 = "Test term C1";
    private static final String DEFAULT_TEST_CAT_NAME1 = "Test cat A1";
    private static final String DEFAULT_TEST_CAT_NAME2 = "Test cat B1";
    private static final String DEFAULT_TEST_CAT_NAME3 = "Test cat C1";
    private static final String DEFAULT_TEST_CAT_NAME4 = "Test cat D1";
    private static final String DEFAULT_TEST_PROJECT_NAME = "Test Project for relationships FVT";
    private GlossaryAuthorViewRelationshipsClient glossaryAuthorViewRelationshipsClient = null;
    private GlossaryAuthorViewCategoryClient glossaryAuthorViewCategory = null;
    private GlossaryAuthorViewTermClient glossaryAuthorViewTerm = null;
    private GlossaryFVT glossaryFVT = null;
    private TermFVT termFVT = null;
    private CategoryFVT catFVT = null;
    private ProjectFVT projectFVT = null;
    private String url = null;
    private String serverName = null;
    private String userId = null;
    private static Logger log = LoggerFactory.getLogger(RelationshipsFVT.class);


    private static final String HAS_A = "has-as";
    private static final String RELATED_TERM = "related-terms";
    private static final String SYNONYM = "synonyms";
    private static final String ANTONYM = "antonyms";
    private static final String TRANSLATION = "translations";
    private static final String USED_IN_CONTEXT = "used-in-contexts";
    private static final String PREFERRED_TERM = "preferred-terms";
    private static final String VALID_VALUE = "valid-values";
    private static final String REPLACEMENT_TERM = "replacement-terms";
    private static final String TYPED_BY = "typed-bys";
    private static final String IS_A = "is-as";
    private static final String IS_A_TYPE_OF_DEPRECATED = "is-a-type-of-deprecateds";
    private static final String IS_A_TYPE_OF = "is-a-type-ofs";
    private static final String TERM_CATEGORIZATION = "term-categorizations";
    private static final String SEMANTIC_ASSIGNMENT = "semantic-assignments";
    private static final String TERM_ANCHOR = "term-anchor";
    private static final String CATEGORY_ANCHOR = "category-anchor";
    private static final String PROJECT_SCOPE = "project-scopes";
    private static final String CATEGORY_HIERARCHY_LINK = "category-hierarchy-links";

    public RelationshipsFVT(String url, String serverName, String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        this.url = url;
        GlossaryAuthorViewRestClient client = new GlossaryAuthorViewRestClient(serverName, url);
        glossaryAuthorViewRelationshipsClient = new GlossaryAuthorViewRelationshipsClient(client);
        glossaryAuthorViewCategory = new GlossaryAuthorViewCategoryClient(client);
        glossaryAuthorViewTerm = new GlossaryAuthorViewTermClient(client);
        termFVT = new TermFVT(url, serverName, userId);
        catFVT = new CategoryFVT(url, serverName, userId);
        glossaryFVT = new GlossaryFVT(url, serverName, userId);
        projectFVT = new ProjectFVT(url, serverName, userId);
        this.serverName = serverName;
        this.userId = userId;
    }

    /**
     * Delete the nodes that have been created to test relationships. This method deletes all those nodes - so the
     * associated relationships will be deleted as the associated node is deleted.
     * @throws UserNotAuthorizedException
     * @throws PropertyServerException
     * @throws InvalidParameterException
     * @throws GlossaryAuthorFVTCheckedException
     */
    void deleteRemaining() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, GlossaryAuthorFVTCheckedException {
        catFVT.deleteRemainingCategories();
        termFVT.deleteRemainingTerms();
        glossaryFVT.deleteRemainingGlossaries();
        projectFVT.deleteRemainingProjects();
    }

    public static void main(String args[]) {
        try {
            String url = RunAllFVTOn2Servers.getUrl(args);
            runWith2Servers(url);
        } catch (IOException e1) {
            System.out.println("Error getting user input");
        } catch (GlossaryAuthorFVTCheckedException e) {
            log.error("ERROR: " + e.getMessage());
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            log.error("ERROR: " + e.getReportedErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        }

    }

    public static void runWith2Servers(String url) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        runIt(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        runIt(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
    }

    public static void runIt(String url, String serverName, String userId) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        try
        {
            System.out.println("relationshipFVT runIt started");
            RelationshipsFVT fvt = new RelationshipsFVT(url, serverName, userId);
            fvt.run();
            fvt.deleteRemaining();
            System.out.println("relationshipFVT runIt stopped");
        }
        catch (Exception error) {
            log.error("The FVT Encountered an Exception", error);
            throw error;
        }
    }

    public void run() throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        if (log.isDebugEnabled()) {
            log.debug("Create a glossary");
        }

        int term1relationshipcount = 0;
        int term2relationshipcount = 0;
        int term3relationshipcount = 0;
        int glossaryRelationshipCount = 0;
        int cat1RelationshipCount = 0;
        int cat2RelationshipCount = 0;

        Glossary glossary = glossaryFVT.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
        if (log.isDebugEnabled()) {
            log.debug("Create a term called " + DEFAULT_TEST_TERM_NAME + " using glossary GUID");
        }
        String glossaryGuid = glossary.getSystemAttributes().getGUID();
        Term term1 = termFVT.createTerm(DEFAULT_TEST_TERM_NAME, glossaryGuid);

        term1relationshipcount++;
        glossaryRelationshipCount++;
        checkRelationshipNumberforGlossary(glossaryRelationshipCount, glossary);
        checkRelationshipNumberforTerm(term1relationshipcount, term1);
        FVTUtils.validateNode(term1);
        if (log.isDebugEnabled()) {
            log.debug("Create a term called " + DEFAULT_TEST_TERM_NAME2 + " using glossary GUID");
        }
        Term term2 = termFVT.createTerm(DEFAULT_TEST_TERM_NAME2, glossaryGuid);
        term2relationshipcount++;
        glossaryRelationshipCount++;
        checkRelationshipNumberforGlossary(glossaryRelationshipCount, glossary);
        checkRelationshipNumberforTerm(term2relationshipcount, term2);
        FVTUtils.validateNode(term2);
        if (log.isDebugEnabled()) {
            log.debug("Create a term called " + DEFAULT_TEST_TERM_NAME3 + " using glossary GUID");
        }
        Term term3 = termFVT.createTerm(DEFAULT_TEST_TERM_NAME3, glossaryGuid);
        FVTUtils.validateNode(term3);
        term3relationshipcount++;
        glossaryRelationshipCount++;
        checkRelationshipNumberforGlossary(glossaryRelationshipCount, glossary);
        checkRelationshipNumberforTerm(term3relationshipcount, term3);
        Category cat1 = catFVT.createCategory(DEFAULT_TEST_CAT_NAME1, glossaryGuid);
        glossaryRelationshipCount++;
        checkRelationshipNumberforGlossary(glossaryRelationshipCount, glossary);
        cat1RelationshipCount++;
        checkRelationshipNumberforCategory(cat1RelationshipCount, cat1);
        Category cat2 = catFVT.createCategory(DEFAULT_TEST_CAT_NAME2, glossaryGuid);
        glossaryRelationshipCount++;
        checkRelationshipNumberforGlossary(glossaryRelationshipCount, glossary);
        cat2RelationshipCount++;
        checkRelationshipNumberforCategory(cat2RelationshipCount, cat1);
        synonymFVT(term1, term2);
        antonymFVT(term1, term3);
        relatedtermFVT(term1, term3);
        hasaFVT(term1, term3);
        translationFVT(term1, term2);
        usedincontextFVT(term1, term2);
        preferredtermFVT(term1, term2);
        validvalueFVT(term1, term2);
        replacementTermFVT(term1, term2);
        typedByFVT(term1, term2);
        isaFVT(term1, term2);
      //  isatypeofFVT(term1, term2);
        isATypeOfFVT(term1, term2);
        termCategorizationFVT(term1, cat1);
        /**/
        // No TermAnchor or CategoryAnchor tests as these are anchor relationships that cannot be  modified directly in the subject Area API.
        createSomeTermRelationships(term1, term2, term3);
        term1relationshipcount = term1relationshipcount + 12;
        term2relationshipcount = term2relationshipcount + 11;
        term3relationshipcount = term3relationshipcount + 1;
        checkRelationshipNumberforTerm(term1relationshipcount, term1);
        checkRelationshipNumberforTerm(term2relationshipcount, term2);
        checkRelationshipNumberforTerm(term3relationshipcount, term3);

        FVTUtils.validateRelationship(createTermCategorization(term1, cat1));
        term1relationshipcount++;
        cat1RelationshipCount++;
        checkRelationshipNumberforCategory(cat1RelationshipCount, cat1);
        FVTUtils.validateRelationship(createTermCategorization(term1, cat2));
        term1relationshipcount++;
        cat2RelationshipCount++;
        checkRelationshipNumberforCategory(cat2RelationshipCount, cat1);
        FVTUtils.validateRelationship(createTermCategorization(term2, cat1));
        cat1RelationshipCount++;
        checkRelationshipNumberforCategory(cat1RelationshipCount, cat1);
        term2relationshipcount++;
        FVTUtils.validateRelationship(createTermCategorization(term3, cat1));

        term3relationshipcount++;
        cat1RelationshipCount++;
        checkRelationshipNumberforCategory(cat1RelationshipCount, cat1);
        checkRelationshipNumberforTerm(term1relationshipcount, term1);
        checkRelationshipNumberforTerm(term2relationshipcount, term2);
        checkRelationshipNumberforTerm(term3relationshipcount, term3);
        if (log.isDebugEnabled()) {
            log.debug("get term relationships");
        }
        List<Relationship> term1Relationships = termFVT.getTermRelationships(term1);

        if (log.isDebugEnabled()) {
            log.debug("Get paged term relationships");
        }
        int offset = 0;

        int numberofrelationships = 0;
        while (offset < term1relationshipcount) {
            if (log.isDebugEnabled()) {
                log.debug("Get paged term relationships offset = " + offset + ",pageSize=3");
            }
            List<Relationship> term1PagedRelationships = termFVT.getTermRelationships(term1, null, offset, 3, SequencingOrder.GUID, null);
            numberofrelationships = numberofrelationships + term1PagedRelationships.size();
            if (log.isDebugEnabled()) {
                log.debug("numberofrelationships = " + numberofrelationships  + " term1PagedRelationships.size = " + term1PagedRelationships.size());
            }
            offset += 3;
        }

        if (term1relationshipcount != numberofrelationships) {
            throw new GlossaryAuthorFVTCheckedException("Expected " + term1Relationships.size() + " got " + numberofrelationships  );
        }


        Project project= projectFVT.createProject(DEFAULT_TEST_PROJECT_NAME );

        projectScopeFVT(project, term1);
        projectFVT.deleteProject(project.getSystemAttributes().getGUID());

        Category cat3 = catFVT.createCategory(DEFAULT_TEST_CAT_NAME3, glossaryGuid);
        Category cat4 = catFVT.createCategory(DEFAULT_TEST_CAT_NAME4, glossaryGuid);
        categoryHierarchyLinkFVT(cat3, cat4);

    }

    private void checkRelationshipNumberforTerm(int expectedrelationshipcount, Term term) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        int actualCount = termFVT.getTermRelationships(term).size();
        if (expectedrelationshipcount != actualCount) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: expected " + expectedrelationshipcount + " for " + term.getName() + " got " + actualCount);
        }
    }

    private void checkRelationshipNumberforGlossary(int expectedrelationshipcount, Glossary glossary) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException {
        int actualCount = glossaryFVT.getGlossaryRelationships(glossary).size();
        if (expectedrelationshipcount != actualCount) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: expected " + expectedrelationshipcount + " for " + glossary.getName() + " got " + actualCount);
        }
    }

    private void checkRelationshipNumberforCategory(int expectedrelationshipcount, Category category) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException {
        int actualCount = catFVT.getCategoryRelationships(category).size();
        if (expectedrelationshipcount != actualCount) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: expected " + expectedrelationshipcount + " for " + category.getName() + " got " + actualCount);
        }
    }

        private void createSomeTermRelationships(Term term1, Term term2, Term term3) throws InvalidParameterException, PropertyServerException, GlossaryAuthorFVTCheckedException, UserNotAuthorizedException {

            FVTUtils.validateRelationship(createValidValue(term1, term2));
            FVTUtils.validateRelationship(createAntonym(term1, term2));
            FVTUtils.validateRelationship(createIsaRelationship(term1, term2));
            FVTUtils.validateRelationship(createPreferredTerm(term1, term2));
            FVTUtils.validateRelationship(createRelatedTerm(term1, term2));
            FVTUtils.validateRelationship(createHasA(term1, term2));
            FVTUtils.validateRelationship(createSynonym(term1, term3));
            FVTUtils.validateRelationship(createReplacementTerm(term1, term2));
            FVTUtils.validateRelationship(createTermTYPEDBYRelationship(term1, term2));
            FVTUtils.validateRelationship(createTranslation(term1, term2));
            FVTUtils.validateRelationship(createUsedInContext(term1, term2));
            FVTUtils.validateRelationship(createIsATypeOf(term1, term2));
//            FVTUtils.validateRelationship(createIsATypeOf(term1, term2));
        }

        public IsATypeOf createIsATypeOf(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException {
            IsATypeOf isATypeOf = new IsATypeOf();
            isATypeOf.setDescription("ddd");
            isATypeOf.setSource("source");
            isATypeOf.setSteward("Stew");
            isATypeOf.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
            isATypeOf.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());

            ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, IsATypeOf.class);
            ParameterizedTypeReference<GenericResponse<IsATypeOf>> type = ParameterizedTypeReference.forType(resolvableType.getType());


            IsATypeOf createdisATypeOf = glossaryAuthorViewRelationshipsClient.createRel(this.userId, isATypeOf, type ,IS_A_TYPE_OF);

            FVTUtils.validateRelationship(createdisATypeOf);
            FVTUtils.checkEnds(isATypeOf, createdisATypeOf, "isATypeOf", "create");

            if (log.isDebugEnabled()) {
                log.debug("Created isATypeOf Relationship " + createdisATypeOf);
            }
            return createdisATypeOf;
        }

        private void isATypeOfFVT(Term term1, Term term2) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, GlossaryAuthorFVTCheckedException {
            String relType = IS_A_TYPE_OF;
            ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, IsATypeOf.class);
            ParameterizedTypeReference<GenericResponse<IsATypeOf>> type = ParameterizedTypeReference.forType(resolvableType.getType());


            IsATypeOf createdIsATypeOf = createIsATypeOf(term1, term2);
            String guid = createdIsATypeOf.getGuid();
            if (log.isDebugEnabled()) {
                log.debug("Created IsaTypeOf " + createdIsATypeOf);
            }


            IsATypeOf gotIsATypeOf = glossaryAuthorViewRelationshipsClient.getRel(this.userId, guid,type,relType);
            FVTUtils.validateRelationship(gotIsATypeOf);
            if (log.isDebugEnabled()) {
                log.debug("Got IsaTypeOf " + gotIsATypeOf);
            }

            IsATypeOf updateIsATypeOf = new IsATypeOf();
            updateIsATypeOf.setDescription("ddd2");
            IsATypeOf updatedIsATypeOf = glossaryAuthorViewRelationshipsClient.updateRel(this.userId, guid, updateIsATypeOf,type,relType,false);
            FVTUtils.validateRelationship(updatedIsATypeOf);
            if (!updatedIsATypeOf.getDescription().equals(updateIsATypeOf.getDescription())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: IsaTypeOf update description not as expected");
            }
            if (!updatedIsATypeOf.getSource().equals(createdIsATypeOf.getSource())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: IsaTypeOf update source not as expected");
            }
            if (!updatedIsATypeOf.getSteward().equals(createdIsATypeOf.getSteward())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: IsaTypeOf update steward not as expected");
            }
            FVTUtils.checkEnds(updatedIsATypeOf,createdIsATypeOf,"IsATypeOf","update");
            if (log.isDebugEnabled()) {
                log.debug("Updated IsaTypeOf " + updatedIsATypeOf);
            }
            IsATypeOf replaceIsATypeOf = new IsATypeOf();
            replaceIsATypeOf.setDescription("ddd3");
            IsATypeOf replacedIsATypeOf = glossaryAuthorViewRelationshipsClient.replaceRel(this.userId, guid, replaceIsATypeOf,type,relType,true);
            FVTUtils.validateRelationship(replacedIsATypeOf);
            if (!replacedIsATypeOf.getDescription().equals(replaceIsATypeOf.getDescription())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: IsaTypeOf replace description not as expected");
            }
            if (replacedIsATypeOf.getSource() != null) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: IsaTypeOf replace source not as expected");
            }
            if (replacedIsATypeOf.getSteward() != null) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: IsaTypeOf replace steward not as expected");
            }
            if (!replacedIsATypeOf.getEnd1().getNodeGuid().equals(createdIsATypeOf.getEnd1().getNodeGuid())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: IsaTypeOf replace end 1 not as expected");
            }
            if (!replacedIsATypeOf.getEnd2().getNodeGuid().equals(createdIsATypeOf.getEnd2().getNodeGuid())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: IsaTypeOf replace end 2 not as expected");
            }
            if (log.isDebugEnabled()) {
                log.debug("Replaced IsaTypeOf " + replacedIsATypeOf);
            }
            glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
            //FVTUtils.validateLine(gotIsATypeOf);
            if (log.isDebugEnabled()) {
                log.debug("Deleted IsaTypeOf with userId=" + guid);
            }
            gotIsATypeOf = glossaryAuthorViewRelationshipsClient.restoreRel(this.userId, guid,type, relType);
            FVTUtils.validateRelationship(gotIsATypeOf);
            if (log.isDebugEnabled()) {
                log.debug("Restored IsaTypeOf with userId=" + guid);
            }
            glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
            //FVTUtils.validateLine(gotIsATypeOf);
            if (log.isDebugEnabled()) {
                log.debug("Deleted IsaTypeOf with userId=" + guid);
            }

        }



        private void isaFVT(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, GlossaryAuthorFVTCheckedException, UserNotAuthorizedException {

            String relType = IS_A;
            ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, IsA.class);
            ParameterizedTypeReference<GenericResponse<IsA>> type = ParameterizedTypeReference.forType(resolvableType.getType());

            IsA createdIsA = createIsaRelationship(term1, term2);
            FVTUtils.validateRelationship(createdIsA);
            if (log.isDebugEnabled()) {
                log.debug("Created Isa " + createdIsA);
            }
            String guid = createdIsA.getGuid();

            IsA gotIsA = glossaryAuthorViewRelationshipsClient.getRel(this.userId, guid,type, relType);
            FVTUtils.validateRelationship(gotIsA);
            if (log.isDebugEnabled()) {
                log.debug("Got Isa " + gotIsA);
            }

            IsA updateIsA = new IsA();
            updateIsA.setDescription("ddd2");
            IsA updatedIsA = glossaryAuthorViewRelationshipsClient.updateRel(this.userId, guid, updateIsA,type,relType,false);
            if (!updatedIsA.getDescription().equals(updateIsA.getDescription())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: isa update description not as expected");
            }
            if (!updatedIsA.getSource().equals(createdIsA.getSource())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: isa update source not as expected");
            }
            if (!updatedIsA.getExpression().equals(createdIsA.getExpression())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: isa update expression not as expected");
            }
            if (!updatedIsA.getSteward().equals(createdIsA.getSteward())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: isa update steward not as expected");
            }
            if (!updatedIsA.getEnd1().getNodeGuid().equals(createdIsA.getEnd1().getNodeGuid())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: isa update end 1 not as expected");
            }
            if (!updatedIsA.getEnd2().getNodeGuid().equals(createdIsA.getEnd2().getNodeGuid())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: isa update end 2 not as expected");
            }
            if (log.isDebugEnabled()) {
                log.debug("Updated Isa " + updatedIsA);
            }
            IsA replaceIsA = new IsA();
            replaceIsA.setDescription("ddd3");
            IsA replacedIsA = glossaryAuthorViewRelationshipsClient.replaceRel(this.userId, guid, replaceIsA,type,relType,true);
            FVTUtils.validateRelationship(replacedIsA);
            if (!replacedIsA.getDescription().equals(replaceIsA.getDescription())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: isa replace description not as expected");
            }
            if (replacedIsA.getSource() != null) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: isa replace source not as expected");
            }
            if (replacedIsA.getExpression() != null) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: isa replace expression not as expected");
            }
            if (replacedIsA.getSteward() != null) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: isa replace steward not as expected");
            }
            if (!replacedIsA.getEnd1().getNodeGuid().equals(createdIsA.getEnd1().getNodeGuid())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: isa replace end 1 not as expected");
            }
            if (!replacedIsA.getEnd2().getNodeGuid().equals(createdIsA.getEnd2().getNodeGuid())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: isa replace end 2 not as expected");
            }
            if (log.isDebugEnabled()) {
                log.debug("Replaced Isa " + replacedIsA);
            }
            glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
            //FVTUtils.validateLine(gotIsa);
            if (log.isDebugEnabled()) {
                log.debug("Deleted Isa with userId=" + guid);
            }
            gotIsA = glossaryAuthorViewRelationshipsClient.restoreRel(this.userId, guid,type,relType);
            FVTUtils.validateRelationship(gotIsA);
            if (log.isDebugEnabled()) {
                log.debug("Restored Isa with userId=" + guid);
            }
            glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
            //FVTUtils.validateLine(gotIsa);
            if (log.isDebugEnabled()) {
                log.debug("Deleted Isa with userId=" + guid);
            }
        }


        private IsA createIsaRelationship(Term term1, Term term2) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
            IsA isa = new IsA();
            isa.setDescription("ddd");
            isa.setExpression("Ex");
            isa.setSource("source");
            isa.setSteward("Stew");
            isa.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
            isa.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());

            ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, IsA.class);
            ParameterizedTypeReference<GenericResponse<IsA>> type = ParameterizedTypeReference.forType(resolvableType.getType());


            IsA createdIsA = glossaryAuthorViewRelationshipsClient.createRel(this.userId, isa,type,IS_A);
            FVTUtils.validateRelationship(createdIsA);
            FVTUtils.checkEnds(isa, createdIsA, "isa", "create");

            return createdIsA;
        }

        private void typedByFVT(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, GlossaryAuthorFVTCheckedException, UserNotAuthorizedException {
            String relType = TYPED_BY;
            ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, TypedBy.class);
            ParameterizedTypeReference<GenericResponse<TypedBy>> type = ParameterizedTypeReference.forType(resolvableType.getType());

            TypedBy createdTermTYPEDBYRelationship = createTermTYPEDBYRelationship(term1, term2);
            FVTUtils.validateRelationship(createdTermTYPEDBYRelationship);
            if (log.isDebugEnabled()) {
                log.debug("Created TypedBy " + createdTermTYPEDBYRelationship);
            }
            String guid = createdTermTYPEDBYRelationship.getGuid();

            TypedBy gotTermTYPEDBYRelationship = glossaryAuthorViewRelationshipsClient.getRel(this.userId, guid,type,relType);
            FVTUtils.validateRelationship(gotTermTYPEDBYRelationship);
            if (log.isDebugEnabled()) {
                log.debug("Got TypedBy " + gotTermTYPEDBYRelationship);
            }

            TypedBy updateTermTYPEDBYRelationship = new TypedBy();
            updateTermTYPEDBYRelationship.setDescription("ddd2");
            TypedBy updatedTermTYPEDBYRelationship = glossaryAuthorViewRelationshipsClient.updateRel(this.userId, guid, updateTermTYPEDBYRelationship,type, relType, false);
            FVTUtils.validateRelationship(updatedTermTYPEDBYRelationship);
            if (!updatedTermTYPEDBYRelationship.getDescription().equals(updateTermTYPEDBYRelationship.getDescription())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: termTYPEDBYRelationship update description not as expected");
            }
            if (!updatedTermTYPEDBYRelationship.getSource().equals(createdTermTYPEDBYRelationship.getSource())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: termTYPEDBYRelationship update source not as expected");
            }
            if (!updatedTermTYPEDBYRelationship.getSteward().equals(createdTermTYPEDBYRelationship.getSteward())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: termTYPEDBYRelationship update steward not as expected");
            }
            FVTUtils.checkEnds(updatedTermTYPEDBYRelationship,createdTermTYPEDBYRelationship,"TYPEDBY","update");
            if (log.isDebugEnabled()) {
                log.debug("Updated TypedBy " + updatedTermTYPEDBYRelationship);
            }
            TypedBy replaceTermTYPEDBYRelationship = new TypedBy();
            replaceTermTYPEDBYRelationship.setDescription("ddd3");
            TypedBy replacedTermTYPEDBYRelationship = glossaryAuthorViewRelationshipsClient.replaceRel(this.userId, guid, replaceTermTYPEDBYRelationship,type,relType, true);
            FVTUtils.validateRelationship(replacedTermTYPEDBYRelationship);
            if (!replacedTermTYPEDBYRelationship.getDescription().equals(replaceTermTYPEDBYRelationship.getDescription())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: termTYPEDBYRelationship replace description not as expected");
            }
            if (replacedTermTYPEDBYRelationship.getSource() != null) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: termTYPEDBYRelationship replace source not as expected");
            }
            if (replacedTermTYPEDBYRelationship.getSteward() != null) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: termTYPEDBYRelationship replace steward not as expected");
            }

            FVTUtils.checkEnds(replacedTermTYPEDBYRelationship,createdTermTYPEDBYRelationship,"TYPEDBY","replace");
            if (log.isDebugEnabled()) {
                log.debug("Replaced TypedBy " + replacedTermTYPEDBYRelationship);
            }
            glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
            //FVTUtils.validateLine(gotTermTYPEDBYRelationship);
            if (log.isDebugEnabled()) {
                log.debug("Deleted TypedBy with userId=" + guid);
            }
            gotTermTYPEDBYRelationship = glossaryAuthorViewRelationshipsClient.restoreRel(this.userId, guid,type, relType);
            FVTUtils.validateRelationship(gotTermTYPEDBYRelationship);
            if (log.isDebugEnabled()) {
                log.debug("Restored TypedBy with userId=" + guid);
            }
            glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
            //FVTUtils.validateLine(gotTermTYPEDBYRelationship);
            if (log.isDebugEnabled()) {
                log.debug("Deleted TypedBy with userId=" + guid);
            }
        }

        private TypedBy createTermTYPEDBYRelationship(Term term1, Term term2) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
            TypedBy termTYPEDBYRelationship = new TypedBy();
            termTYPEDBYRelationship.setDescription("ddd");
            termTYPEDBYRelationship.setSource("source");
            termTYPEDBYRelationship.setSteward("Stew");
            termTYPEDBYRelationship.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
            termTYPEDBYRelationship.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());

            ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, TypedBy.class);
            ParameterizedTypeReference<GenericResponse<TypedBy>> type = ParameterizedTypeReference.forType(resolvableType.getType());

            TypedBy createdTermTYPEDBYRelationship = glossaryAuthorViewRelationshipsClient.createRel(this.userId,termTYPEDBYRelationship,type,TYPED_BY);
            FVTUtils.validateRelationship(createdTermTYPEDBYRelationship);
            FVTUtils.checkEnds(termTYPEDBYRelationship, createdTermTYPEDBYRelationship, "TypedBy", "create");

            return createdTermTYPEDBYRelationship;
        }

        private void replacementTermFVT(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, GlossaryAuthorFVTCheckedException, UserNotAuthorizedException {

            String relType = REPLACEMENT_TERM;
            ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, ReplacementTerm.class);
            ParameterizedTypeReference<GenericResponse<ReplacementTerm>> type = ParameterizedTypeReference.forType(resolvableType.getType());


            ReplacementTerm createdReplacementTerm = createReplacementTerm(term1, term2);
            FVTUtils.validateRelationship(createdReplacementTerm);
            if (log.isDebugEnabled()) {
                log.debug("Created ReplacementTerm " + createdReplacementTerm);
            }
            String guid = createdReplacementTerm.getGuid();

            ReplacementTerm gotReplacementTerm = glossaryAuthorViewRelationshipsClient.getRel(this.userId, guid, type,relType);
            FVTUtils.validateRelationship(gotReplacementTerm);
            if (log.isDebugEnabled()) {
                log.debug("Got ReplacementTerm " + gotReplacementTerm);
            }

            ReplacementTerm updateReplacementTerm = new ReplacementTerm();
            updateReplacementTerm.setDescription("ddd2");
            ReplacementTerm updatedReplacementTerm = glossaryAuthorViewRelationshipsClient.updateRel(this.userId, guid, updateReplacementTerm,type,relType,false);
            FVTUtils.validateRelationship(updatedReplacementTerm);
            if (!updatedReplacementTerm.getDescription().equals(updateReplacementTerm.getDescription())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: replacementTerm update description not as expected");
            }
            if (!updatedReplacementTerm.getSource().equals(createdReplacementTerm.getSource())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: replacementTerm update source not as expected");
            }
            if (!updatedReplacementTerm.getExpression().equals(createdReplacementTerm.getExpression())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: replacementTerm update expression not as expected");
            }
            if (!updatedReplacementTerm.getSteward().equals(createdReplacementTerm.getSteward())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: replacementTerm update steward not as expected");
            }
            FVTUtils.checkEnds(updatedReplacementTerm,createdReplacementTerm,"replacementTerm","update");
            if (log.isDebugEnabled()) {
                log.debug("Updated ReplacementTerm " + updatedReplacementTerm);
            }
            ReplacementTerm replaceReplacementTerm = new ReplacementTerm();
            replaceReplacementTerm.setDescription("ddd3");
            ReplacementTerm replacedReplacementTerm = glossaryAuthorViewRelationshipsClient.replaceRel(this.userId, guid, replaceReplacementTerm, type,relType,true);
            FVTUtils.validateRelationship(replacedReplacementTerm);
            if (!replacedReplacementTerm.getDescription().equals(replaceReplacementTerm.getDescription())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: replacementTerm replace description not as expected");
            }
            if (replacedReplacementTerm.getSource() != null) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: replacementTerm replace source not as expected");
            }
            if (replacedReplacementTerm.getExpression() != null) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: replacementTerm replace expression not as expected");
            }
            if (replacedReplacementTerm.getSteward() != null) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: replacementTerm replace steward not as expected");
            }

            FVTUtils.checkEnds(replacedReplacementTerm,createdReplacementTerm,"replacementTerm","replace");
            if (log.isDebugEnabled()) {
                log.debug("Replaced ReplacementTerm " + replacedReplacementTerm);
            }
            glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
            //FVTUtils.validateLine(gotReplacementTerm);
            if (log.isDebugEnabled()) {
                log.debug("Deleted ReplacementTerm with userId=" + guid);
            }
            gotReplacementTerm = glossaryAuthorViewRelationshipsClient.restoreRel(this.userId, guid,type,relType);
            FVTUtils.validateRelationship(gotReplacementTerm);
            if (log.isDebugEnabled()) {
                log.debug("Restored ReplacementTerm with userId=" + guid);
            }
            glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
            //FVTUtils.validateLine(gotReplacementTerm);
            if (log.isDebugEnabled()) {
                log.debug("Deleted ReplacementTerm with userId=" + guid);
            }
        }

        private ReplacementTerm createReplacementTerm(Term term1, Term term2) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
            ReplacementTerm replacementTerm = new ReplacementTerm();
            replacementTerm.setDescription("ddd");
            replacementTerm.setExpression("Ex");
            replacementTerm.setSource("source");
            replacementTerm.setSteward("Stew");
            replacementTerm.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
            replacementTerm.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());
            ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, ReplacementTerm.class);
            ParameterizedTypeReference<GenericResponse<ReplacementTerm>> type = ParameterizedTypeReference.forType(resolvableType.getType());

            ReplacementTerm createdReplacementTerm = glossaryAuthorViewRelationshipsClient.createRel(this.userId, replacementTerm,type, REPLACEMENT_TERM);
            FVTUtils.validateRelationship(createdReplacementTerm);
            FVTUtils.checkEnds(replacementTerm, createdReplacementTerm, "ReplacementTerm", "create");

            return createdReplacementTerm;
        }

        private void validvalueFVT(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, GlossaryAuthorFVTCheckedException, UserNotAuthorizedException {

            String relType = VALID_VALUE;
            ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, ValidValue.class);
            ParameterizedTypeReference<GenericResponse<ValidValue>> type = ParameterizedTypeReference.forType(resolvableType.getType());

            ValidValue createdValidValue = createValidValue(term1, term2);
            FVTUtils.validateRelationship(createdValidValue);
            if (log.isDebugEnabled()) {
                log.debug("Created ValidValue " + createdValidValue);
            }
            String guid = createdValidValue.getGuid();

            ValidValue gotValidValue = glossaryAuthorViewRelationshipsClient.getRel(this.userId, guid,type, relType);
            FVTUtils.validateRelationship(gotValidValue);
            if (log.isDebugEnabled()) {
                log.debug("Got ValidValue " + gotValidValue);
            }

            ValidValue updateValidValue = new ValidValue();
            updateValidValue.setDescription("ddd2");
            ValidValue updatedValidValue = glossaryAuthorViewRelationshipsClient.updateRel(this.userId, guid, updateValidValue,type,relType,false);
            if (!updatedValidValue.getDescription().equals(updateValidValue.getDescription())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: validValue update description not as expected");
            }
            if (!updatedValidValue.getSource().equals(createdValidValue.getSource())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: validValue update source not as expected");
            }
            if (!updatedValidValue.getExpression().equals(createdValidValue.getExpression())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: validValue update expression not as expected");
            }
            if (!updatedValidValue.getSteward().equals(createdValidValue.getSteward())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: validValue update steward not as expected");
            }

            FVTUtils.checkEnds(updatedValidValue, createdValidValue, "ValidValue", "update");

            if (log.isDebugEnabled()) {
                log.debug("Updated ValidValue " + updateValidValue);
            }
            ValidValue replaceValidValue = new ValidValue();
            replaceValidValue.setDescription("ddd3");
            replaceValidValue.setGuid(createdValidValue.getGuid());
            ValidValue replacedValidValue = glossaryAuthorViewRelationshipsClient.replaceRel(this.userId, guid, replaceValidValue, type,relType,true);
            if (!replacedValidValue.getDescription().equals(replaceValidValue.getDescription())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: validValue replace description not as expected");
            }
            if (replacedValidValue.getSource() != null) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: validValue replace source not as expected");
            }
            if (replacedValidValue.getExpression() != null) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: validValue replace expression not as expected");
            }
            if (replacedValidValue.getSteward() != null) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: validValue replace steward not as expected");
            }
            FVTUtils.checkEnds(replacedValidValue, createdValidValue, "ValidValue", "replace");

            if (log.isDebugEnabled()) {
                log.debug("Replaced ValidValue " + replaceValidValue);
            }
            glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
            //FVTUtils.validateLine(gotValidValue);
            if (log.isDebugEnabled()) {
                log.debug("Deleted ValidValue with userId=" + guid);
            }
            gotValidValue = glossaryAuthorViewRelationshipsClient.restoreRel(this.userId, guid,type,relType);
            FVTUtils.validateRelationship(gotValidValue);
            if (log.isDebugEnabled()) {
                log.debug("Restored ValidValue with userId=" + guid);
            }
            glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
            //FVTUtils.validateLine(gotValidValue);
            if (log.isDebugEnabled()) {
                log.debug("Deleted ValidValue with userId=" + guid);
            }
        }

        private ValidValue createValidValue(Term term1, Term term2) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
            ValidValue validValue = new ValidValue();
            validValue.setDescription("ddd");
            validValue.setExpression("Ex");
            validValue.setSource("source");
            validValue.setSteward("Stew");
            validValue.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
            validValue.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());

            ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, ValidValue.class);
            ParameterizedTypeReference<GenericResponse<ValidValue>> type = ParameterizedTypeReference.forType(resolvableType.getType());

            ValidValue createdValidValue = glossaryAuthorViewRelationshipsClient.createRel(this.userId, validValue,type, VALID_VALUE);
            FVTUtils.validateRelationship(createdValidValue);
            FVTUtils.checkEnds(validValue, createdValidValue, "ValidValue", "create");

            return createdValidValue;
        }

        private void preferredtermFVT(Term term1, Term term2) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, GlossaryAuthorFVTCheckedException {

            String relType = PREFERRED_TERM;
            ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, PreferredTerm.class);
            ParameterizedTypeReference<GenericResponse<PreferredTerm>> type = ParameterizedTypeReference.forType(resolvableType.getType());


            PreferredTerm createdPreferredTerm = createPreferredTerm(term1, term2);
            FVTUtils.validateRelationship(createdPreferredTerm);
            if (log.isDebugEnabled()) {
                log.debug("Created PreferredTerm " + createdPreferredTerm);
            }
            String guid = createdPreferredTerm.getGuid();

            PreferredTerm gotPreferredTerm = glossaryAuthorViewRelationshipsClient.getRel(this.userId, guid,type, relType);
            FVTUtils.validateRelationship(gotPreferredTerm);
            if (log.isDebugEnabled()) {
                log.debug("Got PreferredTerm " + gotPreferredTerm);
            }

            PreferredTerm updatePreferredTerm = new PreferredTerm();
            updatePreferredTerm.setDescription("ddd2");
            PreferredTerm updatedPreferredTerm = glossaryAuthorViewRelationshipsClient.updateRel(this.userId, guid, updatePreferredTerm,type,relType,false);
            FVTUtils.validateRelationship(updatedPreferredTerm);
            if (!updatedPreferredTerm.getDescription().equals(updatePreferredTerm.getDescription())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: preferredTerm update description not as expected");
            }
            if (!updatedPreferredTerm.getSource().equals(createdPreferredTerm.getSource())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: preferredTerm update source not as expected");
            }
            if (!updatedPreferredTerm.getExpression().equals(createdPreferredTerm.getExpression())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: preferredTerm update expression not as expected");
            }
            if (!updatedPreferredTerm.getSteward().equals(createdPreferredTerm.getSteward())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: preferredTerm update steward not as expected");
            }
            FVTUtils.checkEnds(updatedPreferredTerm,createdPreferredTerm,"PreferredTerm","update");
            if (log.isDebugEnabled()) {
                log.debug("Updated PreferredTerm " + updatedPreferredTerm);
            }
            PreferredTerm replacePreferredTerm = new PreferredTerm();
            replacePreferredTerm.setDescription("ddd3");
            PreferredTerm replacedPreferredTerm = glossaryAuthorViewRelationshipsClient.replaceRel(this.userId, guid, replacePreferredTerm, type,relType,true);
            FVTUtils.validateRelationship(replacedPreferredTerm);
            if (!replacedPreferredTerm.getDescription().equals(replacePreferredTerm.getDescription())) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: preferredTerm replace description not as expected");
            }
            if (replacedPreferredTerm.getSource() != null) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: preferredTerm replace source not as expected");
            }
            if (replacedPreferredTerm.getExpression() != null) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: preferredTerm replace expression not as expected");
            }
            if (replacedPreferredTerm.getSteward() != null) {
                throw new GlossaryAuthorFVTCheckedException("ERROR: preferredTerm replace steward not as expected");
            }
            FVTUtils.checkEnds(replacedPreferredTerm,createdPreferredTerm,"PreferredTerm","replace");
            if (log.isDebugEnabled()) {
                log.debug("Replaced PreferredTerm " + createdPreferredTerm);
            }
            glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
            //FVTUtils.validateLine(gotPreferredTerm);
            if (log.isDebugEnabled()) {
                log.debug("Deleted PreferredTerm with userId=" + guid);
            }
            gotPreferredTerm = glossaryAuthorViewRelationshipsClient.restoreRel(this.userId, guid,type,relType);
            FVTUtils.validateRelationship(gotPreferredTerm);
            if (log.isDebugEnabled()) {
                log.debug("restored PreferredTerm with userId=" + guid);
            }
            glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
            //FVTUtils.validateLine(gotPreferredTerm);
            log.debug("Deleted PreferredTerm with userId=" + guid);
        }

    private PreferredTerm createPreferredTerm(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException {
        PreferredTerm preferredTerm = new PreferredTerm();
        preferredTerm.setDescription("ddd");
        preferredTerm.setExpression("Ex");
        preferredTerm.setSource("source");
        preferredTerm.setSteward("Stew");
        preferredTerm.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        preferredTerm.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, PreferredTerm.class);
        ParameterizedTypeReference<GenericResponse<PreferredTerm>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        PreferredTerm createdPreferredTerm = glossaryAuthorViewRelationshipsClient.createRel(this.userId, preferredTerm,type, PREFERRED_TERM);
        FVTUtils.validateRelationship(createdPreferredTerm);
        FVTUtils.checkEnds(preferredTerm, createdPreferredTerm, "PreferredTerm", "create");

        return createdPreferredTerm;
    }

    private void usedincontextFVT(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, GlossaryAuthorFVTCheckedException, UserNotAuthorizedException {

        String relType = USED_IN_CONTEXT;
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, UsedInContext.class);
        ParameterizedTypeReference<GenericResponse<UsedInContext>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        UsedInContext createdUsedInContext = createUsedInContext(term1, term2);
        FVTUtils.validateRelationship(createdUsedInContext);
        if (log.isDebugEnabled()) {
            log.debug("Created UsedInContext " + createdUsedInContext);
        }
        String guid = createdUsedInContext.getGuid();

        UsedInContext gotUsedInContext = glossaryAuthorViewRelationshipsClient.getRel(this.userId, guid,type, relType);
        FVTUtils.validateRelationship(gotUsedInContext);
        if (log.isDebugEnabled()) {
            log.debug("Got UsedInContext " + gotUsedInContext);
        }

        UsedInContext updateUsedInContext = new UsedInContext();
        updateUsedInContext.setDescription("ddd2");
        UsedInContext updatedUsedInContext = glossaryAuthorViewRelationshipsClient.updateRel(this.userId, guid, updateUsedInContext,type,relType,false);
        FVTUtils.validateRelationship(updatedUsedInContext);
        if (!updatedUsedInContext.getDescription().equals(updateUsedInContext.getDescription())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: usedInContext update description not as expected");
        }
        if (!updatedUsedInContext.getSource().equals(createdUsedInContext.getSource())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: usedInContext update source not as expected");
        }
        if (!updatedUsedInContext.getExpression().equals(createdUsedInContext.getExpression())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: usedInContext update expression not as expected");
        }
        if (!updatedUsedInContext.getSteward().equals(createdUsedInContext.getSteward())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: usedInContext update steward not as expected");
        }

        FVTUtils.checkEnds(updatedUsedInContext,createdUsedInContext,"UsedInContext","update");
        if (log.isDebugEnabled()) {
            log.debug("Updated UsedInContext " + updatedUsedInContext);
        }
        UsedInContext replaceUsedInContext = new UsedInContext();
        replaceUsedInContext.setDescription("ddd3");
        UsedInContext replacedUsedInContext = glossaryAuthorViewRelationshipsClient.replaceRel(this.userId, guid, replaceUsedInContext, type,relType,true);
        FVTUtils.validateRelationship(replacedUsedInContext);
        if (!replacedUsedInContext.getDescription().equals(replaceUsedInContext.getDescription())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: usedInContext replace description not as expected");
        }
        if (replacedUsedInContext.getSource() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: usedInContext replace source not as expected");
        }
        if (replacedUsedInContext.getExpression() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: usedInContext replace expression not as expected");
        }
        if (replacedUsedInContext.getSteward() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: usedInContext replace steward not as expected");
        }
        FVTUtils.checkEnds(replacedUsedInContext,createdUsedInContext,"UsedInContext","replace");
        if (log.isDebugEnabled()) {
            log.debug("Replaced UsedInContext " + replacedUsedInContext);
        }
        glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
        //FVTUtils.validateLine(gotUsedInContext);
        if (log.isDebugEnabled()) {
            log.debug("Deleted UsedInContext with userId=" + guid);
        }
        gotUsedInContext = glossaryAuthorViewRelationshipsClient.restoreRel(this.userId, guid,type,relType);
        FVTUtils.validateRelationship(gotUsedInContext);
        if (log.isDebugEnabled()) {
            log.debug("Restored UsedInContext with userId=" + guid);
        }
        glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
        //FVTUtils.validateLine(gotUsedInContext);
        if (log.isDebugEnabled()) {
            log.debug("Deleted UsedInContext with userId=" + guid);
        }
    }

    private UsedInContext createUsedInContext(Term term1, Term term2) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        UsedInContext usedInContext = new UsedInContext();
        usedInContext.setDescription("ddd");
        usedInContext.setExpression("Ex");
        usedInContext.setSource("source");
        usedInContext.setSteward("Stew");
        usedInContext.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        usedInContext.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, UsedInContext.class);
        ParameterizedTypeReference<GenericResponse<UsedInContext>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        UsedInContext createdUsedInContext = glossaryAuthorViewRelationshipsClient.createRel(this.userId, usedInContext,type, USED_IN_CONTEXT);
        FVTUtils.validateRelationship(createdUsedInContext);
        FVTUtils.checkEnds(usedInContext, createdUsedInContext, "UsedInContext", "create");
        return createdUsedInContext;
    }

    private void translationFVT(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, GlossaryAuthorFVTCheckedException, UserNotAuthorizedException {
        String relType = TRANSLATION;
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Translation.class);
        ParameterizedTypeReference<GenericResponse<Translation>> type = ParameterizedTypeReference.forType(resolvableType.getType());


        Translation createdTranslation = createTranslation(term1, term2);
        FVTUtils.validateRelationship(createdTranslation);
        if (log.isDebugEnabled()) {
            log.debug("Created Translation " + createdTranslation);
        }
        String guid = createdTranslation.getGuid();

        Translation gotTranslation = glossaryAuthorViewRelationshipsClient.getRel(this.userId, guid,type, relType);
        FVTUtils.validateRelationship(gotTranslation);
        if (log.isDebugEnabled()) {
            log.debug("Got Translation " + gotTranslation);
        }

        Translation updateTranslation = new Translation();
        updateTranslation.setDescription("ddd2");
        Translation updatedTranslation = glossaryAuthorViewRelationshipsClient.updateRel(this.userId, guid, updateTranslation,type,relType,false);
        FVTUtils.validateRelationship(updatedTranslation);
        if (!updatedTranslation.getDescription().equals(updateTranslation.getDescription())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: translation update description not as expected");
        }
        if (!updatedTranslation.getSource().equals(createdTranslation.getSource())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: translation update source not as expected");
        }
        if (!updatedTranslation.getExpression().equals(createdTranslation.getExpression())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: translation update expression not as expected");
        }
        if (!updatedTranslation.getSteward().equals(createdTranslation.getSteward())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: translation update steward not as expected");
        }
        FVTUtils.checkEnds(updatedTranslation, createdTranslation, "translation", "update");

        if (log.isDebugEnabled()) {
            log.debug("Updated Translation " + updatedTranslation);
        }
        Translation replaceTranslation = new Translation();
        replaceTranslation.setDescription("ddd3");
        Translation replacedTranslation = glossaryAuthorViewRelationshipsClient.replaceRel(this.userId, guid, replaceTranslation,type,relType,true);
        FVTUtils.validateRelationship(replacedTranslation);
        if (!replacedTranslation.getDescription().equals(replaceTranslation.getDescription())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: translation replace description not as expected");
        }
        if (replacedTranslation.getSource() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: translation replace source not as expected");
        }
        if (replacedTranslation.getExpression() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: translation replace expression not as expected");
        }
        if (replacedTranslation.getSteward() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: translation replace steward not as expected");
        }
        FVTUtils.checkEnds(replacedTranslation, updatedTranslation, "translation", "replace");

        if (log.isDebugEnabled()) {
            log.debug("Replaced Translation " + replacedTranslation);
        }
        glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
        //FVTUtils.validateLine(gotTranslation);
        if (log.isDebugEnabled()) {
            log.debug("Deleted Translation with userId=" + guid);
        }
        gotTranslation = glossaryAuthorViewRelationshipsClient.restoreRel(this.userId, guid,type,relType);
        FVTUtils.validateRelationship(gotTranslation);
        if (log.isDebugEnabled()) {
            log.debug("Restored Translation with userId=" + guid);
        }
        glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
        //FVTUtils.validateLine(gotTranslation);
        if (log.isDebugEnabled()) {
            log.debug("Deleted Translation with userId=" + guid);
        }
    }

    private Translation createTranslation(Term term1, Term term2) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Translation.class);
        ParameterizedTypeReference<GenericResponse<Translation>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        Translation translation = new Translation();
        translation.setDescription("ddd");
        translation.setExpression("Ex");
        translation.setSource("source");
        translation.setSteward("Stew");
        translation.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        translation.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());
        Translation createdTranslation = glossaryAuthorViewRelationshipsClient.createRel(this.userId, translation,type,TRANSLATION);
        FVTUtils.validateRelationship(createdTranslation);
        FVTUtils.checkEnds(translation, createdTranslation, "translations", "create");

        return createdTranslation;
    }

    private void hasaFVT(Term term1, Term term3) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, GlossaryAuthorFVTCheckedException {
        String relType = HAS_A;
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, HasA.class);
        ParameterizedTypeReference<GenericResponse<HasA>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        HasA createdHasA = createHasA(term1, term3);

        Term term1PostCreate = termFVT.getTermByGUID(term1.getSystemAttributes().getGUID());
        if (log.isDebugEnabled()) {
            log.debug(" term1PostCreate.isSpineObject() " + term1PostCreate.isSpineObject());
            log.debug(" term1PostCreate GUID " + term1PostCreate.getSystemAttributes().getGUID());
            log.debug(" term1  GUID " + term1.getSystemAttributes().getGUID());
        }

        Term term3PostCreate = termFVT.getTermByGUID(term3.getSystemAttributes().getGUID());
        if (log.isDebugEnabled()) {
            log.debug(" term3PostCreate.isSpineAttribute() " + term3PostCreate.isSpineAttribute());
            log.debug(" term3PostCreate GUID " + term3PostCreate.getSystemAttributes().getGUID());
            log.debug(" term3  GUID " + term3.getSystemAttributes().getGUID());
        }

        FVTUtils.validateRelationship(createdHasA);
        if (log.isDebugEnabled()) {
            log.debug("Created Hasa " + createdHasA);
            log.debug("Hasa End1" + createdHasA.getEnd1().getNodeGuid());
            log.debug("Hasa End2" + createdHasA.getEnd2().getNodeGuid());
        }
        String guid = createdHasA.getGuid();


        HasA gotHasATerm = glossaryAuthorViewRelationshipsClient.getRel(this.userId, guid,type,relType);
        FVTUtils.validateRelationship(gotHasATerm);
        if (log.isDebugEnabled()) {
            log.debug("Got Hasa " + gotHasATerm);
        }
        HasA updateHasATerm = new HasA();
        updateHasATerm.setDescription("ddd2");
        HasA updatedHasATerm = glossaryAuthorViewRelationshipsClient.updateRel(this.userId, guid, updateHasATerm,type,relType,false);
        FVTUtils.validateRelationship(updatedHasATerm);
        if (!updatedHasATerm.getDescription().equals(updateHasATerm.getDescription())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: HASARelationship update description not as expected");
        }
        if (!updatedHasATerm.getSource().equals(createdHasA.getSource())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: HASARelationship update source not as expected");
        }
        if (!updatedHasATerm.getSteward().equals(createdHasA.getSteward())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: HASARelationship update steward not as expected");
        }
        FVTUtils.checkEnds(updatedHasATerm, createdHasA, "has-a", "update");

        if (log.isDebugEnabled()) {
            log.debug("Updated HASARelationship " + updatedHasATerm);
        }
        HasA replaceHasA = new HasA();
        replaceHasA.setDescription("ddd3");
        HasA replacedHasA = glossaryAuthorViewRelationshipsClient.replaceRel(this.userId, guid, replaceHasA,type,relType,true);
        FVTUtils.validateRelationship(replacedHasA);
        if (!replacedHasA.getDescription().equals(replaceHasA.getDescription())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: HASARelationship replace description not as expected");
        }
        if (replacedHasA.getSource() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: HASARelationship replace source not as expected");
        }
        if (replacedHasA.getSteward() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: HASARelationship replace steward not as expected");
        }
        FVTUtils.checkEnds(updatedHasATerm, replacedHasA, "has-a", "replace");

        if (log.isDebugEnabled()) {
            log.debug("Replaced HASARelationship " + replacedHasA);
        }
        term1PostCreate = termFVT.getTermByGUID(term1.getSystemAttributes().getGUID());
        if (log.isDebugEnabled()) {
            log.debug(" term1PostCreate.isSpineObject() " + term1PostCreate.isSpineObject());
        }
        term3PostCreate = termFVT.getTermByGUID(term3.getSystemAttributes().getGUID());
        if (log.isDebugEnabled()) {
            log.debug(" term3PostCreate.isSpineAttribute() " + term3PostCreate.isSpineAttribute());
        }
//
//  check that term1 and term3 have the spine object and attribute flags sets

        term1PostCreate = termFVT.getTermByGUID(term1.getSystemAttributes().getGUID());
        if (!term1PostCreate.isSpineObject()) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: expect term 1 to be a Spine Object");
        }

        term3PostCreate = termFVT.getTermByGUID(term3.getSystemAttributes().getGUID());

        if (!term3PostCreate.isSpineAttribute()) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: expect term 3 to be a Spine Attribute");
        }


        glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
        //FVTUtils.validateLine(gotHASATerm);
        if (log.isDebugEnabled()) {
            log.debug("Deleted Hasa with userId=" + guid);
        }
        gotHasATerm = glossaryAuthorViewRelationshipsClient.restoreRel(this.userId, guid,type,relType);
        FVTUtils.validateRelationship(gotHasATerm);
        if (log.isDebugEnabled()) {
            log.debug("Restored Hasa with userId=" + guid);
        }
        glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
        //FVTUtils.validateLine(gotHASATerm);
        if (log.isDebugEnabled()) {
            log.debug("Deleted Hasa with userId=" + guid);
        }
    }

    private HasA createHasA(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException {
        HasA hasA = new HasA();
        hasA.setDescription("ddd");
        hasA.setSource("source");
        hasA.setSteward("Stew");
        hasA.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        hasA.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());

        String relType = HAS_A;
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, HasA.class);
        ParameterizedTypeReference<GenericResponse<HasA>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        HasA createdTermHasARelationship = glossaryAuthorViewRelationshipsClient.createRel(this.userId, hasA,type, relType);
        FVTUtils.validateRelationship(createdTermHasARelationship);
        FVTUtils.checkEnds(hasA, createdTermHasARelationship, "Has-a", "create");

        return createdTermHasARelationship;
    }

    private void relatedtermFVT(Term term1, Term term3) throws InvalidParameterException, PropertyServerException, GlossaryAuthorFVTCheckedException, UserNotAuthorizedException {

        String relType = RELATED_TERM;
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, RelatedTerm.class);
        ParameterizedTypeReference<GenericResponse<RelatedTerm>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        RelatedTerm createdRelatedTerm = createRelatedTerm(term1, term3);
        FVTUtils.validateRelationship(createdRelatedTerm);
        if (log.isDebugEnabled()) {
            log.debug("Created RelatedTerm " + createdRelatedTerm);
        }
        String guid = createdRelatedTerm.getGuid();

        RelatedTerm gotRelatedTerm = glossaryAuthorViewRelationshipsClient.getRel(this.userId, guid,type,relType);
        FVTUtils.validateRelationship(gotRelatedTerm);
        if (log.isDebugEnabled()) {
            log.debug("Got RelatedTerm " + gotRelatedTerm);
        }
        RelatedTerm updateRelatedTerm = new RelatedTerm();
        updateRelatedTerm.setDescription("ddd2");
        updateRelatedTerm.setGuid(createdRelatedTerm.getGuid());
        RelatedTerm updatedRelatedTerm = glossaryAuthorViewRelationshipsClient.updateRel(this.userId, guid, updateRelatedTerm,type,relType,false);
        FVTUtils.validateRelationship(updatedRelatedTerm);
        if (!updatedRelatedTerm.getDescription().equals(updateRelatedTerm.getDescription())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: RelatedTerm update description not as expected");
        }
        if (!updatedRelatedTerm.getSource().equals(createdRelatedTerm.getSource())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: RelatedTerm update source not as expected");
        }
        if (!updatedRelatedTerm.getExpression().equals(createdRelatedTerm.getExpression())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: RelatedTerm update expression not as expected");
        }
        if (!updatedRelatedTerm.getSteward().equals(createdRelatedTerm.getSteward())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: RelatedTerm update steward not as expected");
        }
        FVTUtils.checkEnds(updatedRelatedTerm,createdRelatedTerm,"RelatedTerm","update");
        if (log.isDebugEnabled()) {
            log.debug("Updated RelatedTerm " + updatedRelatedTerm);
        }
        RelatedTerm replaceRelatedTerm = new RelatedTerm();
        replaceRelatedTerm.setDescription("ddd3");
        replaceRelatedTerm.setGuid(createdRelatedTerm.getGuid());
        RelatedTerm replacedRelatedTerm = glossaryAuthorViewRelationshipsClient.replaceRel(this.userId, guid, replaceRelatedTerm,type,relType, true);
        FVTUtils.validateRelationship(replacedRelatedTerm);
        if (!replacedRelatedTerm.getDescription().equals(replaceRelatedTerm.getDescription())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: RelatedTerm replace description not as expected");
        }
        if (replacedRelatedTerm.getSource() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: RelatedTerm replace source not as expected");
        }
        if (replacedRelatedTerm.getExpression() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: RelatedTerm replace expression not as expected");
        }
        if (replacedRelatedTerm.getSteward() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: RelatedTerm replace steward not as expected");
        }
        FVTUtils.checkEnds(replacedRelatedTerm,createdRelatedTerm,"RelatedTerm","replace");
        if (log.isDebugEnabled()) {
            log.debug("Replaced RelatedTerm " + replacedRelatedTerm);
        }

        glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type, relType);
        //FVTUtils.validateLine(gotRelatedTerm);
        if (log.isDebugEnabled()) {
            log.debug("Deleted RelatedTerm with userId=" + guid);
        }
        gotRelatedTerm = glossaryAuthorViewRelationshipsClient.restoreRel(this.userId, guid,type, relType);
        FVTUtils.validateRelationship(gotRelatedTerm);
        if (log.isDebugEnabled()) {
            log.debug("Restored RelatedTerm with userId=" + guid);
        }
        glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type, relType);
        //FVTUtils.validateLine(gotRelatedTerm);
        if (log.isDebugEnabled()) {
            log.debug("Deleted RelatedTerm with userId=" + guid);
        }
    }

    private RelatedTerm createRelatedTerm(Term term1, Term term2) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        RelatedTerm relatedterm = new RelatedTerm();
        relatedterm.setDescription("ddd");
        relatedterm.setExpression("Ex");
        relatedterm.setSource("source");
        relatedterm.setSteward("Stew");
        relatedterm.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        relatedterm.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, RelatedTerm.class);
        ParameterizedTypeReference<GenericResponse<RelatedTerm>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        RelatedTerm createdRelatedTerm = glossaryAuthorViewRelationshipsClient.createRel(this.userId, relatedterm,type,RELATED_TERM);
        FVTUtils.validateRelationship(createdRelatedTerm);
        FVTUtils.checkEnds(relatedterm, createdRelatedTerm, "RelatedTerm", "create");

        return createdRelatedTerm;

    }

    private void antonymFVT(Term term1, Term term3) throws InvalidParameterException, PropertyServerException, GlossaryAuthorFVTCheckedException, UserNotAuthorizedException {

        String relType = ANTONYM;
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Antonym.class);
        ParameterizedTypeReference<GenericResponse<Antonym>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        Antonym createdAntonym = createAntonym(term1, term3);
        FVTUtils.validateRelationship(createdAntonym);
        if (log.isDebugEnabled()) {
            log.debug("Created Antonym " + createdAntonym);
        }
        String guid = createdAntonym.getGuid();

        Antonym gotAntonym = glossaryAuthorViewRelationshipsClient.getRel(this.userId, guid,type,relType);
        FVTUtils.validateRelationship(gotAntonym);
        if (log.isDebugEnabled()) {
            log.debug("Got Antonym " + gotAntonym);
        }
        Antonym updateAntonym = new Antonym();
        updateAntonym.setDescription("ddd2");
        Antonym updatedAntonym = glossaryAuthorViewRelationshipsClient.updateRel(this.userId, guid, updateAntonym,type,relType, false);
        FVTUtils.validateRelationship(updatedAntonym);
        if (!updatedAntonym.getDescription().equals(updateAntonym.getDescription())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Antonym update description not as expected");
        }
        if (!updatedAntonym.getSource().equals(createdAntonym.getSource())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Antonym update source not as expected");
        }
        if (!updatedAntonym.getExpression().equals(createdAntonym.getExpression())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Antonym update expression not as expected");
        }
        if (!updatedAntonym.getSteward().equals(createdAntonym.getSteward())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Antonym update steward not as expected");
        }
        FVTUtils.checkEnds(updatedAntonym,createdAntonym,"Antonym","update");
        if (log.isDebugEnabled()) {
            log.debug("Updated Antonym " + updatedAntonym);
        }
        Antonym replaceAntonym = new Antonym();
        replaceAntonym.setDescription("ddd3");
        replaceAntonym.setGuid(createdAntonym.getGuid());
        Antonym replacedAntonym = glossaryAuthorViewRelationshipsClient.replaceRel(this.userId, guid, replaceAntonym,type,relType, true);
        FVTUtils.validateRelationship(replacedAntonym);
        if (!replacedAntonym.getDescription().equals(replaceAntonym.getDescription())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Antonym replace description not as expected");
        }
        if (replacedAntonym.getSource() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Antonym replace source not as expected");
        }
        if (replacedAntonym.getExpression() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Antonym replace expression not as expected");
        }
        if (replacedAntonym.getSteward() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Antonym replace steward not as expected");
        }
        FVTUtils.checkEnds(updatedAntonym,createdAntonym,"Antonym","replace");
        if (log.isDebugEnabled()) {
            log.debug("Replaced Antonym " + replacedAntonym);
        }

        glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
        //FVTUtils.validateLine(gotAntonym);
        if (log.isDebugEnabled()) {
            log.debug("Deleted Antonym with userId=" + guid);
        }
        gotAntonym = glossaryAuthorViewRelationshipsClient.restoreRel(this.userId, guid,type,relType);
        FVTUtils.validateRelationship(gotAntonym);
        if (log.isDebugEnabled()) {
            log.debug("Restored Antonym with userId=" + guid);
        }
        glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
        //FVTUtils.validateLine(gotAntonym);
        if (log.isDebugEnabled()) {
            log.debug("Deleted Antonym with userId=" + guid);
        }
    }

    private Antonym createAntonym(Term term1, Term term2) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Antonym antonym = new Antonym();
        antonym.setDescription("ddd");
        antonym.setExpression("Ex");
        antonym.setSource("source");
        antonym.setSteward("Stew");
        antonym.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        antonym.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Antonym.class);
        ParameterizedTypeReference<GenericResponse<Antonym>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        Antonym createdAntonym = glossaryAuthorViewRelationshipsClient.createRel(this.userId, antonym,type,ANTONYM);
        FVTUtils.validateRelationship(createdAntonym);
        FVTUtils.checkEnds(antonym, createdAntonym, "Antonym", "create");
        return createdAntonym;
    }

    private void synonymFVT(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, GlossaryAuthorFVTCheckedException, UserNotAuthorizedException {
        String relType = SYNONYM;
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Synonym.class);
        ParameterizedTypeReference<GenericResponse<Synonym>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        Synonym createdSynonym = createSynonym(term1, term2);
        FVTUtils.validateRelationship(createdSynonym);
        if (log.isDebugEnabled()) {
            log.debug("Created Synonym " + createdSynonym);
        }
        String guid = createdSynonym.getGuid();

        Synonym gotSynonym = glossaryAuthorViewRelationshipsClient.getRel(this.userId, guid,type,SYNONYM);

        FVTUtils.validateRelationship(gotSynonym);
        if (log.isDebugEnabled()) {
            log.debug("Got Synonym " + gotSynonym);
        }
        //return;
        Synonym updateSynonym = new Synonym();
        updateSynonym.setDescription("ddd2");


        Synonym updatedSynonym = glossaryAuthorViewRelationshipsClient.updateRel(this.userId, guid, updateSynonym,type,relType,false);
        FVTUtils.validateRelationship(updatedSynonym);
        if (!updatedSynonym.getDescription().equals(updateSynonym.getDescription())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: synonym update description not as expected");
        }
        if (log.isDebugEnabled()) {
            log.debug(" updatedSynonym " + updatedSynonym.toString());
        }
        if (!updatedSynonym.getSource().equals(createdSynonym.getSource())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: synonym update source not as expected");
        }
        if (!updatedSynonym.getExpression().equals(createdSynonym.getExpression())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: synonym update expression not as expected");
        }
        if (!updatedSynonym.getSteward().equals(createdSynonym.getSteward())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: synonym update steward not as expected");
        }

        FVTUtils.checkEnds(updatedSynonym, createdSynonym, "synonym", "update");

        if (log.isDebugEnabled()) {
            log.debug("Updated Synonym " + createdSynonym);
        }
        Synonym replaceSynonym = new Synonym();
        replaceSynonym.setDescription("ddd3");
        Synonym replacedSynonym = glossaryAuthorViewRelationshipsClient.replaceRel(this.userId, guid, replaceSynonym,type,relType,true);
        FVTUtils.validateRelationship(replacedSynonym);
        if (!replacedSynonym.getDescription().equals(replaceSynonym.getDescription())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: synonym replace description not as expected");
        }
        if (replacedSynonym.getSource() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: synonym replace source not as expected");
        }
        if (replacedSynonym.getExpression() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: synonym replace expression not as expected");
        }
        if (replacedSynonym.getSteward() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: synonym replace steward not as expected");
        }
        FVTUtils.checkEnds(updatedSynonym, replacedSynonym, "synonym", "replace");

        if (log.isDebugEnabled()) {
            log.debug("Replaced Synonym " + replacedSynonym
            );
        }
        glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
        //FVTUtils.validateLine(gotSynonym);
        if (log.isDebugEnabled()) {
            log.debug("Deleted Synonym with userId=" + guid);
        }
        gotSynonym = glossaryAuthorViewRelationshipsClient.restoreRel(this.userId, guid,type,relType);
        FVTUtils.validateRelationship(gotSynonym);
        if (log.isDebugEnabled()) {
            log.debug("Restored Synonym with userId=" + guid);
        }
        glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid, type,relType);
        //FVTUtils.validateLine(gotSynonym);

        if (log.isDebugEnabled()) {
            log.debug("Hard deleted Synonym with userId=" + guid);
        }
    }

    public Synonym createSynonym(Term term1, Term term2) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Synonym synonym = new Synonym();
        synonym.setDescription("ddd");
        synonym.setExpression("Ex");
        synonym.setSource("source");
        synonym.setSteward("Stew");
        synonym.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        synonym.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Synonym.class);
        ParameterizedTypeReference<GenericResponse<Synonym>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        //Synonym createdSynonym = glossaryAuthorViewRelationshipsClient.createSynonym(this.userId, synonym);
        Synonym createdSynonym = glossaryAuthorViewRelationshipsClient.createRel(this.userId, synonym,type,SYNONYM);
        FVTUtils.validateRelationship(createdSynonym);
        FVTUtils.checkEnds(synonym, createdSynonym, "synonym", "create");
        return createdSynonym;
    }


    private void termCategorizationFVT(Term term, Category category) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, GlossaryAuthorFVTCheckedException {

        String relType = TERM_CATEGORIZATION;
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Categorization.class);
        ParameterizedTypeReference<GenericResponse<Categorization>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        Categorization createdTermCategorizationRelationship = createTermCategorization(term, category);
        FVTUtils.validateRelationship(createdTermCategorizationRelationship);
        if (log.isDebugEnabled()) {
            log.debug("Created TermCategorizationRelationship " + createdTermCategorizationRelationship);
        }
        String guid = createdTermCategorizationRelationship.getGuid();

        Categorization gotTermCategorizationRelationship = glossaryAuthorViewRelationshipsClient.getRel(this.userId, guid,type, relType);
        FVTUtils.validateRelationship(gotTermCategorizationRelationship);
        if (log.isDebugEnabled()) {
            log.debug("Got TermCategorizationRelationship " + gotTermCategorizationRelationship);
        }

        Categorization updateTermCategorizationRelationship = new Categorization();
        updateTermCategorizationRelationship.setDescription("ddd2");
        Categorization updatedTermCategorizationRelationship = glossaryAuthorViewRelationshipsClient.updateRel(this.userId, guid, updateTermCategorizationRelationship,type,relType,false);
        FVTUtils.validateRelationship(updatedTermCategorizationRelationship);
        FVTUtils.checkEnds(updatedTermCategorizationRelationship,createdTermCategorizationRelationship,"TermCategorization","update");

        if (!updatedTermCategorizationRelationship.getDescription().equals(updateTermCategorizationRelationship.getDescription())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: TermCategorization update description not as expected");
        }
        if (updatedTermCategorizationRelationship.getStatus() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: TermCategorization update status not as expected");
        }

        if (log.isDebugEnabled()) {
            log.debug("Updated TermCategorizationRelationship " + createdTermCategorizationRelationship);
        }
        Categorization replaceTermCategorizationRelationship = new Categorization();
        replaceTermCategorizationRelationship.setDescription("ddd3");
        Categorization replacedTermCategorizationRelationship = glossaryAuthorViewRelationshipsClient.replaceRel(this.userId, guid, replaceTermCategorizationRelationship, type,relType,true);
        FVTUtils.validateRelationship(replacedTermCategorizationRelationship);
        if (!replacedTermCategorizationRelationship.getDescription().equals(replaceTermCategorizationRelationship.getDescription())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: TermCategorization replace description not as expected");
        }
        if (replacedTermCategorizationRelationship.getStatus() != null) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: TermCategorization replace source not as expected");
        }

        FVTUtils.checkEnds(replacedTermCategorizationRelationship,createdTermCategorizationRelationship,"TermCategorization","replace");
        if (log.isDebugEnabled()) {
            log.debug("Replaced TermCategorizationRelationship " + replacedTermCategorizationRelationship);
        }
        glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
        //FVTUtils.validateLine(gotTermCategorizationRelationship);
        if (log.isDebugEnabled()) {
            log.debug("Deleted TermCategorizationRelationship with userId=" + guid);
        }
        gotTermCategorizationRelationship = glossaryAuthorViewRelationshipsClient.restoreRel(this.userId, guid,type,relType);
        FVTUtils.validateRelationship(gotTermCategorizationRelationship);
        if (log.isDebugEnabled()) {
            log.debug("Restored TermCategorizationRelationship with userId=" + guid);
        }
        glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
        if (log.isDebugEnabled()) {
            log.debug("Deleted TermCategorization with userId=" + guid);
        }
    }

    public Categorization createTermCategorization(Term term, Category category) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException {
        Categorization termCategorization = new Categorization();
        termCategorization.getEnd1().setNodeGuid(category.getSystemAttributes().getGUID());
        termCategorization.getEnd2().setNodeGuid(term.getSystemAttributes().getGUID());

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Categorization.class);
        ParameterizedTypeReference<GenericResponse<Categorization>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        Categorization createdTermCategorization = glossaryAuthorViewRelationshipsClient.createRel(this.userId, termCategorization,type, TERM_CATEGORIZATION);
        FVTUtils.validateRelationship(createdTermCategorization);
        FVTUtils.checkEnds(termCategorization, createdTermCategorization, "TermCategorizationRelationship", "create");
        if (log.isDebugEnabled()) {
            log.debug("Created TermCategorizationRelationship " + createdTermCategorization);
        }

        return createdTermCategorization;
    }


    private void projectScopeFVT(Project project, Term term) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException {
        String relType = PROJECT_SCOPE;
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, ProjectScope.class);
        ParameterizedTypeReference<GenericResponse<ProjectScope>> type = ParameterizedTypeReference.forType(resolvableType.getType());


        ProjectScope createdProjectScope = createProjectScope(project, term);
        FVTUtils.validateRelationship(createdProjectScope);

        if (log.isDebugEnabled()) {
            log.debug("Created ProjectScopeRelationship " + createdProjectScope);
        }
        String guid = createdProjectScope.getGuid();

        ProjectScope gotProjectScopeRelationship = glossaryAuthorViewRelationshipsClient.getRel(this.userId, guid,type, relType);
        FVTUtils.validateRelationship(gotProjectScopeRelationship);
        if (log.isDebugEnabled()) {
            log.debug("Got ProjectScopeRelationship " + gotProjectScopeRelationship);
        }

        ProjectScope updateProjectScope = new ProjectScope();
        updateProjectScope.setDescription("ddd2");
        updateProjectScope.setGuid(createdProjectScope.getGuid());
        ProjectScope updatedProjectScope = glossaryAuthorViewRelationshipsClient.updateRel(this.userId, guid, updateProjectScope,type,relType,false);
        FVTUtils.validateRelationship(updatedProjectScope);
        if (!updatedProjectScope.getDescription().equals(updateProjectScope.getDescription())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Project scope  update scopeDescription not as expected");
        }

        FVTUtils.checkEnds(updatedProjectScope,createdProjectScope,"ProjectScope","update");

        if (log.isDebugEnabled()) {
            log.debug("Updated ProjectScopeRelationship " + updatedProjectScope);
        }
        ProjectScope replaceProjectScope = new ProjectScope();
        replaceProjectScope.setDescription("ddd3");
        ProjectScope replacedProjectScope = glossaryAuthorViewRelationshipsClient.replaceRel(this.userId, guid, replaceProjectScope, type,relType,true);
        FVTUtils.validateRelationship(replacedProjectScope);
        if (!replacedProjectScope.getDescription().equals(replaceProjectScope.getDescription())) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: project scope replace scope description not as expected");
        }
        FVTUtils.checkEnds(replacedProjectScope,createdProjectScope,"ProjectScope","replace");

        if (log.isDebugEnabled()) {
            log.debug("Replaced ProjectScopeRelationship " + replacedProjectScope);
        }
        glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
        //FVTUtils.validateLine(gotProjectScopeRelationship);
        if (log.isDebugEnabled()) {
            log.debug("Deleted ProjectScopeRelationship with userId=" + guid);
        }
        gotProjectScopeRelationship = glossaryAuthorViewRelationshipsClient.restoreRel(this.userId, guid,type,relType);
        FVTUtils.validateRelationship(gotProjectScopeRelationship);
        if (log.isDebugEnabled()) {
            log.debug("Restored ProjectScopeRelationship with userId=" + guid);
        }
        glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
        //FVTUtils.validateLine(gotProjectScopeRelationship);

        if (log.isDebugEnabled()) {
            log.debug("Hard deleted ProjectScopeRelationship with userId=" + guid);
        }
    }

    protected ProjectScope createProjectScope(Project project, Term term) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException {
        ProjectScope projectScope = new ProjectScope();
        projectScope.getEnd1().setNodeGuid(project.getSystemAttributes().getGUID());
        projectScope.getEnd2().setNodeGuid(term.getSystemAttributes().getGUID());

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, ProjectScope.class);
        ParameterizedTypeReference<GenericResponse<ProjectScope>> type = ParameterizedTypeReference.forType(resolvableType.getType());


        ProjectScope createdProjectScope = glossaryAuthorViewRelationshipsClient.createRel(this.userId, projectScope,type, PROJECT_SCOPE);
        FVTUtils.validateRelationship(createdProjectScope);
        if (log.isDebugEnabled()) {
            log.debug("CreatedProjectScopeRelationship " + createdProjectScope);
        }
        return createdProjectScope;
    }

    private void categoryHierarchyLinkFVT(Category parent, Category child) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, GlossaryAuthorFVTCheckedException {

        String relType = CATEGORY_HIERARCHY_LINK;
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, CategoryHierarchyLink.class);
        ParameterizedTypeReference<GenericResponse<CategoryHierarchyLink>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        CategoryHierarchyLink categoryHierarchyLink = createCategoryHierarchyLink(parent, child);
        if (log.isDebugEnabled()) {
            log.debug("Create CategoryHierarchyLink " + categoryHierarchyLink);
        }
        String guid = categoryHierarchyLink.getGuid();

        CategoryHierarchyLink gotCategoryHierarchyLink = glossaryAuthorViewRelationshipsClient.getRel(this.userId, guid,type, relType);
        FVTUtils.validateRelationship(gotCategoryHierarchyLink);
        if (log.isDebugEnabled()) {
            log.debug("Got CategoryHierarchyLink " + gotCategoryHierarchyLink);
        }
        Category gotChild = glossaryAuthorViewCategory.getByGUID(userId, child.getSystemAttributes().getGUID());
        if (log.isDebugEnabled()) {
            log.debug("Got Category gotChild " + gotChild);
        }

        checkParent(parent, gotChild);
        glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
        if (log.isDebugEnabled()) {
            log.debug("Deleted CategoryHierarchyLink with userId=" + guid);
        }
        gotCategoryHierarchyLink = glossaryAuthorViewRelationshipsClient.restoreRel(this.userId, guid,type,relType);
        FVTUtils.validateRelationship(gotCategoryHierarchyLink);
        if (log.isDebugEnabled()) {
            log.debug("Restored CategoryHierarchyLink with userId=" + guid);
        }
        glossaryAuthorViewRelationshipsClient.deleteRel(this.userId, guid,type,relType);
        if (log.isDebugEnabled()) {
            log.debug("Deleted CategoryHierarchyLink with userId=" + guid);
        }
    }

    public CategoryHierarchyLink createCategoryHierarchyLink(Category parent, Category child) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException {

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, CategoryHierarchyLink.class);
        ParameterizedTypeReference<GenericResponse<CategoryHierarchyLink>> type = ParameterizedTypeReference.forType(resolvableType.getType());

        CategoryHierarchyLink categoryHierarchyLink = new CategoryHierarchyLink();
        categoryHierarchyLink.getEnd1().setNodeGuid(parent.getSystemAttributes().getGUID());
        categoryHierarchyLink.getEnd2().setNodeGuid(child.getSystemAttributes().getGUID());

        CategoryHierarchyLink createdCategoryHierarchyLink = glossaryAuthorViewRelationshipsClient.createRel(this.userId, categoryHierarchyLink,type, CATEGORY_HIERARCHY_LINK);
        FVTUtils.validateRelationship(createdCategoryHierarchyLink);
        FVTUtils.checkEnds(categoryHierarchyLink, createdCategoryHierarchyLink, "CategoryHierarchyLink", "create");

        if (log.isDebugEnabled()) {
            log.debug("Created CategoryHierarchyLink " + createdCategoryHierarchyLink);
            log.debug("Created CategoryHierarchyLink End1 " + createdCategoryHierarchyLink.getEnd1().getNodeGuid());
            log.debug("Created CategoryHierarchyLink End2 " + createdCategoryHierarchyLink.getEnd2().getNodeGuid());
        }

        return createdCategoryHierarchyLink;
    }

    public void checkParent(Category parent, Category gotChildCategory) throws GlossaryAuthorFVTCheckedException {
        if (gotChildCategory.getParentCategory() != null) {
            CategorySummary categorySummary = gotChildCategory.getParentCategory();
            String parentGuid = parent.getSystemAttributes().getGUID();
            String parentGuidFromChild = categorySummary.getGuid();
            if(!parentGuid.equals(parentGuidFromChild)) {
                throw new GlossaryAuthorFVTCheckedException("ERROR parent category guid - " + parentGuid
                        + " no equal parent guid " + parentGuidFromChild + " from child.");
            }
        } else {
            throw new GlossaryAuthorFVTCheckedException("ERROR parent category is null");
        }
    }
}
