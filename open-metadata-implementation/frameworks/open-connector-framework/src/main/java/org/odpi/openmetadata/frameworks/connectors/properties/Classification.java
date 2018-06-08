/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;

/**
 * The Classification class stores information about a classification assigned to an asset.  The Classification
 * has a name and some properties.  It also stores the typename of the asset it is connected to for debug purposes.
 *
 * Note: it is not valid to have a classification with a null or blank name.
 */
public class Classification extends AssetPropertyBase
{
    private String               classificationName       = null;
    private AdditionalProperties classificationProperties = null;

    /**
     * A private validation method used by the constructors.
     *
     * @param name - name to check
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
             * Build and throw exception.  This should not happen - likely to be a problem in the
             * repository connector.
             */
            OCFErrorCode errorCode = OCFErrorCode.NULL_CLASSIFICATION_NAME;
            String       errorMessage = errorCode.getErrorMessageId()
                                      + errorCode.getFormattedErrorMessage(super.getParentAssetName(),
                                                                           super.getParentAssetTypeName());

            throw new OCFRuntimeException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          "validateName",
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());
        }
        else
        {
            return name;
        }
    }


    /**
     * Typical constructor - verifies and saves parameters.
     *
     * @param parentAsset - name and type of related asset
     * @param name - name of the classification
     * @param properties - additional properties for the classification
     */
    public Classification(AssetDescriptor      parentAsset,
                          String               name,
                          AdditionalProperties properties)
    {
        super(parentAsset);

        this.classificationName = validateName(name);
        this.classificationProperties = properties;
    }


    /**
     * Copy/clone Constructor - sets up new classification using values from the template
     *
     * @param parentAsset - details of the asset that this classification is linked to.
     * @param templateClassification - object to copy
     */
    public Classification(AssetDescriptor parentAsset, Classification templateClassification)
    {
        super(parentAsset, templateClassification);

        /*
         * An empty classification object is passed in the variable declaration so throw exception
         * because we need the classification name.
         */
        if (templateClassification == null)
        {
            /*
             * Build and throw exception.  This should not happen - likely to be a problem in the
             * repository connector.
             */
            OCFErrorCode errorCode = OCFErrorCode.NULL_CLASSIFICATION_NAME;
            String       errorMessage = errorCode.getErrorMessageId()
                                      + errorCode.getFormattedErrorMessage("<Unknown>");

            throw new OCFRuntimeException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          "Copy Constructor",
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());
        }
        else
        {
            /*
             * Save the name and properties.
             */
            this.classificationName = validateName(templateClassification.getName());
            this.classificationProperties = templateClassification.getProperties();
        }
    }


    /**
     * Return the name of the classification
     *
     * @return name of classification
     */
    public String getName()
    {
        return classificationName;
    }


    /**
     * Returns a collection of the additional stored properties for the classification.
     * If no stored properties are present then null is returned.
     *
     * @return properties for the classification
     */
    public AdditionalProperties getProperties()
    {
        if (classificationProperties == null)
        {
            return classificationProperties;
        }
        else
        {
            return new AdditionalProperties(super.getParentAsset(), classificationProperties);
        }
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Classification{" +
                "classificationName='" + classificationName + '\'' +
                ", classificationProperties=" + classificationProperties +
                '}';
    }
}