/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization;

import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;

/**
 *	The class keeps identifiers used by synchronization feature.
 *	Any type GUID and name used by the feature should be referenced here to keep track.
 */
public class IdMap {

	public static final String CAPABILITY_TYPE_GUID = OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID;
	public static final String CAPABILITY_TYPE_NAME = OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME;
	
	public static final String DEPLOYED_SOFTWARE_COMPONENT_TYPE_NAME = OpenMetadataAPIMapper.DEPLOYED_SOFTWARE_COMPONENT_TYPE_NAME;

	public static final String ASSET_TYPE_GUID = OpenMetadataAPIMapper.ASSET_TYPE_GUID;
	public static final String ASSET_TYPE_NAME = OpenMetadataAPIMapper.ASSET_TYPE_NAME;
	public static final String SCHEMA_ATTRIBUTE_TYPE_GUID = OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_GUID;
	public static final String SCHEMA_ATTRIBUTE_TYPE_NAME = OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME;
	public static final String COMPLEX_SCHEMA_TYPE_TYPE_GUID = OpenMetadataAPIMapper.COMPLEX_SCHEMA_TYPE_TYPE_GUID;
	public static final String COMPLEX_SCHEMA_TYPE_TYPE_NAME = OpenMetadataAPIMapper.COMPLEX_SCHEMA_TYPE_TYPE_NAME;
    public static final String ASSET_TO_SCHEMA_TYPE_TYPE_GUID = OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID;
    public static final String ASSET_TO_SCHEMA_TYPE_TYPE_NAME = OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME;

	
	// next two are from org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive1_2.getInformationViewEntity()
	public static final String INFOTMATION_VIEW_TYPE_NAME = "InformationView";
	public static final String INFOTMATION_VIEW_TYPE_GUID = "68d7b905-6438-43be-88cf-5de027b4aaaf";
	// next two are from org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive1_2.getDeployedReportEntity()
	public static final String DEPLOYED_REPORT_TYPE_GUID = "e9077f4f-955b-4d7b-b1f7-12ee769ff0c3";
	public static final String DEPLOYED_REPORT_TYPE_NAME = "DeployedReport";
	

	public static final String SERVER_ASSET_USE_TYPE_GUID = OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID;

	public static final String SCHEMATYPE_TO_SCHEMAATTRIBUTE_NAME = OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME;
	public static final String SCHEMATYPE_TO_SCHEMAATTRIBUTE_GUID = OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID;

	public static final String NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID = OpenMetadataAPIMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID;
	public static final String NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME = OpenMetadataAPIMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME;

	public static final String SOURCE_GUID = "sourceGuid";
	public static final String SOURCE_ID = "sourceId";

	public static final String DATA_CONTENT_FOR_DATA_SET_TYPE_NAME = OpenMetadataAPIMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_NAME;
	public static final String DATA_CONTENT_FOR_DATA_SET_TYPE_GUID = OpenMetadataAPIMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_GUID;

	public static final String SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_GUID = OpenMetadataAPIMapper.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_GUID;
	public static final String SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_NAME = OpenMetadataAPIMapper.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_NAME;
	
}
