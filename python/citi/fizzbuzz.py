print '\n'.join(['Fizz'*(x % 3 == 0) + 'Buzz'*(x % 5 == 0) or str( x ) for x in xrange( 1,101 )])

"""
Write a program that prints the numbers from 1 to 100. 
But for multiples of three print “Fizz” instead of the number and for the multiples of five print “Buzz”. 
For numbers which are multiples of both three and five print “FizzBuzz”.
"""
