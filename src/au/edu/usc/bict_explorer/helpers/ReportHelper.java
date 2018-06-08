package au.edu.usc.bict_explorer.helpers;
import au.edu.usc.bict_explorer.gui.BICTExplorer;

import au.edu.usc.bict_explorer.rules.Option;
import au.edu.usc.bict_explorer.rules.Course;

import java.awt.Desktop;
import java.io.*;
import java.util.Map;

public class ReportHelper {

    /**
     * This outputs a report
     * @param outputFile
     * @throws Exception
     */
    public static void WriteReport(File outputFile) throws Exception {

        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));

        String cssString = "<style>" +
                "body {font-family: Arial, Helvetica, sans-serif}" +
                "table {}" +
                "</style>";
        bw.write("<html>" + cssString + "<body>" +
                 "<img src=\"data:image/png;base64,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\" \\>"
                + "<h1>BICT Report</h1><h3>Selected Careers</h3>"
                + "<table border='1' cellpadding='3' cellspacing='3'><tr>"
                + "<th>Code</th>"
                + "<th>Name</th>"
                + "<th>Description</th></tr>");

        Map<String,Option> map = BICTExplorer.getDegree().careers();
        for (String key : map.keySet()) {

            Option career = map.get(key);
            if (career.isChosen() == true) {
                bw.write("<tr>");
                bw.write("<td>" + career.getCode() + "</td>");
                bw.write("<td>" + career.getName() + "</td>");
                bw.write("<td>" + career.getDescription() + "</td>");
            }
            bw.write("</tr>");
        }
        bw.write("</table>");

        bw.write("<h3>Selected Minors</h3><table border='1' cellpadding='3' cellspacing='3'><tr>"
                + "<th>Code</th>"
                + "<th>Name</th>"
                + "<th>Description</th></tr>");

        map = BICTExplorer.getDegree().minors();
        for (String key : map.keySet()) {

            Option career = map.get(key);
            if (career.isChosen() == true) {
                bw.write("<tr>");
                bw.write("<td>" + career.getCode() + "</td>");
                bw.write("<td>" + career.getName() + "</td>");
                bw.write("<td>" + career.getDescription() + "</td>");
            }
            bw.write("</tr>");
        }
        bw.write("</table>");

        bw.write("<h3>Selected Courses</h3><table border='1' cellpadding='3' cellspacing='3'><tr>"
                + "<th>Code</th>"
                + "<th>Name</th>"
                + "<th>Description</th>"
                + "<th>Prerequisites</th>"
                + "<th>Semesters</th>"
                + "</tr>");

        map = BICTExplorer.getDegree().courses();
        for (String key : map.keySet()) {

            Course career = (Course) map.get(key);
            if (career.isChosen() == true) {
                bw.write("<tr>");
                bw.write("<td>" + career.getCode() + "</td>");
                bw.write("<td>" + career.getName() + "</td>");
                bw.write("<td>" + career.getDescription() + "</td>");
                bw.write("<td>" + career.getPreReqs().toString() + "</td>");

                char[] chars = career.getSemesters().toCharArray();
                String semestersOffered = "";

                for (int i=0;i<chars.length;i++) {
                    semestersOffered += chars[i];
                    if (i+1 < chars.length) {
                        semestersOffered += ", ";
                    }
                }
                bw.write("<td>" + semestersOffered + "</td>");
            }
            bw.write("</tr>");
        }
        bw.write("</table>");

        bw.write("</body>");
        bw.write("</html>");

        bw.close();

        Desktop.getDesktop().browse(outputFile.toURI());
    }
}