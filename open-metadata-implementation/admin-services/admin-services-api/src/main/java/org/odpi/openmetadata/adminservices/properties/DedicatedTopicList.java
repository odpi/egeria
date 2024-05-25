/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.rest.AdminServicesAPIResponse;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * DedicatedTopicList returns the names of the topics used for the dedicated topic structure.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DedicatedTopicList
{
    private String registrationTopicName = null;
    private String typesTopicName        = null;
    private String instancesTopicName    = null;


    /**
     * Default constructor
     */
    public DedicatedTopicList()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DedicatedTopicList(DedicatedTopicList template)
    {
        if (template != null)
        {
            registrationTopicName = template.getRegistrationTopicName();
            typesTopicName = template.getTypesTopicName();
            instancesTopicName = template.getInstancesTopicName();
        }
    }


    /**
     * Return the name of the topic used for registration events.
     *
     * @return string name
     */
    public String getRegistrationTopicName()
    {
        return registrationTopicName;
    }


    /**
     * Set up the name of the topic used for registration events.
     *
     * @param registrationTopicName string name
     */
    public void setRegistrationTopicName(String registrationTopicName)
    {
        this.registrationTopicName = registrationTopicName;
    }


    /**
     * Return the name of the topic used to pass type verification messages.
     *
     * @return string name
     */
    public String getTypesTopicName()
    {
        return typesTopicName;
    }


    /**
     * Set up the name of the topic used to pass type verification messages.
     *
     * @param typesTopicName string name
     */
    public void setTypesTopicName(String typesTopicName)
    {
        this.typesTopicName = typesTopicName;
    }


    /**
     * Return the name of the topic used to pass messages about metadata instances.
     *
     * @return string name
     */
    public String getInstancesTopicName()
    {
        return instancesTopicName;
    }


    /**
     * Set up the name of the topic used to pass messages about metadata instances.
     *
     * @param instancesTopicName string name
     */
    public void setInstancesTopicName(String instancesTopicName)
    {
        this.instancesTopicName = instancesTopicName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DedicatedTopicList{" +
                       "registrationTopicName='" + registrationTopicName + '\'' +
                       ", typesTopicName='" + typesTopicName + '\'' +
                       ", instancesTopicName='" + instancesTopicName + '\'' +
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
        DedicatedTopicList that = (DedicatedTopicList) objectToCompare;
        return Objects.equals(registrationTopicName, that.registrationTopicName) &&
                       Objects.equals(typesTopicName, that.typesTopicName) &&
                       Objects.equals(instancesTopicName, that.instancesTopicName);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(registrationTopicName, typesTopicName, instancesTopicName);
    }
}
