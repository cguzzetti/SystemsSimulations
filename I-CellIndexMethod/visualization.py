import numpy as np
import matplotlib.pyplot as plt
from matplotlib.colors import ListedColormap

# Read from file
file = open("CIMOutput.txt","r") 
lines = file.readlines()
print(lines)

# Create data
N = int(lines[0])
x = np.zeros(N)
y = np.zeros(N)
radius = np.zeros(N)
values = np.zeros(N)

# Selected particle
id = 17
neighbors = []
if len(lines[id+1].split()) == 5:
    neighbors = list(map(int,lines[id+1].split()[4].split(',')))
print(neighbors)

for i in range(1,N+1):
    if i-1 == id:
        values[i-1] = 1
    elif i-1 in neighbors:
        values[i-1] = 2
    else:
        values[i-1] = 0
    x[i-1] = lines[i].split()[1]
    y[i-1] = lines[i].split()[2]
    radius[i-1] = lines[i].split()[3]
    
print(radius) #Todo: include in visualization

colours = ListedColormap(['grey','r','b'])

plt.scatter(x, y, c=values, cmap=colours)
plt.show()