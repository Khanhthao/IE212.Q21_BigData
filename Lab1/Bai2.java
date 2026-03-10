import java.io.*;
import java.util.*;

public class Bai2 {
    public static void main(String[] args) throws Exception {
        Map<String, String> movies = new HashMap<>();
        Map<String, List<Double>> ratings = new HashMap<>();

        // đọc danh sách phim (id -> genres)
        readMovies("movies.txt", movies);

        // đọc ratings từ 2 file
        readRatings("ratings_1.txt", ratings);
        readRatings("ratings_2.txt", ratings);

        // Map lưu genre -> danh sách rating
        Map<String, List<Double>> genreRatings = new HashMap<>();

        for (String movieId : ratings.keySet()) {
            List<Double> list = ratings.get(movieId);
            String genres = movies.get(movieId);
            if (genres == null) continue;

            String[] genreList = genres.split("\\|");
            for (String g : genreList) {
                g = g.trim();
                genreRatings.putIfAbsent(g, new ArrayList<>());
                genreRatings.get(g).addAll(list);
            }
        }

        // In kết quả
        for (String genre : genreRatings.keySet()) {
            List<Double> list = genreRatings.get(genre);
            int count = list.size();
            double sum = 0;
            for (double r : list) sum += r;
            double avg = sum / count;

            System.out.println(genre + ": AverageRating = " + String.format("%.2f", avg) +
                               " (TotalRatings: " + count + ")");
        }
    }

    private static void readMovies(String filename, Map<String, String> movies) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            if (fields.length >= 3) {
                String movieId = fields[0].trim();
                String genres = fields[2].trim();
                movies.put(movieId, genres);
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
