/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'cdc_mapping' asset type in IGC, displayed as 'CDC Mapping' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CdcMapping extends MainObject {

    public static final String IGC_TYPE_ID = "cdc_mapping";

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


    public static final Boolean isCdcMapping(Object obj) { return (obj.getClass() == CdcMapping.class); }

}
