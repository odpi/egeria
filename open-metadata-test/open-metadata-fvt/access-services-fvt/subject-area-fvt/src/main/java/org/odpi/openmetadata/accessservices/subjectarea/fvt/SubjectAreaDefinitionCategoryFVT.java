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
    private static final String USERID = " Fred";
    private static final String DEFAULT_URL = "http://localhost:8080/open-metadata/access-services/subject-area";
    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Test Glossary for subject area definition sample";
    private static final String DEFAULT_TEST_CATEGORY_NAME = "Test subject area definition A";
    private static final String DEFAULT_TEST_CATEGORY_NAME_UPDATED = "Test subject area definition A updated";
    private static final String DEFAULT_TEST_CATEGORY_NAME2 = "Test subject area definition B";
    private static final String DEFAULT_TEST_CATEGORY_NAME3 = "Test subject area definition C";
    private static SubjectAreaCategory subjectAreaCategory = null;

    public static void main(String args[])
    {
        SubjectAreaDefinition subjectAreaDefinition = null;
        try
        {
            String url = RunAllFVT.getUrl(args);
            initialiseSubjectAreaDefinitionFVT(url);
            System.out.println("Create a glossary");
            GlossaryFVT.initialiseGlossaryFVT(url);
            Glossary glossary = GlossaryFVT.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
            System.out.println("Create a subjectArea1  using glossary name");
            SubjectAreaDefinition subjectArea1  = SubjectAreaDefinitionCategoryFVT.createSubjectAreaDefinitionWithGlossaryName(DEFAULT_TEST_CATEGORY_NAME, DEFAULT_TEST_GLOSSARY_NAME);
            System.out.println("Create a subjectArea2 using glossary guid");
            SubjectAreaDefinition subjectArea2 = SubjectAreaDefinitionCategoryFVT.createSubjectAreaDefinitionWithGlossaryGuid(DEFAULT_TEST_CATEGORY_NAME2, glossary.getSystemAttributes().getGUID());


            SubjectAreaDefinition subjectAreaForUpdate = new SubjectAreaDefinition();
            subjectAreaForUpdate.setName(DEFAULT_TEST_CATEGORY_NAME_UPDATED);

            if (subjectArea1  != null)
            {
                System.out.println("Get the subjectArea1 ");
                String guid = subjectArea1 .getSystemAttributes().getGUID();
                SubjectAreaDefinition gotSubjectAreaDefinition = getSubjectAreaDefinitionByGUID(guid);
                System.out.println("Update the subjectArea1 ");
                SubjectAreaDefinition updatedSubjectAreaDefinition = updateSubjectAreaDefinition(guid, subjectAreaForUpdate);
                System.out.println("Get the subjectArea1  again");
                gotSubjectAreaDefinition = getSubjectAreaDefinitionByGUID(guid);
                System.out.println("Delete the subjectArea1 ");
                gotSubjectAreaDefinition = deleteSubjectAreaDefinition(guid);
                System.out.println("Purge a subjectArea1 ");

                // create subjectArea DEFAULT_TEST_CATEGORY_NAME3 with parent
                System.out.println("Create a subjectArea with a parent subjectArea");
                SubjectAreaDefinition subjectArea3 = SubjectAreaDefinitionCategoryFVT.createSubjectAreaDefinitionWithParentGlossaryName(DEFAULT_TEST_CATEGORY_NAME3, subjectArea2, DEFAULT_TEST_GLOSSARY_NAME);
                System.out.println("Create a 2nd subjectArea with the same name and parent subjectArea - expect to fail");
                // create 2nd subjectArea DEFAULT_TEST_CATEGORY_NAME3 with parent should fail.
                try
                {
                    SubjectAreaDefinitionCategoryFVT.createSubjectAreaDefinitionWithParentGlossaryName(DEFAULT_TEST_CATEGORY_NAME3, subjectArea2, DEFAULT_TEST_GLOSSARY_NAME);
                } catch (InvalidParameterException e)
                {
                    System.out.println("Creating subjectArea with same name as a sibling not allowed");
                }
            }
        } catch (SubjectAreaCheckedExceptionBase e)
        {
            System.out.println("ERROR: " + e.getErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        } catch (IOException e)
        {
            System.out.println("Error getting user input");
        }
    }

    private static SubjectAreaDefinition createSubjectAreaDefinitionWithParentGlossaryName(String subjectAreaName, SubjectAreaDefinition parent, String glossaryName) throws SubjectAreaCheckedExceptionBase
    {
        SubjectAreaDefinition subjectArea = new SubjectAreaDefinition();
        subjectArea.setName(subjectAreaName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setName(glossaryName);
        subjectArea.setGlossary(glossarySummary);
        CategorySummary parentCategory = new CategorySummary();
        parentCategory.setGuid(parent.getSystemAttributes().getGUID());
        subjectArea.setParentCategory(parentCategory);
        SubjectAreaDefinition newSubjectAreaDefinition = subjectAreaCategory.createSubjectAreaDefinition(USERID, subjectArea);


        if (newSubjectAreaDefinition != null)
        {
            System.out.println("Created SubjectAreaDefinition " + newSubjectAreaDefinition.getName() + " with guid " + newSubjectAreaDefinition.getSystemAttributes().getGUID());
        }
        return newSubjectAreaDefinition;
    }

    public static SubjectAreaDefinition createSubjectAreaDefinitionWithGlossaryGuid(String subjectAreaName, String glossaryGuid) throws SubjectAreaCheckedExceptionBase
    {
        SubjectAreaDefinition subjectArea = new SubjectAreaDefinition();
        subjectArea.setName(subjectAreaName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        subjectArea.setGlossary(glossarySummary);
        SubjectAreaDefinition newSubjectAreaDefinition = subjectAreaCategory.createSubjectAreaDefinition(USERID, subjectArea);
        if (newSubjectAreaDefinition != null)
        {
            System.out.println("Created SubjectAreaDefinition " + newSubjectAreaDefinition.getName() + " with guid " + newSubjectAreaDefinition.getSystemAttributes().getGUID());
        }
        return newSubjectAreaDefinition;
    }

    public static SubjectAreaDefinition createSubjectAreaDefinitionWithGlossaryName(String subjectAreaName, String glossaryName) throws SubjectAreaCheckedExceptionBase
    {
        SubjectAreaDefinition subjectArea = new SubjectAreaDefinition();
        subjectArea.setName(subjectAreaName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setName(glossaryName);
        subjectArea.setGlossary(glossarySummary);
        SubjectAreaDefinition newSubjectAreaDefinition = subjectAreaCategory.createSubjectAreaDefinition(USERID, subjectArea);
        if (newSubjectAreaDefinition != null)
        {
            System.out.println("Created SubjectAreaDefinition " + newSubjectAreaDefinition.getName() + " with guid " + newSubjectAreaDefinition.getSystemAttributes().getGUID());
        }
        return newSubjectAreaDefinition;
    }

    public static SubjectAreaDefinition getSubjectAreaDefinitionByGUID(String guid) throws SubjectAreaCheckedExceptionBase
    {
        SubjectAreaDefinition subjectArea = subjectAreaCategory.getSubjectAreaDefinitionByGuid(USERID, guid);
        if (subjectArea != null)
        {
            System.out.println("Got SubjectAreaDefinition " + subjectArea.getName() + " with guid " + subjectArea.getSystemAttributes().getGUID() + " and status " + subjectArea.getSystemAttributes().getStatus());
        }
        return subjectArea;
    }

    public static SubjectAreaDefinition updateSubjectAreaDefinition(String guid, SubjectAreaDefinition subjectArea) throws SubjectAreaCheckedExceptionBase
    {
        SubjectAreaDefinition updatedSubjectAreaDefinition = subjectAreaCategory.updateSubjectAreaDefinition(USERID, guid, subjectArea);
        if (updatedSubjectAreaDefinition != null)
        {
            System.out.println("Updated SubjectAreaDefinition name to " + updatedSubjectAreaDefinition.getName());
        }
        return updatedSubjectAreaDefinition;
    }

    public static SubjectAreaDefinition deleteSubjectAreaDefinition(String guid) throws SubjectAreaCheckedExceptionBase
    {
        SubjectAreaDefinition deletedSubjectAreaDefinition = subjectAreaCategory.deleteSubjectAreaDefinition(USERID, guid);
        if (deletedSubjectAreaDefinition != null)
        {
            System.out.println("Deleted SubjectAreaDefinition name is " + deletedSubjectAreaDefinition.getName());
        }
        return deletedSubjectAreaDefinition;
    }

    public static void purgeSubjectAreaDefinition(String guid) throws SubjectAreaCheckedExceptionBase
    {
        subjectAreaCategory.purgeSubjectAreaDefinition(USERID, guid);
        System.out.println("Purge succeeded");
    }

    /**
     * Call this to initialise the glossary FVT
     *
     * @param url supplied base url for the subject area OMAS
     * @throws InvalidParameterException a parameter is null or an invalid value.
     */
    public static void initialiseSubjectAreaDefinitionFVT(String url) throws InvalidParameterException
    {
        subjectAreaCategory = new SubjectAreaImpl(url).getSubjectAreaCategory();
    }
}
