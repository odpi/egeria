/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.mappers.PersonalProfileMapper;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelement.ContactMethodElement;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelement.ContributionRecordElement;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelement.PersonalProfileElement;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelement.UserIdentityElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ContactMethodProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfileProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * PersonalProfileConverter generates a PersonalProfileProperties bean from a PersonalProfileProperties entity.
 */
public class PersonalProfileConverter<B> extends CommunityProfileOMASConverter<B>
{
    private static final Logger log = LoggerFactory.getLogger(PersonalProfileConverter.class);

    private String serverName;

    private List<UserIdentityElement>  associatedUserIds;
    private List<ContactMethodElement> contactDetails;
    private ContributionRecordElement  contributionRecord;


    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public PersonalProfileConverter(OMRSRepositoryHelper repositoryHelper,
                                    String               serviceName,
                                    String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * a connection bean which made up of 3 entities (Connection, ConnectorType and Endpoint) plus the
     * relationships between them.  The relationships may be omitted if they do not have any properties.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewComplexBean(Class<B>           beanClass,
                               EntityDetail       primaryEntity,
                               List<EntityDetail> supplementaryEntities,
                               List<Relationship> relationships,
                               String             methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewComplexBean(with supplementary entities)";

        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.newInstance();

            if (returnBean instanceof PersonalProfileElement)
            {
                PersonalProfileElement    bean              = (PersonalProfileElement) returnBean;
                PersonalProfileProperties profileProperties = new PersonalProfileProperties();

                if (primaryEntity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                    profileProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    profileProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    profileProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    profileProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setPersonalProfileProperties(profileProperties);
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


    /**
     * Return the bean constructed from the repository content.
     *
     * @return bean
     */
    public PersonalProfileElement getBean()
    {
        final String methodName = "getBean";

        PersonalProfileElement bean = new PersonalProfileElement();
        PersonalProfileProperties properties = new PersonalProfileProperties();

        super.updateBean(bean);

        if (entity != null)
        {
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                /*
                 * As properties are retrieved, they are removed from the instance properties object so that what is left going into
                 * community properties.
                 */
                properties.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, PersonalProfileMapper.QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
                properties.setKnownName(repositoryHelper.removeStringProperty(serviceName, PersonalProfileMapper.NAME_PROPERTY_NAME, instanceProperties, methodName));
                properties.setDescription(repositoryHelper.removeStringProperty(serviceName, PersonalProfileMapper.DESCRIPTION_PROPERTY_NAME, instanceProperties, methodName));
                properties.setFullName(repositoryHelper.removeStringProperty(serviceName, PersonalProfileMapper.FULL_NAME_PROPERTY_NAME, instanceProperties, methodName));
                properties.setJobTitle(repositoryHelper.removeStringProperty(serviceName, PersonalProfileMapper.JOB_TITLE_PROPERTY_NAME, instanceProperties, methodName));
                properties.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, PersonalProfileMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
                properties.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));

            }
        }

        bean.setPersonalProfileProperties(properties);
        bean.setUserIdentities(associatedUserIds);
        bean.setContactMethods(contactDetails);
        bean.setContributionRecord(contributionRecord);

        log.debug("Bean: " + properties.toString());

        return bean;
    }
}
