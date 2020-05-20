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
    /* Referenceable */

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

    public static final String APPLICATION_TYPE_GUID                  = "58280f3c-9d63-4eae-9509-3f223872fb25";
    public static final String APPLICATION_TYPE_NAME                  = "Application";
    /* SoftwareServerCapability */

    public static final String ENGINE_TYPE_GUID                       = "3566527f-b1bd-4e7a-873e-a3e04d5f2a14";
    public static final String ENGINE_TYPE_NAME                       = "Engine";
    /* SoftwareServerCapability */

    public static final String WORKFLOW_ENGINE_CLASSIFICATION_GUID   = "37a6d212-7c4a-4a82-b4e2-601d4358381c";
    public static final String WORKFLOW_ENGINE_CLASSIFICATION_NAME   = "WorkflowEngine";
    /* Engine */

    public static final String REPORTING_ENGINE_CLASSIFICATION_GUID   = "e07eefaa-16e0-46cf-ad54-bed47fb15812";
    public static final String REPORTING_ENGINE_CLASSIFICATION_NAME   = "ReportingEngine";
    /* Engine */

    public static final String ANALYTICS_ENGINE_CLASSIFICATION_GUID   = "1a0dc6f6-7980-42f5-98bd-51e56543a07e";
    public static final String ANALYTICS_ENGINE_CLASSIFICATION_NAME   = "AnalyticsEngine";
    /* Engine */

    public static final String DATA_MOVEMENT_ENGINE_CLASSIFICATION_GUID = "d2ed6621-9d99-4fe8-843a-b28d816cf888";
    public static final String DATA_MOVEMENT_ENGINE_CLASSIFICATION_NAME = "DataMovementEngine";
    /* Engine */

    public static final String DATA_VIRTUALIZATION_ENGINE_CLASSIFICATION_GUID = "03e25cd0-03d7-4d96-b28b-eed671824ed6";
    public static final String DATA_VIRTUALIZATION_ENGINE_CLASSIFICATION_NAME = "DataVirtualizationEngine";
    /* Engine */

    public static final String NETWORK_GATEWAY_TYPE_GUID             = "9bbae94d-e109-4c96-b072-4f97123f04fd";
    public static final String NETWORK_GATEWAY_TYPE_NAME             = "NetworkGateway";
    /* SoftwareServerCapability */

    public static final String CLOUD_SERVICE_CLASSIFICATION_GUID     = "337e7b1a-ad4b-4818-aa3e-0ff3307b2fbe6";
    public static final String CLOUD_SERVICE_CLASSIFICATION_NAME     = "CloudService";
    /* SoftwareServerCapability */

    public static final String OFFERING_NAME_PROPERTY_NAME           = "offeringName";          /* from CloudService */
    public static final String SERVICE_TYPE_PROPERTY_NAME            = "type";                  /* from CloudService */

    public static final String FILE_SYSTEM_CLASSIFICATION_GUID       = "cab5ba1d-cfd3-4fca-857d-c07711fc4157";
    public static final String FILE_SYSTEM_CLASSIFICATION_NAME       = "FileSystem";
    /* SoftwareServerCapability */

    public static final String FORMAT_PROPERTY_NAME                  = "format";                /* from FileSystem */
    public static final String ENCRYPTION_PROPERTY_NAME              = "encryption";            /* from FileSystem */

    public static final String NOTIFICATION_MANAGER_CLASSIFICATION_GUID   = "3e7502a7-396a-4737-a106-378c9c94c1057";
    public static final String NOTIFICATION_MANAGER_CLASSIFICATION_NAME   = "NotificationManager";
    /* SoftwareServerCapability */

    public static final String ENTERPRISE_ACCESS_LAYER_TYPE_GUID     = "39444bf9-638e-4124-a5f9-1b8f3e1b008b";
    public static final String ENTERPRISE_ACCESS_LAYER_TYPE_NAME     = "EnterpriseAccessLayer";
    /* SoftwareServerCapability */

    public static final String TOPIC_ROOT_PROPERTY_NAME              = "topicRoot";            /* from EnterpriseAccessLayer */
    public static final String METADATA_COLLECTION_ID_PROPERTY_NAME  = "metadataCollectionId"; /* from EnterpriseAccessLayer */

    public static final String COHORT_MEMBER_TYPE_GUID               = "42063797-a78a-4720-9353-52026c75f667";
    public static final String COHORT_MEMBER_TYPE_NAME               = "CohortMember";
    /* SoftwareServerCapability */

    public static final String EVENT_VERSION_PROPERTY_NAME           = "version";            /* from CohortMember */

    public static final String OPEN_DISCOVERY_ENGINE_TYPE_GUID       = "be650674-790b-487a-a619-0a9002488055";
    public static final String OPEN_DISCOVERY_ENGINE_TYPE_NAME       = "OpenDiscoveryEngine";
    /* SoftwareServerCapability */

    public static final String METADATA_INTEGRATION_CAPABILITY_TYPE_GUID = "cc6d2d77-626c-4d0d-aa22-0a491b5fee94";
    public static final String METADATA_INTEGRATION_CAPABILITY_TYPE_NAME = "MetadataIntegrationCapability";
    /* SoftwareServerCapability */

    public static final String DATA_PLATFORM_INTEGRATION_CLASSIFICATION_GUID   = "2356af59-dda5-45ad-927f-540bed6b281d";
    public static final String DATA_PLATFORM_INTEGRATION_CLASSIFICATION_NAME   = "DataPlatformIntegration";
    /* MetadataIntegrationCapability */

    public static final String DATA_ENGINE_INTEGRATION_CLASSIFICATION_GUID   = "2356af59-dda5-45ad-927f-540bed6b281d";
    public static final String DATA_ENGINE_INTEGRATION_CLASSIFICATION_NAME   = "DataEngineIntegration";
    /* MetadataIntegrationCapability */

    public static final String DATABASE_PLATFORM_TYPE_GUID           = "68b35c1e-6c28-4ac3-94f9-2c3dbcbb79e9";
    public static final String DATABASE_PLATFORM_TYPE_NAME           = "DatabasePlatform";
    /* SoftwareServerCapability */

}
