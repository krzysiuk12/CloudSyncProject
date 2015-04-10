package pl.edu.agh.iosr.cloud.common.tasks;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Class performs wrapper role for all tasks performed between application and cloud providers.
 *
 * @param <T> Returned object's type
 *
 * Created by Krzysztof Kicinger on 2015-04-09.
 */
public abstract class CloudTask<T> extends FutureTask<T> {

    protected CloudTask(Callable<T> callable) {
        super(callable);
    }

    /**
     *
     * @return Ratio that is [0.0, 1.0]
     */
    public abstract float getProgress();
}
