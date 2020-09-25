import numpy as np
import matplotlib.pyplot as plt


def do_stuff():
    fig, ax = plt.subplots()
    ax.set_xlabel('Apertura de la ranura [m]')
    ax.set_ylabel('Tiempo hasta llegar al equilibrio [s]')
    x = [0.01, 0.02, 0.03]
    y = [695.2501818, 342.3121416, 235.66612999999998]
    err = [244.36173942729218, 78.58737716318488, 76.98263594874633]

    plt.errorbar(x, y, yerr=err, fmt="-o", ecolor="red")
    plt.savefig("timeVsGapN300.png")


if __name__ == '__main__':
    do_stuff()