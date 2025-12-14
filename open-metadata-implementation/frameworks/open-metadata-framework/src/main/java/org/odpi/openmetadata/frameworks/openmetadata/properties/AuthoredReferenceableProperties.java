/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.DeployedSoftwareComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataClassProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataStructureProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingDescriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.DesignModelElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Authored referenceables are elements that are created by subject-matter experts.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CollectionProperties.class, name = "CollectionProperties"),
        @JsonSubTypes.Type(value = DataClassProperties.class, name = "DataClassProperties"),
        @JsonSubTypes.Type(value = DataFieldProperties.class, name = "DataFieldProperties"),
        @JsonSubTypes.Type(value = DataProcessingActionProperties.class, name = "DataProcessingActionProperties"),
        @JsonSubTypes.Type(value = DataProcessingDescriptionProperties.class, name = "DataProcessingDescriptionProperties"),
        @JsonSubTypes.Type(value = DataStructureProperties.class, name = "DataStructureProperties"),
        @JsonSubTypes.Type(value = DesignModelElementProperties.class, name = "DesignModelElementProperties"),
        @JsonSubTypes.Type(value = GlossaryTermProperties.class, name = "GlossaryTermProperties"),
        @JsonSubTypes.Type(value = GovernanceDefinitionProperties.class, name = "GovernanceDefinitionProperties"),
        @JsonSubTypes.Type(value = SchemaElementProperties.class, name = "SchemaElementProperties"),
        @JsonSubTypes.Type(value = ValidValueDefinitionProperties.class, name = "ValidValueDefinitionProperties"),
})
public class AuthoredReferenceableProperties extends ReferenceableProperties
{
    private List<String>  authors = null;
    private ContentStatus contentStatus            = null;
    private String        userDefinedContentStatus = null;


    /**
     * Default constructor
     */
    public AuthoredReferenceableProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.AUTHORED_REFERENCEABLE.typeName);;
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public AuthoredReferenceableProperties(AuthoredReferenceableProperties template)
    {
        super(template);

        if (template != null)
        {
            authors                  = template.getAuthors();
            contentStatus            = template.getContentStatus();
            userDefinedContentStatus = template.getUserDefinedContentStatus();
        }
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public AuthoredReferenceableProperties(ReferenceableProperties template)
    {
        super(template);
    }


    /**
     * Return the list of authors for this element.
     *
     * @return list
     */
    public List<String> getAuthors()
    {
        return authors;
    }

    /**
     * Set up the list of authors for this element.
     *
     * @param authors list
     */
    public void setAuthors(List<String> authors)
    {
        this.authors = authors;
    }


    /**
     * Return the status of the content.
     *
     * @return status enum
     */
    public ContentStatus getContentStatus()
    {
        return contentStatus;
    }


    /**
     * Set up the status of the content.
     *
     * @param contentStatus status enum
     */
    public void setContentStatus(ContentStatus contentStatus)
    {
        this.contentStatus = contentStatus;
    }


    /**
     * Return additionally defined content statuses.
     *
     * @return string
     */
    public String getUserDefinedContentStatus()
    {
        return userDefinedContentStatus;
    }


    /**
     * Set up additionally defined content statuses.
     *
     * @param userDefinedContentStatus string
     */
    public void setUserDefinedContentStatus(String userDefinedContentStatus)
    {
        this.userDefinedContentStatus = userDefinedContentStatus;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AuthoredReferenceableProperties{" +
                "authors=" + authors +
                ", contentStatus=" + contentStatus +
                ", userDefinedContentStatus='" + userDefinedContentStatus + '\'' +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        AuthoredReferenceableProperties that = (AuthoredReferenceableProperties) objectToCompare;
        return  authors == that.authors &&
                contentStatus == that.contentStatus &&
                Objects.equals(userDefinedContentStatus, that.userDefinedContentStatus);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), authors, contentStatus, userDefinedContentStatus);
    }
}
