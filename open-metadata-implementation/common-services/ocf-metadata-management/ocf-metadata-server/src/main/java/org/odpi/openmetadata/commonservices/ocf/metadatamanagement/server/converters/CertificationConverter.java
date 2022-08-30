/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.server.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OCFConverter;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Certification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * CertificationConverter transfers the relevant properties from some Open Metadata Repository Services (OMRS)
 * EntityDetail object into an Certification bean.
 */
public class CertificationConverter<B> extends OCFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public CertificationConverter(OMRSRepositoryHelper repositoryHelper,
                            String               serviceName,
                            String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
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
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof Certification)
            {
                Certification bean = (Certification) returnBean;

                /*
                 * Check that the entity is of the correct type.
                 */
                this.setUpElementHeader(bean, entity, OpenMetadataAPIMapper.CERTIFICATION_TYPE_TYPE_NAME, methodName);

                /*
                 * The initial set of values come from the entity.
                 */
                InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
                bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                bean.setSummary(this.removeSummary(instanceProperties));
                bean.setCertificationTypeName(entity.getType().getTypeDefName());

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                bean.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                if (relationship != null)
                {
                    bean.setCreatedBy(relationship.getCreatedBy());

                    instanceProperties = new InstanceProperties(entity.getProperties());

                    bean.setCertificateGUID(this.getCertificationGUID(instanceProperties));
                    bean.setCertificationConditions(getConditions(instanceProperties));
                    bean.setStartDate(getStart(instanceProperties));
                    bean.setEndDate(getEnd(instanceProperties));
                    bean.setExaminer(getCertifiedBy(instanceProperties));
                    bean.setCustodian(getCustodian(instanceProperties));
                    bean.setRecipient(getRecipient(instanceProperties));
                    bean.setNotes(getNotes(instanceProperties));
                }

                return returnBean;
            }
            else
            {
                super.handleUnexpectedBeanClass(beanClass.getName(), Certification.class.getName(), methodName);
            }
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }
}
