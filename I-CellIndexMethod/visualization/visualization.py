import numpy as np
import matplotlib.pyplot as plt
from matplotlib.colors import ListedColormap
import matplotlib.collections
import sys

def is_out_of_bounds(L, id, x, y, radius):
    if( x < radius or
        x + radius > L or
        y < radius or
        y + radius > L):
        return True
    return False

def create_mark(x, y, radius, color_id, x_periodic, y_periodic, radius_periodic, values_periodic):
    values_periodic.append(color_id)
    x_periodic.append(x)
    y_periodic.append(y)
    radius_periodic.append(radius)

def check_out_of_bounds_and_plot(L, x, y, radius, color_id, x_periodic, y_periodic, radius_periodic, values_periodic):
    if is_out_of_bounds(L, id, x, y, radius):
        left = right = bottom = up = False
        if(x < radius):
            create_mark(x+L, y, radius, color_id, x_periodic, y_periodic, radius_periodic, values_periodic)
            left = True
        if(x+radius > L):
            create_mark(x-L, y, radius, color_id, x_periodic, y_periodic, radius_periodic, values_periodic)
            right = True
        if(y < radius):
            create_mark(x, y+L, radius, color_id, x_periodic, y_periodic, radius_periodic, values_periodic)
            bottom = True
        if(y+radius > L):
            create_mark(x, y-L, radius, color_id, x_periodic, y_periodic, radius_periodic, values_periodic)
            up = True
        
        # Corners
        if(left and bottom):
            create_mark(x+L, y+L, radius, color_id, x_periodic, y_periodic, radius_periodic, values_periodic)
        if(left and up):
            create_mark(x+L, y-L, radius, color_id, x_periodic, y_periodic, radius_periodic, values_periodic)
        if(right and bottom):
            create_mark(x-L, y+L, radius, color_id, x_periodic, y_periodic, radius_periodic, values_periodic)
        if(right and up):
            create_mark(x-L, y-L, radius, color_id, x_periodic, y_periodic, radius_periodic, values_periodic)
        
        return

# Read from file
file = open("CIMOutput.txt","r")
lines = file.readlines()

# Create data
N = int(lines[0].split()[0])
L = float(lines[0].split()[1])
rc = float(lines[0].split()[2])
periodic = True if lines[0].split()[3] == 'true' else False
x = np.zeros(N)
y = np.zeros(N)
radius = np.zeros(N)
values = [None] * N

x_periodic = []
y_periodic = []
radius_periodic = []
values_periodic = []

# Selected particle
id = -1
if len(sys.argv) == 2 and sys.argv[1].isdigit() and int(sys.argv[1]) >= 0 and int(sys.argv[1]) < N:
    id = int(sys.argv[1])
neighbors = []
if id!= -1 & len(lines[id+1].split()) == 5:
    neighbors = list(map(int,lines[id+1].split()[4].split(',')))

for i in range(1,N+1):
    if i-1 == id:
        values[i-1] = 'r'
    elif i-1 in neighbors:
        values[i-1] = 'b'
    else:
        values[i-1] = 'none'
    x[i-1] = lines[i].split()[1]
    y[i-1] = lines[i].split()[2]
    if float(lines[i].split()[3]) > 0:
        radius[i-1] = lines[i].split()[3]
    else:
        radius[i-1] = 0.06
    if periodic:
        check_out_of_bounds_and_plot(L, x[i-1], y[i-1], radius[i-1], values[i-1], x_periodic, y_periodic, radius_periodic, values_periodic)

# Define plot
fig, ax = plt.subplots()
ax.set_xlim(0, L)
ax.set_ylim(0, L)
ax.set_aspect('equal')

# Marks
for xi,yi,value,rad in zip(x,y,values,radius):
    c = plt.Circle((xi,yi), radius=rad, facecolor=value, edgecolor='black' if value == 'none' else value)
    ax.add_patch(c)

# Radius for rc
c_rad = plt.Circle((x[id], y[id]), rc, color='r', fill=False)
ax.add_patch(c_rad)
if periodic:
        check_out_of_bounds_and_plot(L, x[id], y[id], rc, 'r', x_periodic, y_periodic, radius_periodic, values_periodic)

# Duplicates for infinite boundaries
for xi,yi,value,rad in zip(x_periodic,y_periodic,values_periodic,radius_periodic):
    c = plt.Circle((xi,yi), radius=rad, facecolor=value if rad!=rc else 'none', edgecolor='black' if value == 'none' else value)
    ax.add_patch(c)

plt.show()


