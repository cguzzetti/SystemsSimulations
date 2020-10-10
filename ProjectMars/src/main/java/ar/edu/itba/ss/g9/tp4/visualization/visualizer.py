import matplotlib.pyplot as plt
import sys
import numpy as np
from sklearn.metrics import mean_squared_error

from xyzParser import ParserXYZ


def visualize_error():
    methods = ["Gear", "Verlet", "Beeman"]
    errors = {key: [] for key in methods}
    deltas = ["0.0000001", "0.000001", "0.00001", "0.0001", "0.001"]
    fig, ax = plt.subplots()
    ax.set_xlabel('deltaT [s]')
    ax.set_ylabel('Error [m?]')
    for deltaT in deltas:
        parser: ParserXYZ = ParserXYZ.initialize_parser(f"mars_OVITO_{deltaT}.xyz")
        parser.parse_file()
        values = parser.calculate_position_error()
        analytical = values[3]
        print(f"For deltaT = {deltaT}")
        counter = 0
        for method in methods:
            sqr_err = mean_squared_error(analytical, values[counter])
            print(f"\t{method}: {sqr_err}")
            errors[method].append(sqr_err)
            counter += 1

    deltas = [float(x) for x in deltas]
    for method in methods:
        plt.scatter(deltas, errors[method], label=method)

    plt.legend(loc="lower right")
    ax.set_yscale('log')
    ax.set_xscale('log')
#        from matplotlib import ticker
#        formatter = ticker.ScalarFormatter(useMathText=True)
#        formatter.set_scientific(True)
#        ax.yaxis.set_major_formatter(formatter)
    plt.savefig("error.png")


def visualize_solution():
    fig, ax = plt.subplots()
    ax.set_xlabel('t [s]')
    ax.set_ylabel('Posición [m]')

    methods = ["Gear predictor", "Verlet original", "Beeman", "Analítico"]

    # plot_rows = 3
    # for j in range(1,plot_rows+1):

    # Read from file
    file = open("mars_SOLUTION.xyz", "r")
    lines = file.readlines()
    # plt.subplot(plot_rows, 1, j)
    for i in range(0, len(methods)):
        x = []
        y = []
        for line in lines[:]:
            line = line.split()
            print(line)
            x.append(float(line[0]))
            y.append(float(line[i+1]))

        plt.plot(x, y, label =methods[i])

    plt.legend(loc ="lower right")
    plt.savefig("solution.png")


if __name__ == '__main__':
    # Filename is always the first argument
    if len(sys.argv) == 1:
        print("Need to specify a visualization (SOLUTION|ERROR)")
        exit(-1)

    mode = sys.argv[1]
    if mode == "SOLUTION":
        visualize_solution()
    elif mode == "ERROR":
        visualize_error()
    else:
        print(f"{mode} is not a recognized mdoe :(")
