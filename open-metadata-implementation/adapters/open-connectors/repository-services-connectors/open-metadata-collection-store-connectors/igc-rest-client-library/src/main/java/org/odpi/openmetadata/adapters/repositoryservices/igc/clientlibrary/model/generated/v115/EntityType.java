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
 * POJO for the 'entity_type' asset type in IGC, displayed as 'Entity Type' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class EntityType extends MainObject {

    public static final String IGC_TYPE_ID = "entity_type";

    /**
     * The 'member_type' property, displayed as 'Member Type' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MemberType} object.
     */
    protected Reference member_type;

    /**
     * The 'member_type_for_query' property, displayed as 'Member Type' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MemberType} object.
     */
    protected Reference member_type_for_query;

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'entity_type_label' property, displayed as 'Entity Type Label' in the IGC UI.
     */
    protected String entity_type_label;

    /**
     * The 'blueprint_elements' property, displayed as 'Blueprint Elements' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BlueprintElementLink} objects.
     */
    protected ReferenceList blueprint_elements;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #member_type */ @JsonProperty("member_type")  public Reference getMemberType() { return this.member_type; }
    /** @see #member_type */ @JsonProperty("member_type")  public void setMemberType(Reference member_type) { this.member_type = member_type; }

    /** @see #member_type_for_query */ @JsonProperty("member_type_for_query")  public Reference getMemberTypeForQuery() { return this.member_type_for_query; }
    /** @see #member_type_for_query */ @JsonProperty("member_type_for_query")  public void setMemberTypeForQuery(Reference member_type_for_query) { this.member_type_for_query = member_type_for_query; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #entity_type_label */ @JsonProperty("entity_type_label")  public String getEntityTypeLabel() { return this.entity_type_label; }
    /** @see #entity_type_label */ @JsonProperty("entity_type_label")  public void setEntityTypeLabel(String entity_type_label) { this.entity_type_label = entity_type_label; }

    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public ReferenceList getBlueprintElements() { return this.blueprint_elements; }
    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public void setBlueprintElements(ReferenceList blueprint_elements) { this.blueprint_elements = blueprint_elements; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isEntityType(Object obj) { return (obj.getClass() == EntityType.class); }

}
