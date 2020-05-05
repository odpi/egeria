/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataPlatformOMASAPIRequestBody provides a common header for Data Platform OMAS request bodies for its REST API.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DataPlatformRegistrationRequestBody.class, name = "dataPlatform"),
                @JsonSubTypes.Type(value = DeployedDatabaseSchemaRequestBody.class, name = "deployedDatabaseSchema")
        })
@Deprecated
public abstract class DataPlatformOMASAPIRequestBody implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /* unique name for the external source */
    private String externalSourceName;

    /**
     * Default constructor
     */
    DataPlatformOMASAPIRequestBody() {
    }


    /**
     * Gets external source name.
     *
     * @return the external source name
     */
    public String getExternalSourceName() {
        return externalSourceName;
    }

    /**
     * Sets external source name.
     *
     * @param externalSourceName the external source name
     */
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
        return "DataPlatformOMASAPIRequestBody{" +
                ", externalSourceName='" + externalSourceName + '\'' +
                '}';
    }
}

