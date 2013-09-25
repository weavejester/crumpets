(ns crumpets.core
  (:require [clojure.string :as str]))

(defprotocol ColorConversions
  "Functions for converting colors to different formats."
  (->int-argb [color] "Pack a color as a 32 bit ARGB int."))

(defn- to-hex [byte]
  (let [s (Integer/toString byte 16)]
    (if (< byte 16)
      (str "0" s)
      s)))

(defn- from-hex [hex]
  (Integer/parseInt hex 16))

(defrecord ColorRGB [red green blue]
  ColorConversions
  (->int-argb [_]
    (bit-or (bit-shift-left 255 24)
            (bit-shift-left red 16)
            (bit-shift-left green 8)
            blue))
  Object
  (toString [_]
    (str "#color/rgb \"#" (to-hex red) (to-hex green) (to-hex blue) "\"")))

(defn rgb
  "Create an RGB color from red, green and blue values."
  [r g b]
  (ColorRGB. r g b))

(defmethod print-method ColorRGB [^ColorRGB c ^java.io.Writer w]
  (.write w (.toString c)))

(defmethod print-dup ColorRGB [^ColorRGB c ^java.io.Writer w]
  (.write w (.toString c)))

(defrecord ColorRGBA [red green blue alpha]
  ColorConversions
  (->int-argb [_]
    (bit-or (bit-shift-left alpha 24)
            (bit-shift-left red 16)
            (bit-shift-left green 8)
            blue))
  Object
  (toString [_]
    (str "#color/rgba \"#" (to-hex red) (to-hex green) (to-hex blue) (to-hex alpha) "\"")))

(defn rgba
  "Create an RGB color with red, green and blue values, plus an alpha channel."
  [r g b a]
  (ColorRGBA. r g b a))

(defmethod print-method ColorRGBA [^ColorRGBA c ^java.io.Writer w]
  (.write w (.toString c)))

(defmethod print-dup ColorRGBA [^ColorRGBA c ^java.io.Writer w]
  (.write w (.toString c)))

(defn- hex-bytes [hex-string]
  (->> (str/replace hex-string #"^#" "")
       (re-seq #"..")
       (map from-hex)))

(defn- vec-bytes [v]
  (if (some float? v)
    (map #(int (* 255 %)) v)
    v))

(defprotocol ToRGB
  (->rgb [x] "Turn a data structure into an RGB color."))

(extend-protocol ToRGB
  String
  (->rgb [s] (apply rgb (hex-bytes s)))
  clojure.lang.IPersistentVector
  (->rgb [v] (apply rgb (vec-bytes v))))

(defprotocol ToRGBA
  (->rgba [x] "Turn a data structure into an RGB color with an alpha channel."))

(extend-protocol ToRGBA
  String
  (->rgba [s] (apply rgba (hex-bytes s)))
  clojure.lang.IPersistentVector
  (->rgba [v] (apply rgba (vec-bytes v)))
  ColorRGB
  (->rgba [c] (rgba (:red c) (:green c) (:blue c) 255)))
