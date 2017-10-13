package main.java;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class MooreMachines implements Cloneable{
    private final String DELIMITER = " ";
    private ArrayList<ArrayList<Integer>> mTable;
    private ArrayList<Integer> mStates;
    private ArrayList<Integer> mOutSignals;
    public MooreMachines(){
        this.mTable = new ArrayList<ArrayList<Integer>>();
        this.mStates = new ArrayList<Integer>();
        this.mOutSignals = new ArrayList<Integer>();
    }
    public MooreMachines(MooreMachines original) {
        this.mTable = new ArrayList<ArrayList<Integer>>(original.getTable());
        this.mStates = new ArrayList<Integer> (original.getStates());
        this.mOutSignals = new ArrayList<Integer> (original.getOutputSignals());
    }
    public MooreMachines copy() {
        return new MooreMachines(this);
    }
    public ArrayList<ArrayList<Integer>> getTable() {
        return mTable;
    }

    public ArrayList<Integer> getOutputSignals() {
        return mOutSignals;
    }

    public ArrayList<Integer> getStates() {
        return mStates;
    }

    public void readTableFromFile(String filePath) throws FileNotFoundException {
        Scanner scanner = null;
        FileReader file = new FileReader(filePath);
        scanner = new Scanner(new BufferedReader(file));

        String line = scanner.nextLine();
        mOutSignals = new ArrayList<Integer>();
        for(String element : line.split(DELIMITER)) {
            if(!element.isEmpty()) {
                mOutSignals.add(Integer.parseInt(element));
            }
        }

        line = scanner.nextLine();
        mStates = new ArrayList<Integer>();
        for(String element : line.split(DELIMITER)) {
            if(!element.isEmpty()) {
                mStates.add(Integer.parseInt(element));
            }
        }


        mTable = new ArrayList<ArrayList<Integer>>();
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            String[] elements = line.split(DELIMITER);
            ArrayList<Integer> transitions = new ArrayList<Integer>();
            for(int i = 1; i < elements.length; ++i) {
                if(!elements[i].isEmpty()) {
                    transitions.add(Integer.parseInt(elements[i]));
                }
            }
            mTable.add(transitions);
        }

    }

    public void printTable() {
        System.out.println("  " + mOutSignals);
        System.out.println("  " + mStates);
        System.out.println("  " + mTable);

    }
}
