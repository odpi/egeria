/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LicenseTypeProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LicenseTypeElement contains the properties and header for a licence type retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LicenseTypeElement implements MetadataElement
{
    private ElementHeader         elementHeader  = null;
    private LicenseTypeProperties properties = null;
    private RelatedBy             relatedBy  = null;


    /**
     * Default constructor
     */
    public LicenseTypeElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public LicenseTypeElement(LicenseTypeElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();
            relatedBy  = template.getRelatedBy();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties of the license type.
     *
     * @return properties
     */
    public LicenseTypeProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the license type properties.
     *
     * @param properties  properties
     */
    public void setProperties(LicenseTypeProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return details of the relationship used to retrieve this element.
     * Will be null if the element was retrieved directly rather than via a relationship.
     *
     * @return list of element stubs
     */
    public RelatedBy getRelatedBy()
    {
        return relatedBy;
    }


    /**
     * Set up details of the relationship used to retrieve this element.
     * Will be null if the element was retrieved directly rather than via a relationship.
     *
     * @param relatedBy relationship details
     */
    public void setRelatedBy(RelatedBy relatedBy)
    {
        this.relatedBy = relatedBy;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "LicenseTypeElement{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
                       ", relatedBy=" + relatedBy +
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
        LicenseTypeElement that = (LicenseTypeElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(relatedBy, that.relatedBy);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, properties, relatedBy);
    }
}
