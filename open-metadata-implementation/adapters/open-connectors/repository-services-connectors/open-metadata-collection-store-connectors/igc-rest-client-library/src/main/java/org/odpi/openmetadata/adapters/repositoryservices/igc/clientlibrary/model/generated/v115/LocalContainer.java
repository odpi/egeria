/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'local_container' asset type in IGC, displayed as 'Local Container' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class LocalContainer extends MainObject {

    public static final String IGC_TYPE_ID = "local_container";

    /**
     * The 'job_or_container' property, displayed as 'Job or Container' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList job_or_container;

    /**
     * The 'stages' property, displayed as 'Stages' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Stage} objects.
     */
    protected ReferenceList stages;

    /**
     * The 'referenced_by_stages' property, displayed as 'Referenced by Stages' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Stage} objects.
     */
    protected ReferenceList referenced_by_stages;

    /**
     * The 'referenced_by_containers' property, displayed as 'Referenced by Containers' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ReferencedContainer} objects.
     */
    protected ReferenceList referenced_by_containers;

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
     * The 'annotations' property, displayed as 'Annotations' in the IGC UI.
     */
    protected ArrayList<String> annotations;


    /** @see #job_or_container */ @JsonProperty("job_or_container")  public ReferenceList getJobOrContainer() { return this.job_or_container; }
    /** @see #job_or_container */ @JsonProperty("job_or_container")  public void setJobOrContainer(ReferenceList job_or_container) { this.job_or_container = job_or_container; }

    /** @see #stages */ @JsonProperty("stages")  public ReferenceList getStages() { return this.stages; }
    /** @see #stages */ @JsonProperty("stages")  public void setStages(ReferenceList stages) { this.stages = stages; }

    /** @see #referenced_by_stages */ @JsonProperty("referenced_by_stages")  public ReferenceList getReferencedByStages() { return this.referenced_by_stages; }
    /** @see #referenced_by_stages */ @JsonProperty("referenced_by_stages")  public void setReferencedByStages(ReferenceList referenced_by_stages) { this.referenced_by_stages = referenced_by_stages; }

    /** @see #referenced_by_containers */ @JsonProperty("referenced_by_containers")  public ReferenceList getReferencedByContainers() { return this.referenced_by_containers; }
    /** @see #referenced_by_containers */ @JsonProperty("referenced_by_containers")  public void setReferencedByContainers(ReferenceList referenced_by_containers) { this.referenced_by_containers = referenced_by_containers; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #version */ @JsonProperty("version")  public String getVersion() { return this.version; }
    /** @see #version */ @JsonProperty("version")  public void setVersion(String version) { this.version = version; }

    /** @see #annotations */ @JsonProperty("annotations")  public ArrayList<String> getAnnotations() { return this.annotations; }
    /** @see #annotations */ @JsonProperty("annotations")  public void setAnnotations(ArrayList<String> annotations) { this.annotations = annotations; }


    public static final Boolean isLocalContainer(Object obj) { return (obj.getClass() == LocalContainer.class); }

}
