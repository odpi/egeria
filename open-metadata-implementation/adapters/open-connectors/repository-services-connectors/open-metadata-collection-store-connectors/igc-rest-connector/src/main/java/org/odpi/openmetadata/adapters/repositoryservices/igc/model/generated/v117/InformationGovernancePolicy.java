/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'information_governance_policy' asset type in IGC, displayed as 'Information Governance Policy' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationGovernancePolicy extends MainObject {

    public static final String IGC_TYPE_ID = "information_governance_policy";

    /**
     * The 'parent_policy' property, displayed as 'Parent Policy' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationGovernancePolicy} object.
     */
    protected Reference parent_policy;

    /**
     * The 'language' property, displayed as 'Language' in the IGC UI.
     */
    protected String language;

    /**
     * The 'subpolicies' property, displayed as 'Subpolicies' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernancePolicy} objects.
     */
    protected ReferenceList subpolicies;

    /**
     * The 'information_governance_rules' property, displayed as 'Information Governance Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList information_governance_rules;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;

    /**
     * The 'workflow_current_state' property, displayed as 'Workflow Current State' in the IGC UI.
     */
    protected ArrayList<String> workflow_current_state;

    /**
     * The 'workflow_stored_state' property, displayed as 'Workflow Stored State' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>DRAFT (displayed in the UI as 'DRAFT')</li>
     *     <li>WAITING_APPROVAL (displayed in the UI as 'WAITING_APPROVAL')</li>
     *     <li>APPROVED (displayed in the UI as 'APPROVED')</li>
     * </ul>
     */
    protected ArrayList<String> workflow_stored_state;

    /**
     * The 'glossary_type' property, displayed as 'Glossary Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>PUBLISHED (displayed in the UI as 'PUBLISHED')</li>
     *     <li>DRAFT (displayed in the UI as 'DRAFT')</li>
     * </ul>
     */
    protected String glossary_type;


    /** @see #parent_policy */ @JsonProperty("parent_policy")  public Reference getParentPolicy() { return this.parent_policy; }
    /** @see #parent_policy */ @JsonProperty("parent_policy")  public void setParentPolicy(Reference parent_policy) { this.parent_policy = parent_policy; }

    /** @see #language */ @JsonProperty("language")  public String getLanguage() { return this.language; }
    /** @see #language */ @JsonProperty("language")  public void setLanguage(String language) { this.language = language; }

    /** @see #subpolicies */ @JsonProperty("subpolicies")  public ReferenceList getSubpolicies() { return this.subpolicies; }
    /** @see #subpolicies */ @JsonProperty("subpolicies")  public void setSubpolicies(ReferenceList subpolicies) { this.subpolicies = subpolicies; }

    /** @see #information_governance_rules */ @JsonProperty("information_governance_rules")  public ReferenceList getInformationGovernanceRules() { return this.information_governance_rules; }
    /** @see #information_governance_rules */ @JsonProperty("information_governance_rules")  public void setInformationGovernanceRules(ReferenceList information_governance_rules) { this.information_governance_rules = information_governance_rules; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }

    /** @see #workflow_current_state */ @JsonProperty("workflow_current_state")  public ArrayList<String> getWorkflowCurrentState() { return this.workflow_current_state; }
    /** @see #workflow_current_state */ @JsonProperty("workflow_current_state")  public void setWorkflowCurrentState(ArrayList<String> workflow_current_state) { this.workflow_current_state = workflow_current_state; }

    /** @see #workflow_stored_state */ @JsonProperty("workflow_stored_state")  public ArrayList<String> getWorkflowStoredState() { return this.workflow_stored_state; }
    /** @see #workflow_stored_state */ @JsonProperty("workflow_stored_state")  public void setWorkflowStoredState(ArrayList<String> workflow_stored_state) { this.workflow_stored_state = workflow_stored_state; }

    /** @see #glossary_type */ @JsonProperty("glossary_type")  public String getGlossaryType() { return this.glossary_type; }
    /** @see #glossary_type */ @JsonProperty("glossary_type")  public void setGlossaryType(String glossary_type) { this.glossary_type = glossary_type; }


    public static final Boolean isInformationGovernancePolicy(Object obj) { return (obj.getClass() == InformationGovernancePolicy.class); }

}
