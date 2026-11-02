/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.core;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.GovernanceDomain;

import java.util.List;


/**
 * The StrideThreatDefinition is used to feed the definition of the six categories of threat in the STRIDE threat
 * modelling method.  They are shared by every threat model: each risk found during threat modelling is linked to
 * the STRIDE threat that gives rise to it with the GovernanceResponse relationship.
 */
public enum StrideThreatDefinition
{
    /**
     * Spoofing
     */
    SPOOFING("f10eccb4-c490-400e-83f0-cc4d92861798",
             "Spoofing",
             "STRIDE - Spoofing",
             "An actor successfully identifies as another user, component or system by falsifying data, to gain an illegitimate advantage.",
             "Ask of each component and data flow: could an actor pretend to be another user, component or system, and how are callers authenticated?",
             List.of("Every user, component and system that calls another must be authenticated",
                     "Credentials and other secrets must be protected, rotated and removed when no longer needed")),

    /**
     * Tampering
     */
    TAMPERING("fdba5fe4-e704-47bd-8836-ab6cd31b77ba",
              "Tampering",
              "STRIDE - Tampering",
              "Data or software is modified without authorization, whether at rest, in transit or as it is built and deployed.",
              "Ask of each component and data flow: could data or software be changed without authorization - at rest, in transit or in the build pipeline?",
              List.of("Changes to data and software must be authorized",
                      "The integrity of data and software must be verifiable")),

    /**
     * Repudiation
     */
    REPUDIATION("0ba46e73-704b-4efa-8be9-d2a15b4d707d",
                "Repudiation",
                "STRIDE - Repudiation",
                "An actor denies having performed an action, and there is no reliable evidence to show otherwise.",
                "Ask of each component and data flow: could someone deny an action because there is no reliable record of who did it?",
                List.of("Significant actions must be recorded with who performed them and when",
                        "Audit records must be protected from alteration")),

    /**
     * Information disclosure
     */
    INFORMATION_DISCLOSURE("26d74bae-f781-4029-8e80-6825e831226a",
                           "InformationDisclosure",
                           "STRIDE - Information Disclosure",
                           "Information is revealed to individuals or systems that are not authorized to see it.",
                           "Ask of each component and data flow: could data be read by someone who should not see it - including configuration, credentials and logs?",
                           List.of("Access to sensitive data must be authorized and limited to what is needed",
                                   "Sensitive data must be protected at rest and in transit")),

    /**
     * Denial of service
     */
    DENIAL_OF_SERVICE("916a676e-53fc-4de1-bd1b-aa33916d680f",
                      "DenialOfService",
                      "STRIDE - Denial of Service",
                      "Legitimate users are prevented from using a system or service.",
                      "Ask of each component and data flow: could the component be made unavailable, or starve others of resources?",
                      List.of("Services that the business depends on must be protected from overload and exhaustion",
                              "Recovery objectives must be defined and tested")),

    /**
     * Elevation of privilege
     */
    ELEVATION_OF_PRIVILEGE("4382723c-b8e5-4ee4-bae2-46503d92ca67",
                           "ElevationOfPrivilege",
                           "STRIDE - Elevation of Privilege",
                           "An actor gains access to a system or resource with higher privileges than they were granted.",
                           "Ask of each component and data flow: could an actor gain more access than they were granted?",
                           List.of("Users, components and systems must run with the least privilege they need",
                                   "Privileged access must be separated, controlled and reviewed")),
    ;


    private final String       guid;
    private final String       identifier;
    private final String       displayName;
    private final String       summary;
    private final String       usage;
    private final List<String> implications;


    /**
     * The constructor creates an instance of the enum
     *
     * @param guid         unique id for the enum
     * @param identifier   identifier of the threat category
     * @param displayName  title of the threat
     * @param summary      short description
     * @param usage        the question to ask during threat modelling
     * @param implications what must be true to guard against the threat
     */
    StrideThreatDefinition(String       guid,
                           String       identifier,
                           String       displayName,
                           String       summary,
                           String       usage,
                           List<String> implications)
    {
        this.guid         = guid;
        this.identifier   = identifier;
        this.displayName  = displayName;
        this.summary      = summary;
        this.usage        = usage;
        this.implications = implications;
    }


    /**
     * Return the unique name of the threat.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "Threat::STRIDE::" + identifier;
    }


    /**
     * Return the GUID for the element.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the identifier of the threat category.
     *
     * @return string
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Return the display name.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the short description.
     *
     * @return string
     */
    public String getSummary()
    {
        return summary;
    }


    /**
     * Return the question to ask during threat modelling.
     *
     * @return string
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Return the identifier of the governance domain.
     *
     * @return int
     */
    public int getDomainIdentifier()
    {
        return GovernanceDomain.SECURITY.getOrdinal();
    }


    /**
     * Return what must be true to guard against the threat.
     *
     * @return list
     */
    public List<String> getImplications()
    {
        return implications;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "StrideThreatDefinition{" + displayName + '}';
    }
}
