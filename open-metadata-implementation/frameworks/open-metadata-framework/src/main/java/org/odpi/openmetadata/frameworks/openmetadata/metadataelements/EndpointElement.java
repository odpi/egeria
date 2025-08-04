/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EndpointElement contains the properties and header for an endpoint retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EndpointElement extends OpenMetadataRootElement
{
    private RelatedMetadataElementSummary       serverEndpoint    = null;
    private List<RelatedMetadataElementSummary> deployedAPIs      = null;
    private List<RelatedMetadataElementSummary> connections       = null;
    private List<RelatedMetadataElementSummary> visibleInNetworks = null;


    /**
     * Default constructor
     */
    public EndpointElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public EndpointElement(EndpointElement template)
    {
        super(template);

        if (template != null)
        {
            serverEndpoint = template.getServerEndpoint();
            deployedAPIs = template.getDeployedAPIs();
            connections = template.getConnections();
            visibleInNetworks = template.getVisibleInNetworks();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public EndpointElement(OpenMetadataRootElement template)
    {
        super(template);
    }


    /**
     * Return the IT infrastructure asset that this endpoint belongs to.
     *
     * @return related elements
     */
    public RelatedMetadataElementSummary getServerEndpoint()
    {
        return serverEndpoint;
    }


    /**
     * Set up the infrastructure asset that this endpoint belongs to.
     *
     * @param serverEndpoint related elements
     */
    public void setServerEndpoint(RelatedMetadataElementSummary serverEndpoint)
    {
        this.serverEndpoint = serverEndpoint;
    }


    /**
     * Return the list of APIs supported by this endpoint.
     *
     * @return related elements
     */
    public List<RelatedMetadataElementSummary> getDeployedAPIs()
    {
        return deployedAPIs;
    }


    /**
     * Set up the list of APIs supported by this endpoint.
     *
     * @param deployedAPIs related elements
     */
    public void setDeployedAPIs(List<RelatedMetadataElementSummary> deployedAPIs)
    {
        this.deployedAPIs = deployedAPIs;
    }


    /**
     * Return the list of connections that connect to this endpoint.
     *
     * @return related elements
     */
    public List<RelatedMetadataElementSummary> getConnections()
    {
        return connections;
    }


    /**
     * Set up the list of connections that connect to this endpoint.
     *
     * @param connections related elements
     */
    public void setConnections(List<RelatedMetadataElementSummary> connections)
    {
        this.connections = connections;
    }


    /**
     * Return the list of networks that this endpoint is visible in.
     *
     * @return related elements
     */
    public List<RelatedMetadataElementSummary> getVisibleInNetworks()
    {
        return visibleInNetworks;
    }


    /**
     * Set up the list of networks that this endpoint is visible in.
     *
     * @param visibleInNetworks related elements
     */
    public void setVisibleInNetworks(List<RelatedMetadataElementSummary> visibleInNetworks)
    {
        this.visibleInNetworks = visibleInNetworks;
    }

    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "EndpointElement{" +
                "serverEndpoint=" + serverEndpoint +
                ", deployedAPIs=" + deployedAPIs +
                ", connections=" + connections +
                ", visibleInNetworks=" + visibleInNetworks +
                "} " + super.toString();
    }



    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        EndpointElement that = (EndpointElement) objectToCompare;
        return Objects.equals(serverEndpoint, that.serverEndpoint) && Objects.equals(deployedAPIs, that.deployedAPIs) && Objects.equals(connections, that.connections) && Objects.equals(visibleInNetworks, that.visibleInNetworks);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), serverEndpoint, deployedAPIs, connections, visibleInNetworks);
    }
}
