import matplotlib.pyplot as plt

if __name__ == '__main__':
    fig, ax = plt.subplots()
    ax.set_xlabel('t [s]')
    ax.set_ylabel('Posición [m]')

    methods = ["Gear predictor", "Verlet original", "Beeman", "Analítico"]
    # Read from file
    file = open("mars_SOLUTION.xyz","r")
    lines = file.readlines()

    for i in range(0, len(methods)):
        x = []
        y = []
        for line in lines[:]:
            line = line.split()
            print(line)
            x.append(float(line[0]))
            y.append(float(line[i+1]))

        plt.plot(x, y, label ='Método: '+methods[i])

    plt.legend(loc ="lower right")
    plt.show()
