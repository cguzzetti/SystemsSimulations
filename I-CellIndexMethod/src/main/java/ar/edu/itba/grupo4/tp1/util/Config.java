package ar.edu.itba.grupo4.tp1.util;

import org.apache.commons.cli.*;

import java.util.Arrays;

public class Config {
    final private InputType inputType;
    final private String inputFileName;
    final private String outputFileName;
    final private Boolean isPeriodic;

    public Config(String inputType, String inputFileName, String outputFileName, Boolean isPeriodic) throws IllegalArgumentException{
        if(!(inputType.equals("DYNAMIC") || inputType.equals("STATIC"))){
            throw new IllegalArgumentException(String.format("%s is not a valid file type", inputType));
        }
        this.inputType = InputType.valueOf(inputType);
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        this.isPeriodic = isPeriodic;
    }


    public InputType getInputType() {
        return inputType;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public Boolean getPeriodic() {
        return isPeriodic;
    }

    public static Config parseArguments(String[] args, Options options) throws ParseException {

        Option fileType = new Option("t", "type", true, "File type (Dynamic | Static)");
        fileType.setRequired(true);
        options.addOption(fileType);
        Option inputFile = new Option("f", "input", true, "Input filename");
        inputFile.setRequired(true);
        options.addOption(inputFile);
        Option outputFile = new Option("o", "output", true, "Output filename");
        outputFile.setRequired(false);
        options.addOption(outputFile);
        Option periodic = new Option("p", "periodic", false, "Periodic mode");
        periodic.setRequired(false);
        options.addOption(periodic);

        CommandLineParser parser = new DefaultParser();

        CommandLine cmd;

        cmd = parser.parse(options, args);
        String fileTypeValue = cmd.getOptionValue("type");
        String inputNameValue = cmd.getOptionValue("input");
        String outputNameValue = cmd.getOptionValue("output");
        if (outputNameValue == null)
            outputNameValue = "CIMOutput.txt";

        Boolean isPeriodic = cmd.hasOption("p");

        return new Config(fileTypeValue.toUpperCase(), inputNameValue, outputNameValue, isPeriodic);
    }

    @Override
    public String toString() {
        return String.format("Filetype: %s \nInputName: %s \nOutputName: %s \nisPeriodic: %s\n",
                this.inputType, this.inputFileName, this.outputFileName, this.isPeriodic
        );
    }
}
