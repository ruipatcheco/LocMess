package pt.ulisboa.tecnico.cmu.tg14.DTO;

/**
 * Created by trosado on 5/9/17.
 */
public class HashResult {

    byte[] hash;

    public HashResult(byte[] hash) {
        this.hash = hash;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }


}
