(defproject clojure-tictactoe "1.0.0-SNAPSHOT"
  :description  "FIXME: write description"
  :dependencies [[org.clojure/clojure       "1.3.0"]
                 [michael/clojure-webserver "1.0.1"]
                 [michael/min-max-player    "1.0.2"]
                 [michael/move              "1.0.0"]
                 [org.clojure/data.json     "0.1.2"]]
  :repositories {"local" ~(str (.toURI (java.io.File. "local_jars")))}
  :main         clojure-tictactoe.core)
