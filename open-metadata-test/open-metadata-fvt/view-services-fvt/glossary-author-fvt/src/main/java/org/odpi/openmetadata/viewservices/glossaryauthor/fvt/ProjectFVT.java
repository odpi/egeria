/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.fvt;

import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.GlossaryAuthorViewRestClient;
import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.project.GlossaryAuthorViewProjectClient;
import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.relationships.GlossaryAuthorViewRelationshipsClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.GlossaryProject;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ProjectScope;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GenericResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * FVT resource to call Glossary Author View project API
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
    private static final String DEFAULT_TEST_PROJECT_NAME8 = "Testproject8";
    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Test Glossary for project FVT";
    private static final String DEFAULT_TEST_TERM_NAME = "Test term A1";
    private static final String PROJECT_SCOPE = "project-scopes";
    private static Logger log = LoggerFactory.getLogger(ProjectFVT.class);


    //  private SubjectAreaProjectClient<Project> subjectAreaProject = null;
    private GlossaryAuthorViewProjectClient glossaryAuthorViewProjectClient = null;
    private GlossaryAuthorViewRelationshipsClient glossaryAuthorViewRelationshipsClient = null;
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
        GlossaryAuthorViewRestClient client = new GlossaryAuthorViewRestClient(serverName, url);
        glossaryAuthorViewProjectClient = new GlossaryAuthorViewProjectClient(client);
         glossaryAuthorViewRelationshipsClient =
                            new GlossaryAuthorViewRelationshipsClient(client);
        glossaryFVT = new GlossaryFVT(url, serverName, userId);
        termFVT = new TermFVT(url, serverName, userId);

        this.serverName=serverName;
        this.userId=userId;
        existingProjectCount = findProjects("").size();
        if (log.isDebugEnabled()) {
            System.out.println("existingProjectCount " + existingProjectCount);
        }
    }
    public static void runWith2Servers(String url) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
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
        } catch (GlossaryAuthorFVTCheckedException e) {
            log.error("ERROR: " + e.getMessage() );
        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            log.error("ERROR: " + e.getReportedErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        }
    }

    public static void runIt(String url, String serverName, String userId) throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        try {
            System.out.println("ProjectFVT runIt started");
            ProjectFVT fvt = new ProjectFVT(url, serverName, userId);
            fvt.run();
            fvt.termFVT.deleteRemainingTerms();
            fvt.glossaryFVT.deleteRemainingGlossaries();
            fvt.deleteRemainingProjects();
            System.out.println("ProjectFVT runIt stopped");
        }
        catch (Exception error) {
            log.error("The FVT Encountered an Exception", error);
            throw error;
        }
    }
    public static int getProjectCount(String url, String serverName, String userId) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, GlossaryAuthorFVTCheckedException {
        ProjectFVT fvt = new ProjectFVT(url, serverName, userId);
        return fvt.findProjects(".*").size();
    }

    public void run() throws GlossaryAuthorFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        if (log.isDebugEnabled()) {
            log.debug("Create a project");
        }
        Project project = createProject(serverName+" "+DEFAULT_TEST_PROJECT_NAME);
        FVTUtils.validateNode(project);
        Project project2 = createProject(serverName+" "+DEFAULT_TEST_PROJECT_NAME2);
        FVTUtils.validateNode(project2);

        List<Project> results = findProjects(null);
        if (results.size() !=2 ) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 2 back on the find got " + results.size());
        }

        Project projectForUpdate = new Project();
        projectForUpdate.setName(serverName+" "+DEFAULT_TEST_PROJECT_NAME3);
        if (log.isDebugEnabled()) {
            log.debug("Get the project");
        }
        String guid = project.getSystemAttributes().getGUID();
        Project gotProject = getProjectByGUID(guid);
        FVTUtils.validateNode(gotProject);
        if (log.isDebugEnabled()) {
            log.debug("Update the project");
        }
        Project updatedProject = updateProject(guid, projectForUpdate);
        FVTUtils.validateNode(updatedProject);
        if (log.isDebugEnabled()) {
            log.debug("Get the project again");
        }
        gotProject = getProjectByGUID(guid);
        FVTUtils.validateNode(gotProject);
        if (log.isDebugEnabled()) {
            log.debug("Delete the project");
        }
        deleteProject(guid);
        //FVTUtils.validateNode(gotProject);
        if (log.isDebugEnabled()) {
            log.debug("restore the project");
        }
        gotProject = restoreProject(guid);
        FVTUtils.validateNode(gotProject);
        if (log.isDebugEnabled()) {
            log.debug("Delete the project again");
        }
        deleteProject(guid);
        //FVTUtils.validateNode(gotProject);
        if (log.isDebugEnabled()) {
            log.debug("Create project with the same name as a deleted one");
        }
        project = createProject(serverName + " " + DEFAULT_TEST_PROJECT_NAME);
        FVTUtils.validateNode(project);

        if (log.isDebugEnabled()) {
            log.debug("create projects to find");
        }
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
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 1 back on the find got " +results.size());
        }
        results = findProjects(DEFAULT_TEST_PROJECT_NAME6);
        if (results.size() !=2 ) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 2 back on the find got " +results.size());
        }
        //soft delete a project and check it is not found
        deleteProject(projectForFind2.getSystemAttributes().getGUID());
        //FVTUtils.validateNode(deleted4);
        results = findProjects(DEFAULT_TEST_PROJECT_NAME6);
        if (results.size() !=1 ) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 1 back on the find got " +results.size());
        }

        // search for a project with a name with spaces in
        results = findProjects("This is a Project with spaces in name");
        if (results.size() !=1 ) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 1 back on the find got " +results.size());
        }

        // make sure there is a project with the name
        createProject( DEFAULT_TEST_PROJECT_NAME);
        Project projectForUniqueQFN2= createProject(DEFAULT_TEST_PROJECT_NAME);
        if (projectForUniqueQFN2 == null || projectForUniqueQFN2.getQualifiedName().length() == 0) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected qualified name to be set");
        }

        //create relationhip for a project and retriev it
        Project project8 = createProject(serverName+" "+DEFAULT_TEST_PROJECT_NAME8);
        Glossary glossary = glossaryFVT.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
        if (log.isDebugEnabled()) {
            log.debug("Create a term called " + DEFAULT_TEST_TERM_NAME + " using glossary GUID");
        }
        String glossaryGuid = glossary.getSystemAttributes().getGUID();
        Term term1 = termFVT.createTerm(DEFAULT_TEST_TERM_NAME, glossaryGuid);
        if (log.isDebugEnabled()) {
            log.debug("Create a project scope relationship between called " + DEFAULT_TEST_TERM_NAME + " and " + DEFAULT_TEST_PROJECT_NAME8);
        }
        createProjectScope(project8,term1);

        List<Relationship> prjRelationships = getProjectRelationships(project8);
