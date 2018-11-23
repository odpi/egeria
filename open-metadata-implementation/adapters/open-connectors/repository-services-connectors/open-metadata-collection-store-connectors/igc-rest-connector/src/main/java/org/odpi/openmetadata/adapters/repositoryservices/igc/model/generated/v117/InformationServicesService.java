/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'information_services_service' asset type in IGC, displayed as 'Information Services Service' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationServicesService extends MainObject {

    public static final String IGC_TYPE_ID = "information_services_service";

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'information_services_application' property, displayed as 'Information Services Application' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationServicesApplication} object.
     */
    protected Reference information_services_application;


    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #information_services_application */ @JsonProperty("information_services_application")  public Reference getInformationServicesApplication() { return this.information_services_application; }
    /** @see #information_services_application */ @JsonProperty("information_services_application")  public void setInformationServicesApplication(Reference information_services_application) { this.information_services_application = information_services_application; }


    public static final Boolean isInformationServicesService(Object obj) { return (obj.getClass() == InformationServicesService.class); }

}
