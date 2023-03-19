/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.NoteLogEntryElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.NoteLogEntryProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * NoteLogEntryConverter generates a NoteLogEntryForumContribution from an NoteEntry entity.
 */
public class NoteLogEntryConverter<B> extends CommunityProfileOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public NoteLogEntryConverter(OMRSRepositoryHelper repositoryHelper,
                                 String               serviceName,
                                 String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied entity, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
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
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof NoteLogEntryElement)
            {
                NoteLogEntryElement    bean               = (NoteLogEntryElement) returnBean;
                NoteLogEntryProperties logEntryProperties = new NoteLogEntryProperties();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                InstanceProperties instanceProperties;

                /*
                 * The initial set of values come from the entity.
                 */
                if (entity != null)
                {
                    instanceProperties = new InstanceProperties(entity.getProperties());

                    logEntryProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    logEntryProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    logEntryProperties.setTitle(this.removeTitle(instanceProperties));
                    logEntryProperties.setText(this.removeText(instanceProperties));

                    logEntryProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
                    logEntryProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());

                    List<String> contributors = new ArrayList<>();

                    contributors.add(entity.getCreatedBy());

                    if ((entity.getUpdatedBy() != null) && (! entity.getUpdatedBy().equals(entity.getCreatedBy())))
                    {
                        contributors.add(entity.getUpdatedBy());
                    }

                    if (entity.getMaintainedBy() != null)
                    {
                        for (String maintainer : entity.getMaintainedBy())
                        {
                            if ((maintainer != null) && (! maintainer.equals(entity.getCreatedBy())))
                            {
                                contributors.add(maintainer);
                            }
                        }
                    }

                    logEntryProperties.setContributors(contributors);

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    logEntryProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    logEntryProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }

                bean.setProperties(logEntryProperties);
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
    @SuppressWarnings(value = "unused")
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        return this.getNewBean(beanClass, entity, methodName);
    }
}
