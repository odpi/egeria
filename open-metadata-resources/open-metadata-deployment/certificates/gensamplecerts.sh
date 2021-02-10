#!/bin/sh
# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.

# ---
# Certificate generation for Egeria
#
# Creates a self-signed certificate authority, as well as appropriate certificates for both the server and client role (in a TLS interaction)
#
# Only provided in .sh / *nix format, but a similar principle should work on other platforms
# Requires a current version of the 'openssl' tool as well as access to Java (8 and above) keytool
#
# Provided as a sample only. There is no error checking in this script
# hostname is assumed to be localhost - this will need extending for your environment
#
# Generated files are checked in. Run manually and re-checkin to update. This is to avoid
# the certs changing every build
#
# ANY GENERATED CERTIFICATES SHOULD NOT BE USED IN PRODUCTION
# ---

# Fail if any command fails
set -e

# Add any additional keys here
SERVERCERTS="ServerChassis UIChassis ReactUIServer"
CLIENTCERTS="ReactUIClient Client"

# We used to call this the truststore, but this caused some confusion
# This file contains the Egeria Certificate Authority when we are using our own certs
TRUSTSTORE=EgeriaCA.p12

# Password used for all the stores
KEYPASS=egeria
STOREPASS=egeria

CA=Egeria

# Hostname to add in cert -- but note this is assumed to be the same for ALL
HOST=localhost
# ---
# Certificate Authority
#
# This signs our certificates, and is included in the truststore for both the client and the server
# Note - in a real world environment you would create additinal intermediate CAs.
# ---
printf "\n\n---- Generating CA ($FNAME)  ----\n\n"
FNAME=${CA}
openssl genrsa -out ${FNAME}.key -passout pass:${KEYPASS}
openssl req -x509 -new -nodes -key ${FNAME}.key -sha256 -days 3650 -out ${FNAME}.pem -subj '/C=US/ST=CA/O=LFAIData/CN=Egeria' -config openssl.cnf
keytool -importcert -alias ${FNAME} -file ${FNAME}.pem -keypass ${KEYPASS} -storepass ${STOREPASS} -noprompt -destkeystore ${FNAME}.p12

# ---
# Server Certs
# ---
for cert in ${SERVERCERTS}
do
  FNAME=Egeria${cert}
  printf "\n\n---- Generating cert ($FNAME)  ----\n\n"
  openssl genrsa -out ${FNAME}.key -passout pass:${KEYPASS}
  openssl req -new -key ${FNAME}.key -passin pass:${KEYPASS} -out ${FNAME}.csr  -subj '/C=US/ST=CA/O=LFAIData/CN='${cert} -config openssl.cnf -extensions v3_server -addext 'subjectAltName = DNS:'${HOST}
  openssl x509 -req -in ${FNAME}.csr -CA ${CA}.pem -CAkey ${CA}.key -CAcreateserial -out ${FNAME}.crt
  keytool -importcert -alias ${FNAME} -file ${FNAME}.crt -keypass ${KEYPASS} -storepass ${STOREPASS} -noprompt -destkeystore ${FNAME}.p12
done

# ---
# Client certs
# ---
for cert in ${CLIENTCERTS}
do
  printf "\n\n---- Generating cert ($FNAME)  ----\n\n"
  FNAME=Egeria${cert}
  openssl genrsa -out ${FNAME}.key -passout pass:${KEYPASS}
  openssl req -new -key ${FNAME}.key -passin pass:${KEYPASS} -out ${FNAME}.csr  -subj '/C=US/ST=CA/O=LFAIData/CN='${cert} -config openssl.cnf -extensions v3_client -addext 'subjectAltName = DNS:'$HOST
  openssl x509 -req -in ${FNAME}.csr -CA ${CA}.pem -CAkey ${CA}.key -CAcreateserial -out ${FNAME}.crt
  keytool -importcert -alias ${FNAME} -file ${FNAME}.crt -keypass ${KEYPASS} -storepass ${STOREPASS} -noprompt -destkeystore ${FNAME}.p12
done

# ---
# output message
# ---
echo "Certificate generation complete."




