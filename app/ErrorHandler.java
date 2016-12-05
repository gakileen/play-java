import play.Configuration;
import play.Environment;
import play.Logger;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.http.HttpErrorHandlerExceptions;
import play.api.routing.Router;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ErrorHandler extends play.http.DefaultHttpErrorHandler {

    private final Environment environment;
    private final OptionalSourceMapper sourceMapper;

    @Inject
    public ErrorHandler(Configuration configuration, Environment environment,
                        OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(configuration, environment, sourceMapper, routes);

        this.environment = environment;
        this.sourceMapper = sourceMapper;
    }

    @Override
    protected CompletionStage<Result> onBadRequest(RequestHeader request, String message) {
        return CompletableFuture.completedFuture(Results.badRequest(message));
    }

    @Override
    public CompletionStage<Result> onClientError(RequestHeader request, int statusCode,
                                                 String message) {
        if (statusCode == play.mvc.Http.Status.NOT_FOUND) {
            return CompletableFuture
                    .completedFuture(Results.badRequest("No such handler. " + message));
        } else if (statusCode == play.mvc.Http.Status.BAD_REQUEST) {
            return CompletableFuture.completedFuture(Results.badRequest(message));
        } else {
            return super.onClientError(request, statusCode, message);
        }
    }


    @Override
    public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {
        try {
            UsefulException usefulException = throwableToUsefulException(exception);

            logServerError(request, usefulException);

            switch (environment.mode()) {
                case PROD:
                    return onProdServerError(request, usefulException);
                default:
                    return onDevServerError(request, usefulException);
            }
        } catch (Exception e) {
            Logger.error("Error while handling error", e);
            return CompletableFuture.completedFuture(Results.internalServerError());
        }
    }

    protected CompletionStage<Result> onDevServerError(RequestHeader request, UsefulException exception) {
        return CompletableFuture.completedFuture(Results.internalServerError("A server error occurred: " + exception.getMessage()));
    }

    protected CompletionStage<Result> onProdServerError(RequestHeader request, UsefulException exception) {
        return CompletableFuture.completedFuture(Results.internalServerError("A server error occurred!"));
    }

    protected void logServerError(RequestHeader request, UsefulException usefulException) {
        Logger.error(String.format("\n\n! @%s - Internal server error, for (%s) [%s] ->\n",
                usefulException.id, request.method(), request.uri()),
                usefulException
        );
    }

    private UsefulException throwableToUsefulException(final Throwable throwable) {
        return HttpErrorHandlerExceptions.throwableToUsefulException(sourceMapper.sourceMapper(), environment.isProd(), throwable);
    }

}
