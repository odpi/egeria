/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'olapobject' asset type in IGC, displayed as 'OLAPObject' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Olapobject extends MainObject {

    public static final String IGC_TYPE_ID = "olapobject";

    /**
     * The 'business_name' property, displayed as 'Business Name' in the IGC UI.
     */
    protected String business_name;


    /** @see #business_name */ @JsonProperty("business_name")  public String getBusinessName() { return this.business_name; }
    /** @see #business_name */ @JsonProperty("business_name")  public void setBusinessName(String business_name) { this.business_name = business_name; }


    public static final Boolean isOlapobject(Object obj) { return (obj.getClass() == Olapobject.class); }

}
