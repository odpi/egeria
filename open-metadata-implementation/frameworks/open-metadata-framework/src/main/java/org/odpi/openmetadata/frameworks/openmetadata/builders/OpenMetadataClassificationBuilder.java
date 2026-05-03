/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.builders;

import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.KeyPattern;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetEncodingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.UserAccountProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.EditingCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.ScopingCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.StagingCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.BusinessSignificantProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.CyberLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.FixedLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.SecureLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.operatingplatforms.CloudPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.operatingplatforms.CloudProviderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.operatingplatforms.CloudServiceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.operatingplatforms.CloudTenantProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ExperimentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.CalculatedValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.PrimaryKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.TypeEmbeddedAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityListMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneMembershipProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Builds element properties for classification property beans.  The aim is to extract the properties from the
 * specialist classification beans.  It is only necessary to process beans that add new properties.
 */
public class OpenMetadataClassificationBuilder
{
    private final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Convert the properties beans into Element properties beans.  This is a bit fiddly to ensure the
     * effectivity dates are properly included for each classification.
     *
     * @param classificationPropertiesMap map of classification name to classification bean
     * @return map of classification name to new element properties
     */
    public Map<String, NewElementProperties> getInitialClassifications(Map<String, ClassificationProperties> classificationPropertiesMap)
    {
        if ((classificationPropertiesMap != null) && (!classificationPropertiesMap.isEmpty()))
        {
            Map<String, NewElementProperties> initialClassifications = new HashMap<>();

            for (String classificationName : classificationPropertiesMap.keySet())
            {
                if ((classificationName != null) && (classificationPropertiesMap.get(classificationName) != null))
                {
                    NewElementProperties newElementProperties = new NewElementProperties(this.getElementProperties(classificationPropertiesMap.get(classificationName)));

                    newElementProperties.setEffectiveFrom(classificationPropertiesMap.get(classificationName).getEffectiveFrom());
                    newElementProperties.setEffectiveTo(classificationPropertiesMap.get(classificationName).getEffectiveTo());

                    initialClassifications.put(classificationName, newElementProperties);
                }
            }

            return initialClassifications;
        }

        return null;
    }


