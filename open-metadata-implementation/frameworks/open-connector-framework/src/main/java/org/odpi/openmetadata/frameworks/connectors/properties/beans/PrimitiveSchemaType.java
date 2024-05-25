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
 * PrimitiveSchemaType describes a schema element that has a primitive type.  This class stores which
 * type of primitive type it is an a default value if supplied.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PrimitiveSchemaType extends SimpleSchemaType
{
    /**
     * Default constructor used by subclasses
     */
    public PrimitiveSchemaType()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public PrimitiveSchemaType(PrimitiveSchemaType template)
    {
        super(template);
    }


    /**
     * Returns a clone of this object as the abstract SchemaElement class.
     *
     * @return SchemaElement
     */
    @Override
    public SchemaElement cloneSchemaElement()
    {
        return new PrimitiveSchemaType(this);
    }


    /**
     * Returns a clone of this object as the abstract SchemaType class.
     *
     * @return PrimitiveSchemaType object
     */
    @Override
    public SchemaType cloneSchemaType()
    {
        return new PrimitiveSchemaType(this);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "PrimitiveSchemaType{" +
                       "dataType='" + dataType + '\'' +
                       ", defaultValue='" + defaultValue + '\'' +
                       '}';
    }
}