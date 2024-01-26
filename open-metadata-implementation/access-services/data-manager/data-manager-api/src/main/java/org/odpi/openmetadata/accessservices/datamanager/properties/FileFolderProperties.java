/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FileFolderProperties defines an asset that is a folder.  The qualified name is the fully qualified path name of the folder.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileFolderProperties extends DataStoreProperties
{
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
                       "deployedImplementationType='" + getDeployedImplementationType() + '\'' +
                       ", pathName='" + getPathName() + '\'' +
                       ", createTime=" + getCreateTime() +
                       ", modifiedTime=" + getModifiedTime() +
                       ", encodingType='" + getEncodingType() + '\'' +
                       ", encodingLanguage='" + getEncodingLanguage() + '\'' +
                       ", encodingDescription='" + getEncodingDescription() + '\'' +
                       ", encodingProperties=" + getEncodingProperties() +
                       ", name='" + getName() + '\'' +
                       ", versionIdentifier='" + getVersionIdentifier() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
    }
}
