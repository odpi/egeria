/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'acl' asset type in IGC, displayed as 'Acl' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Acl extends MainObject {

    public static final String IGC_TYPE_ID = "acl";

    /**
     * The 'has_acl_entry' property, displayed as 'Has Acl Entry' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Aclentry} objects.
     */
    protected ReferenceList has_acl_entry;

    /**
     * The 'of_common_object' property, displayed as 'Of Common Object' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationAsset} object.
     */
    protected Reference of_common_object;


    /** @see #has_acl_entry */ @JsonProperty("has_acl_entry")  public ReferenceList getHasAclEntry() { return this.has_acl_entry; }
    /** @see #has_acl_entry */ @JsonProperty("has_acl_entry")  public void setHasAclEntry(ReferenceList has_acl_entry) { this.has_acl_entry = has_acl_entry; }

    /** @see #of_common_object */ @JsonProperty("of_common_object")  public Reference getOfCommonObject() { return this.of_common_object; }
    /** @see #of_common_object */ @JsonProperty("of_common_object")  public void setOfCommonObject(Reference of_common_object) { this.of_common_object = of_common_object; }


    public static final Boolean isAcl(Object obj) { return (obj.getClass() == Acl.class); }

}
