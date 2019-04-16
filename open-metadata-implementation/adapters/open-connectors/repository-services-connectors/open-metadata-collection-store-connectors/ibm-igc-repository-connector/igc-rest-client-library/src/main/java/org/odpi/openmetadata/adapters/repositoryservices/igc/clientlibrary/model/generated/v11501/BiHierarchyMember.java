/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11501;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code bi_hierarchy_member} asset type in IGC, displayed as '{@literal BI Hierarchy Member}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("bi_hierarchy_member")
public class BiHierarchyMember extends Reference {

    public static String getIgcTypeDisplayName() { return "BI Hierarchy Member"; }

    /**
     * The {@code bi_level} property, displayed as '{@literal BI Level}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiLevel} object.
     */
    protected Reference bi_level;

    /**
     * The {@code child_level} property, displayed as '{@literal Child Level}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiLevel} objects.
     */
    protected ReferenceList child_level;

    /**
     * The {@code sorting_order} property, displayed as '{@literal Sorting Order}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>ASCENDING (displayed in the UI as 'ASCENDING')</li>
     *     <li>DESCENDING (displayed in the UI as 'DESCENDING')</li>
     *     <li>NONE (displayed in the UI as 'NONE')</li>
     * </ul>
     */
    protected String sorting_order;

    /**
     * The {@code sequence} property, displayed as '{@literal Sequence}' in the IGC UI.
     */
    protected Number sequence;


    /** @see #bi_level */ @JsonProperty("bi_level")  public Reference getBiLevel() { return this.bi_level; }
    /** @see #bi_level */ @JsonProperty("bi_level")  public void setBiLevel(Reference bi_level) { this.bi_level = bi_level; }

    /** @see #child_level */ @JsonProperty("child_level")  public ReferenceList getChildLevel() { return this.child_level; }
    /** @see #child_level */ @JsonProperty("child_level")  public void setChildLevel(ReferenceList child_level) { this.child_level = child_level; }

    /** @see #sorting_order */ @JsonProperty("sorting_order")  public String getSortingOrder() { return this.sorting_order; }
    /** @see #sorting_order */ @JsonProperty("sorting_order")  public void setSortingOrder(String sorting_order) { this.sorting_order = sorting_order; }

    /** @see #sequence */ @JsonProperty("sequence")  public Number getSequence() { return this.sequence; }
    /** @see #sequence */ @JsonProperty("sequence")  public void setSequence(Number sequence) { this.sequence = sequence; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "sorting_order",
        "sequence"
    );
    private static final List<String> STRING_PROPERTIES = new ArrayList<>();
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "child_level"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "bi_level",
        "child_level",
        "sorting_order",
        "sequence"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isBiHierarchyMember(Object obj) { return (obj.getClass() == BiHierarchyMember.class); }

}
