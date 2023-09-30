package com.yelmanov.service

import com.yelmanov.domain.Price
import com.yelmanov.domain.User
import com.yelmanov.repository.PriceRepository
import com.yelmanov.service.mapper.convertElementsToPrices
import com.yelmanov.telegram.service.BotMessageService
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.slf4j.LoggerFactory
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
    private val logger = LoggerFactory.getLogger(javaClass)

    companion object {
        const val LATENCY_MILLIS: Long = 300
        const val DAY_HOURS = 24
        const val BASE_URL = "https://www.elbruk.se/"
        const val TODAY_PRICES_URL = "${BASE_URL}timpriser-"
        const val TOMORROW_PRICES_URL = "${BASE_URL}planera-elforbrukning?e="
    }

    fun getTodayPricesFromElbruk(user: User) {
        val region = user.region.regionName
        val existingPrices = priceRepository.findAllByRegion(user.region)

        if (existingPrices.isEmpty()) {
            driver.get("$TODAY_PRICES_URL$region")
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(LATENCY_MILLIS))
            submitCookies()
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(LATENCY_MILLIS))

            val sizes = getTableSize(
                "/html/body/div[6]/div/div/div/table/thead/tr/th",
                "/html/body/div[6]/div/div/div/table/tbody/tr/td[1]"
            )
            val elements =
                getTableElements(sizes["Rows"]!!, sizes["Columns"]!!, "/html/body/div[6]/div/div/div/table/tbody")

            val prices = convertElementsToPrices(elements, sizes["Columns"]!!, user.region)
            priceRepository.save(prices)

            sendFormattedPrices(prices, user.chatId, LocalDate.now().toString())
        } else {
            sendFormattedPrices(existingPrices, user.chatId, LocalDate.now().toString())
        }
    }

    fun getTomorrowPricesFromElbruk(user: User) {
        val regionNumber = user.region.regionNumber
        driver.get(TOMORROW_PRICES_URL + regionNumber)
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
        val prices = convertElementsToPrices(elements, sizes["Columns"]!!, user.region)
        sendFormattedPrices(prices, user.chatId, LocalDate.now().plusDays(1).toString())
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

    private fun sendFormattedPrices(prices: List<Price>, chatId: Long, date: String) {
        val formattedPrices = prices.joinToString("") { it.toString() }
        val message = """*Prices for ${date}:*
$formattedPrices""".replace("-", "\\-")
        botMessageService.sendMessage(message, chatId, false)
    }

    private fun submitCookies() {
        try {
            val submitCookieButton = driver.findElement(By.className("css-47sehv"))
            submitCookieButton.click()
        } catch (e: RuntimeException) {
            logger.warn("Cookies dialog hasn't appear")
        }
    }
}