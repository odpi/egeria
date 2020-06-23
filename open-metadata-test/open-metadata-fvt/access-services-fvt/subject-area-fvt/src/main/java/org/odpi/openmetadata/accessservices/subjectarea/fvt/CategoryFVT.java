/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaEntityClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.categories.SubjectAreaCategoryClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

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
    private SubjectAreaEntityClient<Category> subjectAreaCategory = null;
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
        } catch (SubjectAreaFVTCheckedException e) {
            System.out.println("ERROR: " + e.getMessage() );
        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            e.printStackTrace();
        }

    }

    public static void runWith2Servers(String url) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        CategoryFVT fvt = new CategoryFVT(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        fvt.run();
        CategoryFVT fvt2 = new CategoryFVT(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
        fvt2.run();
    }

    public CategoryFVT(String url, String serverName, String userId) throws InvalidParameterException {
        SubjectAreaRestClient client = new SubjectAreaRestClient(serverName, url);
        subjectAreaCategory = new SubjectAreaCategoryClient(client);
        glossaryFVT = new GlossaryFVT(url, serverName, userId);
        this.serverName = serverName;
        this.userId = userId;
    }

    public static void runIt(String url, String serverName, String userId) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        CategoryFVT fvt = new CategoryFVT(url, serverName, userId);
        fvt.run();
    }

    public void run() throws  SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

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
        deleteCategory(guid);
        //FVTUtils.validateNode(gotCategory);
        System.out.println("Restore the category1");
        gotCategory = restoreCategory(guid);
        FVTUtils.validateNode(gotCategory);
        System.out.println("Delete the category1");
        deleteCategory(guid);
        //FVTUtils.validateNode(gotCategory);
        System.out.println("Purge a category1");
        purgeCategory(gotCategory.getSystemAttributes().getGUID());
        // create category DEFAULT_TEST_CATEGORY_NAME3 with parent
        System.out.println("Create a category with a parent category");
        Category category3 = createCategoryWithParentGlossaryGuid(serverName + " " + DEFAULT_TEST_CATEGORY_NAME3, category2.getSystemAttributes().getGUID(), glossary.getSystemAttributes().getGUID());
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
        results = findCategories(null);
        if (results.size() != 6) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 6 back on the find got " + results.size());
        }

        results = subjectAreaCategory.findAll(userId);
        if (results.size() != 6) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 6 back on the find got " + results.size());
        }
        //soft delete a category and check it is not found
        deleteCategory(categoryForFind2.getSystemAttributes().getGUID());
        //FVTUtils.validateNode(deleted4);
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

    private Category createCategoryWithParentGlossaryGuid(String subjectAreaName, String parentGuid, String glossaryGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        Category category = new Category();
        category.setName(subjectAreaName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        category.setGlossary(glossarySummary);
        CategorySummary parentCategory = new CategorySummary();
        parentCategory.setGuid(parentGuid);
        category.setParentCategory(parentCategory);
        Category newCategory = subjectAreaCategory.create(this.userId, category);
        FVTUtils.validateNode(newCategory);

        System.out.println("Created Category " + newCategory.getName() + " with glossaryGuid " + newCategory.getSystemAttributes().getGUID());
        return newCategory;
    }

    public Category createCategoryWithGlossaryGuid(String categoryName, String glossaryGuid) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Category category = new Category();
        category.setName(categoryName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        category.setGlossary(glossarySummary);
        Category newCategory = subjectAreaCategory.create(this.userId, category);
        FVTUtils.validateNode(newCategory);
        System.out.println("Created Category " + newCategory.getName() + " with userId " + newCategory.getSystemAttributes().getGUID());
        return newCategory;
    }

    public Category createCategory(String categoryName, String glossaryGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Category category = getCategoryForInput(categoryName, glossaryGuid);
        return issueCreateCategory(category);
    }

    private Category issueCreateCategory(Category category) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException {
        Category newCategory = subjectAreaCategory.create(this.userId, category);
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


    public Category getCategoryByGUID(String guid) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Category category = subjectAreaCategory.getByGUID(this.userId, guid);
        FVTUtils.validateNode(category);
        System.out.println("Got Category " + category.getName() + " with userId " + category.getSystemAttributes().getGUID() + " and status " + category.getSystemAttributes().getStatus());
        return category;
    }

    public List<Category> findCategories(String criteria) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        FindRequest findRequest = new FindRequest();
        findRequest.setSearchCriteria(criteria);
        return subjectAreaCategory.find(this.userId, findRequest);
    }

    public Category updateCategory(String guid, Category category) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Category updatedCategory = subjectAreaCategory.update(this.userId, guid, category);
        FVTUtils.validateNode(updatedCategory);
        System.out.println("Updated Category name to " + updatedCategory.getName());
        return updatedCategory;
    }

    public void deleteCategory(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        subjectAreaCategory.delete(this.userId, guid);
        System.out.println("Deleted Category succeeded");
    }

    public Category restoreCategory(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        Category restoredCategory = subjectAreaCategory.restore(this.userId, guid);
        FVTUtils.validateNode(restoredCategory);
        System.out.println("restored Category name is " + restoredCategory.getName());
        return restoredCategory;
    }

    public void purgeCategory(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        subjectAreaCategory.purge(this.userId, guid);
        System.out.println("Purge succeeded");
    }

    public List<Line> getCategoryRelationships(Category category) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return subjectAreaCategory.getAllRelationships(this.userId, category.getSystemAttributes().getGUID());
    }
}
