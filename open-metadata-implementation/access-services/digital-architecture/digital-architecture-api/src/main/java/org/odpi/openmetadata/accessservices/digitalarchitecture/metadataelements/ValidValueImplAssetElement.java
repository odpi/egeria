/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.ValidValuesImplProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValueImplAssetElement contains the properties and header for a reference data set linked to a valid value retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueImplAssetElement extends ValidValuesImplProperties
{
    private ReferenceDataAssetElement referenceDataAsset       = null;
    private List<Connection>          referenceDataConnections = null;


    /**
     * Default constructor
     */
    public ValidValueImplAssetElement()
    {
        super();
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public ValidValueImplAssetElement(ValidValueImplAssetElement template)
    {
        super(template);

        if (template != null)
        {
            referenceDataAsset       = template.getReferenceDataAsset();
            referenceDataConnections = template.getReferenceDataConnections();
        }
    }

    /**
     * Returns the asset where the implementation of the valid value set is stored.
     *
     * @return properties of the asset
     */
    public ReferenceDataAssetElement getReferenceDataAsset()
    {
        return referenceDataAsset;
    }


    /**
     * Set up the asset where the implementation of the valid value set is stored.
     *
     * @param referenceDataAsset properties of the asset
     */
    public void setReferenceDataAsset(ReferenceDataAssetElement referenceDataAsset)
    {
        this.referenceDataAsset = referenceDataAsset;
    }


    /**
     * Return the list of connections assigned to the reference data asset.
     *
     * @return Connection object
     */
    public List<Connection> getReferenceDataConnections()
    {
        if (referenceDataConnections == null)
        {
            return null;
        }
        else if (referenceDataConnections.isEmpty())
        {
            return null;
        }
        else
        {
            return referenceDataConnections;
        }
    }


    /**
     * Set up the list of connections assigned to the reference data asset.
     *
     * @param connections Connection object list
     */
    public void setReferenceDataConnections(List<Connection> connections)
    {
        this.referenceDataConnections = connections;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ValidValueImplAssetElement{" +
                ", referenceDataAsset=" + referenceDataAsset +
                ", ReferenceDataConnections=" + referenceDataConnections +
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
        ValidValueImplAssetElement that = (ValidValueImplAssetElement) objectToCompare;
        return Objects.equals(referenceDataAsset, that.referenceDataAsset) &&
                Objects.equals(referenceDataConnections, that.referenceDataConnections);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), referenceDataAsset, referenceDataConnections);
    }
}
