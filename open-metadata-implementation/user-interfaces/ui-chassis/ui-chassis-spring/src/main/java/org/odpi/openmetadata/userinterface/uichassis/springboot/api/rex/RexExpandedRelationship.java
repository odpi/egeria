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

    // TODO bring out yer dead...

    private Relationship          relationship;
    private RexRelationshipDigest relationshipDigest;
    private RexEntityDigest       entityOneDigest;
    private RexEntityDigest       entityTwoDigest;
    private String                serverName;         // name of the server that returned this object
    //private String  label;
    //private String  end1GUID;
    //private String  end2GUID;
    //private Integer idx;
    //private Integer gen;

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

       //this.label = label;
       //this.end1GUID = end1GUID;
       //this.end2GUID = end2GUID;
       //this.idx = idx;
       //this.gen = gen;
    }

    /*
     * Getters for Jackson
     */

    public Relationship getRelationship() { return relationship; }
    public RexRelationshipDigest getRelationshipDigest() { return relationshipDigest; }
    public RexEntityDigest getEntityOneDigest() { return entityOneDigest; }
    public RexEntityDigest getEntityTwoDigest() { return entityTwoDigest; }
    public String getServerName() { return serverName; }
    //public String getEnd1GUID() { return end1GUID; }
    //public String getEnd2GUID() { return end2GUID; }
    //public String getLabel() { return label; }
    //public Integer getIdx() { return idx; }
    //public Integer getGen() { return gen; }

    public void setRelationship(Relationship relationship) { this.relationship = relationship; }
    public void setRelationshipDigest(RexRelationshipDigest relationshipDigest) { this.relationshipDigest = relationshipDigest; }
    public void setEntityOneDigest(RexEntityDigest entityOneDigest) { this.entityOneDigest = entityOneDigest; }
    public void setEntityTwoDigest(RexEntityDigest entityTwoDigest) { this.entityTwoDigest = entityTwoDigest; }
    public void setServerName(String serverName) { this.serverName = serverName; }
    //public void setLabel(String label) { this.label = label; }
    //public void setEnd1GUID(String end1GUID) { this.end1GUID = end1GUID; }
    //public void setEnd2GUID(String end2GUID) { this.end2GUID = end2GUID; }
    //public void setIdx(Integer idx) { this.idx = idx; }
    //public void setGen(Integer gen) { this.gen = gen; }



    @Override
    public String toString()
    {
        return "RexExpandedRelationship{" +
                ", relationship=" + relationship +
                ", relationshipDigest=" + relationshipDigest +
                ", serverName=" + serverName +
               // ", label=" + label +
               // ", end1GUID=" + end1GUID +  // TODO tidy up
               // ", end2GUID=" + end2GUID +
               // ", idx=" + idx +
               // ", gen=" + gen +
                '}';
    }



}
