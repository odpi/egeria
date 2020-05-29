/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;

import java.io.IOException;
import java.util.List;

/**
 * FVT resource to call subject area term client API
 */
public class RelationshipsFVT {
    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Test Glossary for relationships FVT";
    private static final String DEFAULT_TEST_TERM_NAME = "Test term A1";
    private static final String DEFAULT_TEST_TERM_NAME2 = "Test term B1";
    private static final String DEFAULT_TEST_TERM_NAME3 = "Test term C1";
    private static final String DEFAULT_TEST_CAT_NAME1 = "Test cat A1";
    private static final String DEFAULT_TEST_CAT_NAME2 = "Test cat B1";
    private static final String DEFAULT_TEST_CAT_NAME3 = "Test cat C1";
    private SubjectAreaRelationship subjectAreaRelationship = null;
    private GlossaryFVT glossaryFVT = null;
    private TermFVT termFVT = null;
    private CategoryFVT catFVT = null;
    private ProjectFVT projectFVT = null;
    private String url = null;
    private String serverName = null;
    private String userId = null;

    public RelationshipsFVT(String url, String serverName, String userId) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException {
        this.url = url;
        subjectAreaRelationship = new SubjectAreaImpl(serverName, url).getSubjectAreaRelationship();
        termFVT = new TermFVT(url, serverName, userId);
        catFVT = new CategoryFVT(url, serverName, userId);
        glossaryFVT = new GlossaryFVT(url, serverName, userId);
        projectFVT = new ProjectFVT(url, serverName, userId);
        this.serverName = serverName;
        this.userId = userId;
    }

