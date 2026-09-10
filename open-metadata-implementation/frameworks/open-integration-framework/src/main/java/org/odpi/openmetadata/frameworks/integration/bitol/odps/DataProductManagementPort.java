/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odps;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolAuthoritativeDefinition;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolCustomProperty;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a management port of an Open Data Product Standard (ODPS) data product. Management ports
 * are the access points used to manage the data product, covering discoverability, observability, control and
 * dictionary content.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataProductManagementPort
{
    private String                             name = null;
    private String                             content = null;
    private String                             type = null;
    private String                             url = null;
    private String                             channel = null;
    private String                             description = null;
    private List<String>                       tags = null;
    private List<BitolCustomProperty>          customProperties = null;
    private List<BitolAuthoritativeDefinition> authoritativeDefinitions = null;
    private String id = null;
    private Boolean deprecated = null;


    /**
     * Default constructor
     */
    public DataProductManagementPort()
    {
    }


    /**
     * Return the unique name (endpoint identifier) of the management port.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the unique name (endpoint identifier) of the management port.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the type of content: discoverability, observability, control or dictionary.  Standard values are defined in DataProductManagementPortContent.
     *
     * @return string content type
     */
    public String getContent()
    {
        return content;
    }


    /**
     * Set up the type of content: discoverability, observability, control or dictionary.  Standard values are defined in DataProductManagementPortContent.
     *
     * @param content string content type
     */
    public void setContent(String content)
    {
        this.content = content;
    }


    /**
     * Return the type of access: rest or topic.  Standard values are defined in DataProductManagementPortType.
     *
     * @return string type name
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the type of access: rest or topic.  Standard values are defined in DataProductManagementPortType.
     *
     * @param type string type name
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Return the URL used to access the endpoint.
     *
     * @return string URL
     */
    public String getUrl()
    {
        return url;
    }


    /**
     * Set up the URL used to access the endpoint.
     *
     * @param url string URL
     */
    public void setUrl(String url)
    {
        this.url = url;
    }


    /**
     * Return the channel (for example topic name) used to communicate with the data product.
     *
     * @return string channel name
     */
    public String getChannel()
    {
        return channel;
    }


    /**
     * Set up the channel (for example topic name) used to communicate with the data product.
     *
     * @param channel string channel name
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }


    /**
     * Return the purpose and usage of the management port.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the purpose and usage of the management port.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the list of tags attached to this element.
     *
     * @return list of tag strings
     */
    public List<String> getTags()
    {
        return tags;
    }


    /**
     * Set up the list of tags attached to this element.
     *
     * @param tags list of tag strings
     */
    public void setTags(List<String> tags)
    {
        this.tags = tags;
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
     * Return the list of links to sources that provide more details about this element.
     *
     * @return list of authoritative definitions
     */
    public List<BitolAuthoritativeDefinition> getAuthoritativeDefinitions()
    {
        return authoritativeDefinitions;
    }


    /**
     * Set up the list of links to sources that provide more details about this element.
     *
     * @param authoritativeDefinitions list of authoritative definitions
     */
    public void setAuthoritativeDefinitions(List<BitolAuthoritativeDefinition> authoritativeDefinitions)
    {
        this.authoritativeDefinitions = authoritativeDefinitions;
    }


    /**
     * Return the stable identifier of this port.
     *
     * @return String
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set up the stable identifier of this port.
     *
     * @param id String
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Return whether this port is deprecated and should not be used in new implementations.
     *
     * @return Boolean
     */
    public Boolean getDeprecated()
    {
        return deprecated;
    }


    /**
     * Set up whether this port is deprecated and should not be used in new implementations.
     *
     * @param deprecated Boolean
     */
    public void setDeprecated(Boolean deprecated)
    {
        this.deprecated = deprecated;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataProductManagementPort{" +
                       "name='" + name + '\'' +
                       ", content='" + content + '\'' +
                       ", type='" + type + '\'' +
                       ", url='" + url + '\'' +
                       ", channel='" + channel + '\'' +
                       ", description='" + description + '\'' +
                       ", tags=" + tags +
                       ", customProperties=" + customProperties +
                       ", authoritativeDefinitions=" + authoritativeDefinitions +
                       ", id=" + id +
                       ", deprecated=" + deprecated +
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
        DataProductManagementPort that = (DataProductManagementPort) objectToCompare;
        return Objects.equals(name, that.name) &&
                       Objects.equals(content, that.content) &&
                       Objects.equals(type, that.type) &&
                       Objects.equals(url, that.url) &&
                       Objects.equals(channel, that.channel) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(tags, that.tags) &&
                       Objects.equals(customProperties, that.customProperties) &&
                       Objects.equals(authoritativeDefinitions, that.authoritativeDefinitions) &&
                       Objects.equals(id, that.id) &&
                       Objects.equals(deprecated, that.deprecated);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(name, content, type, url, channel, description, tags, customProperties, authoritativeDefinitions, id, deprecated);
    }
}
