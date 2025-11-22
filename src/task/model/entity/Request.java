package task.model.entity;

public class Request {
    private final String bookName;
    private int amount = 1;

    public Request(String bookName) {
        this.bookName = bookName;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void incrementAmount() {
        this.amount++;
    }

    public int getAmount() {
        return amount;
    }

    public String getBookName() {
        return bookName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Request) {
            return this.bookName.equals(((Request) obj).bookName);
        } else return false;
    }

    @Override
    public String toString() {
        return String.format("""
                REQUEST:
                    bookName: %s,
                    amount: %d
                """,
                bookName,
                amount
        );
    }


}
