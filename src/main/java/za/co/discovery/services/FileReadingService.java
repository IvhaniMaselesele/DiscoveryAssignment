package za.co.discovery.services;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.discovery.Models.Graph;
import za.co.discovery.Models.Planet;
import za.co.discovery.Models.Route;
import za.co.discovery.dataAccess.PlanetDAO;
import za.co.discovery.dataAccess.RouteDAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

@Service
public class FileReadingService {
    PlanetDAO planetDAO;
    RouteDAO routeDAO;
    ShortestPathService shortestPathService;

    @Autowired
    public FileReadingService(PlanetDAO planetDAO, RouteDAO routeDAO, ShortestPathService shortestPathService) {
        this.shortestPathService = shortestPathService;
        this.planetDAO = planetDAO;
        this.routeDAO = routeDAO;
    }

    //TODO  : test this class
    public Planet persistPlanet(Planet planet) {
        return planetDAO.save(planet);
    }

    private Route getRouteRowValues(Iterator<Cell> cellIterator) {
        String destination = "";
        double distance = 0;
        String origin = "";
        int id = -1;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    if (cell.getColumnIndex() == 3) distance = cell.getNumericCellValue();
                    else if (cell.getColumnIndex() == 0) id = (int) cell.getNumericCellValue();

                    break;
                case Cell.CELL_TYPE_STRING:
                    if (cell.getColumnIndex() == 1) origin = cell.getStringCellValue();
                    else if (cell.getColumnIndex() == 2) destination = cell.getStringCellValue();
                    break;
            }
        }
        if (id != -1) {
            Route route = new Route(id, origin, destination, distance);
            return route;
        }
        return null;
    }

    public void readRouteAndTrafficSheets(Graph graph) {
        try {
            String fileName = new File("./").getCanonicalPath() + "\\src\\main\\java\\za\\co\\discovery\\dataObjects\\planetTravelDetails.xlsx";
            FileInputStream file = new FileInputStream(fileName);

            XSSFWorkbook workbook = new XSSFWorkbook(file);

            XSSFSheet sheet = workbook.getSheetAt(1);

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                Route route = getRouteRowValues(cellIterator);
                if (route != null) {
                    persistRoute(route);
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getTraffic(graph);
    }

    public Route persistRoute(Route route) {
        return routeDAO.save(route);
    }

    public Route updateRoute(Route route) {
        return routeDAO.update(route);
    }

    public void readPlanetSheet(Graph graph) {
        try {
            String fileName = new File("./").getCanonicalPath() + "\\src\\main\\java\\za\\co\\discovery\\dataObjects\\planetTravelDetails.xlsx";
            FileInputStream file = new FileInputStream(fileName);

            XSSFWorkbook workbook = new XSSFWorkbook(file);

            XSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Planet planet;
                String name = "";
                String node = "";
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            if (cell.getColumnIndex() == 1) name = cell.getStringCellValue();
                            else if (cell.getColumnIndex() == 0) node = cell.getStringCellValue();
                            break;
                    }
                }

                if (!name.toLowerCase().contains("name")) {
                    planet = new Planet(node, name);
                    if (!planet.getNode().equalsIgnoreCase("")) {
                        persistPlanet(planet);
                        shortestPathService.addNode(planet, graph);
                    }
                }
            }
            file.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getTraffic(Graph graph) {
        try {
            String fileName = new File("./").getCanonicalPath() + "\\src\\main\\java\\za\\co\\discovery\\dataObjects\\planetTravelDetails.xlsx";
            FileInputStream file = new FileInputStream(fileName);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(2);

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                Route route = getRouteRowValues(cellIterator);

                if (route != null) {
                    double traffic = route.getDistance();
                    int id = route.getId();
                    Route retrievedRoute = getRouteById(id);
                    retrievedRoute.setTraffic(traffic);
                    updateRoute(retrievedRoute);
                    shortestPathService.addEdge(retrievedRoute, graph);
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    private Route getRouteById(int id) {
        return routeDAO.retrieve(id);

    }
}

