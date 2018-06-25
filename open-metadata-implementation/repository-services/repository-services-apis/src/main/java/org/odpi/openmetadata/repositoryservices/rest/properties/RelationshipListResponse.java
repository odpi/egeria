/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationshipListResponse holds the response information for an OMRS REST API call that returns a list of
 * relationship objects or an exception.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipListResponse extends OMRSRESTAPIPagedResponse
{
    private List<Relationship> relationships = null;


    /**
     * Default constructor
     */
    public RelationshipListResponse()
    {
    }


    /**
     * Return the list of relationships for this response.
     *
     * @return list of relationship objects
     */
    public List<Relationship> getRelationships()
    {
        return relationships;
    }


    /**
     * Set up the list of relationships for this response.
     *
     * @param relationships list of relationship objects
     */
    public void setRelationships(List<Relationship> relationships)
    {
        this.relationships = new ArrayList<>(relationships);
    }


    @Override
    public String toString()
    {
        return "RelationshipListResponse{" +
                "relationships=" + relationships +
                ", nextPageURL='" + nextPageURL + '\'' +
                ", offset=" + offset +
                ", pageSize=" + pageSize +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
