<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Migrating configuration documents

As ODPi Egeria evolves, the content of the configuration document expands.
Many of the changes are pure additions and are therefore backward compatible.
However, from time to time the structure of the configuration document needs to
change.  When this happens the OMAG Server Platform is not able to load the
configuration document and a message similar to this is returned:

```json
{
    "class": "VoidResponse",
    "relatedHTTPCode": 400,
    "exceptionClassName": "org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException",
    "exceptionErrorMessage": "OMAG-ADMIN-400-022 The configuration document for OMAG server cocoMDS1 is at version V1.0 which is not compatible with this OMAG Server Platform which supports versions [V2.0]",
    "exceptionSystemAction": "The system is unable to configure the local server because it can not read the configuration document.",
    "exceptionUserAction": "Migrate the configuration document to a compatible version (or delete and recreate it).  See https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/user/migrating-configuration-documents.html"
}
```

The guidance for each version change is below ...

## Migrating a configuration document from V1.0 to V2.0

The `additionalProperties` property name changed to `configurationProperties`.
To migrate the configuration document, make a global change from 
`additionalProperties` to `configurationProperties` throughout the configuration document.

## Release 2.x+ of Egeria

Release 2.0 encrypts the configuration document by default. This includes automatically
detecting and encrypting any clear-text (unencrypted) configuration document that may already
exist. No user action is required for this migration, the encryption will be handled
automatically when the clear-text configuration document is first opened by the platform in
these releases.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.