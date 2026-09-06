/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolSupportChannel;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolTeam;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents an Open Data Contract Standard (ODCS) data contract as defined by the Bitol project
 * (https://bitol-io.github.io/open-data-contract-standard/). It is used internally in Egeria to pass this
 * information to the integration connectors and view services. A data contract describes a dataset: its schema,
 * data quality checks, service level agreement, support channels, pricing, team, access roles and the servers
 * where it resides. The fundamentals shared with the Open Data Product Standard are inherited from
 * BitolDocument.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataContract extends BitolDocument
{
    /**
     * The version of the Open Data Contract Standard that this bean is aligned with.  It is the default apiVersion for new documents.
     */
    public static final String CURRENT_API_VERSION = "v3.1.0";


    private String                         dataProduct = null;
    private List<DataContractServer>       servers = null;
    private List<DataContractSchemaObject> schema = null;
    private List<BitolSupportChannel>      support = null;
    private DataContractPricing            price = null;
    private BitolTeam                      team = null;
    private List<DataContractRole>         roles = null;
    private String                         slaDefaultElement = null;
    private List<DataContractSLAProperty>  slaProperties = null;
    private String                         contractCreatedTs = null;


    /**
     * Default constructor
     */
    public DataContract()
    {
        super(DATA_CONTRACT_KIND, CURRENT_API_VERSION);
    }


    /**
     * Return the name of the data product that this contract belongs to (deprecated since v3.1.0 in favour of the ODPS document).
     *
     * @return string name
     */
    public String getDataProduct()
    {
        return dataProduct;
    }


    /**
     * Set up the name of the data product that this contract belongs to (deprecated since v3.1.0 in favour of the ODPS document).
     *
     * @param dataProduct string name
     */
    public void setDataProduct(String dataProduct)
    {
        this.dataProduct = dataProduct;
    }


    /**
     * Return the list of servers where the data resides.
     *
     * @return list of servers
     */
    public List<DataContractServer> getServers()
    {
        return servers;
    }


    /**
     * Set up the list of servers where the data resides.
     *
     * @param servers list of servers
     */
    public void setServers(List<DataContractServer> servers)
    {
        this.servers = servers;
    }


    /**
     * Return the list of schema objects (tables, topics, files, ...) described by the contract.
     *
     * @return list of schema objects
     */
    public List<DataContractSchemaObject> getSchema()
    {
        return schema;
    }


    /**
     * Set up the list of schema objects (tables, topics, files, ...) described by the contract.
     *
     * @param schema list of schema objects
     */
    public void setSchema(List<DataContractSchemaObject> schema)
    {
        this.schema = schema;
    }


    /**
     * Return the list of support and communication channels.
     *
     * @return list of support channels
     */
    public List<BitolSupportChannel> getSupport()
    {
        return support;
    }


    /**
     * Set up the list of support and communication channels.
     *
     * @param support list of support channels
     */
    public void setSupport(List<BitolSupportChannel> support)
    {
        this.support = support;
    }


    /**
     * Return the pricing information.
     *
     * @return pricing structure
     */
    public DataContractPricing getPrice()
    {
        return price;
    }


    /**
     * Set up the pricing information.
     *
     * @param price pricing structure
     */
    public void setPrice(DataContractPricing price)
    {
        this.price = price;
    }


    /**
     * Return the team responsible for the data contract.  Older documents supply a bare list of members; BitolModule (used by BitolDocumentFormatter) accepts this form and wraps it into a team.
     *
     * @return team structure
     */
    public BitolTeam getTeam()
    {
        return team;
    }


    /**
     * Set up the team responsible for the data contract.  Older documents supply a bare list of members; BitolModule (used by BitolDocumentFormatter) accepts this form and wraps it into a team.
     *
     * @param team team structure
     */
    public void setTeam(BitolTeam team)
    {
        this.team = team;
    }


    /**
     * Return the list of access roles that provide access to the data.
     *
     * @return list of roles
     */
    public List<DataContractRole> getRoles()
    {
        return roles;
    }


    /**
     * Set up the list of access roles that provide access to the data.
     *
     * @param roles list of roles
     */
    public void setRoles(List<DataContractRole> roles)
    {
        this.roles = roles;
    }


    /**
     * Return the default schema element that SLA properties refer to (deprecated since v3.1.0).
     *
     * @return string element reference
     */
    public String getSlaDefaultElement()
    {
        return slaDefaultElement;
    }


    /**
     * Set up the default schema element that SLA properties refer to (deprecated since v3.1.0).
     *
     * @param slaDefaultElement string element reference
     */
    public void setSlaDefaultElement(String slaDefaultElement)
    {
        this.slaDefaultElement = slaDefaultElement;
    }


    /**
     * Return the list of service level agreement properties.
     *
     * @return list of SLA properties
     */
    public List<DataContractSLAProperty> getSlaProperties()
    {
        return slaProperties;
    }


    /**
     * Set up the list of service level agreement properties.
     *
     * @param slaProperties list of SLA properties
     */
    public void setSlaProperties(List<DataContractSLAProperty> slaProperties)
    {
        this.slaProperties = slaProperties;
    }


    /**
     * Return the timestamp (UTC, ISO 8601) when the data contract was created.
     *
     * @return string timestamp
     */
    public String getContractCreatedTs()
    {
        return contractCreatedTs;
    }


    /**
     * Set up the timestamp (UTC, ISO 8601) when the data contract was created.
     *
     * @param contractCreatedTs string timestamp
     */
    public void setContractCreatedTs(String contractCreatedTs)
    {
        this.contractCreatedTs = contractCreatedTs;
    }


    /**
     * The prefixes of the apiVersion values that this bean is able to represent.  Older (v2.x) documents have a
     * different top-level structure and are not supported.
     */
    public static final List<String> SUPPORTED_API_VERSION_PREFIXES = List.of("v3.");


    /**
     * Return whether the apiVersion of this document is one that this bean is able to represent.
     *
     * @return boolean flag
     */
    @Override
    public boolean hasSupportedApiVersion()
    {
        if (getApiVersion() != null)
        {
            for (String prefix : SUPPORTED_API_VERSION_PREFIXES)
            {
                if (getApiVersion().startsWith(prefix))
                {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataContract{" +
                       "dataProduct='" + dataProduct + '\'' +
                       ", servers=" + servers +
                       ", schema=" + schema +
                       ", support=" + support +
                       ", price=" + price +
                       ", team=" + team +
                       ", roles=" + roles +
                       ", slaDefaultElement='" + slaDefaultElement + '\'' +
                       ", slaProperties=" + slaProperties +
                       ", contractCreatedTs='" + contractCreatedTs + '\'' +
                       "} " + super.toString();
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        DataContract that = (DataContract) objectToCompare;
        return Objects.equals(dataProduct, that.dataProduct) &&
                       Objects.equals(servers, that.servers) &&
                       Objects.equals(schema, that.schema) &&
                       Objects.equals(support, that.support) &&
                       Objects.equals(price, that.price) &&
                       Objects.equals(team, that.team) &&
                       Objects.equals(roles, that.roles) &&
                       Objects.equals(slaDefaultElement, that.slaDefaultElement) &&
                       Objects.equals(slaProperties, that.slaProperties) &&
                       Objects.equals(contractCreatedTs, that.contractCreatedTs);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), dataProduct, servers, schema, support, price, team, roles, slaDefaultElement, slaProperties, contractCreatedTs);
    }
}
