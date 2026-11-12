/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat.jsonld;

/**
 * JsonLdValue is a value of a property of a node in a JSON-LD graph.  It is either a literal (a string with an optional
 * language or datatype) or a reference to another node.
 */
public class JsonLdValue
{
    private final String value;
    private final String language;
    private final String datatype;
    private final String nodeId;


    /**
     * Create a value.
     *
     * @param value literal value (null for a reference)
     * @param language language tag of a literal (may be null)
     * @param datatype IRI of the datatype of a literal (may be null)
     * @param nodeId identifier of the referenced node (null for a literal)
     */
    private JsonLdValue(String value,
                        String language,
                        String datatype,
                        String nodeId)
    {
        this.value    = value;
        this.language = language;
        this.datatype = datatype;
        this.nodeId   = nodeId;
    }


    /**
     * Create a literal value.
     *
     * @param value literal value
     * @param language language tag (may be null)
     * @param datatype IRI of the datatype (may be null)
     * @return value
     */
    public static JsonLdValue literal(String value,
                                      String language,
                                      String datatype)
    {
        return new JsonLdValue(value, language, datatype, null);
    }


    /**
     * Create a reference to another node.
     *
     * @param nodeId identifier of the node - either an IRI or a blank node identifier
     * @return value
     */
    public static JsonLdValue reference(String nodeId)
    {
        return new JsonLdValue(null, null, null, nodeId);
    }


    /**
     * Return whether this value is a reference to another node.
     *
     * @return boolean
     */
    public boolean isReference()
    {
        return nodeId != null;
    }


    /**
     * Return the literal value.
     *
     * @return string or null if this is a reference
     */
    public String getValue()
    {
        return value;
    }


    /**
     * Return the language tag of the literal.
     *
     * @return string or null
     */
    public String getLanguage()
    {
        return language;
    }


    /**
     * Return the IRI of the datatype of the literal.
     *
     * @return string or null
     */
    public String getDatatype()
    {
        return datatype;
    }


    /**
     * Return the identifier of the referenced node.
     *
     * @return string or null if this is a literal
     */
    public String getNodeId()
    {
        return nodeId;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "JsonLdValue{" +
                       "value='" + value + '\'' +
                       ", language='" + language + '\'' +
                       ", datatype='" + datatype + '\'' +
                       ", nodeId='" + nodeId + '\'' +
                       '}';
    }
}
