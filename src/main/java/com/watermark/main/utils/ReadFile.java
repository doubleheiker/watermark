package com.watermark.main.utils;

import com.watermark.main.DPWA.Dataset;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ReadFile {
    public static File multipartFileToFile(MultipartFile mFile) throws Exception {
        File file = new File(Objects.requireNonNull(mFile.getOriginalFilename()));
        FileUtils.copyInputStreamToFile(mFile.getInputStream(), file);

        return file;
    }

    public static ArrayList<String> splitVal(String line) {
        String[] split = line.split(",");
        ArrayList<String> ret = new ArrayList<>();
        //获取每行的属性值
        ret.addAll(Arrays.asList(split));

        return ret;
    }

    public static Dataset readTable(File file) throws IOException {
        Dataset dataset = new Dataset();
        LineIterator it = FileUtils.lineIterator(file, "UTF-8");
        Integer lineNum = 0;
        try {
            while(it.hasNext())
            {
                String line = it.nextLine();
                ArrayList<String> retTuple = ReadFile.splitVal(line);
                if (retTuple == null) {
                    lineNum++;
                    continue;
                }
                dataset.insert(lineNum, retTuple);
                lineNum++;
            }
        } finally {
            it.close();
        }
        return dataset;
    }
}
