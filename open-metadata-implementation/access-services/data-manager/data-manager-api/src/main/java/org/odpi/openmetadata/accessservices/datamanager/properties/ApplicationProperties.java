/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.properties;


/**
 * ApplicationProperties describes an collection of processes (application) that implements support for the business.
 */
public class ApplicationProperties extends SoftwareCapabilitiesProperties
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public ApplicationProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ApplicationProperties(ApplicationProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor from OCF bean.
     *
     * @param template object to copy
     */
    public ApplicationProperties(SoftwareCapabilitiesProperties template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ApplicationProperties{" +
                       "displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", typeDescription='" + getTypeDescription() + '\'' +
                       ", version='" + getVersion() + '\'' +
                       ", patchLevel='" + getPatchLevel() + '\'' +
                       ", source='" + getSource() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
    }
}
