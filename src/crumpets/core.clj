(ns crumpets.core
  "Represent and manipulate color data."
  (:require [clojure.string :as str]))

(defprotocol ^:no-doc ColorConversions
  (hex [color] "Return the hex string of the color.")
  (int-argb [color] "Pack a color into a 32 bit ARGB int.")
  (awt-color [color] "Create a java.awt.Color object from the color."))

(defn- byte->hex [byte]
  (let [s (Integer/toString byte 16)]
    (if (< byte 16)
      (str "0" s)
      s)))

(defn- hex->byte [hex]
  (Integer/parseInt hex 16))

(deftype ColorRGB [red green blue]
  clojure.lang.Counted
  (count [_] 3)

  clojure.lang.Sequential

  clojure.lang.Seqable
  (seq [_] (list red green blue))

  clojure.lang.ILookup
  (valAt [color key]
    (.valAt color key nil))
  (valAt [_ color not-found]
    (case color :red red, :green green, :blue blue not-found))

  clojure.lang.IFn
  (invoke [q i]
    (.valAt q i))
  
  ColorConversions
  (hex [_]
    (str "#" (byte->hex red) (byte->hex green) (byte->hex blue)))
  (int-argb [_]
    (.intValue ^Long (bit-or (bit-shift-left 255 24)
                             (bit-shift-left red 16)
                             (bit-shift-left green 8)
                             blue)))
  (awt-color [_]
    (java.awt.Color. ^int red ^int green ^int blue))

  Object
  (toString [self]
    (str "#color/rgb \"" (hex self) "\""))
  (equals [self color]
    (or (identical? self color)
        (and (instance? ColorRGB color)
             (= red   (:red color))
             (= green (:green color))
             (= blue  (:blue color))))))

(alter-meta! #'->ColorRGB assoc :no-doc true)

(defn rgb
  "Create an RGB color from red, green and blue values. The values should be
  integers between 0 and 255."
  [r g b]
  (ColorRGB. r g b))

(defmethod print-method ColorRGB [^ColorRGB c ^java.io.Writer w]
  (.write w (.toString c)))

(defmethod print-dup ColorRGB [^ColorRGB c ^java.io.Writer w]
  (.write w (.toString c)))

(deftype ColorRGBA [red green blue alpha]
  clojure.lang.Counted
  (count [_] 4)

  clojure.lang.Sequential

  clojure.lang.Seqable
  (seq [_] (list red green blue alpha))

  clojure.lang.ILookup
  (valAt [color key]
    (.valAt color key nil))
  (valAt [_ color not-found]
    (case color :red red, :green green, :blue blue, :alpha alpha not-found))

  clojure.lang.IFn
  (invoke [q i]
    (.valAt q i))
  
  ColorConversions
  (hex [_]
    (str "#" (byte->hex red) (byte->hex green) (byte->hex blue) (byte->hex alpha)))
  (int-argb [_]
    (.intValue ^Long (bit-or (bit-shift-left alpha 24)
                             (bit-shift-left red 16)
                             (bit-shift-left green 8)
                             blue)))
  (awt-color [_]
    (java.awt.Color. ^int red ^int green ^int blue ^int alpha))

  Object
  (toString [self]
    (str "#color/rgba \"" (hex self) "\""))
  (equals [self color]
    (or (identical? self color)
        (and (instance? ColorRGBA color)
             (= red   (:red color))
             (= green (:green color))
             (= blue  (:blue color))
             (= alpha (:alpha color))))))

(alter-meta! #'->ColorRGBA assoc :no-doc true)

(defn rgba
  "Create an RGB color with red, green and blue values, plus an alpha channel.
  The values should be integers between 0 and 255."
  [r g b a]
  (ColorRGBA. r g b a))

(defmethod print-method ColorRGBA [^ColorRGBA c ^java.io.Writer w]
  (.write w (.toString c)))

(defmethod print-dup ColorRGBA [^ColorRGBA c ^java.io.Writer w]
  (.write w (.toString c)))

(defn- hex-bytes [hex-string]
  (->> (str/replace hex-string #"^#" "")
       (re-seq #"..")
       (map hex->byte)))

(defn- vec-bytes [v]
  (if (some float? v)
    (map #(int (* 255 %)) v)
    v))

(defprotocol ^:no-doc ToRGB
  (->rgb [x] "Turn a data structure into an RGB color."))

(extend-protocol ToRGB
  String
  (->rgb [s] (apply rgb (hex-bytes s)))
  clojure.lang.IPersistentVector
  (->rgb [v] (apply rgb (vec-bytes v))))

(defprotocol ^:no-doc ToRGBA
  (->rgba [x] "Turn a data structure into an RGB color with an alpha channel."))

(extend-protocol ToRGBA
  String
  (->rgba [s] (apply rgba (hex-bytes s)))
  clojure.lang.IPersistentVector
  (->rgba [v] (apply rgba (vec-bytes v)))
  ColorRGB
  (->rgba [c] (rgba (:red c) (:green c) (:blue c) 255)))
