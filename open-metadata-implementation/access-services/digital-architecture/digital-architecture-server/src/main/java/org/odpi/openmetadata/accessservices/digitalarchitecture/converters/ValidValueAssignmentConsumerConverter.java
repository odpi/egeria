/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.converters;

import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.MetadataElement;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.ReferenceableElement;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.ValidValueAssignmentConsumerElement;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * ValidValueAssignmentConsumerConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * Relationship and linked EntityDetail object into a bean that inherits from ValidValueAssignmentConsumerElement.
 */
public class ValidValueAssignmentConsumerConverter<B> extends DigitalArchitectureOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ValidValueAssignmentConsumerConverter(OMRSRepositoryHelper repositoryHelper,
                                                 String               serviceName,
                                                 String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }



    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that have
     * contain a combination of the properties from an entity and a that os a connected relationship.
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
            B returnBean = beanClass.newInstance();

            if (returnBean instanceof ValidValueAssignmentConsumerElement)
            {
                ValidValueAssignmentConsumerElement bean = (ValidValueAssignmentConsumerElement) returnBean;
                ReferenceableElement                consumer = new ReferenceableElement();
                ReferenceableProperties             referenceableProperties = new ReferenceableProperties();

                consumer.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));

                /*
                 * The initial set of values come from the entity.
                 */
                InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                referenceableProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                referenceableProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                referenceableProperties.setTypeName(consumer.getElementHeader().getType().getTypeName());
                referenceableProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                consumer.setReferenceableProperties(referenceableProperties);
                bean.setConsumer(consumer);

                if (relationship != null)
                {
                    instanceProperties = relationship.getProperties();

                    bean.setStrictRequirement(this.getStrictRequirement(instanceProperties));
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.RELATIONSHIP_DEF, methodName);
                }
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }
    /**
     * Extract the properties from the entity.  Each concrete DataManager OMAS converter implements this method.
     * The top level fills in the header
     *
     * @param metadataElement output bean
     * @param entity entity containing the properties
     * @param relationship optional relationship containing the properties
     */
    void updateSimpleMetadataElement(MetadataElement metadataElement, EntityDetail entity, Relationship relationship)
    {

        if (metadataElement instanceof ValidValueAssignmentConsumerElement)
        {

        }
    }
}
