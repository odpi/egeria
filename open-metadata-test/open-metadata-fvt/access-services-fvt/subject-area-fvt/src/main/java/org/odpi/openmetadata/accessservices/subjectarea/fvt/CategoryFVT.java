/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.categories.SubjectAreaCategoryClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.io.IOException;
import java.util.*;

/**
 * FVT resource to call subject area category client API
 */
public class CategoryFVT {
    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Test Glossary for category sample";
    private static final String DEFAULT_TEST_GLOSSARY_NAME2 = "Test Glossary for category sample 2";
    private static final String DEFAULT_TEST_CATEGORY_NAME = "Test category A";
    private static final String DEFAULT_TEST_CATEGORY_NAME_UPDATED = "Test category A updated";
    private static final String DEFAULT_TEST_CATEGORY_NAME2 = "Test category B";
    private static final String DEFAULT_TEST_CATEGORY_NAME3 = "Test category C";
    private static final String DEFAULT_TEST_CATEGORY_A_CHILD = "Test category A child";
    private SubjectAreaNodeClient<Category> subjectAreaCategory = null;
    private SubjectAreaCategoryClient subjectAreaCategoryClient = null;
    private GlossaryFVT glossaryFVT = null;
    private String serverName = null;
    private String userId = null;
    private int existingCategoryCount = 0;
    /*
     * Keep track of all the created guids in this set, by adding create and restore guids and removing when deleting.
     * At the end of the test it will delete any remaining guids.
     *
     * Note this FVT is called by other FVTs. Who ever constructs the FVT should run deleteRemainingCategories
     */
    private Set<String> createdCategoriesSet = new HashSet<>();

    public static void main(String[] args) {
        String url;
        try {
            url = RunAllFVTOn2Servers.getUrl(args);
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
        runIt(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        runIt(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
    }

    public CategoryFVT(String url, String serverName, String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaRestClient client = new SubjectAreaRestClient(serverName, url);
        subjectAreaCategory = new SubjectAreaCategoryClient<>(client);
        subjectAreaCategoryClient = (SubjectAreaCategoryClient) subjectAreaCategory;
        glossaryFVT = new GlossaryFVT(url, serverName, userId);
        this.serverName = serverName;
        this.userId = userId;
        existingCategoryCount = findCategories(".*").size();
        System.out.println("existingCategoryCount " + existingCategoryCount);
    }

    public static void runIt(String url, String serverName, String userId) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        System.out.println("CategoryFVT runIt started");
        CategoryFVT fvt = new CategoryFVT(url, serverName, userId);
        fvt.run();
        fvt.deleteRemaining();
        System.out.println("CategoryFVT runIt stopped");
    }
    public static int getCategoryCount(String url, String serverName, String userId) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, SubjectAreaFVTCheckedException  {
        CategoryFVT fvt = new CategoryFVT(url, serverName, userId);
        return fvt.findCategories(".*").size();
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

        FindRequest findRequest = new FindRequest();
        List<Category> results = glossaryFVT.getCategories(glossaryGuid, findRequest, true);
        if (results.size() != 2) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 2 back on getGlossaryCategories onlyTop true" + results.size());
        }
        results = glossaryFVT.getCategories(glossaryGuid, findRequest, false);
        if (results.size() != 2) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 2 back on getGlossaryCategories " + results.size());
        }
        findRequest.setPageSize(1);
        results = glossaryFVT.getCategories(glossaryGuid, findRequest, false);
        if (results.size() != 1) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 1 back on getGlossaryCategories with page size 1, got " + results.size());
        }
        Category categoryChild = createCategoryWithParentGlossary(DEFAULT_TEST_CATEGORY_A_CHILD, category1, glossaryGuid);
        FVTUtils.validateNode(categoryChild);
        findRequest.setPageSize(null);
        results = glossaryFVT.getCategories(glossaryGuid, findRequest, true);
        if (results.size() != 2) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 2 top categories back on getGlossaryCategories " + results.size());
        }
        results = glossaryFVT.getCategories(glossaryGuid, findRequest, false);
        if (results.size() != 3) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected all 3 categories  getGlossaryCategories " + results.size());
        }
        String category1Guid = category1.getSystemAttributes().getGUID();
        List<Category>  children = getCategoryChildren(category1Guid);
        if (children.size() != 1) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 1 Category as the child " + children.size());
        }
        deleteCategory(categoryChild.getSystemAttributes().getGUID());
        children = getCategoryChildren(category1Guid);
        if (children.size() != 0) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 0 Categories as the child has been deleted " + children.size());
        }
        purgeCategory(categoryChild.getSystemAttributes().getGUID());
        if (children.size() != 0) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 0 Categories as the child has been purged " + children.size());
        }

        Category categoryForUpdate = new Category();
        categoryForUpdate.setName(serverName + " " + DEFAULT_TEST_CATEGORY_NAME_UPDATED);
        System.out.println("Get the category1");
        String guid = category1Guid;
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
        results = findCategories("zzz");
        if (results.size() != 0) {
            for (Category result : results) {
                System.err.println("pre result name " + result.getName());
                System.err.println("pre result guid " + result.getSystemAttributes().getGUID());
                if ( result.getParentCategory() != null) {
                    System.err.println("pre result parent cat name " + result.getParentCategory().getName());
                }
            }
        }


        Category categoryForFind1 = getCategoryForInput("abc", glossaryGuid);
        categoryForFind1.setQualifiedName("iii");
        categoryForFind1 = issueCreateCategory(categoryForFind1);
        FVTUtils.validateNode(categoryForFind1);
        Category categoryForFind2 = createCategory("iii", glossaryGuid);
        FVTUtils.validateNode(categoryForFind2);
