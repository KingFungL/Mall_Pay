package com.imooc.mall;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
/**
 * @author King
 * @create 2023-03-04 3:25 下午
 * @Description:
 */
public class TestNG_Test {

    @Test
    public void shopLogin(){
        System.setProperty("webdriver.chrome.driver", "/Users/liangjingfeng/资源库/AutoTools/chromedriver111_mac_arm64/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get("localhost:8880");

    }
}
