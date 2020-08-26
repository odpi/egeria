/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.nodes.categories;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.AbstractSubjectAreaNode;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;

@SubjectAreaNodeClient
public class SubjectAreaDefinitionClient extends AbstractSubjectAreaNode<SubjectAreaDefinition> {

    public SubjectAreaDefinitionClient(SubjectAreaRestClient client) {
        super(client, SUBJECT_AREA_BASE_URL + "categories");
    }
}
