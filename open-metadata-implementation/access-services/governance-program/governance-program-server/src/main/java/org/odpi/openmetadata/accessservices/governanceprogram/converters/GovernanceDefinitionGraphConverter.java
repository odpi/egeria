/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.converters;


import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceDefinitionGraph;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.CertificationTypeProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDefinitionProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.LicenseTypeProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.SecurityGroupProperties;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * GovernanceDefinitionConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from GovernanceDefinitionGraph.
 */
public class GovernanceDefinitionGraphConverter<B> extends GovernanceProgramOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public GovernanceDefinitionGraphConverter(OMRSRepositoryHelper repositoryHelper,
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

            if (returnBean instanceof GovernanceDefinitionGraph)
            {
                GovernanceDefinitionGraph bean = (GovernanceDefinitionGraph) returnBean;

                if (primaryEntity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, primaryEntity.getClassifications(), methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                    String typeName = primaryEntity.getType().getTypeDefName();
                    GovernanceDefinitionProperties governanceDefinitionProperties;

                    if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataType.CERTIFICATION_TYPE_TYPE_NAME))
                    {
                        governanceDefinitionProperties = new CertificationTypeProperties();

                        ((CertificationTypeProperties) governanceDefinitionProperties).setDetails(this.removeDetails(instanceProperties));
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataType.LICENSE_TYPE_TYPE_NAME))
                    {
                        governanceDefinitionProperties = new LicenseTypeProperties();

                        ((LicenseTypeProperties) governanceDefinitionProperties).setDetails(this.removeDetails(instanceProperties));
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataType.SECURITY_GROUP_TYPE_NAME))
                    {
                        governanceDefinitionProperties = new SecurityGroupProperties();

                        ((SecurityGroupProperties) governanceDefinitionProperties).setDistinguishedName(this.removeDistinguishedName(instanceProperties));
                    }
                    else
                    {
                        governanceDefinitionProperties = new GovernanceDefinitionProperties();
                    }

                    governanceDefinitionProperties.setDocumentIdentifier(this.removeQualifiedName(instanceProperties));
                    governanceDefinitionProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    governanceDefinitionProperties.setTitle(this.removeTitle(instanceProperties));
                    governanceDefinitionProperties.setScope(this.removeScope(instanceProperties));
                    governanceDefinitionProperties.setDomainIdentifier(this.removeDomainIdentifier(instanceProperties));
                    governanceDefinitionProperties.setPriority(this.removePriority(instanceProperties));
                    governanceDefinitionProperties.setOutcomes(this.removeOutcomes(instanceProperties));
                    governanceDefinitionProperties.setResults(this.removeResults(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    governanceDefinitionProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    governanceDefinitionProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setProperties(governanceDefinitionProperties);

                    if (relationships != null)
                    {
                        List<RelatedElement> parents            = new ArrayList<>();
                        List<RelatedElement> peers              = new ArrayList<>();
                        List<RelatedElement> children           = new ArrayList<>();
                        List<RelatedElement> metrics            = new ArrayList<>();
                        List<RelatedElement> externalReferences = new ArrayList<>();
                        List<RelatedElement> others             = new ArrayList<>();

                        for (Relationship relationship : relationships)
                        {
                            if (relationship != null)
                            {
                                EntityProxy otherEnd = repositoryHelper.getOtherEnd(serviceName, primaryEntity.getGUID(), relationship);
                                RelatedElement element = super.getRelatedElement(beanClass, relationship, otherEnd, methodName);

                                if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName))
                                {
                                    externalReferences.add(element);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.GOVERNANCE_DEFINITION_METRIC_TYPE_NAME))
                                {
                                    metrics.add(element);
                                }
                                else if ((repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.GOVERNANCE_DRIVER_LINK_TYPE_NAME)) ||
                                         (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.GOVERNANCE_POLICY_LINK_TYPE_NAME)) ||
                                         (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.GOVERNANCE_CONTROL_LINK_TYPE_NAME)))
                                {

                                    peers.add(element);
                                }
                                else if ((repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.GOVERNANCE_RESPONSE_TYPE_NAME)) ||
                                         (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.GOVERNANCE_IMPLEMENTATION_TYPE_NAME)))
                                {
                                    if (primaryEntity.getGUID().equals(relationship.getEntityTwoProxy().getGUID()))
                                    {
                                        parents.add(element);
                                    }
                                    else
                                    {
                                        children.add(element);
                                    }
                                }
                                else
                                {
                                    others.add(element);
                                }
                            }
                        }

                        if (! parents.isEmpty())
                        {
                            bean.setParents(parents);
                        }

                        if (! peers.isEmpty())
                        {
                            bean.setPeers(peers);
                        }

                        if (! children.isEmpty())
                        {
                            bean.setChildren(children);
                        }

                        if (! metrics.isEmpty())
                        {
                            bean.setMetrics(metrics);
                        }

                        if (! externalReferences.isEmpty())
                        {
                            bean.setExternalReferences(externalReferences);
                        }

                        if (! others.isEmpty())
                        {
                            bean.setOthers(others);
                        }
                    }
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
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
