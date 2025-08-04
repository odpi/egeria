/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.schema;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaElementProperties carries the common parameters for creating or updating schema elements such as schema types and attributes.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SchemaTypeProperties.class, name = "SchemaTypeProperties"),
        @JsonSubTypes.Type(value = SchemaAttributeProperties.class, name = "SchemaAttributeProperties")
              })
public class SchemaElementProperties extends ReferenceableProperties
{
    private String userDefinedStatus = null;


    /**
     * Default constructor
     */
    public SchemaElementProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SCHEMA_ELEMENT.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SchemaElementProperties(SchemaElementProperties template)
    {
        super(template);

        if (template != null)
        {
            userDefinedStatus = template.getUserDefinedStatus();
        }
    }


    /**
     * What is the status of the element if element status is OTHER?
     *
     * @return string
     */
    public String getUserDefinedStatus()
    {
        return userDefinedStatus;
    }


    /**
     * Set  the schema element userDefinedStatus.
     *
     * @param userDefinedStatus string
     */
    public void setUserDefinedStatus(String userDefinedStatus)
    {
        this.userDefinedStatus = userDefinedStatus;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SchemaElementProperties{" +
                ", userDefinedStatus=" + userDefinedStatus +
                "} " + super.toString();
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
        if (!super.equals(objectToCompare)) return false;
        SchemaElementProperties that = (SchemaElementProperties) objectToCompare;
        return Objects.equals(userDefinedStatus, that.userDefinedStatus);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), userDefinedStatus);
    }
}
