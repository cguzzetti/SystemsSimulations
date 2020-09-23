import numpy as np
import matplotlib.pyplot as plt

def plot_pressure():
    fig, ax = plt.subplots()
    ax.set_xlabel('Energía')
    ax.set_ylabel('Presión')
    #ax.set_ylim(0, 1)
    # ax.set_title('Va vs η')

    # for j in [40,100,400,4000]:
    # Read from file
    file = open("./gasExperiment.txt","r")
    # file = open("./ruidoN"+str(j),"r")
    lines = file.readlines()

    # Create data
    x = np.zeros(len(lines)-1)
    y = np.zeros(len(lines)-1)
    #std = np.zeros(len(lines)-1)
    index = 0
    for line in lines[1:]:
        line = line.split()
        x[index] = float(line[0])
        y[index] = float(line[1])
        #std[index] = float(line[2])
        index+=1

    plt.plot(x,y)
    #plt.errorbar(x_eta, y_vp, yerr=std,fmt='-o', label ='N ='+str(N))

    #plt.legend(loc ="upper right")
    plt.show()

if __name__ == '__main__':
    plot_pressure()