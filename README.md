# Do It

A simple [React-Native](https://facebook.github.io/react-native/) IOS app written in [Clojurescript](https://github.com/clojure/clojurescript) using [re-natal](https://github.com/drapanjanas/re-natal) template.

Notable libraries used:
* [cljs-react-navigation](https://github.com/seantempesta/cljs-react-navigation)
* [re-frame](https://github.com/Day8/re-frame)
* [reagent](https://github.com/reagent-project/reagent)

## Usage

```
$ re-natal use-figwheel
$ lein figwheel ios
```
or nREPL
```
$ lein repl
user=> (start-ios-fig)
```
and finally
```
$ react-native run-ios
```

Please, refer to [re-natal documentation](https://github.com/drapanjanas/re-natal/blob/master/README.md) for more information.

## License

Copyright © 2018 Kirill Isakov

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
