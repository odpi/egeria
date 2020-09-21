/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.nodes.terms;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.AbstractSubjectAreaNode;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;

@SubjectAreaNodeClient
public class SubjectAreaTermClient extends AbstractSubjectAreaNode<Term> {
    public SubjectAreaTermClient(SubjectAreaRestClient client)
    {
        super(client, SUBJECT_AREA_BASE_URL + "terms");
    }

    @Override
    public Class<Term> type() {
        return Term.class;
    }
}