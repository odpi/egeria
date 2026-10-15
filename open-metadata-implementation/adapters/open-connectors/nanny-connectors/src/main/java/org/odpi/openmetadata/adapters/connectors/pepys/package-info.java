/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * The pepys connector is the activity observer for the open metadata ecosystem.  Named after Samuel Pepys and the
 * diary in which he so faithfully recorded the activity around him, it listens to the topics that the OMAG Servers
 * write their audit logs to and maintains the UsageActivity classification of each anchor element (asset, digital
 * product or glossary term) whose retrieval, discovery through search, update, or update or deletion of attachments
 * and feedback is reported there.
 */
package org.odpi.openmetadata.adapters.connectors.pepys;
