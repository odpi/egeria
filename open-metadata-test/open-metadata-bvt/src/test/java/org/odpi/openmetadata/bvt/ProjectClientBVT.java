/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.bvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ProjectClient;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ProjectClientBVT exercises the create/get/update/find/delete lifecycle of
 * {@link ProjectClient}, one of the connector context clients, against the running BVT server.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ProjectClientBVT
{
    @Test
    void projectLifecycle() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        ProjectClient         projectClient    = connectorContext.getProjectClient();

        String qualifiedName = "open-metadata-bvt:Project:" + UUID.randomUUID();

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        ProjectProperties createProperties = new ProjectProperties();
        createProperties.setQualifiedName(qualifiedName);
        createProperties.setDisplayName("BVT Project");
        createProperties.setDescription("Created by the open-metadata-bvt build verification test suite");

        String projectGUID = projectClient.createProject(newElementOptions, null, null, createProperties, null);

        assertNotNull(projectGUID, "createProject should return a GUID");

        OpenMetadataRootElement retrievedElement = projectClient.getProjectByGUID(projectGUID, new GetOptions());

        assertNotNull(retrievedElement, "getProjectByGUID should find the project that was just created");
        assertEquals(projectGUID, retrievedElement.getElementHeader().getGUID());
        assertEquals(qualifiedName, ((ProjectProperties) retrievedElement.getProperties()).getQualifiedName());
        assertEquals("BVT Project", ((ProjectProperties) retrievedElement.getProperties()).getDisplayName());

        ProjectProperties updateProperties = new ProjectProperties();
        updateProperties.setQualifiedName(qualifiedName);
        updateProperties.setDisplayName("BVT Project (updated)");
        updateProperties.setDescription("Updated by the open-metadata-bvt build verification test suite");

        boolean updateOccurred = projectClient.updateProject(projectGUID, new UpdateOptions(), updateProperties);

        assertTrue(updateOccurred, "updateProject should report that an update occurred");

        OpenMetadataRootElement updatedElement = projectClient.getProjectByGUID(projectGUID, new GetOptions());

        assertEquals("BVT Project (updated)", ((ProjectProperties) updatedElement.getProperties()).getDisplayName());

        List<OpenMetadataRootElement> foundElements = projectClient.findProjects(qualifiedName, new SearchOptions());

        assertNotNull(foundElements, "findProjects should return a (possibly empty) list, not null");
        assertTrue(foundElements.stream().anyMatch(element -> projectGUID.equals(element.getElementHeader().getGUID())),
                   "findProjects should find the project by its qualified name");

        projectClient.deleteProject(projectGUID, new DeleteOptions());

        assertThrows(Exception.class,
                     () -> projectClient.getProjectByGUID(projectGUID, new GetOptions()),
                     "getProjectByGUID should no longer find the project after it has been deleted");
    }
}
