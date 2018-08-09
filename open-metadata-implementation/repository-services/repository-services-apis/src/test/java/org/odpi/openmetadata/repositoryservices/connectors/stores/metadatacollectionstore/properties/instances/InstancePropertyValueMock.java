/* SPDX-License-Identifier: Apache-2.0 */
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
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of InstancePropertyValue
     */
    public  InstancePropertyValue cloneFromSubclass()
    {
        return new InstancePropertyValueMock(this);
    }
}
