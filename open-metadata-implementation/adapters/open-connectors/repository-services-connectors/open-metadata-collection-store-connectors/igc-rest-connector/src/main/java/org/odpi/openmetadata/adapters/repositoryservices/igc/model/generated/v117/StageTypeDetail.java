/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'stage_type_detail' asset type in IGC, displayed as 'Stage Type Detail' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class StageTypeDetail extends MainObject {

    public static final String IGC_TYPE_ID = "stage_type_detail";

    /**
     * The 'of_stage_type' property, displayed as 'Stage Type' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link StageType} object.
     */
    protected Reference of_stage_type;

    /**
     * The 'display_caption' property, displayed as 'Prompt' in the IGC UI.
     */
    protected String display_caption;

    /**
     * The 'default_value' property, displayed as 'Default Value' in the IGC UI.
     */
    protected String default_value;


    /** @see #of_stage_type */ @JsonProperty("of_stage_type")  public Reference getOfStageType() { return this.of_stage_type; }
    /** @see #of_stage_type */ @JsonProperty("of_stage_type")  public void setOfStageType(Reference of_stage_type) { this.of_stage_type = of_stage_type; }

    /** @see #display_caption */ @JsonProperty("display_caption")  public String getDisplayCaption() { return this.display_caption; }
    /** @see #display_caption */ @JsonProperty("display_caption")  public void setDisplayCaption(String display_caption) { this.display_caption = display_caption; }

    /** @see #default_value */ @JsonProperty("default_value")  public String getDefaultValue() { return this.default_value; }
    /** @see #default_value */ @JsonProperty("default_value")  public void setDefaultValue(String default_value) { this.default_value = default_value; }


    public static final Boolean isStageTypeDetail(Object obj) { return (obj.getClass() == StageTypeDetail.class); }

}
