/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaProject;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.GlossaryProject;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;

import java.io.IOException;
import java.util.List;

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
    private SubjectAreaProject subjectAreaProject = null;
    private String serverName = null;
    private String userId = null;

    public ProjectFVT(String url, String serverName, String userId) throws InvalidParameterException
    {
        subjectAreaProject = new SubjectAreaImpl(serverName,url).getSubjectAreaProject();
        this.serverName=serverName;
        this.userId=userId;
    }
    public static void runWith2Servers(String url) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException
    {
        ProjectFVT fvt =new ProjectFVT(url,FVTConstants.SERVER_NAME1,FVTConstants.USERID);
        fvt.run();
        // check that a second server will work
        ProjectFVT fvt2 =new ProjectFVT(url,FVTConstants.SERVER_NAME2,FVTConstants.USERID);
        fvt2.run();
    }
    public static void main(String args[])
    {
        try
        {
           String url = RunAllFVT.getUrl(args);
           runWith2Servers(url);

        } catch (IOException e1)
        {
            System.out.println("Error getting user input");
        } catch (SubjectAreaCheckedException e)
        {
            System.out.println("ERROR: " + e.getErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        } catch (SubjectAreaFVTCheckedException e) {
            System.out.println("ERROR: " + e.getMessage() );
        }
    }

    public static void runIt(String url, String serverName, String userId) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException {
        ProjectFVT fvt =new ProjectFVT(url,serverName,userId);
        fvt.run();
    }

    public void run() throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException
    {
        System.out.println("Create a project");
        Project project = createProject(serverName+" "+DEFAULT_TEST_PROJECT_NAME);
        FVTUtils.validateNode(project);
        Project project2 = createProject(serverName+" "+DEFAULT_TEST_PROJECT_NAME2);
        FVTUtils.validateNode(project2);

        List<Project> results = findProjects(null);
        if (results.size() !=2 ) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected 2 back on the find got " +results.size());
        }

        Project projectForUpdate = new Project();
        projectForUpdate.setName(serverName+" "+DEFAULT_TEST_PROJECT_NAME3);
        System.out.println("Get the project");
        String guid = project.getSystemAttributes().getGUID();
        Project gotProject = getProjectByGUID(guid);
        System.out.println("Update the project");
        Project updatedProject = updateProject(guid, projectForUpdate);
        FVTUtils.validateNode(updatedProject);
        System.out.println("Get the project again");
        gotProject = getProjectByGUID(guid);
        FVTUtils.validateNode(gotProject);
        System.out.println("Delete the project");
        gotProject = deleteProject(guid);
        FVTUtils.validateNode(gotProject);
        System.out.println("restore the project");
        gotProject = restoreProject(guid);
        FVTUtils.validateNode(gotProject);
        System.out.println("Delete the project again");
        gotProject = deleteProject(guid);
        FVTUtils.validateNode(gotProject);
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
        Project deleted4 = deleteProject(projectForFind2.getSystemAttributes().getGUID());
        FVTUtils.validateNode(deleted4);
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
        List<Term> terms = getProjectTerms(projectForGraph.getSystemAttributes().getGUID());
        if (terms != null ) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected null got " +terms.size());
        }
    }

    public  Project createProject(String projectName) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException
    {
        Project project = getProjectForInput(projectName);
        return issueCreateProject(project);
    }

    public Project issueCreateProject(Project project) throws MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException, UnrecognizedGUIDException {
        Project newProject = subjectAreaProject.createProject(this.userId, project);
        if (newProject != null)
        {
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

    public List<Project> findProjects(String criteria) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException
    {
        List<Project> projects = subjectAreaProject.findProject(
                this.userId,
                criteria,
                null,
                0,
                0,
                null,
                null);
        return projects;
    }

    public  Project getProjectByGUID(String guid) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException {
        Project project = subjectAreaProject.getProjectByGuid(this.userId, guid);
        FVTUtils.validateNode(project);
        System.out.println("Got Project " + project.getName() + " with userId " + project.getSystemAttributes().getGUID() + " and status " + project.getSystemAttributes().getStatus());

        return project;
    }
    public  List<Term> getProjectTerms(String guid) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException {
        List<Term> terms = subjectAreaProject.getProjectTerms(this.userId, guid,null);
        System.out.println("Got terms from project with userId " + guid);
        return terms;
    }
    public  Project updateProject(String guid, Project project) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException
    {
        Project updatedProject = subjectAreaProject.updateProject(this.userId, guid, project);
        FVTUtils.validateNode(updatedProject);
        System.out.println("Updated Project name to " + updatedProject.getName());
        return updatedProject;
    }

    public Project deleteProject(String guid) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException
    {
        Project deletedProject = subjectAreaProject.deleteProject(this.userId, guid);
        FVTUtils.validateNode(deletedProject);
        System.out.println("Deleted Project name is " + deletedProject.getName());
        return deletedProject;
    }
    public Project restoreProject(String guid) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException
    {
        Project restoredProject = subjectAreaProject.restoreProject(this.userId, guid);
        FVTUtils.validateNode(restoredProject);
        System.out.println("Restored Project name is " + restoredProject.getName());
        return restoredProject;
    }

    public  void purgeProject(String guid) throws SubjectAreaCheckedException, SubjectAreaFVTCheckedException
    {
        subjectAreaProject.purgeProject(this.userId, guid);
        System.out.println("Purge succeeded");
    }
    public List<Line> getProjectRelationships(Project project) throws UserNotAuthorizedException, UnexpectedResponseException, InvalidParameterException, FunctionNotSupportedException, MetadataServerUncontactableException {
        return subjectAreaProject.getProjectRelationships(this.userId,
                project.getSystemAttributes().getGUID(),
                null,
                0,
                0,
                null,
                null);
    }
}
