package com.watermark.main.utils.wmoperate;

import com.watermark.main.DPWA.Dataset;
import org.apache.commons.math3.distribution.LaplaceDistribution;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WaterMarking {
    //嵌入算法
    public Dataset Embed(String K, Double M, Dataset dataset, Integer markedLine) {
        ArrayList<Integer> skey = new ArrayList<>();
        ArrayList<Double> lap = new ArrayList<>();
        HashMap<Integer,ArrayList<String>> table = dataset.getDatatable();
        HashMap<Integer,ArrayList<String>> markedtable = new HashMap<>();
        ArrayList<Double> intAttrs = new ArrayList<>();
        ArrayList<Double> ST = new ArrayList<>();
        ArrayList<Double> valuePlusS = new ArrayList<>();

        for (Map.Entry<Integer, ArrayList<String>> entry : table.entrySet()) {
            Integer p = entry.getKey();
            ArrayList<String> value = entry.getValue();
            if (F(K, p) % 2 == 1) {
                skey.add(1);
            } else {
                skey.add(-1);
            }
            double noise = laplaceMechanism(0.03);
            lap.add(Math.abs(noise));
            double st = Math.abs(noise) * skey.get(skey.size()-1);
            ST.add(st);
            intAttrs.add(Double.valueOf(value.get(markedLine)));
            double vs = intAttrs.get(intAttrs.size()-1)*skey.get(skey.size()-1);
            valuePlusS.add(vs);
        }

        double avgOfValue = avgOfDoubleArrayList(intAttrs);
        double avgOfLap = avgOfDoubleArrayList(lap);
        double avgOfS = avgOfArrayList(skey);
        double avgOfST = avgOfDoubleArrayList(ST);
        double varOfvalue = varianceOfDouble(intAttrs,avgOfValue);
        double varOfST = varianceOfDouble(ST,avgOfST);
        double avgOfVS = avgOfDoubleArrayList(valuePlusS);

        double[] coeff = SolveFunction.inputCoefficient(avgOfValue, avgOfLap, avgOfS, avgOfST,varOfvalue,varOfST,avgOfVS, M);
        SolveFunction sov = new SolveFunction();
        double[][] abr = {{1},{10},{5}};
        double[][] jacobiMatrix = SolveFunction.computeJacobiMatrix(abr,coeff);
        double[][] matrix = SolveFunction.computeOriginalFun(abr,coeff);
        double[][] result = sov.computeResult(abr, jacobiMatrix, matrix, coeff);

        Iterator<Map.Entry<Integer,ArrayList<String>>> iterator2 = table.entrySet().iterator();
        while (iterator2.hasNext()){
            Map.Entry<Integer, ArrayList<String>> entry = iterator2.next();
            Integer p = entry.getKey();
            ArrayList<String> value =  table.get(p);
            ArrayList<String> newvalue = new ArrayList<>();
            newvalue.addAll(value);
            double attrsValue = Double.valueOf(value.get(markedLine));
            double markedValue = result[0][0]*attrsValue + result[1][0] + lap.get(p)*skey.get(p)*result[2][0];//嵌入水印修改原值
            String markedValueString = Double.toString(markedValue);
            newvalue.set(markedLine,markedValueString);
            markedtable.put(p,newvalue);
        }

        Dataset markeddatast = new Dataset();
        markeddatast.setDatatable(markedtable);
        return markeddatast;
    }

    public Integer F(String K, Integer P) {
        Integer KoPDigest = this.SHA(K + P);
        return this.SHA(K + KoPDigest);
    }

    public Integer SHA(String message) {
        BigInteger digest = null;

        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA");
            algorithm.update(message.getBytes(), 0, message.length());
            digest = new BigInteger(1, algorithm.digest());
        } catch (Exception var4) {
            Logger.getLogger(WaterMarking.class.getName()).log(Level.SEVERE, (String)null, var4);
        }

        return Math.abs(digest.intValue());
    }

    public  boolean Detect(Dataset dataset, String K, Double M,Integer MarkedLine){//检测过程
        Integer length = dataset.getSize();

        HashMap<Integer,ArrayList<String>> table = dataset.getDatatable();
        ArrayList<Double> array = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<String>> entry : table.entrySet()) {
            Integer p = entry.getKey();
            ArrayList<String> value = entry.getValue();
            int s;
            if (F(K, p) % 2 == 1) {
                s = 1;
            } else {
                s = -1;
            }
            double markedvalue = Double.parseDouble(value.get(MarkedLine));
            array.add(s * markedvalue);
        }

        double avg = avgOfDoubleArrayList(array);
        return avg > M / 2;
    }

    public static Double laplaceMechanism(double epsilon){//laplace noise
        LaplaceDistribution ld = new LaplaceDistribution(0,1/epsilon);
        double noise = ld.sample();
        return noise;
    }

    static double avgofArray(double[] arr){
        double sum = 0;
        for (double v : arr) {
            sum += v;
        }
        return sum / arr.length;
    }

    static double avgOfArrayList(ArrayList<Integer> arrayList){
        double avg = 0.0;
        if(arrayList == null){
            return 0.0;
        }
        int n = arrayList.size();
        double sum = 0;
        for(Integer tmp:arrayList){
            sum += tmp;
        }
        avg = sum/n;
        return avg;
    }

    static double avgOfDoubleArrayList(ArrayList<Double> arrayList){
        double avg = 0.0;
        if(arrayList == null){
            return 0.0;
        }
        int n = arrayList.size();
        double sum = 0;
        for(Double tmp:arrayList){
            sum += tmp;
        }
        avg = sum/n;
        return avg;
    }

    static double variance(ArrayList<Integer> arrayList, double avg){
        double variance=0.0;
        for(Integer value:arrayList){
            variance = variance + Math.pow(value-avg, 2);
        }
        variance = variance/arrayList.size();
        return variance;

    }

    static double varianceOfDouble(ArrayList<Double> arrayList, double avg){
        double variance=0.0;
        for(Double value:arrayList){
            variance = variance + Math.pow(value-avg, 2);
        }
        variance = variance/arrayList.size();
        return variance;

    }
}

