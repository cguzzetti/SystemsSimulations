import matplotlib.pyplot as plt
from matplotlib import cm
import numpy as np
from celluloid import Camera
from file_parser import parse_file

if __name__ == '__main__':
    N, L, t_max, x_values, y_values = parse_file("../CIMOutput.txt")
    camera = Camera(plt.figure())
    for i in range(t_max):
        show_x = x_values[i]
        show_y = y_values[i]
        plt.scatter(x=show_x, y=show_y, c="red")
        camera.snap()
    anim = camera.animate(blit=True)
    anim.save('scatter.gif')