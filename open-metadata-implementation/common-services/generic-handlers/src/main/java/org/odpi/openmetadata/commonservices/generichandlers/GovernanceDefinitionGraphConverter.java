/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.commonservices.mermaid.GovernanceDefinitionMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDefinitionGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.CertificationTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LicenseTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
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
public class GovernanceDefinitionGraphConverter<B> extends OMFConverter<B>
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

            if (returnBean instanceof GovernanceDefinitionGraph bean)
            {

                if (primaryEntity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, primaryEntity.getClassifications(), methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                    String                         typeName = primaryEntity.getType().getTypeDefName();
                    GovernanceDefinitionProperties governanceDefinitionProperties;

                    if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataType.CERTIFICATION_TYPE.typeName))
                    {
                        governanceDefinitionProperties = new CertificationTypeProperties();

                        ((CertificationTypeProperties) governanceDefinitionProperties).setDetails(this.removeDetails(instanceProperties));
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataType.LICENSE_TYPE.typeName))
                    {
                        governanceDefinitionProperties = new LicenseTypeProperties();

                        ((LicenseTypeProperties) governanceDefinitionProperties).setDetails(this.removeDetails(instanceProperties));
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataType.SECURITY_GROUP.typeName))
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
                    governanceDefinitionProperties.setSummary(this.removeSummary(instanceProperties));
                    governanceDefinitionProperties.setDescription(this.removeDescription(instanceProperties));
                    governanceDefinitionProperties.setScope(this.removeScope(instanceProperties));
                    governanceDefinitionProperties.setDomainIdentifier(this.removeDomainIdentifier(instanceProperties));
                    governanceDefinitionProperties.setImportance(this.removeImportance(instanceProperties));
                    governanceDefinitionProperties.setImplications(this.removeImplications(instanceProperties));
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
                        List<RelatedElementStub> parents            = new ArrayList<>();
                        List<RelatedElementStub> peers              = new ArrayList<>();
                        List<RelatedElementStub> children           = new ArrayList<>();
                        List<RelatedElementStub> metrics            = new ArrayList<>();
                        List<RelatedElementStub> externalReferences = new ArrayList<>();
                        List<RelatedElementStub> others             = new ArrayList<>();

                        for (Relationship relationship : relationships)
                        {
                            if (relationship != null)
                            {
                                EntityProxy        otherEnd = repositoryHelper.getOtherEnd(serviceName, primaryEntity.getGUID(), relationship);
                                RelatedElementStub element  = super.getRelatedElement(beanClass, relationship, otherEnd, methodName);

                                if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName))
                                {
                                    externalReferences.add(element);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.GOVERNANCE_DEFINITION_METRIC_RELATIONSHIP.typeName))
                                {
                                    metrics.add(element);
                                }
                                else if ((repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.GOVERNANCE_DRIVER_LINK_RELATIONSHIP.typeName)) ||
                                         (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.GOVERNANCE_POLICY_LINK_RELATIONSHIP.typeName)) ||
                                         (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.GOVERNANCE_CONTROL_LINK_RELATIONSHIP.typeName)))
                                {

                                    peers.add(element);
                                }
                                else if ((repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName)) ||
                                         (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.GOVERNANCE_IMPLEMENTATION_RELATIONSHIP.typeName)))
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

                        GovernanceDefinitionMermaidGraphBuilder graphBuilder = new GovernanceDefinitionMermaidGraphBuilder(bean);

                        bean.setMermaidGraph(graphBuilder.getMermaidGraph());
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
