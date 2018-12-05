/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'olap_member_source' asset type in IGC, displayed as 'OLAP Member Source' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class OlapMemberSource extends MainObject {

    public static final String IGC_TYPE_ID = "olap_member_source";

    /**
     * The 'olap_member' property, displayed as 'OLAP Member' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiCollectionMember} object.
     */
    protected Reference olap_member;

    /**
     * The 'data_field' property, displayed as 'Data Field' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataField} object.
     */
    protected Reference data_field;


    /** @see #olap_member */ @JsonProperty("olap_member")  public Reference getOlapMember() { return this.olap_member; }
    /** @see #olap_member */ @JsonProperty("olap_member")  public void setOlapMember(Reference olap_member) { this.olap_member = olap_member; }

    /** @see #data_field */ @JsonProperty("data_field")  public Reference getDataField() { return this.data_field; }
    /** @see #data_field */ @JsonProperty("data_field")  public void setDataField(Reference data_field) { this.data_field = data_field; }


    public static final Boolean isOlapMemberSource(Object obj) { return (obj.getClass() == OlapMemberSource.class); }

}
