import unittest

import random

from python.man_ahl.RandomGen import RandomGen


class RandomGenTests(unittest.TestCase):
    _random_nums = []
    _probabilities = []
    _TOLERANCE = 0.01

    def setUp(self):
        # Generate a list of a size between 5 and 15
        size = int(random.uniform(5, 15))
        # Generate a set of random numbers
        self._random_nums = list(set(int(random.uniform(0, 100)) for _ in range(size)))
        temp_probabilities = [random.uniform(0, 1) for _ in range(len(self._random_nums))]
        # Generate corresponding probabilities
        self._probabilities = [p / sum(temp_probabilities) for p in temp_probabilities]

    def test_next_num(self):
        gen = RandomGen(self._random_nums, self._probabilities)

        output = [gen.next_num() for _ in range(10000)]
        _ = [gen.next_num_() for _ in range(10)]

        # Work out the probabilities
        results = [(float(output.count(n)) / len(output)) for n in self._random_nums]
        # Compare
        self.assertTrue(all([abs(result - expected) < self._TOLERANCE for result, expected in zip(results, self._probabilities)]))


if __name__ == '__main__':
    unittest.main()
