import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class classBai1 {

    public static void main(String[] args) {
        String inputFile = "hotel-review.csv";
        String stopwordsFile = "stopwords.txt";
        String outputFile = "hotel-review-clean.csv";

        // Đọc danh sách stop words
        Set<String> stopwords = loadStopwords(stopwordsFile);

        int totalLines = 0;
        int processedLines = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8));
             BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                totalLines++;

                // Tách các cột theo dấu ;
                String[] columns = line.split(";", -1); // -1 để giữ cột rỗng

                if (columns.length < 5) {
                    // Nếu dòng không đủ cột, ghi lại nguyên bản
                    bw.write(line);
                    bw.newLine();
                    continue;
                }

                // Chỉ xử lý cột review (cột thứ 2 - index 1)
                String review = columns[1].trim();

                // Bước 1: Chuyển về chữ thường
                review = review.toLowerCase();

                // Bước 2: Tách thành các từ (theo khoảng trắng)
                String[] words = review.split("\\s+");

                // Bước 3: Loại bỏ stop words và từ rỗng
                List<String> cleanedWords = new ArrayList<>();
                for (String word : words) {
                    word = word.trim();
                    if (!word.isEmpty() && !stopwords.contains(word)) {
                        cleanedWords.add(word);
                    }
                }

                // Ghép lại thành review đã sạch
                String cleanedReview = String.join(" ", cleanedWords);

                // Thay thế cột review bằng nội dung đã xử lý
                columns[1] = cleanedReview;

                // Ghép lại thành dòng CSV mới
                String outputLine = String.join(";", columns);

                bw.write(outputLine);
                bw.newLine();

                processedLines++;

                // In tiến độ mỗi 1000 dòng
                if (processedLines % 1000 == 0) {
                    System.out.println("Đã xử lý: " + processedLines + " dòng...");
                }
            }

            System.out.println("\n=== HOÀN THÀNH BÀI 1 ===");
            System.out.println("Tổng số dòng gốc      : " + totalLines);
            System.out.println("Số dòng đã xử lý      : " + processedLines);
            System.out.println("File kết quả          : " + outputFile);

        } catch (IOException e) {
            System.err.println("Lỗi khi đọc/ghi file!");
            e.printStackTrace();
        }
    }

    // Hàm đọc file stopwords.txt
    private static Set<String> loadStopwords(String filePath) {
        Set<String> stopwords = new HashSet<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            String word;
            while ((word = br.readLine()) != null) {
                word = word.trim().toLowerCase();
                if (!word.isEmpty()) {
                    stopwords.add(word);
                }
            }
        } catch (IOException e) {
            System.err.println("Không thể đọc file stopwords: " + filePath);
        }

        return stopwords;
    }
}