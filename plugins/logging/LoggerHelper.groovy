/**
 * Log per channel
 * Output html ?
 * Log html on the fly, or transform later ?
 * Option ?
 * <li><span class="timestamp">10:00:00</span><span class="nickname">mwinmwin</span>MON MESSAGE</li>
 * <li><ul><li class="timestamp">10:00:00</li><li class="nickname">mwinmwin</li><li>MON MESSAGE</li></ul></li>
 */
package logging

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.PatternLayout
import ch.qos.logback.classic.spi.LoggingEvent
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy

/**
 * We're completely using our own Logger and LoggerContext, here.
 */
class LoggerHelper {
    final def logger

    def log(s) {
        logger.info(s)
    }

    LoggerHelper() {
        def lc = new LoggerContext()
        lc.start()
        logger = lc.getLogger("ROOT")
        PatternLayout pl = new PatternLayout()
        pl.setPattern("%d %5p %t [%c:%L] %m%n)")
        pl.setContext(lc)
        pl.start()

//        def rollingPolicy = new DefaultTimeBasedFileNamingAndTriggeringPolicy()
        //        rollingPolicy.get
        //        rollingPolicy.start()

        // TODO - consider this (from docs) - perhaps not the best thing here... since there's a good chance no events will occur until the next morning at 8am
        //  If there are no logging events during, say 23 minutes and 47 seconds after midnight,
        // then rollover will actually occur at 00:23'47 AM on March 9th and not at 0:00 AM.
        // Thus, depending on the arrival rate of events, rollovers might be triggered with some latency.


        RollingFileAppender<LoggingEvent> fileAppender = new RollingFileAppender<LoggingEvent>()
//        FileAppender<LoggingEvent> fileAppender = new FileAppender<LoggingEvent>()
        fileAppender.file = 'padgett.log'
        fileAppender.setContext(lc)
        fileAppender.setLayout(pl)

        def rollingPolicy = new TimeBasedRollingPolicy()
        rollingPolicy.setParent(fileAppender)
        rollingPolicy.setContext(lc)

        rollingPolicy.fileNamePattern = "padgett.%d{yyyy-MM-dd}.log"
//        rollingPolicy.fileNamePattern = "padgett.%d{yyyy-MM-dd_HH-mm}.log"
        rollingPolicy.maxHistory = 0 // no delete
        rollingPolicy.start()


        // TODO fileAppender.triggeringPolicy =
        fileAppender.rollingPolicy = rollingPolicy

        fileAppender.start()

        logger.addAppender(fileAppender)
//        LoggerFactory.getLogger("PadgettIRCLogger").info("SALUT LES FILLES")
        logger.info("SALUT LES FILLES")
    }

    def stop() {}

    //    == FileAppender<LoggingEvent> fileAppender = (FileAppender<LoggingEvent>)
    //if (fileAppender != null) {
    //    fileAppender.stop()
    //    fileAppender.setFile("new.log")
    //    PatternLayout pl = new PatternLayout()
    //    pl.setPattern("%d %5p %t [%c:%L] %m%n)")
    //    pl.setContext(lc)
    //    pl.start()
    //    fileAppender.setLayout(pl)
    //    fileAppender.setContext(lc)
    //    fileAppender.start()
    //}
    //    ... etc
}