package cacheSimulation

import com.CacheManager
import com.CachedObject
import com.FileLogger
import com.TTLCache
import com.ipAddress
import com.quitException
import genericTTLCache.CacheDocuments
import genericTTLCache.CacheSimulateDocuments
import genericTTLCache.CacheSimulation
import org.springframework.web.multipart.commons.CommonsMultipartFile
import sun.misc.Cache
import utils.AppUtil

import java.util.logging.Level
import java.util.logging.Logger

/**
 * Created by narendra on 26/2/15.
 */
class CacheSimulateService {

    def simulateUtilService
    static Scanner scanner = null;
    static int EntriesAtATime = 100

    private static int LogFileUpdate = 0;

    public performLogic(CacheSimulation cacheSimulation, CommonsMultipartFile cacheFile, CommonsMultipartFile firstReadingFile) {

        CacheManager Cm = null;
        String Sample_File = null;
        int InputFileRead = cacheSimulation?.checkNextSampleRate

        int EntriesAtATime = cacheSimulation?.numberOfEntries

        LogFileUpdate = cacheSimulation?.updateLOGFILERate

        try {

            println("From Now ONWARDS ......");
            println("type 'quit' or 'exit' to terminate the program. OR type 'back' to Go To Main Menu (AnyTIme) ");


            FileLogger.copytoLog("Cache File : " + cacheFile
                    + " -- Sample Data File :" + firstReadingFile
                    + " -- Cache Size : " + cacheSimulation?.cacheSize
                    + " -- Cache Refresh Rate : " + cacheSimulation?.cacheRefreshRate
                    + " -- Cache Replacing Policy : "
                    + (cacheSimulation?.replacingScheme == 1 ? "LRU" : "FIFO"));




            FileLogger
                    .copytoLog("\n\nTime\tTotal_Hits\tTotal_Miss\tEntries_in_Queue\t"
                    + "Entries_Expired_in_Queue\tTotal_Entries\n");


            println("----------------------------after file logger---------------------")



            CacheSimulateDocuments cacheSimulateDocuments = CacheSimulateDocuments?.findByCacheSimulation(cacheSimulation)
            String fileName = cacheSimulateDocuments?.name

            CacheDocuments cacheDocuments = CacheDocuments?.findByCacheSimulation(cacheSimulation)
            String cacheName = cacheDocuments?.name

            def webRootDir = AppUtil.staticResourcesDirPath
            String cacheFilename = webRootDir + "/uploadedFile/storeCacheFile/${cacheSimulation?.id}/" + cacheName
            String firstReadingFileName = webRootDir + "/uploadedFile/firstReadingFile/${cacheSimulation?.id}/" + fileName

            println("---------------i am hear---------------12------------" + cacheFilename)
            println("---------------i am hear----------------11-----------" + firstReadingFileName)





            Cm = CacheManager.GetCacheManagerInstance(cacheSimulation?.cacheSize as int,
                    cacheFilename, cacheSimulation?.cacheRefreshRate as int, LogFileUpdate as int, cacheSimulation?.replacingScheme as int);



            println("----------------------------after cm---------------------")


            boolean end = false;
            FileLogger.openNewHandle();
            int entries = 0;

            Sample_File = firstReadingFileName

            println("------------1-----------------before while-------")

            while (!end) {
                start_Reading(Sample_File);
                println("Reading data from File : " + Sample_File);
                while (true) {
                    println("------------2-----------------in while----true---")

                    LinkedList LIST = getNorLessEntriesFromFile();


                    println("------------3-----------------in while----true---" + LIST)


                    if (LIST.size() == 0) {
                        /*
                         * while (LIST.size() == 0) { println(
                         * "File seems Empty : Waiting for user to input data . Program will check the file after "
                         * + InputFileRead + " Seconds .");
                         * Thread.sleep(InputFileRead * 1000);
                         * println("Reading data from File : " +
                         * Sample_File); start_Reading(Sample_File); LIST =
                         * getNorLessEntriesFromFile(); }
                         */

                    }


                    println("----------------------------------4-----------------------------")

                    List list = Collections.synchronizedList(LIST);
                    for (Object obj : list) {
                        CachedObject entry = (CachedObject) obj;
                        Cm.putEntry(entry);
                        entries++;
                    }
                    println("----------------------------------5-----------------------------")

                    if (list.size() < EntriesAtATime) {
                        break;
                    }
                    println("----------------------------------6-----------------------------")

                    // remove data from sample.txt
                }

                println("-----------------------1--------he he-------------------------")

                println("\nFile Completely Read -> " + Sample_File);
                println("***********************************************************************************");

                try {
                    println("----------------------1-------------------------")
                    Sample_File = getNextFileName(Sample_File);
//                while (!FileExists(Sample_File)) {
//                    println("Reading in Sequence . File not found ->  "
//                            + Sample_File);
//                    println("Program will check after " + InputFileRead
//                            + " seconds");
//                    Thread.sleep(InputFileRead * 1000);
//                }
//
//                if (FileExists(Sample_File)) {
//                    start_Reading(Sample_File);
//                }
                }
                catch (Exception e) {
                    println("---------------------file not found----------------")
                }

            }
            FileLogger.closeLogHandle();
        }

        catch (InterruptedException ex) {
            println("Got the signal");
            Cm.refresh();
            Cm.killThreads();
            if (quit) {
                println("Program Stopping ...");
                return;

            }
            back = false;
            quit = false;
            Thread.sleep(2 * 1000);
        } catch (IOException ex) {
            Logger.getLogger(TTLCache.class.getName()).log(Level.SEVERE, null, ex);

        } catch (quitException ex) {
//            break;
        }
    }

    static void start_Reading(String _fFile) throws FileNotFoundException {
        scanner = new Scanner(new FileReader(_fFile));
        // get file lock
    }


    static LinkedList getNorLessEntriesFromFile() throws FileNotFoundException {
        LinkedList list = new LinkedList();
        try {
            // first use a Scanner to get each line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split("[\\s|\\t]");
                tokens[0] = tokens[0].trim();
                if (ipAddress.isvalid(tokens[0])) {

                    try {
                        Integer.parseInt(tokens[1]);
                        list.add(new CachedObject(new ipAddress(tokens[0]),
                                Integer.parseInt(tokens[1])));
                    } catch (NumberFormatException ex) {
                        continue;
                    }
                    if (list.size() == EntriesAtATime)
                        return list;
                }

            }
        } finally {
            // ensure the underlying stream is always closed
            // this only has any effect if the item passed to the Scanner
            // constructor implements Closeable (which it does in this case).
        }

        println("----------------------return list--------------------" + list)

        return list;

    }

    private static boolean FileExists(String filename) {
        File f = new File(filename);
        if (f.exists()) {
            return true;
        }
        return false;
    }

    public static String getNextFileName(String Sample_File) {
        Sample_File = Sample_File.replace(".", ",");
        String[] tokens = Sample_File.split(",");
        String name = tokens[0];
        String extension = tokens.length == 2 ? tokens[1] : "";
        int i = name.length() - 1;
        for (; i >= 0; --i) {
            if (!(name.charAt(i) >= '1' && name.charAt(i) <= '9')) {
                break;
            }
        }
        i++;
        String numberPart = null;
        String namePart = null;
        if (i != 0)
            namePart = String.copyValueOf(name.toCharArray(), 0, i);
        else
            namePart = "";

        if (i != name.length()) {
            numberPart = String.copyValueOf(name.toCharArray(), i,
                    name.length() - i);
            namePart += Integer.parseInt(numberPart) + 1;
        } else
            namePart += "1";
        return namePart + "." + extension;
    }
}
