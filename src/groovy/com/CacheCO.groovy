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


    static constraints = {
        cacheSize(blank: false, nullable: false)
        cacheRefreshRate(blank: false, nullable: false)
        checkNextSampleRate(blank: false, nullable: false)
        numberOfEntries(blank: false, nullable: false)
        updateLOGFILERate(blank: false, nullable: false)
        replacingScheme(blank: false, nullable: false)
    }

}
