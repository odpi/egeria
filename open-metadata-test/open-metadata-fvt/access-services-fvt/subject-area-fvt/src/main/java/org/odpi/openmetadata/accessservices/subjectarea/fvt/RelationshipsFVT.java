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
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;

import java.io.IOException;
import java.util.List;

/**
 * FVT resource to call subject area term client API
 */
public class RelationshipsFVT
{
    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Test Glossary for relationships FVT";
    private static final String DEFAULT_TEST_TERM_NAME = "Test term A1";
    private static final String DEFAULT_TEST_TERM_NAME2 = "Test term B1";
    private static final String DEFAULT_TEST_TERM_NAME3 = "Test term C1";
    private static final String DEFAULT_TEST_CAT_NAME1 = "Test cat A1";
    private static final String DEFAULT_TEST_CAT_NAME2 = "Test cat B1";
    private static final String DEFAULT_TEST_CAT_NAME3 = "Test cat C1";
    private SubjectAreaRelationship subjectAreaRelationship = null;
    private GlossaryFVT glossaryFVT =null;
    private TermFVT termFVT =null;
    private CategoryFVT catFVT = null;
    private String url = null;
    private String serverName = null;

    public RelationshipsFVT(String url,String serverName) throws SubjectAreaCheckedExceptionBase
    {
        this.url=url;
        subjectAreaRelationship = new SubjectAreaImpl(serverName,url).getSubjectAreaRelationship();
        termFVT = new TermFVT(url,serverName);
        catFVT = new CategoryFVT(url,serverName);
        glossaryFVT = new GlossaryFVT(url,serverName);
        this.serverName=serverName;
    }

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
    public static void runit(String url) throws SubjectAreaCheckedExceptionBase
    {
        RelationshipsFVT fvt =new  RelationshipsFVT(url,FVTConstants.SERVER_NAME1);
        fvt.run();
        RelationshipsFVT fvt2 =new RelationshipsFVT(url,FVTConstants.SERVER_NAME2);
        fvt2.run();
    }
    public void run() throws SubjectAreaCheckedExceptionBase
    {
        System.out.println("Create a glossary");

        int term1relationshipcount = 0;
        int term2relationshipcount = 0;
        int term3relationshipcount = 0;
        int glossaryRelationshipCount = 0;
        int cat1RelationshipCount = 0;
        int cat2RelationshipCount = 0;

        Glossary glossary = glossaryFVT.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
        System.out.println("Create a term called " + DEFAULT_TEST_TERM_NAME+ " using glossary guid");
        String glossaryGuid = glossary.getSystemAttributes().getGUID();
        Term term1 = termFVT.createTerm(DEFAULT_TEST_TERM_NAME, glossaryGuid);
        term1relationshipcount++;
        glossaryRelationshipCount++;
        checkRelationshipNumberforGlossary(glossaryRelationshipCount, glossary);
        checkRelationshipNumberforTerm(term1relationshipcount, term1);
        FVTUtils.validateNode(term1);
        System.out.println("Create a term called " + DEFAULT_TEST_TERM_NAME2+ " using glossary guid");
        Term term2 = termFVT.createTerm(DEFAULT_TEST_TERM_NAME2, glossaryGuid);
        term2relationshipcount++;
        glossaryRelationshipCount++;
        checkRelationshipNumberforGlossary(glossaryRelationshipCount, glossary);
        checkRelationshipNumberforTerm(term2relationshipcount, term2);
        FVTUtils.validateNode(term2);
        System.out.println("Create a term called " + DEFAULT_TEST_TERM_NAME3+ " using glossary guid");
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
        checkRelationshipNumberforCategory(cat1RelationshipCount,cat1);
        Category cat2 = catFVT.createCategory(DEFAULT_TEST_CAT_NAME2, glossaryGuid);
        glossaryRelationshipCount++;
        checkRelationshipNumberforGlossary(glossaryRelationshipCount, glossary);
        cat2RelationshipCount++;
        checkRelationshipNumberforCategory(cat2RelationshipCount,cat1);
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
        termCategorizationFVT(term1,cat1);
        createSomeTermRelationships(term1, term2, term3);
        term1relationshipcount = term1relationshipcount + 12;
        term2relationshipcount = term2relationshipcount + 11;
        term3relationshipcount = term3relationshipcount + 1;
        checkRelationshipNumberforTerm(term1relationshipcount, term1);
        checkRelationshipNumberforTerm(term2relationshipcount, term2);
        checkRelationshipNumberforTerm(term3relationshipcount, term3);
        FVTUtils.validateLine(createTermCategorization(term1,cat1));
        term1relationshipcount++;
        cat1RelationshipCount++;
        checkRelationshipNumberforCategory(cat1RelationshipCount,cat1);
        FVTUtils.validateLine(createTermCategorization(term1,cat2));
        term1relationshipcount++;
        cat2RelationshipCount++;
        checkRelationshipNumberforCategory(cat2RelationshipCount,cat1);
        FVTUtils.validateLine(createTermCategorization(term2,cat1));
        cat1RelationshipCount++;
        checkRelationshipNumberforCategory(cat1RelationshipCount,cat1);
        term2relationshipcount++;
        FVTUtils.validateLine(createTermCategorization(term3,cat1));
        term3relationshipcount++;
        cat1RelationshipCount++;
        checkRelationshipNumberforCategory(cat1RelationshipCount,cat1);
        checkRelationshipNumberforTerm(term1relationshipcount, term1);
        checkRelationshipNumberforTerm(term2relationshipcount, term2);
        checkRelationshipNumberforTerm(term3relationshipcount, term3);
        System.out.println("get term relationships");
        List<Line> term1Relationships = termFVT.getTermRelationships(term1);

        System.out.println("Get paged term relationships");
        int offset = 0;

        int numberofrelationships =0;
        while (offset<term1relationshipcount) {
            System.out.println("Get paged term relationships offset = " + offset + ",pageSize=3");
            List<Line> term1PagedRelationships = termFVT.getTermRelationships(term1, null, offset, 3, SequencingOrder.GUID, null);
            numberofrelationships = numberofrelationships + term1PagedRelationships.size();
            offset+=3;
        }
        if (term1relationshipcount !=numberofrelationships) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "Expected " + term1Relationships.size() + " got "+ numberofrelationships, "", "");
        }
    }

    private void checkRelationshipNumberforTerm(int expectedrelationshipcount, Term term) throws UserNotAuthorizedException, UnexpectedResponseException, InvalidParameterException, FunctionNotSupportedException, MetadataServerUncontactableException, SubjectAreaFVTCheckedException {
        int actualCount = termFVT.getTermRelationships(term).size();
        if (expectedrelationshipcount != actualCount) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: expected " +expectedrelationshipcount+ " for " +term.getName() + " got "  +actualCount, "", "");
        }
    }
    private void checkRelationshipNumberforGlossary(int expectedrelationshipcount, Glossary glossary) throws UserNotAuthorizedException, UnexpectedResponseException, InvalidParameterException, FunctionNotSupportedException, MetadataServerUncontactableException, SubjectAreaFVTCheckedException {
        int actualCount = glossaryFVT.getGlossaryRelationships(glossary).size();
        if (expectedrelationshipcount != actualCount) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: expected " +expectedrelationshipcount+ " for " +glossary.getName() + " got "  +actualCount, "", "");
        }
    }
    private void checkRelationshipNumberforCategory(int expectedrelationshipcount, Category category) throws UserNotAuthorizedException, UnexpectedResponseException, InvalidParameterException, FunctionNotSupportedException, MetadataServerUncontactableException, SubjectAreaFVTCheckedException {
        int actualCount = catFVT.getCategoryRelationships(category).size();
        if (expectedrelationshipcount != actualCount) {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: expected " +expectedrelationshipcount+ " for " +category.getName() + " got "  +actualCount, "", "");
        }
    }

    private void createSomeTermRelationships(Term term1, Term term2, Term term3) throws SubjectAreaFVTCheckedException, InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        FVTUtils.validateLine(createValidValue(term1, term2));
        FVTUtils.validateLine(createAntonym(term1,term2));
        FVTUtils.validateLine(createIsaRelationship(term1,term2));
        FVTUtils.validateLine(createPreferredTerm(term1,term2));
        FVTUtils.validateLine(createRelatedTerm(term1,term2));
        FVTUtils.validateLine(createTermHASARelationship(term1,term2));
        FVTUtils.validateLine(createSynonym(term1,term3));
        FVTUtils.validateLine(createReplacementTerm(term1,term2));
        FVTUtils.validateLine(createTermTYPEDBYRelationship(term1,term2));
        FVTUtils.validateLine(createTranslation(term1,term2));
        FVTUtils.validateLine(createUsedInContext(term1,term2));
        FVTUtils.validateLine(createTermISATypeOFRelationship(term1,term2));
    }

    private void isatypeofFVT(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, GUIDNotPurgedException {
        TermISATypeOFRelationship createdTermISATypeOFRelationship = createTermISATypeOFRelationship(term1, term2);
        String guid = createdTermISATypeOFRelationship.getGuid();

        TermISATypeOFRelationship gotTermISATypeOFRelationship=subjectAreaRelationship.getTermISATypeOFRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotTermISATypeOFRelationship);
        System.out.println("Got TermISATypeOFRelationship " + createdTermISATypeOFRelationship);

        TermISATypeOFRelationship updateTermISATypeOFRelationship = new TermISATypeOFRelationship();
        updateTermISATypeOFRelationship.setDescription("ddd2");
        updateTermISATypeOFRelationship.setGuid(createdTermISATypeOFRelationship.getGuid());
        TermISATypeOFRelationship updatedTermISATypeOFRelationship = subjectAreaRelationship.updateTermISATypeOFRelationship(this.serverName,FVTConstants.USERID, updateTermISATypeOFRelationship);
        FVTUtils.validateLine(updatedTermISATypeOFRelationship);
        if (!updatedTermISATypeOFRelationship.getDescription().equals(updateTermISATypeOFRelationship.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship update description not as expected", "", "");
        }
        if (!updatedTermISATypeOFRelationship.getSource().equals(createdTermISATypeOFRelationship.getSource()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship update source not as expected", "", "");
        }
        if (!updatedTermISATypeOFRelationship.getSteward().equals(createdTermISATypeOFRelationship.getSteward()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship update steward not as expected", "", "");
        }
        if (!updatedTermISATypeOFRelationship.getSubTypeGuid().equals(createdTermISATypeOFRelationship.getSubTypeGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship update end 1 not as expected", "", "");
        }
        if (!updatedTermISATypeOFRelationship.getSuperTypeGuid().equals(createdTermISATypeOFRelationship.getSuperTypeGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship update end 2 not as expected", "", "");
        }
        System.out.println("Updated TermISATypeOFRelationship " + createdTermISATypeOFRelationship);
        TermISATypeOFRelationship replaceTermISATypeOFRelationship = new TermISATypeOFRelationship();
        replaceTermISATypeOFRelationship.setDescription("ddd3");
        replaceTermISATypeOFRelationship.setGuid(createdTermISATypeOFRelationship.getGuid());
        TermISATypeOFRelationship replacedTermISATypeOFRelationship = subjectAreaRelationship.replaceTermISATypeOFRelationship(this.serverName,FVTConstants.USERID, replaceTermISATypeOFRelationship);
        FVTUtils.validateLine(replacedTermISATypeOFRelationship);
        if (!replacedTermISATypeOFRelationship.getDescription().equals(replaceTermISATypeOFRelationship.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship replace description not as expected", "", "");
        }
        if (replacedTermISATypeOFRelationship.getSource() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship replace source not as expected", "", "");
        }
        if (replacedTermISATypeOFRelationship.getSteward() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship replace steward not as expected", "", "");
        }
        if (!replacedTermISATypeOFRelationship.getSuperTypeGuid().equals(createdTermISATypeOFRelationship.getSuperTypeGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship replace end 1 not as expected", "", "");
        }
        if (!replacedTermISATypeOFRelationship.getSubTypeGuid().equals(createdTermISATypeOFRelationship.getSubTypeGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermISATypeOFRelationship replace end 2 not as expected", "", "");
        }
        System.out.println("Replaced TermISATypeOFRelationship " + createdTermISATypeOFRelationship);
        gotTermISATypeOFRelationship=subjectAreaRelationship.deleteTermISATypeOFRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotTermISATypeOFRelationship);
        System.out.println("Soft deleted TermISATypeOFRelationship with guid=" + guid);
        gotTermISATypeOFRelationship=subjectAreaRelationship.restoreIsaTypeOfRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotTermISATypeOFRelationship);
        System.out.println("Restored TermISATypeOFRelationship with guid=" + guid);
        gotTermISATypeOFRelationship=subjectAreaRelationship.deleteTermISATypeOFRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotTermISATypeOFRelationship);
        System.out.println("Soft deleted TermISATypeOFRelationship with guid=" + guid);
        subjectAreaRelationship.purgeTermISATypeOFRelationship(this.serverName,FVTConstants.USERID, guid);
        System.out.println("Hard deleted TermISATypeOFRelationship with guid=" + guid);
    }

    private void isaFVT(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, GUIDNotPurgedException {
        ISARelationship createdIsa = createIsaRelationship(term1, term2);
        FVTUtils.validateLine(createdIsa);
        System.out.println("Created Isa " + createdIsa);
        String guid = createdIsa.getGuid();

        ISARelationship gotIsa = subjectAreaRelationship.getIsaRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotIsa);
        System.out.println("Got Isa " + createdIsa);

        ISARelationship updateIsa = new ISARelationship();
        updateIsa.setDescription("ddd2");
        updateIsa.setGuid(createdIsa.getGuid());
        ISARelationship updatedIsa = subjectAreaRelationship.updateIsaRelationship(this.serverName,FVTConstants.USERID, updateIsa);
        if (!updatedIsa.getDescription().equals(updateIsa.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa update description not as expected", "", "");
        }
        if (!updatedIsa.getSource().equals(createdIsa.getSource()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa update source not as expected", "", "");
        }
        if (!updatedIsa.getExpression().equals(createdIsa.getExpression()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa update expression not as expected", "", "");
        }
        if (!updatedIsa.getSteward().equals(createdIsa.getSteward()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa update steward not as expected", "", "");
        }
        if (!updatedIsa.getTermGuid().equals(createdIsa.getTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa update end 1 not as expected", "", "");
        }
        if (!updatedIsa.getSpecialisedTermGuid().equals(createdIsa.getSpecialisedTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa update end 2 not as expected", "", "");
        }
        System.out.println("Updated Isa " + createdIsa);
        ISARelationship replaceIsa = new ISARelationship();
        replaceIsa.setDescription("ddd3");
        replaceIsa.setGuid(createdIsa.getGuid());
        ISARelationship replacedIsa = subjectAreaRelationship.replaceIsaRelationship(this.serverName,FVTConstants.USERID, replaceIsa);
        FVTUtils.validateLine(replacedIsa);
        if (!replacedIsa.getDescription().equals(replaceIsa.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa replace description not as expected", "", "");
        }
        if (replacedIsa.getSource() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa replace source not as expected", "", "");
        }
        if (replacedIsa.getExpression() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa replace expression not as expected", "", "");
        }
        if (replacedIsa.getSteward() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa replace steward not as expected", "", "");
        }
        if (!replacedIsa.getSpecialisedTermGuid().equals(createdIsa.getSpecialisedTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa replace end 1 not as expected", "", "");
        }
        if (!replacedIsa.getTermGuid().equals(createdIsa.getTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: isa replace end 2 not as expected", "", "");
        }
        System.out.println("Replaced Isa " + createdIsa);
        gotIsa = subjectAreaRelationship.deleteIsaRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotIsa);
        System.out.println("Soft deleted Isa with guid=" + guid);
        gotIsa =  subjectAreaRelationship.restoreIsaRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotIsa);
        System.out.println("Restored Isa with guid=" + guid);
        gotIsa = subjectAreaRelationship.deleteIsaRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotIsa);
        System.out.println("Soft deleted Isa with guid=" + guid);
        subjectAreaRelationship.purgeIsaRelationship(this.serverName,FVTConstants.USERID, guid);
        System.out.println("Hard deleted Isa with guid=" + guid);
    }

    private ISARelationship createIsaRelationship(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        ISARelationship isa = new ISARelationship();
        isa.setDescription("ddd");
        isa.setExpression("Ex");
        isa.setSource("source");
        isa.setSteward("Stew");
        isa.setSpecialisedTermGuid(term1.getSystemAttributes().getGUID());
        isa.setTermGuid(term2.getSystemAttributes().getGUID());
        return subjectAreaRelationship.createIsaRelationship(this.serverName,FVTConstants.USERID, isa);
    }

    private void typedByFVT(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, GUIDNotPurgedException {
        TermTYPEDBYRelationship createdTermTYPEDBYRelationship = createTermTYPEDBYRelationship(term1, term2);
        FVTUtils.validateLine(createdTermTYPEDBYRelationship);
        System.out.println("Created TermTYPEDBYRelationship " + createdTermTYPEDBYRelationship);
        String guid = createdTermTYPEDBYRelationship.getGuid();

        TermTYPEDBYRelationship gotTermTYPEDBYRelationship=subjectAreaRelationship.getTermTYPEDBYRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotTermTYPEDBYRelationship);
        System.out.println("Got TermTYPEDBYRelationship " + createdTermTYPEDBYRelationship);

        TermTYPEDBYRelationship updateTermTYPEDBYRelationship = new TermTYPEDBYRelationship();
        updateTermTYPEDBYRelationship.setDescription("ddd2");
        updateTermTYPEDBYRelationship.setGuid(createdTermTYPEDBYRelationship.getGuid());
        TermTYPEDBYRelationship updatedTermTYPEDBYRelationship = subjectAreaRelationship.updateTermTYPEDBYRelationship(this.serverName,FVTConstants.USERID, updateTermTYPEDBYRelationship);
        FVTUtils.validateLine(updatedTermTYPEDBYRelationship);
        if (!updatedTermTYPEDBYRelationship.getDescription().equals(updateTermTYPEDBYRelationship.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship update description not as expected", "", "");
        }
        if (!updatedTermTYPEDBYRelationship.getSource().equals(createdTermTYPEDBYRelationship.getSource()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship update source not as expected", "", "");
        }
        if (!updatedTermTYPEDBYRelationship.getSteward().equals(createdTermTYPEDBYRelationship.getSteward()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship update steward not as expected", "", "");
        }
        if (!updatedTermTYPEDBYRelationship.getAttributeGuid().equals(createdTermTYPEDBYRelationship.getAttributeGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship update end 1 not as expected", "", "");
        }
        if (!updatedTermTYPEDBYRelationship.getTypeGuid().equals(createdTermTYPEDBYRelationship.getTypeGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship update end 2 not as expected", "", "");
        }
        System.out.println("Updated TermTYPEDBYRelationship " + createdTermTYPEDBYRelationship);
        TermTYPEDBYRelationship replaceTermTYPEDBYRelationship = new TermTYPEDBYRelationship();
        replaceTermTYPEDBYRelationship.setDescription("ddd3");
        replaceTermTYPEDBYRelationship.setGuid(createdTermTYPEDBYRelationship.getGuid());
        TermTYPEDBYRelationship replacedTermTYPEDBYRelationship = subjectAreaRelationship.replaceTermTYPEDBYRelationship(this.serverName,FVTConstants.USERID, replaceTermTYPEDBYRelationship);
        FVTUtils.validateLine(replacedTermTYPEDBYRelationship);
        if (!replacedTermTYPEDBYRelationship.getDescription().equals(replaceTermTYPEDBYRelationship.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship replace description not as expected", "", "");
        }
        if (replacedTermTYPEDBYRelationship.getSource() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship replace source not as expected", "", "");
        }
        if (replacedTermTYPEDBYRelationship.getSteward() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship replace steward not as expected", "", "");
        }
        if (!replacedTermTYPEDBYRelationship.getAttributeGuid().equals(createdTermTYPEDBYRelationship.getAttributeGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship replace end 1 not as expected", "", "");
        }
        if (!replacedTermTYPEDBYRelationship.getTypeGuid().equals(createdTermTYPEDBYRelationship.getTypeGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: termTYPEDBYRelationship replace end 2 not as expected", "", "");
        }
        System.out.println("Replaced TermTYPEDBYRelationship " + createdTermTYPEDBYRelationship);
        gotTermTYPEDBYRelationship=subjectAreaRelationship.deleteTermTYPEDBYRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotTermTYPEDBYRelationship);
        System.out.println("Soft deleted TermTYPEDBYRelationship with guid=" + guid);
        gotTermTYPEDBYRelationship=subjectAreaRelationship.restoreTypedByRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotTermTYPEDBYRelationship);
        System.out.println("Restored TermTYPEDBYRelationship with guid=" + guid);
        gotTermTYPEDBYRelationship=subjectAreaRelationship.deleteTermTYPEDBYRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotTermTYPEDBYRelationship);
        System.out.println("Soft deleted TermTYPEDBYRelationship with guid=" + guid);
        subjectAreaRelationship.purgeTermTYPEDBYRelationship(this.serverName,FVTConstants.USERID, guid);
        System.out.println("Hard deleted TermTYPEDBYRelationship with guid=" + guid);
    }

    private TermTYPEDBYRelationship createTermTYPEDBYRelationship(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        TermTYPEDBYRelationship termTYPEDBYRelationship = new TermTYPEDBYRelationship();
        termTYPEDBYRelationship.setDescription("ddd");
        termTYPEDBYRelationship.setSource("source");
        termTYPEDBYRelationship.setSteward("Stew");
        termTYPEDBYRelationship.setAttributeGuid(term1.getSystemAttributes().getGUID());
        termTYPEDBYRelationship.setTypeGuid(term2.getSystemAttributes().getGUID());
        return subjectAreaRelationship.createTermTYPEDBYRelationship(this.serverName,FVTConstants.USERID, termTYPEDBYRelationship);
    }

    private void replacementTermFVT(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, GUIDNotPurgedException {
        ReplacementTerm createdReplacementTerm = createReplacementTerm(term1, term2);
        FVTUtils.validateLine(createdReplacementTerm);
        System.out.println("Created ReplacementTerm " + createdReplacementTerm);
        String guid = createdReplacementTerm.getGuid();

        ReplacementTerm gotReplacementTerm=subjectAreaRelationship.getReplacementTermRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotReplacementTerm);
        System.out.println("Got ReplacementTerm " + createdReplacementTerm);

        ReplacementTerm updateReplacementTerm = new ReplacementTerm();
        updateReplacementTerm.setDescription("ddd2");
        updateReplacementTerm.setGuid(createdReplacementTerm.getGuid());
        ReplacementTerm updatedReplacementTerm = subjectAreaRelationship.updateReplacementTermRelationship(this.serverName,FVTConstants.USERID, updateReplacementTerm);
        FVTUtils.validateLine(updatedReplacementTerm);
        if (!updatedReplacementTerm.getDescription().equals(updateReplacementTerm.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm update description not as expected", "", "");
        }
        if (!updatedReplacementTerm.getSource().equals(createdReplacementTerm.getSource()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm update source not as expected", "", "");
        }
        if (!updatedReplacementTerm.getExpression().equals(createdReplacementTerm.getExpression()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm update expression not as expected", "", "");
        }
        if (!updatedReplacementTerm.getSteward().equals(createdReplacementTerm.getSteward()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm update steward not as expected", "", "");
        }
        if (!updatedReplacementTerm.getReplacedTermGuid().equals(createdReplacementTerm.getReplacedTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm update end 1 not as expected", "", "");
        }
        if (!updatedReplacementTerm.getReplacementTermGuid().equals(createdReplacementTerm.getReplacementTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm update end 2 not as expected", "", "");
        }
        System.out.println("Updated ReplacementTerm " + createdReplacementTerm);
        ReplacementTerm replaceReplacementTerm = new ReplacementTerm();
        replaceReplacementTerm.setDescription("ddd3");
        replaceReplacementTerm.setGuid(createdReplacementTerm.getGuid());
        ReplacementTerm replacedReplacementTerm = subjectAreaRelationship.replaceReplacementTermRelationship(this.serverName,FVTConstants.USERID, replaceReplacementTerm);
        FVTUtils.validateLine(replacedReplacementTerm);
        if (!replacedReplacementTerm.getDescription().equals(replaceReplacementTerm.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm replace description not as expected", "", "");
        }
        if (replacedReplacementTerm.getSource() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm replace source not as expected", "", "");
        }
        if (replacedReplacementTerm.getExpression() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm replace expression not as expected", "", "");
        }
        if (replacedReplacementTerm.getSteward() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm replace steward not as expected", "", "");
        }
        if (!replacedReplacementTerm.getReplacedTermGuid().equals(createdReplacementTerm.getReplacedTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm replace end 1 not as expected", "", "");
        }
        if (!replacedReplacementTerm.getReplacementTermGuid().equals(createdReplacementTerm.getReplacementTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: replacementTerm replace end 2 not as expected", "", "");
        }
        System.out.println("Replaced ReplacementTerm " + createdReplacementTerm);
        gotReplacementTerm=subjectAreaRelationship.deleteReplacementTermRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotReplacementTerm);
        System.out.println("Soft deleted ReplacementTerm with guid=" + guid);
        gotReplacementTerm=subjectAreaRelationship.restoreReplacementTermRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotReplacementTerm);
        System.out.println("Restored ReplacementTerm with guid=" + guid);
        gotReplacementTerm=subjectAreaRelationship.deleteReplacementTermRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotReplacementTerm);
        System.out.println("Soft deleted ReplacementTerm with guid=" + guid);
        subjectAreaRelationship.purgeReplacementTermRelationship(this.serverName,FVTConstants.USERID, guid);
        System.out.println("Hard deleted ReplacementTerm with guid=" + guid);
    }

    private ReplacementTerm createReplacementTerm(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        ReplacementTerm replacementTerm = new ReplacementTerm();
        replacementTerm.setDescription("ddd");
        replacementTerm.setExpression("Ex");
        replacementTerm.setSource("source");
        replacementTerm.setSteward("Stew");
        replacementTerm.setReplacedTermGuid(term1.getSystemAttributes().getGUID());
        replacementTerm.setReplacementTermGuid(term2.getSystemAttributes().getGUID());
        return subjectAreaRelationship.createReplacementTermRelationship(this.serverName,FVTConstants.USERID, replacementTerm);
    }

    private void validvalueFVT(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, GUIDNotPurgedException {
        ValidValue createdValidValue = createValidValue(term1, term2);
        FVTUtils.validateLine(createdValidValue);
        System.out.println("Created ValidValue " + createdValidValue);
        String guid = createdValidValue.getGuid();

        ValidValue gotValidValue=subjectAreaRelationship.getValidValueRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotValidValue);
        System.out.println("Got ValidValue " + createdValidValue);

        ValidValue updateValidValue = new ValidValue();
        updateValidValue.setDescription("ddd2");
        updateValidValue.setGuid(createdValidValue.getGuid());
        ValidValue updatedValidValue = subjectAreaRelationship.updateValidValueRelationship(this.serverName,FVTConstants.USERID, updateValidValue);
        if (!updatedValidValue.getDescription().equals(updateValidValue.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue update description not as expected", "", "");
        }
        if (!updatedValidValue.getSource().equals(createdValidValue.getSource()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue update source not as expected", "", "");
        }
        if (!updatedValidValue.getExpression().equals(createdValidValue.getExpression()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue update expression not as expected", "", "");
        }
        if (!updatedValidValue.getSteward().equals(createdValidValue.getSteward()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue update steward not as expected", "", "");
        }
        if (!updatedValidValue.getValidValueGuid().equals(createdValidValue.getValidValueGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue update end 1 not as expected", "", "");
        }
        if (!updatedValidValue.getTermGuid().equals(createdValidValue.getTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue update end 2 not as expected", "", "");
        }
        System.out.println("Updated ValidValue " + createdValidValue);
        ValidValue replaceValidValue = new ValidValue();
        replaceValidValue.setDescription("ddd3");
        replaceValidValue.setGuid(createdValidValue.getGuid());
        ValidValue replacedValidValue = subjectAreaRelationship.replaceValidValueRelationship(this.serverName,FVTConstants.USERID, replaceValidValue);
        if (!replacedValidValue.getDescription().equals(replaceValidValue.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue replace description not as expected", "", "");
        }
        if (replacedValidValue.getSource() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue replace source not as expected", "", "");
        }
        if (replacedValidValue.getExpression() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue replace expression not as expected", "", "");
        }
        if (replacedValidValue.getSteward() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue replace steward not as expected", "", "");
        }
        if (!replacedValidValue.getValidValueGuid().equals(createdValidValue.getValidValueGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue replace end 1 not as expected", "", "");
        }
        if (!replacedValidValue.getTermGuid().equals(createdValidValue.getTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: validValue replace end 2 not as expected", "", "");
        }
        System.out.println("Replaced ValidValue " + createdValidValue);
        gotValidValue=subjectAreaRelationship.deleteValidValueRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotValidValue);
        System.out.println("Soft deleted ValidValue with guid=" + guid);
        gotValidValue=subjectAreaRelationship.restoreValidValueRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotValidValue);
        System.out.println("Restored ValidValue with guid=" + guid);
        gotValidValue=subjectAreaRelationship.deleteValidValueRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotValidValue);
        System.out.println("Soft deleted ValidValue with guid=" + guid);
        subjectAreaRelationship.purgeValidValueRelationship(this.serverName,FVTConstants.USERID, guid);
        System.out.println("Hard deleted ValidValue with guid=" + guid);
    }

    private ValidValue createValidValue(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        ValidValue validValue = new ValidValue();
        validValue.setDescription("ddd");
        validValue.setExpression("Ex");
        validValue.setSource("source");
        validValue.setSteward("Stew");
        validValue.setTermGuid(term1.getSystemAttributes().getGUID());
        validValue.setValidValueGuid(term2.getSystemAttributes().getGUID());
        return subjectAreaRelationship.createValidValueRelationship(this.serverName,FVTConstants.USERID, validValue);
    }

    private void preferredtermFVT(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, GUIDNotPurgedException {
        PreferredTerm createdPreferredTerm = createPreferredTerm(term1, term2);
        FVTUtils.validateLine(createdPreferredTerm);
        System.out.println("Created PreferredTerm " + createdPreferredTerm);
        String guid = createdPreferredTerm.getGuid();

        PreferredTerm gotPreferredTerm =subjectAreaRelationship.getPreferredTermRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotPreferredTerm);
        System.out.println("Got PreferredTerm " + createdPreferredTerm);

        PreferredTerm updatePreferredTerm = new PreferredTerm();
        updatePreferredTerm.setDescription("ddd2");
        updatePreferredTerm.setGuid(createdPreferredTerm.getGuid());
        PreferredTerm updatedPreferredTerm = subjectAreaRelationship.updatePreferredTermRelationship(this.serverName,FVTConstants.USERID, updatePreferredTerm);
        FVTUtils.validateLine(updatedPreferredTerm);
        if (!updatedPreferredTerm.getDescription().equals(updatePreferredTerm.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm update description not as expected", "", "");
        }
        if (!updatedPreferredTerm.getSource().equals(createdPreferredTerm.getSource()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm update source not as expected", "", "");
        }
        if (!updatedPreferredTerm.getExpression().equals(createdPreferredTerm.getExpression()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm update expression not as expected", "", "");
        }
        if (!updatedPreferredTerm.getSteward().equals(createdPreferredTerm.getSteward()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm update steward not as expected", "", "");
        }
        if (!updatedPreferredTerm.getAlternateTermGuid().equals(createdPreferredTerm.getAlternateTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm update end 1 not as expected", "", "");
        }
        if (!updatedPreferredTerm.getPreferredTermGuid().equals(createdPreferredTerm.getPreferredTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm update end 2 not as expected", "", "");
        }
        System.out.println("Updated PreferredTerm " + createdPreferredTerm);
        PreferredTerm replacePreferredTerm = new PreferredTerm();
        replacePreferredTerm.setDescription("ddd3");
        replacePreferredTerm.setGuid(createdPreferredTerm.getGuid());
        PreferredTerm replacedPreferredTerm = subjectAreaRelationship.replacePreferredTermRelationship(this.serverName,FVTConstants.USERID, replacePreferredTerm);
        FVTUtils.validateLine(replacedPreferredTerm);
        if (!replacedPreferredTerm.getDescription().equals(replacePreferredTerm.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm replace description not as expected", "", "");
        }
        if (replacedPreferredTerm.getSource() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm replace source not as expected", "", "");
        }
        if (replacedPreferredTerm.getExpression() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm replace expression not as expected", "", "");
        }
        if (replacedPreferredTerm.getSteward() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm replace steward not as expected", "", "");
        }
        if (!replacedPreferredTerm.getAlternateTermGuid().equals(createdPreferredTerm.getAlternateTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm replace end 1 not as expected", "", "");
        }
        if (!replacedPreferredTerm.getPreferredTermGuid().equals(createdPreferredTerm.getPreferredTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: preferredTerm replace end 2 not as expected", "", "");
        }
        System.out.println("Replaced PreferredTerm " + createdPreferredTerm);
        gotPreferredTerm= subjectAreaRelationship.deletePreferredTermRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotPreferredTerm);
        System.out.println("Soft deleted PreferredTerm with guid=" + guid);
        gotPreferredTerm=subjectAreaRelationship.restorePreferredTermRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotPreferredTerm);
        System.out.println("restored PreferredTerm with guid=" + guid);
        gotPreferredTerm=subjectAreaRelationship.deletePreferredTermRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotPreferredTerm);
        System.out.println("Soft deleted PreferredTerm with guid=" + guid);
        subjectAreaRelationship.purgePreferredTermRelationship(this.serverName,FVTConstants.USERID, guid);
        System.out.println("Hard deleted PreferredTerm with guid=" + guid);
    }

    private PreferredTerm createPreferredTerm(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        PreferredTerm preferredTerm = new PreferredTerm();
        preferredTerm.setDescription("ddd");
        preferredTerm.setExpression("Ex");
        preferredTerm.setSource("source");
        preferredTerm.setSteward("Stew");
        preferredTerm.setAlternateTermGuid(term1.getSystemAttributes().getGUID());
        preferredTerm.setPreferredTermGuid(term2.getSystemAttributes().getGUID());
        return subjectAreaRelationship.createPreferredTermRelationship(this.serverName,FVTConstants.USERID, preferredTerm);
    }

    private void usedincontextFVT(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, GUIDNotPurgedException {
        UsedInContext createdUsedInContext = createUsedInContext(term1, term2);
        FVTUtils.validateLine(createdUsedInContext);
        System.out.println("Created UsedInContext " + createdUsedInContext);
        String guid = createdUsedInContext.getGuid();

        UsedInContext gotUsedInContext=subjectAreaRelationship.getUsedInContextRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotUsedInContext);
        System.out.println("Got UsedInContext " + createdUsedInContext);

        UsedInContext updateUsedInContext = new UsedInContext();
        updateUsedInContext.setDescription("ddd2");
        updateUsedInContext.setGuid(createdUsedInContext.getGuid());
        UsedInContext updatedUsedInContext = subjectAreaRelationship.updateUsedInContextRelationship(this.serverName,FVTConstants.USERID, updateUsedInContext);
        FVTUtils.validateLine(updatedUsedInContext);
        if (!updatedUsedInContext.getDescription().equals(updateUsedInContext.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext update description not as expected", "", "");
        }
        if (!updatedUsedInContext.getSource().equals(createdUsedInContext.getSource()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext update source not as expected", "", "");
        }
        if (!updatedUsedInContext.getExpression().equals(createdUsedInContext.getExpression()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext update expression not as expected", "", "");
        }
        if (!updatedUsedInContext.getSteward().equals(createdUsedInContext.getSteward()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext update steward not as expected", "", "");
        }
        if (!updatedUsedInContext.getContextGuid().equals(createdUsedInContext.getContextGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext update end 1 not as expected", "", "");
        }
        if (!updatedUsedInContext.getTermInContextGuid().equals(createdUsedInContext.getTermInContextGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext update end 2 not as expected", "", "");
        }
        System.out.println("Updated UsedInContext " + createdUsedInContext);
        UsedInContext replaceUsedInContext = new UsedInContext();
        replaceUsedInContext.setDescription("ddd3");
        replaceUsedInContext.setGuid(createdUsedInContext.getGuid());
        UsedInContext replacedUsedInContext = subjectAreaRelationship.replaceUsedInContextRelationship(this.serverName,FVTConstants.USERID, replaceUsedInContext);
        FVTUtils.validateLine(replacedUsedInContext);
        if (!replacedUsedInContext.getDescription().equals(replaceUsedInContext.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext replace description not as expected", "", "");
        }
        if (replacedUsedInContext.getSource() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext replace source not as expected", "", "");
        }
        if (replacedUsedInContext.getExpression() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext replace expression not as expected", "", "");
        }
        if (replacedUsedInContext.getSteward() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext replace steward not as expected", "", "");
        }
        if (!replacedUsedInContext.getContextGuid().equals(createdUsedInContext.getContextGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext replace end 1 not as expected", "", "");
        }
        if (!replacedUsedInContext.getTermInContextGuid().equals(createdUsedInContext.getTermInContextGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: usedInContext replace end 2 not as expected", "", "");
        }
        System.out.println("Replaced UsedInContext " + createdUsedInContext);
        gotUsedInContext=subjectAreaRelationship.deleteUsedInContextRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotUsedInContext);
        System.out.println("Soft deleted UsedInContext with guid=" + guid);
        gotUsedInContext=subjectAreaRelationship.restoreUsedInContextRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotUsedInContext);
        System.out.println("Restored UsedInContext with guid=" + guid);
        gotUsedInContext=subjectAreaRelationship.deleteUsedInContextRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotUsedInContext);
        System.out.println("Soft deleted UsedInContext with guid=" + guid);
        subjectAreaRelationship.purgeUsedInContextRelationship(this.serverName,FVTConstants.USERID, guid);
        System.out.println("Hard deleted UsedInContext with guid=" + guid);
    }

    private UsedInContext createUsedInContext(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        UsedInContext usedInContext = new UsedInContext();
        usedInContext.setDescription("ddd");
        usedInContext.setExpression("Ex");
        usedInContext.setSource("source");
        usedInContext.setSteward("Stew");
        usedInContext.setContextGuid(term1.getSystemAttributes().getGUID());
        usedInContext.setTermInContextGuid(term2.getSystemAttributes().getGUID());
        return subjectAreaRelationship.createUsedInContextRelationship(this.serverName,FVTConstants.USERID, usedInContext);
    }

    private void translationFVT(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, GUIDNotPurgedException {
        Translation createdTranslation = createTranslation(term1, term2);
        FVTUtils.validateLine(createdTranslation);
        System.out.println("Created Translation " + createdTranslation);
        String guid = createdTranslation.getGuid();

        Translation gotTranslation =subjectAreaRelationship.getTranslationRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotTranslation);
        System.out.println("Got Translation " + createdTranslation);

        Translation updateTranslation = new Translation();
        updateTranslation.setDescription("ddd2");
        updateTranslation.setGuid(createdTranslation.getGuid());
        Translation updatedTranslation = subjectAreaRelationship.updateTranslationRelationship(this.serverName,FVTConstants.USERID, updateTranslation);
        FVTUtils.validateLine(updatedTranslation);
        if (!updatedTranslation.getDescription().equals(updateTranslation.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation update description not as expected", "", "");
        }
        if (!updatedTranslation.getSource().equals(createdTranslation.getSource()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation update source not as expected", "", "");
        }
        if (!updatedTranslation.getExpression().equals(createdTranslation.getExpression()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation update expression not as expected", "", "");
        }
        if (!updatedTranslation.getSteward().equals(createdTranslation.getSteward()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation update steward not as expected", "", "");
        }
        if (!updatedTranslation.getTranslation1Guid().equals(createdTranslation.getTranslation1Guid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation update end 1 not as expected", "", "");
        }
        if (!updatedTranslation.getTranslation2Guid().equals(createdTranslation.getTranslation2Guid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation update end 2 not as expected", "", "");
        }
        System.out.println("Updated Translation " + createdTranslation);
        Translation replaceTranslation = new Translation();
        replaceTranslation.setDescription("ddd3");
        replaceTranslation.setGuid(createdTranslation.getGuid());
        Translation replacedTranslation = subjectAreaRelationship.replaceTranslationRelationship(this.serverName,FVTConstants.USERID, replaceTranslation);
        FVTUtils.validateLine(replacedTranslation);
        if (!replacedTranslation.getDescription().equals(replaceTranslation.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation replace description not as expected", "", "");
        }
        if (replacedTranslation.getSource() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation replace source not as expected", "", "");
        }
        if (replacedTranslation.getExpression() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation replace expression not as expected", "", "");
        }
        if (replacedTranslation.getSteward() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation replace steward not as expected", "", "");
        }
        if (!replacedTranslation.getTranslation1Guid().equals(createdTranslation.getTranslation1Guid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation replace end 1 not as expected", "", "");
        }
        if (!replacedTranslation.getTranslation2Guid().equals(createdTranslation.getTranslation2Guid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: translation replace end 2 not as expected", "", "");
        }
        System.out.println("Replaced Translation " + createdTranslation);
        gotTranslation=subjectAreaRelationship.deleteTranslationRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotTranslation);
        System.out.println("Soft deleted Translation with guid=" + guid);
        gotTranslation=subjectAreaRelationship.restoreTranslationRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotTranslation);
        System.out.println("Restored Translation with guid=" + guid);
        gotTranslation= subjectAreaRelationship.deleteTranslationRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotTranslation);
        System.out.println("Soft deleted Translation with guid=" + guid);
        subjectAreaRelationship.purgeTranslationRelationship(this.serverName,FVTConstants.USERID, guid);
        System.out.println("Hard deleted Translation with guid=" + guid);
    }

    private Translation createTranslation(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        Translation translation = new Translation();
        translation.setDescription("ddd");
        translation.setExpression("Ex");
        translation.setSource("source");
        translation.setSteward("Stew");
        translation.setTranslation1Guid(term1.getSystemAttributes().getGUID());
        translation.setTranslation2Guid(term2.getSystemAttributes().getGUID());
        return subjectAreaRelationship.createTranslationRelationship(this.serverName,FVTConstants.USERID, translation);
    }

    private void hasaFVT(Term term1, Term term3) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, GUIDNotPurgedException {
        TermHASARelationship createdHASATerm = createTermHASARelationship(term1, term3);
        FVTUtils.validateLine(createdHASATerm);
        System.out.println("Created TermHASARelationship " + createdHASATerm);
        String guid = createdHASATerm.getGuid();

        TermHASARelationship gotHASATerm =subjectAreaRelationship.getTermHASARelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotHASATerm);
        System.out.println("Got TermHASARelationship " + createdHASATerm);
        TermHASARelationship updateHASATerm = new TermHASARelationship();
        updateHASATerm.setDescription("ddd2");
        updateHASATerm.setGuid(createdHASATerm.getGuid());
        TermHASARelationship updatedHASATerm = subjectAreaRelationship.updateTermHASARelationship(this.serverName,FVTConstants.USERID, updateHASATerm);
        FVTUtils.validateLine(updatedHASATerm);
        if (!updatedHASATerm.getDescription().equals(updateHASATerm.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship update description not as expected", "", "");
        }
        if (!updatedHASATerm.getSource().equals(createdHASATerm.getSource()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship update source not as expected", "", "");
        }
        if (!updatedHASATerm.getSteward().equals(createdHASATerm.getSteward()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship update steward not as expected", "", "");
        }
        if (!updatedHASATerm.getOwningTermGuid().equals(createdHASATerm.getOwningTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship update end 1 not as expected", "", "");
        }
        if (!updatedHASATerm.getOwnedTermGuid().equals(createdHASATerm.getOwnedTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship update end 2 not as expected", "", "");
        }
        System.out.println("Updated HASARelationship " + createdHASATerm);
        TermHASARelationship replaceHASATerm = new TermHASARelationship();
        replaceHASATerm.setDescription("ddd3");
        replaceHASATerm.setGuid(createdHASATerm.getGuid());
        TermHASARelationship replacedHASATerm = subjectAreaRelationship.replaceTermHASARelationship(this.serverName,FVTConstants.USERID, replaceHASATerm);
        FVTUtils.validateLine(replacedHASATerm);
        if (!replacedHASATerm.getDescription().equals(replaceHASATerm.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship replace description not as expected", "", "");
        }
        if (replacedHASATerm.getSource() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship replace source not as expected", "", "");
        }
        if (replacedHASATerm.getSteward() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship replace steward not as expected", "", "");
        }
        if (!replacedHASATerm.getOwningTermGuid().equals(createdHASATerm.getOwningTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship replace end 1 not as expected", "", "");
        }
        if (!replacedHASATerm.getOwnedTermGuid().equals(createdHASATerm.getOwnedTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: HASARelationship replace end 2 not as expected", "", "");
        }
        System.out.println("Replaced HASARelationship " + createdHASATerm);

        gotHASATerm=subjectAreaRelationship.deleteTermHASARelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotHASATerm);
        System.out.println("Soft deleted TermHASARelationship with guid=" + guid);
        gotHASATerm=subjectAreaRelationship.restoreTermHASARelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotHASATerm);
        System.out.println("Restored TermHASARelationship with guid=" + guid);
        gotHASATerm=subjectAreaRelationship.deleteTermHASARelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotHASATerm);
        System.out.println("Soft deleted TermHASARelationship with guid=" + guid);
        subjectAreaRelationship.purgeTermHASARelationship(this.serverName,FVTConstants.USERID, guid);
        System.out.println("Hard deleted TermHASARelationship with guid=" + guid);
    }

    private TermHASARelationship createTermHASARelationship(Term term1, Term term3) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        TermHASARelationship hasaRelationshipASARelationship = new TermHASARelationship();
        hasaRelationshipASARelationship.setDescription("ddd");
        hasaRelationshipASARelationship.setSource("source");
        hasaRelationshipASARelationship.setSteward("Stew");
        hasaRelationshipASARelationship.setOwningTermGuid(term1.getSystemAttributes().getGUID());
        hasaRelationshipASARelationship.setOwnedTermGuid(term3.getSystemAttributes().getGUID());
        return subjectAreaRelationship.createTermHASARelationship(this.serverName,FVTConstants.USERID, hasaRelationshipASARelationship);
    }

    private void relatedtermFVT(Term term1, Term term3) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, GUIDNotPurgedException {
        RelatedTerm createdRelatedTerm = createRelatedTerm(term1, term3);
        FVTUtils.validateLine(createdRelatedTerm);
        System.out.println("Created RelatedTerm " + createdRelatedTerm);
        String guid = createdRelatedTerm.getGuid();

        RelatedTerm gotRelatedTerm =subjectAreaRelationship.getRelatedTerm(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotRelatedTerm);
        System.out.println("Got RelatedTerm " + createdRelatedTerm);
        RelatedTerm updateRelatedTerm = new RelatedTerm();
        updateRelatedTerm.setDescription("ddd2");
        updateRelatedTerm.setGuid(createdRelatedTerm.getGuid());
        RelatedTerm updatedRelatedTerm = subjectAreaRelationship.updateRelatedTerm(this.serverName,FVTConstants.USERID, updateRelatedTerm);
        FVTUtils.validateLine(updatedRelatedTerm);
        if (!updatedRelatedTerm.getDescription().equals(updateRelatedTerm.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm update description not as expected", "", "");
        }
        if (!updatedRelatedTerm.getSource().equals(createdRelatedTerm.getSource()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm update source not as expected", "", "");
        }
        if (!updatedRelatedTerm.getExpression().equals(createdRelatedTerm.getExpression()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm update expression not as expected", "", "");
        }
        if (!updatedRelatedTerm.getSteward().equals(createdRelatedTerm.getSteward()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm update steward not as expected", "", "");
        }
        if (!updatedRelatedTerm.getRelatedTerm1Guid().equals(createdRelatedTerm.getRelatedTerm1Guid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm update end 1 not as expected", "", "");
        }
        if (!updatedRelatedTerm.getRelatedTerm2Guid().equals(createdRelatedTerm.getRelatedTerm2Guid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm update end 2 not as expected", "", "");
        }
        System.out.println("Updated RelatedTerm " + createdRelatedTerm);
        RelatedTerm replaceRelatedTerm = new RelatedTerm();
        replaceRelatedTerm.setDescription("ddd3");
        replaceRelatedTerm.setGuid(createdRelatedTerm.getGuid());
        RelatedTerm replacedRelatedTerm = subjectAreaRelationship.replaceRelatedTerm(this.serverName,FVTConstants.USERID, replaceRelatedTerm);
        FVTUtils.validateLine(replacedRelatedTerm);
        if (!replacedRelatedTerm.getDescription().equals(replaceRelatedTerm.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm replace description not as expected", "", "");
        }
        if (replacedRelatedTerm.getSource() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm replace source not as expected", "", "");
        }
        if (replacedRelatedTerm.getExpression() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm replace expression not as expected", "", "");
        }
        if (replacedRelatedTerm.getSteward() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm replace steward not as expected", "", "");
        }
        if (!replacedRelatedTerm.getRelatedTerm1Guid().equals(createdRelatedTerm.getRelatedTerm1Guid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm replace end 1 not as expected", "", "");
        }
        if (!replacedRelatedTerm.getRelatedTerm2Guid().equals(createdRelatedTerm.getRelatedTerm2Guid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: RelatedTerm replace end 2 not as expected", "", "");
        }
        System.out.println("Replaced RelatedTerm " + createdRelatedTerm);

        gotRelatedTerm=subjectAreaRelationship.deleteRelatedTerm(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotRelatedTerm);
        System.out.println("Soft deleted RelatedTerm with guid=" + guid);
        gotRelatedTerm=subjectAreaRelationship.restoreRelatedTermRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotRelatedTerm);
        System.out.println("Restored RelatedTerm with guid=" + guid);
        gotRelatedTerm=subjectAreaRelationship.deleteRelatedTerm(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotRelatedTerm);
        System.out.println("Soft deleted RelatedTerm with guid=" + guid);
        subjectAreaRelationship.purgeRelatedTerm(this.serverName,FVTConstants.USERID, guid);
        System.out.println("Hard deleted RelatedTerm with guid=" + guid);
    }

    private RelatedTerm createRelatedTerm(Term term1, Term term3) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        RelatedTerm relatedterm = new RelatedTerm();
        relatedterm.setDescription("ddd");
        relatedterm.setExpression("Ex");
        relatedterm.setSource("source");
        relatedterm.setSteward("Stew");
        relatedterm.setRelatedTerm1Guid(term1.getSystemAttributes().getGUID());
        relatedterm.setRelatedTerm2Guid(term3.getSystemAttributes().getGUID());
        return subjectAreaRelationship.createRelatedTerm(this.serverName,FVTConstants.USERID, relatedterm);
    }

    private void antonymFVT(Term term1, Term term3) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, GUIDNotPurgedException {
        Antonym createdAntonym = createAntonym(term1, term3);
        FVTUtils.validateLine(createdAntonym);
        System.out.println("Created Antonym " + createdAntonym);
        String guid = createdAntonym.getGuid();

        Antonym gotAntonym =subjectAreaRelationship.getAntonymRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotAntonym);
        System.out.println("Got Antonym " + createdAntonym);
        Antonym updateAntonym = new Antonym();
        updateAntonym.setDescription("ddd2");
        updateAntonym.setGuid(createdAntonym.getGuid());
        Antonym updatedAntonym = subjectAreaRelationship.updateAntonymRelationship(this.serverName,FVTConstants.USERID, updateAntonym);
        FVTUtils.validateLine(updatedAntonym);
        if (!updatedAntonym.getDescription().equals(updateAntonym.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym update description not as expected", "", "");
        }
        if (!updatedAntonym.getSource().equals(createdAntonym.getSource()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym update source not as expected", "", "");
        }
        if (!updatedAntonym.getExpression().equals(createdAntonym.getExpression()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym update expression not as expected", "", "");
        }
        if (!updatedAntonym.getSteward().equals(createdAntonym.getSteward()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym update steward not as expected", "", "");
        }
        if (!updatedAntonym.getAntonym1Guid().equals(createdAntonym.getAntonym1Guid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym update end 1 not as expected", "", "");
        }
        if (!updatedAntonym.getAntonym2Guid().equals(createdAntonym.getAntonym2Guid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym update end 2 not as expected", "", "");
        }
        System.out.println("Updated Antonym " + createdAntonym);
        Antonym replaceAntonym = new Antonym();
        replaceAntonym.setDescription("ddd3");
        replaceAntonym.setGuid(createdAntonym.getGuid());
        Antonym replacedAntonym = subjectAreaRelationship.replaceAntonymRelationship(this.serverName,FVTConstants.USERID, replaceAntonym);
        FVTUtils.validateLine(replacedAntonym);
        if (!replacedAntonym.getDescription().equals(replaceAntonym.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym replace description not as expected", "", "");
        }
        if (replacedAntonym.getSource() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym replace source not as expected", "", "");
        }
        if (replacedAntonym.getExpression() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym replace expression not as expected", "", "");
        }
        if (replacedAntonym.getSteward() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym replace steward not as expected", "", "");
        }
        if (!replacedAntonym.getAntonym1Guid().equals(createdAntonym.getAntonym1Guid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym replace end 1 not as expected", "", "");
        }
        if (!replacedAntonym.getAntonym2Guid().equals(createdAntonym.getAntonym2Guid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: Antonym replace end 2 not as expected", "", "");
        }
        System.out.println("Replaced Antonym " + createdAntonym);


        gotAntonym = subjectAreaRelationship.deleteAntonymRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotAntonym);
        System.out.println("Soft deleted Antonym with guid=" + guid);
        gotAntonym = subjectAreaRelationship.restoreAntonymRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotAntonym);
        System.out.println("Restored Antonym with guid=" + guid);
        gotAntonym = subjectAreaRelationship.deleteAntonymRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotAntonym);
        System.out.println("Soft deleted Antonym with guid=" + guid);
        subjectAreaRelationship.purgeAntonymRelationship(this.serverName,FVTConstants.USERID, guid);
        System.out.println("Hard deleted Antonym with guid=" + guid);
    }

    private Antonym createAntonym(Term term1, Term term3) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        Antonym antonym = new Antonym();
        antonym.setDescription("ddd");
        antonym.setExpression("Ex");
        antonym.setSource("source");
        antonym.setSteward("Stew");
        antonym.setAntonym1Guid(term1.getSystemAttributes().getGUID());
        antonym.setAntonym2Guid(term3.getSystemAttributes().getGUID());
        return subjectAreaRelationship.createAntonymRelationship(this.serverName,FVTConstants.USERID, antonym);
    }

    private void synonymFVT(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, GUIDNotPurgedException {
        Synonym createdSynonym = createSynonym(term1, term2);
        FVTUtils.validateLine(createdSynonym);
        System.out.println("Created Synonym " + createdSynonym);
        String guid = createdSynonym.getGuid();

        Synonym gotSynonym =subjectAreaRelationship.getSynonymRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotSynonym);
        System.out.println("Got Synonym " + createdSynonym);

        Synonym updateSynonym = new Synonym();
        updateSynonym.setDescription("ddd2");
        updateSynonym.setGuid(createdSynonym.getGuid());
        Synonym updatedSynonym = subjectAreaRelationship.updateSynonymRelationship(this.serverName,FVTConstants.USERID, updateSynonym);
        FVTUtils.validateLine(updatedSynonym);
        if (!updatedSynonym.getDescription().equals(updateSynonym.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym update description not as expected", "", "");
        }
        if (!updatedSynonym.getSource().equals(createdSynonym.getSource()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym update source not as expected", "", "");
        }
        if (!updatedSynonym.getExpression().equals(createdSynonym.getExpression()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym update expression not as expected", "", "");
        }
        if (!updatedSynonym.getSteward().equals(createdSynonym.getSteward()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym update steward not as expected", "", "");
        }
        if (!updatedSynonym.getSynonym1Guid().equals(createdSynonym.getSynonym1Guid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym update end 1 not as expected", "", "");
        }
        if (!updatedSynonym.getSynonym2Guid().equals(createdSynonym.getSynonym2Guid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym update end 2 not as expected", "", "");
        }
        System.out.println("Updated Synonym " + createdSynonym);
        Synonym replaceSynonym = new Synonym();
        replaceSynonym.setDescription("ddd3");
        replaceSynonym.setGuid(createdSynonym.getGuid());
        Synonym replacedSynonym = subjectAreaRelationship.replaceSynonymRelationship(this.serverName,FVTConstants.USERID, replaceSynonym);
        FVTUtils.validateLine(replacedSynonym);
        if (!replacedSynonym.getDescription().equals(replaceSynonym.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym replace description not as expected", "", "");
        }
        if (replacedSynonym.getSource() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym replace source not as expected", "", "");
        }
        if (replacedSynonym.getExpression() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym replace expression not as expected", "", "");
        }
        if (replacedSynonym.getSteward() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym replace steward not as expected", "", "");
        }
        if (!replacedSynonym.getSynonym1Guid().equals(createdSynonym.getSynonym1Guid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym replace end 1 not as expected", "", "");
        }
        if (!replacedSynonym.getSynonym2Guid().equals(createdSynonym.getSynonym2Guid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: synonym replace end 2 not as expected", "", "");
        }
        System.out.println("Replaced Synonym " + createdSynonym);
        gotSynonym = subjectAreaRelationship.deleteSynonymRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotSynonym);
        System.out.println("Soft deleted Synonym with guid=" + guid);
        gotSynonym = subjectAreaRelationship.restoreSynonymRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotSynonym);
        System.out.println("Restored Synonym with guid=" + guid);
        gotSynonym = subjectAreaRelationship.deleteSynonymRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotSynonym);
        System.out.println("Soft deleted Synonym with guid=" + guid);
        subjectAreaRelationship.purgeSynonymRelationship(this.serverName,FVTConstants.USERID, guid);

        System.out.println("Hard deleted Synonym with guid=" + guid);
    }

    public Synonym createSynonym(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException {
        Synonym synonym = new Synonym();
        synonym.setDescription("ddd");
        synonym.setExpression("Ex");
        synonym.setSource("source");
        synonym.setSteward("Stew");
        synonym.setSynonym1Guid(term1.getSystemAttributes().getGUID());
        synonym.setSynonym2Guid(term2.getSystemAttributes().getGUID());
        return subjectAreaRelationship.createSynonymRelationship(this.serverName,FVTConstants.USERID, synonym);
    }

    public TermISATypeOFRelationship createTermISATypeOFRelationship(Term term1, Term term2) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException {
        TermISATypeOFRelationship termISATypeOFRelationship = new TermISATypeOFRelationship();
        termISATypeOFRelationship.setDescription("ddd");
        termISATypeOFRelationship.setSource("source");
        termISATypeOFRelationship.setSteward("Stew");
        termISATypeOFRelationship.setSubTypeGuid(term1.getSystemAttributes().getGUID());
        termISATypeOFRelationship.setSuperTypeGuid(term2.getSystemAttributes().getGUID());
        TermISATypeOFRelationship createdTermISATypeOFRelationship = subjectAreaRelationship.createTermISATypeOFRelationship(this.serverName,FVTConstants.USERID, termISATypeOFRelationship);
        FVTUtils.validateLine(createdTermISATypeOFRelationship);
        System.out.println("Created termISATypeOFRelationship " + createdTermISATypeOFRelationship);
        return createdTermISATypeOFRelationship;
    }
    private void termCategorizationFVT(Term term, Category category) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException, FunctionNotSupportedException, RelationshipNotDeletedException, GUIDNotPurgedException {
        TermCategorizationRelationship createdTermCategorizationRelationship = createTermCategorization(term, category);
        FVTUtils.validateLine(createdTermCategorizationRelationship);
        System.out.println("Created TermCategorizationRelationship " + createdTermCategorizationRelationship);
        String guid = createdTermCategorizationRelationship.getGuid();

        TermCategorizationRelationship gotTermCategorizationRelationship =subjectAreaRelationship.getTermCategorizationRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotTermCategorizationRelationship);
        System.out.println("Got TermCategorizationRelationship " + createdTermCategorizationRelationship);

        TermCategorizationRelationship updateTermCategorizationRelationship = new TermCategorizationRelationship();
        updateTermCategorizationRelationship.setDescription("ddd2");
        updateTermCategorizationRelationship.setGuid(createdTermCategorizationRelationship.getGuid());
        TermCategorizationRelationship updatedTermCategorizationRelationship = subjectAreaRelationship.updateTermCategorizationRelationship(this.serverName,FVTConstants.USERID, updateTermCategorizationRelationship);
        FVTUtils.validateLine(updatedTermCategorizationRelationship);
        if (!updatedTermCategorizationRelationship.getDescription().equals(updateTermCategorizationRelationship.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermCategorization update description not as expected", "", "");
        }
        if (updatedTermCategorizationRelationship.getStatus()!=null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermCategorization update status not as expected", "", "");
        }
      
        System.out.println("Updated TermCategorizationRelationship " + createdTermCategorizationRelationship);
        TermCategorizationRelationship replaceTermCategorizationRelationship = new TermCategorizationRelationship();
        replaceTermCategorizationRelationship.setDescription("ddd3");
        replaceTermCategorizationRelationship.setGuid(createdTermCategorizationRelationship.getGuid());
        TermCategorizationRelationship replacedTermCategorizationRelationship = subjectAreaRelationship.replaceTermCategorizationRelationship(this.serverName,FVTConstants.USERID, replaceTermCategorizationRelationship);
        FVTUtils.validateLine(replacedTermCategorizationRelationship);
        if (!replacedTermCategorizationRelationship.getDescription().equals(replaceTermCategorizationRelationship.getDescription()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermCategorization replace description not as expected", "", "");
        }
        if (replacedTermCategorizationRelationship.getStatus() != null)
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermCategorization replace source not as expected", "", "");
        }
      
        if (!replacedTermCategorizationRelationship.getTermGuid().equals(createdTermCategorizationRelationship.getTermGuid()))
        {
            throw new SubjectAreaFVTCheckedException(0, "", "", "ERROR: TermCategorization replace end 2 not as expected", "", "");
        }
        System.out.println("Replaced TermCategorizationRelationship " + createdTermCategorizationRelationship);
        gotTermCategorizationRelationship = subjectAreaRelationship.deleteTermCategorizationRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotTermCategorizationRelationship);
        System.out.println("Soft deleted TermCategorizationRelationship with guid=" + guid);
        gotTermCategorizationRelationship = subjectAreaRelationship.restoreTermCategorizationRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotTermCategorizationRelationship);
        System.out.println("Restored TermCategorizationRelationship with guid=" + guid);
        gotTermCategorizationRelationship = subjectAreaRelationship.deleteTermCategorizationRelationship(this.serverName,FVTConstants.USERID, guid);
        FVTUtils.validateLine(gotTermCategorizationRelationship);
        System.out.println("Soft deleted TermCategorization with guid=" + guid);
        subjectAreaRelationship.purgeTermCategorizationRelationship(this.serverName,FVTConstants.USERID, guid);
        System.out.println("Hard deleted TermCategorization with guid=" + guid);
    }
    public TermCategorizationRelationship createTermCategorization(Term term, Category category) throws InvalidParameterException, UserNotAuthorizedException, MetadataServerUncontactableException, UnexpectedResponseException, UnrecognizedGUIDException, SubjectAreaFVTCheckedException {
        TermCategorizationRelationship termCategorization = new TermCategorizationRelationship();
        termCategorization.setTermGuid(term.getSystemAttributes().getGUID());
        termCategorization.setCategoryGuid(category.getSystemAttributes().getGUID());
        TermCategorizationRelationship createdTermCategorization = subjectAreaRelationship.createTermCategorizationRelationship(this.serverName,FVTConstants.USERID, termCategorization);
        FVTUtils.validateLine(createdTermCategorization);
        System.out.println("Created TermCategorizationRelationship " + createdTermCategorization);
        return createdTermCategorization;
    }
}
