/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DcatContactPoint describes who to contact about a resource (vcard:Kind).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DcatContactPoint
{
    private String iri = null;
    private String name = null;
    private String email = null;
    private String telephone = null;
    private String url = null;
    private String organizationName = null;


    /**
     * Default constructor
     */
    public DcatContactPoint()
    {
    }


    /**
     * Return the IRI that identifies the contact point.
     *
     * @return string IRI
     */
    public String getIri()
    {
        return iri;
    }


    /**
     * Set up the IRI that identifies the contact point.
     *
     * @param iri string IRI
     */
    public void setIri(String iri)
    {
        this.iri = iri;
    }


    /**
     * Return the formatted name of the contact (vcard:fn).
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the formatted name of the contact (vcard:fn).
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the email address of the contact (vcard:hasEmail).
     *
     * @return string email address
     */
    public String getEmail()
    {
        return email;
    }


    /**
     * Set up the email address of the contact (vcard:hasEmail).
     *
     * @param email string email address
     */
    public void setEmail(String email)
    {
        this.email = email;
    }


    /**
     * Return the telephone number of the contact (vcard:hasTelephone).
     *
     * @return string telephone number
     */
    public String getTelephone()
    {
        return telephone;
    }


    /**
     * Set up the telephone number of the contact (vcard:hasTelephone).
     *
     * @param telephone string telephone number
     */
    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }


    /**
     * Return the web page for the contact (vcard:hasURL).
     *
     * @return string URL
     */
    public String getUrl()
    {
        return url;
    }


    /**
     * Set up the web page for the contact (vcard:hasURL).
     *
     * @param url string URL
     */
    public void setUrl(String url)
    {
        this.url = url;
    }


    /**
     * Return the name of the contact's organization (vcard:organization-name).
     *
     * @return string name
     */
    public String getOrganizationName()
    {
        return organizationName;
    }


    /**
     * Set up the name of the contact's organization (vcard:organization-name).
     *
     * @param organizationName string name
     */
    public void setOrganizationName(String organizationName)
    {
        this.organizationName = organizationName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DcatContactPoint{" +
                       "iri='" + iri + '\'' +
                       ", name=" + name +
                       ", email=" + email +
                       ", telephone=" + telephone +
                       ", url=" + url +
                       ", organizationName=" + organizationName +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        DcatContactPoint that = (DcatContactPoint) objectToCompare;
        return Objects.equals(iri, that.iri) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(email, that.email) &&
                       Objects.equals(telephone, that.telephone) &&
                       Objects.equals(url, that.url) &&
                       Objects.equals(organizationName, that.organizationName);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(iri, name, email, telephone, url, organizationName);
    }
}
