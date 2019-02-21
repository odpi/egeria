<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# IBM IGC Extensions

As part of implementing the IBM IGC Repository Connector, extension have been developed through the software's
extensibility mechanisms like "OpenIGC", which allows developers to extend the object types in IGC.

## `OMRS` bundle

The OpenIGC bundle named `OMRS` has been created to implement some mechanisms that are not native to IGC itself.

Currently the bundle contains the following types:

### OMRS Stub

The `$OMRS-Stub` object type is added to provide a mechanism for tracking limited history of objects in IGC,
which is useful for creating various OMRS events related to changes (eg. `updateEntity`, etc), where both the "old" and
"new" entity details are provided.

Basically the `$OMRS-Stub` object captures the following details:

- `sourceRID`: the RID of the *real* asset that the stub represents
- `sourceType`: the IGC asset type of the *real* asset that the stub represents
- `payload`: a JSON blob containing the full contents of the *real* asset's properties, at the time the stub was last
    updated (this is basically IGC's REST API form of an object's details)

The intention is that at any given point in time, it is then possible to compare the *real* asset (ie. its latest state)
against this `payload` property to determine what (if any) changes have occurred since the last update to the stub.

Instances of these objects are automatically managed by the event mapper processing as follows:

- When receiving an event from IGC, the event mapper will first check the type of event -- if it is a `delete` action,
    the event mapper will proceed with creating a `purge` event for the asset.
    - It will then retrieve any `$OMRS-Stub` whose `sourceRID` property matches the deleted asset, and delete this
        `$OMRS-Stub`
- Otherwise, the event mapper will check for the existence of an `$OMRS-Stub` whose `sourceRID` property matches the
    RID of the asset in the event.
    - If no matching `$OMRS-Stub` exists, the event mapper will treat the message as a `create` (new) event for the
        asset.
    - If a matching `$OMRS-Stub` exists, the event mapper will treat the message as an `update` (change) event for the
        asset.
- For updates, the event mapper will calculate the differences between the current real asset and the `payload` property
    of the `$OMRS-Stub` instance.
    - Based on any differences, the event mapper may recursively call its processing functions to produce other related
        events such as the addition of a relationship, etc.
- After writing the appropriate event to the OMRS cohort (including unwinding from any recursion), the event mapper will
    upsert an `$OMRS-Stub` for the asset involved in the event.

**Worth noting**: Since the event mapper recursively determines changes based on these objects, what you might expect
was a simple change in the IGC UI could result in many events -- in particular when using an IGC environment which
is already populated with metadata before being connected up as an OMRS repository. This is because no such `$OMRS-Stub`
instances will yet exist: so the very first piece of metadata picked up as an event will likely trigger many other
relationships, corresponding entities, etc to be recursively detected as well.

**Quick tip**: Since the event mapper uses these objects for its change detection, you can effectively reset the events
that the event mapper will generate by deleting the appropriate `$OMRS-Stub` instance. For example: you can delete the
`$OMRS-Stub` instance for a particular term, make a simple change to the term itself, and the event mapper will generate
a `create` event rather than an `update`.

## How the extensions work

The extensions themselves are part of the source code tree under `src/main/resources`, and are built into their
"bundled" form (essentially just a `.zip` file) as part of the Maven code build process.

The bundled form is then automatically deployed to your IGC environment during the initialization of the connector:
specifically, when you call the:

```
POST http://localhost:8080/open-metadata/admin-services/users/{{user}}/servers/{{server}}/instance
```

API interface of the OMAG Server Platform that has been configured to connect to an IGC environment
(see [Getting Started](../README.md)).

Because the extensions are necessary for the connector to operate, if there are any errors or problems deploying the
extensions you should be notified of these during the initialization: you will most likely receive an error `500`
response and should consult the Egeria and IGC logs for further details.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
