import numpy as np
import matplotlib.pyplot as plt

file = open("fpExperiment.txt","r")
lines = file.readlines()

x = []
y = []

for line in lines[1:]:
    splitted_line = line.split()
    num = round(float(splitted_line[1]), 1)
    if(num not in x):
        x.append(num)
        y.append(float(splitted_line[0]))


plt.plot(x, y)
plt.show()
