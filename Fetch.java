import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class GoldBarTester {

    private WebDriver driver;

    public GoldBarTester() {
       
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        this.driver = new ChromeDriver();
    }

  
    public void launchWebsite() {
        driver.get("http://sdetchallenge.fetch.com/");
    }

 
    public void setGoldBars(int[] leftBowl, int[] rightBowl) {
        clearBowls();

        for (int i = 0; i < leftBowl.length; i++) {
            WebElement leftSlot = driver.findElement(By.id("left_" + i));
            leftSlot.sendKeys(String.valueOf(leftBowl[i]));
        }

        for (int i = 0; i < rightBowl.length; i++) {
            WebElement rightSlot = driver.findElement(By.id("right_" + i));
            rightSlot.sendKeys(String.valueOf(rightBowl[i]));
        }
    }

    
    public String executeWeighing() {
        WebElement weighButton = driver.findElement(By.id("weigh"));
        weighButton.click();
        WebElement result = driver.findElement(By.id("result"));
        return result.getText();
    }

  
    public void resetBowls() {
        WebElement resetButton = driver.findElement(By.id("reset"));
        resetButton.click();
    }

   
    public void clickFakeBar(int barIndex) {
        WebElement barButton = driver.findElement(By.id("bar_" + barIndex));
        barButton.click();
    }

   
    public String getAlertMessage() {
        return driver.switchTo().alert().getText();
    }


    private void clearBowls() {
        for (int i = 0; i < 3; i++) {
            WebElement leftSlot = driver.findElement(By.id("left_" + i));
            WebElement rightSlot = driver.findElement(By.id("right_" + i));
            leftSlot.clear();
            rightSlot.clear();
        }
    }

 
    public void closeDriver() {
        driver.quit();
    }

    public static void main(String[] args) {
        GoldBarTester tester = new GoldBarTester();
        tester.launchWebsite();

        int[][] groups = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};

    
        tester.setGoldBars(groups[0], groups[1]);
        String result = tester.executeWeighing();
        int suspectedGroupIndex;
        if (result.contains("Left")) {
            suspectedGroupIndex = 0;
        } else if (result.contains("Right")) {
            suspectedGroupIndex = 1;
        } else {
            suspectedGroupIndex = 2;
        }

        tester.resetBowls();

       
        int[] suspectedGroup = groups[suspectedGroupIndex];
        tester.setGoldBars(new int[]{suspectedGroup[0]}, new int[]{suspectedGroup[1]});
        result = tester.executeWeighing();
        int fakeBarIndex;
        if (result.contains("Left")) {
            fakeBarIndex = suspectedGroup[0];
        } else if (result.contains("Right")) {
            fakeBarIndex = suspectedGroup[1];
        } else {
            fakeBarIndex = suspectedGroup[2];
        }

        
        tester.clickFakeBar(fakeBarIndex);
        String alertMessage = tester.getAlertMessage();
        System.out.println(alertMessage);

        tester.closeDriver();
    }
}
