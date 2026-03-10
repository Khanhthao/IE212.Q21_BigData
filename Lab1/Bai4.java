import java.io.*;
import java.util.*;

public class Bai4 {
    public static void main(String[] args) throws Exception {
        Map<String, String> movieTitles = new HashMap<>();
        Map<String, Integer> userAges = new HashMap<>();
        Map<String, Map<String, List<Double>>> ageGroupRatings = new HashMap<>();

        readMovies("movies.txt", movieTitles);
        readUsers("users.txt", userAges);
        readRatings("ratings_1.txt", userAges, ageGroupRatings);
        readRatings("ratings_2.txt", userAges, ageGroupRatings);

        String[] ageGroups = {"0-18", "18-35", "35-50", "50+"};

        for (String movieId : movieTitles.keySet()) {
            String title = movieTitles.get(movieId);
            Map<String, List<Double>> groupMap = ageGroupRatings.getOrDefault(movieId, new HashMap<>());

            System.out.print(title + ": ");
            for (String group : ageGroups) {
                List<Double> list = groupMap.getOrDefault(group, new ArrayList<>());
                String avg = list.isEmpty() ? "NA" : String.format("%.2f", average(list));
                System.out.print(group + ": " + avg + "  ");
            }
            System.out.println();
        }
    }

    private static double average(List<Double> list) {
        double sum = 0;
        for (double r : list) sum += r;
        return sum / list.size();
    }

    private static String getAgeGroup(int age) {
        if (age <= 18) return "0-18";
        else if (age <= 35) return "18-35";
        else if (age <= 50) return "35-50";
        else return "50+";
    }

    private static void readMovies(String filename, Map<String, String> movieTitles) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            if (fields.length >= 2) {
                String movieId = fields[0].trim();
                String title = fields[1].trim();
                movieTitles.put(movieId, title);
            }
        }
        br.close();
    }

    private static void readUsers(String filename, Map<String, Integer> userAges) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            if (fields.length >= 3) {
                String userId = fields[0].trim();
                int age = Integer.parseInt(fields[2].trim());
                userAges.put(userId, age);
            }
        }
        br.close();
    }

    private static void readRatings(String filename, Map<String, Integer> userAges,
                                    Map<String, Map<String, List<Double>>> ageGroupRatings) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            if (fields.length >= 3) {
                String userId = fields[0].trim();
                String movieId = fields[1].trim();
                double rating = Double.parseDouble(fields[2].trim());

                Integer age = userAges.get(userId);
                if (age == null) continue;

                String group = getAgeGroup(age);
                ageGroupRatings.putIfAbsent(movieId, new HashMap<>());
                Map<String, List<Double>> groupMap = ageGroupRatings.get(movieId);
                groupMap.putIfAbsent(group, new ArrayList<>());
                groupMap.get(group).add(rating);
            }
        }
        br.close();
    }
}
