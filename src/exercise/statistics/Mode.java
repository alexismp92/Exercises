package src.exercise.statistics;

/* Given a list of numbers, find the mode of the elements.
    The mode is the number that appears most frequently in the list.
    If there are multiple modes, return the min value of them.
    For example, given the list [5, 8, 7, 7, 9, 9], the element is 7.
    If the list is empty, return 0.
    If the list has only one element, return that element as the mode.
 */

import java.util.HashMap;

public class Mode {

    private int[][] numbers = {{5,8,7,7,9,9}, {1,2,3,4}, {1,2,2}, {}, {7}};

    public static void main(String[] args) {
        Mode mode = new Mode();

        for(int i = 0; i < mode.numbers.length; i++) {
            System.out.println("The mode of the list " + (i + 1) + " is: ");
            var frequencyMap = mode.getMode(mode.numbers[i]);
            var maxFrequency = mode.getMaxFrequency(frequencyMap);
            var minValue = mode.getMinValue(maxFrequency);
            System.out.println("The element is: " + minValue);
        }
    }

    public HashMap<Integer,Integer> getMode(int[] numbers) {
        HashMap<Integer, Integer> frequencyMap = new HashMap<>();
        for (int number : numbers) {
            frequencyMap.put(number, frequencyMap.getOrDefault(number, 0) + 1);
        }
        System.out.println("Mode: " + frequencyMap);
        return frequencyMap;
    }

    public HashMap getMaxFrequency(HashMap<Integer, Integer> frequencyMap) {
       HashMap<Integer, Integer> maxFrequency = new HashMap<>();

       var frequencyValue = frequencyMap.values().stream()
               .max(Integer::compareTo)
               .orElse(0);
        System.out.println("Max Frequency value: " + frequencyValue);

       frequencyMap.entrySet().stream()
               .filter(entry -> entry.getValue() == frequencyValue)
               .forEach(entry -> maxFrequency.put(entry.getKey(), entry.getValue()));
        return maxFrequency;
    }

    public int getMinValue(HashMap<Integer, Integer> modeMap) {

        var minElement = modeMap.keySet().stream()
                .min(Integer::compareTo)
                .orElse(0);

        System.out.println("Min value: " + minElement);
        return minElement;
    }



}
