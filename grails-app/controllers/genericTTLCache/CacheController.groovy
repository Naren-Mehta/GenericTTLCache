package genericTTLCache

import com.CacheCO
import org.springframework.web.multipart.commons.CommonsMultipartFile
import utils.AppUtil

class CacheController {

    def simulateUtilService
    def cacheSimulateService

    def cacheViewPage() {}


    def calculateTTLCache = { CacheCO cacheCO ->

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

            println("--------------------------111---------------------" + cacheFile?.bytes)

            simulateUtilService?.storeCacheFile(cacheFile, cacheSimulation)

            request.getFiles("userFiles[]").each { file ->
                simulateUtilService?.firstReadingFile(file, cacheSimulation)
            }



            try {
                cacheSimulateService?.performLogic(cacheSimulation, cacheFile, firstReadingFile)
            }
            catch (Exception e) {
                println("--------------------exception------------")
            }

        } else {
            if (!cacheFile?.bytes) {
                flash.message = g.message(code: "Please.Upload.a.file.cacheFile")
            }

            if (!firstReadingFile?.bytes) {
                flash.message = g.message(code: "Please.Upload.a.file.firstReadingFile")
            }
            render(view: "/cache/cacheViewPage", model: [simulateCO: cacheCO])
        }

    }
}
