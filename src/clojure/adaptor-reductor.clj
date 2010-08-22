(ns adaptor-reductor)

(use 'clojure.contrib.str-utils)

(defn raw-data
  "Loads a file and returns a nested sequence"
  [file-path] (partition 4 (re-split #"\n" (slurp file-path))))

(defn sequence-without-adapter
	[sequence adapter min-length current-pos]
	(loop [s sequence
			   a adapter
			   pos current-pos]
			(if (= (first s) (first a))
				(recur (rest s) (rest a) (+ 1 current-pos))
		    (if (>= 5 current-pos)
			    s
			  sequence))))

(defn remove-adapter-dna
	[sequence]
	(str (sequence-without-adapter sequence "CTGTAGGCACCATCAATCGTATGCCGTCTTCTGCTTG" 6 0)))
	
(defn with-parsed-data
  "Appends parsed data to each item"
  [data]
  (list data (remove-adapter-dna (second data))))

(defn parse-adapter
  [file-path] (map with-parsed-data (raw-data file-path)))

(defn parse-adapter-parallel
  [file-path] (pmap with-parsed-data (raw-data file-path)))

(defn main-
	"datadir/trimtest.fastq"
	[file parallel]
	(if (= parallel "p")
	  (parse-adapter file)
	  (parse-adapter-parallel file)))
