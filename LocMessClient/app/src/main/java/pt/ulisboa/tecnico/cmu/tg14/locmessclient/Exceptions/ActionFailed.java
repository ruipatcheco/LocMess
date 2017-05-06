package pt.ulisboa.tecnico.cmu.tg14.locmessclient.Exceptions;


/**
 * Created by trosado on 5/6/17.
 */

public class ActionFailed extends Exception {

    String actionName = "";

    private ActionFailed(){}
    public ActionFailed(String actionName) {
        super();
        this.actionName = actionName;
    }

    @Override
    public String getMessage() {
        return "Action "+actionName+" failed on server-side.";
    }
}