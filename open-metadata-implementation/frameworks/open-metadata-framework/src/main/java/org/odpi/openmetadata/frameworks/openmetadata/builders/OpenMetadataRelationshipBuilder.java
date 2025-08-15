/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.builders;

import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetContentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ImpactedResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EmbeddedConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventImpactProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.DependentContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.RelatedContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataClassAssignmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.MemberDataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.NestedDataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.CitedDocumentLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.MediaReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementationResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.ProfileLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintCompositionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentActorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

/**
 * Builds element properties for relationship property beans
 */
public class OpenMetadataRelationshipBuilder
{
    private final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Convert the properties beans into Element properties beans.  This is a bit fiddly in order to ensure the
     * effectivity dates are properly included for each relationship.
     *
     * @param relationshipProperties properties bean
     * @return new element properties
     */
    public NewElementProperties getNewElementProperties(RelationshipProperties relationshipProperties)
    {
        if (relationshipProperties != null)
        {
            NewElementProperties newElementProperties = new NewElementProperties(this.getElementProperties(relationshipProperties));

            newElementProperties.setEffectiveFrom(relationshipProperties.getEffectiveFrom());
            newElementProperties.setEffectiveTo(relationshipProperties.getEffectiveTo());

            return newElementProperties;
        }

        return null;
    }


