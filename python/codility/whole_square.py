def solution(A, B):
    import math
    if A == B:
        return 1 if math.ceil(math.sqrt(A))**2 == A else 0
    return int( abs( math.ceil(math.sqrt(B)) - math.floor(math.sqrt(A)) ) ) 
    
#codility  whole square
