<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
# Encrypted Configuration File Store Connector

The encrypted configuration file store connector supports managing the
open metadata configuration as an encrypted file.

Symmetric encryption is used to encrypt the contents of the configuration
file using a particular encryption key, and to use the same encryption key
to decrypt the contents of the configuration file.

It is naturally important to keep this encryption key safe to ensure the
security of the configuration file.

This is done as follows:

- A new encryption key is generated and stored in a randomly-
    generated filename with a pre-defined extension (`.key`) under a
    randomly-generated directory name with a pre-defined prefix
    (`keystore_`).
- The connector searches for exactly one such key (and directory) to
    use when trying to retrieve a configuration. If it cannot be found,
    the configuration cannot be decrypted.
- When creating (saving) a configuration for the first time using this
    connector, the key (and directory) are generated and persisted to
    the file system of the OMAG Server Platform. The directory and file
    are made only accessible to the OS-level account under which the
    OMAG Server Platform runs.

The randomly-generated directory and file names ensure that the file
containing the encryption key cannot be easily exfiltrated from the
system without already having access to the account under which the
OMAG Server Platform itself runs (to be able to see the precise directory
and file names that would be required to exfiltrate them).

The connector makes use of [Google's Tink](https://github.com/google/tink)
for encryption. While only the basic file-based mechanism of retaining
the encryption key is currently provided, this could likely be extended
to support other capabilities provided by Tink itself (like the use of
remote Key Management Services to hold the master key) with some additional
configuration inputs.

Return to [configuration-store-connectors](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.