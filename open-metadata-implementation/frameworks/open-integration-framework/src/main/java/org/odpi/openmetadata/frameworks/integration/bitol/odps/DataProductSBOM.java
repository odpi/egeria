/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odps;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolCustomProperty;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolAuthoritativeDefinition;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a link to a software bill of materials (SBOM) for an output port of an Open Data Product
 * Standard (ODPS) data product.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataProductSBOM
{
    private String type = null;
    private String url = null;
    private String id = null;
    private List<String> tags = null;
    private List<BitolCustomProperty> customProperties = null;
    private List<BitolAuthoritativeDefinition> authoritativeDefinitions = null;


    /**
     * Default constructor
     */
    public DataProductSBOM()
    {
    }


    /**
     * Return the type of SBOM reference.  The default is external.
     *
     * @return string type name
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the type of SBOM reference.  The default is external.
     *
     * @param type string type name
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Return the URL of the SBOM.
     *
     * @return string URL
     */
    public String getUrl()
    {
        return url;
    }


    /**
     * Set up the URL of the SBOM.
     *
     * @param url string URL
     */
    public void setUrl(String url)
    {
        this.url = url;
    }


    /**
     * Return the stable identifier of this SBOM reference.
     *
     * @return String
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set up the stable identifier of this SBOM reference.
     *
     * @param id String
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Return the tags attached to this SBOM reference.
     *
     * @return List<String>
     */
    public List<String> getTags()
    {
        return tags;
    }


    /**
     * Set up the tags attached to this SBOM reference.
     *
     * @param tags List<String>
     */
    public void setTags(List<String> tags)
    {
        this.tags = tags;
    }


    /**
     * Return the custom properties of this SBOM reference.
     *
     * @return List<BitolCustomProperty>
     */
    public List<BitolCustomProperty> getCustomProperties()
    {
        return customProperties;
    }


    /**
     * Set up the custom properties of this SBOM reference.
     *
     * @param customProperties List<BitolCustomProperty>
     */
    public void setCustomProperties(List<BitolCustomProperty> customProperties)
    {
        this.customProperties = customProperties;
    }


    /**
     * Return links to the authoritative build record for this SBOM.
     *
     * @return List<BitolAuthoritativeDefinition>
     */
    public List<BitolAuthoritativeDefinition> getAuthoritativeDefinitions()
    {
        return authoritativeDefinitions;
    }


    /**
     * Set up links to the authoritative build record for this SBOM.
     *
     * @param authoritativeDefinitions List<BitolAuthoritativeDefinition>
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
        return "DataProductSBOM{" +
                       "type='" + type + '\'' +
                       ", url='" + url + '\'' +
                       ", id=" + id +
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
        DataProductSBOM that = (DataProductSBOM) objectToCompare;
        return Objects.equals(type, that.type) &&
                       Objects.equals(url, that.url) &&
                       Objects.equals(id, that.id) &&
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
        return Objects.hash(type, url, id, tags, customProperties, authoritativeDefinitions);
    }
}
