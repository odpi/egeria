/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PeerDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetContentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.DeploymentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.ServerAssetUseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CommunityMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EmbeddedConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.DependentContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.RelatedContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataClassAssignmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.MemberDataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalServiceDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalServiceOperatorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalSupportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.OrganizationalCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.FeedbackProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermCategorization;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.ControlFlowProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.DataFlowProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.LineageMappingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.ProcessCallProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.AdjacentLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.AssetLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.NestedLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.ProfileLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectTeamProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.StakeholderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.ForeignKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.DatabaseForeignKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.AssociatedGroupProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.CapabilityDeploymentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementationResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.*;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationshipProperties provides the base class for relationships items.  This provides extended properties with the ability to
 * set effectivity dates.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AdjacentLocationProperties.class, name = "AdjacentLocationProperties"),
                @JsonSubTypes.Type(value = AssetConnectionProperties.class, name = "AssetConnectionProperties"),
                @JsonSubTypes.Type(value = AssetLocationProperties.class, name = "AssetLocationProperties"),
                @JsonSubTypes.Type(value = AssignmentScopeProperties.class, name = "AssignmentScopeProperties"),
                @JsonSubTypes.Type(value = AssociatedGroupProperties.class, name = "AssociatedGroupProperties"),
                @JsonSubTypes.Type(value = CapabilityDeploymentProperties.class, name = "CapabilityDeploymentProperties"),
                @JsonSubTypes.Type(value = CertificationProperties.class, name = "CertificationProperties"),
                @JsonSubTypes.Type(value = CollectionMembershipProperties.class, name = "CollectionMembershipProperties"),
                @JsonSubTypes.Type(value = CommunityMembershipProperties.class, name = "CommunityMembershipProperties"),
                @JsonSubTypes.Type(value = ControlFlowProperties.class, name = "ControlFlowProperties"),
                @JsonSubTypes.Type(value = DataSetContentProperties.class, name = "DataSetContentProperties"),
                @JsonSubTypes.Type(value = DataClassAssignmentProperties.class, name = "DataClassAssignmentProperties"),
                @JsonSubTypes.Type(value = DatabaseForeignKeyProperties.class, name = "DatabaseForeignKeyProperties"),
                @JsonSubTypes.Type(value = DataFlowProperties.class, name = "DataFlowProperties"),
                @JsonSubTypes.Type(value = DependentContextEventProperties.class, name = "DependentContextEventProperties"),
                @JsonSubTypes.Type(value = DeploymentProperties.class, name = "DeploymentProperties"),
                @JsonSubTypes.Type(value = DigitalServiceDependencyProperties.class, name = "DigitalServiceDependencyProperties"),
                @JsonSubTypes.Type(value = DigitalServiceOperatorProperties.class, name = "DigitalServiceOperatorProperties"),
                @JsonSubTypes.Type(value = DigitalSupportProperties.class, name = "DigitalSupportProperties"),
                @JsonSubTypes.Type(value = DuplicateProperties.class, name = "DuplicateProperties"),
                @JsonSubTypes.Type(value = EmbeddedConnectionProperties.class, name = "EmbeddedConnectionProperties"),
                @JsonSubTypes.Type(value = ExternalReferenceLinkProperties.class, name = "ExternalReferenceLinkProperties"),
                @JsonSubTypes.Type(value = FeedbackProperties.class, name = "FeedbackProperties"),
                @JsonSubTypes.Type(value = ForeignKeyProperties.class, name = "ForeignKeyProperties"),
                @JsonSubTypes.Type(value = GlossaryTermCategorization.class, name = "GlossaryTermCategorization"),
                @JsonSubTypes.Type(value = GlossaryTermRelationship.class, name = "GlossaryTermRelationship"),
                @JsonSubTypes.Type(value = GovernanceDefinitionMetricProperties.class, name = "GovernanceDefinitionMetricProperties"),
                @JsonSubTypes.Type(value = GovernanceResultsProperties.class, name = "GovernanceResultsProperties"),
                @JsonSubTypes.Type(value = ImplementedByProperties.class, name = "ImplementedByProperties"),
                @JsonSubTypes.Type(value = ImplementationResourceProperties.class, name = "ImplementationResourceProperties"),
                @JsonSubTypes.Type(value = InformationSupplyChainLinkProperties.class, name = "InformationSupplyChainLinkProperties"),
                @JsonSubTypes.Type(value = ITInfrastructureProfileProperties.class, name = "ITInfrastructureProfileProperties"),
                @JsonSubTypes.Type(value = ITProfileRoleAppointmentProperties.class, name = "ITProfileRoleAppointmentProperties"),
                @JsonSubTypes.Type(value = LicenseProperties.class, name = "LicenseProperties"),
                @JsonSubTypes.Type(value = LineageMappingProperties.class, name = "LineageMappingProperties"),
                @JsonSubTypes.Type(value = MemberDataFieldProperties.class, name = "MemberDataFieldProperties"),
                @JsonSubTypes.Type(value = NestedLocationProperties.class, name = "NestedLocationProperties"),
                @JsonSubTypes.Type(value = OrganizationalCapabilityProperties.class, name = "OrganizationalCapabilityProperties"),
                @JsonSubTypes.Type(value = PeerProperties.class, name = "PeerProperties"),
                @JsonSubTypes.Type(value = PeerDefinitionProperties.class, name = "PeerDefinitionProperties"),
                @JsonSubTypes.Type(value = PersonRoleAppointmentProperties.class, name = "PersonRoleAppointmentProperties"),
                @JsonSubTypes.Type(value = ProcessCallProperties.class, name = "ProcessCallProperties"),
                @JsonSubTypes.Type(value = ProcessHierarchyProperties.class, name = "ProcessHierarchyProperties"),
                @JsonSubTypes.Type(value = ProfileIdentityProperties.class, name = "ProfileIdentityProperties"),
                @JsonSubTypes.Type(value = ProfileLocationProperties.class, name = "ProfileLocationProperties"),
                @JsonSubTypes.Type(value = ProjectTeamProperties.class, name = "ProjectTeamProperties"),
                @JsonSubTypes.Type(value = ReferenceValueAssignmentProperties.class, name = "ReferenceValueAssignmentProperties"),
                @JsonSubTypes.Type(value = RelatedContextEventProperties.class, name = "RelatedContextEventProperties"),
                @JsonSubTypes.Type(value = ResourceListProperties.class, name = "ResourceListProperties"),
                @JsonSubTypes.Type(value = SchemaAttributeRelationshipProperties.class, name = "SchemaAttributeRelationshipProperties"),
                @JsonSubTypes.Type(value = SemanticAssignmentProperties.class, name = "SemanticAssignmentProperties"),
                @JsonSubTypes.Type(value = StakeholderProperties.class, name = "StakeholderProperties"),
                @JsonSubTypes.Type(value = ServerAssetUseProperties.class, name = "ServerAssetUseProperties"),
                @JsonSubTypes.Type(value = SupportingDefinitionProperties.class, name = "SupportingDefinitionProperties"),
                @JsonSubTypes.Type(value = TeamLeadershipProperties.class, name = "TeamLeadershipProperties"),
                @JsonSubTypes.Type(value = TeamMembershipProperties.class, name = "TeamMembershipProperties"),
                @JsonSubTypes.Type(value = TeamStructureProperties.class, name = "TeamStructureProperties"),
                @JsonSubTypes.Type(value = TeamRoleAppointmentProperties.class, name = "TeamRoleAppointmentProperties"),
                @JsonSubTypes.Type(value = ValidValueAssignmentProperties.class, name = "ValidValueAssignmentProperties"),
                @JsonSubTypes.Type(value = ValidValueMembershipProperties.class, name = "ValidValueMembershipProperties"),
                @JsonSubTypes.Type(value = ValidValuesImplProperties.class, name = "ValidValuesImplProperties"),
                @JsonSubTypes.Type(value = ValidValuesMappingProperties.class, name = "ValidValuesMappingProperties"),

        })
