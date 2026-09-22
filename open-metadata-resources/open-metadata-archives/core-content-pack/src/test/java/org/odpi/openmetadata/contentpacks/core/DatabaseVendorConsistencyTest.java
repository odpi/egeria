/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.testng.Assert.assertTrue;

/**
 * DatabaseVendorConsistencyTest checks that the database vendor content packs offer the same things as each
 * other.
 * <br><br>
 * Each relational database vendor - PostgreSQL, Microsoft SQL Server, Oracle, Db2 for Linux, UNIX and
 * Windows - is supported by a connector set and a content pack built to the same pattern, and a new vendor
 * is added by cloning an existing one.  The recurring mistake in that work is not a broken connector: it is
 * a step of the clone that was missed, so the new vendor arrives with most of what it needs and silently
 * without one piece.  An operator then finds that the thing they can do for PostgreSQL has no equivalent for
 * Oracle, and nothing anywhere said so.
 * <br><br>
 * The governance action processes are where that shows up first, because they are what an operator actually
 * runs: they are the "catalog this server", "survey this database" and "remove this asset" actions offered
 * through the Automated Curation API.  A vendor missing one is missing a capability.
 * <br><br>
 * This check compares the packs rather than a live database, so it covers <b>every</b> vendor on every
 * build, including the ones whose databases are hard to stand up - Db2 LUW's container does not run on Apple
 * silicon at all.  That is the point of doing it this way: a suite that drives a real database can only ever
 * check the vendors somebody has a database for.
 */
public class DatabaseVendorConsistencyTest
{
    /**
     * The vendor packs, and the token each uses to name its own elements.
     */
    private static final Map<String, String> VENDOR_PACKS = new LinkedHashMap<>()
    {{
        put("PostgresContentPack", "PostgreSQL");
        put("MSSQLContentPack",    "MSSQL");
        put("OracleContentPack",   "Oracle");
        put("DB2LUWContentPack",   "DB2LUW");
        put("DuckDBContentPack",   "DuckDB");
    }};

    /**
     * The vendor whose pack the others are compared against.  PostgreSQL is the one the others were cloned
     * from and the only one with an FVT driving a real database, so it is the most complete.
     */
    private static final String REFERENCE_VENDOR = "PostgreSQL";

    /**
     * Capabilities a vendor genuinely does not have, each with the reason.  An entry here is a real
     * difference between the products, not a gap in the clone.
     */
    private static final Map<String, String> GENUINE_DIFFERENCES = new LinkedHashMap<>()
    {{
        put("DuckDB/<V>Server",
            "DuckDB is an embedded database held in a file - there is no server to catalog or survey, so " +
                    "the server level processes have no meaning for it");
    }};

    /**
     * Differences that are <b>not</b> understood, each with what is known.  These are findings waiting on a
     * decision, recorded so the check can run green while they are considered rather than being forgotten.
     * <br><br>
     * Keyed the same way as the genuine differences above - vendor, then the process shape - so that an entry
     * excuses only the vendor it was written about.  A single unqualified process name would excuse every
     * vendor at once, and this check's whole job is to notice when one vendor is the odd one out.
     * <br><br>
     * Empty, and worth keeping empty: every vendor now offers every process the reference vendor does, apart
     * from the genuine product difference above.  Both entries this map has held were cleared rather than
     * settled into - the schema level processes were added to MSSQL, Oracle and Db2 LUW, and then to DuckDB -
     * and {@link #theRecordedDifferencesAreStillReal()} is what refused to let either note outlive its finding.
     */
    private static final Map<String, String> UNEXPLAINED_DIFFERENCES = new LinkedHashMap<>();


    /**
     * Every vendor offers the governance action processes the reference vendor offers, allowing for the
     * differences recorded above.
     */
    @Test
    public void everyVendorOffersTheSameGovernanceActionProcesses()
    {
        Map<String, Set<String>> processesByVendor = new LinkedHashMap<>();

        for (Map.Entry<String, String> pack : VENDOR_PACKS.entrySet())
        {
            processesByVendor.put(pack.getValue(),
                                  topLevelProcessShapes(pack.getKey(), pack.getValue()));
        }

        Set<String> reference = processesByVendor.get(REFERENCE_VENDOR);

        assertTrue(reference.size() >= 6,
                   "Expected to find the " + REFERENCE_VENDOR + " pack's governance action processes - found " +
                           reference.size() + ": " + reference + ". Has the naming changed?");

        List<String> gaps = new ArrayList<>();

        for (Map.Entry<String, Set<String>> vendor : processesByVendor.entrySet())
        {
            if (REFERENCE_VENDOR.equals(vendor.getKey()))
            {
                continue;
            }

            for (String process : new TreeSet<>(reference))
            {
                if ((! vendor.getValue().contains(process)) && (! isAccountedFor(vendor.getKey(), process)))
                {
                    gaps.add(vendor.getKey() + " has no " + process + ", which " + REFERENCE_VENDOR + " has");
                }
            }
        }

        assertTrue(gaps.isEmpty(),
                   "These vendors are missing a governance action process their sibling vendors offer, which" +
                           " is how a cloned connector set arrives without a capability and nobody notices:\n    " +
                           String.join("\n    ", gaps));
    }


