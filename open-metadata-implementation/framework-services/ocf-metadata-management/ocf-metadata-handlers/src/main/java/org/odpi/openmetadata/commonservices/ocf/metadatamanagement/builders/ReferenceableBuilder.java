/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.LatestChange;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Referenceable;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SecurityTags;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.List;
import java.util.Map;

/**
 * ReferenceableBuilder creates Open Metadata Repository Services (OMRS) objects based on the
 * bean properties supplied in the constructor.
 */
public class ReferenceableBuilder extends RootBuilder
{
    protected String              qualifiedName        = null;
    protected String              typeId               = null;
    protected String              typeName             = null;
    protected Map<String, String> additionalProperties = null;
    protected Map<String, Object> extendedProperties   = null;

    protected LatestChange latestChange = null;
    private   SecurityTags securityTags = null;


    /**
     * Constructor for simple creates.
     *
     * @param qualifiedName unique name
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    protected ReferenceableBuilder(String               qualifiedName,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super (repositoryHelper, serviceName, serverName);

        this.qualifiedName = qualifiedName;
    }


    /**
     * Constructor for simple creates.
     *
     * @param qualifiedName unique name
     * @param typeName type name to use for the entity
     * @param typeId type GUID to use for the entity
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    protected ReferenceableBuilder(String               qualifiedName,
                                   String               typeName,
                                   String               typeId,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super (repositoryHelper, serviceName, serverName);

        this.qualifiedName = qualifiedName;
        this.typeId = typeId;
        this.typeName = typeName;
    }


    /**
     * Constructor for updates.
     *
     * @param qualifiedName unique name
     * @param additionalProperties additional properties
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    protected ReferenceableBuilder(String               qualifiedName,
                                   Map<String, String>  additionalProperties,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);

        this.qualifiedName = qualifiedName;
        this.additionalProperties = additionalProperties;

    }


    /**
     * Constructor for updates.
     *
     * @param qualifiedName unique name
     * @param additionalProperties additional properties
     * @param typeId type GUID to use for the entity
     * @param typeName type name to use for the entity
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    protected ReferenceableBuilder(String               qualifiedName,
                                   Map<String, String>  additionalProperties,
                                   String               typeId,
                                   String               typeName,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);

        this.qualifiedName = qualifiedName;
        this.typeId = typeId;
        this.typeName = typeName;
        this.additionalProperties = additionalProperties;

    }


    /**
     * Constructor for updates.
     *
     * @param qualifiedName unique name
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    @Deprecated
    protected ReferenceableBuilder(String               qualifiedName,
                                   Map<String, String>  additionalProperties,
                                   Map<String, Object>  extendedProperties,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);

        this.qualifiedName = qualifiedName;
        this.additionalProperties = additionalProperties;
        this.extendedProperties = extendedProperties;

    }


    /**
     * Constructor for updates.
     *
     * @param qualifiedName unique name
     * @param additionalProperties additional properties
     * @param typeId type GUID to use for the entity
     * @param typeName type name to use for the entity
     * @param extendedProperties  properties from the subtype.
     * @param latestChange description of the last change to the asset.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    protected ReferenceableBuilder(String               qualifiedName,
                                   Map<String, String>  additionalProperties,
                                   String               typeId,
                                   String               typeName,
                                   Map<String, Object>  extendedProperties,
                                   LatestChange         latestChange,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);

        this.qualifiedName = qualifiedName;
        this.typeId = typeId;
        this.typeName = typeName;
        this.additionalProperties = additionalProperties;
        this.extendedProperties = extendedProperties;
        this.latestChange = latestChange;
    }



    /**
     * Constructor for updates.
     *
     * @param qualifiedName unique name
     * @param additionalProperties additional properties
     * @param typeName type name to use for the entity
     * @param typeId type GUID to use for the entity
     * @param extendedProperties  properties from the subtype.
     * @param actionDescription description of the last change to the asset.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    protected ReferenceableBuilder(String               qualifiedName,
                                   Map<String, String>  additionalProperties,
                                   String               typeName,
                                   String               typeId,
                                   Map<String, Object>  extendedProperties,
                                   String               actionDescription,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);

        this.qualifiedName = qualifiedName;
        this.typeId = typeId;
        this.typeName = typeName;
        this.additionalProperties = additionalProperties;
        this.extendedProperties = extendedProperties;
        this.latestChange = new LatestChange();
        this.latestChange.setActionDescription(actionDescription);
    }


    /**
     * Constructor for updates.
     *
     * @param qualifiedName unique name
     * @param additionalProperties additional properties
     * @param typeName type name to use for the entity
     * @param typeId type GUID to use for the entity
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    protected ReferenceableBuilder(String               qualifiedName,
                                   Map<String, String>  additionalProperties,
                                   String               typeName,
                                   String               typeId,
                                   Map<String, Object>  extendedProperties,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);

        this.qualifiedName = qualifiedName;
        this.typeId = typeId;
        this.typeName = typeName;
        this.extendedProperties = extendedProperties;
        this.additionalProperties = additionalProperties;
    }


    /**
     * Constructor for classifications.
     *
     * @param beanProperties properties and header
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     * @throws InvalidParameterException bad properties in bean classifications
     */
    public ReferenceableBuilder(Referenceable        beanProperties,
                                OMRSRepositoryHelper repositoryHelper,
                                String               serviceName,
                                String               serverName) throws InvalidParameterException
    {
        super(repositoryHelper, serviceName, serverName);

        if (beanProperties != null)
        {
            this.qualifiedName        = beanProperties.getQualifiedName();
            this.additionalProperties = beanProperties.getAdditionalProperties();
        }
    }


