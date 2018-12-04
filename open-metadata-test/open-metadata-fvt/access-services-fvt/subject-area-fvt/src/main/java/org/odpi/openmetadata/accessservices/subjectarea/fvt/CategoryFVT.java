/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaCategory;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;

import java.io.IOException;

/**
 * FVT resource to call subject area category client API
 */
public class CategoryFVT
{


    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Test Glossary for category sample";
    private static final String DEFAULT_TEST_CATEGORY_NAME = "Test category A";
    private static final String DEFAULT_TEST_CATEGORY_NAME_UPDATED = "Test category A updated";
    private static final String DEFAULT_TEST_CATEGORY_NAME2 = "Test category B";
    private static final String DEFAULT_TEST_CATEGORY_NAME3 = "Test category C";
    private SubjectAreaCategory subjectAreaCategory = null;
    private GlossaryFVT glossaryFVT =null;
    private String serverName = null;

    public static void main(String args[])
    {
        SubjectArea subjectArea = null;
        String url = null;
        try
        {
            url = RunAllFVT.getUrl(args);
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
        CategoryFVT fvt =new CategoryFVT(url,FVTConstants.SERVER_NAME1);
        fvt.run();
        CategoryFVT fvt2 =new CategoryFVT(url,FVTConstants.SERVER_NAME2);
        fvt2.run();
    }
    public CategoryFVT(String url,String serverName) throws InvalidParameterException
    {
        subjectAreaCategory = new SubjectAreaImpl(serverName,url).getSubjectAreaCategory();
        glossaryFVT = new GlossaryFVT(url,serverName);
        this.serverName=serverName;
    }

    public void run() throws SubjectAreaCheckedExceptionBase
    {

            System.out.println("Create a glossary");
            Glossary glossary = glossaryFVT.createGlossary(serverName+" "+DEFAULT_TEST_GLOSSARY_NAME);
            System.out.println("Create a category1 using glossary name");
            Category category1 = createCategoryWithGlossaryName(serverName+" "+DEFAULT_TEST_CATEGORY_NAME,serverName+" "+ DEFAULT_TEST_GLOSSARY_NAME);
            System.out.println("Create a category2 using glossary guid");
            Category category2 = createCategoryWithGlossaryGuid(serverName+" "+DEFAULT_TEST_CATEGORY_NAME2, glossary.getSystemAttributes().getGUID());
            Category categoryForUpdate = new Category();
            categoryForUpdate.setName(serverName+" "+DEFAULT_TEST_CATEGORY_NAME_UPDATED);

            if (category1 != null)
            {
                System.out.println("Get the category1");
                String guid = category1.getSystemAttributes().getGUID();
                Category gotCategory = getCategoryByGUID(guid);
                System.out.println("Update the category1");
                Category updatedCategory = updateCategory(guid, categoryForUpdate);
                System.out.println("Get the category1 again");
                gotCategory = getCategoryByGUID(guid);
                System.out.println("Delete the category1");
                gotCategory = deleteCategory(guid);
                System.out.println("Purge a category1");

                // create category DEFAULT_TEST_CATEGORY_NAME3 with parent
                System.out.println("Create a category with a parent category");
                Category category3 = createCategoryWithParentGlossaryName(serverName+" "+DEFAULT_TEST_CATEGORY_NAME3, category2, serverName+" "+DEFAULT_TEST_GLOSSARY_NAME);
                System.out.println("Create a 2nd category with the same name and parent category - expect to fail");
                // create 2nd category DEFAULT_TEST_CATEGORY_NAME3 with parent should fail.
                try
                {
                    createCategoryWithParentGlossaryName(serverName+" "+DEFAULT_TEST_CATEGORY_NAME3, category2, serverName+" "+DEFAULT_TEST_GLOSSARY_NAME);
                } catch (InvalidParameterException e)
                {
                    System.out.println("Creating category with same name as a sibling not allowed");
                }
            }
    }

    public Category createCategoryWithParentGlossaryName(String categoryName, Category parent, String glossaryName) throws SubjectAreaCheckedExceptionBase
    {
        Category category = new Category();
        category.setName(categoryName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setName(glossaryName);
        category.setGlossary(glossarySummary);
        CategorySummary parentCategorysummary = new CategorySummary();
        parentCategorysummary.setGuid(parent.getSystemAttributes().getGUID());
        category.setParentCategory(parentCategorysummary);
        Category newCategory = subjectAreaCategory.createCategory(serverName,FVTConstants.USERID, category);
        if (newCategory != null)
        {
            System.out.println("Created Category " + newCategory.getName() + " with guid " + newCategory.getSystemAttributes().getGUID());
        }
        return newCategory;
    }

    public Category createCategoryWithGlossaryGuid(String categoryName, String glossaryGuid) throws SubjectAreaCheckedExceptionBase
    {
        Category category = new Category();
        category.setName(categoryName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        category.setGlossary(glossarySummary);
        Category newCategory = subjectAreaCategory.createCategory(serverName,FVTConstants.USERID, category);
        if (newCategory != null)
        {
            System.out.println("Created Category " + newCategory.getName() + " with guid " + newCategory.getSystemAttributes().getGUID());
        }
        return newCategory;
    }

    public Category createCategoryWithGlossaryName(String categoryName, String glossaryName) throws SubjectAreaCheckedExceptionBase
    {
        Category category = new Category();
        category.setName(categoryName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setName(glossaryName);
        category.setGlossary(glossarySummary);
        Category newCategory = subjectAreaCategory.createCategory(serverName,FVTConstants.USERID, category);
        if (newCategory != null)
        {
            System.out.println("Created Category " + newCategory.getName() + " with guid " + newCategory.getSystemAttributes().getGUID());
        }
        return newCategory;
    }

    public Category getCategoryByGUID(String guid) throws SubjectAreaCheckedExceptionBase
    {
        Category category = subjectAreaCategory.getCategoryByGuid(serverName,FVTConstants.USERID, guid);
        if (category != null)
        {
            System.out.println("Got Category " + category.getName() + " with guid " + category.getSystemAttributes().getGUID() + " and status " + category.getSystemAttributes().getStatus());
        }
        return category;
    }

    public Category updateCategory(String guid, Category category) throws SubjectAreaCheckedExceptionBase
    {
        Category updatedCategory = subjectAreaCategory.updateCategory(serverName,FVTConstants.USERID, guid, category);
        if (updatedCategory != null)
        {
            System.out.println("Updated Category name to " + updatedCategory.getName());
        }
        return updatedCategory;
    }

    public Category deleteCategory(String guid) throws SubjectAreaCheckedExceptionBase
    {
        Category deletedCategory = subjectAreaCategory.deleteCategory(serverName,FVTConstants.USERID, guid);
        if (deletedCategory != null)
        {
            System.out.println("Deleted Category name is " + deletedCategory.getName());
        }
        return deletedCategory;
    }

    public void purgeCategory(String guid) throws SubjectAreaCheckedExceptionBase
    {
        subjectAreaCategory.purgeCategory(serverName,FVTConstants.USERID, guid);
        System.out.println("Purge succeeded");
    }
}
