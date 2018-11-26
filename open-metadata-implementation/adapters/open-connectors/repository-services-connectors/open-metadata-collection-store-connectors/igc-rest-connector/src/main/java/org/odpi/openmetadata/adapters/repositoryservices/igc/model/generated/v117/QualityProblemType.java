/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'quality_Problem_Type' asset type in IGC, displayed as 'QualityProblemType' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class QualityProblemType extends MainObject {

    public static final String IGC_TYPE_ID = "quality_Problem_Type";

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'code' property, displayed as 'Code' in the IGC UI.
     */
    protected String code;


    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #code */ @JsonProperty("code")  public String getCode() { return this.code; }
    /** @see #code */ @JsonProperty("code")  public void setCode(String code) { this.code = code; }


    public static final Boolean isQualityProblemType(Object obj) { return (obj.getClass() == QualityProblemType.class); }

}
