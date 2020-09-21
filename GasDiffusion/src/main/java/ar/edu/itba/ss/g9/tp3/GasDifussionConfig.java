package ar.edu.itba.ss.g9.tp3;


import org.apache.commons.cli.*;

public class GasDifussionConfig {
    private final int N;
    private final String inputFileName;
    private final String outputFileName;
    private final Double width;
    private final Double height;
    private Double partitionLen;

    public GasDifussionConfig(String N, String inputName, String outputName){
        this.N = Integer.parseInt(N);
        this.inputFileName = inputName;
        this.outputFileName = outputName;
        this.width = 0.05;
        this.height = 0.05;
        this.partitionLen = 0.01;
    }
    public static GasDifussionConfig parseArguments(String[] args, Options options) throws ParseException {
        Option particleNum = new Option("N", "particleNum", true, "Number of particles");
        particleNum.setRequired(true);
        options.addOption(particleNum);

        Option inputFile = new Option("f", "inputFile", true, "Input filename");
        inputFile.setRequired(true);
        options.addOption(inputFile);

        Option outputFile = new Option("o", "outputFile", true, "Output filename");
        outputFile.setRequired(false);
        options.addOption(outputFile);

        CommandLineParser parser = new DefaultParser();

        CommandLine cmd;

        cmd = parser.parse(options, args);
        return new GasDifussionConfig(
                cmd.getOptionValue("N"),
                cmd.getOptionValue("f"),
                cmd.getOptionValue("o")
        );
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    @Override
    public String toString() {
        return String.format("Using N: %d; inputFile: %s, outputFile: %s",
                N, inputFileName, outputFileName);
    }

    public int getN() {
        return N;
    }

    public Double getWidth() {
        return width;
    }

    public Double getHeight() {
        return height;
    }

    public Double getPartitionLen() {
        return partitionLen;
    }

    public void setPartitionLen(double newLen){
        this.partitionLen = newLen;
    }
}
