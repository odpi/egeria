/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * PossibleClassificationsResponse is the response structure used on the subject area OMAS REST API calls that return
 * possible classifications as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PossibleClassificationsResponse extends SubjectAreaOMASAPIResponse
{
    private Set<String> possibleClassifications  = null;


    /**
     * Default constructor
     */
    public PossibleClassificationsResponse()
    {
        this.setResponseCategory(ResponseCategory.PossibleClassifications);
    }
    public PossibleClassificationsResponse(Set<String> possibleClassifications)
    {
        this.possibleClassifications=possibleClassifications;
        this.setResponseCategory(ResponseCategory.PossibleClassifications);
    }

    /**
     * Return the possible classifications .
     *
     * @return all details known about the asset
     */
    public Set<String> getPossibleClassifications()
    {
        return possibleClassifications;
    }

    /**
     * Set up the possible classifications .
     *
     * @param possibleClassifications - all the possible classifications
     */
    public void setPossibleClassifications(Set<String> possibleClassifications)
    {
        this.possibleClassifications=possibleClassifications;
    }

    @Override
    public String toString()
    {
        return "AssetUniverseResponse{" +
                "assetUniverse=" + possibleClassifications +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
