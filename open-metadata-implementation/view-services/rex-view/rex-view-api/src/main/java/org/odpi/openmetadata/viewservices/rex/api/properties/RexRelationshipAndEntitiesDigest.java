/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.rex.api.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RexRelationshipAndEntitiesDigest {


    // The RexSubGraph class provides a container for a set of entities and relationships.
    // It is used as the return type from a relationship search, so that both the found
    // relationships and the associated (related) entities can be stored together. These
    // are then used to populate the RexSearchResponse.



    // Maps of instance digests.
    // An instance summary is much smaller than the full instance.
    // The entities map is keyed by entityGUID and each digest part consists of
    // entityGUID, label, gen, (home) metadataCollectionName, (home) metadataCollectionId, provenance
    //
    // The relationships map is keyed by relationshipGUID and each digest consists of
    // relationshipGUID, label, end1GUID, end2GUID, idx, gen, (home) metadataCollectionName, (home) metadataCollectionId, provenance
    //
    // The above value types are described by the RexEntityDigest and RexRelationshipDigest Java classes.

    private RexRelationshipDigest   relationshipDigest;
    private RexEntityDigest   end1Digest;
    private RexEntityDigest   end2Digest;




    public RexRelationshipAndEntitiesDigest() {

       // No initialization yet
    }


    public RexEntityDigest getEnd1Digest() { return end1Digest;}
    public RexEntityDigest getEnd2Digest() { return end2Digest;}

    public RexRelationshipDigest getRelationshipDigest() { return relationshipDigest;}


    public void setEnd1Digest(RexEntityDigest end1Digest) { this.end1Digest = end1Digest;}
    public void setEnd2Digest(RexEntityDigest end2Digest) { this.end2Digest = end2Digest;}

    public void setRelationshipDigest(RexRelationshipDigest relationshipDigest) { this.relationshipDigest = relationshipDigest;}


    @Override
    public String toString()
    {
        return "RexRelationshipAndEntitiesDigest{" +
                "relationshipDigest=" + relationshipDigest +
                ", end1Digest=" + end1Digest +
                ", end2Digest=" + end2Digest +
                '}';
    }



}