//        Category categoryForFind3 = createCategory("jjj", glossaryGuid);
        Category categoryForFind3ForInput = getCategoryForInput("jjj", glossaryGuid);
        categoryForFind3ForInput.setDescription("This is a description for jjj");
        Category categoryForFind3 = issueCreateCategory(categoryForFind3ForInput);
        FVTUtils.validateNode(categoryForFind3);
        Category categoryForFind4 = createCategory("This is a Category with spaces in name", glossaryGuid);
        FVTUtils.validateNode(categoryForFind4);

        results = findCategories("jjj");
        if (results.size() != 1) {
            System.err.println("categoryForFind3 name " + categoryForFind3.getName());
            for (Category result: results) {
                System.err.println("result name " + result.getName());
                System.err.println("result desc " + result.getDescription());
                System.err.println("result guid " + result.getSystemAttributes().getGUID());
            }

            throw new SubjectAreaFVTCheckedException("ERROR: Expected 1 back on the find for jjj got " + results.size());
        }
        results = findCategories("iii");
        if (results.size() != 2) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 2 back on the find got " + results.size());
        }
        results = findCategories(null);
        if (results.size() != 6 +existingCategoryCount) {
            throw new SubjectAreaFVTCheckedException("ERROR1: Expected 6 back on the find got " + results.size());
        }

        results = subjectAreaCategory.findAll(userId);
        if (results.size() != 6 + existingCategoryCount) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 6 back on the find got " + results.size());
        }
        //soft delete a category and check it is not found
        deleteCategory(categoryForFind2.getSystemAttributes().getGUID());
        //FVTUtils.validateNode(deleted4);
        results = findCategories("iii");
        if (results.size() != 1) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 1 back on the find for yyy got " + results.size());
        }

        // search for a category with a name with spaces in
        results = findCategories("This is a Category with spaces in name");
        if (results.size() != 1) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 1 back on the find for Category with spaces in name got " + results.size());
        }
        // make sure there is a category with the name
        createCategory(DEFAULT_TEST_CATEGORY_NAME, glossaryGuid);
        Category categoryForUniqueQFN2= createCategory(DEFAULT_TEST_CATEGORY_NAME, glossaryGuid);
        if (categoryForUniqueQFN2 == null || categoryForUniqueQFN2.equals("")) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected qualified name to be set");
        }
        deleteCategory(categoryForFind1.getSystemAttributes().getGUID());
        deleteCategory(categoryForFind3.getSystemAttributes().getGUID());
        deleteCategory(categoryForFind4.getSystemAttributes().getGUID());
        deleteCategory(categoryForUniqueQFN2.getSystemAttributes().getGUID());

        testHierarchyWithSearchCriteria();

    }

    private void testHierarchyWithSearchCriteria() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        System.out.println("Create a glossary");
        Glossary glossary = glossaryFVT.createGlossary(serverName + " " + DEFAULT_TEST_GLOSSARY_NAME2);
        String glossaryGuid = glossary.getSystemAttributes().getGUID();
        System.out.println("Create a aaa");
        Category parentCategory = createCategoryWithGlossaryGuid("aaa", glossary.getSystemAttributes().getGUID());
        String parentGuid = parentCategory.getSystemAttributes().getGUID();

        Set<String> childGuids = new HashSet();
        // create 20 children
        for (int i=0;i<10;i++) {
            Category cat1 = createCategoryWithParentGlossaryGuid("aa" + i, parentGuid, glossaryGuid);
            childGuids.add(cat1.getSystemAttributes().getGUID());
            Category cat2 =createCategoryWithParentGlossaryGuid("bb" + i, parentGuid, glossaryGuid);
            childGuids.add(cat2.getSystemAttributes().getGUID());
            // create a grandchild of cat1 with the same name
            Category grandchild =createCategoryWithParentGlossaryGuid("aa" + i, cat1.getSystemAttributes().getGUID(), glossaryGuid);
            childGuids.add(grandchild.getSystemAttributes().getGUID());
        }

        if (getCategoryChildren(parentGuid).size() != 20) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 20 child categories");
        }
        FindRequest findRequest =new FindRequest();
        findRequest.setSearchCriteria("aa3");
        int count = subjectAreaCategoryClient.getCategoryChildren(userId, parentGuid, findRequest).size();
        if (count !=1) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 1 child category, got " + count);
        }
        count = glossaryFVT.getCategories(glossaryGuid, findRequest, false).size();
        if (count !=2) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 2 glossary categories for aa3 including grandchild, got " + count);
        }
        count = glossaryFVT.getCategories(glossaryGuid, findRequest, true).size();
        if (count !=0) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 0 glossary categories for aa3 not including grandchild, got " + count);
        }

        findRequest.setSearchCriteria("aa.*");
        count = subjectAreaCategoryClient.getCategoryChildren(userId, parentGuid, findRequest).size();
        if (count !=10) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 10 child category, got " + count);
        }
        count = glossaryFVT.getCategories(glossaryGuid, findRequest, false).size();
        if (count !=21) {
            // 1 aaa and its 10 aa1-10 children and then the first category has another 10 aa1-10 children
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 21 glossary categories for aa* including grandchildren, got " + count);
        }
        count = glossaryFVT.getCategories(glossaryGuid, findRequest, true).size();
        if (count !=1) {
            // should find aaa
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 1 glossary categories for aa* not including grandchildren, got " + count);
        }

        // issue with page size 5
        findRequest.setPageSize(5);
        List<Category> categories = subjectAreaCategoryClient.getCategoryChildren(userId, parentGuid, findRequest);
        count = categories.size();
        if (count !=5) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 5 child categories with aa*,got " + count);
        }
        count = glossaryFVT.getCategories(glossaryGuid, findRequest, false).size();
        if (count !=5) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 5 glossary categories for aa* including grandchildren, got " + count);
        }
        count = glossaryFVT.getCategories(glossaryGuid, findRequest, true).size();
        if (count !=1) {
            // expect to find aaa
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 1 glossary categories for aa* not including grandchildren, got " + count);
        }
        // issue with page size 5, startingFrom 5
        findRequest.setStartingFrom(5);

        categories = subjectAreaCategoryClient.getCategoryChildren(userId, parentGuid, findRequest);
        count = categories.size();
        if (count !=5) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 5 child categories with aa (findRequest.setStartingFrom(5)),got " + count);
        }
        count = glossaryFVT.getCategories(glossaryGuid, findRequest, false).size();
        if (count !=5) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 5 glossary categories for aa* (findRequest.setStartingFrom(5) including grandchildren, got " + count);
        }
        count = glossaryFVT.getCategories(glossaryGuid, findRequest, true).size();
        if (count !=0) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 0 glossary categories as startingFrom is higher than the number of elements " + count);
        }

        findRequest.setStartingFrom(0);
        findRequest.setSearchCriteria("bb.*");
        findRequest.setPageSize(20);
        if (subjectAreaCategoryClient.getCategoryChildren(userId, parentGuid, findRequest).size() !=10) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 10 child categories for bb*");
        }
        count = glossaryFVT.getCategories(glossaryGuid, findRequest, false).size();
        if (count !=10) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 10 glossary categories for bb* including grandchildren, got " + count);
        }
        count = glossaryFVT.getCategories(glossaryGuid, findRequest, true).size();
        if (count !=0) {
            // only aaa at the top and we are looking for bb*
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 0 glossary categories for bb* not including grandchildren, got " + count);
        }
        // issue with page size 5, startingFrom 5 check the categorychildren
        findRequest.setPageSize(5);
        count = subjectAreaCategoryClient.getCategoryChildren(userId, parentGuid, findRequest).size();
        if (count !=5) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 5 child categories for bb, got " + count);
        }
        count = glossaryFVT.getCategories(glossaryGuid, findRequest, false).size();
        if (count !=5) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 5 glossary categories for bb* including grandchildren, got " + count);
        }
        // issue with page size 5, startingFrom 5 check the categorychildren
        findRequest.setStartingFrom(5);
        count = subjectAreaCategoryClient.getCategoryChildren(userId, parentGuid, findRequest).size();
        if (count !=5) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 5 child categories for bb, got " + count);
        }
        count = glossaryFVT.getCategories(glossaryGuid, findRequest, false).size();
        if (count !=5) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 5 glossary categories for bb* including grandchildren, got " + count);
        }

        count = glossaryFVT.getCategories(glossaryGuid, findRequest, true).size();
        if (count !=0) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 0 glossary categories for bb* not including grandchildren, got " + count);
        }

        findRequest.setStartingFrom(0);
        findRequest.setPageSize(10);
        count = subjectAreaCategoryClient.getCategoryChildren(userId, parentGuid, findRequest).size();
        if (count !=10) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 10 child categories for bb*, got " + count);
        }
        count = glossaryFVT.getCategories(glossaryGuid, findRequest, false).size();
        if (count !=10) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 10 glossary categories for bb* including grandchildren, got " + count);
        }
        count = glossaryFVT.getCategories(glossaryGuid, findRequest, true).size();
        if (count !=0) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 0 glossary categories for bb* not including grandchildren, got " + count);
        }
        //cleanup
        for (String childGuid: childGuids) {
            deleteCategory(childGuid);
        }
        deleteCategory(parentGuid);
        glossaryFVT.deleteGlossary(glossaryGuid);
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
        Category newCategory =issueCreateCategory(category);
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
        Category newCategory = issueCreateCategory(category);
        FVTUtils.validateNode(newCategory);
        System.out.println("Created Category " + newCategory.getName() + " with userId " + newCategory.getSystemAttributes().getGUID());
        return newCategory;
    }
    /**
     * Create a category associated under a parent category and associate with the named glossary
     *
     * @param categoryName name of the category to create
     * @param parent       category under which to create this category
     * @param glossaryGuid userId of the associated glossary
     * @return created category
     */
    Category createCategoryWithParentGlossary(String categoryName, Category parent, String glossaryGuid) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Category category = new Category();
        category.setName(categoryName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        category.setGlossary(glossarySummary);
        CategorySummary parentCategorysummary = new CategorySummary();
        parentCategorysummary.setGuid(parent.getSystemAttributes().getGUID());
        category.setParentCategory(parentCategorysummary);
        Category newCategory = issueCreateCategory(category);
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
            createdCategoriesSet.add(newCategory.getSystemAttributes().getGUID());
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
            createdCategoriesSet.remove(guid);
            System.out.println("Deleted Category succeeded");
    }
    public Category restoreCategory(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        Category restoredCategory = subjectAreaCategory.restore(this.userId, guid);
        FVTUtils.validateNode(restoredCategory);
        createdCategoriesSet.add(guid);
        System.out.println("restored Category name is " + restoredCategory.getName());
        return restoredCategory;
    }

    public void purgeCategory(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        subjectAreaCategory.purge(this.userId, guid);
        System.out.println("Purge succeeded");
    }

    public List<Relationship> getCategoryRelationships(Category category) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return subjectAreaCategory.getAllRelationships(this.userId, category.getSystemAttributes().getGUID());
    }
    private void deleteRemaining() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, SubjectAreaFVTCheckedException {
      deleteRemainingCategories();
      glossaryFVT.deleteRemainingGlossaries();
    }
    void deleteRemainingCategories() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        Iterator<String> iter =  createdCategoriesSet.iterator();
        while (iter.hasNext()) {
            String guid = iter.next();
            iter.remove();
            deleteCategory(guid);
        }
        List<Category> categories = findCategories(".*");
        if (categories.size() != existingCategoryCount) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + existingCategoryCount  +" Categories to be found, got " + categories.size());
        }
    }
    public List<Category> getCategoryChildren(String categoryGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        return subjectAreaCategoryClient.getCategoryChildren(userId, categoryGuid, new FindRequest());
    }

    public List<Term> getTerms(String categoryGuid)  throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        return subjectAreaCategoryClient.getTerms(userId, categoryGuid, new FindRequest());
    }
    public List<Term> getTerms(String categoryGuid, FindRequest findRequest)  throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        return subjectAreaCategoryClient.getTerms(userId, categoryGuid, findRequest);
    }
}
