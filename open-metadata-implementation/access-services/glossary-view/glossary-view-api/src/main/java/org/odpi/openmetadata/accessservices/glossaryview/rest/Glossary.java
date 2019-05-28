package org.odpi.openmetadata.accessservices.glossaryview.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Simple POJO that holds the data of a Glossary
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Glossary extends GlossaryViewEntityDetail {

    private static String DISPLAY_NAME = "displayName";
    private static String USAGE = "usage";
    private static String DESCRIPTION = "description";
    private static String LANGUAGE = "language";

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

}
