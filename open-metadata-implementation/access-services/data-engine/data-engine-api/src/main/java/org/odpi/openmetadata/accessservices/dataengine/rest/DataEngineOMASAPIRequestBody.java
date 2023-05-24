/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
                @JsonSubTypes.Type(value = ProcessRequestBody.class, name = "process"),
                @JsonSubTypes.Type(value = SchemaTypeRequestBody.class, name = "schema"),
                @JsonSubTypes.Type(value = DatabaseRequestBody.class, name = "database"),
                @JsonSubTypes.Type(value = DatabaseRequestBody.class, name = "table"),
                @JsonSubTypes.Type(value = DataFileRequestBody.class, name = "dataFile")
        })
@EqualsAndHashCode
@ToString(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
public abstract class DataEngineOMASAPIRequestBody implements Serializable {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 1L;

    /**
     * The unique name of the external source
     * -- GETTER --
     * Gets the external source name
     * @return the external source name
     * -- SETTER --
     * Sets the external source name
     * @param externalSourceName the external source name
     */
    private String externalSourceName;

}

