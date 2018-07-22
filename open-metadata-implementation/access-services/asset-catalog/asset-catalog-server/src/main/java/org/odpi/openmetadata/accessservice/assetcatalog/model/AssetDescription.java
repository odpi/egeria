/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetDescription object holds properties that are used for displaying details of an asset, plus the properties
 * and classifications ans relationships.
 * Also the connection to asset is available in this object.
 */
@EqualsAndHashCode(callSuper = true)
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AssetDescription extends Asset implements Serializable {

    private static final long serialVersionUID = 1L;

    private String displayName;
    private String url;

    private Map<String, Object> properties;
    private List<Classification> classifications;
    private List<Relationship> relationships;
    private List<Connection> connection;
}
