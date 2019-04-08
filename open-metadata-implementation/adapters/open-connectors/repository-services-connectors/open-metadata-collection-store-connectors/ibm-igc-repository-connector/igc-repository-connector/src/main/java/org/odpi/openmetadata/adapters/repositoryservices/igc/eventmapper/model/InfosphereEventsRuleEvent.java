/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The payload contents of all Rule-specific events on the InfosphereEvents topic.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InfosphereEventsRuleEvent extends InfosphereEvents {

    /**
     * The 'projectRid' property indicates the Repository ID (RID) of the Information Analyzer project.
     */
    protected String projectRid;

    /**
     * The 'tamRid' property indicates the Repository ID (RID) of a Table Analysis Master object.
     * Unfortunately these objects are currently not queryable through IGC's REST API.
     */
    protected String tamRid;

    /**
     * The 'commonRuleRid' property indicates the Repository ID (RID) of a data rule or set ('data_rule' or 'rule_set')
     * that is fully-bound to be executable.
     */
    protected String commonRuleRid;

    /**
     * The 'ruleRid' property indicates the Repository ID (RID) of some other data rule or set ('data_rule' or 'rule_set')
     * representation, without the bindings.
     */
    protected String ruleRid;

    /** @see #projectRid */ @JsonProperty("projectRid") public String getProjectRid() { return this.projectRid; }
    /** @see #projectRid */ @JsonProperty("projectRid") public void setProjectRid(String projectRid) { this.projectRid = projectRid; }

    /** @see #tamRid */ @JsonProperty("tamRid") public String getTamRid() { return this.tamRid; }
    /** @see #tamRid */ @JsonProperty("tamRid") public void setTamRid(String tamRid) { this.tamRid = tamRid; }

    /** @see #commonRuleRid */ @JsonProperty("commonRuleRid") public String getCommonRuleRid() { return this.commonRuleRid; }
    /** @see #commonRuleRid */ @JsonProperty("commonRuleRid") public void setCommonRuleRid(String commonRuleRid) { this.commonRuleRid = commonRuleRid; }

    /** @see #ruleRid */ @JsonProperty("ruleRid") public String getRuleRid() { return this.ruleRid; }
    /** @see #ruleRid */ @JsonProperty("ruleRid") public void setRuleRid(String ruleRid) { this.ruleRid = ruleRid; }

}
