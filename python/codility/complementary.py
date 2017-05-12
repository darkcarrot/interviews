def solution(K, A):
    temp = [ K - i for i in A]
    count = [ A.count(i) for i in temp ]
    return sum(count)
    
# Codility K Complementary
