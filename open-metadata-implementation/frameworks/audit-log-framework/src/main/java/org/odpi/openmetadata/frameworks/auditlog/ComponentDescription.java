/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.auditlog;

/**
 * ComponentDescription is an interface implemented by an enum that describes the components using the audit log
 */
public interface ComponentDescription
{
    /**
     * Return the numerical code for this component.
     *
     * @return int componentId
     */
    int getComponentId();


    /**
     * Return the development status of the component.
     *
     * @return enum describing the status
     */
    ComponentDevelopmentStatus getComponentDevelopmentStatus();


    /**
     * Return the name of the component.  This is the name used in the audit log records.
     *
     * @return String component name
     */
    String getComponentName();



    /**
     * Return the short description of the component. This is an English description.  Natural language support for
     * these values can be added to UIs using a resource bundle indexed with the component id.  This value is
     * provided as a default if the resource bundle is not available.
     *
     * @return String description
     */
    String getComponentDescription();

    /**
     * URL to the wiki page that describes this component.  This provides more information to the log reader
     * on the operation of the component.
     *
     * @return String URL
     */
    String getComponentWikiURL();
}
