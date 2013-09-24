(ns crumpets.core
  (:require [clojure.string :as str]))

(defn- to-hex [byte]
  (let [s (Integer/toString byte 16)]
    (if (< byte 16)
      (str "0" s)
      s)))

(defn- from-hex [hex]
  (Integer/parseInt hex 16))

(defrecord ColorRGB [red green blue]
  Object
  (toString [_]
    (str "#color \"#" (to-hex red) (to-hex green) (to-hex blue) "\"")))

(defrecord ColorRGBA [red green blue alpha]
  Object
  (toString [_]
    (str "#color \"#" (to-hex red) (to-hex green) (to-hex blue) (to-hex alpha) "\"")))

(defmethod print-method ColorRGB [^ColorRGB c ^java.io.Writer w]
  (.write w (.toString c)))

(defmethod print-dup ColorRGB [^ColorRGB c ^java.io.Writer w]
  (.write w (.toString c)))

(defmethod print-method ColorRGBA [^ColorRGBA c ^java.io.Writer w]
  (.write w (.toString c)))

(defmethod print-dup ColorRGBA [^ColorRGBA c ^java.io.Writer w]
  (.write w (.toString c)))

(defn hex->color [hex-string]
  (let [hex (str/replace hex-string #"^#" "")]
    (case (count hex)
      8 (let [[r g b a] (map from-hex (re-seq #".." hex))]
          (->ColorRGBA r g b a))
      6 (let [[r g b] (map from-hex (re-seq #".." hex))]
          (->ColorRGB r g b)))))

(defn- ints->color [[r g b a]]
  (if a
    (->ColorRGBA r g b a)
    (->ColorRGB r g b)))

(defn- floats->color [rgba]
  (ints->color (map #(int (* 255 %)) rgba)))

(defn vec->color [[r g b a :as rgba]]
  (cond
   (float? r) (floats->color rgba)
   (integer? r) (ints->color rgba)))

(defprotocol ToColor
  (->color [x]))

(extend-protocol ToColor
  String
  (->color [s] (hex->color s))
  clojure.lang.IPersistentVector
  (->color [v] (vec->color v)))
