package com.vikmak.distance.servlets;

import com.vikmak.distance.services.DistanceService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Viktor Makarov
 */
public class ShowDistanceServlet extends HttpServlet {

    DistanceService distanceService = new DistanceService();

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        PrintWriter out = res.getWriter();

        //Get info from form
        String calculationType = req.getParameter("calculationType");
        String cityFrom = req.getParameter("cityFrom");
        String cityTo = req.getParameter("cityTo");

        List<String> origins = Arrays.asList(cityFrom.split("\\W+"));
        List<String> destinations = Arrays.asList(cityTo.split("\\W+"));

        LinkedHashMap<String[], Double> distancesMap = new LinkedHashMap<>();
        distancesMap = distanceService.getMapping(origins, destinations);

        res.setContentType("text/html");
        out.println("<html><body>");

        out.println("<table border=1 width=50% height=50%>");

        out.println("<tr><th>Origin</th><th>Destination</th><th>Distance</th><tr>");
        for (String[] s : distancesMap.keySet())
        {
            String from = s[0];
            String to = s[1];
            Double distance = distancesMap.get(s);
            out.println("<tr><td>" + from + "</td><td>" + to + "</td><td>" + distance + "</td></tr>");
        }
        out.println("</table>");
        out.println("</html></body>");
}
}
