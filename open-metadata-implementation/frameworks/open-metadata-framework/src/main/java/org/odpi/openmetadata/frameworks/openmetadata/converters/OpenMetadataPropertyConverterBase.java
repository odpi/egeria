/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.GovernanceRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.analytics.AnalyticsModelRunProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.analytics.DeployedAnalyticsModelProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.metadatarepositories.CohortRegistryStoreProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.metadatarepositories.MetadataCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.metadatarepositories.MetadataRepositoryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.referencedata.ReferenceCodeMappingTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.referencedata.ReferenceCodeTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.referencedata.ReferenceDataProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.SurveyReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.topics.AssociatedLogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.topics.TopicProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CommunityMemberProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CommunityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CrowdSourcingContributionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CrowdSourcingContributorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingDescriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingPurposeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.ConceptDesignProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.ConceptModelProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.DesignModelFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.DesignModelProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementationResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.operatingplatforms.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.propertyfacets.PropertyFacetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.propertyfacets.ReferenceableFacetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.MoreInformationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIOperationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIParameterListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIParameterProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APISchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalDBSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.events.EventSchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.events.EventTypeListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.events.EventTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularFileColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.CatalogTemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateSubstituteProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * OpenMetadataPropertyConverterBase provides the methods for converting each of the Open Metadata properties from
 * an element property value to a bean property.
 */
public class OpenMetadataPropertyConverterBase
{
    protected PropertyHelper propertyHelper;
    protected String         localServiceName;


    /**
     * Constructor captures the initial content
     *
     * @param propertyHelper helper object to parse element
     * @param localServiceName name of this component
     */
    public OpenMetadataPropertyConverterBase(PropertyHelper propertyHelper,
                                             String         localServiceName)
    {
        this.propertyHelper   = propertyHelper;
        this.localServiceName = localServiceName;
    }


    /**
     * Extract and delete the ActivityStatus property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return KeyPattern enum
     */
    protected ActivityStatus removeActivityStatus(ElementProperties elementProperties)
    {
        final String methodName = "removeActivityStatus";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                                         elementProperties,
                                                                         methodName);

