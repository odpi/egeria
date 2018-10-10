/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Schema;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SchemaResponse is the response structure used on the OMAS REST API calls that return a
 * Schema object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaResponse extends ConnectedAssetOMASAPIResponse
{
    private Schema schema               = null;
    private int    schemaAttributeCount = 0;


    /**
     * Default constructor
     */
    public SchemaResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SchemaResponse(SchemaResponse template)
    {
        super(template);

        if (template != null)
        {
            this.schema = template.getSchema();
            this.schemaAttributeCount = template.getSchemaAttributeCount();
        }
    }


    /**
     * Return the schema result.
     *
     * @return unique identifier
     */
    public Schema getSchema()
    {
        return schema;
    }


    /**
     * Set up the schema result.
     *
     * @param schema response object
     */
    public void setSchema(Schema schema)
    {
        this.schema = schema;
    }


    /**
     * Return the number of schema attributes in this schema.
     *
     * @return int
     */
    public int getSchemaAttributeCount()
    {
        return schemaAttributeCount;
    }


    /**
     * Set up the number of schema elements in this schema.
     *
     * @param schemaAttributeCount int
     *
     */
    public void setSchemaAttributeCount(int schemaAttributeCount)
    {
        this.schemaAttributeCount = schemaAttributeCount;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SchemaResponse{" +
                "schema=" + schema +
                ", schemaAttributeCount=" + schemaAttributeCount +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
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
        SchemaResponse that = (SchemaResponse) objectToCompare;
        return getSchemaAttributeCount() == that.getSchemaAttributeCount() &&
                Objects.equals(getSchema(), that.getSchema());
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getSchema(), getSchemaAttributeCount());
    }
}
