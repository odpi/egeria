/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.CollectionElement;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.ElementStub;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.CollectionOrder;
import org.odpi.openmetadata.accessservices.communityprofile.properties.CollectionProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.RelationshipProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


/**
 * RelatedElementConverter generates a RelatedElement from a relationship and attached entity
 */
public class RelatedElementConverter<B> extends CommunityProfileOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public RelatedElementConverter(OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
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
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        try
        {
            return (B)this.getRelatedElement(beanClass, entity, relationship, methodName);
        }
        catch (ClassCastException  error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }
}
