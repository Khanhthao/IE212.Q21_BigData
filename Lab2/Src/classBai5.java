import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class classBai5 {

    public static void main(String[] args) {
        String inputFile = "hotel-review-final.csv";
        String outputFile = "ket-qua-bai5.txt";

        // Map: category → (word → frequency)
        Map<String, Map<String, Integer>> wordsByCategory = new HashMap<>();

        int totalReviews = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8));
             BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {

            // Bỏ qua header
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] cols = line.split(";", -1);
                if (cols.length < 4) continue;

                String review = cols[0].trim();
                String category = cols[1].trim().toUpperCase();

                totalReviews++;

                if (review.isEmpty()) continue;

                // Tách từ và thống kê theo category
                wordsByCategory.putIfAbsent(category, new HashMap<>());
                Map<String, Integer> wordMap = wordsByCategory.get(category);

                String[] words = review.split("\\s+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
                    }
                }
            }

            // ====================== GHI KẾT QUẢ ======================
            bw.write("BÁO CÁO BÀI 5 - 5 TỪ LIÊN QUAN NHẤT THEO TỪNG PHÂN LOẠI\n");
            bw.write("================================================================\n\n");
            bw.write("Tổng số bình luận được phân tích: " + totalReviews + "\n\n");

            System.out.println("==================================================");
            System.out.println("               BÀI 5 - HOÀN THÀNH");
            System.out.println("==================================================\n");
            System.out.println("Tổng số bình luận: " + totalReviews + "\n");

            List<String> sortedCategories = new ArrayList<>(wordsByCategory.keySet());
            Collections.sort(sortedCategories);

            for (String category : sortedCategories) {
                Map<String, Integer> freqMap = wordsByCategory.get(category);

                bw.write("PHÂN LOẠI: " + category + "\n");
                bw.write("--------------------------------------------------\n");
                bw.write("5 từ liên quan nhất:\n");

                System.out.println("PHÂN LOẠI: " + category);
                System.out.println("   5 từ liên quan nhất:");

                // Sắp xếp giảm dần theo tần suất
                List<Map.Entry<String, Integer>> topWords = new ArrayList<>(freqMap.entrySet());
                topWords.sort((a, b) -> b.getValue().compareTo(a.getValue()));

                for (int i = 0; i < Math.min(5, topWords.size()); i++) {
                    String result = String.format("   %d. %-20s : %d lần\n", 
                        i + 1, topWords.get(i).getKey(), topWords.get(i).getValue());
                    
                    bw.write(result);
                    System.out.print(result);
                }

                bw.write("\n");
                System.out.println();
            }

            System.out.println("Đã xuất kết quả ra file: " + outputFile);

        } catch (IOException e) {
            System.err.println("Lỗi khi xử lý file!");
            e.printStackTrace();
        }
    }
}