    /**
     * The differences recorded above are all still real.
     * <br><br>
     * An entry that has been dealt with should be deleted, not left behind excusing something that is no
     * longer happening - which is how an exclusion list stops describing the code.  This half of the check is
     * what stopped the entry that used to sit here, covering MSSQL, Oracle and Db2 LUW's missing schema level
     * processes, from quietly outliving them.
     */
    @Test
    public void theRecordedDifferencesAreStillReal()
    {
        Map<String, Set<String>> processesByVendor = new LinkedHashMap<>();

        for (Map.Entry<String, String> pack : VENDOR_PACKS.entrySet())
        {
            processesByVendor.put(pack.getValue(),
                                  topLevelProcessShapes(pack.getKey(), pack.getValue()));
        }

        Set<String>  reference = processesByVendor.get(REFERENCE_VENDOR);
        List<String> stale     = new ArrayList<>();

        Set<String> recorded = new LinkedHashSet<>(GENUINE_DIFFERENCES.keySet());
        recorded.addAll(UNEXPLAINED_DIFFERENCES.keySet());

        for (String entry : recorded)
        {
            int separator = entry.indexOf('/');

            if (separator <= 0)
            {
                stale.add(entry + " (not written as vendor/process, so it cannot be checked or applied)");
                continue;
            }

            String      vendor    = entry.substring(0, separator);
            String      prefix    = entry.substring(separator + 1);
            Set<String> offered   = processesByVendor.get(vendor);

            if (offered == null)
            {
                stale.add(entry + " (names " + vendor + ", which is not one of the vendor packs)");
                continue;
            }

            boolean stillMissing = false;

            for (String process : reference)
            {
                if (process.startsWith(prefix) && (! offered.contains(process)))
                {
                    stillMissing = true;
                }
            }

            if (! stillMissing)
            {
                stale.add(entry);
            }
        }

        assertTrue(stale.isEmpty(),
                   "These differences are recorded but the vendor named is no longer missing them - delete the" +
                           " entries rather than leaving them excusing nothing: " + stale);
    }


    /**
     * Return the vendor's top level governance action processes, with its own name replaced by a placeholder
     * so that one vendor's set can be compared with another's.
     * <br><br>
     * Only the processes an operator runs are compared.  The process <i>steps</i> are named after the GUID of
     * the step they belong to, so they differ between vendors by construction and comparing them would
     * report every vendor as different from every other.
     *
     * @param packName content pack to read
     * @param vendorToken the vendor's own name, as it appears in its qualified names
     * @return process shapes
     */
    private Set<String> topLevelProcessShapes(String packName, String vendorToken)
    {
        Set<String> shapes = new LinkedHashSet<>();

        for (String qualifiedName : ContentPackReader.qualifiedNames(packName))
        {
            if (qualifiedName.contains("GovernanceActionProcess")
                        && (! qualifiedName.startsWith("GovernanceActionProcessStep::")))
            {
                shapes.add(qualifiedName.replace(vendorToken, "<V>"));
            }
        }

        return shapes;
    }


    /**
     * Is this vendor's lack of this process either a genuine product difference or a recorded finding?
     *
     * @param vendor the vendor
     * @param process the process shape it does not have
     * @return true when it is accounted for
     */
    private boolean isAccountedFor(String vendor, String process)
    {
        return isRecorded(GENUINE_DIFFERENCES.keySet(), vendor, process)
                       || isRecorded(UNEXPLAINED_DIFFERENCES.keySet(), vendor, process);
    }


    /**
     * Is this vendor's lack of this process a genuine difference between the products?
     *
     * @param vendor the vendor
     * @param process the process shape it does not have
     * @return true when it is a recorded genuine difference
     */
    private boolean isGenuineDifference(String vendor, String process)
    {
        return isRecorded(GENUINE_DIFFERENCES.keySet(), vendor, process);
    }


    /**
     * Does one of the supplied "vendor/process" entries cover this vendor's lack of this process?
     *
     * @param entries recorded differences, each keyed vendor/process
     * @param vendor the vendor
     * @param process the process shape it does not have
     * @return true when one of them covers it
     */
    private boolean isRecorded(Set<String> entries, String vendor, String process)
    {
        for (String entry : entries)
        {
            int separator = entry.indexOf('/');

            if ((separator > 0)
                        && vendor.equals(entry.substring(0, separator))
                        && process.startsWith(entry.substring(separator + 1)))
            {
                return true;
            }
        }

        return false;
    }
}
