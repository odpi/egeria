/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.rex;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RexExpandedRelationship {

    private Relationship          relationship;
    private RexRelationshipDigest relationshipDigest;
    private RexEntityDigest       entityOneDigest;
    private RexEntityDigest       entityTwoDigest;
    private String                serverName;         // name of the server that returned this object


    public RexExpandedRelationship(Relationship relationship, String label, RexEntityDigest digest1,  RexEntityDigest digest2, String serverName) {
       this.relationship = relationship;
       this.relationshipDigest = new RexRelationshipDigest(relationship.getGUID(),
                                                           label,
                                                           digest1.getEntityGUID(),
                                                           digest2.getEntityGUID(),
                                                           0,
                                                           0,
                                                           relationship.getMetadataCollectionName());
       this.entityOneDigest = digest1;
       this.entityTwoDigest = digest2;
       this.serverName      = serverName;

    }

    /*
     * Getters for Jackson
     */

    public Relationship getRelationship() { return relationship; }
    public RexRelationshipDigest getRelationshipDigest() { return relationshipDigest; }
    public RexEntityDigest getEntityOneDigest() { return entityOneDigest; }
    public RexEntityDigest getEntityTwoDigest() { return entityTwoDigest; }
    public String getServerName() { return serverName; }


    public void setRelationship(Relationship relationship) { this.relationship = relationship; }
    public void setRelationshipDigest(RexRelationshipDigest relationshipDigest) { this.relationshipDigest = relationshipDigest; }
    public void setEntityOneDigest(RexEntityDigest entityOneDigest) { this.entityOneDigest = entityOneDigest; }
    public void setEntityTwoDigest(RexEntityDigest entityTwoDigest) { this.entityTwoDigest = entityTwoDigest; }
    public void setServerName(String serverName) { this.serverName = serverName; }


    @Override
    public String toString()
    {
        return "RexExpandedRelationship{" +
                ", relationship=" + relationship +
                ", relationshipDigest=" + relationshipDigest +
                ", entityOneDigest=" + entityOneDigest +
                ", entityTwoDigest=" + entityTwoDigest +
                ", serverName=" + serverName +
                '}';
    }



}
