/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from 'react';

function relogin() {
  console.log("Relogin  called"); 
  const serverName = window.location.pathname.split('/')[1];
  fetch('/' + serverName + '/login');
}
function LoggedOut() {
  return (
    <div>
       <h1>User not logged in</h1>
       <button onClick={relogin}>Login</button>
    </div>
  );
}

export default LoggedOut;
