/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OCFConverter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.integration.properties.CatalogTarget;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * CatalogTargetConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS) Relationship object into a
 * CatalogTarget bean.
 */
public class CatalogTargetConverter<B> extends OCFConverter<B>
{
    /**
     * Constructor captures the repository content needed to create the endpoint object.
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
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
                    bean.setCatalogTargetName(repositoryHelper.getStringProperty(serviceName,
                                                                                 OpenMetadataType.CATALOG_TARGET_NAME_PROPERTY_NAME,
                                                                                 relationship.getProperties(),
                                                                                 methodName));
                    bean.setMetadataSourceQualifiedName(repositoryHelper.getStringProperty(serviceName,
                                                                                           OpenMetadataType.METADATA_SOURCE_QUALIFIED_NAME_PROPERTY_NAME,
                                                                                           relationship.getProperties(),
                                                                                           methodName));
                    bean.setConfigurationProperties(repositoryHelper.getMapFromProperty(serviceName,
                                                                                        OpenMetadataType.CONFIGURATION_PROPERTIES_PROPERTY_NAME,
                                                                                        relationship.getProperties(),
                                                                                        methodName));

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
}
