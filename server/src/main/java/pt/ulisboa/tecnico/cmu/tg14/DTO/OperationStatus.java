package pt.ulisboa.tecnico.cmu.tg14.DTO;

/**
 * Created by trosado on 4/26/17.
 */
public class OperationStatus {
    String status;

    public OperationStatus(){
        setOK();
    }

    public void setOK(){
        status = "OK";
    }

    public void setError(){
        status = "Error";
    }

    public String getStatus(){
        return status;
    }
}
