package com

import grails.validation.Validateable
import org.springframework.web.multipart.commons.CommonsMultipartFile


@Validateable
class CacheCO {

    CommonsMultipartFile cacheFile
    CommonsMultipartFile firstReadingFile

    Long cacheSize
    Long cacheRefreshRate
    Long checkNextSampleRate
    Long numberOfEntries
    Long updateLOGFILERate
    Long replacingScheme
    String fileType

    static constraints = {
        fileType(blank: true, nullable: true)
        cacheSize(blank: true, nullable: true)
        cacheRefreshRate(blank: true, nullable: true)
        checkNextSampleRate(blank: true, nullable: true)
        numberOfEntries(blank: true, nullable: true)
        updateLOGFILERate(blank: true, nullable: true)
        replacingScheme(blank: true, nullable: true)
    }

}
