/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.nodes.glossaries;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.AbstractSubjectAreaNode;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;

@SubjectAreaNodeClient
public class SubjectAreaGlossaryClient<G extends Glossary> extends AbstractSubjectAreaNode<G> {
    public SubjectAreaGlossaryClient(SubjectAreaRestClient client)
    {
        super(client, SUBJECT_AREA_BASE_URL + "glossaries");
    }
}
