<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# Big glossaries

This utility creates 26 glossaries of 3,000 unique terms each, one glossary per archive file.  Each archive is
written to the repository's `content-packs` directory and is named for the glossary it holds:

* `BigGlossaryA.omarchive`
  * BigGlossaryA
     * TermA00001
     *  :
     * TermA03000
* `BigGlossaryB.omarchive`
  * BigGlossaryB
     * TermB00001
     *  :
     * TermB03000
*    :
* `BigGlossaryZ.omarchive`
  * BigGlossaryZ
     * TermZ00001
     *  :
     * TermZ03000

They are used for testing a deployment environment to make sure it has enough resources to manage a large repository.

The archives are not committed to the repository - run the utility when you need them.  It is easily extended to
create more terms per glossary, or to create additional glossaries.

----

* Return to [sample metadata](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.