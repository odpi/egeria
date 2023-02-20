/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.registration;

/**
 * The registration package provides the definitions and interfaces to describe each of the Open Metadata
 * Access Services and View Services and enable them to register with the admin services.  The admin services then
 * call the registered access services at start up and shutdown of a server instance if it is
 * configured in the server instance's configuration document.
 *
 * AccessServiceAdmin is the API that each access service implements. It is called to initialize and shutdown
 * the access service.
 *
 * ViewServiceAdmin is the API that each view service implements. It is called to initialize and shutdown
 * the view service.
 */