/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.oif.builder;

import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * IntegrationReportBuilder creates the parts for an entity that represents an integration report.
 */
public class IntegrationReportBuilder extends OpenMetadataAPIGenericBuilder
{
    private final String              daemonName;
    private final String              connectorId;
    private final String              connectorName;
    private final Date                refreshStartDate;
    private final Date                refreshCompletionDate;
    private final List<String>        createdElements;
    private final List<String>        updatedElements;
    private final List<String>        deletedElements;
    private final Map<String, String> additionalProperties;


    /**
     * Create constructor
     *
     * @param daemonName name of the integration daemon
     * @param connectorId unique identifier of the connector deployment
     * @param connectorName name of the connector
     * @param refreshStartDate starting time period for the report
     * @param refreshCompletionDate ending time period for the report
     * @param createdElements list of elements that were created
     * @param updatedElements list of elements that were updated
     * @param deletedElements list of elements that were deleted (or archived)
     * @param additionalProperties additional properties for a actor profile
     * @param typeGUID unique identifier of this element's type
     * @param typeName unique name of this element's type
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public IntegrationReportBuilder(String daemonName,
                                    String connectorId,
                                    String connectorName,
                                    Date refreshStartDate,
                                    Date refreshCompletionDate,
                                    List<String> createdElements,
                                    List<String> updatedElements,
                                    List<String> deletedElements,
                                    Map<String, String> additionalProperties,
                                    String typeGUID,
                                    String typeName,
                                    OMRSRepositoryHelper repositoryHelper,
                                    String serviceName,
                                    String serverName)
    {
        super(typeGUID,
              typeName,
              repositoryHelper,
              serviceName,
              serverName);

        this.daemonName = daemonName;
        this.connectorId = connectorId;
        this.connectorName = connectorName;
        this.refreshStartDate = refreshStartDate;
        this.refreshCompletionDate = refreshCompletionDate;
        this.createdElements = createdElements;
        this.updatedElements = updatedElements;
        this.deletedElements = deletedElements;
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.SERVER_NAME_PROPERTY_NAME,
                                                                  daemonName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.CONNECTOR_ID_PROPERTY_NAME,
                                                                  connectorId,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.CONNECTOR_NAME_PROPERTY_NAME,
                                                                  connectorName,
                                                                  methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataType.REFRESH_START_DATE_PROPERTY_NAME,
                                                                refreshStartDate,
                                                                methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataType.REFRESH_COMPLETION_DATE_PROPERTY_NAME,
                                                                refreshCompletionDate,
                                                                methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataType.CREATED_ELEMENTS_PROPERTY_NAME,
                                                                       createdElements,
                                                                       methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataType.UPDATED_ELEMENTS_PROPERTY_NAME,
                                                                       updatedElements,
                                                                       methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataType.DELETED_ELEMENTS_PROPERTY_NAME,
                                                                       deletedElements,
                                                                       methodName);

        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                     additionalProperties,
                                                                     methodName);

        return properties;
    }
}
