/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * VirtualRelationalTableProperties is a class for representing a query that supports the access to
 * data as a relational table.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class VirtualRelationalTableProperties extends InformationViewProperties
{
    /**
     * Default constructor
     */
    public VirtualRelationalTableProperties()
    {
        super();
        super.typeName = OpenMetadataType.VIRTUAL_RELATIONAL_TABLE.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public VirtualRelationalTableProperties(VirtualRelationalTableProperties template)
    {
        super(template);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "VirtualRelationalTableProperties{} " + super.toString();
    }
}
