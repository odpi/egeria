/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InstanceGraph stores a subgraph of entities and relationships and provides methods to access its content.
 * It stores a list of entities and a list of relationships.  It is possible to request a list for each
 * of these two lists, or request elements that link to a specific element.  For example, request the relationships
 * that link to an entity or the entity at a specific end of a relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InstanceGraph extends InstanceElementHeader
{
    private static final long    serialVersionUID = 1L;

    public static final long  CURRENT_INSTANCE_GRAPH_HEADER_VERSION = 1;

    private List<EntityDetail> entities      = null;
    private List<Relationship> relationships = null;


    /**
     * Default constructor
     */
    public InstanceGraph()
    {
        super();
    }


    /**
     * Typical Constructor creates a graph with the supplied list of elements.  It assumes the caller has supplied
     * elements that do link together.  However, this graph supports graph fragments.
     *
     * @param entities list of entity elements to add to the list
     * @param relationships list of relationship elements to add to the list
     */
    public InstanceGraph(List<EntityDetail> entities,
                         List<Relationship> relationships)
    {
        super();

        if (entities == null)
        {
            this.entities = null;
        }
        else
        {
            this.entities = new ArrayList<>(entities);
        }

        if (relationships == null)
        {
            this.relationships = null;
        }
        else
        {
            this.relationships = new ArrayList<>(relationships);
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateGraph graph to copy; null to create an empty graph
     */
    public InstanceGraph(InstanceGraph templateGraph)
    {
        super(templateGraph);

        if (templateGraph != null)
        {
            setEntities(templateGraph.getEntities());
            setRelationships(templateGraph.getRelationships());
        }
    }


    /**
     * Return the list of all the entities (vertices/nodes) in the instance graph.  Null means empty graph.
     *
     * @return EntityDetails entity list
     */
    public List<EntityDetail> getEntities()
    {
        if (entities == null)
        {
            return null;
        }
        else if (entities.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(entities);
        }
    }


    /**
     * Set up the list of entities for this instance graph.
     *
     * @param entityElementList list of entities
     */
    public void setEntities(List<EntityDetail> entityElementList)
    {
        this.entities = entityElementList;
    }



    /**
     * Return the list of all relationships (edges/links) in the instance graph. Null means a disconnected/empty graph.
     *
     * @return Relationships relationship list
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
            return new ArrayList<>(relationships);
        }
    }


    /**
     * Set up the list of relationships in this instance graph.
     *
     * @param relationshipElementList list of relationships
     */
    public void setRelationships(List<Relationship> relationshipElementList)
    {
        this.relationships = relationshipElementList;
    }


    /**
     * Return a list of relationships that are connected to a specific entity.
     *
     * @param anchorEntityGUID unique identifier for an entity
     * @return Relationships relationship iterator
     */
    public List<Relationship> returnRelationshipsForEntity(String  anchorEntityGUID)
    {
        ArrayList<Relationship> matchingRelationships = new ArrayList<>();

        /*
         * Load copies of each relationship that matches the requested entity into matchingRelationships.
         */
        if (relationships != null)
        {
            for (Relationship  relationship : relationships)
            {
                if (relationship.relatedToEntity(anchorEntityGUID))
                {
                    matchingRelationships.add(new Relationship(relationship));
                }
            }
        }

        /*
         * Return any matched relationships in an iterator for the caller to step through.
         */
        if (matchingRelationships.isEmpty())
        {
            return null;
        }
        else
        {
            return matchingRelationships;
        }
    }


    /**
     * Return the entity connected at the far end of an entity's relationship.
     *
     * @param anchorEntityGUID unique id for the known entity.
     * @param linkingRelationshipGUID the relationship to traverse.
     * @return EntityDetail the requested entity at the far end of the relationship.
     * Null if the relationship or entity is not found.
     */
    public EntityDetail returnLinkedEntity(String  anchorEntityGUID, String linkingRelationshipGUID)
    {
        Relationship    matchingRelationship = null;
        String          linkedEntityGUID;
        EntityDetail    linkedEntity = null;

        /*
         * Step through the list of relationships looking for the matching one.  If parameters are null we will not
         * match with the list.
         */
        if (relationships != null)
        {
            for (Relationship  relationship : relationships)
            {
                if (relationship.getGUID().equals(linkingRelationshipGUID))
                {
                    matchingRelationship = relationship;
                    break;
                }
            }
        }

        /*
         * Return null if the relationship is not known
         */
        if (matchingRelationship == null)
        {
            return null;
        }

        /*
         * Extract the guid of the linking entity.
         */
        linkedEntityGUID = matchingRelationship.returnLinkedEntity(anchorEntityGUID);

        /*
         * Return null if the entity does not match.
         */
        if (linkedEntityGUID == null)
        {
            return null;
        }

        /*
         * Step through the list of entities in the graph looking for the appropriate entity to return.
         * If no match occurs, null will be returned.
         */
        for (EntityDetail  entity : entities)
        {
            if (entity.getGUID().equals(linkedEntityGUID))
            {
                linkedEntity = new EntityDetail(entity);
                break;
            }
        }

        return linkedEntity;
    }


    /**
     * Return the number of entities in the graph.
     *
     * @return elementCount for entities
     */
    public int returnEntityElementCount()
    {
        return entities.size();
    }


    /**
     * Return the number of relationships in the graph.
     *
     * @return elementCount for relationships
     */
    public int returnRelationshipElementCount()
    {
        return relationships.size();
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "InstanceGraph{" +
                "entities=" + entities +
                ", relationships=" + relationships +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof InstanceGraph))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }

        InstanceGraph that = (InstanceGraph) objectToCompare;

        if (entities != null ? ! entities.equals(that.entities) : that.entities != null)
        {
            return false;
        }
        return relationships != null ? relationships.equals(that.relationships) : that.relationships == null;
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (entities != null ? entities.hashCode() : 0);
        result = 31 * result + (relationships != null ? relationships.hashCode() : 0);
        return result;
    }
}
