/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odps;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolAuthoritativeDefinition;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolCustomProperty;

import java.util.List;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolSynonym;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolContext;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents an output port of an Open Data Product Standard (ODPS) data product. An output port
 * identifies data that the data product provides to its consumers, described by an Open Data Contract Standard
 * (ODCS) data contract. There is a separate output port entry for each version.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataProductOutputPort
{
    private String                             name = null;
    private String                             description = null;
    private String                             type = null;
    private String                             version = null;
    private String                             contractId = null;
    private List<DataProductSBOM>              sbom = null;
    private List<DataProductInputContract>     inputContracts = null;
    private List<String>                       tags = null;
    private List<BitolCustomProperty>          customProperties = null;
    private List<BitolAuthoritativeDefinition> authoritativeDefinitions = null;
    private String id = null;
    private Boolean deprecated = null;
    private List<BitolSynonym> synonyms = null;
    private BitolContext context = null;


    /**
     * Default constructor
     */
    public DataProductOutputPort()
    {
    }


    /**
     * Return the name of the output port.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the output port.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the short description of the output port.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the short description of the output port.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the type of output port, for example tables, files, topics or API.
     *
     * @return string type name
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the type of output port, for example tables, files, topics or API.
     *
     * @param type string type name
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Return the version of the output port.
     *
     * @return string version
     */
    public String getVersion()
    {
        return version;
    }


    /**
     * Set up the version of the output port.
     *
     * @param version string version
     */
    public void setVersion(String version)
    {
        this.version = version;
    }


    /**
     * Return the identifier of the data contract that describes the data provided through this port.
     *
     * @return string contract identifier
     */
    public String getContractId()
    {
        return contractId;
    }


    /**
     * Set up the identifier of the data contract that describes the data provided through this port.
     *
     * @param contractId string contract identifier
     */
    public void setContractId(String contractId)
    {
        this.contractId = contractId;
    }


    /**
     * Return the list of software bill of materials links for this version of the port.
     *
     * @return list of SBOM links
     */
    public List<DataProductSBOM> getSbom()
    {
        return sbom;
    }


    /**
     * Set up the list of software bill of materials links for this version of the port.
     *
     * @param sbom list of SBOM links
     */
    public void setSbom(List<DataProductSBOM> sbom)
    {
        this.sbom = sbom;
    }


    /**
     * Return the list of data contracts that this output port depends on.
     *
     * @return list of input contracts
     */
    public List<DataProductInputContract> getInputContracts()
    {
        return inputContracts;
    }


    /**
     * Set up the list of data contracts that this output port depends on.
     *
     * @param inputContracts list of input contracts
     */
    public void setInputContracts(List<DataProductInputContract> inputContracts)
    {
        this.inputContracts = inputContracts;
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
     * Return the alternative names for this port.
     *
     * @return List<BitolSynonym>
     */
    public List<BitolSynonym> getSynonyms()
    {
        return synonyms;
    }


    /**
     * Set up the alternative names for this port.
     *
     * @param synonyms List<BitolSynonym>
     */
    public void setSynonyms(List<BitolSynonym> synonyms)
    {
        this.synonyms = synonyms;
    }


    /**
     * Return the AI and semantic context block for this port.
     *
     * @return BitolContext
     */
    public BitolContext getContext()
    {
        return context;
    }


    /**
     * Set up the AI and semantic context block for this port.
     *
     * @param context BitolContext
     */
    public void setContext(BitolContext context)
    {
        this.context = context;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataProductOutputPort{" +
                       "name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", type='" + type + '\'' +
                       ", version='" + version + '\'' +
                       ", contractId='" + contractId + '\'' +
                       ", sbom=" + sbom +
                       ", inputContracts=" + inputContracts +
                       ", tags=" + tags +
                       ", customProperties=" + customProperties +
                       ", authoritativeDefinitions=" + authoritativeDefinitions +
                       ", id=" + id +
                       ", deprecated=" + deprecated +
                       ", synonyms=" + synonyms +
                       ", context=" + context +
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
        DataProductOutputPort that = (DataProductOutputPort) objectToCompare;
        return Objects.equals(name, that.name) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(type, that.type) &&
                       Objects.equals(version, that.version) &&
                       Objects.equals(contractId, that.contractId) &&
                       Objects.equals(sbom, that.sbom) &&
                       Objects.equals(inputContracts, that.inputContracts) &&
                       Objects.equals(tags, that.tags) &&
                       Objects.equals(customProperties, that.customProperties) &&
                       Objects.equals(authoritativeDefinitions, that.authoritativeDefinitions) &&
                       Objects.equals(id, that.id) &&
                       Objects.equals(deprecated, that.deprecated) &&
                       Objects.equals(synonyms, that.synonyms) &&
                       Objects.equals(context, that.context);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(name, description, type, version, contractId, sbom, inputContracts, tags, customProperties, authoritativeDefinitions, id, deprecated, synonyms, context);
    }
}
