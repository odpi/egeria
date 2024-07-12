/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.client.converters;

import org.odpi.openmetadata.accessservices.projectmanagement.metadataelements.ProjectTeamMember;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectTeamProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * TeamMemberConverter generates a CollectionMember bean from a RelatedMetadataElement.
 */
public class TeamMemberConverter<B> extends ProjectManagementConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public TeamMemberConverter(PropertyHelper propertyHelper,
                               String         serviceName,
                               String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Using the supplied openMetadataElement, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param relatedMetadataElement the properties of an open metadata element plus details of the relationship used to navigate to it
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>               beanClass,
                        RelatedMetadataElement relatedMetadataElement,
                        String                 methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof ProjectTeamMember bean)
            {
                bean.setElementHeader(super.getMetadataElementHeader(beanClass,
                                                                     relatedMetadataElement,
                                                                     relatedMetadataElement.getRelationshipGUID(),
                                                                     null,
                                                                     methodName));

                ElementProperties elementProperties;

                /*
                 * The initial set of values come from the openMetadataElement.
                 */
                if (relatedMetadataElement.getRelationshipProperties() != null)
                {
                    ProjectTeamProperties projectTeamProperties = new ProjectTeamProperties();

                    elementProperties = new ElementProperties(relatedMetadataElement.getRelationshipProperties());

                    projectTeamProperties.setTeamRole(this.removeTeamRole(elementProperties));
                    projectTeamProperties.setEffectiveFrom(relatedMetadataElement.getEffectiveFromTime());
                    projectTeamProperties.setEffectiveTo(relatedMetadataElement.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    projectTeamProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

                    bean.setProjectTeamProperties(projectTeamProperties);
                }

                bean.setMember(super.getElementStub(beanClass, relatedMetadataElement.getElement(), methodName));
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
