/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.rest;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetOwnerOMASAPIRequestBody provides a common header for Asset Owner OMAS request bodies for its REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReferenceableRequestBody.class, name = "ReferenceableRequestBody"),
        @JsonSubTypes.Type(value = SchemaTypeRequestBody.class, name = "SchemaTypeRequestBody"),
        @JsonSubTypes.Type(value = SchemaAttributeRequestBody.class, name = "SchemaAttributeRequestBody"),
        @JsonSubTypes.Type(value = SchemaAttributesRequestBody.class, name = "SchemaAttributesRequestBody"),
        @JsonSubTypes.Type(value = NewFileAssetRequestBody.class, name = "NewFileAssetRequestBody"),
        @JsonSubTypes.Type(value = NewFileSystemRequestBody.class, name = "NewFileSystemRequestBody"),
        @JsonSubTypes.Type(value = PathNameRequestBody.class, name = "PathNameRequestBody")

              })
public abstract class AssetOwnerOMASAPIRequestBody implements java.io.Serializable
{
    private static final long    serialVersionUID = 1L;


    /**
     * Default constructor
     */
    public AssetOwnerOMASAPIRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetOwnerOMASAPIRequestBody(AssetOwnerOMASAPIRequestBody template)
    {
    }


    /**
     * JSON-like toString
     *
     * @return string containing the class name
     */
    @Override
    public String toString()
    {
        return "AssetOwnerOMASAPIRequestBody{}";
    }
}
