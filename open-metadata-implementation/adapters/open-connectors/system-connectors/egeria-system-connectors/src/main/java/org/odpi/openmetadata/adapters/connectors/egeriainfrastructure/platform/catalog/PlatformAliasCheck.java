/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog;

import java.util.Date;
import java.util.List;

/**
 * Works out whether two addresses reach the same running OMAG Server Platform.  The
 * OMAGServerPlatformCatalogConnector uses it to avoid cataloguing one platform once per address.  It is kept
 * apart from the connector so that it can be tested without an integration context.
 */
final class PlatformAliasCheck
{
    private PlatformAliasCheck()
    {
    }


    /**
     * What a platform says about itself that tells it apart from every other running platform.  The start
     * time does the work - two platforms started in the same millisecond is not a realistic deployment -
     * and the origin is compared as well so that a platform that does not report a start time is never
     * mistaken for one that does.
     *
     * @param platformOrigin description of the platform's build
     * @param platformStartTime time the platform started
     */
    record PlatformIdentity(String platformOrigin, Date platformStartTime)
    {
        /**
         * Is this definitely the same running platform as the other one?  Without a start time on both
         * sides there is no telling, and the answer is no.
         *
         * @param other identity of another platform
         * @return boolean
         */
        boolean isSamePlatformAs(PlatformIdentity other)
        {
            return (other != null) &&
                    (platformStartTime != null) &&
                    (platformStartTime.equals(other.platformStartTime)) &&
                    ((platformOrigin == null) ? (other.platformOrigin == null) : platformOrigin.equals(other.platformOrigin));
        }
    }


    /**
     * Asks the platform at an address who it is.
     */
    interface PlatformIdentityReader
    {
        /**
         * Return the identity of the platform at an address.
         *
         * @param platformURLRoot address of the platform
         * @return identity
         * @throws Exception the platform could not be asked
         */
        PlatformIdentity getPlatformIdentity(String platformURLRoot) throws Exception;
    }


    /**
     * Return the earlier address in the list that reaches the same running platform as this one, or null if
     * this address reaches a platform of its own.
     * <br>
     * The platforms are compared by what they say about themselves (see {@link PlatformIdentity}), not by
     * their addresses.  The comparison is made with the earlier addresses whether or not they have been
     * registered yet, so that a registration that is still deferred cannot let the same platform in under
     * its other address.  If either platform cannot be asked, the exception is passed on and the caller
     * tries again on the next refresh - it never registers an address it has not been able to check.
     * <br>
     * The first address in the list has nothing to be compared with, so the ordinary deployment, which has
     * only one address, never calls a platform at all.
     *
     * @param platformURLRoot address to check
     * @param earlierAddresses the addresses before it in the list, in order
     * @param platformIdentityReader asks a platform who it is
     * @return an earlier address of the same platform, or null
     * @throws Exception one of the platforms could not be asked
     */
    static String findEarlierAddressOfSamePlatform(String                 platformURLRoot,
                                                   List<String>           earlierAddresses,
                                                   PlatformIdentityReader platformIdentityReader) throws Exception
    {
        if ((earlierAddresses == null) || (earlierAddresses.isEmpty()))
        {
            return null;
        }

        PlatformIdentity platformIdentity = platformIdentityReader.getPlatformIdentity(platformURLRoot);

        for (String earlierAddress : earlierAddresses)
        {
            if (platformIdentity.isSamePlatformAs(platformIdentityReader.getPlatformIdentity(earlierAddress)))
            {
                return earlierAddress;
            }
        }

        return null;
    }
}
