/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.events.content;

public class SubjectAreaEventContent {
    public String getGlossaryGuid() {
        return glossaryGuid;
    }

    public void setGlossaryGuid(String glossaryGuid) {
        this.glossaryGuid = glossaryGuid;
    }

    String glossaryGuid =null;
}
