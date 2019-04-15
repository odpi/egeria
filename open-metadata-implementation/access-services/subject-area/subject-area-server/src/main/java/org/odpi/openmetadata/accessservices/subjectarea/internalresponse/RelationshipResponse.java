/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.internalresponse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * RelationshipResponse is a response structure used internally by the Subject Area OMAS REST API
 * to represent an omrs relationship
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private Relationship relationship = null;

    /**
     * Default constructor
     */
    public RelationshipResponse() {
        this.setResponseCategory(ResponseCategory.OmrsRelationship);
    }
    public RelationshipResponse(Relationship relationship)
    {
        this();
        this.relationship = relationship;
    }


    /**
     * Return the Relationship object.
     *
     * @return relationship
     */
    public Relationship getRelationship()
    {
        return relationship;
    }

    /**
     * Set up the Relationship object.
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
                '}';
    }
}
