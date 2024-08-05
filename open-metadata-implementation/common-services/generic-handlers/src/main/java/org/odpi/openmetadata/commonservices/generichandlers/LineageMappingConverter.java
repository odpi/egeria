/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.LineageMappingElement;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * LineageMappingConverter provides common methods for transferring relevant header from an Open Metadata Repository Services (OMRS)
 * Relationship object into a LineageMappingElement bean.
 */
public class LineageMappingConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public LineageMappingConverter(OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied relationship, return a new instance of the bean.  It is used for beans that
     * represent a simple relationship between two entities.
     *
     * @param beanClass name of the class to create
     * @param relationship relationship linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewRelationshipBean(Class<B>     beanClass,
                                    Relationship relationship,
                                    String       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof LineageMappingElement bean)
            {
                if (relationship != null)
                {
                    bean.setLineageMappingHeader(super.getMetadataElementHeader(beanClass, relationship, null, methodName));

                    EntityProxy entityProxy = relationship.getEntityOneProxy();
                    if (entityProxy != null)
                    {
                        bean.setSourceElement(super.getMetadataElementHeader(beanClass, entityProxy, entityProxy.getClassifications(), methodName));
                    }

                    entityProxy = relationship.getEntityTwoProxy();
                    if (entityProxy != null)
                    {
                        bean.setTargetElement(super.getMetadataElementHeader(beanClass, entityProxy, entityProxy.getClassifications(), methodName));
                    }
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.RELATIONSHIP_DEF, methodName);
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
