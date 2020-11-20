/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.rex.api.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import java.util.List;
import java.util.Map;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RexTraversal {


    // The RexTraversal class enables the packaging and interrogation of a traversal query
    // and a summary of the results - enough information for the display in Rex ut not the
    // whole InstanceGraph.
    // The traversal will have been centered on an entity instance and may have been filtered by
    // lists of entity types, relationship types and/or classification names.
    // The graph will also have been explored to the specified depth.
    // Associated with the traversal (and result) is the gen in the user sequence.
    // The settings of these parameters are stored in the RexTraversal.


    // Fields that describe the query that was performed:
    private String               entityGUID;               // must be non-null
    private List<String>         entityTypeNames;          // a list of type names or null
    private List<String>         relationshipTypeGUIDs;    // a list of type guids or null
    private List<String>         classificationNames;      // a list of names or null
    private Integer              depth;                    // the depth used to create the subgraph
    private Integer              gen;                      // which generation this subgraph pertains to
    private String               serverName;               // the name of the repo server that was traversed
    private String               platformName;             // the name of the platform for the operation


    // Fields that contain the maps of instance summaries.
    // An instance summary is much smaller than the full instance.
    // The entities map is keyed by entityGUID and the value part consists of
    //    { entityGUID, label, gen }
    // The relationships map is keyed by relationshipGUID and the value part consists of
    //    { relationshipGUID, end1GUID, end2GUID, idx, label, gen }
    // The above value types are described by the RexEntityDigest and RexRelationshipDigest Java classes.
    private Map<String,RexEntityDigest>         entities;
    private Map<String,RexRelationshipDigest>   relationships;


    public RexTraversal() {

       // No initialization yet
    }

    /*
     * Getters for Jackson
     */


    public String getEntityGUID() { return entityGUID; }

    public List<String> getEntityTypeNames() { return entityTypeNames; }

    public List<String> getRelationshipTypeGUIDs() {
        return relationshipTypeGUIDs;
    }

    public List<String> getClassificationNames() {
        return classificationNames;
    }

    public Integer getDepth() { return depth; }

    public Integer getGen() { return gen; }

    public Map<String,RexEntityDigest> getEntities() { return entities;}

    public Map<String,RexRelationshipDigest> getRelationships() { return relationships;}

    public String getServerName() { return serverName; }

    public String getPlatformName() { return platformName; }




    public void setEntityGUID(String entityGUID) { this.entityGUID = entityGUID; }

    public void setEntityTypeNames(List<String> entityTypeNames) { this.entityTypeNames = entityTypeNames; }

    public void setRelationshipTypeGUIDs(List<String> relationshipTypeGUIDs) { this.relationshipTypeGUIDs = relationshipTypeGUIDs; }

    public void setClassificationNames(List<String> classificationNames) { this.classificationNames = classificationNames; }

    public void setDepth(Integer depth) { this.depth = depth; }

    public void setGen(Integer gen) { this.gen = gen; }

    public void setEntities(Map<String,RexEntityDigest> entities) { this.entities = entities;}

    public void setRelationships(Map<String,RexRelationshipDigest> relationships) { this.relationships = relationships;}

    public void setServerName(String serverName) { this.serverName = serverName; }

    public void setPlatformName(String platformName) { this.platformName = platformName; }

    @Override
    public String toString()
    {
        return "RexTraversal{" +
                "entityGUID=" + entityGUID +
                ", depth=" + depth +
                ", gen=" + gen +
                ", entityTypeNames=" + entityTypeNames +
                ", relationshipTypeGUIDs=" + relationshipTypeGUIDs +
                ", classificationNames=" + classificationNames +
                ", entities=" + entities +
                ", relationships=" + relationships +
                ", serverName=" + serverName +
                ", platformName=" + platformName +
                '}';
    }



}
