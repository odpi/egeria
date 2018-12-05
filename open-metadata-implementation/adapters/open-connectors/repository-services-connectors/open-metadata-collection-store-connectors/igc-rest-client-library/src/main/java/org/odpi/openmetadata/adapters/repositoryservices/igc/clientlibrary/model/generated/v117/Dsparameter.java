/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'dsparameter' asset type in IGC, displayed as 'Parameter' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Dsparameter extends MainObject {

    public static final String IGC_TYPE_ID = "dsparameter";

    /**
     * The 'of_container_def' property, displayed as 'Context' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link ReferencedContainer} object.
     */
    protected Reference of_container_def;

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


    /** @see #of_container_def */ @JsonProperty("of_container_def")  public Reference getOfContainerDef() { return this.of_container_def; }
    /** @see #of_container_def */ @JsonProperty("of_container_def")  public void setOfContainerDef(Reference of_container_def) { this.of_container_def = of_container_def; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }


    public static final Boolean isDsparameter(Object obj) { return (obj.getClass() == Dsparameter.class); }

}
