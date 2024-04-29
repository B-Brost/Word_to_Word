import java.util.*;
import java.nio.file.*;
import java.io.IOException;

public class WordToWord {
    private static HashMap<String, ArrayList<Pair>> graph = new HashMap<>();

    public static void main(String[] args) throws IOException {
        // Load the dictionary into a set for fast lookup
        HashSet<String> dictionary = new HashSet<>(Files.readAllLines(Paths.get("Dict.txt")));

        // Build the graph
        for (String word : dictionary) {
            for (int i = 0; i < word.length(); i++) {
                for (char c = 'a'; c <= 'z'; c++) {
                    String next = word.substring(0, i) + c + word.substring(i + 1);
                    if (dictionary.contains(next)) {
                        int weight = Math.abs(c - word.charAt(i));
                        graph.computeIfAbsent(word, k -> new ArrayList<>()).add(new Pair(weight, next));
                    }
                }
            }
        }

        // User interaction
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Input '1' to exit the program.");
            System.out.println("Input start word: ");
            String start = scanner.nextLine();
            if (start.equals("1")) {
                System.out.println("Exiting the Program.");
                break;
            }
            System.out.println("Input target word: ");
            String end = scanner.nextLine();
            if (end.equals("1")) {
                System.out.println("Exiting the Program.");
                break;
            }
            if (start.length() != end.length()) {
                System.out.println("The two words must be the same length.");
                continue;
            }
            Pair result = shortestPath(start.toLowerCase(), end.toLowerCase());
            if (result.weight == Integer.MAX_VALUE) {
                System.out.println("No path exists.");
            } else {
                System.out.println("The shortest path is: " + String.join(" > ", result.path));
                System.out.println("The length of the path is: " + result.weight);
            }
        }
    }

    private static Pair shortestPath(String start, String end) {
        PriorityQueue<Pair> heap = new PriorityQueue<>();
        heap.add(new Pair(0, start));
        HashMap<String, Integer> distances = new HashMap<>();
        distances.put(start, 0);
        HashMap<String, ArrayList<String>> paths = new HashMap<>();
        paths.put(start, new ArrayList<>(Collections.singletonList(start)));
        while (!heap.isEmpty()) {
            Pair current = heap.poll();
            if (current.node.equals(end)) {
                return new Pair(distances.get(end), paths.get(end));
            }
            for (Pair edge : graph.getOrDefault(current.node, new ArrayList<>())) {
                int newDist = current.weight + edge.weight;
                if (!distances.containsKey(edge.node) || newDist < distances.get(edge.node)) {
                    distances.put(edge.node, newDist);
                    ArrayList<String> newPath = new ArrayList<>(paths.get(current.node));
                    newPath.add(edge.node);
                    paths.put(edge.node, newPath);
                    heap.add(new Pair(newDist, edge.node));
                }
            }
        }
        return new Pair(Integer.MAX_VALUE, new ArrayList<>());
    }

    private static class Pair implements Comparable<Pair> {
        int weight;
        String node;
        ArrayList<String> path;

        Pair(int weight, String node) {
            this.weight = weight;
            this.node = node;
        }

        Pair(int weight, ArrayList<String> path) {
            this.weight = weight;
            this.path = path;
        }

        @Override
        public int compareTo(Pair other) {
            return Integer.compare(this.weight, other.weight);
        }
    }
}
