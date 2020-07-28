/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.entities.terms;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.AbstractSubjectAreaEntity;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;

@SubjectAreaNodeClient
public class SubjectAreaTermClient extends AbstractSubjectAreaEntity<Term> {
    public SubjectAreaTermClient(SubjectAreaRestClient client)
    {
        super(client, SUBJECT_AREA_BASE_URL + "terms");
    }

    @Override
    public Class<Term> type() {
        return Term.class;
    }
}