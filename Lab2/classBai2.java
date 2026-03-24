import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class classBai2 {

    public static void main(String[] args) {
        String inputFile = "hotel-review-clean.csv";
        String finalFile = "hotel-review-final.csv";
        String statisticFile = "thong-ke-bai2.txt";

        Map<String, Integer> wordFreq = new HashMap<>();
        Map<String, Integer> categoryCount = new HashMap<>();
        Map<String, Integer> aspectCount = new HashMap<>();

        int total = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8));
             BufferedWriter finalWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(finalFile), StandardCharsets.UTF_8));
             BufferedWriter statWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(statisticFile), StandardCharsets.UTF_8))) {

            // Header cho file final
            finalWriter.write("binh_luan;category;aspect;sentiment");
            finalWriter.newLine();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] cols = line.split(";", -1);
                if (cols.length < 5) continue;

                String review = cols[1].trim();
                String category = cols[2].trim().toUpperCase();
                String aspect = cols[3].trim().toUpperCase();
                String sentiment = cols[4].trim().toLowerCase();

                if (!sentiment.equals("positive") && !sentiment.equals("negative")) {
                    sentiment = "neutral";
                }

                total++;

                // Thống kê từ
                if (!review.isEmpty()) {
                    for (String w : review.split("\\s+")) {
                        if (!w.isEmpty()) {
                            wordFreq.put(w, wordFreq.getOrDefault(w, 0) + 1);
                        }
                    }
                }

                // Thống kê category & aspect
                categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
                aspectCount.put(aspect, aspectCount.getOrDefault(aspect, 0) + 1);

                // Ghi vào file final
                finalWriter.write(String.join(";", review, category, aspect, sentiment));
                finalWriter.newLine();
            }

            // ====================== IN THỐNG KÊ RA CONSOLE ======================
            System.out.println("==================================================");
            System.out.println("               BÀI 2 - THỐNG KÊ HOÀN THÀNH");
            System.out.println("==================================================\n");
            System.out.println("Tổng số bình luận được xử lý: " + total + "\n");

            // 1. Top 5 từ phổ biến nhất
            System.out.println("1. TOP 5 TỪ XUẤT HIỆN NHIỀU NHẤT:");
            printTop5(wordFreq, System.out);

            // 2. Theo Category
            System.out.println("\n2. SỐ BÌNH LUẬN THEO TỪNG PHÂN LOẠI (CATEGORY):");
            printSortedMap(categoryCount, System.out);

            // 3. Theo Aspect
            System.out.println("\n3. SỐ BÌNH LUẬN THEO TỪNG KHÍA CẠNH (ASPECT):");
            printSortedMap(aspectCount, System.out);

            // ====================== GHI FILE THỐNG KÊ ======================
            statWriter.write("BÁO CÁO THỐNG KÊ - BÀI 2\n");
            statWriter.write("Tổng số bình luận: " + total + "\n\n");

            statWriter.write("1. TOP 5 TỪ XUẤT HIỆN NHIỀU NHẤT:\n");
            writeTop5ToFile(wordFreq, statWriter);

            statWriter.write("\n2. THỐNG KÊ THEO CATEGORY:\n");
            writeMapToFile(categoryCount, statWriter);

            statWriter.write("\n3. THỐNG KÊ THEO ASPECT:\n");
            writeMapToFile(aspectCount, statWriter);

            System.out.println("\nĐã xuất file thống kê: " + statisticFile);
            System.out.println("Đã tạo file dữ liệu sạch: " + finalFile);

        } catch (IOException e) {
            System.err.println("Lỗi xử lý file!");
            e.printStackTrace();
        }
    }

    // In Top 5 ra console
    private static void printTop5(Map<String, Integer> map, PrintStream out) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        for (int i = 0; i < Math.min(5, list.size()); i++) {
            out.printf("   %d. %-20s : %d lần\n", i + 1, 
                       list.get(i).getKey(), list.get(i).getValue());
        }
    }

    // In map đã sắp xếp ra console
    private static void printSortedMap(Map<String, Integer> map, PrintStream out) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        for (Map.Entry<String, Integer> e : list) {
            out.printf("   %-25s : %d bình luận\n", e.getKey(), e.getValue());
        }
    }

    // Ghi Top 5 vào file
    private static void writeTop5ToFile(Map<String, Integer> map, BufferedWriter bw) throws IOException {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        for (int i = 0; i < Math.min(5, list.size()); i++) {
            bw.write(String.format("   %d. %-20s : %d lần\n", i + 1,
                    list.get(i).getKey(), list.get(i).getValue()));
        }
    }

    // Ghi map vào file
    private static void writeMapToFile(Map<String, Integer> map, BufferedWriter bw) throws IOException {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        for (Map.Entry<String, Integer> e : list) {
            bw.write(String.format("   %-25s : %d bình luận\n", e.getKey(), e.getValue()));
        }
    }
}