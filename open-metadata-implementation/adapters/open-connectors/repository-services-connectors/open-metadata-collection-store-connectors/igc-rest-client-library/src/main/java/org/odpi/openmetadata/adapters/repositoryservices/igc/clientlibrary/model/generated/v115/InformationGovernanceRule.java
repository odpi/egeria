/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'information_governance_rule' asset type in IGC, displayed as 'Information Governance Rule' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationGovernanceRule extends MainObject {

    public static final String IGC_TYPE_ID = "information_governance_rule";

    /**
     * The 'referencing_policies' property, displayed as 'Referencing Policies' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernancePolicy} objects.
     */
    protected ReferenceList referencing_policies;

    /**
     * The 'language' property, displayed as 'Language' in the IGC UI.
     */
    protected String language;

    /**
     * The 'related_rules' property, displayed as 'Related Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList related_rules;

    /**
     * The 'implemented_by_assets' property, displayed as 'Implemented by Assets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList implemented_by_assets;

    /**
     * The 'governs_assets' property, displayed as 'Governs Assets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList governs_assets;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;

    /**
     * The 'workflow_current_state' property, displayed as 'Workflow Current State' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>DRAFT (displayed in the UI as 'DRAFT')</li>
     *     <li>WAITING_APPROVAL (displayed in the UI as 'WAITING_APPROVAL')</li>
     *     <li>APPROVED (displayed in the UI as 'APPROVED')</li>
     * </ul>
     */
    protected ArrayList<String> workflow_current_state;


    /** @see #referencing_policies */ @JsonProperty("referencing_policies")  public ReferenceList getReferencingPolicies() { return this.referencing_policies; }
    /** @see #referencing_policies */ @JsonProperty("referencing_policies")  public void setReferencingPolicies(ReferenceList referencing_policies) { this.referencing_policies = referencing_policies; }

    /** @see #language */ @JsonProperty("language")  public String getLanguage() { return this.language; }
    /** @see #language */ @JsonProperty("language")  public void setLanguage(String language) { this.language = language; }

    /** @see #related_rules */ @JsonProperty("related_rules")  public ReferenceList getRelatedRules() { return this.related_rules; }
    /** @see #related_rules */ @JsonProperty("related_rules")  public void setRelatedRules(ReferenceList related_rules) { this.related_rules = related_rules; }

    /** @see #implemented_by_assets */ @JsonProperty("implemented_by_assets")  public ReferenceList getImplementedByAssets() { return this.implemented_by_assets; }
    /** @see #implemented_by_assets */ @JsonProperty("implemented_by_assets")  public void setImplementedByAssets(ReferenceList implemented_by_assets) { this.implemented_by_assets = implemented_by_assets; }

    /** @see #governs_assets */ @JsonProperty("governs_assets")  public ReferenceList getGovernsAssets() { return this.governs_assets; }
    /** @see #governs_assets */ @JsonProperty("governs_assets")  public void setGovernsAssets(ReferenceList governs_assets) { this.governs_assets = governs_assets; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }

    /** @see #workflow_current_state */ @JsonProperty("workflow_current_state")  public ArrayList<String> getWorkflowCurrentState() { return this.workflow_current_state; }
    /** @see #workflow_current_state */ @JsonProperty("workflow_current_state")  public void setWorkflowCurrentState(ArrayList<String> workflow_current_state) { this.workflow_current_state = workflow_current_state; }


    public static final Boolean isInformationGovernanceRule(Object obj) { return (obj.getClass() == InformationGovernanceRule.class); }

}
