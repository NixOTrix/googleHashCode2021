import java.util.*;
import java.io.*;

public class Solution {
    public static void main(String[] args) {
        solve("./a.txt", "./a_solution.txt");
        solve("./b.txt", "./b_solution.txt");
        solve("./c.txt", "./c_solution.txt");
        solve("./d.txt", "./d_solution.txt");
        solve("./e.txt", "./e_solution.txt");
        solve("./f.txt", "./f_solution.txt");
    }

    public static void solve(String inPath, String outPath) {
        try (Scanner in = new Scanner(new BufferedReader(new FileReader(inPath)))) {
            int d = in.nextInt();
            int i = in.nextInt();
            int s = in.nextInt();
            int v = in.nextInt();
            int f = in.nextInt();
            int[][] adj = new int[i][i];
            Map<String, List<Integer>> map = new HashMap<>();
            for (int j = 0; j < s; j++) {
                int b = in.nextInt();
                int e = in.nextInt();
                String n = in.next();
                int l = in.nextInt();
                adj[b][e] = l;
                List<Integer> pos = new ArrayList<>();
                pos.add(b);
                pos.add(e);
                map.put(n, pos);
            }
            String[][] cars = new String[v][];
            for (int j = 0; j < v; j++) {
                int p = in.nextInt();
                cars[j] = new String[p];
                for (int k = 0; k < p; k++) {
                    cars[j][k] = in.next();
                }
            }
            // solution
            Arrays.sort(cars, Comparator.comparingInt((String[] arr) -> arr.length));
            Map<String, Integer> count = new HashMap<>();
            for (int j = 0; j < (int) ((double) cars.length / 2.5); j++) {
                for (String street : cars[j]) {
                    count.put(street, count.getOrDefault(street, 0) + 1);
                }
            }
            Map<String, Integer> streetOrdering = new HashMap<>();
            for (String[] car : cars) {
                streetOrdering.put(car[0], streetOrdering.getOrDefault(car[0], 0) + 1);
            }
            Map<Integer, List<OutputPair>> output = new HashMap<>();
            for (Map.Entry<String, List<Integer>> entry : map.entrySet()) {
                String streetName = entry.getKey();
                int dest = entry.getValue().get(1);
                int value = count.getOrDefault(streetName, 0);
                if (value == 0)
                    continue;
                output.putIfAbsent(dest, new ArrayList<>());
                output.get(dest).add(new OutputPair(streetName, value));
            }
            try (BufferedWriter out = new BufferedWriter(new FileWriter(outPath))) {
                out.append(String.valueOf(output.size())).append("\n");
                for (Map.Entry<Integer, List<OutputPair>> entry : output.entrySet()) {
                    int dest = entry.getKey();
                    List<OutputPair> list = entry.getValue();
                    int total = 0;
                    for (OutputPair pair : list)
                        total += pair.duration;
                    double average = (double) total / list.size();
                    List<OutputPair> filtered = new ArrayList<>();
                    for (OutputPair pair : list)
                        filtered.add(pair);
                    int min = Integer.MAX_VALUE;
                    for (OutputPair pair : filtered)
                        min = Math.min(min, pair.duration);
                    for (OutputPair pair : filtered)
                        pair.duration = pair.duration / min;
                    filtered.sort(Comparator
                            .comparingInt((OutputPair pair) -> streetOrdering.getOrDefault(pair.street, 0)).reversed());
                    out.append(String.valueOf(dest)).append("\n");
                    out.append(String.valueOf(filtered.size())).append("\n");
                    for (OutputPair pair : filtered) {
                        out.append(pair.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class OutputPair {
    String street;
    int duration;

    OutputPair(String street, int duration) {
        this.street = street;
        this.duration = duration;
    }

    public String toString() {
        return street + " " + duration + "\n";
    }
}