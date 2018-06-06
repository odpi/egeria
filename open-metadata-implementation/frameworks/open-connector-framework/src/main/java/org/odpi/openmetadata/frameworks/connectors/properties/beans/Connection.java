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
public class Connection extends org.odpi.openmetadata.frameworks.connectors.properties.Connection
{
    /**
     * Default constructor sets the Connection properties to null.
     */
    public Connection()
    {
        super(null);
    }


    /**
     * Copy/clone Constructor to return a copy of a connection object that is not connected to an asset.
     *
     * @param templateConnection - Connection to copy
     */
    public Connection(Connection templateConnection)
    {
        /*
         * Set parentAsset to null
         */
        super(templateConnection);
    }


    /**
     * Copy/clone Constructor to return a copy of a connection object that is not connected to an asset.
     *
     * @param templateConnection - Connection to copy
     */
    public Connection(org.odpi.openmetadata.frameworks.connectors.properties.Connection templateConnection)
    {
        /*
         * Set parentAsset to null
         */
        super(templateConnection);
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
     * Set up the connector type properties for this Connection.
     *
     * @param connectorType - ConnectorType properties object
     */
    public void setConnectorType(ConnectorType connectorType)
    {
        super.connectorType = connectorType;
    }


    /**
     * Set up the endpoint properties for this Connection.
     *
     * @param endpoint - Endpoint properties object
     */
    public void setEndpoint(Endpoint endpoint)
    {
        super.endpoint = endpoint;
    }


    /**
     * Set up the secured properties for this Connection.
     *
     * @param securedProperties - properties that contain secret information such as log on information.
     */
    public void setSecuredProperties(AdditionalProperties securedProperties)
    {
        super.securedProperties = securedProperties;
    }
}
