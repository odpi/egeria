/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11502sp3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'mwb_database_alias' asset type in IGC, displayed as 'Database Alias' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MwbDatabaseAlias extends Reference {

    public static String getIgcTypeId() { return "mwb_database_alias"; }
    public static String getIgcTypeDisplayName() { return "Database Alias"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     */
    protected String type;

    /**
     * The 'database' property, displayed as 'Database' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Database} object.
     */
    protected Reference database;

    /**
     * The 'referenced_by_jobs' property, displayed as 'Referenced by Jobs' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsjob} objects.
     */
    protected ReferenceList referenced_by_jobs;

    /**
     * The 'created_by' property, displayed as 'Created By' in the IGC UI.
     */
    protected String created_by;

    /**
     * The 'created_on' property, displayed as 'Created On' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The 'modified_by' property, displayed as 'Modified By' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The 'modified_on' property, displayed as 'Modified On' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #database */ @JsonProperty("database")  public Reference getDatabase() { return this.database; }
    /** @see #database */ @JsonProperty("database")  public void setDatabase(Reference database) { this.database = database; }

    /** @see #referenced_by_jobs */ @JsonProperty("referenced_by_jobs")  public ReferenceList getReferencedByJobs() { return this.referenced_by_jobs; }
    /** @see #referenced_by_jobs */ @JsonProperty("referenced_by_jobs")  public void setReferencedByJobs(ReferenceList referenced_by_jobs) { this.referenced_by_jobs = referenced_by_jobs; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "type",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "referenced_by_jobs"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "type",
        "database",
        "referenced_by_jobs",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isMwbDatabaseAlias(Object obj) { return (obj.getClass() == MwbDatabaseAlias.class); }

}
