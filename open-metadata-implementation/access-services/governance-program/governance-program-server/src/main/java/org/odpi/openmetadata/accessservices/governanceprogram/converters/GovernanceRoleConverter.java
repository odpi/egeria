/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.converters;

import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceAppointee;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceRoleElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.ProfileElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceRoleProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;


/**
 * GovernanceRoleConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into an GovernanceRoleElement bean.
 */
public class GovernanceRoleConverter<B> extends GovernanceProgramOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public GovernanceRoleConverter(OMRSRepositoryHelper repositoryHelper,
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
            B returnBean = beanClass.newInstance();

            if (returnBean instanceof GovernanceRoleElement)
            {
                GovernanceRoleElement    bean                     = (GovernanceRoleElement) returnBean;
                GovernanceRoleProperties governanceRoleProperties = new GovernanceRoleProperties();

                if (entity != null)
                {
                    bean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    governanceRoleProperties.setDomainIdentifier(this.removeDomainIdentifier(instanceProperties));
                    governanceRoleProperties.setRoleId(this.removeQualifiedName(instanceProperties));
                    governanceRoleProperties.setScope(this.removeScope(instanceProperties));
                    governanceRoleProperties.setTitle(this.removeName(instanceProperties));
                    governanceRoleProperties.setDescription(this.removeDescription(instanceProperties));
                    governanceRoleProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    governanceRoleProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    governanceRoleProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setRole(governanceRoleProperties);
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
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
}