            for (ActivityStatus status : ActivityStatus.values())
            {
                if (status.getName().equals(retrievedProperty))
                {
                    return status;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the qualifiedName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeQualifiedName(ElementProperties  elementProperties)
    {
        final String methodName = "removeQualifiedName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.QUALIFIED_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the qualifiedName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return map or null
     */
    protected Map<String, String> removeAdditionalProperties(ElementProperties  elementProperties)
    {
        final String methodName = "removeAdditionalProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Convert the remaining properties into a map that is returned as the extended properties.
     *
     * @param elementProperties properties from element
     * @return map or null
     */
    public Map<String, Object> getRemainingExtendedProperties(ElementProperties  elementProperties)
    {
        if (elementProperties != null)
        {
            return propertyHelper.getElementPropertiesAsMap(elementProperties);
        }

        return null;
    }


    /**
     * Extract and delete the displayName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeDisplayName(ElementProperties  elementProperties)
    {
        final String methodName = "removeDisplayName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DISPLAY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the resource name property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeResourceName(ElementProperties  elementProperties)
    {
        final String methodName = "removeResourceName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.RESOURCE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the version identifier property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeVersionIdentifier(ElementProperties  elementProperties)
    {
        final String methodName = "removeVersionIdentifier";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.VERSION_IDENTIFIER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the description property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    public String removeDescription(ElementProperties  elementProperties)
    {
        final String methodName = "removeDescription";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DESCRIPTION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeActorName(ElementProperties  elementProperties)
    {
        final String methodName = "removeActorName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ACTOR_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeAgreementItemId(ElementProperties  elementProperties)
    {
        final String methodName = "removeAgreementItemId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.AGREEMENT_ITEM_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeAgreementStart(ElementProperties  elementProperties)
    {
        final String methodName = "removeAgreementStart";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.AGREEMENT_START.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeAgreementEnd(ElementProperties  elementProperties)
    {
        final String methodName = "removeAgreementEnd";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.AGREEMENT_END.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Map<String, String> removeUsageMeasurements(ElementProperties  elementProperties)
    {
        final String methodName = "removeUsageMeasurements";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.USAGE_MEASUREMENTS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeContractId(ElementProperties  elementProperties)
    {
        final String methodName = "removeContractId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONTRACT_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeContractLiaison(ElementProperties  elementProperties)
    {
        final String methodName = "removeContractLiaison";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONTRACT_LIAISON.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeContractLiaisonTypeName(ElementProperties  elementProperties)
    {
        final String methodName = "removeContractLiaisonTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONTRACT_LIAISON_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeContractLiaisonPropertyName(ElementProperties  elementProperties)
    {
        final String methodName = "removeContractLiaisonPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONTRACT_LIAISON_PROPERTY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the keyword property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeKeyword(ElementProperties  elementProperties)
    {
        final String methodName = "removeKeyword";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.KEYWORD.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the topicType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeTopicType(ElementProperties  elementProperties)
    {
        final String methodName = "removeTopicType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.TOPIC_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the topicName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeTopicName(ElementProperties  elementProperties)
    {
        final String methodName = "removeTopicName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.TOPIC_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeOperationName(ElementProperties  elementProperties)
    {
        final String methodName = "removeOperationName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.OPERATION_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the operatingSystem property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeOperatingSystem(ElementProperties  elementProperties)
    {
        final String methodName = "removeOperatingSystem";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.OPERATING_SYSTEM.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the operatingSystemPatchLevel property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeOperatingSystemPatchLevel(ElementProperties  elementProperties)
    {
        final String methodName = "removeOperatingSystemPatchLevel";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.OPERATING_SYSTEM_PATCH_LEVEL.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return int
     */
    protected int removeExpectedTimeAllocationPercent(ElementProperties  elementProperties)
    {
        final String methodName = "removeExpectedTimeAllocationPercent";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.EXPECTED_TIME_ALLOCATION_PERCENT.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return int
     */
    protected int removeMinimumInstances(ElementProperties  elementProperties)
    {
        final String methodName = "removeMinimumInstances";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.MINIMUM_INSTANCES.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return int
     */
    protected int removeMaximumInstances(ElementProperties  elementProperties)
    {
        final String methodName = "removeMaximumInstances";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.MAXIMUM_INSTANCES.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the initials property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeInitials(ElementProperties  elementProperties)
    {
        final String methodName = "removeInitials";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.INITIALS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the courtesyTitle property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCourtesyTitle(ElementProperties  elementProperties)
    {
        final String methodName = "removeCourtesyTitle";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.COURTESY_TITLE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the givenNames property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeGivenNames(ElementProperties  elementProperties)
    {
        final String methodName = "removeGivenNames";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.GIVEN_NAMES.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the surname property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeSurname(ElementProperties  elementProperties)
    {
        final String methodName = "removeSurname";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SURNAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the fullName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeFullName(ElementProperties  elementProperties)
    {
        final String methodName = "removeFullName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.FULL_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the preferredLanguage property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePreferredLanguage(ElementProperties  elementProperties)
    {
        final String methodName = "removePreferredLanguage";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PREFERRED_LANGUAGE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the residentCountry property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeResidentCountry(ElementProperties  elementProperties)
    {
        final String methodName = "removeResidentCountry";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.RESIDENT_COUNTRY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the jobTitle property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeJobTitle(ElementProperties  elementProperties)
    {
        final String methodName = "removeJobTitle";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.JOB_TITLE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the employeeNumber property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeEmployeeNumber(ElementProperties  elementProperties)
    {
        final String methodName = "removeEmployeeNumber";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EMPLOYEE_NUMBER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the employeeType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeEmployeeType(ElementProperties  elementProperties)
    {
        final String methodName = "removeEmployeeType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EMPLOYEE_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the contactType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeContactType(ElementProperties  elementProperties)
    {
        final String methodName = "removeContactType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONTACT_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the contactMethodService property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeContactMethodService(ElementProperties  elementProperties)
    {
        final String methodName = "removeContactMethodService";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONTACT_METHOD_SERVICE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the contactMethodValue property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeContactMethodValue(ElementProperties  elementProperties)
    {
        final String methodName = "removeContactMethodValue";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONTACT_METHOD_VALUE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the mission property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeMission(ElementProperties  elementProperties)
    {
        final String methodName = "removeMission";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.MISSION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePath(ElementProperties  elementProperties)
    {
        final String methodName = "removePath";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PATH.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCommand(ElementProperties  elementProperties)
    {
        final String methodName = "removeCommand";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.COMMAND.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the associationType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeAssignmentType(ElementProperties  elementProperties)
    {
        final String methodName = "removeAssignmentType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ASSIGNMENT_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeAssociationType(ElementProperties  elementProperties)
    {
        final String methodName = "removeAssociationType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ASSOCIATION_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeAssociationName(ElementProperties  elementProperties)
    {
        final String methodName = "removeAssociationName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ASSOCIATION_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the identifier property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeIdentifier(ElementProperties  elementProperties)
    {
        final String methodName = "removeIdentifier";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.IDENTIFIER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the identifier property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeKey(ElementProperties  elementProperties)
    {
        final String methodName = "removeKey";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.KEY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the background property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeBackground(ElementProperties  elementProperties)
    {
        final String methodName = "removeBackground";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.BACKGROUND.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the incidentClassifiers map property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return map
     */
    protected Map<String, Integer> removeIncidentClassifiers(ElementProperties  elementProperties)
    {
        final String methodName = "removeIncidentClassifiers";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntegerMapFromProperty(localServiceName,
                                                               OpenMetadataProperty.INCIDENT_CLASSIFIERS.name,
                                                               elementProperties,
                                                               methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeExternalInstanceTypeName(ElementProperties  elementProperties)
    {
        final String methodName = "removeExternalInstanceTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EXT_INSTANCE_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the externalInstanceCreatedBy property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeExternalInstanceCreatedBy(ElementProperties  elementProperties)
    {
        final String methodName = "removeExternalInstanceCreatedBy";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EXT_INSTANCE_CREATED_BY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the externalInstanceCreationTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeExternalInstanceCreationTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeExternalInstanceCreationTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.EXT_INSTANCE_CREATION_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the externalInstanceLastUpdatedBy property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeExternalInstanceLastUpdatedBy(ElementProperties  elementProperties)
    {
        final String methodName = "removeExternalInstanceLastUpdatedBy";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EXT_INSTANCE_LAST_UPDATED_BY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the externalInstanceLastUpdateTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeExternalInstanceLastUpdateTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeExternalInstanceCreationTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.EXT_INSTANCE_LAST_UPDATE_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the externalInstanceVersion property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected long removeExternalInstanceVersion(ElementProperties  elementProperties)
    {
        final String methodName = "removeExternalInstanceVersion";

        if (elementProperties != null)
        {
            return propertyHelper.removeLongProperty(localServiceName,
                                                     OpenMetadataProperty.EXT_INSTANCE_VERSION.name,
                                                     elementProperties,
                                                     methodName);
        }

        return 0L;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeMethod(ElementProperties  elementProperties)
    {
        final String methodName = "removeMethod";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.METHOD.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the threshold property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return int
     */
    protected int removeThreshold(ElementProperties  elementProperties)
    {
        final String methodName = "removeThreshold";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.THRESHOLD.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the matchThreshold property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return int
     */
    protected int removeMatchThreshold(ElementProperties  elementProperties)
    {
        final String methodName = "removeMatchThreshold";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.MATCH_THRESHOLD.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 100;
    }



    /**
     * Extract and delete the numberOfPages property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return int
     */
    protected int removeNumberOfPages(ElementProperties  elementProperties)
    {
        final String methodName = "removeNumberOfPages";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.NUMBER_OF_PAGES.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the URL property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeURL(ElementProperties  elementProperties)
    {
        final String methodName = "removeURL";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.URL.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePageRange(ElementProperties  elementProperties)
    {
        final String methodName = "removePageRange";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PAGE_RANGE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePublicationSeries(ElementProperties  elementProperties)
    {
        final String methodName = "removePublicationSeries";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PUBLICATION_SERIES.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePublicationSeriesVolume(ElementProperties  elementProperties)
    {
        final String methodName = "removePublicationSeriesVolume";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PUBLICATION_SERIES_VOLUME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePublisher(ElementProperties  elementProperties)
    {
        final String methodName = "removePublisher";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PUBLISHER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeEdition(ElementProperties  elementProperties)
    {
        final String methodName = "removeEdition";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EDITION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeFirstPublicationDate(ElementProperties  elementProperties)
    {
        final String methodName = "removeFirstPublicationDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.FIRST_PUB_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removePublicationDate(ElementProperties  elementProperties)
    {
        final String methodName = "removePublicationDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.PUBLICATION_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePublicationCity(ElementProperties  elementProperties)
    {
        final String methodName = "removePublicationCity";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PUBLICATION_CITY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePublicationYear(ElementProperties  elementProperties)
    {
        final String methodName = "removePublicationYear";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PUBLICATION_YEAR.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected List<String> removePublicationNumbers(ElementProperties  elementProperties)
    {
        final String methodName = "removePublicationNumbers";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.PUBLICATION_NUMBERS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeOrganization(ElementProperties  elementProperties)
    {
        final String methodName = "removeOrganization";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ORGANIZATION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeOrganizationPropertyName(ElementProperties  elementProperties)
    {
        final String methodName = "removeOrganizationPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ORGANIZATION_PROPERTY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeReferenceId(ElementProperties  elementProperties)
    {
        final String methodName = "removeReferenceId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.REFERENCE_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePages(ElementProperties  elementProperties)
    {
        final String methodName = "removePages";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PAGES.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the referenceTitle property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeReferenceTitle(ElementProperties  elementProperties)
    {
        final String methodName = "removeReferenceTitle";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.REFERENCE_TITLE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the license property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeLicense(ElementProperties  elementProperties)
    {
        final String methodName = "removeLicense";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LICENSE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the mediaTypeOtherId property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeMediaTypeOtherId(ElementProperties  elementProperties)
    {
        final String methodName = "removeMediaTypeOtherId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.MEDIA_TYPE_OTHER_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the  property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeMediaId(ElementProperties  elementProperties)
    {
        final String methodName = "removeMediaId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.MEDIA_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDefaultMediaUsageOtherId(ElementProperties  elementProperties)
    {
        final String methodName = "removeDefaultMediaUsageOtherId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DEFAULT_MEDIA_USAGE_OTHER_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeMediaUsageOtherId(ElementProperties  elementProperties)
    {
        final String methodName = "removeMediaUsageOtherId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.MEDIA_USAGE_OTHER_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the copyright property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCopyright(ElementProperties  elementProperties)
    {
        final String methodName = "removeCopyright";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.COPYRIGHT.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the attribution property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeAttribution(ElementProperties  elementProperties)
    {
        final String methodName = "removeAttribution";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ATTRIBUTION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the authors property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected List<String> removeAuthors(ElementProperties  elementProperties)
    {
        final String methodName = "removeAuthors";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.AUTHORS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the sources property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Map<String, String> removeSources(ElementProperties  elementProperties)
    {
        final String methodName = "removeSources";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.SOURCES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }



    /**
     * Extract and delete the referenceAbstract property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeReferenceAbstract(ElementProperties  elementProperties)
    {
        final String methodName = "removeReferenceAbstract";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.REFERENCE_ABSTRACT.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the orderPropertyName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeOrderPropertyName(ElementProperties  elementProperties)
    {
        final String methodName = "removeOrderPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ORDER_BY_PROPERTY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeMembershipRationale(ElementProperties  elementProperties)
    {
        final String methodName = "removeMembershipRationale";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.MEMBERSHIP_RATIONALE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeMembershipType(ElementProperties  elementProperties)
    {
        final String methodName = "removeMembershipType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.MEMBERSHIP_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the teamType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeTeamType(ElementProperties  elementProperties)
    {
        final String methodName = "removeTeamType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.TEAM_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the mappingProperties property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return map or null
     */
    protected Map<String, String> removeMappingProperties(ElementProperties  elementProperties)
    {
        final String methodName = "removeMappingProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.MAPPING_PROPERTIES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }



    /**
     * Extract and delete the lastSynchronized property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return map or null
     */
    protected Date removeLastSynchronized(ElementProperties  elementProperties)
    {
        final String methodName = "removeLastSynchronized";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.LAST_SYNCHRONIZED.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }



    /**
     * Extract and delete the networkAddress property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeNetworkAddress(ElementProperties  elementProperties)
    {
        final String methodName = "removeNetworkAddress";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.NETWORK_ADDRESS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the postalAddress property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePostalAddress(ElementProperties  elementProperties)
    {
        final String methodName = "removePostalAddress";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.POSTAL_ADDRESS.name,
                                                       elementProperties,
                                                       methodName);

        }

        return null;
    }


    /**
     * Extract and delete the "coordinates" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCoordinates(ElementProperties  elementProperties)
    {
        final String methodName = "removeCoordinates";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.COORDINATES.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the mapProjection property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeMapProjection(ElementProperties  elementProperties)
    {
        final String methodName = "removeMapProjection";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.MAP_PROJECTION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the timeZone property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeTimeZone(ElementProperties  elementProperties)
    {
        final String methodName = "removeTimeZone";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.TIME_ZONE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the level property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeLevel(ElementProperties  elementProperties)
    {
        final String methodName = "removeLevel";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LEVEL.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the protocol property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeProtocol(ElementProperties  elementProperties)
    {
        final String methodName = "removeProtocol";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PROTOCOL.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the encryption method property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeEncryptionMethod(ElementProperties  elementProperties)
    {
        final String methodName = "removeEncryptionMethod";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ENCRYPTION_METHOD.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the connector provider class name property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeConnectorProviderClassName(ElementProperties  elementProperties)
    {
        final String methodName = "removeConnectorProviderClassName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONNECTOR_PROVIDER_CLASS_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the supported asset type name property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeSupportedAssetTypeName(ElementProperties  elementProperties)
    {
        final String methodName = "removeSupportedAssetTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SUPPORTED_ASSET_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the supported deployed implementation type name property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeSupportedDeployedImplementationType(ElementProperties  elementProperties)
    {
        final String methodName = "removeSupportedDeployedImplementationType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SUPPORTED_DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the expected data format property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeExpectedDataFormat(ElementProperties  elementProperties)
    {
        final String methodName = "removeExpectedDataFormat";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EXPECTED_DATA_FORMAT.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the connector id property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeConnectorId(ElementProperties  elementProperties)
    {
        final String methodName = "removeConnectorId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONNECTOR_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeConnectorName(ElementProperties  elementProperties)
    {
        final String methodName = "removeConnectorName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONNECTOR_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeConnectorUserId(ElementProperties  elementProperties)
    {
        final String methodName = "removeConnectorUserId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONNECTOR_USER_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the server name property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeServerName(ElementProperties  elementProperties)
    {
        final String methodName = "removeServerName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SERVER_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the connector start date property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeConnectorStartDate(ElementProperties  elementProperties)
    {
        final String methodName = "removeConnectorStartDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.CONNECTOR_START_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }



    /**
     * Extract and delete the refresh start date property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeRefreshStartDate(ElementProperties  elementProperties)
    {
        final String methodName = "removeRefreshStartDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.REFRESH_START_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }



    /**
     * Extract and delete the refresh completion data  property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeRefreshCompletionDate(ElementProperties  elementProperties)
    {
        final String methodName = "removeRefreshCompletionDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.REFRESH_COMPLETION_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }



    /**
     * Extract and delete the connector disconnect data property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeConnectorDisconnectDate(ElementProperties  elementProperties)
    {
        final String methodName = "removeConnectorDisconnectDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.CONNECTOR_DISCONNECT_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }



    /**
     * Extract and delete the connector framework name property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeConnectorFrameworkName(ElementProperties  elementProperties)
    {
        final String methodName = "removeConnectorFrameworkName";

        if (elementProperties != null)
        {
            String connectorFrameworkName = propertyHelper.removeStringProperty(localServiceName,
                                                                                OpenMetadataProperty.CONNECTOR_FRAMEWORK_NAME.name,
                                                                                elementProperties,
                                                                                methodName);
            if (connectorFrameworkName != null)
            {
                return connectorFrameworkName;
            }
        }

        return OpenMetadataValidValues.CONNECTOR_FRAMEWORK_NAME_DEFAULT;
    }


    /**
     * Extract and delete the connector interface language property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeConnectorInterfaceLanguage(ElementProperties  elementProperties)
    {
        final String methodName = "removeConnectorInterfaceLanguage";

        if (elementProperties != null)
        {
            String connectorInterfaceLanguage = propertyHelper.removeStringProperty(localServiceName,
                                                                                    OpenMetadataProperty.CONNECTOR_INTERFACE_LANGUAGE.name,
                                                                                    elementProperties,
                                                                                    methodName);
            if (connectorInterfaceLanguage != null)
            {
                return connectorInterfaceLanguage;
            }
        }

        return OpenMetadataValidValues.CONNECTOR_INTERFACE_LANGUAGE_DEFAULT;
    }


    /**
     * Extract and delete the connector interfaces property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeConnectorInterfaces(ElementProperties  elementProperties)
    {
        final String methodName = "removeConnectorInterfaces";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.CONNECTOR_INTERFACES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }



    /**
     * Extract and delete the created elements property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeCreatedElements(ElementProperties  elementProperties)
    {
        final String methodName = "removeCreatedElements";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.CREATED_ELEMENTS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the updated elements property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeUpdatedElements(ElementProperties  elementProperties)
    {
        final String methodName = "removeUpdatedElements";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.UPDATED_ELEMENTS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the updated elements property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeDeletedElements(ElementProperties  elementProperties)
    {
        final String methodName = "removeDeletedElements";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.DELETED_ELEMENTS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }

    /**
     * Extract and delete the target technology source property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeTargetTechnologySource(ElementProperties  elementProperties)
    {
        final String methodName = "removeTargetTechnologySource";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.TARGET_TECHNOLOGY_SOURCE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the target technology name property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeTargetTechnologyName(ElementProperties  elementProperties)
    {
        final String methodName = "removeTargetTechnologyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.TARGET_TECHNOLOGY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the target technology interfaces property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeTargetTechnologyInterfaces(ElementProperties  elementProperties)
    {
        final String methodName = "removeTargetTechnologyInterfaces";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.TARGET_TECHNOLOGY_INTERFACES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the target technology versions property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeTargetTechnologyVersions(ElementProperties  elementProperties)
    {
        final String methodName = "removeTargetTechnologyVersions";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.TARGET_TECHNOLOGY_VERSIONS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the recognizedAdditionalProperties property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeRecognizedAdditionalProperties(ElementProperties  elementProperties)
    {
        final String methodName = "removeRecognizedAdditionalProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.RECOGNIZED_ADDITIONAL_PROPERTIES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the recognizedSecuredProperties property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeRecognizedSecuredProperties(ElementProperties  elementProperties)
    {
        final String methodName = "removeRecognizedSecuredProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.RECOGNIZED_SECURED_PROPERTIES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }



    /**
     * Extract and delete the recognized configuration properties property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeRecognizedConfigurationProperties(ElementProperties  elementProperties)
    {
        final String methodName = "removeRecognizedConfigurationProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.RECOGNIZED_CONFIGURATION_PROPERTIES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the securedProperties property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected Map<String, String> removeSecuredProperties(ElementProperties  elementProperties)
    {
        final String methodName = "removeSecuredProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.SECURED_PROPERTIES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the  configuration properties property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected Map<String, Object> removeConfigurationProperties(ElementProperties  elementProperties)
    {
        final String methodName = "removeConfigurationProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeMapFromProperty(localServiceName,
                                                        OpenMetadataProperty.CONFIGURATION_PROPERTIES.name,
                                                        elementProperties,
                                                        methodName);
        }

        return null;
    }


    /**
     * Extract and delete the userId property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeUserId(ElementProperties  elementProperties)
    {
        final String methodName = "removeUserId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.USER_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the user property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeUser(ElementProperties  elementProperties)
    {
        final String methodName = "removeUser";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.USER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the user property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePurpose(ElementProperties  elementProperties)
    {
        final String methodName = "removePurpose";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PURPOSE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the clear password property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeClearPassword(ElementProperties  elementProperties)
    {
        final String methodName = "removeClearPassword";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CLEAR_PASSWORD.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the encrypted password property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeEncryptedPassword(ElementProperties  elementProperties)
    {
        final String methodName = "removeEncryptedPassword";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ENCRYPTED_PASSWORD.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract the "arguments" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Map<String, Object> removeArguments(ElementProperties  elementProperties)
    {
        final String methodName = "removeArguments";

        if (elementProperties != null)
        {
            return propertyHelper.removeMapFromProperty(localServiceName,
                                                     OpenMetadataProperty.ARGUMENTS.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Retrieve the zone membership from the properties of the zone membership classification.
     *
     * @param elementProperties properties from the classification
     * @return list of zone names
     */
    protected List<String> removeZoneMembership(ElementProperties elementProperties)
    {
        final String methodName = "removeZoneMembership";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Retrieve the subjectAreaName from the properties.
     *
     * @param elementProperties properties from the element
     * @return subject area name
     */
    protected String removeSubjectAreaName(ElementProperties elementProperties)
    {
        final String methodName = "removeSubjectAreaName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SUBJECT_AREA_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the owner property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeOwner(ElementProperties elementProperties)
    {
        final String methodName = "removeOwner";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.OWNER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the ownerPropertyName property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string
     */
    protected String removeOwnerPropertyName(ElementProperties elementProperties)

    {
        final String methodName = "removeClassificationPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.OWNER_PROPERTY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the ownerTypeName property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string
     */
    protected String removeOwnerTypeName(ElementProperties elementProperties)

    {
        final String methodName = "removeTypePropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.OWNER_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string
     */
    protected String removeRoleTypeName(ElementProperties elementProperties)

    {
        final String methodName = "removeRoleTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ROLE_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string
     */
    protected String removeRoleGUID(ElementProperties elementProperties)

    {
        final String methodName = "removeRoleGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ROLE_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the distinguishedName property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string
     */
    protected String removeDistinguishedName(ElementProperties elementProperties)

    {
        final String methodName = "removeDistinguishedName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DISTINGUISHED_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the "groups" property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string map or null
     */
    protected List<String> removeGroups(ElementProperties  elementProperties)
    {
        final String methodName = "removeGroups";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                         OpenMetadataProperty.GROUPS.name,
                                                         elementProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the securityLabels property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string map or null
     */
    protected List<String> removeSecurityLabels(ElementProperties  elementProperties)
    {
        final String methodName = "removeSecurityLabels";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                         OpenMetadataProperty.SECURITY_LABELS.name,
                                                         elementProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string map or null
     */
    protected Map<String, Object> removeSecurityProperties(ElementProperties  elementProperties)
    {
        final String methodName = "removeSecurityProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeMapFromProperty(localServiceName,
                                                        OpenMetadataProperty.SECURITY_PROPERTIES.name,
                                                        elementProperties,
                                                        methodName);
        }

        return null;
    }


    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string map or null
     */
    protected Map<String, List<String>> removeAccessGroups(ElementProperties  elementProperties)
    {
        final String methodName = "removeAccessGroups";

        if (elementProperties != null)
        {
            return propertyHelper.removeListStringMapFromProperty(localServiceName,
                                                                  OpenMetadataProperty.ACCESS_GROUPS.name,
                                                                  elementProperties,
                                                                  methodName);
        }

        return null;
    }


    /**
     * Extract the karmaPoints property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return ordinal or 0 for not specified
     */
    protected int removeKarmaPoints(ElementProperties elementProperties)
    {
        final String methodName = "removeKarmaPoints";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.KARMA_POINTS.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }



    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String removeBusinessCapability(ElementProperties  elementProperties)
    {
        final String methodName = "removeBusinessCapability";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.BUSINESS_CAPABILITY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String removeBusinessCapabilityPropertyName(ElementProperties  elementProperties)
    {
        final String methodName = "removeBusinessCapabilityPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.BUSINESS_CAPABILITY_PROPERTY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the otherOriginValues property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string map or null
     */
    protected Map<String, String> removeOtherOriginValues(ElementProperties  elementProperties)
    {
        final String methodName = "removeOtherOriginValues";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.OTHER_ORIGIN_VALUES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }



    /**
     * Extract and delete the resourceCreateTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date or null
     */
    protected Date removeResourceCreateTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeResourceCreateTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.RESOURCE_CREATE_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the resourceUpdateTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date or null
     */
    protected Date removeResourceUpdateTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeResourceUpdateTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.RESOURCE_UPDATE_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the resourceLastAccessedTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date or null
     */
    protected Date removeResourceLastAccessedTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeResourceLastAccessedTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.RESOURCE_LAST_ACCESSED_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the pathName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string or null
     */
    protected String removePathName(ElementProperties  elementProperties)
    {
        final String methodName = "removePathName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PATH_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string or null
     */
    protected String removeDelimiterCharacter(ElementProperties  elementProperties)
    {
        final String methodName = "removeDelimiterCharacter";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DELIMITER_CHARACTER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string or null
     */
    protected String removeQuoteCharacter(ElementProperties  elementProperties)
    {
        final String methodName = "removeQuoteCharacter";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.QUOTE_CHARACTER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the sourceCreateTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date or null
     */
    protected Date removeStoreCreateTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeStoreCreateTime";

        if (elementProperties != null)
        {
            Date createTime1 = propertyHelper.removeDateProperty(localServiceName,
                                                                 OpenMetadataProperty.STORE_CREATE_TIME.name,
                                                                 elementProperties,
                                                                 methodName);
            Date createTime2 = propertyHelper.removeDateProperty(localServiceName,
                                                                 OpenMetadataProperty.CREATE_TIME.name,
                                                                 elementProperties,
                                                                 methodName);
            return createTime1 == null ? createTime2 : createTime1;
        }

        return null;
    }


    /**
     * Extract and delete the storeUpdateTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date or null
     */
    protected Date removeStoreUpdateTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeStoreUpdateTime";

        if (elementProperties != null)
        {
            Date modifiedTime1 = propertyHelper.removeDateProperty(localServiceName,
                                                                   OpenMetadataProperty.STORE_UPDATE_TIME.name,
                                                                   elementProperties,
                                                                   methodName);
            Date modifiedTime2 = propertyHelper.removeDateProperty(localServiceName,
                                                                   OpenMetadataProperty.UPDATE_TIME.name,
                                                                   elementProperties,
                                                                   methodName);
            return modifiedTime1 == null ? modifiedTime2 : modifiedTime1;
        }

        return null;
    }


    /**
     * Extract the encoding property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String removeEncodingType(ElementProperties  elementProperties)
    {
        final String methodName = "removeEncodingType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ENCODING_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the encoding language property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String removeEncodingLanguage(ElementProperties  elementProperties)
    {
        final String methodName = "removeEncodingLanguage";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                    OpenMetadataProperty.ENCODING_LANGUAGE.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the encoding description property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String removeEncodingDescription(ElementProperties  elementProperties)
    {
        final String methodName = "removeEncodingDescription";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                    OpenMetadataProperty.ENCODING_DESCRIPTION.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the encoding properties property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string map or null
     */
    protected Map<String, String> removeEncodingProperties(ElementProperties  elementProperties)
    {
        final String methodName = "removeEncodingProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                           OpenMetadataProperty.ENCODING_PROPERTIES.name,
                                                           elementProperties,
                                                           methodName);
        }

        return null;
    }



    /**
     * Extract and delete the database instance property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeDatabaseInstance(ElementProperties  elementProperties)
    {
        final String methodName = "removeDatabaseInstance";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.INSTANCE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the database importedFrom property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeImportedFrom(ElementProperties  elementProperties)
    {
        final String methodName = "removeImportedFrom";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.IMPORTED_FROM.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the fileType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeFileType(ElementProperties  elementProperties)
    {
        final String methodName = "removeFileType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.FILE_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the format property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeFormat(ElementProperties  elementProperties)
    {
        final String methodName = "removeFormat";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                    OpenMetadataProperty.FORMAT.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract and delete the encryption property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeEncryption(ElementProperties  elementProperties)
    {
        final String methodName = "removeEncryption";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ENCRYPTION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDeployedImplementationType(ElementProperties  elementProperties)
    {
        final String methodName = "removeDeployedImplementationType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeProviderName(ElementProperties  elementProperties)
    {
        final String methodName = "removeProviderName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PROVIDER_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeOfferingName(ElementProperties  elementProperties)
    {
        final String methodName = "removeOfferingName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.OFFERING_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeServiceType(ElementProperties  elementProperties)
    {
        final String methodName = "removeServiceType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SERVICE_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeTenantName(ElementProperties  elementProperties)
    {
        final String methodName = "removeTenantName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.TENANT_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeTenantType(ElementProperties  elementProperties)
    {
        final String methodName = "removeTenantType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.TENANT_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the user defined status property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeUserDefinedStatus(ElementProperties  elementProperties)
    {
        final String methodName = "removeUserDefinedStatus";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeBusinessImplementationType(ElementProperties  elementProperties)
    {
        final String methodName = "removeBusinessImplementationType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.BUSINESS_IMPLEMENTATION_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the user defined activity status property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeUserDefinedActivityStatus(ElementProperties  elementProperties)
    {
        final String methodName = "removeUserDefinedActivityStatus";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.USER_DEFINED_ACTIVITY_STATUS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the product name property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeProductName(ElementProperties  elementProperties)
    {
        final String methodName = "removeProductName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PRODUCT_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the maturity property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeMaturity(ElementProperties  elementProperties)
    {
        final String methodName = "removeMaturity";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.MATURITY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the service life property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeServiceLife(ElementProperties  elementProperties)
    {
        final String methodName = "removeServiceLife";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SERVICE_LIFE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the current version property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCurrentVersion(ElementProperties  elementProperties)
    {
        final String methodName = "removeCurrentVersion";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CURRENT_VERSION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the introduction date property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeIntroductionDate(ElementProperties  elementProperties)
    {
        final String methodName = "removeIntroductionDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.INTRODUCTION_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the next version date property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeNextVersionDate(ElementProperties  elementProperties)
    {
        final String methodName = "removeNextVersionDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.NEXT_VERSION_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the withdraw date property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeWithdrawDate(ElementProperties  elementProperties)
    {
        final String methodName = "removeWithdrawDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.WITHDRAW_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the support levels property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeSupportLevel(ElementProperties  elementProperties)
    {
        final String methodName = "removeSupportLevel";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SUPPORT_LEVEL.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the service levels property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Map<String, String> removeServiceLevels(ElementProperties  elementProperties)
    {
        final String methodName = "removeServiceLevels";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.SERVICE_LEVELS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }



    /**
     * Extract and delete the patchLevel property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePatchLevel(ElementProperties  elementProperties)
    {
        final String methodName = "removePatchLevel";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PATCH_LEVEL.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Retrieve the isDefaultValue flag from the properties from the supplied element properties.
     *
     * @param elementProperties properties from the classification
     * @return boolean - default is false
     */
    protected boolean removeIsDefaultValue(ElementProperties  elementProperties)
    {
        final String methodName = "removeIsDefaultValue";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.IS_DEFAULT_VALUE.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
    }


    /**
     * Retrieve the flag from the properties from the supplied element properties.
     *
     * @param elementProperties properties from the classification
     * @return boolean - default is false
     */
    protected boolean removeDelegationEscalationAuthority(ElementProperties  elementProperties)
    {
        final String methodName = "removeDelegationEscalationAuthority";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.DELEGATION_ESCALATION.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
    }


    /**
     * Extract and delete the anchorGUID property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeAnchorGUID(ElementProperties  elementProperties)
    {
        final String methodName = "removeAnchorGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ANCHOR_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeAnchorTypeName(ElementProperties  elementProperties)
    {
        final String methodName = "removeAnchorTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ANCHOR_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeAnchorDomainName(ElementProperties  elementProperties)
    {
        final String methodName = "removeAnchorDomainName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ANCHOR_DOMAIN_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeAnchorScopeGUID(ElementProperties  elementProperties)
    {
        final String methodName = "removeAnchorScopeGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ANCHOR_SCOPE_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeOrganizingPrinciple(ElementProperties  elementProperties)
    {
        final String methodName = "removeOrganizingPrinciple";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ORGANIZING_PRINCIPLE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected float removeMinLongitude(ElementProperties  elementProperties)
    {
        final String methodName = "removeMinLongitude";

        if (elementProperties != null)
        {
            return propertyHelper.removeFloatProperty(localServiceName,
                                                       OpenMetadataProperty.MIN_LONGITUDE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return 0F;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected float removeMaxLongitude(ElementProperties  elementProperties)
    {
        final String methodName = "removeMaxLongitude";

        if (elementProperties != null)
        {
            return propertyHelper.removeFloatProperty(localServiceName,
                                                      OpenMetadataProperty.MAX_LONGITUDE.name,
                                                      elementProperties,
                                                      methodName);
        }

        return 0F;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected float removeMinLatitude(ElementProperties  elementProperties)
    {
        final String methodName = "removeMinLatitude";

        if (elementProperties != null)
        {
            return propertyHelper.removeFloatProperty(localServiceName,
                                                      OpenMetadataProperty.MIN_LATITUDE.name,
                                                      elementProperties,
                                                      methodName);
        }

        return 0F;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected float removeMaxLatitude(ElementProperties  elementProperties)
    {
        final String methodName = "removeMaxLatitude";

        if (elementProperties != null)
        {
            return propertyHelper.removeFloatProperty(localServiceName,
                                                      OpenMetadataProperty.MAX_LATITUDE.name,
                                                      elementProperties,
                                                      methodName);
        }

        return 0F;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected float removeMinHeight(ElementProperties  elementProperties)
    {
        final String methodName = "removeMaxHeight";

        if (elementProperties != null)
        {
            return propertyHelper.removeFloatProperty(localServiceName,
                                                      OpenMetadataProperty.MIN_HEIGHT.name,
                                                      elementProperties,
                                                      methodName);
        }

        return 0F;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected float removeMaxHeight(ElementProperties  elementProperties)
    {
        final String methodName = "removeMaxHeight";

        if (elementProperties != null)
        {
            return propertyHelper.removeFloatProperty(localServiceName,
                                                      OpenMetadataProperty.MAX_HEIGHT.name,
                                                      elementProperties,
                                                      methodName);
        }

        return 0F;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeArchiveProcess(ElementProperties  elementProperties)
    {
        final String methodName = "removeArchiveProcess";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ARCHIVE_PROCESS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }




    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected Date removeArchiveDate(ElementProperties  elementProperties)
    {
        final String methodName = "removeArchiveDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.ARCHIVE_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeArchiveUser(ElementProperties  elementProperties)
    {
        final String methodName = "removeArchiveUser";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ARCHIVE_USER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeArchiveService(ElementProperties  elementProperties)
    {
        final String methodName = "removeArchiveService";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ARCHIVE_SERVICE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeArchiveMethod(ElementProperties  elementProperties)
    {
        final String methodName = "removeArchiveMethod";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ARCHIVE_METHOD.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected Map<String,String> removeArchiveProperties(ElementProperties  elementProperties)
    {
        final String methodName = "removeArchiveProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.ARCHIVE_PROPERTIES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected Map<String,String> removeValues(ElementProperties  elementProperties)
    {
        final String methodName = "removeValues";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.VALUES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected Map<String,Boolean> removeFlags(ElementProperties  elementProperties)
    {
        final String methodName = "removeFlags";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.FLAGS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected Map<String,Integer> removeCounts(ElementProperties  elementProperties)
    {
        final String methodName = "removeCounts";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntegerMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.COUNTS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected Map<String,Date> removeDates(ElementProperties  elementProperties)
    {
        final String methodName = "removeDates";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateMapFromProperty(localServiceName,
                                                            OpenMetadataProperty.DATES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected Date removeDataCollectionStartTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeDataCollectionStartTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.DATA_COLLECTION_START_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected Date removeDataCollectionEndTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeDataCollectionEndTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.DATA_COLLECTION_END_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the data type property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeDataType(ElementProperties  elementProperties)
    {
        final String methodName = "removeDataType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DATA_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the units property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeUnits(ElementProperties  elementProperties)
    {
        final String methodName = "removeDataType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.UNITS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the defaultValue property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDefaultValue(ElementProperties  elementProperties)
    {
        final String methodName = "removeDefaultValue";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DEFAULT_VALUE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the defaultValue property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeFixedValue(ElementProperties  elementProperties)
    {
        final String methodName = "removeFixedValue";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.FIXED_VALUE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the createdTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeCreatedTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeCreatedTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.CREATED_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the createdTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeLastModifiedTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeLastModifiedTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.LAST_MODIFIED_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the lastModifier property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeLastModifier(ElementProperties  elementProperties)
    {
        final String methodName = "removeId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LAST_MODIFIER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the author property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeAuthor(ElementProperties  elementProperties)
    {
        final String methodName = "removeAuthor";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.AUTHOR.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the encoding standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeEncodingStandard(ElementProperties  elementProperties)
    {
        final String methodName = "removeEncodingStandard";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ENCODING_STANDARD.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the namespace property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeNamespace(ElementProperties  elementProperties)
    {
        final String methodName = "removeNamespace";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.NAMESPACE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the specification property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeSpecification(ElementProperties  elementProperties)
    {
        final String methodName = "removeSpecification";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SPECIFICATION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the position property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer - default 0
     */
    protected int removePosition(ElementProperties  elementProperties)
    {
        final String methodName = "removePosition";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.POSITION.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }



    /**
     * Extract and delete the minCardinality property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer - default 0
     */
    protected int removeMinCardinality(ElementProperties  elementProperties)
    {
        final String methodName = "removeMinCardinality";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.MIN_CARDINALITY.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the maxCardinality property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer - default -1 which is unlimited
     */
    protected int removeMaxCardinality(ElementProperties  elementProperties)
    {
        final String methodName = "removeMaxCardinality";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.MAX_CARDINALITY.name,
                                                    elementProperties,
                                                    methodName);
        }

        return -1;
    }


    /**
     * Retrieve the allowsDuplicateValues flag from the properties of the zone membership classification.
     *
     * @param elementProperties properties from the classification
     * @return boolean - default is true
     */
    protected boolean removeAllowsDuplicateValues(ElementProperties  elementProperties)
    {
        final String methodName = "removeAllowsDuplicateValues";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.ALLOWS_DUPLICATE_VALUES.name,
                                                        elementProperties,
                                                        methodName);
        }

        return true;
    }


    /**
     * Retrieve the orderedValues flag from the properties of the zone membership classification.
     *
     * @param elementProperties properties from the classification
     * @return boolean - default is false
     */
    protected boolean removeOrderedValues(ElementProperties  elementProperties)
    {
        final String methodName = "removeOrderedValues";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.ORDERED_VALUES.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
    }


    /**
     * Extract and delete the defaultValueOverride property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDefaultValueOverride(ElementProperties  elementProperties)
    {
        final String methodName = "removeDefaultValueOverride";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DEFAULT_VALUE_OVERRIDE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the minimumLength property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer - default 0
     */
    protected int removeMinimumLength(ElementProperties  elementProperties)
    {
        final String methodName = "removeMinimumLength";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.MINIMUM_LENGTH.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the length property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer - default 0
     */
    protected int removeLength(ElementProperties  elementProperties)
    {
        final String methodName = "removeLength";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.LENGTH.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the precision/significantDigits property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer - default 0
     */
    protected int removePrecision(ElementProperties  elementProperties)
    {
        final String methodName = "removePrecision";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.PRECISION.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Retrieve the isNullable flag from the properties from the supplied element properties.
     *
     * @param elementProperties properties from the classification
     * @return boolean - default is false
     */
    protected boolean removeIsNullable(ElementProperties  elementProperties)
    {
        final String methodName = "removeIsNullable";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.IS_NULLABLE.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
    }


    /**
     * Retrieve the required flag from the properties from the supplied element properties.
     *
     * @param elementProperties properties from the classification
     * @return boolean - default is false
     */
    protected boolean removeRequired(ElementProperties  elementProperties)
    {
        final String methodName = "removeRequired";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.REQUIRED.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
    }


    /**
     * Extract and delete the native class property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeNativeClass(ElementProperties  elementProperties)
    {
        final String methodName = "removeNativeClass";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.NATIVE_CLASS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the "aliases" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected List<String> removeAliases(ElementProperties  elementProperties)
    {
        final String methodName = "removeAliases";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.ALIASES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }



    /**
     * Extract and delete the "minutes" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected List<String> removeMinutes(ElementProperties  elementProperties)
    {
        final String methodName = "removeMinutes";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.MINUTES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the "decisions" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected List<String> removeDecisions(ElementProperties  elementProperties)
    {
        final String methodName = "removeDecisions";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.DECISIONS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }



    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeManagedMetadataCollectionId(ElementProperties  elementProperties)
    {
        final String methodName = "removeManagedMetadataCollectionId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.MANAGED_METADATA_COLLECTION_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the formula property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeFormula(ElementProperties  elementProperties)
    {
        final String methodName = "removeFormula";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.FORMULA.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeFormulaType(ElementProperties  elementProperties)
    {
        final String methodName = "removeFormulaType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.FORMULA_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeQueryType(ElementProperties  elementProperties)
    {
        final String methodName = "removeQueryType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.QUERY_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeQuery(ElementProperties  elementProperties)
    {
        final String methodName = "removeQuery";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.QUERY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeQueryId(ElementProperties  elementProperties)
    {
        final String methodName = "removeQueryId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.QUERY_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeLinkTypeName(ElementProperties  elementProperties)
    {
        final String methodName = "removeLinkTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LINK_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeRelationshipEndName(ElementProperties  elementProperties)
    {
        final String methodName = "removeRelationshipEndName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.RELATIONSHIP_END_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return int
     */
    protected int removeRelationshipEnd(ElementProperties  elementProperties)
    {
        final String methodName = "removeRelationshipEndName";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.RELATIONSHIP_END.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }



    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeDeploymentTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeDeploymentTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                       OpenMetadataProperty.DEPLOYMENT_TIME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDeployer(ElementProperties  elementProperties)
    {
        final String methodName = "removeDeployer";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DEPLOYER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDeployerTypeName(ElementProperties  elementProperties)
    {
        final String methodName = "removeDeployerTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DEPLOYER_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDeployerPropertyName(ElementProperties  elementProperties)
    {
        final String methodName = "removeDeployerPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DEPLOYER_PROPERTY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeLineNumber(ElementProperties  elementProperties)
    {
        final String methodName = "removeLineNumber";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LINE_NUMBER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and remove the implementationLanguage property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeImplementationLanguage(ElementProperties  elementProperties)
    {
        final String methodName = "removeImplementationLanguage";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.IMPLEMENTATION_LANGUAGE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and remove the objective property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeObjective(ElementProperties  elementProperties)
    {
        final String methodName = "removeObjective";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.OBJECTIVE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and remove the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeSystemAction(ElementProperties  elementProperties)
    {
        final String methodName = "removeSystemAction";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SYSTEM_ACTION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and remove the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeUserResponse(ElementProperties  elementProperties)
    {
        final String methodName = "removeUserResponse";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.USER_RESPONSE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and remove the usesBlockingCalls property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected boolean removeUsesBlockingCalls(ElementProperties  elementProperties)
    {
        final String methodName = "removeUsesBlockingCalls";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.USES_BLOCKING_CALLS.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
    }


    /**
     * Extract and delete the type property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeSource(ElementProperties  elementProperties)
    {
        final String methodName = "removeSource";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SOURCE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the usage property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeUsage(ElementProperties elementProperties)
    {
        final String methodName = "removeUsage";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.USAGE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the language property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeLanguage(ElementProperties elementProperties)
    {
        final String methodName = "removeLanguage";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LANGUAGE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and remove the summary property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected String removeSummary(ElementProperties elementProperties)
    {
        final String methodName = "removeSummary";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SUMMARY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and remove the abbreviation property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected String removeAbbreviation(ElementProperties elementProperties)
    {
        final String methodName = "removeAbbreviation";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ABBREVIATION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and remove the "examples" property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected String removeExamples(ElementProperties elementProperties)
    {
        final String methodName = "removeExamples";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EXAMPLES.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the "pronouns" property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected String removePronouns(ElementProperties elementProperties)
    {
        final String methodName = "removePronouns";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PRONOUNS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the priority property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected String removeImportance(ElementProperties elementProperties)
    {
        final String methodName = "removeImportance";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.IMPORTANCE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the priority integer property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected int removeIntPriority(ElementProperties elementProperties)
    {
        final String methodName = "removeIntPriority";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.PRIORITY.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract the headcount integer property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected int removeHeadCount(ElementProperties elementProperties)
    {
        final String methodName = "removeHeadCount";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.HEAD_COUNT.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the scope property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeScope(ElementProperties elementProperties)
    {
        final String methodName = "removeScope";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SCOPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the "implications" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeImplications(ElementProperties elementProperties)
    {
        final String methodName = "removeImplications";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.IMPLICATIONS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the "outcomes" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeOutcomes(ElementProperties elementProperties)
    {
        final String methodName = "removeOutcomes";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.OUTCOMES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the "results" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeResults(ElementProperties elementProperties)
    {
        final String methodName = "removeResults";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.RESULTS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the businessImperatives property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeBusinessImperatives(ElementProperties elementProperties)
    {
        final String methodName = "removeBusinessImperatives";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.BUSINESS_IMPERATIVES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeRegulationSource(ElementProperties elementProperties)
    {
        final String methodName = "removeRegulationSource";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.REGULATION_SOURCE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCertificateGUID(ElementProperties elementProperties)
    {
        final String methodName = "removeCertificateGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CERTIFICATE_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeConditions(ElementProperties elementProperties)
    {
        final String methodName = "removeConditions";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONDITIONS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCertifiedBy(ElementProperties elementProperties)
    {
        final String methodName = "removeCertifiedBy";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CERTIFIED_BY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCertifiedByTypeName(ElementProperties elementProperties)
    {
        final String methodName = "removeCertifiedByTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CERTIFIED_BY_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCertifiedByPropertyName(ElementProperties elementProperties)
    {
        final String methodName = "removeCertifiedByPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CERTIFIED_BY_PROPERTY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCustodian(ElementProperties elementProperties)
    {
        final String methodName = "removeCustodian";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CUSTODIAN.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCustodianTypeName(ElementProperties elementProperties)
    {
        final String methodName = "removeCustodianTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CUSTODIAN_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCustodianPropertyName(ElementProperties elementProperties)
    {
        final String methodName = "removeCustodianPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CUSTODIAN_PROPERTY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeRecipient(ElementProperties elementProperties)
    {
        final String methodName = "removeRecipient";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.RECIPIENT.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeRecipientTypeName(ElementProperties elementProperties)
    {
        final String methodName = "removeRecipientTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.RECIPIENT_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeRecipientPropertyName(ElementProperties elementProperties)
    {
        final String methodName = "removeRecipientPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.RECIPIENT_PROPERTY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeCoverageStart(ElementProperties elementProperties)
    {
        final String methodName = "removeCoverageStart";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.COVERAGE_START.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeCoverageEnd(ElementProperties elementProperties)
    {
        final String methodName = "removeCoverageEnd";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.COVERAGE_END.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeLicenseGUID(ElementProperties elementProperties)
    {
        final String methodName = "removeLicenseGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LICENSE_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the regulators property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return array
     */
    protected List<String> removeRegulators(ElementProperties elementProperties)
    {
        final String methodName = "removeRegulators";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.REGULATORS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the entitlements property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return map
     */
    protected Map<String, String> removeEntitlements(ElementProperties elementProperties)

    {
        final String methodName = "removeEntitlements";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.ENTITLEMENTS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }




    /**
     * Extract and delete the restrictions property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return map
     */
    protected Map<String, String> removeRestrictions(ElementProperties elementProperties)

    {
        final String methodName = "removeRestrictions";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.RESTRICTIONS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the obligations property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected Map<String, String> removeObligations(ElementProperties elementProperties)

    {
        final String methodName = "removeObligations";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.OBLIGATIONS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the rational property from the supplied element properties.
     *
     * @param elementProperties properties from relationship
     * @return string text or null
     */
    protected String removeRationale(ElementProperties elementProperties)
    {
        final String methodName = "removeRationale";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.RATIONALE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the implementationDescription property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeImplementationDescription(ElementProperties elementProperties)
    {
        final String methodName = "removeImplementationDescription";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.IMPLEMENTATION_DESCRIPTION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the criteria property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCriteria(ElementProperties elementProperties)
    {
        final String methodName = "removeCriteria";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CRITERIA.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the domain identifier property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer = default is 0 which is ALL
     */
    protected int removeDomainIdentifier(ElementProperties elementProperties)

    {
        final String methodName = "removeDomainIdentifier";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the level identifier property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer = default is 0 which is ALL
     */
    protected int removeLevelIdentifier(ElementProperties elementProperties)

    {
        final String methodName = "removeLevelIdentifier";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.LEVEL_IDENTIFIER.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the level identifier property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer = default is 0 which is ALL
     */
    protected int removeSeverityLevelIdentifier(ElementProperties elementProperties)

    {
        final String methodName = "removeSeverityLevelIdentifier";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.SEVERITY_LEVEL_IDENTIFIER.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the measurement property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeMeasurement(ElementProperties elementProperties)

    {
        final String methodName = "removeMeasurement";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.MEASUREMENT.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the target property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeTarget(ElementProperties elementProperties)

    {
        final String methodName = "removeTarget";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.TARGET.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the classificationName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeClassificationName(ElementProperties elementProperties)

    {
        final String methodName = "removeClassificationName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CLASSIFICATION_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the classificationPropertyName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeClassificationPropertyName(ElementProperties elementProperties)

    {
        final String methodName = "removeClassificationPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CLASSIFICATION_PROPERTY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the processingEngineUserId property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeProcessingEngineUserId(ElementProperties elementProperties)

    {
        final String methodName = "removeProcessingEngineUserId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.REQUESTER_USER_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the requesterUserId property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeRequesterUserId(ElementProperties elementProperties)

    {
        final String methodName = "removeRequesterUserId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.REQUESTER_USER_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the requestType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeRequestType(ElementProperties elementProperties)
    {
        final String methodName = "removeRequestType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.REQUEST_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the serviceRequestType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeServiceRequestType(ElementProperties elementProperties)
    {
        final String methodName = "removeServiceRequestType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SERVICE_REQUEST_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the requestParameters property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected Map<String, String> removeRequestParameters(ElementProperties elementProperties)

    {
        final String methodName = "removeRequestParameters";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected Map<String, String> removeRequestParameterMap(ElementProperties elementProperties)

    {
        final String methodName = "removeRequestParameterMap";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.REQUEST_PARAMETER_MAP.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected List<String> removeRequestParameterFilter(ElementProperties elementProperties)

    {
        final String methodName = "removeRequestParameterFilter";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.REQUEST_PARAMETER_FILTER.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected Map<String, String> removeActionTargetMap(ElementProperties elementProperties)

    {
        final String methodName = "removeActionTargetMap";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.ACTION_TARGET_MAP.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected List<String> removeActionTargetFilter(ElementProperties elementProperties)

    {
        final String methodName = "removeActionTargetFilter";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.ACTION_TARGET_FILTER.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }

    /**
     * Extract and delete the requestParameters property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected boolean removeGenerateConnectorActivityReports(ElementProperties elementProperties)

    {
        final String methodName = "removeGenerateConnectorActivityReports";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.GENERATE_CONNECTOR_ACTIVITY_REPORT.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
    }


    /**
     * Extract and delete the executorEngineGUID property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeExecutorEngineGUID(ElementProperties elementProperties)

    {
        final String methodName = "removeExecutorEngineGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EXECUTOR_ENGINE_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the engineActionGUID property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeEngineActionGUID(ElementProperties elementProperties)

    {
        final String methodName = "removeEngineActionGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ENGINE_ACTION_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the assetGUID property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeAssetGUID(ElementProperties elementProperties)

    {
        final String methodName = "removeAssetGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ASSET_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the executorEngineName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeExecutorEngineName(ElementProperties elementProperties)

    {
        final String methodName = "removeExecutorEngineName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EXECUTOR_ENGINE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the processName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeProcessName(ElementProperties elementProperties)

    {
        final String methodName = "removeProcessName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PROCESS_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the processStepName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeProcessStepName(ElementProperties elementProperties)

    {
        final String methodName = "removeProcessStepName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PROCESS_STEP_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the processStepGUID property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeProcessStepGUID(ElementProperties elementProperties)

    {
        final String methodName = "removeProcessStepGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PROCESS_STEP_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the governanceActionTypeGUID property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeGovernanceActionTypeGUID(ElementProperties elementProperties)

    {
        final String methodName = "removeGovernanceActionTypeGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.GOVERNANCE_ACTION_TYPE_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the governanceActionTypeName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeGovernanceActionTypeName(ElementProperties elementProperties)

    {
        final String methodName = "removeGovernanceActionTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.GOVERNANCE_ACTION_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeInternalEndpointAddress(ElementProperties elementProperties)

    {
        final String methodName = "removeInternalEndpointAddress";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.INTERNAL_ENDPOINT_ADDRESS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeExternalEndpointAddress(ElementProperties elementProperties)

    {
        final String methodName = "removeExternalEndpointAddress";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EXTERNAL_ENDPOINT_ADDRESS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeGuard(ElementProperties elementProperties)

    {
        final String methodName = "removeGuard";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.GUARD.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the mandatoryGuard property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return flag
     */
    protected boolean removeMandatoryGuard(ElementProperties elementProperties)

    {
        final String methodName = "removeMandatoryGuard";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.MANDATORY_GUARD.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
    }


    /**
     * Extract and delete the ignoreMultipleTriggers property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return flag
     */
    protected boolean removeIgnoreMultipleTriggers(ElementProperties elementProperties)

    {
        final String methodName = "removeIgnoreMultipleTriggers";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.IGNORE_MULTIPLE_TRIGGERS.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
    }


    /**
     * Extract and delete the waitTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return flag
     */
    protected int removeWaitTime(ElementProperties elementProperties)

    {
        final String methodName = "removeWaitTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.WAIT_TIME.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the receivedGuards property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return list of guards
     */
    protected List<String> removeReceivedGuards(ElementProperties elementProperties)

    {
        final String methodName = "removeReceivedGuards";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.RECEIVED_GUARDS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the mandatoryGuards property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return list of guards
     */
    protected List<String> removeMandatoryGuards(ElementProperties elementProperties)

    {
        final String methodName = "removeMandatoryGuards";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.MANDATORY_GUARDS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the completionGuards property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return list of guards
     */
    protected List<String> removeCompletionGuards(ElementProperties elementProperties)

    {
        final String methodName = "removeCompletionGuards";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.COMPLETION_GUARDS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the completionMessage property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeCompletionMessage(ElementProperties elementProperties)

    {
        final String methodName = "removeCompletionMessage";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.COMPLETION_MESSAGE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeEventEffect(ElementProperties elementProperties)

    {
        final String methodName = "removeEventEffect";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EVENT_EFFECT.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the processStartTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeStartTime(ElementProperties elementProperties)

    {
        final String methodName = "removeProcessStartTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.START_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the requested property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeRequestedTime(ElementProperties elementProperties)

    {
        final String methodName = "removeRequestedTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.REQUESTED_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }



    /**
     * Extract and delete the requested property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeRequestedStartTime(ElementProperties elementProperties)

    {
        final String methodName = "removeRequestedStartTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.REQUESTED_START_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the startDate property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeStartDate(ElementProperties elementProperties)

    {
        final String methodName = "removeStartDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.START_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the startDate property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removePlannedStartDate(ElementProperties elementProperties)

    {
        final String methodName = "removePlannedStartDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.PLANNED_START_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeActualStartDate(ElementProperties elementProperties)

    {
        final String methodName = "removeActualStartDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.ACTUAL_START_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeActualCompletionDate(ElementProperties elementProperties)

    {
        final String methodName = "removeActualCompletionDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.ACTUAL_COMPLETION_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }



    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removePlannedCompletionDate(ElementProperties elementProperties)

    {
        final String methodName = "removePlannedCompletionDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.PLANNED_COMPLETION_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeReferenceEffectiveFrom(ElementProperties elementProperties)

    {
        final String methodName = "removeReferenceEffectiveFrom";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.REFERENCE_EFFECTIVE_FROM.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeReferenceEffectiveTo(ElementProperties elementProperties)

    {
        final String methodName = "removeReferenceEffectiveTo";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.REFERENCE_EFFECTIVE_TO.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeConnectorShutdownDate(ElementProperties elementProperties)

    {
        final String methodName = "removeConnectorShutdownDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.CONNECTOR_SHUTDOWN_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return long
     */
    protected long removeRefreshTimeInterval(ElementProperties elementProperties)

    {
        final String methodName = "removeRefreshTimeInterval";

        if (elementProperties != null)
        {
            return propertyHelper.removeLongProperty(localServiceName,
                                                     OpenMetadataProperty.REFRESH_TIME_INTERVAL.name,
                                                     elementProperties,
                                                     methodName);
        }

        return 0L;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return long
     */
    protected long removePlannedDuration(ElementProperties elementProperties)

    {
        final String methodName = "removePlannedDuration";

        if (elementProperties != null)
        {
            return propertyHelper.removeLongProperty(localServiceName,
                                                     OpenMetadataProperty.PLANNED_DURATION.name,
                                                     elementProperties,
                                                     methodName);
        }

        return 0L;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return long
     */
    protected long removeActualDuration(ElementProperties elementProperties)

    {
        final String methodName = "removeActualDuration";

        if (elementProperties != null)
        {
            return propertyHelper.removeLongProperty(localServiceName,
                                                     OpenMetadataProperty.ACTUAL_DURATION.name,
                                                     elementProperties,
                                                     methodName);
        }

        return 0L;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return long
     */
    protected long removeRepeatInterval(ElementProperties elementProperties)

    {
        final String methodName = "removeRepeatInterval";

        if (elementProperties != null)
        {
            return propertyHelper.removeLongProperty(localServiceName,
                                                     OpenMetadataProperty.REPEAT_INTERVAL.name,
                                                     elementProperties,
                                                     methodName);
        }

        return 0L;
    }


    /**
     * Extract and delete the lastReviewTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeLastReviewTime(ElementProperties elementProperties)

    {
        final String methodName = "removeLastReviewTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.LAST_REVIEW_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }




    /**
     * Extract and delete the lastPauseTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeLastPauseTime(ElementProperties elementProperties)

    {
        final String methodName = "removeLastPauseTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.LAST_PAUSE_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the lastResumeTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeLastResumeTime(ElementProperties elementProperties)

    {
        final String methodName = "removeLastResumeTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.LAST_RESUME_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the dueTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeDueTime(ElementProperties elementProperties)

    {
        final String methodName = "removeDueTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.DUE_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the completionTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeCompletionTime(ElementProperties elementProperties)

    {
        final String methodName = "removeCompletionTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.COMPLETION_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the completionDate property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeCompletionDate(ElementProperties elementProperties)

    {
        final String methodName = "removeCompletionDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.COMPLETION_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeRegistrationDate(ElementProperties elementProperties)

    {
        final String methodName = "removeRegistrationDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.REGISTRATION_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the status property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return String text or null
     */
    protected String removeProjectStatus(ElementProperties elementProperties)

    {
        final String methodName = "removeProjectStatus";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PROJECT_STATUS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the health property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return String text or null
     */
    protected String removeProjectHealth(ElementProperties elementProperties)
    {
        final String methodName = "removeProjectHealth";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PROJECT_HEALTH.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the phase property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return String text or null
     */
    protected String removeProjectPhase(ElementProperties elementProperties)
    {
        final String methodName = "removeProjectPhase";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PROJECT_PHASE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return String text or null
     */
    protected String removeActionTargetName(ElementProperties elementProperties)

    {
        final String methodName = "removeActionTargetName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return String text or null
     */
    protected String removeResourceUse(ElementProperties elementProperties)

    {
        final String methodName = "removeResourceUse";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.RESOURCE_USE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the  property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeLicensedBy(ElementProperties elementProperties)
    {
        final String methodName = "getLicensedBy";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LICENSED_BY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the  property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeLicensedByTypeName(ElementProperties elementProperties)
    {
        final String methodName = "getLicensedByTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LICENSED_BY_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the  property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeLicensedByPropertyName(ElementProperties elementProperties)
    {
        final String methodName = "getLicensedByPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LICENSED_BY_PROPERTY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract the  property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeLicensee(ElementProperties elementProperties)
    {
        final String methodName = "getLicensee";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LICENSEE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract the  property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeLicenseeTypeName(ElementProperties elementProperties)
    {
        final String methodName = "getLicenseeTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LICENSEE_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the  property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeLicenseePropertyName(ElementProperties elementProperties)
    {
        final String methodName = "getLicenseePropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LICENSEE_PROPERTY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the description property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePreferredValue(ElementProperties elementProperties)
    {
        final String methodName = "removePreferredValue";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PREFERRED_VALUE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the category property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCategory(ElementProperties elementProperties)
    {
        final String methodName = "removeCategory";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CATEGORY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the isCaseSensitive property from the supplied element properties.
     *
     * @param elementProperties properties from  entity
     * @return boolean
     */
    protected boolean removeIsCaseSensitive(ElementProperties  elementProperties)
    {
        final String methodName = "removeIsCaseSensitive";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.IS_CASE_SENSITIVE.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
    }


    /**
     * Extract the strictRequirement property from the supplied element properties.
     *
     * @param elementProperties properties from ValidValuesAssignment relationship
     * @return boolean
     */
    protected boolean removeStrictRequirement(ElementProperties  elementProperties)
    {
        final String methodName = "removeStrictRequirement";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                     OpenMetadataProperty.STRICT_REQUIREMENT.name,
                                                     elementProperties,
                                                     methodName);
        }

        return false;
    }


    /**
     * Extract the pointType property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String removePointType(ElementProperties  elementProperties)
    {
        final String methodName = "removePointType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.POINT_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the associationDescription property from the supplied element properties.
     *
     * @param elementProperties properties from ValidValuesMapping relationship
     * @return string text or null
     */
    protected String removeAssociationDescription(ElementProperties  elementProperties)
    {
        final String methodName = "removeAssociationDescription";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                    OpenMetadataProperty.ASSOCIATION_DESCRIPTION.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the symbolicName property from the supplied element properties.
     *
     * @param elementProperties properties from ValidValuesImplementation relationship
     * @return string text or null
     */
    protected String removeSymbolicName(ElementProperties  elementProperties)
    {
        final String methodName = "removeSymbolicName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                    OpenMetadataProperty.SYMBOLIC_NAME.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the implementationValue property from the supplied element properties.
     *
     * @param elementProperties properties from ValidValuesImplementation relationship
     * @return string text or null
     */
    protected String removeImplementationValue(ElementProperties  elementProperties)
    {
        final String methodName = "removeImplementationValue";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                    OpenMetadataProperty.IMPLEMENTATION_VALUE.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the additionalValues property from the supplied element properties.
     *
     * @param elementProperties properties from ValidValuesImplementation relationship
     * @return map of name-value pairs
     */
    protected Map<String, String> removeAdditionalValues(ElementProperties  elementProperties)
    {
        final String methodName = "removeAdditionalValues";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.ADDITIONAL_VALUES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the review property from the supplied element properties.
     *
     * @param elementProperties properties from review/rating entities
     * @return string property or null
     */
    protected String removeReview(ElementProperties elementProperties)
    {
        final String methodName = "removeReview";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.REVIEW.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the emoji property from the supplied element properties.
     *
     * @param elementProperties properties from like entities
     * @return string property or null
     */
    protected String removeEmoji(ElementProperties elementProperties)
    {
        final String methodName = "removeEmoji";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EMOJI.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the tagName property from the supplied element properties.
     *
     * @param elementProperties properties from informal tag entities
     * @return string property or null
     */
    protected String removeTagName(ElementProperties elementProperties)
    {
        final String methodName = "removeTagName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DISPLAY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the tagDescription property from the supplied element properties.
     *
     * @param elementProperties properties from informal tag entities
     * @return string property or null
     */
    protected String removeTagDescription(ElementProperties elementProperties)
    {
        final String methodName = "removeTagDescription";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DESCRIPTION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the analysis parameters property from the supplied element properties.
     *
     * @param elementProperties properties from discovery analysis report entities
     * @return string property or null
     */
    protected Map<String, String> removeAnalysisParameters(ElementProperties elementProperties)
    {
        final String methodName = "removeAnalysisParameters";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.ANALYSIS_PARAMETERS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the analysis step property from the supplied element properties.
     *
     * @param elementProperties properties from entities
     * @return string property or null
     */
    protected String removeAnalysisStep(ElementProperties elementProperties)
    {
        final String methodName = "removeAnalysisStep";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ANALYSIS_STEP.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the annotation type property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeAnnotationType(ElementProperties elementProperties)
    {
        final String methodName = "removeAnnotationType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ANNOTATION_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the confidence level property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return integer or 0
     */
    protected int removeConfidenceLevel(ElementProperties elementProperties)
    {
        final String methodName = "removeConfidenceLevel";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.CONFIDENCE_LEVEL.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return integer or 0
     */
    protected int removeConfidence(ElementProperties elementProperties)
    {
        final String methodName = "removeConfidence";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.CONFIDENCE.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return integer or 0
     */
    protected int removeStatusIdentifier(ElementProperties elementProperties)
    {
        final String methodName = "removeStatusIdentifier";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return integer or 0
     */
    protected int removeBasisIdentifier(ElementProperties elementProperties)
    {
        final String methodName = "removeBasisIdentifier";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.RETENTION_BASIS_IDENTIFIER.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeExpression(ElementProperties elementProperties)
    {
        final String methodName = "removeExpression";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EXPRESSION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeSamplingMethod(ElementProperties elementProperties)
    {
        final String methodName = "removeSamplingMethod";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SAMPLING_METHOD.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the attributeName property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeAttributeName(ElementProperties elementProperties)
    {
        final String methodName = "removeAttributeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ATTRIBUTE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the explanation property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeExplanation(ElementProperties elementProperties)
    {
        final String methodName = "removeExplanation";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EXPLANATION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the jsonProperties property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeJsonProperties(ElementProperties elementProperties)
    {
        final String methodName = "removeJsonProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.JSON_PROPERTIES.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the reviewDate property from the supplied element properties.
     *
     * @param elementProperties properties from annotation review entities
     * @return date or null
     */
    protected Date removeReviewDate(ElementProperties elementProperties)
    {
        final String methodName = "removeReviewDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.REVIEW_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract the steward property from the supplied element properties.
     *
     * @param elementProperties properties from annotation review entities
     * @return string property or null
     */
    protected String removeSteward(ElementProperties elementProperties)
    {
        final String methodName = "removeSteward";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.STEWARD.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the stewardTypeName property from the supplied element properties.
     *
     * @param elementProperties properties from annotation review entities
     * @return string property or null
     */
    protected String removeStewardTypeName(ElementProperties elementProperties)
    {
        final String methodName = "removeStewardTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the stewardPropertyName property from the supplied element properties.
     *
     * @param elementProperties properties from annotation review entities
     * @return string property or null
     */
    protected String removeStewardPropertyName(ElementProperties elementProperties)
    {
        final String methodName = "removeStewardPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the notes property from the supplied element properties.
     *
     * @param elementProperties properties from annotation review entities
     * @return string property or null
     */
    protected String removeNotes(ElementProperties elementProperties)
    {
        final String methodName = "removeNotes";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.NOTES.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract the comment property from the supplied element properties.
     *
     * @param elementProperties properties from annotation review entities
     * @return string property or null
     */
    protected String removeComment(ElementProperties elementProperties)
    {
        final String methodName = "removeComment";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.COMMENT.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the schemaName property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeSchemaName(ElementProperties elementProperties)
    {
        final String methodName = "removeSchemaName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SCHEMA_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the schemaType property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeSchemaType(ElementProperties elementProperties)
    {
        final String methodName = "removeSchemaType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SCHEMA_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the candidateClassifications property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name value pairs
     */
    protected Map<String, String> removeCandidateClassifications(ElementProperties elementProperties)
    {
        final String methodName = "removeCandidateClassifications";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.CANDIDATE_CLASSIFICATIONS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the candidateDataClassGUIDs property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of string guids
     */
    protected List<String> removeCandidateDataClassGUIDs(ElementProperties elementProperties)
    {
        final String methodName = "removeCandidateDataClassGUIDs";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.CANDIDATE_DATA_CLASS_GUIDS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return long property or 0
     */
    protected long removeHash(ElementProperties elementProperties)
    {
        final String methodName = "removeHash";

        if (elementProperties != null)
        {
            return propertyHelper.removeLongProperty(localServiceName,
                                                    OpenMetadataProperty.HASH.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0L;
    }


    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeHashAlgorithm(ElementProperties elementProperties)
    {
        final String methodName = "removeHashAlgorithm";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.HASH_ALGORITHM.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeFingerprint(ElementProperties elementProperties)
    {
        final String methodName = "removeFingerprint";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.FINGERPRINT.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeFingerprintAlgorithm(ElementProperties elementProperties)
    {
        final String methodName = "removeFingerprintAlgorithm";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.FINGERPRINT_ALGORITHM.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeInferredDataType(ElementProperties elementProperties)
    {
        final String methodName = "removeInferredDataType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.INFERRED_DATA_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the inferredFormat property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeInferredFormat(ElementProperties elementProperties)
    {
        final String methodName = "removeInferredFormat";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.INFERRED_FORMAT.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the inferredLength property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return int property or 0
     */
    protected int removeInferredLength(ElementProperties elementProperties)
    {
        final String methodName = "removeInferredLength";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.INFERRED_LENGTH.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract the inferredPrecision property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return int property or 0
     */
    protected int removeInferredPrecision(ElementProperties elementProperties)
    {
        final String methodName = "removeInferredPrecision";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.INFERRED_PRECISION.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract the inferredScale property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return int property or 0
     */
    protected int removeInferredScale(ElementProperties elementProperties)
    {
        final String methodName = "removeInferredScale";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.INFERRED_SCALE.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract the profilePropertyNames property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of names
     */
    protected List<String> removeProfilePropertyNames(ElementProperties elementProperties)
    {
        final String methodName = "removeProfilePropertyNames";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.PROFILE_PROPERTY_NAMES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract the matchPropertyNames property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of names
     */
    protected List<String> removeMatchPropertyNames(ElementProperties elementProperties)
    {
        final String methodName = "removeMatchPropertyNames";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.MATCH_PROPERTY_NAMES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }



    /**
     * Extract the sampleValues property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of names
     */
    protected List<String> removeSampleValues(ElementProperties elementProperties)
    {
        final String methodName = "removeSampleValues";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.SAMPLE_VALUES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }



    /**
     * Extract the dataPatterns property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of names
     */
    protected List<String> removeDataPatterns(ElementProperties elementProperties)
    {
        final String methodName = "removeDataPatterns";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.DATA_PATTERNS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }



    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of names
     */
    protected List<String> removeNamePatterns(ElementProperties elementProperties)
    {
        final String methodName = "removeNamePatterns";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.NAME_PATTERNS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }



    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of names
     */
    protected List<String> removeCohortTopics(ElementProperties elementProperties)
    {
        final String methodName = "removeCohortTopics";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.COHORT_TOPICS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract the profileStartDate property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return date
     */
    protected Date removeProfileStartDate(ElementProperties elementProperties)
    {
        final String methodName = "removeProfileStartDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.PROFILE_START_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract the profileEndDate property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return date
     */
    protected Date removeProfileEndDate(ElementProperties elementProperties)
    {
        final String methodName = "removeProfileEndDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.PROFILE_END_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }




    /**
     * Extract the specificationDetails property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name value pairs
     */
    protected Map<String, String> removeSpecificationDetails(ElementProperties elementProperties)
    {
        final String methodName = "removeSpecificationDetails";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.SPECIFICATION_DETAILS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }

    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name value pairs
     */
    protected Map<String, String> removeProperties(ElementProperties elementProperties)
    {
        final String methodName = "removeProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.PROPERTIES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }

    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name value pairs
     */
    protected Map<String, String> removeProfileProperties(ElementProperties elementProperties)
    {
        final String methodName = "removeProfileProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.PROFILE_PROPERTIES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the profileFlags property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name to boolean pairs
     */
    protected Map<String, Boolean> removeProfileFlags(ElementProperties elementProperties)
    {
        final String methodName = "removeProfileFlags";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanMapFromProperty(localServiceName,
                                                               OpenMetadataProperty.PROFILE_FLAGS.name,
                                                               elementProperties,
                                                               methodName);
        }

        return null;
    }


    /**
     * Extract the profileDates property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name to boolean pairs
     */
    protected Map<String, Date> removeProfileDates(ElementProperties elementProperties)
    {
        final String methodName = "removeProfileDates";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateMapFromProperty(localServiceName,
                                                            OpenMetadataProperty.PROFILE_DATES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract the profileCounts property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name to long pairs
     */
    protected Map<String, Long> removeProfileCounts(ElementProperties elementProperties)
    {
        final String methodName = "removeProfileCounts";

        if (elementProperties != null)
        {
            return propertyHelper.removeLongMapFromProperty(localServiceName,
                                                            OpenMetadataProperty.PROFILE_COUNTS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract the profileCounts property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name to long pairs
     */
    protected Map<String, Double> removeProfileDoubles(ElementProperties elementProperties)
    {
        final String methodName = "removeProfileDoubles";

        if (elementProperties != null)
        {
            return propertyHelper.removeDoubleMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.PROFILE_DOUBLES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the valueList property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of values
     */
    protected List<String> removeValueList(ElementProperties elementProperties)
    {
        final String methodName = "removeValueList";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.VALUE_LIST.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract the valueCount property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name to integer pairs
     */
    protected Map<String, Integer> removeValueCount(ElementProperties elementProperties)
    {
        final String methodName = "removeValueCount";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntegerMapFromProperty(localServiceName,
                                                               OpenMetadataProperty.VALUE_COUNT.name,
                                                               elementProperties,
                                                               methodName);
        }

        return null;
    }


    /**
     * Extract and delete the valueRangeFrom property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeValueRangeFrom(ElementProperties elementProperties)
    {
        final String methodName = "removeValueRangeFrom";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.VALUE_RANGE_FROM.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the valueRangeTo property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeValueRangeTo(ElementProperties elementProperties)
    {
        final String methodName = "removeValueRangeTo";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.VALUE_RANGE_TO.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the averageValue property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeAverageValue(ElementProperties elementProperties)
    {
        final String methodName = "removeAverageValue";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.AVERAGE_VALUE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the resourceProperties property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name value pairs
     */
    protected Map<String, String> removeResourceProperties(ElementProperties elementProperties)
    {
        final String methodName = "removeResourceProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.RESOURCE_PROPERTIES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return int property or 0
     */
    protected long removeSize(ElementProperties elementProperties)
    {
        final String methodName = "removeSize";

        if (elementProperties != null)
        {
            return propertyHelper.removeLongProperty(localServiceName,
                                                     OpenMetadataProperty.SIZE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return 0L;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return int property or 0
     */
    protected long removeSourceVersionNumber(ElementProperties elementProperties)
    {
        final String methodName = "removeSourceVersionNumber";

        if (elementProperties != null)
        {
            return propertyHelper.removeLongProperty(localServiceName,
                                                     OpenMetadataProperty.SOURCE_VERSION_NUMBER.name,
                                                     elementProperties,
                                                     methodName);
        }

        return 0L;
    }


    /**
     * Extract and delete the encoding property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeEncoding(ElementProperties  elementProperties)
    {
        final String methodName = "removeEncoding";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ENCODING_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the parameterType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeParameterType(ElementProperties  elementProperties)
    {
        final String methodName = "removeParameterType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PARAMETER_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the qualityDimension standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeQualityDimension(ElementProperties  elementProperties)
    {
        final String methodName = "removeQualityDimension";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.QUALITY_DIMENSION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the qualityScore property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return int property or 0
     */
    protected int removeQualityScore(ElementProperties elementProperties)
    {
        final String methodName = "removeQualityScore";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.QUALITY_SCORE.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeAttachmentGUID(ElementProperties  elementProperties)
    {
        final String methodName = "removeAttachmentGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ATTACHMENT_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeAttachmentType(ElementProperties  elementProperties)
    {
        final String methodName = "removeAttachmentType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ATTACHMENT_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeRelationshipType(ElementProperties  elementProperties)
    {
        final String methodName = "removeRelationshipType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.RELATIONSHIP_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the relatedEntityGUID standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeRelatedEntityGUID(ElementProperties  elementProperties)
    {
        final String methodName = "removeRelatedEntityGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.RELATED_ENTITY_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the relationshipTypeName standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeRelationshipTypeName(ElementProperties  elementProperties)
    {
        final String methodName = "removeRelationshipTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.RELATIONSHIP_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected Map<String, String> removeRelationshipProperties(ElementProperties elementProperties)
    {
        final String methodName = "removeRelationshipProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.RELATIONSHIP_PROPERTIES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected Map<String, String> removeEmbeddedMetadata(ElementProperties elementProperties)
    {
        final String methodName = "removeEmbeddedMetadata";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.EMBEDDED_METADATA.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the actionSourceName standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeActionSourceName(ElementProperties  elementProperties)
    {
        final String methodName = "removeActionSourceName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ACTION_SOURCE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the actionRequested standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeActionRequested(ElementProperties  elementProperties)
    {
        final String methodName = "removeActionRequested";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ACTION_REQUESTED.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the actionProperties property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected Map<String, String> removeActionProperties(ElementProperties elementProperties)
    {
        final String methodName = "removeActionProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.ACTION_PROPERTIES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the informalTerm standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeInformalTerm(ElementProperties  elementProperties)
    {
        final String methodName = "removeInformalTerm";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.INFORMAL_TERM.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the candidateGlossaryTermGUIDs property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of values
     */
    protected List<String> removeCandidateGlossaryTermGUIDs(ElementProperties elementProperties)
    {
        final String methodName = "removeCandidateGlossaryTermGUIDs";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.CANDIDATE_GLOSSARY_TERM_GUIDS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract the candidateGlossaryCategoryGUIDs property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of values
     */
    protected List<String> removeCandidateGlossaryFolderGUIDs(ElementProperties elementProperties)
    {
        final String methodName = "removeCandidateGlossaryFolderGUIDs";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.CANDIDATE_GLOSSARY_FOLDER_GUIDS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the solutionComponentType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeSolutionComponentType(ElementProperties  elementProperties)
    {
        final String methodName = "removeSolutionComponentType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SOLUTION_COMPONENT_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the plannedDeployedImplementationType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removePlannedDeployedImplementationType(ElementProperties  elementProperties)
    {
        final String methodName = "removePlannedDeployedImplementationType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PLANNED_DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the integrationStyle property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeIntegrationStyle(ElementProperties  elementProperties)
    {
        final String methodName = "removeIntegrationStyle";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.INTEGRATION_STYLE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the estimatedVolumetrics property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected Map<String, String> removeEstimatedVolumetrics(ElementProperties elementProperties)
    {
        final String methodName = "removeEstimatedVolumetrics";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.ESTIMATED_VOLUMETRICS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    public String removeRole(ElementProperties  elementProperties)
    {
        final String methodName = "removeRole";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ROLE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    public String removePropertyName(ElementProperties  elementProperties)
    {
        final String methodName = "removePropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PROPERTY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the designStep property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    public String removeDesignStep(ElementProperties  elementProperties)
    {
        final String methodName = "removeDesignStep";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DESIGN_STEP.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the transformation property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    public String removeTransformation(ElementProperties  elementProperties)
    {
        final String methodName = "removeTransformation";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.TRANSFORMATION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    public String removeLabel(ElementProperties  elementProperties)
    {
        final String methodName = "removeLabel";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LABEL.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    public String removeCatalogTargetName(ElementProperties  elementProperties)
    {
        final String methodName = "removeCatalogTargetName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CATALOG_TARGET_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    public String removeMetadataSourceQualifiedName(ElementProperties  elementProperties)
    {
        final String methodName = "removeMetadataSourceQualifiedName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.METADATA_SOURCE_QUALIFIED_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    public String removeConnectionName(ElementProperties  elementProperties)
    {
        final String methodName = "removeConnectionName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONNECTION_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    public Map<String, String> removeTemplates(ElementProperties  elementProperties)
    {
        final String methodName = "removeTemplates";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.TEMPLATES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the property from the supplied element properties.
     *
     * @param elementProperties properties from entities
     * @return list of name-value pairs
     */
    protected List<String> removePurposes(ElementProperties elementProperties)
    {
        final String methodName = "removePurposes";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.PURPOSES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return list string name or null
     */
    public List<String> removeISCQualifiedNames(ElementProperties  elementProperties)
    {
        final String methodName = "removeISCQualifiedNames";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.ISC_QUALIFIED_NAMES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    public String removeISCQualifiedName(ElementProperties  elementProperties)
    {
        final String methodName = "removeISCQualifiedName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    public String removeSubscriberId(ElementProperties  elementProperties)
    {
        final String methodName = "removeSubscriberId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SUBSCRIBER_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    public Map<String, Integer> removeHops(ElementProperties  elementProperties)
    {
        final String methodName = "removeHops";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntegerMapFromProperty(localServiceName,
                                                               OpenMetadataProperty.HOPS.name,
                                                               elementProperties,
                                                               methodName);
        }

        return null;
    }


    /**
     * Extract (and remove) the solution direction enum from element properties.
     *
     * @param elementProperties properties
     * @return enum
     */
    protected SolutionPortDirection removeSolutionPortDirection(ElementProperties elementProperties)
    {
        final String methodName = "removeSolutionPortDirection";

        if (elementProperties != null)
        {
            String enumValue = propertyHelper.removeEnumProperty(localServiceName,
                                                                 OpenMetadataProperty.DIRECTION.name,
                                                                 elementProperties,
                                                                 methodName);

            for (SolutionPortDirection portDirection : SolutionPortDirection.values())
            {
                if (portDirection.getName().equals(enumValue))
                {
                    return portDirection;
                }
            }

            return SolutionPortDirection.UNKNOWN;
        }

        return null;
    }



    /**
     * Extract and delete the CommentType property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    BusinessCapabilityType removeBusinessCapabilityType(ElementProperties elementProperties)
    {
        final String methodName = "removeBusinessCapabilityType";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.BUSINESS_CAPABILITY_TYPE.name,
                                                                         elementProperties,
                                                                         methodName);

            for (BusinessCapabilityType businessCapabilityType : BusinessCapabilityType.values())
            {
                if (businessCapabilityType.getName().equals(retrievedProperty))
                {
                    return businessCapabilityType;
                }
            }
        }

        return null;
    }

    /**
     * Extract and delete the CommentType property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    PortType removePortType(ElementProperties elementProperties)
    {
        final String methodName = "removePortType";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.PORT_TYPE.name,
                                                                         elementProperties,
                                                                         methodName);

            for (PortType portType : PortType.values())
            {
                if (portType.getName().equals(retrievedProperty))
                {
                    return portType;
                }
            }
        }

        return null;
    }


    /**
     * Extract (and remove) the sort order enum from element properties.
     *
     * @param elementProperties properties
     * @return enum
     */
    protected DataItemSortOrder removeDataItemSortOrder(ElementProperties elementProperties)
    {
        final String methodName = "removeDataItemSortOrder";

        if (elementProperties != null)
        {
            String enumValue = propertyHelper.removeEnumProperty(localServiceName,
                                                                 OpenMetadataProperty.SORT_ORDER.name,
                                                                 elementProperties,
                                                                 methodName);

            for (DataItemSortOrder portDirection : DataItemSortOrder.values())
            {
                if (portDirection.getName().equals(enumValue))
                {
                    return portDirection;
                }
            }

            return DataItemSortOrder.UNSORTED;
        }

        return null;
    }



    /**
     * Extract and delete the stars property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    StarRating removeStarRating(ElementProperties elementProperties)
    {
        final String methodName = "removeStarRating";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.STARS.name,
                                                                         elementProperties,
                                                                         methodName);

            for (StarRating starRating : StarRating.values())
            {
                if (starRating.getName().equals(retrievedProperty))
                {
                    return starRating;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the CommentType property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    CommentType removeCommentType(ElementProperties elementProperties)
    {
        final String methodName = "removeCommentType";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.COMMENT_TYPE.name,
                                                                         elementProperties,
                                                                         methodName);

            for (CommentType commentType : CommentType.values())
            {
                if (commentType.getName().equals(retrievedProperty))
                {
                    return commentType;
                }
            }
        }

        return null;
    }



    /**
     * Extract and delete the mediaType property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    MediaType removeMediaType(ElementProperties elementProperties)
    {
        final String methodName = "removeMediaType";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.MEDIA_TYPE.name,
                                                                         elementProperties,
                                                                         methodName);

            for (MediaType enumValue : MediaType.values())
            {
                if (enumValue.getName().equals(retrievedProperty))
                {
                    return enumValue;
                }
            }
        }

        return null;
    }



    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    MediaUsage removeDefaultMediaUsage(ElementProperties elementProperties)
    {
        final String methodName = "removeDefaultMediaUsage";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.DEFAULT_MEDIA_USAGE.name,
                                                                         elementProperties,
                                                                         methodName);

            for (MediaUsage enumValue : MediaUsage.values())
            {
                if (enumValue.getName().equals(retrievedProperty))
                {
                    return enumValue;
                }
            }
        }

        return null;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    MediaUsage removeMediaUsage(ElementProperties elementProperties)
    {
        final String methodName = "removeMediaUsage";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.MEDIA_USAGE.name,
                                                                         elementProperties,
                                                                         methodName);

            for (MediaUsage enumValue : MediaUsage.values())
            {
                if (enumValue.getName().equals(retrievedProperty))
                {
                    return enumValue;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the ContactMethodType property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    ContactMethodType removeContactMethodType(ElementProperties elementProperties)
    {
        final String methodName = "removeContactMethodType";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.CONTACT_METHOD_TYPE.name,
                                                                         elementProperties,
                                                                         methodName);

            for (ContactMethodType contactMethodType : ContactMethodType.values())
            {
                if (contactMethodType.getName().equals(retrievedProperty))
                {
                    return contactMethodType;
                }
            }
        }

        return null;
    }



    /**
     * Extract and delete the KeyPattern property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    KeyPattern removeKeyPattern(ElementProperties elementProperties)
    {
        final String methodName = "removeKeyPattern";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.KEY_PATTERN.name,
                                                                         elementProperties,
                                                                         methodName);

            for (KeyPattern value : KeyPattern.values())
            {
                if (value.getName().equals(retrievedProperty))
                {
                    return value;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the ContactMethodType property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    IncidentReportStatus removeIncidentReportStatus(ElementProperties elementProperties)
    {
        final String methodName = "removeIncidentReportStatus";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.INCIDENT_STATUS.name,
                                                                         elementProperties,
                                                                         methodName);

            for (IncidentReportStatus incidentReportStatus : IncidentReportStatus.values())
            {
                if (incidentReportStatus.getName().equals(retrievedProperty))
                {
                    return incidentReportStatus;
                }
            }
        }

        return null;
    }



    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    LatestChangeTarget removeLatestChangeTarget(ElementProperties elementProperties)
    {
        final String methodName = "removeLatestChangeTarget";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.CHANGE_TARGET.name,
                                                                         elementProperties,
                                                                         methodName);

            for (LatestChangeTarget latestChangeTarget : LatestChangeTarget.values())
            {
                if (latestChangeTarget.getName().equals(retrievedProperty))
                {
                    return latestChangeTarget;
                }
            }
        }

        return null;
    }




    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    LatestChangeAction removeLatestChangeAction(ElementProperties elementProperties)
    {
        final String methodName = "removeLatestChangeAction";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.CHANGE_ACTION.name,
                                                                         elementProperties,
                                                                         methodName);

            for (LatestChangeAction latestChangeAction : LatestChangeAction.values())
            {
                if (latestChangeAction.getName().equals(retrievedProperty))
                {
                    return latestChangeAction;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    PermittedSynchronization removePermittedSynchronization(ElementProperties elementProperties)
    {
        final String methodName = "removePermittedSynchronization";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name,
                                                                         elementProperties,
                                                                         methodName);

            for (PermittedSynchronization permittedSynchronization : PermittedSynchronization.values())
            {
                if (permittedSynchronization.getName().equals(retrievedProperty))
                {
                    return permittedSynchronization;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    DeleteMethod removeDeleteMethod(ElementProperties elementProperties)
    {
        final String methodName = "removeDeleteMethod";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.DELETE_METHOD.name,
                                                                         elementProperties,
                                                                         methodName);

            for (DeleteMethod deleteMethod : DeleteMethod.values())
            {
                if (deleteMethod.getName().equals(retrievedProperty))
                {
                    return deleteMethod;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    CollectionMemberStatus removeMembershipStatus(ElementProperties elementProperties)
    {
        final String methodName = "removeDeleteMethod";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.MEMBERSHIP_STATUS.name,
                                                                         elementProperties,
                                                                         methodName);

            for (CollectionMemberStatus memberStatus : CollectionMemberStatus.values())
            {
                if (memberStatus.getName().equals(retrievedProperty))
                {
                    return memberStatus;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    CrowdSourcingRole removeRoleType(ElementProperties elementProperties)
    {
        final String methodName = "removeRoleType";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.ROLE_TYPE.name,
                                                                         elementProperties,
                                                                         methodName);

            for (CrowdSourcingRole roleType : CrowdSourcingRole.values())
            {
                if (roleType.getName().equals(retrievedProperty))
                {
                    return roleType;
                }
            }
        }

        return null;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    CoverageCategory removeCoverageCategory(ElementProperties elementProperties)
    {
        final String methodName = "removeCoverageCategory";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.COVERAGE_CATEGORY.name,
                                                                         elementProperties,
                                                                         methodName);

            for (CoverageCategory coverageCategory : CoverageCategory.values())
            {
                if (coverageCategory.getName().equals(retrievedProperty))
                {
                    return coverageCategory;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    AnnotationStatus removeAnnotationStatus(ElementProperties elementProperties)
    {
        final String methodName = "removeAnnotationStatus";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.ANNOTATION_STATUS.name,
                                                                         elementProperties,
                                                                         methodName);

            for (AnnotationStatus annotationStatus : AnnotationStatus.values())
            {
                if (annotationStatus.getName().equals(retrievedProperty))
                {
                    return annotationStatus;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    OperationalStatus removeOperationalStatus(ElementProperties elementProperties)
    {
        final String methodName = "removeOperationalStatus";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.OPERATIONAL_STATUS.name,
                                                                         elementProperties,
                                                                         methodName);

            for (OperationalStatus operationalStatus : OperationalStatus.values())
            {
                if (operationalStatus.getName().equals(retrievedProperty))
                {
                    return operationalStatus;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    CapabilityAssetUseType removeUseType(ElementProperties elementProperties)
    {
        final String methodName = "removeUseType";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.USE_TYPE.name,
                                                                         elementProperties,
                                                                         methodName);

            for (CapabilityAssetUseType capabilityAssetUseType : CapabilityAssetUseType.values())
            {
                if (capabilityAssetUseType.getName().equals(retrievedProperty))
                {
                    return capabilityAssetUseType;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    ProcessContainmentType removeContainmentType(ElementProperties elementProperties)
    {
        final String methodName = "removeUseType";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.CONTAINMENT_TYPE.name,
                                                                         elementProperties,
                                                                         methodName);

            for (ProcessContainmentType processContainmentType : ProcessContainmentType.values())
            {
                if (processContainmentType.getName().equals(retrievedProperty))
                {
                    return processContainmentType;
                }
            }
        }

        return null;
    }



    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    DataClassAssignmentStatus removeDataClassAssignmentStatus(ElementProperties elementProperties)
    {
        final String methodName = "removeDataClassAssignmentStatus";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.DATA_CLASS_ASSIGNMENT_STATUS.name,
                                                                         elementProperties,
                                                                         methodName);

            for (DataClassAssignmentStatus dataClassAssignmentStatus : DataClassAssignmentStatus.values())
            {
                if (dataClassAssignmentStatus.getName().equals(retrievedProperty))
                {
                    return dataClassAssignmentStatus;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    TermAssignmentStatus removeTermAssignmentStatus(ElementProperties elementProperties)
    {
        final String methodName = "removeTermAssignmentStatus";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.TERM_ASSIGNMENT_STATUS.name,
                                                                         elementProperties,
                                                                         methodName);

            for (TermAssignmentStatus termAssignmentStatus : TermAssignmentStatus.values())
            {
                if (termAssignmentStatus.getName().equals(retrievedProperty))
                {
                    return termAssignmentStatus;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    ActivityType removeActivityType(ElementProperties elementProperties)
    {
        final String methodName = "removeActivityType";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.ACTIVITY_TYPE.name,
                                                                         elementProperties,
                                                                         methodName);

            for (ActivityType activityType : ActivityType.values())
            {
                if (activityType.getName().equals(retrievedProperty))
                {
                    return activityType;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    TermRelationshipStatus removeStatus(ElementProperties elementProperties)
    {
        final String methodName = "removeStatus";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name,
                                                                         elementProperties,
                                                                         methodName);

            for (TermRelationshipStatus status : TermRelationshipStatus.values())
            {
                if (status.getName().equals(retrievedProperty))
                {
                    return status;
                }
            }
        }

        return null;
    }


    /**
     * Uses the type of the classification to determine the type of bean to use for the properties.
     *
     * @param attachedClassification element retrieved
     * @return properties
     */
    public ClassificationBeanProperties getClassificationProperties(AttachedClassification attachedClassification)
    {
        if (attachedClassification != null)
        {
            ClassificationBeanProperties beanProperties;

            ElementProperties elementProperties = new ElementProperties(attachedClassification.getClassificationProperties());

            if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.ABSTRACT_CONCEPT_CLASSIFICATION.typeName))
            {
                beanProperties = new AbstractConceptProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.ACTIVITY_DESCRIPTION_CLASSIFICATION.typeName))
            {
                beanProperties = new ActivityDescriptionProperties();

                ((ActivityDescriptionProperties)beanProperties).setActivityType(this.removeActivityType(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.ANCHORS_CLASSIFICATION.typeName))
            {
                beanProperties = new AnchorsProperties();

                ((AnchorsProperties)beanProperties).setAnchorGUID(this.removeAnchorGUID(elementProperties));
                ((AnchorsProperties)beanProperties).setAnchorTypeName(this.removeAnchorTypeName(elementProperties));
                ((AnchorsProperties)beanProperties).setAnchorDomainName(this.removeAnchorDomainName(elementProperties));
                ((AnchorsProperties)beanProperties).setAnchorScopeGUID(this.removeAnchorScopeGUID(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION.typeName))
            {
                beanProperties = new CalculatedValueProperties();

                ((CalculatedValueProperties)beanProperties).setFormula(this.removeFormula(elementProperties));
                ((CalculatedValueProperties)beanProperties).setFormulaType(this.removeFormulaType(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.CAMPAIGN_CLASSIFICATION.typeName))
            {
                beanProperties = new CampaignProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.CANONICAL_VOCABULARY_CLASSIFICATION.typeName))
            {
                beanProperties = new CanonicalVocabularyProperties();

                ((CanonicalVocabularyProperties)beanProperties).setScope(this.removeScope(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.CLOUD_PLATFORM_CLASSIFICATION.typeName))
            {
                beanProperties = new CloudPlatformProperties();

                ((CloudPlatformProperties)beanProperties).setDeployedImplementationType(this.removeDeployedImplementationType(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.CLOUD_PROVIDER_CLASSIFICATION.typeName))
            {
                beanProperties = new CloudProviderProperties();

                ((CloudProviderProperties)beanProperties).setProviderName(this.removeProviderName(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.CLOUD_SERVICE_CLASSIFICATION.typeName))
            {
                beanProperties = new CloudServiceProperties();

                ((CloudServiceProperties)beanProperties).setOfferingName(this.removeOfferingName(elementProperties));
                ((CloudServiceProperties)beanProperties).setServiceType(this.removeServiceType(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.CLOUD_TENANT_CLASSIFICATION.typeName))
            {
                beanProperties = new CloudTenantProperties();

                ((CloudTenantProperties)beanProperties).setTenantName(this.removeTenantName(elementProperties));
                ((CloudTenantProperties)beanProperties).setTenantType(this.removeTenantType(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName))
            {
                beanProperties = new ConfidenceProperties();

                ((ConfidenceProperties)beanProperties).setStatusIdentifier(this.removeStatusIdentifier(elementProperties));
                ((ConfidenceProperties)beanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((ConfidenceProperties)beanProperties).setSteward(this.removeSteward(elementProperties));
                ((ConfidenceProperties)beanProperties).setStewardTypeName(this.removeStewardTypeName(elementProperties));
                ((ConfidenceProperties)beanProperties).setStewardPropertyName(this.removeStewardPropertyName(elementProperties));
                ((ConfidenceProperties)beanProperties).setSource(this.removeSource(elementProperties));
                ((ConfidenceProperties)beanProperties).setNotes(this.removeNotes(elementProperties));
                ((ConfidenceProperties)beanProperties).setLevelIdentifier(this.removeLevelIdentifier(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName))
            {
                beanProperties = new ConfidentialityProperties();

                ((ConfidentialityProperties)beanProperties).setStatusIdentifier(this.removeStatusIdentifier(elementProperties));
                ((ConfidentialityProperties)beanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((ConfidentialityProperties)beanProperties).setSteward(this.removeSteward(elementProperties));
                ((ConfidentialityProperties)beanProperties).setStewardTypeName(this.removeStewardTypeName(elementProperties));
                ((ConfidentialityProperties)beanProperties).setStewardPropertyName(this.removeStewardPropertyName(elementProperties));
                ((ConfidentialityProperties)beanProperties).setSource(this.removeSource(elementProperties));
                ((ConfidentialityProperties)beanProperties).setNotes(this.removeNotes(elementProperties));
                ((ConfidentialityProperties)beanProperties).setLevelIdentifier(this.removeLevelIdentifier(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.CONTEXT_DEFINITION_CLASSIFICATION.typeName))
            {
                beanProperties = new ContextDefinitionProperties();

                ((ContextDefinitionProperties)beanProperties).setDescription(this.removeDescription(elementProperties));
                ((ContextDefinitionProperties)beanProperties).setScope(this.removeScope(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.CONTEXT_EVENT_COLLECTION_CLASSIFICATION.typeName))
            {
                beanProperties = new ContextEventCollectionProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName))
            {
                beanProperties = new CriticalityProperties();

                ((CriticalityProperties)beanProperties).setStatusIdentifier(this.removeStatusIdentifier(elementProperties));
                ((CriticalityProperties)beanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((CriticalityProperties)beanProperties).setSteward(this.removeSteward(elementProperties));
                ((CriticalityProperties)beanProperties).setStewardTypeName(this.removeStewardTypeName(elementProperties));
                ((CriticalityProperties)beanProperties).setStewardPropertyName(this.removeStewardPropertyName(elementProperties));
                ((CriticalityProperties)beanProperties).setSource(this.removeSource(elementProperties));
                ((CriticalityProperties)beanProperties).setNotes(this.removeNotes(elementProperties));
                ((CriticalityProperties)beanProperties).setStatusIdentifier(this.removeStatusIdentifier(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.CYBER_LOCATION_CLASSIFICATION.typeName))
            {
                beanProperties = new CyberLocationProperties();

                ((CyberLocationProperties)beanProperties).setNetworkAddress(this.removeNetworkAddress(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.DATA_ASSET_ENCODING_CLASSIFICATION.typeName))
            {
                beanProperties = new DataAssetEncodingProperties();

                ((DataAssetEncodingProperties)beanProperties).setEncodingType(this.removeEncodingType(elementProperties));
                ((DataAssetEncodingProperties)beanProperties).setEncodingLanguage(this.removeEncodingLanguage(elementProperties));
                ((DataAssetEncodingProperties)beanProperties).setEncodingDescription(this.removeEncodingDescription(elementProperties));
                ((DataAssetEncodingProperties)beanProperties).setEncodingProperties(this.removeEncodingProperties(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.DATA_SCOPE_CLASSIFICATION.typeName))
            {
                beanProperties = new DataScopeProperties();

                ((DataScopeProperties)beanProperties).setMinLongitude(this.removeMinLongitude(elementProperties));
                ((DataScopeProperties)beanProperties).setMinLatitude(this.removeMinLatitude(elementProperties));
                ((DataScopeProperties)beanProperties).setMaxLongitude(this.removeMaxLongitude(elementProperties));
                ((DataScopeProperties)beanProperties).setMaxLatitude(this.removeMaxLatitude(elementProperties));
                ((DataScopeProperties)beanProperties).setMinHeight(this.removeMinHeight(elementProperties));
                ((DataScopeProperties)beanProperties).setMaxHeight(this.removeMaxHeight(elementProperties));
                ((DataScopeProperties)beanProperties).setDataCollectionStartTime(this.removeDataCollectionStartTime(elementProperties));
                ((DataScopeProperties)beanProperties).setDataCollectionEndTime(this.removeDataCollectionEndTime(elementProperties));
                ((DataScopeProperties)beanProperties).setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.DATA_VALUE_CLASSIFICATION.typeName))
            {
                beanProperties = new DataValueProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName))
            {
                beanProperties = new DigitalResourceOriginProperties();

                ((DigitalResourceOriginProperties)beanProperties).setOrganization(this.removeOrganization(elementProperties));
                ((DigitalResourceOriginProperties)beanProperties).setOrganizationPropertyName(this.removeOrganizationPropertyName(elementProperties));
                ((DigitalResourceOriginProperties)beanProperties).setBusinessCapability(this.removeBusinessCapability(elementProperties));
                ((DigitalResourceOriginProperties)beanProperties).setBusinessCapabilityPropertyName(this.removeBusinessCapabilityPropertyName(elementProperties));
                ((DigitalResourceOriginProperties)beanProperties).setOtherOriginValues(this.removeOtherOriginValues(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.EDITING_COLLECTION_CLASSIFICATION.typeName))
            {
                beanProperties = new EditingCollectionProperties();

                ((EditingCollectionProperties)beanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.ELEMENT_SUPPLEMENT_CLASSIFICATION.typeName))
            {
                beanProperties = new ElementSupplementProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.FIXED_LOCATION_CLASSIFICATION.typeName))
            {
                beanProperties = new FixedLocationProperties();

                ((FixedLocationProperties)beanProperties).setCoordinates(this.removeCoordinates(elementProperties));
                ((FixedLocationProperties)beanProperties).setMapProjection(this.removeMapProjection(elementProperties));
                ((FixedLocationProperties)beanProperties).setPostalAddress(this.removePostalAddress(elementProperties));
                ((FixedLocationProperties)beanProperties).setTimeZone(this.removeTimeZone(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName))
            {
                beanProperties = new FolderProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.GLOSSARY_PROJECT_CLASSIFICATION.typeName))
            {
                beanProperties = new GlossaryProjectProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.GOVERNANCE_EXPECTATIONS_CLASSIFICATION.typeName))
            {
                beanProperties = new GovernanceExpectationsProperties();

                ((GovernanceExpectationsProperties)beanProperties).setFlags(this.removeFlags(elementProperties));
                ((GovernanceExpectationsProperties)beanProperties).setValues(this.removeValues(elementProperties));
                ((GovernanceExpectationsProperties)beanProperties).setDates(this.removeDates(elementProperties));
                ((GovernanceExpectationsProperties)beanProperties).setCounts(this.removeCounts(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.GOVERNANCE_MEASUREMENTS_CLASSIFICATION.typeName))
            {
                beanProperties = new GovernanceMeasurementsProperties();

                ((GovernanceMeasurementsProperties)beanProperties).setDataCollectionStartTime(this.removeDataCollectionStartTime(elementProperties));
                ((GovernanceMeasurementsProperties)beanProperties).setDataCollectionEndTime(this.removeDataCollectionEndTime(elementProperties));
                ((GovernanceMeasurementsProperties)beanProperties).setFlags(this.removeFlags(elementProperties));
                ((GovernanceMeasurementsProperties)beanProperties).setValues(this.removeValues(elementProperties));
                ((GovernanceMeasurementsProperties)beanProperties).setDates(this.removeDates(elementProperties));
                ((GovernanceMeasurementsProperties)beanProperties).setCounts(this.removeCounts(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.HOME_COLLECTION_CLASSIFICATION.typeName))
            {
                beanProperties = new HomeCollectionProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.IMPACT_CLASSIFICATION.typeName))
            {
                beanProperties = new ImpactProperties();

                ((ImpactProperties)beanProperties).setStatusIdentifier(this.removeStatusIdentifier(elementProperties));
                ((ImpactProperties)beanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((ImpactProperties)beanProperties).setSteward(this.removeSteward(elementProperties));
                ((ImpactProperties)beanProperties).setStewardTypeName(this.removeStewardTypeName(elementProperties));
                ((ImpactProperties)beanProperties).setStewardPropertyName(this.removeStewardPropertyName(elementProperties));
                ((ImpactProperties)beanProperties).setSource(this.removeSource(elementProperties));
                ((ImpactProperties)beanProperties).setNotes(this.removeNotes(elementProperties));
                ((ImpactProperties)beanProperties).setStatusIdentifier(this.removeStatusIdentifier(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.LATEST_CHANGE_CLASSIFICATION.typeName))
            {
                beanProperties = new LatestChangeProperties();

                ((LatestChangeProperties)beanProperties).setLatestChangeTarget(this.removeLatestChangeTarget(elementProperties));
                ((LatestChangeProperties)beanProperties).setLatestChangeAction(this.removeLatestChangeAction(elementProperties));
                ((LatestChangeProperties)beanProperties).setClassificationName(this.removeClassificationName(elementProperties));
                ((LatestChangeProperties)beanProperties).setAttachmentGUID(this.removeAttachmentGUID(elementProperties));
                ((LatestChangeProperties)beanProperties).setAttachmentTypeName(this.removeAttachmentType(elementProperties));
                ((LatestChangeProperties)beanProperties).setRelationshipTypeName(this.removeRelationshipType(elementProperties));
                ((LatestChangeProperties)beanProperties).setUserId(this.removeUser(elementProperties));
                ((LatestChangeProperties)beanProperties).setActionDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.LISTENER_INTERFACE_CLASSIFICATION.typeName))
            {
                beanProperties = new ListenerInterfaceProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.MEMENTO_CLASSIFICATION.typeName))
            {
                beanProperties = new MementoProperties();

                ((MementoProperties)beanProperties).setArchiveDate(this.removeArchiveDate(elementProperties));
                ((MementoProperties)beanProperties).setArchiveUser(this.removeArchiveUser(elementProperties));
                ((MementoProperties)beanProperties).setArchiveProcess(this.removeArchiveProcess(elementProperties));
                ((MementoProperties)beanProperties).setArchiveService(this.removeArchiveService(elementProperties));
                ((MementoProperties)beanProperties).setArchiveMethod(this.removeArchiveMethod(elementProperties));
                ((MementoProperties)beanProperties).setArchiveProperties(this.removeArchiveProperties(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.MOBILE_RESOURCE_CLASSIFICATION.typeName))
            {
                beanProperties = new MobileResourceProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.NAMESPACE_COLLECTION_CLASSIFICATION.typeName))
            {
                beanProperties = new NamespaceProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName))
            {
                beanProperties = new OwnershipProperties();

                ((OwnershipProperties)beanProperties).setOwner(this.removeOwner(elementProperties));
                ((OwnershipProperties)beanProperties).setOwnerTypeName(this.removeOwnerTypeName(elementProperties));
                ((OwnershipProperties)beanProperties).setOwnerPropertyName(this.removeOwnerPropertyName(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.PERSONAL_PROJECT_CLASSIFICATION.typeName))
            {
                beanProperties = new PersonalProjectProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.PRIMARY_KEY_CLASSIFICATION.typeName))
            {
                beanProperties = new PrimaryKeyProperties();

                ((PrimaryKeyProperties)beanProperties).setDisplayName(this.removeDisplayName(elementProperties));
                ((PrimaryKeyProperties)beanProperties).setKeyPattern(this.removeKeyPattern(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.PUBLISHER_INTERFACE_CLASSIFICATION.typeName))
            {
                beanProperties = new PublisherInterfaceProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.RECENT_ACCESS_COLLECTION_CLASSIFICATION.typeName))
            {
                beanProperties = new RecentAccessProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.REFERENCE_DATA_CLASSIFICATION.typeName))
            {
                beanProperties = new ReferenceDataProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.REFERENCE_LIST_CLASSIFICATION.typeName))
            {
                beanProperties = new ReferenceListProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.REQUEST_RESPONSE_INTERFACE_CLASSIFICATION.typeName))
            {
                beanProperties = new RequestResponseInterfaceProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.RESOURCE_MANAGER_CLASSIFICATION.typeName))
            {
                if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.ASSET_MANAGER.typeName))
                {
                    beanProperties = new AssetManagerProperties();
                }
                else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.CHANGE_MANAGEMENT_LIBRARY_CLASSIFICATION.typeName))
                {
                    beanProperties = new ChangeManagementLibraryProperties();
                }
                else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.CONTENT_COLLECTION_MANAGER.typeName))
                {
                    beanProperties = new ContentCollectionManagerProperties();
                }
                else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.FILE_MANAGER_CLASSIFICATION.typeName))
                {
                    beanProperties = new FileManagerProperties();
                }
                else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.FILE_SYSTEM_CLASSIFICATION.typeName))
                {
                    beanProperties = new FileSystemProperties();
                }
                else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.NOTIFICATION_MANAGER.typeName))
                {
                    beanProperties = new NotificationManagerProperties();
                }
                else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.MASTER_DATA_MANAGER.typeName))
                {
                    beanProperties = new MasterDataManagerProperties();
                }
                else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.SOURCE_CONTROL_LIBRARY_CLASSIFICATION.typeName))
                {
                    beanProperties = new SourceControlLibraryProperties();
                }
                else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.SOFTWARE_LIBRARY_CLASSIFICATION.typeName))
                {
                    beanProperties = new SoftwareLibraryProperties();
                }
                else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.USER_ACCESS_DIRECTORY.typeName))
                {
                    beanProperties = new UserAccessDirectoryProperties();
                }
                else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.USER_PROFILE_MANAGER.typeName))
                {
                    beanProperties = new UserProfileManagerProperties();
                }
                else
                {
                    beanProperties = new ResourceManagerProperties();
                }
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.RESULTS_SET_CLASSIFICATION.typeName))
            {
                beanProperties = new ResultsSetProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.RETENTION_CLASSIFICATION.typeName))
            {
                beanProperties = new RetentionProperties();

                ((RetentionProperties)beanProperties).setStatusIdentifier(this.removeStatusIdentifier(elementProperties));
                ((RetentionProperties)beanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((RetentionProperties)beanProperties).setSteward(this.removeSteward(elementProperties));
                ((RetentionProperties)beanProperties).setStewardTypeName(this.removeStewardTypeName(elementProperties));
                ((RetentionProperties)beanProperties).setStewardPropertyName(this.removeStewardPropertyName(elementProperties));
                ((RetentionProperties)beanProperties).setSource(this.removeSource(elementProperties));
                ((RetentionProperties)beanProperties).setNotes(this.removeNotes(elementProperties));
                ((RetentionProperties)beanProperties).setBasisIdentifier(this.removeBasisIdentifier(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.ROOT_COLLECTION_CLASSIFICATION.typeName))
            {
                beanProperties = new RootCollectionProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.SCOPING_COLLECTION_CLASSIFICATION.typeName))
            {
                beanProperties = new ScopingCollectionProperties();

                ((ScopingCollectionProperties)beanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.SECURE_LOCATION_CLASSIFICATION.typeName))
            {
                beanProperties = new SecureLocationProperties();

                ((SecureLocationProperties)beanProperties).setDescription(this.removeDescription(elementProperties));
                ((SecureLocationProperties)beanProperties).setLevel(this.removeLevel(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.SECURITY_GROUP_MEMBERSHIP_CLASSIFICATION.typeName))
            {
                beanProperties = new SecurityGroupMembershipProperties();

                ((SecurityGroupMembershipProperties)beanProperties).setGroups(this.removeGroups(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.SECURITY_TAGS_CLASSIFICATION.typeName))
            {
                beanProperties = new SecurityTagsProperties();

                ((SecurityTagsProperties)beanProperties).setSecurityLabels(this.removeSecurityLabels(elementProperties));
                ((SecurityTagsProperties)beanProperties).setSecurityProperties(this.removeSecurityProperties(elementProperties));
                ((SecurityTagsProperties)beanProperties).setAccessGroups(this.removeAccessGroups(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.STAGING_COLLECTION_CLASSIFICATION.typeName))
            {
                beanProperties = new StagingCollectionProperties();

                ((StagingCollectionProperties)beanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.STUDY_PROJECT_CLASSIFICATION.typeName))
            {
                beanProperties = new StudyProjectProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.SUBJECT_AREA_CLASSIFICATION.typeName))
            {
                beanProperties = new SubjectAreaProperties();

                ((SubjectAreaProperties)beanProperties).setSubjectAreaName(this.removeSubjectAreaName(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.TASK_CLASSIFICATION.typeName))
            {
                beanProperties = new TaskProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.TAXONOMY_CLASSIFICATION.typeName))
            {
                beanProperties = new TaxonomyProperties();

                ((TaxonomyProperties)beanProperties).setOrganizingPrinciple(this.removeOrganizingPrinciple(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName))
            {
                beanProperties = new TemplateProperties();

                ((TemplateProperties)beanProperties).setDisplayName(this.removeDisplayName(elementProperties));
                ((TemplateProperties)beanProperties).setDescription(this.removeDescription(elementProperties));
                ((TemplateProperties)beanProperties).setVersionIdentifier(this.removeVersionIdentifier(elementProperties));
                ((TemplateProperties)beanProperties).setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.TEMPLATE_SUBSTITUTE_CLASSIFICATION.typeName))
            {
                beanProperties = new TemplateSubstituteProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName))
            {
                beanProperties = new TypeEmbeddedAttributeProperties();

                ((TypeEmbeddedAttributeProperties)beanProperties).setSchemaTypeName(this.removeSchemaType(elementProperties));
                ((TypeEmbeddedAttributeProperties)beanProperties).setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                ((TypeEmbeddedAttributeProperties)beanProperties).setQualifiedName(this.removeQualifiedName(elementProperties));
                ((TypeEmbeddedAttributeProperties)beanProperties).setDescription(this.removeDescription(elementProperties));
                ((TypeEmbeddedAttributeProperties)beanProperties).setVersionIdentifier(this.removeVersionIdentifier(elementProperties));
                ((TypeEmbeddedAttributeProperties)beanProperties).setCategory(this.removeCategory(elementProperties));
                ((TypeEmbeddedAttributeProperties)beanProperties).setAuthor(this.removeAuthor(elementProperties));
                ((TypeEmbeddedAttributeProperties)beanProperties).setUsage(this.removeUsage(elementProperties));
                ((TypeEmbeddedAttributeProperties)beanProperties).setEncodingStandard(this.removeEncodingStandard(elementProperties));
                ((TypeEmbeddedAttributeProperties)beanProperties).setNamespace(this.removeNamespace(elementProperties));
                ((TypeEmbeddedAttributeProperties)beanProperties).setDataType(this.removeDataType(elementProperties));
                ((TypeEmbeddedAttributeProperties)beanProperties).setDefaultValue(this.removeDefaultValue(elementProperties));
                ((TypeEmbeddedAttributeProperties)beanProperties).setFixedValue(this.removeFixedValue(elementProperties));
                ((TypeEmbeddedAttributeProperties)beanProperties).setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.WORK_ITEM_LIST_COLLECTION_CLASSIFICATION.typeName))
            {
                beanProperties = new WorkItemListProperties();
            }
            else if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName))
            {
                beanProperties = new ZoneMembershipProperties();

                ((ZoneMembershipProperties)beanProperties).setZoneMembership(this.removeZoneMembership(elementProperties));
            }
            else
            {
                beanProperties = new ClassificationBeanProperties();
            }


            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype that we don't know about - but it also helps to catch
             * missing mappings in the code above.
             */
            beanProperties.setTypeName(attachedClassification.getType().getTypeName());
            beanProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

            return beanProperties;

        }

        return null;
    }


    /**
     * Retrieve the relationship bean properties from a relationship.
     *
     * @param relatedMetadataElement related element
     *
     * @return relationship bean properties
     */
    public RelationshipBeanProperties getRelationshipProperties(RelatedMetadataElement relatedMetadataElement)
    {
        if (relatedMetadataElement != null)
        {
            RelationshipBeanProperties relationshipProperties = getRelationshipProperties(relatedMetadataElement,
                                                                                          relatedMetadataElement.getRelationshipProperties());

            relationshipProperties.setEffectiveFrom(relatedMetadataElement.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(relatedMetadataElement.getEffectiveToTime());

            return relationshipProperties;
        }

        return null;
    }


    /**
     * Retrieve the relationship bean properties from a relationship.
     *
     * @param openMetadataRelationship relationship element
     *
     * @return relationship bean properties
     */
    public RelationshipBeanProperties getRelationshipProperties(OpenMetadataRelationship openMetadataRelationship)
    {
        if (openMetadataRelationship != null)
        {
            RelationshipBeanProperties relationshipProperties = getRelationshipProperties(openMetadataRelationship,
                                                                                              openMetadataRelationship.getRelationshipProperties());

            relationshipProperties.setEffectiveFrom(openMetadataRelationship.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(openMetadataRelationship.getEffectiveToTime());

            return relationshipProperties;
        }

        return null;
    }


    /**
     * Retrieve the relationship bean properties from a relationship's element properties.
     *
     * @param relationshipProperties element properties
     *
     * @return relationship bean properties
     */
    public RelationshipBeanProperties getRelationshipProperties(ElementControlHeader relationshipHeader,
                                                                ElementProperties    relationshipProperties)
    {
        if (relationshipHeader != null)
        {
            RelationshipBeanProperties relationshipBeanProperties;

            /*
             * The initial set of values come from the attributes - the bean properties object to use is
             * determined by its type.  Attributes are removed from element properties as the bean properties
             * object is filled.  Any remaining properties are added to extended properties so none are lost.
             * The properties added to extended properties should be from subtypes that are not part of the
             * open metadata type standard.  However, there may also be properties that are not (yet) explicitly
             * mapped to the converter process below.
             */
            ElementProperties elementProperties = new ElementProperties(relationshipProperties);

            if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ACCEPTED_ANSWER_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AcceptedAnswerProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ActionRequesterProperties();

                ((ActionRequesterProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((ActionRequesterProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ACTIONS_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ActionsProperties();

                ((ActionsProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((ActionsProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ActionTargetProperties();

                ((ActionTargetProperties)relationshipBeanProperties).setActionTargetName(this.removeActionTargetName(elementProperties));
                ((ActionTargetProperties)relationshipBeanProperties).setActivityStatus(this.removeActivityStatus(elementProperties));
                ((ActionTargetProperties)relationshipBeanProperties).setStartTime(this.removeStartTime(elementProperties));
                ((ActionTargetProperties)relationshipBeanProperties).setCompletionTime(this.removeCompletionTime(elementProperties));
                ((ActionTargetProperties)relationshipBeanProperties).setCompletionMessage(this.removeCompletionMessage(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ADJACENT_LOCATION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AdjacentLocationProperties();

                ((AdjacentLocationProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((AdjacentLocationProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.AGREEMENT_ACTOR_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AgreementActorProperties();

                ((AgreementActorProperties)relationshipBeanProperties).setActorName(this.removeActorName(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AgreementItemProperties();

                ((AgreementItemProperties)relationshipBeanProperties).setAgreementItemId(this.removeAgreementItemId(elementProperties));
                ((AgreementItemProperties)relationshipBeanProperties).setAgreementStart(this.removeAgreementStart(elementProperties));
                ((AgreementItemProperties)relationshipBeanProperties).setAgreementEnd(this.removeAgreementEnd(elementProperties));
                ((AgreementItemProperties)relationshipBeanProperties).setEntitlements(this.removeEntitlements(elementProperties));
                ((AgreementItemProperties)relationshipBeanProperties).setRestrictions(this.removeRestrictions(elementProperties));
                ((AgreementItemProperties)relationshipBeanProperties).setObligations(this.removeObligations(elementProperties));
                ((AgreementItemProperties)relationshipBeanProperties).setUsageMeasurements(this.removeUsageMeasurements(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ANNOTATION_EXTENSION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AnnotationExtensionProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ANTONYM_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AntonymProperties();

                ((AntonymProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((AntonymProperties)relationshipBeanProperties).setExpression(this.removeExpression(elementProperties));
                ((AntonymProperties)relationshipBeanProperties).setStatus(this.removeStatus(elementProperties));
                ((AntonymProperties)relationshipBeanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((AntonymProperties)relationshipBeanProperties).setSteward(this.removeSteward(elementProperties));
                ((AntonymProperties)relationshipBeanProperties).setSource(this.removeSource(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.API_ENDPOINT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new APIEndpointProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ARCHIVE_CONTENTS_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ArchiveContentsProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AssetConnectionProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ASSET_SCHEMA_TYPE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AssetSchemaTypeProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AssignmentScopeProperties();

                ((AssignmentScopeProperties)relationshipBeanProperties).setAssignmentType(this.removeAssignmentType(elementProperties));
                ((AssignmentScopeProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AssociatedAnnotationProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ASSOCIATED_LOG_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AssociatedLogProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ASSOCIATED_SECURITY_GROUP_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AssociatedSecurityGroupProperties();

                ((AssociatedSecurityGroupProperties)relationshipBeanProperties).setOperationName(this.removeOperationName(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AttachedCommentProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AttachedLikeProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AttachedRatingProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AttachedTagProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AttributeForSchemaProperties();

                ((AttributeForSchemaProperties)relationshipBeanProperties).setPosition(this.removePosition(elementProperties));
                ((AttributeForSchemaProperties)relationshipBeanProperties).setMaxCardinality(this.removeMaxCardinality(elementProperties));
                ((AttributeForSchemaProperties)relationshipBeanProperties).setMinCardinality(this.removeMinCardinality(elementProperties));
                ((AttributeForSchemaProperties)relationshipBeanProperties).setCoverageCategory(this.removeCoverageCategory(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.BUSINESS_CAPABILITY_DEPENDENCY_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new BusinessCapabilityDependencyProperties();

                ((BusinessCapabilityDependencyProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((BusinessCapabilityDependencyProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new CapabilityAssetUseProperties();

                ((CapabilityAssetUseProperties)relationshipBeanProperties).setUseType(this.removeUseType(elementProperties));
                ((CapabilityAssetUseProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((CapabilityAssetUseProperties)relationshipBeanProperties).setMinimumInstances(this.removeMinimumInstances(elementProperties));
                ((CapabilityAssetUseProperties)relationshipBeanProperties).setMaximumInstances(this.removeMaximumInstances(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new CatalogTargetProperties();

                ((CatalogTargetProperties)relationshipBeanProperties).setCatalogTargetName(this.removeCatalogTargetName(elementProperties));
                ((CatalogTargetProperties)relationshipBeanProperties).setMetadataSourceQualifiedName(this.removeMetadataSourceQualifiedName(elementProperties));
                ((CatalogTargetProperties)relationshipBeanProperties).setConnectionName(this.removeConnectionName(elementProperties));
                ((CatalogTargetProperties)relationshipBeanProperties).setConfigurationProperties(this.removeConfigurationProperties(elementProperties));
                ((CatalogTargetProperties)relationshipBeanProperties).setTemplates(this.removeTemplates(elementProperties));
                ((CatalogTargetProperties)relationshipBeanProperties).setPermittedSynchronization(this.removePermittedSynchronization(elementProperties));
                ((CatalogTargetProperties)relationshipBeanProperties).setDeleteMethod(this.removeDeleteMethod(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new CatalogTemplateProperties();

                ((CatalogTemplateProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((CatalogTemplateProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new CertificationProperties();

                ((CertificationProperties)relationshipBeanProperties).setCertificateGUID(this.removeCertificateGUID(elementProperties));
                ((CertificationProperties)relationshipBeanProperties).setCoverageStart(this.removeCoverageStart(elementProperties));
                ((CertificationProperties)relationshipBeanProperties).setCoverageEnd(this.removeCoverageEnd(elementProperties));
                ((CertificationProperties)relationshipBeanProperties).setConditions(this.removeConditions(elementProperties));
                ((CertificationProperties)relationshipBeanProperties).setCertifiedBy(this.removeCertifiedBy(elementProperties));
                ((CertificationProperties)relationshipBeanProperties).setCertifiedByTypeName(this.removeCertifiedByTypeName(elementProperties));
                ((CertificationProperties)relationshipBeanProperties).setCertifiedByPropertyName(this.removeCertifiedByPropertyName(elementProperties));
                ((CertificationProperties)relationshipBeanProperties).setCustodian(this.removeCustodian(elementProperties));
                ((CertificationProperties)relationshipBeanProperties).setCustodianTypeName(this.removeCustodianTypeName(elementProperties));
                ((CertificationProperties)relationshipBeanProperties).setCustodianPropertyName(this.removeCustodianPropertyName(elementProperties));
                ((CertificationProperties)relationshipBeanProperties).setRecipient(this.removeRecipient(elementProperties));
                ((CertificationProperties)relationshipBeanProperties).setRecipientTypeName(this.removeRecipientTypeName(elementProperties));
                ((CertificationProperties)relationshipBeanProperties).setRecipientPropertyName(this.removeRecipientPropertyName(elementProperties));
                ((CertificationProperties)relationshipBeanProperties).setNotes(this.removeNotes(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CITED_DOCUMENT_LINK_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new CitedDocumentLinkProperties();

                ((CitedDocumentLinkProperties)relationshipBeanProperties).setReferenceId(this.removeReferenceId(elementProperties));
                ((CitedDocumentLinkProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((CitedDocumentLinkProperties)relationshipBeanProperties).setPages(this.removePages(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new CollectionMembershipProperties();

                ((CollectionMembershipProperties)relationshipBeanProperties).setMembershipType(this.removeMembershipType(elementProperties));
                ((CollectionMembershipProperties)relationshipBeanProperties).setMembershipRationale(this.removeMembershipRationale(elementProperties));
                ((CollectionMembershipProperties)relationshipBeanProperties).setExpression(this.removeExpression(elementProperties));
                ((CollectionMembershipProperties)relationshipBeanProperties).setMembershipStatus(this.removeMembershipStatus(elementProperties));
                ((CollectionMembershipProperties)relationshipBeanProperties).setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                ((CollectionMembershipProperties)relationshipBeanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((CollectionMembershipProperties)relationshipBeanProperties).setSteward(this.removeSteward(elementProperties));
                ((CollectionMembershipProperties)relationshipBeanProperties).setStewardTypeName(this.removeStewardTypeName(elementProperties));
                ((CollectionMembershipProperties)relationshipBeanProperties).setStewardPropertyName(this.removeStewardPropertyName(elementProperties));
                ((CollectionMembershipProperties)relationshipBeanProperties).setSource(this.removeSource(elementProperties));
                ((CollectionMembershipProperties)relationshipBeanProperties).setNotes(this.removeNotes(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CONCEPT_DESIGN_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ConceptDesignProperties();

                ((ConceptDesignProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((ConceptDesignProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ConnectionConnectorTypeProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ConnectToEndpointProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ConsistentValidValuesProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ContactThroughProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CONTEXT_EVENT_EVIDENCE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ContextEventEvidenceProperties();

                ((ContextEventEvidenceProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((ContextEventEvidenceProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CONTEXT_EVENT_FOR_TIMELINE_EFFECTS_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ContextEventForTimelineEffectsProperties();

                ((ContextEventForTimelineEffectsProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((ContextEventForTimelineEffectsProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CONTEXT_EVENT_IMPACT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ContextEventImpactProperties();

                ((ContextEventImpactProperties)relationshipBeanProperties).setSeverityLevelIdentifier(this.removeSeverityLevelIdentifier(elementProperties));
                ((ContextEventImpactProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((ContextEventImpactProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CONTRACT_LINK_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ContractLinkProperties();

                ((ContractLinkProperties)relationshipBeanProperties).setContractId(this.removeContractId(elementProperties));
                ((ContractLinkProperties)relationshipBeanProperties).setContractLiaison(this.removeContractLiaison(elementProperties));
                ((ContractLinkProperties)relationshipBeanProperties).setContractLiaisonTypeName(this.removeContractLiaisonTypeName(elementProperties));
                ((ContractLinkProperties)relationshipBeanProperties).setContractLiaisonPropertyName(this.removeContractLiaisonPropertyName(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CONTRIBUTION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ContributionProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ControlFlowProperties();

                ((ControlFlowProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((ControlFlowProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((ControlFlowProperties)relationshipBeanProperties).setISCQualifiedName(this.removeISCQualifiedName(elementProperties));
                ((ControlFlowProperties)relationshipBeanProperties).setGuard(this.removeGuard(elementProperties));
                ((ControlFlowProperties)relationshipBeanProperties).setMandatoryGuard(this.removeMandatoryGuard(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CROWD_SOURCING_CONTRIBUTION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new CrowdSourcingContributionProperties();

                ((CrowdSourcingContributionProperties)relationshipBeanProperties).setRoleType(this.removeRoleType(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DATA_CLASS_ASSIGNMENT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new DataClassAssignmentProperties();

                ((DataClassAssignmentProperties)relationshipBeanProperties).setMethod(this.removeMethod(elementProperties));
                ((DataClassAssignmentProperties)relationshipBeanProperties).setSource(this.removeSource(elementProperties));
                ((DataClassAssignmentProperties)relationshipBeanProperties).setThreshold(this.removeThreshold(elementProperties));
                ((DataClassAssignmentProperties)relationshipBeanProperties).setDataClassAssignmentStatus(this.removeDataClassAssignmentStatus(elementProperties));
                ((DataClassAssignmentProperties)relationshipBeanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((DataClassAssignmentProperties)relationshipBeanProperties).setSteward(this.removeSteward(elementProperties));
                ((DataClassAssignmentProperties)relationshipBeanProperties).setStewardTypeName(this.removeStewardTypeName(elementProperties));
                ((DataClassAssignmentProperties)relationshipBeanProperties).setStewardPropertyName(this.removeStewardPropertyName(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DATA_CLASS_COMPOSITION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new DataClassCompositionProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DATA_CLASS_DEFINITION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new DataClassDefinitionProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DATA_CLASS_HIERARCHY_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new DataClassHierarchyProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DATA_CLASS_MATCH_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new DataClassMatchProperties();

                ((DataClassMatchProperties)relationshipBeanProperties).setMethod(this.removeMethod(elementProperties));
                ((DataClassMatchProperties)relationshipBeanProperties).setThreshold(this.removeThreshold(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DATA_DESCRIPTION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new DataDescriptionProperties();

                ((DataDescriptionProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((DataDescriptionProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new DataFlowProperties();

                ((DataFlowProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((DataFlowProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((DataFlowProperties)relationshipBeanProperties).setISCQualifiedName(this.removeISCQualifiedName(elementProperties));
                ((DataFlowProperties)relationshipBeanProperties).setFormula(this.removeFormula(elementProperties));
                ((DataFlowProperties)relationshipBeanProperties).setFormulaType(this.removeFormulaType(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new DataMappingProperties();

                ((DataMappingProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((DataMappingProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((DataMappingProperties)relationshipBeanProperties).setISCQualifiedName(this.removeISCQualifiedName(elementProperties));
                ((DataMappingProperties)relationshipBeanProperties).setFormula(this.removeFormula(elementProperties));
                ((DataMappingProperties)relationshipBeanProperties).setFormulaType(this.removeFormulaType(elementProperties));
                ((DataMappingProperties)relationshipBeanProperties).setQueryType(this.removeQueryType(elementProperties));
                ((DataMappingProperties)relationshipBeanProperties).setQuery(this.removeQuery(elementProperties));
                ((DataMappingProperties)relationshipBeanProperties).setQueryId(this.removeQueryId(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new DataSetContentProperties();

                ((DataSetContentProperties)relationshipBeanProperties).setISCQualifiedName(this.removeISCQualifiedName(elementProperties));
                ((DataSetContentProperties)relationshipBeanProperties).setQueryType(this.removeQueryType(elementProperties));
                ((DataSetContentProperties)relationshipBeanProperties).setQuery(this.removeQuery(elementProperties));
                ((DataSetContentProperties)relationshipBeanProperties).setQueryId(this.removeQueryId(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DATA_STRUCTURE_DEFINITION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new DataStructureDefinitionProperties();

                ((DataStructureDefinitionProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((DataStructureDefinitionProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DEPENDENT_CONTEXT_EVENT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new DependentContextEventProperties();

                ((DependentContextEventProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((DependentContextEventProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new DeployedOnProperties();

                ((DeployedOnProperties)relationshipBeanProperties).setDeploymentTime(this.removeDeploymentTime(elementProperties));
                ((DeployedOnProperties)relationshipBeanProperties).setDeployer(this.removeDeployer(elementProperties));
                ((DeployedOnProperties)relationshipBeanProperties).setDeployerTypeName(this.removeDeployerTypeName(elementProperties));
                ((DeployedOnProperties)relationshipBeanProperties).setDeployerPropertyName(this.removeDeployerPropertyName(elementProperties));
                ((DeployedOnProperties)relationshipBeanProperties).setOperationalStatus(this.removeOperationalStatus(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new DerivedSchemaTypeQueryTargetProperties();

                ((DerivedSchemaTypeQueryTargetProperties)relationshipBeanProperties).setQueryId(this.removeQueryId(elementProperties));
                ((DerivedSchemaTypeQueryTargetProperties)relationshipBeanProperties).setQuery(this.removeQuery(elementProperties));
                ((DerivedSchemaTypeQueryTargetProperties)relationshipBeanProperties).setQueryType(this.removeQueryType(elementProperties));
                ((DerivedSchemaTypeQueryTargetProperties)relationshipBeanProperties).setISCQualifiedName(this.removeISCQualifiedName(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new DigitalProductDependencyProperties();

                ((DigitalProductDependencyProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((DigitalProductDependencyProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((DigitalProductDependencyProperties)relationshipBeanProperties).setISCQualifiedName(this.removeISCQualifiedName(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DIGITAL_SUBSCRIBER_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new DigitalSubscriberProperties();

                ((DigitalSubscriberProperties)relationshipBeanProperties).setSubscriberId(this.removeSubscriberId(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DIGITAL_SUPPORT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new DigitalSupportProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DISCOVERED_SCHEMA_TYPE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new DiscoveredSchemaTypeProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new EmbeddedConnectionProperties();

                ((EmbeddedConnectionProperties)relationshipBeanProperties).setPosition(this.removePosition(elementProperties));
                ((EmbeddedConnectionProperties)relationshipBeanProperties).setDisplayName(this.removeDisplayName(elementProperties));
                ((EmbeddedConnectionProperties)relationshipBeanProperties).setArguments(this.removeArguments(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ExternalIdLinkProperties();

                ((ExternalIdLinkProperties)relationshipBeanProperties).setUsage(this.removeUsage(elementProperties));
                ((ExternalIdLinkProperties)relationshipBeanProperties).setSource(this.removeSource(elementProperties));
                ((ExternalIdLinkProperties)relationshipBeanProperties).setLastSynchronized(this.removeLastSynchronized(elementProperties));
                ((ExternalIdLinkProperties)relationshipBeanProperties).setMappingProperties(this.removeMappingProperties(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ExternalIdScopeProperties();

                ((ExternalIdScopeProperties)relationshipBeanProperties).setPermittedSynchronization(this.removePermittedSynchronization(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ExternalReferenceLinkProperties();

                ((ExternalReferenceLinkProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((ExternalReferenceLinkProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new FolderHierarchyProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new GovernanceActionExecutorProperties();

                ((GovernanceActionExecutorProperties)relationshipBeanProperties).setRequestType(this.removeGuard(elementProperties));
                ((GovernanceActionExecutorProperties)relationshipBeanProperties).setRequestParameters(this.removeRequestParameters(elementProperties));
                ((GovernanceActionExecutorProperties)relationshipBeanProperties).setRequestParameterFilter(this.removeRequestParameterFilter(elementProperties));
                ((GovernanceActionExecutorProperties)relationshipBeanProperties).setRequestParameterMap(this.removeRequestParameterMap(elementProperties));
                ((GovernanceActionExecutorProperties)relationshipBeanProperties).setActionTargetFilter(this.removeActionTargetFilter(elementProperties));
                ((GovernanceActionExecutorProperties)relationshipBeanProperties).setActionTargetMap(this.removeActionTargetMap(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new GovernanceActionProcessFlowProperties();

                ((GovernanceActionProcessFlowProperties)relationshipBeanProperties).setGuard(this.removeGuard(elementProperties));
                ((GovernanceActionProcessFlowProperties)relationshipBeanProperties).setRequestParameters(this.removeRequestParameters(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.GOVERNANCE_CONTROL_LINK_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new GovernanceControlLinkProperties();

                ((GovernanceControlLinkProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((GovernanceControlLinkProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.GOVERNANCE_DRIVER_LINK_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new GovernanceDriverLinkProperties();

                ((GovernanceDriverLinkProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((GovernanceDriverLinkProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.GOVERNANCE_POLICY_LINK_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new GovernancePolicyLinkProperties();

                ((GovernancePolicyLinkProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((GovernancePolicyLinkProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.GOVERNANCE_MECHANISM_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new GovernanceMechanismProperties();

                ((GovernanceMechanismProperties)relationshipBeanProperties).setRationale(this.removeRationale(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new GovernanceResponseProperties();

                ((GovernanceResponseProperties)relationshipBeanProperties).setRationale(this.removeRationale(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.GOVERNANCE_RESULTS_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new GovernanceResultsProperties();

                ((GovernanceResultsProperties)relationshipBeanProperties).setQuery(this.removeQuery(elementProperties));
                ((GovernanceResultsProperties)relationshipBeanProperties).setQueryType(this.removeQueryType(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new GovernedByProperties();

                ((GovernedByProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((GovernedByProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.GRAPH_EDGE_LINK_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new GraphEdgeLinkProperties();

                ((GraphEdgeLinkProperties)relationshipBeanProperties).setLinkTypeName(this.removeLinkTypeName(elementProperties));
                ((GraphEdgeLinkProperties)relationshipBeanProperties).setRelationshipEnd(this.removeRelationshipEnd(elementProperties));
                ((GraphEdgeLinkProperties)relationshipBeanProperties).setRelationshipEndName(this.removeRelationshipEndName(elementProperties));
                ((GraphEdgeLinkProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((GraphEdgeLinkProperties)relationshipBeanProperties).setMinCardinality(this.removeMinCardinality(elementProperties));
                ((GraphEdgeLinkProperties)relationshipBeanProperties).setMaxCardinality(this.removeMaxCardinality(elementProperties));
                ((GraphEdgeLinkProperties)relationshipBeanProperties).setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ForeignKeyProperties();

                ((ForeignKeyProperties)relationshipBeanProperties).setDisplayName(this.removeDisplayName(elementProperties));
                ((ForeignKeyProperties)relationshipBeanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((ForeignKeyProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((ForeignKeyProperties)relationshipBeanProperties).setSteward(this.removeSteward(elementProperties));
                ((ForeignKeyProperties)relationshipBeanProperties).setSource(this.removeSource(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.IMPACTED_RESOURCE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ImpactedResourceProperties();

                ((ImpactedResourceProperties)relationshipBeanProperties).setSeverityLevelIdentifier(this.removeSeverityLevelIdentifier(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ImplementationResourceProperties();

                ((ImplementationResourceProperties)relationshipBeanProperties).setRole(this.removeRole(elementProperties));
                ((ImplementationResourceProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ImplementedByProperties();

                ((ImplementedByProperties)relationshipBeanProperties).setRole(this.removeRole(elementProperties));
                ((ImplementedByProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((ImplementedByProperties)relationshipBeanProperties).setDesignStep(this.removeDesignStep(elementProperties));
                ((ImplementedByProperties)relationshipBeanProperties).setTransformation(this.removeDesignStep(elementProperties));
                ((ImplementedByProperties)relationshipBeanProperties).setISCQualifiedName(this.removeISCQualifiedName(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.INFORMATION_SUPPLY_CHAIN_LINK_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new InformationSupplyChainLinkProperties();

                ((InformationSupplyChainLinkProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((InformationSupplyChainLinkProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ISA_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ISARelationshipProperties();

                ((ISARelationshipProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((ISARelationshipProperties)relationshipBeanProperties).setExpression(this.removeExpression(elementProperties));
                ((ISARelationshipProperties)relationshipBeanProperties).setStatus(this.removeStatus(elementProperties));
                ((ISARelationshipProperties)relationshipBeanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((ISARelationshipProperties)relationshipBeanProperties).setSteward(this.removeSteward(elementProperties));
                ((ISARelationshipProperties)relationshipBeanProperties).setSource(this.removeSource(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ITInfrastructureProfileProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.IT_PROFILE_ROLE_APPOINTMENT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ITProfileRoleAppointmentProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.KNOWN_LOCATION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new KnownLocationProperties();

                ((KnownLocationProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((KnownLocationProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.LICENSE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new LicenseProperties();

                ((LicenseProperties)relationshipBeanProperties).setLicenseGUID(this.removeLicenseGUID(elementProperties));
                ((LicenseProperties)relationshipBeanProperties).setCoverageStart(this.removeCoverageStart(elementProperties));
                ((LicenseProperties)relationshipBeanProperties).setCoverageEnd(this.removeCoverageEnd(elementProperties));
                ((LicenseProperties)relationshipBeanProperties).setConditions(this.removeConditions(elementProperties));
                ((LicenseProperties)relationshipBeanProperties).setLicensedBy(this.removeLicensedBy(elementProperties));
                ((LicenseProperties)relationshipBeanProperties).setLicensedByTypeName(this.removeLicensedByTypeName(elementProperties));
                ((LicenseProperties)relationshipBeanProperties).setLicensedByPropertyName(this.removeLicensedByPropertyName(elementProperties));
                ((LicenseProperties)relationshipBeanProperties).setCustodian(this.removeCustodian(elementProperties));
                ((LicenseProperties)relationshipBeanProperties).setCustodianTypeName(this.removeCustodianTypeName(elementProperties));
                ((LicenseProperties)relationshipBeanProperties).setCustodianPropertyName(this.removeCustodianPropertyName(elementProperties));
                ((LicenseProperties)relationshipBeanProperties).setLicensee(this.removeLicensee(elementProperties));
                ((LicenseProperties)relationshipBeanProperties).setLicenseeTypeName(this.removeLicenseeTypeName(elementProperties));
                ((LicenseProperties)relationshipBeanProperties).setLicenseePropertyName(this.removeLicenseePropertyName(elementProperties));
                ((LicenseProperties)relationshipBeanProperties).setEntitlements(this.removeEntitlements(elementProperties));
                ((LicenseProperties)relationshipBeanProperties).setRestrictions(this.removeRestrictions(elementProperties));
                ((LicenseProperties)relationshipBeanProperties).setObligations(this.removeObligations(elementProperties));
                ((LicenseProperties)relationshipBeanProperties).setNotes(this.removeNotes(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new LineageMappingProperties();

                ((LineageMappingProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((LineageMappingProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((LineageMappingProperties)relationshipBeanProperties).setISCQualifiedName(this.removeISCQualifiedName(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new LinkedExternalSchemaTypeProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.LINKED_FILE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new LinkedFileProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.LINKED_MEDIA_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new LinkedMediaProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.MAP_FROM_ELEMENT_TYPE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new MapFromElementTypeProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.MAP_TO_ELEMENT_TYPE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new MapToElementTypeProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.MEDIA_REFERENCE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new MediaReferenceProperties();

                ((MediaReferenceProperties)relationshipBeanProperties).setMediaId(this.removeMediaId(elementProperties));
                ((MediaReferenceProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                if (((MediaReferenceProperties)relationshipBeanProperties).getMediaUsage() != null)
                {
                    ((MediaReferenceProperties)relationshipBeanProperties).setMediaUsage(this.removeDefaultMediaUsage(elementProperties));
                }
                ((MediaReferenceProperties)relationshipBeanProperties).setMediaUsageOtherId(this.removeDefaultMediaUsageOtherId(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new MemberDataFieldProperties();

                ((MemberDataFieldProperties)relationshipBeanProperties).setCoverageCategory(this.removeCoverageCategory(elementProperties));
                ((MemberDataFieldProperties)relationshipBeanProperties).setPosition(this.removePosition(elementProperties));
                ((MemberDataFieldProperties)relationshipBeanProperties).setMaxCardinality(this.removeMaxCardinality(elementProperties));
                ((MemberDataFieldProperties)relationshipBeanProperties).setMinCardinality(this.removeMinCardinality(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.METADATA_COHORT_PEER_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new MetadataCohortPeerProperties();

                ((MetadataCohortPeerProperties)relationshipBeanProperties).setRegistrationDate(this.removeRegistrationDate(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new MonitoredResourceProperties();

                ((MonitoredResourceProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((MonitoredResourceProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new MoreInformationProperties();

                ((MoreInformationProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((MoreInformationProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new NestedDataFieldProperties();

                ((NestedDataFieldProperties)relationshipBeanProperties).setCoverageCategory(this.removeCoverageCategory(elementProperties));
                ((NestedDataFieldProperties)relationshipBeanProperties).setPosition(this.removePosition(elementProperties));
                ((NestedDataFieldProperties)relationshipBeanProperties).setMaxCardinality(this.removeMaxCardinality(elementProperties));
                ((NestedDataFieldProperties)relationshipBeanProperties).setMinCardinality(this.removeMinCardinality(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.NESTED_FILE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new NestedFileProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.NESTED_LOCATION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new NestedLocationProperties();

                ((NestedLocationProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((NestedLocationProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new NestedSchemaAttributeProperties();

                ((NestedSchemaAttributeProperties)relationshipBeanProperties).setCoverageCategory(this.removeCoverageCategory(elementProperties));
                ((NestedSchemaAttributeProperties)relationshipBeanProperties).setPosition(this.removePosition(elementProperties));
                ((NestedSchemaAttributeProperties)relationshipBeanProperties).setMaxCardinality(this.removeMaxCardinality(elementProperties));
                ((NestedSchemaAttributeProperties)relationshipBeanProperties).setMinCardinality(this.removeMinCardinality(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.NETWORK_GATEWAY_LINK_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new NetworkGatewayLinkProperties();

                ((NetworkGatewayLinkProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((NetworkGatewayLinkProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((NetworkGatewayLinkProperties)relationshipBeanProperties).setInternalEndpointAddress(this.removeInternalEndpointAddress(elementProperties));
                ((NetworkGatewayLinkProperties)relationshipBeanProperties).setExternalEndpointAddress(this.removeExternalEndpointAddress(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new NextGovernanceActionProcessStepProperties();

                ((NextGovernanceActionProcessStepProperties)relationshipBeanProperties).setGuard(this.removeGuard(elementProperties));
                ((NextGovernanceActionProcessStepProperties)relationshipBeanProperties).setMandatoryGuard(this.removeMandatoryGuard(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.NOTIFICATION_SUBSCRIBER_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new NotificationSubscriberProperties();

                ((NotificationSubscriberProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((NotificationSubscriberProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((NotificationSubscriberProperties)relationshipBeanProperties).setActivityStatus(this.removeActivityStatus(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.PEER_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new PeerProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new PersonRoleAppointmentProperties();

                ((PersonRoleAppointmentProperties)relationshipBeanProperties).setExpectedTimeAllocationPercent(this.removeExpectedTimeAllocationPercent(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.PORT_DELEGATION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new PortDelegationProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.PORT_SCHEMA_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new PortSchemaProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.PREFERRED_TERM_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new PreferredTermProperties();

                ((PreferredTermProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((PreferredTermProperties)relationshipBeanProperties).setExpression(this.removeExpression(elementProperties));
                ((PreferredTermProperties)relationshipBeanProperties).setStatus(this.removeStatus(elementProperties));
                ((PreferredTermProperties)relationshipBeanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((PreferredTermProperties)relationshipBeanProperties).setSteward(this.removeSteward(elementProperties));
                ((PreferredTermProperties)relationshipBeanProperties).setSource(this.removeSource(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ProcessCallProperties();

                ((ProcessCallProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((ProcessCallProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((ProcessCallProperties)relationshipBeanProperties).setISCQualifiedName(this.removeISCQualifiedName(elementProperties));
                ((ProcessCallProperties)relationshipBeanProperties).setFormula(this.removeFormula(elementProperties));
                ((ProcessCallProperties)relationshipBeanProperties).setFormulaType(this.removeFormulaType(elementProperties));
                ((ProcessCallProperties)relationshipBeanProperties).setLineNumber(this.removeLineNumber(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ProcessHierarchyProperties();

                ((ProcessHierarchyProperties)relationshipBeanProperties).setContainmentType(this.removeContainmentType(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.PROCESS_PORT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ProcessPortProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ProfileIdentityProperties();

                ((ProfileIdentityProperties)relationshipBeanProperties).setRoleTypeName(this.removeRoleTypeName(elementProperties));
                ((ProfileIdentityProperties)relationshipBeanProperties).setRoleGUID(this.removeRoleGUID(elementProperties));
                ((ProfileIdentityProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ProjectDependencyProperties();

                ((ProjectDependencyProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((ProjectDependencyProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ProjectHierarchyProperties();

                ((ProjectHierarchyProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((ProjectHierarchyProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ReferenceableFacetProperties();

                ((ReferenceableFacetProperties)relationshipBeanProperties).setSource(this.removeLabel(elementProperties));
                ((ReferenceableFacetProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((ReferenceableFacetProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ReferenceValueAssignmentProperties();

                ((ReferenceValueAssignmentProperties)relationshipBeanProperties).setAttributeName(this.removeAttributeName(elementProperties));
                ((ReferenceValueAssignmentProperties)relationshipBeanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((ReferenceValueAssignmentProperties)relationshipBeanProperties).setSteward(this.removeSteward(elementProperties));
                ((ReferenceValueAssignmentProperties)relationshipBeanProperties).setStewardTypeName(this.removeStewardTypeName(elementProperties));
                ((ReferenceValueAssignmentProperties)relationshipBeanProperties).setStewardPropertyName(this.removeStewardPropertyName(elementProperties));
                ((ReferenceValueAssignmentProperties)relationshipBeanProperties).setNotes(this.removeNotes(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new RegisteredIntegrationConnectorProperties();

                ((RegisteredIntegrationConnectorProperties)relationshipBeanProperties).setConnectorName(this.removeConnectorName(elementProperties));
                ((RegisteredIntegrationConnectorProperties)relationshipBeanProperties).setConnectorUserId(this.removeConnectorUserId(elementProperties));
                ((RegisteredIntegrationConnectorProperties)relationshipBeanProperties).setMetadataSourceQualifiedName(this.removeMetadataSourceQualifiedName(elementProperties));
                ((RegisteredIntegrationConnectorProperties)relationshipBeanProperties).setStartDate(this.removeStartDate(elementProperties));
                ((RegisteredIntegrationConnectorProperties)relationshipBeanProperties).setRefreshTimeInterval(this.removeRefreshTimeInterval(elementProperties));
                ((RegisteredIntegrationConnectorProperties)relationshipBeanProperties).setConnectorShutdownDate(this.removeConnectorShutdownDate(elementProperties));
                ((RegisteredIntegrationConnectorProperties)relationshipBeanProperties).setPermittedSynchronization(this.removePermittedSynchronization(elementProperties));
                ((RegisteredIntegrationConnectorProperties)relationshipBeanProperties).setGenerateConnectorActivityReports(this.removeGenerateConnectorActivityReports(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.REGULATION_CERTIFICATION_TYPE.typeName))
            {
                relationshipBeanProperties = new RegulationCertificationTypeProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.RELATED_CONTEXT_EVENT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new RelatedContextEventProperties();

                ((RelatedContextEventProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((RelatedContextEventProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((RelatedContextEventProperties)relationshipBeanProperties).setStatusIdentifier(this.removeStatusIdentifier(elementProperties));
                ((RelatedContextEventProperties)relationshipBeanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((RelatedContextEventProperties)relationshipBeanProperties).setSteward(this.removeSteward(elementProperties));
                ((RelatedContextEventProperties)relationshipBeanProperties).setStewardTypeName(this.removeStewardTypeName(elementProperties));
                ((RelatedContextEventProperties)relationshipBeanProperties).setStewardPropertyName(this.removeStewardPropertyName(elementProperties));
                ((RelatedContextEventProperties)relationshipBeanProperties).setSource(this.removeSource(elementProperties));
                ((RelatedContextEventProperties)relationshipBeanProperties).setNotes(this.removeNotes(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.RELATED_TERM_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new RelatedTermProperties();

                ((RelatedTermProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((RelatedTermProperties)relationshipBeanProperties).setExpression(this.removeExpression(elementProperties));
                ((RelatedTermProperties)relationshipBeanProperties).setStatus(this.removeStatus(elementProperties));
                ((RelatedTermProperties)relationshipBeanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((RelatedTermProperties)relationshipBeanProperties).setSteward(this.removeSteward(elementProperties));
                ((RelatedTermProperties)relationshipBeanProperties).setSource(this.removeSource(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.REPLACEMENT_TERM_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ReplacementTermProperties();

                ((ReplacementTermProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((ReplacementTermProperties)relationshipBeanProperties).setExpression(this.removeExpression(elementProperties));
                ((ReplacementTermProperties)relationshipBeanProperties).setStatus(this.removeStatus(elementProperties));
                ((ReplacementTermProperties)relationshipBeanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((ReplacementTermProperties)relationshipBeanProperties).setSteward(this.removeSteward(elementProperties));
                ((ReplacementTermProperties)relationshipBeanProperties).setSource(this.removeSource(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ReportedAnnotationProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.REPORT_DEPENDENCY_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ReportDependencyProperties();

                ((ReportDependencyProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((ReportDependencyProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.REPORT_ORIGINATOR.typeName))
            {
                relationshipBeanProperties = new ReportOriginatorProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.REPORT_SUBJECT.typeName))
            {
                relationshipBeanProperties = new ReportSubjectProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.REQUEST_FOR_ACTION_TARGET_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new RequestForActionTargetProperties();

                ((RequestForActionTargetProperties)relationshipBeanProperties).setActionTargetName(this.removeActionTargetName(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ResourceListProperties();

                ((ResourceListProperties)relationshipBeanProperties).setResourceUse(this.removeResourceUse(elementProperties));
                ((ResourceListProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((ResourceListProperties)relationshipBeanProperties).setDisplayName(this.removeDisplayName(elementProperties));
                ((ResourceListProperties)relationshipBeanProperties).setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.RESOURCE_PROFILE_DATA_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ResourceProfileDataProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SAMPLE_DATA_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new SampleDataProperties();

                ((SampleDataProperties)relationshipBeanProperties).setSamplingMethod(this.removeSamplingMethod(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new SchemaTypeOptionProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ScopedByProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new SearchKeywordLinkProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new SemanticAssignmentProperties();

                ((SemanticAssignmentProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((SemanticAssignmentProperties)relationshipBeanProperties).setExpression(this.removeExpression(elementProperties));
                ((SemanticAssignmentProperties)relationshipBeanProperties).setTermAssignmentStatus(this.removeTermAssignmentStatus(elementProperties));
                ((SemanticAssignmentProperties)relationshipBeanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((SemanticAssignmentProperties)relationshipBeanProperties).setSteward(this.removeSteward(elementProperties));
                ((SemanticAssignmentProperties)relationshipBeanProperties).setStewardTypeName(this.removeStewardTypeName(elementProperties));
                ((SemanticAssignmentProperties)relationshipBeanProperties).setStewardPropertyName(this.removeStewardPropertyName(elementProperties));
                ((SemanticAssignmentProperties)relationshipBeanProperties).setSource(this.removeSource(elementProperties));
                ((SemanticAssignmentProperties)relationshipBeanProperties).setNotes(this.removeNotes(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SEMANTIC_DEFINITION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new SemanticDefinitionProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ServerEndpointProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SOLUTION_COMPONENT_ACTOR_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new SolutionComponentActorProperties();

                ((SolutionComponentActorProperties)relationshipBeanProperties).setRole(this.removeRole(elementProperties));
                ((SolutionComponentActorProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SOLUTION_COMPONENT_PORT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new SolutionComponentPortProperties();

                ((SolutionComponentPortProperties)relationshipBeanProperties).setRole(this.removeRole(elementProperties));
                ((SolutionComponentPortProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SOLUTION_COMPOSITION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new SolutionCompositionProperties();

                ((SolutionCompositionProperties)relationshipBeanProperties).setRole(this.removeRole(elementProperties));
                ((SolutionCompositionProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SOLUTION_DESIGN_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new SolutionDesignProperties();

                ((SolutionDesignProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((SolutionDesignProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new SolutionLinkingWireProperties();

                ((SolutionLinkingWireProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((SolutionLinkingWireProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((SolutionLinkingWireProperties)relationshipBeanProperties).setISCQualifiedNames(this.removeISCQualifiedNames(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SOLUTION_PORT_DELEGATION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new SolutionPortDelegationProperties();

                ((SolutionPortDelegationProperties)relationshipBeanProperties).setRole(this.removeRole(elementProperties));
                ((SolutionPortDelegationProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SOLUTION_PORT_SCHEMA_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new SolutionPortSchemaProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new SourcedFromProperties();

                ((SourcedFromProperties)relationshipBeanProperties).setSourceVersionNumber(this.removeSourceVersionNumber(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new SpecificationPropertyAssignmentProperties();

                ((SpecificationPropertyAssignmentProperties)relationshipBeanProperties).setPropertyName(this.removePropertyName(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new SubjectAreaHierarchyProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new SupportedGovernanceServiceProperties();

                ((SupportedGovernanceServiceProperties)relationshipBeanProperties).setRequestType(this.removeRequestType(elementProperties));
                ((SupportedGovernanceServiceProperties)relationshipBeanProperties).setServiceRequestType(this.removeServiceRequestType(elementProperties));
                ((SupportedGovernanceServiceProperties)relationshipBeanProperties).setRequestParameters(this.removeRequestParameters(elementProperties));
                ((SupportedGovernanceServiceProperties)relationshipBeanProperties).setGenerateConnectorActivityReports(this.removeGenerateConnectorActivityReports(elementProperties));
                ((SupportedGovernanceServiceProperties)relationshipBeanProperties).setDeleteMethod(this.removeDeleteMethod(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SUPPLEMENTARY_PROPERTIES_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new SupplementaryPropertiesProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.SYNONYM_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new SynonymProperties();

                ((SynonymProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((SynonymProperties)relationshipBeanProperties).setExpression(this.removeExpression(elementProperties));
                ((SynonymProperties)relationshipBeanProperties).setStatus(this.removeStatus(elementProperties));
                ((SynonymProperties)relationshipBeanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((SynonymProperties)relationshipBeanProperties).setSteward(this.removeSteward(elementProperties));
                ((SynonymProperties)relationshipBeanProperties).setSource(this.removeSource(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new TargetForGovernanceActionProperties();

                ((TargetForGovernanceActionProperties)relationshipBeanProperties).setActionTargetName(this.removeActionTargetName(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.TEAM_ROLE_APPOINTMENT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new TeamRoleAppointmentProperties();

                ((TeamRoleAppointmentProperties)relationshipBeanProperties).setExpectedTimeAllocationPercent(this.removeExpectedTimeAllocationPercent(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new TeamStructureProperties();

                ((TeamStructureProperties)relationshipBeanProperties).setDelegationEscalationAuthority(this.removeDelegationEscalationAuthority(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ULTIMATE_DESTINATION.typeName))
            {
                relationshipBeanProperties = new UltimateDestinationProperties();

                ((UltimateDestinationProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((UltimateDestinationProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((UltimateDestinationProperties)relationshipBeanProperties).setISCQualifiedName(this.removeISCQualifiedName(elementProperties));
                ((UltimateDestinationProperties)relationshipBeanProperties).setHops(this.removeHops(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ULTIMATE_SOURCE.typeName))
            {
                relationshipBeanProperties = new UltimateSourceProperties();

                ((UltimateSourceProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((UltimateSourceProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((UltimateSourceProperties)relationshipBeanProperties).setISCQualifiedName(this.removeISCQualifiedName(elementProperties));
                ((UltimateSourceProperties)relationshipBeanProperties).setHops(this.removeHops(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.USED_IN_CONTEXT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new UsedInContextProperties();

                ((UsedInContextProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((UsedInContextProperties)relationshipBeanProperties).setExpression(this.removeExpression(elementProperties));
                ((UsedInContextProperties)relationshipBeanProperties).setStatus(this.removeStatus(elementProperties));
                ((UsedInContextProperties)relationshipBeanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((UsedInContextProperties)relationshipBeanProperties).setSteward(this.removeSteward(elementProperties));
                ((UsedInContextProperties)relationshipBeanProperties).setSource(this.removeSource(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.VALID_VALUE_ASSOCIATION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ValidValueAssociationProperties();

                ((ValidValueAssociationProperties)relationshipBeanProperties).setAssociationName(this.removeAssociationName(elementProperties));
                ((ValidValueAssociationProperties)relationshipBeanProperties).setAssociationType(this.removeAssociationType(elementProperties));
                ((ValidValueAssociationProperties)relationshipBeanProperties).setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ValidValueMemberProperties();

                ((ValidValueMemberProperties)relationshipBeanProperties).setIsDefaultValue(this.removeIsDefaultValue(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ValidValuesAssignmentProperties();

                ((ValidValuesAssignmentProperties)relationshipBeanProperties).setStrictRequirement(this.removeStrictRequirement(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ValidValuesImplementationProperties();

                ((ValidValuesImplementationProperties)relationshipBeanProperties).setSymbolicName(this.removeSymbolicName(elementProperties));
                ((ValidValuesImplementationProperties)relationshipBeanProperties).setImplementationValue(this.removeImplementationValue(elementProperties));
                ((ValidValuesImplementationProperties)relationshipBeanProperties).setAdditionalValues(this.removeAdditionalValues(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ValidValuesMappingProperties();

                ((ValidValuesMappingProperties)relationshipBeanProperties).setAssociationDescription(this.removeAssociationDescription(elementProperties));
                ((ValidValuesMappingProperties)relationshipBeanProperties).setConfidence(this.removeConfidence(elementProperties));
                ((ValidValuesMappingProperties)relationshipBeanProperties).setSteward(this.removeSteward(elementProperties));
                ((ValidValuesMappingProperties)relationshipBeanProperties).setStewardTypeName(this.removeStewardTypeName(elementProperties));
                ((ValidValuesMappingProperties)relationshipBeanProperties).setStewardPropertyName(this.removeStewardPropertyName(elementProperties));
                ((ValidValuesMappingProperties)relationshipBeanProperties).setNotes(this.removeNotes(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.VISIBLE_ENDPOINT.typeName))
            {
                relationshipBeanProperties = new VisibleEndpointProperties();
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ZONE_HIERARCHY_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ZoneHierarchyProperties();
            }
            else
            {
                relationshipBeanProperties = new RelationshipBeanProperties();
            }

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype that we don't know about - but it also helps to catch
             * missing mappings in the code above.
             */
            relationshipBeanProperties.setTypeName(relationshipHeader.getType().getTypeName());
            relationshipBeanProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

            return relationshipBeanProperties;
        }

        return null;
    }


    /**
     * Uses the type of the entity to determine the type of bean to use for the properties.
     *
     * @param openMetadataElement element retrieved
     * @param methodName calling method
     * @return properties
     * @throws PropertyServerException problem in conversion
     */
    protected OpenMetadataRootProperties getBeanProperties(OpenMetadataElement openMetadataElement,
                                                           String              methodName) throws PropertyServerException
    {
        if (openMetadataElement != null)
        {
            return this.getBeanProperties(openMetadataElement);
        }
        else
        {
            handleMissingMetadataInstance(OpenMetadataRootProperties.class.getName(), OpenMetadataElement.class.getName(), methodName);
        }

        return null;
    }


    /**
     * Uses the type of the entity to determine the type of bean to use for the properties.
     *
     * @param openMetadataElement element retrieved
     * @return properties
     */
    public OpenMetadataRootProperties getBeanProperties(OpenMetadataElement openMetadataElement)
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

            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ANNOTATION.typeName))
            {
                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_FIELD_ANNOTATION.typeName))
                {
                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.CLASSIFICATION_ANNOTATION.typeName))
                    {
                        beanProperties = new ClassificationAnnotationProperties();

                        ((ClassificationAnnotationProperties)beanProperties).setCandidateClassifications(this.removeCandidateClassifications(elementProperties));
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_CLASS_ANNOTATION.typeName))
                    {
                        beanProperties = new DataClassAnnotationProperties();

                        ((DataClassAnnotationProperties)beanProperties).setCandidateDataClassGUIDs(this.removeCandidateDataClassGUIDs(elementProperties));
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.FINGERPRINT_ANNOTATION.typeName))
                    {
                        beanProperties = new FingerprintAnnotationProperties();

                        ((FingerprintAnnotationProperties)beanProperties).setFingerprint(this.removeFingerprint(elementProperties));
                        ((FingerprintAnnotationProperties)beanProperties).setFingerprintAlgorithm(this.removeFingerprintAlgorithm(elementProperties));
                        ((FingerprintAnnotationProperties)beanProperties).setHash(this.removeHash(elementProperties));
                        ((FingerprintAnnotationProperties)beanProperties).setHashAlgorithm(this.removeHashAlgorithm(elementProperties));
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName))
                    {
                        beanProperties = new ResourceProfileAnnotationProperties();

                        ((ResourceProfileAnnotationProperties)beanProperties).setProfilePropertyNames(this.removeProfilePropertyNames(elementProperties));
                        ((ResourceProfileAnnotationProperties)beanProperties).setLength(this.removeLength(elementProperties));
                        ((ResourceProfileAnnotationProperties)beanProperties).setInferredDataType(this.removeInferredDataType(elementProperties));
                        ((ResourceProfileAnnotationProperties)beanProperties).setInferredFormat(this.removeInferredFormat(elementProperties));
                        ((ResourceProfileAnnotationProperties)beanProperties).setInferredLength(this.removeInferredLength(elementProperties));
                        ((ResourceProfileAnnotationProperties)beanProperties).setInferredPrecision(this.removeInferredPrecision(elementProperties));
                        ((ResourceProfileAnnotationProperties)beanProperties).setInferredScale(this.removeInferredScale(elementProperties));
                        ((ResourceProfileAnnotationProperties)beanProperties).setProfileStartDate(this.removeProfileStartDate(elementProperties));
                        ((ResourceProfileAnnotationProperties)beanProperties).setProfileEndDate(this.removeProfileEndDate(elementProperties));
                        ((ResourceProfileAnnotationProperties)beanProperties).setProfileProperties(this.removeProfileProperties(elementProperties));
                        ((ResourceProfileAnnotationProperties)beanProperties).setProfileFlags(this.removeProfileFlags(elementProperties));
                        ((ResourceProfileAnnotationProperties)beanProperties).setProfileDates(this.removeProfileDates(elementProperties));
                        ((ResourceProfileAnnotationProperties)beanProperties).setProfileCounts(this.removeProfileCounts(elementProperties));
                        ((ResourceProfileAnnotationProperties)beanProperties).setProfileDoubles(this.removeProfileDoubles(elementProperties));
                        ((ResourceProfileAnnotationProperties)beanProperties).setValueList(this.removeValueList(elementProperties));
                        ((ResourceProfileAnnotationProperties)beanProperties).setValueCount(this.removeValueCount(elementProperties));
                        ((ResourceProfileAnnotationProperties)beanProperties).setValueRangeFrom(this.removeValueRangeFrom(elementProperties));
                        ((ResourceProfileAnnotationProperties)beanProperties).setValueRangeTo(this.removeValueRangeTo(elementProperties));
                        ((ResourceProfileAnnotationProperties)beanProperties).setAverageValue(this.removeAverageValue(elementProperties));

                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.RESOURCE_PROFILE_LOG_ANNOTATION.typeName))
                    {
                        beanProperties = new ResourceProfileLogAnnotationProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.QUALITY_ANNOTATION.typeName))
                    {
                        beanProperties = new QualityAnnotationProperties();

                        ((QualityAnnotationProperties)beanProperties).setQualityDimension(this.removeQualityDimension(elementProperties));
                        ((QualityAnnotationProperties)beanProperties).setQualityScore(this.removeQualityScore(elementProperties));
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.RELATIONSHIP_ADVICE_ANNOTATION.typeName))
                    {
                        beanProperties = new RelationshipAdviceAnnotationProperties();

                        ((RelationshipAdviceAnnotationProperties)beanProperties).setRelatedEntityGUID(this.removeRelatedEntityGUID(elementProperties));
                        ((RelationshipAdviceAnnotationProperties)beanProperties).setRelationshipTypeName(this.removeRelationshipTypeName(elementProperties));
                        ((RelationshipAdviceAnnotationProperties)beanProperties).setRelationshipProperties(this.removeRelationshipProperties(elementProperties));
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION.typeName))
                    {
                        beanProperties = new RequestForActionProperties();

                        ((RequestForActionProperties)beanProperties).setActionSourceName(this.removeActionSourceName(elementProperties));
                        ((RequestForActionProperties)beanProperties).setActionRequested(this.removeActionRequested(elementProperties));
                        ((RequestForActionProperties)beanProperties).setActionProperties(this.removeActionProperties(elementProperties));
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SEMANTIC_ANNOTATION.typeName))
                    {
                        beanProperties = new SemanticAnnotationProperties();

                        ((SemanticAnnotationProperties)beanProperties).setInformalTerm(this.removeInformalTerm(elementProperties));
                        ((SemanticAnnotationProperties)beanProperties).setSubjectAreaName(this.removeSubjectAreaName(elementProperties));
                        ((SemanticAnnotationProperties)beanProperties).setCandidateGlossaryTermGUIDs(this.removeCandidateGlossaryTermGUIDs(elementProperties));
                        ((SemanticAnnotationProperties)beanProperties).setCandidateGlossaryFolderGUIDs(this.removeCandidateGlossaryFolderGUIDs(elementProperties));
                    }
                    else
                    {
                        beanProperties = new DataFieldAnnotationProperties();
                    }
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName))
                {
                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.RESOURCE_PHYSICAL_STATUS_ANNOTATION.typeName))
                    {
                        beanProperties = new ResourcePhysicalStatusAnnotationProperties();

                        ((ResourcePhysicalStatusAnnotationProperties)beanProperties).setResourceCreateTime(this.removeResourceCreateTime(elementProperties));
                        ((ResourcePhysicalStatusAnnotationProperties)beanProperties).setResourceUpdateTime(this.removeResourceUpdateTime(elementProperties));
                        ((ResourcePhysicalStatusAnnotationProperties)beanProperties).setResourceLastAccessedTime(this.removeResourceLastAccessedTime(elementProperties));
                        ((ResourcePhysicalStatusAnnotationProperties)beanProperties).setSize(this.removeSize(elementProperties));
                        ((ResourcePhysicalStatusAnnotationProperties)beanProperties).setEncodingType(this.removeEncoding(elementProperties));
                    }
                    else
                    {
                        beanProperties = new ResourceMeasureAnnotationProperties();
                    }
                    ((ResourceMeasureAnnotationProperties)beanProperties).setResourceProperties(this.removeResourceProperties(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SCHEMA_ANALYSIS_ANNOTATION.typeName))
                {
                    beanProperties = new SchemaAnalysisAnnotationProperties();

                    ((SchemaAnalysisAnnotationProperties)beanProperties).setSchemaName(this.removeSchemaName(elementProperties));
                    ((SchemaAnalysisAnnotationProperties)beanProperties).setSchemaType(this.removeSchemaType(elementProperties));
                }
                else
                {
                    beanProperties = new AnnotationProperties();
                }

                ((AnnotationProperties)beanProperties).setAnnotationType(this.removeAnnotationType(elementProperties));
                ((AnnotationProperties)beanProperties).setSummary(this.removeSummary(elementProperties));
                ((AnnotationProperties)beanProperties).setConfidenceLevel(this.removeConfidenceLevel(elementProperties));
                ((AnnotationProperties)beanProperties).setExpression(this.removeExpression(elementProperties));
                ((AnnotationProperties)beanProperties).setExplanation(this.removeExplanation(elementProperties));
                ((AnnotationProperties)beanProperties).setAnalysisStep(this.removeAnalysisStep(elementProperties));
                ((AnnotationProperties)beanProperties).setJsonProperties(this.removeJsonProperties(elementProperties));
                ((AnnotationProperties)beanProperties).setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
            }
            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.INFORMAL_TAG.typeName))
            {
                beanProperties = new InformalTagProperties();

                ((InformalTagProperties)beanProperties).setDisplayName(this.removeDisplayName(elementProperties));
                ((InformalTagProperties)beanProperties).setDescription(this.removeDescription(elementProperties));
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
            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.REFERENCEABLE.typeName))
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
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ORGANIZATION.typeName))
                            {
                                beanProperties = new OrganizationProperties();
                            }
                            else
                            {
                                beanProperties = new TeamProperties();
                            }

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
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.COMMUNITY_MEMBER.typeName))
                            {
                                beanProperties = new CommunityMemberProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.CROWD_SOURCING_CONTRIBUTOR.typeName))
                            {
                                beanProperties = new CrowdSourcingContributorProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DIGITAL_PRODUCT_MANAGER.typeName))
                            {
                                beanProperties = new DigitalProductManagerProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_ROLE.typeName))
                            {
                                beanProperties = new GovernanceRoleProperties();

                                ((GovernanceRoleProperties) beanProperties).setDomainIdentifier(this.removeDomainIdentifier(elementProperties));
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.PROJECT_MANAGER.typeName))
                            {
                                beanProperties = new ProjectManagerProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TEAM_LEADER.typeName))
                            {
                                beanProperties = new TeamLeaderProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TEAM_MEMBER.typeName))
                            {
                                beanProperties = new TeamMemberProperties();
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
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_FILE_COLLECTION.typeName))
                            {
                                beanProperties = new DataFileCollectionProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName))
                            {
                                beanProperties = new DeployedDatabaseSchemaProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.REFERENCE_CODE_MAPPING_TABLE.typeName))
                            {
                                beanProperties = new ReferenceCodeMappingTableProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.REFERENCE_CODE_TABLE.typeName))
                            {
                                beanProperties = new ReferenceCodeTableProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.REPORT.typeName))
                            {
                                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.CONNECTOR_ACTIVITY_REPORT.typeName))
                                {
                                    beanProperties = new ConnectorActivityReportProperties();

                                    ((ConnectorActivityReportProperties)beanProperties).setServerName(removeServerName(elementProperties));
                                    ((ConnectorActivityReportProperties)beanProperties).setConnectorId(removeConnectorId(elementProperties));
                                    ((ConnectorActivityReportProperties)beanProperties).setConnectorName(removeConnectorName(elementProperties));
                                    ((ConnectorActivityReportProperties)beanProperties).setCreatedElements(removeCreatedElements(elementProperties));
                                    ((ConnectorActivityReportProperties)beanProperties).setUpdatedElements(removeUpdatedElements(elementProperties));
                                    ((ConnectorActivityReportProperties)beanProperties).setDeletedElements(removeDeletedElements(elementProperties));
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.INCIDENT_REPORT.typeName))
                                {
                                    beanProperties = new IncidentReportProperties();

                                    ((IncidentReportProperties)beanProperties).setDomainIdentifier(removeDomainIdentifier(elementProperties));
                                    ((IncidentReportProperties)beanProperties).setBackground(removeBackground(elementProperties));
                                    ((IncidentReportProperties)beanProperties).setIncidentStatus(removeIncidentReportStatus(elementProperties));
                                    ((IncidentReportProperties)beanProperties).setIncidentClassifiers(removeIncidentClassifiers(elementProperties));
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SURVEY_REPORT.typeName))
                                {
                                    beanProperties = new SurveyReportProperties();

                                    ((SurveyReportProperties)beanProperties).setAnalysisParameters(removeAnalysisParameters(elementProperties));
                                    ((SurveyReportProperties)beanProperties).setAnalysisStep(removeAnalysisStep(elementProperties));
                                }
                                else
                                {
                                    beanProperties = new ReportProperties();
                                }

                                ((ReportProperties)beanProperties).setPurpose(removePurpose(elementProperties));
                                ((ReportProperties)beanProperties).setAuthor(removeAuthor(elementProperties));
                                ((ReportProperties)beanProperties).setStartTime(removeStartTime(elementProperties));
                                ((ReportProperties)beanProperties).setCompletionTime(removeCompletionTime(elementProperties));
                                ((ReportProperties)beanProperties).setCompletionMessage(removeCompletionMessage(elementProperties));
                                ((ReportProperties)beanProperties).setCreatedTime(removeCreatedTime(elementProperties));
                                ((ReportProperties)beanProperties).setLastModifiedTime(removeLastModifiedTime(elementProperties));
                                ((ReportProperties)beanProperties).setLastModifier(removeLastModifier(elementProperties));
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
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.MEDIA_COLLECTION.typeName))
                            {
                                beanProperties = new MediaCollectionProperties();

                                ((MediaCollectionProperties)beanProperties).setEmbeddedMetadata(removeEmbeddedMetadata(elementProperties));
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.METADATA_COLLECTION.typeName))
                            {
                                beanProperties = new MetadataCollectionProperties();

                                ((MetadataCollectionProperties)beanProperties).setManagedMetadataCollectionId(removeManagedMetadataCollectionId(elementProperties));
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SECRETS_COLLECTION.typeName))
                            {
                                beanProperties = new SecretsCollectionProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TABLE_DATA_SET.typeName))
                            {
                                beanProperties = new TableDataSetProperties();
                            }
                            else
                            {
                                beanProperties = new DataSetProperties();
                            }

                            ((DataSetProperties)beanProperties).setFormula(removeFormula(elementProperties));
                            ((DataSetProperties)beanProperties).setFormulaType(removeFormulaType(elementProperties));
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_FEED.typeName))
                        {
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TOPIC.typeName))
                            {
                                beanProperties = new TopicProperties();

                                ((TopicProperties)beanProperties).setTopicName(removeTopicName(elementProperties));
                                ((TopicProperties)beanProperties).setTopicType(removeTopicType(elementProperties));
                            }
                            else
                            {
                                beanProperties = new DataFeedProperties();
                            }
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_STORE.typeName))
                        {
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.COHORT_REGISTRY_STORE.typeName))
                            {
                                beanProperties = new CohortRegistryStoreProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATABASE.typeName))
                            {
                                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.RELATIONAL_DATABASE.typeName))
                                {
                                    beanProperties = new RelationalDatabaseProperties();
                                }
                                else
                                {
                                    beanProperties = new DatabaseProperties();
                                }

                                ((DatabaseProperties)beanProperties).setDatabaseInstance(removeDatabaseInstance(elementProperties));
                                ((DatabaseProperties)beanProperties).setImportedFrom(removeImportedFrom(elementProperties));
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_FILE.typeName))
                            {
                                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ARCHIVE_FILE.typeName))
                                {
                                    beanProperties = new ArchiveFileProperties();
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.AVRO_FILE.typeName))
                                {
                                    beanProperties = new AvroFileProperties();
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.BUILD_INSTRUCTION_FILE.typeName))
                                {
                                    beanProperties = new BuildInstructionFileProperties();
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.CSV_FILE.typeName))
                                {
                                    beanProperties = new CSVFileProperties();

                                    ((CSVFileProperties)beanProperties).setDelimiterCharacter(removeDelimiterCharacter(elementProperties));
                                    ((CSVFileProperties)beanProperties).setQuoteCharacter(removeQuoteCharacter(elementProperties));
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.EXECUTABLE_FILE.typeName))
                                {
                                    beanProperties = new ExecutableFileProperties();
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.JSON_FILE.typeName))
                                {
                                    beanProperties = new JSONFileProperties();
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.KEY_STORE_FILE.typeName))
                                {
                                    beanProperties = new KeyStoreFileProperties();
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.LOG_FILE.typeName))
                                {
                                    beanProperties = new LogFileProperties();
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.MEDIA_FILE.typeName))
                                {
                                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.AUDIO_FILE.typeName))
                                    {
                                        beanProperties = new AudioFileProperties();
                                    }
                                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DOCUMENT.typeName))
                                    {
                                        beanProperties = new DocumentProperties();
                                    }
                                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.RASTER_FILE.typeName))
                                    {
                                        beanProperties = new RasterFileProperties();
                                    }
                                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.THREE_D_IMAGE_FILE.typeName))
                                    {
                                        beanProperties = new ThreeDImageFileProperties();
                                    }
                                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.VECTOR_FILE.typeName))
                                    {
                                        beanProperties = new VectorFileProperties();
                                    }
                                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.VIDEO_FILE.typeName))
                                    {
                                        beanProperties = new VideoFileProperties();
                                    }
                                    else
                                    {
                                        beanProperties = new MediaFileProperties();
                                    }

                                    ((MediaFileProperties)beanProperties).setEmbeddedMetadata(removeEmbeddedMetadata(elementProperties));
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.PARQUET_FILE.typeName))
                                {
                                    beanProperties = new ParquetFileProperties();
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.PROPERTIES_FILE.typeName))
                                {
                                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.YAML_FILE.typeName))
                                    {
                                        beanProperties = new YAMLFileProperties();
                                    }
                                    else
                                    {
                                        beanProperties = new PropertiesFileProperties();
                                    }
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SCRIPT_FILE.typeName))
                                {
                                    beanProperties = new ScriptFileProperties();
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SPREADSHEET_FILE.typeName))
                                {
                                    beanProperties = new SpreadsheetFileProperties();
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SOURCE_CODE_FILE.typeName))
                                {
                                    beanProperties = new SourceCodeFileProperties();
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.XML_FILE.typeName))
                                {
                                    beanProperties = new XMLFileProperties();
                                }
                                else
                                {
                                    beanProperties = new DataFileProperties();
                                }
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DOCUMENT_STORE.typeName))
                            {
                                beanProperties = new DocumentStoreProperties();

                                ((DocumentStoreProperties)beanProperties).setEmbeddedMetadata(removeEmbeddedMetadata(elementProperties));
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.FILE_FOLDER.typeName))
                            {
                                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_FOLDER.typeName))
                                {
                                    beanProperties = new DataFolderProperties();
                                }
                                else
                                {
                                    beanProperties = new FileFolderProperties();
                                }
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GRAPH_STORE.typeName))
                            {
                                beanProperties = new GraphStoreProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.METADATA_REPOSITORY.typeName))
                            {
                                beanProperties = new MetadataRepositoryProperties();
                            }
                            else
                            {
                                beanProperties = new DataStoreProperties();
                            }

                            ((DataStoreProperties)beanProperties).setStoreCreateTime(removeStoreCreateTime(elementProperties));
                            ((DataStoreProperties)beanProperties).setStoreUpdateTime(removeStoreUpdateTime(elementProperties));
                            ((DataStoreProperties)beanProperties).setPathName(removePathName(elementProperties));
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.REPORT_TYPE.typeName))
                        {
                            beanProperties = new ReportTypeProperties();

                            ((ReportTypeProperties)beanProperties).setPurpose(removePurpose(elementProperties));
                            ((ReportTypeProperties)beanProperties).setAuthor(removeAuthor(elementProperties));
                            ((ReportTypeProperties)beanProperties).setCreatedTime(removeCreatedTime(elementProperties));
                            ((ReportTypeProperties)beanProperties).setLastModifiedTime(removeLastModifiedTime(elementProperties));
                            ((ReportTypeProperties)beanProperties).setLastModifier(removeLastModifier(elementProperties));
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
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.EMBEDDED_PROCESS.typeName))
                            {
                                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TRANSIENT_EMBEDDED_PROCESS.typeName))
                                {
                                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ANALYTICS_MODEL_RUN.typeName))
                                    {
                                        beanProperties = new AnalyticsModelRunProperties();
                                    }
                                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeName))
                                    {
                                        beanProperties = new GovernanceActionProcessInstanceProperties();
                                    }
                                    else
                                    {
                                        beanProperties = new TransientEmbeddedProcessProperties();
                                    }
                                }
                                else
                                {
                                    beanProperties = new EmbeddedProcessProperties();
                                }
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TO_DO.typeName))
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
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.NOTIFICATION.typeName))
                            {
                                beanProperties = new NotificationProperties();

                                ((NotificationProperties) beanProperties).setSystemAction(this.removeSystemAction(elementProperties));
                                ((NotificationProperties) beanProperties).setUserResponse(this.removeUserResponse(elementProperties));
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
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.REVIEW.typeName))
                            {
                                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ANNOTATION_REVIEW.typeName))
                                {
                                    beanProperties = new AnnotationReviewProperties();

                                    ((AnnotationReviewProperties)beanProperties).setAnnotationStatus(this.removeAnnotationStatus(elementProperties));
                                }
                                else
                                {
                                    beanProperties = new ReviewProperties();
                                }

                                ((ReviewProperties)beanProperties).setReviewDate(this.removeReviewDate(elementProperties));
                                ((ReviewProperties)beanProperties).setComment(this.removeComment(elementProperties));
                            }
                            else
                            {
                                beanProperties = new ActionProperties();
                            }
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName))
                        {
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DEPLOYED_API.typeName))
                            {
                                beanProperties = new DeployedAPIProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DEPLOYED_ANALYTICS_MODEL.typeName))
                            {
                                beanProperties = new DeployedAnalyticsModelProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DEPLOYED_CONNECTOR.typeName))
                            {
                                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_SERVICE.typeName))
                                {
                                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_ACTION_SERVICE.typeName))
                                    {
                                        beanProperties = new GovernanceActionServiceProperties();
                                    }
                                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.REPOSITORY_GOVERNANCE_SERVICE.typeName))
                                    {
                                        beanProperties = new RepositoryGovernanceServiceProperties();
                                    }
                                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SURVEY_ACTION_SERVICE.typeName))
                                    {
                                        beanProperties = new SurveyActionServiceProperties();
                                    }
                                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.WATCHDOG_ACTION_SERVICE.typeName))
                                    {
                                        beanProperties = new WatchdogActionServiceProperties();
                                    }
                                    else
                                    {
                                        beanProperties = new GovernanceServiceProperties();
                                    }
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.INTEGRATION_CONNECTOR.typeName))
                                {
                                    beanProperties = new IntegrationConnectorProperties();

                                    ((IntegrationConnectorProperties) beanProperties).setUsesBlockingCalls(this.removeUsesBlockingCalls(elementProperties));
                                }
                                else
                                {
                                    beanProperties = new DeployedConnectorProperties();
                                }
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
                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.AGREEMENT.typeName))
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
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.BUSINESS_CAPABILITY.typeName))
                    {
                        beanProperties = new BusinessCapabilityProperties();

                        ((BusinessCapabilityProperties)beanProperties).setBusinessCapabilityType(this.removeBusinessCapabilityType(elementProperties));
                        ((BusinessCapabilityProperties)beanProperties).setBusinessImplementationType(this.removeBusinessImplementationType(elementProperties));
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_SPEC_COLLECTION.typeName))
                    {
                        beanProperties = new DataSpecProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_DICTIONARY_COLLECTION.typeName))
                    {
                        beanProperties = new DataDictionaryProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DESIGN_MODEL.typeName))
                    {
                        if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.CONCEPT_MODEL.typeName))
                        {
                            beanProperties = new ConceptModelProperties();
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SOLUTION_BLUEPRINT.typeName))
                        {
                            beanProperties = new SolutionBlueprintProperties();
                        }
                        else
                        {
                            beanProperties = new DesignModelProperties();
                        }

                        ((DesignModelProperties)beanProperties).setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                        ((DesignModelProperties)beanProperties).setAuthors(this.removeAuthors(elementProperties));
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DESIGN_MODEL_FOLDER.typeName))
                    {
                        beanProperties = new DesignModelFolderProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DIGITAL_PRODUCT.typeName))
                    {
                        if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName))
                        {
                            beanProperties = new DigitalProductFamilyProperties();
                        }
                        else
                        {
                            beanProperties = new DigitalProductProperties();
                        }

                        ((DigitalProductProperties)beanProperties).setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                        ((DigitalProductProperties)beanProperties).setProductName(this.removeProductName(elementProperties));
                        ((DigitalProductProperties)beanProperties).setIntroductionDate(this.removeIntroductionDate(elementProperties));
                        ((DigitalProductProperties)beanProperties).setMaturity(this.removeMaturity(elementProperties));
                        ((DigitalProductProperties)beanProperties).setServiceLife(this.removeServiceLife(elementProperties));
                        ((DigitalProductProperties)beanProperties).setNextVersionDate(this.removeNextVersionDate(elementProperties));
                        ((DigitalProductProperties)beanProperties).setWithdrawDate(this.removeWithdrawDate(elementProperties));
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DIGITAL_PRODUCT_CATALOG.typeName))
                    {
                        beanProperties = new DigitalProductCatalogProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.EVENT_SET_COLLECTION.typeName))
                    {
                        beanProperties = new EventSetProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GLOSSARY.typeName))
                    {
                        beanProperties = new GlossaryProperties();

                        ((GlossaryProperties)beanProperties).setLanguage(this.removeLanguage(elementProperties));
                        ((GlossaryProperties)beanProperties).setUsage(this.removeUsage(elementProperties));
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName))
                    {
                        beanProperties = new InformationSupplyChainProperties();

                        ((InformationSupplyChainProperties)beanProperties).setScope(this.removeScope(elementProperties));
                        ((InformationSupplyChainProperties)beanProperties).setPurposes(this.removePurposes(elementProperties));
                        ((InformationSupplyChainProperties)beanProperties).setIntegrationStyle(this.removeIntegrationStyle(elementProperties));
                        ((InformationSupplyChainProperties)beanProperties).setEstimatedVolumetrics(this.removeEstimatedVolumetrics(elementProperties));
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.NAMING_STANDARD_RULE_SET_COLLECTION.typeName))
                    {
                        beanProperties = new NamingStandardRuleSetProperties();
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
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.COMMUNITY.typeName))
                {
                    beanProperties = new CommunityProperties();

                    ((CommunityProperties)beanProperties).setMission(this.removeMission(elementProperties));
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
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.CONTEXT_EVENT.typeName))
                {
                    beanProperties = new ContextEventProperties();

                    ((ContextEventProperties)beanProperties).setEventEffect(this.removeEventEffect(elementProperties));
                    ((ContextEventProperties)beanProperties).setPlannedStartDate(this.removePlannedStartDate(elementProperties));
                    ((ContextEventProperties)beanProperties).setActualStartDate(this.removeActualStartDate(elementProperties));
                    ((ContextEventProperties)beanProperties).setPlannedDuration(this.removePlannedDuration(elementProperties));
                    ((ContextEventProperties)beanProperties).setActualDuration(this.removeActualDuration(elementProperties));
                    ((ContextEventProperties)beanProperties).setRepeatInterval(this.removeRepeatInterval(elementProperties));
                    ((ContextEventProperties)beanProperties).setPlannedCompletionDate(this.removePlannedCompletionDate(elementProperties));
                    ((ContextEventProperties)beanProperties).setActualCompletionDate(this.removeActualCompletionDate(elementProperties));
                    ((ContextEventProperties)beanProperties).setReferenceEffectiveFrom(this.removeReferenceEffectiveFrom(elementProperties));
                    ((ContextEventProperties)beanProperties).setReferenceEffectiveTo(this.removeReferenceEffectiveTo(elementProperties));
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
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_PROCESSING_ACTION.typeName))
                {
                    beanProperties = new DataProcessingActionProperties();
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_PROCESSING_DESCRIPTION.typeName))
                {
                    beanProperties = new DataProcessingDescriptionProperties();
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_STRUCTURE.typeName))
                {
                    beanProperties = new DataStructureProperties();

                    ((DataStructureProperties)beanProperties).setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                    ((DataStructureProperties)beanProperties).setNamespace(this.removeNamespace(elementProperties));
                    ((DataStructureProperties)beanProperties).setNamePatterns(this.removeNamePatterns(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_FIELD.typeName))
                {
                    beanProperties = new DataFieldProperties();

                    ((DataFieldProperties)beanProperties).setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                    ((DataFieldProperties)beanProperties).setNamespace(this.removeNamespace(elementProperties));
                    ((DataFieldProperties)beanProperties).setAliases(this.removeAliases(elementProperties));
                    ((DataFieldProperties)beanProperties).setNamePatterns(this.removeNamePatterns(elementProperties));
                    ((DataFieldProperties)beanProperties).setDefaultValue(this.removeDefaultValue(elementProperties));
                    ((DataFieldProperties)beanProperties).setIsNullable(this.removeIsNullable(elementProperties));
                    ((DataFieldProperties)beanProperties).setDataType(this.removeDataType(elementProperties));
                    ((DataFieldProperties)beanProperties).setUnits(this.removeUnits(elementProperties));
                    ((DataFieldProperties)beanProperties).setMinimumLength(this.removeMinimumLength(elementProperties));
                    ((DataFieldProperties)beanProperties).setLength(this.removeLength(elementProperties));
                    ((DataFieldProperties)beanProperties).setPrecision(this.removePrecision(elementProperties));
                    ((DataFieldProperties)beanProperties).setOrderedValues(this.removeOrderedValues(elementProperties));
                    ((DataFieldProperties)beanProperties).setSortOrder(this.removeDataItemSortOrder(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ENDPOINT.typeName))
                {
                    beanProperties = new EndpointProperties();

                    ((EndpointProperties)beanProperties).setNetworkAddress(this.removeNetworkAddress(elementProperties));
                    ((EndpointProperties)beanProperties).setProtocol(this.removeProtocol(elementProperties));
                    ((EndpointProperties)beanProperties).setEncryptionMethod(this.removeEncryptionMethod(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.EXTERNAL_ID.typeName))
                {
                    beanProperties = new ExternalIdProperties();

                    ((ExternalIdProperties)beanProperties).setKey(this.removeKey(elementProperties));
                    ((ExternalIdProperties)beanProperties).setKeyPattern(this.removeKeyPattern(elementProperties));
                    ((ExternalIdProperties)beanProperties).setExternalInstanceTypeName(this.removeExternalInstanceTypeName(elementProperties));
                    ((ExternalIdProperties)beanProperties).setExternalInstanceCreatedBy(this.removeExternalInstanceCreatedBy(elementProperties));
                    ((ExternalIdProperties)beanProperties).setExternalInstanceCreationTime(this.removeExternalInstanceCreationTime(elementProperties));
                    ((ExternalIdProperties)beanProperties).setExternalInstanceLastUpdatedBy(this.removeExternalInstanceLastUpdatedBy(elementProperties));
                    ((ExternalIdProperties)beanProperties).setExternalInstanceLastUpdateTime(this.removeExternalInstanceLastUpdateTime(elementProperties));
                    ((ExternalIdProperties)beanProperties).setExternalInstanceVersion(this.removeExternalInstanceVersion(elementProperties));

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
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GLOSSARY_TERM.typeName))
                {
                    beanProperties = new GlossaryTermProperties();

                    ((GlossaryTermProperties)beanProperties).setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                    ((GlossaryTermProperties)beanProperties).setAliases(this.removeAliases(elementProperties));
                    ((GlossaryTermProperties)beanProperties).setSummary(this.removeSummary(elementProperties));
                    ((GlossaryTermProperties)beanProperties).setExamples(this.removeExamples(elementProperties));
                    ((GlossaryTermProperties)beanProperties).setAbbreviation(this.removeAbbreviation(elementProperties));
                    ((GlossaryTermProperties)beanProperties).setUsage(this.removeUsage(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_DEFINITION.typeName))
                {
                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_CONTROL.typeName))
                    {
                        if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SECURITY_GROUP.typeName))
                        {
                            beanProperties = new SecurityGroupProperties();

                            ((SecurityGroupProperties) beanProperties).setDistinguishedName(this.removeDistinguishedName(elementProperties));
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_METRIC.typeName))
                        {
                            beanProperties = new GovernanceMetricProperties();

                            ((GovernanceMetricProperties) beanProperties).setTarget(this.removeTarget(elementProperties));
                            ((GovernanceMetricProperties) beanProperties).setMeasurement(this.removeMeasurement(elementProperties));
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SECURITY_ACCESS_CONTROL.typeName))
                        {
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_ZONE.typeName))
                            {
                                beanProperties = new GovernanceZoneProperties();

                                ((GovernanceZoneProperties) beanProperties).setCriteria(this.removeCriteria(elementProperties));
                            }
                            else
                            {
                                beanProperties = new SecurityAccessControlProperties();
                            }
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.NOTIFICATION_TYPE.typeName))
                        {
                            beanProperties = new NotificationTypeProperties();

                            ((NotificationTypeProperties) beanProperties).setStartDate(this.removeStartDate(elementProperties));
                            ((NotificationTypeProperties) beanProperties).setRefreshTimeInterval(this.removeRefreshTimeInterval(elementProperties));
                            ((NotificationTypeProperties) beanProperties).setConnectorShutdownDate(this.removeConnectorShutdownDate(elementProperties));

                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_ACTION.typeName))
                        {
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName))
                            {
                                beanProperties = new GovernanceActionProcessProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName))
                            {
                                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName))
                                {
                                    beanProperties = new GovernanceActionProcessStepProperties();

                                    ((GovernanceActionProcessStepProperties) beanProperties).setIgnoreMultipleTriggers(this.removeIgnoreMultipleTriggers(elementProperties));
                                }
                                else
                                {
                                    beanProperties = new GovernanceActionTypeProperties();
                                }

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
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TERMS_AND_CONDITIONS.typeName))
                        {
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.CERTIFICATION_TYPE.typeName))
                            {
                                beanProperties = new CertificationTypeProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.LICENSE_TYPE.typeName))
                            {
                                beanProperties = new LicenseTypeProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SERVICE_LEVEL_OBJECTIVE.typeName))
                            {
                                beanProperties = new ServiceLevelObjectiveProperties();
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
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.LOCATION.typeName))
                {
                    beanProperties = new LocationProperties();
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.METADATA_REPOSITORY_COHORT.typeName))
                {
                    beanProperties = new MetadataRepositoryCohortProperties();

                    ((MetadataRepositoryCohortProperties)beanProperties).setCohortTopics(this.removeCohortTopics(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.PORT.typeName))
                {
                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.PORT_ALIAS.typeName))
                    {
                        beanProperties = new PortAliasProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.PORT_IMPLEMENTATION.typeName))
                    {
                        beanProperties = new PortImplementationProperties();
                    }
                    else
                    {
                        beanProperties = new PortProperties();
                    }

                    ((PortProperties)beanProperties).setPortType(this.removePortType(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.PROJECT.typeName))
                {
                    beanProperties = new ProjectProperties();

                    ((ProjectProperties)beanProperties).setPriority(this.removeIntPriority(elementProperties));
                    ((ProjectProperties)beanProperties).setPlannedStartDate(this.removePlannedStartDate(elementProperties));
                    ((ProjectProperties)beanProperties).setActualStartDate(this.removeActualStartDate(elementProperties));
                    ((ProjectProperties)beanProperties).setPlannedCompletionDate(this.removePlannedCompletionDate(elementProperties));
                    ((ProjectProperties)beanProperties).setActualCompletionDate(this.removeActualCompletionDate(elementProperties));
                    ((ProjectProperties)beanProperties).setProjectHealth(this.removeProjectHealth(elementProperties));
                    ((ProjectProperties)beanProperties).setProjectStatus(this.removeProjectStatus(elementProperties));
                    ((ProjectProperties)beanProperties).setProjectPhase(this.removeProjectPhase(elementProperties));
                    ((ProjectProperties)beanProperties).setMission(this.removeMission(elementProperties));
                    ((ProjectProperties)beanProperties).setPurposes(this.removePurposes(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.PROPERTY_FACET.typeName))
                {
                    beanProperties = new PropertyFacetProperties();

                    ((PropertyFacetProperties)beanProperties).setProperties(this.removeProperties(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SCHEMA_ELEMENT.typeName))
                {
                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SCHEMA_TYPE.typeName))
                    {
                        if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.API_OPERATION.typeName))
                        {
                            beanProperties = new APIOperationProperties();

                            ((APIOperationProperties) beanProperties).setPath(this.removePath(elementProperties));
                            ((APIOperationProperties) beanProperties).setCommand(this.removeCommand(elementProperties));
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SIMPLE_SCHEMA_TYPE.typeName))
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
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.EVENT_TYPE_LIST.typeName))
                            {
                                beanProperties = new EventTypeListProperties();
                            }
                            else
                            {
                                beanProperties = new SchemaTypeChoiceProperties();
                            }
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
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.API_PARAMETER_LIST.typeName))
                            {
                                beanProperties = new APIParameterListProperties();

                                ((APIParameterListProperties)beanProperties).setRequired(this.removeRequired(elementProperties));
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.STRUCT_SCHEMA_TYPE.typeName))
                            {
                                beanProperties = new StructSchemaTypeProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ROOT_SCHEMA_TYPE.typeName))
                            {
                                if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.API_SCHEMA_TYPE.typeName))
                                {
                                    beanProperties = new APISchemaTypeProperties();
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.EVENT_TYPE.typeName))
                                {
                                    beanProperties = new EventTypeProperties();
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TABULAR_SCHEMA_TYPE.typeName))
                                {
                                    beanProperties = new TabularSchemaTypeProperties();
                                }
                                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.RELATIONAL_DB_SCHEMA_TYPE.typeName))
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
                        if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.API_PARAMETER.typeName))
                        {
                            beanProperties = new APIParameterProperties();
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.EVENT_SCHEMA_ATTRIBUTE.typeName))
                        {
                            beanProperties = new EventSchemaAttributeProperties();
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TABULAR_COLUMN.typeName))
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
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SOFTWARE_CAPABILITY.typeName))
                {
                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.APPLICATION.typeName))
                    {
                        beanProperties = new ApplicationProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.API_MANAGER.typeName))
                    {
                        beanProperties = new APIManagerProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.AUTHORIZATION_MANAGER.typeName))
                    {
                        beanProperties = new AuthorizationManagerProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.COHORT_MEMBER.typeName))
                    {
                        beanProperties = new CohortMemberProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_MANAGER.typeName))
                    {
                        if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_ACCESS_MANAGER.typeName))
                        {
                            beanProperties = new DataAccessManagerProperties();
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATABASE_MANAGER.typeName))
                        {
                            beanProperties = new DatabaseManagerProperties();
                        }
                        else
                        {
                            beanProperties = new DataManagerProperties();
                        }
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ENGINE.typeName))
                    {
                        if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_ENGINE.typeName))
                        {
                            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName))
                            {
                                beanProperties = new GovernanceActionEngineProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.REPOSITORY_GOVERNANCE_ENGINE.typeName))
                            {
                                beanProperties = new RepositoryGovernanceEngineProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SURVEY_ACTION_ENGINE.typeName))
                            {
                                beanProperties = new SurveyActionEngineProperties();
                            }
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.WATCHDOG_ACTION_ENGINE.typeName))
                            {
                                beanProperties = new WatchdogActionEngineProperties();
                            }
                            else
                            {
                                beanProperties = new GovernanceEngineProperties();
                            }
                        }
                        else
                        {
                            beanProperties = new EngineProperties();
                        }
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.EVENT_BROKER.typeName))
                    {
                        beanProperties = new EventBrokerProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.EVENT_MANAGER.typeName))
                    {
                        beanProperties = new EventManagerProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.INVENTORY_CATALOG.typeName))
                    {
                        beanProperties = new InventoryCatalogProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.INTEGRATION_GROUP.typeName))
                    {
                        beanProperties = new IntegrationGroupProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.NETWORK_GATEWAY.typeName))
                    {
                        beanProperties = new NetworkGatewayProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SOFTWARE_SERVICE.typeName))
                    {
                        beanProperties = new SoftwareServiceProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.USER_AUTHENTICATION_MANAGER.typeName))
                    {
                        beanProperties = new UserAuthenticationManagerProperties();
                    }
                    else
                    {
                        beanProperties = new SoftwareCapabilityProperties();
                    }

                    ((SoftwareCapabilityProperties)beanProperties).setPatchLevel(this.removePatchLevel(elementProperties));
                    ((SoftwareCapabilityProperties)beanProperties).setSource(this.removeSource(elementProperties));
                    ((SoftwareCapabilityProperties)beanProperties).setProtocolVersion(this.removeDeployedImplementationType(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SOLUTION_COMPONENT.typeName))
                {
                    beanProperties = new SolutionComponentProperties();

                    ((SolutionComponentProperties)beanProperties).setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                    ((SolutionComponentProperties)beanProperties).setSolutionComponentType(this.removeSolutionComponentType(elementProperties));
                    ((SolutionComponentProperties)beanProperties).setPlannedDeployedImplementationType(this.removePlannedDeployedImplementationType(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SOLUTION_PORT.typeName))
                {
                    beanProperties = new SolutionPortProperties();

                    ((SolutionPortProperties)beanProperties).setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                    ((SolutionPortProperties)beanProperties).setDirection(this.removeSolutionPortDirection(elementProperties));
                }
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.VALID_VALUE_DEFINITION.typeName))
                {
                    if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.REFERENCE_DATA_VALUE.typeName))
                    {
                        beanProperties = new ReferenceDataValueProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.VALID_METADATA_VALUE.typeName))
                    {
                        beanProperties = new ValidMetadataValueProperties();
                    }
                    else
                    {
                        beanProperties = new ValidValueDefinitionProperties();
                    }

                    ((ValidValueDefinitionProperties)beanProperties).setNamespace(this.removeNamespace(elementProperties));
                    ((ValidValueDefinitionProperties)beanProperties).setUsage(this.removeUsage(elementProperties));
                    ((ValidValueDefinitionProperties)beanProperties).setScope(this.removeScope(elementProperties));
                    ((ValidValueDefinitionProperties)beanProperties).setPreferredValue(this.removePreferredValue(elementProperties));
                    ((ValidValueDefinitionProperties)beanProperties).setDataType(this.removeDataType(elementProperties));
                    ((ValidValueDefinitionProperties)beanProperties).setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                    ((ValidValueDefinitionProperties)beanProperties).setIsCaseSensitive(this.removeIsCaseSensitive(elementProperties));
                }
                else
                {
                    beanProperties = new ReferenceableProperties();
                }

                /*
                 * These are the standard properties for a Referenceable.
                 */
                ((ReferenceableProperties)beanProperties).setQualifiedName(this.removeQualifiedName(elementProperties));
                ((ReferenceableProperties)beanProperties).setIdentifier(this.removeIdentifier(elementProperties));
                ((ReferenceableProperties)beanProperties).setDisplayName(this.removeDisplayName(elementProperties));
                ((ReferenceableProperties)beanProperties).setDescription(this.removeDescription(elementProperties));
                ((ReferenceableProperties)beanProperties).setVersionIdentifier(this.removeVersionIdentifier(elementProperties));
                ((ReferenceableProperties)beanProperties).setCategory(this.removeCategory(elementProperties));
                ((ReferenceableProperties)beanProperties).setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
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

        return null;
    }


    /**
     * Throw an exception to indicate that a critical instance (typically the main element) has not been passed
     * to the converter.
     *
     * @param beanClassName class name of bean
     * @param elementClassName class name that the converter is able to process
     * @param methodName method that is missing
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    protected void handleMissingMetadataInstance(String          beanClassName,
                                                 String          elementClassName,
                                                 String          methodName) throws PropertyServerException
    {
        throw new PropertyServerException(OMFErrorCode.MISSING_METADATA_INSTANCE.getMessageDefinition(localServiceName,
                                                                                                      beanClassName,
                                                                                                      elementClassName,
                                                                                                      methodName),
                                          this.getClass().getName(),
                                          methodName);
    }


}

