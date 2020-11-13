/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SchemaTypeResponse is the response structure used on the OMAS REST API calls that return a
 * StructSchemaType object as a response.  The counts for meanings, attributes and links are used to set up the iterators
 * to for the detail linked to the SchemaType.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaTypeResponse extends PagedResponse
{
    private static final long    serialVersionUID = 1L;

    private SchemaType schemaType = null;


    /**
     * Default constructor
     */
    public SchemaTypeResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SchemaTypeResponse(SchemaTypeResponse template)
    {
        super(template);

        if (template != null)
        {
            this.schemaType = template.getSchemaType();
        }
    }


    /**
     * Return the schemaType result.
     *
     * @return unique identifier
     */
    public SchemaType getSchemaType()
    {
        return schemaType;
    }


    /**
     * Set up the schemaType result.
     *
     * @param schemaType response object
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
        return "SchemaTypeResponse{" +
                "schemaType=" + schemaType +
                ", startingFromElement=" + getStartingFromElement() +
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
        SchemaTypeResponse that = (SchemaTypeResponse) objectToCompare;
        return Objects.equals(getSchemaType(), that.getSchemaType());
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getSchemaType());
    }
}
