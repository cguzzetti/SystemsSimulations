import numpy as np
import matplotlib.pyplot as plt


def do_stuff():
    x = [0.01, 0.02, 0.03]
    plt.subplot()
    y = [173084.0, 80152.8, 62401.0]
    err = [30654.86, 16263.1930, 4601.141032396204]

    plt.errorbar(x, y, yerr=err, fmt="-o", ecolor="red")
    plt.savefig("timeVsGapN300.png")


if __name__=='__main__':
    do_stuff()