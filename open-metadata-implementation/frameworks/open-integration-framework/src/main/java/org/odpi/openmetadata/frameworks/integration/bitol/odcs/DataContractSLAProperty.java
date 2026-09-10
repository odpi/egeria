/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolCustomProperty;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolAuthoritativeDefinition;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a service level agreement (SLA) property of an Open Data Contract Standard (ODCS) data
 * contract. Examples of properties are latency, generalAvailability, endOfSupport, endOfLife, retention,
 * frequency and timeOfAvailability. The type of the value depends on the property.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataContractSLAProperty
{
    private String id = null;
    private String property = null;
    private Object value = null;
    private Object valueExt = null;
    private String unit = null;
    private String element = null;
    private String driver = null;
    private String description = null;
    private String scheduler = null;
    private String schedule = null;
    private List<BitolCustomProperty> customProperties = null;
    private List<BitolAuthoritativeDefinition> authoritativeDefinitions = null;


    /**
     * Default constructor
     */
    public DataContractSLAProperty()
    {
    }


    /**
     * Return the stable identifier of this element (alphanumeric, hyphen and underscore characters only).
     *
     * @return string identifier
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set up the stable identifier of this element (alphanumeric, hyphen and underscore characters only).
     *
     * @param id string identifier
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Return the name of the SLA property.
     *
     * @return string property name
     */
    public String getProperty()
    {
        return property;
    }


    /**
     * Set up the name of the SLA property.
     *
     * @param property string property name
     */
    public void setProperty(String property)
    {
        this.property = property;
    }


    /**
     * Return the agreed value of the SLA property (string, number, boolean or null).
     *
     * @return agreed value
     */
    public Object getValue()
    {
        return value;
    }


    /**
     * Set up the agreed value of the SLA property (string, number, boolean or null).
     *
     * @param value agreed value
     */
    public void setValue(Object value)
    {
        this.value = value;
    }


    /**
     * Return the extended agreed value of the SLA property.
     *
     * @return extended value
     */
    public Object getValueExt()
    {
        return valueExt;
    }


    /**
     * Set up the extended agreed value of the SLA property.
     *
     * @param valueExt extended value
     */
    public void setValueExt(Object valueExt)
    {
        this.valueExt = valueExt;
    }


    /**
     * Return the unit of the value (ISO standard, for example d for days and y for years).
     *
     * @return string unit
     */
    public String getUnit()
    {
        return unit;
    }


    /**
     * Set up the unit of the value (ISO standard, for example d for days and y for years).
     *
     * @param unit string unit
     */
    public void setUnit(String unit)
    {
        this.unit = unit;
    }


    /**
     * Return the schema element(s) that the SLA property applies to (object.property).
     *
     * @return string element reference
     */
    public String getElement()
    {
        return element;
    }


    /**
     * Set up the schema element(s) that the SLA property applies to (object.property).
     *
     * @param element string element reference
     */
    public void setElement(String element)
    {
        this.element = element;
    }


    /**
     * Return the importance of the SLA property: regulatory, analytics or operational.  Standard values are defined in DataContractSLADriver.
     *
     * @return string driver
     */
    public String getDriver()
    {
        return driver;
    }


    /**
     * Set up the importance of the SLA property: regulatory, analytics or operational.  Standard values are defined in DataContractSLADriver.
     *
     * @param driver string driver
     */
    public void setDriver(String driver)
    {
        this.driver = driver;
    }


    /**
     * Return the description of the SLA property.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the SLA property.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the name of the scheduler used to check the SLA property, for example cron.
     *
     * @return string scheduler name
     */
    public String getScheduler()
    {
        return scheduler;
    }


    /**
     * Set up the name of the scheduler used to check the SLA property, for example cron.
     *
     * @param scheduler string scheduler name
     */
    public void setScheduler(String scheduler)
    {
        this.scheduler = scheduler;
    }


    /**
     * Return the schedule configuration for the scheduler, for example a cron expression.
     *
     * @return string schedule
     */
    public String getSchedule()
    {
        return schedule;
    }


    /**
     * Set up the schedule configuration for the scheduler, for example a cron expression.
     *
     * @param schedule string schedule
     */
    public void setSchedule(String schedule)
    {
        this.schedule = schedule;
    }


    /**
     * Return the custom properties of this service level property.
     *
     * @return List<BitolCustomProperty>
     */
    public List<BitolCustomProperty> getCustomProperties()
    {
        return customProperties;
    }


    /**
     * Set up the custom properties of this service level property.
     *
     * @param customProperties List<BitolCustomProperty>
     */
    public void setCustomProperties(List<BitolCustomProperty> customProperties)
    {
        this.customProperties = customProperties;
    }


    /**
     * Return links to the sources that define this service level property.
     *
     * @return List<BitolAuthoritativeDefinition>
     */
    public List<BitolAuthoritativeDefinition> getAuthoritativeDefinitions()
    {
        return authoritativeDefinitions;
    }


    /**
     * Set up links to the sources that define this service level property.
     *
     * @param authoritativeDefinitions List<BitolAuthoritativeDefinition>
     */
    public void setAuthoritativeDefinitions(List<BitolAuthoritativeDefinition> authoritativeDefinitions)
    {
        this.authoritativeDefinitions = authoritativeDefinitions;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataContractSLAProperty{" +
                       "id='" + id + '\'' +
                       ", property='" + property + '\'' +
                       ", value=" + value +
                       ", valueExt=" + valueExt +
                       ", unit='" + unit + '\'' +
                       ", element='" + element + '\'' +
                       ", driver='" + driver + '\'' +
                       ", description='" + description + '\'' +
                       ", scheduler='" + scheduler + '\'' +
                       ", schedule='" + schedule + '\'' +
                       ", customProperties=" + customProperties +
                       ", authoritativeDefinitions=" + authoritativeDefinitions +
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
        DataContractSLAProperty that = (DataContractSLAProperty) objectToCompare;
        return Objects.equals(id, that.id) &&
                       Objects.equals(property, that.property) &&
                       Objects.equals(value, that.value) &&
                       Objects.equals(valueExt, that.valueExt) &&
                       Objects.equals(unit, that.unit) &&
                       Objects.equals(element, that.element) &&
                       Objects.equals(driver, that.driver) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(scheduler, that.scheduler) &&
                       Objects.equals(schedule, that.schedule) &&
                       Objects.equals(customProperties, that.customProperties) &&
                       Objects.equals(authoritativeDefinitions, that.authoritativeDefinitions);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(id, property, value, valueExt, unit, element, driver, description, scheduler, schedule, customProperties, authoritativeDefinitions);
    }
}
