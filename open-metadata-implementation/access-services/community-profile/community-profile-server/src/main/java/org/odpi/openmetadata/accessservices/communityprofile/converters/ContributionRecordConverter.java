/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.metadataelement.ContributionRecordElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ContributionRecord;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * ContributionRecordConverter generates a ContributionRecordProperties bean from a ContributionRecord entity.
 */
public class ContributionRecordConverter<B> extends CommunityProfileOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     * @param karmaPointPlateau how many karma points to a plateau
     */
    public ContributionRecordConverter(OMRSRepositoryHelper repositoryHelper,
                                       String               serviceName,
                                       String               serverName,
                                       int                  karmaPointPlateau)
    {
        super(repositoryHelper, serviceName, serverName, karmaPointPlateau);
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

            if (returnBean instanceof ContributionRecordElement)
            {
                ContributionRecordElement    bean               = (ContributionRecordElement) returnBean;
                ContributionRecord           contributionRecord = new ContributionRecord();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                InstanceProperties entityProperties;

                /*
                 * The initial set of values come from the entity.
                 */
                if (entity != null)
                {
                    entityProperties = new InstanceProperties(entity.getProperties());

                    contributionRecord.setQualifiedName(this.removeQualifiedName(entityProperties));
                    contributionRecord.setAdditionalProperties(this.removeAdditionalProperties(entityProperties));
                    contributionRecord.setKarmaPoints(this.removeKarmaPoints(entityProperties));
                    contributionRecord.setKarmaPointPlateau(karmaPointPlateau);

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    contributionRecord.setTypeName(bean.getElementHeader().getType().getTypeName());
                    contributionRecord.setExtendedProperties(this.getRemainingExtendedProperties(entityProperties));
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }

                bean.setProperties(contributionRecord);
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
