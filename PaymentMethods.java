abstract class Payment {
    private double amount;
    Payment(double amount){
        this.amount=amount;
    }
    public double getAmount(){
        return amount;
    }

    public void setAmount(double amount){
        this.amount=amount;
    }

    abstract void makePayment();
    void generateReceipt(){
        System.out.println("Amount is sent"); 
    }

}
class CreditCardPayement extends Payment {
    CreditCardPayement( double amount){
        super(amount);
    }
    void makePayment(){
        System.out.println("Paying " + getAmount() + " using credit card..");
    }
}

class UPIPayement extends Payment {
    UPIPayement( double amount){
        super(amount);
    }
    void makePayment(){
        System.out.println("Paying " + getAmount() + " using UPI..");
    }
}
class WalletPayement extends Payment {
    WalletPayement( double amount){
        super(amount);
    }
    void makePayment(){
        System.out.println("Paying " + getAmount() + " using wallet..");
    }
}

public class PaymentMethods{
    public static void main(String[] args) {
        Payment p1 = new CreditCardPayement(2500);
        Payment p2 = new UPIPayement(25);
        Payment p3 = new WalletPayement(500);
        p1.makePayment();
        p1.generateReceipt();
        p2.makePayment();
        p2.generateReceipt();
        p3.makePayment();                                               
        p3.generateReceipt();
    }
}