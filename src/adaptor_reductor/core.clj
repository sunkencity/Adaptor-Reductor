(ns adaptor-reductor.core)

(use 'clojure.contrib.str-utils)

(defn raw-data
  "Loads a file and returns a nested sequence"
  [] (partition 4 (re-split #"\n" (slurp "trimtest.fastq"))))

(defn sequence-without-adapter
  "removes adapter from a sequence"
  [s] (map last
       (drop-while #(apply = %)
                   (mapcat #(list [%1 %2]) "CTGTAGGCACCATCAATCGTATGCCGTCTTCTGCTTG" s))))

(defn remove-adapter-dna
  "removes adapter from dna if adapter matches at least 6 letters"
  [s]
  (let [s-removed (sequence-without-adapter s)]
    (if (> 5 (- (.length s) (.length s-removed)))
      s-removed 
			s)))

(defn with-parsed-data
  "Appends parsed data to each item"
  [data]
  (list data (remove-adapter-dna (second data))))

(defn parse-adapter
  [] (map with-parsed-data (raw-data)))

(defn parse-adapter-parallel
  [] (pmap with-parsed-data (raw-data)))

