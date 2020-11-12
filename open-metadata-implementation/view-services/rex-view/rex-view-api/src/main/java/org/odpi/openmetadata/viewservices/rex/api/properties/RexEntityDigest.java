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
public class RexEntityDigest {

    private String  entityGUID;
    private String  label;
    private Integer gen;
    private String  metadataCollectionName; // name of the mdc that is home to this entity
    private String  metadataCollectionId;   // id of the mdc that is home to this entity
    private String  provenance;             // "ent" if query was at enterprise scope
                                            // "refCopy" if not ent query and returned by repo other than home
                                            // "home" if not ent query and returned by home repo
                                            // "proxy" if returned as a rel end proxy

    public RexEntityDigest(String entityGUID,
                           String label,
                           Integer gen,
                           String metadataCollectionName,
                           String metadataCollectionId,
                           String provenance)
    {
       this.entityGUID = entityGUID;
       this.label = label;
       this.gen = gen;
       this.metadataCollectionName = metadataCollectionName;
       this.metadataCollectionId = metadataCollectionId;
       this.provenance = provenance;
    }

    /*
     * Getters for Jackson
     */

    public String  getEntityGUID() { return entityGUID; }
    public String  getLabel() { return label; }
    public Integer getGen() { return gen; }
    public String  getMetadataCollectionName() { return metadataCollectionName; }
    public String  getMetadataCollectionId() { return metadataCollectionId; }
    public String  getProvenance() { return provenance; }

    public void setEntityGUID(String entityGUID) { this.entityGUID = entityGUID; }
    public void setLabel(String label) { this.label = label; }
    public void setGen(Integer gen) { this.gen = gen; }
    public void setMetadataCollectionName(String metadataCollectionName) { this.metadataCollectionName = metadataCollectionName; }
    public void setMetadataCollectionId(String metadataCollectionId) { this.metadataCollectionId = metadataCollectionId; }
    public void setProvenance(String provenance) { this.provenance = provenance; }



    @Override
    public String toString()
    {
        return "RexEntityDigest{" +
                ", entityGUID=" + entityGUID +
                ", label=" + label +
                ", gen=" + gen +
                ", metadataCollectionName=" + metadataCollectionName +
                ", metadataCollectionId=" + metadataCollectionId +
                ", provenance=" + provenance +
                '}';
    }



}
