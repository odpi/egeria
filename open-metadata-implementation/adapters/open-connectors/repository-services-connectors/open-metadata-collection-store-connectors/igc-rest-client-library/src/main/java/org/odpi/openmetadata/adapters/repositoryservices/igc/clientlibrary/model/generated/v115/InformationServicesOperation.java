/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'information_services_operation' asset type in IGC, displayed as 'Information Services Operation' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationServicesOperation extends MainObject {

    public static final String IGC_TYPE_ID = "information_services_operation";

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'information_services_service' property, displayed as 'Information Services Service' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationServicesService} object.
     */
    protected Reference information_services_service;


    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #information_services_service */ @JsonProperty("information_services_service")  public Reference getInformationServicesService() { return this.information_services_service; }
    /** @see #information_services_service */ @JsonProperty("information_services_service")  public void setInformationServicesService(Reference information_services_service) { this.information_services_service = information_services_service; }


    public static final Boolean isInformationServicesOperation(Object obj) { return (obj.getClass() == InformationServicesOperation.class); }

}
