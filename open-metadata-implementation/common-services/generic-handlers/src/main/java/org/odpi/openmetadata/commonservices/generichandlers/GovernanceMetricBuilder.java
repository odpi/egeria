/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * GovernanceMetricBuilder creates the parts for an entity that represents a governance metric.
 */
public class GovernanceMetricBuilder extends ReferenceableBuilder
{
    private String displayName = null;
    private String description = null;
    private String measurement = null;
    private String target = null;
    private int    domainIdentifier = 0;


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the governance metric
     * @param displayName short display name for the governance metric
     * @param description description of the governance metric
     * @param measurement values describing what to measure
     * @param target values describing the desired state
     * @param domainIdentifier identifier of the governance domain - default 0 (all/any)
     * @param additionalProperties additional properties for a governance metric
     * @param typeGUID unique identifier of this element's type
     * @param typeName unique name of this element's type
     * @param extendedProperties  properties for a governance metric subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GovernanceMetricBuilder(String               qualifiedName,
                            String               displayName,
                            String               description,
                            String               measurement,
                            String               target,
                            int                  domainIdentifier,
                            Map<String, String>  additionalProperties,
                            String               typeGUID,
                            String               typeName,
                            Map<String, Object>  extendedProperties,
                            OMRSRepositoryHelper repositoryHelper,
                            String               serviceName,
                            String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              typeGUID,
              typeName,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
        this.measurement = measurement;
        this.target = target;
        this.domainIdentifier = domainIdentifier;
    }


    /**
     * Classification constructor
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GovernanceMetricBuilder(OMRSRepositoryHelper repositoryHelper,
                            String               serviceName,
                            String               serverName)
    {
        super(OpenMetadataType.GOVERNANCE_METRIC.typeGUID,
              OpenMetadataType.GOVERNANCE_METRIC.typeName,
              repositoryHelper,
              serviceName,
              serverName);
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

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.MEASUREMENT.name,
                                                                  measurement,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.TARGET.name,
                                                                  target,
                                                                  methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                               domainIdentifier,
                                                               methodName);

        return properties;
    }
}
