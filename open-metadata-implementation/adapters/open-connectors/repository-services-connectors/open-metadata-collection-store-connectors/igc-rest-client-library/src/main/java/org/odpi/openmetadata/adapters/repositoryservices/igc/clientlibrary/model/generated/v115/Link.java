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
 * POJO for the 'link' asset type in IGC, displayed as 'Link' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Link extends MainObject {

    public static final String IGC_TYPE_ID = "link";

    /**
     * The 'job_or_container' property, displayed as 'Job or Container' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList job_or_container;

    /**
     * The 'stage_columns' property, displayed as 'Stage Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataItem} objects.
     */
    protected ReferenceList stage_columns;

    /**
     * The 'input_stages' property, displayed as 'Input Stages' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Stage} object.
     */
    protected Reference input_stages;

    /**
     * The 'output_stages' property, displayed as 'Output Stages' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Stage} object.
     */
    protected Reference output_stages;


    /** @see #job_or_container */ @JsonProperty("job_or_container")  public ReferenceList getJobOrContainer() { return this.job_or_container; }
    /** @see #job_or_container */ @JsonProperty("job_or_container")  public void setJobOrContainer(ReferenceList job_or_container) { this.job_or_container = job_or_container; }

    /** @see #stage_columns */ @JsonProperty("stage_columns")  public ReferenceList getStageColumns() { return this.stage_columns; }
    /** @see #stage_columns */ @JsonProperty("stage_columns")  public void setStageColumns(ReferenceList stage_columns) { this.stage_columns = stage_columns; }

    /** @see #input_stages */ @JsonProperty("input_stages")  public Reference getInputStages() { return this.input_stages; }
    /** @see #input_stages */ @JsonProperty("input_stages")  public void setInputStages(Reference input_stages) { this.input_stages = input_stages; }

    /** @see #output_stages */ @JsonProperty("output_stages")  public Reference getOutputStages() { return this.output_stages; }
    /** @see #output_stages */ @JsonProperty("output_stages")  public void setOutputStages(Reference output_stages) { this.output_stages = output_stages; }


    public static final Boolean isLink(Object obj) { return (obj.getClass() == Link.class); }

}