public class RelationshipProperties
{
    private Date                 effectiveFrom        = null;
    private Date                 effectiveTo          = null;
    private String               typeName             = null;
    private Map<String, Object>  extendedProperties   = null;

    /**
     * Default constructor
     */
    public RelationshipProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public RelationshipProperties(RelationshipProperties template)
    {
        if (template != null)
        {
            effectiveFrom        = template.getEffectiveFrom();
            effectiveTo          = template.getEffectiveTo();
            typeName             = template.getTypeName();
            extendedProperties   = template.getExtendedProperties();
        }
    }


    /**
     * Return the date/time that this element is effective from (null means effective from the epoch).
     *
     * @return date object
     */
    public Date getEffectiveFrom()
    {
        return effectiveFrom;
    }


    /**
     * Set up the date/time that this element is effective from (null means effective from the epoch).
     *
     * @param effectiveFrom date object
     */
    public void setEffectiveFrom(Date effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }


    /**
     * Return the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @return date object
     */
    public Date getEffectiveTo()
    {
        return effectiveTo;
    }


    /**
     * Set the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @param effectiveTo date object
     */
    public void setEffectiveTo(Date effectiveTo)
    {
        this.effectiveTo = effectiveTo;
    }


    /**
     * Return the name of the open metadata type for this metadata element.
     *
     * @return string name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the name of the open metadata type for this element.
     *
     * @param typeName string name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return the properties that have been defined for a subtype of this object that are not supported explicitly
     * by this bean.
     *
     * @return property map
     */
    public Map<String, Object> getExtendedProperties()
    {
        return extendedProperties;
    }


    /**
     * Set up the properties that have been defined for a subtype of this object that are not supported explicitly
     * by this bean.
     *
     * @param extendedProperties property map
     */
    public void setExtendedProperties(Map<String, Object> extendedProperties)
    {
        this.extendedProperties = extendedProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RelationshipProperties{" +
                "effectiveFrom=" + effectiveFrom +
                ", effectiveTo=" + effectiveTo +
                ", typeName='" + typeName + '\'' +
                ", extendedProperties=" + extendedProperties +
                '}';
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        RelationshipProperties that = (RelationshipProperties) objectToCompare;
        return Objects.equals(effectiveFrom, that.effectiveFrom) &&
                Objects.equals(effectiveTo, that.effectiveTo) &&
                Objects.equals(typeName, that.typeName) &&
                Objects.equals(extendedProperties, that.extendedProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(effectiveFrom, effectiveTo, typeName, extendedProperties);
    }
}