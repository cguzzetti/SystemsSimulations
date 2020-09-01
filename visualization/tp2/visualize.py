import numpy as np
import matplotlib.pyplot as plt
import re

def visualize_va_against_t():
    repetitions = 10

    fig, ax = plt.subplots()
    ax.set_xlabel('t')
    ax.set_ylabel('Va')
    # ax.set_title()
    ax.set_ylim(0, 1)
    
    for j in [0,2,5,7,10]:
    # for j in range(0, repetitions+1):
        # Read from file
        file = open("../simulationOutputVa"+str(j)+".txt","r")
        lines = file.readlines()

        # Create data
        t = int(lines[0].split()[0])
        N = int(lines[0].split()[1])
        L = float(lines[0].split()[2])
        eta = float(lines[0].split()[3])
        
        x = np.zeros(t+1)
        y = np.zeros(t+1)


        index = 0

        for line in lines[1:]:
            line = line.split()
            x[index] = int(index)
            y[index] = float(line[0])
            index+=1
        
        # plt.plot(x, y, label ='N ='+str(N))
        plt.plot(x, y, label ='η ='+str(eta))
        
    plt.legend(loc ="lower right")
    plt.show()

def visualize_noise_against_va():
    fig, ax = plt.subplots()
    ax.set_xlabel('eta')
    ax.set_ylabel('Va')
    ax.set_ylim(0, 1)
    # Read from file
    for n in ["40", "100", "400"]:
        file = open(f"../../experimentResult{n}.txt","r")
        lines = file.readlines()

        # Create data
        x_eta = np.zeros(len(lines)-1)
        y_va = np.zeros(len(lines)-1)
        err = np.zeros(len(lines)-1)
        index = 0
        for line in lines[1:]:
            line = line.split()
            print(line)
            x_eta[index] = float(line[0])
            y_va[index] = float(line[1])
            err[index] = float(line[2])
            index+=1

        plt.plot(x_eta, y_va, "-o", label=f'N={n}')
        plt.errorbar(x_eta, y_va, yerr=err, fmt='none')
        
    plt.legend(loc='best')
    plt.show()

visualize_va_against_t()
# visualize_density_against_va()
visualize_noise_against_va()
