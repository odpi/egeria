/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.tex.responses;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = MetadataServersResponse.class, name = "MetadataServersResponse"),
                @JsonSubTypes.Type(value = TypeExplorerResponse.class, name = "TypeExplorerResponse")
        })
public class TypeExplorerOMVSAPIResponse {

    private Integer httpStatusCode;
    private String exceptionText;
    protected ResponseCategory responseCategory = ResponseCategory.Unknown;

    /**
     * constructor
     *
     * @param statusCode    http status code for the response
     * @param exceptionText text describing the exception.
     */
    public TypeExplorerOMVSAPIResponse(Integer statusCode, String exceptionText) {
        this.httpStatusCode = statusCode;
        this.exceptionText = exceptionText;
        this.responseCategory = ResponseCategory.error;
    }

    public Integer getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public String getExceptionText() {
        return this.exceptionText;
    }

    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public void setExceptionText(String exceptionText) {
        this.exceptionText = exceptionText;
    }
}
