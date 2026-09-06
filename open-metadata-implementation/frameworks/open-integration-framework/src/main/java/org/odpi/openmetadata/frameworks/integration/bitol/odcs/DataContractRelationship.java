/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolCustomProperty;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a relationship (typically a foreign key) between schema elements in an Open Data
 * Contract Standard (ODCS) data contract. At the schema object level both the "from" and "to" references are
 * supplied. At the property level the "from" is implicit (the property itself) and only the "to" reference is
 * supplied. Each reference is either a shorthand reference (object.property) or a fully qualified reference
 * (contractId.object.property). The standard allows a single reference or a list of references (for composite
 * keys); this bean always holds a list.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataContractRelationship
{
    private String                    type = null;
    private List<String>              from = null;
    private List<String>              to = null;
    private List<BitolCustomProperty> customProperties = null;


    /**
     * Default constructor
     */
    public DataContractRelationship()
    {
    }


    /**
     * Return the type of relationship.  The only standard value is foreignKey.
     *
     * @return string type name
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the type of relationship.  The only standard value is foreignKey.
     *
     * @param type string type name
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Return the source property reference(s) of the relationship.
     *
     * @return list of property references
     */
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    public List<String> getFrom()
    {
        return from;
    }


    /**
     * Set up the source property reference(s) of the relationship.
     *
     * @param from list of property references
     */
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    public void setFrom(List<String> from)
    {
        this.from = from;
    }


    /**
     * Return the target property reference(s) of the relationship.
     *
     * @return list of property references
     */
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    public List<String> getTo()
    {
        return to;
    }


    /**
     * Set up the target property reference(s) of the relationship.
     *
     * @param to list of property references
     */
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    public void setTo(List<String> to)
    {
        this.to = to;
    }


    /**
     * Return the list of custom (key/value) properties attached to this element.
     *
     * @return list of custom properties
     */
    public List<BitolCustomProperty> getCustomProperties()
    {
        return customProperties;
    }


    /**
     * Set up the list of custom (key/value) properties attached to this element.
     *
     * @param customProperties list of custom properties
     */
    public void setCustomProperties(List<BitolCustomProperty> customProperties)
    {
        this.customProperties = customProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataContractRelationship{" +
                       "type='" + type + '\'' +
                       ", from=" + from +
                       ", to=" + to +
                       ", customProperties=" + customProperties +
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
        DataContractRelationship that = (DataContractRelationship) objectToCompare;
        return Objects.equals(type, that.type) &&
                       Objects.equals(from, that.from) &&
                       Objects.equals(to, that.to) &&
                       Objects.equals(customProperties, that.customProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(type, from, to, customProperties);
    }
}
