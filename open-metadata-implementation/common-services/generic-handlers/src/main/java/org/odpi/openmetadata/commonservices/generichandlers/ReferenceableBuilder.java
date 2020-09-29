/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.List;
import java.util.Map;

/**
 * ReferenceableBuilder creates Open Metadata Repository Services (OMRS) objects based on the
 * bean properties supplied in the constructor.
 */
public class ReferenceableBuilder extends OpenMetadataAPIGenericBuilder
{
    protected String              qualifiedName = null;
    private   Map<String, String> additionalProperties = null;


    /**
     * Constructor for simple creates.
     *
     * @param qualifiedName    unique name
     * @param repositoryHelper helper methods
     * @param serviceName      name of this OMAS
     * @param serverName       name of local server
     */
    protected ReferenceableBuilder(String               qualifiedName,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(OpenMetadataAPIMapper.REFERENCEABLE_TYPE_GUID,
              OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);

        this.qualifiedName = qualifiedName;
    }


    /**
     * Constructor for simple creates.
     *
     * @param typeId           type GUID to use for the entity
     * @param typeName         type name to use for the entity
     * @param repositoryHelper helper methods
     * @param serviceName      name of this OMAS
     * @param serverName       name of local server
     */
    protected ReferenceableBuilder(String               typeId,
                                   String               typeName,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(typeId,
              typeName,
              repositoryHelper,
              serviceName,
              serverName);
    }

    /**
     * Constructor for simple creates.
     *
     * @param qualifiedName    unique name
     * @param typeName         type name to use for the entity
     * @param typeId           type GUID to use for the entity
     * @param repositoryHelper helper methods
     * @param serviceName      name of this OMAS
     * @param serverName       name of local server
     */
    protected ReferenceableBuilder(String               qualifiedName,
                                   String               typeId,
                                   String               typeName,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(typeId,
              typeName,
              repositoryHelper,
              serviceName,
              serverName);

        this.qualifiedName = qualifiedName;
    }


    /**
     * Constructor for updates.
     *
     * @param qualifiedName        unique name
     * @param additionalProperties additional properties
     * @param repositoryHelper     helper methods
     * @param serviceName          name of this OMAS
     * @param serverName           name of local server
     */
    protected ReferenceableBuilder(String               qualifiedName,
                                   Map<String, String>  additionalProperties,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(OpenMetadataAPIMapper.REFERENCEABLE_TYPE_GUID,
              OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);

        this.qualifiedName        = qualifiedName;
        this.additionalProperties = additionalProperties;
    }


    /**
     * Constructor for updates.
     *
     * @param qualifiedName        unique name
     * @param additionalProperties additional properties
     * @param typeName             type name to use for the entity
     * @param typeId               type GUID to use for the entity
     * @param repositoryHelper     helper methods
     * @param serviceName          name of this OMAS
     * @param serverName           name of local server
     */
    protected ReferenceableBuilder(String               qualifiedName,
                                   Map<String, String>  additionalProperties,
                                   String               typeId,
                                   String               typeName,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(typeId,
              typeName,
              repositoryHelper,
              serviceName,
              serverName);

        this.qualifiedName        = qualifiedName;
        this.additionalProperties = additionalProperties;
    }


    /**
     * Constructor for updates.
     *
     * @param qualifiedName        unique name
     * @param additionalProperties name value pairs for values that are not formally defined in the type system
     * @param typeName             type name to use for the entity
     * @param typeId               type GUID to use for the entity
     * @param extendedProperties   properties from the subtype.
     * @param repositoryHelper     helper methods
     * @param serviceName          name of this OMAS
     * @param serverName           name of local server
     */
    protected ReferenceableBuilder(String               qualifiedName,
                                   Map<String, String>  additionalProperties,
                                   String               typeId,
                                   String               typeName,
                                   Map<String, Object>  extendedProperties,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(typeId,
              typeName,
              extendedProperties,
              null,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.qualifiedName        = qualifiedName;
        this.additionalProperties = additionalProperties;
    }


    /**
     * Constructor for templated entities.
     *
     * @param qualifiedName        unique name
     * @param additionalProperties name value pairs for values that are not formally defined in the type system
     * @param typeName             type name to use for the entity
     * @param typeId               type GUID to use for the entity
     * @param extendedProperties   properties from the subtype
     * @param instanceStatus       initial instance status for the entity
     * @param repositoryHelper     helper methods
     * @param serviceName          name of this OMAS
     * @param serverName           name of local server
     */
    protected ReferenceableBuilder(String               qualifiedName,
                                   Map<String, String>  additionalProperties,
                                   String               typeId,
                                   String               typeName,
                                   Map<String, Object>  extendedProperties,
                                   InstanceStatus       instanceStatus,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(typeId,
              typeName,
              extendedProperties,
              instanceStatus,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.qualifiedName        = qualifiedName;
        this.additionalProperties = additionalProperties;
    }


    /**
     * Set up the template classification.
     *
     * @param userId calling user
     * @param name template name
     * @param description template description
     * @param additionalProperties additional properties about the template
     * @param methodName calling method
     * @throws InvalidParameterException Template classification not available in the repositories
     */
    public void setTemplate(String              userId,
                            String              name,
                            String              description,
                            Map<String, String> additionalProperties,
                            String              methodName) throws InvalidParameterException
    {
        try
        {
            Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                  null,
                                                                                  null,
                                                                                  InstanceProvenanceType.LOCAL_COHORT,
                                                                                  userId,
                                                                                  OpenMetadataAPIMapper.TEMPLATE_CLASSIFICATION_TYPE_NAME,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  getTemplateProperties(name,
                                                                                                        description,
                                                                                                        additionalProperties,
                                                                                                        methodName));
            newClassifications.put(classification.getName(), classification);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.TEMPLATE_CLASSIFICATION_TYPE_NAME);
        }
    }


