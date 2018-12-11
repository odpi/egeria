/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'datastage_item' asset type in IGC, displayed as 'DataStage Item' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DatastageItem extends Reference {

    @JsonIgnore public static final String IGC_TYPE_ID = "datastage_item";

    /**
     * The 'repository_id' property, displayed as 'Repository ID' in the IGC UI.
     */
    protected String repository_id;

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'class_name' property, displayed as 'Class Name' in the IGC UI.
     */
    protected String class_name;

    /**
     * The 'reference_item' property, displayed as 'Reference Item' in the IGC UI.
     */
    protected Boolean reference_item;

    /**
     * The 'created_by' property, displayed as 'Created By' in the IGC UI.
     */
    protected String created_by;

    /**
     * The 'created_on' property, displayed as 'Created on' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The 'modified_by' property, displayed as 'Modified By' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The 'modified_on' property, displayed as 'Last Modified on' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #repository_id */ @JsonProperty("repository_id")  public String getRepositoryId() { return this.repository_id; }
    /** @see #repository_id */ @JsonProperty("repository_id")  public void setRepositoryId(String repository_id) { this.repository_id = repository_id; }

    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #class_name */ @JsonProperty("class_name")  public String getClassName() { return this.class_name; }
    /** @see #class_name */ @JsonProperty("class_name")  public void setClassName(String class_name) { this.class_name = class_name; }

    /** @see #reference_item */ @JsonProperty("reference_item")  public Boolean getReferenceItem() { return this.reference_item; }
    /** @see #reference_item */ @JsonProperty("reference_item")  public void setReferenceItem(Boolean reference_item) { this.reference_item = reference_item; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }


    public static final Boolean isDatastageItem(Object obj) { return (obj.getClass() == DatastageItem.class); }

}
