/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataplatform.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated
public class TableSource extends Source {

    private String name;
    private String schemaName;
    private DatabaseSource databaseSource;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }


    public DatabaseSource getDatabaseSource() {
        return databaseSource;
    }

    public void setDatabaseSource(DatabaseSource databaseSource) {
        this.databaseSource = databaseSource;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", schemaName='" + schemaName + '\'' +
                ", guid='" + guid + '\'' +
                ", databaseSource=" + databaseSource +
                '}';
    }
}