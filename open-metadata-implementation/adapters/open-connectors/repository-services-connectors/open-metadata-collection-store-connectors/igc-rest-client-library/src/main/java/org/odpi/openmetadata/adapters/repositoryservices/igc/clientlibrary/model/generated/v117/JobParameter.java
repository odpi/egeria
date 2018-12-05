/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'job_parameter' asset type in IGC, displayed as 'Job Parameter' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class JobParameter extends MainObject {

    public static final String IGC_TYPE_ID = "job_parameter";

    /**
     * The 'job_run' property, displayed as 'Job Run' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link JobRun} object.
     */
    protected Reference job_run;

    /**
     * The 'value' property, displayed as 'Value' in the IGC UI.
     */
    protected String value;


    /** @see #job_run */ @JsonProperty("job_run")  public Reference getJobRun() { return this.job_run; }
    /** @see #job_run */ @JsonProperty("job_run")  public void setJobRun(Reference job_run) { this.job_run = job_run; }

    /** @see #value */ @JsonProperty("value")  public String getValue() { return this.value; }
    /** @see #value */ @JsonProperty("value")  public void setValue(String value) { this.value = value; }


    public static final Boolean isJobParameter(Object obj) { return (obj.getClass() == JobParameter.class); }

}
