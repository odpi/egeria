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
 * DcatAgent describes the person or organization (foaf:Agent) that publishes or creates a resource.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DcatAgent
{
    private String iri = null;
    private String name = null;
    private String agentType = null;
    private String homepage = null;
    private String mbox = null;


    /**
     * Default constructor
     */
    public DcatAgent()
    {
    }


    /**
     * Return the IRI that identifies the agent.
     *
     * @return string IRI
     */
    public String getIri()
    {
        return iri;
    }


    /**
     * Set up the IRI that identifies the agent.
     *
     * @param iri string IRI
     */
    public void setIri(String iri)
    {
        this.iri = iri;
    }


    /**
     * Return the name of the agent (foaf:name).
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the agent (foaf:name).
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the type of the agent - typically the IRI for foaf:Organization or foaf:Person.
     *
     * @return string IRI
     */
    public String getAgentType()
    {
        return agentType;
    }


    /**
     * Set up the type of the agent - typically the IRI for foaf:Organization or foaf:Person.
     *
     * @param agentType string IRI
     */
    public void setAgentType(String agentType)
    {
        this.agentType = agentType;
    }


    /**
     * Return the home page of the agent (foaf:homepage).
     *
     * @return string URL
     */
    public String getHomepage()
    {
        return homepage;
    }


    /**
     * Set up the home page of the agent (foaf:homepage).
     *
     * @param homepage string URL
     */
    public void setHomepage(String homepage)
    {
        this.homepage = homepage;
    }


    /**
     * Return the email address of the agent (foaf:mbox).
     *
     * @return string email address
     */
    public String getMbox()
    {
        return mbox;
    }


    /**
     * Set up the email address of the agent (foaf:mbox).
     *
     * @param mbox string email address
     */
    public void setMbox(String mbox)
    {
        this.mbox = mbox;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DcatAgent{" +
                       "iri='" + iri + '\'' +
                       ", name=" + name +
                       ", agentType=" + agentType +
                       ", homepage=" + homepage +
                       ", mbox=" + mbox +
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
        DcatAgent that = (DcatAgent) objectToCompare;
        return Objects.equals(iri, that.iri) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(agentType, that.agentType) &&
                       Objects.equals(homepage, that.homepage) &&
                       Objects.equals(mbox, that.mbox);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(iri, name, agentType, homepage, mbox);
    }
}
