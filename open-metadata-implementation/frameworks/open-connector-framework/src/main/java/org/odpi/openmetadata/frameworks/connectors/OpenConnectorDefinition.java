/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;

/**
 * Provides a standard definition for open connectors with simple connector provider behaviour.
 * This information enables the connector provider to inherit from OpenConnectorProviderBase
 * and hence have a minimal implementation.
 */
public interface OpenConnectorDefinition
{
    /**
     * Return the component identifier for the connector.
     *
     * @return int
     */
    int getConnectorComponentId();


    /**
     * Return the unique identifier for the connector type.
     *
     * @return string
     */
    String getConnectorTypeGUID();


    /**
     * Return the qualified name of the connector type.
     *
     * @return string
     */
    String getConnectorQualifiedName();


    /**
     * Return the display name of the connector type.
     *
     * @return string
     */
    String getConnectorDisplayName();


    /**
     * Return the description of the connector type.
     *
     * @return string
     */
    String getConnectorDescription();


    /**
     * Return the link to the wiki page for this connector.
     *
     * @return string
     */
    String getConnectorWikiPage();


    /**
     * Return the class name of the connector provider that implements this connector.
     *
     * @return string
     */
    String getConnectorProviderClassName();


    /**
     * Return the development status of the connector.
     *
     * @return ComponentDevelopmentStatus enum
     */
    ComponentDevelopmentStatus getConnectorDevelopmentStatus();
}
