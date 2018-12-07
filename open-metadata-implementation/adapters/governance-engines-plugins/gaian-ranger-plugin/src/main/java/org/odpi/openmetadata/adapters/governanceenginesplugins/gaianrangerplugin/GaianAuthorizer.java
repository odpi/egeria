/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin;

public interface GaianAuthorizer {

	void init();

	boolean isAuthorized(QueryContext queryContext) throws GaianAuthorizationException;

	void applyRowFilterAndColumnMasking(QueryContext queryContext);

	void cleanUp();
}