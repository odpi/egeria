/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.projects;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProjectKindProperties is used to provide the properties for a classification that identifies the role that the project is playing.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = CampaignProperties.class, name = "CampaignProperties"),
                @JsonSubTypes.Type(value = PersonalProjectProperties.class, name = "PersonalProjectProperties"),
                @JsonSubTypes.Type(value = StudyProjectProperties.class, name = "StudyProjectProperties"),
                @JsonSubTypes.Type(value = TaskProperties.class, name = "TaskProperties"),
        })
public class ProjectKindProperties extends ClassificationBeanProperties
{
    /**
     * Default constructor
     */
    public ProjectKindProperties()
    {
        super();
        super.typeName = OpenMetadataType.PROJECT_KIND_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor for a project kind classification.
     *
     * @param template template object to copy.
     */
    public ProjectKindProperties(ProjectKindProperties template)
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
        return "ProjectKindProperties{} " + super.toString();
    }
}
