/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Simple POJO that represents a Category
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlossaryCategory extends GlossaryViewEntityDetail {

    private static final  String DISPLAY_NAME = "displayName";
    private static final String DESCRIPTION = "description";

    public String getDisplayName(){
        return getProperties().get(DISPLAY_NAME);
    }

    public String getDescription(){
        return getProperties().get(DESCRIPTION);
    }

    public void setDisplayName(String displayName) {
        getProperties().put(DISPLAY_NAME, displayName);
    }

    public void setDescription(String description) {
        getProperties().put(DESCRIPTION, description);
    }

}
