/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Date;
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
     * @param repositoryHelper helper methods
     * @param serviceName      name of this OMAS
     * @param serverName       name of local server
     */
    public ReferenceableBuilder(OMRSRepositoryHelper repositoryHelper,
                                String serviceName,
                                String serverName)
    {
        super(OpenMetadataType.REFERENCEABLE.typeGUID,
              OpenMetadataType.REFERENCEABLE.typeName,
              repositoryHelper,
              serviceName,
              serverName);
    }


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
        super(OpenMetadataType.REFERENCEABLE.typeGUID,
              OpenMetadataType.REFERENCEABLE.typeName,
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
        super(OpenMetadataType.REFERENCEABLE.typeGUID,
              OpenMetadataType.REFERENCEABLE.typeName,
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
     * Set up the Ownership classification for this entity.
     * This method overrides a previously defined AssetOwnership classification for this entity.
     *
     * @param userId calling user
     * @param owner name of the owner
     * @param ownerTypeName type of element that owner comes from
     * @param ownerPropertyName name of property used to identify owner
     * @param methodName calling method
     * @throws InvalidParameterException Ownership is not supported in the local repository, or any repository
     *                                   connected by an open metadata repository cohort
     */
    protected void setOwnershipClassification(String userId,
                                              String owner,
                                              String ownerTypeName,
                                              String ownerPropertyName,
                                              String methodName) throws InvalidParameterException
    {
        if (owner != null)
        {
            try
            {
                Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                      null,
                                                                                      null,
                                                                                      InstanceProvenanceType.LOCAL_COHORT,
                                                                                      userId,
                                                                                      OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName,
                                                                                      typeName,
                                                                                      ClassificationOrigin.ASSIGNED,
                                                                                      null,
                                                                                      getOwnershipProperties(owner,
                                                                                                             ownerTypeName,
                                                                                                             ownerPropertyName,
                                                                                                             methodName));
                newClassifications.put(classification.getName(), classification);
            }
            catch (TypeErrorException error)
            {
                errorHandler.handleUnsupportedType(error, methodName, OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName);
            }
        }
    }


    /**
     * Return the bean properties describing the element's owner in an InstanceProperties object.
     *
     * @param owner name of the owner
     * @param ownerTypeName type of element that owner comes from
     * @param ownerPropertyName name of property used to identify owner
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getOwnershipProperties(String owner,
                                              String ownerTypeName,
                                              String ownerPropertyName,
                                              String methodName)
    {
        InstanceProperties properties = null;

        if (owner != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataProperty.OWNER.name,
                                                                      owner,
                                                                      methodName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataProperty.OWNER_TYPE_NAME.name,
                                                                      ownerTypeName,
                                                                      methodName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataProperty.OWNER_PROPERTY_NAME.name,
                                                                      ownerPropertyName,
                                                                      methodName);
        }

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Set up the Template classification.
     *
     * @param userId calling user
     * @param name template name
     * @param versionIdentifier version identifier for the template eg 1.0
     * @param description template description
     * @param additionalProperties additional properties about the template
     * @param methodName calling method
     * @throws InvalidParameterException Template classification not available in the repositories
     */
    public void setTemplate(String              userId,
                            String              name,
                            String              versionIdentifier,
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
                                                                                  OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  getTemplateProperties(name,
                                                                                                        versionIdentifier,
                                                                                                        description,
                                                                                                        additionalProperties,
                                                                                                        methodName));
            newClassifications.put(classification.getName(), classification);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName);
        }
    }


    /**
     * Return the template properties in an InstanceProperties object.
     *
     * @param name template name
     * @param versionIdentifier version identifier for the template eg 1.0
     * @param description template description
     * @param additionalProperties additional properties about the template
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getTemplateProperties(String              name,
                                             String              versionIdentifier,
                                             String              description,
                                             Map<String, String> additionalProperties,
                                             String              methodName)
    {
        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  OpenMetadataProperty.NAME.name,
                                                                  name,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.VERSION_IDENTIFIER.name,
                                                                  versionIdentifier,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                     additionalProperties,
                                                                     methodName);

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the template properties in an InstanceProperties object.
     *
     * @param defaultValue initial value of a data field value
     * @param sampleValues list of sample values
     * @param dataPattern regular expression describing the values in the data filed
     * @param namePattern regular expression describing the name of the data field
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getDataFieldValuesProperties(String              defaultValue,
                                                    List<String>        sampleValues,
                                                    List<String>        dataPattern,
                                                    List<String>        namePattern,
                                                    String              methodName)
    {
        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  OpenMetadataProperty.DEFAULT_VALUE.name,
                                                                  defaultValue,
                                                                  methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataProperty.SAMPLE_VALUES.name,
                                                                       sampleValues,
                                                                       methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataProperty.DATA_PATTERNS.name,
                                                                       dataPattern,
                                                                       methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataProperty.NAME_PATTERNS.name,
                                                                       namePattern,
                                                                       methodName);

        setEffectivityDates(properties);

        return properties;
    }



    /**
     * Set up the Memento classification.
     *
     * @param userId calling user
     * @param archiveDate timestamp that the archive either occurred or was detected
     * @param archiveUser name of user responsible for performing the archive (or detecting it)
     * @param archiveProcess process responsible for performing the archive (or detecting it)
     * @param archiveProperties additional properties to locate the archived asset/artifact (if known)
     * @param methodName calling method
     * @throws InvalidParameterException Template classification not available in the repositories
     */
    public void setMemento(String              userId,
                           Date                archiveDate,
                           String              archiveUser,
                           String              archiveProcess,
                           Map<String, String> archiveProperties,
                           String              methodName) throws InvalidParameterException
    {
        try
        {
            Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                  null,
                                                                                  null,
                                                                                  InstanceProvenanceType.LOCAL_COHORT,
                                                                                  userId,
                                                                                  OpenMetadataType.MEMENTO_CLASSIFICATION.typeName,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  getMementoProperties(archiveDate,
                                                                                                               archiveUser,
                                                                                                               archiveProcess,
                                                                                                               archiveProperties,
                                                                                                               methodName));
            newClassifications.put(classification.getName(), classification);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataType.MEMENTO_CLASSIFICATION.typeName);
        }
    }


    /**
     * Return the template properties in an InstanceProperties object.
     *
     * @param archiveDate timestamp that the archive either occurred or was detected
     * @param archiveUser name of user responsible for performing the archive (or detecting it)
     * @param archiveProcess process responsible for performing the archive (or detecting it)
     * @param archiveProperties additional properties to locate the archived asset/artifact (if known)
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getMementoProperties(Date                archiveDate,
                                                   String              archiveUser,
                                                   String              archiveProcess,
                                                   Map<String, String> archiveProperties,
                                                   String              methodName)
    {
        InstanceProperties properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                                   null,
                                                                                   OpenMetadataProperty.ARCHIVE_DATE.name,
                                                                                   archiveDate != null ? archiveDate : new Date(),
                                                                                   methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.ARCHIVE_USER.name,
                                                                  archiveUser,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.ARCHIVE_PROCESS.name,
                                                                  archiveProcess,
                                                                  methodName);
        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataProperty.ARCHIVE_PROPERTIES.name,
                                                                     archiveProperties,
                                                                     methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.ARCHIVE_SERVICE.name,
                                                                  serviceName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.ARCHIVE_METHOD.name,
                                                                  methodName,
                                                                  methodName);

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Set up the SecurityTags classification for this entity.
     * This method overrides a previously defined SecurityTags classification for this entity.
     *
     * @param userId calling user
     * @param securityLabels list of security labels
     * @param securityProperties map of name value pairs
     * @param accessGroups map from operation to list of security groups
     * @param methodName calling method
     * @throws InvalidParameterException security tags is not supported in the local repository, or any repository
     *                                   connected by an open metadata repository cohort
     */
    public void setSecurityTags(String                    userId,
                                List<String>              securityLabels,
                                Map<String, Object>       securityProperties,
                                Map<String, List<String>> accessGroups,
                                String                    methodName) throws InvalidParameterException
    {
        try
        {
            Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                  null,
                                                                                  null,
                                                                                  InstanceProvenanceType.LOCAL_COHORT,
                                                                                  userId,
                                                                                  OpenMetadataType.SECURITY_TAGS_CLASSIFICATION.typeName,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  getSecurityTagProperties(securityLabels,
                                                                                                           securityProperties,
                                                                                                           accessGroups,
                                                                                                           methodName));
            newClassifications.put(classification.getName(), classification);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataType.SECURITY_TAGS_CLASSIFICATION.typeName);
        }
    }


    /**
     * Return the security tag properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @param securityLabels list of security labels
     * @param securityProperties map of name value pairs
     * @param accessGroups map from operation to list of security groups
     * @return InstanceProperties object
     */
    InstanceProperties getSecurityTagProperties(List<String>              securityLabels,
                                                Map<String, Object>       securityProperties,
                                                Map<String, List<String>> accessGroups,
                                                String                    methodName)
    {
        InstanceProperties properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                                          null,
                                                                                          OpenMetadataProperty.SECURITY_LABELS.name,
                                                                                          securityLabels,
                                                                                          methodName);
        properties = repositoryHelper.addMapPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataProperty.SECURITY_PROPERTIES.name,
                                                               securityProperties,
                                                               methodName);

        properties = repositoryHelper.addStringArrayStringMapPropertyToInstance(serviceName,
                                                                                properties,
                                                                                OpenMetadataProperty.ACCESS_GROUPS.name,
                                                                                accessGroups,
                                                                                methodName);

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the business significance properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @param description description of why this is significant
     * @param scope scope of its business significance
     * @param businessCapabilityGUID unique identifier of the business capability that rates this as significant
     * @return InstanceProperties object
     */
    InstanceProperties getBusinessSignificanceProperties(String description,
                                                         String scope,
                                                         String businessCapabilityGUID,
                                                         String methodName)
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                                     description,
                                                                                     methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.SCOPE.name,
                                                                  scope,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.BUSINESS_CAPABILITY_GUID_PROPERTY_NAME,
                                                                  businessCapabilityGUID,
                                                                  methodName);

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the bean properties describing the data flow relationship.
     *
     * @param qualifiedName unique name of this relationship
     * @param description description of this relationship
     * @param formula logic describing any filtering of data
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getDataFlowProperties(String qualifiedName,
                                             String description,
                                             String formula,
                                             String methodName)
    {
        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                  qualifiedName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.FORMULA.name,
                                                                  formula,
                                                                  methodName);

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the bean properties describing the control flow relationship.
     *
     * @param qualifiedName unique name of this relationship
     * @param description description of this relationship
     * @param guard logic describing what must be true for control to pass down this control flow
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getControlFlowProperties(String qualifiedName,
                                                String description,
                                                String guard,
                                                String methodName)
    {
        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                  qualifiedName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.GUARD_PROPERTY_NAME,
                                                                  guard,
                                                                  methodName);

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the bean properties describing the process call relationship.
     *
     * @param qualifiedName unique name of this relationship
     * @param description description of this relationship
     * @param formula logic describing any filtering of data on the call
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getProcessCallProperties(String qualifiedName,
                                                String description,
                                                String formula,
                                                String methodName)
    {
        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                  qualifiedName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.FORMULA.name,
                                                                  formula,
                                                                  methodName);

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the bean properties describing the lineage mapping relationship.
     *
     * @param qualifiedName unique name of this relationship
     * @param description description of this relationship
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getLineageMappingProperties(String qualifiedName,
                                                   String description,
                                                   String methodName)
    {
        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                  qualifiedName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        setEffectivityDates(properties);

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
    @Override
    public InstanceProperties getInstanceProperties(String methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (qualifiedName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                      qualifiedName,
                                                                      methodName);
        }

        if (additionalProperties != null)
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                         additionalProperties,
                                                                         methodName);
        }

        return properties;
    }
}
