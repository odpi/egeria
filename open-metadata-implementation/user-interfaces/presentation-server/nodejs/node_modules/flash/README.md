
# flash

[![NPM version][npm-image]][npm-url]
[![Build status][travis-image]][travis-url]
[![Test coverage][coveralls-image]][coveralls-url]
[![Dependency Status][david-image]][david-url]
[![License][license-image]][license-url]
[![Downloads][downloads-image]][downloads-url]

The simplest flash implementation for Express.

## Usage

```bash
npm i flash
```

```js
app.use(session()); // session middleware
app.use(require('flash')());

app.use(function (req, res) {
  // flash a message
  req.flash('info', 'hello!');
  next();
})
```

```jade
for message in flash
  a.alert(class='alert-' + message.type)
    p= message.message
```

## API

### req.flash([type], msg)

Flash a message defaulting the `type` to `info`.

### res.locals.flash

An array of flash messages of the form:

```json
{
  "type": "info",
  "message": "message"
}
```

[npm-image]: https://img.shields.io/npm/v/flash.svg?style=flat-square
[npm-url]: https://npmjs.org/package/flash
[github-tag]: http://img.shields.io/github/tag/expressjs/flash.svg?style=flat-square
[github-url]: https://github.com/expressjs/flash/tags
[travis-image]: https://img.shields.io/travis/expressjs/flash.svg?style=flat-square
[travis-url]: https://travis-ci.org/expressjs/flash
[coveralls-image]: https://img.shields.io/coveralls/expressjs/flash.svg?style=flat-square
[coveralls-url]: https://coveralls.io/r/expressjs/flash?branch=master
[david-image]: http://img.shields.io/david/expressjs/flash.svg?style=flat-square
[david-url]: https://david-dm.org/expressjs/flash
[license-image]: http://img.shields.io/npm/l/flash.svg?style=flat-square
[license-url]: LICENSE
[downloads-image]: http://img.shields.io/npm/dm/flash.svg?style=flat-square
[downloads-url]: https://npmjs.org/package/flash
[gittip-image]: https://img.shields.io/gittip/jonathanong.svg?style=flat-square
[gittip-url]: https://www.gittip.com/jonathanong/
