# Crumpets

A Clojure library for representing an manipulating color.

## Installation

Add the following dependency to your `project.clj` file:

    [crumpets "0.1.0-SNAPSHOT"]

## Usage

Crumpets provides a reader literal for representing RGB color data:

```clojure
#color/rgb "#ff0000"
#color/rgb [255 0 0]
#color/rgb [1.0 0.0 0.0]
```

The color data can be representing as a hex string, a vector of
integers between 0 and 255, or a vector of floats between 0.0 and 1.0.

Alpha channels can be encoded with the `#color/rgba` reader literal:

```clojure
#color/rgba "#ff000099"
#color/rgba [255 0 0 153]
```

## License

Copyright © 2013 James Reeves

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
