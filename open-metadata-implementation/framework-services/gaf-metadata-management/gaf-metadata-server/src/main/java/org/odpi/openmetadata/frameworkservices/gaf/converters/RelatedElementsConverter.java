/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.converters;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * gafPropertiesConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a gafProperties bean.
 */
public class RelatedElementsConverter<B> extends OpenMetadataStoreConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName this server
     */
    public RelatedElementsConverter(OMRSRepositoryHelper repositoryHelper,
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

            if (returnBean instanceof OpenMetadataRelationship)
            {
                OpenMetadataRelationship bean = (OpenMetadataRelationship) returnBean;

                fillElementControlHeader(bean, relationship);

                bean.setRelationshipGUID(relationship.getGUID());
                bean.setRelationshipType(super.getElementType(relationship));

                InstanceProperties instanceProperties = relationship.getProperties();

                if (instanceProperties != null)
                {
                    bean.setEffectiveFromTime(instanceProperties.getEffectiveFromTime());
                    bean.setEffectiveToTime(instanceProperties.getEffectiveToTime());
                    bean.setRelationshipProperties(mapElementProperties(instanceProperties));
                }

                EntityProxy entityProxy = relationship.getEntityOneProxy();
                ElementStub elementStub = new ElementStub();

                bean.setElementGUIDAtEnd1(entityProxy.getGUID());
                fillElementControlHeader(elementStub, entityProxy);
                elementStub.setGUID(entityProxy.getGUID());
                elementStub.setUniqueName(getQualifiedName(entityProxy.getUniqueProperties()));
                bean.setElementAtEnd1(elementStub);

                entityProxy = relationship.getEntityTwoProxy();
                elementStub = new ElementStub();

                bean.setElementGUIDAtEnd2(entityProxy.getGUID());
                fillElementControlHeader(elementStub, entityProxy);
                elementStub.setGUID(entityProxy.getGUID());
                elementStub.setUniqueName(getQualifiedName(entityProxy.getUniqueProperties()));
                bean.setElementAtEnd2(elementStub);

                if (repositoryHelper.getTypeDefByName(serviceName, relationship.getType().getTypeDefName()) instanceof RelationshipDef relationshipDef)
                {
                    bean.setLabelAtEnd1(relationshipDef.getEndDef1().getAttributeName());
                    bean.setLabelAtEnd2(relationshipDef.getEndDef2().getAttributeName());
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
