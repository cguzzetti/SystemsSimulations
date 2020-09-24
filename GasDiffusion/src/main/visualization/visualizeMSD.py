import matplotlib.pyplot as plt
import numpy as np


def visualize_MSD(x, y):
        p = np.polyfit(y, x, 1)
        yfit = np.polyval(p, y)
        print(f"D= {abs(p[1])}")
        plt.scatter(y, x, c='k')
        plt.xlabel("Tiempo [s]")
        plt.ylabel("Desplazamiento cuadrático médio [m*m]")
        plt.plot(y, yfit, label='regr')
        plt.savefig('MSD_N200.png')


def find_x_y(i):
    startT = 200
    endT = 210
    with open(f"animation{i}.xyz") as f:
        x = np.zeros(11)
        y = list(range(startT, endT + 1))
        current_N = 0
        parts = dict()
        is_time = False
        for line in f:
            if current_N == 0:  # Then we are looking at an N
                current_N = int(line)
                N = current_N
                is_time = True
            elif is_time:
                current_time = float(line.split(" ")[0])
                is_time = False
            else:
                if startT <= current_time <= endT:
                    particle_id = int(line.split(" ")[0])
                    if particle_id not in parts:
                        parts[particle_id] = [float(line.split(" ")[7])]
                    else:
                        parts[particle_id].append(float(line.split(" ")[7]))
                current_N -= 1

        for values in parts.values():
            if -1 not in values:
                aux = [x, values]
                x = np.add.reduce(aux)  # Vector sum x + values

        x = x / N

        return x, y


if __name__ == "__main__":
    final_x = np.zeros(11)
    x_acumulator = []
    for i in range(0, 100):
        x, y = find_x_y(i)
        x_acumulator.append(x)
        final_x = np.add.reduce([final_x, x])

    final_x = final_x / 10
    visualize_MSD(final_x, y)
