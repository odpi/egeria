/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.categories.SubjectAreaCategoryClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaRelationshipClients;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;

import java.io.IOException;
import java.util.List;

/**
 * FVT resource to call subject area relationships client API
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
    private SubjectAreaRelationshipClients subjectAreaRelationship = null;
    private SubjectAreaNodeClient<Category> subjectAreaCategory = null;
    private GlossaryFVT glossaryFVT = null;
    private TermFVT termFVT = null;
    private CategoryFVT catFVT = null;
    private ProjectFVT projectFVT = null;
    private String url = null;
    private String serverName = null;
    private String userId = null;

    public RelationshipsFVT(String url, String serverName, String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        this.url = url;
        SubjectAreaRestClient client = new SubjectAreaRestClient(serverName, url);
        subjectAreaRelationship = new SubjectAreaRelationship(client);
        subjectAreaCategory = new SubjectAreaCategoryClient(client);
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
     * @throws SubjectAreaFVTCheckedException
     */
    void deleteRemaining() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, SubjectAreaFVTCheckedException {
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
        } catch (SubjectAreaFVTCheckedException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            System.out.println("ERROR: " + e.getReportedErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        }

    }

    public static void runWith2Servers(String url) throws  SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        runIt(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        runIt(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
    }

    public static void runIt(String url, String serverName, String userId) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        System.out.println("relationshipFVT runIt started");
        RelationshipsFVT fvt = new RelationshipsFVT(url, serverName, userId);
        fvt.run();
        fvt.deleteRemaining();
        System.out.println("relationshipFVT runIt stopped");
    }

    public void run() throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        System.out.println("Create a glossary");

        int term1relationshipcount = 0;
        int term2relationshipcount = 0;
        int term3relationshipcount = 0;
        int glossaryRelationshipCount = 0;
        int cat1RelationshipCount = 0;
        int cat2RelationshipCount = 0;

        Glossary glossary = glossaryFVT.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
        System.out.println("Create a term called " + DEFAULT_TEST_TERM_NAME + " using glossary userId");
        String glossaryGuid = glossary.getSystemAttributes().getGUID();
        Term term1 = termFVT.createTerm(DEFAULT_TEST_TERM_NAME, glossaryGuid);

        term1relationshipcount++;
        glossaryRelationshipCount++;
        checkRelationshipNumberforGlossary(glossaryRelationshipCount, glossary);
        checkRelationshipNumberforTerm(term1relationshipcount, term1);
        FVTUtils.validateNode(term1);
        System.out.println("Create a term called " + DEFAULT_TEST_TERM_NAME2 + " using glossary userId");
        Term term2 = termFVT.createTerm(DEFAULT_TEST_TERM_NAME2, glossaryGuid);
        term2relationshipcount++;
        glossaryRelationshipCount++;
        checkRelationshipNumberforGlossary(glossaryRelationshipCount, glossary);
        checkRelationshipNumberforTerm(term2relationshipcount, term2);
        FVTUtils.validateNode(term2);
        System.out.println("Create a term called " + DEFAULT_TEST_TERM_NAME3 + " using glossary userId");
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
        isatypeofFVT(term1, term2);
        termCategorizationFVT(term1, cat1);
        termAnchorFVT(term1);
        categoryAnchorFVT(cat1);
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
        System.out.println("get term relationships");
        List<Relationship> term1Relationships = termFVT.getTermRelationships(term1);

        System.out.println("Get paged term relationships");
        int offset = 0;

        int numberofrelationships = 0;
        while (offset < term1relationshipcount) {
            System.out.println("Get paged term relationships offset = " + offset + ",pageSize=3");
            List<Relationship> term1PagedRelationships = termFVT.getTermRelationships(term1, null, offset, 3, SequencingOrder.GUID, null);
            numberofrelationships = numberofrelationships + term1PagedRelationships.size();
            offset += 3;
        }
        if (term1relationshipcount != numberofrelationships) {
            throw new SubjectAreaFVTCheckedException("Expected " + term1Relationships.size() + " got " + numberofrelationships);
        }
        Project project= projectFVT.createProject(DEFAULT_TEST_PROJECT_NAME );
        projectScopeFVT(project, term1);
        projectFVT.deleteProject(project.getSystemAttributes().getGUID());
        projectFVT.purgeProject(project.getSystemAttributes().getGUID());

        Category cat3 = catFVT.createCategory(DEFAULT_TEST_CAT_NAME3, glossaryGuid);
        Category cat4 = catFVT.createCategory(DEFAULT_TEST_CAT_NAME4, glossaryGuid);
        categoryHierarchyLinkFVT(cat3, cat4);
    }

    private void checkRelationshipNumberforTerm(int expectedrelationshipcount, Term term) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        int actualCount = termFVT.getTermRelationships(term).size();
        if (expectedrelationshipcount != actualCount) {
            throw new SubjectAreaFVTCheckedException("ERROR: expected " + expectedrelationshipcount + " for " + term.getName() + " got " + actualCount);
        }
    }

    private void checkRelationshipNumberforGlossary(int expectedrelationshipcount, Glossary glossary) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        int actualCount = glossaryFVT.getGlossaryRelationships(glossary).size();
        if (expectedrelationshipcount != actualCount) {
            throw new SubjectAreaFVTCheckedException("ERROR: expected " + expectedrelationshipcount + " for " + glossary.getName() + " got " + actualCount);
        }
    }

    private void checkRelationshipNumberforCategory(int expectedrelationshipcount, Category category) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        int actualCount = catFVT.getCategoryRelationships(category).size();
        if (expectedrelationshipcount != actualCount) {
            throw new SubjectAreaFVTCheckedException("ERROR: expected " + expectedrelationshipcount + " for " + category.getName() + " got " + actualCount);
        }
    }

    private void createSomeTermRelationships(Term term1, Term term2, Term term3) throws InvalidParameterException, PropertyServerException, SubjectAreaFVTCheckedException, UserNotAuthorizedException {
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
        FVTUtils.validateRelationship(createTermISATypeOFRelationship(term1, term2));
    }

    private void isatypeofFVT(Term term1, Term term2) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, SubjectAreaFVTCheckedException {
        IsATypeOf createdTermIsATypeOFRelationship = createTermISATypeOFRelationship(term1, term2);
        String guid = createdTermIsATypeOFRelationship.getGuid();

        IsATypeOf gotTermIsATypeOFRelationship = subjectAreaRelationship.isaTypeOf().getByGUID(this.userId, guid);
        FVTUtils.validateRelationship(gotTermIsATypeOFRelationship);
        System.out.println("Got IsaTypeOf " + createdTermIsATypeOFRelationship);

        IsATypeOf updateTermIsATypeOFRelationship = new IsATypeOf();
        updateTermIsATypeOFRelationship.setDescription("ddd2");
        IsATypeOf updatedTermIsATypeOFRelationship = subjectAreaRelationship.isaTypeOf().update(this.userId, guid, updateTermIsATypeOFRelationship);
        FVTUtils.validateRelationship(updatedTermIsATypeOFRelationship);
        if (!updatedTermIsATypeOFRelationship.getDescription().equals(updateTermIsATypeOFRelationship.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: IsaTypeOf update description not as expected");
        }
        if (!updatedTermIsATypeOFRelationship.getSource().equals(createdTermIsATypeOFRelationship.getSource())) {
            throw new SubjectAreaFVTCheckedException("ERROR: IsaTypeOf update source not as expected");
        }
        if (!updatedTermIsATypeOFRelationship.getSteward().equals(createdTermIsATypeOFRelationship.getSteward())) {
            throw new SubjectAreaFVTCheckedException("ERROR: IsaTypeOf update steward not as expected");
        }
        FVTUtils.checkEnds(updatedTermIsATypeOFRelationship,createdTermIsATypeOFRelationship,"IsATypeOf","update");
        System.out.println("Updated IsaTypeOf " + createdTermIsATypeOFRelationship);
        IsATypeOf replaceTermIsATypeOFRelationship = new IsATypeOf();
        replaceTermIsATypeOFRelationship.setDescription("ddd3");
        IsATypeOf replacedTermIsATypeOFRelationship = subjectAreaRelationship.isaTypeOf().replace(this.userId, guid, replaceTermIsATypeOFRelationship);
        FVTUtils.validateRelationship(replacedTermIsATypeOFRelationship);
        if (!replacedTermIsATypeOFRelationship.getDescription().equals(replaceTermIsATypeOFRelationship.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: IsaTypeOf replace description not as expected");
        }
        if (replacedTermIsATypeOFRelationship.getSource() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: IsaTypeOf replace source not as expected");
        }
        if (replacedTermIsATypeOFRelationship.getSteward() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: IsaTypeOf replace steward not as expected");
        }
        if (!replacedTermIsATypeOFRelationship.getEnd1().getNodeGuid().equals(createdTermIsATypeOFRelationship.getEnd1().getNodeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: IsaTypeOf replace end 1 not as expected");
        }
        if (!replacedTermIsATypeOFRelationship.getEnd2().getNodeGuid().equals(createdTermIsATypeOFRelationship.getEnd2().getNodeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: IsaTypeOf replace end 2 not as expected");
        }
        System.out.println("Replaced IsaTypeOf " + createdTermIsATypeOFRelationship);
        subjectAreaRelationship.isaTypeOf().delete(this.userId, guid);
        //FVTUtils.validateLine(gotTermISATypeOFRelationship);
        System.out.println("Soft deleted IsaTypeOf with userId=" + guid);
        gotTermIsATypeOFRelationship = subjectAreaRelationship.isaTypeOf().restore(this.userId, guid);
        FVTUtils.validateRelationship(gotTermIsATypeOFRelationship);
        System.out.println("Restored IsaTypeOf with userId=" + guid);
        subjectAreaRelationship.isaTypeOf().delete(this.userId, guid);
        //FVTUtils.validateLine(gotTermISATypeOFRelationship);
        System.out.println("Soft deleted IsaTypeOf with userId=" + guid);
        subjectAreaRelationship.isaTypeOf().purge(this.userId, guid);
        System.out.println("Hard deleted IsaTypeOf with userId=" + guid);
    }

    private void isaFVT(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, SubjectAreaFVTCheckedException, UserNotAuthorizedException {
        IsA createdIsA = createIsaRelationship(term1, term2);
        FVTUtils.validateRelationship(createdIsA);
        System.out.println("Created Isa " + createdIsA);
        String guid = createdIsA.getGuid();

        IsA gotIsA = subjectAreaRelationship.isA().getByGUID(this.userId, guid);
        FVTUtils.validateRelationship(gotIsA);
        System.out.println("Got Isa " + createdIsA);

        IsA updateIsA = new IsA();
        updateIsA.setDescription("ddd2");
        IsA updatedIsA = subjectAreaRelationship.isA().update(this.userId, guid, updateIsA);
        if (!updatedIsA.getDescription().equals(updateIsA.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa update description not as expected");
        }
        if (!updatedIsA.getSource().equals(createdIsA.getSource())) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa update source not as expected");
        }
        if (!updatedIsA.getExpression().equals(createdIsA.getExpression())) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa update expression not as expected");
        }
        if (!updatedIsA.getSteward().equals(createdIsA.getSteward())) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa update steward not as expected");
        }
        if (!updatedIsA.getEnd1().getNodeGuid().equals(createdIsA.getEnd1().getNodeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa update end 1 not as expected");
        }
        if (!updatedIsA.getEnd2().getNodeGuid().equals(createdIsA.getEnd2().getNodeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa update end 2 not as expected");
        }
        System.out.println("Updated Isa " + createdIsA);
        IsA replaceIsA = new IsA();
        replaceIsA.setDescription("ddd3");
        IsA replacedIsA = subjectAreaRelationship.isA().replace(this.userId, guid, replaceIsA);
        FVTUtils.validateRelationship(replacedIsA);
        if (!replacedIsA.getDescription().equals(replaceIsA.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa replace description not as expected");
        }
        if (replacedIsA.getSource() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa replace source not as expected");
        }
        if (replacedIsA.getExpression() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa replace expression not as expected");
        }
        if (replacedIsA.getSteward() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa replace steward not as expected");
        }
        if (!replacedIsA.getEnd1().getNodeGuid().equals(createdIsA.getEnd1().getNodeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa replace end 1 not as expected");
        }
        if (!replacedIsA.getEnd2().getNodeGuid().equals(createdIsA.getEnd2().getNodeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa replace end 2 not as expected");
        }
        System.out.println("Replaced Isa " + createdIsA);
        subjectAreaRelationship.isA().delete(this.userId, guid);
        //FVTUtils.validateLine(gotIsa);
        System.out.println("Soft deleted Isa with userId=" + guid);
        gotIsA = subjectAreaRelationship.isA().restore(this.userId, guid);
        FVTUtils.validateRelationship(gotIsA);
        System.out.println("Restored Isa with userId=" + guid);
        subjectAreaRelationship.isA().delete(this.userId, guid);
        //FVTUtils.validateLine(gotIsa);
        System.out.println("Soft deleted Isa with userId=" + guid);
        subjectAreaRelationship.isA().purge(this.userId, guid);
        System.out.println("Hard deleted Isa with userId=" + guid);
    }

    private IsA createIsaRelationship(Term term1, Term term2) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        IsA isa = new IsA();
        isa.setDescription("ddd");
        isa.setExpression("Ex");
        isa.setSource("source");
        isa.setSteward("Stew");
        isa.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        isa.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());
        IsA createdIsA = subjectAreaRelationship.isA().create(this.userId, isa);
        FVTUtils.validateRelationship(createdIsA);
        FVTUtils.checkEnds(isa, createdIsA, "isa", "create");

        return createdIsA;
    }

    private void typedByFVT(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, SubjectAreaFVTCheckedException, UserNotAuthorizedException {
        TypedBy createdTermTYPEDBYRelationship = createTermTYPEDBYRelationship(term1, term2);
        FVTUtils.validateRelationship(createdTermTYPEDBYRelationship);
        System.out.println("Created TypedBy " + createdTermTYPEDBYRelationship);
        String guid = createdTermTYPEDBYRelationship.getGuid();

        TypedBy gotTermTYPEDBYRelationship = subjectAreaRelationship.typedBy().getByGUID(this.userId, guid);
        FVTUtils.validateRelationship(gotTermTYPEDBYRelationship);
        System.out.println("Got TypedBy " + createdTermTYPEDBYRelationship);

        TypedBy updateTermTYPEDBYRelationship = new TypedBy();
        updateTermTYPEDBYRelationship.setDescription("ddd2");
        TypedBy updatedTermTYPEDBYRelationship = subjectAreaRelationship.typedBy().update(this.userId, guid, updateTermTYPEDBYRelationship);
        FVTUtils.validateRelationship(updatedTermTYPEDBYRelationship);
        if (!updatedTermTYPEDBYRelationship.getDescription().equals(updateTermTYPEDBYRelationship.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: termTYPEDBYRelationship update description not as expected");
        }
        if (!updatedTermTYPEDBYRelationship.getSource().equals(createdTermTYPEDBYRelationship.getSource())) {
            throw new SubjectAreaFVTCheckedException("ERROR: termTYPEDBYRelationship update source not as expected");
        }
        if (!updatedTermTYPEDBYRelationship.getSteward().equals(createdTermTYPEDBYRelationship.getSteward())) {
            throw new SubjectAreaFVTCheckedException("ERROR: termTYPEDBYRelationship update steward not as expected");
        }
        FVTUtils.checkEnds(updatedTermTYPEDBYRelationship,createdTermTYPEDBYRelationship,"TYPEDBY","update");
        System.out.println("Updated TypedBy " + createdTermTYPEDBYRelationship);
        TypedBy replaceTermTYPEDBYRelationship = new TypedBy();
        replaceTermTYPEDBYRelationship.setDescription("ddd3");
        TypedBy replacedTermTYPEDBYRelationship = subjectAreaRelationship.typedBy().replace(this.userId, guid, replaceTermTYPEDBYRelationship);
        FVTUtils.validateRelationship(replacedTermTYPEDBYRelationship);
        if (!replacedTermTYPEDBYRelationship.getDescription().equals(replaceTermTYPEDBYRelationship.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: termTYPEDBYRelationship replace description not as expected");
        }
        if (replacedTermTYPEDBYRelationship.getSource() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: termTYPEDBYRelationship replace source not as expected");
        }
        if (replacedTermTYPEDBYRelationship.getSteward() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: termTYPEDBYRelationship replace steward not as expected");
        }

        FVTUtils.checkEnds(replacedTermTYPEDBYRelationship,createdTermTYPEDBYRelationship,"TYPEDBY","replace");
        System.out.println("Replaced TypedBy " + createdTermTYPEDBYRelationship);
        subjectAreaRelationship.typedBy().delete(this.userId, guid);
        //FVTUtils.validateLine(gotTermTYPEDBYRelationship);
        System.out.println("Soft deleted TypedBy with userId=" + guid);
        gotTermTYPEDBYRelationship = subjectAreaRelationship.typedBy().restore(this.userId, guid);
        FVTUtils.validateRelationship(gotTermTYPEDBYRelationship);
        System.out.println("Restored TypedBy with userId=" + guid);
        subjectAreaRelationship.typedBy().delete(this.userId, guid);
        //FVTUtils.validateLine(gotTermTYPEDBYRelationship);
        System.out.println("Soft deleted TypedBy with userId=" + guid);
        subjectAreaRelationship.typedBy().purge(this.userId, guid);
        System.out.println("Hard deleted TypedBy with userId=" + guid);
    }

    private TypedBy createTermTYPEDBYRelationship(Term term1, Term term2) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        TypedBy termTYPEDBYRelationship = new TypedBy();
        termTYPEDBYRelationship.setDescription("ddd");
        termTYPEDBYRelationship.setSource("source");
        termTYPEDBYRelationship.setSteward("Stew");
        termTYPEDBYRelationship.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        termTYPEDBYRelationship.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());
        TypedBy createdTermTYPEDBYRelationship = subjectAreaRelationship.typedBy().create(this.userId, termTYPEDBYRelationship);
        FVTUtils.validateRelationship(createdTermTYPEDBYRelationship);
        FVTUtils.checkEnds(termTYPEDBYRelationship, createdTermTYPEDBYRelationship, "TypedBy", "create");

        return createdTermTYPEDBYRelationship;
    }

    private void replacementTermFVT(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, SubjectAreaFVTCheckedException, UserNotAuthorizedException {
        ReplacementTerm createdReplacementTerm = createReplacementTerm(term1, term2);
        FVTUtils.validateRelationship(createdReplacementTerm);
        System.out.println("Created ReplacementTerm " + createdReplacementTerm);
        String guid = createdReplacementTerm.getGuid();

        ReplacementTerm gotReplacementTerm = subjectAreaRelationship.replacementTerm().getByGUID(this.userId, guid);
        FVTUtils.validateRelationship(gotReplacementTerm);
        System.out.println("Got ReplacementTerm " + createdReplacementTerm);

        ReplacementTerm updateReplacementTerm = new ReplacementTerm();
        updateReplacementTerm.setDescription("ddd2");
        ReplacementTerm updatedReplacementTerm = subjectAreaRelationship.replacementTerm().update(this.userId, guid, updateReplacementTerm);
        FVTUtils.validateRelationship(updatedReplacementTerm);
        if (!updatedReplacementTerm.getDescription().equals(updateReplacementTerm.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: replacementTerm update description not as expected");
        }
        if (!updatedReplacementTerm.getSource().equals(createdReplacementTerm.getSource())) {
            throw new SubjectAreaFVTCheckedException("ERROR: replacementTerm update source not as expected");
        }
        if (!updatedReplacementTerm.getExpression().equals(createdReplacementTerm.getExpression())) {
            throw new SubjectAreaFVTCheckedException("ERROR: replacementTerm update expression not as expected");
        }
        if (!updatedReplacementTerm.getSteward().equals(createdReplacementTerm.getSteward())) {
            throw new SubjectAreaFVTCheckedException("ERROR: replacementTerm update steward not as expected");
        }
        FVTUtils.checkEnds(updatedReplacementTerm,createdReplacementTerm,"replacementTerm","update");
        System.out.println("Updated ReplacementTerm " + createdReplacementTerm);
        ReplacementTerm replaceReplacementTerm = new ReplacementTerm();
        replaceReplacementTerm.setDescription("ddd3");
        ReplacementTerm replacedReplacementTerm = subjectAreaRelationship.replacementTerm().replace(this.userId, guid, replaceReplacementTerm);
        FVTUtils.validateRelationship(replacedReplacementTerm);
        if (!replacedReplacementTerm.getDescription().equals(replaceReplacementTerm.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: replacementTerm replace description not as expected");
        }
        if (replacedReplacementTerm.getSource() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: replacementTerm replace source not as expected");
        }
        if (replacedReplacementTerm.getExpression() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: replacementTerm replace expression not as expected");
        }
        if (replacedReplacementTerm.getSteward() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: replacementTerm replace steward not as expected");
        }

        FVTUtils.checkEnds(replacedReplacementTerm,createdReplacementTerm,"replacementTerm","replace");
        System.out.println("Replaced ReplacementTerm " + createdReplacementTerm);
        subjectAreaRelationship.replacementTerm().delete(this.userId, guid);
        //FVTUtils.validateLine(gotReplacementTerm);
        System.out.println("Soft deleted ReplacementTerm with userId=" + guid);
        gotReplacementTerm = subjectAreaRelationship.replacementTerm().restore(this.userId, guid);
        FVTUtils.validateRelationship(gotReplacementTerm);
        System.out.println("Restored ReplacementTerm with userId=" + guid);
        subjectAreaRelationship.replacementTerm().delete(this.userId, guid);
        //FVTUtils.validateLine(gotReplacementTerm);
        System.out.println("Soft deleted ReplacementTerm with userId=" + guid);
        subjectAreaRelationship.replacementTerm().purge(this.userId, guid);
        System.out.println("Hard deleted ReplacementTerm with userId=" + guid);
    }

    private ReplacementTerm createReplacementTerm(Term term1, Term term2) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ReplacementTerm replacementTerm = new ReplacementTerm();
        replacementTerm.setDescription("ddd");
        replacementTerm.setExpression("Ex");
        replacementTerm.setSource("source");
        replacementTerm.setSteward("Stew");
        replacementTerm.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        replacementTerm.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());
        ReplacementTerm createdReplacementTerm = subjectAreaRelationship.replacementTerm().create(this.userId, replacementTerm);
        FVTUtils.validateRelationship(createdReplacementTerm);
        FVTUtils.checkEnds(replacementTerm, createdReplacementTerm, "ReplacementTerm", "create");

        return createdReplacementTerm;
    }

    private void validvalueFVT(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, SubjectAreaFVTCheckedException, UserNotAuthorizedException {
        ValidValue createdValidValue = createValidValue(term1, term2);
        FVTUtils.validateRelationship(createdValidValue);
        System.out.println("Created ValidValue " + createdValidValue);
        String guid = createdValidValue.getGuid();

        ValidValue gotValidValue = subjectAreaRelationship.validValue().getByGUID(this.userId, guid);
        FVTUtils.validateRelationship(gotValidValue);
        System.out.println("Got ValidValue " + createdValidValue);

        ValidValue updateValidValue = new ValidValue();
        updateValidValue.setDescription("ddd2");
        ValidValue updatedValidValue = subjectAreaRelationship.validValue().update(this.userId, guid, updateValidValue);
        if (!updatedValidValue.getDescription().equals(updateValidValue.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: validValue update description not as expected");
        }
        if (!updatedValidValue.getSource().equals(createdValidValue.getSource())) {
            throw new SubjectAreaFVTCheckedException("ERROR: validValue update source not as expected");
        }
        if (!updatedValidValue.getExpression().equals(createdValidValue.getExpression())) {
            throw new SubjectAreaFVTCheckedException("ERROR: validValue update expression not as expected");
        }
        if (!updatedValidValue.getSteward().equals(createdValidValue.getSteward())) {
            throw new SubjectAreaFVTCheckedException("ERROR: validValue update steward not as expected");
        }

        FVTUtils.checkEnds(updatedValidValue, createdValidValue, "ValidValue", "update");

        System.out.println("Updated ValidValue " + createdValidValue);
        ValidValue replaceValidValue = new ValidValue();
        replaceValidValue.setDescription("ddd3");
        replaceValidValue.setGuid(createdValidValue.getGuid());
        ValidValue replacedValidValue = subjectAreaRelationship.validValue().replace(this.userId, guid, replaceValidValue);
        if (!replacedValidValue.getDescription().equals(replaceValidValue.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: validValue replace description not as expected");
        }
        if (replacedValidValue.getSource() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: validValue replace source not as expected");
        }
        if (replacedValidValue.getExpression() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: validValue replace expression not as expected");
        }
        if (replacedValidValue.getSteward() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: validValue replace steward not as expected");
        }
        FVTUtils.checkEnds(replacedValidValue, createdValidValue, "ValidValue", "replace");

        System.out.println("Replaced ValidValue " + createdValidValue);
        subjectAreaRelationship.validValue().delete(this.userId, guid);
        //FVTUtils.validateLine(gotValidValue);
        System.out.println("Soft deleted ValidValue with userId=" + guid);
        gotValidValue = subjectAreaRelationship.validValue().restore(this.userId, guid);
        FVTUtils.validateRelationship(gotValidValue);
        System.out.println("Restored ValidValue with userId=" + guid);
        subjectAreaRelationship.validValue().delete(this.userId, guid);
        //FVTUtils.validateLine(gotValidValue);
        System.out.println("Soft deleted ValidValue with userId=" + guid);
        subjectAreaRelationship.validValue().purge(this.userId, guid);
        System.out.println("Hard deleted ValidValue with userId=" + guid);
    }

    private ValidValue createValidValue(Term term1, Term term2) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        ValidValue validValue = new ValidValue();
        validValue.setDescription("ddd");
        validValue.setExpression("Ex");
        validValue.setSource("source");
        validValue.setSteward("Stew");
        validValue.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        validValue.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());
        ValidValue createdValidValue = subjectAreaRelationship.validValue().create(this.userId, validValue);
        FVTUtils.validateRelationship(createdValidValue);
        FVTUtils.checkEnds(validValue, createdValidValue, "ValidValue", "create");

        return createdValidValue;
    }

    private void preferredtermFVT(Term term1, Term term2) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, SubjectAreaFVTCheckedException {
        PreferredTerm createdPreferredTerm = createPreferredTerm(term1, term2);
        FVTUtils.validateRelationship(createdPreferredTerm);
        System.out.println("Created PreferredTerm " + createdPreferredTerm);
        String guid = createdPreferredTerm.getGuid();

        PreferredTerm gotPreferredTerm = subjectAreaRelationship.preferredTerm().getByGUID(this.userId, guid);
        FVTUtils.validateRelationship(gotPreferredTerm);
        System.out.println("Got PreferredTerm " + createdPreferredTerm);

        PreferredTerm updatePreferredTerm = new PreferredTerm();
        updatePreferredTerm.setDescription("ddd2");
        PreferredTerm updatedPreferredTerm = subjectAreaRelationship.preferredTerm().update(this.userId, guid, updatePreferredTerm);
        FVTUtils.validateRelationship(updatedPreferredTerm);
        if (!updatedPreferredTerm.getDescription().equals(updatePreferredTerm.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: preferredTerm update description not as expected");
        }
        if (!updatedPreferredTerm.getSource().equals(createdPreferredTerm.getSource())) {
            throw new SubjectAreaFVTCheckedException("ERROR: preferredTerm update source not as expected");
        }
        if (!updatedPreferredTerm.getExpression().equals(createdPreferredTerm.getExpression())) {
            throw new SubjectAreaFVTCheckedException("ERROR: preferredTerm update expression not as expected");
        }
        if (!updatedPreferredTerm.getSteward().equals(createdPreferredTerm.getSteward())) {
            throw new SubjectAreaFVTCheckedException("ERROR: preferredTerm update steward not as expected");
        }
        FVTUtils.checkEnds(updatedPreferredTerm,createdPreferredTerm,"PreferredTerm","update");
        System.out.println("Updated PreferredTerm " + createdPreferredTerm);
        PreferredTerm replacePreferredTerm = new PreferredTerm();
        replacePreferredTerm.setDescription("ddd3");
        PreferredTerm replacedPreferredTerm = subjectAreaRelationship.preferredTerm().replace(this.userId, guid, replacePreferredTerm);
        FVTUtils.validateRelationship(replacedPreferredTerm);
        if (!replacedPreferredTerm.getDescription().equals(replacePreferredTerm.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: preferredTerm replace description not as expected");
        }
        if (replacedPreferredTerm.getSource() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: preferredTerm replace source not as expected");
        }
        if (replacedPreferredTerm.getExpression() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: preferredTerm replace expression not as expected");
        }
        if (replacedPreferredTerm.getSteward() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: preferredTerm replace steward not as expected");
        }
        FVTUtils.checkEnds(replacedPreferredTerm,createdPreferredTerm,"PreferredTerm","replace");
        System.out.println("Replaced PreferredTerm " + createdPreferredTerm);
        subjectAreaRelationship.preferredTerm().delete(this.userId, guid);
        //FVTUtils.validateLine(gotPreferredTerm);
        System.out.println("Soft deleted PreferredTerm with userId=" + guid);
        gotPreferredTerm = subjectAreaRelationship.preferredTerm().restore(this.userId, guid);
        FVTUtils.validateRelationship(gotPreferredTerm);
        System.out.println("restored PreferredTerm with userId=" + guid);
        subjectAreaRelationship.preferredTerm().delete(this.userId, guid);
        //FVTUtils.validateLine(gotPreferredTerm);
        System.out.println("Soft deleted PreferredTerm with userId=" + guid);
        subjectAreaRelationship.preferredTerm().purge(this.userId, guid);
        System.out.println("Hard deleted PreferredTerm with userId=" + guid);
    }

    private PreferredTerm createPreferredTerm(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        PreferredTerm preferredTerm = new PreferredTerm();
        preferredTerm.setDescription("ddd");
        preferredTerm.setExpression("Ex");
        preferredTerm.setSource("source");
        preferredTerm.setSteward("Stew");
        preferredTerm.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        preferredTerm.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());
        PreferredTerm createdPreferredTerm = subjectAreaRelationship.preferredTerm().create(this.userId, preferredTerm);
        FVTUtils.validateRelationship(createdPreferredTerm);
        FVTUtils.checkEnds(preferredTerm, createdPreferredTerm, "PreferredTerm", "create");

        return createdPreferredTerm;
    }

    private void usedincontextFVT(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, SubjectAreaFVTCheckedException, UserNotAuthorizedException {
        UsedInContext createdUsedInContext = createUsedInContext(term1, term2);
        FVTUtils.validateRelationship(createdUsedInContext);
        System.out.println("Created UsedInContext " + createdUsedInContext);
        String guid = createdUsedInContext.getGuid();

        UsedInContext gotUsedInContext = subjectAreaRelationship.usedInContext().getByGUID(this.userId, guid);
        FVTUtils.validateRelationship(gotUsedInContext);
        System.out.println("Got UsedInContext " + createdUsedInContext);

        UsedInContext updateUsedInContext = new UsedInContext();
        updateUsedInContext.setDescription("ddd2");
        UsedInContext updatedUsedInContext = subjectAreaRelationship.usedInContext().update(this.userId, guid, updateUsedInContext);
        FVTUtils.validateRelationship(updatedUsedInContext);
        if (!updatedUsedInContext.getDescription().equals(updateUsedInContext.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: usedInContext update description not as expected");
        }
        if (!updatedUsedInContext.getSource().equals(createdUsedInContext.getSource())) {
            throw new SubjectAreaFVTCheckedException("ERROR: usedInContext update source not as expected");
        }
        if (!updatedUsedInContext.getExpression().equals(createdUsedInContext.getExpression())) {
            throw new SubjectAreaFVTCheckedException("ERROR: usedInContext update expression not as expected");
        }
        if (!updatedUsedInContext.getSteward().equals(createdUsedInContext.getSteward())) {
            throw new SubjectAreaFVTCheckedException("ERROR: usedInContext update steward not as expected");
        }

        FVTUtils.checkEnds(updatedUsedInContext,createdUsedInContext,"UsedInContext","update");
        System.out.println("Updated UsedInContext " + createdUsedInContext);
        UsedInContext replaceUsedInContext = new UsedInContext();
        replaceUsedInContext.setDescription("ddd3");
        UsedInContext replacedUsedInContext = subjectAreaRelationship.usedInContext().replace(this.userId, guid, replaceUsedInContext);
        FVTUtils.validateRelationship(replacedUsedInContext);
        if (!replacedUsedInContext.getDescription().equals(replaceUsedInContext.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: usedInContext replace description not as expected");
        }
        if (replacedUsedInContext.getSource() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: usedInContext replace source not as expected");
        }
        if (replacedUsedInContext.getExpression() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: usedInContext replace expression not as expected");
        }
        if (replacedUsedInContext.getSteward() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: usedInContext replace steward not as expected");
        }
        FVTUtils.checkEnds(replacedUsedInContext,createdUsedInContext,"UsedInContext","replace");
        System.out.println("Replaced UsedInContext " + createdUsedInContext);
        subjectAreaRelationship.usedInContext().delete(this.userId, guid);
        //FVTUtils.validateLine(gotUsedInContext);
        System.out.println("Soft deleted UsedInContext with userId=" + guid);
        gotUsedInContext = subjectAreaRelationship.usedInContext().restore(this.userId, guid);
        FVTUtils.validateRelationship(gotUsedInContext);
        System.out.println("Restored UsedInContext with userId=" + guid);
        subjectAreaRelationship.usedInContext().delete(this.userId, guid);
        //FVTUtils.validateLine(gotUsedInContext);
        System.out.println("Soft deleted UsedInContext with userId=" + guid);
        subjectAreaRelationship.usedInContext().purge(this.userId, guid);
        System.out.println("Hard deleted UsedInContext with userId=" + guid);
    }

    private UsedInContext createUsedInContext(Term term1, Term term2) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        UsedInContext usedInContext = new UsedInContext();
        usedInContext.setDescription("ddd");
        usedInContext.setExpression("Ex");
        usedInContext.setSource("source");
        usedInContext.setSteward("Stew");
        usedInContext.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        usedInContext.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());
        UsedInContext createdUsedInContext = subjectAreaRelationship.usedInContext().create(this.userId, usedInContext);
        FVTUtils.validateRelationship(createdUsedInContext);
        FVTUtils.checkEnds(usedInContext, createdUsedInContext, "UsedInContext", "create");
        return createdUsedInContext;
    }

    private void translationFVT(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, SubjectAreaFVTCheckedException, UserNotAuthorizedException {
        Translation createdTranslation = createTranslation(term1, term2);
        FVTUtils.validateRelationship(createdTranslation);
        System.out.println("Created Translation " + createdTranslation);
        String guid = createdTranslation.getGuid();

        Translation gotTranslation = subjectAreaRelationship.translation().getByGUID(this.userId, guid);
        FVTUtils.validateRelationship(gotTranslation);
        System.out.println("Got Translation " + createdTranslation);

        Translation updateTranslation = new Translation();
        updateTranslation.setDescription("ddd2");
        Translation updatedTranslation = subjectAreaRelationship.translation().update(this.userId, guid, updateTranslation);
        FVTUtils.validateRelationship(updatedTranslation);
        if (!updatedTranslation.getDescription().equals(updateTranslation.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: translation update description not as expected");
        }
        if (!updatedTranslation.getSource().equals(createdTranslation.getSource())) {
            throw new SubjectAreaFVTCheckedException("ERROR: translation update source not as expected");
        }
        if (!updatedTranslation.getExpression().equals(createdTranslation.getExpression())) {
            throw new SubjectAreaFVTCheckedException("ERROR: translation update expression not as expected");
        }
        if (!updatedTranslation.getSteward().equals(createdTranslation.getSteward())) {
            throw new SubjectAreaFVTCheckedException("ERROR: translation update steward not as expected");
        }
        FVTUtils.checkEnds(updatedTranslation, createdTranslation, "translation", "update");

        System.out.println("Updated Translation " + createdTranslation);
        Translation replaceTranslation = new Translation();
        replaceTranslation.setDescription("ddd3");
        Translation replacedTranslation = subjectAreaRelationship.translation().replace(this.userId, guid, replaceTranslation);
        FVTUtils.validateRelationship(replacedTranslation);
        if (!replacedTranslation.getDescription().equals(replaceTranslation.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: translation replace description not as expected");
        }
        if (replacedTranslation.getSource() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: translation replace source not as expected");
        }
        if (replacedTranslation.getExpression() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: translation replace expression not as expected");
        }
        if (replacedTranslation.getSteward() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: translation replace steward not as expected");
        }
        FVTUtils.checkEnds(replacedTranslation, updatedTranslation, "translation", "replace");

        System.out.println("Replaced Translation " + createdTranslation);
        subjectAreaRelationship.translation().delete(this.userId, guid);
        //FVTUtils.validateLine(gotTranslation);
        System.out.println("Soft deleted Translation with userId=" + guid);
        gotTranslation = subjectAreaRelationship.translation().restore(this.userId, guid);
        FVTUtils.validateRelationship(gotTranslation);
        System.out.println("Restored Translation with userId=" + guid);
        subjectAreaRelationship.translation().delete(this.userId, guid);
        //FVTUtils.validateLine(gotTranslation);
        System.out.println("Soft deleted Translation with userId=" + guid);
        subjectAreaRelationship.translation().purge(this.userId, guid);
        System.out.println("Hard deleted Translation with userId=" + guid);
    }

    private Translation createTranslation(Term term1, Term term2) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Translation translation = new Translation();
        translation.setDescription("ddd");
        translation.setExpression("Ex");
        translation.setSource("source");
        translation.setSteward("Stew");
        translation.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        translation.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());
        Translation createdTranslation = subjectAreaRelationship.translation().create(this.userId, translation);
        FVTUtils.validateRelationship(createdTranslation);
        FVTUtils.checkEnds(translation, createdTranslation, "translations", "create");

        return createdTranslation;
    }

    private void hasaFVT(Term term1, Term term3) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, SubjectAreaFVTCheckedException {
        HasA createdHasATerm = createHasA(term1, term3);
        FVTUtils.validateRelationship(createdHasATerm);
        System.out.println("Created Hasa " + createdHasATerm);
        String guid = createdHasATerm.getGuid();

        HasA gotHasATerm = subjectAreaRelationship.hasA().getByGUID(this.userId, guid);
        FVTUtils.validateRelationship(gotHasATerm);
        System.out.println("Got Hasa " + createdHasATerm);
        HasA updateHasATerm = new HasA();
        updateHasATerm.setDescription("ddd2");
        HasA updatedHasATerm = subjectAreaRelationship.hasA().update(this.userId, guid, updateHasATerm);
        FVTUtils.validateRelationship(updatedHasATerm);
        if (!updatedHasATerm.getDescription().equals(updateHasATerm.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: HASARelationship update description not as expected");
        }
        if (!updatedHasATerm.getSource().equals(createdHasATerm.getSource())) {
            throw new SubjectAreaFVTCheckedException("ERROR: HASARelationship update source not as expected");
        }
        if (!updatedHasATerm.getSteward().equals(createdHasATerm.getSteward())) {
            throw new SubjectAreaFVTCheckedException("ERROR: HASARelationship update steward not as expected");
        }
        FVTUtils.checkEnds(updatedHasATerm, createdHasATerm, "has-a", "update");

        System.out.println("Updated HASARelationship " + createdHasATerm);
        HasA replaceHasATerm = new HasA();
        replaceHasATerm.setDescription("ddd3");
        HasA replacedHasATerm = subjectAreaRelationship.hasA().replace(this.userId, guid, replaceHasATerm);
        FVTUtils.validateRelationship(replacedHasATerm);
        if (!replacedHasATerm.getDescription().equals(replaceHasATerm.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: HASARelationship replace description not as expected");
        }
        if (replacedHasATerm.getSource() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: HASARelationship replace source not as expected");
        }
        if (replacedHasATerm.getSteward() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: HASARelationship replace steward not as expected");
        }
        FVTUtils.checkEnds(updatedHasATerm, replacedHasATerm, "has-a", "replace");

        System.out.println("Replaced HASARelationship " + createdHasATerm);

        subjectAreaRelationship.hasA().delete(this.userId, guid);
        //FVTUtils.validateLine(gotHASATerm);
        System.out.println("Soft deleted Hasa with userId=" + guid);
        gotHasATerm = subjectAreaRelationship.hasA().restore(this.userId, guid);
        FVTUtils.validateRelationship(gotHasATerm);
        System.out.println("Restored Hasa with userId=" + guid);
        subjectAreaRelationship.hasA().delete(this.userId, guid);
        //FVTUtils.validateLine(gotHASATerm);
        System.out.println("Soft deleted Hasa with userId=" + guid);
        subjectAreaRelationship.hasA().purge(this.userId, guid);
        System.out.println("Hard deleted Hasa with userId=" + guid);
    }

    private HasA createHasA(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        HasA hasA = new HasA();
        hasA.setDescription("ddd");
        hasA.setSource("source");
        hasA.setSteward("Stew");
        hasA.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        hasA.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());


        HasA createdTermHasARelationship = subjectAreaRelationship.hasA().create(this.userId, hasA);
        FVTUtils.validateRelationship(createdTermHasARelationship);
        FVTUtils.checkEnds(hasA, createdTermHasARelationship, "Has-a", "create");

        return createdTermHasARelationship;
    }

    private void relatedtermFVT(Term term1, Term term3) throws InvalidParameterException, PropertyServerException, SubjectAreaFVTCheckedException, UserNotAuthorizedException {
        RelatedTerm createdRelatedTerm = createRelatedTerm(term1, term3);
        FVTUtils.validateRelationship(createdRelatedTerm);
        System.out.println("Created RelatedTerm " + createdRelatedTerm);
        String guid = createdRelatedTerm.getGuid();

        RelatedTerm gotRelatedTerm = subjectAreaRelationship.relatedTerm().getByGUID(this.userId, guid);
        FVTUtils.validateRelationship(gotRelatedTerm);
        System.out.println("Got RelatedTerm " + createdRelatedTerm);
        RelatedTerm updateRelatedTerm = new RelatedTerm();
        updateRelatedTerm.setDescription("ddd2");
        updateRelatedTerm.setGuid(createdRelatedTerm.getGuid());
        RelatedTerm updatedRelatedTerm = subjectAreaRelationship.relatedTerm().update(this.userId, guid, updateRelatedTerm);
        FVTUtils.validateRelationship(updatedRelatedTerm);
        if (!updatedRelatedTerm.getDescription().equals(updateRelatedTerm.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: RelatedTerm update description not as expected");
        }
        if (!updatedRelatedTerm.getSource().equals(createdRelatedTerm.getSource())) {
            throw new SubjectAreaFVTCheckedException("ERROR: RelatedTerm update source not as expected");
        }
        if (!updatedRelatedTerm.getExpression().equals(createdRelatedTerm.getExpression())) {
            throw new SubjectAreaFVTCheckedException("ERROR: RelatedTerm update expression not as expected");
        }
        if (!updatedRelatedTerm.getSteward().equals(createdRelatedTerm.getSteward())) {
            throw new SubjectAreaFVTCheckedException("ERROR: RelatedTerm update steward not as expected");
        }
        FVTUtils.checkEnds(updatedRelatedTerm,createdRelatedTerm,"RelatedTerm","update");
        System.out.println("Updated RelatedTerm " + createdRelatedTerm);
        RelatedTerm replaceRelatedTerm = new RelatedTerm();
        replaceRelatedTerm.setDescription("ddd3");
        replaceRelatedTerm.setGuid(createdRelatedTerm.getGuid());
        RelatedTerm replacedRelatedTerm = subjectAreaRelationship.relatedTerm().replace(this.userId, guid, replaceRelatedTerm);
        FVTUtils.validateRelationship(replacedRelatedTerm);
        if (!replacedRelatedTerm.getDescription().equals(replaceRelatedTerm.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: RelatedTerm replace description not as expected");
        }
        if (replacedRelatedTerm.getSource() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: RelatedTerm replace source not as expected");
        }
        if (replacedRelatedTerm.getExpression() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: RelatedTerm replace expression not as expected");
        }
        if (replacedRelatedTerm.getSteward() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: RelatedTerm replace steward not as expected");
        }
        FVTUtils.checkEnds(replacedRelatedTerm,createdRelatedTerm,"RelatedTerm","replace");
        System.out.println("Replaced RelatedTerm " + createdRelatedTerm);

        subjectAreaRelationship.relatedTerm().delete(this.userId, guid);
        //FVTUtils.validateLine(gotRelatedTerm);
        System.out.println("Soft deleted RelatedTerm with userId=" + guid);
        gotRelatedTerm = subjectAreaRelationship.relatedTerm().restore(this.userId, guid);
        FVTUtils.validateRelationship(gotRelatedTerm);
        System.out.println("Restored RelatedTerm with userId=" + guid);
        subjectAreaRelationship.relatedTerm().delete(this.userId, guid);
        //FVTUtils.validateLine(gotRelatedTerm);
        System.out.println("Soft deleted RelatedTerm with userId=" + guid);
        subjectAreaRelationship.relatedTerm().purge(this.userId, guid);
        System.out.println("Hard deleted RelatedTerm with userId=" + guid);
    }

    private RelatedTerm createRelatedTerm(Term term1, Term term2) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        RelatedTerm relatedterm = new RelatedTerm();
        relatedterm.setDescription("ddd");
        relatedterm.setExpression("Ex");
        relatedterm.setSource("source");
        relatedterm.setSteward("Stew");
        relatedterm.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        relatedterm.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());
        RelatedTerm createdRelatedTerm = subjectAreaRelationship.relatedTerm().create(this.userId, relatedterm);
        FVTUtils.validateRelationship(createdRelatedTerm);
        FVTUtils.checkEnds(relatedterm, createdRelatedTerm, "RelatedTerm", "create");

        return createdRelatedTerm;

    }

    private void antonymFVT(Term term1, Term term3) throws InvalidParameterException, PropertyServerException, SubjectAreaFVTCheckedException, UserNotAuthorizedException {
        Antonym createdAntonym = createAntonym(term1, term3);
        FVTUtils.validateRelationship(createdAntonym);
        System.out.println("Created Antonym " + createdAntonym);
        String guid = createdAntonym.getGuid();

        Antonym gotAntonym = subjectAreaRelationship.antonym().getByGUID(this.userId, guid);
        FVTUtils.validateRelationship(gotAntonym);
        System.out.println("Got Antonym " + createdAntonym);
        Antonym updateAntonym = new Antonym();
        updateAntonym.setDescription("ddd2");
        Antonym updatedAntonym = subjectAreaRelationship.antonym().update(this.userId, guid, updateAntonym);
        FVTUtils.validateRelationship(updatedAntonym);
        if (!updatedAntonym.getDescription().equals(updateAntonym.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: Antonym update description not as expected");
        }
        if (!updatedAntonym.getSource().equals(createdAntonym.getSource())) {
            throw new SubjectAreaFVTCheckedException("ERROR: Antonym update source not as expected");
        }
        if (!updatedAntonym.getExpression().equals(createdAntonym.getExpression())) {
            throw new SubjectAreaFVTCheckedException("ERROR: Antonym update expression not as expected");
        }
        if (!updatedAntonym.getSteward().equals(createdAntonym.getSteward())) {
            throw new SubjectAreaFVTCheckedException("ERROR: Antonym update steward not as expected");
        }
        FVTUtils.checkEnds(updatedAntonym,createdAntonym,"Antonym","update");
        System.out.println("Updated Antonym " + createdAntonym);
        Antonym replaceAntonym = new Antonym();
        replaceAntonym.setDescription("ddd3");
        replaceAntonym.setGuid(createdAntonym.getGuid());
        Antonym replacedAntonym = subjectAreaRelationship.antonym().replace(this.userId, guid, replaceAntonym);
        FVTUtils.validateRelationship(replacedAntonym);
        if (!replacedAntonym.getDescription().equals(replaceAntonym.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: Antonym replace description not as expected");
        }
        if (replacedAntonym.getSource() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: Antonym replace source not as expected");
        }
        if (replacedAntonym.getExpression() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: Antonym replace expression not as expected");
        }
        if (replacedAntonym.getSteward() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: Antonym replace steward not as expected");
        }
        FVTUtils.checkEnds(updatedAntonym,createdAntonym,"Antonym","replace");
        System.out.println("Replaced Antonym " + createdAntonym);

        subjectAreaRelationship.antonym().delete(this.userId, guid);
        //FVTUtils.validateLine(gotAntonym);
        System.out.println("Soft deleted Antonym with userId=" + guid);
        gotAntonym = subjectAreaRelationship.antonym().restore(this.userId, guid);
        FVTUtils.validateRelationship(gotAntonym);
        System.out.println("Restored Antonym with userId=" + guid);
        subjectAreaRelationship.antonym().delete(this.userId, guid);
        //FVTUtils.validateLine(gotAntonym);
        System.out.println("Soft deleted Antonym with userId=" + guid);
        subjectAreaRelationship.antonym().purge(this.userId, guid);
        System.out.println("Hard deleted Antonym with userId=" + guid);
    }

    private Antonym createAntonym(Term term1, Term term2) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Antonym antonym = new Antonym();
        antonym.setDescription("ddd");
        antonym.setExpression("Ex");
        antonym.setSource("source");
        antonym.setSteward("Stew");
        antonym.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        antonym.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());
        Antonym createdAntonym = subjectAreaRelationship.antonym().create(this.userId, antonym);
        FVTUtils.validateRelationship(createdAntonym);
        FVTUtils.checkEnds(antonym, createdAntonym, "Antonym", "create");
        return createdAntonym;
    }

    private void synonymFVT(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, SubjectAreaFVTCheckedException, UserNotAuthorizedException {
        Synonym createdSynonym = createSynonym(term1, term2);
        FVTUtils.validateRelationship(createdSynonym);
        System.out.println("Created Synonym " + createdSynonym);
        String guid = createdSynonym.getGuid();

        Synonym gotSynonym = subjectAreaRelationship.synonym().getByGUID(this.userId, guid);
        FVTUtils.validateRelationship(gotSynonym);
        System.out.println("Got Synonym " + createdSynonym);

        Synonym updateSynonym = new Synonym();
        updateSynonym.setDescription("ddd2");
        Synonym updatedSynonym = subjectAreaRelationship.synonym().update(this.userId, guid, updateSynonym);
        FVTUtils.validateRelationship(updatedSynonym);
        if (!updatedSynonym.getDescription().equals(updateSynonym.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: synonym update description not as expected");
        }
        if (!updatedSynonym.getSource().equals(createdSynonym.getSource())) {
            throw new SubjectAreaFVTCheckedException("ERROR: synonym update source not as expected");
        }
        if (!updatedSynonym.getExpression().equals(createdSynonym.getExpression())) {
            throw new SubjectAreaFVTCheckedException("ERROR: synonym update expression not as expected");
        }
        if (!updatedSynonym.getSteward().equals(createdSynonym.getSteward())) {
            throw new SubjectAreaFVTCheckedException("ERROR: synonym update steward not as expected");
        }

        FVTUtils.checkEnds(updatedSynonym, createdSynonym, "synonym", "update");

        System.out.println("Updated Synonym " + createdSynonym);
        Synonym replaceSynonym = new Synonym();
        replaceSynonym.setDescription("ddd3");
        Synonym replacedSynonym = subjectAreaRelationship.synonym().replace(this.userId, guid, replaceSynonym);
        FVTUtils.validateRelationship(replacedSynonym);
        if (!replacedSynonym.getDescription().equals(replaceSynonym.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: synonym replace description not as expected");
        }
        if (replacedSynonym.getSource() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: synonym replace source not as expected");
        }
        if (replacedSynonym.getExpression() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: synonym replace expression not as expected");
        }
        if (replacedSynonym.getSteward() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: synonym replace steward not as expected");
        }
        FVTUtils.checkEnds(updatedSynonym, replacedSynonym, "synonym", "replace");

        System.out.println("Replaced Synonym " + createdSynonym);
        subjectAreaRelationship.synonym().delete(this.userId, guid);
        //FVTUtils.validateLine(gotSynonym);
        System.out.println("Soft deleted Synonym with userId=" + guid);
        gotSynonym = subjectAreaRelationship.synonym().restore(this.userId, guid);
        FVTUtils.validateRelationship(gotSynonym);
        System.out.println("Restored Synonym with userId=" + guid);
        subjectAreaRelationship.synonym().delete(this.userId, guid);
        //FVTUtils.validateLine(gotSynonym);
        System.out.println("Soft deleted Synonym with userId=" + guid);
        subjectAreaRelationship.synonym().purge(this.userId, guid);

        System.out.println("Hard deleted Synonym with userId=" + guid);
    }

    public Synonym createSynonym(Term term1, Term term2) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Synonym synonym = new Synonym();
        synonym.setDescription("ddd");
        synonym.setExpression("Ex");
        synonym.setSource("source");
        synonym.setSteward("Stew");
        synonym.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        synonym.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());
        Synonym createdSynonym = subjectAreaRelationship.synonym().create(this.userId, synonym);
        FVTUtils.validateRelationship(createdSynonym);
        FVTUtils.checkEnds(synonym, createdSynonym, "synonym", "create");
        return createdSynonym;
    }


    public IsATypeOf createTermISATypeOFRelationship(Term term1, Term term2) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        IsATypeOf termIsATypeOFRelationship = new IsATypeOf();
        termIsATypeOFRelationship.setDescription("ddd");
        termIsATypeOFRelationship.setSource("source");
        termIsATypeOFRelationship.setSteward("Stew");
        termIsATypeOFRelationship.getEnd1().setNodeGuid(term1.getSystemAttributes().getGUID());
        termIsATypeOFRelationship.getEnd2().setNodeGuid(term2.getSystemAttributes().getGUID());
        IsATypeOf createdTermIsATypeOFRelationship = subjectAreaRelationship.isaTypeOf().create(this.userId, termIsATypeOFRelationship);
        FVTUtils.validateRelationship(createdTermIsATypeOFRelationship);
        FVTUtils.checkEnds(termIsATypeOFRelationship, createdTermIsATypeOFRelationship, "IsaTypeOf", "create");

        System.out.println("Created termISATypeOFRelationship " + createdTermIsATypeOFRelationship);
        return createdTermIsATypeOFRelationship;
    }

    private void termCategorizationFVT(Term term, Category category) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, SubjectAreaFVTCheckedException {
        Categorization createdTermCategorizationRelationship = createTermCategorization(term, category);
        FVTUtils.validateRelationship(createdTermCategorizationRelationship);
        System.out.println("Created TermCategorizationRelationship " + createdTermCategorizationRelationship);
        String guid = createdTermCategorizationRelationship.getGuid();

        Categorization gotTermCategorizationRelationship = subjectAreaRelationship.termCategorization().getByGUID(this.userId, guid);
        FVTUtils.validateRelationship(gotTermCategorizationRelationship);
        System.out.println("Got TermCategorizationRelationship " + createdTermCategorizationRelationship);

        Categorization updateTermCategorizationRelationship = new Categorization();
        updateTermCategorizationRelationship.setDescription("ddd2");
        Categorization updatedTermCategorizationRelationship = subjectAreaRelationship.termCategorization().update(this.userId, guid, updateTermCategorizationRelationship);
        FVTUtils.validateRelationship(updatedTermCategorizationRelationship);
        FVTUtils.checkEnds(updatedTermCategorizationRelationship,createdTermCategorizationRelationship,"TermCategorization","update");

        if (!updatedTermCategorizationRelationship.getDescription().equals(updateTermCategorizationRelationship.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: TermCategorization update description not as expected");
        }
        if (updatedTermCategorizationRelationship.getStatus() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: TermCategorization update status not as expected");
        }

        System.out.println("Updated TermCategorizationRelationship " + createdTermCategorizationRelationship);
        Categorization replaceTermCategorizationRelationship = new Categorization();
        replaceTermCategorizationRelationship.setDescription("ddd3");
        Categorization replacedTermCategorizationRelationship = subjectAreaRelationship.termCategorization().replace(this.userId, guid, replaceTermCategorizationRelationship);
        FVTUtils.validateRelationship(replacedTermCategorizationRelationship);
        if (!replacedTermCategorizationRelationship.getDescription().equals(replaceTermCategorizationRelationship.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: TermCategorization replace description not as expected");
        }
        if (replacedTermCategorizationRelationship.getStatus() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: TermCategorization replace source not as expected");
        }

        FVTUtils.checkEnds(replacedTermCategorizationRelationship,createdTermCategorizationRelationship,"TermCategorization","replace");
        System.out.println("Replaced TermCategorizationRelationship " + createdTermCategorizationRelationship);
        subjectAreaRelationship.termCategorization().delete(this.userId, guid);
        //FVTUtils.validateLine(gotTermCategorizationRelationship);
        System.out.println("Soft deleted TermCategorizationRelationship with userId=" + guid);
        gotTermCategorizationRelationship = subjectAreaRelationship.termCategorization().restore(this.userId, guid);
        FVTUtils.validateRelationship(gotTermCategorizationRelationship);
        System.out.println("Restored TermCategorizationRelationship with userId=" + guid);
        subjectAreaRelationship.termCategorization().delete(this.userId, guid);
        //FVTUtils.validateLine(gotTermCategorizationRelationship);
        System.out.println("Soft deleted TermCategorization with userId=" + guid);
        subjectAreaRelationship.termCategorization().purge(this.userId, guid);
        System.out.println("Hard deleted TermCategorization with userId=" + guid);
    }


    private void termAnchorFVT(Term term) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        // No create for TermAnchor - because this OMAS cannot create a Term without a glossary
        String termGuid = term.getSystemAttributes().getGUID();
        String glossaryGuid = term.getGlossary().getGuid();
        String relationshipGuid = term.getGlossary().getRelationshipguid();

        TermAnchor gotTermAnchorRelationship = subjectAreaRelationship.termAnchor().getByGUID(this.userId, relationshipGuid);
        FVTUtils.validateRelationship(gotTermAnchorRelationship);
        System.out.println("Got TermAnchorRelationship " + gotTermAnchorRelationship);

        // no update or replace as this relationship has no properties

        subjectAreaRelationship.termAnchor().delete(this.userId, relationshipGuid);
        ///FVTUtils.validateLine(gotTermAnchorRelationship);
        System.out.println("Soft deleted TermAnchorRelationship with relationshipGuid=" + relationshipGuid);
        gotTermAnchorRelationship = subjectAreaRelationship.termAnchor().restore(this.userId, relationshipGuid);
        FVTUtils.validateRelationship(gotTermAnchorRelationship);
        System.out.println("Restored TermAnchorRelationship with relationshipGuid=" + relationshipGuid);
        subjectAreaRelationship.termAnchor().delete(this.userId, relationshipGuid);
        //FVTUtils.validateLine(gotTermAnchorRelationship);
        System.out.println("Soft deleted TermAnchor with relationshipGuid=" + relationshipGuid);
        subjectAreaRelationship.termAnchor().purge(this.userId, relationshipGuid);
        System.out.println("Hard deleted TermAnchor with relationshipGuid=" + relationshipGuid);

        TermAnchor newTermAnchorRelationship = new TermAnchor();
        newTermAnchorRelationship.getEnd1().setNodeGuid(glossaryGuid);
        newTermAnchorRelationship.getEnd2().setNodeGuid(termGuid);
        FVTUtils.validateRelationship(subjectAreaRelationship.termAnchor().create(userId, newTermAnchorRelationship));
    }

    private void categoryAnchorFVT(Category category) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        // No create for CategoryAnchor - because this OMAS cannot create a Category without a glossary
        String categoryGuid = category.getSystemAttributes().getGUID();
        String glossaryGuid = category.getGlossary().getGuid();
        String relationshipGuid = category.getGlossary().getRelationshipguid();
        CategoryAnchor gotCategoryAnchorRelationship = subjectAreaRelationship.categoryAnchor().getByGUID(this.userId, relationshipGuid);
        FVTUtils.validateRelationship(gotCategoryAnchorRelationship);
        System.out.println("Got CategoryAnchorRelationship " + gotCategoryAnchorRelationship);
        // no update as this relationship has no properties

        subjectAreaRelationship.categoryAnchor().delete(this.userId, relationshipGuid);
        //FVTUtils.validateLine(gotCategoryAnchorRelationship);
        System.out.println("Soft deleted CategoryAnchorRelationship with relationshipGuid=" + relationshipGuid);
        gotCategoryAnchorRelationship = subjectAreaRelationship.categoryAnchor().restore(this.userId, relationshipGuid);
        FVTUtils.validateRelationship(gotCategoryAnchorRelationship);
        System.out.println("Restored CategoryAnchorRelationship with relationshipGuid=" + relationshipGuid);
        subjectAreaRelationship.categoryAnchor().delete(this.userId, relationshipGuid);
        //FVTUtils.validateLine(gotCategoryAnchorRelationship);
        System.out.println("Soft deleted CategoryAnchor with relationshipGuid=" + relationshipGuid);
        subjectAreaRelationship.categoryAnchor().purge(this.userId, relationshipGuid);
        System.out.println("Hard deleted CategoryAnchor with relationshipGuid=" + relationshipGuid);

        CategoryAnchor newCategoryAnchorRelationship = new CategoryAnchor();
        newCategoryAnchorRelationship.getEnd1().setNodeGuid(glossaryGuid);
        newCategoryAnchorRelationship.getEnd2().setNodeGuid(categoryGuid);
        FVTUtils.validateRelationship(subjectAreaRelationship.categoryAnchor().create(userId, newCategoryAnchorRelationship));
    }

    public Categorization createTermCategorization(Term term, Category category) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        Categorization termCategorization = new Categorization();
        termCategorization.getEnd1().setNodeGuid(category.getSystemAttributes().getGUID());
        termCategorization.getEnd2().setNodeGuid(term.getSystemAttributes().getGUID());
        Categorization createdTermCategorization = subjectAreaRelationship.termCategorization().create(this.userId, termCategorization);
        FVTUtils.validateRelationship(createdTermCategorization);
        FVTUtils.checkEnds(termCategorization, createdTermCategorization, "TermCategorizationRelationship", "create");
        System.out.println("Created TermCategorizationRelationship " + createdTermCategorization);

        return createdTermCategorization;
    }

    private void projectScopeFVT(Project project, Term term) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        ProjectScope createdProjectScope = createProjectScope(project, term);
        FVTUtils.validateRelationship(createdProjectScope);
        if (projectFVT.getProjectTerms(project.getSystemAttributes().getGUID()).size() !=1){
            throw new SubjectAreaFVTCheckedException("ERROR: Project terms were not as expected");
        }

        System.out.println("Created ProjectScopeRelationship " + createdProjectScope);
        String guid = createdProjectScope.getGuid();

        ProjectScope gotProjectScopeRelationship = subjectAreaRelationship.projectScope().getByGUID(this.userId, guid);
        FVTUtils.validateRelationship(gotProjectScopeRelationship);
        System.out.println("Got ProjectScopeRelationship " + gotProjectScopeRelationship);

        ProjectScope updateProjectScope = new ProjectScope();
        updateProjectScope.setDescription("ddd2");
        updateProjectScope.setGuid(createdProjectScope.getGuid());
        ProjectScope updatedProjectScope = subjectAreaRelationship.projectScope().update(this.userId, guid, updateProjectScope);
        FVTUtils.validateRelationship(updatedProjectScope);
        if (!updatedProjectScope.getDescription().equals(updateProjectScope.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: Project scope  update scopeDescription not as expected");
        }

        FVTUtils.checkEnds(updatedProjectScope,createdProjectScope,"ProjectScope","update");

        System.out.println("Updated ProjectScopeRelationship " + createdProjectScope);
        ProjectScope replaceProjectScope = new ProjectScope();
        replaceProjectScope.setDescription("ddd3");
        ProjectScope replacedProjectScope = subjectAreaRelationship.projectScope().replace(this.userId, guid, replaceProjectScope);
        FVTUtils.validateRelationship(replacedProjectScope);
        if (!replacedProjectScope.getDescription().equals(replaceProjectScope.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: project scope replace scope description not as expected");
        }
        FVTUtils.checkEnds(replacedProjectScope,createdProjectScope,"ProjectScope","replace");

        System.out.println("Replaced ProjectScopeRelationship " + createdProjectScope);
        subjectAreaRelationship.projectScope().delete(this.userId, guid);
        //FVTUtils.validateLine(gotProjectScopeRelationship);
        System.out.println("Soft deleted ProjectScopeRelationship with userId=" + guid);
        gotProjectScopeRelationship = subjectAreaRelationship.projectScope().restore(this.userId, guid);
        FVTUtils.validateRelationship(gotProjectScopeRelationship);
        System.out.println("Restored ProjectScopeRelationship with userId=" + guid);
        subjectAreaRelationship.projectScope().delete(this.userId, guid);
        //FVTUtils.validateLine(gotProjectScopeRelationship);
        System.out.println("Soft deleted ProjectScopeRelationship with userId=" + guid);
        subjectAreaRelationship.projectScope().purge(this.userId, guid);

        System.out.println("Hard deleted ProjectScopeRelationship with userId=" + guid);
    }

    protected ProjectScope createProjectScope(Project project, Term term) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        ProjectScope projectScope = new ProjectScope();
        projectScope.getEnd1().setNodeGuid(project.getSystemAttributes().getGUID());
        projectScope.getEnd2().setNodeGuid(term.getSystemAttributes().getGUID());
        ProjectScope createdProjectScope = subjectAreaRelationship.projectScope().create(this.userId, projectScope);
        FVTUtils.validateRelationship(createdProjectScope);
        System.out.println("CreatedProjectScopeRelationship " + createdProjectScope);
        return createdProjectScope;
    }

    private void categoryHierarchyLinkFVT(Category parent, Category child) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, SubjectAreaFVTCheckedException {
        CategoryHierarchyLink categoryHierarchyLink = createCategoryHierarchyLink(parent, child);
        String guid = categoryHierarchyLink.getGuid();
        CategoryHierarchyLink gotCategoryHierarchyLink = subjectAreaRelationship.categoryHierarchyLink().getByGUID(this.userId, guid);
        FVTUtils.validateRelationship(gotCategoryHierarchyLink);
        System.out.println("Got CategoryHierarchyLink " + categoryHierarchyLink);
        Category gotChild = subjectAreaCategory.getByGUID(userId, child.getSystemAttributes().getGUID());
        checkParent(parent, gotChild);
        subjectAreaRelationship.categoryHierarchyLink().delete(this.userId, guid);
        System.out.println("Soft deleted CategoryHierarchyLink with userId=" + guid);
        gotCategoryHierarchyLink = subjectAreaRelationship.categoryHierarchyLink().restore(this.userId, guid);
        FVTUtils.validateRelationship(gotCategoryHierarchyLink);
        System.out.println("Restored CategoryHierarchyLink with userId=" + guid);
        subjectAreaRelationship.categoryHierarchyLink().delete(this.userId, guid);
        System.out.println("Soft deleted CategoryHierarchyLink with userId=" + guid);
        subjectAreaRelationship.categoryHierarchyLink().purge(this.userId, guid);
        System.out.println("Hard deleted CategoryHierarchyLink with userId=" + guid);
    }

    public CategoryHierarchyLink createCategoryHierarchyLink(Category parent, Category child) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        CategoryHierarchyLink categoryHierarchyLink = new CategoryHierarchyLink();
        categoryHierarchyLink.getEnd1().setNodeGuid(parent.getSystemAttributes().getGUID());
        categoryHierarchyLink.getEnd2().setNodeGuid(child.getSystemAttributes().getGUID());
        CategoryHierarchyLink createdCategoryHierarchyLink = subjectAreaRelationship.categoryHierarchyLink().create(this.userId, categoryHierarchyLink);
        FVTUtils.validateRelationship(createdCategoryHierarchyLink);
        FVTUtils.checkEnds(categoryHierarchyLink, createdCategoryHierarchyLink, "CategoryHierarchyLink", "create");

        System.out.println("Created CategoryHierarchyLink " + createdCategoryHierarchyLink);

        return createdCategoryHierarchyLink;
    }

    public void checkParent(Category parent, Category gotChildCategory) throws SubjectAreaFVTCheckedException {
        if (gotChildCategory.getParentCategory() != null) {
            CategorySummary categorySummary = gotChildCategory.getParentCategory();
            String parentGuid = parent.getSystemAttributes().getGUID();
            String parentGuidFromChild = categorySummary.getGuid();
            if(!parentGuid.equals(parentGuidFromChild)) {
                throw new SubjectAreaFVTCheckedException("ERROR parent category guid - " + parentGuid
                        + " no equal parent guid " + parentGuidFromChild + " from child.");
            }
        } else {
            throw new SubjectAreaFVTCheckedException("ERROR parent category is null");
        }
    }
}
