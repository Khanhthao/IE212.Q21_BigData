import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class classBai3 {

    public static void main(String[] args) {
        String inputFile = "hotel-review-final.csv";
        String outputFile = "ket-qua-bai3.txt";

        Map<String, Integer> positiveCount = new HashMap<>();
        Map<String, Integer> negativeCount = new HashMap<>();

        int totalPositive = 0;
        int totalNegative = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8));
             BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {

            // Bỏ qua header
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] columns = line.split(";", -1);
                if (columns.length < 4) continue;

                String aspect = columns[2].trim().toUpperCase();
                String sentiment = columns[3].trim().toLowerCase();

                if (sentiment.equals("positive")) {
                    positiveCount.put(aspect, positiveCount.getOrDefault(aspect, 0) + 1);
                    totalPositive++;
                } else if (sentiment.equals("negative")) {
                    negativeCount.put(aspect, negativeCount.getOrDefault(aspect, 0) + 1);
                    totalNegative++;
                }
            }

            // Tìm khía cạnh negative nhiều nhất
            String worstAspect = "";
            int maxNegative = -1;
            for (Map.Entry<String, Integer> e : negativeCount.entrySet()) {
                if (e.getValue() > maxNegative) {
                    maxNegative = e.getValue();
                    worstAspect = e.getKey();
                }
            }

            // Tìm khía cạnh positive nhiều nhất
            String bestAspect = "";
            int maxPositive = -1;
            for (Map.Entry<String, Integer> e : positiveCount.entrySet()) {
                if (e.getValue() > maxPositive) {
                    maxPositive = e.getValue();
                    bestAspect = e.getKey();
                }
            }

            // ====================== GHI FILE KẾT QUẢ ======================
            bw.write("BÁO CÁO BÀI 3 - PHÂN TÍCH KHÍA CẠNH ĐÁNH GIÁ\n");
            bw.write("==================================================\n\n");
            bw.write("Tổng số đánh giá tích cực   : " + totalPositive + "\n");
            bw.write("Tổng số đánh giá tiêu cực   : " + totalNegative + "\n\n");

            bw.write("1. KHÍA CẠNH NHẬN NHIỀU ĐÁNH GIÁ TIÊU CỰC NHẤT:\n");
            bw.write(String.format("   → %-25s : %d đánh giá negative\n\n", worstAspect, maxNegative));

            bw.write("2. KHÍA CẠNH NHẬN NHIỀU ĐÁNH GIÁ TÍCH CỰC NHẤT:\n");
            bw.write(String.format("   → %-25s : %d đánh giá positive\n\n", bestAspect, maxPositive));

            // Chi tiết tất cả khía cạnh
            bw.write("3. CHI TIẾT THEO TỪNG KHÍA CẠNH:\n");
            bw.write("--------------------------------------------------\n");

            Set<String> allAspects = new HashSet<>();
            allAspects.addAll(positiveCount.keySet());
            allAspects.addAll(negativeCount.keySet());

            List<String> sortedAspects = new ArrayList<>(allAspects);
            Collections.sort(sortedAspects);

            for (String aspect : sortedAspects) {
                int pos = positiveCount.getOrDefault(aspect, 0);
                int neg = negativeCount.getOrDefault(aspect, 0);
                bw.write(String.format("   %-25s | Positive: %4d | Negative: %4d\n", aspect, pos, neg));
            }

            System.out.println("==================================================");
            System.out.println("               BÀI 3 - HOÀN THÀNH");
            System.out.println("==================================================");
            System.out.println("File kết quả đã được xuất ra: " + outputFile);
            System.out.println("Mở file ket-qua-bai3.txt để xem chi tiết.");

        } catch (IOException e) {
            System.err.println("Lỗi khi xử lý file!");
            e.printStackTrace();
        }
    }
}
