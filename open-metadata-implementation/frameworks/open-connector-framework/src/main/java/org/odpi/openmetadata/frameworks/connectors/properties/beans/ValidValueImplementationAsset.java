/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ValidValueImplementationAsset contains the properties for a reference data set implementing a requested valid value.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueImplementationAsset extends ValidValueImplementation
{
    private Asset               referenceDataAsset      = null;


    /**
     * Default constructor
     */
    public ValidValueImplementationAsset()
    {
        super();
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public ValidValueImplementationAsset(ValidValueImplementationAsset template)
    {
        super(template);

        if (template != null)
        {
            referenceDataAsset      = template.getReferenceDataAsset();
        }
    }


    /**
     * Returns the asset where the implementation of the valid value set is stored.
     *
     * @return properties of the asset
     */
    public Asset getReferenceDataAsset()
    {
        return referenceDataAsset;
    }


    /**
     * Set up the asset where the implementation of the valid value set is stored.
     *
     * @param referenceDataAsset properties of the asset
     */
    public void setReferenceDataAsset(Asset referenceDataAsset)
    {
        this.referenceDataAsset = referenceDataAsset;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ValidValueImplElement{" +
                ", referenceDataAsset=" + referenceDataAsset +
                ", symbolicName='" + getSymbolicName() + '\'' +
                ", implementationValue='" + getImplementationValue() + '\'' +
                ", additionalValues=" + getAdditionalValues() +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        ValidValueImplementationAsset that = (ValidValueImplementationAsset) objectToCompare;
        return Objects.equals(referenceDataAsset, that.referenceDataAsset);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), referenceDataAsset);
    }
}
