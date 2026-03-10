import java.io.*;
import java.util.*;

public class Bai1 {
    public static void main(String[] args) throws Exception {
        Map<String, List<Double>> ratings = new HashMap<>();
        Map<String, String> movies = new HashMap<>();

        // đọc danh sách phim
        readMovies("movies.txt", movies);

        // đọc ratings từ 2 file
        readRatings("ratings_1.txt", ratings);
        readRatings("ratings_2.txt", ratings);

        String maxMovie = "";
        double maxRating = 0.0;

        for (String movieId : ratings.keySet()) {
            List<Double> list = ratings.get(movieId);
            int count = list.size();
            double sum = 0;
            for (double r : list) sum += r;
            double avg = sum / count;

            String title = movies.getOrDefault(movieId, movieId);
            System.out.println(title + " Average rating: " + avg + " (Total ratings: " + count + ")");

            if (count >= 5 && avg > maxRating) {
                maxRating = avg;
                maxMovie = title;
            }
        }

        if (!maxMovie.isEmpty()) {
            System.out.println(maxMovie + " is the highest rated movie with an average rating of "
                    + maxRating + " among movies with at least 5 ratings");
        }
    }

    private static void readMovies(String filename, Map<String, String> movies) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            if (fields.length >= 2) {
                String movieId = fields[0].trim();
                String title = fields[1].trim();
                movies.put(movieId, title);
            }
        }
        br.close();
    }

    private static void readRatings(String filename, Map<String, List<Double>> ratings) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            if (fields.length >= 3) {
                String movieId = fields[1].trim();
                double rating = Double.parseDouble(fields[2].trim());
                ratings.putIfAbsent(movieId, new ArrayList<>());
                ratings.get(movieId).add(rating);
            }
        }
        br.close();
    }
}
