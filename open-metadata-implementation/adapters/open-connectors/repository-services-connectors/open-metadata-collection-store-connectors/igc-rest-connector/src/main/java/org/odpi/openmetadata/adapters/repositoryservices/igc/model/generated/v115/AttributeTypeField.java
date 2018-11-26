/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'attribute_type_field' asset type in IGC, displayed as 'Attribute Type Field' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class AttributeTypeField extends MainObject {

    public static final String IGC_TYPE_ID = "attribute_type_field";

    /**
     * The 'attribute_type' property, displayed as 'Attribute Type' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link AttributeType} object.
     */
    protected Reference attribute_type;

    /**
     * The 'attribute_type_for_query' property, displayed as 'Attribute Type' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link AttributeType} object.
     */
    protected Reference attribute_type_for_query;

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>CHAR (displayed in the UI as 'CHAR')</li>
     *     <li>DATETIME (displayed in the UI as 'DATETIME')</li>
     *     <li>SQWORD (displayed in the UI as 'SQWORD')</li>
     *     <li>UDWORD (displayed in the UI as 'UDWORD')</li>
     *     <li>UWORD (displayed in the UI as 'UWORD')</li>
     *     <li>VARCHAR (displayed in the UI as 'VARCHAR')</li>
     * </ul>
     */
    protected String type;

    /**
     * The 'length' property, displayed as 'Length' in the IGC UI.
     */
    protected Number length;

    /**
     * The 'required' property, displayed as 'Required' in the IGC UI.
     */
    protected Boolean required;

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


    /** @see #attribute_type */ @JsonProperty("attribute_type")  public Reference getAttributeType() { return this.attribute_type; }
    /** @see #attribute_type */ @JsonProperty("attribute_type")  public void setAttributeType(Reference attribute_type) { this.attribute_type = attribute_type; }

    /** @see #attribute_type_for_query */ @JsonProperty("attribute_type_for_query")  public Reference getAttributeTypeForQuery() { return this.attribute_type_for_query; }
    /** @see #attribute_type_for_query */ @JsonProperty("attribute_type_for_query")  public void setAttributeTypeForQuery(Reference attribute_type_for_query) { this.attribute_type_for_query = attribute_type_for_query; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #length */ @JsonProperty("length")  public Number getLength() { return this.length; }
    /** @see #length */ @JsonProperty("length")  public void setLength(Number length) { this.length = length; }

    /** @see #required */ @JsonProperty("required")  public Boolean getRequired() { return this.required; }
    /** @see #required */ @JsonProperty("required")  public void setRequired(Boolean required) { this.required = required; }

    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public ReferenceList getBlueprintElements() { return this.blueprint_elements; }
    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public void setBlueprintElements(ReferenceList blueprint_elements) { this.blueprint_elements = blueprint_elements; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isAttributeTypeField(Object obj) { return (obj.getClass() == AttributeTypeField.class); }

}
