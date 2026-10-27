/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.core;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.GovernanceDomain;

import java.util.List;


/**
 * The CertificationTypeDefinition is used to feed the definition of the standard certification types that
 * organizations commonly work towards.  Only the identity and purpose of each standard is described - the text of
 * the standards themselves is owned by the standards bodies.
 */
public enum CertificationTypeDefinition
{
    /**
     * ISO/IEC 27001:2022 Information Security Management Systems
     */
    ISO_IEC_27001("f51e1e11-62c7-4148-8104-4477551e446d",
                  "ISO/IEC 27001:2022",
                  "ISO/IEC 27001:2022 - Information Security Management System Certification",
                  "The international standard for establishing, implementing, maintaining and continually improving an information security management system (ISMS), against which an organization can be independently certified.",
                  "ISO/IEC 27001 sets out the requirements for an information security management system.  Its Annex A provides a reference set of information security controls covering organizational, people, physical and technological aspects of security.  An organization records which of these controls apply to it, why, and how far each one has been implemented in a statement of applicability.  Certification is awarded, and re-audited at regular surveillance audits, by an accredited certification body.",
                  "The information security management system of an organization, or the part of it that is named in the scope of the certification.",
                  "Create a StatementOfApplicability for the scope of the information security management system, and link each Annex A control to it with the ControlApplicability relationship.  Record the certificate awarded with the Certification relationship.",
                  GovernanceDomain.SECURITY.getOrdinal(),
                  "https://www.iso.org/standard/27001",
                  List.of("A statement of applicability must be maintained, recording which Annex A controls apply and how they are implemented",
                          "The information security management system must pass an initial certification audit and regular surveillance audits",
                          "Information security risks must be assessed, and the controls chosen to treat them must be traceable to the risks")),
    ;


    private final String       guid;
    private final String       identifier;
    private final String       displayName;
    private final String       summary;
    private final String       description;
    private final String       scope;
    private final String       usage;
    private final int          domainIdentifier;
    private final String       url;
    private final List<String> implications;


    /**
     * The constructor creates an instance of the enum
     *
     * @param guid             unique id for the enum
     * @param identifier       identifier of the standard
     * @param displayName      title of the certification type
     * @param summary          short description
     * @param description      longer description
     * @param scope            what can be certified
     * @param usage            how to use this certification type
     * @param domainIdentifier governance domain
     * @param url              link to the standard
     * @param implications     what an organization must do to be certified
     */
    CertificationTypeDefinition(String       guid,
                                String       identifier,
                                String       displayName,
                                String       summary,
                                String       description,
                                String       scope,
                                String       usage,
                                int          domainIdentifier,
                                String       url,
                                List<String> implications)
    {
        this.guid             = guid;
        this.identifier       = identifier;
        this.displayName      = displayName;
        this.summary          = summary;
        this.description      = description;
        this.scope            = scope;
        this.usage            = usage;
        this.domainIdentifier = domainIdentifier;
        this.url              = url;
        this.implications     = implications;
    }


    /**
     * Return the unique name of the certification type.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "CertificationType::" + identifier;
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
     * Return the identifier of the standard.
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
     * Return the description.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return what can be certified.
     *
     * @return string
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Return how to use this certification type.
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
        return domainIdentifier;
    }


    /**
     * Return the link to the standard.
     *
     * @return string
     */
    public String getURL()
    {
        return url;
    }


    /**
     * Return what an organization must do to be certified.
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
        return "CertificationTypeDefinition{" + displayName + '}';
    }
}
