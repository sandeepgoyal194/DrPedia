package frameworks.network.usecases;

import rx.Observable;
import rx.Subscriber;


public interface Interactor<T> {

    void execute(Subscriber<T> subscriber);

    void execute(RequestParams requestParams, Subscriber<T> subscriber);

    Observable<T> getExecuteObservable(RequestParams requestParams);
}
