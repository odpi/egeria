/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ResourceManagerProperties describes the properties of a resource manager.  This is a classification on
 * some form of IT infrastructure.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
// todo this mapping is not complete
@JsonSubTypes({
        @JsonSubTypes.Type(value = AssetManagerProperties.class, name = "AssetManagerProperties"),
        @JsonSubTypes.Type(value = ChangeManagementLibraryProperties.class, name = "ChangeManagementLibraryProperties"),
        @JsonSubTypes.Type(value = ContentCollectionManagerProperties.class, name = "ContentCollectionManagerProperties"),
        @JsonSubTypes.Type(value = FileSystemProperties.class, name = "FileSystemProperties"),
        @JsonSubTypes.Type(value = FileManagerProperties.class, name = "FileManagerProperties"),
        @JsonSubTypes.Type(value = NotificationManagerProperties.class, name = "NotificationManagerProperties"),
        @JsonSubTypes.Type(value = MasterDataManagerProperties.class, name = "MasterDataManagerProperties"),
        @JsonSubTypes.Type(value = SourceControlLibraryProperties.class, name = "SourceControlLibraryProperties"),
        @JsonSubTypes.Type(value = SoftwareLibraryProperties.class, name = "SoftwareLibraryProperties"),
        @JsonSubTypes.Type(value = UserAccessDirectoryProperties.class, name = "UserAccessDirectoryProperties"),
        @JsonSubTypes.Type(value = UserProfileManagerProperties.class, name = "UserProfileManagerProperties"),

})
public class ResourceManagerProperties extends ClassificationBeanProperties
{
    /**
     * Default constructor
     */
    public ResourceManagerProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.RESOURCE_MANAGER_CLASSIFICATION.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ResourceManagerProperties(ResourceManagerProperties template)
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
        return "ResourceManagerProperties{} " + super.toString();
    }
}
