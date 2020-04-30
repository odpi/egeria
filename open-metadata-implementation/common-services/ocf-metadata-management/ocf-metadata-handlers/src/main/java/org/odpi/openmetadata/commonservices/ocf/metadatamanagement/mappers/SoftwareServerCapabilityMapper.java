/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * SoftwareServerCapabilityMapper provides property name mapping for Software Server Capabilities.
 */
public class SoftwareServerCapabilityMapper
{
    public static final String SOFTWARE_SERVER_CAPABILITY_TYPE_GUID   = "fe30a033-8f86-4d17-8986-e6166fa24177";
    public static final String SOFTWARE_SERVER_CAPABILITY_TYPE_NAME   = "SoftwareServerCapability";

    public static final String QUALIFIED_NAME_PROPERTY_NAME           = "qualifiedName";               /* from Referenceable entity */
    public static final String DISPLAY_NAME_PROPERTY_NAME             = "name";                        /* from SoftwareServerCapability entity */
    public static final String DESCRIPTION_PROPERTY_NAME              = "description";                 /* from SoftwareServerCapability entity */
    public static final String TYPE_PROPERTY_NAME                     = "type";                        /* from SoftwareServerCapability entity */
    public static final String VERSION_PROPERTY_NAME                  = "version";                     /* from SoftwareServerCapability entity */
    public static final String PATCH_LEVEL_PROPERTY_NAME              = "patchLevel";                  /* from SoftwareServerCapability entity */
    public static final String SOURCE_PROPERTY_NAME                   = "source";                      /* from SoftwareServerCapability entity */

    public static final String SUPPORTED_CAPABILITY_TYPE_GUID         = "8b7d7da5-0668-4174-a43b-8f8c6c068dd0";
    public static final String SUPPORTED_CAPABILITY_TYPE_NAME         = "SoftwareServerSupportedCapability";
    /* End 1 = SoftwareServer; End 2 = SoftwareServerCapability */

    public static final String DEPLOYMENT_TIME_PROPERTY_NAME          = "deploymentTime";          /* from SoftwareServerSupportedCapability */
    public static final String DEPLOYER_PROPERTY_NAME                 = "deployer";                /* from SoftwareServerSupportedCapability */
    public static final String SERVER_CAPABILITY_STATUS_PROPERTY_NAME = "serverCapabilityStatus";  /* from SoftwareServerSupportedCapability */

}
