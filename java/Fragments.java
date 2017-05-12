import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Fragments {
    public static void main(String[] args) {
        try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
            String fragmentProblem;
            while ((fragmentProblem = in.readLine()) != null) {
                System.out.println(reassemble(fragmentProblem));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String reassemble(String fragmentProblem) {
        //Put the Fragments into a list
        String[] fragments = fragmentProblem.split(";");
        List<String> list = new ArrayList<>(Arrays.asList(fragments));

        orderList(list);

        // This is a recursive way to 'shrink' the list by merging pairs
        while (list.size() > 1) {
            list = mergeFragments(list);
        }

        //Get the first item in the list
        return list.get(0);

    }

    private static void orderList(List<String> list) {
        // order the list, decrease on string length
        Collections.sort(list, (s1, s2) -> s2.length() - s1.length()
        );
    }

    private static List<String> mergeFragments(List<String> list) {
        // test the overlap, find the maximum overlap, merge it into base
        int max_overlap = 0; //current max overlap length
        int merge_from = 0;
        int merge_to = 0; //indexes of the pair in the list with max overlap

        int index = 0; //the position where the overlap ends in the current text
        int m = 0, n = 0;

        for (int i = 0; i <= list.size() - 1; i++) {
            String current_text = list.get(i);
            m = current_text.length();
            for (int j = i + 1; j <= list.size() - 1; j++) {
                String match_text = list.get(j);
                n = match_text.length();
                for (int k = 1 - match_text.length(); k < current_text.length(); k++) {
                    if (k < 0) { // prefix
                        int len = n + k;
                        if (current_text.regionMatches(0, match_text, -k, len)) { //test overlap
                            if (len > max_overlap) {
                                index = k;
                                max_overlap = len;
                                merge_to = i;
                                merge_from = j;
                            }
                        }
                    } else { // suffix
                        int len = k + n <= m ? n : m - k;
                        // start with the full length of current text and move down the base string
                        if (current_text.regionMatches(k, match_text, 0, len)) { //test overlap
                            if (len > max_overlap) {
                                index = k;
                                max_overlap = len;
                                merge_to = i;
                                merge_from = j;
                            }
                        }
                    }
                }
            }
        }

        if (merge_from == merge_to) {
            //in case there are more items in the list but no matchings
            //Get the longest item in the list
            orderList(list);
            return list.subList(0, 1);
        }

        //merge the pair
        if (index <= 0) { // prefix
            list.set(merge_to, list.get(merge_from).substring(0, -index) + list.get(merge_to));
        } else if ((index > list.get(merge_to).length() - list.get(merge_from).length())) { // suffix
            list.set(merge_to, list.get(merge_to).substring(0, index) + list.get(merge_from));
        }

        //update the list
        list.remove(merge_from);
        return list;
    }
}
