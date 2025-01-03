/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.connectors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;

/**
 * OpenMetadataServerSecurityConnector provides the base class for an Open Metadata Security Connector for
 * a server.  This connector is configured in an OMAG Configuration Document.  Its default behavior
 * is to reject every request.  It generates well-defined exceptions and audit log
 * messages.
 *
 * Override these to define the required access for the deployment environment.  The methods
 * in this base class can be called if access is to be denied as a way of making use of the message
 * logging and exceptions.
 */
public class OpenMetadataServerSecurityConnector extends OpenMetadataSecurityConnector implements AuditLoggingComponent

{
}
