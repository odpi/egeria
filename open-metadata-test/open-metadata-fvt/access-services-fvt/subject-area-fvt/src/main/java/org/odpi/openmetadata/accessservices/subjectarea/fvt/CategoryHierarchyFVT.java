/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
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

    private GlossaryFVT glossaryFVT =null;
    private CategoryFVT categoryFVT = null;

    public static void main(String args[])
    {
        try
        {
            String url = RunAllFVTOn2Servers.getUrl(args);
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
        runIt(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        runIt(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
    }

    public CategoryHierarchyFVT(String url,String serverName,String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        categoryFVT = new CategoryFVT(url,serverName,userId);
        glossaryFVT = new GlossaryFVT(url,serverName,userId);
    }

    public static void runIt(String url, String serverName, String userId) throws InvalidParameterException, PropertyServerException, SubjectAreaFVTCheckedException, UserNotAuthorizedException {
        System.out.println("CategoryHierarchyFVT runIt started");
        CategoryHierarchyFVT fvt = new CategoryHierarchyFVT(url,serverName,userId);
        fvt.run();
        fvt.deleteRemaining();
        System.out.println("CategoryHierarchyFVT runIt stopped");
    }

    public void run() throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
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
            Category category =categoryFVT.createCategoryWithGlossaryGuid(categoryName,glossaryGuid);
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
            Category category = categoryFVT.createCategoryWithParentGlossary(categoryName, parent, glossaryGuid);
            FVTUtils.validateNode(category);
            System.out.println("Created category with name  " + categoryName + " with parent " + parent.getName());
            categories.add(category);
        }
        return categories;
    }

    void deleteRemaining() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, SubjectAreaFVTCheckedException {
        categoryFVT.deleteRemainingCategories();
        glossaryFVT.deleteRemainingGlossaries();
    }
}
