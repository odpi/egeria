/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import java.io.Serializable;

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
                @JsonSubTypes.Type(value = DataEngineRegistrationRequestBody.class, name = "dataEngine"),
                @JsonSubTypes.Type(value = PortImplementationRequestBody.class, name = "port"),
                @JsonSubTypes.Type(value = PortAliasRequestBody.class, name = "portAlias"),
                @JsonSubTypes.Type(value = ProcessesRequestBody.class, name = "processes"),
                @JsonSubTypes.Type(value = SchemaTypeRequestBody.class, name = "schema"),
                @JsonSubTypes.Type(value = DatabaseRequestBody.class, name = "database"),
                @JsonSubTypes.Type(value = DatabaseRequestBody.class, name = "table"),
                @JsonSubTypes.Type(value = DataFileRequestBody.class, name = "dataFile")
        })

public abstract class DataEngineOMASAPIRequestBody implements Serializable {
    private static final long serialVersionUID = 1L;


    /* unique name for the external source */
    private String externalSourceName;

    /**
     * Default constructor
     */
    DataEngineOMASAPIRequestBody() {
    }


    public String getExternalSourceName() {
        return externalSourceName;
    }

    public void setExternalSourceName(String externalSourceName) {
        this.externalSourceName = externalSourceName;
    }

    /**
     * JSON-like toString
     *
     * @return string containing the class name
     */
    @Override
    public String toString() {
        return "DataEngineOMASAPIRequestBody{" +
                ", externalSourceName='" + externalSourceName + '\'' +
                '}';
    }
}

