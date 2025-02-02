import numpy as np
import matplotlib.pyplot as plt
import math
from matplotlib.colors import ListedColormap
import matplotlib.collections
import sys

# Time calculated in Microseconds
def visualize_R():
    file = open("../inputM.txt", "r")
    maxTime = 0
    times = []
    errors = []
    for line in file.readlines():
        avg_time, err = line.split()
        avg_time = math.ceil(float(avg_time) * 1000)
        err = math.ceil(float(err) * 1000)

        if(avg_time > maxTime):
            maxTime = avg_time
        times.append(avg_time)
        errors.append(err)

    fig, ax = plt.subplots()
    ax.set_xlim(0, len(times) + 1)
    ax.set_ylim(0, maxTime + 100)
    ax.set_aspect('auto')

    plt.errorbar(range(1, len(times)+1), times, yerr=err, fmt='b', ecolor='red')
    # plt.plot(range(1, len(times)+1), [882]*(len(times)))
    ax.set_xlabel('Valores de M')
    ax.set_ylabel('Tiempo [μs]')
    plt.title("Tiempos de ejecución con N = 100; L = 20; rc = 1")
    plt.show()

visualize_R()