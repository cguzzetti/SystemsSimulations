package ar.edu.itba.grupo9.tp1.util;

import org.apache.commons.cli.*;

import java.util.Optional;


public class Config {
    final private InputType inputType;
    final private String inputFileName;
    final private String outputFileName;
    final private Boolean isPeriodic;
    final private Boolean isExperiment;
    final private RunMode runMode;
    final private Double rc;
    final private Long sideAreaLength;
    final private Integer numberOfParticles;
    final private Integer M;

    public Config(String inputType,
                  String inputFileName,
                  String outputFileName,
                  Boolean isPeriodic,
                  String mode,
                  String rc,
                  String M) throws IllegalArgumentException{
        if(!(inputType.equals("DYNAMIC") || inputType.equals("STATIC"))){
            throw new IllegalArgumentException(String.format("%s is not a valid file type", inputType));
        }
        this.inputType = InputType.valueOf(inputType);
        this.inputFileName = inputFileName;
        this.outputFileName = String.format("visualization/%s",outputFileName);
        this.isPeriodic = isPeriodic;
        this.isExperiment = false;
        if(!(mode.equals(RunMode.FORCE.toString()) || mode.equals(RunMode.CIM.toString())))
            throw new IllegalArgumentException(String.format("%s is not a valid mode", mode));
        this.runMode = RunMode.valueOf(mode);
        this.rc = Double.parseDouble(rc);
        this.sideAreaLength = null;
        this.numberOfParticles = null;
        this.M = (M != null)? Integer.parseInt(M): null;

    }

    public Config(String inputType,
                  String inputFileName,
                  String outputFileName,
                  Boolean isPeriodic,
                  String mode,
                  String rc,
                  String L,
                  String N,
                  String M) throws IllegalArgumentException{
        if(!(inputType.equals("DYNAMIC") || inputType.equals("STATIC"))){
            throw new IllegalArgumentException(String.format("%s is not a valid file type", inputType));
        }
        this.inputType = InputType.valueOf(inputType);
        this.inputFileName = inputFileName;
        this.outputFileName = String.format("visualization/%s",outputFileName);
        this.isPeriodic = isPeriodic;
        this.isExperiment = true;
        if(!(mode.equals(RunMode.FORCE.toString()) || mode.equals(RunMode.CIM.toString())))
            throw new IllegalArgumentException(String.format("%s is not a valid mode", mode));
        this.runMode = RunMode.valueOf(mode);
        this.rc = Double.parseDouble(rc);
        this.sideAreaLength = Long.parseLong(L);
        this.numberOfParticles = Integer.parseInt(N);
        this.M = (M != null)? Integer.parseInt(M): null;

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
        Option experiment = new Option("e", "experiment", false, "Create random particles in file");
        experiment.setRequired(false);
        options.addOption(experiment);
        Option sideValue = new Option("L", true, "Length of the side of the grid");
        sideValue.setRequired(false);
        options.addOption(sideValue);
        Option rc = new Option("rc", true, "Interaction radius");
        rc.setRequired(false);
        options.addOption(rc);
        Option numberOfParticles = new Option("N", "particles-number", true, "Number of particles");
        numberOfParticles.setRequired(false);
        options.addOption(numberOfParticles);
        Option mode = new Option("m", "mode", true, "Run mode (BRUTE|CIM)");
        mode.setRequired(true);
        options.addOption(mode);
        Option M = new Option("M", "mode", true, "Run mode (BRUTE|CIM)");
        M.setRequired(false);
        options.addOption(M);

        CommandLineParser parser = new DefaultParser();

        CommandLine cmd;

        cmd = parser.parse(options, args);
        String fileTypeValue = cmd.getOptionValue("type");
        String inputNameValue = cmd.getOptionValue("input");
        String outputNameValue = cmd.getOptionValue("output");
        if (outputNameValue == null)
            outputNameValue = "CIMOutput.txt";

        Boolean isPeriodic = cmd.hasOption("p");
        Boolean isExperiment = cmd.hasOption("e");


        String rcValue;
        if(!cmd.hasOption("rc")){
            if(cmd.getOptionValue("mode").toUpperCase().equals("CIM"))
                rcValue = "0.9";
            else
                rcValue = "1.0";
        }else{
            rcValue = cmd.getOptionValue("rc");
        }

        if(isExperiment){
            if(!(cmd.hasOption("L") && cmd.hasOption("N")))
                throw new ParseException("N and L must be specified in Experiment mode");
            return new Config(
                    fileTypeValue.toUpperCase(),
                    inputNameValue,
                    outputNameValue,
                    isPeriodic,
                    cmd.getOptionValue("mode").toUpperCase(),
                    rcValue,
                    cmd.getOptionValue("L"),
                    cmd.getOptionValue("N"),
                    cmd.getOptionValue("M")
            );
        }

        return new Config(
                fileTypeValue.toUpperCase(),
                inputNameValue,
                outputNameValue,
                isPeriodic,
                cmd.getOptionValue("mode").toUpperCase(),
                rcValue,
                cmd.getOptionValue("M")
        );
    }

    @Override
    public String toString() {
        return String.format("Filetype: %s \nInputName: %s \nOutputName: %s \nisPeriodic: %s\n",
                this.inputType, this.inputFileName, this.outputFileName, this.isPeriodic
        );
    }

    public Boolean isExperiment() {
        return isExperiment;
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

    public Boolean isPeriodic() {
        return isPeriodic;
    }

    public RunMode getRunMode() {
        return runMode;
    }

    public Double getRc() {
        return rc;
    }

    public Long getSideAreaLength() {
        return sideAreaLength;
    }

    public Integer getNumberOfParticles() {
        return numberOfParticles;
    }

    public Integer getM() {
        return M;
    }
}
