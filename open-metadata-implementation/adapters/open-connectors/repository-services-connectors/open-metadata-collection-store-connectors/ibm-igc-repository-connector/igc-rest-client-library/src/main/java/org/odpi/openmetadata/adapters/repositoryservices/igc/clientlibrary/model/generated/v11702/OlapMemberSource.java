/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11702;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code olap_member_source} asset type in IGC, displayed as '{@literal OLAP Member Source}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class OlapMemberSource extends Reference {

    public static String getIgcTypeId() { return "olap_member_source"; }
    public static String getIgcTypeDisplayName() { return "OLAP Member Source"; }

    /**
     * The {@code olap_member} property, displayed as '{@literal OLAP Member}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiCollectionMember} object.
     */
    protected Reference olap_member;

    /**
     * The {@code data_field} property, displayed as '{@literal Data Field}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataField} object.
     */
    protected Reference data_field;


    /** @see #olap_member */ @JsonProperty("olap_member")  public Reference getOlapMember() { return this.olap_member; }
    /** @see #olap_member */ @JsonProperty("olap_member")  public void setOlapMember(Reference olap_member) { this.olap_member = olap_member; }

    /** @see #data_field */ @JsonProperty("data_field")  public Reference getDataField() { return this.data_field; }
    /** @see #data_field */ @JsonProperty("data_field")  public void setDataField(Reference data_field) { this.data_field = data_field; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> STRING_PROPERTIES = new ArrayList<>();
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "olap_member",
        "data_field"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isOlapMemberSource(Object obj) { return (obj.getClass() == OlapMemberSource.class); }

}
