/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.categories.SubjectAreaCategoryClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
    private static SubjectAreaNodeClient<SubjectAreaDefinition> subjectAreaCategory = null;
    private GlossaryFVT glossaryFVT =null;
    private String userId = null;
    private int existingSubjectAreaCount = 0;
    /*
     * Keep track of all the created guids in this set, by adding create and restore guids and removing when deleting.
     * At the end of the test it will delete any remaining guids.
     *
     * Note this FVT is called by other FVTs. Who ever constructs the FVT should run deleteRemainingSubjectAreas
     */
    private Set<String> createdSubjectAreasSet = new HashSet<>();

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
    public static void runWith2Servers(String url) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        runIt(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        runIt(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
    }
    public SubjectAreaDefinitionCategoryFVT(String url,String serverName,String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaRestClient client = new SubjectAreaRestClient(serverName, url);
        subjectAreaCategory = new SubjectAreaCategoryClient<>(client);
        glossaryFVT = new GlossaryFVT(url,serverName,userId);
        this.userId=userId;
        existingSubjectAreaCount = findSubjectAreaDefinitions(".*").size();
        System.out.println("existingSubjectAreaCount " + existingSubjectAreaCount);
    }

    public static void runIt(String url, String serverName, String userId) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        System.out.println("SubjectAreaDefinitionCategoryFVT runIt started");
        SubjectAreaDefinitionCategoryFVT fvt =new SubjectAreaDefinitionCategoryFVT(url,serverName,userId);
        fvt.run();
        fvt.deleteRemaining();
        System.out.println("SubjectAreaDefinitionCategoryFVT runIt stopped");
    }
    public static int getSubjectAreaCount(String url, String serverName, String userId) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, SubjectAreaFVTCheckedException  {
        SubjectAreaDefinitionCategoryFVT fvt = new SubjectAreaDefinitionCategoryFVT(url, serverName, userId);
        return fvt.findSubjectAreaDefinitions(".*").size();
    }

    public void run() throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        System.out.println("Create a glossary");
        Glossary glossary = glossaryFVT.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
        FVTUtils.validateNode(glossary);
        System.out.println("Create a subjectArea1");
        SubjectAreaDefinition subjectArea1  = createSubjectAreaDefinitionWithGlossaryGuid(DEFAULT_TEST_CATEGORY_NAME, glossary.getSystemAttributes().getGUID());
        FVTUtils.validateNode(subjectArea1);
        System.out.println("Create a subjectArea2");
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
            deleteSubjectAreaDefinition(guid);
            //FVTUtils.validateNode( gotSubjectAreaDefinition);
            System.out.println("restore the subjectArea1 ");
            gotSubjectAreaDefinition = restoreSubjectAreaDefinition(guid);
            FVTUtils.validateNode( gotSubjectAreaDefinition);
            System.out.println("Delete the subjectArea1 ");
            deleteSubjectAreaDefinition(guid);
            //FVTUtils.validateNode( gotSubjectAreaDefinition);
            System.out.println("Purge a subjectArea1 ");

            // create subjectArea DEFAULT_TEST_CATEGORY_NAME3 with parent
            System.out.println("Create a subjectArea with a parent subjectArea");
            System.out.println("Create a category with a parent category");

            SubjectAreaDefinition  subjectAreaDefinition3 = createSubjectAreaDefinitionWithParentGlossaryGuid( DEFAULT_TEST_CATEGORY_NAME3, subjectArea2.getSystemAttributes().getGUID(), glossary.getSystemAttributes().getGUID());
            FVTUtils.validateNode(subjectAreaDefinition3);

        }
    }
    private SubjectAreaDefinition createSubjectAreaDefinitionWithParentGlossaryGuid(String subjectAreaName, String parentGuid, String glossaryGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaDefinition subjectArea = new SubjectAreaDefinition();
        subjectArea.setName(subjectAreaName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        subjectArea.setGlossary(glossarySummary);
        CategorySummary parentCategory = new CategorySummary();
        parentCategory.setGuid(parentGuid);
        subjectArea.setParentCategory(parentCategory);
        SubjectAreaDefinition newSubjectAreaDefinition = subjectAreaCategory.create(this.userId, subjectArea);

        if (newSubjectAreaDefinition != null)
        {
            createdSubjectAreasSet.add(newSubjectAreaDefinition.getSystemAttributes().getGUID());
            System.out.println("Created SubjectAreaDefinition " + newSubjectAreaDefinition.getName() + " with glossaryGuid " + newSubjectAreaDefinition.getSystemAttributes().getGUID());
        }
        return newSubjectAreaDefinition;
    }

    public  SubjectAreaDefinition createSubjectAreaDefinitionWithGlossaryGuid(String subjectAreaName, String glossaryGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaDefinition subjectArea = new SubjectAreaDefinition();
        subjectArea.setName(subjectAreaName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        subjectArea.setGlossary(glossarySummary);
        SubjectAreaDefinition newSubjectAreaDefinition = subjectAreaCategory.create(this.userId, subjectArea);
        if (newSubjectAreaDefinition != null)
        {
            createdSubjectAreasSet.add(newSubjectAreaDefinition.getSystemAttributes().getGUID());
            System.out.println("Created SubjectAreaDefinition " + newSubjectAreaDefinition.getName() + " with guid " + newSubjectAreaDefinition.getSystemAttributes().getGUID());
        }
        return newSubjectAreaDefinition;
    }


    public  SubjectAreaDefinition getSubjectAreaDefinitionByGUID(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaDefinition subjectArea = subjectAreaCategory.getByGUID(this.userId, guid);
        if (subjectArea != null)
        {
            System.out.println("Got SubjectAreaDefinition " + subjectArea.getName() + " with userId " + subjectArea.getSystemAttributes().getGUID() + " and status " + subjectArea.getSystemAttributes().getStatus());
        }
        return subjectArea;
    }

    public SubjectAreaDefinition updateSubjectAreaDefinition(String guid, SubjectAreaDefinition subjectArea) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaDefinition updatedSubjectAreaDefinition = subjectAreaCategory.update(this.userId, guid, subjectArea);
        if (updatedSubjectAreaDefinition != null)
        {
            System.out.println("Updated SubjectAreaDefinition name to " + updatedSubjectAreaDefinition.getName());
        }
        return updatedSubjectAreaDefinition;
    }

    public void deleteSubjectAreaDefinition(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
           subjectAreaCategory.delete(this.userId, guid);
           createdSubjectAreasSet.remove(guid);
           System.out.println("Deleted SubjectAreaDefinition guid is " + guid);
    }


    public SubjectAreaDefinition restoreSubjectAreaDefinition(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaDefinition restoredSubjectAreaDefinition = subjectAreaCategory.restore(this.userId, guid);
        if (restoredSubjectAreaDefinition != null)
        {
            createdSubjectAreasSet.add(restoredSubjectAreaDefinition.getSystemAttributes().getGUID());
            System.out.println("restored SubjectAreaDefinition name is " + restoredSubjectAreaDefinition.getName());
        }
        return restoredSubjectAreaDefinition;
    }
    public List<SubjectAreaDefinition> findSubjectAreaDefinitions(String criteria) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        FindRequest findRequest = new FindRequest();
        findRequest.setSearchCriteria(criteria);
        return subjectAreaCategory.find(this.userId, findRequest);
    }
    private void deleteRemaining() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, SubjectAreaFVTCheckedException {
        deleteRemainingSubjectAreas();
        glossaryFVT.deleteRemainingGlossaries();
    }
    void deleteRemainingSubjectAreas() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        Iterator<String> iter =  createdSubjectAreasSet.iterator();
        while (iter.hasNext()) {
            String guid = iter.next();
            iter.remove();
            deleteSubjectAreaDefinition(guid);
        }
        List<SubjectAreaDefinition> subjectAreas = findSubjectAreaDefinitions(".*");
        if (subjectAreas.size() != existingSubjectAreaCount) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + existingSubjectAreaCount + " Subject Area Definitions to be found, got " + subjectAreas.size());
        }
    }
}
