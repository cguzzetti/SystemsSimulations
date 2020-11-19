import matplotlib.pyplot as plt
from typing import List
import sys
import numpy as np
from xyzParser import ParserXYZ
from glob import glob


def velocity_over_time(velocities: np.ndarray, t: float):
    step = 0.01
    times = np.arange(0, t/100, 0.01)
    fig, ax = plt.subplots()
    ax.set_xlabel('Tiempo [s]')
    ax.set_ylabel('Velocidad [m/s]')

    plt.plot(times, velocities)
    #plt.savefig("velocities.png")
    plt.show()


def plot_metric(velocities: dict, errors: dict, label: str, filename: str):
    dmins = velocities.keys()
    vels = velocities.values()
    errs = errors.values()

    fig, ax = plt.subplots()
    ax.set_xlabel('dmin [m]')
    ax.set_ylabel(label)
    plt.plot(dmins, vels, 'bo')
    plt.errorbar(dmins, vels, yerr=errs, fmt='none', ecolor="red")
    plt.savefig(filename)
    #plt.show()


def get_mean_velocity(parser: ParserXYZ):
    v_index = 5
    velocities = np.zeros((len(parser.blocks), 2), dtype=np.float)
    counter = 0
    for block in parser.blocks:
        pedestrian: List = block[0]  # We are only interested in the velocity of the pedestrian
        vx: float = float(pedestrian[v_index].split(",")[0])
        vy: float = float(pedestrian[v_index+1].split(",")[0])
        velocities[counter] = (vx, vy)
        counter += 1

    mean_velocity = tuple(np.mean(velocities, axis=0))

    return np.linalg.norm(mean_velocity)


def get_distance(parser: ParserXYZ):
    p_index = 3
    prev_position = None
    dist = 0
    for block in parser.blocks:
        pedestrian: List = block[0]
        x: float = float(pedestrian[p_index].split(",")[0])
        y: float = float(pedestrian[p_index + 1].split(",")[0])
        current_position = np.array((x, y))
        if prev_position is not None:
            # https://stackoverflow.com/questions/1401712/how-can-the-euclidean-distance-be-calculated-with-numpy
            dist += np.linalg.norm(prev_position - current_position)
        prev_position = current_position

    return dist


if __name__ == '__main__':
    if len(sys.argv) < 2:
        print("You must specify a filename/file pattern as a CLI argument")
        exit(1)

    if len(sys.argv) < 3:
        print("Using default mode: SINGLE")
        mode = "SINGLE"
    else:
        mode = sys.argv[2]

    print(mode)
    if mode == "SINGLE":
        filename = sys.argv[1]
        _parser = ParserXYZ.initialize_parser(filename)
        _parser.parse_file()
        print(f"Mean velocity: {get_mean_velocity(_parser)} m/s")
        print(f"Distance travelled: {get_distance(_parser)} m")
        print(f"Time spent: {len(_parser.blocks)/100 } seconds")
    elif mode == "BULK":
        vel_dict = dict()
        dist_dict = dict()
        time_dict = dict()

        vel_errs = dict()
        dist_errs = dict()
        time_errs = dict()
        dmin = [0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0]
        #dmid = [0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5]
        dmid = [1.5, 1.6, 1.7, 1.8, 1.9, 2.0, 2.1, 2.2, 2.3, 2.4, 2.5]
        #rad = [0.2, 0.25, 0.3, 0.35, 0.4, 0.45, 0.5, 0.55, 0.6]
        rad = [0.35, 0.40, 0.5, 0.60, 0.7, 0.80, 0.9, 1.0, 1.1, 1.2]
        chosen_observable = sys.argv[3]
        print("Observable:")
        print(chosen_observable)
        if chosen_observable == "dmin":
            observables = dmin
        elif chosen_observable == "dmid":
            observables = dmid
        else:
            observables = rad

        for observable in observables:
            pattern = sys.argv[1]
            filenames = glob(f"bulk/{pattern}{observable}_*")
            velocities = np.zeros(len(filenames), dtype=np.float)
            distances = np.zeros(len(filenames), dtype=np.float)
            times = np.zeros(len(filenames), dtype=np.float)
            index = 0
            for filename in filenames:
                _parser = ParserXYZ.initialize_parser(filename)
                _parser.parse_file()

                velocities[index] = get_mean_velocity(_parser)
                distances[index] = get_distance(_parser)
                times[index] = len(_parser.blocks)/100
                index += 1
            vel_dict[observable] = velocities.mean()
            vel_errs[observable] = velocities.std()
            dist_dict[observable] = distances.mean()
            dist_errs[observable] = distances.std()
            time_dict[observable] = times.mean()
            time_errs[observable] = times.std()

        plot_metric(vel_dict, vel_errs, "Velocidad media [m/s]", "vmedia_dmin.png")
        plot_metric(dist_dict, dist_errs, "Longitud del recorrido [m]", "dist_dmin.png")
        plot_metric(time_dict, time_errs, "Tiempo de tránsito [s]", "tiempo_dmin.png")

        # print(" ----------- Velocities --------------")
        # print(velocities)
        # print(" ----------- distance travelled --------------")
        # print(distances)
        # print(" ----------- times --------------")
        # print(times)
        # print(f"Mean velocity: {velocities.mean()} with std {velocities.std()}")
        # print(f"Mean distance travelled: {distances.mean()} with std {distances.std()}")
        # print(f"Mean time spent: {times.mean()} with std {times.std()}")

    else:
        print("Error")

