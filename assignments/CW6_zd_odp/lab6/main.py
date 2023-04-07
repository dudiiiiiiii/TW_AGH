import multiprocessing as mp
from time import time


def opA(args):
    k, i, m, A = args
    m[k] = A[k][i] / A[i][i]
    return


def opB(args):
    k, j, i, m, n1, A = args
    n1[k][j] = m[k] * A[i][j]
    return


def opC(args):
    k, j, i, n, A = args
    A[k][j] = A[k][j] - n[k][j]
    return


def scheduler(x, A):
    manager1 = mp.Manager()
    manager2 = mp.Manager()
    m = manager1.list([0 for _ in range(x)])
    n1 = manager2.list([manager1.list([0 for _ in range(x + 1)]) for _ in range(x)])
    newA = manager2.list([manager1.list([float(y) for y in p]) for p in A])

    with mp.Pool(mp.cpu_count()) as pool:

        for i in range(x - 1):

            if newA[i][i] == float(0):
                valid = 0
                for k in range(i + 1, x):
                    if newA[k][i] != 0:
                        newA[k], newA[i] = newA[i], newA[k]
                        valid = 1
                        break
                if not valid:
                    raise Exception("bad matrix1")

            pool.map(opA, [(k, i, m, newA) for k in range(i + 1, x)])

            pool.map(opB, [(k, j, i, m, n1, newA) for j in range(i, x + 1) for k in range(i + 1, x)])

            pool.map(opC, [(k, j, i, n1, newA) for j in range(i, x + 1) for k in range(i + 1, x)])

    res = []
    for i in newA:
        tmp = []
        for x in i:
            tmp.append(x)
        res.append(tmp)

    return res


def in_file(filename):
    f = open(filename, 'r')
    text = f.read()
    lines = text.split('\n')
    A = []
    x = int(lines[0])
    for i in range(1, len(lines) - 1):
        line = lines[i].split(' ')
        tmp = []
        for j in line:
            tmp.append(float(j))
        A.append(tmp)
    f.close()

    free = []
    last = lines[len(lines) - 1].split(' ')
    for i in last:
        free.append(float(i))

    for i in range(x):
        A[i].append(free[i])

    return x, A


def back_substitution(A, x):
    if A[x - 1][x - 1] == float(0):
        raise Exception("bad matrix2")

    for i in range(x - 1, -1, -1):
        for j in range(i - 1, -1, -1):
            factor = A[j][i] / A[i][i]
            A[j][i] -= factor * A[i][i]
            A[j][x] -= factor * A[i][x]
        A[i][x] /= A[i][i]
        A[i][i] /= A[i][i]

    return A


def to_file(A, filename):
    f = open(filename, 'w')
    x = len(A)
    f.write(str(x) + '\n')

    for i in range(x):
        tmp = ''
        for j in range(x):
            tmp += str(A[i][j]) + ' '
        f.write(tmp[:-1] + '\n')

    tmp = ''
    for i in range(x):
        tmp += str(A[i][x]) + ' '
    f.write(tmp[:-1])

    f.close()


if __name__ == '__main__':
    x, A = in_file("test.txt")

    start = time()
    new_A = scheduler(x, A)
    end = time()

    print(end - start)
    res_A = back_substitution(new_A, x)

    to_file(res_A, "res.txt")
