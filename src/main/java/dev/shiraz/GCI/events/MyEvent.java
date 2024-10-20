package dev.shiraz.GCI.events;


import org.springframework.stereotype.Component;

//@Component
public class MyEvent {

//    @NotNull(message = "Please provide a title")
//    @Size(max = 100, message = "Size should not be greater than 100 characters")
    private String eventTitle;

//    @NotNull
//    @Size(min = 1, max = 14, message = "The date should be between 1 and 14 days from today")
    private int daysFromToday;

//    @NotNull(message = "Please provide an event summary")
//    @Size(max = 1000, message = "Size should not be greater than 1000 characters")
    private String eventSummary;

//    @NotNull(message = "secret key can not be null")
//    @Size(min = 25, max = 25, message = "The size of secret key is not valid")
    private String secretKey;


    public String getEventTitle() {
        return eventTitle;
    }

    public int getDaysFromToday() {
        return daysFromToday;
    }

    public String getEventSummary() {
        return eventSummary;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public void setDaysFromToday(int daysFromToday) {
        this.daysFromToday = daysFromToday;
    }

    public void setEventSummary(String eventSummary) {
        this.eventSummary = eventSummary;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String toString() {
        return "MyEvent{" +
                "eventTitle='" + eventTitle + '\'' +
                ", daysFromToday=" + daysFromToday +
                ", eventSummary='" + eventSummary + '\'' +
                ", secretKey='" + secretKey + '\'' +
                '}';
    }
}

