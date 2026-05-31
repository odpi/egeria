/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The DataHub entity describes a collection of data assets that are available for sharing, as long as the requester satisfies the requirements laid down by the data owner.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataHubProperties extends CollectionProperties
{
    /**
     * Default constructor
     */
    public DataHubProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_HUB.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public DataHubProperties(DataHubProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DataHubProperties(CollectionProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.DATA_HUB.typeName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataHubProperties{} " + super.toString();
    }
}
