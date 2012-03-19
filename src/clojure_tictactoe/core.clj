(ns clojure-tictactoe.core
  (:import MinMaxPlayer)
  (:import Move)
  (:use clojure-webserver.option-parser)
  (:use clojure-webserver.http-server))

(defn handler [env]
  (let [board (drop 1(map str (env :uri)))]
    (println board)
    {:status 200 :headers {} :body "hello"}))

(defn -main [& args]
  (let [[directory port] (parse-options args)]
    (start-server port handler)))
