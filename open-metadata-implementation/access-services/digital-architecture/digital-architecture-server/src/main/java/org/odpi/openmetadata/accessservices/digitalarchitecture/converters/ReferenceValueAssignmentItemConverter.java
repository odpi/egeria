/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.converters;

import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.ReferenceValueAssignmentItemElement;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.ReferenceableElement;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * ReferenceValueAssignmentItemConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * Relationship and linked EntityDetail object into a bean that inherits from ReferenceValueAssignmentItemElement.
 */
public class ReferenceValueAssignmentItemConverter<B> extends DigitalArchitectureOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ReferenceValueAssignmentItemConverter(OMRSRepositoryHelper repositoryHelper,
                                                 String serviceName,
                                                 String serverName)
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
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof ReferenceValueAssignmentItemElement)
            {
                ReferenceValueAssignmentItemElement bean         = (ReferenceValueAssignmentItemElement) returnBean;
                ReferenceableElement                assignedItem = new ReferenceableElement();
                ReferenceableProperties             referenceableProperties = new ReferenceableProperties();

                if (entity != null)
                {
                    assignedItem.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));

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
                    referenceableProperties.setTypeName(assignedItem.getElementHeader().getType().getTypeName());
                    referenceableProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    assignedItem.setReferenceableProperties(referenceableProperties);
                    bean.setAssignedItem(assignedItem);

                    if (relationship != null)
                    {
                        instanceProperties = relationship.getProperties();

                        bean.setAttributeName(this.getAttributeName(instanceProperties));
                        bean.setConfidence(this.getConfidence(instanceProperties));
                        bean.setSteward(this.getSteward(instanceProperties));
                        bean.setStewardTypeName(this.getStewardTypeName(instanceProperties));
                        bean.setStewardPropertyName(this.getStewardPropertyName(instanceProperties));
                        bean.setNotes(this.getNotes(instanceProperties));
                    }
                    else
                    {
                        handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.RELATIONSHIP_DEF, methodName);
                    }
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
