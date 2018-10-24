/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ColumnContext implements Serializable {

    private static final long serialVersionUID = 1L;

    private String columnGuid;
    private String columnQualifiedName;
    private String columnAttributeName;
    private String columnType;
    private String columnQualifiedNameColumnType;

    private String tableName;
    private String tableQualifiedName;
    private String tableTypeQualifiedName;

    private String schemaName;
    private String schemaQualifiedName;
    private String schemaTypeQualifiedName;

    private String databaseName;
    private String databaseQualifiedName;
}