/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewAttachmentRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootHierarchy;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.CampaignProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ExperimentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.GlossaryProjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.GovernanceProjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.InvestigationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.PersonalProjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectKindProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.StudyProjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.TaskProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.FindProjectClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ProjectManagerFVT covers every request of the Project Manager OMVS: the project lifecycle (create, create
 * with a classification, create a task, create from a template, update, find, delete), project teams,
 * hierarchies and dependencies, the project classifications and the project graph and hierarchy views.
 * <br>
 * The team member is a person created with the Actor Manager.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProjectManagerFVT
{
    private static final ViewServiceClient projectManager = new ViewServiceClient(ViewServiceDescription.PROJECT_MANAGER);
    private static final ViewServiceClient actorManager   = new ViewServiceClient(ViewServiceDescription.ACTOR_MANAGER);

    private static final List<String> projectsToDelete = new ArrayList<>();

    private static String projectGUID      = null;
    private static String campaignGUID     = null;
    private static String otherProjectGUID = null;
    private static String personGUID       = null;


    /**
     * A project can be created, retrieved with its properties, updated, and found by name and search string.
     * A project created with a classification name carries that classification and is returned by
     * getClassifiedProjects.
     */
    @Test
    @Order(1)
    void projectLifecycle()
    {
        ProjectProperties properties = projectProperties("main");

        properties.setMission("Prove the project manager works");
        properties.setProjectStatus("Active");

        projectGUID = createProject(properties, null);

        ProjectProperties retrieved = assertInstanceOf(ProjectProperties.class, getProject(projectGUID).getProperties());

        assertEquals(properties.getQualifiedName(), retrieved.getQualifiedName());
        assertEquals("Prove the project manager works", retrieved.getMission());

        ProjectProperties update = new ProjectProperties();

        update.setProjectPhase("Testing");

        UpdateElementRequestBody updateRequestBody = new UpdateElementRequestBody();

        updateRequestBody.setMergeUpdate(true);
        updateRequestBody.setProperties(update);

        assertTrue(projectManager.post("/projects/" + projectGUID + "/update", updateRequestBody, BooleanResponse.class).getFlag(),
                   "updateProject reported that nothing changed");

        retrieved = assertInstanceOf(ProjectProperties.class, getProject(projectGUID).getProperties());

        assertEquals("Testing", retrieved.getProjectPhase());
        assertEquals("Prove the project manager works", retrieved.getMission(), "a merge update should keep the mission");

        campaignGUID     = createProject(projectProperties("campaign"), OpenMetadataType.CAMPAIGN_CLASSIFICATION.typeName);
        otherProjectGUID = createProject(projectProperties("other"), null);

        assertTrue(RelatedElementChecks.classificationNames(getProject(campaignGUID).getElementHeader()).contains(OpenMetadataType.CAMPAIGN_CLASSIFICATION.typeName),
                   "a project created with the Campaign classification name is not a campaign");

        FilterRequestBody byClassification = new FilterRequestBody();

        byClassification.setFilter(OpenMetadataType.CAMPAIGN_CLASSIFICATION.typeName);

        List<OpenMetadataRootElement> campaigns = projectManager.elements("/projects/by-classifications", byClassification);

        assertTrue(RelatedElementChecks.contains(campaigns, campaignGUID), "getClassifiedProjects did not return the campaign");
        assertFalse(RelatedElementChecks.contains(campaigns, projectGUID), "getClassifiedProjects returned a project that is not a campaign");

        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(properties.getQualifiedName());

        List<OpenMetadataRootElement> byNameResults = projectManager.elements("/projects/by-name", byName);

        assertTrue(RelatedElementChecks.contains(byNameResults, projectGUID), "getProjectsByName did not return the project");
        assertFalse(RelatedElementChecks.contains(byNameResults, otherProjectGUID), "getProjectsByName returned a project with another name");

        SearchStringRequestBody search = new SearchStringRequestBody();

        search.setSearchString(ViewServicesFvtTestSupport.runSearchString());

        List<OpenMetadataRootElement> found = projectManager.elements("/projects/by-search-string", search);

        assertTrue(RelatedElementChecks.contains(found, projectGUID), "findProjects did not return the project");
        assertTrue(RelatedElementChecks.contains(found, campaignGUID), "findProjects did not return the campaign");
    }


    /**
     * A task created for a project is a Task-classified project managed by the project.  A project created from
     * a template takes its properties with the placeholder in its qualified name replaced.
     */
    @Test
    @Order(2)
    void tasksAndTemplates()
    {
        NewAttachmentRequestBody taskRequestBody = new NewAttachmentRequestBody();

        taskRequestBody.setProperties(projectProperties("task"));

        String taskGUID = projectManager.create("/projects/" + projectGUID + "/task", taskRequestBody);

        projectsToDelete.add(taskGUID);

        assertTrue(RelatedElementChecks.classificationNames(getProject(taskGUID).getElementHeader()).contains(OpenMetadataType.TASK_CLASSIFICATION.typeName),
                   "createTaskProject did not create a Task");
        assertTrue(RelatedElementChecks.isLinked(getProject(projectGUID), OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName, taskGUID),
                   "the project does not manage its new task");

        ProjectProperties templateProperties = projectProperties("template");

        templateProperties.setQualifiedName(templateProperties.getQualifiedName() + "::~{projectName}~");
        templateProperties.setMission("Mission from the template");

        String templateGUID = createProject(templateProperties, null);

        TemplateRequestBody templateRequestBody = new TemplateRequestBody();

        templateRequestBody.setTemplateGUID(templateGUID);
        templateRequestBody.setIsOwnAnchor(true);
        templateRequestBody.setPlaceholderPropertyValues(Map.of("projectName", "copy1"));

        String copyGUID = projectManager.create("/projects/from-template", templateRequestBody);

        projectsToDelete.add(copyGUID);

        assertNotEquals(templateGUID, copyGUID);

        ProjectProperties copy = assertInstanceOf(ProjectProperties.class, getProject(copyGUID).getProperties());

        assertEquals(templateProperties.getQualifiedName().replace("~{projectName}~", "copy1"), copy.getQualifiedName());
        assertEquals("Mission from the template", copy.getMission());
    }


    /**
     * A person added to the project team is listed in the team until removed.
     */
    @Test
    @Order(3)
    void projectTeam()
    {
        PersonProperties personProperties = new PersonProperties();

        personProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Person", "project-manager"));
        personProperties.setDisplayName("Project manager FVT person");

        NewElementRequestBody personRequestBody = new NewElementRequestBody();

        personRequestBody.setIsOwnAnchor(true);
        personRequestBody.setProperties(personProperties);

        personGUID = actorManager.create("/actor-profiles", personRequestBody);

        AssignmentScopeProperties assignmentScopeProperties = new AssignmentScopeProperties();

        assignmentScopeProperties.setAssignmentType("Contributor");

        NewRelationshipRequestBody attach = new NewRelationshipRequestBody();

        attach.setProperties(assignmentScopeProperties);

        projectManager.call("/projects/" + projectGUID + "/members/" + personGUID + "/attach", attach);

        assertTrue(RelatedElementChecks.contains(projectManager.elements("/projects/" + projectGUID + "/team", new FilterRequestBody()), personGUID),
                   "getProjectTeam does not include the new member");

        projectManager.call("/projects/" + projectGUID + "/members/" + personGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.contains(projectManager.elements("/projects/" + projectGUID + "/team", new FilterRequestBody()), personGUID),
                    "getProjectTeam still includes the removed member");
    }


    /**
     * A managed project is linked to its manager, appears in the manager's hierarchy, graph and linked projects,
     * and a dependency links the two projects; both relationships are gone once cleared.
     */
    @Test
    @Order(4)
    void hierarchiesAndDependencies()
    {
        ProjectHierarchyProperties hierarchyProperties = new ProjectHierarchyProperties();

        hierarchyProperties.setLabel("Sub-project");

        NewRelationshipRequestBody hierarchyRequest = new NewRelationshipRequestBody();

        hierarchyRequest.setProperties(hierarchyProperties);

        projectManager.call("/projects/" + projectGUID + "/project-hierarchies/" + otherProjectGUID + "/attach", hierarchyRequest);

        assertTrue(RelatedElementChecks.isLinked(getProject(projectGUID), OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName, otherProjectGUID),
                   "the project does not show the project it manages");
        assertTrue(RelatedElementChecks.contains(projectManager.elements("/metadata-elements/" + projectGUID + "/projects", new FilterRequestBody()), otherProjectGUID),
                   "getLinkedProjects does not include the managed project");

        OpenMetadataRootElement hierarchy = projectManager.element("/projects/" + projectGUID + "/hierarchy", new ResultsRequestBody());

        assertNotNull(hierarchy, "getProjectHierarchy returned nothing");
        assertEquals(projectGUID, hierarchy.getElementHeader().getGUID());

        assertInstanceOf(OpenMetadataRootHierarchy.class, hierarchy);

        /*
         * The content of the hierarchy is checked by projectHierarchyIncludesManagedProjects.
         */

        OpenMetadataRootElement graph = projectManager.element("/projects/" + projectGUID + "/graph", new ResultsRequestBody());

        assertNotNull(graph, "getProjectGraph returned nothing");
        assertEquals(projectGUID, graph.getElementHeader().getGUID());
        assertTrue(RelatedElementChecks.isLinked(graph, OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName, otherProjectGUID),
                   "the project graph does not show the managed project");

        projectManager.call("/projects/" + projectGUID + "/project-hierarchies/" + otherProjectGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.isLinked(getProject(projectGUID), OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName, otherProjectGUID),
                    "the project still shows the project it no longer manages");

        ProjectDependencyProperties dependencyProperties = new ProjectDependencyProperties();

        dependencyProperties.setLabel("Needs results");

        NewRelationshipRequestBody dependencyRequest = new NewRelationshipRequestBody();

        dependencyRequest.setProperties(dependencyProperties);

        projectManager.call("/projects/" + projectGUID + "/project-dependencies/" + otherProjectGUID + "/attach", dependencyRequest);

        assertTrue(RelatedElementChecks.isLinked(getProject(projectGUID), OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP.typeName, otherProjectGUID),
                   "the project does not show the project it depends on");

        projectManager.call("/projects/" + projectGUID + "/project-dependencies/" + otherProjectGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.isLinked(getProject(projectGUID), OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP.typeName, otherProjectGUID),
                    "the project still shows the cleared dependency");
    }


    /**
     * The project hierarchy lists the projects that the project manages.
     */
    @Test
    @Order(5)
    void projectHierarchyIncludesManagedProjects()
    {
        projectManager.call("/projects/" + projectGUID + "/project-hierarchies/" + otherProjectGUID + "/attach", new NewRelationshipRequestBody());

        try
        {
            OpenMetadataRootHierarchy rootHierarchy = assertInstanceOf(OpenMetadataRootHierarchy.class,
                                                                       projectManager.element("/projects/" + projectGUID + "/hierarchy", new ResultsRequestBody()));

            assertNotNull(rootHierarchy.getOpenMetadataRootHierarchies(), "the project hierarchy has no managed projects");
            assertTrue(rootHierarchy.getOpenMetadataRootHierarchies().stream().anyMatch(nested -> otherProjectGUID.equals(nested.getElementHeader().getGUID())),
                       "the project hierarchy does not include the managed project");
        }
        finally
        {
            projectManager.call("/projects/" + projectGUID + "/project-hierarchies/" + otherProjectGUID + "/detach", new DeleteRelationshipRequestBody());
        }
    }


    /**
     * A project classification's properties can be searched for, and the classification is gone once cleared.
     */
    @Test
    @Order(6)
    void projectClassificationProperties()
    {
        String approach = ViewServicesFvtTestSupport.qualifiedName("Approach", "agile");

        ProjectClassificationProperties properties = new ProjectClassificationProperties();

        properties.setApproach(approach);
        properties.setManagementStyle("Self-organizing");

        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setProperties(properties);

        projectManager.call("/projects/" + projectGUID + "/classification-properties/classify", requestBody);

        assertTrue(RelatedElementChecks.classificationNames(getProject(projectGUID).getElementHeader())
                                       .contains(OpenMetadataType.PROJECT_CLASSIFICATION_CLASSIFICATION.typeName),
                   "addProjectClassification did not classify the project");

        FindProjectClassificationProperties query = new FindProjectClassificationProperties();

        query.setApproach(approach);

        List<OpenMetadataRootElement> found = projectManager.elements("/projects/by-classification-properties", query);

        assertTrue(RelatedElementChecks.contains(found, projectGUID), "getProjectsByClassificationProperties did not return the project");
        assertFalse(RelatedElementChecks.contains(found, otherProjectGUID), "getProjectsByClassificationProperties returned an unclassified project");

        projectManager.call("/projects/" + projectGUID + "/classification-properties/declassify", new DeleteClassificationRequestBody());

        assertFalse(RelatedElementChecks.classificationNames(getProject(projectGUID).getElementHeader())
                                        .contains(OpenMetadataType.PROJECT_CLASSIFICATION_CLASSIFICATION.typeName),
                    "clearProjectClassification did not declassify the project");
        assertFalse(RelatedElementChecks.contains(projectManager.elements("/projects/by-classification-properties", query), projectGUID),
                    "getProjectsByClassificationProperties still returns the declassified project");
    }


    /**
     * Each project kind classification shows on the project once set and is gone once cleared.
     */
    @Test
    @Order(7)
    void projectKindClassifications()
    {
        classifyAndClear("/campaign", new CampaignProperties(), OpenMetadataType.CAMPAIGN_CLASSIFICATION.typeName);
        classifyAndClear("/task-classification", new TaskProperties(), OpenMetadataType.TASK_CLASSIFICATION.typeName);
        classifyAndClear("/personal-project", new PersonalProjectProperties(), OpenMetadataType.PERSONAL_PROJECT_CLASSIFICATION.typeName);
        classifyAndClear("/study-project", new StudyProjectProperties(), OpenMetadataType.STUDY_PROJECT_CLASSIFICATION.typeName);
        classifyAndClear("/experiment", new ExperimentProperties(), OpenMetadataType.EXPERIMENT_CLASSIFICATION.typeName);
        classifyAndClear("/investigation", new InvestigationProperties(), OpenMetadataType.INVESTIGATION_CLASSIFICATION.typeName);
        classifyAndClear("/glossary-project", new GlossaryProjectProperties(), OpenMetadataType.GLOSSARY_PROJECT_CLASSIFICATION.typeName);
        classifyAndClear("/governance-project", new GovernanceProjectProperties(), OpenMetadataType.GOVERNANCE_PROJECT_CLASSIFICATION.typeName);
        classifyAndClear("/project-kind", new ProjectKindProperties(), OpenMetadataType.PROJECT_KIND_CLASSIFICATION.typeName);
    }


    /**
     * Every project can be deleted, after which it can no longer be retrieved.
     */
    @Test
    @Order(99)
    void deleteProjects()
    {
        /*
         * Newest first: the task is anchored to the main project, so it must go before the project does.
         */
        java.util.Collections.reverse(projectsToDelete);

        for (String guid : projectsToDelete)
        {
            projectManager.call("/projects/" + guid + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(projectManager, "/projects/" + guid);
        }

        projectsToDelete.clear();
    }


    @AfterAll
    static void deletePerson()
    {
        if (personGUID != null)
        {
            actorManager.call("/actor-profiles/" + personGUID + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(actorManager, "/actor-profiles/" + personGUID + "/retrieve");
            personGUID = null;
        }
    }


    private void classifyAndClear(String                       path,
                                  ClassificationBeanProperties properties,
                                  String                       classificationName)
    {
        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setProperties(properties);

        projectManager.call("/projects/" + otherProjectGUID + path, requestBody);

        assertTrue(RelatedElementChecks.classificationNames(getProject(otherProjectGUID).getElementHeader()).contains(classificationName),
                   path + " did not add " + classificationName);

        projectManager.call("/projects/" + otherProjectGUID + path + "/remove", new DeleteClassificationRequestBody());

        assertFalse(RelatedElementChecks.classificationNames(getProject(otherProjectGUID).getElementHeader()).contains(classificationName),
                    path + "/remove did not remove " + classificationName);
    }


    private static ProjectProperties projectProperties(String name)
    {
        ProjectProperties properties = new ProjectProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Project", name));
        properties.setDisplayName("Project manager FVT " + name);

        return properties;
    }


    private static String createProject(ProjectProperties properties,
                                        String            classificationName)
    {
        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        String path = (classificationName == null) ? "/projects" : "/projects?classificationName=" + classificationName;
        String guid = projectManager.create(path, requestBody);

        projectsToDelete.add(guid);

        return guid;
    }


    private static OpenMetadataRootElement getProject(String guid)
    {
        OpenMetadataRootElement element = projectManager.element("/projects/" + guid, new GetRequestBody());

        assertNotNull(element, "project " + guid + " is missing");

        return element;
    }
}
