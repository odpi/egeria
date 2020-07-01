/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.entities.categories;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.AbstractSubjectAreaEntity;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;

@SubjectAreaNodeClient
public class SubjectAreaCategoryClient extends AbstractSubjectAreaEntity<Category> {
    public SubjectAreaCategoryClient(SubjectAreaRestClient client)
    {
        super(client, SUBJECT_AREA_BASE_URL + "categories");
    }

    @Override
    public Class<Category> type() {
        return Category.class;
    }
}
