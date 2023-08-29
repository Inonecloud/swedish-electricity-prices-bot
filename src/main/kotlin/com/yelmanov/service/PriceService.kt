package com.yelmanov.service

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class PriceService(val driver: WebDriver) {


    //se1-lulea
    //se2-sundsvall
    //s3-stockholm
    //se4-malmo


    companion object {
        const val LATENCY_MILLIS: Long = 500
    }

    @Scheduled()
    fun getTodayPricesFromElbruk(region: String) {

        driver.get("https://www.elbruk.se/timpriser-$region")
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(LATENCY_MILLIS))

        val submitCookieButton = driver.findElement(By.className("css-47sehv"))
        submitCookieButton.click()
        val sizes = getTableSize(
            "/html/body/div/div[8]/div/div/div/table/thead/tr/th",
            "/html/body/div/div[8]/div/div/div/table/tbody/tr/td[1]"
        )
        val elements =
            getTableElements(sizes["Rows"]!!, sizes["Columns"]!!,"/html/body/div/div[8]/div/div/div/table/tbody")

        driver.quit()
    }

    @Scheduled()
    fun getTomorrowPricesFromElbruck(region: String){

    }


    private fun getTableSize(columnPath: String, rowsPath: String): Map<String, Int> {
        return mapOf(
            "Columns" to driver.findElements(By.xpath(columnPath)).size,
            "Rows" to driver.findElements(By.xpath(rowsPath)).size
        )
    }

    private fun getTableElements(
        numberOfRaws: Int,
        numberOfColumns: Int,
        xpath: String
    ): List<String> {
        val elements = mutableListOf<String>()
        for (i in 1..numberOfRaws step 1) {
            for (j in 1..numberOfColumns step 1) {
                val element =
                    driver.findElement(By.xpath("$xpath/tr[$i]/td[$j]")).text
                elements.add(element)
            }
        }
        return elements.toList()
    }

}