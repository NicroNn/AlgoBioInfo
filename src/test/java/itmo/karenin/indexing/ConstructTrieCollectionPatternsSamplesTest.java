package itmo.karenin.indexing;

import itmo.karenin.TaskSamplesTestSupport;
import itmo.karenin.core.TaskName;
import org.junit.jupiter.api.Test;

class ConstructTrieCollectionPatternsSamplesTest {
    @Test
    void runsAllSamples() {
        TaskSamplesTestSupport.assertAllSamples(TaskName.CONSTRUCT_TRIE_COLLECTION_PATTERNS);
    }
}
