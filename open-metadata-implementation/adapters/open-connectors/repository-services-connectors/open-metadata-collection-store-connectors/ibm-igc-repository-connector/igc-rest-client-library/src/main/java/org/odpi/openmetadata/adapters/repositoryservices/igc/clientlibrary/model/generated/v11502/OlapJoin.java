/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11502;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code olap_join} asset type in IGC, displayed as '{@literal OLAP Join}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class OlapJoin extends Reference {

    public static String getIgcTypeId() { return "olap_join"; }
    public static String getIgcTypeDisplayName() { return "OLAP Join"; }

    /**
     * The {@code name} property, displayed as '{@literal Join Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code business_name} property, displayed as '{@literal Business Name}' in the IGC UI.
     */
    protected String business_name;

    /**
     * The {@code short_description} property, displayed as '{@literal Short Description}' in the IGC UI.
     */
    protected String short_description;

    /**
     * The {@code type} property, displayed as '{@literal Type}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>INNER (displayed in the UI as 'INNER')</li>
     *     <li>FULL_OUTER (displayed in the UI as 'FULL_OUTER')</li>
     *     <li>LEFT_OUTER (displayed in the UI as 'LEFT_OUTER')</li>
     *     <li>RIGHT_OUTER (displayed in the UI as 'RIGHT_OUTER')</li>
     * </ul>
     */
    protected String type;

    /**
     * The {@code condition} property, displayed as '{@literal Condition}' in the IGC UI.
     */
    protected String condition;

    /**
     * The {@code bi_model} property, displayed as '{@literal OLAP Model}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiModel} object.
     */
    protected Reference bi_model;

    /**
     * The {@code contains_references} property, displayed as '{@literal Contains References}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Olapjoinref} objects.
     */
    protected ReferenceList contains_references;

    /**
     * The {@code referenced_by_an_olap_cube} property, displayed as '{@literal Referenced by an OLAP Cube}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiCube} objects.
     */
    protected ReferenceList referenced_by_an_olap_cube;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #business_name */ @JsonProperty("business_name")  public String getBusinessName() { return this.business_name; }
    /** @see #business_name */ @JsonProperty("business_name")  public void setBusinessName(String business_name) { this.business_name = business_name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #condition */ @JsonProperty("condition")  public String getCondition() { return this.condition; }
    /** @see #condition */ @JsonProperty("condition")  public void setCondition(String condition) { this.condition = condition; }

    /** @see #bi_model */ @JsonProperty("bi_model")  public Reference getBiModel() { return this.bi_model; }
    /** @see #bi_model */ @JsonProperty("bi_model")  public void setBiModel(Reference bi_model) { this.bi_model = bi_model; }

    /** @see #contains_references */ @JsonProperty("contains_references")  public ReferenceList getContainsReferences() { return this.contains_references; }
    /** @see #contains_references */ @JsonProperty("contains_references")  public void setContainsReferences(ReferenceList contains_references) { this.contains_references = contains_references; }

    /** @see #referenced_by_an_olap_cube */ @JsonProperty("referenced_by_an_olap_cube")  public ReferenceList getReferencedByAnOlapCube() { return this.referenced_by_an_olap_cube; }
    /** @see #referenced_by_an_olap_cube */ @JsonProperty("referenced_by_an_olap_cube")  public void setReferencedByAnOlapCube(ReferenceList referenced_by_an_olap_cube) { this.referenced_by_an_olap_cube = referenced_by_an_olap_cube; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "business_name",
        "short_description",
        "type",
        "condition"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "business_name",
        "short_description",
        "condition"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "contains_references",
        "referenced_by_an_olap_cube"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "business_name",
        "short_description",
        "type",
        "condition",
        "bi_model",
        "contains_references",
        "referenced_by_an_olap_cube"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isOlapJoin(Object obj) { return (obj.getClass() == OlapJoin.class); }

}
