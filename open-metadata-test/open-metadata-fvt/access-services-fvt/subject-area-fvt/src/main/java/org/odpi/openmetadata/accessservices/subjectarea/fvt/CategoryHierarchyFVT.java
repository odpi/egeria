/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaEntityClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.categories.SubjectAreaCategoryClient;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * FVT to call subject area category client API to create a category hierarchy,
 * based on given DEPTH and WIDTH values.
 */
public class CategoryHierarchyFVT
{
    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Test Glossary for category hierarchy FVT";
    private static final String DEFAULT_TEST_CATEGORY_NAME_BASE = "Test hierarchy category ";

    private static final int WIDTH = 3;
    private static final int DEPTH = 4;
    private static int depth_counter = 0;

    private static SubjectAreaEntityClient<Category> subjectAreaCategory = null;
    private GlossaryFVT glossaryFVT =null;
    private String url = null;
    private String glossaryGuid = null;
    private String userId = null;
    private String serverName = null;

    public static void main(String args[])
    {
        SubjectArea subjectArea = null;
        String url = null;
        try
        {
            url = RunAllFVT.getUrl(args);
            runWith2Servers(url);
        } catch (IOException e1)
        {
            System.out.println("Error getting user input");
        } catch (SubjectAreaFVTCheckedException e) {
            System.out.println("ERROR: " + e.getMessage() );
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            System.out.println("ERROR: " + e.getReportedErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        }

    }
    public static void runWith2Servers(String url) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, SubjectAreaFVTCheckedException {
        CategoryHierarchyFVT fvt = new CategoryHierarchyFVT(url,FVTConstants.SERVER_NAME1,FVTConstants.USERID);
        fvt.run();
    }

    public CategoryHierarchyFVT(String url,String serverName,String userId) throws InvalidParameterException {
        SubjectAreaRestClient client = new SubjectAreaRestClient(serverName, url);
        subjectAreaCategory = new SubjectAreaCategoryClient(client);
        glossaryFVT = new GlossaryFVT(url,serverName,userId);
        this.url=url;
        this.userId=userId;
        this.serverName=serverName;
    }

    public static void runIt(String url, String serverName, String userId) throws InvalidParameterException, PropertyServerException, SubjectAreaFVTCheckedException, UserNotAuthorizedException {
        CategoryHierarchyFVT fvt = new CategoryHierarchyFVT(url,serverName,userId);
        fvt.run();
    }

    public void run() throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectArea subjectArea = null;

        System.out.println("Create a glossary");
        Glossary glossary = glossaryFVT.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
        FVTUtils.validateNode(glossary);
        String glossaryGuid = glossary.getSystemAttributes().getGUID();
        System.out.println("Create category hierarchy");
        Set<Category> categories = createTopCategories(glossaryGuid);
        while (depth_counter < DEPTH)
        {
            depth_counter++;
            Set<Category> childrenCategories = new HashSet<>();
            for (Category category : categories)
            {
                FVTUtils.validateNode(category);
                childrenCategories = createChildrenCategories(category,glossaryGuid);
            }
            categories = childrenCategories;
        }
    }

    /**
     * Create top categories i.e. categories with no parent category
     * @param glossaryGuid glossary userId
     * @return a set of created categories
     * @throws SubjectAreaFVTCheckedException an error occurred.
     */
    private Set<Category> createTopCategories(String glossaryGuid) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Set<Category> categories = new HashSet<>();
        for (int width_counter = 0; width_counter < WIDTH; width_counter++)
        {
            String categoryName = createName(0, width_counter);
            Category category = createCategoryWithGlossaryGuid(categoryName,glossaryGuid);
            FVTUtils.validateNode(category);
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
     * @param glossaryGuid userId of the associated glossary
     * @return a set of created categories
     * @throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException an error occurred.
     */
    private Set<Category> createChildrenCategories(Category parent,String glossaryGuid) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        Set<Category> categories = new HashSet<>();
        for (int width_counter = 0; width_counter < WIDTH; width_counter++)
        {
            String categoryName = createName(depth_counter, width_counter);
            Category category = createCategoryWithParentGlossary(categoryName, parent, glossaryGuid);
            FVTUtils.validateNode(category);
            System.out.println("Created category with name  " + categoryName + " with parent " + parent.getName());
            categories.add(category);
        }
        return categories;
    }

    /**
     * Create a category associated under a parent category and associate with the named glossary
     *
     * @param categoryName name of the category to create
     * @param parent       category under which to create this category
     * @param glossaryGuid userId of the associated glossary
     * @return created category
     */
    private Category createCategoryWithParentGlossary(String categoryName, Category parent, String glossaryGuid) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Category category = new Category();
        category.setName(categoryName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        category.setGlossary(glossarySummary);
        CategorySummary parentCategorysummary = new CategorySummary();
        parentCategorysummary.setGuid(parent.getSystemAttributes().getGUID());
        category.setParentCategory(parentCategorysummary);
        Category newCategory = subjectAreaCategory.create(userId, category);
        FVTUtils.validateNode(newCategory);
        System.out.println("Created Category " + newCategory.getName() + " with userId " + newCategory.getSystemAttributes().getGUID());
        return newCategory;
    }

    /**
     * Create a category associated with a glossary, identified with a userId.
     *
     * @param categoryName name of the category to create
     * @param glossaryGuid userId of the glossary to associate with this category
     * @return created category
     * @throws SubjectAreaFVTCheckedException FVT error
     */
    public  Category createCategoryWithGlossaryGuid(String categoryName, String glossaryGuid) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Category category = new Category();
        category.setName(categoryName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        category.setGlossary(glossarySummary);
        Category newCategory = subjectAreaCategory.create(userId, category);
        FVTUtils.validateNode(newCategory);
        System.out.println("Created Category " + newCategory.getName() + " with userId " + newCategory.getSystemAttributes().getGUID());
        return newCategory;
    }
}
