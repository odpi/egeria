/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * The "label" asset type in IGC is one of the few that does not inherent the common characteristics of MainObject.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Label extends Reference {

    public static final String IGC_TYPE_ID = "label";

    /**
     * Provides the context to the unique identity of this asset.
     */
    protected ArrayList<Reference> _context = new ArrayList<Reference>();

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     * <br><br>
     * Equivalent to the '_name' property of a Reference, but only populated when an asset has been retrieved
     * rather than just its reference.
     */
    protected String name;

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'labeled_assets' property, displayed as 'Labeled Assets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList labeled_assets;

    /** @see #_context */ @JsonProperty("_context") public ArrayList<Reference> getContext() { return this._context; }
    /** @see #_context */ @JsonProperty("_context") public void setContext(ArrayList<Reference> _context) { this._context = _context; }

    /** @see #name */ @JsonProperty("name") public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name") public void setTheName(String name) { this.name = name; }

    /** @see #description */ @JsonProperty("description") public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description") public void setDescription(String description) { this.description = description; }

    /** @see #labeled_assets */ @JsonProperty("labeled_assets") public ReferenceList getLabeledAssets() { return this.labeled_assets; }
    /** @see #labeled_assets */ @JsonProperty("labeled_assets") public void setLabeledAssets(ReferenceList labeled_assets) { this.labeled_assets = labeled_assets; }

    public static final Boolean isLabel(Object obj) {
        return (obj.getClass() == Label.class);
    }

}
