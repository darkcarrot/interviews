import random


# Weighted Random Issue?
class RandomGen(object):
    # Values that may be returned by next_num()
    _random_nums = []
    # Probability of the occurence of random_nums
    _probabilities = []

    def __init__(self, nums, prob=None):
        """
        @nums: Values that may be returned by next_num()
        @prob: Probability of the occurence of random_nums
        """
        self.__validate_input(nums, prob)
        self._random_nums = nums
        self._probabilities = prob if prob else [1.0 / len(nums)] * len(nums)

    def next_num(self):
        """
        Returns one of the randomNums. When this method is called
        multiple times over a long period, it should return the
        numbers roughly with the initialized probabilities.
        """
        x = random.uniform(0, 1)
        cp = 0.0
        num = 0.0
        for num, probability in zip(self._random_nums, self._probabilities):
            cp += probability
            if x < cp:
                break
        return num

    def next_num_(self):
        from numpy.random import choice
        return choice(self._random_nums, p=self._probabilities)

    @staticmethod
    def __validate_input(nums, prob):
        """
        Validation of input numbers and probablities
        """
        if not (isinstance(nums, list) or isinstance(nums, tuple)):
            raise Exception('Values that may be returned by next_num() should be provided in a form of list or tuple')
        if prob:
            if not (isinstance(prob, list) or isinstance(prob, tuple)):
                raise Exception('Probability of the occurence of random_nums should be provided in a form of list or tuple')
            if not (len(nums) == len(prob)):
                raise Exception('Probability of the occurence of random_nums should have the same length as values that may be returned by next_num()')
            if not (0 <= min(prob) and max(prob) <= 1):
                raise Exception('Probability of the occurence of random_nums should be positive and less than/equal to 1')
            if not (abs(sum(prob) - 1.0) < 1.0e-6):  # To avoid the rounding issue
                raise Exception('Probability of the occurence of random_nums should have sum equal to 1')
