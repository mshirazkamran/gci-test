package dev.shiraz.GCI.events;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class EventController {

    private static final Logger logger = LoggerFactory.
            getLogger(EventController.class);
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventsService) {
        this.eventService = eventsService;
    }

    @GetMapping("/")
    public String homePage() {

        return "index";
    }

//    @ResponseBody
    @PostMapping("/submitEventDetails")
    public String submitEventData
            (@ModelAttribute MyEvent userEvent,
             BindingResult bindingResult,
             Model model) {

        // called when user enters wrong / deviating details
        if (bindingResult.hasErrors()) {
            return "index";
        }

        if (!(userEvent.getSecretKey().equals("hEcYQ_)p-fh(H^~XScDqn]si@"))) {
            return "index";
        }

        String resultMessage = "";
        boolean isAddedOnGoogleCalendar = false;

        try {
            Optional<String> eventLink = eventService.createEvent(
                    userEvent.getEventTitle(),
                    userEvent.getEventSummary(),
                    userEvent.getDaysFromToday()
            );

            if (eventLink.isPresent()) {
                isAddedOnGoogleCalendar = true;
                resultMessage = eventLink.get();
                logger.info("{} has been created at link: {}", userEvent, eventLink.get());
            } else {
                isAddedOnGoogleCalendar = false;
                resultMessage = "Event creation failed or userEvent link couldn't be generated!";
            }

        } catch (IOException e) {

            resultMessage = "Couldn't add the userEvent due to IOException: " + userEvent.getEventTitle();
            throw new RuntimeException("Couldn't add the userEvent: " + userEvent.getEventTitle() , e);

        } finally {

            model.addAttribute("resultMessage", resultMessage);
            model.addAttribute("isAddedOnGoogleCalendar", isAddedOnGoogleCalendar);

        }

        return "results";
    }
}
