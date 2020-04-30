/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.SoftwareServerCapabilityBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * DiscoveryEngineBuilder creates instance properties based on the bean properties
 * provided in the constructor.
 */
public class DiscoveryEngineBuilder extends SoftwareServerCapabilityBuilder
{
    /**
     * Constructor when basic properties are known.
     *
     * @param qualifiedName unique name
     * @param repositoryHelper helper methods
     * @param displayName display name of discovery engine
     * @param description description of discovery engine
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public DiscoveryEngineBuilder(String               qualifiedName,
                                  String               displayName,
                                  String               description,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(qualifiedName, displayName, description, repositoryHelper, serviceName, serverName);
    }


    /**
     * Constructor supporting all properties.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description new description for the discovery engine.
     * @param typeDescription new description of the type of discovery engine.
     * @param version new version number for the discovery engine implementation.
     * @param patchLevel new patch level for the discovery engine implementation.
     * @param source new source description for the implementation of the discovery engine.
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public DiscoveryEngineBuilder(String               qualifiedName,
                                  String               displayName,
                                  String               description,
                                  String               typeDescription,
                                  String               version,
                                  String               patchLevel,
                                  String               source,
                                  Map<String, String>  additionalProperties,
                                  Map<String, Object>  extendedProperties,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(qualifiedName,
              displayName,
              description,
              typeDescription,
              version,
              patchLevel,
              source,
              additionalProperties,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);
    }
}
