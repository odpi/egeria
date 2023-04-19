/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.converters;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.FeedbackTargetElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;

/**
 * GlossaryTermConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a GlossaryTermElement bean.
 */
public class GlossaryTermConverter<B> extends AssetManagerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public GlossaryTermConverter(OMRSRepositoryHelper repositoryHelper,
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
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        String       methodName) throws PropertyServerException
    {
        return getNewBean(beanClass, entity, null, methodName);
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
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof GlossaryTermElement)
            {
                GlossaryTermElement bean = (GlossaryTermElement) returnBean;

                if (entity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));
                    GlossaryTermProperties glossaryTermProperties = new GlossaryTermProperties();

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    glossaryTermProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    glossaryTermProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    glossaryTermProperties.setDisplayName(this.removeDisplayName(instanceProperties));
                    glossaryTermProperties.setDescription(this.removeDescription(instanceProperties));
                    glossaryTermProperties.setAbbreviation(this.removeAbbreviation(instanceProperties));
                    glossaryTermProperties.setUsage(this.removeUsage(instanceProperties));
                    glossaryTermProperties.setExamples(this.removeExamples(instanceProperties));
                    glossaryTermProperties.setSummary(this.removeSummary(instanceProperties));
                    glossaryTermProperties.setPublishVersionIdentifier(this.removePublishVersionIdentifier(instanceProperties));


                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    glossaryTermProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    glossaryTermProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setGlossaryTermProperties(glossaryTermProperties);

                    if (relationship != null)
                    {
                        RelatedElement relatedElement = new RelatedElement();

                        relatedElement.setRelationshipHeader(super.getMetadataElementHeader(beanClass, relationship, null, methodName));
                        relatedElement.setRelatedElement(super.getElementStub(beanClass, relationship.getEntityOneProxy(), methodName));

                        if (relationship.getProperties() != null)
                        {
                            instanceProperties = new InstanceProperties(relationship.getProperties());

                            RelationshipProperties relationshipProperties = new RelationshipProperties();
                            relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));
                            relationshipProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
                            relationshipProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());

                            relatedElement.setRelationshipProperties(relationshipProperties);
                        }

                        bean.setRelatedElement(relatedElement);
                    }
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
