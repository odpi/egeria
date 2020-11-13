/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.rex.api.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RexRelationshipDigest {

    private String  relationshipGUID;
    private String  label;
    private String  end1GUID;
    private String  end2GUID;
    private Integer idx;
    private Integer gen;
    private String  metadataCollectionName;
    private String  metadataCollectionId;
    private String  provenance;

    public RexRelationshipDigest(String relationshipGUID,
                                 String label,
                                 String end1GUID,
                                 String end2GUID,
                                 Integer idx,
                                 Integer gen,
                                 String metadataCollectionName,
                                 String metadataCollectionId,
                                 String provenance)
    {
       this.relationshipGUID = relationshipGUID;
       this.label = label;
       this.end1GUID = end1GUID;
       this.end2GUID = end2GUID;
       this.idx = idx;
       this.gen = gen;
       this.metadataCollectionName = metadataCollectionName;
       this.metadataCollectionId = metadataCollectionId;
       this.provenance = provenance;
    }

    /*
     * Getters for Jackson
     */

    public String getRelationshipGUID() { return relationshipGUID; }
    public String getEnd1GUID() { return end1GUID; }
    public String getEnd2GUID() { return end2GUID; }
    public String getLabel() { return label; }
    public Integer getIdx() { return idx; }
    public Integer getGen() { return gen; }
    public String getMetadataCollectionName() { return metadataCollectionName; }
    public String getMetadataCollectionId() { return metadataCollectionId; }
    public String getProvenance() { return provenance; }

    public void setRelationshipGUID(String relationshipGUID) { this.relationshipGUID = relationshipGUID; }
    public void setLabel(String label) { this.label = label; }
    public void setEnd1GUID(String end1GUID) { this.end1GUID = end1GUID; }
    public void setEnd2GUID(String end2GUID) { this.end2GUID = end2GUID; }
    public void setIdx(Integer idx) { this.idx = idx; }
    public void setGen(Integer gen) { this.gen = gen; }
    public void setMetadataCollectionName(String metadataCollectionName) { this.metadataCollectionName = metadataCollectionName; }
    public void setMetadataCollectionId(String metadataCollectionId) { this.metadataCollectionId = metadataCollectionId; }
    public void setProvenance(String provenance) { this.provenance = provenance; }



    @Override
    public String toString()
    {
        return "RexRelationshipDigest{" +
                ", relationshipGUID=" + relationshipGUID +
                ", label=" + label +
                ", end1GUID=" + end1GUID +
                ", end2GUID=" + end2GUID +
                ", idx=" + idx +
                ", gen=" + gen +
                ", metadataCollectionName=" + metadataCollectionName +
                ", metadataCollectionId=" + metadataCollectionId +
                ", provenance=" + provenance +
                '}';
    }



}
