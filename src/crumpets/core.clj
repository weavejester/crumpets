(ns crumpets.core)

(defn- to-hex [byte]
  (let [s (Integer/toString byte 16)]
    (if (< byte 16)
      (str "0" s)
      s)))

(defn- from-hex [hex]
  (Integer/parseInt hex 16))

(defrecord Color [red green blue alpha]
  Object
  (toString [_]
    (str "#color \"" (to-hex red) (to-hex green) (to-hex blue) (to-hex alpha) "\"")))

(defmethod print-method Color [^Color c ^java.io.Writer w]
  (.write w (.toString c)))

(defmethod print-dup Color [^Color c ^java.io.Writer w]
  (.write w (.toString c)))

(defn hex->color [hex-string]
  (case (count hex-string)
    8 (let [[r g b a] (map from-hex (re-seq #".." hex-string))]
        (->Color r g b a))
    6 (let [[r g b] (map from-hex (re-seq #".." hex-string))]
        (->Color r g b 255))))
