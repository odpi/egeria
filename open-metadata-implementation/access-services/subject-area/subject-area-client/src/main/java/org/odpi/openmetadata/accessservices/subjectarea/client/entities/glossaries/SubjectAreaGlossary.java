/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.entities.glossaries;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaEntityClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;

public interface SubjectAreaGlossary {
   SubjectAreaEntityClient<Glossary> glossary();
}
