/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Date;
import java.util.Map;

/**
 * DiscoveryAnalysisReportBuilder supports the creation of the entities and
 * relationships that describe an Open Discovery Framework (ODF) DiscoveryAnalysisReport.
 */
public class DiscoveryAnalysisReportBuilder extends ReferenceableBuilder
{
    private final String                 displayName;
    private final String                 description;
    private final Date                   creationDate;
    private final Map<String, String>    analysisParameters;
    private final int                    discoveryRequestStatus;



    /**
     * Constructor supporting all properties - used for updates.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description long description
     * @param creationDate date that the report ran
     * @param discoveryRequestStatus status of the discovery request (is it still running?)
     * @param analysisParameters list of zones that this discovery service belongs to.
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    DiscoveryAnalysisReportBuilder(String                 qualifiedName,
                                   String                 displayName,
                                   String                 description,
                                   Date                   creationDate,
                                   Map<String, String>    analysisParameters,
                                   int                    discoveryRequestStatus,
                                   Map<String, String>    additionalProperties,
                                   Map<String, Object>    extendedProperties,
                                   OMRSRepositoryHelper   repositoryHelper,
                                   String                 serviceName,
                                   String                 serverName)
    {
        super(qualifiedName,
              additionalProperties,
              OpenMetadataType.DISCOVERY_ANALYSIS_REPORT_TYPE_GUID,
              OpenMetadataType.DISCOVERY_ANALYSIS_REPORT_TYPE_NAME,
              extendedProperties,
              InstanceStatus.DRAFT,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
        this.creationDate = creationDate;
        this.analysisParameters = analysisParameters;
        this.discoveryRequestStatus = discoveryRequestStatus;
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
                                                                  OpenMetadataProperty.DISPLAY_NAME.name,
                                                                  displayName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataType.EXECUTION_DATE_PROPERTY_NAME,
                                                                creationDate,
                                                                methodName);

        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataProperty.ANALYSIS_PARAMETERS.name,
                                                                     analysisParameters,
                                                                     methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataType.DISCOVERY_SERVICE_STATUS_PROPERTY_NAME,
                                                                    OpenMetadataType.DISCOVERY_REQUEST_STATUS_ENUM_TYPE_GUID,
                                                                    OpenMetadataType.DISCOVERY_REQUEST_STATUS_ENUM_TYPE_NAME,
                                                                    discoveryRequestStatus,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataType.DISCOVERY_SERVICE_STATUS_PROPERTY_NAME);
        }

        return properties;
    }
}
