/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.subjectarea.events.content;


import org.odpi.openmetadata.accessservices.subjectarea.events.GlossaryArtifactsType;
import org.odpi.openmetadata.accessservices.subjectarea.events.GlossaryArtifactsType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Note;


public class SubjectAreaNoteEventContent extends SubjectAreaEventContent{
    private GlossaryArtifactsType glossaryArtifactsType=null;
    private String glossaryArtifactGuid = null;
    private Note note = null;

    public GlossaryArtifactsType getGlossaryArtifactsType() {
        return glossaryArtifactsType;
    }

    public void setGlossaryArtifactsType(GlossaryArtifactsType glossaryArtifactsType) {
        this.glossaryArtifactsType = glossaryArtifactsType;
    }

    public String getGlossaryArtifactGuid() {
        return glossaryArtifactGuid;
    }

    public void setGlossaryArtifactGuid(String glossaryArtifactGuid) {
        this.glossaryArtifactGuid = glossaryArtifactGuid;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }
}
