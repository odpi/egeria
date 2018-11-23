/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'parameterset' asset type in IGC, displayed as 'ParameterSet' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ParameterSet2 extends MainObject {

    public static final String IGC_TYPE_ID = "parameterset";

    /**
     * The 'has_parameter_def' property, displayed as 'Has Parameter Def' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Parameter} objects.
     */
    protected ReferenceList has_parameter_def;

    /**
     * The 'used_as_parameter_def' property, displayed as 'Used As Parameter Def' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Parameter} objects.
     */
    protected ReferenceList used_as_parameter_def;


    /** @see #has_parameter_def */ @JsonProperty("has_parameter_def")  public ReferenceList getHasParameterDef() { return this.has_parameter_def; }
    /** @see #has_parameter_def */ @JsonProperty("has_parameter_def")  public void setHasParameterDef(ReferenceList has_parameter_def) { this.has_parameter_def = has_parameter_def; }

    /** @see #used_as_parameter_def */ @JsonProperty("used_as_parameter_def")  public ReferenceList getUsedAsParameterDef() { return this.used_as_parameter_def; }
    /** @see #used_as_parameter_def */ @JsonProperty("used_as_parameter_def")  public void setUsedAsParameterDef(ReferenceList used_as_parameter_def) { this.used_as_parameter_def = used_as_parameter_def; }


    public static final Boolean isParameterSet2(Object obj) { return (obj.getClass() == ParameterSet2.class); }

}
