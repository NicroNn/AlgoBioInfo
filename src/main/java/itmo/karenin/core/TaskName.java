package itmo.karenin.core;

public enum TaskName {
    COMPUTE_HAMMING_DISTANCE_BETWEEN_TWO_STRINGS("ComputeHammingDistanceBetweenTwoStrings"),
    COMPUTE_EDIT_DISTANCE_BETWEEN_TWO_STRINGS("ComputeEditDistanceBetweenTwoStrings"),
    GENERATE_KMER_COMPOSITION_STRING("GenerateKmerCompositionString"),
    FIND_MOST_FREQUENT_WORDS_STRING("FindMostFrequentWordsString"),
    FIND_CLUMPS_STRING("FindClumpsString"),
    FIND_HIGHEST_SCORING_FITTING_ALIGNMENT("FindHighestScoringFittingAlignment"),
    FIND_HIGHEST_SCORING_GLOBAL_ALIGNMENT_TWO_STRINGS("FindHighestScoringGlobalAlignmentTwoStrings"),
    FIND_HIGHEST_SCORING_LOCAL_ALIGNMENT_TWO_STRINGS("FindHighestScoringLocalAlignmentTwoStrings"),
    ALIGN_TWO_STRINGS_USING_AFFINE_GAP_PENALTIES("AlignTwoStringsUsingAffineGapPenalties"),
    CONSTRUCT_TRIE_COLLECTION_PATTERNS("ConstructTrieCollectionPatterns"),
    IMPLEMENT_TRIE_MATCHING("ImplementTrieMatching"),
    CONSTRUCT_BURROWS_WHEELER_TRANSFORM_STRING("ConstructBurrowsWheelerTransformString"),
    RECONSTRUCT_STRING_BURROWS_WHEELER_TRANSFORM("ReconstructStringBurrowsWheelerTransform"),
    PATTERN_MATCHING_BURROWS_WHEELER_TRANSFORM("PatternMatchingBurrowsWheelerTransform"),
    IMPLEMENT_MOTIF_ENUMERATION("ImplementMotifEnumeration"),
    FIND_MEDIAN_STRING("FindMedianString"),
    FIND_PROFILE_MOST_PROBABLE_KMER_STRING("FindProfileMostProbableKmerString"),
    CONSTRUCT_DE_BRUIJN_GRAPH_STRING("ConstructDeBruijnGraphString"),
    CONSTRUCT_DE_BRUIJN_GRAPH_COLLECTION_KMERS("ConstructDeBruijnGraphCollectionKmers"),
    FIND_EULERIAN_CYCLE_GRAPH("FindEulerianCycleGraph"),
    FIND_EULERIAN_PATH_GRAPH("FindEulerianPathGraph"),
    RECONSTRUCT_STRING_KMER_COMPOSITION("ReconstructStringKmerComposition"),
    CONSTRUCT_UPGMA_TREE("ConstructUPGMATree"),
    IMPLEMENT_NEIGHBOR_JOINING("ImplementNeighborJoining");

    private final String className;

    TaskName(String className) {
        this.className = className;
    }

    public String className() {
        return className;
    }

    public static TaskName parse(String value) {
        for (TaskName taskName : values()) {
            if (taskName.className.equals(value)) {
                return taskName;
            }
        }

        throw new IllegalArgumentException("Unknown task: " + value);
    }
}
