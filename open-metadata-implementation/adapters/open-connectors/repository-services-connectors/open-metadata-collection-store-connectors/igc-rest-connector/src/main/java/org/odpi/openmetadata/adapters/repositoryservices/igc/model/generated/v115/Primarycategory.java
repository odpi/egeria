/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'primarycategory' asset type in IGC, displayed as 'PrimaryCategory' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Primarycategory extends MainObject {

    public static final String IGC_TYPE_ID = "primarycategory";

    /**
     * The 'color' property, displayed as 'Color' in the IGC UI.
     */
    protected String color;

    /**
     * The 'icon' property, displayed as 'Icon' in the IGC UI.
     */
    protected String icon;

    /**
     * The 'uses_business_category' property, displayed as 'Uses Business Category' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference uses_business_category;


    /** @see #color */ @JsonProperty("color")  public String getColor() { return this.color; }
    /** @see #color */ @JsonProperty("color")  public void setColor(String color) { this.color = color; }

    /** @see #icon */ @JsonProperty("icon")  public String getIcon() { return this.icon; }
    /** @see #icon */ @JsonProperty("icon")  public void setIcon(String icon) { this.icon = icon; }

    /** @see #uses_business_category */ @JsonProperty("uses_business_category")  public Reference getUsesBusinessCategory() { return this.uses_business_category; }
    /** @see #uses_business_category */ @JsonProperty("uses_business_category")  public void setUsesBusinessCategory(Reference uses_business_category) { this.uses_business_category = uses_business_category; }


    public static final Boolean isPrimarycategory(Object obj) { return (obj.getClass() == Primarycategory.class); }

}
