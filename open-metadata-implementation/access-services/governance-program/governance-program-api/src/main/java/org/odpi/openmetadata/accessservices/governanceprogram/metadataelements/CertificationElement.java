/*  SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.CertificationProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CertificationElement contains the properties and header for a certification for an element.
 * It includes the details of the specific element's certification and details of the certification type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CertificationElement
{
    private ElementHeader           certificationHeader         = null;
    private RelationshipProperties  certificationProperties     = null;
    private ElementHeader           certificationTypeHeader     = null;
    private CertificationProperties certificationTypeProperties = null;

    /**
     * Default constructor
     */
    public CertificationElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CertificationElement(CertificationElement template)
    {
        if (template != null)
        {
            certificationHeader = template.getCertificationHeader();
            certificationProperties = template.getCertificationProperties();
            certificationTypeHeader = template.getCertificationTypeHeader();
            certificationTypeProperties = template.getCertificationTypeProperties();
        }
    }


    /**
     * Return the element header associated with the relationship.
     *
     * @return element header object
     */
    public ElementHeader getCertificationHeader()
    {
        return certificationHeader;
    }


    /**
     * Set up the element header associated with the relationship.
     *
     * @param certificationHeader element header object
     */
    public void setCertificationHeader(ElementHeader certificationHeader)
    {
        this.certificationHeader = certificationHeader;
    }


    /**
     * Return details of the relationship
     *
     * @return relationship properties
     */
    public RelationshipProperties getCertificationProperties()
    {
        return certificationProperties;
    }


    /**
     * Set up relationship properties
     *
     * @param certificationProperties relationship properties
     */
    public void setCertificationProperties(RelationshipProperties certificationProperties)
    {
        this.certificationProperties = certificationProperties;
    }


    /**
     * Return the element header associated with end 2 of the relationship (certification type).
     *
     * @return element stub object
     */
    public ElementHeader getCertificationTypeHeader()
    {
        return certificationTypeHeader;
    }


    /**
     * Set up the element header associated with end 2 of the relationship (certification type).
     *
     * @param certificationTypeHeader element stub object
     */
    public void setCertificationTypeHeader(ElementHeader certificationTypeHeader)
    {
        this.certificationTypeHeader = certificationTypeHeader;
    }


    /**
     * Return the properties of the certification type.
     * 
     * @return properties
     */
    public CertificationProperties getCertificationTypeProperties()
    {
        return certificationTypeProperties;
    }


    /**
     * Set up the properties of the certification type.
     * 
     * @param certificationTypeProperties properties
     */
    public void setCertificationTypeProperties(CertificationProperties certificationTypeProperties)
    {
        this.certificationTypeProperties = certificationTypeProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CertificationElement{" +
                       "certificationHeader=" + certificationHeader +
                       ", relationshipProperties=" + certificationProperties +
                       ", certificationTypeHeader=" + certificationTypeHeader +
                       ", certificationTypeProperties=" + certificationTypeProperties +
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
        if (! (objectToCompare instanceof CertificationElement))
        {
            return false;
        }
        CertificationElement that = (CertificationElement) objectToCompare;
        return Objects.equals(certificationHeader, that.certificationHeader) && 
                       Objects.equals(certificationProperties, that.certificationProperties) &&
                       Objects.equals(certificationTypeHeader, that.certificationTypeHeader) && 
                       Objects.equals(certificationTypeProperties, that.certificationTypeProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), certificationHeader, certificationProperties, certificationTypeHeader, certificationTypeProperties);
    }
}
