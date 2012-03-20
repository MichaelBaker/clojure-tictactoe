(ns clojure-tictactoe.core
  (:import MinMaxPlayer
           Move
           java.io.File)
  (:use [clojure-webserver
         option-parser
         http-server
         request-handler]))

(defn move-handler [env]
  (let [board (drop 1(map str (env :uri)))]
    (println board)
    {:status 200 :headers {} :body "hello"}))

(defn make-dispatcher [file-handler move-handler]
  (fn [env]
    (cond
      (.startsWith (:uri env) "/public") (file-handler env)
      (= (:uri env) "/") (file-handler (assoc env :uri "/public/index.html"))
      :otherwise (move-handler env))))

(defn -main [& args]
  (let [[directory port] (parse-options args)
        file-handler     (request-handler (File. directory))
        request-dispatch (make-dispatcher file-handler move-handler)]
    (start-server port request-dispatch)))
