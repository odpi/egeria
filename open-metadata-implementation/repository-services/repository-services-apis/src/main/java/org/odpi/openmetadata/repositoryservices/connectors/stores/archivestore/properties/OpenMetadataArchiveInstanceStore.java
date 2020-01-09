/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationEntityExtension;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataArchiveInstanceStore defines the contents of the InstanceStore in an open metadata archive.  It
 * consists of a list of entities, a list of relationships and a list of classifications.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataArchiveInstanceStore extends OpenMetadataArchiveElementHeader
{
    private static final long    serialVersionUID = 1L;

    private List<EntityDetail>                  entities        = null;
    private List<Relationship>                  relationships   = null;
    private List<ClassificationEntityExtension> classifications = null;


    /**
     * Default constructor relying on the initialization of variables in their declaration.
     */
    public OpenMetadataArchiveInstanceStore()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OpenMetadataArchiveInstanceStore(OpenMetadataArchiveInstanceStore   template)
    {
        super(template);

        if (template != null)
        {
            entities = template.getEntities();
            relationships = template.getRelationships();
            classifications = template.getClassifications();
        }
    }


    /**
     * Return the list of entities defined in the open metadata archive.
     *
     * @return list of entities
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
            List<EntityDetail>  clonedList = new ArrayList<>();

            for (EntityDetail  existingElement : entities)
            {
                clonedList.add(new EntityDetail(existingElement));
            }

            return clonedList;
        }
    }


    /**
     * Set up the list of entities defined in the open metadata archive.
     *
     * @param entities  list of entities
     */
    public void setEntities(List<EntityDetail> entities)
    {
        this.entities = entities;
    }


    /**
     * Return the list of relationships defined in this open metadata archive.
     *
     * @return list of relationships
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
            List<Relationship>  clonedList = new ArrayList<>();

            for (Relationship  existingElement : relationships)
            {
                clonedList.add(new Relationship(existingElement));
            }

            return clonedList;
        }
    }


    /**
     * Set up the list of relationships defined in this open metadata archive.
     *
     * @param relationships  list of relationship objects
     */
    public void setRelationships(List<Relationship> relationships)
    {
        this.relationships = relationships;
    }


    /**
     * Return the list of classifications defined in this open metadata archive.
     *
     * @return list of classification objects
     */
    public List<ClassificationEntityExtension> getClassifications()
    {
        if (classifications == null)
        {
            return null;
        }
        else if (classifications.isEmpty())
        {
            return null;
        }
        else
        {
            List<ClassificationEntityExtension> clonedList = new ArrayList<>();

            for (ClassificationEntityExtension  existingElement : classifications)
            {
                clonedList.add(new ClassificationEntityExtension(existingElement));
            }

            return clonedList;
        }
    }


    /**
     * Set up the list of classification objects defined in the open metadata archive.
     *
     * @param classifications list of classification objects
     */
    public void setClassifications(List<ClassificationEntityExtension> classifications)
    {
        this.classifications = classifications;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataArchiveInstanceStore{" +
                "entities=" + entities +
                ", relationships=" + relationships +
                ", classifications=" + classifications +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        OpenMetadataArchiveInstanceStore that = (OpenMetadataArchiveInstanceStore) objectToCompare;
        return Objects.equals(getEntities(), that.getEntities()) &&
                Objects.equals(getRelationships(), that.getRelationships()) &&
                Objects.equals(getClassifications(), that.getClassifications());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getEntities(), getRelationships(), getClassifications());
    }
}
