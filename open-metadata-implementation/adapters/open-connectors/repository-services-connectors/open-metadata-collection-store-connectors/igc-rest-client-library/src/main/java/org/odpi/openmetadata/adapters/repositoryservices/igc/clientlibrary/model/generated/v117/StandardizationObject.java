/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * POJO for the 'standardization_object' asset type in IGC, displayed as 'Standardization Object' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class StandardizationObject extends MainObject {

    public static final String IGC_TYPE_ID = "standardization_object";

    /**
     * The 'data_quality_specifications' property, displayed as 'Data Quality Specification' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link StandardizationObject} objects.
     */
    protected ReferenceList data_quality_specifications;

    /**
     * The 'folder' property, displayed as 'Folder' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Dsfolder} object.
     */
    protected Reference folder;

    /**
     * The 'used_by_stages' property, displayed as 'Stages' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Stage} objects.
     */
    protected ReferenceList used_by_stages;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     */
    protected String type;

    /**
     * The 'fields' property, displayed as 'Data Fields' in the IGC UI.
     */
    protected ArrayList<String> fields;


    /** @see #data_quality_specifications */ @JsonProperty("data_quality_specifications")  public ReferenceList getDataQualitySpecifications() { return this.data_quality_specifications; }
    /** @see #data_quality_specifications */ @JsonProperty("data_quality_specifications")  public void setDataQualitySpecifications(ReferenceList data_quality_specifications) { this.data_quality_specifications = data_quality_specifications; }

    /** @see #folder */ @JsonProperty("folder")  public Reference getFolder() { return this.folder; }
    /** @see #folder */ @JsonProperty("folder")  public void setFolder(Reference folder) { this.folder = folder; }

    /** @see #used_by_stages */ @JsonProperty("used_by_stages")  public ReferenceList getUsedByStages() { return this.used_by_stages; }
    /** @see #used_by_stages */ @JsonProperty("used_by_stages")  public void setUsedByStages(ReferenceList used_by_stages) { this.used_by_stages = used_by_stages; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #fields */ @JsonProperty("fields")  public ArrayList<String> getFields() { return this.fields; }
    /** @see #fields */ @JsonProperty("fields")  public void setFields(ArrayList<String> fields) { this.fields = fields; }


    public static final Boolean isStandardizationObject(Object obj) { return (obj.getClass() == StandardizationObject.class); }

}
