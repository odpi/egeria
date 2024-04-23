/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * ExternalReferenceBuilder creates the parts for an entity that represents an external reference.
 */
public class ExternalReferenceBuilder extends ReferenceableBuilder
{
    private String displayName      = null;
    private String description      = null;
    private String url              = null;
    private String referenceVersion = null;
    private String organization     = null;


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the reference
     * @param displayName short display name for the reference
     * @param description description of the reference
     * @param url the location of the reference
     * @param referenceVersion version of the reference
     * @param organization owning org for the reference
     * @param additionalProperties additional properties for the reference
     * @param typeGUID unique identifier of this element's type
     * @param typeName unique name of this element's type
     * @param extendedProperties  properties for a subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ExternalReferenceBuilder(String               qualifiedName,
                             String               displayName,
                             String               description,
                             String               url,
                             String               referenceVersion,
                             String               organization,
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
        this.url = url;
        this.referenceVersion = referenceVersion;
        this.organization = organization;
    }


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the reference
     * @param displayName short display name for the reference
     * @param description description of the reference
     * @param url the location of the reference
     * @param referenceVersion version of the reference
     * @param organization owning org for the reference
     * @param additionalProperties additional properties for the reference
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public ExternalReferenceBuilder(String               qualifiedName,
                                    String               displayName,
                                    String               description,
                                    String               url,
                                    String               referenceVersion,
                                    String               organization,
                                    Map<String, String>  additionalProperties,
                                    OMRSRepositoryHelper repositoryHelper,
                                    String               serviceName,
                                    String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              OpenMetadataType.EXTERNAL_REFERENCE.typeGUID,
              OpenMetadataType.EXTERNAL_REFERENCE.typeName,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
        this.url = url;
        this.referenceVersion = referenceVersion;
        this.organization = organization;
    }
    
    
    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the external reference
     * @param displayName short display name for the external reference
     * @param description description of the external reference
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ExternalReferenceBuilder(String               qualifiedName,
                             String               displayName,
                             String               description,
                             OMRSRepositoryHelper repositoryHelper,
                             String               serviceName,
                             String               serverName)
    {
        super(qualifiedName,
              null,
              OpenMetadataType.EXTERNAL_REFERENCE.typeGUID,
              OpenMetadataType.EXTERNAL_REFERENCE.typeName,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Classification constructor
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ExternalReferenceBuilder(OMRSRepositoryHelper repositoryHelper,
                             String               serviceName,
                             String               serverName)
    {
        super(OpenMetadataType.EXTERNAL_REFERENCE.typeGUID,
              OpenMetadataType.EXTERNAL_REFERENCE.typeName,
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
                                                                  OpenMetadataType.URL_PROPERTY_NAME,
                                                                  url,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.REFERENCE_VERSION_PROPERTY_NAME,
                                                                  referenceVersion,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.ORGANIZATION_PROPERTY_NAME,
                                                                  organization,
                                                                  methodName);

        return properties;
    }


    /**
     * Return the properties for a relationship between a referenceable and an external reference.
     *
     * @param referenceId the local reference identifier for the reference
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getLinkProperties(String referenceId,
                                         String description,
                                         String methodName)
    {
        InstanceProperties properties = null;

        if (referenceId != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataType.REFERENCE_ID_PROPERTY_NAME,
                                                                      referenceId,
                                                                      methodName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataProperty.DESCRIPTION.name,
                                                                      description,
                                                                      methodName);

        }

        setEffectivityDates(properties);

        return properties;
    }
}
