/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration;

import org.odpi.openmetadata.adminservices.configuration.properties.PermittedSynchronization;

import java.io.Serializable;

/**
 * IntegrationServiceDescription provides a list of registered integration services.
 */
public enum IntegrationServiceDescription implements Serializable
{
    CATALOG_INTEGRATOR_OMIS(4000,
                            "Catalog Integrator",
                            "Catalog Integrator OMIS",
                            "catalog-integrator",
                            "Exchange metadata with third party data catalogs.",
                            "https://egeria.odpi.org/open-metadata-implementation/integration-services/catalog-integrator/",
                            "Asset Manager OMAS",
                            PermittedSynchronization.BOTH_DIRECTIONS),

    DATABASE_INTEGRATOR_OMIS     (4004,
                                  "Database Integrator",
                                  "Database Integrator OMIS",
                                  "database-integrator",
                                  "Extract metadata such as schema, tables and columns from database managers.",
                                  "https://egeria.odpi.org/open-metadata-implementation/integration-services/database-integrator/",
                                  "Data Manager OMAS",
                                  PermittedSynchronization.FROM_THIRD_PARTY),

    FILES_INTEGRATOR_OMIS(4005,
                          "Files Integrator",
                          "Files Integrator OMIS",
                          "files-integrator",
                          "Extract metadata about files stored in a file system or file manager.",
                          "https://egeria.odpi.org/open-metadata-implementation/integration-services/files-integrator/",
                          "Data Manager OMAS",
                          PermittedSynchronization.FROM_THIRD_PARTY),

    LINEAGE_INTEGRATOR_OMIS(4006,
                          "Lineage Integrator",
                          "Lineage Integrator OMIS",
                          "lineage-integrator",
                          "Manage capture of lineage from a third party tool.",
                          "https://egeria.odpi.org/open-metadata-implementation/integration-services/lineage-integrator/",
                          "Asset Manager OMAS",
                          PermittedSynchronization.FROM_THIRD_PARTY),

    ORGANIZATION_INTEGRATOR_OMIS     (4007,
                                      "Organization Integrator",
                                      "Organization Integrator OMIS",
                                      "organization-integrator",
                                      "Load information about the teams and people in an organization and return collaboration activity.",
                                      "https://egeria.odpi.org/open-metadata-implementation/integration-services/organization-integrator/",
                                      "Community Profile OMAS",
                                      PermittedSynchronization.FROM_THIRD_PARTY),

    SECURITY_INTEGRATOR_OMIS(4008,
                                      "Security Integrator",
                                      "Security Integrator OMIS",
                                      "security-integrator",
                                      "Distribute security properties to security enforcement points.",
                                      "https://egeria.odpi.org/open-metadata-implementation/integration-services/security-integrator/",
                                      "Security Officer OMAS",
                                      PermittedSynchronization.TO_THIRD_PARTY),
    ;

    private static final long     serialVersionUID    = 1L;

    private int                      integrationServiceCode;
    private String                   integrationServiceName;
    private String                   integrationServiceFullName;
    private String                   integrationServiceURLMarker;
    private String                   integrationServiceDescription;
    private String                   integrationServiceWiki;
    private String                   integrationServicePartnerOMAS;
    private PermittedSynchronization defaultPermittedSynchronization;


    /**
     * Default Constructor
     *
     * @param integrationServiceCode ordinal for this integration service
     * @param integrationServiceName symbolic name for this integration service
     * @param integrationServiceFullName full name for this integration service
     * @param integrationServiceURLMarker string used in URLs
     * @param integrationServiceDescription short description for this integration service
     * @param integrationServiceWiki wiki page for the integration service for this integration service
     * @param integrationServicePartnerOMAS name of the OMAS that is partnered with this integration service
     */
    IntegrationServiceDescription(int                      integrationServiceCode,
                                  String                   integrationServiceName,
                                  String                   integrationServiceFullName,
                                  String                   integrationServiceURLMarker,
                                  String                   integrationServiceDescription,
                                  String                   integrationServiceWiki,
                                  String                   integrationServicePartnerOMAS,
                                  PermittedSynchronization defaultPermittedSynchronization)
    {
        /*
         * Save the values supplied
         */
        this.integrationServiceCode          = integrationServiceCode;
        this.integrationServiceName          = integrationServiceName;
        this.integrationServiceFullName      = integrationServiceFullName;
        this.integrationServiceURLMarker     = integrationServiceURLMarker;
        this.integrationServiceDescription   = integrationServiceDescription;
        this.integrationServiceWiki          = integrationServiceWiki;
        this.integrationServicePartnerOMAS   = integrationServicePartnerOMAS;
        this.defaultPermittedSynchronization = defaultPermittedSynchronization;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int type code
     */
    public int getIntegrationServiceCode()
    {
        return integrationServiceCode;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getIntegrationServiceName()
    {
        return integrationServiceName;
    }


    /**
     * Return the formal name for this enum instance.
     *
     * @return String default name
     */
    public String getIntegrationServiceFullName()
    {
        return integrationServiceFullName;
    }


    /**
     * Return the string that appears in the REST API URL that identifies the owning service.
     *
     * @return String default URL marker
     */
    public String getIntegrationServiceURLMarker()
    {
        return integrationServiceURLMarker;
    }


    /**
     * Return the default description for the type for this enum instance.
     *
     * @return String default description
     */
    public String getIntegrationServiceDescription()
    {
        return integrationServiceDescription;
    }


    /**
     * Return the URL for the wiki page describing this integration service.
     *
     * @return String URL name for the wiki page
     */
    public String getIntegrationServiceWiki()
    {
        return integrationServiceWiki;
    }


    /**
     * Return the full name of the Open Metadata Access Service (OMAS) that this integration service is partnered with.
     *
     * @return  Full name of OMAS
     */
    public String getIntegrationServicePartnerOMAS()
    {
        return integrationServicePartnerOMAS;
    }


    /**
     * Return the default value for permitted synchronization that should be set up for the integration connectors
     * as they are configured.
     *
     * @return enum default
     */
    public PermittedSynchronization getDefaultPermittedSynchronization()
    {
        return defaultPermittedSynchronization;
    }
}
