(ns adaptor-reductor.core (:gen-class))

(use 'clojure.contrib.str-utils)

(defn raw-data
  "Loads a file and returns a nested sequence"
  [file-path] (partition 4 (re-split #"\n" (slurp file-path))))

(defn sequence-without-adapter
	[sequence adapter min-length]
	(apply str
		(loop [s sequence
			     a adapter
			     match-size 0]
			     (if (= (first s) (first a))
				      (recur (rest s) (rest a) (+ 1 match-size))
		          (if (>= min-length match-size)
			          s
			          sequence)))))


(defn remove-adapter-dna
	[sequence]
  (sequence-without-adapter sequence "CTGTAGGCACCATCAATCGTATGCCGTCTTCTGCTTG" 6 ))
	
(defn with-parsed-data
  "Appends parsed data to each item"
  [data]
  (list data (remove-adapter-dna (second data))))

(defn parse-adapter
  [file-path] (map with-parsed-data (raw-data file-path)))

(defn parse-adapter-parallel
  [file-path] (pmap with-parsed-data (raw-data file-path)))

(defn -main
	"usage: datadir/trimtest.fastq -p"
	[file parallel]
	(apply println 
		(if (= parallel "-p")
	  	(parse-adapter file)
	  	(parse-adapter-parallel file))))
