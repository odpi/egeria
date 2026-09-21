/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataTypeDefElementHeader provides a common base for all typedef information.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataTypeDefElementHeader
{
    /**
     * Header version for this code base.
     */
    public static final long  CURRENT_TYPE_DEF_HEADER_VERSION = 1;

    /**
     * Default constructor sets OpenMetadataTypeDef to nulls.
     */
    public OpenMetadataTypeDefElementHeader()
    {
        super();

        /*
         * Nothing to do
         */
    }


    /**
     * Copy/clone constructor set OpenMetadataTypeDef to value in template.
     *
     * @param template OpenMetadataTypeDefElementHeader
     */
    public OpenMetadataTypeDefElementHeader(OpenMetadataTypeDefElementHeader template)
    {
        /*
         * Nothing to do
         */
    }


    /**
     * Two headers are equal when they are of the same class.  This class holds no instance fields, so there
     * is nothing else to compare - but without these two methods every subclass's {@code super.equals} and
     * {@code super.hashCode} reached {@link Object}, which compares by identity.  That made the whole type
     * def bean family compare unequal to its own copies, and gave equal beans different hash codes.
     *
     * @param objectToCompare object
     * @return boolean
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }

        return (objectToCompare != null) && (getClass() == objectToCompare.getClass());
    }


    /**
     * Return a hash code consistent with {@link #equals(Object)}.  There are no instance fields to hash, so
     * every instance of a given class hashes alike.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}
