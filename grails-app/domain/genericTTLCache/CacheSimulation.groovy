package genericTTLCache

class CacheSimulation {

    Long cacheSize
    Long cacheRefreshRate
    Long checkNextSampleRate
    Long numberOfEntries
    Long updateLOGFILERate
    Long replacingScheme

    static constraints = {
        cacheRefreshRate(blank: false, nullable: false)
        checkNextSampleRate(blank: false, nullable: false)
        cacheSize(blank: false, nullable: false)
        numberOfEntries(blank: false, nullable: false)
        updateLOGFILERate(blank: false, nullable: false)
        replacingScheme(blank: false, nullable: false)
    }

}
