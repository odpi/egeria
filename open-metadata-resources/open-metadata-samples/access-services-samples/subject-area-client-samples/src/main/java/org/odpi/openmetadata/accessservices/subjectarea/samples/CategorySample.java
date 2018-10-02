/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.samples;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaCategory;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Sample to call subject area category client API
 */
public class CategorySample {
    private static final String USERID = " Fred";
    private static final String DEFAULT_URL = "http://localhost:8080/open-metadata/access-services/subject-area";
    private static final String DEFAULT_TEST_GLOSSARY_NAME  = "Test Glossary for category sample";
    private static final String DEFAULT_TEST_CATEGORY_NAME  = "Test category A";
    private static final String DEFAULT_TEST_CATEGORY_NAME_UPDATED = "Test category A updated";
    private static final String DEFAULT_TEST_CATEGORY_NAME2  = "Test category B";
    private static final String DEFAULT_TEST_CATEGORY_NAME3  = "Test category C";
    private static SubjectAreaCategory subjectAreaCategory = null;

    public static void main(String args[]) {
        SubjectArea subjectArea = null;
        try {
            String url =RunAllSamples.getUrl(args);
            initialiseCategorySample(url);
            System.out.println("Create a glossary");
            GlossarySample.initialiseGlossarySample(url);
            Glossary glossary = GlossarySample.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
            System.out.println("Create a category1 using glossary name");
            Category category1 = CategorySample.createCategoryWithGlossaryName(DEFAULT_TEST_CATEGORY_NAME, DEFAULT_TEST_GLOSSARY_NAME);
            System.out.println("Create a category2 using glossary guid");
            Category category2 = CategorySample.createCategoryWithGlossaryGuid(DEFAULT_TEST_CATEGORY_NAME2, glossary.getSystemAttributes().getGUID());


            Category categoryForUpdate = new Category();
            categoryForUpdate.setName(DEFAULT_TEST_CATEGORY_NAME_UPDATED);

            if (category1 != null) {
                System.out.println("Get the category1");
                String guid = category1.getSystemAttributes().getGUID();
                Category gotCategory = getCategoryByGUID( guid);
                System.out.println("Update the category1");
                Category updatedCategory = updateCategory( guid, categoryForUpdate);
                System.out.println("Get the category1 again");
                gotCategory = getCategoryByGUID( guid);
                System.out.println("Delete the category1");
                gotCategory = deleteCategory( guid);
                System.out.println("Purge a category1");

                // create category DEFAULT_TEST_CATEGORY_NAME3 with parent
                System.out.println("Create a category with a parent category");
                Category category3 = CategorySample.createCategoryWithParentGlossaryName(DEFAULT_TEST_CATEGORY_NAME3,category2, DEFAULT_TEST_GLOSSARY_NAME);
                System.out.println("Create a 2nd category with the same name and parent category - expect to fail");
                // create 2nd category DEFAULT_TEST_CATEGORY_NAME3 with parent should fail.
                try {
                    CategorySample.createCategoryWithParentGlossaryName(DEFAULT_TEST_CATEGORY_NAME3, category2, DEFAULT_TEST_GLOSSARY_NAME);
                } catch (InvalidParameterException e) {
                    System.out.println("Creating category with same name as a sibling not allowed");
                }
            }
        } catch (SubjectAreaCheckedExceptionBase e) {
            System.out.println("ERROR: " + e.getErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        } catch (IOException e) {
            System.out.println("Error getting user input");
        }
    }

    private static Category createCategoryWithParentGlossaryName(String categoryName,Category parent, String glossaryName) throws SubjectAreaCheckedExceptionBase {
        Category category = new Category();
        category.setName(categoryName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setName(glossaryName);
        category.setGlossary(glossarySummary);
        CategorySummary parentCategorysummary = new CategorySummary();
        parentCategorysummary.setGuid(parent.getSystemAttributes().getGUID());
        category.setParentCategory(parentCategorysummary);
        Category newCategory = subjectAreaCategory.createCategory(USERID, category);
        if (newCategory != null) {
            System.out.println("Created Category " + newCategory.getName() + " with guid " + newCategory.getSystemAttributes().getGUID());
        }
        return newCategory;
    }

    public static Category createCategoryWithGlossaryGuid(String categoryName,String glossaryGuid) throws SubjectAreaCheckedExceptionBase {
        Category category = new Category();
        category.setName(categoryName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        category.setGlossary(glossarySummary);
        Category newCategory = subjectAreaCategory.createCategory(USERID, category);
        if (newCategory != null) {
            System.out.println("Created Category " + newCategory.getName() + " with guid " + newCategory.getSystemAttributes().getGUID());
        }
        return newCategory;
    }

    public static Category createCategoryWithGlossaryName(String categoryName,String glossaryName) throws SubjectAreaCheckedExceptionBase {
        Category category = new Category();
        category.setName(categoryName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setName(glossaryName);
        category.setGlossary(glossarySummary);
        Category newCategory = subjectAreaCategory.createCategory(USERID, category);
        if (newCategory != null) {
            System.out.println("Created Category " + newCategory.getName() + " with guid " + newCategory.getSystemAttributes().getGUID());
        }
        return newCategory;
    }

    public static Category getCategoryByGUID(String guid) throws SubjectAreaCheckedExceptionBase {
        Category category = subjectAreaCategory.getCategoryByGuid(USERID, guid);
        if (category != null) {
            System.out.println("Got Category " + category.getName() + " with guid " + category.getSystemAttributes().getGUID() + " and status " + category.getSystemAttributes().getStatus());
        }
        return category;
    }

    public static Category updateCategory(String guid, Category category) throws SubjectAreaCheckedExceptionBase {
        Category updatedCategory = subjectAreaCategory.updateCategory(USERID, guid, category);
        if (updatedCategory != null) {
            System.out.println("Updated Category name to " + updatedCategory.getName());
        }
        return updatedCategory;
    }

    public static Category deleteCategory(String guid) throws SubjectAreaCheckedExceptionBase {
        Category deletedCategory = subjectAreaCategory.deleteCategory(USERID, guid);
        if (deletedCategory != null) {
            System.out.println("Deleted Category name is " + deletedCategory.getName());
        }
        return deletedCategory;
    }
    public static void purgeCategory(String guid) throws SubjectAreaCheckedExceptionBase {
        subjectAreaCategory.purgeCategory(USERID, guid);
        System.out.println("Purge succeeded");
    }
    /**
     * Call this to initialise the glossary sample
     * @param url
     * @throws InvalidParameterException
     */
    public static void initialiseCategorySample(String url) throws InvalidParameterException {
        subjectAreaCategory = new SubjectAreaImpl(url).getSubjectAreaCategory();
    }
}
