INPUT = [14, 48, 64, 50, 58, 0, 230, 14, 67, 52, 50, 70, 39, 63, 73, 50, 44, 72, 47, 58, 66, 57, 235, 58, 53, 61, 244, 57, 58, 64, 60, 52, 66, 45, 235, 72, 46, 48, 244, 13, 44, 65, 40, 52, 72, 230,
         46, 60, 39, 55, 64, 43, 57, 59, 43, 249, 244, 22, 55, 57, 39, 62, 57, 230, 62, 57, 52, 47, 244, 63, 58, 73, 56, 235, 71, 53, 55, 73, 58, 52, 67, 52, 235, 53, 52, 47, 244, 9, 33, 244, 58, 58,
         244, 47, 46, 53, 52, 46, 67, 42, 48, 20, 45, 44, 65, 40, 52, 72, 56, 48, 71, 43, 44, 70, 41, 51, 2, 41, 58, 65, 230, 60, 73, 53, 63, 61, 52, 50, 244, 56, 48, 58, 43, 61, 57, 52, 46, 57, 0,
         235, 53, 255, 253, 13, 44, 49, 12, 39, 252, 54, 244]


def func(a=58, b=53, c=44):
    result = []
    for i in range(0, len(INPUT)):
        if i % 3 == 0:
            result.append((INPUT[i] + a) % 256)
        elif i % 3 == 1:
            result.append((INPUT[i] + b) % 256)
        elif i % 3 == 2:
            result.append((INPUT[i] + c) % 256)
    return result


if __name__ == '__main__':
    for i in range(len(INPUT) - 5):
        if INPUT[i] - INPUT[i + 3] == 5:
            if INPUT[i + 1] - INPUT[i + 4] == -8:
                if INPUT[i + 2] - INPUT[i + 5] == -7:
                    print INPUT[i], INPUT[i + 1], INPUT[i + 2]
                    print "".join(map(chr, func(103 - INPUT[i], 97 - INPUT[i + 1], 109 - INPUT[i + 2])))
