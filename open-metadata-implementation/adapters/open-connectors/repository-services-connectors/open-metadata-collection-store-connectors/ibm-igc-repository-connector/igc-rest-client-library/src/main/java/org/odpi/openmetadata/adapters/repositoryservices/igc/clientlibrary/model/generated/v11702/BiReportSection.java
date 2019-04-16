/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11702;

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
 * POJO for the {@code bi_report_section} asset type in IGC, displayed as '{@literal BI Report Section}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("bi_report_section")
public class BiReportSection extends Reference {

    public static String getIgcTypeDisplayName() { return "BI Report Section"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code alias_(business_name)} property, displayed as '{@literal Alias (Business Name)}' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The {@code type} property, displayed as '{@literal Type}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>CHART (displayed in the UI as 'CHART')</li>
     *     <li>LIST (displayed in the UI as 'LIST')</li>
     *     <li>MATRIX (displayed in the UI as 'MATRIX')</li>
     *     <li>TABLE (displayed in the UI as 'TABLE')</li>
     *     <li>PAGE (displayed in the UI as 'PAGE')</li>
     *     <li>PAGEBODY (displayed in the UI as 'PAGEBODY')</li>
     *     <li>PAGEFOOTER (displayed in the UI as 'PAGEFOOTER')</li>
     *     <li>PAGEHEADER (displayed in the UI as 'PAGEHEADER')</li>
     *     <li>RECTANGLE (displayed in the UI as 'RECTANGLE')</li>
     *     <li>TEXT (displayed in the UI as 'TEXT')</li>
     * </ul>
     */
    protected String type;

    /**
     * The {@code label} property, displayed as '{@literal Label}' in the IGC UI.
     */
    protected String label;

    /**
     * The {@code short_description} property, displayed as '{@literal Short Description}' in the IGC UI.
     */
    protected String short_description;

    /**
     * The {@code sequence} property, displayed as '{@literal Sequence}' in the IGC UI.
     */
    protected Number sequence;

    /**
     * The {@code bi_report} property, displayed as '{@literal BI Report}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiReport} object.
     */
    protected Reference bi_report;

    /**
     * The {@code bi_report_fields} property, displayed as '{@literal BI Report Fields}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Reportobject} objects.
     */
    protected ReferenceList bi_report_fields;

    /**
     * The {@code contains_sub_section} property, displayed as '{@literal Contains Sub Section}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiReportSection} objects.
     */
    protected ReferenceList contains_sub_section;

    /**
     * The {@code contained_in_report_section} property, displayed as '{@literal Contained in Report Section}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiReportSection} object.
     */
    protected Reference contained_in_report_section;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #label */ @JsonProperty("label")  public String getLabel() { return this.label; }
    /** @see #label */ @JsonProperty("label")  public void setLabel(String label) { this.label = label; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #sequence */ @JsonProperty("sequence")  public Number getSequence() { return this.sequence; }
    /** @see #sequence */ @JsonProperty("sequence")  public void setSequence(Number sequence) { this.sequence = sequence; }

    /** @see #bi_report */ @JsonProperty("bi_report")  public Reference getBiReport() { return this.bi_report; }
    /** @see #bi_report */ @JsonProperty("bi_report")  public void setBiReport(Reference bi_report) { this.bi_report = bi_report; }

    /** @see #bi_report_fields */ @JsonProperty("bi_report_fields")  public ReferenceList getBiReportFields() { return this.bi_report_fields; }
    /** @see #bi_report_fields */ @JsonProperty("bi_report_fields")  public void setBiReportFields(ReferenceList bi_report_fields) { this.bi_report_fields = bi_report_fields; }

    /** @see #contains_sub_section */ @JsonProperty("contains_sub_section")  public ReferenceList getContainsSubSection() { return this.contains_sub_section; }
    /** @see #contains_sub_section */ @JsonProperty("contains_sub_section")  public void setContainsSubSection(ReferenceList contains_sub_section) { this.contains_sub_section = contains_sub_section; }

    /** @see #contained_in_report_section */ @JsonProperty("contained_in_report_section")  public Reference getContainedInReportSection() { return this.contained_in_report_section; }
    /** @see #contained_in_report_section */ @JsonProperty("contained_in_report_section")  public void setContainedInReportSection(Reference contained_in_report_section) { this.contained_in_report_section = contained_in_report_section; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "alias_(business_name)",
        "type",
        "label",
        "short_description",
        "sequence"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "alias_(business_name)",
        "label",
        "short_description"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "bi_report_fields",
        "contains_sub_section"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "alias_(business_name)",
        "type",
        "label",
        "short_description",
        "sequence",
        "bi_report",
        "bi_report_fields",
        "contains_sub_section",
        "contained_in_report_section"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isBiReportSection(Object obj) { return (obj.getClass() == BiReportSection.class); }

}
