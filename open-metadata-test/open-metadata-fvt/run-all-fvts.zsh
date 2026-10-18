#!/bin/zsh
# ---
# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the ODPi Egeria project.
# ---
#
# Run everything: the unit tests, every FVT suite in turn, then the aggregated coverage report.
#
#   open-metadata-test/open-metadata-fvt/run-all-fvts.zsh [log-directory]
#
# Logs, one per step, and summary.txt (exit code and duration of each step) are written to the log directory,
# by default build/fvt-runs/<date-time> under the repository root.  A run takes about 45 minutes.
#
# Every step is run whatever happened to the one before, so summary.txt is the place to look first - and a
# step's exit code is not the whole story: check the test counts in each suite's build/test-results XML too.
#
# Suites run one at a time because they share the PostgreSQL server and bind ports.  cts-fvt is not included:
# it runs much longer than everything else together and certifies one repository per run.
#
# Each step uses --no-build-cache.  Without it Gradle can restore a whole test run, results and coverage data
# included, from the cache in seconds - reporting a pass for tests that never ran.

R=${0:A:h:h:h}
L=${1:-${R}/build/fvt-runs/$(date '+%Y%m%d-%H%M%S')}

mkdir -p ${L} || exit 99
cd ${R} || exit 99

# Most suites store their metadata in PostgreSQL on localhost:5442.  An open port is not enough: Docker keeps
# its port forwards listening when its engine has failed, and a run against it fails nearly every suite in
# seconds.  A real server answers an SSLRequest with a single byte, 'S' or 'N'.
postgresReply=$(printf '\x00\x00\x00\x08\x04\xd2\x16\x2f' | nc -w 3 localhost 5442 2>/dev/null | head -c1)

if [[ ${postgresReply} != "S" && ${postgresReply} != "N" ]]
then
    echo "PostgreSQL on localhost:5442 is not answering - start it before running the FVTs." >&2
    exit 98
fi

SUITES=(auth-fvt:runAuthFvt
        server-fvt:runServerFvt
        security-fvt:runSecurityFvt
        query-fvt:runQueryFvt
        anchor-fvt:runAnchorFvt
        view-services-fvt:runViewServicesFvt
        type-fvt:runTypeFvt
        client-fvt:runClientFvt
        templates-fvt:runTemplatesFvt
        duplicate-fvt:runDuplicateFvt
        darwin-fvt:runDarwinFvt
        platform-catalog-fvt:runPlatformCatalogFvt
        tabular-data-fvt:runTabularDataFvt
        postgres-fvt:runPostgresFvt
        files-fvt:runFilesFvt
        bitol-fvt:runBitolFvt
        openlineage-fvt:runOpenLineageFvt
        unity-catalog-fvt:runUnityCatalogFvt
        subscription-fvt:runSubscriptionFvt)

echo "START $(date '+%H:%M:%S') logs in ${L}" | tee ${L}/summary.txt

# Results and coverage data left by an earlier run would otherwise be read as this run's.
find ${R} -path '*/build/jacoco/*.exec' -delete
find ${R} -type d -path '*/build/test-results' -prune -exec rm -rf {} +

t0=$(date +%s)
${R}/gradlew test --no-build-cache --continue < /dev/null > ${L}/ut.log 2>&1
echo "ut EXIT $? $(( $(date +%s) - t0 ))s $(date '+%H:%M:%S')" | tee -a ${L}/summary.txt

for entry in ${SUITES[@]}
do
    suite=${entry%%:*}
    flag=${entry##*:}
    t0=$(date +%s)
    ${R}/gradlew ":open-metadata-test:open-metadata-fvt:${suite}:test" "-P${flag}" --no-build-cache --continue < /dev/null > ${L}/${suite}.log 2>&1
    echo "${suite} EXIT $? $(( $(date +%s) - t0 ))s $(date '+%H:%M:%S')" | tee -a ${L}/summary.txt
done

t0=$(date +%s)
${R}/gradlew codeCoverageReport --no-build-cache < /dev/null > ${L}/coverage.log 2>&1
echo "coverage EXIT $? $(( $(date +%s) - t0 ))s $(date '+%H:%M:%S')" | tee -a ${L}/summary.txt

echo "END $(date '+%H:%M:%S')" | tee -a ${L}/summary.txt
