# AlgoBioInfo Solutions Guide

## Структура проекта

- `src/main/java/itmo/karenin/...` — решения задач по разделам.
- `src/main/resources/datasets/...` — sample и actual датасеты.
- `src/test/java/itmo/karenin/...` — тесты, разложенные по тем же пакетам, что и основные решения.
- `src/main/java/itmo/karenin/core` — общий рантайм:
  - `AbstractBioinformaticsTask` — базовый класс задачи.
  - `TaskRegistry` — создание задачи по имени.
  - `TaskName` — enum всех задач.
  - `RunMode` — `SAMPLE` / `ACTUAL`.

## Полезные термины

- `k-mer` — подстрока длины `k`.
- `Hamming distance` — число позиций, в которых две строки одинаковой длины отличаются.
- `Edit distance / Levenshtein distance` — минимальное число вставок, удалений и замен для превращения одной строки в другую.
- `Profile matrix` — матрица вероятностей символов по позициям мотива.
- `Trie` — префиксное дерево.
- `BWT` — Burrows-Wheeler transform.
- `De Bruijn graph` — граф, где вершины — `(k-1)`-меры, а ребра соответствуют `k`-мерам.
- `Eulerian path/cycle` — путь/цикл, проходящий по каждому ребру ровно один раз.
- `UPGMA` — алгоритм агломеративной кластеризации для построения ультраметрического дерева.
- `Neighbor Joining` — алгоритм восстановления филогенетического дерева без предположения ультраметричности.

## Общие идеи по проверке

Для части задач правильный ответ не единственный. Поэтому в проекте используется `normalizeForComparison(...)`:

- где порядок не важен, ответы сравниваются как отсортированные множества;
- где допустимы разные оптимальные выравнивания, сравнение идёт по score;
- где sample-датасеты локально не совпадают с форматом Rosalind, comparison умеет нормализовать оба формата.

---

## Algorithms / Strings

### Compute Hamming Distance Between Two Strings

- Файл: `algorithms/strings/ComputeHammingDistanceBetweenTwoStrings.java`
- Идея:
  - пройти по строкам посимвольно;
  - увеличить счётчик, если символы отличаются.
- Асимптотика:
  - время: `O(n)`
  - память: `O(1)`

### Compute Edit Distance Between Two Strings

- Файл: `algorithms/strings/ComputeEditDistanceBetweenTwoStrings.java`
- Идея:
  - классический DP для Levenshtein distance;
  - `dp[i][j]` — минимальная стоимость перехода между префиксами;
  - используются только две строки DP, чтобы уменьшить память.
- Асимптотика:
  - время: `O(nm)`
  - память: `O(min(n, m))`

---

## NGS / Motifs

### Generate the k-mer Composition of a String

- Файл: `ngs/GenerateKmerCompositionString.java`
- Идея:
  - сгенерировать все подстроки длины `k`;
  - отсортировать лексикографически.
- Асимптотика:
  - генерация: `O(n)`
  - итог: `O((n-k+1) log(n-k+1))` без учёта стоимости сравнения строк

### Find the Most Frequent Words in a String

- Файл: `ngs/FindMostFrequentWordsString.java`
- Идея:
  - пройти по всем `k`-мерам;
  - посчитать частоты в `HashMap`;
  - выбрать все `k`-меры с максимальной частотой.
- Асимптотика:
  - время: `O(nk)` в прямой реализации через `substring`
  - память: `O(U)`, где `U` — число различных `k`-меров

### Find Patterns Forming Clumps in a String

- Файл: `ngs/FindClumpsString.java`
- Идея:
  - использовать скользящее окно длины `L`;
  - поддерживать частоты `k`-меров внутри текущего окна;
  - при каждом сдвиге удалить outgoing `k`-мер и добавить incoming `k`-мер.
- Асимптотика:
  - время: примерно `O(nk)`
  - память: `O(U)`

---

## SNP / Motif Search

### Implement MotifEnumeration

- Файл: `snp/ImplementMotifEnumeration.java`
- Идея:
  - перебрать все `k`-меры из всех строк;
  - для каждого сгенерировать `neighbors` на расстоянии не больше `d`;
  - проверить, что кандидат встречается в каждой строке с не более чем `d` mismatches.
- Асимптотика:
  - экспоненциальна по `k` и `d`, что нормально для учебной постановки;
  - грубо: `O(t * (n-k+1) * |neighbors| * t * (n-k+1) * k)`

