import numpy as np
import matplotlib.pyplot as plt

# Read from file
file = open("../../experimentResult.txt","r")
lines = file.readlines()

# Create data
N = int(lines[0].split()[0])
print(N)
x = np.zeros(N)
y = np.zeros(N)

for i in range(1,N+1):
    x[i-1] = i-1
    y[i-1] = lines[i].split()[0]   

plt.plot(x, y)
plt.show()