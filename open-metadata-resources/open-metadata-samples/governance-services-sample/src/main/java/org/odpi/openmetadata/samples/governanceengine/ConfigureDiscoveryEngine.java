/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.governanceengine;

/**
 * ConfigureDiscoveryEngine uses the DiscoveryConfigurationClient from
 * the Discovery Engine OMAS to set up a new discovery engine called
 * ClinicalTrials that contains sample discovery services to validate
 * some of the assets that Coco Pharmaceuticals is using in its clinical
 * trials.
 *
 * The process of configuring a discovery engine is in three phases:
 *
 * <ul>
 *     <li>Create the definition of the discovery engine.</li>
 *     <li>Create a discovery service definition for each discovery service.</li>
 *     <li>Register each discovery service with the discovery engine.
 *         This sets up a name to use to call the discovery service.
 *         This name is called the discovery request type.
 *         The registration can also include default analysis parameters that are passed to the
 *         discovery service.  These analysis parameters can be overridden on each discovery request.</li>
 * </ul>
 */
public class ConfigureDiscoveryEngine
{
}
