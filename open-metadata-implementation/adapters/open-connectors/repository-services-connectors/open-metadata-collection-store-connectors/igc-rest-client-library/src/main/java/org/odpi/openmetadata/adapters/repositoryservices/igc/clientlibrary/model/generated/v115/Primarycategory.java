/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'primarycategory' asset type in IGC, displayed as 'PrimaryCategory' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Primarycategory extends Reference {

    public static String getIgcTypeId() { return "primarycategory"; }
    public static String getIgcTypeDisplayName() { return "PrimaryCategory"; }

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

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'created_by' property, displayed as 'Created By' in the IGC UI.
     */
    protected String created_by;

    /**
     * The 'created_on' property, displayed as 'Created On' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The 'modified_by' property, displayed as 'Modified By' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The 'modified_on' property, displayed as 'Modified On' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #color */ @JsonProperty("color")  public String getColor() { return this.color; }
    /** @see #color */ @JsonProperty("color")  public void setColor(String color) { this.color = color; }

    /** @see #icon */ @JsonProperty("icon")  public String getIcon() { return this.icon; }
    /** @see #icon */ @JsonProperty("icon")  public void setIcon(String icon) { this.icon = icon; }

    /** @see #uses_business_category */ @JsonProperty("uses_business_category")  public Reference getUsesBusinessCategory() { return this.uses_business_category; }
    /** @see #uses_business_category */ @JsonProperty("uses_business_category")  public void setUsesBusinessCategory(Reference uses_business_category) { this.uses_business_category = uses_business_category; }

    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static final Boolean canBeCreated() { return false; }
    public static final Boolean includesModificationDetails() { return true; }
    public static final ArrayList<String> NON_RELATIONAL_PROPERTIES = new ArrayList<String>() {{
        add("color");
        add("icon");
        add("name");
        add("created_by");
        add("created_on");
        add("modified_by");
        add("modified_on");
    }};
    public static final List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static final Boolean isPrimarycategory(Object obj) { return (obj.getClass() == Primarycategory.class); }

}
