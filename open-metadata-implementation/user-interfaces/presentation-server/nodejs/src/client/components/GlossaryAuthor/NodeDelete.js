/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useContext } from "react";

import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";

function NodeDelete(props) {
  // const {node} = props;
  console.log("NodeDelete");

  const glossaryAuthorContext = useContext(GlossaryAuthorContext);
  console.log("NodeDelete glossaryAuthorContext", glossaryAuthorContext);

  const [errorMsg, setErrorMsg] = useState();
  const [issuedDelete, setIssuedDelete] = useState(false);

  function issueDelete() {
    setIssuedDelete(true);
    const guid = glossaryAuthorContext.selectedNode.systemAttributes.guid;
    const url = glossaryAuthorContext.currentNodeType.url + "/" + guid;
    console.log("issueDelete " + url);
    setErrorMsg("");
    let msg = "";
    fetch(url, {
      method: "delete",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    })
      .then((res) => res.json())
      .then((res) => {
        console.log("delete completed ");
        if (res.relatedHTTPCode == 200) {
          console.log("Delete successful for guid " + guid);
          glossaryAuthorContext.setRefreshSearchActionState();
        } else {
          // if this is a formatted Egeria response, we have a user action
          if (res.relatedHTTPCode) {
            if (res.exceptionUserAction) {
              msg = "Delete Failed: " + res.exceptionUserAction;
            } else {
              msg =
                "Delete Failed unexpected Egeria response: " +
                JSON.stringify(res);
            }
          } else if (res.errno) {
            if (res.errno == "ECONNREFUSED") {
              msg = "Connection refused to the view server.";
            } else {
              // TODO create nice messages for all the http codes we think are relevant
              msg = "Delete Failed with http errno " + res.errno;
            }
          } else {
            msg = "Delete Failed - unexpected response";
          }
          setErrorMsg(errorMsg + ",\n" + msg);
        }
      })
      .catch((res) => {
        msg = "Delete Failed - logic error " + JSON.stringify(res);
        setErrorMsg(errorMsg + ",\n" + msg);
        glossaryAuthorContext.setRefreshSearchActionState();
      });
    return "Deleting";
  }

  return (
    <div>
      {glossaryAuthorContext.authoringActionState == 6 && !issuedDelete && issueDelete()}
    </div>
  );
}

export default NodeDelete;
