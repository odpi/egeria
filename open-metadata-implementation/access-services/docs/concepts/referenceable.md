<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Referenceable metadata element

A referenceable metadata element is an element that is significant enough to need
its own unique name so that it can be referred to accurately in different contexts
(typically outside of open metadata).

The unique name is stored in the qualified name (**qualifiedName**) property and the open
metadata servers validate that this value is unique within its type.

It is possible that two different metadata elements with the same qualified name
could be created in different metadata servers.  This may be because
both servers have loaded metadata about the same object.
Alternatively, there is a name clash as two servers have used the same unique name for
two different objects.


## Further reading

* [GUIDs](guid.md)
* [Referenceable Open Metadata Type](../../../../open-metadata-publication/website/open-metadata-types/0010-Base-Model.md)




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.