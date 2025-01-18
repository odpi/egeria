/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.converters;


import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IntegrationConnectorElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RegisteredIntegrationConnectorElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RegisteredIntegrationConnectorProperties;
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
                                                                                           OpenMetadataProperty.CONNECTOR_NAME.name,
                                                                                           instanceProperties,
                                                                                           methodName));
                registrationProperties.setConnectorUserId(repositoryHelper.getStringProperty(serviceName,
                                                                                             OpenMetadataProperty.CONNECTOR_USER_ID.name,
                                                                                             instanceProperties,
                                                                                             methodName));
                registrationProperties.setMetadataSourceQualifiedName(repositoryHelper.getStringProperty(serviceName,
                                                                                                         OpenMetadataProperty.METADATA_SOURCE_QUALIFIED_NAME.name,
                                                                                                         instanceProperties,
                                                                                                         methodName));
                registrationProperties.setStartDate(repositoryHelper.getDateProperty(serviceName,
                                                                                     OpenMetadataProperty.START_DATE.name,
                                                                                     instanceProperties,
                                                                                     methodName));
                registrationProperties.setRefreshTimeInterval(repositoryHelper.getLongProperty(serviceName,
                                                                                               OpenMetadataProperty.REFRESH_TIME_INTERVAL.name,
                                                                                               instanceProperties,
                                                                                               methodName));
                registrationProperties.setStopDate(repositoryHelper.getDateProperty(serviceName,
                                                                                    OpenMetadataProperty.STOP_DATE.name,
                                                                                    instanceProperties,
                                                                                    methodName));
                registrationProperties.setGenerateIntegrationReports(repositoryHelper.getBooleanProperty(serviceName,
                                                                                                         OpenMetadataProperty.GENERATE_INTEGRATION_REPORT.name,
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
     * @return PermittedSynchronization enum
     */
    PermittedSynchronization removePermittedSynchronization(InstanceProperties  instanceProperties)
    {
        final String methodName = "removePermittedSynchronization";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name,
                                                                     instanceProperties,
                                                                     methodName);

            for (PermittedSynchronization permittedSynchronization : PermittedSynchronization.values())
            {
                if (permittedSynchronization.getOrdinal() == ordinal)
                {
                    return permittedSynchronization;
                }
            }
        }

        return PermittedSynchronization.BOTH_DIRECTIONS;
    }

}
