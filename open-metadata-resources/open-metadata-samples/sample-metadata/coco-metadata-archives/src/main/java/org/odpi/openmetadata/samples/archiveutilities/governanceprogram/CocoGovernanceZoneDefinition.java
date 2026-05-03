/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.governanceprogram;

import org.odpi.openmetadata.samples.archiveutilities.sustainability.SustainabilityDomainDefinition;

/**
 * The CocoGovernanceZoneDefinition is used to add governance zones to other elements.
 * The definition of the governance zone is in the coco user directory,
 * and this is catalogued by SecretsStoreCataloguer.
 */
public enum CocoGovernanceZoneDefinition
{
    /**
     * quarantine
     */
    QUARANTINE("98f784a5-1e32-422d-b058-d8b0e321300a",
               "quarantine",
               "Quarantine Zone",
               "Resources from third parties that are being evaluated by the onboarding team. " +
                       "The assets will move into the other zones once the asset has been catalogued and classified.",
               "Data sets just received and have not yet been properly catalogued.",
               GovernanceDomainDefinition.DATA.getDomainIdentifier(),
               null),

    /**
     * landing-area
     */
    LANDING_AREA("3c256517-7618-4f5a-9fe0-0aa790297ae1",
                 "landing-area",
                 "Landing Area Zone",
                 "Denotes resources from third parties in their initial form. " +
                         "These resources will be copied and augmented by various onboarding processes.",
                 "Original data sets received from third parties that have not been moved or transformed.",
                 GovernanceDomainDefinition.DATA.getDomainIdentifier(),
                 null),

    /**
     * data-lake
     */
    DATA_LAKE("029498ca-a142-41a3-8adc-09083ec1bd8a",
              "data-lake",
              "Data Lake Zone",
              "Resources for sharing that are read only.",
              "These are production assets that can be used for business decisions.",
              GovernanceDomainDefinition.DATA.getDomainIdentifier(),
              null),

    /**
     * research
     */
    RESEARCH("382029d9-2f30-4e83-9b12-0e4cb74ca8f9",
             "research",
             "Research Zone",
             "Research data sets and findings",
             "Resources that are driving the development of new products and techniques.",
             GovernanceDomainDefinition.DRUG_DEVELOPMENT.getDomainIdentifier(),
             null),

    /**
     * clinical-trials
     */
    CLINICAL_TRIALS("b66a4e68-8392-4dbc-a0c0-156e892ea03e",
                    "clinical-trials",
                    "Clinical Trials Zone",
                    "Patient data, protocols, outcomes and analysis used within a clinical trial." +
                            "This data is highly confidential and has restricted access.  It is also subject " +
                            "to the data management requirements of the regulators.",
                    "Assets supporting the clinical trials.",
                    GovernanceDomainDefinition.DRUG_DEVELOPMENT.getDomainIdentifier(),
                    null),

    /**
     * human-resources
     */
    HUMAN_RESOURCES("d1af64c2-7727-49ee-bc32-4fcdba363488",
                    "human-resources",
                    "Human Resources (Personnel) Zone",
                    "Resources used to manage and support employees of Coco Pharmaceuticals.",
                    "Assets controlled by the HR and management teams.",
                    GovernanceDomainDefinition.ALL.getDomainIdentifier(),
                    null),

    /**
     * finance
     */
    FINANCE("c2197558-57c4-47a8-aa76-8ebe24c10e3b",
            "finance",
            "Finance Zone",
            "Resources that support the financial management of Coco Pharmaceuticals.",
            "Resources controlled by the finance team.",
            GovernanceDomainDefinition.CORPORATE.getDomainIdentifier(),
            null),

    /**
     * infrastructure
     */
    INFRASTRUCTURE("3c45885f-2773-4108-9ef1-69d16e9157f7",
                   "infrastructure",
                   "IT Infrastructure Zone",
                   "Resources that describe the IT infrastructure such as hosts, servers, applications, " +
                           "databases and network infrastructure descriptions.",
                   "Resources controlled by the IT Infrastructure team.",
                   GovernanceDomainDefinition.IT_GOVERNANCE.getDomainIdentifier(),
                   null),

    /**
     * machines
     */
    IT_MACHINES("a5b7215e-f991-4976-ad98-56a5514477f9",
                "it-machines",
                "IT Machines Zone",
                "Resources that describe the IT machines (hosts) supporting the Coco Pharmaceuticals' operations.",
                "Hosts controlled by the IT Infrastructure team.",
                GovernanceDomainDefinition.IT_GOVERNANCE.getDomainIdentifier(),
                INFRASTRUCTURE),

    /**
     * business-systems
     */
    BUSINESS_SYSTEMS("b5662be2-f7c0-4fe9-b8af-bb5e7a588e77",
                     "business-systems",
                     "Business Systems Zone",
                     "Systems running applications that support the Coco Pharmaceuticals' business operations.",
                     "Business systems controlled by the IT Infrastructure team.",
                     GovernanceDomainDefinition.IT_GOVERNANCE.getDomainIdentifier(),
                     INFRASTRUCTURE),

    /**
     * manufacturing-systems
     */
    MANUFACTURING_SYSTEMS("3f5661df-30f9-4b01-baec-f582596ff11b",
                          "manufacturing-systems",
                          "Manufacturing Systems Zone",
                          "Systems running applications that support the Coco Pharmaceuticals' manufacturing operations.",
                          "Manufacturing systems controlled by the IT Infrastructure team.",
                          GovernanceDomainDefinition.IT_GOVERNANCE.getDomainIdentifier(),
                          INFRASTRUCTURE),

