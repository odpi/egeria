/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.rex.api.rest;


import org.odpi.openmetadata.viewservices.rex.api.properties.RexEntityDigest;
import org.odpi.openmetadata.viewservices.rex.api.properties.RexRelationshipAndEntitiesDigest;

import java.util.Map;

public class RexSearchResponse extends RexViewOMVSAPIResponse {


    private String        serverName;
    private String        searchCategory;   // Set to either { "Entity" | "Relationship"}
    private String        searchText;

    /*
     * Maps of digests. A digest is much smaller than the full instance.
     *
     * The entity digest map is keyed by entityGUID and each digest consists of
     * { entityGUID, label, gen, metadataCollectionName, metadataCollectionId, provenance }
     *
     * The relationship digest map is keyed by relationshipGUID and each digest
     * consists of a triplet of digests:
     * { relationshipDigest, end1Digest, end2Digest }, where the entity digests are
     * as described above and the relationship digest is similar, but pertains to a
     * relationship, so it contains
     * { relationshipGUID, label, end1GUID, end2GUID, idx, gen, metadataCollectionName,
     *   metadataCollectionId, provenance }
     */
    private Map<String, RexEntityDigest>         entities;
    private Map<String, RexRelationshipAndEntitiesDigest>   relationships;

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

    public Map<String, RexRelationshipAndEntitiesDigest> getRelationships() {
        return this.relationships;
    }


    public void setServerName(String serverName) { this.serverName = serverName; }

    public void setSearchText(String searchText) { this.searchText = searchText; }

    public void setSearchCategory(String searchCategory) { this.searchCategory = searchCategory; }

    public void setEntities(Map<String,RexEntityDigest>  entities) { this.entities = entities; }

    public void setRelationships(Map<String,RexRelationshipAndEntitiesDigest>  relationships) { this.relationships = relationships; }
}
