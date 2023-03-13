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
# Generated files are checked in. Run manuv3_defaulty and re-checkin to update. This is to avoid
# the certs changing every build
#
# ANY GENERATED CERTIFICATES SHOULD NOT BE USED IN PRODUCTION
# ---

# Fail if any command fails
set -e

# Add any additional keys here
SERVERCERTS="ServerChassis UIChassis ReactUIServer"
CLIENTCERTS="ReactUIClient Client"

# We used to cv3_default this the truststore, but this caused some confusion
# This file contains the Egeria Certificate Authority when we are using our own certs
TRUSTSTORE=EgeriaCA.p12

# Password used for v3_default the stores
KEYPASS=egeria
STOREPASS=egeria

# The tools will be creating a few directories for handling the CAs to useful to keep these here
export iCA=EgeriaIntermediateCA
export rootCA=EgeriaRootCA

CA_CHAIN=EgeriaCAChain

# Used in openssl.cnf for the subjectAltName where needed - export to environment
export SAN="DNS:localhost"

# Cleanup directories from old runs (this will DELETE all your CAs!!)
# ONLY run these if you want to start afresh
#
# ie set CA_CLEAN
#
if [ ! -z $CA_CLEAN ]; then
  rm -f -- *.pem
  rm -f -- *.p12
  rm -fr -- ${rootCA}
  rm -fr -- ${iCA}

  # A few empty dirs needed by the signing process
  for d in ./${rootCA} ./${iCA}; do
    for e in certs crl csr newcerts private; do
      mkdir -p ${d}/${e}
    done
    chmod 700 ${d}/private
    touch ${d}/index.txt
    echo 1000 >${d}/serial
  done
  echo 1000 >./${iCA}/crlnumber

  # ---
  # Root Certificate Authority
  #
  # Generates a root Certificate Authority for all the certs we create here in Egeria
  # ---
  # CA/CA_POLICY are variables used in the openssl.cnf file -- we need to use two different contexts -
  # for both the root cert authority, and then the intermediate cert authority
  export CA=${rootCA}
  export CA_POLICY=policy_strict
  printf "\n\n---- Generating root CA (%s)  ----\n\n" ${rootCA}
  openssl genrsa -aes256 -out ${rootCA}/private/${rootCA}.key.pem -passout pass:${KEYPASS} 4096
  openssl req -batch -passin pass:${KEYPASS} -new -x509 -days 3650 -sha256 \
    -key ${rootCA}/private/${rootCA}.key.pem \
    -subj '/C=US/ST=CA/O=LFAIData/CN=EgeriaRootCertificateAuthority' -config ./openssl.cnf \
    -extensions v3_ca -out ${rootCA}/certs/${rootCA}.cert.pem

  # ---
  # Intermediate Certificate Authority
  #
  # This is an intermediate Certificate Authority which actually does the work of signing user/server certs
  # Good practice as a comprimised intermediate CA can be invalidated
  # ---
  export CA=${iCA}
  export CA_POLICY=policy_loose
  printf "\n\n---- Generating Intermediate CA (%s)  ----\n\n" ${iCA}
  openssl genrsa -aes256 -out ${iCA}/private/${iCA}.key.pem -passout pass:${KEYPASS} 4096
  openssl req -batch -passin pass:${KEYPASS} -new -key ${iCA}/private/${iCA}.key.pem \
    -sha256 -out ${iCA}/csr/${iCA}.csr.pem \
    -subj '/C=US/ST=CA/O=LFAIData/CN=EgeriaIntermediateCertificateAuthority' \
    -config ./openssl.cnf
  export CA=${rootCA}
  export CA_POLICY=policy_strict
  openssl ca -config ./openssl.cnf -batch -passin pass:${KEYPASS} \
    -extensions v3_intermediate_ca -days 3650 -notext -md sha256 \
    -in ${iCA}/csr/${iCA}.csr.pem -out ${iCA}/certs/${iCA}.cert.pem

  # Create truststore - just the rootCA. Intermediate is included for keystores
  # Deploy this anywhere that needs to 'trust' the certs we create later. It should be sufficient
  printf "\n\n---- Create truststore (rootCA) ----\n\n"

  # -- will use keytool instead, can't seem to get a cert exported via openssl ...
  #openssl pkcs12 -export -in ${rootCA}/certs/${rootCA}.cert.pem -out ${rootCA}.p12 \
  #          -passin pass:${KEYPASS} -passout pass:${KEYPASS} -nokeys -cacerts
  keytool -importcert -noprompt -keystore ${rootCA}.p12 -storepass ${STOREPASS} -alias ${rootCA} \
    -file ${rootCA}/certs/${rootCA}.cert.pem
