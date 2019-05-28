/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Simple POJO that holds the data of a Term
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Term extends GlossaryViewEntityDetail {

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

}
