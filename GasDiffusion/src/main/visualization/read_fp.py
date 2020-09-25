from statistics import stdev

import numpy as np
import matplotlib.pyplot as plt


def get_fp_from_file(gap):
    file = open(f"fpExperiment-{gap}.txt", "r")
    lines = file.readlines()
    t = []
    fp_values = []

    for line in lines[1:]:
        splitted_line = line.split()
        num = round(float(splitted_line[1]), 1)
        if (num not in t):
            t.append(num)
            fp_values.append(float(splitted_line[0]))

    return t, fp_values


if __name__ == '__main__':

    fig, ax = plt.subplots()
    for gap in [0.01, 0.02, 0.03]:
        x, y = get_fp_from_file(gap)
        plt.plot(x, y, label=f"Apertura del tabique: {gap}m")

    ax.set_xlabel("tiempo [s]")
    ax.set_ylabel("% de partículas del lado derecho")
    plt.legend(loc="lower right")
    plt.savefig("fp_graph.png")
