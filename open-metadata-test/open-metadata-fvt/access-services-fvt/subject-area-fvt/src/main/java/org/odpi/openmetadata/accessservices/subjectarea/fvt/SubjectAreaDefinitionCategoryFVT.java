/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaEntityClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.categories.SubjectAreaDefinitionClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.io.IOException;

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
    private static SubjectAreaEntityClient<SubjectAreaDefinition> subjectAreaCategory = null;
    private GlossaryFVT glossaryFVT =null;
    private String url = null;
    private String serverName = null;
    private String userId = null;

    public static void main(String args[])
    {
        try
        {
            String url = RunAllFVT.getUrl(args);
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
        SubjectAreaDefinitionCategoryFVT fvt =new SubjectAreaDefinitionCategoryFVT(url,FVTConstants.SERVER_NAME1,FVTConstants.USERID);
        fvt.run();
        SubjectAreaDefinitionCategoryFVT fvt2 =new SubjectAreaDefinitionCategoryFVT(url,FVTConstants.SERVER_NAME2,FVTConstants.USERID);
        fvt2.run();
    }
    public SubjectAreaDefinitionCategoryFVT(String url,String serverName,String userId) throws InvalidParameterException {
        SubjectAreaRestClient client = new SubjectAreaRestClient(serverName, url);
        subjectAreaCategory = new SubjectAreaDefinitionClient(client);
        glossaryFVT = new GlossaryFVT(url,serverName,userId);
        this.url=url;
        this.serverName = serverName;
        this.userId=userId;
    }

    public static void runIt(String url, String serverName, String userId) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        SubjectAreaDefinitionCategoryFVT fvt =new SubjectAreaDefinitionCategoryFVT(url,serverName,userId);
        fvt.run();
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
            gotSubjectAreaDefinition = deleteSubjectAreaDefinition(guid);
            FVTUtils.validateNode( gotSubjectAreaDefinition);
            System.out.println("restore the subjectArea1 ");
            gotSubjectAreaDefinition = restoreSubjectAreaDefinition(guid);
            FVTUtils.validateNode( gotSubjectAreaDefinition);
            System.out.println("Delete the subjectArea1 ");
            gotSubjectAreaDefinition = deleteSubjectAreaDefinition(guid);
            FVTUtils.validateNode( gotSubjectAreaDefinition);
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
            System.out.println("Created SubjectAreaDefinition " + newSubjectAreaDefinition.getName() + " with userId " + newSubjectAreaDefinition.getSystemAttributes().getGUID());
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
        System.out.println("Deleted SubjectAreaDefinition guid is " + guid);
    }

    public void purgeSubjectAreaDefinition(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        subjectAreaCategory.purge(this.userId, guid);
        System.out.println("Purge succeeded");
    }
    public SubjectAreaDefinition restoreSubjectAreaDefinition(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaDefinition restoredSubjectAreaDefinition = subjectAreaCategory.restore(this.userId, guid);
        if (restoredSubjectAreaDefinition != null)
        {
            System.out.println("Deleted SubjectAreaDefinition name is " + restoredSubjectAreaDefinition.getName());
        }
        return restoredSubjectAreaDefinition;
    }
}
