/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalSubscriptionProperties describes the agreement between a subscriber to one or more digital products.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DigitalSubscriptionProperties extends AgreementProperties
{
    private String              supportLevel  = null;
    private Map<String, String> serviceLevels = null;


    /**
     * Default constructor
     */
    public DigitalSubscriptionProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DigitalSubscriptionProperties(DigitalSubscriptionProperties template)
    {
        super(template);

        if (template != null)
        {
            this.supportLevel = template.getSupportLevel();
            this.serviceLevels = template.getServiceLevels();
        }
    }


    /**
     * Return the level of support offered in this subscription.
     *
     * @return string
     */
    public String getSupportLevel()
    {
        return supportLevel;
    }


    /**
     * Set up the level of support offered in this subscription.
     *
     * @param supportLevel string
     */
    public void setSupportLevel(String supportLevel)
    {
        this.supportLevel = supportLevel;
    }


    /**
     * Return the map of service levels agreed in this subscription.
     *
     * @return map
     */
    public Map<String, String> getServiceLevels()
    {
        return serviceLevels;
    }


    /**
     * Set up the map of service levels agreed in this subscription.
     *
     * @param serviceLevels int
     */
    public void setServiceLevels(Map<String, String> serviceLevels)
    {
        this.serviceLevels = serviceLevels;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DigitalSubscriptionProperties{" +
                "supportLevel='" + supportLevel + '\'' +
                ", serviceLevels=" + serviceLevels +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        DigitalSubscriptionProperties that = (DigitalSubscriptionProperties) objectToCompare;
        return Objects.equals(supportLevel, that.supportLevel) && Objects.equals(serviceLevels, that.serviceLevels);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getSupportLevel(), getServiceLevels());
    }
}
