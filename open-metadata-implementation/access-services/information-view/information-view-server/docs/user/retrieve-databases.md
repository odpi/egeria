<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Retrieve databases

Retrieve list of databases using pagination

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/information-view/users/{userId}/databases?pageSize=10&startFrom=0
```

`DatabaseListResponse` response containing list of databases or
`VoidResponse` with error message.

Sample:

```json
{
    "class": "DatabaseListResponse",
    "relatedHTTPCode": 200,
    "databasesList": [
        {
            "class": "DatabaseSource",
            "@id": 1,
            "guid": "b1c497ce.6e83759b.0v9mgsb2t.fauo7tn.0vv57r.miqdq9fqjod6vnco6jfep",
            "name": "EMPLSANL",
            "endpointSource": {
                "class": "EndpointSource",
                "@id": 2,
                "networkAddress": "{host}",
                "connectorProviderName": "DB2Connector"
            }
        },
        {
            "class": "DatabaseSource",
            "@id": 3,
            "guid": "b1c497ce.6e83759b.0v9mgnepg.2vq7cd4.m41j85.01dktrea2fingt8raol7l",
            "name": "IADB",
            "endpointSource": {
                "class": "EndpointSource",
                "@id": 4,
                "networkAddress": "{host}",
                "connectorProviderName": "DB2Connector"
            }
        },
        {
            "class": "DatabaseSource",
            "@id": 5,
            "guid": "b1c497ce.6e83759b.0v9mgsb23.pajbvbk.bi4t6t.6bj8srpdi3ra2uo10pslq",
            "name": "EMPLOYEE",
            "endpointSource": {
                "class": "EndpointSource",
                "@id": 6,
                "networkAddress": "{host}",
                "connectorProviderName": "DB2Connector"
            }
        },
        {
            "class": "DatabaseSource",
            "@id": 7,
            "guid": "b1c497ce.6e83759b.0v9mgsb1m.9vb8pg1.6dhnfn.eqf2drkck0l4nho3m500f",
            "name": "LOCATION",
            "endpointSource": {
                "class": "EndpointSource",
                "@id": 8,
                "networkAddress": "{host}",
                "connectorProviderName": "DB2Connector"
            }
        },
        {
            "class": "DatabaseSource",
            "@id": 9,
            "guid": "b1c497ce.6e83759b.0v9mgsb39.qiqs969.3r3pjo.qbkl7gfvsu96q5ohpddb4",
            "name": "PATIENT",
            "endpointSource": {
                "class": "EndpointSource",
                "@id": 10,
                "networkAddress": "{host}",
                "connectorProviderName": "DB2Connector"
            }
        },
        {
            "class": "DatabaseSource",
            "@id": 11,
            "guid": "b1c497ce.6e83759b.0v9mgsb2h.cgleq3j.tn4590.f6182m5pasm71v45do246",
            "name": "COMPDIR",
            "endpointSource": {
                "class": "EndpointSource",
                "@id": 12,
                "networkAddress": "{host}",
                "connectorProviderName": "DB2Connector"
            }
        }
    ]
}
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.