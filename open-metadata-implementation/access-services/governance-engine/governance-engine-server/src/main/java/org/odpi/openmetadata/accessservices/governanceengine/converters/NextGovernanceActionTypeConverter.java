/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.converters;

import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceActionTypeElement;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.NextGovernanceActionTypeElement;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;



/**
 * NextGovernanceActionTypeConverter transfers the relevant properties from a GovernanceServiceElement bean
 * and the Open Metadata Repository Services (OMRS) Relationship object into a NextGovernanceAction bean.
 */
public class NextGovernanceActionTypeConverter
{
    private final OMRSRepositoryHelper       repositoryHelper;
    private final String                     serviceName;

    /**
     * Constructor captures the repository content needed to create the endpoint object.
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     */
    public NextGovernanceActionTypeConverter(OMRSRepositoryHelper       repositoryHelper,
                                             String                     serviceName)
    {
        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;
    }


    /**
     * Request the bean is extracted from the repository entity.
     *
     * @param actionTypeElement next action type element
     * @param relationship NextGovernanceActionType relationships
     * @return output bean
     */
    public NextGovernanceActionTypeElement getBean(GovernanceActionTypeElement actionTypeElement,
                                                   Relationship                relationship)
    {
        final String  methodName = "getBean";

        NextGovernanceActionTypeElement bean = new NextGovernanceActionTypeElement();

        bean.setNextActionType(actionTypeElement);

        if (relationship != null)
        {
            InstanceProperties instanceProperties = relationship.getProperties();

            if (instanceProperties != null)
            {
                bean.setGuard(repositoryHelper.getStringProperty(serviceName,
                                                                 OpenMetadataType.GUARD_PROPERTY_NAME,
                                                                 instanceProperties,
                                                                 methodName));
                bean.setMandatoryGuard(repositoryHelper.getBooleanProperty(serviceName,
                                                                           OpenMetadataType.MANDATORY_GUARD_PROPERTY_NAME,
                                                                           instanceProperties,
                                                                           methodName));

                bean.setNextActionLinkGUID(relationship.getGUID());
            }
        }

        return bean;
    }
}