    /**
     * Convert the properties beans into Element properties beans.  This is a bit fiddly in order to ensure the
     * effectivity dates are properly included for each classification.
     *
     * @param classificationProperties properties bean
     * @return new element properties
     */
    public NewElementProperties getNewElementProperties(ClassificationProperties classificationProperties)
    {
        if (classificationProperties != null)
        {
            NewElementProperties newElementProperties = new NewElementProperties(this.getElementProperties(classificationProperties));

            newElementProperties.setEffectiveFrom(classificationProperties.getEffectiveFrom());
            newElementProperties.setEffectiveTo(classificationProperties.getEffectiveTo());

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
    public ElementProperties getElementProperties(ClassificationProperties properties)
    {
        if (properties instanceof ClassificationElementProperties classificationElementProperties)
        {
            if ((classificationElementProperties.getPropertyValueMap() != null) && (!classificationElementProperties.getPropertyValueMap().isEmpty()))
            {
                ElementProperties elementProperties = new ElementProperties();

                elementProperties.setPropertyValueMap(classificationElementProperties.getPropertyValueMap());

                return elementProperties;
            }
        }
        else if (properties instanceof ClassificationBeanProperties classificationBeanProperties)
        {
            ElementProperties elementProperties = null;

            if (properties instanceof ActivityDescriptionProperties activityDescriptionProperties)
            {
                if (activityDescriptionProperties.getActivityType() != null)
                {
                    elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                       OpenMetadataProperty.ACTIVITY_TYPE.name,
                                                                       ActivityType.getOpenTypeName(),
                                                                       activityDescriptionProperties.getActivityType().name());
                }
            }
            else if (properties instanceof AnchorsProperties anchorsProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ANCHOR_GUID.name,
                                                                     anchorsProperties.getAnchorGUID());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ANCHOR_TYPE_NAME.name,
                                                                     anchorsProperties.getAnchorTypeName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ANCHOR_DOMAIN_NAME.name,
                                                                     anchorsProperties.getAnchorDomainName());

                elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                          OpenMetadataProperty.ANCHOR_SCOPE_GUIDS.name,
                                                                          anchorsProperties.getAnchorScopeGUIDs());

                elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                          OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                                          anchorsProperties.getZoneMembership());
            }
            else if (properties instanceof AuditLogProperties auditLogProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NOTES.name,
                                                                     auditLogProperties.getNotes());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.PROCESS.name,
                                                                     auditLogProperties.getProcess());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SOURCE.name,
                                                                     auditLogProperties.getSource());
            }
            else if (properties instanceof BusinessSignificantProperties businessSignificantProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     businessSignificantProperties.getDescription());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SCOPE.name,
                                                                     businessSignificantProperties.getScope());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.BUSINESS_CAPABILITY.name,
                                                                     businessSignificantProperties.getBusinessCapability());
            }
            else if (properties instanceof CalculatedValueProperties calculatedValueProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.FORMULA.name,
                                                                     calculatedValueProperties.getFormula());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.FORMULA_TYPE.name,
                                                                     calculatedValueProperties.getFormulaType());
            }
            else if (properties instanceof CanonicalVocabularyProperties canonicalVocabularyProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SCOPE.name,
                                                                     canonicalVocabularyProperties.getScope());
            }
            else if (properties instanceof CloudPlatformProperties cloudPlatformProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                     cloudPlatformProperties.getDeployedImplementationType());
            }
            else if (properties instanceof CloudProviderProperties cloudProviderProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.PROVIDER_NAME.name,
                                                                     cloudProviderProperties.getProviderName());
            }
            else if (properties instanceof CloudServiceProperties cloudServiceProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.OFFERING_NAME.name,
                                                                     cloudServiceProperties.getOfferingName());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SERVICE_TYPE.name,
                                                                     cloudServiceProperties.getServiceType());
            }
            else if (properties instanceof CloudTenantProperties cloudTenantProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.TENANT_NAME.name,
                                                                     cloudTenantProperties.getTenantName());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.TENANT_TYPE.name,
                                                                     cloudTenantProperties.getTenantType());
            }
            else if (properties instanceof ConsolidatedDuplicateProperties consolidatedDuplicateProperties)
            {
                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                                  consolidatedDuplicateProperties.getStatusIdentifier());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD.name,
                                                                     consolidatedDuplicateProperties.getSteward());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                     consolidatedDuplicateProperties.getStewardPropertyName());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                     consolidatedDuplicateProperties.getStewardTypeName());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SOURCE.name,
                                                                     consolidatedDuplicateProperties.getSource());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NOTES.name,
                                                                     consolidatedDuplicateProperties.getNotes());
            }
            else if (properties instanceof ContextDefinitionProperties contextDefinitionProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     contextDefinitionProperties.getDescription());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SCOPE.name,
                                                                     contextDefinitionProperties.getScope());
            }
            else if (properties instanceof CyberLocationProperties cyberLocationProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NETWORK_ADDRESS.name,
                                                                     cyberLocationProperties.getNetworkAddress());
            }
            else if (properties instanceof DigitalResourceOriginProperties digitalResourceOriginProperties)
            {
                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.OTHER_ORIGIN_VALUES.name,
                                                                        digitalResourceOriginProperties.getOtherOriginValues());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ORGANIZATION.name,
                                                                     digitalResourceOriginProperties.getOrganization());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ORGANIZATION_PROPERTY_NAME.name,
                                                                     digitalResourceOriginProperties.getOrganizationPropertyName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.BUSINESS_CAPABILITY.name,
                                                                     digitalResourceOriginProperties.getBusinessCapability());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.BUSINESS_CAPABILITY_PROPERTY_NAME.name,
                                                                     digitalResourceOriginProperties.getBusinessCapabilityPropertyName());
            }
            else if (properties instanceof DataAssetEncodingProperties dataAssetEncodingProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ENCODING_TYPE.name,
                                                                     dataAssetEncodingProperties.getEncodingType());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ENCODING_LANGUAGE.name,
                                                                     dataAssetEncodingProperties.getEncodingLanguage());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ENCODING_DESCRIPTION.name,
                                                                     dataAssetEncodingProperties.getEncodingDescription());
                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.ENCODING_PROPERTIES.name,
                                                                        dataAssetEncodingProperties.getEncodingProperties());
            }
            else if (properties instanceof DataScopeProperties dataScopeProperties)
            {
                elementProperties = propertyHelper.addFloatProperty(elementProperties,
                                                                    OpenMetadataProperty.MAX_LONGITUDE.name,
                                                                    dataScopeProperties.getMaxLongitude());
                elementProperties = propertyHelper.addFloatProperty(elementProperties,
                                                                    OpenMetadataProperty.MIN_LONGITUDE.name,
                                                                    dataScopeProperties.getMinLongitude());
                elementProperties = propertyHelper.addFloatProperty(elementProperties,
                                                                    OpenMetadataProperty.MAX_LATITUDE.name,
                                                                    dataScopeProperties.getMaxLatitude());
                elementProperties = propertyHelper.addFloatProperty(elementProperties,
                                                                    OpenMetadataProperty.MIN_LATITUDE.name,
                                                                    dataScopeProperties.getMinLatitude());
                elementProperties = propertyHelper.addFloatProperty(elementProperties,
                                                                    OpenMetadataProperty.MAX_HEIGHT.name,
                                                                    dataScopeProperties.getMaxHeight());
                elementProperties = propertyHelper.addFloatProperty(elementProperties,
                                                                    OpenMetadataProperty.MIN_HEIGHT.name,
                                                                    dataScopeProperties.getMinHeight());
                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.DATA_COLLECTION_START_TIME.name,
                                                                   dataScopeProperties.getDataCollectionStartTime());
                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.DATA_COLLECTION_END_TIME.name,
                                                                   dataScopeProperties.getDataCollectionEndTime());
                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.SCOPE_ELEMENTS.name,
                                                                        dataScopeProperties.getScopeElements());
                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                        dataScopeProperties.getAdditionalProperties());
            }
            else if (properties instanceof EditingCollectionProperties editingCollectionProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     editingCollectionProperties.getDescription());
            }
            else if (properties instanceof ExceptionBacklogProperties exceptionBacklogProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NOTES.name,
                                                                     exceptionBacklogProperties.getNotes());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.PROCESS.name,
                                                                     exceptionBacklogProperties.getProcess());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SOURCE.name,
                                                                     exceptionBacklogProperties.getSource());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD.name,
                                                                     exceptionBacklogProperties.getSteward());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                     exceptionBacklogProperties.getStewardTypeName());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                     exceptionBacklogProperties.getStewardPropertyName());
            }
            else if (properties instanceof ExecutionPointProperties executionPointProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                     executionPointProperties.getQualifiedName());
            }
            else if (properties instanceof ExperimentProperties experimentProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.HYPOTHESIS.name,
                                                                     experimentProperties.getHypothesis());
            }
            else if (properties instanceof FixedLocationProperties fixedLocationProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.COORDINATES.name,
                                                                     fixedLocationProperties.getCoordinates());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.MAP_PROJECTION.name,
                                                                     fixedLocationProperties.getMapProjection());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.TIME_ZONE.name,
                                                                     fixedLocationProperties.getTimeZone());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.POSTAL_ADDRESS.name,
                                                                     fixedLocationProperties.getPostalAddress());
            }
            else if (properties instanceof GovernedDataClassificationBase governedDataClassificationBase)
            {
                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                                  governedDataClassificationBase.getStatusIdentifier());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.CONFIDENCE.name,
                                                                  governedDataClassificationBase.getConfidence());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD.name,
                                                                     governedDataClassificationBase.getSteward());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                     governedDataClassificationBase.getStewardPropertyName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                     governedDataClassificationBase.getStewardTypeName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SOURCE.name,
                                                                     governedDataClassificationBase.getSource());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NOTES.name,
                                                                     governedDataClassificationBase.getNotes());

                if (properties instanceof ConfidenceProperties confidenceProperties)
                {
                    elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                      OpenMetadataProperty.CONFIDENCE_LEVEL.name,
                                                                      confidenceProperties.getConfidenceLevel());
                }
                else if (properties instanceof ConfidentialityProperties confidentialityProperties)
                {
                    elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                      OpenMetadataProperty.CONFIDENTIALITY_LEVEL.name,
                                                                      confidentialityProperties.getConfidentialityLevel());
                }
                else if (properties instanceof CriticalityProperties criticalityProperties)
                {
                    elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                      OpenMetadataProperty.CRITICALITY_LEVEL.name,
                                                                      criticalityProperties.getCriticalityLevel());
                }
                else if (properties instanceof ImpactProperties impactProperties)
                {
                    elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                      OpenMetadataProperty.SEVERITY_LEVEL.name,
                                                                      impactProperties.getSeverityLevel());
                }
                else if (properties instanceof RetentionProperties retentionProperties)
                {
                    elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                      OpenMetadataProperty.RETENTION_BASIS.name,
                                                                      retentionProperties.getRetentionBasis());

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.ASSOCIATED_GUID.name,
                                                                         retentionProperties.getAssociatedGUID());

                    elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                       OpenMetadataProperty.ARCHIVE_AFTER.name,
                                                                       retentionProperties.getArchiveAfter());

                    elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                       OpenMetadataProperty.DELETE_AFTER.name,
                                                                       retentionProperties.getDeleteAfter());
                }
            }
            else if (properties instanceof GovernanceExpectationsProperties governanceExpectationsProperties)
            {
                elementProperties = propertyHelper.addIntMapProperty(elementProperties,
                                                                     OpenMetadataProperty.COUNTS.name,
                                                                     governanceExpectationsProperties.getCounts());

                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.VALUES.name,
                                                                        governanceExpectationsProperties.getValues());

                elementProperties = propertyHelper.addBooleanMapProperty(elementProperties,
                                                                         OpenMetadataProperty.FLAGS.name,
                                                                         governanceExpectationsProperties.getFlags());

                elementProperties = propertyHelper.addDateMapProperty(elementProperties,
                                                                      OpenMetadataProperty.DATES.name,
                                                                      governanceExpectationsProperties.getDates());
            }
            else if (properties instanceof GovernanceMeasurementsProperties governanceMeasurementsProperties)
            {
                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.DATA_COLLECTION_START_TIME.name,
                                                                   governanceMeasurementsProperties.getDataCollectionStartTime());

                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.DATA_COLLECTION_END_TIME.name,
                                                                   governanceMeasurementsProperties.getDataCollectionEndTime());

                elementProperties = propertyHelper.addIntMapProperty(elementProperties,
                                                                     OpenMetadataProperty.COUNTS.name,
                                                                     governanceMeasurementsProperties.getCounts());

                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.VALUES.name,
                                                                        governanceMeasurementsProperties.getValues());

                elementProperties = propertyHelper.addBooleanMapProperty(elementProperties,
                                                                         OpenMetadataProperty.FLAGS.name,
                                                                         governanceMeasurementsProperties.getFlags());

                elementProperties = propertyHelper.addDateMapProperty(elementProperties,
                                                                      OpenMetadataProperty.DATES.name,
                                                                      governanceMeasurementsProperties.getDates());
            }
            else if (properties instanceof LineageLogProperties lineageLogProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NOTES.name,
                                                                     lineageLogProperties.getNotes());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.PROCESS.name,
                                                                     lineageLogProperties.getProcess());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SOURCE.name,
                                                                     lineageLogProperties.getSource());
            }
            else if (properties instanceof LogAnalysisProperties logAnalysisProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NOTES.name,
                                                                     logAnalysisProperties.getNotes());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.PROCESS.name,
                                                                     logAnalysisProperties.getProcess());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SOURCE.name,
                                                                     logAnalysisProperties.getSource());
                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.DATA_COLLECTION_START_TIME.name,
                                                                   logAnalysisProperties.getDataCollectionStartTime());
                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.DATA_COLLECTION_END_TIME.name,
                                                                   logAnalysisProperties.getDataCollectionEndTime());
                elementProperties = propertyHelper.addIntMapProperty(elementProperties,
                                                                   OpenMetadataProperty.COUNTS.name,
                                                                   logAnalysisProperties.getCounts());
                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                     OpenMetadataProperty.VALUES.name,
                                                                     logAnalysisProperties.getValues());
                elementProperties = propertyHelper.addBooleanMapProperty(elementProperties,
                                                                         OpenMetadataProperty.FLAGS.name,
                                                                         logAnalysisProperties.getFlags());
                elementProperties = propertyHelper.addDateMapProperty(elementProperties,
                                                                      OpenMetadataProperty.DATES.name,
                                                                      logAnalysisProperties.getDates());
            }
            else if (properties instanceof MementoProperties mementoProperties)
            {
                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.ARCHIVE_DATE.name,
                                                                     mementoProperties.getArchiveDate());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ARCHIVE_USER.name,
                                                                     mementoProperties.getArchiveUser());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ARCHIVE_PROCESS.name,
                                                                     mementoProperties.getArchiveProcess());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ARCHIVE_SERVICE.name,
                                                                     mementoProperties.getArchiveService());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ARCHIVE_METHOD.name,
                                                                     mementoProperties.getArchiveMethod());
                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.ARCHIVE_PROPERTIES.name,
                                                                        mementoProperties.getArchiveProperties());
            }
            else if (properties instanceof MeteringLogProperties meteringLogProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NOTES.name,
                                                                     meteringLogProperties.getNotes());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.PROCESS.name,
                                                                     meteringLogProperties.getProcess());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SOURCE.name,
                                                                     meteringLogProperties.getSource());
            }
            else if (properties instanceof OwnershipProperties ownershipProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.OWNER.name,
                                                                     ownershipProperties.getOwner());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.OWNER_TYPE_NAME.name,
                                                                     ownershipProperties.getOwnerTypeName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.OWNER_PROPERTY_NAME.name,
                                                                     ownershipProperties.getOwnerPropertyName());
                elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                          OpenMetadataProperty.USER_IDS.name,
                                                                          ownershipProperties.getUserIds());
            }
            else if (properties instanceof PolicyManagementPointProperties policyManagementPointProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.LABEL.name,
                                                                     policyManagementPointProperties.getLabel());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     policyManagementPointProperties.getDescription());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.POINT_TYPE.name,
                                                                     policyManagementPointProperties.getPointType());
            }
            else if (properties instanceof PrimaryKeyProperties primaryKeyProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DISPLAY_NAME.name,
                                                                     primaryKeyProperties.getDisplayName());

                if (primaryKeyProperties.getKeyPattern() != null)
                {
                    elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                       OpenMetadataProperty.KEY_PATTERN.name,
                                                                       KeyPattern.getOpenTypeName(),
                                                                       primaryKeyProperties.getKeyPattern().name());
                }

            }
            else if (properties instanceof ProjectClassificationProperties projectClassificationProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.APPROACH.name,
                                                                     projectClassificationProperties.getApproach());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.MANAGEMENT_STYLE.name,
                                                                     projectClassificationProperties.getManagementStyle());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.RESULTS_USAGE.name,
                                                                     projectClassificationProperties.getResultsUsage());
            }
            else if (properties instanceof ScopingCollectionProperties scopingCollectionProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     scopingCollectionProperties.getDescription());
            }
            else if (properties instanceof SecureLocationProperties secureLocationProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     secureLocationProperties.getDescription());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.LEVEL.name,
                                                                     secureLocationProperties.getLevel());
            }
            else if (properties instanceof SecurityListMembershipProperties securityListMembershipProperties)
            {
                elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                          OpenMetadataProperty.SECURITY_GROUPS.name,
                                                                          securityListMembershipProperties.getSecurityGroups());
            }
            else if (properties instanceof SecurityLogProperties securityLogProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NOTES.name,
                                                                     securityLogProperties.getNotes());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.PROCESS.name,
                                                                     securityLogProperties.getProcess());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SOURCE.name,
                                                                     securityLogProperties.getSource());
            }
            else if (properties instanceof SecurityTagsProperties securityTagsProperties)
            {
                elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                          OpenMetadataProperty.SECURITY_LABELS.name,
                                                                          securityTagsProperties.getSecurityLabels());

                elementProperties = propertyHelper.addMapProperty(elementProperties,
                                                                  OpenMetadataProperty.SECURITY_PROPERTIES.name,
                                                                  securityTagsProperties.getSecurityProperties());
            }
            else if (properties instanceof StagingCollectionProperties stagingCollectionProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     stagingCollectionProperties.getDescription());
            }
            else if (properties instanceof TaxonomyProperties taxonomyProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ORGANIZING_PRINCIPLE.name,
                                                                     taxonomyProperties.getOrganizingPrinciple());
            }
            else if (properties instanceof TemplateProperties templateProperties)
            {
                elementProperties = propertyHelper.addStringProperty(null,
                                                                     OpenMetadataProperty.DISPLAY_NAME.name,
                                                                     templateProperties.getDisplayName());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     templateProperties.getDescription());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.VERSION_IDENTIFIER.name,
                                                                     templateProperties.getVersionIdentifier());
                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                        templateProperties.getAdditionalProperties());
            }
            else if (properties instanceof TypeEmbeddedAttributeProperties typeEmbeddedAttributeProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SCHEMA_TYPE_NAME.name,
                                                                     typeEmbeddedAttributeProperties.getSchemaTypeName());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                     typeEmbeddedAttributeProperties.getQualifiedName());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DISPLAY_NAME.name,
                                                                     typeEmbeddedAttributeProperties.getDisplayName());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     typeEmbeddedAttributeProperties.getDescription());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.VERSION_IDENTIFIER.name,
                                                                     typeEmbeddedAttributeProperties.getVersionIdentifier());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.CATEGORY.name,
                                                                     typeEmbeddedAttributeProperties.getCategory());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.USAGE.name,
                                                                     typeEmbeddedAttributeProperties.getUsage());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ENCODING_STANDARD.name,
                                                                     typeEmbeddedAttributeProperties.getEncodingStandard());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NAMESPACE_PATH.name,
                                                                     typeEmbeddedAttributeProperties.getNamespacePath());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DATA_TYPE.name,
                                                                     typeEmbeddedAttributeProperties.getDataType());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DEFAULT_VALUE.name,
                                                                     typeEmbeddedAttributeProperties.getDefaultValue());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.FIXED_VALUE.name,
                                                                     typeEmbeddedAttributeProperties.getFixedValue());
                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                        typeEmbeddedAttributeProperties.getAdditionalProperties());

            }
            else if (properties instanceof UserAccountProfileProperties userAccountProfileProperties)
            {
                elementProperties = propertyHelper.addLongProperty(elementProperties,
                                                                   OpenMetadataProperty.USER_ACCOUNT_COUNT.name,
                                                                   userAccountProfileProperties.getUserAccountCount());
                elementProperties = propertyHelper.addLongProperty(elementProperties,
                                                                   OpenMetadataProperty.EMPLOYEE_ACCOUNT_COUNT.name,
                                                                   userAccountProfileProperties.getEmployeeAccountCount());
                elementProperties = propertyHelper.addLongProperty(elementProperties,
                                                                   OpenMetadataProperty.CONTRACTOR_ACCOUNT_COUNT.name,
                                                                   userAccountProfileProperties.getContractorAccountCount());
                elementProperties = propertyHelper.addLongProperty(elementProperties,
                                                                   OpenMetadataProperty.EXTERNAL_ACCOUNT_COUNT.name,
                                                                   userAccountProfileProperties.getExternalAccountCount());
                elementProperties = propertyHelper.addLongProperty(elementProperties,
                                                                   OpenMetadataProperty.DIGITAL_ACCOUNT_COUNT.name,
                                                                   userAccountProfileProperties.getDigitalAccountCount());
                elementProperties = propertyHelper.addLongProperty(elementProperties,
                                                                   OpenMetadataProperty.ACTIVE_ACCOUNT_COUNT.name,
                                                                   userAccountProfileProperties.getActiveAccountCount());
                elementProperties = propertyHelper.addLongProperty(elementProperties,
                                                                   OpenMetadataProperty.EXPIRED_ACCOUNT_COUNT.name,
                                                                   userAccountProfileProperties.getExpiredAccountCount());
                elementProperties = propertyHelper.addLongProperty(elementProperties,
                                                                   OpenMetadataProperty.LOCKED_ACCOUNT_COUNT.name,
                                                                   userAccountProfileProperties.getLockedAccountCount());
                elementProperties = propertyHelper.addLongProperty(elementProperties,
                                                                   OpenMetadataProperty.DISABLED_ACCOUNT_COUNT.name,
                                                                   userAccountProfileProperties.getDisabledAccountCount());
                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                   OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                   userAccountProfileProperties.getAdditionalProperties());

            }
            else if (properties instanceof ZoneMembershipProperties zoneMembershipProperties)
            {
                elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                          OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                                          zoneMembershipProperties.getZoneMembership());
            }
            else if (properties instanceof ZoneMembershipProfileProperties zoneMembershipProperties)
            {
                elementProperties = propertyHelper.addLongProperty(elementProperties,
                                                                      OpenMetadataProperty.TOTAL_MEMBERSHIP.name,
                                                                      zoneMembershipProperties.getTotalMembership());
                elementProperties = propertyHelper.addLongMapProperty(elementProperties,
                                                                      OpenMetadataProperty.TYPE_MEMBERSHIP.name,
                                                                      zoneMembershipProperties.getTypeMembership());
                elementProperties = propertyHelper.addLongProperty(elementProperties,
                                                                   OpenMetadataProperty.ANCHORED_TOTAL_MEMBERSHIP.name,
                                                                   zoneMembershipProperties.getAnchoredTotalMembership());
                elementProperties = propertyHelper.addLongMapProperty(elementProperties,
                                                                      OpenMetadataProperty.ANCHORED_TYPE_MEMBERSHIP.name,
                                                                      zoneMembershipProperties.getAnchoredTypeMembership());
                elementProperties = propertyHelper.addLongProperty(elementProperties,
                                                                   OpenMetadataProperty.ALL_TOTAL_MEMBERSHIP.name,
                                                                   zoneMembershipProperties.getAllTotalMembership());
                elementProperties = propertyHelper.addLongMapProperty(elementProperties,
                                                                      OpenMetadataProperty.ALL_TYPE_MEMBERSHIP.name,
                                                                      zoneMembershipProperties.getAllTypeMembership());
                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.ANALYSIS_TIME.name,
                                                                   zoneMembershipProperties.getAnalysisTime());
            }


            elementProperties = propertyHelper.addPropertyMap(elementProperties,
                                                              classificationBeanProperties.getExtendedProperties());

            return elementProperties;
        }

        return null;
    }
}
