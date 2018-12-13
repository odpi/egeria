/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataEngineOMASAPIResponse provides a common header for Data Engine OMAS managed rest to its REST API.
 * It manages information about exceptions.  If no exception has been raised exceptionClassName is null.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = GUIDResponse.class, name = "GUIDResponse"),
        })
@Data
abstract class DataEngineOMASAPIResponse implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private int relatedHTTPCode = 200;
    private String exceptionClassName = null;
    private String exceptionErrorMessage = null;
    private String exceptionSystemAction = null;
    private String exceptionUserAction = null;
    private Map<String, Object> exceptionProperties = null;

    /**
     * Default constructor
     */
    DataEngineOMASAPIResponse() {
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    DataEngineOMASAPIResponse(DataEngineOMASAPIResponse template) {
        if (template != null) {
            this.relatedHTTPCode = template.getRelatedHTTPCode();
            this.exceptionClassName = template.getExceptionClassName();
            this.exceptionErrorMessage = template.getExceptionErrorMessage();
            this.exceptionSystemAction = template.getExceptionSystemAction();
            this.exceptionUserAction = template.getExceptionUserAction();
            this.exceptionProperties = template.getExceptionProperties();
        }
    }
}
