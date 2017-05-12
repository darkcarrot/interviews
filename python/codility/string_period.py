def solution(N):
    nb = "{0:b}".format(N)
    result = -1
    for i in xrange( 1, len(nb)/2 ):
        test = [ nb[k] == nb[i+k] for k in xrange( len(nb) - i - 1 ) ]
        if all( test ):
            result = i
    return result
    
# codility string period
