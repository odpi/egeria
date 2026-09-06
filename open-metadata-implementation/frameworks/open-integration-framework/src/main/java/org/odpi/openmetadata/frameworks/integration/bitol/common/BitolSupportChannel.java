/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a support or communication channel for a Bitol Open Data Contract Standard (ODCS) data
 * contract or Open Data Product Standard (ODPS) data product.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BitolSupportChannel
{
    private String                             id = null;
    private String                             channel = null;
    private String                             url = null;
    private String                             description = null;
    private String                             tool = null;
    private String                             scope = null;
    private String                             invitationUrl = null;
    private List<String>                       tags = null;
    private List<BitolCustomProperty>          customProperties = null;
    private List<BitolAuthoritativeDefinition> authoritativeDefinitions = null;


    /**
     * Default constructor
     */
    public BitolSupportChannel()
    {
    }


    /**
     * Return the stable identifier of this element (alphanumeric, hyphen and underscore characters only).
     *
     * @return string identifier
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set up the stable identifier of this element (alphanumeric, hyphen and underscore characters only).
     *
     * @param id string identifier
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Return the name or identifier of the channel.
     *
     * @return string name
     */
    public String getChannel()
    {
        return channel;
    }


    /**
     * Set up the name or identifier of the channel.
     *
     * @param channel string name
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }


    /**
     * Return the access URL for the channel (https, mailto, etc).
     *
     * @return string URL
     */
    public String getUrl()
    {
        return url;
    }


    /**
     * Set up the access URL for the channel (https, mailto, etc).
     *
     * @param url string URL
     */
    public void setUrl(String url)
    {
        this.url = url;
    }


    /**
     * Return the free text description of the channel.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the free text description of the channel.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the name of the tool that hosts the channel.  Standard values are defined in BitolSupportTool.
     *
     * @return string tool name
     */
    public String getTool()
    {
        return tool;
    }


    /**
     * Set up the name of the tool that hosts the channel.  Standard values are defined in BitolSupportTool.
     *
     * @param tool string tool name
     */
    public void setTool(String tool)
    {
        this.tool = tool;
    }


    /**
     * Return the scope of the channel.  Standard values are defined in BitolSupportScope.
     *
     * @return string scope name
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Set up the scope of the channel.  Standard values are defined in BitolSupportScope.
     *
     * @param scope string scope name
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }


    /**
     * Return the URL used to request access to, or subscribe to, the channel.
     *
     * @return string URL
     */
    public String getInvitationUrl()
    {
        return invitationUrl;
    }


    /**
     * Set up the URL used to request access to, or subscribe to, the channel.
     *
     * @param invitationUrl string URL
     */
    public void setInvitationUrl(String invitationUrl)
    {
        this.invitationUrl = invitationUrl;
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BitolSupportChannel{" +
                       "id='" + id + '\'' +
                       ", channel='" + channel + '\'' +
                       ", url='" + url + '\'' +
                       ", description='" + description + '\'' +
                       ", tool='" + tool + '\'' +
                       ", scope='" + scope + '\'' +
                       ", invitationUrl='" + invitationUrl + '\'' +
                       ", tags=" + tags +
                       ", customProperties=" + customProperties +
                       ", authoritativeDefinitions=" + authoritativeDefinitions +
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
        BitolSupportChannel that = (BitolSupportChannel) objectToCompare;
        return Objects.equals(id, that.id) &&
                       Objects.equals(channel, that.channel) &&
                       Objects.equals(url, that.url) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(tool, that.tool) &&
                       Objects.equals(scope, that.scope) &&
                       Objects.equals(invitationUrl, that.invitationUrl) &&
                       Objects.equals(tags, that.tags) &&
                       Objects.equals(customProperties, that.customProperties) &&
                       Objects.equals(authoritativeDefinitions, that.authoritativeDefinitions);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(id, channel, url, description, tool, scope, invitationUrl, tags, customProperties, authoritativeDefinitions);
    }
}
