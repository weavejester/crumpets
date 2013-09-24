(ns crumpets.core-test
  (:require [clojure.test :refer :all]
            [crumpets.core :refer :all]))

(deftest reader-test
  (testing "hex string"
    (is (= (read-string "#color \"#ff9900aa\"")
           (->ColorRGBA 255 153 0 170)))
    (is (= (read-string "#color \"ff9900aa\"")
           (->ColorRGBA 255 153 0 170)))
    (is (= (read-string "#color \"#ff9900\"")
           (->ColorRGB 255 153 0)))
    (is (= (read-string "#color \"ff9900\"")
           (->ColorRGB 255 153 0))))
  (testing "int vector"
    (is (= (read-string "#color [255 153 0 170]")
           (->ColorRGBA 255 153 0 170)))
    (is (= (read-string "#color [255 153 0]")
           (->ColorRGB 255 153 0))))
  (testing "float vector"
    (is (= (read-string "#color [1.0 0.6 0.0 0.4]")
           (->ColorRGBA 255 153 0 102)))
    (is (= (read-string "#color [1.0 0.6 0.0]")
           (->ColorRGB 255 153 0)))))

(deftest print-test
  (is (= (pr-str (->ColorRGBA 255 153 0 170))
         "#color \"#ff9900aa\""))
  (is (= (pr-str (->ColorRGB 255 153 0))
         "#color \"#ff9900\"")))

(deftest color-test
  (is (= (color [255 153 0 170])
         (->ColorRGBA 255 153 0 170)))
  (is (= (color [1.0 0.0 0.6])
         (->ColorRGB 255 0 153))))

(deftest ->int-argb-test
  (is (= (->int-argb (color "#ff9900")) 0xffff9900))
  (is (= (->int-argb (color "#ff9900aa")) 0xaaff9900)))
