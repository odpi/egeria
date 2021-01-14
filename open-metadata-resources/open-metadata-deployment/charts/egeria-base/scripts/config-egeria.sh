

# Script will be run by k8s as part of our initialization job.
# Assumed here - platform up & responding to REST api, plus Kafka is available

# Note - expect to port this to python, aligned with our notebook configuration
# - this will facilitate error handling (vs very verbose scripting). Groovy an alternative
# Initial a version a script to get the basics working

# Set the URL root
curl -f --verbose --basic admin:admin -X POST \
  ${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/server-url-root?url=${EGERIA_ENDPOINT}

# Setup the event bus
curl -f --verbose --basic admin:admin -X POST
  --header "Content-Type: application/json"
  ${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/event-bus
  --data '{"producer": {"bootstrap.servers": "'"${KAFKA_ENDPOINT}"'"}, "consumer": {"bootstrap.servers": "'"${KAFKA_ENDPOINT}"'"} }'

# Enable all the access services (we will adjust this later)
curl -f --verbose --basic admin:admin -X POST
  ${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/access-services?serviceMode=ENABLED

# Use a local graph repo
curl -f --verbose --basic admin:admin -X POST
  ${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/local-repository/mode/local-graph-repository

# Configure the cohort membership
curl -f --verbose --basic admin:admin -X POST
  ${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/cohorts/${EGERIA_COHORT}

# Start up the server
curl -f --verbose --basic admin:admin -X POST --max-time 900
                ${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${EGERIA_SERVER}/instance

#Configure autostarting of the servers
#
# tbd !
