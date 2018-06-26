/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.events.content;

import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm;

public class SubjectAreaGlossaryTermEventContent extends SubjectAreaEventContent{
    public GlossaryTerm getGlossaryTerm() {
        return glossaryTerm;
    }

    public void setGlossaryTerm(GlossaryTerm glossaryTerm) {
        this.glossaryTerm = glossaryTerm;
    }

    GlossaryTerm glossaryTerm = null;


}
