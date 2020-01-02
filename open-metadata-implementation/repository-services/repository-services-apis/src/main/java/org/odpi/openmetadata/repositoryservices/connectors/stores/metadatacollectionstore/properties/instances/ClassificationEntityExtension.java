/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;


import java.util.Objects;

/**
 * ClassificationEntityExtension is used to represent a single classification for an entity.
 * It includes the details of the classification and an entity proxy to define which entity
 * it should be attached to.  This version of classification is used in open metadata archives
 * and events where the complete entity is either not available, or needs to be augmented.
 */
public class ClassificationEntityExtension extends InstanceElementHeader
{
    private static final long    serialVersionUID = 1L;

    private Classification   classification = null;
    private EntityProxy      entityToClassify = null;


    /**
     * Default constructor
     */
    public ClassificationEntityExtension()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ClassificationEntityExtension(ClassificationEntityExtension  template)
    {
        super(template);

        if (template != null)
        {
            classification = template.getClassification();
            entityToClassify = template.getEntityToClassify();
        }
    }


    /**
     * Return the classification for the entity.
     *
     * @return classification object
     */
    public Classification getClassification()
    {
        return classification;
    }


    /**
     * Set up the classification for the entity.
     *
     * @param classification classification object
     */
    public void setClassification(Classification classification)
    {
        this.classification = classification;
    }


    /**
     * Return which entity to attach this classification to.
     *
     * @return entity proxy object
     */
    public EntityProxy getEntityToClassify()
    {
        return entityToClassify;
    }


    /**
     * Set up which entity to attach this classification to.
     *
     * @param entityToClassify entity proxy object
     */
    public void setEntityToClassify(EntityProxy entityToClassify)
    {
        this.entityToClassify = entityToClassify;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "ClassificationEntityExtension{" +
                "classification=" + classification +
                ", entityToClassify=" + entityToClassify +
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
        ClassificationEntityExtension that = (ClassificationEntityExtension) objectToCompare;
        return Objects.equals(getClassification(), that.getClassification()) &&
                Objects.equals(getEntityToClassify(), that.getEntityToClassify());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getClassification(), getEntityToClassify());
    }
}
