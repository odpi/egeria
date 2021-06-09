/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.beans;

import com.fasterxml.jackson.annotation.*;



import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import java.util.Objects;

/**
 * <p>
 *     The SchemaElement object provides a base class for the pieces that make up a schema for an asset.
 *     A schema provides information about how the data is structured in the asset.  Schemas are typically
 *     described as nested structures of schema elements.  There are two basic types:
 * </p>
 *     <ul>
 *         <li>SchemaType describes the structure of data.</li>
 *         <li>SchemaAttribute describes the use of another schema as part of the structure within a bigger schema.</li>
 *     </ul>
 * <p>
 *     Assets are linked to a SchemaType.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = SchemaAttribute.class, name = "SchemaAttribute"),
                @JsonSubTypes.Type(value = SchemaType.class, name = "SchemaType")
        })

public abstract class SchemaElement extends Referenceable
{
    private static final long     serialVersionUID = 1L;

    protected String  displayName = null;
    protected String  description = null;

    /**
     * Default constructor
     */
    public SchemaElement()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public SchemaElement(SchemaElement template)
    {
        super(template);

        if (template != null)
        {
            displayName = template.getDisplayName();
            description = template.getDescription();
        }
    }

    /**
     * Return the simple name of the schema element.
     *
     * @return string name
     */
    public String  getDisplayName() { return displayName; }


    /**
     * Set up the simple name of the schema element.
     *
     * @param name String display name
     */
    public void setDisplayName(String   name)
    {
        this.displayName = name;
    }


    /**
     * Returns the stored description property for the schema element.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the stored description property for the schema element.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Return a clone of this schema element.  This method is needed because schema element
     * is abstract.
     *
     * @return Clone of subclass.
     */
    public abstract SchemaElement cloneSchemaElement();


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaElement{" +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
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
        SchemaElement that = (SchemaElement) objectToCompare;
        return  Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description);
    }


    /**
     * Return a number that represents the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), displayName, description);
    }
}
