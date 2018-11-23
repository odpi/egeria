/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'transforms_function' asset type in IGC, displayed as 'Transforms Function' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TransformsFunction extends MainObject {

    public static final String IGC_TYPE_ID = "transforms_function";

    /**
     * The 'transformation_project' property, displayed as 'Transformation Project' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TransformationProject} object.
     */
    protected Reference transformation_project;

    /**
     * The 'source_code' property, displayed as 'Definition' in the IGC UI.
     */
    protected String source_code;

    /**
     * The 'arguments' property, displayed as 'Arguments' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TransformArgument} objects.
     */
    protected ReferenceList arguments;


    /** @see #transformation_project */ @JsonProperty("transformation_project")  public Reference getTransformationProject() { return this.transformation_project; }
    /** @see #transformation_project */ @JsonProperty("transformation_project")  public void setTransformationProject(Reference transformation_project) { this.transformation_project = transformation_project; }

    /** @see #source_code */ @JsonProperty("source_code")  public String getSourceCode() { return this.source_code; }
    /** @see #source_code */ @JsonProperty("source_code")  public void setSourceCode(String source_code) { this.source_code = source_code; }

    /** @see #arguments */ @JsonProperty("arguments")  public ReferenceList getArguments() { return this.arguments; }
    /** @see #arguments */ @JsonProperty("arguments")  public void setArguments(ReferenceList arguments) { this.arguments = arguments; }


    public static final Boolean isTransformsFunction(Object obj) { return (obj.getClass() == TransformsFunction.class); }

}
