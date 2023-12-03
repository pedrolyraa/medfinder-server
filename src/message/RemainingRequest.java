package message;

public class RemainingRequest extends Message {
    private int dailyUse;
    private int totalQuantity;

    public RemainingRequest(int dailyUse, int totalQuantity) {
        this.dailyUse = dailyUse;
        this.totalQuantity = totalQuantity;
    }

    public int getDailyUse() {
        return dailyUse;
    }

    public void setDailyUse(int dailyUse) {
        this.dailyUse = dailyUse;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}