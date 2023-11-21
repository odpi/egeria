<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Developer utilities

These utilities can be used as is.  They were written to support Egeria's developer education but can be useful
when you are working on new connectors for Egeria.
They have hard-coded defaults at the top that you can change for your deployment.
Also, feel free to extend them to match your specific deployment.

* **egeria-config-utility** - issues commands to configure different types of OMAG servers.
  It has a list of default values at the top of the file and that you can update for your environment
  and you can extend with new commands and options.

* **egeria-ops-utility** - issues commands to start and stop different types of OMAG servers.
  It has a list of default values at the top of the file and that you can update for your environment
  and you can extend with new commands and options.

* **big-glossaries** - creates 10 glossaries of 10,000 unique terms each in their own archive file.  
  They are used for testing a deployment environment to make sure it has enough resources to manage a large repository.
  The archive files are named as follows:

    * BigGlossaryA.json
        * BigGlossaryA
            * TermA00001
            *  :
            * TermA10000
        * BigGlossaryB
            * TermB00001
            *  :
            * TermB10000
        *    :
        * BigGlossaryJ
            * TermJ00001
            *  :
            * TermJ10000


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.