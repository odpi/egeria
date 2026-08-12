/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * Package containing the REST client connector implementation used by default throughout Egeria, built
 * on the JDK's own java.net.http.HttpClient rather than a third-party framework.  It supports connection
 * reuse and every HTTP method Egeria needs, including PATCH, and it calls Jackson directly to
 * (de)serialize request and response bodies so that Jackson's own detailed error messages are surfaced
 * unwrapped when a response body cannot be parsed into the expected type.
 */
package org.odpi.openmetadata.adapters.connectors.restclients.jdk;
