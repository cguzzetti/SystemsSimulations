import matplotlib.pyplot as plt

def do_stfuff():
    with open(f"output_0.01_0.xyz") as f:
        fp_values = []
        right_side_count = 0
        animation_file = open(f"fpValues.xyz", "w")
        times = []
        current_N = 0
        N = 0
        is_time = False
        expected_time = 0
        should_write = True
        for line in f:
            if current_N == 0:  # Then we are looking at an N
                current_N = int(line)
                is_time = True
                should_write = True
                if N == 0:
                    N = current_N
                else:
                    fp_values.append(right_side_count/N)
                    right_side_count = 0
            elif is_time:
                current_time = float(line)
                is_time = False
                times.append(current_time)
            else:
                print(current_N)
                x = float(line.split(" ")[1])
                if x > 0.12:
                    right_side_count+=1
                current_N -= 1

        fp_values.append(right_side_count / N)
        plt.plot(times, fp_values)
        plt.show()


if __name__ == '__main__':
    do_stfuff()