/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11700;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code lineage_report_template} asset type in IGC, displayed as '{@literal Lineage Report Template}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("lineage_report_template")
public class LineageReportTemplate extends Reference {

    public static String getIgcTypeDisplayName() { return "Lineage Report Template"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code long_description} property, displayed as '{@literal Description}' in the IGC UI.
     */
    protected String long_description;

    /**
     * The {@code uses_lineage_filter} property, displayed as '{@literal Uses Lineage Filter}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Lineagefilter} objects.
     */
    protected ReferenceList uses_lineage_filter;

    /**
     * The {@code asset_display_properties} property, displayed as '{@literal Asset Type Properties}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationAsset} object.
     */
    protected Reference asset_display_properties;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #uses_lineage_filter */ @JsonProperty("uses_lineage_filter")  public ReferenceList getUsesLineageFilter() { return this.uses_lineage_filter; }
    /** @see #uses_lineage_filter */ @JsonProperty("uses_lineage_filter")  public void setUsesLineageFilter(ReferenceList uses_lineage_filter) { this.uses_lineage_filter = uses_lineage_filter; }

    /** @see #asset_display_properties */ @JsonProperty("asset_display_properties")  public Reference getAssetDisplayProperties() { return this.asset_display_properties; }
    /** @see #asset_display_properties */ @JsonProperty("asset_display_properties")  public void setAssetDisplayProperties(Reference asset_display_properties) { this.asset_display_properties = asset_display_properties; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "long_description"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "long_description"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "uses_lineage_filter"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "long_description",
        "uses_lineage_filter",
        "asset_display_properties"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isLineageReportTemplate(Object obj) { return (obj.getClass() == LineageReportTemplate.class); }

}