### Find a Median String

- Файл: `snp/FindMedianString.java`
- Идея:
  - перебрать все `4^k` возможные `k`-меры;
  - для каждого посчитать сумму минимальных Hamming distance до каждой строки из `Dna`;
  - выбрать строку с минимальным значением.
- Асимптотика:
  - время: `O(4^k * t * (L-k+1) * k)`
  - память: `O(4^k)` на список всех кандидатов в текущей реализации

### Find a Profile-most Probable k-mer in a String

- Файл: `snp/FindProfileMostProbableKmerString.java`
- Идея:
  - для каждого `k`-мера из `Text` вычислить вероятность по profile matrix;
  - выбрать максимальный;
  - при равенстве оставить первый встретившийся.
- Асимптотика:
  - время: `O((n-k+1) * k)`
  - память: `O(1)` кроме входного профиля

---

## Alignment

### Find a Highest-Scoring Fitting Alignment

- Файл: `alignment/FindHighestScoringFittingAlignment.java`
- Скоринг:
  - match `+1`
  - mismatch `-1`
  - gap `-1`
- Идея:
  - fitting alignment заставляет вторую строку выровняться полностью, а в первой разрешает выбрать лучший подотрезок;
  - DP похож на global alignment, но первая колонка инициализируется нулями.
- Асимптотика:
  - время: `O(nm)`
  - память: `O(nm)` из-за backtracking

### Find Highest-Scoring Global Alignment of Two Strings

- Файл: `alignment/FindHighestScoringGlobalAlignmentTwoStrings.java`
- Скоринг:
  - BLOSUM62
  - gap penalty `5`
- Идея:
  - Needleman-Wunsch;
  - полная таблица DP плюс восстановление одного оптимального выравнивания.
- Асимптотика:
  - время: `O(nm)`
  - память: `O(nm)`

### Find Highest-Scoring Local Alignment of Two Strings

- Файл: `alignment/FindHighestScoringLocalAlignmentTwoStrings.java`
- Скоринг:
  - PAM250
  - gap penalty `5`
- Идея:
  - Smith-Waterman;
  - в каждой клетке разрешён переход в `0`;
  - восстановление начинается из глобального максимума и идёт до первой `0`.
- Асимптотика:
  - время: `O(nm)`
  - память: `O(nm)`

### Align Two Strings Using Affine Gap Penalties

- Файл: `alignment/AlignTwoStringsUsingAffineGapPenalties.java`
- Скоринг:
  - BLOSUM62
  - gap opening `11`
  - gap extension `1`
- Идея:
  - три DP-матрицы:
    - `lower` — сейчас идём внутри гэпа во второй строке;
    - `upper` — сейчас идём внутри гэпа в первой строке;
    - `middle` — матч/замена;
  - это стандартная affine-gap dynamic programming.
- Асимптотика:
  - время: `O(nm)`
  - память: `O(nm)`

### Scoring Matrices

- `alignment/Blosum62.java` — матрица BLOSUM62.
- `alignment/Pam250.java` — матрица PAM250.
- Это отдельные helper-классы, чтобы не дублировать таблицы в решениях.

---

## Indexing

### Construct a Trie from a Collection of Patterns

- Файл: `indexing/ConstructTrieCollectionPatterns.java`
- Идея:
  - строить trie по паттернам символ за символом;
  - если ребро уже есть — переходить по нему;
  - если нет — создавать новую вершину и ребро.
- Асимптотика:
  - время: `O(totalLengthOfPatterns * outDegreeSearch)`
  - память: `O(numberOfTrieNodes)`
- Примечание:
  - вывод на Rosalind: `u->v:c`

### Implement TrieMatching

- Файл: `indexing/ImplementTrieMatching.java`
- Идея:
  - построить trie по паттернам;
  - для каждой стартовой позиции в `Text` идти вниз по trie;
  - как только попадаем в terminal-узел, текущая позиция подходит.
- Асимптотика:
  - время: в худшем случае `O(|Text| * maxPatternLength)`
  - память: `O(sizeOfTrie)`

### Construct the Burrows-Wheeler Transform of a String

- Файл: `indexing/ConstructBurrowsWheelerTransformString.java`
- Идея:
  - сгенерировать все циклические сдвиги;
  - отсортировать;
  - взять последний столбец.
