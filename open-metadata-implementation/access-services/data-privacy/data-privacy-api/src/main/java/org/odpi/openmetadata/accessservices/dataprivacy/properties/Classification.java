/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataprivacy.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Classification bean defines a single classification for an asset.  This can be used for REST calls and other
 * JSON based functions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Classification extends DataPrivacyElementHeader
{
    private static final long    serialVersionUID = 1L;

    private String              classificationName       = null;
    private Map<String, Object> classificationProperties = null;

    /**
     * Default constructor
     */
    public Classification()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param templateClassification template to copy
     */
    public Classification(Classification templateClassification)
    {
        super(templateClassification);

        if (templateClassification != null)
        {
            classificationName = templateClassification.getClassificationName();
            classificationProperties = templateClassification.getClassificationProperties();
        }
    }


    /**
     * Set up the name of the classification
     *
     * @param classificationName  name of classification
     */
    public void setClassificationName(String classificationName)
    {
        this.classificationName = classificationName;
    }


    /**
     * Return the name of the classification
     *
     * @return String name
     */
    public String getClassificationName()
    {
        return classificationName;
    }


    /**
     * Set up a collection of the additional stored properties for the classification.
     * If no stored properties are present then null is returned.
     *
     * @param classificationProperties  properties for the classification
     */
    public void setClassificationProperties(Map<String, Object> classificationProperties)
    {
        this.classificationProperties = classificationProperties;
    }


    /**
     * Return a collection of the additional stored properties for the classification.
     * If no stored properties are present then null is returned.
     *
     * @return properties map
     */
    public Map<String, Object> getClassificationProperties()
    {
        if (classificationProperties == null)
        {
            return null;
        }
        else
        {
            return new HashMap<>(classificationProperties);
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
        if (!(objectToCompare instanceof Classification))
        {
            return false;
        }
        Classification that = (Classification) objectToCompare;
        return Objects.equals(getClassificationName(), that.getClassificationName()) &&
                Objects.equals(getClassificationProperties(), that.getClassificationProperties());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getClassificationName());
    }
}
