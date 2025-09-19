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
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.DeployedSoftwareComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.DeployedConnectorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.topics.TopicProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CommunityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CrowdSourcingContributionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectorTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.VirtualConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventImpactProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingPurposeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.AdjacentLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.LocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.NestedLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalDBSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularFileColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.AssociatedSecurityGroupProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.GovernanceZoneProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityAccessControlProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.CatalogTemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateSubstituteProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueDefinitionProperties;
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
     * Extract the qualifiedName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String getQualifiedName(ElementProperties  elementProperties)
    {
        final String methodName = "getQualifiedName";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
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
     * Extract and delete the minimumInstances property from the supplied element properties.
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
     * Extract and delete the maximumInstances property from the supplied element properties.
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
     * Extract and delete the associationType property from the supplied element properties.
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
     * Extract and delete the organization property from the supplied element properties.
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
     * Extract and delete the defaultMediaUsageOtherId property from the supplied element properties.
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
     * Extract the referenceId property from the supplied element properties.
     *
     * @param elementProperties properties from relationship
     * @return string text or null
     */
    protected String getReferenceId(ElementProperties  elementProperties)
    {
        final String methodName = "getReferenceId";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.REFERENCE_ID.name,
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
     * Extract and delete the connector name property from the supplied element properties.
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
     * Extract the assetSummary property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getAssetSummary(ElementProperties  elementProperties)
    {
        final String methodName = "removeEncryptedPassword";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.ASSET_SUMMARY.name,
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
    protected Map<String, Object> getArguments(ElementProperties  elementProperties)
    {
        final String methodName = "getArguments";

        if (elementProperties != null)
        {
            return propertyHelper.getMapFromProperty(localServiceName,
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
     * Retrieve the zone membership from the properties of the zone membership classification.
     *
     * @param elementProperties properties from the classification
     * @return list of zone names
     */
    protected List<String> getZoneMembership(ElementProperties elementProperties)
    {
        final String methodName = "getZoneMembership";

        if (elementProperties != null)
        {
            return propertyHelper.getStringArrayProperty(localServiceName,
                                                         OpenMetadataProperty.ZONE_MEMBERSHIP.name,
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
     * Extract the owner property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String getOwner(ElementProperties elementProperties)
    {
        final String methodName = "getOwner";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
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
     * Extract and delete the roleTypeName property from the supplied element properties.
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
    protected List<String> getGroups(ElementProperties  elementProperties)
    {
        final String methodName = "getGroups";

        if (elementProperties != null)
        {
            return propertyHelper.getStringArrayProperty(localServiceName,
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
    protected List<String> getSecurityLabels(ElementProperties  elementProperties)
    {
        final String methodName = "getSecurityLabels";

        if (elementProperties != null)
        {
            return propertyHelper.getStringArrayProperty(localServiceName,
                                                         OpenMetadataProperty.SECURITY_LABELS.name,
                                                         elementProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the securityProperties property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string map or null
     */
    protected Map<String, String> getSecurityProperties(ElementProperties  elementProperties)
    {
        final String methodName = "getSecurityProperties";

        if (elementProperties != null)
        {
            return propertyHelper.getStringMapFromProperty(localServiceName,
                                                           OpenMetadataProperty.SECURITY_PROPERTIES.name,
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
     * Extract the organizationGUID property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String getOriginOrganizationGUID(ElementProperties  elementProperties)
    {
        final String methodName = "getOriginOrganizationGUID";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.ORGANIZATION.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the businessCapabilityGUID property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String getOriginBusinessCapabilityGUID(ElementProperties  elementProperties)
    {
        final String methodName = "getOriginBusinessCapabilityGUID";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.BUSINESS_CAPABILITY.name,
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
    protected Map<String, String> getOtherOriginValues(ElementProperties  elementProperties)
    {
        final String methodName = "getOtherOriginValues";

        if (elementProperties != null)
        {
            return propertyHelper.getStringMapFromProperty(localServiceName,
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
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.PATH_NAME.name,
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
    protected String getDataStoreEncoding(ElementProperties  elementProperties)
    {
        final String methodName = "getDataStoreEncoding";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
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
    protected String getDataStoreEncodingLanguage(ElementProperties  elementProperties)
    {
        final String methodName = "getDataStoreEncodingLanguage";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
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
    protected String getDataStoreEncodingDescription(ElementProperties  elementProperties)
    {
        final String methodName = "getDataStoreEncodingDescription";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
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
    protected Map<String, String> getEncodingProperties(ElementProperties  elementProperties)
    {
        final String methodName = "getEncodingProperties";

        if (elementProperties != null)
        {
            return propertyHelper.getStringMapFromProperty(localServiceName,
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
    protected String removeDatabaseImportedFrom(ElementProperties  elementProperties)
    {
        final String methodName = "removeDatabaseImportedFrom";

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
    protected String getFormat(ElementProperties  elementProperties)
    {
        final String methodName = "getFormat";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
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
    protected String getEncryption(ElementProperties  elementProperties)
    {
        final String methodName = "getEncryption";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.ENCRYPTION.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract and delete the type property from the supplied element properties.
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
     * Extract the anchorGUID property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String getAnchorGUID(ElementProperties  elementProperties)
    {
        final String methodName = "getAnchorGUID";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.ANCHOR_GUID.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
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
     * Extract the query property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getQuery(ElementProperties  elementProperties)
    {
        final String methodName = "setQuery";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.QUERY.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the queryId property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getQueryId(ElementProperties  elementProperties)
    {
        final String methodName = "setQueryId";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.QUERY_ID.name,
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
     * Extract the position property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer - default 0
     */
    protected int getPosition(ElementProperties  elementProperties)
    {
        final String methodName = "getPosition";

        if (elementProperties != null)
        {
            return propertyHelper.getIntProperty(localServiceName,
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
     * Extract the guard property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getGuard(ElementProperties  elementProperties)
    {
        final String methodName = "getGuard";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.GUARD.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the formula property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getFormula(ElementProperties  elementProperties)
    {
        final String methodName = "getFormula";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.FORMULA.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the formulaType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getFormulaType(ElementProperties  elementProperties)
    {
        final String methodName = "getFormulaType";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.FORMULA_TYPE.name,
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
     * Extract and delete the formulaType property from the supplied element properties.
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
     * Extract the implementationLanguage property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getImplementationLanguage(ElementProperties  elementProperties)
    {
        final String methodName = "getImplementationLanguage";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.IMPLEMENTATION_LANGUAGE.name,
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
    protected String getUsage(ElementProperties elementProperties)
    {
        final String methodName = "getUsage";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.USAGE.name,
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
     * Extract the summary property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected String getSummary(ElementProperties elementProperties)
    {
        final String methodName = "getSummary";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.SUMMARY.name,
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
     * Extract the abbreviation property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected String getAbbreviation(ElementProperties elementProperties)
    {
        final String methodName = "getAbbreviation";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.ABBREVIATION.name,
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
    protected String getRationale(ElementProperties elementProperties)
    {
        final String methodName = "removeRationale";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
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
     * Extract and delete the guard property from the supplied element properties.
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
     * Extract and delete the plannedEndDate property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removePlannedEndDate(ElementProperties elementProperties)

    {
        final String methodName = "removePlannedEndDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.PLANNED_END_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the creationTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeCreationTime(ElementProperties elementProperties)

    {
        final String methodName = "removeCreationTime";

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
     * Extract and delete the actionTargetName property from the supplied element properties.
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
     * Extract the "conditions" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getConditions(ElementProperties elementProperties)
    {
        final String methodName = "getConditions";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.CONDITIONS.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the licensee property from the supplied element properties.
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
     * @param elementProperties properties from ValidValueDefinition entity
     * @return boolean
     */
    protected boolean removeIsCaseSensitive(ElementProperties  elementProperties)
    {
        final String methodName = "getStrictRequirement";

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
    protected boolean getStrictRequirement(ElementProperties  elementProperties)
    {
        final String methodName = "getStrictRequirement";

        if (elementProperties != null)
        {
            return propertyHelper.getBooleanProperty(localServiceName,
                                                     OpenMetadataProperty.STRICT_REQUIREMENT.name,
                                                     elementProperties,
                                                     methodName);
        }

        return false;
    }


    /**
     * Extract the confidence property from the supplied element properties.
     *
     * @param elementProperties properties from ReferenceValueAssignment or ValidValuesMapping relationship
     * @return int
     */
    protected int getConfidence(ElementProperties  elementProperties)
    {
        final String methodName = "getConfidence";

        if (elementProperties != null)
        {
            return propertyHelper.getIntProperty(localServiceName,
                                                 OpenMetadataProperty.CONFIDENCE.name,
                                                 elementProperties,
                                                 methodName);
        }

        return 0;
    }


    /**
     * Extract the steward property from the supplied element properties.
     *
     * @param elementProperties properties from ReferenceValueAssignment or ValidValuesMapping relationship
     * @return string text or null
     */
    protected String getSteward(ElementProperties  elementProperties)
    {
        final String methodName = "getSteward";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.STEWARD.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }



    /**
     * Extract the stewardTypeName property from the supplied element properties.
     *
     * @param elementProperties properties from ReferenceValueAssignment or ValidValuesMapping relationship
     * @return string text or null
     */
    protected String getStewardTypeName(ElementProperties  elementProperties)
    {
        final String methodName = "getStewardTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the stewardTypeName property from the supplied element properties.
     *
     * @param elementProperties properties from ReferenceValueAssignment or ValidValuesMapping relationship
     * @return string text or null
     */
    protected String getStewardPropertyName(ElementProperties  elementProperties)
    {
        final String methodName = "getStewardPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the "notes" property from the supplied element properties.
     *
     * @param elementProperties properties from GovernanceRuleImplementation, GovernanceProcessImplementation,
     *                           ReferenceValueAssignment or ValidValuesMapping relationship
     * @return string text or null
     */
    protected String getNotes(ElementProperties  elementProperties)
    {
        final String methodName = "getNotes";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.NOTES.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the "attributeName" property from the supplied element properties.
     *
     * @param elementProperties properties from ReferenceValueAssignment relationship
     * @return string text or null
     */
    protected String getAttributeName(ElementProperties  elementProperties)
    {
        final String methodName = "getAttributeName";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.ATTRIBUTE_NAME.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the pointType property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String getPointType(ElementProperties  elementProperties)
    {
        final String methodName = "getPointType";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
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
    protected String getAssociationDescription(ElementProperties  elementProperties)
    {
        final String methodName = "getAssociationDescription";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
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
    protected String getSymbolicName(ElementProperties  elementProperties)
    {
        final String methodName = "getSymbolicName";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
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
    protected String getImplementationValue(ElementProperties  elementProperties)
    {
        final String methodName = "getImplementationValue";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
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
    protected Map<String, String> getAdditionalValues(ElementProperties  elementProperties)
    {
        final String methodName = "getAdditionalValues";

        if (elementProperties != null)
        {
            return propertyHelper.getStringMapFromProperty(localServiceName,
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
     * Extract the confidence property from the supplied element properties.
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
     * Extract the expression property from the supplied element properties.
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
     * Extract the inferredDataType property from the supplied element properties.
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
     * Extract the namePatterns property from the supplied element properties.
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
     * Extract the profileProperties property from the supplied element properties.
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
     * Extract and delete the size property from the supplied element properties.
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

        return 0;
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
     * Extract the relationshipProperties property from the supplied element properties.
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
     * Extract and delete the informalTopic standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeInformalTopic(ElementProperties  elementProperties)
    {
        final String methodName = "removeInformalTopic";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.INFORMAL_CATEGORY.name,
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
    protected List<String> removeCandidateGlossaryCategoryGUIDs(ElementProperties elementProperties)
    {
        final String methodName = "removeCandidateGlossaryCategoryGUIDs";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.CANDIDATE_GLOSSARY_CATEGORY_GUIDS.name,
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
     * Extract and delete the role property from the supplied element properties.
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
     * Extract the purposes property from the supplied element properties.
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
     * Extract and delete the iscQualifiedNames property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
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
     * Extract and delete the defaultMediaUsage property from the supplied element properties.
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

            if (propertyHelper.isTypeOf(attachedClassification, OpenMetadataType.ANCHORS_CLASSIFICATION.typeName))
            {
                beanProperties = new AnchorsProperties();

                ((AnchorsProperties)beanProperties).setAnchorGUID(this.removeAnchorGUID(elementProperties));
                ((AnchorsProperties)beanProperties).setAnchorTypeName(this.removeAnchorTypeName(elementProperties));
                ((AnchorsProperties)beanProperties).setAnchorDomainName(this.removeAnchorDomainName(elementProperties));
                ((AnchorsProperties)beanProperties).setAnchorScopeGUID(this.removeAnchorScopeGUID(elementProperties));
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
        if (relationshipProperties != null)
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
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ACTIONS_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ActionsProperties();
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
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new GovernedByProperties();

                ((GovernedByProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((GovernedByProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
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
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AssignmentScopeProperties();

                ((AssignmentScopeProperties)relationshipBeanProperties).setAssignmentType(this.removeAssignmentType(elementProperties));
                ((AssignmentScopeProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ASSOCIATED_SECURITY_GROUP_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AssociatedSecurityGroupProperties();

                ((AssociatedSecurityGroupProperties)relationshipBeanProperties).setOperationName(this.removeOperationName(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new AttributeForSchemaProperties();

                ((AttributeForSchemaProperties)relationshipBeanProperties).setPosition(this.removePosition(elementProperties));
                ((AttributeForSchemaProperties)relationshipBeanProperties).setMaxCardinality(this.removeMaxCardinality(elementProperties));
                ((AttributeForSchemaProperties)relationshipBeanProperties).setMinCardinality(this.removeMinCardinality(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.BUSINESS_CAPABILITY_DEPENDENCY_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new BusinessCapabilityDependencyProperties();

                ((BusinessCapabilityDependencyProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((BusinessCapabilityDependencyProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
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
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CONTEXT_EVENT_IMPACT_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ContextEventImpactProperties();

                ((ContextEventImpactProperties)relationshipBeanProperties).setSeverityLevelIdentifier(this.removeSeverityLevelIdentifier(elementProperties));
                ((ContextEventImpactProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CROWD_SOURCING_CONTRIBUTION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new CrowdSourcingContributionProperties();

                ((CrowdSourcingContributionProperties)relationshipBeanProperties).setRoleType(this.removeRoleType(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.CONTRACT_LINK_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new ContractLinkProperties();

                ((ContractLinkProperties)relationshipBeanProperties).setContractId(this.removeContractId(elementProperties));
                ((ContractLinkProperties)relationshipBeanProperties).setContractLiaison(this.removeContractLiaison(elementProperties));
                ((ContractLinkProperties)relationshipBeanProperties).setContractLiaisonTypeName(this.removeContractLiaisonTypeName(elementProperties));
                ((ContractLinkProperties)relationshipBeanProperties).setContractLiaisonPropertyName(this.removeContractLiaisonPropertyName(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new MemberDataFieldProperties();

                ((MemberDataFieldProperties)relationshipBeanProperties).setPosition(this.removePosition(elementProperties));
                ((MemberDataFieldProperties)relationshipBeanProperties).setMaxCardinality(this.removeMaxCardinality(elementProperties));
                ((MemberDataFieldProperties)relationshipBeanProperties).setMinCardinality(this.removeMinCardinality(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new MonitoredResourceProperties();

                ((MonitoredResourceProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((MonitoredResourceProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new NestedDataFieldProperties();

                ((NestedDataFieldProperties)relationshipBeanProperties).setPosition(this.removePosition(elementProperties));
                ((NestedDataFieldProperties)relationshipBeanProperties).setMaxCardinality(this.removeMaxCardinality(elementProperties));
                ((NestedDataFieldProperties)relationshipBeanProperties).setMinCardinality(this.removeMinCardinality(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.NESTED_LOCATION_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new NestedLocationProperties();

                ((NestedLocationProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((NestedLocationProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
            }
            else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.NOTIFICATION_SUBSCRIBER_RELATIONSHIP.typeName))
            {
                relationshipBeanProperties = new NotificationSubscriberProperties();

                ((NotificationSubscriberProperties)relationshipBeanProperties).setLabel(this.removeLabel(elementProperties));
                ((NotificationSubscriberProperties)relationshipBeanProperties).setDescription(this.removeDescription(elementProperties));
                ((NotificationSubscriberProperties)relationshipBeanProperties).setActivityStatus(this.removeActivityStatus(elementProperties));
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
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TOPIC.typeName))
                            {
                                beanProperties = new TopicProperties();

                                ((TopicProperties)beanProperties).setTopicName(removeTopicName(elementProperties));
                                ((TopicProperties)beanProperties).setTopicType(removeTopicType(elementProperties));
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
                            beanProperties = new DataFeedProperties();
                        }
                        else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_STORE.typeName))
                        {
                            beanProperties = new DataStoreProperties();

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
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.BUSINESS_CAPABILITY.typeName))
                {
                    beanProperties = new BusinessCapabilityProperties();
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
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_SPEC_COLLECTION.typeName))
                    {
                        beanProperties = new DataSpecProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_DICTIONARY_COLLECTION.typeName))
                    {
                        beanProperties = new DataDictionaryProperties();
                    }
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DIGITAL_PRODUCT.typeName))
                    {
                        beanProperties = new DigitalProductProperties();

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
                    else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GLOSSARY.typeName))
                    {
                        beanProperties = new GlossaryProperties();

                        ((GlossaryProperties)beanProperties).setLanguage(this.removeLanguage(elementProperties));
                        ((GlossaryProperties)beanProperties).setUsage(this.removeUsage(elementProperties));

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
                    ((DataStructureProperties)beanProperties).setNamespace(this.removeNamespace(elementProperties));
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
                            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.NOTIFICATION_TYPE.typeName))
                            {
                                beanProperties = new NotificationTypeProperties();

                                ((NotificationTypeProperties)beanProperties).setStartDate(this.removeStartDate(elementProperties));
                                ((NotificationTypeProperties)beanProperties).setRefreshTimeInterval(this.removeRefreshTimeInterval(elementProperties));
                                ((NotificationTypeProperties)beanProperties).setConnectorShutdownDate(this.removeConnectorShutdownDate(elementProperties));

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
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.PROJECT.typeName))
                {
                    beanProperties = new ProjectProperties();

                    ((ProjectProperties)beanProperties).setPriority(this.removeIntPriority(elementProperties));
                    ((ProjectProperties)beanProperties).setStartDate(this.removeStartDate(elementProperties));
                    ((ProjectProperties)beanProperties).setPlannedEndDate(this.removePlannedEndDate(elementProperties));
                    ((ProjectProperties)beanProperties).setProjectHealth(this.removeProjectHealth(elementProperties));
                    ((ProjectProperties)beanProperties).setProjectStatus(this.removeProjectStatus(elementProperties));
                    ((ProjectProperties)beanProperties).setProjectPhase(this.removeProjectPhase(elementProperties));
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
                else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.VALID_VALUE_DEFINITION.typeName))
                {
                    beanProperties = new ValidValueDefinitionProperties();

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

