/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'cdc_mapping' asset type in IGC, displayed as 'CDC Mapping' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CdcMapping extends Reference {

    public static String getIgcTypeId() { return "cdc_mapping"; }
    public static String getIgcTypeDisplayName() { return "CDC Mapping"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'cdc_mapping_document' property, displayed as 'CDC Mapping Document' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link CdcMappingDocument} objects.
     */
    protected ReferenceList cdc_mapping_document;

    /**
     * The 'sources' property, displayed as 'Sources' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList sources;

    /**
     * The 'rule' property, displayed as 'Rule' in the IGC UI.
     */
    protected String rule;

    /**
     * The 'targets' property, displayed as 'Targets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList targets;

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

    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #cdc_mapping_document */ @JsonProperty("cdc_mapping_document")  public ReferenceList getCdcMappingDocument() { return this.cdc_mapping_document; }
    /** @see #cdc_mapping_document */ @JsonProperty("cdc_mapping_document")  public void setCdcMappingDocument(ReferenceList cdc_mapping_document) { this.cdc_mapping_document = cdc_mapping_document; }

    /** @see #sources */ @JsonProperty("sources")  public ReferenceList getSources() { return this.sources; }
    /** @see #sources */ @JsonProperty("sources")  public void setSources(ReferenceList sources) { this.sources = sources; }

    /** @see #rule */ @JsonProperty("rule")  public String getRule() { return this.rule; }
    /** @see #rule */ @JsonProperty("rule")  public void setRule(String rule) { this.rule = rule; }

    /** @see #targets */ @JsonProperty("targets")  public ReferenceList getTargets() { return this.targets; }
    /** @see #targets */ @JsonProperty("targets")  public void setTargets(ReferenceList targets) { this.targets = targets; }

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
        "description",
        "rule",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "cdc_mapping_document",
        "sources",
        "targets"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "description",
        "cdc_mapping_document",
        "sources",
        "rule",
        "targets",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isCdcMapping(Object obj) { return (obj.getClass() == CdcMapping.class); }

}
