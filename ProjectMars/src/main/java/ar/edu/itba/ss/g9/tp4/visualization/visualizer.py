import matplotlib.pyplot as plt
import sys


def visualize_error():
    print("NOT IMPLEMENTED")
    pass


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
