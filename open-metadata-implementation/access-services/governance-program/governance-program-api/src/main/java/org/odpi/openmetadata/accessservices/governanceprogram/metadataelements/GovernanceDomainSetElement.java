/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.metadataelements;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomainSetProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * GovernanceDomainSetElement is the bean used to return a governance domain set definition stored in the open metadata repositories.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceDomainSetElement implements Serializable
{
    private static final long serialVersionUID = 1L;

    private ElementHeader                 elementHeader = null;
    private GovernanceDomainSetProperties properties    = null;
    private List<GovernanceDomainElement> domains       = null;


    /**
     * Default constructor
     */
    public GovernanceDomainSetElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceDomainSetElement(GovernanceDomainSetElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();
            domains = template.getDomains();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties of the governance domain set.
     *
     * @return properties bean
     */
    public GovernanceDomainSetProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of the governance domain set.
     *
     * @param properties properties bean
     */
    public void setProperties(GovernanceDomainSetProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the list of governance domains that are included in the governance set.
     *
     * @return list of governance domain elements
     */
    public List<GovernanceDomainElement> getDomains()
    {
        return domains;
    }


    /**
     * Set up the list of governance domains that are included in the governance set.
     *
     * @param domains list of governance domain elements
     */
    public void setDomains(List<GovernanceDomainElement> domains)
    {
        this.domains = domains;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceDomainSetElement{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
                       ", domains=" + domains +
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
        GovernanceDomainSetElement that = (GovernanceDomainSetElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(domains, that.domains);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, properties, domains);
    }
}
