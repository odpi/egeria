/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance;


/**
 * VerificationGovernanceContext provides access to details of the verification request along with access to the
 * metadata store.
 *
 * A verification service is typically verifying that the properties associated with actionTargetElements
 * are set appropriately.  Part of these checks may look for consistency with the request source elements.
 * For example, if a discovery service documented a schema extracted from the real world counterpart of an Asset
 * metadata element, a verification service could check that the schema metadata elements for the Asset
 * are correct.
 */
public interface VerificationGovernanceContext extends GovernanceContext
{
    /*
     * No additional methods needed for verification
     */
}
