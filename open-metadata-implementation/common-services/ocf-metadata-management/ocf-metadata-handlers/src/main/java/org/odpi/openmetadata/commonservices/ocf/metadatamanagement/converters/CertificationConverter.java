/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.CertificationMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.EndpointMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.GovernanceDefinitionMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Certification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * CertificationConverter transfers the relevant properties from some Open Metadata Repository Services (OMRS)
 * EntityDetail object into an Certification bean.
 */
public class CertificationConverter extends ReferenceableConverter
{
    /**
     * Constructor captures the repository content needed to create the endpoint object.
     *
     * @param mainEntity properties to convert
     * @param linkingRelationship relationship from anchor to main entity
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     */
    public CertificationConverter(EntityDetail             mainEntity,
                                  Relationship             linkingRelationship,
                                  OMRSRepositoryHelper     repositoryHelper,
                                  String                   serviceName)
    {
        super(mainEntity,
              linkingRelationship,
              repositoryHelper,
              serviceName);
    }


    /**
     * Request the bean is extracted from the repository entity.
     *
     * @return output bean
     */
    public Certification getBean()
    {
        final String  methodName = "getBean";

        Certification      bean = null;

        if (relationship != null)
        {
            bean = new Certification();
            bean.setCreatedBy(relationship.getCreatedBy());

            InstanceProperties instanceProperties;

            if (entity != null)
            {
                super.updateBean(bean);

                /*
                 * The properties are removed from the instance properties and stowed in the bean.
                 * Any remaining properties are stored in extendedProperties.
                 */
                instanceProperties = entity.getProperties();

                if (instanceProperties != null)
                {
                    bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName,
                                                                                ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                                instanceProperties,
                                                                                methodName));
                    bean.setSummary(repositoryHelper.removeStringProperty(serviceName,
                                                                          GovernanceDefinitionMapper.SUMMARY_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                    bean.setCertificationTypeName(repositoryHelper.removeStringProperty(serviceName,
                                                                                        GovernanceDefinitionMapper.TITLE_PROPERTY_NAME,
                                                                                        instanceProperties,
                                                                                        methodName));
                    bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                              ReferenceableMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                              instanceProperties,
                                                                                              methodName));
                }
            }

            instanceProperties = relationship.getProperties();

            if (instanceProperties != null)
            {
                bean.setCertificateGUID(repositoryHelper.removeStringProperty(serviceName,
                                                                              CertificationMapper.CERTIFICATE_GUID_PROPERTY_NAME,
                                                                              instanceProperties,
                                                                              methodName));
                bean.setCertificationConditions(repositoryHelper.removeStringProperty(serviceName,
                                                                                      CertificationMapper.CONDITIONS_PROPERTY_NAME,
                                                                                      instanceProperties,
                                                                                      methodName));
                bean.setStartDate(repositoryHelper.removeDateProperty(serviceName,
                                                                      CertificationMapper.START_PROPERTY_NAME,
                                                                      instanceProperties,
                                                                      methodName));
                bean.setEndDate(repositoryHelper.removeDateProperty(serviceName,
                                                                    CertificationMapper.END_PROPERTY_NAME,
                                                                    instanceProperties,
                                                                    methodName));
                bean.setExaminer(repositoryHelper.removeStringProperty(serviceName,
                                                                       CertificationMapper.CERTIFIED_BY_PROPERTY_NAME,
                                                                       instanceProperties,
                                                                       methodName));
                bean.setCustodian(repositoryHelper.removeStringProperty(serviceName,
                                                                        CertificationMapper.CUSTODIAN_PROPERTY_NAME,
                                                                        instanceProperties,
                                                                        methodName));
                bean.setNotes(repositoryHelper.removeStringProperty(serviceName,
                                                                    CertificationMapper.NOTES_PROPERTY_NAME,
                                                                    instanceProperties,
                                                                    methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }


        }

        return bean;
    }
}
