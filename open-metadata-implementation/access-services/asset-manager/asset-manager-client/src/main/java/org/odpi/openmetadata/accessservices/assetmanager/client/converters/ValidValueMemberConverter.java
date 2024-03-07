/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.client.converters;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ValidValueElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ValidValueMember;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ValidValueMembershipProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ValidValueProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * ValidValueMemberConverter generates a ValidValueMemberElement bean from a RelatedMetadataElement.
 */
public class ValidValueMemberConverter<B> extends AssetManagerConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ValidValueMemberConverter(PropertyHelper propertyHelper,
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

            if (returnBean instanceof ValidValueMember bean)
            {
                bean.setRelationshipHeader(super.getMetadataElementHeader(beanClass,
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
                    ValidValueMembershipProperties validValueMembershipProperties = new ValidValueMembershipProperties();

                    elementProperties = new ElementProperties(relatedMetadataElement.getRelationshipProperties());

                    validValueMembershipProperties.setDefaultValue(this.removeIsDefaultValue(elementProperties));
                    validValueMembershipProperties.setEffectiveFrom(relatedMetadataElement.getEffectiveFromTime());
                    validValueMembershipProperties.setEffectiveTo(relatedMetadataElement.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    validValueMembershipProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

                    bean.setRelationshipProperties(validValueMembershipProperties);
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }

                if (relatedMetadataElement.getElement() != null)
                {
                    ValidValueElement validValueElement = new ValidValueElement();

                    validValueElement.setElementHeader(super.getMetadataElementHeader(beanClass,
                                                                                      relatedMetadataElement.getElement(),
                                                                                      relatedMetadataElement.getElement().getElementGUID(),
                                                                                      relatedMetadataElement.getElement().getClassifications(),
                                                                                      methodName));

                    ValidValueProperties validValueProperties = new ValidValueProperties();

                    elementProperties = new ElementProperties(relatedMetadataElement.getElement().getElementProperties());

                    validValueProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
                    validValueProperties.setDisplayName(this.removeName(elementProperties));
                    validValueProperties.setDescription(this.removeDescription(elementProperties));
                    validValueProperties.setUsage(this.removeUsage(elementProperties));
                    validValueProperties.setScope(this.removeScope(elementProperties));
                    validValueProperties.setIsDeprecated(this.removeIsDeprecated(elementProperties));
                    validValueProperties.setPreferredValue(this.removePreferredValue(elementProperties));
                    validValueProperties.setDataType(this.removeDataType(elementProperties));
                    validValueProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    validValueProperties.setTypeName(relatedMetadataElement.getElement().getType().getTypeName());
                    validValueProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

                    validValueProperties.setEffectiveFrom(relatedMetadataElement.getElement().getEffectiveFromTime());
                    validValueProperties.setEffectiveTo(relatedMetadataElement.getElement().getEffectiveToTime());

                    validValueElement.setValidValueProperties(validValueProperties);

                    bean.setValidValueElement(validValueElement);
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
