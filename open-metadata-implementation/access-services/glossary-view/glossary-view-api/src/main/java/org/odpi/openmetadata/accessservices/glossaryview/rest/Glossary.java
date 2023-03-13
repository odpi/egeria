/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Simple POJO that represents a Glossary
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Glossary extends GlossaryViewEntityDetail {

    private static final String DISPLAY_NAME = "displayName";
    private static final String USAGE = "usage";
    private static final String DESCRIPTION = "description";
    private static final String LANGUAGE = "language";

    public String getDisplayName(){
        return getProperties().get(DISPLAY_NAME);
    }

    public String getUsage(){
        return getProperties().get(USAGE);
    }

    public String getDescription(){
        return getProperties().get(DESCRIPTION);
    }

    public String getLanguage(){
        return getProperties().get(LANGUAGE);
    }

    public void setDisplayName(String displayName) {
        getProperties().put(DISPLAY_NAME, displayName);
    }

    public void setUsage(String usage) {
        getProperties().put(USAGE, usage);
    }

    public void setDescription(String description) {
        getProperties().put(DESCRIPTION, description);
    }

    public void setLanguage(String language) {
        getProperties().put(LANGUAGE, language);
    }
}
