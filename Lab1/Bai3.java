import java.io.*;
import java.util.*;

public class Bai3 {
    public static void main(String[] args) throws Exception {
        Map<String, String> movieTitles = new HashMap<>();
        Map<String, String> userGenders = new HashMap<>();
        Map<String, List<Double>> maleRatings = new HashMap<>();
        Map<String, List<Double>> femaleRatings = new HashMap<>();

        readMovies("movies.txt", movieTitles);
        readUsers("users.txt", userGenders);
        readRatings("ratings_1.txt", userGenders, maleRatings, femaleRatings);
        readRatings("ratings_2.txt", userGenders, maleRatings, femaleRatings);

        for (String movieId : movieTitles.keySet()) {
            String title = movieTitles.get(movieId);

            List<Double> maleList = maleRatings.getOrDefault(movieId, new ArrayList<>());
            List<Double> femaleList = femaleRatings.getOrDefault(movieId, new ArrayList<>());

            double maleAvg = maleList.isEmpty() ? 0.0 : avg(maleList);
            double femaleAvg = femaleList.isEmpty() ? 0.0 : avg(femaleList);

            System.out.println(title + ": Male = " + String.format("%.2f", maleAvg) +
                               ", Female = " + String.format("%.2f", femaleAvg));
        }
    }

    private static double avg(List<Double> list) {
        double sum = 0;
        for (double r : list) sum += r;
        return sum / list.size();
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

    private static void readUsers(String filename, Map<String, String> userGenders) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            if (fields.length >= 2) {
                String userId = fields[0].trim();
                String gender = fields[1].trim();
                userGenders.put(userId, gender);
            }
        }
        br.close();
    }

    private static void readRatings(String filename, Map<String, String> userGenders,
                                    Map<String, List<Double>> maleRatings,
                                    Map<String, List<Double>> femaleRatings) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            if (fields.length >= 3) {
                String userId = fields[0].trim();
                String movieId = fields[1].trim();
                double rating = Double.parseDouble(fields[2].trim());

                String gender = userGenders.get(userId);
                if (gender == null) continue;

                if (gender.equalsIgnoreCase("M")) {
                    maleRatings.putIfAbsent(movieId, new ArrayList<>());
                    maleRatings.get(movieId).add(rating);
                } else if (gender.equalsIgnoreCase("F")) {
                    femaleRatings.putIfAbsent(movieId, new ArrayList<>());
                    femaleRatings.get(movieId).add(rating);
                }
            }
        }
        br.close();
    }
}

