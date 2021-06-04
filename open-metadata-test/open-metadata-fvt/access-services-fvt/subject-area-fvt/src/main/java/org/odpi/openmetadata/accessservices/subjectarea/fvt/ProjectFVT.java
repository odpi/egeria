/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.projects.SubjectAreaProjectClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.GlossaryProject;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * FVT resource to call subject area project client API
 */
public class ProjectFVT
{
    private static final String DEFAULT_TEST_PROJECT_NAME = "Testproject1";
    private static final String DEFAULT_TEST_PROJECT_NAME2 = "Testproject2";
    private static final String DEFAULT_TEST_PROJECT_NAME3 = "Testproject3";
    private static final String DEFAULT_TEST_PROJECT_NAME4 = "Testproject4";
    private static final String DEFAULT_TEST_PROJECT_NAME5 = "Testproject5";
    private static final String DEFAULT_TEST_PROJECT_NAME6 = "Testproject6";
    private static final String DEFAULT_TEST_PROJECT_NAME7 = "Testproject7";
    private SubjectAreaProjectClient<Project> subjectAreaProject = null;
    private SubjectAreaProjectClient subjectAreaProjectClient= null;

    private GlossaryFVT glossaryFVT =null;
    private TermFVT termFVT =null;
    private RelationshipsFVT relationshipsFVT =null;

    private String serverName = null;
    private String userId = null;
    private int existingProjectCount = 0;
    /*
     * Keep track of all the created guids in this set, by adding create and restore guids and removing when deleting.
     * At the end of the test it will delete any remaining guids.
     *
     * Note this FVT is called by other FVTs. Who ever constructs the FVT should run deleteRemainingProjects.
     */
    private Set<String> createdProjectsSet = new HashSet<>();

    public ProjectFVT(String url, String serverName, String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaRestClient client = new SubjectAreaRestClient(serverName, url);
        subjectAreaProject = new SubjectAreaProjectClient<>(client);
        this.serverName=serverName;
        this.userId=userId;
        existingProjectCount = findProjects(".*").size();
        System.out.println("existingProjectCount " + existingProjectCount);
    }
    public static void runWith2Servers(String url) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        runIt(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        runIt(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
    }
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
        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            System.out.println("ERROR: " + e.getReportedErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        }
    }

