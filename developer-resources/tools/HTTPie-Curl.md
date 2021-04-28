<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Command line Http request tools - HTTPie and Curl

In addition to [Postman](Postman.md) there are command line tools for
calling REST APIs.

The command that is most commonly available is **curl**.
It uses commands such as:

```bash
$ curl --insecure -X GET https://localhost:9443/open-metadata/platform-services/users/test/server-platform/origin
Egeria OMAG Server Platform (version 2.10-SNAPSHOT)
```

Note that Egeria is using https, so if you have not replaced the provided self-signed certificate, ensure to add '--insecure' to any requests 
to skip certificate validation


As an alternative you might like to try [HTTPie](https://httpie.org/) which has more advanced functions

Note that Egeria is using https, so if you have not replaced the provided self-signed certificate, ensure to add '--verify no' to any requests 
to skip certificate validation
----
* Return to [Developer Tools](.)


* Link to [Egeria's Community Guide](../../Community-Guide.md)
* Link to the [Egeria Dojo Education](../../open-metadata-resources/open-metadata-tutorials/egeria-dojo)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
