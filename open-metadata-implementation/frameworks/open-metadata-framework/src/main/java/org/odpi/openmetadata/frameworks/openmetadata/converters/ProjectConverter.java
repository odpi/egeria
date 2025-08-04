/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ProjectElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;


/**
 * ProjectConverter generates a ProjectElement from a Project entity
 */
public class ProjectConverter<B> extends OpenMetadataConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ProjectConverter(PropertyHelper propertyHelper,
                            String         serviceName,
                            String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or To Do bean which combine knowledge from the element and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryElement element that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewComplexBean(Class<B>                     beanClass,
                               OpenMetadataElement          primaryElement,
                               List<RelatedMetadataElement> relationships,
                               String                       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof ProjectElement bean)
            {
                bean.setElementHeader(super.getMetadataElementHeader(beanClass, primaryElement, methodName));

                if (primaryElement == null)
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }
                else
                {
                    bean.setProperties(this.getProjectProperties(primaryElement));
                    bean.setResourceList(this.getResourceList(relationships));
                    bean.setProjectManagers(this.getProjectManagers(relationships));
                    bean.setProjectTeam(this.getProjectTeam(relationships));
                    bean.setExternalReferences(super.getExternalReferences(relationships));
                    bean.setOtherRelatedElements(super.getOtherRelatedElements(relationships,
                                                                               Arrays.asList(OpenMetadataType.PROJECT_TEAM_RELATIONSHIP.typeName,
                                                                                             OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                                                             OpenMetadataType.PROJECT_MANAGEMENT_RELATIONSHIP.typeName,
                                                                                             OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName)));
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


    /**
     * Retrieve the project properties from the retrieved element.
     *
     * @param primaryElement element
     *
     * @return project properties
     */
    protected ProjectProperties getProjectProperties(OpenMetadataElement primaryElement)
    {
        if (primaryElement.getElementProperties() != null)
        {
            ElementProperties elementProperties = new ElementProperties(primaryElement.getElementProperties());
            ProjectProperties projectProperties = new ProjectProperties();

            projectProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
            projectProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
            projectProperties.setIdentifier(this.removeIdentifier(elementProperties));
            projectProperties.setDisplayName(this.removeDisplayName(elementProperties));
            projectProperties.setDescription(this.removeDescription(elementProperties));
            projectProperties.setProjectStatus(this.removeProjectStatus(elementProperties));
            projectProperties.setStartDate(this.removeStartDate(elementProperties));
            projectProperties.setPlannedEndDate(this.removePlannedEndDate(elementProperties));
            projectProperties.setEffectiveFrom(primaryElement.getEffectiveFromTime());
            projectProperties.setEffectiveTo(primaryElement.getEffectiveToTime());

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            projectProperties.setTypeName(primaryElement.getType().getTypeName());
            projectProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

            return projectProperties;
        }

        return null;
    }


    /**
     * Summarize the elements linked off of the project in the resource list.
     *
     * @param relatedMetadataElements elements to summarize
     * @return list or null
     * @throws PropertyServerException problem in converter
     */
    protected List<RelatedMetadataElementSummary> getResourceList(List<RelatedMetadataElement> relatedMetadataElements) throws PropertyServerException
    {
        return super.getRelatedElements(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName, relatedMetadataElements);
    }


    /**
     * Summarize the elements linked off of the project in the project management list.
     *
     * @param relatedMetadataElements elements to summarize
     * @return list or null
     * @throws PropertyServerException problem in converter
     */
    protected List<RelatedMetadataElementSummary> getProjectManagers(List<RelatedMetadataElement> relatedMetadataElements) throws PropertyServerException
    {
        return super.getRelatedElements(OpenMetadataType.PROJECT_MANAGEMENT_RELATIONSHIP.typeName, relatedMetadataElements);
    }


    /**
     * Summarize the elements linked off of the project in the project team list.
     *
     * @param relatedMetadataElements elements to summarize
     * @return list or null
     * @throws PropertyServerException problem in converter
     */
    protected List<RelatedMetadataElementSummary> getProjectTeam(List<RelatedMetadataElement> relatedMetadataElements) throws PropertyServerException
    {
        return super.getRelatedElements(OpenMetadataType.PROJECT_TEAM_RELATIONSHIP.typeName, relatedMetadataElements);
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or To Do bean which combine knowledge from the element and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryElement element that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewComplexBean(Class<B>                     beanClass,
                               RelatedMetadataElement       primaryElement,
                               List<RelatedMetadataElement> relationships,
                               String                       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof ProjectElement bean)
            {
                ProjectProperties    projectProperties    = new ProjectProperties();
                OpenMetadataElement  openMetadataElement  = primaryElement.getElement();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, openMetadataElement, methodName));

                ElementProperties elementProperties;

                /*
                 * The initial set of values come from the openMetadataElement.
                 */
                if (openMetadataElement != null)
                {
                    elementProperties = new ElementProperties(openMetadataElement.getElementProperties());

                    projectProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
                    projectProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
                    projectProperties.setIdentifier(this.removeIdentifier(elementProperties));
                    projectProperties.setDisplayName(this.removeDisplayName(elementProperties));
                    projectProperties.setDescription(this.removeDescription(elementProperties));
                    projectProperties.setProjectStatus(this.removeProjectStatus(elementProperties));
                    projectProperties.setProjectHealth(this.removeProjectHealth(elementProperties));
                    projectProperties.setProjectPhase(this.removeProjectPhase(elementProperties));
                    projectProperties.setPriority(this.removeIntPriority(elementProperties));
                    projectProperties.setStartDate(this.removeStartDate(elementProperties));
                    projectProperties.setPlannedEndDate(this.removePlannedEndDate(elementProperties));
                    projectProperties.setEffectiveFrom(openMetadataElement.getEffectiveFromTime());
                    projectProperties.setEffectiveTo(openMetadataElement.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    projectProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    projectProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }

                if (relationships != null)
                {
                    bean.setResourceList(this.getResourceList(relationships));
                    bean.setProjectManagers(this.getProjectManagers(relationships));
                    bean.setProjectTeam(this.getProjectTeam(relationships));
                }

                bean.setProperties(projectProperties);
                bean.setRelatedBy(super.getRelatedBy(beanClass, primaryElement, methodName));
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
