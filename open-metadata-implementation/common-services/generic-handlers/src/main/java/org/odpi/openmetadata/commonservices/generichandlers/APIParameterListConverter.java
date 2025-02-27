/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.APIParameterListElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIParameterListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.APIParameterListType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * EventTypeConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from EventTypeElement.  It is working with a ComplexSchemaType.
 */
public class APIParameterListConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public APIParameterListConverter(OMRSRepositoryHelper repositoryHelper,
                                     String               serviceName,
                                     String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or DataField bean which combine knowledge from the entity and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewComplexBean(Class<B>           beanClass,
                               EntityDetail       primaryEntity,
                               List<Relationship> relationships,
                               String             methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof APIParameterListElement bean)
            {
                if ((primaryEntity != null) && (primaryEntity.getProperties() != null))
                {
                    /*
                     * The API Parameter List has many different subtypes.
                     * This next piece of logic sorts out which type of schema bean to create.
                     */
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, methodName));

                    APIParameterListProperties properties = new APIParameterListProperties();

                    InstanceProperties propertiesCopy = new InstanceProperties(primaryEntity.getProperties());

                    properties.setQualifiedName(this.removeQualifiedName(propertiesCopy));
                    properties.setDisplayName(this.removeDisplayName(propertiesCopy));
                    properties.setDescription(this.removeDescription(propertiesCopy));
                    properties.setIsDeprecated(this.removeIsDeprecated(propertiesCopy));
                    properties.setVersionNumber(this.removeVersionNumber(propertiesCopy));
                    properties.setAuthor(this.removeAuthor(propertiesCopy));
                    properties.setUsage(this.removeUsage(propertiesCopy));
                    properties.setEncodingStandard(this.removeEncodingStandard(propertiesCopy));
                    properties.setNamespace(this.removeNamespace(propertiesCopy));
                    properties.setAdditionalProperties(this.removeAdditionalProperties(propertiesCopy));
                    properties.setRequired(this.removeRequired(propertiesCopy));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    properties.setExtendedProperties(this.getRemainingExtendedProperties(propertiesCopy));

                    bean.setProperties(properties);

                    if (relationships != null)
                    {
                        int parameterCount = 0;

                        for (Relationship relationship : relationships)
                        {
                            if (relationship != null)
                            {
                                EntityProxy endOne = relationship.getEntityOneProxy();
                                if (repositoryHelper.isTypeOf(serviceName,
                                                              endOne.getType().getTypeDefName(),
                                                              OpenMetadataType.API_OPERATION.typeName))
                                {
                                    if (repositoryHelper.isTypeOf(serviceName,
                                                                  relationship.getType().getTypeDefName(),
                                                                  OpenMetadataType.API_HEADER_RELATIONSHIP.typeName))
                                    {
                                        bean.setParameterListType(APIParameterListType.HEADER);
                                    }
                                    else if (repositoryHelper.isTypeOf(serviceName,
                                                                       relationship.getType().getTypeDefName(),
                                                                       OpenMetadataType.API_REQUEST_RELATIONSHIP.typeName))
                                    {
                                        bean.setParameterListType(APIParameterListType.REQUEST);
                                    }
                                    else if (repositoryHelper.isTypeOf(serviceName,
                                                                       relationship.getType().getTypeDefName(),
                                                                       OpenMetadataType.API_RESPONSE_RELATIONSHIP.typeName))
                                    {
                                        bean.setParameterListType(APIParameterListType.RESPONSE);
                                    }
                                }
                                else if (repositoryHelper.isTypeOf(serviceName,
                                                                   endOne.getType().getTypeDefName(),
                                                                   OpenMetadataType.API_PARAMETER.typeName))
                                {
                                    parameterCount ++;
                                }
                            }
                        }

                        bean.setParameterCount(parameterCount);

                    }

                    return returnBean;
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }
            }
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }

}
