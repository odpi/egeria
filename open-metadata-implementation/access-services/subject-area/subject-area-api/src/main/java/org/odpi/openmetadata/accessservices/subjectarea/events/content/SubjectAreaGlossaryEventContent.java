/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.subjectarea.events.content;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;

public class SubjectAreaGlossaryEventContent extends SubjectAreaEventContent{
    Glossary glossary = null;

    public Glossary getGlossary() {
        return glossary;
    }

    public void setGlossary(Glossary glossary) {
        this.glossary = glossary;
    }
}
