from typing import List
from sklearn.metrics import mean_squared_error
import numpy as np


class ParserXYZ:
    N: int
    attr_number: int
    attributes: List
    blocks: List  # Contains the M attributes
    filename: str

    def __init__(self, N: int, filename: str):
        self.N = N
        self.blocks = []
        self.attributes = []
        self.filename = filename

    @staticmethod
    def initialize_parser(filename):
        with open(filename, "r") as file:
            N = int(file.readline())
        return ParserXYZ(N, filename)

    def parse_file(self):
        with open(self.filename, "r") as file:
            counter: int = -1
            for line in file.readlines()[1:]:  # Skip the first line (N)
                if counter == self.N:
                    counter = -2
                    self.blocks.append(self.attributes)
                    self.attributes = []
                elif counter >= 0:
                    self.attributes.append(line.split())
                counter += 1

    def calculate_position_error(self):
        values: np.ndarray = np.zeros((4, len(self.blocks)), dtype=np.float)
        counter = 0
        for block in self.blocks:
            for i in range(4):
                values[i][counter] = float(block[i][1])
            counter += 1

        return values


if __name__ == "__main__":
    for deltaT in ["0.0000001", "0.000001", "0.00001", "0.0001", "0.001"]:
        parser: ParserXYZ = ParserXYZ.initialize_parser(f"mars_OVITO_{deltaT}.xyz")
        parser.parse_file()
        values = parser.calculate_position_error()
        analytical = values[3]
        print(f"For deltaT = {deltaT}")
        print(f"\tGear: {mean_squared_error(analytical, values[0])}")
        print(f"\tVerlet: {mean_squared_error(analytical, values[1])}")
        print(f"\tBeeman: {mean_squared_error(analytical, values[2])}")
