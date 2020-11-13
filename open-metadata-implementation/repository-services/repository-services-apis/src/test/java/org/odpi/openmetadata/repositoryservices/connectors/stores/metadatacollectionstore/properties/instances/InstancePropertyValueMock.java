/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InstancePropertyValueMock provides a concrete class to test InstancePropertyValue
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InstancePropertyValueMock extends InstancePropertyValue
{
    private static final long     serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public InstancePropertyValueMock()
    {
        super();
    }


    /**
     * Typical constructor initializes the instance property value to nulls.
     *
     * @param instancePropertyCategory InstancePropertyCategory Enum
     */
    protected InstancePropertyValueMock(InstancePropertyCategory   instancePropertyCategory)
    {
        super(instancePropertyCategory);
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to clone
     */
    public InstancePropertyValueMock(InstancePropertyValue template)
    {
        super(template);
    }


    /**
     * Return the string version of the value - used for error logging.
     *
     * @return string value
     */
    public String valueAsString()
    {
        return "PropertyValue";
    }


    /**
     * Return the object version of the value - used for comparisons.
     *
     * @return object value
     */
    public Object valueAsObject()
    {
        return "PropertyObject";
    }

    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of InstancePropertyValue
     */
    public  InstancePropertyValue cloneFromSubclass()
    {
        return new InstancePropertyValueMock(this);
    }
}
