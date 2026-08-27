/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.EndMatchCriteria;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationshipFindRequest restricts a find request to relationships linked with specific entities.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationshipFindRequest extends InstanceFindRequest
{
    private List<String>     end1EntityGUIDs = null;
    private String           end1EntityTypeGUID = null;
    private List<String>     end2EntityGUIDs  = null;
    private String           end2EntityTypeGUID = null;
    private EndMatchCriteria endMatchCriteria = null;


    /**
     * Default constructor
     */
    public RelationshipFindRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public RelationshipFindRequest(RelationshipFindRequest template)
    {
        super(template);

        if (template != null)
        {
            this.end1EntityGUIDs = template.getEnd1EntityGUIDs();
            this.end1EntityTypeGUID = template.getEnd1EntityTypeGUID();
            this.end2EntityGUIDs = template.getEnd2EntityGUIDs();
            this.end2EntityTypeGUID = template.getEnd2EntityTypeGUID();
            this.endMatchCriteria = template.getEndMatchCriteria();
        }
    }


    /**
     * Return the list of entity guids used to match end 1 of the relationships.
     *
     * @return list of guids
     */
    public List<String> getEnd1EntityGUIDs()
    {
        return end1EntityGUIDs;
    }


    /**
     * Set up the list of entity guids used to match end 1 of the relationships.
     *
     * @param end1EntityGUIDs list of guids
     */
    public void setEnd1EntityGUIDs(List<String> end1EntityGUIDs)
    {
        this.end1EntityGUIDs = end1EntityGUIDs;
    }


    /**
     * Return the unique identifier of the type that the entity at end 1 of the relationship must belong to.
     * This is an optional request - a null means that any type of entity is acceptable at end 1.  It may be
     * used on its own, without any end 1 entity guids, to match all relationships that have an entity of
     * this type (or one of its subtypes) at end 1.
     *
     * @return type guid
     */
    public String getEnd1EntityTypeGUID()
    {
        return end1EntityTypeGUID;
    }


    /**
     * Set up the unique identifier of the type that the entity at end 1 of the relationship must belong to.
     *
     * @param end1EntityTypeGUID type guid
     */
    public void setEnd1EntityTypeGUID(String end1EntityTypeGUID)
    {
        this.end1EntityTypeGUID = end1EntityTypeGUID;
    }


    /**
     * Return the list of entity guids used to match end 2 of the relationships.
     *
     * @return list of guids
     */
    public List<String> getEnd2EntityGUIDs()
    {
        return end2EntityGUIDs;
    }


    /**
     * Set up the list of entity guids used to match end 2 of the relationships.
     *
     * @param end2EntityGUIDs list of guids
     */
    public void setEnd2EntityGUIDs(List<String> end2EntityGUIDs)
    {
        this.end2EntityGUIDs = end2EntityGUIDs;
    }


    /**
     * Return the unique identifier of the type that the entity at end 2 of the relationship must belong to.
     * This is an optional request - a null means that any type of entity is acceptable at end 2.  It may be
     * used on its own, without any end 2 entity guids, to match all relationships that have an entity of
     * this type (or one of its subtypes) at end 2.
     *
     * @return type guid
     */
    public String getEnd2EntityTypeGUID()
    {
        return end2EntityTypeGUID;
    }


    /**
     * Set up the unique identifier of the type that the entity at end 2 of the relationship must belong to.
     *
     * @param end2EntityTypeGUID type guid
     */
    public void setEnd2EntityTypeGUID(String end2EntityTypeGUID)
    {
        this.end2EntityTypeGUID = end2EntityTypeGUID;
    }


    /**
     * Return the end matching search criteria.
     *
     * @return SearchClassifications
     */
    public EndMatchCriteria getEndMatchCriteria()
    {
        return endMatchCriteria;
    }


    /**
     * Set the end matching search criteria.
     *
     * @param endMatchCriteria to set as search criteria
     */
    public void setEndMatchCriteria(EndMatchCriteria endMatchCriteria)
    {
        this.endMatchCriteria = endMatchCriteria;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RelationshipFindRequest{" +
                "end1EntityGUIDs=" + end1EntityGUIDs +
                ", end1EntityTypeGUID='" + end1EntityTypeGUID + '\'' +
                ", end2EntityGUIDs=" + end2EntityGUIDs +
                ", end2EntityTypeGUID='" + end2EntityTypeGUID + '\'' +
                ", endMatchCriteria=" + endMatchCriteria +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        RelationshipFindRequest that = (RelationshipFindRequest) objectToCompare;
        return Objects.equals(end1EntityGUIDs, that.end1EntityGUIDs) &&
                Objects.equals(end1EntityTypeGUID, that.end1EntityTypeGUID) &&
                Objects.equals(end2EntityGUIDs, that.end2EntityGUIDs) &&
                Objects.equals(end2EntityTypeGUID, that.end2EntityTypeGUID) &&
                endMatchCriteria == that.endMatchCriteria;
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), end1EntityGUIDs, end1EntityTypeGUID, end2EntityGUIDs, end2EntityTypeGUID, endMatchCriteria);
    }

}
