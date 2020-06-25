/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.client.AbstractSubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;

import java.util.List;


public abstract class AbstractSubjectAreaRelationship<L extends Line> extends AbstractSubjectArea<L> implements SubjectAreaRelationshipClient<L> {
    protected AbstractSubjectAreaRelationship(SubjectAreaRestClient subjectAreaRestClient, String resourceUrnName)
    {
        super(subjectAreaRestClient, BASE_RELATIONSHIPS_URL + "/" + resourceUrnName);
    }

    @Override
    public List<L> find(String userId, FindRequest findRequest) {
        throw new UnsupportedOperationException();
    }
}