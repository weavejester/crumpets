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

(deftest ->int-argb-test
  (is (= (->int-argb (->rgb "#ff9900"))   0xffff9900))
  (is (= (->int-argb (->rgba "#ff9900aa")) 0xaaff9900)))
