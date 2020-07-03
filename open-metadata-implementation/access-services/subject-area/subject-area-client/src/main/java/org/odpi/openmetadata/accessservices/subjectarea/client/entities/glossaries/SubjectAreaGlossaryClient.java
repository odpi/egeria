/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.entities.glossaries;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.AbstractSubjectAreaEntity;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;

@SubjectAreaNodeClient
public class SubjectAreaGlossaryClient extends AbstractSubjectAreaEntity<Glossary> {
    public SubjectAreaGlossaryClient(SubjectAreaRestClient client)
    {
        super(client, SUBJECT_AREA_BASE_URL + "glossaries");
    }


    @Override
    public Class<Glossary> type() {
        return Glossary.class;
    }
}
