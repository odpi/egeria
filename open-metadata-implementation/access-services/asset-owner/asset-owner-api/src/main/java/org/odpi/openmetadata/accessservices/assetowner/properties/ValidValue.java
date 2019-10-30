/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Referenceable;

/**
 * ValidValue provides the common super calls for ValidValueSet and ValidValueDefinition.
 */
public class ValidValue extends Referenceable
{
    private String name = null;
    private String description = null;
    private String usage = null;
    private String scope = null;

}
