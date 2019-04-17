/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11700;

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
 * POJO for the {@code dsexternaldependency} asset type in IGC, displayed as '{@literal DSExternalDependency}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("dsexternaldependency")
public class Dsexternaldependency extends Reference {

    public static String getIgcTypeDisplayName() { return "DSExternalDependency"; }

    /**
     * The {@code of_ds_job_def} property, displayed as '{@literal Of DS Job Def}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Dsjob} object.
     */
    protected Reference of_ds_job_def;

    /**
     * The {@code of_ds_routine} property, displayed as '{@literal Of DS Routine}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Routine} object.
     */
    protected Reference of_ds_routine;

    /**
     * The {@code a_xmeta_locking_root} property, displayed as '{@literal A XMeta Locking Root}' in the IGC UI.
     */
    protected String a_xmeta_locking_root;

    /**
     * The {@code calls_ds_routine} property, displayed as '{@literal Calls DS Routine}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Routine} object.
     */
    protected Reference calls_ds_routine;

    /**
     * The {@code type} property, displayed as '{@literal Type}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>JOB (displayed in the UI as 'JOB')</li>
     *     <li>ROUTINE (displayed in the UI as 'ROUTINE')</li>
     *     <li>UVROUTINE (displayed in the UI as 'UVROUTINE')</li>
     *     <li>FILE (displayed in the UI as 'FILE')</li>
     *     <li>ACTIVEX (displayed in the UI as 'ACTIVEX')</li>
     *     <li>WEBSERVICE (displayed in the UI as 'WEBSERVICE')</li>
     *     <li>ROOTJOB (displayed in the UI as 'ROOTJOB')</li>
     * </ul>
     */
    protected String type;

    /**
     * The {@code location} property, displayed as '{@literal Location}' in the IGC UI.
     */
    protected String location;

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code runs_ds_job} property, displayed as '{@literal Runs DS Job}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Dsjob} object.
     */
    protected Reference runs_ds_job;

    /**
     * The {@code created_by} property, displayed as '{@literal Created By}' in the IGC UI.
     */
    protected String created_by;

    /**
     * The {@code created_on} property, displayed as '{@literal Created On}' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The {@code modified_by} property, displayed as '{@literal Modified By}' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The {@code modified_on} property, displayed as '{@literal Modified On}' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #of_ds_job_def */ @JsonProperty("of_ds_job_def")  public Reference getOfDsJobDef() { return this.of_ds_job_def; }
    /** @see #of_ds_job_def */ @JsonProperty("of_ds_job_def")  public void setOfDsJobDef(Reference of_ds_job_def) { this.of_ds_job_def = of_ds_job_def; }

    /** @see #of_ds_routine */ @JsonProperty("of_ds_routine")  public Reference getOfDsRoutine() { return this.of_ds_routine; }
    /** @see #of_ds_routine */ @JsonProperty("of_ds_routine")  public void setOfDsRoutine(Reference of_ds_routine) { this.of_ds_routine = of_ds_routine; }

    /** @see #a_xmeta_locking_root */ @JsonProperty("a_xmeta_locking_root")  public String getAXmetaLockingRoot() { return this.a_xmeta_locking_root; }
    /** @see #a_xmeta_locking_root */ @JsonProperty("a_xmeta_locking_root")  public void setAXmetaLockingRoot(String a_xmeta_locking_root) { this.a_xmeta_locking_root = a_xmeta_locking_root; }

    /** @see #calls_ds_routine */ @JsonProperty("calls_ds_routine")  public Reference getCallsDsRoutine() { return this.calls_ds_routine; }
    /** @see #calls_ds_routine */ @JsonProperty("calls_ds_routine")  public void setCallsDsRoutine(Reference calls_ds_routine) { this.calls_ds_routine = calls_ds_routine; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #location */ @JsonProperty("location")  public String getLocation() { return this.location; }
    /** @see #location */ @JsonProperty("location")  public void setLocation(String location) { this.location = location; }

    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #runs_ds_job */ @JsonProperty("runs_ds_job")  public Reference getRunsDsJob() { return this.runs_ds_job; }
    /** @see #runs_ds_job */ @JsonProperty("runs_ds_job")  public void setRunsDsJob(Reference runs_ds_job) { this.runs_ds_job = runs_ds_job; }

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
        "a_xmeta_locking_root",
        "type",
        "location",
        "name",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "a_xmeta_locking_root",
        "location",
        "name",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "of_ds_job_def",
        "of_ds_routine",
        "a_xmeta_locking_root",
        "calls_ds_routine",
        "type",
        "location",
        "name",
        "runs_ds_job",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isDsexternaldependency(Object obj) { return (obj.getClass() == Dsexternaldependency.class); }

}
