<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Unique Identifiers (GUIDs)

Every [open metadata instance](../../../open-metadata-implementation/repository-services/docs/open-metadata-instances.md)
has a unique identifier called the guid (globally unique identifier).
This identifier needs to be globally unique - so even if two metadata repositories
simultaneously created a metadata instance about the same thing, the GUIDs of these
two instances would be different.

In Egeria, new GUIDs are created
using the `UUID.randomUUID().toString()` method and they look something like this:

```
87b06ffe-9db2-4ef5-ba6e-8127480cf30d
```

They are often used on [Open Metadata Access Service (OMAS)](../../../open-metadata-implementation/access-services)
API calls to request a specific instance of metadata.
However, they are not very consumable for people, so most metadata instances also have
[qualified names](../../../open-metadata-implementation/access-services/docs/concepts/referenceable.md) - also unique - and display names - not necessarily unique
for displaying information to end users.

There is a tiny chance that two servers will generate the same guid. If this happens, it is
detected by the [repository services](../../../open-metadata-implementation/repository-services) and messages are
output on the detecting server's 
[audit log](../../../open-metadata-implementation/repository-services/docs/component-descriptions/audit-log.md).
The repository services also have APIs for re-identifying (ie changing the guid)
for a metadata instance.

----
* Return to [Glossary](../open-metadata-glossary.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.