/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataFlowElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.DataFlowProperties;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * DataFlowConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * Relationship object into a DataFlowElement bean.
 */
public class DataFlowConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public DataFlowConverter(OMRSRepositoryHelper repositoryHelper,
                             String               serviceName,
                             String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied relationship, return a new instance of the bean.  It is used for beans that
     * represent a simple relationship between two entities.
     *
     * @param beanClass name of the class to create
     * @param relationship relationship linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewRelationshipBean(Class<B>     beanClass,
                                    Relationship relationship,
                                    String       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof DataFlowElement bean)
            {
                DataFlowProperties dataFlowProperties = new DataFlowProperties();

                if (relationship != null)
                {
                    bean.setDataFlowHeader(super.getMetadataElementHeader(beanClass, relationship, null, methodName));

                    EntityProxy entityProxy = relationship.getEntityOneProxy();
                    if (entityProxy != null)
                    {
                        bean.setDataSupplier(super.getMetadataElementHeader(beanClass, entityProxy, entityProxy.getClassifications(), methodName));
                    }

                    entityProxy = relationship.getEntityTwoProxy();
                    if (entityProxy != null)
                    {
                        bean.setDataConsumer(super.getMetadataElementHeader(beanClass, entityProxy, entityProxy.getClassifications(), methodName));
                    }

                    /*
                     * The rest of the properties come from the relationship.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(relationship.getProperties());

                    dataFlowProperties.setQualifiedName(this.getISCQualifiedName(instanceProperties));
                    dataFlowProperties.setDescription(this.getDescription(instanceProperties));
                    dataFlowProperties.setFormula(this.getFormula(instanceProperties));
                    dataFlowProperties.setFormulaType(this.getFormulaType(instanceProperties));

                    bean.setDataFlowProperties(dataFlowProperties);
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.RELATIONSHIP_DEF, methodName);
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
