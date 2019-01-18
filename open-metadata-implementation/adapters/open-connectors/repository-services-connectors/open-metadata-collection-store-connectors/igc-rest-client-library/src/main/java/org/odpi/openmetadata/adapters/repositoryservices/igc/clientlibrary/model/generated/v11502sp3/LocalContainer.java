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
 * POJO for the 'local_container' asset type in IGC, displayed as 'Local Container' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class LocalContainer extends Reference {

    public static String getIgcTypeId() { return "local_container"; }
    public static String getIgcTypeDisplayName() { return "Local Container"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'short_description' property, displayed as 'Short Description' in the IGC UI.
     */
    protected String short_description;

    /**
     * The 'long_description' property, displayed as 'Long Description' in the IGC UI.
     */
    protected String long_description;

    /**
     * The 'job_or_container' property, displayed as 'Job or Container' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList job_or_container;

    /**
     * The 'stages' property, displayed as 'Stages' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Stage} objects.
     */
    protected ReferenceList stages;

    /**
     * The 'referenced_by_stages' property, displayed as 'Referenced by Stages' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Stage} objects.
     */
    protected ReferenceList referenced_by_stages;

    /**
     * The 'referenced_by_containers' property, displayed as 'Referenced by Containers' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ReferencedContainer} objects.
     */
    protected ReferenceList referenced_by_containers;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>SERVER (displayed in the UI as 'SERVER')</li>
     *     <li>MAINFRAME (displayed in the UI as 'MAINFRAME')</li>
     *     <li>SEQUENCE (displayed in the UI as 'SEQUENCE')</li>
     *     <li>PARALLEL (displayed in the UI as 'PARALLEL')</li>
     * </ul>
     */
    protected String type;

    /**
     * The 'version' property, displayed as 'Version' in the IGC UI.
     */
    protected String version;

    /**
     * The 'annotations' property, displayed as 'Annotations' in the IGC UI.
     */
    protected ArrayList<String> annotations;

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

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #job_or_container */ @JsonProperty("job_or_container")  public ReferenceList getJobOrContainer() { return this.job_or_container; }
    /** @see #job_or_container */ @JsonProperty("job_or_container")  public void setJobOrContainer(ReferenceList job_or_container) { this.job_or_container = job_or_container; }

    /** @see #stages */ @JsonProperty("stages")  public ReferenceList getStages() { return this.stages; }
    /** @see #stages */ @JsonProperty("stages")  public void setStages(ReferenceList stages) { this.stages = stages; }

    /** @see #referenced_by_stages */ @JsonProperty("referenced_by_stages")  public ReferenceList getReferencedByStages() { return this.referenced_by_stages; }
    /** @see #referenced_by_stages */ @JsonProperty("referenced_by_stages")  public void setReferencedByStages(ReferenceList referenced_by_stages) { this.referenced_by_stages = referenced_by_stages; }

    /** @see #referenced_by_containers */ @JsonProperty("referenced_by_containers")  public ReferenceList getReferencedByContainers() { return this.referenced_by_containers; }
    /** @see #referenced_by_containers */ @JsonProperty("referenced_by_containers")  public void setReferencedByContainers(ReferenceList referenced_by_containers) { this.referenced_by_containers = referenced_by_containers; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #version */ @JsonProperty("version")  public String getVersion() { return this.version; }
    /** @see #version */ @JsonProperty("version")  public void setVersion(String version) { this.version = version; }

    /** @see #annotations */ @JsonProperty("annotations")  public ArrayList<String> getAnnotations() { return this.annotations; }
    /** @see #annotations */ @JsonProperty("annotations")  public void setAnnotations(ArrayList<String> annotations) { this.annotations = annotations; }

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
        "short_description",
        "long_description",
        "type",
        "version",
        "annotations",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "version",
        "annotations",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "job_or_container",
        "stages",
        "referenced_by_stages",
        "referenced_by_containers"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "job_or_container",
        "stages",
        "referenced_by_stages",
        "referenced_by_containers",
        "type",
        "version",
        "annotations",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isLocalContainer(Object obj) { return (obj.getClass() == LocalContainer.class); }

}
