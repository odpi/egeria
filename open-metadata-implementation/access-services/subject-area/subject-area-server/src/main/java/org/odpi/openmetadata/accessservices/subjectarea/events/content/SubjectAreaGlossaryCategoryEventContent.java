/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.events.content;

import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryCategory.GlossaryCategory;

public class SubjectAreaGlossaryCategoryEventContent extends SubjectAreaEventContent{
    public GlossaryCategory getGlossaryCategory() {
        return glossaryCategory;
    }

    public void setGlossaryCategory(GlossaryCategory glossaryCategory) {
        this.glossaryCategory = glossaryCategory;
    }

    GlossaryCategory glossaryCategory = null;


}
