/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.converters;

import org.odpi.openmetadata.accessservices.projectmanagement.metadataelements.RelatedElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContactMethodType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;



/**
 * ProjectManagementOMASConverter provides the generic methods for the Community Profile beans converters.  Generic classes
 * have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing a Community Profile bean.
 */
public class ProjectManagementOMASConverter<B> extends OpenMetadataAPIGenericConverter<B>
{
    long karmaPointPlateau = 0;


    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName name of this server
     */
    public ProjectManagementOMASConverter(OMRSRepositoryHelper   repositoryHelper,
                                          String                 serviceName,
                                          String                 serverName)
    {
        super (repositoryHelper, serviceName, serverName);
    }

    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     * @param karmaPointPlateau how many karma points to a plateau
     */
    public ProjectManagementOMASConverter(OMRSRepositoryHelper repositoryHelper,
                                          String               serviceName,
                                          String               serverName,
                                          int                  karmaPointPlateau)
    {
        this(repositoryHelper, serviceName, serverName);

        this.karmaPointPlateau = karmaPointPlateau;
    }


    /*===============================
     * Methods to fill out headers and enums
     */


    /**
     * Using the supplied instances, return a new instance of a relatedElement bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public RelatedElement getRelatedElement(Class<B>     beanClass,
                                            EntityDetail entity,
                                            Relationship relationship,
                                            String       methodName) throws PropertyServerException
    {
        RelatedElement  relatedElement = new RelatedElement();

        relatedElement.setRelationshipHeader(this.getMetadataElementHeader(beanClass, relationship, null, methodName));

        if (relationship != null)
        {
            InstanceProperties instanceProperties = new InstanceProperties(relationship.getProperties());

            RelationshipProperties relationshipProperties = new RelationshipProperties();

            relationshipProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());
            relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            relatedElement.setRelationshipProperties(relationshipProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.RELATIONSHIP_DEF, methodName);
        }


        if (entity != null)
        {
            ElementStub elementStub = this.getElementStub(beanClass, entity, methodName);

            relatedElement.setRelatedElement(elementStub);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }

        return relatedElement;
    }


    /**
     * Retrieve the ContactMethodType enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return ContactMethodType  enum value
     */
    ContactMethodType getContactMethodTypeFromProperties(InstanceProperties   properties)
    {
        final String methodName = "getContactMethodTypeFromProperties";

        ContactMethodType contactMethodType = ContactMethodType.OTHER;

        if (properties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName, OpenMetadataProperty.CONTACT_METHOD_TYPE.name, properties, methodName);

            switch (ordinal)
            {
                case 0:
                    contactMethodType = ContactMethodType.EMAIL;
                    break;

                case 1:
                    contactMethodType = ContactMethodType.PHONE;
                    break;

                case 2:
                    contactMethodType = ContactMethodType.CHAT;
                    break;

                case 3:
                    contactMethodType = ContactMethodType.PROFILE;
                    break;

                case 4:
                    contactMethodType = ContactMethodType.ACCOUNT;
                    break;

                case 99:
                    contactMethodType = ContactMethodType.OTHER;
                    break;
            }
        }

        return contactMethodType;
    }
}
