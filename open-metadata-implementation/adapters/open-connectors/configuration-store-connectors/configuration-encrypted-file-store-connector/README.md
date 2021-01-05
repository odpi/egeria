<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
![Released](../../../../../open-metadata-publication/website/images/egeria-content-status-released.png#pagewidth)

[![javadoc](https://javadoc.io/badge2/org.odpi.egeria/configuration-encrypted-file-store-connector/javadoc.svg)](https://javadoc.io/doc/org.odpi.egeria/configuration-encrypted-file-store-connector)

# Encrypted Configuration File Store Connector

The encrypted configuration file store connector supports managing the
open metadata configuration as an encrypted file.

Symmetric encryption is used to encrypt the contents of the configuration
file using a particular encryption key, and to use the same encryption key
to decrypt the contents of the configuration file.

It is therefore important to keep this encryption key safe to ensure the
security of the configuration file.

The following methods are supported to secure the keys, and are used in this
order of precedence (the higher in the list, the higher the precedence):

1. If an environment variable named `EGERIA_CONFIG_KEYS` is set, this is
    used as the keyset for encryption. This ensures that the keyset can be
    injected into the environment, for example at runtime of a Kubernetes
    pod, and is never actually persisted inside the system itself.
1. If a single directory with a name starting with `keystore_` exists
    in the `data/platform/keys` directory,relative to the current 
    working directory, of the OMAG Server Platform,
    and has within it a single file with a `.key` extension, the contents
    of this file are used as the keyset for encryption. This allows an
    operator to define and manage their own keyset, for example using Google's
    `Tinkey` utility. (See notes below on the generated directory for securing
    such a locally-stored keyset.)
1. If not, a new encryption key is generated and stored in a randomly-
    generated filename with a pre-defined extension (`.key`) under a
    randomly-generated directory name with a pre-defined prefix
    (`keystore_`). These are generated in the `data/platform/keys` working 
    directory relative to
    which the OMAG Server Platform is run, and are made accessible only
    to the OS-level account under which the OMAG Server Platform runs.
    The randomly-generated directory and file names ensure that the file
    containing the encryption key cannot be easily exfiltrated from the
    system without already having access to the account under which the
    OMAG Server Platform itself runs (to be able to see the precise directory
    and file names that would be required to exfiltrate them). This default
    fallback of generating such keysets ensures that the configuration files
    can be encrypted by default in a relatively secure way, without relying
    on any upfront input from an operator.

To read an encrypted configuration file, again in precedence order:

1. The connector checks if an environment variable named `EGERIA_CONFIG_KEYS`
    is set. If it is, this is used as the keyset for decryption.
1. If not, the connector searches for exactly one `...key` file (under exactly
    one `keystore_...` directory) under `data/platform/keys`. 
    If found, the contents of this file are used as the keyset for decryption.

Note that if neither of these is found, any pre-existing configuration cannot be
decrypted. **To ensure the encryption is secure, there is no mechanism to recover
these keys, so guard them appropriately if your configuration cannot be easily
recreated.**

The connector makes use of [Google's Tink](https://github.com/google/tink)
for encryption. Therefore, the contents of the keyset (whether the environment
variable or the file) must conform to the way Google Tink stores these as JSON.
For example:

```json
{
  "primaryKeyId": 123456789,
  "key": [
    {
      "keyData": {
        "typeUrl": "type.googleapis.com/google.crypto.tink.ChaCha20Poly1305Key",
        "keyMaterialType": "SYMMETRIC",
        "value": "EiDnb78IK7j/gpHMt0XdITLd3AbvAAQwvGlekvp70GTc2Q=="
      },
      "outputPrefixType": "TINK",
      "keyId": 123456789,
      "status": "ENABLED"
    }
  ]
}
```

Return to [configuration-store-connectors](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.