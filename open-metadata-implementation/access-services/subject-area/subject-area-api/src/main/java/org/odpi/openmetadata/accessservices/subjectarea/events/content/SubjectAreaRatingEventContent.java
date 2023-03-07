/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.subjectarea.events.content;

import org.odpi.openmetadata.accessservices.subjectarea.events.GlossaryArtifactsType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Rating;

public class SubjectAreaRatingEventContent extends SubjectAreaEventContent{
    private GlossaryArtifactsType glossaryArtifactsType=null;
    private String glossaryArtifactGuid = null;
    private Rating rating =null;

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

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
