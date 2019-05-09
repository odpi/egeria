/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * LicenseMapper provides property name mapping for Licenses.
 * These are relationships from Asset to a LicenseType and indicates
 * that the Asset has associated terms and conditions.
 */
public class LicenseMapper
{
    public static final String LICENSE_TYPE_TYPE_GUID                 = "046a049d-5f80-4e5b-b0ae-f3cf6009b513";
    public static final String LICENSE_TYPE_TYPE_NAME                 = "LicenseType";
    /* GovernanceDefinition */

    public static final String DETAILS_PROPERTY_NAME                  = "details";             /* from LicenseType entity */

    public static final String LICENSE_OF_REFERENCEABLE_TYPE_GUID     = "35e53b7f-2312-4d66-ae90-2d4cb47901ee";
    public static final String LICENSE_OF_REFERENCEABLE_TYPE_NAME     = "License";
    /* End1 = Referenceable; End 2 = LicenseType */

    public static final String LICENSE_GUID_PROPERTY_NAME             = "licenseGUID";         /* from License relationship */
    public static final String START_PROPERTY_NAME                    = "start";               /* from License relationship */
    public static final String END_PROPERTY_NAME                      = "end";                 /* from License relationship */
    public static final String CONDITIONS_PROPERTY_NAME               = "conditions";          /* from License relationship */
    public static final String LICENSED_BY_PROPERTY_NAME             = "licensedBy";          /* from License relationship */
    public static final String CUSTODIAN_PROPERTY_NAME                = "custodian";           /* from License relationship */
    public static final String RECIPIENT_PROPERTY_NAME                = "licensee";            /* from License relationship */
    public static final String NOTES_PROPERTY_NAME                    = "notes";               /* from License relationship */
}
