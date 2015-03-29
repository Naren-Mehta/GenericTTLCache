package utils

import grails.util.GrailsUtil
import org.codehaus.groovy.grails.web.context.ServletContextHolder

import java.util.regex.Matcher
import java.util.regex.Pattern

class AppUtil {

    static Boolean save(def object) {
        Boolean result = false
        if (object.validate() && !object.hasErrors() && object.save(flush: true)) {
            result = true
        } else {
            object.errors.allErrors.each {
                println '----------------------' + it

            }
            result = false

        }
        return result
    }

    public static String getStaticResourcesDirPath() {
        String path = '';
        if (GrailsUtil.developmentEnv) {
            path = ServletContextHolder.getServletContext().getRealPath("/")
        } else {
            path = '/home/ubuntu/cacheSimulationStatic/'
        }
        return path
    }

    public static String getUrlId(String youtubeUrl) {
        int index
        String id

        String video_id = "";
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http")) {

            String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*";
            // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
            CharSequence input = youtubeUrl;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                String groupIndex1 = matcher.group(7);
                if (groupIndex1 != null && groupIndex1.length() == 11)
                    video_id = groupIndex1;
            }
        }
        return video_id;
    }

}
