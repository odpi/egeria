/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ActorProfileElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ITProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.TeamProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.lang.reflect.InvocationTargetException;


/**
 * ActorProfileConverter provides common methods for transferring relevant properties from an Open Metadata Element
 * object into a bean that inherits from ActorProfileElement.
 */
public class SchemaTypeConverter<B> extends AttributedElementConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public SchemaTypeConverter(PropertyHelper propertyHelper,
                               String         serviceName,
                               String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Uses the type of the entity to determine the type of bean to use for the properties.
     *
     * @param beanClass element bean class
     * @param openMetadataElement element retrieved
     * @param methodName calling method
     * @return properties
     * @throws PropertyServerException problem in conversion
     */
    protected ActorProfileProperties getActorProfileProperties(Class<B>            beanClass,
                                                               OpenMetadataElement openMetadataElement,
                                                               String              methodName) throws PropertyServerException
    {
        if (openMetadataElement != null)
        {
            ActorProfileProperties actorProfileProperties;

            /*
             * The initial set of values come from the entity.
             */
            ElementProperties elementProperties = new ElementProperties(openMetadataElement.getElementProperties());

            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.IT_PROFILE.typeName))
            {
                actorProfileProperties = new ITProfileProperties();
            }
            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TEAM.typeName))
            {
                actorProfileProperties = new TeamProfileProperties();

                ((TeamProfileProperties) actorProfileProperties).setIdentifier(this.removeIdentifier(elementProperties));
                ((TeamProfileProperties) actorProfileProperties).setTeamType(this.removeTeamType(elementProperties));
            }
            else if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.PERSON.typeName))
            {
                actorProfileProperties = new PersonProfileProperties();

                ((PersonProfileProperties) actorProfileProperties).setCourtesyTitle(this.removeCourtesyTitle(elementProperties));
                ((PersonProfileProperties) actorProfileProperties).setInitials(this.removeInitials(elementProperties));
                ((PersonProfileProperties) actorProfileProperties).setGivenNames(this.removeGivenNames(elementProperties));
                ((PersonProfileProperties) actorProfileProperties).setSurname(this.removeSurname(elementProperties));
                ((PersonProfileProperties) actorProfileProperties).setFullName(this.removeFullName(elementProperties));
                ((PersonProfileProperties) actorProfileProperties).setPronouns(this.removePronouns(elementProperties));
                ((PersonProfileProperties) actorProfileProperties).setJobTitle(this.removeJobTitle(elementProperties));
                ((PersonProfileProperties) actorProfileProperties).setEmployeeNumber(this.removeEmployeeNumber(elementProperties));
                ((PersonProfileProperties) actorProfileProperties).setEmployeeType(this.removeEmployeeType(elementProperties));
                ((PersonProfileProperties) actorProfileProperties).setPreferredLanguage(this.removePreferredLanguage(elementProperties));
                ((PersonProfileProperties) actorProfileProperties).setResidentCountry(this.removeResidentCountry(elementProperties));
                ((PersonProfileProperties) actorProfileProperties).setTimeZone(this.removeTimeZone(elementProperties));
                ((PersonProfileProperties) actorProfileProperties).setIsPublic(this.removeIsPublic(elementProperties));
            }
            else
            {
                actorProfileProperties = new ActorProfileProperties();
            }

            /*
             * These are the standard properties for an actor profile.
             */
            actorProfileProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
            actorProfileProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
            actorProfileProperties.setKnownName(this.removeName(elementProperties));
            actorProfileProperties.setDescription(this.removeDescription(elementProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            actorProfileProperties.setTypeName(openMetadataElement.getType().getTypeName());
            actorProfileProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

            return actorProfileProperties;
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
        }

        return null;
    }


    /**
     * Using the supplied entity, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param openMetadataElement openMetadataElement containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the entity supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>            beanClass,
                        OpenMetadataElement openMetadataElement,
                        String              methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof ActorProfileElement bean)
            {
                bean.setElementHeader(super.getMetadataElementHeader(beanClass, openMetadataElement, methodName));
                bean.setProfileProperties(getActorProfileProperties(beanClass, openMetadataElement, methodName));
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
