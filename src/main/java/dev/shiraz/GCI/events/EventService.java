package dev.shiraz.GCI.events;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.google.api.services.calendar.*;


import org.springframework.stereotype.Service;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final Calendar calendarService;

    // make this an environment variable
    private static final String calendarId
            = "09f5d3c956275e39b0e95b17ef8130e04bda72bb53beb611ce4efe37e1642780@group.calendar.google.com";


    public EventService() throws GeneralSecurityException, IOException {
        this.calendarService = CalendarService.calendarServiceBuilder();
    }


    private static class CalendarService {

        private static final String ApplicationName = "Google Calendar Events";
        private static final JsonFactory jsonInstance = GsonFactory.getDefaultInstance();
        private static final List<String> Scopes = Collections.singletonList(CalendarScopes.CALENDAR);
        static final String calendarId
                = EventService.calendarId;
        // credentials are obtained as a stream using classpath
        private static final String credentialsFilePath = "/creds-GCI.json";


        private static Credential getCredentials(final NetHttpTransport HTTPTransport)
                throws IOException {

            InputStream in = EventService.class.getResourceAsStream(credentialsFilePath);
            if (in == null) {
                throw new FileNotFoundException("File :"+  credentialsFilePath +" not found");
            }

            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonInstance, new InputStreamReader(in));

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder
                    (HTTPTransport, jsonInstance, clientSecrets, Scopes)
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(credentialsFilePath)))
                    .setAccessType("offline")
                    .build();

            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

            Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

            return credential;
        }

        private static Calendar calendarServiceBuilder() throws IOException, GeneralSecurityException {

            final NetHttpTransport HTTPTransport = GoogleNetHttpTransport.newTrustedTransport();
            Calendar calendarService = new Calendar.Builder
                    (HTTPTransport, jsonInstance, getCredentials(HTTPTransport))
                    .setApplicationName(ApplicationName)
                    .build();
            return calendarService;
        }
    }




//    public static void main(String... args) throws IOException, GeneralSecurityException {
//
//        Event event = buildEventDetails(
//                "Making burgers",
//                "we will be enjoying burgers in pakistan",
//                4
//        );
//
//        executeEvent(event);
//    }


    public Optional<String> createEvent(String summary, String description, int daysFromToday)
            throws IOException{

        // these elements are only used in building event reminder, refer to Event.Reminders for real object.
        EventReminder[] reminders = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(1140),
                new EventReminder().setMethod("popup").setMinutes(10),
        };

        // main object that is used to build the event reminders
        Event.Reminders remindersBuilder = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminders));

        String[] recurrence = new String[]{"RRULE:FREQ=DAILY;UNTIL=20241010T170000Z"};

        EventDateTime startTime = new EventDateTime()
                .setDateTime(new DateTime(System.currentTimeMillis()))
                .setTimeZone("Asia/Karachi");

        EventDateTime endTime = new EventDateTime()
                .setDateTime(new DateTime(System.currentTimeMillis() + (86400000L * daysFromToday)))
                .setTimeZone("Asia/Karachi");


        Event event = new Event()
                .setSummary(summary)
                .setDescription(description)
                .setStart(startTime)
                .setEnd(endTime)
                .setCreator(new Event.Creator().setSelf(true))
                .setReminders(remindersBuilder)
                .setRecurrence(Arrays.asList(recurrence));



        Optional<String> eventCreationLink = executeEvent(event);
        return eventCreationLink;

    }

    private Optional<String> executeEvent(Event event) throws IOException {

        Event executedEvent;
        try {
            executedEvent = calendarService.events().insert(calendarId, event).execute();
        } catch (IOException e) {
            System.err.println("Error executing event: " + e.getMessage());
            return Optional.empty(); // Return an empty Optional if there was an error
        }

        // Extract the event link
        return Optional.ofNullable(executedEvent.getHtmlLink());
    }



    // method to add a random event
    private void addEvent() throws IOException {

        Event event = new Event()
                .setSummary("Google I/O 2015")
                .setLocation("800 Howard St., San Francisco, CA 94103")
                .setDescription("A chance to hear more about Google's developer products.");

        DateTime startDateTime = new DateTime("2015-05-28T09:00:00-07:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setStart(start);


        DateTime endDateTime = new DateTime("2015-05-28T17:00:00-07:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setEnd(end);

        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
        event.setRecurrence(Arrays.asList(recurrence));

        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail("lpage@example.com"),
                new EventAttendee().setEmail("sbrin@example.com"),
        };
        event.setAttendees(Arrays.asList(attendees));

        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        String calendarId = "primary";
        event = calendarService.events().insert(calendarId, event).execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());

    }


//    public void getEvents(Calendar calendarService) throws IOException {
//
//        Events events = calendarService.events().list(calendarId)
//                .setMaxResults(10)
//                .setTimeMin(new DateTime(System.currentTimeMillis()))
//                .setOrderBy("startTime")
//                .setSingleEvents(true)
//                .execute();
//
//        List<Event> eventList = events.getItems();
//
//        if (eventList.isEmpty()) {
//            System.out.println("No upcoming events found.");
//        } else {
//            System.out.println("Upcoming events");
//            for (Event event : eventList) {
//                DateTime start = event.getStart().getDateTime();
//                if (start == null) {
//                    start = event.getStart().getDate();
//                }
//                System.out.printf("%s (%s)\n, description: %s", event.getSummary(), start, event.getDescription());
//                System.out.println();
//
//            }
//        }
//    }
}

