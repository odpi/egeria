/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odps;

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
 * This class represents an Open Data Product Standard (ODPS) data product as defined by the Bitol project
 * (https://bitol-io.github.io/open-data-product-standard/). It is used internally in Egeria to pass this
 * information to the integration connectors and view services. A data product describes the data it consumes
 * (input ports) and provides (output ports), each described by an Open Data Contract Standard (ODCS) data
 * contract, along with its management ports, support channels and team. The fundamentals shared with the Open
 * Data Contract Standard are inherited from BitolDocument.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataProduct extends BitolDocument
{
    /**
     * The version of the Open Data Product Standard that this bean is aligned with.  It is the default apiVersion for new documents.
     */
    public static final String CURRENT_API_VERSION = "v1.0.0";


    private List<DataProductInputPort>      inputPorts = null;
    private List<DataProductOutputPort>     outputPorts = null;
    private List<DataProductManagementPort> managementPorts = null;
    private List<BitolSupportChannel>       support = null;
    private BitolTeam                       team = null;
    private String                          productCreatedTs = null;


    /**
     * Default constructor
     */
    public DataProduct()
    {
        super(DATA_PRODUCT_KIND, CURRENT_API_VERSION);
    }


    /**
     * Return the list of input ports (data consumed by the data product).
     *
     * @return list of input ports
     */
    public List<DataProductInputPort> getInputPorts()
    {
        return inputPorts;
    }


    /**
     * Set up the list of input ports (data consumed by the data product).
     *
     * @param inputPorts list of input ports
     */
    public void setInputPorts(List<DataProductInputPort> inputPorts)
    {
        this.inputPorts = inputPorts;
    }


    /**
     * Return the list of output ports (data provided by the data product).
     *
     * @return list of output ports
     */
    public List<DataProductOutputPort> getOutputPorts()
    {
        return outputPorts;
    }


    /**
     * Set up the list of output ports (data provided by the data product).
     *
     * @param outputPorts list of output ports
     */
    public void setOutputPorts(List<DataProductOutputPort> outputPorts)
    {
        this.outputPorts = outputPorts;
    }


    /**
     * Return the list of management ports.
     *
     * @return list of management ports
     */
    public List<DataProductManagementPort> getManagementPorts()
    {
        return managementPorts;
    }


    /**
     * Set up the list of management ports.
     *
     * @param managementPorts list of management ports
     */
    public void setManagementPorts(List<DataProductManagementPort> managementPorts)
    {
        this.managementPorts = managementPorts;
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
     * Return the team responsible for the data product.
     *
     * @return team structure
     */
    public BitolTeam getTeam()
    {
        return team;
    }


    /**
     * Set up the team responsible for the data product.
     *
     * @param team team structure
     */
    public void setTeam(BitolTeam team)
    {
        this.team = team;
    }


    /**
     * Return the timestamp (UTC, ISO 8601) when the data product was created.
     *
     * @return string timestamp
     */
    public String getProductCreatedTs()
    {
        return productCreatedTs;
    }


    /**
     * Set up the timestamp (UTC, ISO 8601) when the data product was created.
     *
     * @param productCreatedTs string timestamp
     */
    public void setProductCreatedTs(String productCreatedTs)
    {
        this.productCreatedTs = productCreatedTs;
    }


    /**
     * The prefixes of the apiVersion values that this bean is able to represent.  The v0.9.0 pre-release has the same
     * structure as v1.0.0.
     */
    public static final List<String> SUPPORTED_API_VERSION_PREFIXES = List.of("v0.9.", "v1.");


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
        return "DataProduct{" +
                       "inputPorts=" + inputPorts +
                       ", outputPorts=" + outputPorts +
                       ", managementPorts=" + managementPorts +
                       ", support=" + support +
                       ", team=" + team +
                       ", productCreatedTs='" + productCreatedTs + '\'' +
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
        DataProduct that = (DataProduct) objectToCompare;
        return Objects.equals(inputPorts, that.inputPorts) &&
                       Objects.equals(outputPorts, that.outputPorts) &&
                       Objects.equals(managementPorts, that.managementPorts) &&
                       Objects.equals(support, that.support) &&
                       Objects.equals(team, that.team) &&
                       Objects.equals(productCreatedTs, that.productCreatedTs);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), inputPorts, outputPorts, managementPorts, support, team, productCreatedTs);
    }
}
