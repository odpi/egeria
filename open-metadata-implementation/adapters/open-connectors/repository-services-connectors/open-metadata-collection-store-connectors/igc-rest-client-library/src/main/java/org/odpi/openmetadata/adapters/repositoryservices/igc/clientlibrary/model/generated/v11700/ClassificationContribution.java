/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11700;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'classification_contribution' asset type in IGC, displayed as 'Classification Contribution' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ClassificationContribution extends Reference {

    public static String getIgcTypeId() { return "classification_contribution"; }
    public static String getIgcTypeDisplayName() { return "Classification Contribution"; }

    /**
     * The 'infoset' property, displayed as 'Infoset' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Infoset} object.
     */
    protected Reference infoset;

    /**
     * The 'data_class' property, displayed as 'Data Class' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataClass} object.
     */
    protected Reference data_class;

    /**
     * The 'object_count' property, displayed as 'Number of Objects' in the IGC UI.
     */
    protected Number object_count;

    /**
     * The 'size' property, displayed as 'Size (Bytes)' in the IGC UI.
     */
    protected Number size;

    /**
     * The 'match_type' property, displayed as 'Match Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>Exact (displayed in the UI as 'Exact')</li>
     *     <li>GreaterThan (displayed in the UI as 'GreaterThan')</li>
     * </ul>
     */
    protected String match_type;


    /** @see #infoset */ @JsonProperty("infoset")  public Reference getInfoset() { return this.infoset; }
    /** @see #infoset */ @JsonProperty("infoset")  public void setInfoset(Reference infoset) { this.infoset = infoset; }

    /** @see #data_class */ @JsonProperty("data_class")  public Reference getDataClass() { return this.data_class; }
    /** @see #data_class */ @JsonProperty("data_class")  public void setDataClass(Reference data_class) { this.data_class = data_class; }

    /** @see #object_count */ @JsonProperty("object_count")  public Number getObjectCount() { return this.object_count; }
    /** @see #object_count */ @JsonProperty("object_count")  public void setObjectCount(Number object_count) { this.object_count = object_count; }

    /** @see #size */ @JsonProperty("size")  public Number getSize() { return this.size; }
    /** @see #size */ @JsonProperty("size")  public void setSize(Number size) { this.size = size; }

    /** @see #match_type */ @JsonProperty("match_type")  public String getMatchType() { return this.match_type; }
    /** @see #match_type */ @JsonProperty("match_type")  public void setMatchType(String match_type) { this.match_type = match_type; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "object_count",
        "size",
        "match_type"
    );
    private static final List<String> STRING_PROPERTIES = new ArrayList<>();
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "infoset",
        "data_class",
        "object_count",
        "size",
        "match_type"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isClassificationContribution(Object obj) { return (obj.getClass() == ClassificationContribution.class); }

}
