/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * RelationshipResponse describes the response to an OMRS REST API request that returns a relationship object.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipResponse extends OMRSAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private Relationship relationship = null;


    /**
     * Default constructor
     */
    public RelationshipResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RelationshipResponse(RelationshipResponse template)
    {
        super(template);

        if (template != null)
        {
            relationship = template.getRelationship();
        }
    }


    /**
     * Return the resulting relationship.
     *
     * @return relationship object
     */
    public Relationship getRelationship()
    {
        if (relationship == null)
        {
            return null;
        }
        else {
            return new Relationship(relationship);
        }
    }


    /**
     * Set up the returned relationship.
     *
     * @param relationship relationship object
     */
    public void setRelationship(Relationship relationship)
    {
        this.relationship = relationship;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RelationshipResponse{" +
                "relationship=" + relationship +
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
        if (!(objectToCompare instanceof RelationshipResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        RelationshipResponse
                that = (RelationshipResponse) objectToCompare;
        return Objects.equals(getRelationship(), that.getRelationship());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getRelationship());
    }
}
