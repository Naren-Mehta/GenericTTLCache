package genericTTLCache

class CacheSimulation {

    Long cacheSize
    Long cacheRefreshRate
    Long checkNextSampleRate
    Long numberOfEntries
    Long updateLOGFILERate
    Long replacingScheme

    static constraints = {
        cacheRefreshRate(blank: true, nullable: true)
        checkNextSampleRate(blank: true, nullable: true)
        cacheSize(blank: true, nullable: true)
        numberOfEntries(blank: true, nullable: true)
        updateLOGFILERate(blank: true, nullable: true)
        replacingScheme(blank: true, nullable: true)
    }

}
