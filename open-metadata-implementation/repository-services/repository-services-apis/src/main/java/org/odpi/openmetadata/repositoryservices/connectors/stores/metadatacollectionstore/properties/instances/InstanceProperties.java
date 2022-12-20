/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSRuntimeException;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The InstanceProperties class provides support for properties to be added to an entity,
 * classification or relationship instances.   These properties are maintained by the consumers of metadata
 * (typically the Open Metadata Access Services (OMASs).
 *
 * There are some fixed properties that are available on all instances.  These are:
 * <ul>
 *     <li>
 *         effectiveFromTime: Date/Time when the instance should be used.  If this value is null then the
 *         instance can be used as soon as it is created.
 *     </li>
 *     <li>
 *         effectiveToTime: Date/Time when the instance should not longer be used.  If this is null then
 *         the instance can be used until it is deleted.
 *     </li>
 * </ul>
 * Then there are variable properties that are defined in the TypeDefs. They are managed in
 * a java.util.Map map object built around HashMap.  The property name (or domain) of the map is the name
 * of the property.  The property value (or range) of the map is a subclass of InstancePropertyValue depending on
 * the type of the property:
 * <ul>
 *     <li>
 *         PrimitivePropertyValue: for primitives such as strings and numbers.  The full list of primitives are
 *         given in PrimitiveDefCategory.
 *     </li>
 *     <li>
 *         EnumPropertyValue: for properties with a type consisting of an enumeration of valid values.  Each
 *     </li>
 *     <li>
 *         StructPropertyValue: for properties that have a type of a complex structure (aka struct).
 *         The Struct can be thought of as a list of related properties.
 *     </li>
 *     <li>
 *         MapPropertyValue: for properties that have a type of map.
 *         The map holds an unordered list of name-value pairs.  The pairs are of the same type and the name for
 *         the pair is unique within the map.
 *     </li>
 *     <li>
 *         ArrayPropertyValue: for properties that have a type of Array.
 *         This is an ordered list of values of the same type.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InstanceProperties extends InstanceElementHeader
{
    private static final long    serialVersionUID = 1L;

    private Date                                effectiveFromTime = null;
    private Date                                effectiveToTime = null;
    private Map<String, InstancePropertyValue>  instanceProperties = new HashMap<>();


    /**
     * Typical constructor
     */
    public InstanceProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateProperties template object to copy.
     */
    public InstanceProperties(InstanceProperties templateProperties)
    {
        super(templateProperties);

        /*
         * An empty properties object is created in the private variable declaration so nothing to do.
         */
        if (templateProperties != null)
        {
            this.effectiveFromTime = templateProperties.getEffectiveFromTime();
            this.effectiveToTime = templateProperties.getEffectiveToTime();
            Iterator<String> propertyNames = templateProperties.getPropertyNames();

            if (propertyNames != null)
            {
                while (propertyNames.hasNext())
                {
                    String                 newPropertyName  = propertyNames.next();
                    InstancePropertyValue  newPropertyValue = templateProperties.getPropertyValue(newPropertyName);

                    instanceProperties.put(newPropertyName, newPropertyValue);
                }
            }
        }
    }


    /**
     * Return the date/time that this instance should start to be used (null means it can be used from creationTime).
     *
     * @return Date object
     */
    public Date getEffectiveFromTime()
    {
        if (effectiveFromTime == null)
        {
            return null;
        }
        else
        {
            return effectiveFromTime;
        }
    }


    /**
     * Set up the date/time that this instance should start to be used (null means it can be used from creationTime).
     *
     * @param effectiveFromTime Date object
     */
    public void setEffectiveFromTime(Date effectiveFromTime)
    {
        this.effectiveFromTime = effectiveFromTime;
    }


    /**
     * Return the date/time that this instance should no longer be used.
     *
     * @return Date object
     */
    public Date getEffectiveToTime()
    {
        return effectiveToTime;
    }


    /**
     * Set up the date/time that this instance should no longer be used.
     *
     * @param effectiveToTime Date object
     */
    public void setEffectiveToTime(Date effectiveToTime)
    {
        this.effectiveToTime = effectiveToTime;
    }


    /**
     * Return the instance properties as a map.
     *
     * @return  instance properties map.
     */
    public Map<String, InstancePropertyValue> getInstanceProperties()
    {
        if (instanceProperties == null)
        {
            return null;
        }
        else if (instanceProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(instanceProperties);
        }
    }


    /**
     * Set up the instance properties map.
     *
     * @param instanceProperties map of name valued properties
     */
    public void setInstanceProperties(Map<String, InstancePropertyValue> instanceProperties)
    {
        if (instanceProperties == null)
        {
            this.instanceProperties = new HashMap<>();
        }
        else
        {
            this.instanceProperties = instanceProperties;
        }
    }


    /**
     * Returns a list of the instance properties for the element.
     * If no stored properties are present then null is returned.
     *
     * @return list of properties
     */
    public Iterator<String> getPropertyNames()
    {
        return instanceProperties.keySet().iterator();
    }


    /**
     * Returns the requested instance property for the element.
     * If no stored property with that name is present then null is returned.
     *
     * @param name String name of the property to return.
     * @return requested property value.
     */
    public InstancePropertyValue getPropertyValue(String name)
    {
        return instanceProperties.get(name);
    }


    /**
     * Adds or updates an instance property.
     * If a null is supplied for the property name, an OMRS runtime exception is thrown.
     * If a null is supplied for the property value, the property is removed.
     *
     * @param  newPropertyName name
     * @param  newPropertyValue value
     */
    public void setProperty(String newPropertyName, InstancePropertyValue newPropertyValue)
    {
        final String methodName = "setProperty";

        if (newPropertyName == null)
        {
            /*
             * Build and throw exception.
             */
            throw new OMRSRuntimeException(OMRSErrorCode.NULL_PROPERTY_NAME.getMessageDefinition(),
                                           this.getClass().getName(),
                                           methodName);
        }
        else if (newPropertyValue == null)
        {
            instanceProperties.remove(newPropertyName);
        }
        else
        {
            instanceProperties.put(newPropertyName, newPropertyValue);
        }
    }


    /**
     * Return the number of properties stored.
     *
     * @return int property count
     */
    public int getPropertyCount()
    {
        return instanceProperties.size();
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "InstanceProperties{" +
                "propertyNames=" + getPropertyNames() +
                ", propertyCount=" + getPropertyCount() +
                ", instanceProperties=" + instanceProperties +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof InstanceProperties))
        {
            return false;
        }
        InstanceProperties that = (InstanceProperties) objectToCompare;
        return Objects.equals(getEffectiveFromTime(), that.getEffectiveFromTime()) &&
                Objects.equals(getEffectiveToTime(), that.getEffectiveToTime()) &&
                Objects.equals(getInstanceProperties(), that.getInstanceProperties());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(getEffectiveFromTime(), getEffectiveToTime(), getInstanceProperties());
    }
}

