/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * RelationshipResponse describes the response to an OMRS REST API request that returns a relationship object.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipResponse extends OMRSRESTAPIResponse
{
    private Relationship relationship = null;


    /**
     * Default constructor
     */
    public RelationshipResponse()
    {
    }


    /**
     * Return the resulting relationship.
     *
     * @return relationship object
     */
    public Relationship getRelationship()
    {
        return relationship;
    }


    /**
     * Set up the returned relationship.
     *
     * @param relationship - relationship object
     */
    public void setRelationship(Relationship relationship)
    {
        this.relationship = relationship;
    }


    @Override
    public String toString()
    {
        return "RelationshipResponse{" +
                "relationship=" + relationship +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
