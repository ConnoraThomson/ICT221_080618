package au.edu.usc.bict_explorer.helpers;

import au.edu.usc.bict_explorer.gui.BICTExplorer;
import au.edu.usc.bict_explorer.rules.Option;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
public class DegreeHelper {


    /**
     * Deselects every Career, Minor and Course.
     */
    public static void clearAllSelections() {


        Map<String, Option> map;

        map = BICTExplorer.getDegree().courses();
        for (String key : map.keySet()) {
            Option value = map.get(key);
            value.setChosen(false);

        }

        map = BICTExplorer.getDegree().minors();
        for (String key : map.keySet()) {
            Option value = map.get(key);
            value.setChosen(false);

        }

        map = BICTExplorer.getDegree().careers();
        for (String key : map.keySet()) {
            Option value = map.get(key);
            value.setChosen(false);

        }

    }

    /**
     * returns the amount of courses that have been selected
     * @return
     */

    public static int getSelectedCourseCount() {

        int count = 0;
        Map<String, Option> map;
        map = BICTExplorer.getDegree().courses();
        for (String key : map.keySet()) {

            if (map.get(key).isChosen()) {
                count++;
            }

        }
        return count;


    }

    /**
     * retuns the courses that have been selected
     * @return
     */
    public static Map<String, Option> getSelectedCourses() {

        Map<String, Option> returnMap = new LinkedHashMap<>();

        Map<String, Option> map;
        map = BICTExplorer.getDegree().courses();
        for (String key : map.keySet()) {

            if (map.get(key).isChosen()) {
                returnMap.put(key,map.get(key));
            }

        }

        return returnMap;

    }


    public static Map<String, Option> getAlphabeticalSortedMap(Map<String, Option> map) {

        Map<String, Option> returnMap = new LinkedHashMap<>();

        List<String> codeList = new ArrayList<String>();

        for (String key : map.keySet()) {
            codeList.add(key);
        }

        java.util.Collections.sort(codeList);

        for (String key : codeList) {
            returnMap.put(key, map.get(key));
        }

        return returnMap;

    }

}
