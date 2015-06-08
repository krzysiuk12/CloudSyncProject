package pl.edu.agh.iosr.domain.cloud.session;

/**
 * Enumeration represents CloudSession status.
 *
 * Created by Krzysztof Kicinger on 2015-04-11.
 */
public enum CloudSessionStatus {

    /**
     * If session is active it means, that can be used to access all cloud management operations.
     */
    ACTIVE,
    /**
     *
     */
    EXPIRED,
    /**
     *
     */
    REMOVED

}
