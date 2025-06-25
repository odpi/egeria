/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actions.ToDoProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContributionRecord;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.PortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CommunityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectorTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataClassProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataStructureProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.BusinessCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.CommentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.NoteLogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.NoteProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryCategoryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.MeaningProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainSegmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.LocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.DatabaseSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.MetadataSourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SecurityManagerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueSetProperties;

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
                @JsonSubTypes.Type(value = ActorProfileProperties.class, name = "ActorProfileProperties"),
                @JsonSubTypes.Type(value = ActorRoleProperties.class, name = "ActorRoleProperties"),
                @JsonSubTypes.Type(value = AgreementProperties.class, name = "AgreementProperties"),
                @JsonSubTypes.Type(value = BusinessCapabilityProperties.class, name = "BusinessCapabilityProperties"),
                @JsonSubTypes.Type(value = CollectionProperties.class, name = "CollectionProperties"),
                @JsonSubTypes.Type(value = CommentProperties.class, name = "CommentProperties"),
                @JsonSubTypes.Type(value = CommunityProperties.class, name = "CommunityProperties"),
                @JsonSubTypes.Type(value = ConnectionProperties.class, name = "ConnectionProperties"),
                @JsonSubTypes.Type(value = ConnectorTypeProperties.class, name = "ConnectorTypeProperties"),
                @JsonSubTypes.Type(value = ContributionRecord.class, name = "ContributionRecord"),
                @JsonSubTypes.Type(value = DataClassProperties.class, name = "DataClassProperties"),
                @JsonSubTypes.Type(value = DataFieldProperties.class, name = "DataFieldProperties"),
                @JsonSubTypes.Type(value = DataStructureProperties.class, name = "DataStructureProperties"),
                @JsonSubTypes.Type(value = DatabaseSchemaTypeProperties.class, name = "DatabaseSchemaTypeProperties"),
                @JsonSubTypes.Type(value = ExecutionPointProperties.class, name = "ExecutionPointProperties"),
                @JsonSubTypes.Type(value = ExternalReferenceProperties.class, name = "ExternalReferenceProperties"),
                @JsonSubTypes.Type(value = GlossaryProperties.class, name = "GlossaryProperties"),
                @JsonSubTypes.Type(value = GlossaryCategoryProperties.class, name = "GlossaryCategoryProperties"),
                @JsonSubTypes.Type(value = GlossaryTermProperties.class, name = "GlossaryTermProperties"),
                @JsonSubTypes.Type(value = GovernanceMetricProperties.class, name = "GovernanceMetricProperties"),
                @JsonSubTypes.Type(value = GovernanceZoneProperties.class, name = "GovernanceZoneProperties"),
                @JsonSubTypes.Type(value = InformationSupplyChainProperties.class, name = "InformationSupplyChainProperties"),
                @JsonSubTypes.Type(value = InformationSupplyChainSegmentProperties.class, name = "InformationSupplyChainSegmentProperties"),
                @JsonSubTypes.Type(value = LocationProperties.class, name = "LocationProperties"),
                @JsonSubTypes.Type(value = MeaningProperties.class, name = "MeaningProperties"),
                @JsonSubTypes.Type(value = MetadataSourceProperties.class, name = "MetadataSourceProperties"),
                @JsonSubTypes.Type(value = NoteProperties.class, name = "NoteProperties"),
                @JsonSubTypes.Type(value = NoteLogProperties.class, name = "NoteLogProperties"),
                @JsonSubTypes.Type(value = PortProperties.class, name = "PortProperties"),
                @JsonSubTypes.Type(value = ProjectProperties.class, name = "ProjectProperties"),
                @JsonSubTypes.Type(value = SchemaElementProperties.class, name = "SchemaElementProperties"),
                @JsonSubTypes.Type(value = SecurityManagerProperties.class, name = "SecurityManagerProperties"),
                @JsonSubTypes.Type(value = SolutionComponentProperties.class, name = "SolutionComponentProperties"),
                @JsonSubTypes.Type(value = SubjectAreaProperties.class, name = "SubjectAreaProperties"),
                @JsonSubTypes.Type(value = ToDoProperties.class, name = "ToDoProperties"),
                @JsonSubTypes.Type(value = UserIdentityProperties.class, name = "UserIdentityProperties"),
                @JsonSubTypes.Type(value = SupplementaryProperties.class, name = "SupplementaryProperties"),
                @JsonSubTypes.Type(value = ValidValueProperties.class, name = "ValidValueProperties"),
                @JsonSubTypes.Type(value = ValidValueSetProperties.class, name = "ValidValueSetProperties"),
        })
public class ReferenceableProperties extends OpenMetadataRootProperties
{
    private String               qualifiedName        = null;
    private Map<String, String>  additionalProperties = null;
    private Map<String, String>  vendorProperties     = null;

    /**
     * Default constructor
     */
    public ReferenceableProperties()
    {
        super();
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
            additionalProperties = template.getAdditionalProperties();

            vendorProperties     = template.getVendorProperties();
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
     * Return specific properties for the data manager vendor.
     *
     * @return name value pairs
     */
    public Map<String, String> getVendorProperties()
    {
        return vendorProperties;
    }


    /**
     * Set up specific properties for the data manager vendor.
     *
     * @param vendorProperties name value pairs
     */
    public void setVendorProperties(Map<String, String> vendorProperties)
    {
        this.vendorProperties = vendorProperties;
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
                ", additionalProperties=" + additionalProperties +
                ", vendorProperties=" + vendorProperties +
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
                Objects.equals(additionalProperties, that.additionalProperties) &&
                Objects.equals(vendorProperties, that.vendorProperties);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), qualifiedName, additionalProperties, vendorProperties);
    }
}