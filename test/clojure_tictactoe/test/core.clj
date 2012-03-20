(ns clojure-tictactoe.test.core
  (:import java.util.Arrays)
  (:use [clojure-tictactoe.core])
  (:use [clojure.test]))

(defn nils [amount]
  (take amount (repeat nil)))

(testing "string-to-board"
  (deftest empty-board
    (is (Arrays/equals
          (into-array String (nils 9))
          (string-to-board "/_________"))))

  (deftest one-X
    (is (Arrays/equals
          (into-array String (cons "X" (nils 8)))
          (string-to-board "/X________")))))
