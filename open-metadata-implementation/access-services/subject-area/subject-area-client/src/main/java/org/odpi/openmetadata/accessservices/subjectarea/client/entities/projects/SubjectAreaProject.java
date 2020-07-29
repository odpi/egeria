/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.entities.projects;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaEntityClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;

/**
 * The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for projects.
 */
public interface SubjectAreaProject {

    /**
     * @return {@link SubjectAreaEntityClient} for client calls(described in {@link SubjectAreaClient})
     * when working with Project objects
     */
    SubjectAreaEntityClient<Project> project();
}
