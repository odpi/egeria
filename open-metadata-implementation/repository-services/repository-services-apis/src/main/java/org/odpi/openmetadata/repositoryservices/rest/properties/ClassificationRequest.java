/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ClassificationRequest carries the properties needed to create a classification.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ClassificationRequest extends OMRSAPIRequest
{
    private static final long    serialVersionUID = 1L;

    private ClassificationOrigin classificationOrigin     = null;
    private String               classificationOriginGUID = null;
    private InstanceProperties   classificationProperties = null;
    private String               metadataCollectionId     = null;
    private String               metadataCollectionName   = null;

    /**
     * Default constructor
     */
    public ClassificationRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ClassificationRequest(ClassificationRequest template)
    {
        super(template);

        if (template != null)
        {
            this.classificationOrigin = template.getClassificationOrigin();
            this.classificationOriginGUID = template.getClassificationOriginGUID();
            this.classificationProperties = template.getClassificationProperties();
            this.metadataCollectionId = template.getMetadataCollectionId();
            this.metadataCollectionName = template.getMetadataCollectionName();
        }
    }


    /**
     * Return the origin of the classification.
     *
     * @return ClassificationOrigin enum
     */
    public ClassificationOrigin getClassificationOrigin()
    {
        return classificationOrigin;
    }


    /**
     * Set up the origin of the classification.
     *
     * @param classificationOrigin ClassificationOrigin enum
     */
    public void setClassificationOrigin(ClassificationOrigin classificationOrigin)
    {
        this.classificationOrigin = classificationOrigin;
    }


    /**
     * Return the guid of the entity where a propagate classification came from.
     *
     * @return unique identifier of the classification's origin
     */
    public String getClassificationOriginGUID()
    {
        return classificationOriginGUID;
    }


    /**
     * Set up the guid of the entity where a propagate classification came from.
     *
     * @param classificationOriginGUID unique identifier of the classification's origin
     */
    public void setClassificationOriginGUID(String classificationOriginGUID)
    {
        this.classificationOriginGUID = classificationOriginGUID;
    }

    /**
     * Return the list of properties for the new entity.
     *
     * @return instance properties object
     */
    public InstanceProperties getClassificationProperties()
    {
        if (classificationProperties == null)
        {
            return null;
        }
        else
        {
            return new InstanceProperties(classificationProperties);
        }
    }


    /**
     * Set up the initial properties for the entity.
     *
     * @param classificationProperties InstanceProperties object
     */
    public void setClassificationProperties(InstanceProperties classificationProperties)
    {
        this.classificationProperties = classificationProperties;
    }


    /**
     * Return the metadata collection id for this new entity
     *
     * @return guid
     */
    public String getMetadataCollectionId()
    {
        return metadataCollectionId;
    }


    /**
     * Set up the metadata collection id for this new entity.
     * This field is optional for addEntity and mandatory for addExternalEntity.
     *
     * @param metadataCollectionId guid
     */
    public void setMetadataCollectionId(String metadataCollectionId)
    {
        this.metadataCollectionId = metadataCollectionId;
    }


    /**
     * Return the name of the metadata collection for this new relationship.
     *
     * @return name
     */
    public String getMetadataCollectionName()
    {
        return metadataCollectionName;
    }


    /**
     * Set up the name of the metadata collection for this new relationship.
     *
     * @param metadataCollectionName name
     */
    public void setMetadataCollectionName(String metadataCollectionName)
    {
        this.metadataCollectionName = metadataCollectionName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "InstancePropertiesRequest{" +
                "instanceProperties=" + classificationProperties +
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
        if (!(objectToCompare instanceof ClassificationRequest))
        {
            return false;
        }
        ClassificationRequest
                that = (ClassificationRequest) objectToCompare;
        return Objects.equals(getClassificationProperties(), that.getClassificationProperties());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getClassificationProperties());
    }
}
