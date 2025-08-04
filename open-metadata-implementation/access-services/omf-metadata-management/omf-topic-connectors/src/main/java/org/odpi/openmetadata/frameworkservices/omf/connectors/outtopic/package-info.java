/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * The OMF topic connectors are the interfaces of the
 * connectors supported by the Open Metadata Store services.
 * Currently, there are two connectors, one for the client
 * and one for the server, that are used to exchange events
 * over the Open Metadata Store's out topic.
 * There are two connectors because events should only flow one way
 * and so the client needs a different interface to the server.
 * Specifically the client interface is a listener interface
 * to allow the client to receive events from the server.
 * The server interface is an event sending interface.
 */
package org.odpi.openmetadata.frameworkservices.omf.connectors.outtopic;
