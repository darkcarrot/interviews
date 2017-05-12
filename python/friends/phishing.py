f = lambda x: range(x + 1)
print f(5)

##
f = lambda x: reduce(lambda a, b: (a + b), x) / float(len(x))
print f([1, 2, 3])

##
f = lambda x: x.lstrip().rstrip()[:3]
print f('   hello    ')

##
f = lambda x: x.replace('-', '+')
print f('hello-world')

##
def f(*karg):
    # assume valid input
    return reduce(lambda x, y: x + y, karg[:-1]) - karg[-1]
print f(1, 2, 3)

##
f = lambda x: x[::-1]
print f([1, 2, 3])

##
f = lambda x: filter(lambda i: i % 2 == 0, x)
print f([1, 2, 3, 4])

##
f = lambda x: zip(x[0::2], x[1::2])
print f([1, 2, 3, 4, 5, 6])

##
f = lambda x: len([i for i in x if not i])
print f([7, 0, None, 1, 'hi', '', 88, 0])

##
import itertools

f = lambda x: list(itertools.chain.from_iterable(x))
print f([(1, 2), (3, 4), (5, 6)])
print f([[1, 2, 3], [4, 5], [6, 7]])
f = lambda x: [j for i in x for j in i]
print f([(1, 2), (3, 4), (5, 6)])
print f([[1, 2, 3], [4, 5], [6, 7]])

##
f = lambda x: any([i % 3 == 0 for i in x])
print f([1, 2, 4, 5])
print f([1, 2, 3, 5])

##
from collections import Counter
f = lambda x: Counter(x).most_common()[0][0]
print f([3, 3, 4, 4, 4, 4, 2])

def f(x):
    c = dict()
    for i in x:
        c[i] = c.get(i, 0) + 1
    sorted(c.iteritems(), key=lambda (k, v): (v, k))
    return c.keys()[-1]

print f([3, 3, 4, 4, 4, 4, 2])

##
f = lambda x: {v: k for k, v in x.items()}
print f({'a': 3, 'b': 4, 'c': 9})
