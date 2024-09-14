/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OMFConverter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * CatalogTargetConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS) Relationship object into a
 * CatalogTarget bean.
 */
public class CatalogTargetConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor captures the repository content needed to create the endpoint object.
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName controlling server name
     */
    public CatalogTargetConverter(OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that
     * contain a combination of the properties from an relationship and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewBean(Class<B>     beanClass,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            if (relationship != null)
            {
                B returnBean = beanClass.getDeclaredConstructor().newInstance();

                if (returnBean instanceof CatalogTarget bean)
                {
                    bean.setRelationshipGUID(relationship.getGUID());
                    bean.setRelationshipVersions(super.getElementVersions(relationship));
                    bean.setCatalogTargetName(repositoryHelper.getStringProperty(serviceName,
                                                                                 OpenMetadataType.CATALOG_TARGET_NAME_PROPERTY_NAME,
                                                                                 relationship.getProperties(),
                                                                                 methodName));
                    bean.setMetadataSourceQualifiedName(repositoryHelper.getStringProperty(serviceName,
                                                                                           OpenMetadataType.METADATA_SOURCE_QUALIFIED_NAME_PROPERTY_NAME,
                                                                                           relationship.getProperties(),
                                                                                           methodName));
                    bean.setConnectionName(repositoryHelper.getStringProperty(serviceName,
                                                                              OpenMetadataType.CONNECTION_NAME_PROPERTY_NAME,
                                                                              relationship.getProperties(),
                                                                              methodName));
                    bean.setTemplateProperties(repositoryHelper.getStringMapFromProperty(serviceName,
                                                                                         OpenMetadataType.TEMPLATES_PROPERTY_NAME,
                                                                                         relationship.getProperties(),
                                                                                         methodName));
                    bean.setConfigurationProperties(repositoryHelper.getMapFromProperty(serviceName,
                                                                                        OpenMetadataType.CONFIGURATION_PROPERTIES_PROPERTY_NAME,
                                                                                        relationship.getProperties(),
                                                                                        methodName));
                    bean.setPermittedSynchronization(getPermittedSynchronization(relationship.getProperties()));

                    bean.setDeleteMethod(getDeleteMethod(relationship.getProperties()));

                    bean.setCatalogTargetElement(super.getElementStub(beanClass, relationship.getEntityTwoProxy(), methodName));

                    return returnBean;
                }
            }

            return null;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return list of new instances of the bean.
     *
     * @param beanClass name of the class to create
     * @param relationships list of relationships containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public List<B> getNewBeans(Class<B>           beanClass,
                               List<Relationship> relationships,
                               String             methodName) throws PropertyServerException
    {
        if (relationships != null)
        {
            List<B> beans = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    B bean = getNewBean(beanClass, relationship, methodName);

                    if (bean != null)
                    {
                        beans.add(bean);
                    }
                }
            }

            if (! beans.isEmpty())
            {
                return beans;
            }
        }

        return null;
    }




    /**
     * Extract the permittedSynchronization property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return PermittedSynchronization enum
     */
    PermittedSynchronization getPermittedSynchronization(InstanceProperties instanceProperties)
    {
        final String methodName = "getPermittedSynchronization";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.getEnumPropertyOrdinal(serviceName,
                                                                  OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name,
                                                                  instanceProperties,
                                                                  methodName);

            for (PermittedSynchronization permittedSynchronization : PermittedSynchronization.values())
            {
                if (permittedSynchronization.getOrdinal() == ordinal)
                {
                    return permittedSynchronization;
                }
            }
        }

        return PermittedSynchronization.BOTH_DIRECTIONS;
    }




    /**
     * Extract the deleteMethod property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return PermittedSynchronization enum
     */
    DeleteMethod getDeleteMethod(InstanceProperties instanceProperties)
    {
        final String methodName = "getDeleteMethod";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.getEnumPropertyOrdinal(serviceName,
                                                                  OpenMetadataProperty.DELETE_METHOD.name,
                                                                  instanceProperties,
                                                                  methodName);

            for (DeleteMethod deleteMethod : DeleteMethod.values())
            {
                if (deleteMethod.getOrdinal() == ordinal)
                {
                    return deleteMethod;
                }
            }
        }

        return DeleteMethod.ARCHIVE;
    }
}
