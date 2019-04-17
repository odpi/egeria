/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", URL='" + getURL() + '\'' +
                ", classifications=" + getClassifications() +
                '}';
    }
}
