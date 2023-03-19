/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.assetcatalog.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constants for Open Metadata Types (names and guid)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    public static final String REFERENCEABLE = "Referenceable";
    public static final String ASSET_ZONE_MEMBERSHIP = "AssetZoneMembership";
    public static final String ASSET_GUID = "896d14c2-7522-4f6c-8519-757711943fe6";
    public static final String SCHEMA_ELEMENT_GUID = "718d4244-8559-49ed-ad5a-10e5c305a656";
    public static final String SCHEMA_ELEMENT = "SchemaElement";
    public static final String GLOSSARY_TERM = "GlossaryTerm";
    public static final String GLOSSARY_TERM_TYPE_GUID = "0db3e6ec-f5ef-4d75-ae38-b7ee6fd6ec0a";
    public static final String COMPLEX_SCHEMA_TYPE = "ComplexSchemaType";
    public static final String ASSET = "Asset";
    public static final String SCHEMA_ATTRIBUTE = "SchemaAttribute";
    public static final String NESTED_SCHEMA_ATTRIBUTE = "NestedSchemaAttribute";
    public static final String NESTED_SCHEMA_ATTRIBUTE_GUID = "0ffb9d87-7074-45da-a9b0-ae0859611133";
    public static final String DEPLOYED_API = "DeployedAPI";
    public static final String IT_INFRASTRUCTURE = "ITInfrastructure";
    public static final String PROCESS = "Process";
    public static final String DATA_STORE = "DataStore";
    public static final String DATA_SET = "DataSet";
    public static final String DATABASE = "Database";
    public static final String DATA_FILE = "DataFile";
    public static final String FILE_FOLDER = "FileFolder";
    public static final String PORT_IMPLEMENTATION = "PortImplementation";
    public static final String HOST = "Host";
    public static final String NETWORK = "Network";
    public static final String LOCATION = "Location";
    public static final String ENDPOINT = "Endpoint";
    public static final String CONNECTION = "Connection";
    public static final String SOFTWARE_SERVER_PLATFORM = "SoftwareServerPlatform";
    public static final String SOFTWARE_SERVER = "SoftwareServer";
    public static final String HOST_CLUSTER = "HostCluster";
    public static final String VIRTUAL_CONTAINER = "VirtualContainer";
    //Relationships Type
    public static final String ASSET_SCHEMA_TYPE_GUID = "815b004d-73c6-4728-9dd9-536f4fe803cd";
    public static final String ASSET_SCHEMA_TYPE = "AssetSchemaType";
    public static final String PORT_SCHEMA_GUID = "B216fA00-8281-F9CC-9911-Ae6377f2b457";
    public static final String PORT_SCHEMA = "PortSchema";
    public static final String SOFTWARE_SERVER_PLATFORM_DEPLOYMENT_GUID = "b909eb3b-5205-4180-9f63-122a65b30738";
    public static final String SOFTWARE_SERVER_PLATFORM_DEPLOYMENT = "SoftwareServerPlatformDeployment";
    public static final String HOST_OPERATING_PLATFORM_GUID = "b9179df5-6e23-4581-a8b0-2919e6322b12";
    public static final String HOST_OPERATING_PLATFORM = "HostOperatingPlatform";
    public static final String SERVER_ENDPOINT_GUID = "2b8bfab4-8023-4611-9833-82a0dc95f187";
    public static final String SERVER_ENDPOINT = "ServerEndpoint";
    public static final String SOFTWARE_SERVER_DEPLOYMENT_GUID = "d909eb3b-5205-4180-9f63-122a65b30738";
    public static final String SOFTWARE_SERVER_DEPLOYMENT = "SoftwareServerDeployment";
    public static final String CONNECTION_TO_ASSET_GUID = "e777d660-8dbe-453e-8b83-903771f054c0";
    public static final String CONNECTION_TO_ASSET = "ConnectionToAsset";
    public static final String CONNECTION_CONNECTOR_TYPE_GUID = "e542cfc1-0b4b-42b9-9921-f0a5a88aaf96";
    public static final String CONNECTION_CONNECTOR_TYPE = "ConnectionConnectorType";
    public static final String DATA_CONTENT_FOR_DATA_SET_GUID = "b827683c-2924-4df3-a92d-7be1888e23c0";
    public static final String DATA_CONTENT_FOR_DATA_SET = "DataContentForDataSet";
    public static final String FOLDER_HIERARCHY_GUID = "48ac9028-45dd-495d-b3e1-622685b54a01";
    public static final String FOLDER_HIERARCHY = "FolderHierarchy";
    public static final String NESTED_FILE_GUID = "4cb88900-1446-4eb6-acea-29cd9da45e63";
    public static final String NESTED_FILE = "NestedFile";
    public static final String PROCESS_PORT_GUID = "fB4E00CF-37e4-88CE-4a94-233BAdB84DA2";
    public static final String PROCESS_PORT = "ProcessPort";
    public static final String NETWORK_GATEWAY_LINK_GUID = "5bece460-1fa6-41fb-a29f-fdaf65ec8ce3";
    public static final String NETWORK_GATEWAY_LINK = "NetworkGatewayLink";
    public static final String HOST_NETWORK_GUID = "f2bd7401-c064-41ac-862c-e5bcdc98fa1e";
    public static final String HOST_NETWORK = "HostNetwork";
    public static final String DEPLOYED_VIRTUAL_CONTAINER_GUID = "4b981d89-e356-4d9b-8f17-b3a8d5a86676";
    public static final String DEPLOYED_VIRTUAL_CONTAINER = "DeployedVirtualContainer";
    public static final String HOST_CLUSTER_MEMBER_GUID = "1a1c3933-a583-4b0c-9e42-c3691296a8e0";
    public static final String HOST_CLUSTER_MEMBER = "HostClusterMember";
    public static final String HOST_LOCATION_GUID = "f3066075-9611-4886-9244-32cc6eb07ea9";
    public static final String HOST_LOCATION = "HostLocation";
    public static final String ASSET_LOCATION_GUID = "bc236b62-d0e6-4c5c-93a1-3a35c3dba7b1";
    public static final String ASSET_LOCATION = "AssetLocation";
    public static final String API_ENDPOINT_GUID = "de5b9501-3ad4-4803-a8b2-e311c72a4336";
    public static final String API_ENDPOINT = "APIEndpoint";
    public static final String CONNECTION_ENDPOINT_GUID = "887a7132-d6bc-4b92-a483-e80b60c86fb2";
    public static final String CONNECTION_ENDPOINT = "ConnectionEndpoint";
    public static final String SEMANTIC_ASSIGNMENT_GUID = "e6670973-645f-441a-bec7-6f5570345b92";
    public static final String SEMANTIC_ASSIGNMENT = "SemanticAssignment";
    public static final String SCHEMA_ATTRIBUTE_TYPE_GUID = "2d955049-e59b-45dd-8e62-cde1add59f9e";
    public static final String SCHEMA_ATTRIBUTE_TYPE = "SchemaAttributeType";
    public static final String ATTRIBUTE_FOR_SCHEMA_GUID = "86b176a2-015c-44a6-8106-54d5d69ba661";
    public static final String ATTRIBUTE_FOR_SCHEMA = "AttributeForSchema";
    //Instance Properties fields
    public static final String QUALIFIED_NAME = "qualifiedName";
    public static final String NAME = "name";
    public static final String DISPLAY_NAME = "displayName";
    public static final String TYPE_SEQUENCING = "type";

    public static final String GUID_PARAMETER = "GUID";
    public static final String SEARCH_PARAMETER = "searchParameter";
    public static final String ADDITIONAL_PROPERTIES_PROPERTY_NAME = "additionalProperties";
    public static final String SEARCH_STRING_PARAMETER_NAME = "searchCriteria";
}
