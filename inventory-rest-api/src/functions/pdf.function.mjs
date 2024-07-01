import puppeteer from "puppeteer"

const pdfOptions = {
  format: "A4",
  printBackground: true,
  margin: { top: "1.5cm", bottom: "1.7cm" }
}

export const htmlToPDF = async (html, options = pdfOptions) => {
  const browser = await puppeteer.launch()
  const page = await browser.newPage()

  await page.setContent(html, { waitUntil: "domcontentloaded" })

  const pdf =  await page.pdf(options)

  await browser.close()

  return pdf
}