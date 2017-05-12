class MyBaseClass(object):
    """This is the base class"""

    def __init__(self):
        # Feel free to do something here
        pass

    def printType(self):
        """print the type"""
        print type(self).__name__


class MySubClassA(MyBaseClass):
    """This is sub class A"""

    def __init__(self):
        super(MySubClassA, self).__init__()
        # print 'this is my sub class A'


class MySubClassB(MyBaseClass):
    """This is sub class B"""
    # can do without a super if there is no override
    # def __init__(self):
    #    super(MySubClassB, self).__init__() 


def test():
    base = MyBaseClass();
    a = MySubClassA();
    b = MySubClassB();

    base.printType()
    a.printType()
    b.printType()


if __name__ == "__main__":
    test()
