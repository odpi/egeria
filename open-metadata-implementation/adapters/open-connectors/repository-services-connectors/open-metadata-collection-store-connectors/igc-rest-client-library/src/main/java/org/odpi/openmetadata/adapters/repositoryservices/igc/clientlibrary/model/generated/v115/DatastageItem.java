/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
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
public class DatastageItem extends MainObject {

    public static final String IGC_TYPE_ID = "datastage_item";

    /**
     * The 'repository_id' property, displayed as 'Repository ID' in the IGC UI.
     */
    protected String repository_id;

    /**
     * The 'class_name' property, displayed as 'Class Name' in the IGC UI.
     */
    protected String class_name;

    /**
     * The 'reference_item' property, displayed as 'Reference Item' in the IGC UI.
     */
    protected Boolean reference_item;


    /** @see #repository_id */ @JsonProperty("repository_id")  public String getRepositoryId() { return this.repository_id; }
    /** @see #repository_id */ @JsonProperty("repository_id")  public void setRepositoryId(String repository_id) { this.repository_id = repository_id; }

    /** @see #class_name */ @JsonProperty("class_name")  public String getClassName() { return this.class_name; }
    /** @see #class_name */ @JsonProperty("class_name")  public void setClassName(String class_name) { this.class_name = class_name; }

    /** @see #reference_item */ @JsonProperty("reference_item")  public Boolean getReferenceItem() { return this.reference_item; }
    /** @see #reference_item */ @JsonProperty("reference_item")  public void setReferenceItem(Boolean reference_item) { this.reference_item = reference_item; }


    public static final Boolean isDatastageItem(Object obj) { return (obj.getClass() == DatastageItem.class); }

}
