/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.generichandlers.SAFConverter;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.surveyaction.properties.SurveyReport;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;



/**
 * SurveyReportConverter transfers the relevant properties from some Open Metadata Repository Services (OMRS)
 * EntityDetail object into an SurveyReport bean.
 */
public class SurveyReportConverter<B> extends SAFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public SurveyReportConverter(OMRSRepositoryHelper     repositoryHelper,
                                 String                   serviceName,
                                 String                   serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * a connection bean which made up of 3 entities (Connection, ConnectorType and Endpoint) plus the
     * relationships between them.  The relationships may be omitted if they do not have an properties.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewComplexBean(Class<B>           beanClass,
                               EntityDetail       primaryEntity,
                               List<EntityDetail> supplementaryEntities,
                               List<Relationship> relationships,
                               String             methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof SurveyReport bean)
            {
                /*
                 * Check that the entity is of the correct type.
                 */
                bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, methodName));

                /*
                 * The initial set of values come from the entity properties.  The super class properties are removed from a copy of the entities
                 * properties, leaving any subclass properties to be stored in extended properties.
                 */
                InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
                bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));

                bean.setDisplayName(this.removeDisplayName(instanceProperties));
                bean.setDescription(this.removeDescription(instanceProperties));
                bean.setStartDate(this.removeExecutionDate(instanceProperties));
                bean.setAnalysisParameters(this.removeAnalysisParameters(instanceProperties));
                bean.setAnalysisStep(this.removeAnalysisStep(instanceProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                bean.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                if (relationships != null)
                {
                    for (Relationship relationship : relationships)
                    {
                        if ((relationship != null) && (relationship.getType() != null) && (relationship.getType().getTypeDefName() != null))
                        {
                            if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.REPORT_TO_ASSET_TYPE_NAME))
                            {
                                EntityProxy endOne = relationship.getEntityOneProxy();

                                if (endOne != null)
                                {
                                    bean.setAssetGUID(endOne.getGUID());
                                }
                            }
                            else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.ENGINE_ACTION_SURVEY_REPORT_RELATIONSHIP.typeName))
                            {
                                EntityProxy endOne = relationship.getEntityOneProxy();

                                if (endOne != null)
                                {
                                    bean.setEngineActionGUID(endOne.getGUID());
                                }
                            }
                        }
                    }
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
