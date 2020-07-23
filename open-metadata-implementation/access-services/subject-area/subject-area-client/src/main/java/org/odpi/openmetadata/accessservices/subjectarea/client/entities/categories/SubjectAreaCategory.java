/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.entities.categories;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaEntityClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;

/**
 * The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for categories.
 */
public interface SubjectAreaCategory {

    /**
     * @return {@link SubjectAreaEntityClient} for client calls(described in {@link SubjectAreaClient})
     * when working with Category objects
     */
    SubjectAreaEntityClient<Category> category();

    /**
     * @return {@link SubjectAreaEntityClient} for client calls(described in {@link SubjectAreaClient})
     * when working with SubjectAreaDefinition objects
     */
    SubjectAreaEntityClient<SubjectAreaDefinition> subjectAreaDefinition();
}
