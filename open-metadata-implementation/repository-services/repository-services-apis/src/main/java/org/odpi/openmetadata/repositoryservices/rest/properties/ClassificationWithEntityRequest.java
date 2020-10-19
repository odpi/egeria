/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ClassificationWithEntityRequest carries a classification that has changed along with its entity.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ClassificationWithEntityRequest extends OMRSAPIRequest
{
    private static final long    serialVersionUID = 1L;

    private Classification classification = null;
    private EntityDetail   entity         = null;


    /**
     * Default constructor
     */
    public ClassificationWithEntityRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ClassificationWithEntityRequest(ClassificationWithEntityRequest template)
    {
        super(template);

        if (template != null)
        {
            this.classification           = template.getClassification();
            this.entity                   = template.getEntity();
        }
    }


    /**
     * Return the classification.
     *
     * @return Classification object
     */
    public Classification getClassification()
    {
        return classification;
    }


    /**
     * Set up the classification.
     *
     * @param classification Classification object
     */
    public void setClassification(Classification classification)
    {
        this.classification = classification;
    }


    /**
     * Return the entity where the classification belongs.
     *
     * @return full entity
     */
    public EntityDetail getEntity()
    {
        return entity;
    }


    /**
     * Set up the entity where the classification belongs.
     *
     * @param entity full entity
     */
    public void setEntity(EntityDetail entity)
    {
        this.entity = entity;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ClassificationWithEntityRequest{" +
                "classification=" + classification +
                ", entity=" + entity +
                '}';
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        ClassificationWithEntityRequest that = (ClassificationWithEntityRequest) objectToCompare;
        return Objects.equals(classification, that.classification) &&
                Objects.equals(entity, that.entity);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getClassification(), getEntity());
    }
}
