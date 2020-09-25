import numpy as np
import matplotlib.pyplot as plt
from statistics import stdev
from statistics import mean

def plot_pressure():
    fig, ax = plt.subplots()
    ax.set_xlabel('Energía total del sistema [J]')
    ax.set_ylabel('Presión del sistema [N/m]')
    file = open("./gasExperiment.txt","r")
    lines = file.readlines()

    # Create data
    x = []
    y = []
    std = []

    rep = 0
    y_values = []
    for line in lines[1:]:
        if rep < 5:
            y_values.append(float(line.split()[1]))
            rep+=1
        if rep == 5:
            rep = 0
            x.append(float(line.split()[0]))
            y.append(mean(y_values))
            std.append(stdev(y_values))
            y_values.clear()

    plt.errorbar(x, y, yerr=std,fmt='o', ecolor='red')
    plt.savefig("gas_graph.png")
    #plt.show()

if __name__ == '__main__':
    plot_pressure()