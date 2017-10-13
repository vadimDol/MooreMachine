package main.java;

import java.lang.reflect.Array;
import java.util.*;
public class Minimization {
    private final String DELIMITER = "/";
    private final MooreMachines mMoore;
    private MooreMachines mResultMinimize;
    public Minimization(MooreMachines moore) throws CloneNotSupportedException {
        mMoore = new MooreMachines(moore);
    }

    public MooreMachines getResultMinimize() {
        return mResultMinimize;
    }

    private boolean compareEquivalentClass(Map<String, ArrayList<Integer>> oldEquivalentMap,
                                           Map<String, ArrayList<Integer>> newEquivalentMap) {

        if(oldEquivalentMap.size() != newEquivalentMap.size()) {
            return false;
        }
        Set<ArrayList<Integer>> values1 = new HashSet<>(oldEquivalentMap.values());
        Set<ArrayList<Integer>> values2 = new HashSet<>(newEquivalentMap.values());
        return values1.equals(values2);
    }

    private Map<String, ArrayList<Integer>> getEquivalenceMap(MooreMachines resultMoore) {
        Map<String, ArrayList<Integer>> result = new LinkedHashMap<String, ArrayList<Integer>>();
        for(int i = 0; i < resultMoore.getStates().size(); i++) {
            Integer key = resultMoore.getOutputSignals().get(i);
            String str = "" + key;
            for(ArrayList<Integer> row : resultMoore.getTable()) {
                str += "/" + row.get(i);
            }
            ArrayList<Integer> array = new ArrayList<Integer>();
            if(result.containsKey(str)) {
                array = new ArrayList<Integer>(result.get(str));
            }
            array.add(resultMoore.getStates().get(i));
            result.put(str, array);
        }
        return result;
    }

    private void splittingClassesEquivalence(Map<String, ArrayList<Integer>> equivalenceClass, MooreMachines resultMoore ) {

        while(true) {
            Map<String, ArrayList<Integer>> newEquivalenceClass = getEquivalenceMap(resultMoore);

            resultMoore.getTable().clear();
            resultMoore.getStates().clear();
            resultMoore.getOutputSignals().clear();

            //init output signal
            for (Map.Entry<String, ArrayList<Integer>> pair : newEquivalenceClass.entrySet()) {
                String key = pair.getKey();
                for(int i = 0; i < pair.getValue().size(); ++i) {
                    resultMoore.getOutputSignals().add(Integer.parseInt(key.split(DELIMITER)[0]));
                }
            }

            //init state
            for(Map.Entry<String, ArrayList<Integer>> pair : newEquivalenceClass.entrySet()) {
                for(Integer state : pair.getValue()) {
                    resultMoore.getStates().add(state);
                }
            }


            ArrayList<ArrayList<Integer>> mooreTable =  resultMoore.getTable();
            for(int i = 0; i < mMoore.getTable().size(); ++i) {
                mooreTable.add(new ArrayList<Integer>());
            }

            for(Map.Entry<String, ArrayList<Integer>> pair : newEquivalenceClass.entrySet()) {
                for(int i = 0; i < pair.getValue().size(); ++i) {
                    int indexStateInStartTable =  getIndexByStateName(pair.getValue().get(i));
                    for(int indexStartTable = 0; indexStartTable < mMoore.getTable().size(); indexStartTable++) {
                        Integer transition = mMoore.getTable().get(indexStartTable).get(indexStateInStartTable);

                        int index = 0;
                        for(Map.Entry<String, ArrayList<Integer>> pairEquivalence : newEquivalenceClass.entrySet()) {
                            if (pairEquivalence.getValue().contains(transition)) {
                                mooreTable.get(indexStartTable).add(index + 1);

                            }
                            ++index;
                        }
                    }
                }
            }

            if(compareEquivalentClass(equivalenceClass, newEquivalenceClass)) {
                break;
            }
            equivalenceClass.clear();
            equivalenceClass.putAll(newEquivalenceClass);

        }
    }

    public void run() {
        MooreMachines zeroEquivalence = getZeroEquivalence();

        Map<String, ArrayList<Integer>> equivalenceClass = new LinkedHashMap<String, ArrayList<Integer>>();
        splittingClassesEquivalence(equivalenceClass, zeroEquivalence);

        MooreMachines result = getResultingTable(equivalenceClass);
        System.out.println("========Result=========");
        printMooreTable(result);
        System.out.println("=======================");
        mResultMinimize = result;
    }

