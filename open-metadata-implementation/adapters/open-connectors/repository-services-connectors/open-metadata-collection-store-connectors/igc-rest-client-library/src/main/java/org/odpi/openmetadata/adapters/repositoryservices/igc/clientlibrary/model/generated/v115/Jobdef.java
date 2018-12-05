/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'jobdef' asset type in IGC, displayed as 'JobDef' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Jobdef extends MainObject {

    public static final String IGC_TYPE_ID = "jobdef";

    /**
     * The 'alias' property, displayed as 'Alias' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList alias;


    /** @see #alias */ @JsonProperty("alias")  public ReferenceList getAlias() { return this.alias; }
    /** @see #alias */ @JsonProperty("alias")  public void setAlias(ReferenceList alias) { this.alias = alias; }


    public static final Boolean isJobdef(Object obj) { return (obj.getClass() == Jobdef.class); }

}
