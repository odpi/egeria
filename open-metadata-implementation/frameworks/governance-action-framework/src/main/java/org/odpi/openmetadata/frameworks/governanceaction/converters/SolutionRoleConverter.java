/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.converters;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SolutionRoleElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorRoleProperties;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * SolutionRoleConverter generates a SolutionRoleElement from an "ActorRole" entity and a list of SolutionComponentActor
 * relationships.
 */
public class SolutionRoleConverter<B> extends OpenMetadataConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public SolutionRoleConverter(PropertyHelper propertyHelper,
                                 String         serviceName,
                                 String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean.
     *
     * @param beanClass name of the class to create
     * @param primaryElement element that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
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

            if (returnBean instanceof SolutionRoleElement bean)
            {
                ActorRoleProperties roleProperties = new ActorRoleProperties();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, primaryElement, methodName));

                ElementProperties elementProperties;

                /*
                 * The initial set of values come from the metadata element.
                 */
                if (primaryElement != null)
                {
                    elementProperties = new ElementProperties(primaryElement.getElementProperties());

                    roleProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
                    roleProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
                    roleProperties.setRoleId(this.removeIdentifier(elementProperties));
                    roleProperties.setScope(this.removeScope(elementProperties));
                    roleProperties.setTitle(this.removeName(elementProperties));
                    roleProperties.setDescription(this.removeDescription(elementProperties));
                    roleProperties.setDomainIdentifier(this.removeDomainIdentifier(elementProperties));
                    roleProperties.setEffectiveFrom(primaryElement.getEffectiveFromTime());
                    roleProperties.setEffectiveTo(primaryElement.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    roleProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    roleProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

                    bean.setProperties(roleProperties);

                    if (relationships != null)
                    {
                        List<RelatedMetadataElementSummary> relatedMetadataElementSummaries = new ArrayList<>();

                        for (RelatedMetadataElement relatedMetadataElement : relationships)
                        {
                            if (relatedMetadataElement != null)
                            {
                                relatedMetadataElementSummaries.add(super.getRelatedElementSummary(beanClass, relatedMetadataElement, methodName));
                            }
                        }

                        bean.setSolutionComponents(relatedMetadataElementSummaries);
                    }
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }

                bean.setProperties(roleProperties);
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
