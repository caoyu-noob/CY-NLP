package com.constants;

public class ModelConstant {
    private static String DATASET = "datasets";

    private static String SANGUO_EVENT = "SanGuoEvent";
    private static String SANGUO_FIGURE = "SanGuoFigure";
    private static String SANGUO_LOCATION = "SanGuoLocation";
    private static String SANGUO_TIME = "SanGuoTime";

    public static int CONSOLE_MODE = 1;
    public static int WEB_MODE = 2;

    //File names for different owl files
    public enum FileNames {
        EVENT("sanguoevent_chs.owl"),
        FIGURE("sanguofigure_chs.owl"),
        LOCATION("sanguolocation_chs.owl"),
        TIME("sanguotime_chs.owl");

        private String name;

        private FileNames(String fileName) {
            this.name = fileName;
        }

        public String get() {
            return name;
        }
    }

    //Model names for different types
    public enum ModelNames {
        SANGUO_EVENT("SanGuoEvent"),
        SANGUO_FIGURE("SanGuoFigure"),
        SANGUO_LOCATION("SanGuoLocation"),
        SANGUO_TIME("SanGuoTime");

        private String name;

        private ModelNames(String modelName) {
            this.name = modelName;
        }

        public String get() {
            return name;
        }
    }

    //Saved dirs for different models
    public enum DatasetNames {
        SANGUO_EVENT(DATASET + "\\SanGuoEvent"),
        SANGUO_FIGURE(DATASET + "\\SanGuoFigure"),
        SANGUO_LOCATION(DATASET + "\\SanGuoLocation"),
        SANGUO_TIME(DATASET + "\\SanGuoTime");

        private String name;

        private DatasetNames(String datasetName) {
            this.name = datasetName;
        }

        public String get() {
            return this.name;
        }
    }
}
