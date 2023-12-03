package message;

public class Result extends Message {

    private double result;

    public String toString(){
        return(""+this.result);
    }

    public Result(double result) {
        this.result = result;
    }
    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

}