/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * This package provides the Java clients for the Digital Architecture OMAS.
 *
 * There is a base class (DigitalArchitectureClientBase) that manages
 * the properties for the remote server passed on the constructors, the internal REST client (DigitalArchitectureRESTClient), the audit log and the
 * maximum page size property.  It initializes the InvalidParameterHandler that verifies the parameters passed to the client.
 *
 * Today, the Digital Architecture OMAS has one client for managing valid values and reference data.  This client is called ReferenceDataManager.
 * It implements the ManageReferenceData interface.  More to follow :)
 */

package org.odpi.openmetadata.accessservices.digitalarchitecture.client;
