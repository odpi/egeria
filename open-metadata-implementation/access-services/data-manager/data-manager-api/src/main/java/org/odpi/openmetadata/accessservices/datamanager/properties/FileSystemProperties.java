/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.properties;

import java.util.Objects;


/**
 * FileSystemProperties describes the root node of a file system.
 */
public class FileSystemProperties extends SoftwareServerCapabilitiesProperties
{
    private static final long    serialVersionUID = 1L;

    private String              format = null;
    private String              encryption = null;


    /**
     * Default constructor
     */
    public FileSystemProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public FileSystemProperties(FileSystemProperties template)
    {
        super(template);

        if (template != null)
        {
            format = template.getFormat();
            encryption = template.getEncryption();
        }
    }


    /**
     * Copy/clone constructor from OCF bean.
     *
     * @param template object to copy
     */
    public FileSystemProperties(SoftwareServerCapabilitiesProperties template)
    {
        super(template);
    }


    /**
     * Return the format of the file system.
     *
     * @return string name
     */
    public String getFormat()
    {
        return format;
    }


    /**
     * Set up the format of the file system.
     *
     * @param format string name
     */
    public void setFormat(String format)
    {
        this.format = format;
    }


    /**
     * Return the type of encryption used on the file system (or null is unencrypted).
     *
     * @return encryption type
     */
    public String getEncryption()
    {
        return encryption;
    }


    /**
     * Set up the type of encryption used on the file system (or null is unencrypted).
     *
     * @param encryption encryption type
     */
    public void setEncryption(String encryption)
    {
        this.encryption = encryption;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "FileSystemProperties{" +
                "format='" + format + '\'' +
                ", encryption='" + encryption + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", vendorProperties=" + getVendorProperties() +
                ", typeName='" + getTypeName() + '\'' +
                ", extendedProperties=" + getExtendedProperties() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        FileSystemProperties that = (FileSystemProperties) objectToCompare;
        return Objects.equals(getFormat(), that.getFormat()) && Objects.equals(getEncryption(), that.getEncryption());
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getFormat(), getEncryption());
    }
}