    public static void main(String args[]) {
        try {
            String url = RunAllFVT.getUrl(args);
            runWith2Servers(url);
        } catch (IOException e1) {
            System.out.println("Error getting user input");
        } catch (SubjectAreaCheckedException e) {
            System.out.println("ERROR: " + e.getErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        } catch (SubjectAreaFVTCheckedException e) {
            System.out.println("ERROR: " + e.getMessage() );
        }

    }

    public static void runWith2Servers(String url) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException {
        RelationshipsFVT fvt = new RelationshipsFVT(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        fvt.run();
        RelationshipsFVT fvt2 = new RelationshipsFVT(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
        fvt2.run();
    }

    public static void runIt(String url, String serverName, String userId) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException {
        RelationshipsFVT fvt = new RelationshipsFVT(url, serverName, userId);
        fvt.run();
    }

    public void run() throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException, SubjectAreaFVTCheckedException {
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
        FVTUtils.validateLine(createTermCategorization(term1, cat1));
        term1relationshipcount++;
        cat1RelationshipCount++;
        checkRelationshipNumberforCategory(cat1RelationshipCount, cat1);
        FVTUtils.validateLine(createTermCategorization(term1, cat2));
        term1relationshipcount++;
        cat2RelationshipCount++;
        checkRelationshipNumberforCategory(cat2RelationshipCount, cat1);
        FVTUtils.validateLine(createTermCategorization(term2, cat1));
        cat1RelationshipCount++;
        checkRelationshipNumberforCategory(cat1RelationshipCount, cat1);
        term2relationshipcount++;
        FVTUtils.validateLine(createTermCategorization(term3, cat1));
        term3relationshipcount++;
        cat1RelationshipCount++;
        checkRelationshipNumberforCategory(cat1RelationshipCount, cat1);
        checkRelationshipNumberforTerm(term1relationshipcount, term1);
        checkRelationshipNumberforTerm(term2relationshipcount, term2);
        checkRelationshipNumberforTerm(term3relationshipcount, term3);
        System.out.println("get term relationships");
        List<Line> term1Relationships = termFVT.getTermRelationships(term1);

        System.out.println("Get paged term relationships");
        int offset = 0;

        int numberofrelationships = 0;
        while (offset < term1relationshipcount) {
            System.out.println("Get paged term relationships offset = " + offset + ",pageSize=3");
            List<Line> term1PagedRelationships = termFVT.getTermRelationships(term1, null, offset, 3, SequencingOrder.GUID, null);
            numberofrelationships = numberofrelationships + term1PagedRelationships.size();
            offset += 3;
        }
        if (term1relationshipcount != numberofrelationships) {
            throw new SubjectAreaFVTCheckedException("Expected " + term1Relationships.size() + " got " + numberofrelationships);
        }
        Project project = projectFVT.createProject("Test Project For ProjectScope FVT");
        projectScopeFVT(project, term1);
        projectFVT.deleteProject(project.getSystemAttributes().getGUID());
        projectFVT.purgeProject(project.getSystemAttributes().getGUID());

    }

    private void checkRelationshipNumberforTerm(int expectedrelationshipcount, Term term) throws UserNotAuthorizedException, UnexpectedResponseException, InvalidParameterException, FunctionNotSupportedException, MetadataServerUncontactableException, SubjectAreaFVTCheckedException {
        int actualCount = termFVT.getTermRelationships(term).size();
        if (expectedrelationshipcount != actualCount) {
            throw new SubjectAreaFVTCheckedException("ERROR: expected " + expectedrelationshipcount + " for " + term.getName() + " got " + actualCount);
        }
    }

    private void checkRelationshipNumberforGlossary(int expectedrelationshipcount, Glossary glossary) throws UserNotAuthorizedException, UnexpectedResponseException, InvalidParameterException, FunctionNotSupportedException, MetadataServerUncontactableException, SubjectAreaFVTCheckedException {
        int actualCount = glossaryFVT.getGlossaryRelationships(glossary).size();
        if (expectedrelationshipcount != actualCount) {
            throw new SubjectAreaFVTCheckedException("ERROR: expected " + expectedrelationshipcount + " for " + glossary.getName() + " got " + actualCount);
        }
    }

    private void checkRelationshipNumberforCategory(int expectedrelationshipcount, Category category) throws UserNotAuthorizedException, UnexpectedResponseException, InvalidParameterException, FunctionNotSupportedException, MetadataServerUncontactableException, SubjectAreaFVTCheckedException {
        int actualCount = catFVT.getCategoryRelationships(category).size();
        if (expectedrelationshipcount != actualCount) {
            throw new SubjectAreaFVTCheckedException("ERROR: expected " + expectedrelationshipcount + " for " + category.getName() + " got " + actualCount);
        }
    }

    private void createSomeTermRelationships(Term term1, Term term2, Term term3) throws SubjectAreaFVTCheckedException, InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, ClassificationException, FunctionNotSupportedException {
        FVTUtils.validateLine(createValidValue(term1, term2));
        FVTUtils.validateLine(createAntonym(term1, term2));
        FVTUtils.validateLine(createIsaRelationship(term1, term2));
        FVTUtils.validateLine(createPreferredTerm(term1, term2));
        FVTUtils.validateLine(createRelatedTerm(term1, term2));
        FVTUtils.validateLine(createTermHASARelationship(term1, term2));
        FVTUtils.validateLine(createSynonym(term1, term3));
        FVTUtils.validateLine(createReplacementTerm(term1, term2));
        FVTUtils.validateLine(createTermTYPEDBYRelationship(term1, term2));
        FVTUtils.validateLine(createTranslation(term1, term2));
        FVTUtils.validateLine(createUsedInContext(term1, term2));
        FVTUtils.validateLine(createTermISATypeOFRelationship(term1, term2));
    }

    private void isatypeofFVT(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, RelationshipNotPurgedException {
        IsaTypeOf createdTermISATypeOFRelationship = createTermISATypeOFRelationship(term1, term2);
        String guid = createdTermISATypeOFRelationship.getGuid();

        IsaTypeOf gotTermISATypeOFRelationship = subjectAreaRelationship.getTermISATypeOFRelationship(this.userId, guid);
        FVTUtils.validateLine(gotTermISATypeOFRelationship);
        System.out.println("Got IsaTypeOf " + createdTermISATypeOFRelationship);

        IsaTypeOf updateTermISATypeOFRelationship = new IsaTypeOf();
        updateTermISATypeOFRelationship.setDescription("ddd2");
        updateTermISATypeOFRelationship.setGuid(createdTermISATypeOFRelationship.getGuid());
        IsaTypeOf updatedTermISATypeOFRelationship = subjectAreaRelationship.updateTermISATypeOFRelationship(this.userId, updateTermISATypeOFRelationship);
        FVTUtils.validateLine(updatedTermISATypeOFRelationship);
        if (!updatedTermISATypeOFRelationship.getDescription().equals(updateTermISATypeOFRelationship.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: IsaTypeOf update description not as expected");
        }
        if (!updatedTermISATypeOFRelationship.getSource().equals(createdTermISATypeOFRelationship.getSource())) {
            throw new SubjectAreaFVTCheckedException("ERROR: IsaTypeOf update source not as expected");
        }
        if (!updatedTermISATypeOFRelationship.getSteward().equals(createdTermISATypeOFRelationship.getSteward())) {
            throw new SubjectAreaFVTCheckedException("ERROR: IsaTypeOf update steward not as expected");
        }
        if (!updatedTermISATypeOFRelationship.getSubTypeGuid().equals(createdTermISATypeOFRelationship.getSubTypeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: IsaTypeOf update end 1 not as expected");
        }
        if (!updatedTermISATypeOFRelationship.getSuperTypeGuid().equals(createdTermISATypeOFRelationship.getSuperTypeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: IsaTypeOf update end 2 not as expected");
        }
        System.out.println("Updated IsaTypeOf " + createdTermISATypeOFRelationship);
        IsaTypeOf replaceTermISATypeOFRelationship = new IsaTypeOf();
        replaceTermISATypeOFRelationship.setDescription("ddd3");
        replaceTermISATypeOFRelationship.setGuid(createdTermISATypeOFRelationship.getGuid());
        IsaTypeOf replacedTermISATypeOFRelationship = subjectAreaRelationship.replaceTermISATypeOFRelationship(this.userId, replaceTermISATypeOFRelationship);
        FVTUtils.validateLine(replacedTermISATypeOFRelationship);
        if (!replacedTermISATypeOFRelationship.getDescription().equals(replaceTermISATypeOFRelationship.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: IsaTypeOf replace description not as expected");
        }
        if (replacedTermISATypeOFRelationship.getSource() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: IsaTypeOf replace source not as expected");
        }
        if (replacedTermISATypeOFRelationship.getSteward() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: IsaTypeOf replace steward not as expected");
        }
        if (!replacedTermISATypeOFRelationship.getSuperTypeGuid().equals(createdTermISATypeOFRelationship.getSuperTypeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: IsaTypeOf replace end 1 not as expected");
        }
        if (!replacedTermISATypeOFRelationship.getSubTypeGuid().equals(createdTermISATypeOFRelationship.getSubTypeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: IsaTypeOf replace end 2 not as expected");
        }
        System.out.println("Replaced IsaTypeOf " + createdTermISATypeOFRelationship);
        gotTermISATypeOFRelationship = subjectAreaRelationship.deleteTermISATypeOFRelationship(this.userId, guid);
        FVTUtils.validateLine(gotTermISATypeOFRelationship);
        System.out.println("Soft deleted IsaTypeOf with userId=" + guid);
        gotTermISATypeOFRelationship = subjectAreaRelationship.restoreIsaTypeOfRelationship(this.userId, guid);
        FVTUtils.validateLine(gotTermISATypeOFRelationship);
        System.out.println("Restored IsaTypeOf with userId=" + guid);
        gotTermISATypeOFRelationship = subjectAreaRelationship.deleteTermISATypeOFRelationship(this.userId, guid);
        FVTUtils.validateLine(gotTermISATypeOFRelationship);
        System.out.println("Soft deleted IsaTypeOf with userId=" + guid);
        subjectAreaRelationship.purgeTermISATypeOFRelationship(this.userId, guid);
        System.out.println("Hard deleted IsaTypeOf with userId=" + guid);
    }

    private void isaFVT(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, RelationshipNotPurgedException {
        Isa createdIsa = createIsaRelationship(term1, term2);
        FVTUtils.validateLine(createdIsa);
        System.out.println("Created Isa " + createdIsa);
        String guid = createdIsa.getGuid();

        Isa gotIsa = subjectAreaRelationship.getIsaRelationship(this.userId, guid);
        FVTUtils.validateLine(gotIsa);
        System.out.println("Got Isa " + createdIsa);

        Isa updateIsa = new Isa();
        updateIsa.setDescription("ddd2");
        updateIsa.setGuid(createdIsa.getGuid());
        Isa updatedIsa = subjectAreaRelationship.updateIsaRelationship(this.userId, updateIsa);
        if (!updatedIsa.getDescription().equals(updateIsa.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa update description not as expected");
        }
        if (!updatedIsa.getSource().equals(createdIsa.getSource())) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa update source not as expected");
        }
        if (!updatedIsa.getExpression().equals(createdIsa.getExpression())) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa update expression not as expected");
        }
        if (!updatedIsa.getSteward().equals(createdIsa.getSteward())) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa update steward not as expected");
        }
        if (!updatedIsa.getTermGuid().equals(createdIsa.getTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa update end 1 not as expected");
        }
        if (!updatedIsa.getSpecialisedTermGuid().equals(createdIsa.getSpecialisedTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa update end 2 not as expected");
        }
        System.out.println("Updated Isa " + createdIsa);
        Isa replaceIsa = new Isa();
        replaceIsa.setDescription("ddd3");
        replaceIsa.setGuid(createdIsa.getGuid());
        Isa replacedIsa = subjectAreaRelationship.replaceIsaRelationship(this.userId, replaceIsa);
        FVTUtils.validateLine(replacedIsa);
        if (!replacedIsa.getDescription().equals(replaceIsa.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa replace description not as expected");
        }
        if (replacedIsa.getSource() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa replace source not as expected");
        }
        if (replacedIsa.getExpression() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa replace expression not as expected");
        }
        if (replacedIsa.getSteward() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa replace steward not as expected");
        }
        if (!replacedIsa.getSpecialisedTermGuid().equals(createdIsa.getSpecialisedTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa replace end 1 not as expected");
        }
        if (!replacedIsa.getTermGuid().equals(createdIsa.getTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: isa replace end 2 not as expected");
        }
        System.out.println("Replaced Isa " + createdIsa);
        gotIsa = subjectAreaRelationship.deleteIsaRelationship(this.userId, guid);
        FVTUtils.validateLine(gotIsa);
        System.out.println("Soft deleted Isa with userId=" + guid);
        gotIsa = subjectAreaRelationship.restoreIsaRelationship(this.userId, guid);
        FVTUtils.validateLine(gotIsa);
        System.out.println("Restored Isa with userId=" + guid);
        gotIsa = subjectAreaRelationship.deleteIsaRelationship(this.userId, guid);
        FVTUtils.validateLine(gotIsa);
        System.out.println("Soft deleted Isa with userId=" + guid);
        subjectAreaRelationship.purgeIsaRelationship(this.userId, guid);
        System.out.println("Hard deleted Isa with userId=" + guid);
    }

    private Isa createIsaRelationship(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException {
        Isa isa = new Isa();
        isa.setDescription("ddd");
        isa.setExpression("Ex");
        isa.setSource("source");
        isa.setSteward("Stew");
        isa.setSpecialisedTermGuid(term1.getSystemAttributes().getGUID());
        isa.setTermGuid(term2.getSystemAttributes().getGUID());
        Isa createdIsa = subjectAreaRelationship.createIsaRelationship(this.userId, isa);
        FVTUtils.validateLine(createdIsa);
        FVTUtils.checkGuidEnd1s("Isa", term1.getSystemAttributes().getGUID(), createdIsa.getSpecialisedTermGuid());
        FVTUtils.checkGuidEnd2s("Isa", term2.getSystemAttributes().getGUID(), createdIsa.getTermGuid());

        return createdIsa;
    }

    private void typedByFVT(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, RelationshipNotPurgedException {
        TypedBy createdTermTYPEDBYRelationship = createTermTYPEDBYRelationship(term1, term2);
        FVTUtils.validateLine(createdTermTYPEDBYRelationship);
        System.out.println("Created TypedBy " + createdTermTYPEDBYRelationship);
        String guid = createdTermTYPEDBYRelationship.getGuid();

        TypedBy gotTermTYPEDBYRelationship = subjectAreaRelationship.getTermTYPEDBYRelationship(this.userId, guid);
        FVTUtils.validateLine(gotTermTYPEDBYRelationship);
        System.out.println("Got TypedBy " + createdTermTYPEDBYRelationship);

        TypedBy updateTermTYPEDBYRelationship = new TypedBy();
        updateTermTYPEDBYRelationship.setDescription("ddd2");
        updateTermTYPEDBYRelationship.setGuid(createdTermTYPEDBYRelationship.getGuid());
        TypedBy updatedTermTYPEDBYRelationship = subjectAreaRelationship.updateTermTYPEDBYRelationship(this.userId, updateTermTYPEDBYRelationship);
        FVTUtils.validateLine(updatedTermTYPEDBYRelationship);
        if (!updatedTermTYPEDBYRelationship.getDescription().equals(updateTermTYPEDBYRelationship.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: termTYPEDBYRelationship update description not as expected");
        }
        if (!updatedTermTYPEDBYRelationship.getSource().equals(createdTermTYPEDBYRelationship.getSource())) {
            throw new SubjectAreaFVTCheckedException("ERROR: termTYPEDBYRelationship update source not as expected");
        }
        if (!updatedTermTYPEDBYRelationship.getSteward().equals(createdTermTYPEDBYRelationship.getSteward())) {
            throw new SubjectAreaFVTCheckedException("ERROR: termTYPEDBYRelationship update steward not as expected");
        }
        if (!updatedTermTYPEDBYRelationship.getAttributeGuid().equals(createdTermTYPEDBYRelationship.getAttributeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: termTYPEDBYRelationship update end 1 not as expected");
        }
        if (!updatedTermTYPEDBYRelationship.getTypeGuid().equals(createdTermTYPEDBYRelationship.getTypeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: termTYPEDBYRelationship update end 2 not as expected");
        }
        System.out.println("Updated TypedBy " + createdTermTYPEDBYRelationship);
        TypedBy replaceTermTYPEDBYRelationship = new TypedBy();
        replaceTermTYPEDBYRelationship.setDescription("ddd3");
        replaceTermTYPEDBYRelationship.setGuid(createdTermTYPEDBYRelationship.getGuid());
        TypedBy replacedTermTYPEDBYRelationship = subjectAreaRelationship.replaceTermTYPEDBYRelationship(this.userId, replaceTermTYPEDBYRelationship);
        FVTUtils.validateLine(replacedTermTYPEDBYRelationship);
        if (!replacedTermTYPEDBYRelationship.getDescription().equals(replaceTermTYPEDBYRelationship.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: termTYPEDBYRelationship replace description not as expected");
        }
        if (replacedTermTYPEDBYRelationship.getSource() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: termTYPEDBYRelationship replace source not as expected");
        }
        if (replacedTermTYPEDBYRelationship.getSteward() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: termTYPEDBYRelationship replace steward not as expected");
        }
        if (!replacedTermTYPEDBYRelationship.getAttributeGuid().equals(createdTermTYPEDBYRelationship.getAttributeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: termTYPEDBYRelationship replace end 1 not as expected");
        }
        if (!replacedTermTYPEDBYRelationship.getTypeGuid().equals(createdTermTYPEDBYRelationship.getTypeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: termTYPEDBYRelationship replace end 2 not as expected");
        }
        System.out.println("Replaced TypedBy " + createdTermTYPEDBYRelationship);
        gotTermTYPEDBYRelationship = subjectAreaRelationship.deleteTermTYPEDBYRelationship(this.userId, guid);
        FVTUtils.validateLine(gotTermTYPEDBYRelationship);
        System.out.println("Soft deleted TypedBy with userId=" + guid);
        gotTermTYPEDBYRelationship = subjectAreaRelationship.restoreTypedByRelationship(this.userId, guid);
        FVTUtils.validateLine(gotTermTYPEDBYRelationship);
        System.out.println("Restored TypedBy with userId=" + guid);
        gotTermTYPEDBYRelationship = subjectAreaRelationship.deleteTermTYPEDBYRelationship(this.userId, guid);
        FVTUtils.validateLine(gotTermTYPEDBYRelationship);
        System.out.println("Soft deleted TypedBy with userId=" + guid);
        subjectAreaRelationship.purgeTermTYPEDBYRelationship(this.userId, guid);
        System.out.println("Hard deleted TypedBy with userId=" + guid);
    }

    private TypedBy createTermTYPEDBYRelationship(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException {
        TypedBy termTYPEDBYRelationship = new TypedBy();
        termTYPEDBYRelationship.setDescription("ddd");
        termTYPEDBYRelationship.setSource("source");
        termTYPEDBYRelationship.setSteward("Stew");
        termTYPEDBYRelationship.setAttributeGuid(term1.getSystemAttributes().getGUID());
        termTYPEDBYRelationship.setTypeGuid(term2.getSystemAttributes().getGUID());
        TypedBy createdTermTYPEDBYRelationship = subjectAreaRelationship.createTermTYPEDBYRelationship(this.userId, termTYPEDBYRelationship);
        FVTUtils.validateLine(createdTermTYPEDBYRelationship);
        FVTUtils.checkGuidEnd1s("TypedBy", term1.getSystemAttributes().getGUID(), createdTermTYPEDBYRelationship.getAttributeGuid());
        FVTUtils.checkGuidEnd2s("TypedBy", term2.getSystemAttributes().getGUID(), createdTermTYPEDBYRelationship.getTypeGuid());
        return createdTermTYPEDBYRelationship;
    }

    private void replacementTermFVT(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, RelationshipNotPurgedException {
        ReplacementTerm createdReplacementTerm = createReplacementTerm(term1, term2);
        FVTUtils.validateLine(createdReplacementTerm);
        System.out.println("Created ReplacementTerm " + createdReplacementTerm);
        String guid = createdReplacementTerm.getGuid();

        ReplacementTerm gotReplacementTerm = subjectAreaRelationship.getReplacementTermRelationship(this.userId, guid);
        FVTUtils.validateLine(gotReplacementTerm);
        System.out.println("Got ReplacementTerm " + createdReplacementTerm);

        ReplacementTerm updateReplacementTerm = new ReplacementTerm();
        updateReplacementTerm.setDescription("ddd2");
        updateReplacementTerm.setGuid(createdReplacementTerm.getGuid());
        ReplacementTerm updatedReplacementTerm = subjectAreaRelationship.updateReplacementTermRelationship(this.userId, updateReplacementTerm);
        FVTUtils.validateLine(updatedReplacementTerm);
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
        if (!updatedReplacementTerm.getReplacedTermGuid().equals(createdReplacementTerm.getReplacedTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: replacementTerm update end 1 not as expected");
        }
        if (!updatedReplacementTerm.getReplacementTermGuid().equals(createdReplacementTerm.getReplacementTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: replacementTerm update end 2 not as expected");
        }
        System.out.println("Updated ReplacementTerm " + createdReplacementTerm);
        ReplacementTerm replaceReplacementTerm = new ReplacementTerm();
        replaceReplacementTerm.setDescription("ddd3");
        replaceReplacementTerm.setGuid(createdReplacementTerm.getGuid());
        ReplacementTerm replacedReplacementTerm = subjectAreaRelationship.replaceReplacementTermRelationship(this.userId, replaceReplacementTerm);
        FVTUtils.validateLine(replacedReplacementTerm);
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
        if (!replacedReplacementTerm.getReplacedTermGuid().equals(createdReplacementTerm.getReplacedTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: replacementTerm replace end 1 not as expected");
        }
        if (!replacedReplacementTerm.getReplacementTermGuid().equals(createdReplacementTerm.getReplacementTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: replacementTerm replace end 2 not as expected");
        }
        System.out.println("Replaced ReplacementTerm " + createdReplacementTerm);
        gotReplacementTerm = subjectAreaRelationship.deleteReplacementTermRelationship(this.userId, guid);
        FVTUtils.validateLine(gotReplacementTerm);
        System.out.println("Soft deleted ReplacementTerm with userId=" + guid);
        gotReplacementTerm = subjectAreaRelationship.restoreReplacementTermRelationship(this.userId, guid);
        FVTUtils.validateLine(gotReplacementTerm);
        System.out.println("Restored ReplacementTerm with userId=" + guid);
        gotReplacementTerm = subjectAreaRelationship.deleteReplacementTermRelationship(this.userId, guid);
        FVTUtils.validateLine(gotReplacementTerm);
        System.out.println("Soft deleted ReplacementTerm with userId=" + guid);
        subjectAreaRelationship.purgeReplacementTermRelationship(this.userId, guid);
        System.out.println("Hard deleted ReplacementTerm with userId=" + guid);
    }

    private ReplacementTerm createReplacementTerm(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException {
        ReplacementTerm replacementTerm = new ReplacementTerm();
        replacementTerm.setDescription("ddd");
        replacementTerm.setExpression("Ex");
        replacementTerm.setSource("source");
        replacementTerm.setSteward("Stew");
        replacementTerm.setReplacedTermGuid(term1.getSystemAttributes().getGUID());
        replacementTerm.setReplacementTermGuid(term2.getSystemAttributes().getGUID());
        ReplacementTerm createdReplacementTerm = subjectAreaRelationship.createReplacementTermRelationship(this.userId, replacementTerm);
        FVTUtils.validateLine(createdReplacementTerm);
        FVTUtils.checkGuidEnd1s("ReplacementTerm", term1.getSystemAttributes().getGUID(), createdReplacementTerm.getReplacedTermGuid());
        FVTUtils.checkGuidEnd2s("ReplacementTerm", term2.getSystemAttributes().getGUID(), createdReplacementTerm.getReplacementTermGuid());

        return createdReplacementTerm;
    }

    private void validvalueFVT(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, RelationshipNotPurgedException {
        ValidValue createdValidValue = createValidValue(term1, term2);
        FVTUtils.validateLine(createdValidValue);
        System.out.println("Created ValidValue " + createdValidValue);
        String guid = createdValidValue.getGuid();

        ValidValue gotValidValue = subjectAreaRelationship.getValidValueRelationship(this.userId, guid);
        FVTUtils.validateLine(gotValidValue);
        System.out.println("Got ValidValue " + createdValidValue);

        ValidValue updateValidValue = new ValidValue();
        updateValidValue.setDescription("ddd2");
        updateValidValue.setGuid(createdValidValue.getGuid());
        ValidValue updatedValidValue = subjectAreaRelationship.updateValidValueRelationship(this.userId, updateValidValue);
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
        if (!updatedValidValue.getValidValueGuid().equals(createdValidValue.getValidValueGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: validValue update end 1 not as expected");
        }
        if (!updatedValidValue.getTermGuid().equals(createdValidValue.getTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: validValue update end 2 not as expected");
        }
        System.out.println("Updated ValidValue " + createdValidValue);
        ValidValue replaceValidValue = new ValidValue();
        replaceValidValue.setDescription("ddd3");
        replaceValidValue.setGuid(createdValidValue.getGuid());
        ValidValue replacedValidValue = subjectAreaRelationship.replaceValidValueRelationship(this.userId, replaceValidValue);
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
        if (!replacedValidValue.getValidValueGuid().equals(createdValidValue.getValidValueGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: validValue replace end 1 not as expected");
        }
        if (!replacedValidValue.getTermGuid().equals(createdValidValue.getTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: validValue replace end 2 not as expected");
        }
        System.out.println("Replaced ValidValue " + createdValidValue);
        gotValidValue = subjectAreaRelationship.deleteValidValueRelationship(this.userId, guid);
        FVTUtils.validateLine(gotValidValue);
        System.out.println("Soft deleted ValidValue with userId=" + guid);
        gotValidValue = subjectAreaRelationship.restoreValidValueRelationship(this.userId, guid);
        FVTUtils.validateLine(gotValidValue);
        System.out.println("Restored ValidValue with userId=" + guid);
        gotValidValue = subjectAreaRelationship.deleteValidValueRelationship(this.userId, guid);
        FVTUtils.validateLine(gotValidValue);
        System.out.println("Soft deleted ValidValue with userId=" + guid);
        subjectAreaRelationship.purgeValidValueRelationship(this.userId, guid);
        System.out.println("Hard deleted ValidValue with userId=" + guid);
    }

    private ValidValue createValidValue(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException {
        ValidValue validValue = new ValidValue();
        validValue.setDescription("ddd");
        validValue.setExpression("Ex");
        validValue.setSource("source");
        validValue.setSteward("Stew");
        validValue.setTermGuid(term1.getSystemAttributes().getGUID());
        validValue.setValidValueGuid(term2.getSystemAttributes().getGUID());
        ValidValue createdValidValue = subjectAreaRelationship.createValidValueRelationship(this.userId, validValue);
        FVTUtils.validateLine(createdValidValue);
        FVTUtils.checkGuidEnd1s("ValidValue", term1.getSystemAttributes().getGUID(), createdValidValue.getTermGuid());
        FVTUtils.checkGuidEnd2s("ValidValue", term2.getSystemAttributes().getGUID(), createdValidValue.getValidValueGuid());

        return createdValidValue;
    }

    private void preferredtermFVT(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, RelationshipNotPurgedException {
        PreferredTerm createdPreferredTerm = createPreferredTerm(term1, term2);
        FVTUtils.validateLine(createdPreferredTerm);
        System.out.println("Created PreferredTerm " + createdPreferredTerm);
        String guid = createdPreferredTerm.getGuid();

        PreferredTerm gotPreferredTerm = subjectAreaRelationship.getPreferredTermRelationship(this.userId, guid);
        FVTUtils.validateLine(gotPreferredTerm);
        System.out.println("Got PreferredTerm " + createdPreferredTerm);

        PreferredTerm updatePreferredTerm = new PreferredTerm();
        updatePreferredTerm.setDescription("ddd2");
        updatePreferredTerm.setGuid(createdPreferredTerm.getGuid());
        PreferredTerm updatedPreferredTerm = subjectAreaRelationship.updatePreferredTermRelationship(this.userId, updatePreferredTerm);
        FVTUtils.validateLine(updatedPreferredTerm);
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
        if (!updatedPreferredTerm.getAlternateTermGuid().equals(createdPreferredTerm.getAlternateTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: preferredTerm update end 1 not as expected");
        }
        if (!updatedPreferredTerm.getPreferredTermGuid().equals(createdPreferredTerm.getPreferredTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: preferredTerm update end 2 not as expected");
        }
        System.out.println("Updated PreferredTerm " + createdPreferredTerm);
        PreferredTerm replacePreferredTerm = new PreferredTerm();
        replacePreferredTerm.setDescription("ddd3");
        replacePreferredTerm.setGuid(createdPreferredTerm.getGuid());
        PreferredTerm replacedPreferredTerm = subjectAreaRelationship.replacePreferredTermRelationship(this.userId, replacePreferredTerm);
        FVTUtils.validateLine(replacedPreferredTerm);
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
        if (!replacedPreferredTerm.getAlternateTermGuid().equals(createdPreferredTerm.getAlternateTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: preferredTerm replace end 1 not as expected");
        }
        if (!replacedPreferredTerm.getPreferredTermGuid().equals(createdPreferredTerm.getPreferredTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: preferredTerm replace end 2 not as expected");
        }
        System.out.println("Replaced PreferredTerm " + createdPreferredTerm);
        gotPreferredTerm = subjectAreaRelationship.deletePreferredTermRelationship(this.userId, guid);
        FVTUtils.validateLine(gotPreferredTerm);
        System.out.println("Soft deleted PreferredTerm with userId=" + guid);
        gotPreferredTerm = subjectAreaRelationship.restorePreferredTermRelationship(this.userId, guid);
        FVTUtils.validateLine(gotPreferredTerm);
        System.out.println("restored PreferredTerm with userId=" + guid);
        gotPreferredTerm = subjectAreaRelationship.deletePreferredTermRelationship(this.userId, guid);
        FVTUtils.validateLine(gotPreferredTerm);
        System.out.println("Soft deleted PreferredTerm with userId=" + guid);
        subjectAreaRelationship.purgePreferredTermRelationship(this.userId, guid);
        System.out.println("Hard deleted PreferredTerm with userId=" + guid);
    }

    private PreferredTerm createPreferredTerm(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException {
        PreferredTerm preferredTerm = new PreferredTerm();
        preferredTerm.setDescription("ddd");
        preferredTerm.setExpression("Ex");
        preferredTerm.setSource("source");
        preferredTerm.setSteward("Stew");
        preferredTerm.setAlternateTermGuid(term1.getSystemAttributes().getGUID());
        preferredTerm.setPreferredTermGuid(term2.getSystemAttributes().getGUID());
        PreferredTerm createdPreferredTerm = subjectAreaRelationship.createPreferredTermRelationship(this.userId, preferredTerm);
        FVTUtils.validateLine(createdPreferredTerm);
        FVTUtils.checkGuidEnd1s("PreferredTerm", term1.getSystemAttributes().getGUID(), createdPreferredTerm.getAlternateTermGuid());
        FVTUtils.checkGuidEnd2s("PreferredTerm", term2.getSystemAttributes().getGUID(), createdPreferredTerm.getPreferredTermGuid());

        return createdPreferredTerm;
    }

    private void usedincontextFVT(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, RelationshipNotPurgedException {
        UsedInContext createdUsedInContext = createUsedInContext(term1, term2);
        FVTUtils.validateLine(createdUsedInContext);
        System.out.println("Created UsedInContext " + createdUsedInContext);
        String guid = createdUsedInContext.getGuid();

        UsedInContext gotUsedInContext = subjectAreaRelationship.getUsedInContextRelationship(this.userId, guid);
        FVTUtils.validateLine(gotUsedInContext);
        System.out.println("Got UsedInContext " + createdUsedInContext);

        UsedInContext updateUsedInContext = new UsedInContext();
        updateUsedInContext.setDescription("ddd2");
        updateUsedInContext.setGuid(createdUsedInContext.getGuid());
        UsedInContext updatedUsedInContext = subjectAreaRelationship.updateUsedInContextRelationship(this.userId, updateUsedInContext);
        FVTUtils.validateLine(updatedUsedInContext);
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
        if (!updatedUsedInContext.getContextGuid().equals(createdUsedInContext.getContextGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: usedInContext update end 1 not as expected");
        }
        if (!updatedUsedInContext.getTermInContextGuid().equals(createdUsedInContext.getTermInContextGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: usedInContext update end 2 not as expected");
        }
        System.out.println("Updated UsedInContext " + createdUsedInContext);
        UsedInContext replaceUsedInContext = new UsedInContext();
        replaceUsedInContext.setDescription("ddd3");
        replaceUsedInContext.setGuid(createdUsedInContext.getGuid());
        UsedInContext replacedUsedInContext = subjectAreaRelationship.replaceUsedInContextRelationship(this.userId, replaceUsedInContext);
        FVTUtils.validateLine(replacedUsedInContext);
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
        if (!replacedUsedInContext.getContextGuid().equals(createdUsedInContext.getContextGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: usedInContext replace end 1 not as expected");
        }
        if (!replacedUsedInContext.getTermInContextGuid().equals(createdUsedInContext.getTermInContextGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: usedInContext replace end 2 not as expected");
        }
        System.out.println("Replaced UsedInContext " + createdUsedInContext);
        gotUsedInContext = subjectAreaRelationship.deleteUsedInContextRelationship(this.userId, guid);
        FVTUtils.validateLine(gotUsedInContext);
        System.out.println("Soft deleted UsedInContext with userId=" + guid);
        gotUsedInContext = subjectAreaRelationship.restoreUsedInContextRelationship(this.userId, guid);
        FVTUtils.validateLine(gotUsedInContext);
        System.out.println("Restored UsedInContext with userId=" + guid);
        gotUsedInContext = subjectAreaRelationship.deleteUsedInContextRelationship(this.userId, guid);
        FVTUtils.validateLine(gotUsedInContext);
        System.out.println("Soft deleted UsedInContext with userId=" + guid);
        subjectAreaRelationship.purgeUsedInContextRelationship(this.userId, guid);
        System.out.println("Hard deleted UsedInContext with userId=" + guid);
    }

    private UsedInContext createUsedInContext(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException {
        UsedInContext usedInContext = new UsedInContext();
        usedInContext.setDescription("ddd");
        usedInContext.setExpression("Ex");
        usedInContext.setSource("source");
        usedInContext.setSteward("Stew");
        usedInContext.setContextGuid(term1.getSystemAttributes().getGUID());
        usedInContext.setTermInContextGuid(term2.getSystemAttributes().getGUID());
        UsedInContext createdUsedInContext = subjectAreaRelationship.createUsedInContextRelationship(this.userId, usedInContext);
        FVTUtils.validateLine(createdUsedInContext);
        FVTUtils.checkGuidEnd1s("UsedInContext", term1.getSystemAttributes().getGUID(), createdUsedInContext.getContextGuid());
        FVTUtils.checkGuidEnd2s("UsedInContext", term2.getSystemAttributes().getGUID(), createdUsedInContext.getTermInContextGuid());

        return createdUsedInContext;
    }

    private void translationFVT(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, RelationshipNotPurgedException {
        Translation createdTranslation = createTranslation(term1, term2);
        FVTUtils.validateLine(createdTranslation);
        System.out.println("Created Translation " + createdTranslation);
        String guid = createdTranslation.getGuid();

        Translation gotTranslation = subjectAreaRelationship.getTranslationRelationship(this.userId, guid);
        FVTUtils.validateLine(gotTranslation);
        System.out.println("Got Translation " + createdTranslation);

        Translation updateTranslation = new Translation();
        updateTranslation.setDescription("ddd2");
        updateTranslation.setGuid(createdTranslation.getGuid());
        Translation updatedTranslation = subjectAreaRelationship.updateTranslationRelationship(this.userId, updateTranslation);
        FVTUtils.validateLine(updatedTranslation);
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
        if (!updatedTranslation.getTranslation1Guid().equals(createdTranslation.getTranslation1Guid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: translation update end 1 not as expected");
        }
        if (!updatedTranslation.getTranslation2Guid().equals(createdTranslation.getTranslation2Guid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: translation update end 2 not as expected");
        }
        System.out.println("Updated Translation " + createdTranslation);
        Translation replaceTranslation = new Translation();
        replaceTranslation.setDescription("ddd3");
        replaceTranslation.setGuid(createdTranslation.getGuid());
        Translation replacedTranslation = subjectAreaRelationship.replaceTranslationRelationship(this.userId, replaceTranslation);
        FVTUtils.validateLine(replacedTranslation);
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
        if (!replacedTranslation.getTranslation1Guid().equals(createdTranslation.getTranslation1Guid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: translation replace end 1 not as expected");
        }
        if (!replacedTranslation.getTranslation2Guid().equals(createdTranslation.getTranslation2Guid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: translation replace end 2 not as expected");
        }
        System.out.println("Replaced Translation " + createdTranslation);
        gotTranslation = subjectAreaRelationship.deleteTranslationRelationship(this.userId, guid);
        FVTUtils.validateLine(gotTranslation);
        System.out.println("Soft deleted Translation with userId=" + guid);
        gotTranslation = subjectAreaRelationship.restoreTranslationRelationship(this.userId, guid);
        FVTUtils.validateLine(gotTranslation);
        System.out.println("Restored Translation with userId=" + guid);
        gotTranslation = subjectAreaRelationship.deleteTranslationRelationship(this.userId, guid);
        FVTUtils.validateLine(gotTranslation);
        System.out.println("Soft deleted Translation with userId=" + guid);
        subjectAreaRelationship.purgeTranslationRelationship(this.userId, guid);
        System.out.println("Hard deleted Translation with userId=" + guid);
    }

    private Translation createTranslation(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException {
        Translation translation = new Translation();
        translation.setDescription("ddd");
        translation.setExpression("Ex");
        translation.setSource("source");
        translation.setSteward("Stew");
        translation.setTranslation1Guid(term1.getSystemAttributes().getGUID());
        translation.setTranslation2Guid(term2.getSystemAttributes().getGUID());
        Translation createdTranslation = subjectAreaRelationship.createTranslationRelationship(this.userId, translation);
        FVTUtils.validateLine(createdTranslation);
        FVTUtils.checkGuidEnd1s("Translation", term1.getSystemAttributes().getGUID(), createdTranslation.getTranslation1Guid());
        FVTUtils.checkGuidEnd2s("Translation", term2.getSystemAttributes().getGUID(), createdTranslation.getTranslation2Guid());

        return createdTranslation;
    }

    private void hasaFVT(Term term1, Term term3) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, RelationshipNotPurgedException {
        Hasa createdHASATerm = createTermHASARelationship(term1, term3);
        FVTUtils.validateLine(createdHASATerm);
        System.out.println("Created Hasa " + createdHASATerm);
        String guid = createdHASATerm.getGuid();

        Hasa gotHASATerm = subjectAreaRelationship.getTermHASARelationship(this.userId, guid);
        FVTUtils.validateLine(gotHASATerm);
        System.out.println("Got Hasa " + createdHASATerm);
        Hasa updateHASATerm = new Hasa();
        updateHASATerm.setDescription("ddd2");
        updateHASATerm.setGuid(createdHASATerm.getGuid());
        Hasa updatedHASATerm = subjectAreaRelationship.updateTermHASARelationship(this.userId, updateHASATerm);
        FVTUtils.validateLine(updatedHASATerm);
        if (!updatedHASATerm.getDescription().equals(updateHASATerm.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: HASARelationship update description not as expected");
        }
        if (!updatedHASATerm.getSource().equals(createdHASATerm.getSource())) {
            throw new SubjectAreaFVTCheckedException("ERROR: HASARelationship update source not as expected");
        }
        if (!updatedHASATerm.getSteward().equals(createdHASATerm.getSteward())) {
            throw new SubjectAreaFVTCheckedException("ERROR: HASARelationship update steward not as expected");
        }
        if (!updatedHASATerm.getOwningTermGuid().equals(createdHASATerm.getOwningTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: HASARelationship update end 1 not as expected");
        }
        if (!updatedHASATerm.getOwnedTermGuid().equals(createdHASATerm.getOwnedTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: HASARelationship update end 2 not as expected");
        }
        System.out.println("Updated HASARelationship " + createdHASATerm);
        Hasa replaceHASATerm = new Hasa();
        replaceHASATerm.setDescription("ddd3");
        replaceHASATerm.setGuid(createdHASATerm.getGuid());
        Hasa replacedHASATerm = subjectAreaRelationship.replaceTermHASARelationship(this.userId, replaceHASATerm);
        FVTUtils.validateLine(replacedHASATerm);
        if (!replacedHASATerm.getDescription().equals(replaceHASATerm.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: HASARelationship replace description not as expected");
        }
        if (replacedHASATerm.getSource() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: HASARelationship replace source not as expected");
        }
        if (replacedHASATerm.getSteward() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: HASARelationship replace steward not as expected");
        }
        if (!replacedHASATerm.getOwningTermGuid().equals(createdHASATerm.getOwningTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: HASARelationship replace end 1 not as expected");
        }
        if (!replacedHASATerm.getOwnedTermGuid().equals(createdHASATerm.getOwnedTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: HASARelationship replace end 2 not as expected");
        }
        System.out.println("Replaced HASARelationship " + createdHASATerm);

        gotHASATerm = subjectAreaRelationship.deleteTermHASARelationship(this.userId, guid);
        FVTUtils.validateLine(gotHASATerm);
        System.out.println("Soft deleted Hasa with userId=" + guid);
        gotHASATerm = subjectAreaRelationship.restoreTermHASARelationship(this.userId, guid);
        FVTUtils.validateLine(gotHASATerm);
        System.out.println("Restored Hasa with userId=" + guid);
        gotHASATerm = subjectAreaRelationship.deleteTermHASARelationship(this.userId, guid);
        FVTUtils.validateLine(gotHASATerm);
        System.out.println("Soft deleted Hasa with userId=" + guid);
        subjectAreaRelationship.purgeTermHASARelationship(this.userId, guid);
        System.out.println("Hard deleted Hasa with userId=" + guid);
    }

    private Hasa createTermHASARelationship(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException {
        Hasa hasaRelationshipASARelationship = new Hasa();
        hasaRelationshipASARelationship.setDescription("ddd");
        hasaRelationshipASARelationship.setSource("source");
        hasaRelationshipASARelationship.setSteward("Stew");
        hasaRelationshipASARelationship.setOwningTermGuid(term1.getSystemAttributes().getGUID());
        hasaRelationshipASARelationship.setOwnedTermGuid(term2.getSystemAttributes().getGUID());
        Hasa createdTermHASARelationship = subjectAreaRelationship.createTermHASARelationship(this.userId, hasaRelationshipASARelationship);
        FVTUtils.validateLine(createdTermHASARelationship);
        FVTUtils.checkGuidEnd1s("Hasa", term1.getSystemAttributes().getGUID(), createdTermHASARelationship.getOwningTermGuid());
        FVTUtils.checkGuidEnd2s("Hasa", term2.getSystemAttributes().getGUID(), createdTermHASARelationship.getOwnedTermGuid());

        return createdTermHASARelationship;
    }

    private void relatedtermFVT(Term term1, Term term3) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, RelationshipNotPurgedException {
        RelatedTerm createdRelatedTerm = createRelatedTerm(term1, term3);
        FVTUtils.validateLine(createdRelatedTerm);
        System.out.println("Created RelatedTerm " + createdRelatedTerm);
        String guid = createdRelatedTerm.getGuid();

        RelatedTerm gotRelatedTerm = subjectAreaRelationship.getRelatedTerm(this.userId, guid);
        FVTUtils.validateLine(gotRelatedTerm);
        System.out.println("Got RelatedTerm " + createdRelatedTerm);
        RelatedTerm updateRelatedTerm = new RelatedTerm();
        updateRelatedTerm.setDescription("ddd2");
        updateRelatedTerm.setGuid(createdRelatedTerm.getGuid());
        RelatedTerm updatedRelatedTerm = subjectAreaRelationship.updateRelatedTerm(this.userId, updateRelatedTerm);
        FVTUtils.validateLine(updatedRelatedTerm);
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
        if (!updatedRelatedTerm.getRelatedTerm1Guid().equals(createdRelatedTerm.getRelatedTerm1Guid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: RelatedTerm update end 1 not as expected");
        }
        if (!updatedRelatedTerm.getRelatedTerm2Guid().equals(createdRelatedTerm.getRelatedTerm2Guid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: RelatedTerm update end 2 not as expected");
        }
        System.out.println("Updated RelatedTerm " + createdRelatedTerm);
        RelatedTerm replaceRelatedTerm = new RelatedTerm();
        replaceRelatedTerm.setDescription("ddd3");
        replaceRelatedTerm.setGuid(createdRelatedTerm.getGuid());
        RelatedTerm replacedRelatedTerm = subjectAreaRelationship.replaceRelatedTerm(this.userId, replaceRelatedTerm);
        FVTUtils.validateLine(replacedRelatedTerm);
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
        if (!replacedRelatedTerm.getRelatedTerm1Guid().equals(createdRelatedTerm.getRelatedTerm1Guid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: RelatedTerm replace end 1 not as expected");
        }
        if (!replacedRelatedTerm.getRelatedTerm2Guid().equals(createdRelatedTerm.getRelatedTerm2Guid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: RelatedTerm replace end 2 not as expected");
        }
        System.out.println("Replaced RelatedTerm " + createdRelatedTerm);

        gotRelatedTerm = subjectAreaRelationship.deleteRelatedTerm(this.userId, guid);
        FVTUtils.validateLine(gotRelatedTerm);
        System.out.println("Soft deleted RelatedTerm with userId=" + guid);
        gotRelatedTerm = subjectAreaRelationship.restoreRelatedTermRelationship(this.userId, guid);
        FVTUtils.validateLine(gotRelatedTerm);
        System.out.println("Restored RelatedTerm with userId=" + guid);
        gotRelatedTerm = subjectAreaRelationship.deleteRelatedTerm(this.userId, guid);
        FVTUtils.validateLine(gotRelatedTerm);
        System.out.println("Soft deleted RelatedTerm with userId=" + guid);
        subjectAreaRelationship.purgeRelatedTerm(this.userId, guid);
        System.out.println("Hard deleted RelatedTerm with userId=" + guid);
    }

    private RelatedTerm createRelatedTerm(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException {
        RelatedTerm relatedterm = new RelatedTerm();
        relatedterm.setDescription("ddd");
        relatedterm.setExpression("Ex");
        relatedterm.setSource("source");
        relatedterm.setSteward("Stew");
        relatedterm.setRelatedTerm1Guid(term1.getSystemAttributes().getGUID());
        relatedterm.setRelatedTerm2Guid(term2.getSystemAttributes().getGUID());
        RelatedTerm createdRelatedTerm = subjectAreaRelationship.createRelatedTerm(this.userId, relatedterm);
        FVTUtils.validateLine(createdRelatedTerm);
        FVTUtils.checkGuidEnd1s("RelatedTerm", term1.getSystemAttributes().getGUID(), createdRelatedTerm.getRelatedTerm1Guid());
        FVTUtils.checkGuidEnd2s("RelatedTerm", term2.getSystemAttributes().getGUID(), createdRelatedTerm.getRelatedTerm2Guid());

        return createdRelatedTerm;

    }

    private void antonymFVT(Term term1, Term term3) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, RelationshipNotPurgedException {
        Antonym createdAntonym = createAntonym(term1, term3);
        FVTUtils.validateLine(createdAntonym);
        System.out.println("Created Antonym " + createdAntonym);
        String guid = createdAntonym.getGuid();

        Antonym gotAntonym = subjectAreaRelationship.getAntonymRelationship(this.userId, guid);
        FVTUtils.validateLine(gotAntonym);
        System.out.println("Got Antonym " + createdAntonym);
        Antonym updateAntonym = new Antonym();
        updateAntonym.setDescription("ddd2");
        updateAntonym.setGuid(createdAntonym.getGuid());
        Antonym updatedAntonym = subjectAreaRelationship.updateAntonymRelationship(this.userId, updateAntonym);
        FVTUtils.validateLine(updatedAntonym);
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
        if (!updatedAntonym.getAntonym1Guid().equals(createdAntonym.getAntonym1Guid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: Antonym update end 1 not as expected");
        }
        if (!updatedAntonym.getAntonym2Guid().equals(createdAntonym.getAntonym2Guid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: Antonym update end 2 not as expected");
        }
        System.out.println("Updated Antonym " + createdAntonym);
        Antonym replaceAntonym = new Antonym();
        replaceAntonym.setDescription("ddd3");
        replaceAntonym.setGuid(createdAntonym.getGuid());
        Antonym replacedAntonym = subjectAreaRelationship.replaceAntonymRelationship(this.userId, replaceAntonym);
        FVTUtils.validateLine(replacedAntonym);
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
        if (!replacedAntonym.getAntonym1Guid().equals(createdAntonym.getAntonym1Guid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: Antonym replace end 1 not as expected");
        }
        if (!replacedAntonym.getAntonym2Guid().equals(createdAntonym.getAntonym2Guid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: Antonym replace end 2 not as expected");
        }
        System.out.println("Replaced Antonym " + createdAntonym);


        gotAntonym = subjectAreaRelationship.deleteAntonymRelationship(this.userId, guid);
        FVTUtils.validateLine(gotAntonym);
        System.out.println("Soft deleted Antonym with userId=" + guid);
        gotAntonym = subjectAreaRelationship.restoreAntonymRelationship(this.userId, guid);
        FVTUtils.validateLine(gotAntonym);
        System.out.println("Restored Antonym with userId=" + guid);
        gotAntonym = subjectAreaRelationship.deleteAntonymRelationship(this.userId, guid);
        FVTUtils.validateLine(gotAntonym);
        System.out.println("Soft deleted Antonym with userId=" + guid);
        subjectAreaRelationship.purgeAntonymRelationship(this.userId, guid);
        System.out.println("Hard deleted Antonym with userId=" + guid);
    }

    private Antonym createAntonym(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException {
        Antonym antonym = new Antonym();
        antonym.setDescription("ddd");
        antonym.setExpression("Ex");
        antonym.setSource("source");
        antonym.setSteward("Stew");
        antonym.setAntonym1Guid(term1.getSystemAttributes().getGUID());
        antonym.setAntonym2Guid(term2.getSystemAttributes().getGUID());
        Antonym createdAntonym = subjectAreaRelationship.createAntonymRelationship(this.userId, antonym);
        FVTUtils.validateLine(createdAntonym);
        FVTUtils.checkGuidEnd1s("Antonym", term1.getSystemAttributes().getGUID(), createdAntonym.getAntonym1Guid());
        FVTUtils.checkGuidEnd2s("Antonym", term2.getSystemAttributes().getGUID(), createdAntonym.getAntonym2Guid());

        return createdAntonym;
    }

    private void synonymFVT(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, RelationshipNotPurgedException {
        Synonym createdSynonym = createSynonym(term1, term2);
        FVTUtils.validateLine(createdSynonym);
        System.out.println("Created Synonym " + createdSynonym);
        String guid = createdSynonym.getGuid();

        Synonym gotSynonym = subjectAreaRelationship.getSynonymRelationship(this.userId, guid);
        FVTUtils.validateLine(gotSynonym);
        System.out.println("Got Synonym " + createdSynonym);

        Synonym updateSynonym = new Synonym();
        updateSynonym.setDescription("ddd2");
        updateSynonym.setGuid(createdSynonym.getGuid());
        Synonym updatedSynonym = subjectAreaRelationship.updateSynonymRelationship(this.userId, updateSynonym);
        FVTUtils.validateLine(updatedSynonym);
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
        if (!updatedSynonym.getSynonym1Guid().equals(createdSynonym.getSynonym1Guid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: synonym update end 1 not as expected");
        }
        if (!updatedSynonym.getSynonym2Guid().equals(createdSynonym.getSynonym2Guid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: synonym update end 2 not as expected");
        }
        System.out.println("Updated Synonym " + createdSynonym);
        Synonym replaceSynonym = new Synonym();
        replaceSynonym.setDescription("ddd3");
        replaceSynonym.setGuid(createdSynonym.getGuid());
        Synonym replacedSynonym = subjectAreaRelationship.replaceSynonymRelationship(this.userId, replaceSynonym);
        FVTUtils.validateLine(replacedSynonym);
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
        if (!replacedSynonym.getSynonym1Guid().equals(createdSynonym.getSynonym1Guid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: synonym replace end 1 not as expected");
        }
        if (!replacedSynonym.getSynonym2Guid().equals(createdSynonym.getSynonym2Guid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: synonym replace end 2 not as expected");
        }
        System.out.println("Replaced Synonym " + createdSynonym);
        gotSynonym = subjectAreaRelationship.deleteSynonymRelationship(this.userId, guid);
        FVTUtils.validateLine(gotSynonym);
        System.out.println("Soft deleted Synonym with userId=" + guid);
        gotSynonym = subjectAreaRelationship.restoreSynonymRelationship(this.userId, guid);
        FVTUtils.validateLine(gotSynonym);
        System.out.println("Restored Synonym with userId=" + guid);
        gotSynonym = subjectAreaRelationship.deleteSynonymRelationship(this.userId, guid);
        FVTUtils.validateLine(gotSynonym);
        System.out.println("Soft deleted Synonym with userId=" + guid);
        subjectAreaRelationship.purgeSynonymRelationship(this.userId, guid);

        System.out.println("Hard deleted Synonym with userId=" + guid);
    }

    public Synonym createSynonym(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException {
        Synonym synonym = new Synonym();
        synonym.setDescription("ddd");
        synonym.setExpression("Ex");
        synonym.setSource("source");
        synonym.setSteward("Stew");
        synonym.setSynonym1Guid(term1.getSystemAttributes().getGUID());
        synonym.setSynonym2Guid(term2.getSystemAttributes().getGUID());
        Synonym createdSynonym = subjectAreaRelationship.createSynonymRelationship(this.userId, synonym);
        FVTUtils.validateLine(createdSynonym);
        FVTUtils.checkGuidEnd1s("Synonym", term1.getSystemAttributes().getGUID(), createdSynonym.getSynonym1Guid());
        FVTUtils.checkGuidEnd2s("Synonym", term2.getSystemAttributes().getGUID(), createdSynonym.getSynonym2Guid());

        return createdSynonym;
    }


    public IsaTypeOf createTermISATypeOFRelationship(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException {
        IsaTypeOf termISATypeOFRelationship = new IsaTypeOf();
        termISATypeOFRelationship.setDescription("ddd");
        termISATypeOFRelationship.setSource("source");
        termISATypeOFRelationship.setSteward("Stew");
        termISATypeOFRelationship.setSubTypeGuid(term1.getSystemAttributes().getGUID());
        termISATypeOFRelationship.setSuperTypeGuid(term2.getSystemAttributes().getGUID());
        IsaTypeOf createdTermISATypeOFRelationship = subjectAreaRelationship.createTermISATypeOFRelationship(this.userId, termISATypeOFRelationship);
        FVTUtils.validateLine(createdTermISATypeOFRelationship);
        FVTUtils.checkGuidEnd1s("IsaTypeOf", term1.getSystemAttributes().getGUID(), createdTermISATypeOFRelationship.getSubTypeGuid());
        FVTUtils.checkGuidEnd2s("IsaTypeOf", term2.getSystemAttributes().getGUID(), createdTermISATypeOFRelationship.getSuperTypeGuid());
        System.out.println("Created termISATypeOFRelationship " + createdTermISATypeOFRelationship);
        return createdTermISATypeOFRelationship;
    }

    private void termCategorizationFVT(Term term, Category category) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, RelationshipNotPurgedException {
        Categorization createdTermCategorizationRelationship = createTermCategorization(term, category);
        FVTUtils.validateLine(createdTermCategorizationRelationship);
        System.out.println("Created TermCategorizationRelationship " + createdTermCategorizationRelationship);
        String guid = createdTermCategorizationRelationship.getGuid();

        Categorization gotTermCategorizationRelationship = subjectAreaRelationship.getTermCategorizationRelationship(this.userId, guid);
        FVTUtils.validateLine(gotTermCategorizationRelationship);
        System.out.println("Got TermCategorizationRelationship " + createdTermCategorizationRelationship);

        Categorization updateTermCategorizationRelationship = new Categorization();
        updateTermCategorizationRelationship.setDescription("ddd2");
        updateTermCategorizationRelationship.setGuid(createdTermCategorizationRelationship.getGuid());
        Categorization updatedTermCategorizationRelationship = subjectAreaRelationship.updateTermCategorizationRelationship(this.userId, updateTermCategorizationRelationship);
        FVTUtils.validateLine(updatedTermCategorizationRelationship);

        if (!updatedTermCategorizationRelationship.getDescription().equals(updateTermCategorizationRelationship.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: TermCategorization update description not as expected");
        }
        if (updatedTermCategorizationRelationship.getStatus() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: TermCategorization update status not as expected");
        }

        System.out.println("Updated TermCategorizationRelationship " + createdTermCategorizationRelationship);
        Categorization replaceTermCategorizationRelationship = new Categorization();
        replaceTermCategorizationRelationship.setDescription("ddd3");
        replaceTermCategorizationRelationship.setGuid(createdTermCategorizationRelationship.getGuid());
        Categorization replacedTermCategorizationRelationship = subjectAreaRelationship.replaceTermCategorizationRelationship(this.userId, replaceTermCategorizationRelationship);
        FVTUtils.validateLine(replacedTermCategorizationRelationship);
        if (!replacedTermCategorizationRelationship.getDescription().equals(replaceTermCategorizationRelationship.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: TermCategorization replace description not as expected");
        }
        if (replacedTermCategorizationRelationship.getStatus() != null) {
            throw new SubjectAreaFVTCheckedException("ERROR: TermCategorization replace source not as expected");
        }

        if (!replacedTermCategorizationRelationship.getTermGuid().equals(createdTermCategorizationRelationship.getTermGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: TermCategorization replace end 2 not as expected");
        }
        System.out.println("Replaced TermCategorizationRelationship " + createdTermCategorizationRelationship);
        gotTermCategorizationRelationship = subjectAreaRelationship.deleteTermCategorizationRelationship(this.userId, guid);
        FVTUtils.validateLine(gotTermCategorizationRelationship);
        System.out.println("Soft deleted TermCategorizationRelationship with userId=" + guid);
        gotTermCategorizationRelationship = subjectAreaRelationship.restoreTermCategorizationRelationship(this.userId, guid);
        FVTUtils.validateLine(gotTermCategorizationRelationship);
        System.out.println("Restored TermCategorizationRelationship with userId=" + guid);
        gotTermCategorizationRelationship = subjectAreaRelationship.deleteTermCategorizationRelationship(this.userId, guid);
        FVTUtils.validateLine(gotTermCategorizationRelationship);
        System.out.println("Soft deleted TermCategorization with userId=" + guid);
        subjectAreaRelationship.purgeTermCategorizationRelationship(this.userId, guid);
        System.out.println("Hard deleted TermCategorization with userId=" + guid);
    }


    private void termAnchorFVT(Term term) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, RelationshipNotPurgedException, ClassificationException {
        // No create for TermAnchor - because this OMAS cannot create a Term without a glossary
        String termGuid = term.getSystemAttributes().getGUID();
        String glossaryGuid = term.getGlossary().getGuid();
        String relationshipGuid = term.getGlossary().getRelationshipguid();

        TermAnchor gotTermAnchorRelationship = subjectAreaRelationship.getTermAnchorRelationship(this.userId, relationshipGuid);
        FVTUtils.validateLine(gotTermAnchorRelationship);
        System.out.println("Got TermAnchorRelationship " + gotTermAnchorRelationship);

        // no update or replace as this relationship has no properties

        gotTermAnchorRelationship = subjectAreaRelationship.deleteTermAnchorRelationship(this.userId, relationshipGuid);
        FVTUtils.validateLine(gotTermAnchorRelationship);
        System.out.println("Soft deleted TermAnchorRelationship with relationshipGuid=" + relationshipGuid);
        gotTermAnchorRelationship = subjectAreaRelationship.restoreTermAnchorRelationship(this.userId, relationshipGuid);
        FVTUtils.validateLine(gotTermAnchorRelationship);
        System.out.println("Restored TermAnchorRelationship with relationshipGuid=" + relationshipGuid);
        gotTermAnchorRelationship = subjectAreaRelationship.deleteTermAnchorRelationship(this.userId, relationshipGuid);
        FVTUtils.validateLine(gotTermAnchorRelationship);
        System.out.println("Soft deleted TermAnchor with relationshipGuid=" + relationshipGuid);
        subjectAreaRelationship.purgeTermAnchorRelationship(this.userId, relationshipGuid);
        System.out.println("Hard deleted TermAnchor with relationshipGuid=" + relationshipGuid);

        TermAnchor newTermAnchorRelationship = new TermAnchor();
        newTermAnchorRelationship.setGlossaryGuid(glossaryGuid);
        newTermAnchorRelationship.setTermGuid(termGuid);
        FVTUtils.validateLine(subjectAreaRelationship.createTermAnchorRelationship(userId, newTermAnchorRelationship));
    }

    private void categoryAnchorFVT(Category category) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, RelationshipNotPurgedException, ClassificationException {
        // No create for CategoryAnchor - because this OMAS cannot create a Category without a glossary
        String categoryGuid = category.getSystemAttributes().getGUID();
        String glossaryGuid = category.getGlossary().getGuid();
        String relationshipGuid = category.getGlossary().getRelationshipguid();
        CategoryAnchor gotCategoryAnchorRelationship = subjectAreaRelationship.getCategoryAnchorRelationship(this.userId, relationshipGuid);
        FVTUtils.validateLine(gotCategoryAnchorRelationship);
        System.out.println("Got CategoryAnchorRelationship " + gotCategoryAnchorRelationship);
        // no update as this relationship has no properties

        gotCategoryAnchorRelationship = subjectAreaRelationship.deleteCategoryAnchorRelationship(this.userId, relationshipGuid);
        FVTUtils.validateLine(gotCategoryAnchorRelationship);
        System.out.println("Soft deleted CategoryAnchorRelationship with relationshipGuid=" + relationshipGuid);
        gotCategoryAnchorRelationship = subjectAreaRelationship.restoreCategoryAnchorRelationship(this.userId, relationshipGuid);
        FVTUtils.validateLine(gotCategoryAnchorRelationship);
        System.out.println("Restored CategoryAnchorRelationship with relationshipGuid=" + relationshipGuid);
        gotCategoryAnchorRelationship = subjectAreaRelationship.deleteCategoryAnchorRelationship(this.userId, relationshipGuid);
        FVTUtils.validateLine(gotCategoryAnchorRelationship);
        System.out.println("Soft deleted CategoryAnchor with relationshipGuid=" + relationshipGuid);
        subjectAreaRelationship.purgeCategoryAnchorRelationship(this.userId, relationshipGuid);
        System.out.println("Hard deleted CategoryAnchor with relationshipGuid=" + relationshipGuid);

        CategoryAnchor newCategoryAnchorRelationship = new CategoryAnchor();
        newCategoryAnchorRelationship.setGlossaryGuid(glossaryGuid);
        newCategoryAnchorRelationship.setCategoryGuid(categoryGuid);
        FVTUtils.validateLine(subjectAreaRelationship.createCategoryAnchorRelationship(userId, newCategoryAnchorRelationship));

    }

    public Categorization createTermCategorization(Term term, Category category) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException {
        Categorization termCategorization = new Categorization();
        termCategorization.setTermGuid(term.getSystemAttributes().getGUID());
        termCategorization.setCategoryGuid(category.getSystemAttributes().getGUID());
        Categorization createdTermCategorization = subjectAreaRelationship.createTermCategorizationRelationship(this.userId, termCategorization);
        FVTUtils.validateLine(createdTermCategorization);
        FVTUtils.checkGuidEnd1s("TermCategorizationRelationship", term.getSystemAttributes().getGUID(), createdTermCategorization.getTermGuid());
        FVTUtils.checkGuidEnd2s("TermCategorizationRelationship", category.getSystemAttributes().getGUID(), createdTermCategorization.getCategoryGuid());
        System.out.println("Created TermCategorizationRelationship " + createdTermCategorization);

        return createdTermCategorization;
    }

    private void projectScopeFVT(Project project, Term term) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, RelationshipNotPurgedException {
        ProjectScope createdProjectScope = createProjectScope(project, term);
        FVTUtils.validateLine(createdProjectScope);
        System.out.println("Created ProjectScopeRelationship " + createdProjectScope);
        String guid = createdProjectScope.getGuid();

        ProjectScope gotProjectScopeRelationship = subjectAreaRelationship.getProjectScopeRelationship(this.userId, guid);
        FVTUtils.validateLine(gotProjectScopeRelationship);
        System.out.println("Got ProjectScopeRelationship " + gotProjectScopeRelationship);

        ProjectScope updateProjectScope = new ProjectScope();
        updateProjectScope.setDescription("ddd2");
        updateProjectScope.setGuid(createdProjectScope.getGuid());
        ProjectScope updatedProjectScope = subjectAreaRelationship.updateProjectScopeRelationship(this.userId, updateProjectScope);
        FVTUtils.validateLine(updatedProjectScope);
        if (!updatedProjectScope.getDescription().equals(updateProjectScope.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: Project scope  update scopeDescription not as expected");
        }

        if (!updatedProjectScope.getProjectGuid().equals(createdProjectScope.getProjectGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: Project scope update project end not as expected");
        }
        if (!updatedProjectScope.getNodeGuid().equals(createdProjectScope.getNodeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: Project scope update node end not as expected");
        }
        System.out.println("Updated ProjectScopeRelationship " + createdProjectScope);
        ProjectScope replaceProjectScope = new ProjectScope();
        replaceProjectScope.setDescription("ddd3");
        replaceProjectScope.setGuid(createdProjectScope.getGuid());
        ProjectScope replacedProjectScope = subjectAreaRelationship.replaceProjectScopeRelationship(this.userId, replaceProjectScope);
        FVTUtils.validateLine(replacedProjectScope);
        if (!replacedProjectScope.getDescription().equals(replaceProjectScope.getDescription())) {
            throw new SubjectAreaFVTCheckedException("ERROR: project scope replace scope description not as expected");
        }
        if (!replacedProjectScope.getProjectGuid().equals(createdProjectScope.getProjectGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: project scope replace project end not as expected");
        }
        if (!replacedProjectScope.getNodeGuid().equals(createdProjectScope.getNodeGuid())) {
            throw new SubjectAreaFVTCheckedException("ERROR: project scope replace node end not as expected");
        }
        System.out.println("Replaced ProjectScopeRelationship " + createdProjectScope);
        gotProjectScopeRelationship = subjectAreaRelationship.deleteProjectScopeRelationship(this.userId, guid);
        FVTUtils.validateLine(gotProjectScopeRelationship);
        System.out.println("Soft deleted ProjectScopeRelationship with userId=" + guid);
        gotProjectScopeRelationship = subjectAreaRelationship.restoreProjectScopeRelationship(this.userId, guid);
        FVTUtils.validateLine(gotProjectScopeRelationship);
        System.out.println("Restored ProjectScopeRelationship with userId=" + guid);
        gotProjectScopeRelationship = subjectAreaRelationship.deleteProjectScopeRelationship(this.userId, guid);
        FVTUtils.validateLine(gotProjectScopeRelationship);
        System.out.println("Soft deleted ProjectScopeRelationship with userId=" + guid);
        subjectAreaRelationship.purgeProjectScopeRelationship(this.userId, guid);


        System.out.println("Hard deleted ProjectScopeRelationship with userId=" + guid);
    }

    private ProjectScope createProjectScope(Project project, Term term) throws SubjectAreaFVTCheckedException, UserNotAuthorizedException, UnexpectedResponseException, InvalidParameterException, UnrecognizedGUIDException, MetadataServerUncontactableException {
        ProjectScope projectScope = new ProjectScope();
        projectScope.setNodeGuid(term.getSystemAttributes().getGUID());
        projectScope.setProjectGuid(project.getSystemAttributes().getGUID());
        ProjectScope createdProjectScope = subjectAreaRelationship.createProjectScopeRelationship(this.userId, projectScope);
        FVTUtils.validateLine(createdProjectScope);
        System.out.println("CreatedProjectScopeRelationship " + createdProjectScope);
        return createdProjectScope;
    }
}
