/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSRuntimeException;

import java.io.Serial;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The Classification class stores information about a classification assigned to an entity.  The Classification
 * has a name and some properties.   Some classifications are explicitly added to an entity and other
 * classifications are propagated to an entity along the relationships connected to it.  The origin of the
 * classification is also stored.
 *
 * Note: it is not valid to have a classification with a null or blank name.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Classification extends InstanceAuditHeader
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String               classificationName       = null;
    private InstanceProperties   classificationProperties = null;
    private ClassificationOrigin classificationOrigin     = ClassificationOrigin.ASSIGNED;
    private String               classificationOriginGUID = null;

    /*
     * A private validation method used by the constructors
     */
    private String validateName(String   name)
    {
        /*
         * Throw an exception if the classification's name is null because that does not make sense.
         * The constructors do not catch this exception, so it is received by the creator of the classification
         * object.
         */
        if (name == null || name.equals(""))
        {
            final String methodName = "validateName";

            /*
             * Build and throw exception.  This should not happen, and it is likely to be a problem in the
             * repository connector.
             */
            throw new OMRSRuntimeException(OMRSErrorCode.NULL_CLASSIFICATION_PROPERTY_NAME.getMessageDefinition(),
                                           this.getClass().getName(),
                                           methodName);
        }
        else
        {
            return name;
        }
    }


    /**
     * Default constructor for automated generation tools.
     */
    public Classification()
    {
        super();
    }


    /**
     * Copy/clone constructor sets up new classification using values from the template
     *
     * @param template object to copy
     */
    public Classification(Classification template)
    {
        super(template);

        /*
         * An empty classification object is passed in the variable declaration so throw exception
         * because we need the classification name.
         */
        if (template == null)
        {
            final String methodName = "Copy Constructor";

            /*
             * Build and throw exception.  This should not happen and this is likely to be a problem in the
             * repository connector.
             */
            throw new OMRSRuntimeException(OMRSErrorCode.NULL_CLASSIFICATION_PROPERTY_NAME.getMessageDefinition("<Unknown>"),
                                           this.getClass().getName(),
                                           methodName);
        }
        else
        {
            /*
             * Extract and save the values from the template.
             */
            this.classificationName = validateName(template.getName());
            this.classificationProperties = template.getProperties();
            this.classificationOrigin = template.getClassificationOrigin();
            this.classificationOriginGUID = template.getClassificationOriginGUID();
        }
    }


    /**
     * Return the name of the classification. This name is the type name defined in a ClassificationDef type definition.
     *
     * @return name of classification
     */
    public String getName()
    {
        return classificationName;
    }


    /**
     * Set up the name of the classification. This name is the type name defined in a ClassificationDef type definition.
     *
     * @param classificationName String name
     */
    public void setName(String classificationName)
    {
        this.classificationName = validateName(classificationName);
    }


    /**
     * Returns a collection of the additional stored properties for the classification.
     * If no stored properties are present then null is returned.
     *
     * @return properties for the classification
     */
    public InstanceProperties getProperties()
    {
        if (classificationProperties == null)
        {
            return null;
        }
        else if ((classificationProperties.getInstanceProperties() == null) &&
                 (classificationProperties.getEffectiveFromTime() == null) &&
                 (classificationProperties.getEffectiveToTime() == null))
        {
            return null;
        }
        else
        {
            return new InstanceProperties(classificationProperties);
        }
    }


    /**
     * Set up a collection of the additional stored properties for the classification.
     *
     * @param classificationProperties properties object
     */
    public void setProperties(InstanceProperties classificationProperties)
    {
        this.classificationProperties = classificationProperties;
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
     * Return the guid of the entity where the propagated classification came from.
     *
     * @return unique identifier of the classification's origin
     */
    public String getClassificationOriginGUID()
    {
        return classificationOriginGUID;
    }


    /**
     * Set up the guid of the entity where the propagated classification came from.
     *
     * @param classificationOriginGUID unique identifier of the classification's origin
     */
    public void setClassificationOriginGUID(String classificationOriginGUID)
    {
        this.classificationOriginGUID = classificationOriginGUID;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString() {
        return "Classification{" +
                "classificationName='" + classificationName + '\'' +
                ", classificationProperties=" + classificationProperties +
                ", classificationOrigin=" + classificationOrigin +
                ", classificationOriginGUID='" + classificationOriginGUID + '\'' +
                ", name='" + getName() + '\'' +
                ", properties=" + getProperties() +
                ", headerVersion=" + getHeaderVersion() +
                ", type=" + getType() +
                ", instanceProvenanceType=" + getInstanceProvenanceType() +
                ", metadataCollectionId='" + getMetadataCollectionId() + '\'' +
                ", metadataCollectionName='" + getMetadataCollectionName() + '\'' +
                ", replicatedBy='" + getReplicatedBy() + '\'' +
                ", instanceLicense='" + getInstanceLicense() + '\'' +
                ", status=" + getStatus() +
                ", createdBy='" + getCreatedBy() + '\'' +
                ", updatedBy='" + getUpdatedBy() + '\'' +
                ", maintainedBy=" + getMaintainedBy() +
                ", createTime=" + getCreateTime() +
                ", updateTime=" + getUpdateTime() +
                ", version=" + getVersion() +
                ", statusOnDelete=" + getStatusOnDelete() +
                ", mappingProperties=" + getMappingProperties() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        Classification that = (Classification) objectToCompare;
        return Objects.equals(classificationName, that.classificationName) &&
                Objects.equals(classificationProperties, that.classificationProperties) &&
                getClassificationOrigin() == that.getClassificationOrigin() &&
                Objects.equals(getClassificationOriginGUID(), that.getClassificationOriginGUID());
    }



    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), classificationName, classificationProperties, getClassificationOrigin(),
                            getClassificationOriginGUID());
    }
}
