<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# 0011 Managing Referenceables

**Referenceables** can have chains of related feedback and additional knowledge attached to
them. The following types help a metadata manager to process these collections of elements
more efficiently.

![UML](0011-Managing-Referenceables.png#pagewidth)


The **LastChange** classification is a convenience mechanism to
indicate where the last change occurred.  Components that are monitoring referenceables can use
the open metadata events related to classifications to maintain a complete picture of the asset.

The **Template** classification indicates that a referenceable is a good element be used as a template
when creating a new element of the same type.  There is no restriction on using referenceables
without this classification as templates.  The Template classification is simply a useful marker
to enable templates to be found.  

When one referenceable is created by using another referencable as
a template, the **qualifiedName** must be changed
in the new referenceable to give it a unique name - often the **displayName** changes too.
This makes it hard to identify which referenceables have been created from a template.
The **SourcedFrom** relationship is used to show the provenance of the information from the template.
This is useful to help trace where information has come from and to help understand any
potential impact cause by a change to the template if this change also needs to be made to
the elements that were copied from it.

## More information

* [Cataloguing assets](../cataloging-assets)
* [Using templates](../cataloging-assets/templated-cataloging.md)

----
Return to [Area 0](Area-0-models.md).




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.