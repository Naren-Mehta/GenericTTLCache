package genericTTLCache

import com.CacheCO
import jxl.Cell
import org.springframework.web.multipart.commons.CommonsMultipartFile
import utils.AppUtil

import java.util.regex.Pattern
import jxl.DateCell
import jxl.LabelCell
import jxl.NumberCell
import jxl.Sheet
import jxl.Workbook


class CacheController {

    def simulateUtilService
    def cacheSimulateService

    private final static int COLUMN_IP = 0
    private final static int COLUMN_GGTL = 1


    def cacheViewPage() {}

    def cacheViewPageWithDBChoice() {}

    def calculateTTLCache = { CacheCO cacheCO ->


        println("--fileType----" + params?.fileType)

        CommonsMultipartFile cacheFile = cacheCO?.cacheFile as CommonsMultipartFile
        CommonsMultipartFile firstReadingFile = cacheCO?.firstReadingFile as CommonsMultipartFile

        if (cacheCO?.validate()) {

            CacheSimulation cacheSimulation = new CacheSimulation()
            cacheSimulation.cacheSize = cacheCO?.cacheSize
            cacheSimulation.cacheRefreshRate = cacheCO?.cacheRefreshRate
            cacheSimulation.checkNextSampleRate = cacheCO?.checkNextSampleRate
            cacheSimulation.numberOfEntries = cacheCO?.numberOfEntries
            cacheSimulation.updateLOGFILERate = cacheCO?.updateLOGFILERate
            cacheSimulation.replacingScheme = cacheCO?.replacingScheme

            AppUtil?.save(cacheSimulation)

//            simulateUtilService?.storeCacheFile(cacheFile, cacheSimulation)

            List<IpAddressDetails> ipAddressDetailsList = []


            if (params?.fileType?.equals("text")) {
                if (params.cacheFile) {
                    def file = request.getFile('cacheFile')
                    try {
                        uploadTextFile(file, cacheSimulation)
                    } catch (Exception e) {
                        println("----------------exception----12--------")
                    }
                }
                ipAddressDetailsList = IpAddressDetails?.findAllByCacheSimulation(cacheSimulation)
            } else if (params?.fileType?.equals("excel")) {
                if (params.cacheFile) {
                    def file = request.getFile('cacheFile')
                    try {
                        uploadExcelFile(file, cacheSimulation)
                    } catch (Exception e) {
                        println("----------------exception----2--------")
                    }

                }
                ipAddressDetailsList = IpAddressDetails?.findAllByCacheSimulation(cacheSimulation)
            } else {
                ipAddressDetailsList = IpAddressDetails?.list()
            }


            if (ipAddressDetailsList) {
                println("-------if-------isSuccessful-------------")

                render(view: "/cache/result", model: [ipAddressDetailsList: ipAddressDetailsList])
            } else {
                println("-------else-------isSuccessful-------------" + cacheCO?.fileType)




                if (params?.fileType?.equals("mysql")) {
                    flash.message = "There is no ips in the Mysql database "

                } else if (params?.fileType?.equals("excel")) {
                    flash.message = "File size will be not more than 1.0 mb.(.xls file only allowed)"

                } else if (params?.fileType?.equals("text")) {
                    flash.message = "File size will be not more than 1.0 mb."

                }

//                redirect(action: "cacheViewPageWithDBChoice")
                render(view: "/cache/cacheViewPageWithDBChoice", model: [simulateCO: cacheCO])

            }

        } else {
            if (!cacheFile?.bytes) {
                flash.message = g.message(code: "Please.Upload.a.file.cacheFile")
            }

            if (!firstReadingFile?.bytes) {
                flash.message = g.message(code: "Please.Upload.a.file.firstReadingFile")
            }
            render(view: "/cache/cacheViewPageWithDBChoice", model: [simulateCO: cacheCO])
        }

    }

    def uploadTextFile(def file, CacheSimulation cacheSimulation) {
        def reader = new BufferedReader(new InputStreamReader(file.inputStream));
        String line
        String ipRegex = "\\d{1,3}(?:\\.\\d{1,3}){3}(?::\\d{1,5})?";
        Pattern ipPattern = Pattern.compile(ipRegex);


        while ((line = reader.readLine()) != null) {
            IpAddressDetails ipAddressDetails = new IpAddressDetails()

            String[] arrayOfStrings = line?.trim()?.replaceAll("\\s", " ")?.split(" ")
            def matcher = ipPattern.matcher(arrayOfStrings[0])

            if (matcher.find()) {
                ipAddressDetails.ipAddress = matcher.group()
                ipAddressDetails.ttlCache = arrayOfStrings[1]
                ipAddressDetails.cacheSimulation = cacheSimulation
                ipAddressDetails.save()
                println("----------------1------------------" + ipAddressDetails?.ttlCache)
                println("----------------2------------------" + ipAddressDetails?.ipAddress)

            }
        }
    }


    def uploadExcelFile(def file, CacheSimulation cacheSimulation) {
        Workbook workbook = Workbook.getWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheet(0);

        println("--------------------sheet.getRows()----------------------" + sheet.getRows())
        println("--------------------sheet.getCell(COLUMN_IP, row)---------------------" + sheet.getCell(COLUMN_IP, 1))

        for (int row = 1; row < sheet.getRows(); row++) {
            Cell ipAddress = sheet.getCell(COLUMN_IP, row)
            NumberCell ggtl = sheet.getCell(COLUMN_GGTL, row)


            println("----------ipAddress------------" + ipAddress?.string)
            println("-----------ggtl-----------------" + ggtl?.value)

            IpAddressDetails ipAddressDetails = new IpAddressDetails()


            ipAddressDetails.ipAddress = ipAddress?.string
            ipAddressDetails.ttlCache = ggtl?.value
            ipAddressDetails.cacheSimulation = cacheSimulation
            ipAddressDetails.save()
        }


    }
}
