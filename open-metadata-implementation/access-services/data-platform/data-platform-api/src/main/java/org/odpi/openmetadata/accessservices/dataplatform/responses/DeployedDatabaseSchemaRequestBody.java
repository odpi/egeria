/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.responses;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.odpi.openmetadata.accessservices.dataplatform.properties.DeployedDatabaseSchema;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The request body of creating the new type of the asset deployed database schema.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated
public class DeployedDatabaseSchemaRequestBody extends DataPlatformOMASAPIRequestBody {

    @JsonProperty("deployedDatabaseSchema")
    private DeployedDatabaseSchema deployedDatabaseSchema;

    /**
     * Default constructor
     */
    public DeployedDatabaseSchemaRequestBody() {
    }


    /**
     * Gets deployed database schema.
     *
     * @return the deployed database schema
     */
    public DeployedDatabaseSchema getDeployedDatabaseSchema() {
        return deployedDatabaseSchema;
    }

    /**
     * Sets deployed database schema.
     *
     * @param deployedDatabaseSchema the deployed database schema
     */
    public void setDeployedDatabaseSchema(DeployedDatabaseSchema deployedDatabaseSchema) {
        this.deployedDatabaseSchema = deployedDatabaseSchema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeployedDatabaseSchemaRequestBody that = (DeployedDatabaseSchemaRequestBody) o;
        return Objects.equals(deployedDatabaseSchema, that.deployedDatabaseSchema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deployedDatabaseSchema);
    }

    @Override
    public String toString() {
        return "DeployedDatabaseSchemaRequestBody{" +
                "deployedDatabaseSchema=" + deployedDatabaseSchema +
                "} " + super.toString();
    }
}
