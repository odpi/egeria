/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaCategory;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * FVT to call subject area category client API to create a category hierarchy,
 * based on given DEPTH and WIDTH values.
 */
public class CategoryHierarchyFVT
{
    private static final String USERID = " Fred";
    private static final String DEFAULT_URL = "http://localhost:8080/open-metadata/access-services/subject-area";
    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Test Glossary for category hierarchy FVT";
    private static final String DEFAULT_TEST_CATEGORY_NAME_BASE = "Test hierarchy category ";

    private static final int WIDTH = 3;
    private static final int DEPTH = 4;
    private static int depth_counter = 0;

    private static SubjectAreaCategory subjectAreaCategory = null;

    public static void main(String args[])
    {
        SubjectArea subjectArea = null;
        try
        {
            String url = RunAllFVT.getUrl(args);
            initialiseCategoryFVT(url);
            System.out.println("Create a glossary");
            GlossaryFVT.initialiseGlossaryFVT(url);
            Glossary glossary = GlossaryFVT.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
            System.out.println("Create category hierarchy");
            Set<Category> categories = createTopCategories();
            while (depth_counter < DEPTH)
            {
                depth_counter++;
                Set<Category> childrenCategories = new HashSet();
                for (Category category : categories)
                {
                    childrenCategories = createChildrenCategories(category);
                }
                categories = childrenCategories;
            }

        } catch (SubjectAreaCheckedExceptionBase e)
        {
            System.out.println("ERROR: " + e.getErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        } catch (IOException e)
        {
            System.out.println("Error getting user input");
        }
    }

    /**
     * Create top categories i.e. categories with no parent category
     *
     * @return a set of created categories
     * @throws SubjectAreaCheckedExceptionBase an error occurred.
     */
    private static Set<Category> createTopCategories() throws SubjectAreaCheckedExceptionBase
    {
        Set<Category> categories = new HashSet();
        for (int width_counter = 0; width_counter < WIDTH; width_counter++)
        {
            String categoryName = createName(0, width_counter);
            Category category = CategoryHierarchyFVT.createCategoryWithGlossaryName(categoryName, DEFAULT_TEST_GLOSSARY_NAME);
            System.out.println("Created category with name  " + categoryName + " with no parent");
            categories.add(category);
        }
        return categories;
    }

    /**
     * Derive a category name based on a base string a DEPTH and a WIDTH
     *
     * @param depth DEPTH of hierarchy
     * @param width WIDTH of hierarchy
     * @return category name
     */
    private static String createName(int depth, int width)
    {
        return DEFAULT_TEST_CATEGORY_NAME_BASE + "d" + depth + "w" + width;
    }

    /**
     * Create children categories i.e. categories under the supplied parent category
     *
     * @param parent parent category
     * @return a set of created categories
     * @throws SubjectAreaCheckedExceptionBase an error occurred.
     */
    private static Set<Category> createChildrenCategories(Category parent) throws SubjectAreaCheckedExceptionBase
    {

        Set<Category> categories = new HashSet<>();
        for (int width_counter = 0; width_counter < WIDTH; width_counter++)
        {
            String categoryName = createName(depth_counter, width_counter);
            Category category = createCategoryWithParentGlossaryName(categoryName, parent, DEFAULT_TEST_GLOSSARY_NAME);
            System.out.println("Created category with name  " + categoryName + " with parent " + parent.getName());
            categories.add(category);
        }
        return categories;
    }

    /**
     * Create a category associated under a parent category and associate with the named glossary
     *
     * @param categoryName name of the category to create
     * @param parent       category under whiich to crerate this category
     * @param glossaryName name of the associated glossary
     * @return created category
     * @throws SubjectAreaCheckedExceptionBase
     */
    private static Category createCategoryWithParentGlossaryName(String categoryName, Category parent, String glossaryName) throws SubjectAreaCheckedExceptionBase
    {
        Category category = new Category();
        category.setName(categoryName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setName(glossaryName);
        category.setGlossary(glossarySummary);
        CategorySummary parentCategorysummary = new CategorySummary();
        parentCategorysummary.setGuid(parent.getSystemAttributes().getGUID());
        category.setParentCategory(parentCategorysummary);
        Category newCategory = subjectAreaCategory.createCategory(USERID, category);
        if (newCategory != null)
        {
            System.out.println("Created Category " + newCategory.getName() + " with guid " + newCategory.getSystemAttributes().getGUID());
        }
        return newCategory;
    }

    /**
     * Create a category associated with a glossary, identified with a guid.
     *
     * @param categoryName name of the category to create
     * @param glossaryGuid guid of the glossary to associate with this category
     * @return created category
     * @throws SubjectAreaCheckedExceptionBase
     */
    public static Category createCategoryWithGlossaryGuid(String categoryName, String glossaryGuid) throws SubjectAreaCheckedExceptionBase
    {
        Category category = new Category();
        category.setName(categoryName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        category.setGlossary(glossarySummary);
        Category newCategory = subjectAreaCategory.createCategory(USERID, category);
        if (newCategory != null)
        {
            System.out.println("Created Category " + newCategory.getName() + " with guid " + newCategory.getSystemAttributes().getGUID());
        }
        return newCategory;
    }

    public static Category createCategoryWithGlossaryName(String categoryName, String glossaryName) throws SubjectAreaCheckedExceptionBase
    {
        Category category = new Category();
        category.setName(categoryName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setName(glossaryName);
        category.setGlossary(glossarySummary);
        Category newCategory = subjectAreaCategory.createCategory(USERID, category);
        if (newCategory != null)
        {
            System.out.println("Created Category " + newCategory.getName() + " with guid " + newCategory.getSystemAttributes().getGUID());
        }
        return newCategory;
    }

    public static Category getCategoryByGUID(String guid) throws SubjectAreaCheckedExceptionBase
    {
        Category category = subjectAreaCategory.getCategoryByGuid(USERID, guid);
        if (category != null)
        {
            System.out.println("Got Category " + category.getName() + " with guid " + category.getSystemAttributes().getGUID() + " and status " + category.getSystemAttributes().getStatus());
        }
        return category;
    }

    public static Category updateCategory(String guid, Category category) throws SubjectAreaCheckedExceptionBase
    {
        Category updatedCategory = subjectAreaCategory.updateCategory(USERID, guid, category);
        if (updatedCategory != null)
        {
            System.out.println("Updated Category name to " + updatedCategory.getName());
        }
        return updatedCategory;
    }

    public static Category deleteCategory(String guid) throws SubjectAreaCheckedExceptionBase
    {
        Category deletedCategory = subjectAreaCategory.deleteCategory(USERID, guid);
        if (deletedCategory != null)
        {
            System.out.println("Deleted Category name is " + deletedCategory.getName());
        }
        return deletedCategory;
    }

    public static void purgeCategory(String guid) throws SubjectAreaCheckedExceptionBase
    {
        subjectAreaCategory.purgeCategory(USERID, guid);
        System.out.println("Purge succeeded");
    }

    /**
     * Call this to initialise the glossary FVT
     *
     * @param url server url
     * @throws InvalidParameterException
     */
    public static void initialiseCategoryFVT(String url) throws InvalidParameterException
    {
        subjectAreaCategory = new SubjectAreaImpl(url).getSubjectAreaCategory();
    }
}
