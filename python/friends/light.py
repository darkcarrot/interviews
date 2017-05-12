from __future__ import division


def pointer():
    x = [1, 2, 3]
    y = tuple([x, 4, 5])
    # x = 0
    # x[0] = 0
    print y


def std_dev(*a):
    return (sum([(i - sum(a) / len(a)) ** 2 for i in a]) / len(a)) ** 0.5


def con_var(a, b):
    assert len(a) == len(b)
    return sum([(x - sum(a) / len(a)) * (y - sum(b) / len(b)) for x, y in zip(a, b)]) / len(a)


if __name__ == '__main__':
    pointer()
    print std_dev(1, 2, 3, 4, 5)
    print con_var([1, 2, 3, 4, 5], [100, 50, 75, 23, 99])
