package com.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.constants.ModelConstant;

/**
 * Created by cao_y on 2018/2/2.
 */
public class FileAndDatasetNameService {

    private final String fileSeparator = System.getProperty("file.separator");

    public Map<String, String> getFileNamesMap(int applicationMode) {
        Map<String, String> fileNamesMap = new HashMap<>();
        String prefix = applicationMode == ModelConstant.CONSOLE_MODE ? "owl" + fileSeparator  :
                getCurrentRootDir() + fileSeparator + "owl" + fileSeparator;
        fileNamesMap.put(ModelConstant.ModelNames.SANGUO_EVENT.get(), prefix + ModelConstant.FileNames.EVENT.get());
        fileNamesMap.put(ModelConstant.ModelNames.SANGUO_FIGURE.get(), prefix + ModelConstant.FileNames.FIGURE.get());
        fileNamesMap.put(ModelConstant.ModelNames.SANGUO_LOCATION.get(), prefix + ModelConstant.FileNames.LOCATION.get());
        fileNamesMap.put(ModelConstant.ModelNames.SANGUO_TIME.get(), prefix + ModelConstant.FileNames.TIME.get());
        return fileNamesMap;
    }

    public Map<String, String> getDatasetNamesMap(int applicationMode) {
        Map<String, String> datasetNamesMap = new HashMap<>();
        String prefix = applicationMode == ModelConstant.CONSOLE_MODE ? "" : getCurrentRootDir() + fileSeparator;
        datasetNamesMap.put(ModelConstant.ModelNames.SANGUO_EVENT.get(), prefix + ModelConstant.DatasetNames.SANGUO_EVENT.get());
        datasetNamesMap.put(ModelConstant.ModelNames.SANGUO_FIGURE.get(), prefix + ModelConstant.DatasetNames.SANGUO_FIGURE.get());
        datasetNamesMap.put(ModelConstant.ModelNames.SANGUO_LOCATION.get(), prefix + ModelConstant.DatasetNames.SANGUO_LOCATION.get());
        datasetNamesMap.put(ModelConstant.ModelNames.SANGUO_TIME.get(), prefix + ModelConstant.DatasetNames.SANGUO_TIME.get());
        return datasetNamesMap;
    }

    public String getCurrentRootDir() {
        File file = new File(this.getClass().getResource("/").getFile());
        int i = 4;
        while (i-- > 0) {
            file = file.getParentFile();
        }
        return file.getAbsolutePath();
    }
}