    public String getTypeId()
    {
        return typeId;
    }


    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (qualifiedName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                      qualifiedName,
                                                                      methodName);
        }

        if (additionalProperties != null)
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         ReferenceableMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                         additionalProperties,
                                                                         methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object for search.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getNameInstanceProperties(String  methodName)
    {
        InstanceProperties properties = null;

        if (qualifiedName != null)
        {
            String literalQualifiedName = repositoryHelper.getExactMatchRegex(qualifiedName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                      literalQualifiedName,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object for search.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getQualifiedNameInstanceProperties(String  methodName)
    {
        return this.getNameInstanceProperties(methodName);
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getSearchInstanceProperties(String  methodName)
    {
        InstanceProperties properties = null;

        if (qualifiedName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                      qualifiedName,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Return the bean properties describing the asset's owner in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties getSecurityTagProperties(String  methodName)
    {
        InstanceProperties properties = null;

        if (securityTags != null)
        {
            if (securityTags.getSecurityLabels() != null)
            {
                properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                               null,
                                                                               ReferenceableMapper.SECURITY_LABELS_PROPERTY_NAME,
                                                                               securityTags.getSecurityLabels(),
                                                                               methodName);
            }
            if (securityTags.getSecurityProperties() != null)
            {
                properties = repositoryHelper.addMapPropertyToInstance(serviceName,
                                                                       properties,
                                                                       ReferenceableMapper.SECURITY_LABELS_PROPERTY_NAME,
                                                                       securityTags.getSecurityProperties(),
                                                                       methodName);
            }
        }

        return properties;
    }



    /**
     * Return a list of entity classifications that can be stored in the metadata
     * repository.
     *
     * @param userId calling user
     * @param methodName calling method
     * @return list of entity classification objects
     * @throws InvalidParameterException the properties of the classification are flawed
     */
    public List<Classification> getEntityClassifications(String   userId,
                                                         String   methodName) throws InvalidParameterException
    {
        /*
         * Retrieve the classifications provided by the caller
         */
        List<Classification> entityClassifications = null;

        if (securityTags != null)
        {
            /*
             * Create the special classifications
             */
            try
            {
                Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                      null,
                                                                                      null,
                                                                                      InstanceProvenanceType.LOCAL_COHORT,
                                                                                      userId,
                                                                                      ReferenceableMapper.SECURITY_TAG_CLASSIFICATION_TYPE_NAME,
                                                                                      typeName,
                                                                                      null,
                                                                                      null,
                                                                                      this.getSecurityTagProperties(methodName));

                entityClassifications = repositoryHelper.addClassificationToList(serviceName, null, classification, methodName);
            }
            catch (TypeErrorException error)
            {
                throw new InvalidParameterException(error, "classificationName");
            }
        }

        return entityClassifications;
    }

}
