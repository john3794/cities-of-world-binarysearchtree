import java.io.*;
import java.util.ArrayList;

public class World
{
    private BinarySearchTree BSTDistance;
    private BinarySearchTree BSTAlphabetical;

    public World()
    {
        this.BSTDistance = new BinarySearchTree();
        this.BSTAlphabetical = new BinarySearchTree();
    }

    /**
     * Read txt file and build binary search tree: one sorted by coordinates, another sorted alphabetically
     *
     * @param filename
     */
    public void buildWorld(String filename)
    {
        try
        {
            String workingDir = "src/";
            File file = new File(workingDir.concat(filename.concat(".txt")));
            FileInputStream fileInputStream = new FileInputStream(file.getPath());
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            BufferedReader input = new BufferedReader(new InputStreamReader(dataInputStream));

            ArrayList<City> cities = new ArrayList<>();
            for (String line = input.readLine(); line != null; line = input.readLine())
            {
                String[] tokens = line.trim().split(";");
                City city = new City(tokens[0], tokens[1]);
                city.setLatitude(Integer.parseInt(tokens[2]));
                city.setLatMinutes(Integer.parseInt(tokens[3]));
                city.setNS(tokens[4]);
                city.setLongitude(Integer.parseInt(tokens[5]));     // longitude
                city.setLongMinutes(Integer.parseInt(tokens[6]));   // minutes
                city.setWE(tokens[7]);                              // west/east
                city.calculateToDecimal();
                cities.add(city);
            }

            for (int i = 0; i < cities.size(); i++)
            {
                BSTDistance.insert(cities.get(i));                  // BST used to calculate distance
                BSTAlphabetical.insertAlpha(cities.get(i));         // BST used to find and compare cities
            }

            //Print order by city location from west to east OR Print order by city name from A to Z
            System.out.println();
            BSTDistance.printTreeInOrder(BSTDistance.getRoot());
            // System.out.println();
            // BSTAlphabetical.printTreeInOrder(BSTAlphabetical.getRoot());
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }

    /**
     * Calculate distance between 2 cities
     *
     * @param cName1
     * @param cName2
     * @return
     */
    public double distance(String cName1, String cName2)
    {
        City city1 = BSTAlphabetical.find(cName1);
        City city2 = BSTAlphabetical.find(cName2);

        System.out.println("c1: " + city1 + " | name: " + city1.getCityName() + " | cityLat: " + city1.getLatDecimal() + " | cityLon: " + city1.getLongDecimal());
        System.out.println("c2: " + city2 + " | name: " + city2.getCityName() + " | cityLat: " + city2.getLatDecimal() + " | cityLon: " + city2.getLongDecimal());

        double lon1 = city1.getLongDecimal();
        double lon2 = city2.getLongDecimal();
        double lat1 = city1.getLatDecimal();
        double lat2 = city2.getLatDecimal();

        double theta = lon1 - lon2;
        double dist = Math.sin(convertToRadians(lat1)) * Math.sin(convertToRadians(lat2)) + Math.cos(convertToRadians(lat1)) * Math.cos(convertToRadians(lat2)) * Math.cos(convertToRadians(theta));
        dist = Math.acos(dist);
        dist = convertToDegrees(dist);
        dist = dist * 60 * 1.1515;

        dist = dist * 1.609344; // miles to km
        return (dist);
    }

    private double convertToRadians(double degrees) { return (degrees * Math.PI / 180.0); }
    private double convertToDegrees(double radius) { return (radius * 180.0 / Math.PI); }

    /**
     * Return most western/eastern city by given country
     *
     * @param westOrEast
     * @param country
     * @return
     */
    public City findCityLocation(String westOrEast, String country)
    {
        ArrayList<City> citiesFromCountry = new ArrayList<>();
        BinarySearchTree.Node root = BSTAlphabetical.getRoot();

        /** step 1: find all cities in given country into arraylist */
        BinarySearchTree.Node cityFoundFromCountry = BSTAlphabetical.findCityFrom(root, country);
        BinarySearchTree.Node previousNode = new BinarySearchTree.Node(null);
        while (cityFoundFromCountry != null && !cityFoundFromCountry.equals(previousNode))
        {
            System.out.println("FOUND CITIES: " + cityFoundFromCountry.data.getCityName() + " lon: " + cityFoundFromCountry.data.getLongDecimal());
            previousNode = cityFoundFromCountry;
            citiesFromCountry.add(cityFoundFromCountry.data);
            BSTAlphabetical.delete(cityFoundFromCountry.data);
            cityFoundFromCountry = BSTAlphabetical.findCityFrom(root, country);
        }

        /** step 2: find the most east/west city in given country */
        return calculateMostWestEastCity(westOrEast, citiesFromCountry, citiesFromCountry.get(0));
    }

    /**
     * Return cities found in given 2 countries and calculate most western/eastern city between them
     *
     * @param westOrEast
     * @param country1
     * @param country2
     * @return
     */
    public City findCityLocationBetween(String westOrEast, String country1, String country2)
    {
        ArrayList<City> citiesOfCountries = new ArrayList<>();
        BinarySearchTree.Node root = BSTAlphabetical.getRoot();

        /** step 1: find all cities in given 2 countries into arraylist */
        BinarySearchTree.Node citiesFromCountry1 = BSTAlphabetical.findCityFrom(root, country1);
        BinarySearchTree.Node previousNode1 = new BinarySearchTree.Node(null);
        BinarySearchTree.Node citiesFromCountry2 = BSTAlphabetical.findCityFrom(root, country2);
        BinarySearchTree.Node previousNode2 = new BinarySearchTree.Node(null);

        if (citiesFromCountry1 != null || citiesFromCountry2 != null)
        {
            while (citiesFromCountry1 != null && !citiesFromCountry1.equals(previousNode1))
            {
                System.out.println("FOUND CITIES 1: " + citiesFromCountry1.data.getCityName() + " lon: " + citiesFromCountry1.data.getLongDecimal());

                previousNode1 = citiesFromCountry1;
                citiesOfCountries.add(citiesFromCountry1.data);
                BSTAlphabetical.delete(citiesFromCountry1.data);
                citiesFromCountry1 = BSTAlphabetical.findCityFrom(root, country1);
            }

            while (citiesFromCountry2 != null && !citiesFromCountry2.equals(previousNode2))
            {
                System.out.println("FOUND CITIES 2: " + citiesFromCountry2.data.getCityName() + " lon: " + citiesFromCountry2.data.getLongDecimal());

                previousNode2 = citiesFromCountry2;
                citiesOfCountries.add(citiesFromCountry2.data);
                BSTAlphabetical.delete(citiesFromCountry2.data);
                citiesFromCountry2 = BSTAlphabetical.findCityFrom(root, country2);
            }
        }

        /** step 2: calculate the most west/east city in the 2 given countries */
        return calculateMostWestEastCity(westOrEast, citiesOfCountries, citiesOfCountries.get(0));
    }

    /**
     * Calculate city location from given list of cities from a country
     *
     * @param westOrEast
     * @param citiesFromCountry
     * @param rootCity
     * @return
     */
    private City calculateMostWestEastCity(String westOrEast, ArrayList<City> citiesFromCountry, City rootCity)
    {
        if (westOrEast.equalsIgnoreCase("west"))
        {
            for (int i = 1; i < citiesFromCountry.size(); i++)
            {
                if (citiesFromCountry.get(i).getLongDecimal() < rootCity.getLongDecimal())
                {
                    rootCity = citiesFromCountry.get(i);
                }
            }
        }
        else if (westOrEast.equalsIgnoreCase("east"))
        {
            for (int i = 1; i < citiesFromCountry.size(); i++)
            {
                if (citiesFromCountry.get(i).getLongDecimal() > rootCity.getLongDecimal())
                {
                    rootCity = citiesFromCountry.get(i);
                }
            }
        }
        return rootCity;
    }
}
