/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'applicationfunction' asset type in IGC, displayed as 'ApplicationFunction' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Applicationfunction extends MainObject {

    public static final String IGC_TYPE_ID = "applicationfunction";

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'uses_class_descriptor' property, displayed as 'Uses Class Descriptor' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Classdescriptor} objects.
     */
    protected ReferenceList uses_class_descriptor;


    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #uses_class_descriptor */ @JsonProperty("uses_class_descriptor")  public ReferenceList getUsesClassDescriptor() { return this.uses_class_descriptor; }
    /** @see #uses_class_descriptor */ @JsonProperty("uses_class_descriptor")  public void setUsesClassDescriptor(ReferenceList uses_class_descriptor) { this.uses_class_descriptor = uses_class_descriptor; }


    public static final Boolean isApplicationfunction(Object obj) { return (obj.getClass() == Applicationfunction.class); }

}
