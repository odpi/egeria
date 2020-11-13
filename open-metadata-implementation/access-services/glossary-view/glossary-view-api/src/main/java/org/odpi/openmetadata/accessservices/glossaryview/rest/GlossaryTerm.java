/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Simple POJO that represents a Term
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlossaryTerm extends GlossaryViewEntityDetail {

    private static String DISPLAY_NAME = "displayName";
    private static String DESCRIPTION = "description";
    private static String SUMMARY = "summary";
    private static String EXAMPLES = "examples";
    private static String ABBREVIATION = "abbreviation";
    private static String USAGE = "usage";

    public String getDisplayName(){
        return getProperties().get(DISPLAY_NAME);
    }

    public String getDescription(){
        return getProperties().get(DESCRIPTION);
    }

    public String getSummary(){
        return getProperties().get(SUMMARY);
    }

    public String getExamples(){
        return getProperties().get(EXAMPLES);
    }

    public String getAbbreviation(){
        return getProperties().get(ABBREVIATION);
    }

    public String getUsage(){
        return getProperties().get(USAGE);
    }

    public void setDisplayName(String displayName) {
        getProperties().put(DISPLAY_NAME, displayName);
    }

    public void setDescription(String description) {
        getProperties().put(DESCRIPTION, description);
    }

    public void setSummary(String summary) {
        getProperties().put(SUMMARY, summary);
    }

    public void setExamples(String examples) {
        getProperties().put(EXAMPLES, examples);
    }

    public void setAbbreviation(String abbreviation) {
        getProperties().put(ABBREVIATION, abbreviation);
    }

    public void setUsage(String usage) {
        getProperties().put(USAGE, usage);
    }
}
