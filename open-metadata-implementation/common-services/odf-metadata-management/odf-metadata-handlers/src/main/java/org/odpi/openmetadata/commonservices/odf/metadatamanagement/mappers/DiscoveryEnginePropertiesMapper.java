/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.odf.metadatamanagement.mappers;

/**
 * DiscoveryEnginePropertiesMapper provides property name mapping for Discovery Engines.
 *
 * Specifically, a single discovery engine is represented as an OpenDiscoveryEngine entity.
 */
public class DiscoveryEnginePropertiesMapper
{
    public static final String DISCOVERY_ENGINE_TYPE_GUID                = "be650674-790b-487a-a619-0a9002488055";
    public static final String DISCOVERY_ENGINE_TYPE_NAME                = "OpenDiscoveryEngine";

    public static final String QUALIFIED_NAME_PROPERTY_NAME              = "qualifiedName";               /* from Referenceable entity */
    public static final String DISPLAY_NAME_PROPERTY_NAME                = "name";                        /* from SoftwareServerCapability entity */
    public static final String DESCRIPTION_PROPERTY_NAME                 = "description";                 /* from SoftwareServerCapability entity */
    public static final String TYPE_DESCRIPTION_PROPERTY_NAME            = "type";                        /* from SoftwareServerCapability entity */
    public static final String VERSION_PROPERTY_NAME                     = "version";                     /* from SoftwareServerCapability entity */
    public static final String PATCH_LEVEL_PROPERTY_NAME                 = "patchLevel";                  /* from SoftwareServerCapability entity */
    public static final String SOURCE_PROPERTY_NAME                      = "source";                      /* from SoftwareServerCapability entity */

    public static final String SUPPORTED_DISCOVERY_SERVICE_TYPE_GUID     = "dff45aeb-c65e-428c-9ab3-d756bc5d8dbb";
    public static final String SUPPORTED_DISCOVERY_SERVICE_TYPE_NAME     = "SupportedDiscoveryService";
    /* End1 = OpenDiscoveryService; End 2 = OpenDiscoveryEngine */

    public static final String DISCOVERY_REQUEST_TYPES_PROPERTY_NAME     = "discoveryRequestTypes";
    public static final String DEFAULT_ANALYSIS_PARAMETERS_PROPERTY_NAME = "defaultAnalysisParameters";
}
