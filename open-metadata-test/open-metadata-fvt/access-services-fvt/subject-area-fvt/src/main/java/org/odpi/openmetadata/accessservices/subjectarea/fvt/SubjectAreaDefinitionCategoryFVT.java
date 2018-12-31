/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaCategory;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;

import java.io.IOException;

/**
 * FVT resource to call subject area subjectArea client API
 */
public class SubjectAreaDefinitionCategoryFVT
{
    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Test Glossary for subject area definition sample";
    private static final String DEFAULT_TEST_CATEGORY_NAME = "Test subject area definition A";
    private static final String DEFAULT_TEST_CATEGORY_NAME_UPDATED = "Test subject area definition A updated";
    private static final String DEFAULT_TEST_CATEGORY_NAME2 = "Test subject area definition B";
    private static final String DEFAULT_TEST_CATEGORY_NAME3 = "Test subject area definition C";
    private static SubjectAreaCategory subjectAreaCategory = null;
    private String url = null;

    public static void main(String args[])
    {
        try {
            String url = RunAllFVT.getUrl(args);
            SubjectAreaDefinitionCategoryFVT subjectAreaDefinitionCategoryFVT =new SubjectAreaDefinitionCategoryFVT(url);
            subjectAreaDefinitionCategoryFVT.run();
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
        SubjectAreaDefinitionCategoryFVT fvt =new SubjectAreaDefinitionCategoryFVT(url);
        fvt.run();
    }
    public SubjectAreaDefinitionCategoryFVT(String url) throws InvalidParameterException
    {
        subjectAreaCategory = new SubjectAreaImpl(FVTConstants.SERVER_NAME1,url).getSubjectAreaCategory();
        this.url=url;
    }

    public void run() throws SubjectAreaCheckedExceptionBase
    {
        System.out.println("Create a glossary");
        GlossaryFVT glossaryFVT = new GlossaryFVT(url,FVTConstants.SERVER_NAME1);
        Glossary glossary = glossaryFVT.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
        FVTUtils.validateNode(glossary);
        System.out.println("Create a subjectArea1  using glossary name");
        SubjectAreaDefinition subjectArea1  = createSubjectAreaDefinitionWithGlossaryName(DEFAULT_TEST_CATEGORY_NAME, DEFAULT_TEST_GLOSSARY_NAME);
        FVTUtils.validateNode(subjectArea1);
        System.out.println("Create a subjectArea2 using glossary guid");
        SubjectAreaDefinition subjectArea2 = createSubjectAreaDefinitionWithGlossaryGuid(DEFAULT_TEST_CATEGORY_NAME2, glossary.getSystemAttributes().getGUID());
        FVTUtils.validateNode(subjectArea2);
        SubjectAreaDefinition subjectAreaForUpdate = new SubjectAreaDefinition();
        subjectAreaForUpdate.setName(DEFAULT_TEST_CATEGORY_NAME_UPDATED);

        if (subjectArea1  != null)
        {
            System.out.println("Get the subjectArea1 ");
            String guid = subjectArea1 .getSystemAttributes().getGUID();
            SubjectAreaDefinition gotSubjectAreaDefinition = getSubjectAreaDefinitionByGUID(guid);
            System.out.println("Update the subjectArea1 ");
            SubjectAreaDefinition updatedSubjectAreaDefinition = updateSubjectAreaDefinition(guid, subjectAreaForUpdate);
            FVTUtils.validateNode(updatedSubjectAreaDefinition);
            System.out.println("Get the subjectArea1  again");
            gotSubjectAreaDefinition = getSubjectAreaDefinitionByGUID(guid);
            FVTUtils.validateNode( gotSubjectAreaDefinition);
            System.out.println("Delete the subjectArea1 ");
            gotSubjectAreaDefinition = deleteSubjectAreaDefinition(guid);
            FVTUtils.validateNode( gotSubjectAreaDefinition);
            System.out.println("restore the subjectArea1 ");
            gotSubjectAreaDefinition = restoreSubjectAreaDefinition(guid);
            FVTUtils.validateNode( gotSubjectAreaDefinition);
            System.out.println("Delete the subjectArea1 ");
            gotSubjectAreaDefinition = deleteSubjectAreaDefinition(guid);
            FVTUtils.validateNode( gotSubjectAreaDefinition);
            System.out.println("Purge a subjectArea1 ");

            // create subjectArea DEFAULT_TEST_CATEGORY_NAME3 with parent
            System.out.println("Create a subjectArea with a parent subjectArea");
            System.out.println("Create a category with a parent category");

            SubjectAreaDefinition  subjectAreaDefinition3 = createSubjectAreaDefinitionWithParentGlossaryGuid( DEFAULT_TEST_CATEGORY_NAME3, subjectArea2.getSystemAttributes().getGUID(), glossary.getSystemAttributes().getGUID());
            FVTUtils.validateNode(subjectAreaDefinition3);

        }
    }
    private SubjectAreaDefinition createSubjectAreaDefinitionWithParentGlossaryGuid(String subjectAreaName, String parentGuid, String glossaryGuid) throws SubjectAreaCheckedExceptionBase
    {
        SubjectAreaDefinition subjectArea = new SubjectAreaDefinition();
        subjectArea.setName(subjectAreaName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        subjectArea.setGlossary(glossarySummary);
        CategorySummary parentCategory = new CategorySummary();
        parentCategory.setGuid(parentGuid);
        subjectArea.setParentCategory(parentCategory);
        SubjectAreaDefinition newSubjectAreaDefinition = subjectAreaCategory.createSubjectAreaDefinition(FVTConstants.SERVER_NAME1,FVTConstants.USERID, subjectArea);


        if (newSubjectAreaDefinition != null)
        {
            System.out.println("Created SubjectAreaDefinition " + newSubjectAreaDefinition.getName() + " with glossaryGuid " + newSubjectAreaDefinition.getSystemAttributes().getGUID());
        }
        return newSubjectAreaDefinition;
    }

    public  SubjectAreaDefinition createSubjectAreaDefinitionWithGlossaryGuid(String subjectAreaName, String glossaryGuid) throws SubjectAreaCheckedExceptionBase
    {
        SubjectAreaDefinition subjectArea = new SubjectAreaDefinition();
        subjectArea.setName(subjectAreaName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        subjectArea.setGlossary(glossarySummary);
        SubjectAreaDefinition newSubjectAreaDefinition = subjectAreaCategory.createSubjectAreaDefinition(FVTConstants.SERVER_NAME1,FVTConstants.USERID, subjectArea);
        if (newSubjectAreaDefinition != null)
        {
            System.out.println("Created SubjectAreaDefinition " + newSubjectAreaDefinition.getName() + " with guid " + newSubjectAreaDefinition.getSystemAttributes().getGUID());
        }
        return newSubjectAreaDefinition;
    }

    public  SubjectAreaDefinition createSubjectAreaDefinitionWithGlossaryName(String subjectAreaName, String glossaryName) throws SubjectAreaCheckedExceptionBase
    {
        SubjectAreaDefinition subjectArea = new SubjectAreaDefinition();
        subjectArea.setName(subjectAreaName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setName(glossaryName);
        subjectArea.setGlossary(glossarySummary);
        SubjectAreaDefinition newSubjectAreaDefinition = subjectAreaCategory.createSubjectAreaDefinition(FVTConstants.SERVER_NAME1,FVTConstants.USERID, subjectArea);
        if (newSubjectAreaDefinition != null)
        {
            System.out.println("Created SubjectAreaDefinition " + newSubjectAreaDefinition.getName() + " with guid " + newSubjectAreaDefinition.getSystemAttributes().getGUID());
        }
        return newSubjectAreaDefinition;
    }

    public  SubjectAreaDefinition getSubjectAreaDefinitionByGUID(String guid) throws SubjectAreaCheckedExceptionBase
    {
        SubjectAreaDefinition subjectArea = subjectAreaCategory.getSubjectAreaDefinitionByGuid(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
        if (subjectArea != null)
        {
            System.out.println("Got SubjectAreaDefinition " + subjectArea.getName() + " with guid " + subjectArea.getSystemAttributes().getGUID() + " and status " + subjectArea.getSystemAttributes().getStatus());
        }
        return subjectArea;
    }

    public SubjectAreaDefinition updateSubjectAreaDefinition(String guid, SubjectAreaDefinition subjectArea) throws SubjectAreaCheckedExceptionBase
    {
        SubjectAreaDefinition updatedSubjectAreaDefinition = subjectAreaCategory.updateSubjectAreaDefinition(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid, subjectArea);
        if (updatedSubjectAreaDefinition != null)
        {
            System.out.println("Updated SubjectAreaDefinition name to " + updatedSubjectAreaDefinition.getName());
        }
        return updatedSubjectAreaDefinition;
    }

    public SubjectAreaDefinition deleteSubjectAreaDefinition(String guid) throws SubjectAreaCheckedExceptionBase
    {
        SubjectAreaDefinition deletedSubjectAreaDefinition = subjectAreaCategory.deleteSubjectAreaDefinition(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
        if (deletedSubjectAreaDefinition != null)
        {
            System.out.println("Deleted SubjectAreaDefinition name is " + deletedSubjectAreaDefinition.getName());
        }
        return deletedSubjectAreaDefinition;
    }

    public void purgeSubjectAreaDefinition(String guid) throws SubjectAreaCheckedExceptionBase
    {
        subjectAreaCategory.purgeSubjectAreaDefinition(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
        System.out.println("Purge succeeded");
    }
    public SubjectAreaDefinition restoreSubjectAreaDefinition(String guid) throws SubjectAreaCheckedExceptionBase
    {
        SubjectAreaDefinition restoredSubjectAreaDefinition = subjectAreaCategory.restoreSubjectAreaDefinition(FVTConstants.SERVER_NAME1,FVTConstants.USERID, guid);
        if (restoredSubjectAreaDefinition != null)
        {
            System.out.println("Deleted SubjectAreaDefinition name is " + restoredSubjectAreaDefinition.getName());
        }
        return restoredSubjectAreaDefinition;
    }
}
