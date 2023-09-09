package com.yelmanov.service

import com.yelmanov.domain.Regions
import com.yelmanov.domain.User
import com.yelmanov.repository.PriceRepository
import com.yelmanov.service.mapper.convertElementsToPrices
import com.yelmanov.telegram.service.BotMessageService
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDate

@Service
@Scope("prototype")
class PriceService(
    val driver: WebDriver,
    val botMessageService: BotMessageService,
    val priceRepository: PriceRepository
) {


    companion object {
        const val LATENCY_MILLIS: Long = 200
        const val DAY_HOURS = 24
    }

    fun getTodayPricesFromElbruk(user: User) {
        val region = user.region.regionName
        val existingPrices = priceRepository.findAllByRegion(user.region)
        if (existingPrices.isEmpty()) {
            driver.get("https://www.elbruk.se/timpriser-$region")
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(LATENCY_MILLIS))

            submitCookies()

            val sizes = getTableSize(
                "/html/body/div/div[8]/div/div/div/table/thead/tr/th",
                "/html/body/div/div[8]/div/div/div/table/tbody/tr/td[1]"
            )
            val elements =
                getTableElements(sizes["Rows"]!!, sizes["Columns"]!!, "/html/body/div/div[8]/div/div/div/table/tbody")


            //   driver.close()
            val prices = convertElementsToPrices(elements, sizes["Columns"]!!, Regions.SE3)
            priceRepository.save(prices)

            botMessageService.sendMessage(
                "*Prices for ${LocalDate.now()}:*\n${
                    prices.toString().replace("[", "").replace("]", "").replace(",", "")
                }".replace("-", "\\-"), user.chatId
            )
            return
        }
        botMessageService.sendMessage(
            "*Prices for ${LocalDate.now()}:*\n${
                existingPrices.toString().replace("[", "").replace("]", "").replace(",", "")
            }".replace("-", "\\-"), user.chatId
        )


    }

    fun getTomorrowPricesFromElbruck(user: User) {
        val regionNumber = user.region.regionNumber
        driver.get("https://www.elbruk.se/planera-elforbrukning?e=$regionNumber")
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(LATENCY_MILLIS))
        submitCookies()
        driver.findElement(By.xpath("//*[@id=\"toggle-tbl\"]")).click()
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(LATENCY_MILLIS))

        val sizes = getTableSize(
            "//*[@id=\"result-tbl-toggle\"]/thead/tr/th",
            "//*[@id=\"result-tbl\"]/tr/td[1]"
        )
        var elements =
            getTableElements(sizes["Rows"]!!, sizes["Columns"]!!, "//*[@id=\"result-tbl\"]")

        if (sizes["Rows"]!! > DAY_HOURS) {
            elements = elements.stream()
                .skip(sizes["Rows"]!!.minus(DAY_HOURS).times(sizes["Columns"]!!).toLong())
                .toList()
        }
        val prices = convertElementsToPrices(elements, sizes["Columns"]!!, Regions.SE3)
        botMessageService.sendMessage(
            "*Prices for ${LocalDate.now().plusDays(1)}:*\n${
                prices.toString().replace("[", "").replace("]", "").replace(",", "")
            }".replace("-", "\\-"), 162300020
        )


        //   driver.close()
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

    private fun submitCookies() {
        try {
            val submitCookieButton = driver.findElement(By.className("css-47sehv"))
            submitCookieButton.click()
        } catch (e: RuntimeException) {
        }
    }
}