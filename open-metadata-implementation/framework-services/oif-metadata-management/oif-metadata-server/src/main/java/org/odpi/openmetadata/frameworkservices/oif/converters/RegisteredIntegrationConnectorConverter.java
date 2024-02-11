/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.oif.converters;


import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.integration.contextmanager.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.integration.properties.IntegrationConnectorElement;
import org.odpi.openmetadata.frameworks.integration.properties.RegisteredIntegrationConnectorElement;
import org.odpi.openmetadata.frameworks.integration.properties.RegisteredIntegrationConnectorProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * RegisteredIntegrationConnectorConverter transfers the relevant properties from a IntegrationConnectorElement bean
 * and the Open Metadata Repository Services (OMRS) Relationship object into a RegisteredIntegrationConnector bean.
 */
public class RegisteredIntegrationConnectorConverter
{
    private final OMRSRepositoryHelper       repositoryHelper;
    private final String                     serviceName;

    /**
     * Constructor captures the repository content needed to create the endpoint object.
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     */
    public RegisteredIntegrationConnectorConverter(OMRSRepositoryHelper       repositoryHelper,
                                                   String                     serviceName)
    {
        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;
    }


    /**
     * Request the bean is extracted from the repository entity.
     *
     * @param integrationConnector properties to convert
     * @param registrationRelationship RegisteredIntegrationConnector relationship
     * @return output bean
     */
    public RegisteredIntegrationConnectorElement getBean(IntegrationConnectorElement integrationConnector,
                                                         Relationship                registrationRelationship)
    {
        final String  methodName = "getBean";

        RegisteredIntegrationConnectorElement bean = new RegisteredIntegrationConnectorElement(integrationConnector);

        if (registrationRelationship != null)
        {
            /*
             * The properties are removed from the instance properties and stowed in the bean.
\            */
            InstanceProperties instanceProperties = registrationRelationship.getProperties();

            if (instanceProperties != null)
            {
                RegisteredIntegrationConnectorProperties registrationProperties = new RegisteredIntegrationConnectorProperties();

                bean.setConnectorId(registrationRelationship.getGUID());
                registrationProperties.setConnectorName(repositoryHelper.getStringProperty(serviceName,
                                                                                           OpenMetadataType.CONNECTOR_NAME_PROPERTY_NAME,
                                                                                           instanceProperties,
                                                                                           methodName));
                registrationProperties.setConnectorUserId(repositoryHelper.getStringProperty(serviceName,
                                                                                             OpenMetadataType.CONNECTOR_USER_ID_PROPERTY_NAME,
                                                                                             instanceProperties,
                                                                                             methodName));
                registrationProperties.setMetadataSourceQualifiedName(repositoryHelper.getStringProperty(serviceName,
                                                                                                         OpenMetadataType.METADATA_SOURCE_QUALIFIED_NAME_PROPERTY_NAME,
                                                                                                         instanceProperties,
                                                                                                         methodName));
                registrationProperties.setStartDate(repositoryHelper.getDateProperty(serviceName,
                                                                                     OpenMetadataType.START_DATE_PROPERTY_NAME,
                                                                                     instanceProperties,
                                                                                     methodName));
                registrationProperties.setRefreshTimeInterval(repositoryHelper.getLongProperty(serviceName,
                                                                                               OpenMetadataType.REFRESH_TIME_INTERVAL_PROPERTY_NAME,
                                                                                               instanceProperties,
                                                                                               methodName));
                registrationProperties.setStopDate(repositoryHelper.getDateProperty(serviceName,
                                                                                    OpenMetadataType.STOP_DATE_PROPERTY_NAME,
                                                                                    instanceProperties,
                                                                                    methodName));
                registrationProperties.setGenerateIntegrationReports(repositoryHelper.getBooleanProperty(serviceName,
                                                                                                         OpenMetadataType.GENERATE_INTEGRATION_REPORT_PROPERTY_NAME,
                                                                                                         instanceProperties,
                                                                                                         methodName));

                registrationProperties.setPermittedSynchronization(this.removePermittedSynchronization(instanceProperties));

                bean.setRegistrationProperties(registrationProperties);
            }
        }

        return bean;
    }


    /**
     * Extract and delete the permittedSynchronization property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return SynchronizationDirection enum
     */
    PermittedSynchronization removePermittedSynchronization(InstanceProperties  instanceProperties)
    {
        final String methodName = "removePermittedSynchronization";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataType.PERMITTED_SYNC_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName);

            for (PermittedSynchronization synchronizationDirection : PermittedSynchronization.values())
            {
                if (synchronizationDirection.getOpenTypeOrdinal() == ordinal)
                {
                    return synchronizationDirection;
                }
            }
        }

        return PermittedSynchronization.BOTH_DIRECTIONS;
    }

}
