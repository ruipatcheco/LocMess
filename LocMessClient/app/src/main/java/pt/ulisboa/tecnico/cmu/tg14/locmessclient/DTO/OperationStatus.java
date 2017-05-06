package pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO;

/**
 * Created by trosado on 4/26/17.
 */
public class OperationStatus {
    StatusCode status;

    public enum StatusCode {
        OK, ERROR
    }

    public OperationStatus(){
        setOK();
    }

    public void setOK(){
        status = StatusCode.OK;
    }

    public void setError(){
        status = StatusCode.ERROR;
    }

    public StatusCode getStatus(){
        return status;
    };

    public boolean isOK(){
        return status == StatusCode.OK;
    }


    public boolean isERROR(){
        return status == StatusCode.ERROR;
    }
}
