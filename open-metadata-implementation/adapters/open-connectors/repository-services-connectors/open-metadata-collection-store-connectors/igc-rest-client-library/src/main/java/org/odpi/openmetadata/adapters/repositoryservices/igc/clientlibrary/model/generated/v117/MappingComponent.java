/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'mapping_component' asset type in IGC, displayed as 'Mapping Component' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MappingComponent extends MainObject {

    public static final String IGC_TYPE_ID = "mapping_component";

    /**
     * The 'mapping_project' property, displayed as 'Mapping Project' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MappingProject} objects.
     */
    protected ReferenceList mapping_project;

    /**
     * The 'shared_containers' property, displayed as 'Shared Containers' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link ReferencedContainer} object.
     */
    protected Reference shared_containers;


    /** @see #mapping_project */ @JsonProperty("mapping_project")  public ReferenceList getMappingProject() { return this.mapping_project; }
    /** @see #mapping_project */ @JsonProperty("mapping_project")  public void setMappingProject(ReferenceList mapping_project) { this.mapping_project = mapping_project; }

    /** @see #shared_containers */ @JsonProperty("shared_containers")  public Reference getSharedContainers() { return this.shared_containers; }
    /** @see #shared_containers */ @JsonProperty("shared_containers")  public void setSharedContainers(Reference shared_containers) { this.shared_containers = shared_containers; }


    public static final Boolean isMappingComponent(Object obj) { return (obj.getClass() == MappingComponent.class); }

}
