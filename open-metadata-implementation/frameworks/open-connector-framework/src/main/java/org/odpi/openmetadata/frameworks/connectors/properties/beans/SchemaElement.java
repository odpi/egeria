/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = SchemaAttribute.class, name = "SchemaAttribute"),
                @JsonSubTypes.Type(value = SchemaType.class, name = "SchemaType")
        })

public abstract class SchemaElement extends Referenceable
{
    private static final long     serialVersionUID = 1L;

    protected boolean isDeprecated = false;
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
     * Copy/clone Constructor - the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param template template object to copy.
     */
    public SchemaElement(SchemaElement template)
    {
        super(template);
    }


    /**
     * Is the schema element deprecated?
     *
     * @return boolean flag
     */
    public boolean isDeprecated()
    {
        return isDeprecated;
    }


    /**
     * Set whether the schema element deprecated or not.  Default is false.
     *
     * @param deprecated boolean flag
     */
    public void setDeprecated(boolean deprecated)
    {
        isDeprecated = deprecated;
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
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", meanings=" + meanings +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
                ", extendedProperties=" + extendedProperties +
                '}';
    }
}
