import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    static class Pair<F, S> {
        public final F first;
        public final S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }
    }
    
    public static long decodeBase(String s, int b) {
        long num = 0;
        for (int idx = 0; idx < s.length(); ++idx) {
            char ch = s.charAt(idx);
            num *= b;
            if (Character.isDigit(ch)) num += ch - '0';
            else if (Character.isAlphabetic(ch)) num += Character.toUpperCase(ch) - 'A' + 10;
        }
        return num;
    }

    public static long lagrangeZero(List<Pair<Long, Long>> arr) {
        double res = 0.0;
        int sz = arr.size();
        for (int a = 0; a < sz; ++a) {
            double prod = arr.get(a).second;
            for (int b = 0; b < sz; ++b) {
                if (a == b) continue;
                prod *= -arr.get(b).first;
                prod /= (arr.get(a).first - arr.get(b).first);
            }
            res += prod;
        }
        return Math.round(res);
    }

    public static void parseInput(String fname, int[] k, List<Pair<Long, Long>> arr) throws IOException {
        BufferedReader fin = new BufferedReader(new FileReader(fname));
        String ln;
        int n = 0;
        Map<Long, Pair<Integer, String>> temp = new HashMap<>();
        while ((ln = fin.readLine()) != null) {
            ln = ln.replaceAll("\\s+", "");
            if (ln.contains("\"n\"")) {
                n = Integer.parseInt(ln.substring(ln.indexOf(":") + 1).replace(",", ""));
            } else if (ln.contains("\"k\"")) {
                k[0] = Integer.parseInt(ln.substring(ln.indexOf(":") + 1).replace(",", ""));
            } else {
                int p = ln.indexOf('"');
                if (p != -1 && Character.isDigit(ln.charAt(p + 1))) {
                    long xval = Long.parseLong(ln.substring(p + 1, ln.indexOf('"', p + 1)));
                    String bl = fin.readLine();
                    String vl = fin.readLine();
                    bl = bl.replaceAll("\\s+", "");
                    vl = vl.replaceAll("\\s+", "");
                    int base = Integer.parseInt(
    bl.substring(bl.indexOf(":\"") + 2)
      .replace(",", "")
      .replace("\"", "")
);
                    String val = vl.substring(vl.indexOf(":\"") + 2);
                    if (!val.isEmpty() && val.charAt(val.length() - 1) == '"') val = val.substring(0, val.length() - 1);
                    if (!val.isEmpty() && val.charAt(val.length() - 1) == ',') val = val.substring(0, val.length() - 1);
                    temp.put(xval, new Pair<>(base, val));
                }
            }
        }
        for (Map.Entry<Long, Pair<Integer, String>> entry : temp.entrySet()) {
            arr.add(new Pair<>(entry.getKey(), decodeBase(entry.getValue().second, entry.getValue().first)));
        }
        fin.close();
    }

    public static void main(String[] args) throws IOException {
        String[] testfiles = {"firstcase.json", "second.json"};
        for (int t = 0; t < 2; ++t) {
            int[] k = new int[1];
            List<Pair<Long, Long>> pts = new ArrayList<>();
            parseInput(testfiles[t], k, pts);
            List<Pair<Long, Long>> subset = pts.subList(0, k[0]);
            long secret = lagrangeZero(subset);
            System.out.println("Secret for testcase " + (t + 1) + ": " + secret);
        }
    }
}