/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'host_(engine)' asset type in IGC, displayed as 'Host (Engine)' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class HostEngine extends MainObject {

    public static final String IGC_TYPE_ID = "host_(engine)";

    /**
     * The 'transformation_projects' property, displayed as 'Transformation Projects' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TransformationProject} objects.
     */
    protected ReferenceList transformation_projects;

    /**
     * The 'data_connections' property, displayed as 'Data Connectors' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Connector} objects.
     */
    protected ReferenceList data_connections;

    /**
     * The 'location' property, displayed as 'Location' in the IGC UI.
     */
    protected String location;

    /**
     * The 'network_node' property, displayed as 'Network Node' in the IGC UI.
     */
    protected String network_node;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #transformation_projects */ @JsonProperty("transformation_projects")  public ReferenceList getTransformationProjects() { return this.transformation_projects; }
    /** @see #transformation_projects */ @JsonProperty("transformation_projects")  public void setTransformationProjects(ReferenceList transformation_projects) { this.transformation_projects = transformation_projects; }

    /** @see #data_connections */ @JsonProperty("data_connections")  public ReferenceList getDataConnections() { return this.data_connections; }
    /** @see #data_connections */ @JsonProperty("data_connections")  public void setDataConnections(ReferenceList data_connections) { this.data_connections = data_connections; }

    /** @see #location */ @JsonProperty("location")  public String getLocation() { return this.location; }
    /** @see #location */ @JsonProperty("location")  public void setLocation(String location) { this.location = location; }

    /** @see #network_node */ @JsonProperty("network_node")  public String getNetworkNode() { return this.network_node; }
    /** @see #network_node */ @JsonProperty("network_node")  public void setNetworkNode(String network_node) { this.network_node = network_node; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isHostEngine(Object obj) { return (obj.getClass() == HostEngine.class); }

}
