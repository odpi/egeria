/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * GlossaryCategoryBuilder creates the parts for an entity that represents a glossary category.
 */
public class GlossaryCategoryBuilder extends ReferenceableBuilder
{
    private String displayName = null;
    private String description = null;


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the glossary category
     * @param displayName short display name for the glossary category
     * @param description description of the glossary category
     * @param additionalProperties additional properties for a glossary category
     * @param extendedProperties  properties for a glossary category subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GlossaryCategoryBuilder(String               qualifiedName,
                            String               displayName,
                            String               description,
                            Map<String, String>  additionalProperties,
                            Map<String, Object>  extendedProperties,
                            OMRSRepositoryHelper repositoryHelper,
                            String               serviceName,
                            String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              OpenMetadataType.GLOSSARY_CATEGORY.typeGUID,
              OpenMetadataType.GLOSSARY_CATEGORY.typeName,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the glossary category
     * @param displayName short display name for the glossary category
     * @param description description of the glossary category
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public GlossaryCategoryBuilder(String        qualifiedName,
                            String               displayName,
                            String               description,
                            OMRSRepositoryHelper repositoryHelper,
                            String               serviceName,
                            String               serverName)
    {
        super(qualifiedName,
              null,
              OpenMetadataType.GLOSSARY_CATEGORY.typeGUID,
              OpenMetadataType.GLOSSARY_CATEGORY.typeName,
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
    GlossaryCategoryBuilder(OMRSRepositoryHelper repositoryHelper,
                            String               serviceName,
                            String               serverName)
    {
        super(OpenMetadataType.GLOSSARY_CATEGORY.typeGUID,
              OpenMetadataType.GLOSSARY_CATEGORY.typeName,
              repositoryHelper,
              serviceName,
              serverName);
    }

    /**
     * Set up the "RootCategory" classification for this entity.  This is used when a new entity is being created, and it is known to be
     * a root category.
     *
     * @param userId calling user
     * @param methodName calling method
     * @throws PropertyServerException a null anchors GUID has been supplied
     */
    public void setRootCategory(String userId,
                                String methodName) throws PropertyServerException
    {
        try
        {
            Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                  null,
                                                                                  null,
                                                                                  InstanceProvenanceType.LOCAL_COHORT,
                                                                                  userId,
                                                                                  OpenMetadataType.ROOT_CATEGORY_CLASSIFICATION.typeName,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  null);
            newClassifications.put(classification.getName(), classification);
        }
        catch (Exception error)
        {
            errorHandler.handleUnsupportedAnchorsType(error, methodName, OpenMetadataType.ROOT_CATEGORY_CLASSIFICATION.typeName);
        }
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

        return properties;
    }
}
