# Crumpets

A Clojure library for representing and manipulating color. It's
currently usable, but has limited functionality.

## Installation

Add the following dependency to your `project.clj` file:

    [crumpets "0.1.0"]

## Usage

Crumpets provides types for holding color data:

```clojure
(require '[crumpets.core :as color])

(def color-red
  (color/rgb 255 0 0))
```

Colors can also be defined through the `#color/rgb` reader literal,
which supports a variety of formats:

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

Colors implement the `ILookup` interface, so you can pull out their
values by accessing the `:red`, `:green`, `:blue` and `:alpha` keys:

```clojure
(:red color-red)       ;; => 255
(get color-red :blue)  ;; => 0
```

There's also functions for converting color data to different formats,
such as the [integer ARGB format][1] that can be used in BufferedImage
objects:

[1]: http://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferedImage.html#TYPE_INT_ARGB

```clojure
(int-argb color-red)  ;; => 0xffff0000
```

## Documentation

* [API Docs](http://weavejester.github.io/crumpets/)

## License

Copyright © 2013 James Reeves

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
