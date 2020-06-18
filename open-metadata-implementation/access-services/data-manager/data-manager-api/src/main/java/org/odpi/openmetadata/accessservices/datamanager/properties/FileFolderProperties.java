/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.properties;

/**
 * FileFolderProperties defines an asset that is a folder.  The qualified name is the fully qualified path name of the folder.
 */
public class FileFolderProperties extends DataStoreProperties
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public FileFolderProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FileFolderProperties(FileFolderProperties template)
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
        return "FileFolderProperties{" +
                "createTime=" + getCreateTime() +
                ", modifiedTime=" + getModifiedTime() +
                ", encodingType='" + getEncodingType() + '\'' +
                ", encodingLanguage='" + getEncodingLanguage() + '\'' +
                ", encodingDescription='" + getEncodingDescription() + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", owner='" + getOwner() + '\'' +
                ", ownerCategory=" + getOwnerCategory() +
                ", zoneMembership=" + getZoneMembership() +
                ", origin=" + getOrigin() +
                ", latestChange='" + getLatestChange() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", vendorProperties=" + getVendorProperties() +
                ", typeName='" + getTypeName() + '\'' +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }
}
