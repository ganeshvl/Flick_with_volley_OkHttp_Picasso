package mitul.flickster.handler;

/**
 * Created by mitul on 29/09/15.
 */
public abstract class RequestHandler {
    protected RequestHandler mSuccessor;

    public void setSuccessor(RequestHandler successor){
        this.mSuccessor = successor;
    }
    public abstract int handleRequest(String request);
}