    /**
     * Return the template properties in an InstanceProperties object.
     *
     * @param name template name
     * @param description template description
     * @param additionalProperties additional properties about the template
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    private InstanceProperties getTemplateProperties(String              name,
                                                     String              description,
                                                     Map<String, String> additionalProperties,
                                                     String              methodName)
    {
        InstanceProperties properties = null;

        if (name != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.TEMPLATE_NAME_PROPERTY_NAME,
                                                                      name,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.TEMPLATE_DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (additionalProperties != null)
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         OpenMetadataAPIMapper.TEMPLATE_ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                         additionalProperties,
                                                                         methodName);
        }

        return properties;
    }


    /**
     * Set up the SecurityTags classification for this entity.
     * This method overrides an previously defined SecurityTags classification for this entity.
     *
     * @param userId calling user
     * @param securityLabels list of security labels
     * @param securityProperties map of name value pairs
     * @param methodName calling method
     * @throws InvalidParameterException security tags is not supported in the local repository, or any repository
     *                                   connected by an open metadata repository cohort
     */
    public void setSecurityTags(String              userId,
                                List<String>        securityLabels,
                                Map<String, Object> securityProperties,
                                String              methodName) throws InvalidParameterException
    {
        try
        {
            Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                  null,
                                                                                  null,
                                                                                  InstanceProvenanceType.LOCAL_COHORT,
                                                                                  userId,
                                                                                  OpenMetadataAPIMapper.SECURITY_TAG_CLASSIFICATION_TYPE_NAME,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  getSecurityTagProperties(securityLabels,
                                                                                                           securityProperties,
                                                                                                           methodName));
            newClassifications.put(classification.getName(), classification);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.SECURITY_TAG_CLASSIFICATION_TYPE_NAME);
        }
    }


    /**
     * Return the security tag properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @param securityLabels list of security labels
     * @param securityProperties map of name value pairs
     * @return InstanceProperties object
     */
    InstanceProperties getSecurityTagProperties(List<String>        securityLabels,
                                                Map<String, Object> securityProperties,
                                                String              methodName)
    {
        InstanceProperties properties = null;

        if (securityLabels != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           null,
                                                                           OpenMetadataAPIMapper.SECURITY_LABELS_PROPERTY_NAME,
                                                                           securityLabels,
                                                                           methodName);
        }

        if (securityProperties != null)
        {
            properties = repositoryHelper.addMapPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataAPIMapper.SECURITY_PROPERTIES_PROPERTY_NAME,
                                                                   securityProperties,
                                                                   methodName);
        }

        return properties;
    }


    /**
     * Return the qualified name so the handler can check for uniqueness.
     *
     * @return string name
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getInstanceProperties(String methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (qualifiedName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                      qualifiedName,
                                                                      methodName);
        }

        if (additionalProperties != null)
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         OpenMetadataAPIMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
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
    public InstanceProperties getNameInstanceProperties(String methodName)
    {
        InstanceProperties properties = null;

        if (qualifiedName != null)
        {
            String literalQualifiedName = repositoryHelper.getExactMatchRegex(qualifiedName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
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
    public InstanceProperties getQualifiedNameInstanceProperties(String methodName)
    {
        return this.getNameInstanceProperties(methodName);
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getSearchInstanceProperties(String methodName)
    {
        InstanceProperties properties = null;

        if (qualifiedName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                      qualifiedName,
                                                                      methodName);
        }

        return properties;
    }

}