/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat.jsonld;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JsonLdNode is a node in a JSON-LD graph.  It has an identifier (an IRI or a blank node identifier), a set of types
 * and a map of property IRIs to values.
 */
public class JsonLdNode
{
    /**
     * The prefix of a blank node identifier.
     */
    public static final String BLANK_NODE_PREFIX = "_:";

    private final String                         id;
    private final Set<String>                    types      = new LinkedHashSet<>();
    private final Map<String, List<JsonLdValue>> properties = new LinkedHashMap<>();


    /**
     * Create a node.
     *
     * @param id identifier of the node
     */
    public JsonLdNode(String id)
    {
        this.id = id;
    }


    /**
     * Return the identifier of the node.
     *
     * @return IRI or blank node identifier
     */
    public String getId()
    {
        return id;
    }


    /**
     * Return whether the node is a blank node - that is, it has no IRI.
     *
     * @return boolean
     */
    public boolean isBlank()
    {
        return id.startsWith(BLANK_NODE_PREFIX);
    }


    /**
     * Return the IRI of the node.
     *
     * @return IRI or null if this is a blank node
     */
    public String getIRI()
    {
        return isBlank() ? null : id;
    }


    /**
     * Return the IRIs of the types of the node.
     *
     * @return set of IRIs
     */
    public Set<String> getTypes()
    {
        return types;
    }


    /**
     * Return whether the node has the requested type.
     *
     * @param typeIRI IRI of the type
     * @return boolean
     */
    public boolean hasType(String typeIRI)
    {
        return types.contains(typeIRI);
    }


    /**
     * Return the properties of the node.
     *
     * @return map of property IRI to values
     */
    public Map<String, List<JsonLdValue>> getProperties()
    {
        return properties;
    }


    /**
     * Return the values of a property.
     *
     * @param propertyIRI IRI of the property
     * @return list of values - empty if the property is not set
     */
    public List<JsonLdValue> getValues(String propertyIRI)
    {
        List<JsonLdValue> values = properties.get(propertyIRI);

        return (values == null) ? List.of() : values;
    }


    /**
     * Add a value to a property.
     *
     * @param propertyIRI IRI of the property
     * @param value value to add
     */
    public void addValue(String      propertyIRI,
                         JsonLdValue value)
    {
        properties.computeIfAbsent(propertyIRI, key -> new ArrayList<>()).add(value);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "JsonLdNode{" +
                       "id='" + id + '\'' +
                       ", types=" + types +
                       ", properties=" + properties +
                       '}';
    }
}
