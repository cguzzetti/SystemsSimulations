import numpy as np
import matplotlib.pyplot as plt
import math
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

def visualize():
    # Read from file
    file = open("../CIMOutput.txt","r")
    lines = file.readlines()

    # Create data
    N = int(lines[0].split()[0])
    L = float(lines[0].split()[1])
    rc = float(lines[0].split()[3])
    periodic = True
    maxRadius1 = float(lines[0].split()[4])
    maxRadius2 = float(lines[0].split()[5])
    timeLapse = float(lines[0].split()[2])
    x = np.zeros(N)
    y = np.zeros(N)
    radius = np.zeros(N)
    values = [None] * N

    x_periodic = []
    y_periodic = []
    radius_periodic = []
    values_periodic = []

    # Selected t
    t = -1
    if len(sys.argv) == 3 and sys.argv[2].isdigit() and int(sys.argv[2]) >= 0 and int(sys.argv[2]) < timeLapse:
        t = int(sys.argv[2])
    if t == -1:
        print("You must indicate a t")
        return
    shift = (t-1)*N + t + 1 
    print(N)
    print(t)
    print(shift)
    print(len(lines))

    # Selected particle
    id = -1
    if len(sys.argv) == 3 and sys.argv[1].isdigit() and int(sys.argv[1]) >= 0 and int(sys.argv[1]) < N:
        id = int(sys.argv[1])
    neighbors = []
    if id!= -1 & len(lines[id+shift].split()) == 7:
        print(id+shift)
        print(neighbors)
        print(lines[id+shift])
        neighbors = list(map(int,lines[id+shift].split()[6].split(',')))
        

    for i in range(0,N):
        if i == id:
            values[i] = 'r'
        elif i in neighbors:
            values[i] = 'b'
        else:
            values[i] = 'none'
        x[i] = lines[i+shift].split()[1]
        y[i] = lines[i+shift].split()[2]    
        if float(lines[i+shift].split()[3]) > 0:
            radius[i] = lines[i+shift].split()[3]
        else:
            radius[i] = 0.06
        if periodic:
            check_out_of_bounds_and_plot(L, x[i], y[i], radius[i], values[i], x_periodic, y_periodic, radius_periodic, values_periodic)

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
    if id != -1:
        c_rad = plt.Circle((x[id], y[id]), rc, color='r', fill=False)
        ax.add_patch(c_rad)
        if periodic:
                check_out_of_bounds_and_plot(L, x[id], y[id], rc, 'r', x_periodic, y_periodic, radius_periodic, values_periodic)

    # Duplicates for infinite boundaries
    for xi,yi,value,rad in zip(x_periodic,y_periodic,values_periodic,radius_periodic):
        c = plt.Circle((xi,yi), radius=rad, facecolor=value if rad!=(rc) else 'none', edgecolor='black' if value == 'none' else value)
        ax.add_patch(c)

    plt.show()


visualize()