//        System.out.println(prjRelationships.toString());

        if (prjRelationships.size() != 1) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected 1 relationship on project got " + prjRelationships.size());
        }
    }

    public  Project createProject(String projectName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Project project = getProjectForInput(projectName);
        return issueCreateProject(project);
    }

    public Project issueCreateProject(Project project) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Project newProject = glossaryAuthorViewProjectClient.create(this.userId, project);
        if (newProject != null)
        {
            createdProjectsSet.add(newProject.getSystemAttributes().getGUID());
            if (log.isDebugEnabled()) {
                log.debug("Created Project " + newProject.getName() + " with userId " + newProject.getSystemAttributes().getGUID());
            }
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
        List<Project> projects = glossaryAuthorViewProjectClient.find(this.userId, findRequest,false,true);
        return projects;
    }

    public  Project getProjectByGUID(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException {
        Project project = glossaryAuthorViewProjectClient.getByGUID(this.userId, guid);
        FVTUtils.validateNode(project);
        if (log.isDebugEnabled()) {
            log.debug("Got Project " + project.getName() + " with userId " + project.getSystemAttributes().getGUID() + " and status " + project.getSystemAttributes().getStatus());
        }

        return project;
    }

    public  Project updateProject(String guid, Project project) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException {
        Project updatedProject = glossaryAuthorViewProjectClient.update(this.userId, guid, project,false);
        FVTUtils.validateNode(updatedProject);
        if (log.isDebugEnabled()) {
            log.debug("Updated Project name to " + updatedProject.getName());
        }
        return updatedProject;
    }

    public void deleteProject(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        glossaryAuthorViewProjectClient.delete(this.userId, guid);
            createdProjectsSet.remove(guid);
        if (log.isDebugEnabled()) {
            log.debug("Deleted Project succeeded");
        }
    }
    public Project restoreProject(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException {
        Project restoredProject = glossaryAuthorViewProjectClient.restore(this.userId, guid);
        FVTUtils.validateNode(restoredProject);
        createdProjectsSet.add(restoredProject.getSystemAttributes().getGUID());
        if (log.isDebugEnabled()) {
            log.debug("Restored Project name is " + restoredProject.getName());
        }
        return restoredProject;
    }

    public List<Relationship> getProjectRelationships(Project project) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return glossaryAuthorViewProjectClient.getAllRelationships(this.userId, project.getSystemAttributes().getGUID());
    }

    protected ProjectScope createProjectScope(Project project, Term term) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException {
        ProjectScope projectScope = new ProjectScope();
        projectScope.getEnd1().setNodeGuid(project.getSystemAttributes().getGUID());
        projectScope.getEnd2().setNodeGuid(term.getSystemAttributes().getGUID());

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, ProjectScope.class);
        ParameterizedTypeReference<GenericResponse<ProjectScope>> type = ParameterizedTypeReference.forType(resolvableType.getType());


        ProjectScope createdProjectScope = glossaryAuthorViewRelationshipsClient.createRel(this.userId, projectScope,type, PROJECT_SCOPE);
        FVTUtils.validateRelationship(createdProjectScope);
        return createdProjectScope;
    }

    void deleteRemainingProjects() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, GlossaryAuthorFVTCheckedException {

        Iterator<String> iter =  createdProjectsSet.iterator();
        while (iter.hasNext()) {
            String guid = iter.next();
            iter.remove();
            deleteProject(guid);
        }
        List<Project> projects = findProjects("");
        if (projects.size() !=existingProjectCount) {
            throw new GlossaryAuthorFVTCheckedException("ERROR: Expected " + existingProjectCount + " Projects to be found, got " + projects.size());
        }
    }
}
