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
 * POJO for the 'bi_report_section' asset type in IGC, displayed as 'BI Report Section' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BiReportSection extends MainObject {

    public static final String IGC_TYPE_ID = "bi_report_section";

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
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
     * The 'label' property, displayed as 'Label' in the IGC UI.
     */
    protected String label;

    /**
     * The 'sequence' property, displayed as 'Sequence' in the IGC UI.
     */
    protected Number sequence;

    /**
     * The 'bi_report' property, displayed as 'BI Report' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiReport} object.
     */
    protected Reference bi_report;

    /**
     * The 'bi_report_fields' property, displayed as 'BI Report Fields' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Reportobject} objects.
     */
    protected ReferenceList bi_report_fields;

    /**
     * The 'contains_sub_section' property, displayed as 'Contains Sub Section' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiReportSection} objects.
     */
    protected ReferenceList contains_sub_section;

    /**
     * The 'contained_in_report_section' property, displayed as 'Contained in Report Section' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiReportSection} object.
     */
    protected Reference contained_in_report_section;


    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #label */ @JsonProperty("label")  public String getLabel() { return this.label; }
    /** @see #label */ @JsonProperty("label")  public void setLabel(String label) { this.label = label; }

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


    public static final Boolean isBiReportSection(Object obj) { return (obj.getClass() == BiReportSection.class); }

}
