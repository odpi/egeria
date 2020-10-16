/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * The RepositoryHandler provides object-like interfaces over the Open Metadata Repository Services (OMRS) repository connector.
 *
 * It supports the following abstractions:
 * <ul>
 *     <li>
 *         Conversion of repository services exceptions into common exceptions.
 *     </li>
 *     <li>
 *         Creation of the correct type of instances dependant on the setting of the external source GUID.  This means that
 *         the correct provenance information is added to the instance.
 *     </li>
 *     <li>
 *         Validation of an instance's provenance information when an update is made.  This means that external instances
 *         can only be updated by processes that represent the external source of the instance.
 *     </li>
 * </ul>
 * The aim is to reduce the coding needed in the specific handlers used by the Open Metadata Access Services (OMASs) and ensure
 * external entities are managed correctly.
 */
package org.odpi.openmetadata.commonservices.repositoryhandler;
