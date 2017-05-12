from itertools import product

import pandas as pd

KEYS = ["ABCDE", "FGHIJ", "KLMNO", " 123 "]
KEYS_DF = pd.DataFrame(list(i) for i in KEYS)
VOWEL = "AEIOU"
INVALID_KEY = " "


def find_valid_moves(df):
    """
    Work out all possible moves then all valid moves base on keypad structure
    Validation coding can be improved
    """
    row, column = df.shape
    all_knight_moves = list(product([-1, 1], [-2, 2])) + list(product([-2, 2], [-1, 1]))
    valid_moves_map = dict()
    for i in range(row):
        for j in range(column):
            current = df.loc[i][j]
            if current == INVALID_KEY:
                continue
            valid_move = [df.loc[i + x][j + y] for x, y in all_knight_moves
                          if 0 <= i + x < row and 0 <= j + y < column and df.loc[i + x][j + y] != INVALID_KEY
                          and ((df.loc[i + 1][j] != INVALID_KEY and df.loc[i + 2][j] != INVALID_KEY) if x == 2 else True)
                          and ((df.loc[i - 1][j] != INVALID_KEY and df.loc[i - 2][j] != INVALID_KEY) if x == -2 else True)
                          and ((df.loc[i][j + 1] != INVALID_KEY and df.loc[i][j + 2] != INVALID_KEY) if y == 2 else True)
                          and ((df.loc[i][j - 1] != INVALID_KEY and df.loc[i][j - 2] != INVALID_KEY) if y == -2 else True)]
            valid_moves_map[current] = valid_move

    return valid_moves_map


VALID_MOVES = find_valid_moves(KEYS_DF)


def knight_sequence(possible_key, steps=10, max_vowel=2):
    """
    A recursive way to solve the problem: simple but not the best
    Will think about improvements
    """
    result = 0

    for key in possible_key:
        if max_vowel or key not in VOWEL:
            if steps > 1:
                result += knight_sequence(VALID_MOVES[key], steps - 1, max_vowel - (key in VOWEL))
            elif steps > 0:
                result += 1

    return result


if __name__ == '__main__':
    valid_moves = find_valid_moves(KEYS_DF)
    all_keys = "".join(keys.strip() for keys in KEYS)
    # print knight_sequence(all_keys, 0) # 0
    # print knight_sequence(all_keys, 1) # 18
    # print knight_sequence("A", 1) # 1
    # print knight_sequence(all_keys, 2) # 56
    # print knight_sequence("A", 2) # 2
    # print knight_sequence("H", 2, 0) #3
    # print knight_sequence("AB", 2) # 5
    print knight_sequence(all_keys)
