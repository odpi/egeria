/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * This middleware method takes off the first segment which is the serverName an puts it into a query parameter
 * I did consider using Regex match and replace along these lines 'const matchCriteria = /^\/([A-Za-z_][A-Za-z_0-9]+)\//;'
 * but decided not to in case the characters I was tolerating was not enough. Note the split slice join is not not very performant
 * due to the creation of array elements.
 *
 * For urls that start with servers - these are rest calls that need to be passed through to the back end.
 * URLs before and after
 *   /   => /
 *   /servers/aaa => /servers/aaa
 *   /servers/aaa/bbb => /servers/aaa/bbb
 *   /coco1/ => /?servername=coco1
 *   /coco1/abc => /abc?servername=coco1
 *   /coco1/abc/de => /abc/de?servername=coco1
 *   /display.ico => /display.ico
 *
 */
const serverNameMiddleWare = (req, res, next) => {

  console.log("before " + req.url);
  const segmentArray = req.url.split("/");
  const segmentNumber = segmentArray.length;

  if (segmentNumber > 1) {
    const segment1 = segmentArray.slice(1, 2).join("/");
    console.log("segment1 " + segment1);

    if (segment1 != "servers" && segment1 != "open-metadata") {
      // in a production scenario we are looking at login, favicon.ico and bundle.js for for now look for those in the last segment
      // TODO once we have development webpack, maybe the client should send a /js/ or a /static/ segment after the servername so we know to keep the subsequent segments.

      const lastSegment = segmentArray.slice(-1);
      console.log("Last segment is " + lastSegment);
      if (
        lastSegment == "bundle.js" ||
        lastSegment == "favicon.ico" ||
        lastSegment == "login"
      ) {
        req.url = "/" + lastSegment;
      } else {
        // remove the server name and pass through
        req.url = "/" + segmentArray.slice(2).join("/");
      }
      req.query.serverName = segment1;
    }
  }
  console.log("after " + req.url);
  next();

}

module.exports = serverNameMiddleWare;