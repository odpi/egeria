/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * SoftwareServerMapper provides additional property name mapping for software server over and above assets.
 */
public class SoftwareServerMapper
{
    public static final String SOFTWARE_SERVER_TYPE_GUID                 = "896d14c2-7522-4f6c-8519-757711943fe6";
    public static final String SOFTWARE_SERVER_TYPE_NAME                 = "SoftwareServer";
    /* ITInfrastructure */

    public static final String TYPE_PROPERTY_NAME            = "type";      /* from SoftwareServer entity */
    public static final String VERSION_PROPERTY_NAME         = "version";   /* from SoftwareServer entity */
    public static final String SOURCE_PROPERTY_NAME          = "source";    /* from SoftwareServer entity */
    public static final String USER_ID_PROPERTY_NAME         = "userId";    /* from SoftwareServer entity */


    public static final String SERVER_ENDPOINT_TYPE_GUID     = "2b8bfab4-8023-4611-9833-82a0dc95f187";
    public static final String SERVER_ENDPOINT_TYPE_NAME     = "ServerEndpoint";
    /* End 1 = SoftwareServer; End 2 = Endpoint */

    public static final String SERVER_DEPLOYMENT_TYPE_GUID   = "d909eb3b-5205-4180-9f63-122a65b30738";
    public static final String SERVER_DEPLOYMENT_TYPE_NAME   = "SoftwareServerDeployment";
    /* End 1 = SoftwareServerPlatform; End 2 = SoftwareServer */

    public static final String DEPLOYMENT_TIME_PROPERTY_NAME          = "deploymentTime";          /* from SoftwareServerDeployment */
    public static final String DEPLOYER_PROPERTY_NAME                 = "deployer";                /* from SoftwareServerDeployment */
    public static final String SERVER_CAPABILITY_STATUS_PROPERTY_NAME = "serverCapabilityStatus";  /* from SoftwareServerDeployment */
}
