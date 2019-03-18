/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.governanceservers.openlineage.model.rest.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.governanceservers.openlineage.model.Relationship;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationshipsResponse is the response structure used on the Asset Lineage OMAS REST API calls that
 * returns a relationships object as a response.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationshipResponse extends AssetLineageOMASAPIResponse implements Serializable {

    private Relationship relationship;

    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }
}