/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.builders;

import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.GovernanceRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.CSVFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.DeployedSoftwareComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.PortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.IntegrationConnectorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.SurveyReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.topics.TopicProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CommunityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectorTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataClassProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataStructureProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.DesignModelProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.BusinessCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalSubscriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.CitedDocumentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.RelatedMediaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.GovernanceZoneProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityAccessControlProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionPortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

public class OpenMetadataElementBuilder
{
    protected final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Convert the properties beans into Element properties beans.  This is a bit fiddly in order to ensure the
     * effectivity dates are properly included for each classification.
     *
     * @param openMetadataRootProperties properties bean
     * @return new element properties
     */
    public NewElementProperties getNewElementProperties(EntityProperties openMetadataRootProperties)
    {
        if (openMetadataRootProperties != null)
        {
            NewElementProperties newElementProperties = new NewElementProperties(this.getElementProperties(openMetadataRootProperties));

            newElementProperties.setEffectiveFrom(openMetadataRootProperties.getEffectiveFrom());
            newElementProperties.setEffectiveTo(openMetadataRootProperties.getEffectiveTo());

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
    public ElementProperties getElementProperties(EntityProperties properties)
    {
        if (properties != null)
        {
            ElementProperties elementProperties = null;

            if (properties instanceof EntityElementProperties rootElementProperties)
            {
                elementProperties = new ElementProperties();

                elementProperties.setPropertyValueMap(rootElementProperties.getPropertyValueMap());
            }
            else if (properties instanceof OpenMetadataRootProperties openMetadataRootProperties)
            {
                if (properties instanceof AnnotationProperties annotationProperties)
                {
                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.ANNOTATION_TYPE.name,
                                                                         annotationProperties.getAnnotationType());

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.SUMMARY.name,
                                                                         annotationProperties.getSummary());

                    elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                      OpenMetadataProperty.CONFIDENCE_LEVEL.name,
                                                                      annotationProperties.getConfidenceLevel());

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.EXPRESSION.name,
                                                                         annotationProperties.getExpression());
                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.EXPLANATION.name,
                                                                         annotationProperties.getExplanation());
                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.ANALYSIS_STEP.name,
                                                                         annotationProperties.getAnalysisStep());
                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.JSON_PROPERTIES.name,
                                                                         annotationProperties.getJsonProperties());
                    elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                            OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                            annotationProperties.getAdditionalProperties());

                    if (properties instanceof DataFieldAnnotationProperties)
                    {
                        if (properties instanceof ClassificationAnnotationProperties classificationAnnotationProperties)
                        {
                            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                                    OpenMetadataProperty.CANDIDATE_CLASSIFICATIONS.name,
                                                                                    classificationAnnotationProperties.getCandidateClassifications());
                        }
                        else if (properties instanceof DataClassAnnotationProperties dataClassAnnotationProperties)
                        {
                            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                      OpenMetadataProperty.CANDIDATE_DATA_CLASS_GUIDS.name,
                                                                                      dataClassAnnotationProperties.getCandidateDataClassGUIDs());

                            elementProperties = propertyHelper.addLongProperty(elementProperties,
                                                                               OpenMetadataProperty.MATCHING_VALUES.name,
                                                                               dataClassAnnotationProperties.getMatchingValues());

                            elementProperties = propertyHelper.addLongProperty(elementProperties,
                                                                               OpenMetadataProperty.NON_MATCHING_VALUES.name,
                                                                               dataClassAnnotationProperties.getNonMatchingValues());
                        }
                        else if (properties instanceof FingerprintAnnotationProperties fingerprintAnnotationProperties)
                        {
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.FINGERPRINT.name,
                                                                                 fingerprintAnnotationProperties.getFingerprint());

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.FINGERPRINT_ALGORITHM.name,
                                                                                 fingerprintAnnotationProperties.getFingerprintAlgorithm());

                            elementProperties = propertyHelper.addLongProperty(elementProperties,
                                                                               OpenMetadataProperty.HASH.name,
                                                                               fingerprintAnnotationProperties.getHash());

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.HASH_ALGORITHM.name,
                                                                                 fingerprintAnnotationProperties.getHashAlgorithm());
                        }
                        else if (properties instanceof QualityAnnotationProperties qualityAnnotationProperties)
                        {
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.QUALITY_DIMENSION.name,
                                                                                 qualityAnnotationProperties.getQualityDimension());

                            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                              OpenMetadataProperty.QUALITY_SCORE.name,
                                                                              qualityAnnotationProperties.getQualityScore());
                        }
                        else if (properties instanceof RelationshipAdviceAnnotationProperties relationshipAdviceAnnotationProperties)
                        {
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.RELATED_ENTITY_GUID.name,
                                                                                 relationshipAdviceAnnotationProperties.getRelatedEntityGUID());

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.RELATIONSHIP_TYPE_NAME.name,
                                                                                 relationshipAdviceAnnotationProperties.getRelationshipTypeName());

                            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                                    OpenMetadataProperty.RELATIONSHIP_PROPERTIES.name,
                                                                                    relationshipAdviceAnnotationProperties.getRelationshipProperties());
                        }
                        else if (properties instanceof RequestForActionAnnotationProperties requestForActionAnnotationProperties)
                        {
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.ACTION_SOURCE_NAME.name,
                                                                                 requestForActionAnnotationProperties.getActionSourceName());

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.ACTION_REQUESTED.name,
                                                                                 requestForActionAnnotationProperties.getActionRequested());

                            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                                    OpenMetadataProperty.ACTION_PROPERTIES.name,
                                                                                    requestForActionAnnotationProperties.getActionProperties());
                        }
                        else if (properties instanceof ResourceProfileAnnotationProperties resourceProfileAnnotationProperties)
                        {
                            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                      OpenMetadataProperty.PROFILE_PROPERTY_NAMES.name,
                                                                                      resourceProfileAnnotationProperties.getProfilePropertyNames());

                            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                              OpenMetadataProperty.LENGTH.name,
                                                                              resourceProfileAnnotationProperties.getLength());

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.INFERRED_DATA_TYPE.name,
                                                                                 resourceProfileAnnotationProperties.getInferredDataType());

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.INFERRED_FORMAT.name,
                                                                                 resourceProfileAnnotationProperties.getInferredFormat());

                            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                              OpenMetadataProperty.INFERRED_LENGTH.name,
                                                                              resourceProfileAnnotationProperties.getInferredLength());

                            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                              OpenMetadataProperty.INFERRED_PRECISION.name,
                                                                              resourceProfileAnnotationProperties.getInferredPrecision());

                            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                              OpenMetadataProperty.INFERRED_SCALE.name,
                                                                              resourceProfileAnnotationProperties.getInferredScale());

                            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                               OpenMetadataProperty.PROFILE_START_DATE.name,
                                                                               resourceProfileAnnotationProperties.getProfileStartDate());

                            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                               OpenMetadataProperty.PROFILE_END_DATE.name,
                                                                               resourceProfileAnnotationProperties.getProfileStartDate());

                            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                                    OpenMetadataProperty.PROFILE_PROPERTIES.name,
                                                                                    resourceProfileAnnotationProperties.getProfileProperties());

                            elementProperties = propertyHelper.addBooleanMapProperty(elementProperties,
                                                                                     OpenMetadataProperty.PROFILE_FLAGS.name,
                                                                                     resourceProfileAnnotationProperties.getProfileFlags());

                            elementProperties = propertyHelper.addDateMapProperty(elementProperties,
                                                                                  OpenMetadataProperty.PROFILE_DATES.name,
                                                                                  resourceProfileAnnotationProperties.getProfileDates());

                            elementProperties = propertyHelper.addLongMapProperty(elementProperties,
                                                                                  OpenMetadataProperty.PROFILE_COUNTS.name,
                                                                                  resourceProfileAnnotationProperties.getProfileCounts());

                            elementProperties = propertyHelper.addDoubleMapProperty(elementProperties,
                                                                                    OpenMetadataProperty.PROFILE_DOUBLES.name,
                                                                                    resourceProfileAnnotationProperties.getProfileDoubles());

                            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                      OpenMetadataProperty.VALUE_LIST.name,
                                                                                      resourceProfileAnnotationProperties.getValueList());

                            elementProperties = propertyHelper.addIntMapProperty(elementProperties,
                                                                                 OpenMetadataProperty.VALUE_COUNT.name,
                                                                                 resourceProfileAnnotationProperties.getValueCount());

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.VALUE_RANGE_FROM.name,
                                                                                 resourceProfileAnnotationProperties.getValueRangeFrom());

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.VALUE_RANGE_TO.name,
                                                                                 resourceProfileAnnotationProperties.getValueRangeTo());

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.AVERAGE_VALUE.name,
                                                                                 resourceProfileAnnotationProperties.getAverageValue());

                        }
                        else if (properties instanceof SemanticAnnotationProperties semanticAnnotationProperties)
                        {
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.INFORMAL_TERM.name,
                                                                                 semanticAnnotationProperties.getInformalTerm());

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.SUBJECT_AREA_NAME.name,
                                                                                 semanticAnnotationProperties.getSubjectAreaName());

                            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                      OpenMetadataProperty.CANDIDATE_GLOSSARY_TERM_GUIDS.name,
                                                                                      semanticAnnotationProperties.getCandidateGlossaryTermGUIDs());

                            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                      OpenMetadataProperty.CANDIDATE_GLOSSARY_FOLDER_GUIDS.name,
                                                                                      semanticAnnotationProperties.getCandidateGlossaryFolderGUIDs());
                        }
                    }
                    else if (properties instanceof ResourceMeasureAnnotationProperties resourceMeasureAnnotationProperties)
                    {
                        elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                                OpenMetadataProperty.RESOURCE_PROPERTIES.name,
                                                                                resourceMeasureAnnotationProperties.getResourceProperties());

                        if (properties instanceof ResourcePhysicalStatusAnnotationProperties resourcePhysicalStatusAnnotationProperties)
                        {
                            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                               OpenMetadataProperty.RESOURCE_CREATE_TIME.name,
                                                                               resourcePhysicalStatusAnnotationProperties.getResourceCreateTime());

                            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                               OpenMetadataProperty.RESOURCE_UPDATE_TIME.name,
                                                                               resourcePhysicalStatusAnnotationProperties.getResourceUpdateTime());

                            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                               OpenMetadataProperty.RESOURCE_LAST_ACCESSED_TIME.name,
                                                                               resourcePhysicalStatusAnnotationProperties.getResourceLastAccessedTime());

                            elementProperties = propertyHelper.addLongProperty(elementProperties,
                                                                               OpenMetadataProperty.SIZE.name,
                                                                               resourcePhysicalStatusAnnotationProperties.getSize());

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.ENCODING_TYPE.name,
                                                                                 resourcePhysicalStatusAnnotationProperties.getEncodingType());
                        }
                    }
                    else if (properties instanceof SchemaAnalysisAnnotationProperties schemaAnalysisAnnotationProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.SCHEMA_NAME.name,
                                                                             schemaAnalysisAnnotationProperties.getSchemaName());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.SCHEMA_TYPE.name,
                                                                             schemaAnalysisAnnotationProperties.getSchemaType());
                    }
                }
                else if (properties instanceof InformalTagProperties informalTagProperties)
                {
                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.DISPLAY_NAME.name,
                                                                         informalTagProperties.getDisplayName());

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.DESCRIPTION.name,
                                                                         informalTagProperties.getDescription());
                }
                else if (properties instanceof LikeProperties likeProperties)
                {
                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.EMOJI.name,
                                                                         likeProperties.getEmoji());
                }
                else if (properties instanceof RatingProperties ratingProperties)
                {
                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.REVIEW.name,
                                                                         ratingProperties.getReview());

                    if (ratingProperties.getStarRating() != null)
                    {
                        elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                           OpenMetadataProperty.STARS.name,
                                                                           StarRating.getOpenTypeName(),
                                                                           ratingProperties.getStarRating().getName());
                    }
                }
                else if (properties instanceof SearchKeywordProperties searchKeywordProperties)
                {
                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.KEYWORD.name,
                                                                         searchKeywordProperties.getKeyword());

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.DESCRIPTION.name,
                                                                         searchKeywordProperties.getDescription());
                }
                else if (properties instanceof ReferenceableProperties referenceableProperties)
                {
                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                         referenceableProperties.getQualifiedName());

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.IDENTIFIER.name,
                                                                         referenceableProperties.getIdentifier());

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.DISPLAY_NAME.name,
                                                                         referenceableProperties.getDisplayName());

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.DESCRIPTION.name,
                                                                         referenceableProperties.getDescription());

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.VERSION_IDENTIFIER.name,
                                                                         referenceableProperties.getVersionIdentifier());

                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.CATEGORY.name,
                                                                         referenceableProperties.getCategory());

                    elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                            OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                            referenceableProperties.getAdditionalProperties());

                    if (properties instanceof ActorProfileProperties)
                    {
                        if (properties instanceof PersonProperties personProperties)
                        {
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.COURTESY_TITLE.name,
                                                                                 personProperties.getCourtesyTitle());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.INITIALS.name,
                                                                                 personProperties.getInitials());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.GIVEN_NAMES.name,
                                                                                 personProperties.getGivenNames());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.SURNAME.name,
                                                                                 personProperties.getSurname());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.FULL_NAME.name,
                                                                                 personProperties.getFullName());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.PRONOUNS.name,
                                                                                 personProperties.getPronouns());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.JOB_TITLE.name,
                                                                                 personProperties.getJobTitle());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.EMPLOYEE_NUMBER.name,
                                                                                 personProperties.getEmployeeNumber());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.EMPLOYEE_TYPE.name,
                                                                                 personProperties.getEmployeeType());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.PREFERRED_LANGUAGE.name,
                                                                                 personProperties.getPreferredLanguage());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.RESIDENT_COUNTRY.name,
                                                                                 personProperties.getResidentCountry());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.TIME_ZONE.name,
                                                                                 personProperties.getTimeZone());
                        }
                        else if (properties instanceof TeamProperties teamProperties)
                        {
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.TEAM_TYPE.name,
                                                                                 teamProperties.getTeamType());
                        }
                    }
                    else if (properties instanceof ActorRoleProperties actorRoleProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.SCOPE.name,
                                                                             actorRoleProperties.getScope());

                        if (properties instanceof PersonRoleProperties personRoleProperties)
                        {
                            if (properties instanceof GovernanceRoleProperties governanceRoleProperties)
                            {
                                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                                  OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                                                  governanceRoleProperties.getDomainIdentifier());
                            }

                            if ((personRoleProperties.getHeadCountLimitSet()) || (personRoleProperties.getHeadCount() > 0))
                            {
                                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                                  OpenMetadataProperty.HEAD_COUNT.name,
                                                                                  personRoleProperties.getHeadCount());
                            }
                        }
                        else if (properties instanceof TeamRoleProperties teamRoleProperties)
                        {
                            if ((teamRoleProperties.getHeadCountLimitSet()) || (teamRoleProperties.getHeadCount() > 0))
                            {
                                elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                                  OpenMetadataProperty.HEAD_COUNT.name,
                                                                                  teamRoleProperties.getHeadCount());
                            }
                        }
                    }
                    else if (properties instanceof AssetProperties assetProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.RESOURCE_NAME.name,
                                                                             assetProperties.getResourceName());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                             assetProperties.getDeployedImplementationType());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.NAMESPACE.name,
                                                                             assetProperties.getNamespace());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.SOURCE.name,
                                                                             assetProperties.getSource());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                             assetProperties.getUserDefinedStatus());

                        if (properties instanceof ProcessProperties processProperties)
                        {
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.FORMULA.name,
                                                                                 processProperties.getFormula());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.FORMULA_TYPE.name,
                                                                                 processProperties.getFormulaType());
                            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                              OpenMetadataProperty.PRIORITY.name,
                                                                              processProperties.getPriority());
                            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                               OpenMetadataProperty.REQUESTED_TIME.name,
                                                                               processProperties.getRequestedTime());
                            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                               OpenMetadataProperty.REQUESTED_START_TIME.name,
                                                                               processProperties.getRequestedStartTime());
                            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                               OpenMetadataProperty.START_TIME.name,
                                                                               processProperties.getStartTime());
                            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                               OpenMetadataProperty.DUE_TIME.name,
                                                                               processProperties.getDueTime());
                            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                               OpenMetadataProperty.LAST_REVIEW_TIME.name,
                                                                               processProperties.getLastReviewTime());
                            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                               OpenMetadataProperty.LAST_PAUSE_TIME.name,
                                                                               processProperties.getLastPauseTime());
                            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                               OpenMetadataProperty.LAST_RESUME_TIME.name,
                                                                               processProperties.getLastResumeTime());
                            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                               OpenMetadataProperty.COMPLETION_TIME.name,
                                                                               processProperties.getCompletionTime());
                            if (processProperties.getActivityStatus() != null)
                            {
                                elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                                   OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                                                   ActivityStatus.getOpenTypeName(),
                                                                                   processProperties.getActivityStatus().getName());
                            }

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                               OpenMetadataProperty.USER_DEFINED_ACTIVITY_STATUS.name,
                                                                               processProperties.getUserDefinedActivityStatus());

                            if (properties instanceof DeployedSoftwareComponentProperties deployedSoftwareComponentProperties)
                            {
                                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                     OpenMetadataProperty.IMPLEMENTATION_LANGUAGE.name,
                                                                                     deployedSoftwareComponentProperties.getImplementationLanguage());

                                if (deployedSoftwareComponentProperties instanceof IntegrationConnectorProperties integrationConnectorProperties)
                                {
                                    elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                                          OpenMetadataProperty.USES_BLOCKING_CALLS.name,
                                                                                          integrationConnectorProperties.getUsesBlockingCalls());
                                }
                            }
                            else if (properties instanceof ActionProperties)
                            {
                                if (properties instanceof MeetingProperties meetingProperties)
                                {
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.OBJECTIVE.name,
                                                                                         meetingProperties.getObjective());
                                    elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                              OpenMetadataProperty.MINUTES.name,
                                                                                              meetingProperties.getMinutes());
                                    elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                              OpenMetadataProperty.DECISIONS.name,
                                                                                              meetingProperties.getDecisions());
                                }
                                else if (properties instanceof EngineActionProperties engineActionProperties)
                                {
                                    elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                                      OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                                                      engineActionProperties.getDomainIdentifier());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.REQUEST_TYPE.name,
                                                                                         engineActionProperties.getRequestType());
                                    elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                                            OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                                            engineActionProperties.getRequestParameters());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.REQUESTER_USER_ID.name,
                                                                                         engineActionProperties.getRequesterUserId());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.EXECUTOR_ENGINE_GUID.name,
                                                                                         engineActionProperties.getExecutorEngineGUID());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.EXECUTOR_ENGINE_NAME.name,
                                                                                         engineActionProperties.getExecutorEngineName());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.GOVERNANCE_ACTION_TYPE_GUID.name,
                                                                                         engineActionProperties.getGovernanceActionTypeGUID());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.GOVERNANCE_ACTION_TYPE_NAME.name,
                                                                                         engineActionProperties.getGovernanceActionTypeName());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.PROCESS_NAME.name,
                                                                                         engineActionProperties.getProcessName());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.PROCESS_STEP_GUID.name,
                                                                                         engineActionProperties.getProcessStepGUID());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.PROCESS_STEP_NAME.name,
                                                                                         engineActionProperties.getProcessStepName());
                                    elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                              OpenMetadataProperty.RECEIVED_GUARDS.name,
                                                                                              engineActionProperties.getReceivedGuards());
                                    elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                              OpenMetadataProperty.MANDATORY_GUARDS.name,
                                                                                              engineActionProperties.getMandatoryGuards());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.PROCESSING_ENGINE_USER_ID.name,
                                                                                         engineActionProperties.getProcessingEngineUserId());
                                    elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                              OpenMetadataProperty.COMPLETION_GUARDS.name,
                                                                                              engineActionProperties.getCompletionGuards());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.COMPLETION_MESSAGE.name,
                                                                                         engineActionProperties.getCompletionMessage());
                                }
                                else if (properties instanceof NotificationProperties notificationProperties)
                                {
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.SYSTEM_ACTION.name,
                                                                                         notificationProperties.getSystemAction());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.USER_RESPONSE.name,
                                                                                         notificationProperties.getUserResponse());
                                }
                                else if (properties instanceof ReviewProperties reviewProperties)
                                {
                                    elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                                         OpenMetadataProperty.REVIEW_DATE.name,
                                                                                         reviewProperties.getReviewDate());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.COMMENT.name,
                                                                                         reviewProperties.getComment());

                                    if (properties instanceof AnnotationReviewProperties annotationReviewProperties)
                                    {
                                        if (annotationReviewProperties.getAnnotationStatus() != null)
                                        {
                                            elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                                               OpenMetadataProperty.ANNOTATION_STATUS.name,
                                                                                               AnnotationStatus.getOpenTypeName(),
                                                                                               annotationReviewProperties.getAnnotationStatus().getName());
                                        }
                                    }
                                }
                            }
                        }
                        else if (properties instanceof DataAssetProperties)
                        {
                            if (properties instanceof DataSetProperties dataSetProperties)
                            {
                                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                     OpenMetadataProperty.FORMULA.name,
                                                                                     dataSetProperties.getFormula());
                                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                     OpenMetadataProperty.FORMULA_TYPE.name,
                                                                                     dataSetProperties.getFormulaType());

                                if (properties instanceof ReportProperties reportProperties)
                                {
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.PURPOSE.name,
                                                                                         reportProperties.getPurpose());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.AUTHOR.name,
                                                                                         reportProperties.getAuthor());
                                    elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                                       OpenMetadataProperty.START_TIME.name,
                                                                                       reportProperties.getStartTime());
                                    elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                                       OpenMetadataProperty.COMPLETION_TIME.name,
                                                                                       reportProperties.getCompletionTime());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.COMPLETION_MESSAGE.name,
                                                                                         reportProperties.getCompletionMessage());
                                    elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                                       OpenMetadataProperty.CREATED_TIME.name,
                                                                                       reportProperties.getCreatedTime());
                                    elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                                       OpenMetadataProperty.LAST_MODIFIED_TIME.name,
                                                                                       reportProperties.getLastModifiedTime());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.LAST_MODIFIER.name,
                                                                                         reportProperties.getLastModifier());

                                    if (properties instanceof ConnectorActivityReportProperties connectorActivityReportProperties)
                                    {
                                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                             OpenMetadataProperty.SERVER_NAME.name,
                                                                                             connectorActivityReportProperties.getServerName());
                                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                             OpenMetadataProperty.CONNECTOR_ID.name,
                                                                                             connectorActivityReportProperties.getConnectorId());
                                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                             OpenMetadataProperty.CONNECTOR_NAME.name,
                                                                                             connectorActivityReportProperties.getConnectorName());
                                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                                  OpenMetadataProperty.CREATED_ELEMENTS.name,
                                                                                                  connectorActivityReportProperties.getCreatedElements());
                                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                                  OpenMetadataProperty.CREATED_ELEMENTS.name,
                                                                                                  connectorActivityReportProperties.getCreatedElements());
                                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                                  OpenMetadataProperty.CREATED_ELEMENTS.name,
                                                                                                  connectorActivityReportProperties.getCreatedElements());

                                    }
                                    else if (properties instanceof IncidentReportProperties incidentReportProperties)
                                    {
                                        elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                                          OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                                                          incidentReportProperties.getDomainIdentifier());
                                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                             OpenMetadataProperty.BACKGROUND.name,
                                                                                             incidentReportProperties.getBackground());
                                        if (incidentReportProperties.getIncidentStatus() != null)
                                        {
                                            elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                                               OpenMetadataProperty.INCIDENT_STATUS.name,
                                                                                               IncidentReportStatus.getOpenTypeName(),
                                                                                               incidentReportProperties.getIncidentStatus().getName());
                                        }

                                        elementProperties = propertyHelper.addIntMapProperty(elementProperties,
                                                                                             OpenMetadataProperty.INCIDENT_CLASSIFIERS.name,
                                                                                             incidentReportProperties.getIncidentClassifiers());
                                    }
                                    else if (properties instanceof SurveyReportProperties surveyReportProperties)
                                    {
                                        elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                                                OpenMetadataProperty.ANALYSIS_PARAMETERS.name,
                                                                                                surveyReportProperties.getAnalysisParameters());
                                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                             OpenMetadataProperty.ANALYSIS_STEP.name,
                                                                                             surveyReportProperties.getAnalysisStep());
                                    }
                                }
                                else if (properties instanceof TopicProperties topicProperties)
                                {
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.TOPIC_NAME.name,
                                                                                         topicProperties.getTopicName());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.TOPIC_TYPE.name,
                                                                                         topicProperties.getTopicType());
                                }
                            }
                            else if (properties instanceof DataStoreProperties dataStoreProperties)
                            {
                                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                     OpenMetadataProperty.PATH_NAME.name,
                                                                                     dataStoreProperties.getPathName());

                                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                                   OpenMetadataProperty.STORE_CREATE_TIME.name,
                                                                                   dataStoreProperties.getStoreCreateTime());

                                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                                   OpenMetadataProperty.STORE_UPDATE_TIME.name,
                                                                                   dataStoreProperties.getStoreUpdateTime());
                                if (properties instanceof DataFileProperties dataFileProperties)
                                {
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.FILE_NAME.name,
                                                                                         dataFileProperties.getFileName());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.FILE_TYPE.name,
                                                                                         dataFileProperties.getFileType());
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.FILE_EXTENSION.name,
                                                                                         dataFileProperties.getFileExtension());

                                    if (properties instanceof CSVFileProperties csvFileProperties)
                                    {
                                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                             OpenMetadataProperty.DELIMITER_CHARACTER.name,
                                                                                             String.valueOf(csvFileProperties.getDelimiterCharacter()));
                                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                             OpenMetadataProperty.QUOTE_CHARACTER.name,
                                                                                             String.valueOf(csvFileProperties.getQuoteCharacter()));
                                    }
                                }
                            }
                            else if (properties instanceof ReportTypeProperties reportTypeProperties)
                            {
                                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                     OpenMetadataProperty.PURPOSE.name,
                                                                                     reportTypeProperties.getPurpose());
                                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                     OpenMetadataProperty.AUTHOR.name,
                                                                                     reportTypeProperties.getAuthor());
                                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                                   OpenMetadataProperty.CREATED_TIME.name,
                                                                                   reportTypeProperties.getCreatedTime());
                                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                                   OpenMetadataProperty.LAST_MODIFIED_TIME.name,
                                                                                   reportTypeProperties.getLastModifiedTime());
                                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                     OpenMetadataProperty.LAST_MODIFIER.name,
                                                                                     reportTypeProperties.getLastModifier());
                            }
                        }
                    }
                    else if (properties instanceof CollectionProperties collectionProperties)
                    {
                        if (collectionProperties instanceof AgreementProperties agreementProperties)
                        {
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                                 agreementProperties.getUserDefinedStatus());

                            if (collectionProperties instanceof DigitalSubscriptionProperties digitalSubscriptionProperties)
                            {
                                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                     OpenMetadataProperty.SUPPORT_LEVEL.name,
                                                                                     digitalSubscriptionProperties.getSupportLevel());
                                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                                        OpenMetadataProperty.SERVICE_LEVELS.name,
                                                                                        digitalSubscriptionProperties.getServiceLevels());
                            }
                        }
                        else if (properties instanceof BusinessCapabilityProperties businessCapabilityProperties)
                        {
                            if (businessCapabilityProperties.getBusinessCapabilityType() != null)
                            {
                                elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                                   OpenMetadataProperty.BUSINESS_CAPABILITY_TYPE.name,
                                                                                   BusinessCapabilityType.getOpenTypeName(),
                                                                                   businessCapabilityProperties.getBusinessCapabilityType().getName());
                            }

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.BUSINESS_IMPLEMENTATION_TYPE.name,
                                                                                 businessCapabilityProperties.getBusinessImplementationType());
                        }
                        else if (collectionProperties instanceof DigitalProductProperties digitalProductProperties)
                        {
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.PRODUCT_NAME.name,
                                                                                 digitalProductProperties.getProductName());

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                                 digitalProductProperties.getUserDefinedStatus());

                            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                               OpenMetadataProperty.INTRODUCTION_DATE.name,
                                                                               digitalProductProperties.getIntroductionDate());

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.MATURITY.name,
                                                                                 digitalProductProperties.getMaturity());

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.SERVICE_LIFE.name,
                                                                                 digitalProductProperties.getServiceLife());

                            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                               OpenMetadataProperty.NEXT_VERSION_DATE.name,
                                                                               digitalProductProperties.getNextVersionDate());

                            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                               OpenMetadataProperty.WITHDRAW_DATE.name,
                                                                               digitalProductProperties.getWithdrawDate());
                        }
                        else if (properties instanceof DesignModelProperties designModelProperties)
                        {
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                                 designModelProperties.getUserDefinedStatus());

                            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                      OpenMetadataProperty.AUTHORS.name,
                                                                                      designModelProperties.getAuthors());
                        }
                        else if (properties instanceof GlossaryProperties glossaryProperties)
                        {
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.LANGUAGE.name,
                                                                                 glossaryProperties.getLanguage());

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.USAGE.name,
                                                                                 glossaryProperties.getUsage());
                        }
                        else if (properties instanceof InformationSupplyChainProperties informationSupplyChainProperties)
                        {
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.SCOPE.name,
                                                                                 informationSupplyChainProperties.getScope());

                            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                      OpenMetadataProperty.PURPOSES.name,
                                                                                      informationSupplyChainProperties.getPurposes());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.INTEGRATION_STYLE.name,
                                                                                 informationSupplyChainProperties.getIntegrationStyle());
                            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                                    OpenMetadataProperty.ESTIMATED_VOLUMETRICS.name,
                                                                                    informationSupplyChainProperties.getEstimatedVolumetrics());
                        }
                    }
                    else if (properties instanceof CommentProperties commentProperties)
                    {
                        if (commentProperties.getCommentType() != null)
                        {
                            elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                               OpenMetadataProperty.COMMENT_TYPE.name,
                                                                               CommentType.getOpenTypeName(),
                                                                               commentProperties.getCommentType().getName());
                        }
                    }
                    else if (properties instanceof CommunityProperties communityProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.MISSION.name,
                                                                             communityProperties.getMission());
                    }
                    else if (properties instanceof ConnectionProperties connectionProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.USER_ID.name,
                                                                             connectionProperties.getUserId());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.ENCRYPTED_PASSWORD.name,
                                                                             connectionProperties.getEncryptedPassword());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.CLEAR_PASSWORD.name,
                                                                             connectionProperties.getClearPassword());

                        elementProperties = propertyHelper.addMapProperty(elementProperties,
                                                                          OpenMetadataProperty.CONFIGURATION_PROPERTIES.name,
                                                                          connectionProperties.getConfigurationProperties());

                        elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                                OpenMetadataProperty.SECURED_PROPERTIES.name,
                                                                                connectionProperties.getSecuredProperties());
                    }
                    else if (properties instanceof ConnectorTypeProperties connectorTypeProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.SUPPORTED_ASSET_TYPE_NAME.name,
                                                                             connectorTypeProperties.getSupportedAssetTypeName());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.SUPPORTED_DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                             connectorTypeProperties.getSupportedDeployedImplementationType());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.EXPECTED_DATA_FORMAT.name,
                                                                             connectorTypeProperties.getExpectedDataFormat());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.CONNECTOR_PROVIDER_CLASS_NAME.name,
                                                                             connectorTypeProperties.getConnectorProviderClassName());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.CONNECTOR_FRAMEWORK_NAME.name,
                                                                             connectorTypeProperties.getConnectorFrameworkName());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.CONNECTOR_INTERFACE_LANGUAGE.name,
                                                                             connectorTypeProperties.getConnectorInterfaceLanguage());

                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                  OpenMetadataProperty.CONNECTOR_INTERFACES.name,
                                                                                  connectorTypeProperties.getConnectorInterfaces());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.TARGET_TECHNOLOGY_SOURCE.name,
                                                                             connectorTypeProperties.getTargetTechnologySource());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.TARGET_TECHNOLOGY_NAME.name,
                                                                             connectorTypeProperties.getTargetTechnologyName());

                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                  OpenMetadataProperty.TARGET_TECHNOLOGY_INTERFACES.name,
                                                                                  connectorTypeProperties.getTargetTechnologyInterfaces());

                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                  OpenMetadataProperty.TARGET_TECHNOLOGY_VERSIONS.name,
                                                                                  connectorTypeProperties.getTargetTechnologyVersions());

                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                  OpenMetadataProperty.RECOGNIZED_ADDITIONAL_PROPERTIES.name,
                                                                                  connectorTypeProperties.getRecognizedAdditionalProperties());

                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                  OpenMetadataProperty.RECOGNIZED_SECURED_PROPERTIES.name,
                                                                                  connectorTypeProperties.getRecognizedSecuredProperties());

                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                  OpenMetadataProperty.RECOGNIZED_CONFIGURATION_PROPERTIES.name,
                                                                                  connectorTypeProperties.getRecognizedConfigurationProperties());
                    }
                    else if (properties instanceof ContactDetailsProperties contactDetailsProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.CONTACT_TYPE.name,
                                                                             contactDetailsProperties.getContactType());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.CONTACT_METHOD_SERVICE.name,
                                                                             contactDetailsProperties.getContactMethodService());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.CONTACT_METHOD_VALUE.name,
                                                                             contactDetailsProperties.getContactMethodService());

                        if (contactDetailsProperties.getContactMethodType() != null)
                        {
                            elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                               OpenMetadataProperty.CONTACT_METHOD_TYPE.name,
                                                                               ContactMethodType.getOpenTypeName(),
                                                                               contactDetailsProperties.getContactMethodType().getName());
                        }
                    }
                    else if (properties instanceof DataClassProperties dataClassProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                             dataClassProperties.getUserDefinedStatus());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.NAMESPACE.name,
                                                                             dataClassProperties.getNamespace());

                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                  OpenMetadataProperty.MATCH_PROPERTY_NAMES.name,
                                                                                  dataClassProperties.getMatchPropertyNames());

                        elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                          OpenMetadataProperty.MATCH_THRESHOLD.name,
                                                                          dataClassProperties.getMatchThreshold());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.SPECIFICATION.name,
                                                                             dataClassProperties.getSpecification());

                        elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                                OpenMetadataProperty.SPECIFICATION_DETAILS.name,
                                                                                dataClassProperties.getSpecificationDetails());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.DATA_TYPE.name,
                                                                             dataClassProperties.getDataType());

                        elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                              OpenMetadataProperty.ALLOWS_DUPLICATE_VALUES.name,
                                                                              dataClassProperties.getAllowsDuplicateValues());

                        elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                              OpenMetadataProperty.IS_CASE_SENSITIVE.name,
                                                                              dataClassProperties.getIsCaseSensitive());

                        elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                              OpenMetadataProperty.IS_NULLABLE.name,
                                                                              dataClassProperties.getIsNullable());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.DEFAULT_VALUE.name,
                                                                             dataClassProperties.getDefaultValue());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.AVERAGE_VALUE.name,
                                                                             dataClassProperties.getAverageValue());

                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                  OpenMetadataProperty.VALUE_LIST.name,
                                                                                  dataClassProperties.getValueList());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.VALUE_RANGE_FROM.name,
                                                                             dataClassProperties.getValueRangeFrom());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.VALUE_RANGE_TO.name,
                                                                             dataClassProperties.getValueRangeTo());

                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                  OpenMetadataProperty.SAMPLE_VALUES.name,
                                                                                  dataClassProperties.getSampleValues());

                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                  OpenMetadataProperty.DATA_PATTERNS.name,
                                                                                  dataClassProperties.getDataPatterns());
                    }
                    else if (properties instanceof DataFieldProperties dataFieldProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                             dataFieldProperties.getUserDefinedStatus());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.NAMESPACE.name,
                                                                             dataFieldProperties.getNamespace());

                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                  OpenMetadataProperty.ALIASES.name,
                                                                                  dataFieldProperties.getAliases());

                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                  OpenMetadataProperty.NAME_PATTERNS.name,
                                                                                  dataFieldProperties.getNamePatterns());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                              OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                              dataFieldProperties.getUserDefinedStatus());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.DEFAULT_VALUE.name,
                                                                             dataFieldProperties.getDefaultValue());

                        elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                              OpenMetadataProperty.IS_NULLABLE.name,
                                                                              dataFieldProperties.getIsNullable());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.DATA_TYPE.name,
                                                                             dataFieldProperties.getDataType());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.UNITS.name,
                                                                             dataFieldProperties.getUnits());

                        elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                          OpenMetadataProperty.MINIMUM_LENGTH.name,
                                                                          dataFieldProperties.getMinimumLength());

                        elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                          OpenMetadataProperty.LENGTH.name,
                                                                          dataFieldProperties.getLength());

                        elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                          OpenMetadataProperty.PRECISION.name,
                                                                          dataFieldProperties.getPrecision());

                        elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                              OpenMetadataProperty.ORDERED_VALUES.name,
                                                                              dataFieldProperties.getOrderedValues());

                        if (dataFieldProperties.getSortOrder() != null)
                        {
                            elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                               OpenMetadataProperty.SORT_ORDER.name,
                                                                               DataItemSortOrder.getOpenTypeName(),
                                                                               dataFieldProperties.getSortOrder().getName());
                        }
                    }
                    else if (properties instanceof DataStructureProperties dataStructureProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                             dataStructureProperties.getUserDefinedStatus());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.NAMESPACE.name,
                                                                             dataStructureProperties.getNamespace());

                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                  OpenMetadataProperty.NAME_PATTERNS.name,
                                                                                  dataStructureProperties.getNamePatterns());
                    }
                    else if (properties instanceof EndpointProperties endpointProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.NETWORK_ADDRESS.name,
                                                                             endpointProperties.getNetworkAddress());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.PROTOCOL.name,
                                                                             endpointProperties.getProtocol());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.ENCRYPTION_METHOD.name,
                                                                             endpointProperties.getEncryptionMethod());
                    }
                    else if (properties instanceof ExternalReferenceProperties externalReferenceProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.REFERENCE_TITLE.name,
                                                                             externalReferenceProperties.getReferenceTitle());
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.REFERENCE_ABSTRACT.name,
                                                                             externalReferenceProperties.getReferenceAbstract());

                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                             OpenMetadataProperty.AUTHORS.name,
                                                                             externalReferenceProperties.getAuthors());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.ORGANIZATION.name,
                                                                             externalReferenceProperties.getOrganization());
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.URL.name,
                                                                             externalReferenceProperties.getURL());

                        elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                             OpenMetadataProperty.SOURCES.name,
                                                                             externalReferenceProperties.getSources());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.LICENSE.name,
                                                                             externalReferenceProperties.getLicense());
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.COPYRIGHT.name,
                                                                             externalReferenceProperties.getCopyright());
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.ATTRIBUTION.name,
                                                                             externalReferenceProperties.getAttribution());

                        if (properties instanceof RelatedMediaProperties relatedMediaProperties)
                        {
                            if (relatedMediaProperties.getMediaType() != null)
                            {
                                elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                                     OpenMetadataProperty.MEDIA_TYPE.name,
                                                                                     MediaType.getOpenTypeName(),
                                                                                     relatedMediaProperties.getMediaType().getName());
                            }

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.MEDIA_TYPE_OTHER_ID.name,
                                                                                 relatedMediaProperties.getMediaTypeOtherId());

                            if (relatedMediaProperties.getDefaultMediaUsage() != null)
                            {
                                elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                                   OpenMetadataProperty.DEFAULT_MEDIA_USAGE.name,
                                                                                   MediaUsage.getOpenTypeName(),
                                                                                   relatedMediaProperties.getDefaultMediaUsage().getName());
                            }

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.DEFAULT_MEDIA_USAGE_OTHER_ID.name,
                                                                                 relatedMediaProperties.getDefaultMediaUsageOtherId());
                        }
                        else if (properties instanceof CitedDocumentProperties citedDocumentProperties)
                        {
                            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                              OpenMetadataProperty.NUMBER_OF_PAGES.name,
                                                                              citedDocumentProperties.getNumberOfPages());

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.PAGE_RANGE.name,
                                                                                 citedDocumentProperties.getReferenceTitle());

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.PUBLICATION_SERIES.name,
                                                                                 citedDocumentProperties.getPublicationSeries());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.PUBLICATION_SERIES_VOLUME.name,
                                                                                 citedDocumentProperties.getPublicationSeriesVolume());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.PUBLISHER.name,
                                                                                 citedDocumentProperties.getPublisher());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.EDITION.name,
                                                                                 citedDocumentProperties.getEdition());

                            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                               OpenMetadataProperty.FIRST_PUB_DATE.name,
                                                                               citedDocumentProperties.getFirstPublicationDate());
                            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                               OpenMetadataProperty.PUBLICATION_DATE.name,
                                                                               citedDocumentProperties.getPublicationDate());

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.PUBLICATION_CITY.name,
                                                                                 citedDocumentProperties.getPublicationCity());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.PUBLICATION_YEAR.name,
                                                                                 citedDocumentProperties.getPublicationYear());

                            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                      OpenMetadataProperty.PUBLICATION_NUMBERS.name,
                                                                                      citedDocumentProperties.getPublicationNumbers());
                        }
                    }
                    else if (properties instanceof ExternalIdProperties externalIdProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.KEY.name,
                                                                             externalIdProperties.getKey());
                        if (externalIdProperties.getKeyPattern() != null)
                        {
                            elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                               OpenMetadataProperty.KEY_PATTERN.name,
                                                                               KeyPattern.getOpenTypeName(),
                                                                               externalIdProperties.getKeyPattern().getName());
                        }

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.EXT_INSTANCE_TYPE_NAME.name,
                                                                             externalIdProperties.getExternalInstanceTypeName());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.EXT_INSTANCE_CREATED_BY.name,
                                                                             externalIdProperties.getExternalInstanceCreatedBy());

                        elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                           OpenMetadataProperty.EXT_INSTANCE_CREATION_TIME.name,
                                                                           externalIdProperties.getExternalInstanceCreationTime());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.EXT_INSTANCE_LAST_UPDATED_BY.name,
                                                                             externalIdProperties.getExternalInstanceLastUpdatedBy());

                        elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                           OpenMetadataProperty.EXT_INSTANCE_LAST_UPDATE_TIME.name,
                                                                           externalIdProperties.getExternalInstanceLastUpdateTime());

                        elementProperties = propertyHelper.addLongProperty(elementProperties,
                                                                           OpenMetadataProperty.EXT_INSTANCE_VERSION.name,
                                                                           externalIdProperties.getExternalInstanceVersion());
                    }
                    else if (properties instanceof GlossaryTermProperties glossaryTermProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                             glossaryTermProperties.getUserDefinedStatus());

                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                  OpenMetadataProperty.ALIASES.name,
                                                                                  glossaryTermProperties.getAliases());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.SUMMARY.name,
                                                                             glossaryTermProperties.getSummary());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.EXAMPLES.name,
                                                                             glossaryTermProperties.getExamples());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.ABBREVIATION.name,
                                                                             glossaryTermProperties.getAbbreviation());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.USAGE.name,
                                                                             glossaryTermProperties.getUsage());
                    }
                    else if (properties instanceof GovernanceDefinitionProperties governanceDefinitionProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.SUMMARY.name,
                                                                             governanceDefinitionProperties.getSummary());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.SCOPE.name,
                                                                             governanceDefinitionProperties.getScope());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.IMPORTANCE.name,
                                                                             governanceDefinitionProperties.getImportance());

                        elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                          OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                                          governanceDefinitionProperties.getDomainIdentifier());

                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                  OpenMetadataProperty.IMPLICATIONS.name,
                                                                                  governanceDefinitionProperties.getImplications());

                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                  OpenMetadataProperty.OUTCOMES.name,
                                                                                  governanceDefinitionProperties.getOutcomes());

                        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                  OpenMetadataProperty.RESULTS.name,
                                                                                  governanceDefinitionProperties.getResults());

                        if (governanceDefinitionProperties instanceof GovernanceStrategyProperties governanceStrategyProperties)
                        {
                            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                      OpenMetadataProperty.BUSINESS_IMPERATIVES.name,
                                                                                      governanceStrategyProperties.getBusinessImperatives());
                        }
                        else if (governanceDefinitionProperties instanceof GovernanceControlProperties governanceControlProperties)
                        {
                            if (governanceControlProperties instanceof SecurityGroupProperties securityGroupProperties)
                            {
                                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                     OpenMetadataProperty.DISTINGUISHED_NAME.name,
                                                                                     securityGroupProperties.getDistinguishedName());
                            }
                            else if (governanceControlProperties instanceof GovernanceMetricProperties governanceMetricProperties)
                            {
                                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                     OpenMetadataProperty.MEASUREMENT.name,
                                                                                     governanceMetricProperties.getMeasurement());
                                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                     OpenMetadataProperty.TARGET.name,
                                                                                     governanceMetricProperties.getTarget());
                            }
                            else if (governanceControlProperties instanceof NotificationTypeProperties notificationTypeProperties)
                            {
                                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                                     OpenMetadataProperty.START_DATE.name,
                                                                                     notificationTypeProperties.getStartDate());
                                elementProperties = propertyHelper.addLongProperty(elementProperties,
                                                                                     OpenMetadataProperty.REFRESH_TIME_INTERVAL.name,
                                                                                     notificationTypeProperties.getRefreshTimeInterval());
                                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                                   OpenMetadataProperty.CONNECTOR_SHUTDOWN_DATE.name,
                                                                                   notificationTypeProperties.getConnectorShutdownDate());
                            }
                            else if (governanceControlProperties instanceof GovernanceRuleProperties governanceRuleProperties)
                            {
                                if (governanceRuleProperties instanceof NamingStandardRuleProperties namingStandardRuleProperties)
                                {
                                    elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                              OpenMetadataProperty.NAME_PATTERNS.name,
                                                                                              namingStandardRuleProperties.getNamePatterns());
                                }
                            }
                            else if (governanceControlProperties instanceof SecurityAccessControlProperties)
                            {
                                if (properties instanceof GovernanceZoneProperties governanceZoneProperties)
                                {
                                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                         OpenMetadataProperty.CRITERIA.name,
                                                                                         governanceZoneProperties.getCriteria());
                                }
                            }
                            else if (governanceControlProperties instanceof TermsAndConditionsProperties termsAndConditionsProperties)
                            {
                                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                                        OpenMetadataProperty.ENTITLEMENTS.name,
                                                                                        termsAndConditionsProperties.getEntitlements());
                                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                                        OpenMetadataProperty.RESTRICTIONS.name,
                                                                                        termsAndConditionsProperties.getRestrictions());
                                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                                        OpenMetadataProperty.OBLIGATIONS.name,
                                                                                        termsAndConditionsProperties.getObligations());
                            }

                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.IMPLEMENTATION_DESCRIPTION.name,
                                                                                 governanceControlProperties.getImplementationDescription());
                        }
                        else if (governanceDefinitionProperties instanceof RegulationProperties regulationProperties)
                        {
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.REGULATION_SOURCE.name,
                                                                                 regulationProperties.getRegulationSource());
                        }
                    }
                    else if (properties instanceof PortProperties portProperties)
                    {
                        if (portProperties.getPortType() != null)
                        {
                            elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                               OpenMetadataProperty.PORT_TYPE.name,
                                                                               PortType.getOpenTypeName(),
                                                                               portProperties.getPortType().getName());
                        }

                    }
                    else if (properties instanceof ProjectProperties projectProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.PROJECT_PHASE.name,
                                                                             projectProperties.getProjectStatus());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.PROJECT_HEALTH.name,
                                                                             projectProperties.getProjectStatus());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.PROJECT_STATUS.name,
                                                                             projectProperties.getProjectStatus());

                        elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                           OpenMetadataProperty.START_DATE.name,
                                                                           projectProperties.getStartDate());

                        elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                           OpenMetadataProperty.PLANNED_END_DATE.name,
                                                                           projectProperties.getPlannedEndDate());
                    }
                    else if (properties instanceof SchemaElementProperties schemaElementProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                             schemaElementProperties.getUserDefinedStatus());

                        if (properties instanceof SchemaTypeProperties schemaTypeProperties)
                        {
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.AUTHOR.name,
                                                                                 schemaTypeProperties.getAuthor());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.USAGE.name,
                                                                                 schemaTypeProperties.getUsage());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.ENCODING_STANDARD.name,
                                                                                 schemaTypeProperties.getEncodingStandard());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.NAMESPACE.name,
                                                                                 schemaTypeProperties.getNamespace());

                            if (properties instanceof SimpleSchemaTypeProperties simpleSchemaTypeProperties)
                            {
                                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                     OpenMetadataProperty.DATA_TYPE.name,
                                                                                     simpleSchemaTypeProperties.getDataType());

                                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                     OpenMetadataProperty.DEFAULT_VALUE.name,
                                                                                     simpleSchemaTypeProperties.getDefaultValue());
                            }
                            else if (properties instanceof LiteralSchemaTypeProperties literalSchemaTypeProperties)
                            {
                                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                     OpenMetadataProperty.DATA_TYPE.name,
                                                                                     literalSchemaTypeProperties.getDataType());

                                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                     OpenMetadataProperty.FIXED_VALUE.name,
                                                                                     literalSchemaTypeProperties.getFixedValue());
                            }
                        }
                        else if (properties instanceof SchemaAttributeProperties schemaAttributeProperties)
                        {
                            elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                                 OpenMetadataProperty.ALLOWS_DUPLICATE_VALUES.name,
                                                                                 schemaAttributeProperties.getAllowsDuplicateValues());
                            elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                                  OpenMetadataProperty.ORDERED_VALUES.name,
                                                                                  schemaAttributeProperties.getOrderedValues());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                  OpenMetadataProperty.DEFAULT_VALUE_OVERRIDE.name,
                                                                                  schemaAttributeProperties.getDefaultValueOverride());
                            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                              OpenMetadataProperty.MINIMUM_LENGTH.name,
                                                                              schemaAttributeProperties.getMinimumLength());
                            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                              OpenMetadataProperty.LENGTH.name,
                                                                              schemaAttributeProperties.getLength());
                            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                              OpenMetadataProperty.PRECISION.name,
                                                                              schemaAttributeProperties.getPrecision());
                            elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                                  OpenMetadataProperty.IS_NULLABLE.name,
                                                                                  schemaAttributeProperties.getIsNullable());
                            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                                 OpenMetadataProperty.NATIVE_CLASS.name,
                                                                                 schemaAttributeProperties.getNativeClass());
                            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                                 OpenMetadataProperty.ALIASES.name,
                                                                                 schemaAttributeProperties.getAliases());
                            if (schemaAttributeProperties.getSortOrder() != null)
                            {
                                elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                                   OpenMetadataProperty.SORT_ORDER.name,
                                                                                   DataItemSortOrder.getOpenTypeName(),
                                                                                   schemaAttributeProperties.getSortOrder().getName());
                            }
                        }
                    }
                    else if (properties instanceof SolutionComponentProperties solutionComponentProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.SOLUTION_COMPONENT_TYPE.name,
                                                                             solutionComponentProperties.getSolutionComponentType());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.PLANNED_DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                             solutionComponentProperties.getPlannedDeployedImplementationType());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                             solutionComponentProperties.getUserDefinedStatus());
                    }
                    else if (properties instanceof SolutionPortProperties solutionPortProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                             solutionPortProperties.getUserDefinedStatus());

                        if (solutionPortProperties.getSolutionPortDirection() != null)
                        {
                            elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                               OpenMetadataProperty.DIRECTION.name,
                                                                               SolutionPortDirection.getOpenTypeName(),
                                                                               solutionPortProperties.getSolutionPortDirection().getName());
                        }
                    }
                    else if (properties instanceof UserIdentityProperties userIdentityProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.USER_ID.name,
                                                                             userIdentityProperties.getUserId());

                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.DISTINGUISHED_NAME.name,
                                                                             userIdentityProperties.getDistinguishedName());
                    }
                    else if (properties instanceof ValidValueDefinitionProperties validValueDefinitionProperties)
                    {
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.NAMESPACE.name,
                                                                             validValueDefinitionProperties.getNamespace());
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                             validValueDefinitionProperties.getUserDefinedStatus());
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.USAGE.name,
                                                                             validValueDefinitionProperties.getUsage());
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.DATA_TYPE.name,
                                                                             validValueDefinitionProperties.getDataType());
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.SCOPE.name,
                                                                             validValueDefinitionProperties.getScope());
                        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                             OpenMetadataProperty.PREFERRED_VALUE.name,
                                                                             validValueDefinitionProperties.getPreferredValue());
                        elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                             OpenMetadataProperty.IS_CASE_SENSITIVE.name,
                                                                             validValueDefinitionProperties.getIsCaseSensitive());

                    }
                }

                elementProperties = propertyHelper.addPropertyMap(elementProperties,
                                                                  openMetadataRootProperties.getExtendedProperties());
            }

            return elementProperties;
        }

        return null;
    }

}
