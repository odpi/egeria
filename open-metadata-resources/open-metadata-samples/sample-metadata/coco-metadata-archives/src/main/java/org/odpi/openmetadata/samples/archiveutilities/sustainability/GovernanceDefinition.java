/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.sustainability;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.archiveutilities.organization.ScopeDefinition;

import java.util.Arrays;
import java.util.List;


/**
 * The GovernanceDefinition is used to feed the definition of the Governance Definitions for
 * Coco Pharmaceuticals sustainability scenarios.
 */
public enum GovernanceDefinition
{
    SUSTAINABILITY_TREAT("4552f777-65d6-4706-b4bf-e9177660de88",
                         OpenMetadataType.THREAT.typeName,
                         "The price of ignoring sustainability",
                         "A poor record of sustainable operations will affect Coco Pharmaceuticals credit rating",
                         "Increasingly, financial institutions are viewing organizations with a lack of focus in sustainability as a poor risk for the future.",
                         "High",
                         new String[]{"Higher cost of doing business"},
                         null,
                         null,
                         SustainabilityDomainDefinition.SUSTAINABILITY_REPORTING.getDomainIdentifier(),
                         ScopeDefinition.ALL_COCO,
                         null),

    CSRD_REGULATION("2ba17e3c-825a-409b-928f-d0e46e741c11",
                    OpenMetadataType.REGULATION.typeName,
                    "Corporate Sustainability Reporting Directive (CSRD)",
                    "Details of the types of reporting that organizations must produce.",
                    null,
                    "High",
                    new String[]{"New data pipelines and dashboards need to be implemented"},
                    new String[]{"A greater awareness of sustainability will improve resource use"},
                    null,
                    SustainabilityDomainDefinition.SUSTAINABILITY_REPORTING.getDomainIdentifier(),
                    ScopeDefinition.ALL_COCO,
                    "https://finance.ec.europa.eu/capital-markets-union-and-financial-markets/company-reporting-and-auditing/company-reporting/corporate-sustainability-reporting_en"),

    SUSTAINABILITY_STRATEGY("667c6480-a90a-4fd4-9022-8da646e203d2",
                            OpenMetadataType.GOVERNANCE_STRATEGY.typeName,
                            "Operate Coco Pharmaceuticals in an increasingly sustainable way",
                            "Create a company wide sustainability initiative with rewards.",
                            "Use sustainability regulations to develop a program of education and initiatives that are designed to improve Coco's sustainability measurements with rewards for effective actions.",
                            "High",
                            new String[]{
                                    "A budget needs to be allocated.",
                                    "People need to be given time to participate.",
                                    "New communication paths need to be established.",
                                    "New responsibilities need to be established with key leaders.",
                                    "New employee rewards need to be designed and implemented"},
                            null,
                            null,
                            SustainabilityDomainDefinition.SUSTAINABILITY_REPORTING.getDomainIdentifier(),
                            ScopeDefinition.ALL_COCO,
                            null),

    GREENHOUSE_GASES_APPROACH("a68bd5ca-ec15-4f16-9cdc-b76735c35281",
                              OpenMetadataType.GOVERNANCE_APPROACH.typeName,
                              "Measure harmful emissions",
                              "Use the greenhouse gas protocol to measure and manage harmful emissions.",
                              "The greenhouse gas protocol (GHG) provides a staged approach to assessing the level of harmful emission caused by Coco's operations.  This allows the organization to understand its contribution to climate change and identify where the best place to focus on reducing the effect.",
                              "High",
                              new String[]{"A new reporting pipeline and dashboard is required to gather the data and display it to interested parties"},
                              null,
                              null,
                              SustainabilityDomainDefinition.SUSTAINABILITY_REPORTING.getDomainIdentifier(),
                              ScopeDefinition.ALL_COCO,
                              "https://ghgprotocol.org/"),

    AVOID_HARMFUL_MATERIALS("ed004f39-ab6e-47d7-bdc3-4fbaca213a07",
                            OpenMetadataType.GOVERNANCE_PRINCIPLE.typeName,
                            "Avoid using harmful materials",
                            "Avoid using materials that are known to be harmful to environment",
                            "Locate harmful materials, seek alternatives and switch.",
                            "High",
                            new String[]{"Budget is required to replace equipment, supplies and suppliers before the end of their natural life."},
                            null,
                            null,
                            SustainabilityDomainDefinition.SUSTAINABILITY_REPORTING.getDomainIdentifier(),
                            ScopeDefinition.ALL_COCO,
                            null),

    NEW_SUSTAINABILITY_DOMAIN("334fc1df-692a-4960-bbff-343bf7c486d2",
                              OpenMetadataType.GOVERNANCE_APPROACH.typeName,
                              "New Sustainability Governance Domain",
                              "Set up a new governance domain for sustainability to educate and drive action.",
                              "The new governance domain would provide a focal point for education, initiatives and awards that improve the sustainability of Coco Pharmaceuticals. particularly where cross-organizational collaboration is required.  It will include a leader, a community of advocates and location leaders to help drive initiatives across the organization",
                              "High",
                              new String[]{"Individuals in the company will be assigned new responsibilities to drive initiatives."},
                              null,
                              null,
                              SustainabilityDomainDefinition.SUSTAINABILITY_REPORTING.getDomainIdentifier(),
                              ScopeDefinition.ALL_COCO,
                              null),

