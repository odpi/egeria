/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;

import java.util.Map;
import java.util.Objects;

/**
 * The Classification class stores information about a classification assigned to an asset.  The Classification
 * has a name and some properties.  It also stores the typename of the asset it is connected to for debug purposes.
 *
 * Note: it is not valid to have a classification with a null or blank name.
 */
public class AssetClassification extends AssetPropertyBase
{
    private static final long     serialVersionUID = 1L;

    protected ElementClassification classificationBean;

    /**
     * A private validation method used by the constructors.
     *
     * @param name name to check
     * @return validated name
     */
    private String validateName(String   name)
    {
        /*
         * Throw an exception if the classification's name is null because that does not make sense.
         * The constructors do not catch this exception so it is received by the creator of the classification
         * object.
         */
        if (name == null || name.equals(""))
        {
            /*
             * Build and throw exception.  This should not happen likely to be a problem in the
             * repository connector.
             */
            String parentName = super.getParentAssetName();
            String parentTypeName = super.getParentAssetTypeName();

            if (parentName == null)
            {
                parentName = "<null>";
            }

            if (parentTypeName == null)
            {
                parentTypeName = "<null>";
            }

            throw new OCFRuntimeException(OCFErrorCode.NULL_CLASSIFICATION_NAME.getMessageDefinition(parentName,
                                                                                                     parentTypeName),
                                          this.getClass().getName(),
                                          "validateName");
        }
        else
        {
            return name;
        }
    }


    /**
     * Bean constructor
     *
     * @param classification bean containing the properties
     */
    protected AssetClassification(ElementClassification classification)
    {
        super(null);

        if (classification == null)
        {
            classificationBean = new ElementClassification();
        }
        else
        {
            classificationBean = classification;
        }

        validateName(classificationBean.getClassificationName());
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset details of the asset that this classification is linked to.
     * @param classification bean containing the properties
     */
    protected AssetClassification(AssetDescriptor parentAsset,
                                  ElementClassification classification)
    {
        super(parentAsset);

        if (classification == null)
        {
            classificationBean = new ElementClassification();
        }
        else
        {
            classificationBean = classification;
        }

        validateName(classificationBean.getClassificationName());
    }


    /**
     * Copy/clone Constructor sets up new classification using values from the template
     *
     * @param parentAsset details of the asset that this classification is linked to.
     * @param templateClassification object to copy
     */
    public AssetClassification(AssetDescriptor parentAsset, AssetClassification templateClassification)
    {
        super(parentAsset, templateClassification);

        /*
         * An empty classification object is passed in the variable declaration so throw exception
         * because we need the classification name.
         */
        if (templateClassification == null)
        {
            /*
             * Build and throw exception.  This should not happen likely to be a problem in the
             * repository connector.
             */
            throw new OCFRuntimeException(OCFErrorCode.NULL_CLASSIFICATION_NAME.getMessageDefinition("<Unknown>", "<Unknown>"),
                                          this.getClass().getName(),
                                          "Copy Constructor");
        }
        else
        {
            this.classificationBean = templateClassification.getClassificationBean();

            validateName(this.classificationBean.getClassificationName());
        }
    }


    /**
     * Return the classification bean.  This is used for cloning an AssetClassification.
     *
     * @return classification bean
     */
    protected ElementClassification getClassificationBean()
    {
        return classificationBean;
    }


    /**
     * Return the name of the classification
     *
     * @return name of classification
     */
    public String getName()
    {
        return classificationBean.getClassificationName();
    }


    /**
     * Returns a collection of the additional stored properties for the classification.
     * If no stored properties are present then null is returned.
     *
     * @return properties for the classification
     */
    public Map<String, Object> getProperties()
    {
        return classificationBean.getClassificationProperties();
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return classificationBean.toString();
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
        if (!(objectToCompare instanceof AssetClassification))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetClassification that = (AssetClassification) objectToCompare;
        return Objects.equals(getClassificationBean(), that.getClassificationBean());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return classificationBean.hashCode();
    }
}