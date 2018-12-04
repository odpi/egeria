/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'dsparameter_set' asset type in IGC, displayed as 'Parameter' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DsparameterSet extends MainObject {

    public static final String IGC_TYPE_ID = "dsparameter_set";

    /**
     * The 'of_parameter_set' property, displayed as 'Context' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link ParameterSet2} object.
     */
    protected Reference of_parameter_set;

    /**
     * The 'display_caption' property, displayed as 'Display Caption' in the IGC UI.
     */
    protected String display_caption;

    /**
     * The 'default_value' property, displayed as 'Default Value' in the IGC UI.
     */
    protected String default_value;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>UNUSED (displayed in the UI as 'UNUSED')</li>
     *     <li>ENCRYPTED (displayed in the UI as 'ENCRYPTED')</li>
     *     <li>PATHNAME (displayed in the UI as 'PATHNAME')</li>
     *     <li>STRINGLIST (displayed in the UI as 'STRINGLIST')</li>
     *     <li>INPUTCOL (displayed in the UI as 'INPUTCOL')</li>
     *     <li>OUTPUTCOL (displayed in the UI as 'OUTPUTCOL')</li>
     *     <li>NCHAR (displayed in the UI as 'NCHAR')</li>
     *     <li>PARAMETERSET (displayed in the UI as 'PARAMETERSET')</li>
     * </ul>
     */
    protected String type;

    /**
     * The 'help_text' property, displayed as 'Help Text' in the IGC UI.
     */
    protected String help_text;


    /** @see #of_parameter_set */ @JsonProperty("of_parameter_set")  public Reference getOfParameterSet() { return this.of_parameter_set; }
    /** @see #of_parameter_set */ @JsonProperty("of_parameter_set")  public void setOfParameterSet(Reference of_parameter_set) { this.of_parameter_set = of_parameter_set; }

    /** @see #display_caption */ @JsonProperty("display_caption")  public String getDisplayCaption() { return this.display_caption; }
    /** @see #display_caption */ @JsonProperty("display_caption")  public void setDisplayCaption(String display_caption) { this.display_caption = display_caption; }

    /** @see #default_value */ @JsonProperty("default_value")  public String getDefaultValue() { return this.default_value; }
    /** @see #default_value */ @JsonProperty("default_value")  public void setDefaultValue(String default_value) { this.default_value = default_value; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #help_text */ @JsonProperty("help_text")  public String getHelpText() { return this.help_text; }
    /** @see #help_text */ @JsonProperty("help_text")  public void setHelpText(String help_text) { this.help_text = help_text; }


    public static final Boolean isDsparameterSet(Object obj) { return (obj.getClass() == DsparameterSet.class); }

}
