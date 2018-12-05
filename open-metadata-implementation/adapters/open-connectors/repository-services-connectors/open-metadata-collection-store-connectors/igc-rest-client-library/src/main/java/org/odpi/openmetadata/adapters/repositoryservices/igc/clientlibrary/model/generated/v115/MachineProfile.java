/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'machine_profile' asset type in IGC, displayed as 'Machine Profile' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MachineProfile extends MainObject {

    public static final String IGC_TYPE_ID = "machine_profile";

    /**
     * The 'transformation_project' property, displayed as 'Transformation Project' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TransformationProject} object.
     */
    protected Reference transformation_project;

    /**
     * The 'host' property, displayed as 'Host' in the IGC UI.
     */
    protected String host;

    /**
     * The 'platform_type' property, displayed as 'Platform Type' in the IGC UI.
     */
    protected String platform_type;

    /**
     * The 'port_number' property, displayed as 'Port Number' in the IGC UI.
     */
    protected Number port_number;

    /**
     * The 'transfer_type' property, displayed as 'Transfer Type' in the IGC UI.
     */
    protected String transfer_type;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #transformation_project */ @JsonProperty("transformation_project")  public Reference getTransformationProject() { return this.transformation_project; }
    /** @see #transformation_project */ @JsonProperty("transformation_project")  public void setTransformationProject(Reference transformation_project) { this.transformation_project = transformation_project; }

    /** @see #host */ @JsonProperty("host")  public String getHost() { return this.host; }
    /** @see #host */ @JsonProperty("host")  public void setHost(String host) { this.host = host; }

    /** @see #platform_type */ @JsonProperty("platform_type")  public String getPlatformType() { return this.platform_type; }
    /** @see #platform_type */ @JsonProperty("platform_type")  public void setPlatformType(String platform_type) { this.platform_type = platform_type; }

    /** @see #port_number */ @JsonProperty("port_number")  public Number getPortNumber() { return this.port_number; }
    /** @see #port_number */ @JsonProperty("port_number")  public void setPortNumber(Number port_number) { this.port_number = port_number; }

    /** @see #transfer_type */ @JsonProperty("transfer_type")  public String getTransferType() { return this.transfer_type; }
    /** @see #transfer_type */ @JsonProperty("transfer_type")  public void setTransferType(String transfer_type) { this.transfer_type = transfer_type; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isMachineProfile(Object obj) { return (obj.getClass() == MachineProfile.class); }

}
