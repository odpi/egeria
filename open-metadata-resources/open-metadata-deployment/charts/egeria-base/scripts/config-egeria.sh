#!/bin/sh

# Script will be run by k8s as part of our initialization job.
# Assumed here - platform up & responding to REST api, plus Kafka is available

# Note - expect to port this to python, aligned with our notebook configuration
# - this will facilitate error handling (vs very verbose scripting). Groovy an alternative
# Initial a version a script to get the basics working

echo '-- Environment variables --'
env
echo '-- End of Environment variables --'

echo -e '\n-- Configuring platform with requires servers'
# Set the URL root
echo -e '\n\n > Setting server URL root:\n'
curl -f -k --verbose --basic admin:admin -X POST \
  "${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/server-url-root?url=${EGERIA_ENDPOINT}"

# Setup the event bus
echo -e '\n\n > Setting up event bus:\n'

curl -f -k --verbose --basic admin:admin \
  --header "Content-Type: application/json" \
  "${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/event-bus" \
  --data '{"producer": {"bootstrap.servers": "'"${KAFKA_ENDPOINT}"'"}, "consumer": {"bootstrap.servers": "'"${KAFKA_ENDPOINT}"'"} }'

# Enable all the access services (we will adjust this later)
echo -e '\n\n > Enabling all access servces:\n'

curl -f -k --verbose --basic admin:admin -X POST \
  "${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/access-services?serviceMode=ENABLED"

# Use a local graph repo
echo -e '\n\n > Use local graph repo:\n'

curl -f -k --verbose --basic admin:admin -X POST \
  "${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/local-repository/mode/local-graph-repository"

# Configure the cohort membership
echo -e '\n\n > configuring cohort membership:\n'

curl -f -k --verbose --basic admin:admin -X POST \
  "${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/cohorts/${EGERIA_COHORT}"

# Start up the server
echo -e '\n\n > Starting the server:\n'

curl -f -k --verbose --basic admin:admin -X POST --max-time 900 \
                "${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/instance"

#Configure autostarting of the servers
#
# tbd !
echo '-- End of configuration'