/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.internalresponse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * LinesResponse is a response structure used internally by the Subject Area OMAS REST API
 * to represent an omrs relationships
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipsResponse extends SubjectAreaOMASAPIResponse
{
    private List<Relationship> relationships = null;

    /**
     * Default constructor
     */
    public RelationshipsResponse() {
        this.setResponseCategory(ResponseCategory.OmrsRelationships);
    }
    public RelationshipsResponse(List<Relationship> relationships)
    {
        this();
        this.relationships = relationships;
    }


    /**
     * Return the Lines object.
     *
     * @return relationships
     */
    public List<Relationship> getRelationships()
    {
        if (relationships == null) {
            return new ArrayList<>();
        } else {
            return relationships;
        }
    }

    /**
     * Set up the Relationship object.
     *
     * @param relationships - relationships object
     */
    public void setRelationships(List<Relationship> relationships)
    {
        this.relationships = relationships;
    }


    @Override
    public String toString()
    {
        return "RelationshipResponse{" +
                "relationships=" + relationships +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