    /**
     * compliance-systems
     */
    COMPLIANCE_SYSTEMS("07d98454-ca3d-415d-a1c4-c0db7e574f6f",
                        "compliance-systems",
                        "Compliance Systems Zone",
                        "Systems running applications that support the Coco Pharmaceuticals' governance and compliance operations.",
                        "Compliance systems controlled by the IT Infrastructure team.",
                       GovernanceDomainDefinition.IT_GOVERNANCE.getDomainIdentifier(),
                       INFRASTRUCTURE),

    /**
     * security-systems
     */
    SECURITY_SYSTEMS("2db896ba-9a2a-45e5-9cbb-ff427f28e014",
                     "security-systems",
                     "Security Systems Zone",
                     "Systems running applications that support the Coco Pharmaceuticals' security operations.",
                     "Security systems controlled by the IT Infrastructure team.",
                     GovernanceDomainDefinition.IT_GOVERNANCE.getDomainIdentifier(),
                     INFRASTRUCTURE),

    /**
     * depot-systems
     */
    DEPOT_SYSTEMS("4d5578c5-28f7-48e9-9c0c-7dd7500761c4",
                 "depot-systems",
                 "Depot Systems Zone",
                 "Systems running applications that support the Coco Pharmaceuticals' depot operations.",
                 "Depot systems controlled by the IT Infrastructure team.",
                  GovernanceDomainDefinition.IT_GOVERNANCE.getDomainIdentifier(),
                  INFRASTRUCTURE),

    /**
     * it-development
     */
    DEVELOPMENT("19557672-6eec-43dd-ae32-ab998dd00f40",
                "it-development",
                "IT Development and DevOps Zone",
                "Software components and assets that support their ongoing development.",
                "Software development and dev-ops resources.",
                GovernanceDomainDefinition.SOFTWARE_DEVELOPMENT.getDomainIdentifier(),
                null),

    /**
     * manufacturing
     */
    MANUFACTURING("aa66d6ee-6ee9-4602-9f4a-f4021a0f8138",
                  "manufacturing",
                  "Supply, Manufacturing and Distribution Zone",
                  "Suppliers, manufacturing infrastructure, schedules and outputs.",
                  "These are the resources that support the production of Coco Pharmaceutical's products.",
                  GovernanceDomainDefinition.ALL.getDomainIdentifier(),
                  null),

    /**
     * sales
     */
    SALES("c7a068d8-68c9-4eb4-98c6-fd7aa7407b97",
          "sales",
          "Sales Zone",
          "Customers, sales plans, orders and fulfilment tracking.",
          "Resources supported by the sales teams.",
          GovernanceDomainDefinition.ALL.getDomainIdentifier(),
          null),

    /**
     * governance
     */
    GOVERNANCE("c67fe09c-6d6b-407d-bd78-931444a9c695",
               "governance",
               "Governance Zone",
               "Governance definitions, monitoring and reporting assets.",
               "Resources that support the governance team",
               GovernanceDomainDefinition.ALL.getDomainIdentifier(),
               null),

    /**
     * sustainability
     */
    SUSTAINABILITY("13c0b414-f96a-47bf-8a81-eda217c498e1",
                   "sustainability",
                   "Sustainability Reporting",
                   "Resources used to build reports that illustrate the status of Coco Pharmaceuticals' greenhouse gas emissions and sustainability initiatives.",
                   "Activity data that relates to sustainability",
                   SustainabilityDomainDefinition.SUSTAINABILITY_REPORTING.getDomainIdentifier(),
                   null),
    ;


    private final String guid;
    private final String zoneName;
    private final String displayName;
    private final String description;
    private final String criteria;
    private final int    domainIdentifier;

    private final CocoGovernanceZoneDefinition parentZone;

    /**
     * CocoGovernanceZoneDefinition constructor creates an instance of the enum
     *
     * @param zoneName    unique Id for the zone
     * @param displayName text for the zone
     * @param description description of the assets in the zone
     * @param criteria    criteria for inclusion
     * @param domainIdentifier domain identifier for the zone
     * @param parentZone  parent zone definition
     */
    CocoGovernanceZoneDefinition(String                       guid,
                                 String                       zoneName,
                                 String                       displayName,
                                 String                       description,
                                 String                       criteria,
                                 int                          domainIdentifier,
                                 CocoGovernanceZoneDefinition parentZone)
    {
        this.guid             = guid;
        this.zoneName         = zoneName;
        this.displayName      = displayName;
        this.description      = description;
        this.criteria         = criteria;
        this.domainIdentifier = domainIdentifier;
        this.parentZone       = parentZone;
    }


    /**
     * Returns the unique identifier for the zone entity.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Returns the unique name for the zone entity.
     *
     * @return qualified name
     */
    public String getQualifiedName()
    {
        return "GovernanceZone::" + zoneName;
    }


    /**
     * Returns the unique name for the zone.
     *
     * @return zone name
     */
    public String getZoneName()
    {
        return zoneName;
    }


    /**
     * Returns a descriptive name of the zone.
     *
     * @return display name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Returns a detailed description of the assets within the zone.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Returns a description of the criteria for including assets in the zone.
     *
     * @return criteria
     */
    public String getCriteria()
    {
        return criteria;
    }


    /**
     * Returns the domain identifier for the zone.
     *
     * @return domain identifier
     */
    public int getDomainIdentifier()
    {
        return domainIdentifier;
    }


    /**
     * Returns the unique identifier for the parent zone entity.
     *
     * @return string - or null if no parent zone
     */
    public String getParentZoneGUID()
    {
        if (parentZone != null)
        {
            return parentZone.getGUID();
        }

        return null;
    }
}
