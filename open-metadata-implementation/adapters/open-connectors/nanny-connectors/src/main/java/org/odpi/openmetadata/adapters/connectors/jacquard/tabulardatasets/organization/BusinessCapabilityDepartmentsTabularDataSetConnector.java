/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.organization;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;


/**
 * BusinessCapabilityDepartmentsTabularDataSetConnector lists the departments that deliver each business
 * capability as if they are a tabular data set.  A department belongs to a business capability when it is a
 * member of the capability through the CollectionMembership relationship; the relationship's membership type
 * (such as owner) and rationale say how and why.
 */
public class BusinessCapabilityDepartmentsTabularDataSetConnector extends OrganizationStructureDataSetConnectorBase
{
    private static final String myConnectorName = BusinessCapabilityDepartmentsTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public BusinessCapabilityDepartmentsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.BUSINESS_CAPABILITY_DEPARTMENTS);
    }


    /**
     * One row per department that is a member of a business capability.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectRows() throws Exception
    {
        OrganizationStructure organizationStructure = getOrganizationStructure();

        /*
         * The capabilities are retrieved with their immediate relationships so that their members, and the
         * properties of the membership relationships, come back with them.
         */
        for (OpenMetadataRootElement businessCapability : getAllElements(OpenMetadataType.BUSINESS_CAPABILITY.typeName, 1))
        {
            if (businessCapability.getCollectionMembers() != null)
            {
                for (RelatedMetadataElementSummary member : businessCapability.getCollectionMembers())
                {
                    if ((member != null) && (member.getRelatedElement() != null) &&
                            (organizationStructure.isDepartment(member.getRelatedElement().getElementHeader().getGUID())))
                    {
                        Map<String, String> values = newValues();

                        putValue(values, ProductDataFieldDefinition.BUSINESS_CAPABILITY_GUID, businessCapability.getElementHeader().getGUID());
                        putValue(values, ProductDataFieldDefinition.BUSINESS_CAPABILITY_NAME, OrganizationStructure.getDisplayName(businessCapability.getProperties()));
                        putValue(values, ProductDataFieldDefinition.DEPARTMENT_GUID, member.getRelatedElement().getElementHeader().getGUID());
                        putValue(values, ProductDataFieldDefinition.DEPARTMENT_NAME, OrganizationStructure.getDisplayName(member.getRelatedElement().getProperties()));

                        if (member.getRelationshipProperties() instanceof CollectionMembershipProperties membershipProperties)
                        {
                            putValue(values, ProductDataFieldDefinition.MEMBERSHIP_TYPE, membershipProperties.getMembershipType());
                            putValue(values, ProductDataFieldDefinition.MEMBERSHIP_RATIONALE, membershipProperties.getMembershipRationale());
                        }
                        else
                        {
                            putValue(values, ProductDataFieldDefinition.MEMBERSHIP_TYPE, null);
                            putValue(values, ProductDataFieldDefinition.MEMBERSHIP_RATIONALE, null);
                        }

                        /*
                         * The create and update times are those of the membership relationship.
                         */
                        addRow(member.getRelationshipHeader(), null, values);
                    }
                }
            }
        }
    }
}
