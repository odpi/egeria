/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'bi_report_data_item_source' asset type in IGC, displayed as 'BI Report Data Item Source' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BiReportDataItemSource extends Reference {

    public static String getIgcTypeId() { return "bi_report_data_item_source"; }
    public static String getIgcTypeDisplayName() { return "BI Report Data Item Source"; }

    /**
     * The 'defined_of_report_field' property, displayed as 'Defined of Report Field' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Reportobject} objects.
     */
    protected ReferenceList defined_of_report_field;

    /**
     * The 'defined_in_report_data_item' property, displayed as 'Defined in Report Data Item' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Reportobject} object.
     */
    protected Reference defined_in_report_data_item;

    /**
     * The 'defined_by_data_field' property, displayed as 'Defined by Data Field' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataField} objects.
     */
    protected ReferenceList defined_by_data_field;

    /**
     * The 'defined_by_olap_member' property, displayed as 'Defined by OLAP Member' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiCollectionMember} object.
     */
    protected Reference defined_by_olap_member;


    /** @see #defined_of_report_field */ @JsonProperty("defined_of_report_field")  public ReferenceList getDefinedOfReportField() { return this.defined_of_report_field; }
    /** @see #defined_of_report_field */ @JsonProperty("defined_of_report_field")  public void setDefinedOfReportField(ReferenceList defined_of_report_field) { this.defined_of_report_field = defined_of_report_field; }

    /** @see #defined_in_report_data_item */ @JsonProperty("defined_in_report_data_item")  public Reference getDefinedInReportDataItem() { return this.defined_in_report_data_item; }
    /** @see #defined_in_report_data_item */ @JsonProperty("defined_in_report_data_item")  public void setDefinedInReportDataItem(Reference defined_in_report_data_item) { this.defined_in_report_data_item = defined_in_report_data_item; }

    /** @see #defined_by_data_field */ @JsonProperty("defined_by_data_field")  public ReferenceList getDefinedByDataField() { return this.defined_by_data_field; }
    /** @see #defined_by_data_field */ @JsonProperty("defined_by_data_field")  public void setDefinedByDataField(ReferenceList defined_by_data_field) { this.defined_by_data_field = defined_by_data_field; }

    /** @see #defined_by_olap_member */ @JsonProperty("defined_by_olap_member")  public Reference getDefinedByOlapMember() { return this.defined_by_olap_member; }
    /** @see #defined_by_olap_member */ @JsonProperty("defined_by_olap_member")  public void setDefinedByOlapMember(Reference defined_by_olap_member) { this.defined_by_olap_member = defined_by_olap_member; }

    public static final Boolean canBeCreated() { return false; }
    public static final Boolean includesModificationDetails() { return false; }
    public static final ArrayList<String> NON_RELATIONAL_PROPERTIES = new ArrayList<>();
    public static final List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static final Boolean isBiReportDataItemSource(Object obj) { return (obj.getClass() == BiReportDataItemSource.class); }

}
