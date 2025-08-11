/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.EngineActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.MeetingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ToDoProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.GovernanceRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.DeployedConnectorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.DeployedSoftwareComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.topics.TopicProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectorTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.VirtualConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingPurposeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalSubscriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.CommentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.LikeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.RatingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.SearchKeywordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governanceactions.GovernanceActionTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalDBSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularFileColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.GovernanceZoneProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityAccessControlProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.lang.reflect.InvocationTargetException;


/**
 * OpenMetadataRootConverter provides common methods for transferring relevant properties from an Open Metadata
 * Element object into a bean that inherits from OpenMetadataRootProperties.  This is the converter that is
 * used for most types.
 */
public class OpenMetadataRootConverter<B> extends AttributedElementConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public OpenMetadataRootConverter(PropertyHelper propertyHelper,
                                     String         serviceName,
                                     String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Uses the type of the entity to determine the type of bean to use for the properties.
     *
     * @param beanClass element bean class
     * @param openMetadataElement element retrieved
     * @param methodName calling method
     * @return properties
     * @throws PropertyServerException problem in conversion
     */
    protected OpenMetadataRootProperties getBeanProperties(Class<B>            beanClass,
                                                           OpenMetadataElement openMetadataElement,
                                                           String              methodName) throws PropertyServerException
    {
        if (openMetadataElement != null)
        {
            OpenMetadataRootProperties beanProperties;

            /*
             * The initial set of values come from the attributes - the bean properties object to use is
             * determined by its type.  Attributes are removed from element properties as the bean properties
             * object is filled.  Any remaining properties are added to extended properties so none are lost.
             * The properties added to extended properties should be from subtypes that are not part of the
             * open metadata type standard.  However, there may also be properties that are not (yet) explicitly
             * mapped to the converter process below.
             */
            ElementProperties elementProperties = new ElementProperties(openMetadataElement.getElementProperties());

            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.REFERENCEABLE.typeName))
            {
                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ACTOR.typeName))
                {
                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ACTOR_PROFILE.typeName))
                    {
                        if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.PERSON.typeName))
                        {
                            beanProperties = new PersonProperties();

                            ((PersonProperties) beanProperties).setCourtesyTitle(this.removeCourtesyTitle(elementProperties));
                            ((PersonProperties) beanProperties).setInitials(this.removeInitials(elementProperties));
                            ((PersonProperties) beanProperties).setGivenNames(this.removeGivenNames(elementProperties));
                            ((PersonProperties) beanProperties).setSurname(this.removeSurname(elementProperties));
                            ((PersonProperties) beanProperties).setFullName(this.removeFullName(elementProperties));
                            ((PersonProperties) beanProperties).setPronouns(this.removePronouns(elementProperties));
                            ((PersonProperties) beanProperties).setJobTitle(this.removeJobTitle(elementProperties));
                            ((PersonProperties) beanProperties).setEmployeeNumber(this.removeEmployeeNumber(elementProperties));
                            ((PersonProperties) beanProperties).setEmployeeType(this.removeEmployeeType(elementProperties));
                            ((PersonProperties) beanProperties).setPreferredLanguage(this.removePreferredLanguage(elementProperties));
                            ((PersonProperties) beanProperties).setResidentCountry(this.removeResidentCountry(elementProperties));
                            ((PersonProperties) beanProperties).setTimeZone(this.removeTimeZone(elementProperties));
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TEAM.typeName))
                        {
                            beanProperties = new TeamProperties();

                            ((TeamProperties) beanProperties).setIdentifier(this.removeIdentifier(elementProperties));
                            ((TeamProperties) beanProperties).setTeamType(this.removeTeamType(elementProperties));
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.IT_PROFILE.typeName))
                        {
                            beanProperties = new ITProfileProperties();
                        }
                        else
                        {
                            beanProperties = new ActorProfileProperties();
                        }
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ACTOR_ROLE.typeName))
                    {
                        if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.PERSON_ROLE.typeName))
                        {
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_ROLE.typeName))
                            {
                                beanProperties = new GovernanceRoleProperties();

                                ((GovernanceRoleProperties) beanProperties).setDomainIdentifier(this.removeDomainIdentifier(elementProperties));
                            }
                            else
                            {
                                beanProperties = new PersonRoleProperties();
                            }

                            if (elementProperties.getPropertyValue(OpenMetadataProperty.HEAD_COUNT.name) != null)
                            {
                                ((PersonRoleProperties) beanProperties).setHeadCount(this.removeHeadCount(elementProperties));
                                ((PersonRoleProperties) beanProperties).setHeadCountLimitSet(true);
                            }
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TEAM_ROLE.typeName))
                        {
                            beanProperties = new TeamRoleProperties();

                            if (elementProperties.getPropertyValue(OpenMetadataProperty.HEAD_COUNT.name) != null)
                            {
                                ((TeamRoleProperties) beanProperties).setHeadCount(this.removeHeadCount(elementProperties));
                                ((TeamRoleProperties) beanProperties).setHeadCountLimitSet(true);
                            }
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.IT_PROFILE_ROLE.typeName))
                        {
                            beanProperties = new ITProfileRoleProperties();
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SOLUTION_ACTOR_ROLE.typeName))
                        {
                            beanProperties = new SolutionActorRoleProperties();
                        }
                        else
                        {
                            beanProperties = new ActorRoleProperties();
                        }

                        ((ActorRoleProperties)beanProperties).setScope(this.removeScope(elementProperties));
                        ((ActorRoleProperties)beanProperties).setIdentifier(this.removeIdentifier(elementProperties));
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.USER_IDENTITY.typeName))
                    {
                        beanProperties = new UserIdentityProperties();

                        ((UserIdentityProperties)beanProperties).setUserId(this.removeUserId(elementProperties));
                        ((UserIdentityProperties)beanProperties).setDistinguishedName(this.removeDistinguishedName(elementProperties));
                    }
                    else
                    {
                        beanProperties = new ActorProperties();
                    }
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ASSET.typeName))
                {
                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_ASSET.typeName))
                    {
                        if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_SET.typeName))
                        {
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.REPORT.typeName))
                            {
                                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.CONNECTOR_ACTIVITY_REPORT.typeName))
                                {
                                    beanProperties = new ConnectorActivityReportProperties();

                                    ((ConnectorActivityReportProperties)beanProperties).setServerName(super.removeServerName(elementProperties));
                                    ((ConnectorActivityReportProperties)beanProperties).setConnectorId(super.removeConnectorId(elementProperties));
                                    ((ConnectorActivityReportProperties)beanProperties).setConnectorName(super.removeConnectorName(elementProperties));
                                    ((ConnectorActivityReportProperties)beanProperties).setCreatedElements(super.removeCreatedElements(elementProperties));
                                    ((ConnectorActivityReportProperties)beanProperties).setUpdatedElements(super.removeUpdatedElements(elementProperties));
                                    ((ConnectorActivityReportProperties)beanProperties).setDeletedElements(super.removeDeletedElements(elementProperties));
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.INCIDENT_REPORT.typeName))
                                {
                                    beanProperties = new IncidentReportProperties();

                                    ((IncidentReportProperties)beanProperties).setDomainIdentifier(super.removeDomainIdentifier(elementProperties));
                                    ((IncidentReportProperties)beanProperties).setBackground(super.removeBackground(elementProperties));
                                    ((IncidentReportProperties)beanProperties).setIncidentStatus(super.removeIncidentReportStatus(elementProperties));
                                    ((IncidentReportProperties)beanProperties).setIncidentClassifiers(super.removeIncidentClassifiers(elementProperties));
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SURVEY_REPORT.typeName))
                                {
                                    beanProperties = new SurveyReportProperties();

                                    ((SurveyReportProperties)beanProperties).setAnalysisParameters(super.removeAnalysisParameters(elementProperties));
                                    ((SurveyReportProperties)beanProperties).setAnalysisStep(super.removeAnalysisStep(elementProperties));
                                }
                                else
                                {
                                    beanProperties = new ReportProperties();
                                }

                                ((ReportProperties)beanProperties).setIdentifier(super.removeIdentifier(elementProperties));
                                ((ReportProperties)beanProperties).setPurpose(super.removePurpose(elementProperties));
                                ((ReportProperties)beanProperties).setAuthor(super.removeAuthor(elementProperties));
                                ((ReportProperties)beanProperties).setStartTime(super.removeStartTime(elementProperties));
                                ((ReportProperties)beanProperties).setCompletionTime(super.removeCompletionTime(elementProperties));
                                ((ReportProperties)beanProperties).setCompletionMessage(super.removeCompletionMessage(elementProperties));
                                ((ReportProperties)beanProperties).setCreatedTime(super.removeCreatedTime(elementProperties));
                                ((ReportProperties)beanProperties).setLastModifiedTime(super.removeLastModifiedTime(elementProperties));
                                ((ReportProperties)beanProperties).setLastModifier(super.removeLastModifier(elementProperties));
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.FORM.typeName))
                            {
                                beanProperties = new FormProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.INFORMATION_VIEW.typeName))
                            {
                                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.VIRTUAL_RELATIONAL_TABLE.typeName))
                                {
                                    beanProperties = new VirtualRelationalTableProperties();
                                }
                                else
                                {
                                    beanProperties = new InformationViewProperties();
                                }
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TOPIC.typeName))
                            {
                                beanProperties = new TopicProperties();

                                ((TopicProperties)beanProperties).setTopicName(super.removeTopicName(elementProperties));
                                ((TopicProperties)beanProperties).setTopicType(super.removeTopicType(elementProperties));
                            }
                            else
                            {
                                beanProperties = new DataSetProperties();
                            }

                            ((DataSetProperties)beanProperties).setFormula(super.removeFormula(elementProperties));
                            ((DataSetProperties)beanProperties).setFormulaType(super.removeFormulaType(elementProperties));
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_FEED.typeName))
                        {
                            beanProperties = new DataFeedProperties();
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_STORE.typeName))
                        {
                            beanProperties = new DataStoreProperties();

                            ((DataStoreProperties)beanProperties).setStoreCreateTime(super.removeStoreCreateTime(elementProperties));
                            ((DataStoreProperties)beanProperties).setStoreUpdateTime(super.removeStoreUpdateTime(elementProperties));
                            ((DataStoreProperties)beanProperties).setPathName(super.removePathName(elementProperties));
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.REPORT_TYPE.typeName))
                        {
                            beanProperties = new ReportTypeProperties();

                            ((ReportTypeProperties)beanProperties).setIdentifier(super.removeIdentifier(elementProperties));
                            ((ReportTypeProperties)beanProperties).setPurpose(super.removePurpose(elementProperties));
                            ((ReportTypeProperties)beanProperties).setAuthor(super.removeAuthor(elementProperties));
                            ((ReportTypeProperties)beanProperties).setCreatedTime(super.removeCreatedTime(elementProperties));
                            ((ReportTypeProperties)beanProperties).setLastModifiedTime(super.removeLastModifiedTime(elementProperties));
                            ((ReportTypeProperties)beanProperties).setLastModifier(super.removeLastModifier(elementProperties));
                        }
                        else
                        {
                            beanProperties = new DataAssetProperties();
                        }
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.PROCESS.typeName))
                    {
                        if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ACTION.typeName))
                        {
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TO_DO.typeName))
                            {
                                beanProperties = new ToDoProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.MEETING.typeName))
                            {
                                beanProperties = new MeetingProperties();

                                ((MeetingProperties) beanProperties).setObjective(this.removeObjective(elementProperties));
                                ((MeetingProperties) beanProperties).setMinutes(this.removeMinutes(elementProperties));
                                ((MeetingProperties) beanProperties).setDecisions(this.removeDecisions(elementProperties));
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ENGINE_ACTION.typeName))
                            {
                                beanProperties = new EngineActionProperties();

                                ((EngineActionProperties) beanProperties).setDomainIdentifier(this.removeDomainIdentifier(elementProperties));
                                ((EngineActionProperties) beanProperties).setMandatoryGuards(this.removeMandatoryGuards(elementProperties));
                                ((EngineActionProperties) beanProperties).setReceivedGuards(this.removeReceivedGuards(elementProperties));
                                ((EngineActionProperties) beanProperties).setExecutorEngineGUID(this.removeExecutorEngineGUID(elementProperties));
                                ((EngineActionProperties) beanProperties).setExecutorEngineName(this.removeExecutorEngineName(elementProperties));
                                ((EngineActionProperties) beanProperties).setGovernanceActionTypeGUID(this.removeGovernanceActionTypeGUID(elementProperties));
                                ((EngineActionProperties) beanProperties).setGovernanceActionTypeName(this.removeGovernanceActionTypeName(elementProperties));
                                ((EngineActionProperties) beanProperties).setProcessName(this.removeProcessName(elementProperties));
                                ((EngineActionProperties) beanProperties).setProcessStepGUID(this.removeProcessStepGUID(elementProperties));
                                ((EngineActionProperties) beanProperties).setProcessStepName(this.removeProcessStepName(elementProperties));
                                ((EngineActionProperties) beanProperties).setRequesterUserId(this.removeRequesterUserId(elementProperties));
                                ((EngineActionProperties) beanProperties).setRequestType(this.removeRequestType(elementProperties));
                                ((EngineActionProperties) beanProperties).setRequestParameters(this.removeRequestParameters(elementProperties));
                                ((EngineActionProperties) beanProperties).setProcessingEngineUserId(this.removeProcessingEngineUserId(elementProperties));
                                ((EngineActionProperties) beanProperties).setCompletionGuards(this.removeCompletionGuards(elementProperties));
                                ((EngineActionProperties) beanProperties).setCompletionMessage(this.removeCompletionMessage(elementProperties));
                            }
                            else
                            {
                                beanProperties = new ActionProperties();
                            }
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName))
                        {
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DEPLOYED_CONNECTOR.typeName))
                            {
                                beanProperties = new DeployedConnectorProperties();
                            }
                            else
                            {
                                beanProperties = new DeployedSoftwareComponentProperties();
                            }

                            ((DeployedSoftwareComponentProperties) beanProperties).setImplementationLanguage(this.removeImplementationLanguage(elementProperties));
                        }
                        else
                        {
                            beanProperties = new ProcessProperties();
                        }

                        ((ProcessProperties) beanProperties).setFormula(this.removeFormula(elementProperties));
                        ((ProcessProperties) beanProperties).setFormulaType(this.removeFormulaType(elementProperties));
                        ((ProcessProperties) beanProperties).setPriority(this.removeIntPriority(elementProperties));
                        ((ProcessProperties) beanProperties).setRequestedTime(this.removeRequestedTime(elementProperties));
                        ((ProcessProperties) beanProperties).setRequestedStartTime(this.removeRequestedStartTime(elementProperties));
                        ((ProcessProperties) beanProperties).setStartTime(this.removeStartTime(elementProperties));
                        ((ProcessProperties) beanProperties).setDueTime(this.removeDueTime(elementProperties));
                        ((ProcessProperties) beanProperties).setLastReviewTime(this.removeLastReviewTime(elementProperties));
                        ((ProcessProperties) beanProperties).setLastPauseTime(this.removeLastPauseTime(elementProperties));
                        ((ProcessProperties) beanProperties).setLastResumeTime(this.removeLastResumeTime(elementProperties));
                        ((ProcessProperties) beanProperties).setCompletionTime(this.removeCompletionTime(elementProperties));
                        ((ProcessProperties) beanProperties).setActivityStatus(this.removeActivityStatus(elementProperties));
                        ((ProcessProperties) beanProperties).setUserDefinedActivityStatus(this.removeUserDefinedActivityStatus(elementProperties));
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.INFRASTRUCTURE.typeName))
                    {
                        if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.IT_INFRASTRUCTURE.typeName))
                        {
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.HOST.typeName))
                            {
                                beanProperties = new HostProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName))
                            {
                                beanProperties = new SoftwareServerPlatformProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SOFTWARE_SERVER.typeName))
                            {
                                beanProperties = new SoftwareServerProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.NETWORK.typeName))
                            {
                                beanProperties = new NetworkProperties();
                            }
                            else
                            {
                                beanProperties = new ITInfrastructureProperties();
                            }
                        }
                        else
                        {
                            beanProperties = new InfrastructureProperties();
                        }
                    }
                    else
                    {
                        beanProperties = new AssetProperties();
                    }

                    ((AssetProperties)beanProperties).setResourceName(this.removeResourceName(elementProperties));
                    ((AssetProperties)beanProperties).setDeployedImplementationType(this.removeDeployedImplementationType(elementProperties));
                    ((AssetProperties)beanProperties).setNamespace(this.removeNamespace(elementProperties));
                    ((AssetProperties)beanProperties).setSource(this.removeSource(elementProperties));
                    ((AssetProperties)beanProperties).setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.COLLECTION.typeName))
                {
                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DIGITAL_PRODUCT.typeName))
                    {
                        beanProperties = new DigitalProductProperties();

                        ((DigitalProductProperties)beanProperties).setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                        ((DigitalProductProperties)beanProperties).setProductName(this.removeProductName(elementProperties));
                        ((DigitalProductProperties)beanProperties).setIdentifier(this.removeIdentifier(elementProperties));
                        ((DigitalProductProperties)beanProperties).setIntroductionDate(this.removeIntroductionDate(elementProperties));
                        ((DigitalProductProperties)beanProperties).setMaturity(this.removeMaturity(elementProperties));
                        ((DigitalProductProperties)beanProperties).setServiceLife(this.removeServiceLife(elementProperties));
                        ((DigitalProductProperties)beanProperties).setNextVersionDate(this.removeNextVersionDate(elementProperties));
                        ((DigitalProductProperties)beanProperties).setWithdrawDate(this.removeWithdrawDate(elementProperties));
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.AGREEMENT.typeName))
                    {
                        if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DIGITAL_SUBSCRIPTION.typeName))
                        {
                            beanProperties = new DigitalSubscriptionProperties();

                            ((DigitalSubscriptionProperties)beanProperties).setServiceLevels(this.removeServiceLevels(elementProperties));
                            ((DigitalSubscriptionProperties)beanProperties).setSupportLevel(this.removeSupportLevel(elementProperties));
                        }
                        else
                        {
                            beanProperties = new AgreementProperties();
                        }

                        ((AgreementProperties)beanProperties).setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                        ((AgreementProperties)beanProperties).setIdentifier(this.removeIdentifier(elementProperties));

                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_SPEC_COLLECTION.typeName))
                    {
                        beanProperties = new DataSpecProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_DICTIONARY_COLLECTION.typeName))
                    {
                        beanProperties = new DataDictionaryProperties();
                    }
                    else
                    {
                        beanProperties = new CollectionProperties();
                    }
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.COMMENT.typeName))
                {
                    beanProperties = new CommentProperties();

                    ((CommentProperties)beanProperties).setCommentType(this.removeCommentType(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.CONNECTION.typeName))
                {
                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.VIRTUAL_CONNECTION.typeName))
                    {
                        beanProperties = new VirtualConnectionProperties();
                    }
                    else
                    {
                        beanProperties = new ConnectionProperties();
                    }

                    ((ConnectionProperties)beanProperties).setSecuredProperties(this.removeSecuredProperties(elementProperties));
                    ((ConnectionProperties)beanProperties).setConfigurationProperties(this.removeConfigurationProperties(elementProperties));
                    ((ConnectionProperties)beanProperties).setUserId(this.removeUserId(elementProperties));
                    ((ConnectionProperties)beanProperties).setClearPassword(this.removeClearPassword(elementProperties));
                    ((ConnectionProperties)beanProperties).setEncryptedPassword(this.removeEncryptedPassword(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.CONNECTOR_TYPE.typeName))
                {
                    beanProperties = new ConnectorTypeProperties();

                    ((ConnectorTypeProperties)beanProperties).setSupportedAssetTypeName(this.removeSupportedAssetTypeName(elementProperties));
                    ((ConnectorTypeProperties)beanProperties).setSupportedDeployedImplementationType(this.removeSupportedDeployedImplementationType(elementProperties));
                    ((ConnectorTypeProperties)beanProperties).setExpectedDataFormat(this.removeExpectedDataFormat(elementProperties));
                    ((ConnectorTypeProperties)beanProperties).setConnectorProviderClassName(this.removeConnectorProviderClassName(elementProperties));
                    ((ConnectorTypeProperties)beanProperties).setConnectorFrameworkName(this.removeConnectorFrameworkName(elementProperties));
                    ((ConnectorTypeProperties)beanProperties).setConnectorInterfaceLanguage(this.removeConnectorInterfaceLanguage(elementProperties));
                    ((ConnectorTypeProperties)beanProperties).setConnectorInterfaces(this.removeConnectorInterfaces(elementProperties));
                    ((ConnectorTypeProperties)beanProperties).setTargetTechnologySource(this.removeTargetTechnologySource(elementProperties));
                    ((ConnectorTypeProperties)beanProperties).setTargetTechnologyName(this.removeTargetTechnologyName(elementProperties));
                    ((ConnectorTypeProperties)beanProperties).setTargetTechnologyInterfaces(this.removeTargetTechnologyInterfaces(elementProperties));
                    ((ConnectorTypeProperties)beanProperties).setTargetTechnologyVersions(this.removeTargetTechnologyVersions(elementProperties));
                    ((ConnectorTypeProperties)beanProperties).setRecognizedAdditionalProperties(this.removeRecognizedAdditionalProperties(elementProperties));
                    ((ConnectorTypeProperties)beanProperties).setRecognizedSecuredProperties(this.removeRecognizedSecuredProperties(elementProperties));
                    ((ConnectorTypeProperties)beanProperties).setRecognizedConfigurationProperties(this.removeRecognizedConfigurationProperties(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.CONTACT_DETAILS.typeName))
                {
                    beanProperties = new ContactDetailsProperties();

                    ((ContactDetailsProperties)beanProperties).setContactType(this.removeContactType(elementProperties));
                    ((ContactDetailsProperties)beanProperties).setContactMethodType(this.removeContactMethodType(elementProperties));
                    ((ContactDetailsProperties)beanProperties).setContactMethodService(this.removeContactMethodService(elementProperties));
                    ((ContactDetailsProperties)beanProperties).setContactMethodValue(this.removeContactMethodValue(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.CONTRIBUTION_RECORD.typeName))
                {
                    beanProperties = new ContributionRecordProperties();

                    ((ContributionRecordProperties)beanProperties).setKarmaPoints(this.removeKarmaPoints(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_CLASS.typeName))
                {
                    beanProperties = new DataClassProperties();

                    ((DataClassProperties)beanProperties).setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                    ((DataClassProperties)beanProperties).setNamespace(this.removeNamespace(elementProperties));
                    ((DataClassProperties)beanProperties).setMatchPropertyNames(this.removeMatchPropertyNames(elementProperties));
                    ((DataClassProperties)beanProperties).setMatchThreshold(this.removeMatchThreshold(elementProperties));
                    ((DataClassProperties)beanProperties).setSpecification(this.removeSpecification(elementProperties));
                    ((DataClassProperties)beanProperties).setSpecificationDetails(this.removeSpecificationDetails(elementProperties));
                    ((DataClassProperties)beanProperties).setDataType(this.removeDataType(elementProperties));
                    ((DataClassProperties)beanProperties).setAllowsDuplicateValues(this.removeAllowsDuplicateValues(elementProperties));
                    ((DataClassProperties)beanProperties).setIsCaseSensitive(this.removeIsCaseSensitive(elementProperties));
                    ((DataClassProperties)beanProperties).setIsNullable(this.removeIsNullable(elementProperties));
                    ((DataClassProperties)beanProperties).setDefaultValue(this.removeDefaultValue(elementProperties));
                    ((DataClassProperties)beanProperties).setAverageValue(this.removeAverageValue(elementProperties));
                    ((DataClassProperties)beanProperties).setValueList(this.removeValueList(elementProperties));
                    ((DataClassProperties)beanProperties).setValueRangeFrom(this.removeValueRangeFrom(elementProperties));
                    ((DataClassProperties)beanProperties).setValueRangeTo(this.removeValueRangeTo(elementProperties));
                    ((DataClassProperties)beanProperties).setSampleValues(this.removeSampleValues(elementProperties));
                    ((DataClassProperties)beanProperties).setDataPatterns(this.removeDataPatterns(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_STRUCTURE.typeName))
                {
                    beanProperties = new DataStructureProperties();

                    ((DataStructureProperties)beanProperties).setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_FIELD.typeName))
                {
                    beanProperties = new DataFieldProperties();

                    ((DataFieldProperties)beanProperties).setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.EXTERNAL_REFERENCE.typeName))
                {
                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.CITED_DOCUMENT.typeName))
                    {
                        beanProperties = new CitedDocumentProperties();

                        ((CitedDocumentProperties)beanProperties).setNumberOfPages(this.removeNumberOfPages(elementProperties));
                        ((CitedDocumentProperties)beanProperties).setPageRange(this.removePageRange(elementProperties));
                        ((CitedDocumentProperties)beanProperties).setPublicationSeries(this.removePublicationSeries(elementProperties));
                        ((CitedDocumentProperties)beanProperties).setPublicationSeriesVolume(this.removePublicationSeriesVolume(elementProperties));
                        ((CitedDocumentProperties)beanProperties).setPublisher(this.removePublisher(elementProperties));
                        ((CitedDocumentProperties)beanProperties).setEdition(this.removeEdition(elementProperties));
                        ((CitedDocumentProperties)beanProperties).setFirstPublicationDate(this.removeFirstPublicationDate(elementProperties));
                        ((CitedDocumentProperties)beanProperties).setPublicationDate(this.removePublicationDate(elementProperties));
                        ((CitedDocumentProperties)beanProperties).setPublicationCity(this.removePublicationCity(elementProperties));
                        ((CitedDocumentProperties)beanProperties).setPublicationYear(this.removePublicationYear(elementProperties));
                        ((CitedDocumentProperties)beanProperties).setPublicationNumbers(this.removePublicationNumbers(elementProperties));
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.EXTERNAL_DATA_SOURCE.typeName))
                    {
                        beanProperties = new ExternalDataSourceProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.EXTERNAL_MODEL_SOURCE.typeName))
                    {
                        beanProperties = new ExternalModelSourceProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.RELATED_MEDIA.typeName))
                    {
                        beanProperties = new RelatedMediaProperties();

                        ((RelatedMediaProperties)beanProperties).setMediaType(this.removeMediaType(elementProperties));
                        ((RelatedMediaProperties)beanProperties).setMediaTypeOtherId(this.removeMediaTypeOtherId(elementProperties));
                        ((RelatedMediaProperties)beanProperties).setDefaultMediaUsage(this.removeDefaultMediaUsage(elementProperties));
                        ((RelatedMediaProperties)beanProperties).setDefaultMediaUsageOtherId(this.removeDefaultMediaUsageOtherId(elementProperties));
                    }
                    else
                    {
                        beanProperties = new ExternalReferenceProperties();
                    }

                    ((ExternalReferenceProperties)beanProperties).setReferenceTitle(this.removeReferenceTitle(elementProperties));
                    ((ExternalReferenceProperties)beanProperties).setReferenceAbstract(this.removeReferenceAbstract(elementProperties));
                    ((ExternalReferenceProperties)beanProperties).setAuthors(this.removeAuthors(elementProperties));
                    ((ExternalReferenceProperties)beanProperties).setOrganization(this.removeOrganization(elementProperties));
                    ((ExternalReferenceProperties)beanProperties).setURL(this.removeURL(elementProperties));
                    ((ExternalReferenceProperties)beanProperties).setSources(this.removeSources(elementProperties));
                    ((ExternalReferenceProperties)beanProperties).setCopyright(this.removeCopyright(elementProperties));
                    ((ExternalReferenceProperties)beanProperties).setLicense(this.removeLicense(elementProperties));
                    ((ExternalReferenceProperties)beanProperties).setAttribution(this.removeAttribution(elementProperties));

                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_DEFINITION.typeName))
                {
                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_CONTROL.typeName))
                    {
                        if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TECHNICAL_CONTROL.typeName))
                        {
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SECURITY_GROUP.typeName))
                            {
                                beanProperties = new SecurityGroupProperties();

                                ((SecurityGroupProperties) beanProperties).setDistinguishedName(this.removeDistinguishedName(elementProperties));
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_METRIC.typeName))
                            {
                                beanProperties = new GovernanceMetricProperties();

                                ((GovernanceMetricProperties)beanProperties).setTarget(this.removeTarget(elementProperties));
                                ((GovernanceMetricProperties)beanProperties).setMeasurement(this.removeMeasurement(elementProperties));
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SECURITY_ACCESS_CONTROL.typeName))
                            {
                                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_ZONE.typeName))
                                {
                                    beanProperties = new GovernanceZoneProperties();

                                    ((GovernanceZoneProperties)beanProperties).setCriteria(this.removeCriteria(elementProperties));
                                }
                                else
                                {
                                    beanProperties = new SecurityAccessControlProperties();
                                }
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SERVICE_LEVEL_OBJECTIVE.typeName))
                            {
                                beanProperties = new ServiceLevelObjectiveProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_ACTION.typeName))
                            {
                                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName))
                                {
                                    beanProperties = new GovernanceActionProcessProperties();
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName))
                                {
                                    beanProperties = new GovernanceActionTypeProperties();

                                    ((GovernanceActionTypeProperties) beanProperties).setWaitTime(this.removeWaitTime(elementProperties));
                                }
                                else
                                {
                                    beanProperties = new GovernanceActionProperties();
                                }
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_RULE.typeName))
                            {
                                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.NAMING_STANDARD_RULE.typeName))
                                {
                                    beanProperties = new NamingStandardRuleProperties();

                                    ((NamingStandardRuleProperties) beanProperties).setNamePatterns(this.removeNamePatterns(elementProperties));
                                }
                                else
                                {
                                    beanProperties = new GovernanceRuleProperties();
                                }
                            }
                            else
                            {
                                beanProperties = new TechnicalControlProperties();
                            }
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ORGANIZATIONAL_CONTROL.typeName))
                        {
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TERMS_AND_CONDITIONS.typeName))
                            {
                                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.CERTIFICATION_TYPE.typeName))
                                {
                                    beanProperties = new CertificationTypeProperties();
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.LICENSE_TYPE.typeName))
                                {
                                    beanProperties = new LicenseTypeProperties();
                                }
                                else
                                {
                                    beanProperties = new TermsAndConditionsProperties();
                                }

                                ((TermsAndConditionsProperties) beanProperties).setEntitlements(this.removeEntitlements(elementProperties));
                                ((TermsAndConditionsProperties) beanProperties).setRestrictions(this.removeRestrictions(elementProperties));
                                ((TermsAndConditionsProperties) beanProperties).setObligations(this.removeObligations(elementProperties));
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName))
                            {
                                beanProperties = new SubjectAreaDefinitionProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_PROCESSING_PURPOSE.typeName))
                            {
                                beanProperties = new DataProcessingPurposeProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_RESPONSIBILITY.typeName))
                            {
                                beanProperties = new GovernanceResponsibilityProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_PROCEDURE.typeName))
                            {
                                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.METHODOLOGY.typeName))
                                {
                                    beanProperties = new MethodologyProperties();
                                }
                                else
                                {
                                    beanProperties = new GovernanceProcedureProperties();
                                }
                            }
                            else
                            {
                                beanProperties = new OrganizationalControlProperties();
                            }
                        }
                        else
                        {
                            beanProperties = new GovernanceControlProperties();
                        }
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_DRIVER.typeName))
                    {
                        if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_STRATEGY.typeName))
                        {
                            beanProperties = new GovernanceStrategyProperties();

                            ((GovernanceStrategyProperties) beanProperties).setBusinessImperatives(this.removeBusinessImperatives(elementProperties));
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.BUSINESS_IMPERATIVE.typeName))
                        {
                            beanProperties = new BusinessImperativeProperties();
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.REGULATION.typeName))
                        {
                            beanProperties = new RegulationProperties();

                            ((RegulationProperties) beanProperties).setRegulationSource(this.removeRegulationSource(elementProperties));
                            ((RegulationProperties) beanProperties).setRegulators(this.removeRegulators(elementProperties));
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.REGULATION_ARTICLE.typeName))
                        {
                            beanProperties = new RegulationArticleProperties();
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.THREAT.typeName))
                        {
                            beanProperties = new ThreatProperties();
                        }
                        else
                        {
                            beanProperties = new GovernanceDriverProperties();
                        }
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_POLICY.typeName))
                    {
                        if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_PRINCIPLE.typeName))
                        {
                            beanProperties = new GovernancePrincipleProperties();
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_OBLIGATION.typeName))
                        {
                            beanProperties = new GovernanceObligationProperties();
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_APPROACH.typeName))
                        {
                            beanProperties = new GovernanceApproachProperties();
                        }
                        else
                        {
                            beanProperties = new GovernancePolicyProperties();
                        }
                    }
                    else
                    {
                        beanProperties = new GovernanceDefinitionProperties();
                    }

                    /*
                     * These are the standard properties for a governance definition.
                     */
                    ((GovernanceDefinitionProperties)beanProperties).setIdentifier(this.removeIdentifier(elementProperties));
                    ((GovernanceDefinitionProperties)beanProperties).setSummary(this.removeSummary(elementProperties));
                    ((GovernanceDefinitionProperties)beanProperties).setScope(this.removeScope(elementProperties));
                    ((GovernanceDefinitionProperties)beanProperties).setUsage(this.removeUsage(elementProperties));
                    ((GovernanceDefinitionProperties)beanProperties).setDomainIdentifier(this.removeDomainIdentifier(elementProperties));
                    ((GovernanceDefinitionProperties)beanProperties).setImportance(this.removeImportance(elementProperties));
                    ((GovernanceDefinitionProperties)beanProperties).setImplications(this.removeImplications(elementProperties));
                    ((GovernanceDefinitionProperties)beanProperties).setOutcomes(this.removeOutcomes(elementProperties));
                    ((GovernanceDefinitionProperties)beanProperties).setResults(this.removeResults(elementProperties));
                    ((GovernanceDefinitionProperties)beanProperties).setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SCHEMA_ELEMENT.typeName))
                {
                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SCHEMA_TYPE.typeName))
                    {
                        if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SIMPLE_SCHEMA_TYPE.typeName))
                        {
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName))
                            {
                                beanProperties = new PrimitiveSchemaTypeProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ENUM_SCHEMA_TYPE.typeName))
                            {
                                beanProperties = new EnumSchemaTypeProperties();
                            }
                            else
                            {
                                beanProperties = new SimpleSchemaTypeProperties();
                            }

                            ((SimpleSchemaTypeProperties) beanProperties).setDataType(this.removeDataType(elementProperties));
                            ((SimpleSchemaTypeProperties) beanProperties).setDefaultValue(this.removeDefaultValue(elementProperties));
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.LITERAL_SCHEMA_TYPE.typeName))
                        {
                            beanProperties = new LiteralSchemaTypeProperties();

                            ((LiteralSchemaTypeProperties) beanProperties).setDataType(this.removeDataType(elementProperties));
                            ((LiteralSchemaTypeProperties) beanProperties).setFixedValue(this.removeFixedValue(elementProperties));
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SCHEMA_TYPE_CHOICE.typeName))
                        {
                            beanProperties = new SchemaTypeChoiceProperties();
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.EXTERNAL_SCHEMA_TYPE.typeName))
                        {
                            beanProperties = new ExternalSchemaTypeProperties();
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.MAP_SCHEMA_TYPE.typeName))
                        {
                            beanProperties = new MapSchemaTypeProperties();
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.COMPLEX_SCHEMA_TYPE.typeName))
                        {
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.STRUCT_SCHEMA_TYPE.typeName))
                            {
                                beanProperties = new StructSchemaTypeProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ROOT_SCHEMA_TYPE.typeName))
                            {
                                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TABULAR_SCHEMA_TYPE.typeName))
                                {
                                    beanProperties = new TabularSchemaTypeProperties();
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ROOT_SCHEMA_TYPE.typeName))
                                {
                                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.RELATIONAL_DB_SCHEMA_TYPE.typeName))
                                    {
                                        beanProperties = new RelationalDBSchemaTypeProperties();
                                    }
                                    else
                                    {
                                        beanProperties = new RootSchemaTypeProperties();
                                    }
                                }
                                else
                                {
                                    beanProperties = new RootSchemaTypeProperties();
                                }
                            }
                            else
                            {
                                beanProperties = new ComplexSchemaTypeProperties();
                            }
                        }
                        else
                        {
                            beanProperties = new SchemaTypeProperties();
                        }

                        ((SchemaTypeProperties) beanProperties).setAuthor(this.removeAuthor(elementProperties));
                        ((SchemaTypeProperties) beanProperties).setUsage(this.removeUsage(elementProperties));
                        ((SchemaTypeProperties) beanProperties).setEncodingStandard(this.removeEncodingStandard(elementProperties));
                        ((SchemaTypeProperties) beanProperties).setNamespace(this.removeNamespace(elementProperties));
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SCHEMA_ATTRIBUTE.typeName))
                    {
                        if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TABULAR_COLUMN.typeName))
                        {
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TABULAR_FILE_COLUMN.typeName))
                            {
                                beanProperties = new TabularFileColumnProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.RELATIONAL_COLUMN.typeName))
                            {
                                beanProperties = new RelationalColumnProperties();
                            }
                            else
                            {
                                beanProperties = new TabularColumnProperties();
                            }
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DOCUMENT_SCHEMA_ATTRIBUTE.typeName))
                        {
                            beanProperties = new SchemaAttributeProperties();
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.RELATIONAL_TABLE.typeName))
                        {
                            beanProperties = new RelationalTableProperties();
                        }
                        else
                        {
                            beanProperties = new SchemaAttributeProperties();
                        }

                        ((SchemaAttributeProperties) beanProperties).setAllowsDuplicateValues(this.removeAllowsDuplicateValues(elementProperties));
                        ((SchemaAttributeProperties) beanProperties).setOrderedValues(this.removeOrderedValues(elementProperties));
                        ((SchemaAttributeProperties) beanProperties).setDefaultValueOverride(this.removeDefaultValueOverride(elementProperties));
                        ((SchemaAttributeProperties) beanProperties).setMinimumLength(this.removeMinimumLength(elementProperties));
                        ((SchemaAttributeProperties) beanProperties).setLength(this.removeLength(elementProperties));
                        ((SchemaAttributeProperties) beanProperties).setPrecision(this.removePrecision(elementProperties));
                        ((SchemaAttributeProperties) beanProperties).setIsNullable(this.removeIsNullable(elementProperties));
                        ((SchemaAttributeProperties) beanProperties).setNativeClass(this.removeNativeClass(elementProperties));
                        ((SchemaAttributeProperties) beanProperties).setAliases(this.removeAliases(elementProperties));
                        ((SchemaAttributeProperties) beanProperties).setSortOrder(this.removeDataItemSortOrder(elementProperties));
                    }
                    else
                    {
                        beanProperties = new SchemaElementProperties();
                    }

                    ((SchemaElementProperties)beanProperties).setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                }
                else
                {
                    beanProperties = new ReferenceableProperties();
                }

                /*
                 * These are the standard properties for a Referenceable.
                 */
                ((ReferenceableProperties)beanProperties).setQualifiedName(this.removeQualifiedName(elementProperties));
                ((ReferenceableProperties)beanProperties).setDisplayName(this.removeDisplayName(elementProperties));
                ((ReferenceableProperties)beanProperties).setDescription(this.removeDescription(elementProperties));
                ((ReferenceableProperties)beanProperties).setVersionIdentifier(this.removeVersionIdentifier(elementProperties));
                ((ReferenceableProperties)beanProperties).setCategory(this.removeCategory(elementProperties));
                ((ReferenceableProperties)beanProperties).setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
            }
            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.LIKE.typeName))
            {
                beanProperties = new LikeProperties();

                ((LikeProperties)beanProperties).setEmoji(this.removeEmoji(elementProperties));
            }
            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.RATING.typeName))
            {
                beanProperties = new RatingProperties();

                ((RatingProperties)beanProperties).setReview(this.removeReview(elementProperties));
                ((RatingProperties)beanProperties).setStarRating(this.removeStarRating(elementProperties));
            }
            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SEARCH_KEYWORD.typeName))
            {
                beanProperties = new SearchKeywordProperties();

                ((SearchKeywordProperties)beanProperties).setKeyword(this.removeKeyword(elementProperties));
                ((SearchKeywordProperties)beanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else
            {
                beanProperties = new OpenMetadataRootProperties();
            }


            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype that we don't know about - but it also helps to catch
             * missing mappings in the code above.
             */
            beanProperties.setTypeName(openMetadataElement.getType().getTypeName());
            beanProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

            return beanProperties;
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
        }

        return null;
    }


    /**
     * Using the supplied entity, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param openMetadataElement openMetadataElement containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the entity supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>            beanClass,
                        OpenMetadataElement openMetadataElement,
                        String              methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof OpenMetadataRootElement bean)
            {
                bean.setElementHeader(super.getMetadataElementHeader(beanClass, openMetadataElement, methodName));
                bean.setProperties(getBeanProperties(beanClass, openMetadataElement, methodName));
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied relatedMetadataElement, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param relatedMetadataElement relatedMetadataElement containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the relatedMetadataElement supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>               beanClass,
                        RelatedMetadataElement relatedMetadataElement,
                        String                 methodName) throws PropertyServerException
    {
        B returnBean = this.getNewBean(beanClass, relatedMetadataElement.getElement(), methodName);

        if (returnBean instanceof OpenMetadataRootElement bean)
        {
            bean.setRelatedBy(super.getRelatedBy(beanClass, relatedMetadataElement, methodName));
        }

        return returnBean;
    }


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that
     * contain a combination of the properties from an element and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param element element containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>                 beanClass,
                        OpenMetadataElement      element,
                        OpenMetadataRelationship relationship,
                        String                   methodName) throws PropertyServerException
    {
        B returnBean = this.getNewBean(beanClass, element, methodName);

        if (returnBean instanceof OpenMetadataRootElement bean)
        {
            bean.setRelatedBy(super.getRelatedBy(beanClass, relationship, methodName));
        }

        return returnBean;
    }
}
