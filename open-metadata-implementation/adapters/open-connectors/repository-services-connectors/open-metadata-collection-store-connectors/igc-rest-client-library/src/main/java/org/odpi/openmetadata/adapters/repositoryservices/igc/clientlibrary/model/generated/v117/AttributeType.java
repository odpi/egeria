/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'attribute_type' asset type in IGC, displayed as 'Attribute Type' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class AttributeType extends MainObject {

    public static final String IGC_TYPE_ID = "attribute_type";

    /**
     * The 'mdm_model' property, displayed as 'MDM Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MdmModel} object.
     */
    protected Reference mdm_model;

    /**
     * The 'mdm_model_for_query' property, displayed as 'MDM Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MdmModel} object.
     */
    protected Reference mdm_model_for_query;

    /**
     * The 'contains_attribute_type_fields' property, displayed as 'Contains Attribute Type Fields' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AttributeTypeField} objects.
     */
    protected ReferenceList contains_attribute_type_fields;

    /**
     * The 'referenced_by_attributes' property, displayed as 'Referenced by Attributes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Attribute} objects.
     */
    protected ReferenceList referenced_by_attributes;

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #mdm_model */ @JsonProperty("mdm_model")  public Reference getMdmModel() { return this.mdm_model; }
    /** @see #mdm_model */ @JsonProperty("mdm_model")  public void setMdmModel(Reference mdm_model) { this.mdm_model = mdm_model; }

    /** @see #mdm_model_for_query */ @JsonProperty("mdm_model_for_query")  public Reference getMdmModelForQuery() { return this.mdm_model_for_query; }
    /** @see #mdm_model_for_query */ @JsonProperty("mdm_model_for_query")  public void setMdmModelForQuery(Reference mdm_model_for_query) { this.mdm_model_for_query = mdm_model_for_query; }

    /** @see #contains_attribute_type_fields */ @JsonProperty("contains_attribute_type_fields")  public ReferenceList getContainsAttributeTypeFields() { return this.contains_attribute_type_fields; }
    /** @see #contains_attribute_type_fields */ @JsonProperty("contains_attribute_type_fields")  public void setContainsAttributeTypeFields(ReferenceList contains_attribute_type_fields) { this.contains_attribute_type_fields = contains_attribute_type_fields; }

    /** @see #referenced_by_attributes */ @JsonProperty("referenced_by_attributes")  public ReferenceList getReferencedByAttributes() { return this.referenced_by_attributes; }
    /** @see #referenced_by_attributes */ @JsonProperty("referenced_by_attributes")  public void setReferencedByAttributes(ReferenceList referenced_by_attributes) { this.referenced_by_attributes = referenced_by_attributes; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isAttributeType(Object obj) { return (obj.getClass() == AttributeType.class); }

}
