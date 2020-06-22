/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.rex.api.rest;


import org.odpi.openmetadata.viewservices.rex.api.properties.RexEntityDigest;
import org.odpi.openmetadata.viewservices.rex.api.properties.RexRelationshipDigest;

import java.util.Map;

public class RexSearchResponse extends RexViewOMVSAPIResponse {


    private String        serverName;
    private String        searchCategory;   // Set to either { "Entity" | "Relationship";}
    private String        searchText;

    // Fields that contain the maps of instance summaries.
    // An instance summary is much smaller than the full instance.
    // The entities map is keyed by entityGUID and the value part consists of
    //    { entityGUID, label, gen }
    // The relationships map is keyed by relationshipGUID and the value part consists of
    //    { relationshipGUID, end1GUID, end2GUID, idx, label, gen }
    // The above value types are described by the RexEntityDigest and RexRelationshipDigest Java classes.
    private Map<String, RexEntityDigest>         entities;
    private Map<String, RexRelationshipDigest>   relationships;

    /**
     * Default constructor
     */
    public RexSearchResponse()
    {
        super();
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RexSearchResponse(RexSearchResponse template)
    {
        super(template);

        if (template != null) {
            this.serverName = template.getServerName();
            this.searchCategory = template.getSearchCategory();
            this.searchText = template.getSearchText();
            this.entities = template.getEntities();
            this.relationships = template.getRelationships();
        }
    }




    public String getServerName() { return this.serverName; }

    public String getSearchText() { return this.searchText; }

    public String getSearchCategory() { return this.searchCategory; }

    public Map<String,RexEntityDigest> getEntities() {  return this.entities;  }

    public Map<String,RexRelationshipDigest> getRelationships() {
        return this.relationships;
    }


    public void setServerName(String serverName) { this.serverName = serverName; }

    public void setSearchText(String searchText) { this.searchText = searchText; }

    public void setSearchCategory(String searchCategory) { this.searchCategory = searchCategory; }

    public void setEntities(Map<String,RexEntityDigest>  entities) { this.entities = entities; }

    public void setRelationships(Map<String,RexRelationshipDigest>  relationships) { this.relationships = relationships; }
}
