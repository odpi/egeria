/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'referenced_container' asset type in IGC, displayed as 'Referenced Container' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReferencedContainer extends MainObject {

    public static final String IGC_TYPE_ID = "referenced_container";

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>SERVER (displayed in the UI as 'SERVER')</li>
     *     <li>MAINFRAME (displayed in the UI as 'MAINFRAME')</li>
     *     <li>SEQUENCE (displayed in the UI as 'SEQUENCE')</li>
     *     <li>PARALLEL (displayed in the UI as 'PARALLEL')</li>
     * </ul>
     */
    protected String type;

    /**
     * The 'version' property, displayed as 'Version' in the IGC UI.
     */
    protected String version;

    /**
     * The 'stages' property, displayed as 'Stages' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Stage} objects.
     */
    protected ReferenceList stages;

    /**
     * The 'local_containers' property, displayed as 'Local Containers' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link LocalContainer} object.
     */
    protected Reference local_containers;

    /**
     * The 'shared_containers' property, displayed as 'Shared Containers' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link SharedContainer} object.
     */
    protected Reference shared_containers;


    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #version */ @JsonProperty("version")  public String getVersion() { return this.version; }
    /** @see #version */ @JsonProperty("version")  public void setVersion(String version) { this.version = version; }

    /** @see #stages */ @JsonProperty("stages")  public ReferenceList getStages() { return this.stages; }
    /** @see #stages */ @JsonProperty("stages")  public void setStages(ReferenceList stages) { this.stages = stages; }

    /** @see #local_containers */ @JsonProperty("local_containers")  public Reference getLocalContainers() { return this.local_containers; }
    /** @see #local_containers */ @JsonProperty("local_containers")  public void setLocalContainers(Reference local_containers) { this.local_containers = local_containers; }

    /** @see #shared_containers */ @JsonProperty("shared_containers")  public Reference getSharedContainers() { return this.shared_containers; }
    /** @see #shared_containers */ @JsonProperty("shared_containers")  public void setSharedContainers(Reference shared_containers) { this.shared_containers = shared_containers; }


    public static final Boolean isReferencedContainer(Object obj) { return (obj.getClass() == ReferencedContainer.class); }

}
