/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
const express = require('express');
const fs = require("fs");
const session = require("express-session");
const bodyParser = require("body-parser");
const passport = require("passport");
const https = require("https");
const app = express();

// const getServerInfoFromEnv = require('./functions/getServerInfoFromEnv');
const serverNameMiddleWare = require('./functions/serverNameMiddleware');
const passportConfiguration = require('./functions/passportConfiguration');
const loggedIn = require('./functions/loggedIn');

const router = require('./router/routes');

const PORT = process.env.PORT || 8091;
const env = process.env.NODE_ENV || 'development';


// ssl self signed certificate and key
const cert = fs.readFileSync(__dirname + "/../../ssl/keys/server.cert");
const key = fs.readFileSync(__dirname + "/../../ssl/keys/server.key");
const options = {
  key: key,
  cert: cert,
};
app.set('key', key);
app.set('cert', cert);

// const servers = getServerInfoFromEnv();

// This middleware method takes off the first segment which is the serverName and puts it into a query parameter
app.use((req, res, next) => serverNameMiddleWare(req, res, next));

// Initialize Passport and restore authentication state, if any, from the
// session.
app.use(session({ secret: "cats", resave: false, saveUninitialized: false }));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(passport.initialize());
app.use(passport.session());

// passport configuration
passportConfiguration(passport);

// organize routes in another file
app.use('/', router);

// app.use("*", loggedIn, (req, res) => {
//   res.sendFile(joinedPath);
// });

// create the https server
https.createServer(options, app).listen(PORT, () => {
  console.log(`Server listening on port: ${PORT}`);
  if (env === 'development') console.log(`React UI listening on port: 3000`);
});