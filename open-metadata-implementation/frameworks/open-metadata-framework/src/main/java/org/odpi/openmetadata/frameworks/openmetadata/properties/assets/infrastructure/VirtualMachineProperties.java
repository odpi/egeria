/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * VirtualMachineProperties is a class for representing a host that is a virtual machine.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class VirtualMachineProperties extends HostProperties
{
    /**
     * Default constructor
     */
    public VirtualMachineProperties()
    {
        super();
        super.typeName = OpenMetadataType.VIRTUAL_MACHINE.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public VirtualMachineProperties(VirtualMachineProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public VirtualMachineProperties(HostProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.VIRTUAL_MACHINE.typeName;
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public VirtualMachineProperties(AssetProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.VIRTUAL_MACHINE.typeName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "VirtualMachineProperties{} " + super.toString();
    }
}
