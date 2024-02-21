/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * AssetResponse is the response structure used on the OMAS REST API calls that return a
 * asset bean object as a response.  It also returns counts of the number of connected
 * elements for the asset.  This can be implemented cheaply as a single pass through
 * the relationships linked to the asset and, assuming that the AssetUniverse structure
 * is sparsely populated, and most callers only assess a specific subset of the information,
 * it reduces the number of server calls needed to populate the AssetUniverse.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetResponse extends OCFOMASAPIResponse
{
    private Asset          asset                      = null;
    private SchemaType     schemaType     = null;


    /**
     * Default constructor
     */
    public AssetResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetResponse(AssetResponse template)
    {
        super(template);

        if (template != null)
        {
            this.asset                      = template.getAsset();
            this.schemaType                 = template.getSchemaType();
        }
    }


    /**
     * Return the asset result.
     *
     * @return unique identifier
     */
    public Asset getAsset()
    {
        return asset;
    }


    /**
     * Set up the asset result.
     *
     * @param asset  unique identifier
     */
    public void setAsset(Asset asset)
    {
        this.asset = asset;
    }


    /**
     * Is there an attached schema?
     *
     * @return schema type bean
     */
    public SchemaType getSchemaType()
    {
        return schemaType;
    }


    /**
     * Set up whether there is an attached schema.
     *
     * @param schemaType schema type bean
     */
    public void setSchemaType(SchemaType schemaType)
    {
        this.schemaType = schemaType;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetResponse{" +
                "asset=" + asset +
                ", schemaType=" + schemaType +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }

    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetResponse that = (AssetResponse) objectToCompare;
        return Objects.equals(getSchemaType(), that.getSchemaType()) &&
                Objects.equals(getAsset(), that.getAsset());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getAsset(), getSchemaType());
    }
}
