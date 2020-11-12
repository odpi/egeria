/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.rex.api.properties;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RexExpandedEntityDetail {

    private EntityDetail    entityDetail;
    private RexEntityDigest entityDigest;
    private String          serverName;      // the name of the server that returned this object
    private String          platformName;    // the name of the platform running the server that returned this object


    public RexExpandedEntityDetail(EntityDetail entityDetail,
                                   String label,
                                   String serverName,
                                   String platformName,
                                   String provenance)
    {
       this.entityDetail = entityDetail;
       this.serverName = serverName;
       this.platformName = platformName;
       // Server-side we do not know which gen this is for - so set to 0.
       this.entityDigest = new RexEntityDigest(
               entityDetail.getGUID(),
               label,
               0,
               entityDetail.getMetadataCollectionName(),
               entityDetail.getMetadataCollectionId(),
               provenance );
    }

    /*
     * Getters for Jackson
     */

    public EntityDetail getEntityDetail() { return entityDetail; }
    public String getServerName() { return serverName; }
    public String getPlatformName() { return platformName; }
    public RexEntityDigest getEntityDigest() { return entityDigest; }

    public void setEntityDetail(EntityDetail entityDetail) { this.entityDetail = entityDetail; }
    public void setServerName(String serverName) { this.serverName = serverName; }
    public void setPlatformName(String platformName) { this.platformName = platformName; }
    public void setEntityDigest(RexEntityDigest entityDigest) { this.entityDigest = entityDigest; }



    @Override
    public String toString()
    {
        return "RexExpandedEntityDetail{" +
                ", entityDetail=" + entityDetail +
                ", entityDigest=" + entityDigest +
                ", serverName=" + serverName +
                ", platformName=" + platformName +
                '}';
    }



}
