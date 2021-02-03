package com.vikmak.distance.servlets;

import com.vikmak.distance.dao.DistanceDAO;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * @author Viktor Makarov
 */
public class ShowDistanceServlet extends HttpServlet {

    DistanceDAO distanceDAO = new DistanceDAO();

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        PrintWriter out = res.getWriter();

        //Get info from form
        String calculationType = req.getParameter("calculationType");
        String cityFrom = req.getParameter("cityFrom");
        String cityTo = req.getParameter("cityTo");

        ArrayList<String> origins = new ArrayList<>(Arrays.asList(cityFrom.split("\\W{2,}+|,")));
        ArrayList<String> destinations = new ArrayList<>(Arrays.asList(cityTo.split("\\W{2,}+|,")));

        LinkedHashMap<String[], Double> distancesMap = new LinkedHashMap<>();
        distancesMap = distanceDAO.getMapping(origins, destinations);

        res.setContentType("text/html");
        out.println("<html><body>");


        if (calculationType.equals("Crowflight") || calculationType.equals("All")) {
            out.println("<table border=1 width=50%>");
            out.println("<caption>Distance by crowflight</caption>");
            out.println("<tr><th>From city</th><th>To city</th><th>Distance</th><tr>");
            for (String[] s : distancesMap.keySet()) {
                String from = s[0];
                String to = s[1];

                if (from.equals(to)) { // Ignore line if origin city same as destination
                    continue;
                }

                out.printf("<tr><td>" + from + "</td><td>" + to + "</td><td> %.03f </td></tr>\n", distancesMap.get(s));
            }
            out.println("</table>");
        }

        out.println("</br>");

        if (calculationType.equals("Distance Matrix") || calculationType.equals("All")) {
            LinkedHashSet<String> setTo = new LinkedHashSet<>();
            LinkedHashSet<String> setFrom = new LinkedHashSet<>();
            LinkedHashMap<Integer, Double> distancesToDisplay = new LinkedHashMap<>();
            int i = 0;
            for (String[] s : distancesMap.keySet()) {
                setTo.add(s[1]);
                setFrom.add(s[0]);
                distancesToDisplay.put(i++, distancesMap.get(s));
            }
            i = 0;
            ArrayList<String> listTo = new ArrayList<String>(setTo);

            out.println("<table border=1 width=50%>");
            out.println("<caption>Distance by matrix table</caption>");
            out.print("<tr><th></th>");

            for (String s : setTo) {
                out.print("<th>" + s + "</th>");
            }
            out.println("</tr>");
            for (String s : setFrom) {
                out.print("<tr><th>" + s + "</th>");
                for (int j = 0; j < listTo.size(); j ++) {
                    out.printf("<th> %.03f </th>", distancesToDisplay.get(i));
                    i++;
                }
                out.println("</tr>");
            }
        }
        out.println("</html></body>");
    }
}
