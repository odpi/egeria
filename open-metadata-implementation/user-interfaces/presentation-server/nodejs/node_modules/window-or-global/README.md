window-or-global
================

Use this module to get the global object both on server and client side.
No more `window is not defined` errors just peace and flowers.

Advantages:

- You can easily mock it by [mockery](https://www.npmjs.com/package/mockery) or [proxyquire](https://www.npmjs.com/package/proxyquire) in your tests
- It's really useful in case of universal (or isomorphic) code, for example, when you'd like to render a [React](http://facebook.github.io/react/) component both on client and server side

## Install

```sh
$ npm i window-or-global
```

## Usage

```js
import root from 'window-or-global'
```

## React example

`components/my-component.jsx:`

```js
import React, { Component } from 'react'
// in node, you'll get the global object instead of crashing by an error
import root from 'window-or-global'

class MyComponent extends Component {

  // this method is only invoked in the browser environment
  componentDidMount() {
    root.addEventListener(/*...*/)
  }

  componentWillUnmount() {
    root.addEventListener(/*...*/)
  }

  render() {}

}

// Voilà. Enjoy your universal react component! ;)
// No more 'window is not defined' errors when you render your component
// on server side.

```

## Unit-testing example

You can find a complete test example [here](examples/react). The test passes of course, just type

```sh
$ npm test
```

in your terminal.

## License

[MIT](LICENSE) &copy; Purpose Industries
