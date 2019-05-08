/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * CertificationMapper provides property name mapping for Certifications
 * These are relationships from Asset to a certification type and indicates
 * that the Asset is certified.
 */
public class CertificationMapper
{
    public static final String CERTIFICATION_TYPE_TYPE_GUID              = "97f9ffc9-e2f7-4557-ac12-925257345eea";
    public static final String CERTIFICATION_TYPE_TYPE_NAME              = "CertificationType";
    /* GovernanceDefinition */

    public static final String DETAILS_PROPERTY_NAME                     = "details";                   /* from CertificationType entity */

    public static final String CERTIFICATION_OF_REFERENCEABLE_TYPE_GUID  = "390559eb-6a0c-4dd7-bc95-b9074caffa7f";
    public static final String CERTIFICATION_OF_REFERENCEABLE_TYPE_NAME  = "Certification";
    /* End1 = Referenceable; End 2 = CertificationType */

    public static final String CERTIFICATE_GUID_PROPERTY_NAME            = "certificateGUID";           /* from Certification relationship */
    public static final String START_PROPERTY_NAME                       = "start";                     /* from Certification relationship */
    public static final String END_PROPERTY_NAME                         = "end";                       /* from Certification relationship */
    public static final String CONDITIONS_PROPERTY_NAME                  = "conditions";                /* from Certification relationship */
    public static final String CERTIFIED_BY_PROPERTY_NAME                = "certifiedBy";               /* from Certification relationship */
    public static final String CUSTODIAN_PROPERTY_NAME                   = "custodian";                 /* from Certification relationship */
    public static final String RECIPIENT_PROPERTY_NAME                   = "recipient";                 /* from Certification relationship */
    public static final String NOTES_PROPERTY_NAME                       = "notes";                     /* from Certification relationship */

    public static final String CERTIFICATION_LINK_TYPE_GUID  = "390559eb-6a0c-4dd7-bc95-b9074caffa7f";
    public static final String CERTIFICATION_LINK_TYPE_NAME  = "Certification";
    /* End1 = CertificationType; End 2 = CertificationType */

}