    public static void runIt(String url, String serverName, String userId) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        System.out.println("ProjectFVT runIt started");
        ProjectFVT fvt =new ProjectFVT(url,serverName,userId);
        fvt.run();
        fvt.deleteRemainingProjects();
        System.out.println("ProjectFVT runIt stopped");
    }
    public static int getProjectCount(String url, String serverName, String userId) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, SubjectAreaFVTCheckedException  {
        ProjectFVT fvt = new ProjectFVT(url, serverName, userId);
        return fvt.findProjects(".*").size();
    }

    public void run() throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        System.out.println("Create a project");
        Project project = createProject(serverName+" "+DEFAULT_TEST_PROJECT_NAME);
        FVTUtils.validateNode(project);
        Project project2 = createProject(serverName+" "+DEFAULT_TEST_PROJECT_NAME2);
        FVTUtils.validateNode(project2);
        if (getProjectTerms(project.getSystemAttributes().getGUID()).size() != 0) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected no terms to be in the project. Got " +getProjectTerms(project.getSystemAttributes().getGUID()).size());
        }
        List<Project> results = findProjects(null);
        if (results.size() !=2 ) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 2 back on the find got " + results.size());
        }

        Project projectForUpdate = new Project();
        projectForUpdate.setName(serverName+" "+DEFAULT_TEST_PROJECT_NAME3);
        System.out.println("Get the project");
        String guid = project.getSystemAttributes().getGUID();
        Project gotProject = getProjectByGUID(guid);
        FVTUtils.validateNode(gotProject);
        System.out.println("Update the project");
        Project updatedProject = updateProject(guid, projectForUpdate);
        FVTUtils.validateNode(updatedProject);
        System.out.println("Get the project again");
        gotProject = getProjectByGUID(guid);
        FVTUtils.validateNode(gotProject);
        System.out.println("Delete the project");
        deleteProject(guid);
        //FVTUtils.validateNode(gotProject);
        System.out.println("restore the project");
        gotProject = restoreProject(guid);
        FVTUtils.validateNode(gotProject);
        System.out.println("Delete the project again");
        deleteProject(guid);
        //FVTUtils.validateNode(gotProject);
        //TODO - delete a deletedProject should fail
        System.out.println("Purge a project");
        purgeProject(guid);
        System.out.println("Create project with the same name as a deleted one");
        project = createProject(serverName + " " + DEFAULT_TEST_PROJECT_NAME);
        FVTUtils.validateNode(project);

        System.out.println("create projects to find");
        Project projectForFind1 = getProjectForInput(DEFAULT_TEST_PROJECT_NAME7);
        projectForFind1.setQualifiedName(DEFAULT_TEST_PROJECT_NAME6);
        projectForFind1 = issueCreateProject(projectForFind1);
        FVTUtils.validateNode(projectForFind1);
        Project projectForFind2 = createProject(DEFAULT_TEST_PROJECT_NAME6);
        FVTUtils.validateNode(projectForFind2);
        Project projectForFind3 = createProject(DEFAULT_TEST_PROJECT_NAME5);
        FVTUtils.validateNode(projectForFind3);
        Project projectForFind4 = createProject("This is a Project with spaces in name");
        FVTUtils.validateNode(projectForFind4);

        results = findProjects(DEFAULT_TEST_PROJECT_NAME5);
        if (results.size() !=1 ) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 1 back on the find got " +results.size());
        }
        results = findProjects(DEFAULT_TEST_PROJECT_NAME6);
        if (results.size() !=2 ) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 2 back on the find got " +results.size());
        }
        //soft delete a project and check it is not found
        deleteProject(projectForFind2.getSystemAttributes().getGUID());
        //FVTUtils.validateNode(deleted4);
        results = findProjects(DEFAULT_TEST_PROJECT_NAME6);
        if (results.size() !=1 ) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 1 back on the find got " +results.size());
        }

        // search for a project with a name with spaces in
        results = findProjects("This is a Project with spaces in name");
        if (results.size() !=1 ) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 1 back on the find got " +results.size());
        }
        Project projectForGraph = createProject(DEFAULT_TEST_PROJECT_NAME4);
        FindRequest findRequest = new FindRequest();
        List<Term> terms = getProjectTerms(projectForGraph.getSystemAttributes().getGUID(), findRequest);
        if (terms != null && terms.size() > 0) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected null or empty got " +terms.size());
        }
        // make sure there is a project with the name
        createProject( DEFAULT_TEST_PROJECT_NAME);
        Project projectForUniqueQFN2= createProject(DEFAULT_TEST_PROJECT_NAME);
        if (projectForUniqueQFN2 == null || projectForUniqueQFN2.equals("")) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected qualified name to be set");
        }
    }

    public  Project createProject(String projectName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Project project = getProjectForInput(projectName);
        return issueCreateProject(project);
    }

    public Project issueCreateProject(Project project) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Project newProject = subjectAreaProject.create(this.userId, project);
        if (newProject != null)
        {
            createdProjectsSet.add(newProject.getSystemAttributes().getGUID());
            System.out.println("Created Project " + newProject.getName() + " with userId " + newProject.getSystemAttributes().getGUID());
        }
        return newProject;
    }

    public Project getProjectForInput(String projectName) {
        Project project = new Project();
        project.setName(projectName);
        return project;
    }
    public GlossaryProject getGlossaryProjectForInput(String projectName) {
        GlossaryProject glossaryProject= new GlossaryProject();
        glossaryProject.setName(projectName);
        return glossaryProject;
    }

    public List<Project> findProjects(String criteria) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        FindRequest findRequest = new FindRequest();
        findRequest.setSearchCriteria(criteria);
        List<Project> projects = subjectAreaProject.find(this.userId, findRequest);
        return projects;
    }

    public  Project getProjectByGUID(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        Project project = subjectAreaProject.getByGUID(this.userId, guid);
        FVTUtils.validateNode(project);
        System.out.println("Got Project " + project.getName() + " with userId " + project.getSystemAttributes().getGUID() + " and status " + project.getSystemAttributes().getStatus());

        return project;
    }
    public  List<Term> getProjectTerms(String guid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        List<Term> terms = subjectAreaProject.getProjectTerms(this.userId, guid, findRequest);
        System.out.println("Got terms from project with userId " + guid);
        return terms;
    }
    public  Project updateProject(String guid, Project project) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        Project updatedProject = subjectAreaProject.update(this.userId, guid, project);
        FVTUtils.validateNode(updatedProject);
        System.out.println("Updated Project name to " + updatedProject.getName());
        return updatedProject;
    }

    public void deleteProject(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
            subjectAreaProject.delete(this.userId, guid);
            createdProjectsSet.remove(guid);
            System.out.println("Deleted Project succeeded");
    }
    public Project restoreProject(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        Project restoredProject = subjectAreaProject.restore(this.userId, guid);
        FVTUtils.validateNode(restoredProject);
        createdProjectsSet.add(restoredProject.getSystemAttributes().getGUID());
        System.out.println("Restored Project name is " + restoredProject.getName());
        return restoredProject;
    }

    public  void purgeProject(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        subjectAreaProject.purge(this.userId, guid);
        System.out.println("Purge succeeded");
    }
    public List<Relationship> getProjectRelationships(Project project) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return subjectAreaProject.getAllRelationships(this.userId, project.getSystemAttributes().getGUID());
    }

    void deleteRemainingProjects() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        Iterator<String> iter =  createdProjectsSet.iterator();
        while (iter.hasNext()) {
            String guid = iter.next();
            iter.remove();
            deleteProject(guid);
        }
        List<Project> projects = findProjects(".*");
        if (projects.size() !=existingProjectCount) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " + existingProjectCount + " Projects to be found, got " + projects.size());
        }
    }

    public List<Term> getProjectTerms(String projectGuid)  throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        return subjectAreaProject.getProjectTerms(userId, projectGuid, new FindRequest());
    }
}
