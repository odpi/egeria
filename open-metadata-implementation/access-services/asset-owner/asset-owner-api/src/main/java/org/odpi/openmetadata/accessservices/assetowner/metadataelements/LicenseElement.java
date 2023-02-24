/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetowner.properties.LicenseProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LicenseElement contains the properties and header for a license for an element.
 * It includes the details of the specific element's license and details of the license type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LicenseElement implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader          licenseHeader         = null;
    private RelationshipProperties licenseProperties     = null;
    private ElementHeader          licenseTypeHeader     = null;
    private LicenseProperties      licenseTypeProperties = null;

    /**
     * Default constructor
     */
    public LicenseElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public LicenseElement(LicenseElement template)
    {
        if (template != null)
        {
            licenseHeader = template.getLicenseHeader();
            licenseProperties = template.getLicenseProperties();
            licenseTypeHeader = template.getLicenseTypeHeader();
            licenseTypeProperties = template.getLicenseTypeProperties();
        }
    }


    /**
     * Return the element header associated with the relationship.
     *
     * @return element header object
     */
    public ElementHeader getLicenseHeader()
    {
        return licenseHeader;
    }


    /**
     * Set up the element header associated with the relationship.
     *
     * @param licenseHeader element header object
     */
    public void setLicenseHeader(ElementHeader licenseHeader)
    {
        this.licenseHeader = licenseHeader;
    }


    /**
     * Return details of the relationship
     *
     * @return relationship properties
     */
    public RelationshipProperties getLicenseProperties()
    {
        return licenseProperties;
    }


    /**
     * Set up relationship properties
     *
     * @param licenseProperties relationship properties
     */
    public void setLicenseProperties(RelationshipProperties licenseProperties)
    {
        this.licenseProperties = licenseProperties;
    }


    /**
     * Return the element header associated with end 2 of the relationship (license type).
     *
     * @return element stub object
     */
    public ElementHeader getLicenseTypeHeader()
    {
        return licenseTypeHeader;
    }


    /**
     * Set up the element header associated with end 2 of the relationship (license type).
     *
     * @param licenseTypeHeader element stub object
     */
    public void setLicenseTypeHeader(ElementHeader licenseTypeHeader)
    {
        this.licenseTypeHeader = licenseTypeHeader;
    }


    /**
     * Return the properties of the license type.
     *
     * @return properties
     */
    public LicenseProperties getLicenseTypeProperties()
    {
        return licenseTypeProperties;
    }


    /**
     * Set up the properties of the license type.
     *
     * @param licenseTypeProperties properties
     */
    public void setLicenseTypeProperties(LicenseProperties licenseTypeProperties)
    {
        this.licenseTypeProperties = licenseTypeProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "LicenseElement{" +
                       "licenseHeader=" + licenseHeader +
                       ", relationshipProperties=" + licenseProperties +
                       ", licenseTypeHeader=" + licenseTypeHeader +
                       ", licenseTypeProperties=" + licenseTypeProperties +
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
        if (! (objectToCompare instanceof LicenseElement))
        {
            return false;
        }
        LicenseElement that = (LicenseElement) objectToCompare;
        return Objects.equals(licenseHeader, that.licenseHeader) &&
                       Objects.equals(licenseProperties, that.licenseProperties) &&
                       Objects.equals(licenseTypeHeader, that.licenseTypeHeader) &&
                       Objects.equals(licenseTypeProperties, that.licenseTypeProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), licenseHeader, licenseProperties, licenseTypeHeader, licenseTypeProperties);
    }
}
