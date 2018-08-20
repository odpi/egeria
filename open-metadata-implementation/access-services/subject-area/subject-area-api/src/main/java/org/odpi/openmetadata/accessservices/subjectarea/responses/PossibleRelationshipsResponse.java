/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * PossibleRelationshipsResponse is the response structure used on the subject area OMAS REST API calls that return
 * possible relationships as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PossibleRelationshipsResponse extends SubjectAreaOMASAPIResponse
{
    private Set<String> possibleRelationships  = null;


    /**
     * Default constructor
     */
    public PossibleRelationshipsResponse()
    {
        this.setResponseCategory(ResponseCategory.PossibleRelationships);
    }
    public PossibleRelationshipsResponse(Set<String> possibleRelationships)
    {
        this.possibleRelationships=possibleRelationships;
        this.setResponseCategory(ResponseCategory.PossibleRelationships);
    }

    /**
     * Return the possible relationships.
     *
     * @return all details known about the asset
     */
    public Set<String> getPossibleRelationships()
    {
        return possibleRelationships;
    }

    /**
     * Set up the possible relationships .
     *
     * @param possibleRelationships - all the possible relationships
     */
    public void setPossibleRelationships(Set<String> possibleRelationships)
    {
        this.possibleRelationships=possibleRelationships;
    }

    @Override
    public String toString()
    {
        return "AssetUniverseResponse{" +
                "assetUniverse=" + possibleRelationships +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