    private MooreMachines getZeroEquivalence() {
        MooreMachines resultMoore = new MooreMachines(mMoore);
        sort(resultMoore);
        for(int i = 0; i < resultMoore.getStates().size(); ++i) {
            for(int j = 0; j < resultMoore.getTable().size(); ++j) {
                ArrayList<Integer> value = resultMoore.getTable().get(j);
                Integer outputSignal = mMoore.getOutputSignals().get(getIndexByStateName(value.get(i)));
                value.set(i, outputSignal);
            }
        }
        System.out.println("========Zero Equivalence=========");
        printMooreTable(resultMoore);
        System.out.println("=======================");
        return resultMoore;
    }

    private void sort(MooreMachines resultMoore) {
        ArrayList<Integer> state = resultMoore.getStates(); state.clear();
        ArrayList<Integer> outputSignal = resultMoore.getOutputSignals(); outputSignal.clear();
        ArrayList<ArrayList<Integer>> table = resultMoore.getTable(); table.clear();
        table = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i < mMoore.getTable().size(); ++i) {
            table.add(new ArrayList<Integer>());
        }
        Set setOutSignalIndex = new HashSet();
        for(int index = 0; index < mMoore.getOutputSignals().size(); index++) {
            for(int outSignalIndex : getAllIndex( mMoore.getOutputSignals().get(index))) {
                if(!setOutSignalIndex.contains(outSignalIndex)) {
                    outputSignal.add(mMoore.getOutputSignals().get(index));
                    state.add(outSignalIndex + 1);
                    for(int i = 0; i < table.size(); ++i) {
                        table.get(i).add(mMoore.getTable().get(i).get(outSignalIndex));
                    }
                    setOutSignalIndex.add(outSignalIndex);
                }

            }
        }

        for(int i = 0; i < table.size(); ++i) {
            resultMoore.getTable().add(table.get(i));
        }
        System.out.println("========SORT=========");
        printMooreTable(resultMoore);
        System.out.println("=======================");
    }

    private MooreMachines getResultingTable(Map<String, ArrayList<Integer>> equivalenceClass ) {
        MooreMachines moore = new MooreMachines();

        //init output signal
        for (Map.Entry<String, ArrayList<Integer>> pair : equivalenceClass.entrySet()) {
            String key = pair.getKey();
            moore.getOutputSignals().add(Integer.parseInt(key.split(DELIMITER)[0]));
        }

        //init state
        for (int i = 0; i < moore.getOutputSignals().size(); ++i) {
            moore.getStates().add(i + 1);
        }

        ArrayList<ArrayList<Integer>> mooreTable =  moore.getTable();
        for(int i = 0; i < mMoore.getTable().size(); ++i) {
            mooreTable.add(new ArrayList<Integer>());

        }

        for(Map.Entry<String, ArrayList<Integer>> pair : equivalenceClass.entrySet()) {
            Integer state = pair.getValue().get(0);
            int indexStateInStartTable =  getIndexByStateName(state);
            for(int indexStartTable = 0; indexStartTable < mMoore.getTable().size(); indexStartTable++) {
                Integer transition = mMoore.getTable().get(indexStartTable).get(indexStateInStartTable);

                int i = 0;
                for(Map.Entry<String, ArrayList<Integer>> pairEquivalence : equivalenceClass.entrySet()) {
                    if (pairEquivalence.getValue().contains(transition)) {
                        mooreTable.get(indexStartTable).add(i + 1);
                    }
                    ++i;
                }
            }
        }
        return moore;
    }

    private void printMooreTable(MooreMachines moore) {
        System.out.println(moore.getOutputSignals());
        System.out.println(moore.getStates());
        for(ArrayList<Integer> array : moore.getTable()) {
            System.out.println(array);
        }
    }

    private ArrayList<Integer> getAllIndex(Integer vertex)
    {
        ArrayList<Integer> array = new ArrayList<Integer>();
        ArrayList<Integer> outSignals = mMoore.getOutputSignals();
        for(int i = 0; i < outSignals.size(); ++i) {
            if(outSignals.get(i).equals(vertex)) {
                array.add(i);
            }
        }
        return array;
    }

    private int getIndexByStateName(Integer vertex)
    {
        for(Integer _item : mMoore.getStates()) {
            if(_item.equals(vertex))
                return mMoore.getStates().indexOf(_item);
        }
        return -1;
    }
}
