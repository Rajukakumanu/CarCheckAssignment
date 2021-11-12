import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import sun.awt.windows.WSystemTrayPeer;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class carTaxCheck {
    public static void main(String[] args) throws IOException, InterruptedException {

        WebDriver dr= getDriver();
       ArrayList<String> list=  getCarNumber();
       System.out.println(list.toString());
       for(int i=0;i< list.size();i++)
       {
           String registrationNum= list.get(i).replaceAll(" ","");
           LinkedHashMap<String, String> carDetails= getCarDetails(dr,registrationNum);
          Set<String> set= carDetails.keySet();
          System.out.println("************************************" +registrationNum);
          ArrayList<String> li= new ArrayList<>();
          for(String s: set)
          {
              System.out.println(s + " : "+carDetails.get(s));
              li.add(carDetails.get(s));
          }

          ArrayList<String> carList= getCarRegistrationDetails(registrationNum);

           for(int j=0;j<carList.size();j++) {

               try {

                   String detailsOnGUI= li.get(j).toString();
                   String detailsOnOutput= carList.get(j).toString();
                       Assert.assertEquals(detailsOnGUI,detailsOnOutput);


               }

               catch(AssertionError e)
               {
               System.out.println(e.getMessage());
               }

           }


       }

       dr.close();
       dr.quit();

    }

    public static void searchRegistrationNum(WebDriver dr, String registrationNum)
    {
        dr.get("https://cartaxcheck.co.uk/");
        WebElement ele= dr.findElement(By.xpath("//input[@placeholder='Enter Registration']"));
        ele.clear();
        ele.sendKeys(registrationNum);
        WebElement search= dr.findElement(By.xpath("//button[contains(text(),'Free Car Check')]"));
        search.click();
    }

    public static LinkedHashMap<String, String> getCarDetails(WebDriver dr, String registrationNum) throws InterruptedException {
        searchRegistrationNum(dr,registrationNum );
        LinkedHashMap<String, String>  map= new LinkedHashMap<>();
        Thread.sleep(5000);
           List<WebElement> ele= dr.findElements(By.xpath("//h4[text()='Vehicle Identity']//parent::div//following-sibling::div//dl"));
           int size = ele.size();
           for(int i=1;i<=size;i++)
           {
               //h4[text()='Vehicle Identity']//parent::div//following-sibling::div//dl//dt   -- Title
               //h4[text()='Vehicle Identity']//parent::div//following-sibling::div//dl//dd   -- Value
               WebDriverWait wait= new WebDriverWait(dr, 90);
               wait.until(ExpectedConditions.visibilityOfAllElements(dr.findElement(By.xpath("//h4[text()='Vehicle Identity']//parent::div//following-sibling::div//dl["+i+"]//dt"))));

               String getTitle= dr.findElement(By.xpath("//h4[text()='Vehicle Identity']//parent::div//following-sibling::div//dl["+i+"]//dt")).getText();
               String getValue= dr.findElement(By.xpath("//h4[text()='Vehicle Identity']//parent::div//following-sibling::div//dl["+i+"]//dd")).getText();
                    map.put(getTitle, getValue);
           }
           return map;
    }

    public static WebDriver getDriver()
    {
        WebDriverManager.chromedriver().setup();
        WebDriver dr= new ChromeDriver();
        dr.manage().window().maximize();
        return dr;
    }

    public static void searchRegistrationNumber()
    {
        //h4[text()='Vehicle Identity']//parent::div//following-sibling::div//dl//dt   -- Title
        //h4[text()='Vehicle Identity']//parent::div//following-sibling::div//dl//dd   -- Value
    }

    public static ArrayList<String> getCarNumber() throws IOException {
        File file = new File("C:\\Users\\Raj\\Documents\\TaskAssinment\\car_input.txt");
        FileReader reader = new FileReader(file);
        BufferedReader read= new BufferedReader(reader);
        String line ="";
        String sum="";
        while((line=read.readLine())!=null)
        {
           sum= sum+line;
        //   System.out.println(sum);
        }

        Pattern pr= Pattern.compile("([A-Z]+[0-9]+\\s[A-Z]*)|([A-Z]+[0-9]+[A-Z]*)");
        Matcher mt= pr.matcher(sum);

        ArrayList<String> list= new ArrayList<>();
        while(mt.find())
        {
            list.add(mt.group());
        }

       return list;
    }


    public static ArrayList<String> getCarRegistrationDetails(String registrationNum) throws IOException {
        ArrayList<String> list= getCarDetails();
        ArrayList<String> list1= new ArrayList<>();
        for(String str: list)
        {
            String[] details= str.split(",");
                    for(int i=0;i<details.length;i++) {
                        if (details[0].equals(registrationNum)) {
                             list1.add(details[i]);
                        }
                    }
        }

        return list1;
    }
    public static ArrayList<String> getCarDetails() throws IOException {
        File file = new File("C:\\Users\\Raj\\Documents\\TaskAssinment\\car_output.txt");
        FileReader reader = new FileReader(file);
        BufferedReader read= new BufferedReader(reader);
        String line ="";
        String sum="";
        ArrayList<String> list= new ArrayList<>();
        while((line=read.readLine())!=null)
        {
            list.add(line);
            sum= sum+line;
         //   System.out.println(sum);
        }


        return list;
    }
}
