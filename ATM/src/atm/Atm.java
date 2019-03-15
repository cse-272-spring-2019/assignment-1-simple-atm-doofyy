package atm;

public class Atm {
	private long cardNumber;
	private String name;
	private static long balance = 0;
		public long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public static long getBalance() {
		return balance;
	}
	@SuppressWarnings("static-access")
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public static void withdraw(long amount) {
			balance-=amount;
	}
	public static void deposit(long amount) {
		balance+=amount;
	}

}