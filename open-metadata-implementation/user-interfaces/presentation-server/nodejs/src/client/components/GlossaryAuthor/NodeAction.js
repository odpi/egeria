/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useContext } from "react";
import Add16 from "../../images/Egeria_add_16";
import Search16 from "../../images/Egeria_search_16";
import Delete16 from "../../images/Egeria_delete_16";
import NodeActionContent from "./NodeActionContent";
import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";

const NodeAction = (props) => {
  console.log("NodeAction");
  const glossaryAuthorContext = useContext(GlossaryAuthorContext);
  const [errorMsg, setErrorMsg] = useState();
  const onClickAdd = () => {
    setErrorMsg("");
    glossaryAuthorContext.setCreatingActionState();
  };
  const onClickSearch = () => {
    setErrorMsg("");
    glossaryAuthorContext.setSearchingActionState();
  };
  const onClickDelete = () => {
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
        console.log("delete completed " + JSON.stringify(res));
        if (res.relatedHTTPCode == 200 && res.result) {
          console.log("Delete successful for guid " + guid);
          glossaryAuthorContext.removeSelectedNode();
          glossaryAuthorContext.setRefreshSearchingActionState();
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
            msg = "Delete Failed - unexpected response" + JSON.stringify(res);
          }
          setErrorMsg(errorMsg + ",\n" + msg);
          //document.getElementById("nodeCreateButton").classList.add("shaker");
        }
        // re issue the search to refresh the results table to account for any deletes.
        // issueSearch(debouncedSearchCriteria).then((results) => {
        //   // Set back to false since request finished
        //   setIsSearching(false);
        //   // Set results state
        //   setResults(results);
        // });
      })
      .catch((res) => {
        msg = "Delete Failed - logic error " + JSON.stringify(res);
        setErrorMsg(errorMsg + ",\n" + msg);
      });
  };
  console.log("NodeAction " + glossaryAuthorContext.myState);
  if (glossaryAuthorContext.myState == 0) {
    return null;
  } else {
    return (
      <div>
        <div class="bx--row">
          <div class="bx--col-lg-1 bx--col-md-1">
            <Add16 kind="primary" onClick={() => onClickAdd()} />
          </div>
          <div class="bx--col-lg-1 bx--col-md-1">
            <Search16 onClick={() => onClickSearch()} />
          </div>
          {glossaryAuthorContext.selectedNode && (
            <div class="bx--col-lg-1 bx--col-md-1">
              <Delete16 onClick={() => onClickDelete()} />
            </div>
          )}
          <div style={{ color: "red" }}>{errorMsg}</div>
        </div>

        <div class="bx--row">
          <NodeActionContent></NodeActionContent>
        </div>
      </div>
    );
  }
};

export default NodeAction;
