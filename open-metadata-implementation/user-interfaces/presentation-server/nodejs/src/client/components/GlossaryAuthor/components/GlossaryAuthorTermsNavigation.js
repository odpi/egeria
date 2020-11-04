/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useEffect } from "react";

import Add32 from "../../../images/Egeria_add_32";
import Delete32 from "../../../images/Egeria_delete_32";
import Edit32 from "../../../images/Egeria_edit_32";
import Term32 from "../../../images/Egeria_term_32";
import ParentChild32 from "../../../images/Egeria_parent_child_32";
import { LocalNodeCard, NodeCardSection } from "./NodeCard/NodeCard";
import { withRouter } from "react-router-dom";
//import GlossaryImage from "../../../images/Egeria_glossary_32";
import getNodeType from "./properties/NodeTypes.js";
import { issueRestGet, issueRestDelete } from "./RestCaller";

import { Link } from "react-router-dom";

const GlossaryAuthorTermsNavigation = (props) => {
  const [nodes, setNodes] = useState([]);
  const nodeType = getNodeType("term");
  const [errorMsg, setErrorMsg] = useState();
  const [selectedNodeGuid, setSelectedNodeGuid] = useState();

  useEffect(() => {
    getChildren();
  }, [selectedNodeGuid]);
  const getChildren = () => {
    // encode the URI. Be aware the more recent RFC3986 for URLs makes use of square brackets which are reserved (for IPv6)
    const url = encodeURI(props.getTermsURL);
    issueRestGet(url, onSuccessfulGetChildren, onErrorGetChildren);
  };

  const onClickDelete = () => {
    setErrorMsg("");
    console.log("Delete");
    if (selectedNodeGuid) {
      nodes.forEach(deleteIfSelected);
    }
  };
  /**
   * Delete the supplied glossary if it's guid matches the selected one.
   * @param {*} glossary
   */
  const deleteIfSelected = (glossary) => {
    if (glossary.systemAttributes.guid == selectedNodeGuid) {
      const guid = selectedNodeGuid;

      // TODO change URL
      const url = nodeType.url + "/" + guid;
      issueRestDelete(url, onSuccessfulDelete, onErrorDelete);
    }
  };

  const onSuccessfulDelete = () => {
    setSelectedNodeGuid(undefined);
    // get the children again
    getChildren();
  };

  const onErrorDelete = (msg) => {
    console.log("Error on delete " + msg);
    setErrorMsg(msg);
    // setNodes([]);
  };

  const onSuccessfulGetChildren = (json) => {
    setErrorMsg("");
    console.log("onSuccessfulGetChildren " + json.result);
    setNodes(json.result);
  };

  const onErrorGetChildren = (msg) => {
    console.log("Error on get children " + msg);
    setErrorMsg(msg);
    setNodes([]);
  };

  function getAddNodeUrl() {
    return props.match.url  + "/terms/add-term";
  }
  function getEditNodeUrl() {
    return props.match.url  + "/terms/edit-term/" + selectedNodeGuid;
  }
  const isSelected = (nodeGuid) => {
    return nodeGuid == selectedNodeGuid;
  };
  const setSelected = (nodeGuid) => {
    setSelectedNodeGuid(nodeGuid);
  };
  return (
    <div>
      <div className="bx--grid">
        <NodeCardSection>
          <article className="node-card__controls bx--col-sm-4 bx--col-md-1 bx--col-lg-3 bx--col-xlg-3 bx--col-max-2">
            <div className="bx--row">
              <Link to={getAddNodeUrl}>
                <Add32 kind="primary" />
              </Link>
              {selectedNodeGuid && (
                <Link to={props.getTermsURL}>
                  <ParentChild32 kind="primary" />
                </Link>
              )}
              {selectedNodeGuid && (
                <Link to={getEditNodeUrl()}>
                  <Edit32 kind="primary" />
                </Link>
              )}
              {selectedNodeGuid && <Delete32 onClick={() => onClickDelete()} />}
            </div>
          </article>
        </NodeCardSection>

        <NodeCardSection className="landing-page__r3">
          <article style={{ color: "red" }}>{errorMsg}</article>
        </NodeCardSection>

        <NodeCardSection className="landing-page__r3">
          {nodes.map((node) => (
            <LocalNodeCard
              key={node.systemAttributes.guid}
              heading={node.name}
              guid={node.systemAttributes.guid}
              body={node.description}
              icon={<Term32 />}
              isSelected={isSelected(node.systemAttributes.guid)}
              setSelected={setSelected}
              // link={getNodeChildrenUrl(node.systemAttributes.guid)}
            />
          ))}
          {nodes.length == 0 && <div>No {nodeType.plural} found!</div>}
        </NodeCardSection>
      </div>
    </div>
  );
}
export default withRouter(GlossaryAuthorTermsNavigation);