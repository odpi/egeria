/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11702;

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
 * POJO for the {@code olapjoinref} asset type in IGC, displayed as '{@literal OLAPJoinRef}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("olapjoinref")
public class Olapjoinref extends Reference {

    public static String getIgcTypeDisplayName() { return "OLAPJoinRef"; }

    /**
     * The {@code joins_olap_collection} property, displayed as '{@literal Joins OLAP Collection}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiCollection} objects.
     */
    protected ReferenceList joins_olap_collection;

    /**
     * The {@code joins_database_table} property, displayed as '{@literal Joins Database Table}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Datagroup} object.
     */
    protected Reference joins_database_table;

    /**
     * The {@code alias} property, displayed as '{@literal Alias}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference alias;

    /**
     * The {@code cardinality} property, displayed as '{@literal Cardinality}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>ONE_TO_ONE (displayed in the UI as 'ONE_TO_ONE')</li>
     *     <li>ONE_TO_MANY (displayed in the UI as 'ONE_TO_MANY')</li>
     *     <li>MANY_TO_ONE (displayed in the UI as 'MANY_TO_ONE')</li>
     *     <li>MANY_TO_MANY (displayed in the UI as 'MANY_TO_MANY')</li>
     * </ul>
     */
    protected String cardinality;

    /**
     * The {@code sequence} property, displayed as '{@literal Sequence}' in the IGC UI.
     */
    protected Number sequence;


    /** @see #joins_olap_collection */ @JsonProperty("joins_olap_collection")  public ReferenceList getJoinsOlapCollection() { return this.joins_olap_collection; }
    /** @see #joins_olap_collection */ @JsonProperty("joins_olap_collection")  public void setJoinsOlapCollection(ReferenceList joins_olap_collection) { this.joins_olap_collection = joins_olap_collection; }

    /** @see #joins_database_table */ @JsonProperty("joins_database_table")  public Reference getJoinsDatabaseTable() { return this.joins_database_table; }
    /** @see #joins_database_table */ @JsonProperty("joins_database_table")  public void setJoinsDatabaseTable(Reference joins_database_table) { this.joins_database_table = joins_database_table; }

    /** @see #alias */ @JsonProperty("alias")  public Reference getAlias() { return this.alias; }
    /** @see #alias */ @JsonProperty("alias")  public void setAlias(Reference alias) { this.alias = alias; }

    /** @see #cardinality */ @JsonProperty("cardinality")  public String getCardinality() { return this.cardinality; }
    /** @see #cardinality */ @JsonProperty("cardinality")  public void setCardinality(String cardinality) { this.cardinality = cardinality; }

    /** @see #sequence */ @JsonProperty("sequence")  public Number getSequence() { return this.sequence; }
    /** @see #sequence */ @JsonProperty("sequence")  public void setSequence(Number sequence) { this.sequence = sequence; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "cardinality",
        "sequence"
    );
    private static final List<String> STRING_PROPERTIES = new ArrayList<>();
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "joins_olap_collection"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "joins_olap_collection",
        "joins_database_table",
        "alias",
        "cardinality",
        "sequence"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isOlapjoinref(Object obj) { return (obj.getClass() == Olapjoinref.class); }

}
