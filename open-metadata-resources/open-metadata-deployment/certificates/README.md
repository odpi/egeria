<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Transport Level Security in Egeria & Certificates

The Egeria applications need a truststore and a keystore to communicate securely.
The truststore keeps the list of certificates for trusted servers and the keystore defines the certificate information that the server sends out.

The `gensamplecerts.sh` script is used for generating self-signed certificates for the OMAG Server Platform, Egeria UI Platform and REACT UI presentation server.
Once the certificates are created, the script copies them into the right locations to be picked up by the build.

- The root directory for egeria gets the `truststore.p12` and the `keystore.p12` for the OMAG Server Platform.  This is copied into the distributions when the assembly is created.
- The same truststore and the specialized keystore for the Egeria UI Platform are copied into `egeria/open-metadata-implementation/user-interfaces/ui-chassis/ui-chassis-spring/src/main/resources` which results in them being included in the jar file for this application.

## Set up to run

The `gensamplecerts.sh` script uses `openssl`.  The default version for Linux is ok, but the version on the Mac is `libressl` by default and you need to use `brew install openssl@3` to install the genuine openssl library and then:

```bash
$ export PATH="/opt/homebrew/opt/openssl@3/bin:$PATH"
$ ./gensamplecerts.sh
```

The next time you run the build, the new certificates are incorporated in the distribution.

After running the script, be sure to check in all the updated files into GitHub since they contain counts.

It is possible to reset all the counts by setting CA_CLEAN environment variable.  However, this should not be the normal process. 

## Using the certificates

Refer to the [admin guide documentation](https://egeria-project.org/guides/admin/configuring-the-omag-server-platform) for information on using the examples in this directory.

**Note:** Any generated certs are provided as an example only and to support demos and must not be used for production.  They assume the hostname is "localhost" and do not work in a distributed setup.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
