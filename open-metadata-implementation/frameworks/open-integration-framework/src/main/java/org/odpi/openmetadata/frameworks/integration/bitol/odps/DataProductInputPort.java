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
 * This class represents an input port of an Open Data Product Standard (ODPS) data product. An input port
 * identifies data that the data product consumes, described by an Open Data Contract Standard (ODCS) data
 * contract.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataProductInputPort
{
    private String                             name = null;
    private String                             version = null;
    private String                             contractId = null;
    private List<String>                       tags = null;
    private List<BitolCustomProperty>          customProperties = null;
    private List<BitolAuthoritativeDefinition> authoritativeDefinitions = null;


    /**
     * Default constructor
     */
    public DataProductInputPort()
    {
    }


    /**
     * Return the name of the input port.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the input port.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the version of the input port.
     *
     * @return string version
     */
    public String getVersion()
    {
        return version;
    }


    /**
     * Set up the version of the input port.
     *
     * @param version string version
     */
    public void setVersion(String version)
    {
        this.version = version;
    }


    /**
     * Return the identifier of the data contract that describes the data consumed through this port.
     *
     * @return string contract identifier
     */
    public String getContractId()
    {
        return contractId;
    }


    /**
     * Set up the identifier of the data contract that describes the data consumed through this port.
     *
     * @param contractId string contract identifier
     */
    public void setContractId(String contractId)
    {
        this.contractId = contractId;
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
        return "DataProductInputPort{" +
                       "name='" + name + '\'' +
                       ", version='" + version + '\'' +
                       ", contractId='" + contractId + '\'' +
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
        DataProductInputPort that = (DataProductInputPort) objectToCompare;
        return Objects.equals(name, that.name) &&
                       Objects.equals(version, that.version) &&
                       Objects.equals(contractId, that.contractId) &&
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
        return Objects.hash(name, version, contractId, tags, customProperties, authoritativeDefinitions);
    }
}
