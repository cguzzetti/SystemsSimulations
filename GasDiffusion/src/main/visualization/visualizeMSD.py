import matplotlib.pyplot as plt
import numpy as np


# X: TIempo, Y: DCM
def get_errors(x, y):
    slope = 0.0001
    step = 0.00001
    final_slope = 0.005
    error_acum = 0
    errors = []
    slopes = []
    while slope <= final_slope:
        slopes.append(slope)
        for i in range(len(y)):
            error_acum += (y[i] - (slope*x[i]))**2
        errors.append(error_acum)
        error_acum = 0
        slope += step
    return slopes, errors


def visualize_MSD(x, y):
        plt.subplot()
        slopes, errs = get_errors(y, x)
        min = np.array(errs).min()
        index = np.where(errs == min)[0][0]
        desired_slope = slopes[index]
        print(f"Desired slope: {desired_slope} has error {min}")
        plt.scatter(slopes, errs)
        plt.xlabel("Pendientes [m*m/s]")
        plt.ylabel("Error [m*m]")
        plt.savefig("errorFigureN300.png")
        plt.clf()

        print(f"Polinomio: {desired_slope}*x")
        print(f"D= {desired_slope}")
        plt.scatter(y, x, c='k', label="Datos (promedios de simulaciones")
        plt.xlabel("Tiempo [s]")
        plt.ylabel("Desplazamiento cuadrático médio [m*m]")
        x_lin = np.linspace(np.array(y).min(), np.array(y).max())
        y_lin = desired_slope * x_lin

        plt.plot(x_lin,y_lin, label='Ajuste modelo lineal')
        plt.legend(loc ="lower right")
        plt.ticklabel_format(style='sci', axis='y', scilimits=(0,0), useMathText=True)
        plt.savefig("dcmN300.png")
        plt.savefig('MSD_100.png')


def find_x_y(i):
    startT = 5
    endT = 15
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

        x = x / x.size

        return x, y


if __name__ == "__main__":
    final_x = np.zeros(11)
    x_acumulator = []
    for i in range(0, 5):
        x, y = find_x_y(i)
        x_acumulator.append(x)
        final_x = np.add.reduce([final_x, x])

    final_x = final_x / 10
    visualize_MSD(final_x, y)
