(ns crumpets.core-test
  (:require [clojure.test :refer :all]
            [crumpets.core :refer :all]))

(deftest reader-test
  (testing "hex string"
    (is (= (read-string "#color/rgba \"#ff9900aa\"")
           (->ColorRGBA 255 153 0 170)))
    (is (= (read-string "#color/rgba \"ff9900aa\"")
           (->ColorRGBA 255 153 0 170)))
    (is (= (read-string "#color/rgb \"#ff9900\"")
           (->ColorRGB 255 153 0)))
    (is (= (read-string "#color/rgb \"ff9900\"")
           (->ColorRGB 255 153 0))))
  (testing "int vector"
    (is (= (read-string "#color/rgba [255 153 0 170]")
           (->ColorRGBA 255 153 0 170)))
    (is (= (read-string "#color/rgb [255 153 0]")
           (->ColorRGB 255 153 0))))
  (testing "float vector"
    (is (= (read-string "#color/rgba [1.0 0.6 0.0 0.4]")
           (->ColorRGBA 255 153 0 102)))
    (is (= (read-string "#color/rgb [1.0 0.6 0.0]")
           (->ColorRGB 255 153 0)))))

(deftest print-test
  (is (= (pr-str (rgba 255 153 0 170))
         "#color/rgba \"#ff9900aa\""))
  (is (= (pr-str (rgb 255 153 0))
         "#color/rgb \"#ff9900\"")))

(deftest ->rgb-test
  (is (= (->rgba [255 153 0 170])
         (rgba 255 153 0 170)))
  (is (= (->rgb [1.0 0.0 0.6])
         (rgb 255 0 153)))
  (is (= (->rgb "#ff0099")
         (rgb 255 0 153))))

(deftest int-argb-test
  (is (instance? Integer (int-argb (->rgb "#ffffff"))))
  (is (= (int-argb (->rgb "#ff9900"))    (.intValue 0xffff9900)))
  (is (= (int-argb (->rgba "#ff9900aa")) (.intValue 0xaaff9900))))

(deftest awt-color-test
  (testing "RGB"
    (let [c (awt-color (->rgb [255 153 0]))]
      (is (instance? java.awt.Color c))
      (is (= (.getRed c) 255))
      (is (= (.getGreen c) 153))
      (is (= (.getBlue c) 0))))
  (testing "RGBA"
    (let [c (awt-color (->rgba [255 153 0 102]))]
      (is (instance? java.awt.Color c))
      (is (= (.getRed c) 255))
      (is (= (.getGreen c) 153))
      (is (= (.getBlue c) 0))
      (is (= (.getAlpha c) 102)))))

(deftest hex-test
  (is (= (hex (rgb 255 153 0)) "#ff9900"))
  (is (= (hex (rgba 255 153 0 170)) "#ff9900aa")))

(deftest lookup-test
  (testing "RGB"
    (let [c (rgb 200 150 100)]
      (is (= (:red c) 200))
      (is (= (:green c) 150))
      (is (= (:blue c) 100))))
  (testing "RGBA"
    (let [c (rgba 200 150 100 50)]
      (is (= (:red c) 200))
      (is (= (:green c) 150))
      (is (= (:blue c) 100))
      (is (= (:alpha c) 50)))))
