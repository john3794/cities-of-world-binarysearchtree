public class City
{
    private String cityName;
    private String countryName;
    private int latitude;
    private int latMinutes;
    private int longitude;
    private int longMinutes;
    private String NS;
    private String WE;

    private double longDecimal;
    private double latDecimal;

    public City(String cityName, String countryName)
    {
        this.cityName = cityName;
        this.countryName = countryName;
    }

    public void setLatitude(int latitude)
    {
        this.latitude = latitude;
    }
    public void setLatMinutes(int latMinutes)
    {
        this.latMinutes = latMinutes;
    }
    public void setLongitude(int longitude)
    {
        this.longitude = longitude;
    }
    public void setLongMinutes(int longMinutes)
    {
        this.longMinutes = longMinutes;
    }
    public void setNS(String NS)
    {
        this.NS = NS;
    }
    public void setWE(String WE)
    {
        this.WE = WE;
    }

    public void calculateToDecimal()
    {
        //determine whether city longtitude is west or east of Greenwich
        if(WE.trim().equalsIgnoreCase("W"))
        {
            this.longDecimal = -1.0 * Math.abs((longMinutes / 60.0) + longitude);
        }else{
            this.longDecimal = Math.abs((longMinutes / 60.0) + longitude);
        }

        if(NS.trim().equalsIgnoreCase("S"))
        {
            this.latDecimal = Math.abs((latMinutes / 60.0) + latitude) * -1.0;
        }else{
            this.latDecimal = (latMinutes / 60.0) + latitude;
        }
    }

    public double getLongDecimal()
    {
        return longDecimal;
    }
    public double getLatDecimal()
    {
        return latDecimal;
    }

    public String getCityName()
    {
        return cityName;
    }
    public String getCountryName()
    {
        return countryName;
    }

}

