package com.watermark.main.utils;

import com.watermark.main.DPWA.Dataset;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WriteFile {
    public static void WriteTable(Dataset dataset, File targetFile) throws IOException {

        HashMap<Integer, ArrayList<String>> table = dataset.getDatatable();
        Iterator<Map.Entry<Integer,ArrayList<String>>> iterator = table.entrySet().iterator();
        ArrayList<String> lines = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<Integer, ArrayList<String>> entry = iterator.next();
            //一行的所有属性值
            ArrayList<String> value = entry.getValue();
            String Line = value.get(0);
            for (int i = 1; i < value.size(); i++) {
                Line = Line +  "," + value.get(i);
            }
            lines.add(Line);
        }
        FileUtils.writeLines(targetFile, "UTF-8", lines);
    }
}
