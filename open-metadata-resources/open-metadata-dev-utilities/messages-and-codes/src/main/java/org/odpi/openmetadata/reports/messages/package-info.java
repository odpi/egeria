/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
/**
 * Generates the markdown documentation for every message that Egeria can produce.
 * <br><br>
 * Egeria's messages are defined in <i>message sets</i> - java enums that implement one of the interfaces in
 * the audit log framework's <i>org.odpi.openmetadata.frameworks.auditlog.messagesets</i> package.  The
 * utility in this package scans the Egeria source tree for those enums, reads the message definitions out of
 * them, and writes a page for each message set into the <i>messages-and-codes</i> directory at the root of
 * the repository, along with the README pages that index them.
 * <br><br>
 * The generated pages are checked in so that they can be read and searched from GitHub.  The utility only
 * rewrites a page whose content has changed, so running it on an unchanged repository leaves the working tree
 * clean.
 */
package org.odpi.openmetadata.reports.messages;
