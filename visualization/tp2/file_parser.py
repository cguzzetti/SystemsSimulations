import numpy as np

def parse_header(line):
    aux = line.split()
    return int(aux[0]), int(aux[1]), int(aux[2])

def parse_file(filename):
    file = open(filename,"r")
    lines = file.readlines()
    N, L, t_max = parse_header(lines[0])
    x_values, y_values = np.zeros((t_max, N)), np.zeros((t_max, N))
    
    print(f'N: {N}, L: {L}')
    
    curr_index = t = 0
    for line in lines[1:]:
        splitted_line = line.split()
        if(len(splitted_line) == 1):
            t+=1
            curr_index = 0
        else:
            x, y = splitted_line[1], splitted_line[2]
            x_values[t-1][curr_index] = float(x)
            y_values[t-1][curr_index] = float(y)
            curr_index+=1

    return N, L, t_max, x_values, y_values