from typing import List


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
