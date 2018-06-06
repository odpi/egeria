/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Connection bean extends the Connection from the properties package with a default constructor and
 * setter methods.  This means it can be used for REST calls and other JSON based functions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConnectorType extends org.odpi.openmetadata.frameworks.connectors.properties.ConnectorType
{
    /**
     * Default constructor
     */
    public ConnectorType()
    {
        super(null);
    }


    /**
     * Copy/clone constructor for a connectorType that is not connected to an asset (either directly or indirectly).
     *
     * @param templateConnectorType - template object to copy.
     */
    public ConnectorType(ConnectorType templateConnectorType)
    {
        super(templateConnectorType);
    }


    /**
     * Set up the type of this element.
     *
     * @param type - element type proprerties
     */
    public void setType(ElementType type)
    {
        super.type = type;
    }


    /**
     * Set up the guid for the element.
     *
     * @param guid - String unique identifier
     */
    public void setGUID(String guid)
    {
        super.guid = guid;
    }


    /**
     * Set up the URL of this element.
     *
     * @param url - String
     */
    public void setURL(String url)
    {
        super.url = url;
    }


    /**
     * Set up the fully qualified name.
     *
     * @param qualifiedName - String name
     */
    public void setQualifiedName(String qualifiedName)
    {
        super.qualifiedName = qualifiedName;
    }


    /**
     * Set up additional properties.
     *
     * @param additionalProperties - Additional properties object
     */
    public void setAdditionalProperties(AdditionalProperties additionalProperties)
    {
        super.additionalProperties = additionalProperties;
    }


    /**
     * Set up the display name for UIs and reports.
     *
     * @param displayName - String name
     */
    public void setDisplayName(String displayName)
    {
        super.displayName = displayName;
    }


    /**
     * Set up description of the element.
     *
     * @param description - String
     */
    public void setDescription(String description)
    {
        super.description = description;
    }


    /**
     * The name of the connector provider class name.
     *
     * @param connectorProviderClassName - String class name
     */
    public void setConnectorProviderClassName(String connectorProviderClassName)
    {
        super.connectorProviderClassName = connectorProviderClassName;
    }

}
