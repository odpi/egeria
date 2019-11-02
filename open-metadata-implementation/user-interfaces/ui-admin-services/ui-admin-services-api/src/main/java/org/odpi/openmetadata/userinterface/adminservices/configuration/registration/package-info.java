/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.adminservices.configuration.registration;

/**
 * The registration package provides the definitions and interfaces to describe each of the User
 * Interface (UI) views and enable them to register with the admin services.  The admin services then
 * call the registered ui views at start up and shutdown of a server instance if it is
 * configured in the server instance's configuration document.
 *
 * ViewServiceDescription lists all of the identified UI views, giving them an unique identifier
 * and including a URL to their home page.
 *
 * UIViewRegistration holds the properties of a UI view that is passed to the admin services
 * during registration.
 *
 * ViewServiceOperationalStatus indicates whether a UI view is enabled or disabled.
 */