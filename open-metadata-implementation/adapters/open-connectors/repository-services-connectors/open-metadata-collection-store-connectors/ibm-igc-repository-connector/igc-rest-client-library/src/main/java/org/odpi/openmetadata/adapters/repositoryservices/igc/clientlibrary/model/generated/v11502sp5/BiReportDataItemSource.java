/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11502sp5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code bi_report_data_item_source} asset type in IGC, displayed as '{@literal BI Report Data Item Source}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class BiReportDataItemSource extends Reference {

    public static String getIgcTypeId() { return "bi_report_data_item_source"; }
    public static String getIgcTypeDisplayName() { return "BI Report Data Item Source"; }

    /**
     * The {@code defined_of_report_field} property, displayed as '{@literal Defined of Report Field}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Reportobject} objects.
     */
    protected ReferenceList defined_of_report_field;

    /**
     * The {@code defined_in_report_data_item} property, displayed as '{@literal Defined in Report Data Item}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Reportobject} object.
     */
    protected Reference defined_in_report_data_item;

    /**
     * The {@code defined_by_data_field} property, displayed as '{@literal Defined by Data Field}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataItem} objects.
     */
    protected ReferenceList defined_by_data_field;

    /**
     * The {@code defined_by_olap_member} property, displayed as '{@literal Defined by OLAP Member}' in the IGC UI.
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

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> STRING_PROPERTIES = new ArrayList<>();
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "defined_of_report_field",
        "defined_by_data_field"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "defined_of_report_field",
        "defined_in_report_data_item",
        "defined_by_data_field",
        "defined_by_olap_member"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isBiReportDataItemSource(Object obj) { return (obj.getClass() == BiReportDataItemSource.class); }

}