- Асимптотика:
  - время: наивно `O(n^2 log n)`
  - память: `O(n^2)`

### Reconstruct a String from its Burrows-Wheeler Transform

- Файл: `indexing/ReconstructStringBurrowsWheelerTransform.java`
- Идея:
  - last column дана;
  - first column — это та же строка, но отсортированная;
  - одинаковые символы нумеруются по вхождениям;
  - строится last-to-first mapping, по которому восстанавливается исходный текст.
- Асимптотика:
  - время: `O(n log n)` из-за сортировки
  - память: `O(n)`

### Multiple Pattern Matching with the Suffix Array

- Файл: `indexing/PatternMatchingBurrowsWheelerTransform.java`
- Идея:
  - несмотря на имя класса, в ресурсах и решении это suffix-array matching;
  - строится suffix array;
  - для каждого паттерна бинарными поисками находится диапазон суффиксов, начинающихся с этого паттерна.
- Асимптотика:
  - построение suffix array в текущей реализации: `O(n^2 log n)` из-за `substring`;
  - поиск одного паттерна: `O(log n * |pattern|)` плюс сбор ответов

---

## Genome Assembly / Graphs

### Construct the De Bruijn Graph of a String

- Файл: `genome/ConstructDeBruijnGraphString.java`
- Идея:
  - каждый `k`-мер строки даёт ребро:
    - `prefix = kmer[0..k-2]`
    - `suffix = kmer[1..k-1]`
  - одинаковые prefix группируются в adjacency list.
- Асимптотика:
  - время: `O((n-k+1) * k)`
  - память: `O(numberOfEdges)`

### Construct the De Bruijn Graph of a Collection of k-mers

- Файл: `genome/ConstructDeBruijnGraphCollectionKmers.java`
- Идея:
  - то же самое, только по списку `k`-меров;
  - повторы сохраняются как кратные ребра.
- Асимптотика:
  - время: `O(mk)`, где `m` — число k-меров
  - память: `O(m)`

### Find an Eulerian Cycle in a Graph

- Файл: `genome/FindEulerianCycleGraph.java`
- Идея:
  - реализован алгоритм Hierholzer;
  - пока у текущей вершины есть неиспользованные ребра, идём по одному из них;
  - когда ребер больше нет, откатываемся и добавляем вершину в ответ.
- Асимптотика:
  - время: `O(E)`
  - память: `O(E + V)`

### Find an Eulerian Path in a Graph

- Файл: `genome/FindEulerianPathGraph.java`
- Идея:
  - стартовая вершина определяется как вершина с `outDegree = inDegree + 1`;
  - после этого используется тот же Hierholzer.
- Асимптотика:
  - время: `O(E)`
  - память: `O(E + V)`

### Reconstruct a String from its k-mer Composition

- Файл: `genome/ReconstructStringKmerComposition.java`
- Идея:
  - построить de Bruijn graph по `k`-мерам;
  - найти Eulerian path;
  - восстановить строку по пути:
    - первая вершина целиком;
    - далее дописывать последний символ каждой следующей вершины.
- Асимптотика:
  - построение графа: `O(mk)`
  - поиск пути: `O(E)`
  - восстановление строки: `O(m)`

---

## Phylogenetics

### Implement UPGMA

- Файл: `phylogenetics/ConstructUPGMATree.java`
- Идея:
  - агломеративно объединять два ближайших кластера;
  - возраст нового кластера равен `distance / 2`;
  - расстояния до нового кластера считаются как взвешенное среднее по размерам кластеров.
- Асимптотика:
  - в текущей прямой реализации примерно `O(n^3)`
  - память: `O(n^2)`
- Теоретическое замечание:
  - UPGMA предполагает ультраметричность, то есть одинаковую скорость эволюции во всех ветвях.

### Implement Neighbor Joining

- Файл: `phylogenetics/ImplementNeighborJoining.java`
- Идея:
  - рекурсивно выбирать пару таксонов с минимальным значением в матрице neighbor-joining;
  - добавлять новый внутренний узел;
  - сжимать матрицу расстояний и повторять процесс.
- Асимптотика:
  - в прямой реализации примерно `O(n^3)`
  - память: `O(n^2)`
- Теоретическое замечание:
  - в отличие от UPGMA, neighbor joining не требует ультраметричности.
