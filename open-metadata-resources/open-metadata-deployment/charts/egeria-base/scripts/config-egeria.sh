#!/bin/sh

# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.

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

# --- Now the view server

=# Set the URL root
echo -e '\n\n > Setting view server URL root:\n'
curl -f -k --verbose --basic admin:admin -X POST \
  "${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${VIEW_SERVER}/server-url-root?url=${EGERIA_ENDPOINT}"

# Setup the event bus
echo -e '\n\n > Setting up event bus:\n'

curl -f -k --verbose --basic admin:admin \
  --header "Content-Type: application/json" \
  "${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${VIEW_SERVER}/event-bus" \
  --data '{"producer": {"bootstrap.servers": "'"${KAFKA_ENDPOINT}"'"}, "consumer": {"bootstrap.servers": "'"${KAFKA_ENDPOINT}"'"} }'

# Set as view server
echo -e '\n\n > Set as view server:\n'

curl -f -k --verbose --basic admin:admin -X POST \
  "${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${VIEW_SERVER}/server-type?typeName=View%20Server"

# Configure the view server cohort membership
echo -e '\n\n > configuring cohort membership:\n'

curl -f -k --verbose --basic admin:admin -X POST \
  "${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${VIEW_SERVER}/cohorts/${EGERIA_COHORT}"

# Configure the view services
echo -e '\n\n > Setting up Glossary Author:\n'

 curl -f -k --verbose --basic admin:admin \
   --header "Content-Type: application/json" \
   "${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${VIEW_SERVER}/view-services/glossary-author" \
   --data @- <<EOF
{
  "class": "ViewServiceConfig",
  "omagserverPlatformRootURL": "${EGERIA_ENDPOINT}",
  "omagserverName" : "${EGERIA_SERVER}"
}
EOF

echo -e '\n\n > Setting up TEX:\n'

curl -f -k --verbose --basic admin:admin \
  --header "Content-Type: application/json" \
  "${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${VIEW_SERVER}/view-services/tex" \
  --data @- <<EOF
{
  "class":"IntegrationViewServiceConfig",
  "viewServiceAdminClass":"org.odpi.openmetadata.viewservices.tex.admin.TexViewAdmin",
  "viewServiceFullName":"Type Explorer",
  "viewServiceOperationalStatus":"ENABLED",
  "omagserverPlatformRootURL": "UNUSED",
  "omagserverName" : "UNUSED",
  "resourceEndpoints" : [
    {
      "class"              : "ResourceEndpointConfig",
      "resourceCategory"   : "Platform",
      "description"        : "Platform",
      "platformName"       : "platform",
      "platformRootURL"    : "${EGERIA_ENDPOINT}"
    },
    {
      "class"              : "ResourceEndpointConfig",
      "resourceCategory"   : "Server",
      "serverInstanceName" : "${EGERIA_SERVER}",
      "description"        : "Server",
      "platformName"       : "platform",
      "serverName"         : "${EGERIA_SERVER}"
    }
  ]
}
EOF

echo -e '\n\n > Setting up REX:\n'

curl -f -k --verbose --basic admin:admin \
  --header "Content-Type: application/json" \
  "${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${VIEW_SERVER}/view-services/rex" \
  --data @- <<EOF
{
  "class":"IntegrationViewServiceConfig",
  "viewServiceAdminClass":"org.odpi.openmetadata.viewservices.rex.admin.RexViewAdmin",
  "viewServiceFullName":"Repository Explorer",
  "viewServiceOperationalStatus":"ENABLED",
  "omagserverPlatformRootURL": "UNUSED",
  "omagserverName" : "UNUSED",
  "resourceEndpoints" : [
    {
              "class"              : "ResourceEndpointConfig",
        "resourceCategory"   : "Platform",
        "description"        : "Platform",
        "platformName"       : "platform",
        "platformRootURL"    : "${EGERIA_ENDPOINT}"
    },
                  {
        "class"              : "ResourceEndpointConfig",
        "resourceCategory"   : "Server",
        "serverInstanceName" : "${EGERIA_SERVER}",
        "description"        : "Server",
        "platformName"       : "platform",
        "serverName"         : "${EGERIA_SERVER}"
    }
  ]
}
EOF

echo -e '\n\n > Setting up DINO:\n'

curl -f -k --verbose --basic admin:admin \
  --header "Content-Type: application/json" \
  "${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${VIEW_SERVER}/view-services/dino" \
  --data @- <<EOF
{
  "class":"IntegrationViewServiceConfig",
  "viewServiceAdminClass":"org.odpi.openmetadata.viewservices.dino.admin.DinoViewAdmin",
  "viewServiceFullName":"Dino",
  "viewServiceOperationalStatus":"ENABLED",
  "omagserverPlatformRootURL": "UNUSED",
  "omagserverName" : "UNUSED",
  "resourceEndpoints" : [
    {
        "class"              : "ResourceEndpointConfig",
        "resourceCategory"   : "Platform",
        "description"        : "Platform",
        "platformName"       : "platform",
        "platformRootURL"    : "${EGERIA_ENDPOINT}"
    },
    {
        "class"              : "ResourceEndpointConfig",
        "resourceCategory"   : "Server",
        "serverInstanceName" : "${EGERIA_SERVER}",
        "description"        : "Server",
        "platformName"       : "platform",
        "serverName"         : "${EGERIA_SERVER}"
    },
    {
        "class"              : "ResourceEndpointConfig",
        "resourceCategory"   : "Server",
        "serverInstanceName" : "${VIEW_SERVER}",
        "description"        : "Server",
        "platformName"       : "platform",
        "serverName"         : "${VIEW_SERVER}"
    }
  ]
}
EOF

# Start up the view server
echo -e '\n\n > Starting the view server:\n'

curl -f -k --verbose --basic admin:admin -X POST --max-time 900 \
                "${EGERIA_ENDPOINT}/open-metadata/admin-services/users/${EGERIA_USER}/servers/${VIEW_SERVER}/instance"

# Enabling autostart by updating the configmap
# This can only be done AFTER the server is correctly configured, otherwise it will prevent platform startup

echo -e '\n\n > Enabling auto-start for the configured servers'

token="$(cat /var/run/secrets/kubernetes.io/serviceaccount/token)"
namespace="$(cat /var/run/secrets/kubernetes.io/serviceaccount/namespace)"
cacert=/var/run/secrets/kubernetes.io/serviceaccount/ca.crt

curl -k \
    -X PATCH \
    -d @- \
    -H "Authorization: Bearer $token" \
    -H 'Accept: application/json' \
    -H 'Content-Type: application/strategic-merge-patch+json' \
    https://kubernetes.default.svc/api/v1/namespaces/$namespace/configmaps/$STARTUP_CONFIGMAP <<EOF
{
  "kind": "ConfigMap",
  "apiVersion": "v1",
  "data":
  {
    "STARTUP_SERVER_LIST": "$POSTCONFIG_STARTUP_SERVER_LIST"
  }
}
EOF

echo '-- End of configuration'
