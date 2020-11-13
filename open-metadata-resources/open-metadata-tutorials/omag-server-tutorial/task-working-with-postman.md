<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Working with Postman

Postman is a test tool that allows you to type in calls to
[REST APIs](https://en.wikipedia.org/wiki/Representational_state_transfer).

Basically a REST API is an application program
interface (API) that uses HTTP requests to GET, PUT, POST and DELETE data.
The call is made using a URL - just like requesting a web page
from your browser.  In fact, when you request a web page from your browser,
the browser is issuing a GET HTTP request for the page.

REST APIs for services such as the open metadata and
governance services of ODPi Egeria use the full range of
HTTP requests as follows:

* **GET** - retrieving simple structures.
* **PUT** - creating simple structures.
* **POST** - creating, updating, deleting complex structures and retrieving long lists of information with paging.
* **DELETE** - deleting simple structures.

With Postman it is possible issue these HTTP requests and experiment
with what they do.  It also supports **collections** of requests.
We use Postman collections in tutorials to illustrate the
calls and save you typing in the full URLs (which can be quite long :).

Egeria by default uses https:// requests with a self-signed certificate. Any PostMan users therefore will need to
go into settings->general and turn off 'SSL certificate verification' or requests will fail.

Postman is a [free download](https://www.getpostman.com/) with optional enterprise licenses
for teams.

It includes a wide variety of [tutorials](https://learning.getpostman.com/concepts/)
to help you go from novice to expert.

Familiarity with Postman will help you get the most value from
the ODPi Egeria tutorials.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
