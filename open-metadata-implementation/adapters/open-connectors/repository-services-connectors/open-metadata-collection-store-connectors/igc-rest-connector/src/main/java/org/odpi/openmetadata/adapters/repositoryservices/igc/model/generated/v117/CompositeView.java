/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'composite_view' asset type in IGC, displayed as 'Composite View' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CompositeView extends MainObject {

    public static final String IGC_TYPE_ID = "composite_view";

    /**
     * The 'member_type' property, displayed as 'Member Type' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MemberType} object.
     */
    protected Reference member_type;

    /**
     * The 'composite_view' property, displayed as 'Composite View' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MemberType} object.
     */
    protected Reference composite_view;

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>MEMBER (displayed in the UI as 'MEMBER')</li>
     *     <li>ENTITY (displayed in the UI as 'ENTITY')</li>
     * </ul>
     */
    protected String type;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #member_type */ @JsonProperty("member_type")  public Reference getMemberType() { return this.member_type; }
    /** @see #member_type */ @JsonProperty("member_type")  public void setMemberType(Reference member_type) { this.member_type = member_type; }

    /** @see #composite_view */ @JsonProperty("composite_view")  public Reference getCompositeView() { return this.composite_view; }
    /** @see #composite_view */ @JsonProperty("composite_view")  public void setCompositeView(Reference composite_view) { this.composite_view = composite_view; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isCompositeView(Object obj) { return (obj.getClass() == CompositeView.class); }

}
