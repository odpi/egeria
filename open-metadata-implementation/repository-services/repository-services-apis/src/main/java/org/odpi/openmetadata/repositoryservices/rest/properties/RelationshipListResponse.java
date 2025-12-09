/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationshipListResponse holds the response information for an OMRS REST API call that returns a list of
 * relationship objects or an exception.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipListResponse extends OMRSAPIPagedResponse
{
    private static final long    serialVersionUID = 1L;

    private List<Relationship> relationships = null;


    /**
     * Default constructor
     */
    public RelationshipListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RelationshipListResponse(RelationshipListResponse template)
    {
        super(template);

        if (template != null)
        {
            relationships = template.getRelationships();
        }
    }


    /**
     * Return the list of relationships for this response.
     *
     * @return list of relationship objects
     */
    public List<Relationship> getRelationships()
    {
        if (relationships == null)
        {
            return null;
        }
        else if (relationships.isEmpty())
        {
            return null;
        }
        else
        {
            List<Relationship>  clonedRelationships = new ArrayList<>();

            for (Relationship  relationship : relationships)
            {
                clonedRelationships.add(new Relationship(relationship));
            }

            return clonedRelationships;
        }
    }


    /**
     * Set up the list of relationships for this response.
     *
     * @param relationships list of relationship objects
     */
    public void setRelationships(List<Relationship> relationships)
    {
        this.relationships = relationships;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RelationshipListResponse{" +
                "relationships=" + relationships +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof RelationshipListResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        RelationshipListResponse
                that = (RelationshipListResponse) objectToCompare;
        return Objects.equals(getRelationships(), that.getRelationships());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), getRelationships());
    }
}
