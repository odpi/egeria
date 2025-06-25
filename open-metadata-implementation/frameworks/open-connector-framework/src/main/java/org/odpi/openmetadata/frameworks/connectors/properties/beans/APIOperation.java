/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * APIOperation describes an API command used as part of an API schema.  It also defines the structure of the
 * header information, request and response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class APIOperation extends SchemaType
{
    protected  String      command            = null;
    protected  SchemaType  headerSchemaType   = null;
    protected  SchemaType  requestSchemaType  = null;
    protected  SchemaType  responseSchemaType = null;

    /**
     * Default constructor used by subclasses
     */
    public APIOperation()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public APIOperation(APIOperation template)
    {
        super(template);

        if (template != null)
        {
            command = template.getCommand();
            headerSchemaType = template.getHeaderSchemaType();
            requestSchemaType = template.getRequestSchemaType();
            responseSchemaType = template.getResponseSchemaType();
        }
    }


    /**
     * Return the command for this operation
     *
     * @return String name
     */
    public String getCommand() { return command; }


    /**
     * Set up the command for this operation
     *
     * @param command name
     */
    public void setCommand(String command)
    {
        this.command = command;
    }


    /**
     * Return the structure of the API header (or null if no header).
     *
     * @return schema type
     */
    public SchemaType getHeaderSchemaType()
    {
        return headerSchemaType;
    }


    /**
     * Set up the structure of the API header (or null if no header).
     *
     * @param headerSchemaType schema type
     */
    public void setHeaderSchemaType(SchemaType headerSchemaType)
    {
        this.headerSchemaType = headerSchemaType;
    }


    /**
     * Return the structure of the request parameters (request body).
     *
     * @return schema type
     */
    public SchemaType getRequestSchemaType()
    {
        return requestSchemaType;
    }


    /**
     * Set up the structure of the request parameters (request body).
     *
     * @param requestSchemaType schema type
     */
    public void setRequestSchemaType(SchemaType requestSchemaType)
    {
        this.requestSchemaType = requestSchemaType;
    }


    /**
     * Return the structure of the response.
     *
     * @return schema type
     */
    public SchemaType getResponseSchemaType()
    {
        return responseSchemaType;
    }


    /**
     * Set up the structure of the response.
     *
     * @param responseSchemaType schema type
     */
    public void setResponseSchemaType(SchemaType responseSchemaType)
    {
        this.responseSchemaType = responseSchemaType;
    }


    /**
     * Returns a clone of this object as the abstract SchemaElement class.
     *
     * @return PrimitiveSchemaType object
     */
    @Override
    public SchemaElement cloneSchemaElement()
    {
        return new APIOperation(this);
    }


    /**
     * Returns a clone of this object as the abstract SchemaType class.
     *
     * @return PrimitiveSchemaType object
     */
    @Override
    public SchemaType cloneSchemaType()
    {
        return new APIOperation(this);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "APIOperation{" +
                "command='" + command + '\'' +
                ", headerSchemaType=" + headerSchemaType +
                ", requestSchemaType=" + requestSchemaType +
                ", responseSchemaType=" + responseSchemaType +
                ", displayName='" + getDisplayName() + '\'' +
                ", versionNumber='" + getVersionIdentifier() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", usage='" + getUsage() + '\'' +
                ", encodingStandard='" + getEncodingStandard() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", extendedProperties=" + getExtendedProperties() +
                ", meanings=" + getMeanings() +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", classifications=" + getClassifications() +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
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
        APIOperation that = (APIOperation) objectToCompare;
        return Objects.equals(getCommand(), that.getCommand()) &&
                Objects.equals(getHeaderSchemaType(), that.getHeaderSchemaType()) &&
                Objects.equals(getRequestSchemaType(), that.getRequestSchemaType()) &&
                Objects.equals(getResponseSchemaType(), that.getResponseSchemaType());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getCommand(), getHeaderSchemaType(), getRequestSchemaType(),
                            getResponseSchemaType());
    }
}