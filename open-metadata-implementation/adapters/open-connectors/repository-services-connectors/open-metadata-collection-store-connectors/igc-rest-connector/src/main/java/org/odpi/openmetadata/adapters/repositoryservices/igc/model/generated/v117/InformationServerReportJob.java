/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'information_server_report_(job)' asset type in IGC, displayed as 'Information Server Report (Job)' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationServerReportJob extends MainObject {

    public static final String IGC_TYPE_ID = "information_server_report_(job)";

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'creator' property, displayed as 'Creator' in the IGC UI.
     */
    protected String creator;

    /**
     * The 'product' property, displayed as 'Product' in the IGC UI.
     */
    protected String product;

    /**
     * The 'host_(engine)' property, displayed as 'Host (Engine)' in the IGC UI.
     */
    @JsonProperty("host_(engine)") protected String host__engine_;

    /**
     * The 'transformation_project' property, displayed as 'Transformation Project' in the IGC UI.
     */
    protected String transformation_project;

    /**
     * The 'job' property, displayed as 'Job' in the IGC UI.
     */
    protected ArrayList<String> job;


    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #creator */ @JsonProperty("creator")  public String getCreator() { return this.creator; }
    /** @see #creator */ @JsonProperty("creator")  public void setCreator(String creator) { this.creator = creator; }

    /** @see #product */ @JsonProperty("product")  public String getProduct() { return this.product; }
    /** @see #product */ @JsonProperty("product")  public void setProduct(String product) { this.product = product; }

    /** @see #host__engine_ */ @JsonProperty("host_(engine)")  public String getHostEngine() { return this.host__engine_; }
    /** @see #host__engine_ */ @JsonProperty("host_(engine)")  public void setHostEngine(String host__engine_) { this.host__engine_ = host__engine_; }

    /** @see #transformation_project */ @JsonProperty("transformation_project")  public String getTransformationProject() { return this.transformation_project; }
    /** @see #transformation_project */ @JsonProperty("transformation_project")  public void setTransformationProject(String transformation_project) { this.transformation_project = transformation_project; }

    /** @see #job */ @JsonProperty("job")  public ArrayList<String> getJob() { return this.job; }
    /** @see #job */ @JsonProperty("job")  public void setJob(ArrayList<String> job) { this.job = job; }


    public static final Boolean isInformationServerReportJob(Object obj) { return (obj.getClass() == InformationServerReportJob.class); }

}
