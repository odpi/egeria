<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Security Samples

The open metadata security samples provide sample implementations of the
[open metadata security connectors](https://egeria-project.org/services/metadata-security-services)
that can be added to the OMAG Server Platform and to any OMAG server
running on the platform.  With these samples it is possible to experiment with
how security authorization works with the
[governance zones](https://egeria-project.org/concepts/governance-zone)
that control the visibility of assets
through the [Open Metadata Access Services (OMASs)](https://egeria-project.org/services/omas).

The open metadata platform security connector provides authorization
services for the platform services and the admin service to create a new server.
The open metadata server security connector is specific to an OMAG server instance and
is defined in the configuration document for a server.

The samples show how a security connector extends the appropriate base class and uses their methods to provide
a security service.

The samples are based on the [Coco Pharmaceuticals persona](https://egeria-project.org/practices/coco-pharmaceuticals).

## Platform services

Gary Geeke (`garygeeke`) is the IT Infrastructure Administrator and the IT Infrastructure Governance Officer.
He is the only person able to issue platform services requests, and work with assets in the **infrastructure** zone.

## Asset Management

Peter Profile (`peterprofile`), Information Analyst, and Erin Overview (`erinoverview`),
their Information Architect and Deputy Chief Data Officer,
are the only people permitted to onboard new assets through the **quarantine** zone using the
[Asset Owner OMAS](https://egeria-project.org/services/omas/asset-owner/overview).  Specifically
only Erin can remove the quarantine zone from an Asset.

The other zones defined in the sample are:

* **personal-files** zone - Assets that are only visible to the creator.
* **data-lake** zone - Assets that are read-only to all employees of Coco Pharmaceuticals with access to the data lake services. 
  The non-personal accounts (NPAs) used by the engines that manage the data lake are the only users that can update or delete these assets
* **external-access** - Assets that can be seen by external collaborators, such as partners from the hospitals.
* **research** zone - Assets for the research team Callie Quartile (`calliequartile`) and Tessa Tube (`tessatube`).
* **human-resources** zone - Assets for the HR team, currently just Faith Broker (`faithbroker`).
* **finance** zone - Assets for the finance team Reggie Mint (`reggiemint`), Tom Tally (`tomtally`) and Sally Counter (`sallycounter`).
* **clinical-trials** zone - Assets dedicated to supporting clinical trials which are managed by Tanya Tidie (`tanyatidie`).
* **infrastructure** zone - Assets describing the IT infrastructure that supports Coco Pharmaceuticals.  These are accessible by Gary Geeke (`garygeeke`) alone.
* **development** zone - Assets in use by the development team including Polly Tasker (`pollytasker`), Bob Nitter (`bobnitter`), Lemmie Stage (`lemmiestage`), Nancy Noah (`nancynoah`) and Des Signa (`dessigna`)
* **manufacturing** zone - Assets used in the manufacturing process currently managed by Stew Faster (`stewfaster`).
* **governance** zone - Assets used to govern the organization.  These are effectively the governance leadership team of Jules Keeper (`juleskeeper`),
  Erin Overview (`erinoverview`), Gary Geeke (`garygeeke`), Polly Tasker (`pollytasker`), Faith Broker (`faithbroker`), Ivor Padlock (`ivorpadlock`) and Reggie Mint (`reggiemint`).
* **trash-can** zone - Assets that are waiting to be deleted or archived - this is handled by their archiver processes.  All the NPA accounts have access to this zone to
  enable processes to retrieve files from the trash can and restore them to their original zones.

An asset may be in multiple zones and a person is typically able to access the asset if any of its zones permit access to them.
However, the implementation of your connector may also look for specific combinations of zones and apply special rules.
For example, the **quarantine** zone rules override any other zone's rules
to allow the onboarding team to set up the zones as part of the onboarding process.
Only when the **quarantine** zone is removed, do the other zones take effect.

## Controlling access to services

It is also possible to have special rules for particular services.
Coco Pharmaceuticals have decided that the **assetDelete** method from Asset Owner OMAS is too powerful
to use, and so they have disabled it using this connector.
Only non-personal accounts (NPA) can use this method.
Coco Pharmaceutical's staff delete an asset by moving it to the
"trash-can" zone where it is cleaned up by automated archiver
processes the next day.

## Controlling access to glossaries

The connector illustrates how the **SecurityTags** classification can be used to
guide its look up for a user/group in an external authorization service.

This look up needs to know who the requesting user is, and which group/role to use
to look up whether the user is permitted to perform a specific action.

The **SecurityTags** classification can be attached to an element to show which group
in the external authorization service controls the access to the element.

In the server security connector, this style is illustrated in the glossary security interface.
There is no external authorization service so the access control lists are stored directly in the
**SecurityTags** classification properties - in the **accessGroups** properties to be precise.

The **accessGroups** property is a map from name to string array.
The name can be one of the following, and it is mapped to a list of userIds.

* glossaryCreate - who can create new glossaries
* glossaryDetailUpdate - who can change the properties of a glossary element
* glossaryMemberUpdate - who can create/update a glossary term or glossary category
* glossaryMemberStatusUpdate - who can change the status of a glossary term
* glossaryFeedback - who can add comments and other feedback to the glossary and its terms and categories.
* glossaryRead - who can read the terms and categories in the glossary
* glossaryDelete - who can delete a glossary and all of its contents

Here is an example in JSON - used for setting the access groups on the REST API.

```json

accessGroups = {
    "glossaryMemberUpdate" : [ erinsUserId , harrysUserId ],
    "glossaryMemberStatusUpdate" : [ erinsUserId ],
    "glossaryRead" : [ erinsUserId , harrysUserId ]
}
```


## Connection security

Finally, Coco Pharmaceuticals only permit non-personal accounts (NPAs)
to access Connection object that have security information in it such as userIds and passwords.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.