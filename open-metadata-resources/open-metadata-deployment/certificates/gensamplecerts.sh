#!/bin/sh
# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.

# ---
# TRUSTSTORE and KEYSTORE configuration for Egeria
#
# Creates a self-signed certificate authority, as well as appropriate certificates for both the server and client role (in a TLS interaction)
#
# Only provided in .sh / *nix format, but a similar principle should work on other platforms
# Requires a current version of the 'openssl' tool as well as access to Java (8 and above) keytool
#
# Provided as a sample only. There is no error checking in this script
# ---

# Fail if any command fails
set -e

# Output stores - one for our server, one for the client, plus a truststore both have
SERVERKEYSTORE=serverkeystore.p12
CLIENTKEYSTORE=clientkeystore.p12
TRUSTSTORE=truststore.p12

# Password used for all the stores
PASS=egeria

# ---
# Certificate Authority
#
# This signs our certificates, and is included in the truststore for both the client and the server
# Note - in a real world environment you would create additinal intermediate CAs.
# ---

openssl genrsa -out EgeriaCA.key -passout pass:${PASS}
openssl req -x509 -new -nodes -key EgeriaCA.key -sha256 -days 3650 -out EgeriaCA.pem -subj '/C=US/ST=CA/O=ODPi/CN=CertificateAuthority' -config openssl.cnf


# ---
# Server certificate
# ---

openssl genrsa -out EgeriaServer.key -passout pass:${PASS}
openssl req -new -key EgeriaServer.key -passin pass:${PASS} -out EgeriaServer.csr  -subj '/C=US/ST=CA/O=ODPi/CN=Server' -config openssl.cnf -extensions v3_server -addext "subjectAltName = DNS:localhost"
openssl x509 -req -in EgeriaServer.csr -CA EgeriaCA.pem -CAkey EgeriaCA.key -CAcreateserial -out EgeriaServer.crt

# ---
# Client certificate
# ---

openssl genrsa -out EgeriaClient.key -passout pass:${PASS}
openssl req -new -key EgeriaClient.key -passin pass:${PASS} -out EgeriaClient.csr  -subj '/C=US/ST=CA/O=ODPi/CN=Client' -config openssl.cnf -extensions v3_client -addext "subjectAltName = DNS:localhost"
openssl x509 -req -in EgeriaClient.csr -CA EgeriaCA.pem -CAkey EgeriaCA.key -CAcreateserial -out EgeriaClient.crt

# ---
# Trusstore
#
# This is used for checking cert validity. All that is needed is a CA
# ---
keytool -importcert -alias EgeriaCA -file EgeriaCA.pem -keypass egeria -storepass egeria -noprompt -destkeystore ${TRUSTSTORE}


# ---
# Keystore (server)
# ---
keytool -importcert -alias EgeriaServer -file EgeriaServer.crt -keypass egeria -storepass egeria -noprompt -destkeystore ${SERVERKEYSTORE}

# ---
# Keystore (server)
# ---
keytool -importcert -alias EgeriaClient -file EgeriaClient.crt -keypass egeria -storepass egeria -noprompt -destkeystore ${CLIENTKEYSTORE}


# ---
# output message
# ---
echo "Truststores/keystores created as ${TRUSTSTORE}, ${SERVERKEYSTORE}, ${CLIENTKEYSTORE}"




