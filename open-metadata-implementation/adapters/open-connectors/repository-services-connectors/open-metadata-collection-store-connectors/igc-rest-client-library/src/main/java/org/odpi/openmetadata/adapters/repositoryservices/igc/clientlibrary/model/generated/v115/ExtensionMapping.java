/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'extension_mapping' asset type in IGC, displayed as 'Extension Mapping' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExtensionMapping extends MainObject {

    public static final String IGC_TYPE_ID = "extension_mapping";

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'extension_mapping_document' property, displayed as 'Extension Mapping Document' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference extension_mapping_document;

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
     * The 'function' property, displayed as 'Function' in the IGC UI.
     */
    protected String function;

    /**
     * The 'targets' property, displayed as 'Targets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList targets;


    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #extension_mapping_document */ @JsonProperty("extension_mapping_document")  public Reference getExtensionMappingDocument() { return this.extension_mapping_document; }
    /** @see #extension_mapping_document */ @JsonProperty("extension_mapping_document")  public void setExtensionMappingDocument(Reference extension_mapping_document) { this.extension_mapping_document = extension_mapping_document; }

    /** @see #sources */ @JsonProperty("sources")  public ReferenceList getSources() { return this.sources; }
    /** @see #sources */ @JsonProperty("sources")  public void setSources(ReferenceList sources) { this.sources = sources; }

    /** @see #rule */ @JsonProperty("rule")  public String getRule() { return this.rule; }
    /** @see #rule */ @JsonProperty("rule")  public void setRule(String rule) { this.rule = rule; }

    /** @see #function */ @JsonProperty("function")  public String getFunction() { return this.function; }
    /** @see #function */ @JsonProperty("function")  public void setFunction(String function) { this.function = function; }

    /** @see #targets */ @JsonProperty("targets")  public ReferenceList getTargets() { return this.targets; }
    /** @see #targets */ @JsonProperty("targets")  public void setTargets(ReferenceList targets) { this.targets = targets; }


    public static final Boolean isExtensionMapping(Object obj) { return (obj.getClass() == ExtensionMapping.class); }

}
