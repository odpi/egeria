/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataEngineOMASAPIRequestBody provides a common header for Data Engine OMAS request bodies for its REST API.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ProcessRequestBody.class, name = "process"),
        })

public abstract class DataEngineOMASAPIRequestBody implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor
     */
    DataEngineOMASAPIRequestBody() {
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataEngineOMASAPIRequestBody(DataEngineOMASAPIRequestBody template) {
    }

    /**
     * JSON-like toString
     *
     * @return string containing the class name
     */
    @Override
    public String toString() {
        return "DataEngineOMASAPIRequestBody{}";
    }
}

