/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.handlers;

import org.odpi.openmetadata.frameworks.openmetadata.enums.GovernanceDomain;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GovernanceProgramEnumHandler manages the mapping between Enums in the OMAS API and the open metadata types.
 */
class GovernanceProgramEnumHandler
{
    private static final Logger log = LoggerFactory.getLogger(GovernanceProgramEnumHandler.class);

    private String                        serviceName;
    private OMRSRepositoryHelper          repositoryHelper;

    /**
     * Construct the enum handler handler with a link to the property server's connector and this access service's
     * official name.
     *
     * @param repositoryHelper repository helper for the OMAS
     * @param serviceName name of this omas* @param serviceName  name of this service
     */
    GovernanceProgramEnumHandler(String               serviceName,
                                 OMRSRepositoryHelper repositoryHelper)
    {
        this.serviceName = serviceName;
        this.repositoryHelper = repositoryHelper;
    }


    /**
     * Set up a property value for the GovernanceDomain enum property.
     *
     * @param properties  current properties
     * @param governanceDomain  enum value
     * @param propertyName name of the property that supplied the enum
     * @param methodName  calling method
     * @return InstanceProperties object with the enum value added
     */
    InstanceProperties addGovernanceDomainToProperties(InstanceProperties   properties,
                                                       GovernanceDomain     governanceDomain,
                                                       String               propertyName,
                                                       String               methodName)
    {
        int                ordinal = 999;
        String             symbolicName = null;
        String             description = null;

        log.debug("Governance domain: " + governanceDomain.getName());

        final int    element1Ordinal         = 0;
        final String element1Value           = "Unclassified";
        final String element1Description     = "The governance domain is not specified.";

        final int    element2Ordinal         = 1;
        final String element2Value           = "Data";
        final String element2Description     = "The data (information) governance domain.";

        final int    element3Ordinal         = 2;
        final String element3Value           = "Privacy";
        final String element3Description     = "The data privacy domain.";

        final int    element4Ordinal         = 3;
        final String element4Value           = "Security";
        final String element4Description     = "The security governance domain.";

        final int    element5Ordinal         = 4;
        final String element5Value           = "ITInfrastructure";
        final String element5Description     = "The IT infrastructure governance domain.";


        final int    element6Ordinal         = 5;
        final String element6Value           = "SoftwareDevelopment";
        final String element6Description     = "The software development lifecycle governance domain.";

        final int    element7Ordinal         = 6;
        final String element7Value           = "Corporate";
        final String element7Description     = "The corporate governance domain.";

        final int    element8Ordinal         = 7;
        final String element8Value           = "AssetManagement";
        final String element8Description     = "The physical asset management governance domain.";

        final int    element99Ordinal         = 99;
        final String element99Value           = "Other";
        final String element99Description     = "Another governance domain.";


        switch (governanceDomain)
        {
            case UNCLASSIFIED:
                ordinal = element1Ordinal;
                symbolicName = element1Value;
                description = element1Description;
                break;

            case DATA:
                ordinal = element2Ordinal;
                symbolicName = element2Value;
                description = element2Description;
                break;

            case PRIVACY:
                ordinal = element3Ordinal;
                symbolicName = element3Value;
                description = element3Description;
                break;

            case SECURITY:
                ordinal = element4Ordinal;
                symbolicName = element4Value;
                description = element4Description;
                break;

            case IT_INFRASTRUCTURE:
                ordinal = element5Ordinal;
                symbolicName = element5Value;
                description = element5Description;
                break;

            case SOFTWARE_DEVELOPMENT:
                ordinal = element6Ordinal;
                symbolicName = element6Value;
                description = element6Description;
                break;

            case CORPORATE:
                ordinal = element7Ordinal;
                symbolicName = element7Value;
                description = element7Description;
                break;

            case ASSET_MANAGEMENT:
                ordinal = element8Ordinal;
                symbolicName = element8Value;
                description = element8Description;
                break;

            case OTHER:
                ordinal = element99Ordinal;
                symbolicName = element99Value;
                description = element99Description;
                break;
        }

        return repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                          properties,
                                                          propertyName,
                                                          ordinal,
                                                          symbolicName,
                                                          description,
                                                          methodName);
    }


    /**
     * Retrieve the GovernanceDomain enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @param propertyName name of the property that supplied the enum
     * @param methodName  calling method
     * @return governanceDomain  enum value
     */
    GovernanceDomain getGovernanceDomainFromProperties(InstanceProperties   properties,
                                                       String               propertyName,
                                                       String               methodName)
    {
        GovernanceDomain   governanceDomain = GovernanceDomain.OTHER;

        if (properties != null)
        {
            InstancePropertyValue instancePropertyValue = properties.getPropertyValue(propertyName);

            if (instancePropertyValue instanceof EnumPropertyValue)
            {
                EnumPropertyValue enumPropertyValue = (EnumPropertyValue)instancePropertyValue;

                switch (enumPropertyValue.getOrdinal())
                {
                    case 0:
                        governanceDomain = GovernanceDomain.UNCLASSIFIED;
                        break;

                    case 1:
                        governanceDomain = GovernanceDomain.DATA;
                        break;

                    case 2:
                        governanceDomain = GovernanceDomain.PRIVACY;
                        break;

                    case 3:
                        governanceDomain = GovernanceDomain.SECURITY;
                        break;

                    case 4:
                        governanceDomain = GovernanceDomain.IT_INFRASTRUCTURE;
                        break;

                    case 5:
                        governanceDomain = GovernanceDomain.SOFTWARE_DEVELOPMENT;
                        break;

                    case 6:
                        governanceDomain = GovernanceDomain.CORPORATE;
                        break;

                    case 7:
                        governanceDomain = GovernanceDomain.ASSET_MANAGEMENT;
                        break;

                    case 99:
                        governanceDomain = GovernanceDomain.OTHER;
                        break;
                }
            }
        }

        log.debug("Governance domain: " + governanceDomain.getName() + " for method " + methodName);

        return governanceDomain;
    }
}
