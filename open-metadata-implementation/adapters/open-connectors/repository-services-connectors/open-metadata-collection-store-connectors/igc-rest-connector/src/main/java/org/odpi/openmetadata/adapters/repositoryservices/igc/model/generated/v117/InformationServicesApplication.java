/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'information_services_application' asset type in IGC, displayed as 'Information Services Application' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationServicesApplication extends MainObject {

    public static final String IGC_TYPE_ID = "information_services_application";

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'information_services_project' property, displayed as 'Information Services Project' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationServicesProject} object.
     */
    protected Reference information_services_project;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #information_services_project */ @JsonProperty("information_services_project")  public Reference getInformationServicesProject() { return this.information_services_project; }
    /** @see #information_services_project */ @JsonProperty("information_services_project")  public void setInformationServicesProject(Reference information_services_project) { this.information_services_project = information_services_project; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isInformationServicesApplication(Object obj) { return (obj.getClass() == InformationServicesApplication.class); }

}
