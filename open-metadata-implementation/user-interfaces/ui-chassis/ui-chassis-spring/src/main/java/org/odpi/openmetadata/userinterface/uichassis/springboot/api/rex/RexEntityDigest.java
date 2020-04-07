/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.rex;

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
    private String  metadataCollectionName;

    public RexEntityDigest(String entityGUID, String label, Integer gen, String metadataCollectionName) {
       this.entityGUID = entityGUID;
       this.label = label;
       this.gen = gen;
       this.metadataCollectionName = metadataCollectionName;  /* The name of the metadataCollection that is home to the entity */
    }

    /*
     * Getters for Jackson
     */

    public String  getEntityGUID() { return entityGUID; }
    public String  getLabel() { return label; }
    public Integer getGen() { return gen; }
    public String  getMetadataCollectionName() { return metadataCollectionName; }

    public void setEntityGUID(String entityGUID) { this.entityGUID = entityGUID; }
    public void setLabel(String label) { this.label = label; }
    public void setGen(Integer gen) { this.gen = gen; }
    public void setMetadataCollectionName(String metadataCollectionName) { this.metadataCollectionName = metadataCollectionName; }



    @Override
    public String toString()
    {
        return "RexEntityDigest{" +
                ", entityGUID=" + entityGUID +
                ", label=" + label +
                ", gen=" + gen +
                ", metadataCollectionName=" + metadataCollectionName +
                '}';
    }



}
