/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.builders;

import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermActivityType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.KeyPattern;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AnchorsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetEncodingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.CalculatedValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.PrimaryKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.TypeEmbeddedAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Builds element properties for classification property beans
 */
public class OpenMetadataClassificationBuilder
{
    private final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Convert the properties beans into Element properties beans.  This is a bit fiddly in order to ensure the
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

            for (String classificationName: classificationPropertiesMap.keySet())
            {
                if ((classificationName != null) && (classificationPropertiesMap.get(classificationName) != null))
                {
                    NewElementProperties newElementProperties = new NewElementProperties(this.getElementProperties(classificationPropertiesMap.get(classificationName)));

                    newElementProperties.setEffectiveFrom(classificationPropertiesMap.get(classificationName).getEffectiveFrom());
                    newElementProperties.setEffectiveTo(classificationPropertiesMap.get(classificationName).getEffectiveTo());
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
                                                                       GlossaryTermActivityType.getOpenTypeName(),
                                                                       activityDescriptionProperties.getActivityType().getName());
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

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ANCHOR_SCOPE_GUID.name,
                                                                     anchorsProperties.getAnchorScopeGUID());
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
                                                                    dataScopeProperties.getMaxLongitude());
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
                                                                        OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                        dataScopeProperties.getAdditionalProperties());
            }
            else if (properties instanceof EditingGlossaryProperties editingGlossaryProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     editingGlossaryProperties.getDescription());
            }
            else if (properties instanceof GovernanceClassificationProperties governanceClassificationProperties)
            {
                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                                  governanceClassificationProperties.getStatus());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.CONFIDENCE.name,
                                                                  governanceClassificationProperties.getStatus());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD.name,
                                                                     governanceClassificationProperties.getSteward());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                     governanceClassificationProperties.getStewardPropertyName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                     governanceClassificationProperties.getStewardTypeName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SOURCE.name,
                                                                     governanceClassificationProperties.getSource());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NOTES.name,
                                                                     governanceClassificationProperties.getNotes());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.LEVEL_IDENTIFIER.name,
                                                                  governanceClassificationProperties.getLevelIdentifier());
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
            }
            else if (properties instanceof GovernanceMeasurementsProperties governanceMeasurementsProperties)
            {
                elementProperties = propertyHelper.addIntMapProperty(elementProperties,
                                                                     OpenMetadataProperty.MEASUREMENT_COUNTS.name,
                                                                     governanceMeasurementsProperties.getCounts());

                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.MEASUREMENT_VALUES.name,
                                                                        governanceMeasurementsProperties.getValues());

                elementProperties = propertyHelper.addBooleanMapProperty(elementProperties,
                                                                         OpenMetadataProperty.MEASUREMENT_FLAGS.name,
                                                                         governanceMeasurementsProperties.getFlags());
            }
            else if (properties instanceof GovernanceMeasurementsResultsDataSetProperties governanceMeasurementsResultsDataSetProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     governanceMeasurementsResultsDataSetProperties.getDescription());
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
                                                                       primaryKeyProperties.getKeyPattern().getName());
                }

            }
            else if (properties instanceof RetentionClassificationProperties retentionClassificationProperties)
            {
                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                                  retentionClassificationProperties.getStatus());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.CONFIDENCE.name,
                                                                  retentionClassificationProperties.getStatus());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD.name,
                                                                     retentionClassificationProperties.getSteward());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                     retentionClassificationProperties.getStewardPropertyName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                     retentionClassificationProperties.getStewardTypeName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SOURCE.name,
                                                                     retentionClassificationProperties.getSource());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NOTES.name,
                                                                     retentionClassificationProperties.getNotes());

                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                  OpenMetadataProperty.RETENTION_BASIS_IDENTIFIER.name,
                                                                  retentionClassificationProperties.getRetentionBasis());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ASSOCIATED_GUID.name,
                                                                     retentionClassificationProperties.getAssociatedGUID());

                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.ARCHIVE_AFTER.name,
                                                                   retentionClassificationProperties.getArchiveAfter());

                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.DELETE_AFTER.name,
                                                                   retentionClassificationProperties.getDeleteAfter());
            }
            else if (properties instanceof SecurityGroupMembershipProperties securityGroupMembershipProperties)
            {
                elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                          OpenMetadataProperty.GROUPS.name,
                                                                          securityGroupMembershipProperties.getGroups());
            }
            else if (properties instanceof SecurityTagsProperties securityTagsProperties)
            {
                elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                          OpenMetadataProperty.SECURITY_LABELS.name,
                                                                          securityTagsProperties.getSecurityLabels());

                elementProperties = propertyHelper.addMapProperty(elementProperties,
                                                                  OpenMetadataProperty.SECURITY_PROPERTIES.name,
                                                                  securityTagsProperties.getSecurityProperties());

                elementProperties = propertyHelper.addStringArrayStringMap(elementProperties,
                                                                           OpenMetadataProperty.ACCESS_GROUPS.name,
                                                                           securityTagsProperties.getAccessGroups());
            }
            else if (properties instanceof StagingGlossaryProperties stagingGlossaryProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                     stagingGlossaryProperties.getDescription());
            }
            else if (properties instanceof SubjectAreaProperties subjectAreaProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SUBJECT_AREA_NAME.name,
                                                                     subjectAreaProperties.getSubjectAreaName());
            }
            else if (properties instanceof TaxonomyProperties taxonomyProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ORGANIZING_PRINCIPLE.name,
                                                                     taxonomyProperties.getOrganizingPrinciple());
            }
            else if (properties instanceof TypeEmbeddedAttributeProperties typeEmbeddedAttributeProperties)
            {

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SCHEMA_TYPE_NAME.name,
                                                                     typeEmbeddedAttributeProperties.getSchemaTypeName());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                     typeEmbeddedAttributeProperties.getUserDefinedStatus());
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
                                                                     OpenMetadataProperty.AUTHOR.name,
                                                                     typeEmbeddedAttributeProperties.getAuthor());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.USAGE.name,
                                                                     typeEmbeddedAttributeProperties.getUsage());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.ENCODING_STANDARD.name,
                                                                     typeEmbeddedAttributeProperties.getEncodingStandard());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.NAMESPACE.name,
                                                                     typeEmbeddedAttributeProperties.getNamespace());
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
            else if (properties instanceof ZoneMembershipProperties zoneMembershipProperties)
            {
                elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                          OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                                          zoneMembershipProperties.getZoneMembership());
            }


                elementProperties = propertyHelper.addPropertyMap(elementProperties,
                                                              classificationBeanProperties.getExtendedProperties());

            return elementProperties;
        }

        return null;
    }
}
