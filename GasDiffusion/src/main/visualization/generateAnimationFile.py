
def write_headers(animation_file, N, time, real_time):
    animation_file.write(f'{N}\n{time} ({real_time})\n')


def write_line(animation_file, line):
    print(line)
    animation_file.write(f'{line}')


def generate_animation(i):
    # Save in RAM only one line at a time
    with open(f"output{i}.xyz") as f:
        animation_file = open(f"animation{i}.xyz", "w")
        current_N = 0
        is_time = False
        expected_time = 0
        should_write = True
        for line in f:
            if current_N == 0:  # Then we are looking at an N
                current_N = int(line)
                is_time = True
                should_write = True
            elif is_time:
                current_time = float(line)
                is_time = False
                if current_time >= expected_time:
                    write_headers(animation_file, current_N, expected_time, current_time)
                    expected_time += 1
                else:
                    should_write = False
            else:
                if should_write:
                    write_line(animation_file, line)
                current_N -= 1


if __name__ == '__main__':
    for i in range(100):
        generate_animation(i)

