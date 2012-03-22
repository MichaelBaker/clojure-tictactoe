(ns clojure-tictactoe.core
  (:import java.io.File)
  (:use [clojure-webserver option-parser http-server request-handler])
  (:use [clojure-tictactoe.move-handler :only [move-handler]]))

(defn make-dispatcher [file-handler move-handler]
  (fn [env]
    (cond
      (.startsWith (:uri env) "/public")
        (file-handler env)
      (= (:uri env) "/")
        (file-handler (assoc env :uri "/public/index.html"))
      :otherwise
        (move-handler env))))

(defn -main [& args]
  (let [[directory port] (parse-options args)
        file-handler     (request-handler (File. directory))
        request-dispatch (make-dispatcher file-handler move-handler)]
    (start-server port request-dispatch (fn [server]
      (println "Server is listening on port " port)
      (println "Press ^C to stop the server")))))