    SUSTAINABILITY_LEADER_RESPONSIBILITY("27646717-3f23-496e-b35c-2772de3adfff",
                                         OpenMetadataType.GOVERNANCE_RESPONSIBILITY.typeName,
                                         "Sustainability leadership",
                                         "Lead the sustainability governance domain",
                                         "Lead the community of people who are identified as having responsibility for an aspect of sustainability.  Identify new initiatives and help to get them funded and underway.  Encourage an open conversation about sustainability at all levels of the company.",
                                         "High",
                                         new String[]{"This is a full-time role.  The individual assigned to the role will need board-level sponsorship because they will need to influence behaviour across all aspects of the business."},
                                         null,
                                         null,
                                         SustainabilityDomainDefinition.SUSTAINABILITY_REPORTING.getDomainIdentifier(),
                                         ScopeDefinition.ALL_COCO,
                                         null),

    SUSTAINABILITY_CHAMPION_RESPONSIBILITY("b73fb3aa-f964-4118-9797-81e222a858ce",
                                           OpenMetadataType.GOVERNANCE_RESPONSIBILITY.typeName,
                                           "Sustainability Champion",
                                           "Lead the local team(s) on their sustainability events and initiatives.",
                                           "Provide education and guidance to the people from the assigned business area on improving the sustainability of its operation.",
                                           "High",
                                           new String[]{"This person needs time and resources to set up community events.  They need to belong to a community of advocates that together develop material to support their advocacy."},
                                           null,
                                           null,
                                           SustainabilityDomainDefinition.SUSTAINABILITY_REPORTING.getDomainIdentifier(),
                                           ScopeDefinition.WITHIN_BUSINESS_CAPABILITY,
                                           null),

    SUSTAINABILITY_TECHNOLOGY_LEAD_RESPONSIBILITY("13a8b144-5f86-4197-9ea0-5aae95dc793e",
                                                  OpenMetadataType.GOVERNANCE_RESPONSIBILITY.typeName,
                                                  "Deliver Sustainability Reporting Capability",
                                                  "Operate an information supply chain to measure and monitor Coco Pharmaceuticals' operations for sustainability.",
                                                  null,
                                                  "High",
                                                  null,
                                                  null,
                                                  null,
                                                  SustainabilityDomainDefinition.SUSTAINABILITY_REPORTING.getDomainIdentifier(),
                                                  ScopeDefinition.WITHIN_FACILITY,
                                                  null),

    REMOVE_BAD_COOLING_UNITS("c4635d92-2082-4e01-a92a-f3714ff7a65f",
                             OpenMetadataType.GOVERNANCE_RESPONSIBILITY.typeName,
                             "Eliminate CFC/HFC Cooling Units",
                             "Identify and Replace cooling units using CFCs and HFCs.",
                             null,
                             "High",
                             null,
                             null,
                             null,
                             SustainabilityDomainDefinition.SUSTAINABILITY_REPORTING.getDomainIdentifier(),
                             ScopeDefinition.WITHIN_FACILITY,
                             null),


    ;


    private final String          guid;
    private final String          type;
    private final String          title;
    private final String          summary;
    private final String          description;
    private final String          importance;
    private final String[]        implications;
    private final String[]        outcomes;
    private final String[]        results;
    private final int             domain;
    private final ScopeDefinition scope;
    private final String          externalURL;


    /**
     * The constructor creates an instance of the enum
     *
     * @param guid            unique id for the enum
     * @param type           governance definition type
     * @param title          title the enum
     * @param summary        short description for the enum
     * @param description description of this definition
     * @param importance importance of this definition
     * @param implications implications of implementing this definition
     * @param outcomes expected outcomes
     * @param results results observed from following this definition
     * @param domain governance domain identifier
     * @param scope usage scope
     * @param externalURL link to external information
     */
    GovernanceDefinition(String          guid,
                         String          type,
                         String          title,
                         String          summary,
                         String          description,
                         String          importance,
                         String[]        implications,
                         String[]        outcomes,
                         String[]        results,
                         int             domain,
                         ScopeDefinition scope,
                         String          externalURL)
    {
        this.guid         = guid;
        this.type         = type;
        this.title        = title;
        this.summary      = summary;
        this.description  = description;
        this.importance   = importance;
        this.implications = implications;
        this.outcomes     = outcomes;
        this.results      = results;
        this.domain       = domain;
        this.scope        = scope;
        this.externalURL  = externalURL;
    }

    public String getQualifiedName()
    {
        return type + "::" + title;
    }


    public String getGUID()
    {
        return guid;
    }

    public String getType()
    {
        return type;
    }

    public String getTitle() { return title;
    }
    public String getSummary()
    {
        return summary;
    }


    public String getDescription()
    {
        return description;
    }

    public String getImportance() { return importance; }

    public List<String> getImplications()
    {
        if (implications != null)
        {
            return Arrays.asList(implications);
        }

        return null;
    }

    public List<String> getOutcomes()
    {
        if (outcomes != null)
        {
            return Arrays.asList(outcomes);
        }

        return null;
    }

    public List<String> getResults()
    {
        if (results != null)
        {
            return Arrays.asList(results);
        }

        return null;
    }

    public ScopeDefinition getScope() {return scope; }

    /**
     * Return the identifier of the domain that this subject area belongs - 0 means all domains
     *
     * @return domain identifier
     */
    public int getDomain()
    {
        return domain;
    }


    public String getExternalURL()
    {
        return externalURL;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "GovernanceDefinition{" + summary + '}';
    }
}
