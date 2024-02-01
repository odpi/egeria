/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * GovernanceDomainBuilder creates the parts for an entity that represents a GovernanceDomainDescription.
 */
public class GovernanceDomainBuilder extends ReferenceableBuilder
{
    private String displayName      = null;
    private String description      = null;
    private int    domainIdentifier = 0;


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the governance domain
     * @param displayName short display name for the governance domain
     * @param description description of the governance domain
     * @param domainIdentifier the unique identifier used to represent the domain in governance definitions
     * @param additionalProperties additional properties for a governance domain
     * @param typeGUID unique identifier of this element's type
     * @param typeName unique name of this element's type
     * @param extendedProperties  properties for a governance domain subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GovernanceDomainBuilder(String               qualifiedName,
                            String               displayName,
                            String               description,
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
        this.domainIdentifier = domainIdentifier;
    }


  
    /**
     * Relationship constructor
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GovernanceDomainBuilder(OMRSRepositoryHelper repositoryHelper,
                            String               serviceName,
                            String               serverName)
    {
        super(OpenMetadataType.GOVERNANCE_DOMAIN_TYPE_GUID,
              OpenMetadataType.GOVERNANCE_DOMAIN_TYPE_NAME,
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

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataType.DOMAIN_IDENTIFIER_PROPERTY_NAME,
                                                               domainIdentifier,
                                                               methodName);

        return properties;
    }
}
