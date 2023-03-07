/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.converters;


import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.*;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * GovernanceDefinitionConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from GovernanceDefinitionElement.
 */
public class GovernanceDefinitionConverter<B> extends GovernanceProgramOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public GovernanceDefinitionConverter(OMRSRepositoryHelper repositoryHelper,
                                         String               serviceName,
                                         String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }



    /**
     * Using the supplied entity, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the entity supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail primaryEntity,
                        String       methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewBean(entity)";

        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof GovernanceDefinitionElement)
            {
                GovernanceDefinitionElement bean = (GovernanceDefinitionElement) returnBean;

                if (primaryEntity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, primaryEntity.getClassifications(), methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                    String typeName = primaryEntity.getType().getTypeDefName();
                    GovernanceDefinitionProperties governanceDefinitionProperties;

                    if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.CERTIFICATION_TYPE_TYPE_NAME))
                    {
                        governanceDefinitionProperties = new CertificationTypeProperties();

                        ((CertificationTypeProperties) governanceDefinitionProperties).setDetails(this.removeDetails(instanceProperties));
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.LICENSE_TYPE_TYPE_NAME))
                    {
                        governanceDefinitionProperties = new LicenseTypeProperties();

                        ((LicenseTypeProperties) governanceDefinitionProperties).setDetails(this.removeDetails(instanceProperties));
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.SECURITY_GROUP_TYPE_NAME))
                    {
                        governanceDefinitionProperties = new SecurityGroupProperties();

                        ((SecurityGroupProperties) governanceDefinitionProperties).setDistinguishedName(this.removeDistinguishedName(instanceProperties));
                    }
                    else
                    {
                        governanceDefinitionProperties = new GovernanceDefinitionProperties();
                    }

                    governanceDefinitionProperties.setDocumentIdentifier(this.removeQualifiedName(instanceProperties));
                    governanceDefinitionProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    governanceDefinitionProperties.setTitle(this.removeTitle(instanceProperties));
                    governanceDefinitionProperties.setScope(this.removeScope(instanceProperties));
                    governanceDefinitionProperties.setDomainIdentifier(this.removeDomainIdentifier(instanceProperties));
                    governanceDefinitionProperties.setPriority(this.removePriority(instanceProperties));
                    governanceDefinitionProperties.setOutcomes(this.removeOutcomes(instanceProperties));
                    governanceDefinitionProperties.setResults(this.removeResults(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    governanceDefinitionProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    governanceDefinitionProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setProperties(governanceDefinitionProperties);
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewBean(entity, relationship)";
        B returnBean = this.getNewBean(beanClass, entity, methodName);

        if (returnBean instanceof GovernanceDefinitionElement)
        {
            GovernanceDefinitionElement bean = (GovernanceDefinitionElement) returnBean;

            bean.setRelatedElement(super.getRelatedElement(beanClass, entity, relationship, methodName));
        }

        return returnBean;
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or DataField bean which combine knowledge from the entity and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    @Override
    public B getNewComplexBean(Class<B>           beanClass,
                               EntityDetail       primaryEntity,
                               List<Relationship> relationships,
                               String             methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof GovernanceDefinitionElement)
            {
                GovernanceDefinitionElement bean = (GovernanceDefinitionElement) returnBean;

                if (primaryEntity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, primaryEntity.getClassifications(), methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                    String typeName = primaryEntity.getType().getTypeDefName();
                    GovernanceDefinitionProperties governanceDefinitionProperties;

                    if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.CERTIFICATION_TYPE_TYPE_NAME))
                    {
                        governanceDefinitionProperties = new CertificationTypeProperties();

                        ((CertificationTypeProperties) governanceDefinitionProperties).setDetails(this.removeDetails(instanceProperties));
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.LICENSE_TYPE_TYPE_NAME))
                    {
                        governanceDefinitionProperties = new LicenseTypeProperties();

                        ((LicenseTypeProperties) governanceDefinitionProperties).setDetails(this.removeDetails(instanceProperties));
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataAPIMapper.SECURITY_GROUP_TYPE_NAME))
                    {
                        governanceDefinitionProperties = new SecurityGroupProperties();

                        ((SecurityGroupProperties) governanceDefinitionProperties).setDistinguishedName(this.removeDistinguishedName(instanceProperties));
                    }
                    else
                    {
                        governanceDefinitionProperties = new GovernanceDefinitionProperties();
                    }

                    governanceDefinitionProperties.setDocumentIdentifier(this.removeQualifiedName(instanceProperties));
                    governanceDefinitionProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    governanceDefinitionProperties.setTitle(this.removeTitle(instanceProperties));
                    governanceDefinitionProperties.setScope(this.removeScope(instanceProperties));
                    governanceDefinitionProperties.setDomainIdentifier(this.removeDomainIdentifier(instanceProperties));
                    governanceDefinitionProperties.setPriority(this.removePriority(instanceProperties));
                    governanceDefinitionProperties.setOutcomes(this.removeOutcomes(instanceProperties));
                    governanceDefinitionProperties.setResults(this.removeResults(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    governanceDefinitionProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    governanceDefinitionProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setProperties(governanceDefinitionProperties);
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }
}
