/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContactDetailsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContributionRecordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.PortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CommunityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectorTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataClassProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataStructureProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingDescriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.BusinessCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.CommentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.NoteLogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.NoteProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.LocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SoftwareCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Many open metadata entities are referenceable.  It means that they have a qualified name and additional
 * properties.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ActorProperties.class, name = "ActorProperties"),
                @JsonSubTypes.Type(value = AssetProperties.class, name = "AssetProperties"),
                @JsonSubTypes.Type(value = CollectionProperties.class, name = "CollectionProperties"),
                @JsonSubTypes.Type(value = CommentProperties.class, name = "CommentProperties"),
                @JsonSubTypes.Type(value = CommunityProperties.class, name = "CommunityProperties"),
                @JsonSubTypes.Type(value = ConnectionProperties.class, name = "ConnectionProperties"),
                @JsonSubTypes.Type(value = ConnectorTypeProperties.class, name = "ConnectorTypeProperties"),
                @JsonSubTypes.Type(value = ContactDetailsProperties.class, name = "ContactDetailsProperties"),
                @JsonSubTypes.Type(value = ContextEventProperties.class, name = "ContextEventProperties"),
                @JsonSubTypes.Type(value = ContributionRecordProperties.class, name = "ContributionRecordProperties"),
                @JsonSubTypes.Type(value = DataClassProperties.class, name = "DataClassProperties"),
                @JsonSubTypes.Type(value = DataFieldProperties.class, name = "DataFieldProperties"),
                @JsonSubTypes.Type(value = DataProcessingActionProperties.class, name = "DataProcessingActionProperties"),
                @JsonSubTypes.Type(value = DataProcessingDescriptionProperties.class, name = "DataProcessingDescriptionProperties"),
                @JsonSubTypes.Type(value = DataStructureProperties.class, name = "DataStructureProperties"),
                @JsonSubTypes.Type(value = EndpointProperties.class, name = "EndpointProperties"),
                @JsonSubTypes.Type(value = ExternalIdProperties.class, name = "ExternalIdProperties"),
                @JsonSubTypes.Type(value = ExternalReferenceProperties.class, name = "ExternalReferenceProperties"),
                @JsonSubTypes.Type(value = GlossaryTermProperties.class, name = "GlossaryTermProperties"),
                @JsonSubTypes.Type(value = GovernanceDefinitionProperties.class, name = "GovernanceDefinitionProperties"),
                @JsonSubTypes.Type(value = LocationProperties.class, name = "LocationProperties"),
                @JsonSubTypes.Type(value = NoteProperties.class, name = "NoteProperties"),
                @JsonSubTypes.Type(value = NoteLogProperties.class, name = "NoteLogProperties"),
                @JsonSubTypes.Type(value = PortProperties.class, name = "PortProperties"),
                @JsonSubTypes.Type(value = ProjectProperties.class, name = "ProjectProperties"),
                @JsonSubTypes.Type(value = SchemaElementProperties.class, name = "SchemaElementProperties"),
                @JsonSubTypes.Type(value = SoftwareCapabilityProperties.class, name = "SoftwareCapabilityProperties"),
                @JsonSubTypes.Type(value = SolutionComponentProperties.class, name = "SolutionComponentProperties"),
                @JsonSubTypes.Type(value = ValidValueDefinitionProperties.class, name = "ValidValueDefinitionProperties"),
        })
public class ReferenceableProperties extends OpenMetadataRootProperties
{
    private String              qualifiedName        = null;
    private String              identifier           = null;
    private String              displayName          = null;
    private String              description          = null;
    private String              versionIdentifier    = null;
    private String              category             = null;
    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public ReferenceableProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.REFERENCEABLE.typeName);
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public ReferenceableProperties(ReferenceableProperties template)
    {
        super(template);

        if (template != null)
        {
            qualifiedName        = template.getQualifiedName();
            identifier           = template.getIdentifier();
            displayName          = template.getDisplayName();
            description          = template.getDescription();
            versionIdentifier    = template.getVersionIdentifier();
            category             = template.getCategory();
            additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Set up the fully qualified name.
     *
     * @param qualifiedName String name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Returns the stored qualified name property for the metadata entity.
     * If no qualified name is available then the empty string is returned.
     *
     * @return qualifiedName
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }



    /**
     * Return the code value or symbol used to identify the element - typically unique.
     *
     * @return string identifier
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Set up the code value or symbol used to identify the element - typically unique.
     *
     * @param identifier string identifier
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }



    /**
     * Returns the stored display name property for the element.
     * If no display name is available then null is returned.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the stored display name property for the element.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Returns the stored description property for the element.
     * If no description is provided then null is returned.
     *
     * @return  String text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the stored description property for the element.
     *
     * @param description String text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the author-controlled version identifier.
     *
     * @return version identifier
     */
    public String getVersionIdentifier()
    {
        return versionIdentifier;
    }


    /**
     * Set up the author-controlled version identifier.
     *
     * @param versionIdentifier version identifier
     */
    public void setVersionIdentifier(String versionIdentifier)
    {
        this.versionIdentifier = versionIdentifier;
    }


    /**
     * Return a descriptive name for the element's category.
     *
     * @return string name
     */
    public String getCategory()
    {
        return category;
    }


    /**
     * Set up a descriptive name for the element's category. This field can be used to group related elements together
     * that are used for the same activity, event through they may be different types.
     *
     * @param category string name
     */
    public void setCategory(String category)
    {
        this.category = category;
    }


    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ReferenceableProperties{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", identifier='" + identifier + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", versionIdentifier='" + versionIdentifier + '\'' +
                ", category='" + category + '\'' +
                ", additionalProperties=" + additionalProperties +
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
        ReferenceableProperties that = (ReferenceableProperties) objectToCompare;
        return Objects.equals(qualifiedName, that.qualifiedName) &&
                Objects.equals(identifier, that.identifier) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(versionIdentifier, that.versionIdentifier) &&
                Objects.equals(category, that.category) &&
                Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), qualifiedName, identifier, displayName, description, versionIdentifier, category, additionalProperties);
    }
}