    /**
     * Convert the specific properties into a set of element properties for the open metadata client.
     *
     * @param properties supplied properties
     * @return element properties
     */
    public ElementProperties getElementProperties(RelationshipProperties properties)
    {
        if (properties == null)
        {
            return null;
        }

        ElementProperties elementProperties = null;

        if (properties instanceof RelationshipElementProperties relationshipElementProperties)
        {
            if ((relationshipElementProperties.getPropertyValueMap() != null) && (!relationshipElementProperties.getPropertyValueMap().isEmpty()))
            {
                elementProperties = new ElementProperties();

                elementProperties.setPropertyValueMap(relationshipElementProperties.getPropertyValueMap());

                return elementProperties;
            }
        }
        else if (properties instanceof RelationshipBeanProperties relationshipBeanProperties)
        {
            if (properties instanceof AgreementActorProperties agreementActorProperties)
            {
                elementProperties =  propertyHelper.addStringProperty(elementProperties,
                                                                      OpenMetadataProperty.ACTOR_NAME.name,
                                                                      agreementActorProperties.getActorName());
            }
            else if (properties instanceof AgreementItemProperties agreementItemProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.AGREEMENT_ITEM_ID.name,
                                                                     agreementItemProperties.getAgreementItemId());
                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.AGREEMENT_START.name,
                                                                   agreementItemProperties.getAgreementStart());
                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.AGREEMENT_END.name,
                                                                   agreementItemProperties.getAgreementEnd());
                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.ENTITLEMENTS.name,
                                                                        agreementItemProperties.getEntitlements());
                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.RESTRICTIONS.name,
                                                                        agreementItemProperties.getRestrictions());
                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.OBLIGATIONS.name,
                                                                        agreementItemProperties.getObligations());
                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.USAGE_MEASUREMENTS.name,
                                                                        agreementItemProperties.getUsageMeasurements());
            }
            else if (properties instanceof AssignmentScopeProperties assignmentScopeProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ASSIGNMENT_TYPE.name,
                                                                     assignmentScopeProperties.getAssignmentType());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     assignmentScopeProperties.getDescription());
            }
            else if (properties instanceof AttributeForSchemaProperties attributeForSchemaProperties)
            {
                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.POSITION.name,
                                                                  attributeForSchemaProperties.getPosition());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.MAX_CARDINALITY.name,
                                                                  attributeForSchemaProperties.getMaxCardinality());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.MIN_CARDINALITY.name,
                                                                  attributeForSchemaProperties.getMaxCardinality());
            }
            else if (properties instanceof CertificationProperties certificationProperties)
            {
                elementProperties = propertyHelper.addStringProperty(null,
                                                                     OpenMetadataProperty.CERTIFICATE_GUID.name,
                                                                     certificationProperties.getCertificateId());

                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.COVERAGE_START.name,
                                                                   certificationProperties.getStartDate());

                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.COVERAGE_END.name,
                                                                   certificationProperties.getEndDate());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.CONDITIONS.name,
                                                                     certificationProperties.getConditions());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.CERTIFIED_BY.name,
                                                                     certificationProperties.getCertifiedBy());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.CERTIFIED_BY_TYPE_NAME.name,
                                                                     certificationProperties.getCertifiedByTypeName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.CERTIFIED_BY_PROPERTY_NAME.name,
                                                                     certificationProperties.getCertifiedByPropertyName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.CUSTODIAN.name,
                                                                     certificationProperties.getCustodian());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.CUSTODIAN_TYPE_NAME.name,
                                                                     certificationProperties.getCustodianTypeName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.CUSTODIAN_PROPERTY_NAME.name,
                                                                     certificationProperties.getCustodianPropertyName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.RECIPIENT.name,
                                                                     certificationProperties.getRecipient());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.RECIPIENT_TYPE_NAME.name,
                                                                     certificationProperties.getRecipientTypeName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.RECIPIENT_PROPERTY_NAME.name,
                                                                     certificationProperties.getRecipientPropertyName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NOTES.name,
                                                                     certificationProperties.getNotes());
            }
            else if (properties instanceof CitedDocumentLinkProperties citedDocumentLinkProperties)
            {
                elementProperties = propertyHelper.addStringProperty(null,
                                                                     OpenMetadataProperty.REFERENCE_ID.name,
                                                                     citedDocumentLinkProperties.getReferenceId());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     citedDocumentLinkProperties.getDescription());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.PAGES.name,
                                                                     citedDocumentLinkProperties.getPages());
            }
            else if (properties instanceof CollectionMembershipProperties collectionMembershipProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.MEMBERSHIP_RATIONALE.name,
                                                                     collectionMembershipProperties.getMembershipRationale());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.EXPRESSION.name,
                                                                     collectionMembershipProperties.getExpression());

                if (collectionMembershipProperties.getStatus() != null)
                {
                    elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                       OpenMetadataProperty.MEMBERSHIP_STATUS.name,
                                                                       CollectionMemberStatus.getOpenTypeName(),
                                                                       collectionMembershipProperties.getStatus().getName());
                }

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                     collectionMembershipProperties.getUserDefinedStatus());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.CONFIDENCE.name,
                                                                  collectionMembershipProperties.getConfidence());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD.name,
                                                                     collectionMembershipProperties.getSteward());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                     collectionMembershipProperties.getStewardTypeName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                     collectionMembershipProperties.getStewardPropertyName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SOURCE.name,
                                                                     collectionMembershipProperties.getSource());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NOTES.name,
                                                                     collectionMembershipProperties.getNotes());
            }
            else if (properties instanceof ContextEventImpactProperties contextEventImpactProperties)
            {
                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.SEVERITY_LEVEL_IDENTIFIER.name,
                                                                  contextEventImpactProperties.getSeverityLevelIdentifier());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     contextEventImpactProperties.getDescription());
            }
            else if (properties instanceof ContractLinkProperties contractLinkProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.CONTRACT_ID.name,
                                                                     contractLinkProperties.getContractId());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.CONTRACT_LIAISON.name,
                                                                     contractLinkProperties.getContractLiaison());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.CONTRACT_LIAISON_TYPE_NAME.name,
                                                                     contractLinkProperties.getContractLiaisonTypeName());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.CONTRACT_LIAISON_PROPERTY_NAME.name,
                                                                     contractLinkProperties.getContractLiaisonPropertyName());
            }
            else if (properties instanceof DataClassAssignmentProperties dataClassAssignmentProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.METHOD.name,
                                                                     dataClassAssignmentProperties.getMethod());

                if (dataClassAssignmentProperties.getStatus() != null)
                {
                    elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                       OpenMetadataProperty.DATA_CLASS_ASSIGNMENT_STATUS.name,
                                                                       DataClassAssignmentStatus.getOpenTypeName(),
                                                                       dataClassAssignmentProperties.getStatus().getName());
                }

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.CONFIDENCE.name,
                                                                  dataClassAssignmentProperties.getConfidence());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.THRESHOLD.name,
                                                                  dataClassAssignmentProperties.getThreshold());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD.name,
                                                                     dataClassAssignmentProperties.getSteward());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                     dataClassAssignmentProperties.getStewardTypeName());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                     dataClassAssignmentProperties.getStewardPropertyName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SOURCE.name,
                                                                     dataClassAssignmentProperties.getSource());
            }
            else if (properties instanceof DataSetContentProperties dataSetContentProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.QUERY_ID.name,
                                                                     dataSetContentProperties.getQueryId());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.QUERY.name,
                                                                     dataSetContentProperties.getQuery());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.QUERY_TYPE.name,
                                                                     dataSetContentProperties.getQueryType());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                     dataSetContentProperties.getISCQualifiedName());
            }
            else if (properties instanceof DependentContextEventProperties dependentContextEventProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     dependentContextEventProperties.getDescription());
            }
            else if (properties instanceof DerivedSchemaTypeQueryTargetProperties derivedSchemaTypeQueryTargetProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.QUERY_ID.name,
                                                                     derivedSchemaTypeQueryTargetProperties.getQueryId());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.QUERY.name,
                                                                     derivedSchemaTypeQueryTargetProperties.getQuery());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.QUERY_TYPE.name,
                                                                     derivedSchemaTypeQueryTargetProperties.getQueryType());
            }
            else if (properties instanceof DigitalProductDependencyProperties digitalProductDependencyProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     digitalProductDependencyProperties.getDescription());
            }
            else if (properties instanceof DigitalSubscriberProperties digitalSubscriberProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SUBSCRIBER_ID.name,
                                                                     digitalSubscriberProperties.getSubscriberId());
            }
            else if (properties instanceof EmbeddedConnectionProperties embeddedConnectionProperties)
            {
                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.POSITION.name,
                                                                  embeddedConnectionProperties.getPosition());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DISPLAY_NAME.name,
                                                                     embeddedConnectionProperties.getDisplayName());

                elementProperties = propertyHelper.addMapProperty(elementProperties,
                                                                  OpenMetadataProperty.ARGUMENTS.name,
                                                                  embeddedConnectionProperties.getArguments());
            }
            else if (properties instanceof ExternalIdLinkProperties externalIdLinkProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.USAGE.name,
                                                                     externalIdLinkProperties.getUsage());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SOURCE.name,
                                                                     externalIdLinkProperties.getSource());

                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.LAST_SYNCHRONIZED.name,
                                                                   externalIdLinkProperties.getLastSynchronized());

                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.MAPPING_PROPERTIES.name,
                                                                        externalIdLinkProperties.getMappingProperties());
            }
            else if (properties instanceof ExternalIdScopeProperties externalIdScopeProperties)
            {
                if (externalIdScopeProperties.getSynchronizationDirection() != null)
                {
                    elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                       OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name,
                                                                       PermittedSynchronization.getOpenTypeName(),
                                                                       externalIdScopeProperties.getSynchronizationDirection().getName());
                }
            }
            else if (properties instanceof ExternalReferenceLinkProperties externalReferenceLinkProperties)
            {
                elementProperties = propertyHelper.addStringProperty(null,
                                                                     OpenMetadataProperty.LABEL.name,
                                                                     externalReferenceLinkProperties.getLabel());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     externalReferenceLinkProperties.getDescription());
            }
            else if (properties instanceof ForeignKeyProperties foreignKeyProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DISPLAY_NAME.name,
                                                                     foreignKeyProperties.getDisplayName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     foreignKeyProperties.getDescription());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                     OpenMetadataProperty.CONFIDENCE.name,
                                                                     foreignKeyProperties.getConfidence());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD.name,
                                                                     foreignKeyProperties.getSteward());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SOURCE.name,
                                                                     foreignKeyProperties.getSource());

            }
            else if (properties instanceof GlossaryTermRelationship glossaryTermRelationship)
            {
                elementProperties = propertyHelper.addStringProperty(null,
                                                                     OpenMetadataProperty.EXPRESSION.name,
                                                                     glossaryTermRelationship.getExpression());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.CONFIDENCE.name,
                                                                  glossaryTermRelationship.getConfidence());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     glossaryTermRelationship.getDescription());

                if (glossaryTermRelationship.getStatus() != null)
                {
                    elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                       OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name,
                                                                       GlossaryTermRelationshipStatus.getOpenTypeName(),
                                                                       glossaryTermRelationship.getStatus().getName());
                }

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD.name,
                                                                     glossaryTermRelationship.getSteward());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SOURCE.name,
                                                                     glossaryTermRelationship.getSource());
            }
            else if (properties instanceof GovernanceResultsProperties governanceResultsProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.QUERY.name,
                                                                     governanceResultsProperties.getQuery());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.QUERY_TYPE.name,
                                                                     governanceResultsProperties.getQueryType());
            }
            else if (properties instanceof GovernedByProperties governedByProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.LABEL.name,
                                                                     governedByProperties.getLabel());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     governedByProperties.getDescription());
            }
            else if (properties instanceof GraphEdgeLinkProperties graphEdgeLinkProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.LINK_TYPE_NAME.name,
                                                                     graphEdgeLinkProperties.getLinkTypeName());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.RELATIONSHIP_END.name,
                                                                  graphEdgeLinkProperties.getRelationshipEnd());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.RELATIONSHIP_END_NAME.name,
                                                                     graphEdgeLinkProperties.getRelationshipEndName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     graphEdgeLinkProperties.getDescription());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.MAX_CARDINALITY.name,
                                                                  graphEdgeLinkProperties.getMaxCardinality());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.MIN_CARDINALITY.name,
                                                                  graphEdgeLinkProperties.getMinCardinality());

                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                        graphEdgeLinkProperties.getAdditionalProperties());
            }
            else if (properties instanceof ImpactedResourceProperties impactedResourceProperties)
            {
                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.SEVERITY_LEVEL_IDENTIFIER.name,
                                                                  impactedResourceProperties.getSeverityLevelIdentifier());
            }
            else if (properties instanceof ImplementationResourceProperties implementationResourceProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     implementationResourceProperties.getDescription());
            }
            else if (properties instanceof ImplementedByProperties implementedByProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESIGN_STEP.name,
                                                                     implementedByProperties.getDesignStep());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ROLE.name,
                                                                     implementedByProperties.getRole());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.TRANSFORMATION.name,
                                                                     implementedByProperties.getTransformation());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     implementedByProperties.getDescription());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                     implementedByProperties.getISCQualifiedName());
            }
            else if (properties instanceof InformationSupplyChainLinkProperties informationSupplyChainLinkProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.LABEL.name,
                                                                     informationSupplyChainLinkProperties.getLabel());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     informationSupplyChainLinkProperties.getDescription());
            }
            else if (properties instanceof LicenseProperties licenseProperties)
            {
                elementProperties = propertyHelper.addStringProperty(null,
                                                                     OpenMetadataProperty.LICENSE_GUID.name,
                                                                     licenseProperties.getLicenseId());

                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.COVERAGE_START.name,
                                                                   licenseProperties.getStartDate());

                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.COVERAGE_END.name,
                                                                   licenseProperties.getEndDate());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.CONDITIONS.name,
                                                                     licenseProperties.getConditions());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.LICENSED_BY.name,
                                                                     licenseProperties.getLicensedBy());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.LICENSED_BY_TYPE_NAME.name,
                                                                     licenseProperties.getLicensedByTypeName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.LICENSED_BY_PROPERTY_NAME.name,
                                                                     licenseProperties.getLicensedByPropertyName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.CUSTODIAN.name,
                                                                     licenseProperties.getCustodian());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.CUSTODIAN_TYPE_NAME.name,
                                                                     licenseProperties.getCustodianTypeName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.CUSTODIAN_PROPERTY_NAME.name,
                                                                     licenseProperties.getCustodianPropertyName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.LICENSEE.name,
                                                                     licenseProperties.getLicensee());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.LICENSEE_TYPE_NAME.name,
                                                                     licenseProperties.getLicenseeTypeName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.LICENSEE_PROPERTY_NAME.name,
                                                                     licenseProperties.getLicenseePropertyName());

                return propertyHelper.addStringProperty(elementProperties,
                                                        OpenMetadataProperty.NOTES.name,
                                                        licenseProperties.getNotes());
            }
            else if (properties instanceof MediaReferenceProperties mediaReferenceProperties)
            {
                elementProperties = propertyHelper.addStringProperty(null,
                                                                     OpenMetadataProperty.MEDIA_ID.name,
                                                                     mediaReferenceProperties.getMediaId());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     mediaReferenceProperties.getDescription());

                if (mediaReferenceProperties.getMediaUsage() != null)
                {
                    elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                       OpenMetadataProperty.MEDIA_USAGE.name,
                                                                       MediaUsage.getOpenTypeName(),
                                                                       mediaReferenceProperties.getMediaUsage().getName());
                }

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.MEDIA_USAGE_OTHER_ID.name,
                                                                     mediaReferenceProperties.getMediaUsageOtherId());
            }
            else if (properties instanceof MemberDataFieldProperties memberDataFieldProperties)
            {
                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.POSITION.name,
                                                                  memberDataFieldProperties.getPosition());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.MAX_CARDINALITY.name,
                                                                  memberDataFieldProperties.getMaxCardinality());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.MIN_CARDINALITY.name,
                                                                  memberDataFieldProperties.getMaxCardinality());
            }
            else if (properties instanceof NestedDataFieldProperties nestedDataFieldProperties)
            {
                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.POSITION.name,
                                                                  nestedDataFieldProperties.getPosition());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.MAX_CARDINALITY.name,
                                                                  nestedDataFieldProperties.getMaxCardinality());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.MIN_CARDINALITY.name,
                                                                  nestedDataFieldProperties.getMaxCardinality());
            }
            else if (properties instanceof NestedSchemaAttributeProperties nestedSchemaAttributeProperties)
            {
                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.POSITION.name,
                                                                  nestedSchemaAttributeProperties.getPosition());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.MAX_CARDINALITY.name,
                                                                  nestedSchemaAttributeProperties.getMaxCardinality());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.MIN_CARDINALITY.name,
                                                                  nestedSchemaAttributeProperties.getMaxCardinality());
            }
            else if (properties instanceof PeerDefinitionProperties peerDefinitionProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     peerDefinitionProperties.getDescription());
            }
            else if (properties instanceof PersonRoleAppointmentProperties personRoleAppointmentProperties)
            {
                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.EXPECTED_TIME_ALLOCATION_PERCENT.name,
                                                                  personRoleAppointmentProperties.getExpectedTimeAllocationPercent());
            }
            else if (properties instanceof ProfileIdentityProperties profileIdentityProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ROLE_TYPE_NAME.name,
                                                                     profileIdentityProperties.getRoleTypeName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ROLE_GUID.name,
                                                                     profileIdentityProperties.getRoleGUID());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     profileIdentityProperties.getDescription());
            }
            else if (properties instanceof ProfileLocationProperties profileLocationProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ASSOCIATION_TYPE.name,
                                                                     profileLocationProperties.getAssociationType());
            }
            else if (properties instanceof ProjectDependencyProperties projectDependencyProperties)
            {
                elementProperties = propertyHelper.addStringProperty(null,
                                                                     OpenMetadataProperty.DEPENDENCY_SUMMARY.name,
                                                                     projectDependencyProperties.getDependencySummary());
            }
            else if (properties instanceof ReferenceValueAssignmentProperties referenceValueAssignmentProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                  OpenMetadataProperty.ATTRIBUTE_NAME.name,
                                                                  referenceValueAssignmentProperties.getAttributeName());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.CONFIDENCE.name,
                                                                  referenceValueAssignmentProperties.getConfidence());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD.name,
                                                                     referenceValueAssignmentProperties.getSteward());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                     referenceValueAssignmentProperties.getStewardTypeName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                     referenceValueAssignmentProperties.getStewardPropertyName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NOTES.name,
                                                                     referenceValueAssignmentProperties.getNotes());
            }
            else if (properties instanceof RelatedContextEventProperties relatedContextEventProperties)
            {
                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                                  relatedContextEventProperties.getStatusIdentifier());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.CONFIDENCE.name,
                                                                  relatedContextEventProperties.getConfidence());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD.name,
                                                                     relatedContextEventProperties.getSteward());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                     relatedContextEventProperties.getStewardTypeName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                     relatedContextEventProperties.getStewardPropertyName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SOURCE.name,
                                                                     relatedContextEventProperties.getSource());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NOTES.name,
                                                                     relatedContextEventProperties.getNotes());
            }
            else if (properties instanceof ReportDependencyProperties reportDependencyProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.LABEL.name,
                                                                     reportDependencyProperties.getLabel());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     reportDependencyProperties.getDescription());
            }
            else if (properties instanceof ResourceListProperties resourceListProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.RESOURCE_USE.name,
                                                                     resourceListProperties.getResourceUse());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.RESOURCE_USE_DESCRIPTION.name,
                                                                     resourceListProperties.getResourceUse());
                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.RESOURCE_USE_PROPERTIES.name,
                                                                        resourceListProperties.getResourceUseProperties());
                elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                      OpenMetadataProperty.WATCH_RESOURCE.name,
                                                                      resourceListProperties.getWatchResource());
            }
            else if (properties instanceof SemanticAssignmentProperties semanticAssignmentProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.EXPRESSION.name,
                                                                     semanticAssignmentProperties.getExpression());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.CONFIDENCE.name,
                                                                  semanticAssignmentProperties.getConfidence());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     semanticAssignmentProperties.getDescription());

                if (semanticAssignmentProperties.getStatus() != null)
                {
                    elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                       OpenMetadataProperty.TERM_ASSIGNMENT_STATUS.name,
                                                                       GlossaryTermAssignmentStatus.getOpenTypeName(),
                                                                       semanticAssignmentProperties.getStatus().getName());
                }

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD.name,
                                                                     semanticAssignmentProperties.getSteward());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                     semanticAssignmentProperties.getStewardTypeName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                     semanticAssignmentProperties.getStewardPropertyName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SOURCE.name,
                                                                     semanticAssignmentProperties.getSource());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NOTES.name,
                                                                     semanticAssignmentProperties.getNotes());
            }
            else if (properties instanceof SolutionBlueprintCompositionProperties solutionBlueprintCompositionProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ROLE.name,
                                                                     solutionBlueprintCompositionProperties.getRole());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     solutionBlueprintCompositionProperties.getDescription());
            }
            else if (properties instanceof SolutionComponentActorProperties solutionComponentActorProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ROLE.name,
                                                                     solutionComponentActorProperties.getRole());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     solutionComponentActorProperties.getDescription());
            }
            else if (properties instanceof SolutionLinkingWireProperties solutionLinkingWireProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.LABEL.name,
                                                                     solutionLinkingWireProperties.getLabel());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     solutionLinkingWireProperties.getDescription());

                elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                          OpenMetadataProperty.ISC_QUALIFIED_NAMES.name,
                                                                          solutionLinkingWireProperties.getISCQualifiedNames());
            }
            else if (properties instanceof SupportingDefinitionProperties supportingDefinitionProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.RATIONALE.name,
                                                                     supportingDefinitionProperties.getRationale());
            }
            else if (properties instanceof TeamRoleAppointmentProperties teamRoleAppointmentProperties)
            {
                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.EXPECTED_TIME_ALLOCATION_PERCENT.name,
                                                                  teamRoleAppointmentProperties.getExpectedTimeAllocationPercent());
            }
            else if (properties instanceof TeamStructureProperties teamStructureProperties)
            {
                elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                      OpenMetadataProperty.DELEGATION_ESCALATION.name,
                                                                      teamStructureProperties.getDelegationEscalationAuthority());
            }
            else if (properties instanceof ActionTargetProperties actionTargetProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                                     actionTargetProperties.getActionTargetName());

                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.START_TIME.name,
                                                                   actionTargetProperties.getStartTime());

                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.COMPLETION_TIME.name,
                                                                   actionTargetProperties.getCompletionTime());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.COMPLETION_MESSAGE.name,
                                                                     actionTargetProperties.getCompletionMessage());

                if (actionTargetProperties.getStatus() != null)
                {
                    elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                       OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                                       ActivityStatus.getOpenTypeName(),
                                                                       actionTargetProperties.getStatus().getName());
                }

            }
            else if (properties instanceof ValidValueAssignmentProperties validValueAssignmentProperties)
            {
                elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                     OpenMetadataProperty.STRICT_REQUIREMENT.name,
                                                                     validValueAssignmentProperties.getStrictRequirement());
            }
            else if (properties instanceof ValidValuesImplementationProperties validValuesImplementationProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SYMBOLIC_NAME.name,
                                                                     validValuesImplementationProperties.getSymbolicName());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.IMPLEMENTATION_VALUE.name,
                                                                     validValuesImplementationProperties.getImplementationValue());
                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.ADDITIONAL_VALUES.name,
                                                                        validValuesImplementationProperties.getAdditionalValues());
            }
            else if (properties instanceof ValidValuesMappingProperties validValuesMappingProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ASSOCIATION_DESCRIPTION.name,
                                                                     validValuesMappingProperties.getAssociationDescription());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.CONFIDENCE.name,
                                                                  validValuesMappingProperties.getConfidence());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD.name,
                                                                     validValuesMappingProperties.getSteward());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                     validValuesMappingProperties.getStewardTypeName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                     validValuesMappingProperties.getStewardPropertyName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NOTES.name,
                                                                     validValuesMappingProperties.getNotes());
            }
            else if (properties instanceof ValidValueMemberProperties validValueMemberProperties)
            {
                elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                      OpenMetadataProperty.IS_DEFAULT_VALUE.name,
                                                                      validValueMemberProperties.getDefaultValue());
            }

            elementProperties = propertyHelper.addPropertyMap(elementProperties,
                                                              relationshipBeanProperties.getExtendedProperties());
        }

        return elementProperties;
    }
}
