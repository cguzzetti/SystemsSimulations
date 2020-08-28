import numpy as np
import matplotlib.pyplot as plt

def visualize_va_against_t():
    # Read from file
    file = open("../../experimentResult.txt","r")
    lines = file.readlines()

    # Create data
    t = int(lines[0].split()[0])
    repetitions = int(lines[0].split()[1])
    L = float(lines[0].split()[2])
    eta = float(lines[0].split()[3])
    N = int(lines[0].split()[4])
    x = np.zeros(t)
    y = np.zeros(t)

    fig, ax = plt.subplots()
    ax.set_xlabel('t')
    ax.set_ylabel('Va')
    ax.set_title('Observable con N='+str(N)+', L='+str(L)+', eta='+str(eta))

    for j in range(0, repetitions+1):
        for i in range(0, t):
            x[i] = i
            y[i] = lines[1+j*t+i].split()[0]   
        plt.plot(x, y)

    plt.show()

visualize_va_against_t()
characteristic_t = input("Enter characteristic t:")
print(characteristic_t)
