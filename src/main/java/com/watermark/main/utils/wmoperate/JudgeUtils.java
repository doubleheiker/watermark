package com.watermark.main.utils.wmoperate;

import com.watermark.main.DPWA.Dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JudgeUtils {
    //判断需要添加水印的列是否存在且为数值型
    public void JudgeMarkedLine(Dataset dataset, Integer markedLine) throws Exception {
        HashMap<Integer, ArrayList<String>> table = dataset.getDatatable();

        for (Map.Entry<Integer, ArrayList<String>> entry : table.entrySet()) {
            ArrayList<String> value = entry.getValue();
            if (value.size() > markedLine) {
                try {
                    Double.valueOf(value.get(markedLine));
                } catch (NullPointerException | NumberFormatException e) {
                    throw new Exception("没有该列或者该列属性类型不是数值型！");
                }
            } else {
                throw new Exception("没有该列或者该列属性类型不是数值型！");
            }
        }
    }
}
