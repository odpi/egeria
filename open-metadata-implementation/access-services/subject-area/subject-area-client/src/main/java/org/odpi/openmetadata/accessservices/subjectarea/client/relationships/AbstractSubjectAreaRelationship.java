/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.client.AbstractSubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRelationshipClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;

import java.util.List;

/**
 * @param <L> inherited from {@link Relationship} object type
 * Abstract class for Subject Area client relationship operations
 */
public abstract class AbstractSubjectAreaRelationship<L extends Relationship> extends AbstractSubjectArea<L> implements SubjectAreaRelationshipClient<L> {
    protected AbstractSubjectAreaRelationship(SubjectAreaRestClient subjectAreaRestClient, String resourceUrnName)
    {
        super(subjectAreaRestClient, BASE_RELATIONSHIPS_URL + "/" + resourceUrnName);
    }

    @Override
    public List<L> find(String userId, FindRequest findRequest) {
        throw new UnsupportedOperationException();
    }
}