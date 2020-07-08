/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.entities.glossaries;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaEntityClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;

/**
 * The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for glossaries.
 */
public interface SubjectAreaGlossary {

   /**
    * @return {@link SubjectAreaEntityClient} for client calls(described in {@link SubjectAreaClient})
    * when working with Glossary objects
    */
   SubjectAreaEntityClient<Glossary> glossary();
}
