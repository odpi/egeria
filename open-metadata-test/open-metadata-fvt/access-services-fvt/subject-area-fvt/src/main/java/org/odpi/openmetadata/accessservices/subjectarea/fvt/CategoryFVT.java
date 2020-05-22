/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaCategory;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;

import java.io.IOException;
import java.util.List;

/**
 * FVT resource to call subject area category client API
 */
public class CategoryFVT {


    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Test Glossary for category sample";
    private static final String DEFAULT_TEST_CATEGORY_NAME = "Test category A";
    private static final String DEFAULT_TEST_CATEGORY_NAME_UPDATED = "Test category A updated";
    private static final String DEFAULT_TEST_CATEGORY_NAME2 = "Test category B";
    private static final String DEFAULT_TEST_CATEGORY_NAME3 = "Test category C";
    private SubjectAreaCategory subjectAreaCategory = null;
    private GlossaryFVT glossaryFVT = null;
    private String serverName = null;
    private String userId = null;

    public static void main(String[] args) {
        SubjectArea subjectArea = null;
        String url = null;
        try {
            url = RunAllFVT.getUrl(args);
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
        CategoryFVT fvt = new CategoryFVT(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        fvt.run();
        CategoryFVT fvt2 = new CategoryFVT(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
        fvt2.run();
    }

    public CategoryFVT(String url, String serverName, String userId) throws InvalidParameterException {
        subjectAreaCategory = new SubjectAreaImpl(serverName, url).getSubjectAreaCategory();
        glossaryFVT = new GlossaryFVT(url, serverName, userId);
        this.serverName = serverName;
        this.userId = userId;
    }

    public static void runIt(String url, String serverName, String userId) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException {
        CategoryFVT fvt = new CategoryFVT(url, serverName, userId);
        fvt.run();
    }

    public void run() throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException {

        System.out.println("Create a glossary");
        Glossary glossary = glossaryFVT.createGlossary(serverName + " " + DEFAULT_TEST_GLOSSARY_NAME);
        String glossaryGuid = glossary.getSystemAttributes().getGUID();
        System.out.println("Create a category1");
        Category category1 = createCategoryWithGlossaryGuid(serverName + " " + DEFAULT_TEST_CATEGORY_NAME, glossary.getSystemAttributes().getGUID());
        System.out.println("Create a category2");
        Category category2 = createCategoryWithGlossaryGuid(serverName + " " + DEFAULT_TEST_CATEGORY_NAME2, glossary.getSystemAttributes().getGUID());

        FVTUtils.validateNode(category1);
        FVTUtils.validateNode(category2);


        Category categoryForUpdate = new Category();
        categoryForUpdate.setName(serverName + " " + DEFAULT_TEST_CATEGORY_NAME_UPDATED);
        System.out.println("Get the category1");
        String guid = category1.getSystemAttributes().getGUID();
        Category gotCategory = getCategoryByGUID(guid);
        FVTUtils.validateNode(gotCategory);
        System.out.println("Update the category1");
        Category updatedCategory = updateCategory(guid, categoryForUpdate);
        FVTUtils.validateNode(updatedCategory);
        System.out.println("Get the category1 again");
        gotCategory = getCategoryByGUID(guid);
        FVTUtils.validateNode(gotCategory);
        System.out.println("Delete the category1");
        gotCategory = deleteCategory(guid);
        FVTUtils.validateNode(gotCategory);
        System.out.println("Restore the category1");
        gotCategory = restoreCategory(guid);
        FVTUtils.validateNode(gotCategory);
        System.out.println("Delete the category1");
        gotCategory = deleteCategory(guid);
        FVTUtils.validateNode(gotCategory);
        System.out.println("Purge a category1");
        purgeCategory(gotCategory.getSystemAttributes().getGUID());
        // create category DEFAULT_TEST_CATEGORY_NAME3 with parent
        System.out.println("Create a category with a parent category");
        Category category3 = createCategoryWithParentGlossaryGuid(serverName, serverName + " " + DEFAULT_TEST_CATEGORY_NAME3, category2.getSystemAttributes().getGUID(), glossary.getSystemAttributes().getGUID());
        FVTUtils.validateNode(category3);

        System.out.println("create categories to find");
        Category categoryForFind1 = getCategoryForInput("abc", glossaryGuid);
        categoryForFind1.setQualifiedName("yyy");
        categoryForFind1 = issueCreateCategory(categoryForFind1);
        FVTUtils.validateNode(categoryForFind1);
        Category categoryForFind2 = createCategory("yyy", glossaryGuid);
        FVTUtils.validateNode(categoryForFind2);
        Category categoryForFind3 = createCategory("zzz", glossaryGuid);
        FVTUtils.validateNode(categoryForFind3);
        Category categoryForFind4 = createCategory("This is a Category with spaces in name", glossaryGuid);
        FVTUtils.validateNode(categoryForFind4);

        List<Category> results = findCategories("zzz");
        if (results.size() != 1) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 1 back on the find got " + results.size());
        }
        results = findCategories("yyy");
        if (results.size() != 2) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 2 back on the find got " + results.size());
        }
        //soft delete a category and check it is not found
        Category deleted4 = deleteCategory(categoryForFind2.getSystemAttributes().getGUID());
        FVTUtils.validateNode(deleted4);
        results = findCategories("yyy");
        if (results.size() != 1) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 1 back on the find got " + results.size());
        }

        // search for a category with a name with spaces in
        results = findCategories("This is a Category with spaces in name");
        if (results.size() != 1) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 1 back on the find got " + results.size());
        }
    }

    private Category createCategoryWithParentGlossaryGuid(String serverName, String subjectAreaName, String parentGuid, String glossaryGuid) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException {
        Category category = new Category();
        category.setName(subjectAreaName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        category.setGlossary(glossarySummary);
        CategorySummary parentCategory = new CategorySummary();
        parentCategory.setGuid(parentGuid);
        category.setParentCategory(parentCategory);
        Category newCategory = subjectAreaCategory.createCategory(this.userId, category);
        FVTUtils.validateNode(newCategory);

        System.out.println("Created Category " + newCategory.getName() + " with glossaryGuid " + newCategory.getSystemAttributes().getGUID());
        return newCategory;
    }

    public Category createCategoryWithGlossaryGuid(String categoryName, String glossaryGuid) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException {
        Category category = new Category();
        category.setName(categoryName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        category.setGlossary(glossarySummary);
        Category newCategory = subjectAreaCategory.createCategory(this.userId, category);
        FVTUtils.validateNode(newCategory);
        System.out.println("Created Category " + newCategory.getName() + " with userId " + newCategory.getSystemAttributes().getGUID());
        return newCategory;
    }

    public Category createCategory(String categoryName, String glossaryGuid) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException {
        Category category = getCategoryForInput(categoryName, glossaryGuid);
        return issueCreateCategory(category);
    }

    private Category issueCreateCategory(Category category) throws MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException, UnrecognizedGUIDException {
        Category newCategory = subjectAreaCategory.createCategory(this.userId, category);
        if (newCategory != null) {
            System.out.println("Created Category " + newCategory.getName() + " with userId " + newCategory.getSystemAttributes().getGUID());
        }
        return newCategory;
    }

    private Category getCategoryForInput(String categoryName, String glossaryGuid) {
        Category category = new Category();
        category.setName(categoryName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        category.setGlossary(glossarySummary);
        return category;
    }


    public Category getCategoryByGUID(String guid) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException {
        Category category = subjectAreaCategory.getCategoryByGuid(this.userId, guid);
        FVTUtils.validateNode(category);
        System.out.println("Got Category " + category.getName() + " with userId " + category.getSystemAttributes().getGUID() + " and status " + category.getSystemAttributes().getStatus());
        return category;
    }

    public List<Category> findCategories(String criteria) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException {
        List<Category> categories = subjectAreaCategory.findCategory(
                this.userId,
                criteria,
                null,
                0,
                0,
                null,
                null);
        return categories;
    }

    public Category updateCategory(String guid, Category category) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException {
        Category updatedCategory = subjectAreaCategory.updateCategory(this.userId, guid, category);
        FVTUtils.validateNode(updatedCategory);
        System.out.println("Updated Category name to " + updatedCategory.getName());
        return updatedCategory;
    }

    public Category deleteCategory(String guid) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException {
        Category deletedCategory = subjectAreaCategory.deleteCategory(this.userId, guid);
        System.out.println("Deleted Category name is " + deletedCategory.getName());
        return deletedCategory;
    }

    public Category restoreCategory(String guid) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException {
        Category restoredCategory = subjectAreaCategory.restoreCategory(this.userId, guid);
        FVTUtils.validateNode(restoredCategory);
        System.out.println("restored Category name is " + restoredCategory.getName());
        return restoredCategory;
    }

    public void purgeCategory(String guid) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException {
        subjectAreaCategory.purgeCategory(this.userId, guid);
        System.out.println("Purge succeeded");
    }

    public List<Line> getCategoryRelationships(Category category) throws UserNotAuthorizedException, UnexpectedResponseException, InvalidParameterException, FunctionNotSupportedException, MetadataServerUncontactableException {
        return subjectAreaCategory.getCategoryRelationships(this.userId,
                                                            category.getSystemAttributes().getGUID(),
                                                            null,
                                                            0,
                                                            0,
                                                            null,
                                                            null);
    }
}
