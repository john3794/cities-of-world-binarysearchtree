import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        World world = new World();
        world.buildWorld("CitiesOfTheWorld");

        /**
         * Examples of usage on BST:
         * - calculate distance between 2 cities
         * - find the most western or eastern city in a given country
         * - find the most western or eastern city between 2 given countries
         */
        // options(world);
    }

    private static void options(World world)
    {
        System.out.println("Calculate distance between 2 cities:            1");
        System.out.println("Find the most west/east city in a country:      2");
        System.out.println("Find the most west/east city between 2 countries:    3");
        System.out.println("Choose option: ");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.nextLine().trim();
        if (option.equalsIgnoreCase("1"))
        {
            System.out.print("Enter start city: ");
            String city1 = scanner.nextLine().trim();
            System.out.print("Enter end city: ");
            String city2 = scanner.nextLine().trim();

            System.out.println("Calculating distance from " + city1 + " to " + city2);
            System.out.println("Distance: " + world.distance(city1, city2) + " km");

        }
        else if (option.equalsIgnoreCase("2"))
        {
            System.out.println("Enter country: ");
            String country = scanner.nextLine().trim();

            System.out.println("Enter west or east: ");
            String westEast = scanner.nextLine().trim();

            System.out.println("The " + westEast + " most city in " + country + ": " + world.findCityLocation(westEast, country).getCityName());
        }
        else if (option.equalsIgnoreCase("3"))
        {
            System.out.println("Enter first country: ");
            String country1 = scanner.nextLine().trim();

            System.out.println("Enter second country: ");
            String country2 = scanner.nextLine().trim();

            System.out.println("Enter west or east: ");
            String westEast = scanner.nextLine().trim();

            System.out.println("The " + westEast + " most city between the countries " + country1 + " and " + country2 + ": "
                    + world.findCityLocationBetween(westEast, country1, country2).getCityName());
        }
    }
}
