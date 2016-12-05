import common.utils.Log;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.lang.reflect.Method;
import java.util.concurrent.CompletionStage;

public class ActionCreator implements play.http.ActionCreator {
    private static final String TAG = ActionCreator.class.getSimpleName();

    @Override
    public Action.Simple createAction(Http.Request request, Method actionMethod) {
        Log.i(TAG, request.toString(), actionMethod.toString());

        // May add Authorization header checking here as zapya_api did

        return new Action.Simple() {
            @Override
            public CompletionStage<Result> call(Http.Context ctx) {
                return delegate.call(ctx);
            }
        };
    }
}
