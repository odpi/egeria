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

export iCA=EgeriaIntermediateCA
export rootCA=EgeriaRootCA

CA_CHAIN=EgeriaCAChain

# Used in openssl.cnf for the subjectAltName where needed - export to environment
export SAN="DNS:localhost"

# A few empty dirs needed
for d in ./${rootCA} ./${iCA}
do
  for e in certs crl csr newcerts private
  do
    mkdir -p ${d}/${e}
  done
  chmod 700 ${d}/private
  touch ${d}/index.txt
  echo 1000 > ${d}/serial
done
echo 1000 > ./${iCA}/crlnumber

# ---
# Root Certificate Authority
#
# Generates a root Certificate Authority for all the certs we create here in Egeria
# ---
export CA=${rootCA}
export CA_POLICY=policy_strict
printf "\n\n---- Generating root CA ($rootCA)  ----\n\n"
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
printf "\n\n---- Generating Intermediate CA ($iCA)  ----\n\n"
openssl genrsa -aes256 -out ${iCA}/private/${iCA}.key.pem -passout pass:${KEYPASS} 4096
openssl req  -batch -passin pass:${KEYPASS} -new -key ${iCA}/private/${iCA}.key.pem \
        -sha256 -out ${iCA}/csr/${iCA}.csr.pem \
        -subj '/C=US/ST=CA/O=LFAIData/CN=EgeriaIntermediateCertificateAuthority'\
        -config ./openssl.cnf 
export CA=${rootCA}
export CA_POLICY=policy_strict
openssl ca -config ./openssl.cnf -batch -passin pass:${KEYPASS} \
        -extensions v3_intermediate_ca -days 3650 -notext -md sha256 \
        -in ${iCA}/csr/${iCA}.csr.pem -out ${iCA}/certs/${iCA}.cert.pem


# Create certificate chain files
printf "\n\n---- Create CA chain files ----\n\n"
cat ${rootCA}/certs/${rootCA}.cert.pem ${iCA}/certs/${iCA}.cert.pem > ${CA_CHAIN}.cert.pem
keytool -importcert -trustcacerts -alias EgeriaCA -file ${CA_CHAIN}.cert.pem -keypass ${KEYPASS} \
        -storepass ${STOREPASS} -noprompt -destkeystore ${CA_CHAIN}.p12

# ---
# Server Certs
# ---
for cert in ${SERVERCERTS}
do
  FNAME=Egeria${cert}
  printf "\n\n---- Generating cert ($FNAME)  ----\n\n"
  openssl genrsa -aes256 -out ${FNAME}.key.pem -passout pass:${KEYPASS} 2048
  openssl req -new -batch -passin pass:${KEYPASS} -key ${FNAME}.key.pem \
          -out ${FNAME}.csr.pem -days 825 -subj '/C=US/ST=CA/O=LFAIData/CN='${cert} \
          -config ./openssl.cnf 
  openssl ca -in ${FNAME}.csr.pem -out ${FNAME}.cert.pem -extensions server_cert -notext -days 375 \
          -config ./openssl.cnf -batch -passin pass:${KEYPASS}
  keytool -importcert -alias ${FNAME} -file ${FNAME}.cert.pem -keypass ${KEYPASS} -storepass ${STOREPASS} \
          -noprompt -destkeystore ${FNAME}.p12
done

# ---
# Client certs
# ---
for cert in ${CLIENTCERTS}
do
  printf "\n\n---- Generating cert ($FNAME)  ----\n\n"
  FNAME=Egeria${cert}
  openssl genrsa -aes256 -out ${FNAME}.key.pem -passout pass:${KEYPASS} 2048
  openssl req -batch -new -passin pass:${KEYPASS} -key ${FNAME}.key.pem \
          -out ${FNAME}.csr.pem -days 825 -subj '/C=US/ST=CA/O=LFAIData/CN='${cert} \
          -config ./openssl.cnf 
  openssl ca -in ${FNAME}.csr.pem -out ${FNAME}.cert.pem -extensions client_cert -notext -days 375 \
          -config ./openssl.cnf -batch -passin pass:${KEYPASS}
  keytool -importcert -alias ${FNAME} -file ${FNAME}.cert.pem -keypass ${KEYPASS} -storepass ${STOREPASS} \
          -noprompt -destkeystore ${FNAME}.p12
done

# ---
# output message
# ---
echo "\n\n---- Certificate generation complete."




