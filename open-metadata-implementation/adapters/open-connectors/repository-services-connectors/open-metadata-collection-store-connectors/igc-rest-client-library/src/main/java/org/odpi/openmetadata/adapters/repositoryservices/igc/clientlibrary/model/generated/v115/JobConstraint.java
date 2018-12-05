/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'job_constraint' asset type in IGC, displayed as 'Job Constraint' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class JobConstraint extends MainObject {

    public static final String IGC_TYPE_ID = "job_constraint";

    /**
     * The 'of_link' property, displayed as 'Link' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference of_link;

    /**
     * The 'expression' property, displayed as 'Expression' in the IGC UI.
     */
    protected String expression;

    /**
     * The 'uses_flow_variable' property, displayed as 'Stage Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataItem} objects.
     */
    protected ReferenceList uses_flow_variable;


    /** @see #of_link */ @JsonProperty("of_link")  public Reference getOfLink() { return this.of_link; }
    /** @see #of_link */ @JsonProperty("of_link")  public void setOfLink(Reference of_link) { this.of_link = of_link; }

    /** @see #expression */ @JsonProperty("expression")  public String getExpression() { return this.expression; }
    /** @see #expression */ @JsonProperty("expression")  public void setExpression(String expression) { this.expression = expression; }

    /** @see #uses_flow_variable */ @JsonProperty("uses_flow_variable")  public ReferenceList getUsesFlowVariable() { return this.uses_flow_variable; }
    /** @see #uses_flow_variable */ @JsonProperty("uses_flow_variable")  public void setUsesFlowVariable(ReferenceList uses_flow_variable) { this.uses_flow_variable = uses_flow_variable; }


    public static final Boolean isJobConstraint(Object obj) { return (obj.getClass() == JobConstraint.class); }

}
