import numpy as np
import matplotlib.pyplot as plt
import re

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
    ax.set_ylim(0, 1)

    for j in range(0, repetitions+1):
        for i in range(0, t):
            x[i] = i
            y[i] = lines[1+j*t+i].split()[0]   
        plt.plot(x, y)

    plt.show()

def visualize_noise_against_va():
    # Read from file
    file = open("../../experimentResult.txt","r")
    lines = file.readlines()

    # Create data
    x_eta = np.zeros(len(lines))
    y_vp = np.zeros(len(lines))
    index = 0
    for line in lines[1:]:
        line = line.split()
        x_eta[index] = float(line[0])
        y_vp[index] = float(line[1])
    
    fig, ax = plt.subplots()
    ax.set_xlabel('noise')
    ax.set_ylabel('va')
    ax.set_title('Noise vs VA')
     
    plt.plot(x_eta, y_vp)

    plt.show()

#visualize_va_against_t()
#characteristic_t = input("Enter characteristic t:")
#print(characteristic_t)
visualize_noise_against_va()
