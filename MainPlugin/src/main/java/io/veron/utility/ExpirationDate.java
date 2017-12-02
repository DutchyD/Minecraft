package io.veron.utility;

public interface ExpirationDate {

    int getRemaining();

    void increase(int seconds);

    void decrease(int seconds);

    void expire();
}
