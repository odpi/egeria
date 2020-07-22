/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.nodes.categories;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;

/**
 * The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for categories.
 */
public interface SubjectAreaCategory {

    /**
     * @return {@link SubjectAreaNodeClient} for client calls(described in {@link SubjectAreaClient})
     * when working with Category objects
     */
    SubjectAreaNodeClient<Category> category();

    /**
     * @return {@link SubjectAreaNodeClient} for client calls(described in {@link SubjectAreaClient})
     * when working with SubjectAreaDefinition objects
     */
    SubjectAreaNodeClient<SubjectAreaDefinition> subjectAreaDefinition();
}
