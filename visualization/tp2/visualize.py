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
    
    # for j in [0,2,4,6,10,20]:
    for j in [0,2,5,7,10]:
    # for j in range(0, repetitions+1):
        # Read from file
        # file = open("/home/bb8/src/itba/SystemsSimulations/visualization/old_density/simulationOutputVa"+str(j)+".txt","r")
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
    ax.set_xlabel('η')
    ax.set_ylabel('Va')
    ax.set_ylim(0, 1)
    # ax.set_title('Va vs η')

    for j in [40,100,400,4000]:
        # Read from file
        # file = open("../../experimentResult.txt","r")
        file = open("./ruidoN"+str(j),"r")
        lines = file.readlines()
        N = int(lines[0].split()[4])
        
        # Create data
        x_eta = np.zeros(len(lines)-1)
        y_vp = np.zeros(len(lines)-1)
        std = np.zeros(len(lines)-1)
        index = 0
        for line in lines[1:]:
            line = line.split()
            x_eta[index] = float(line[0])
            y_vp[index] = float(line[1])
            std[index] = float(line[2])
            index+=1
        
        plt.errorbar(x_eta, y_vp, yerr=std,fmt='-o', label ='N ='+str(N))
        
    plt.legend(loc ="upper right")
    plt.show()

def visualize_density_against_va():
    # Read from file
    file = open("../../experimentResult.txt","r")
    lines = file.readlines()

    # Create data
    x_density = np.zeros(len(lines)-1)
    y_va = np.zeros(len(lines)-1)
    std = np.zeros(len(lines)-1)
    index = 0
    for line in lines[1:]:
        line = line.split()
        x_density[index] = float(line[0])
        y_va[index] = float(line[1])
        std[index] = float(line[2])
        index+=1
    
    fig, ax = plt.subplots()
    ax.set_xlabel('ρ')
    ax.set_ylabel('Va')
    # ax.set_title('Va vs ρ')
    ax.set_ylim(0, 1)

    plt.errorbar(x_density, y_va, yerr=std,fmt='-o')

    plt.show()

# visualize_va_against_t()
# visualize_density_against_va()
visualize_noise_against_va()
