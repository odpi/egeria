/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.responses;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.dataplatform.properties.asset.DeployedDatabaseSchema;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Registration request body of DeployedDatabaseSchema.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeployedDatabaseSchemaRequestBody extends FFDCResponseBase {

    private DeployedDatabaseSchema deployedDatabaseSchema;

    /**
     * Default constructor
     */
    public DeployedDatabaseSchemaRequestBody() {
    }

    /**
     * Default constructor
     */
    public DeployedDatabaseSchemaRequestBody(DeployedDatabaseSchema deployedDatabaseSchema) {
        this.deployedDatabaseSchema = deployedDatabaseSchema;
    }

    public DeployedDatabaseSchema getDeployedDatabaseSchema() {
        return deployedDatabaseSchema;
    }

    public void setDeployedDatabaseSchema(DeployedDatabaseSchema deployedDatabaseSchema) {
        this.deployedDatabaseSchema = deployedDatabaseSchema;
    }

    @Override
    public String toString() {
        return "DeployedDatabaseSchemaRequestBody{" +
                "deployedDatabaseSchema=" + deployedDatabaseSchema +
                "} " + super.toString();
    }
}
