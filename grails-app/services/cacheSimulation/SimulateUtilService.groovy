package cacheSimulation

import genericTTLCache.CacheDocuments
import genericTTLCache.CacheSimulateDocuments
import genericTTLCache.CacheSimulation
import org.springframework.web.multipart.commons.CommonsMultipartFile
import utils.AppUtil

/**
 * Created by narendra on 27/2/15.
 */
class SimulateUtilService {

    public void storeCacheFile(CommonsMultipartFile uploadedFile, CacheSimulation cacheSimulation) {
//        if (uploadedFile?.bytes) {

        println("--------------1----if bytes------------------")

        def webRootDir = AppUtil.staticResourcesDirPath
        def userDir = new File(webRootDir, "/uploadedFile/storeCacheFile/${cacheSimulation?.id}/")
        userDir.mkdirs()

        String fileName = uploadedFile.originalFilename.trim()
        uploadedFile.transferTo(new File(userDir, fileName))
        CacheDocuments cacheDocuments = new CacheDocuments()
        cacheDocuments.name = fileName
        cacheDocuments.contentType = uploadedFile.contentType
        cacheDocuments.cacheSimulation = cacheSimulation

        AppUtil?.save(cacheDocuments)
//        }
    }


    public void firstReadingFile(CommonsMultipartFile uploadedFile, CacheSimulation cacheSimulation) {
        if (uploadedFile?.bytes) {
            def webRootDir = AppUtil.staticResourcesDirPath
            def userDir = new File(webRootDir, "/uploadedFile/firstReadingFile/${cacheSimulation?.id}/")
            userDir.mkdirs()

            String fileName = uploadedFile.originalFilename.trim()
            uploadedFile.transferTo(new File(userDir, fileName))
            CacheSimulateDocuments simulateDocuments = new CacheSimulateDocuments()
            simulateDocuments.name = fileName
            simulateDocuments.contentType = uploadedFile.contentType
            simulateDocuments.cacheSimulation = cacheSimulation

            AppUtil?.save(simulateDocuments)
        }
    }
}
