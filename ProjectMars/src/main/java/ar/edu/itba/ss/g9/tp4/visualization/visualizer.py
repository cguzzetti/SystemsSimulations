import matplotlib.pyplot as plt
import sys
import numpy as np

def visualize_error():
    methods = [ "Gear", "Verlet", "Beeman"]
    methods_dict = {key: [] for key in methods}
    methods_delta = {key: [] for key in methods}
    x = []
    y = []
    fig, ax = plt.subplots()
    ax.set_xlabel('deltaT [s]')
    ax.set_ylabel('Error [m?]')
    with open("mars_ERROR.xyz", "r") as file:
        lines = file.readlines()
        for line in lines:
            arr = line.split(";")
            key = arr[0].split(" ")[0]
            methods_dict[key].append(arr[2])
            methods_delta[key].append(arr[1])

        # plt.scatter(np.unique(x), methods_dict["Gear"])
        plt.scatter(methods_dict["Gear"], methods_delta["Gear"], label="Gear Predictor(V)")
        plt.scatter(methods_dict["Verlet"], methods_delta["Verlet"], label="Verlet")
        plt.scatter(methods_dict["Beeman"], methods_delta["Beeman"], label="Beeman")
        plt.legend(loc="lower right")
#        from matplotlib import ticker
#        formatter = ticker.ScalarFormatter(useMathText=True)
#        formatter.set_scientific(True)
#        ax.yaxis.set_major_formatter(formatter)
        plt.show()


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