fi
# Now we use the intermediate CA from now on for signing
export CA=${iCA}
export CA_POLICY=policy_loose

# ---
# Server Certs
# ---
for cert in ${SERVERCERTS}; do

  FNAME=Egeria${cert}
  rm -f -- ${FNAME}*
  printf "\n\n---- Generating cert (%s)  ----\n\n" ${FNAME}
  openssl genrsa -aes256 -out ${FNAME}.key.pem -passout pass:${KEYPASS} 2048
  openssl req -new -batch -passin pass:${KEYPASS} -key ${FNAME}.key.pem \
    -out ${FNAME}.csr.pem -days 825 -subj '/C=US/ST=CA/O=LFAIData/CN='${cert} \
    -config ./openssl.cnf
  openssl ca -in ${FNAME}.csr.pem -out ${FNAME}.cert.pem -extensions server_cert -notext -days 375 \
    -config ./openssl.cnf -batch -passin pass:${KEYPASS}
  # We include the full chain in the .p12 archive
  cat ${rootCA}/certs/${rootCA}.cert.pem ${iCA}/certs/${iCA}.cert.pem >tmp.pem
  openssl pkcs12 -export -chain -in ${FNAME}.cert.pem -CAfile tmp.pem -inkey ${FNAME}.key.pem -name $FNAME -out ${FNAME}.p12 \
    -passin pass:${KEYPASS} -passout pass:${KEYPASS}
done

# ---
# Client certs
# ---
for cert in ${CLIENTCERTS}; do
  printf "\n\n---- Generating cert (%s)  ----\n\n" ${FNAME}
  FNAME=Egeria${cert}
  rm -f -- ${FNAME}*
  openssl genrsa -aes256 -out ${FNAME}.key.pem -passout pass:${KEYPASS} 2048
  openssl req -batch -new -passin pass:${KEYPASS} -key ${FNAME}.key.pem \
    -out ${FNAME}.csr.pem -days 825 -subj '/C=US/ST=CA/O=LFAIData/CN='${cert} \
    -config ./openssl.cnf
  openssl ca -in ${FNAME}.csr.pem -out ${FNAME}.cert.pem -extensions client_cert -notext -days 375 \
    -config ./openssl.cnf -batch -passin pass:${KEYPASS}
  cat ${rootCA}/certs/${rootCA}.cert.pem ${iCA}/certs/${iCA}.cert.pem >tmp.pem
  openssl pkcs12 -export -in ${FNAME}.cert.pem -CAfile tmp.pem -inkey ${FNAME}.key.pem -name $FNAME -out ${FNAME}.p12 \
    -passin pass:${KEYPASS} -passout pass:${KEYPASS}
done

# ---
# Distribute certs in build tree
#
# This is for transition only -- to validate/test the certs created here and replace
# the simpler certs originally provided with egeria
# This is a dev activity, not deployment, and primarily to support testing as we migrate
# ---
echo "\n\n---- Deploying certs to current build"
BASE=../../..
cp ${rootCA}.p12 ${BASE}/truststore.p12
cp EgeriaClient.p12 ${BASE}/EgeriaClient.p12
cp EgeriaUIChassis.p12 ${BASE}/open-metadata-implementation/user-interfaces/ui-chassis/ui-chassis-spring/src/main/resources/keystore.p12
cp ${rootCA}.p12 ${BASE}/open-metadata-implementation/user-interfaces/ui-chassis/ui-chassis-spring/src/main/resources/truststore.p12
cp EgeriaServerChassis.p12 ${BASE}/open-metadata-implementation/server-chassis/server-chassis-spring/src/main/resources/keystore.p12
cp ${rootCA}.p12 ${BASE}/open-metadata-implementation/server-chassis/server-chassis-spring/src/main/resources/truststore.p12

# ---
# output message
# ---
echo "\n\n---- Certificate generation complete."
