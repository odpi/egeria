/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ServerAssetUseType;
import org.odpi.openmetadata.commonservices.ffdc.rest.EffectiveTimeRequestBody;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UseTypeRequestBody for passing the use type and effective time.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UseTypeRequestBody extends EffectiveTimeRequestBody
{
    private static final long    serialVersionUID = 1L;

    private ServerAssetUseType useType = null;


    /**
     * Default constructor
     */
    public UseTypeRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public UseTypeRequestBody(UseTypeRequestBody template)
    {
        if (template != null)
        {
            useType = template.getUseType();
        }
    }


    /**
     * Return the use type to use for the query.
     *
     * @return enum
     */
    public ServerAssetUseType getUseType()
    {
        return useType;
    }


    /**
     * Set up the use type to use for the query.
     *
     * @param useType enum
     */
    public void setUseType(ServerAssetUseType useType)
    {
        this.useType = useType;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "UseTypeRequestBody{" +
                       "useType=" + useType +
                       ", effectiveTime=" + getEffectiveTime() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        UseTypeRequestBody that = (UseTypeRequestBody) objectToCompare;
        return Objects.equals(useType, that.useType);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), useType);
    }
}
