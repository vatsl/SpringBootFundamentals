package ttl.larku.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ttl.larku.domain.Preferences;

@Controller
@RequestMapping("/preferences")
public class PreferencesController {

    @RequestMapping(method = RequestMethod.GET)
    public String get(Preferences preferences) {
        preferences.setFavoriteNotes(new String[]{"C#", "Eb"});
        String[] interests = {"lilting", "humming"};

        preferences.setStringInstrument("Double Bass");
        preferences.setInterests(interests);
        preferences.setProfessional(false);

        System.out.println("In First Get");
        //return "/WEB-INF/jsp/preferences/preferences.jsp";
        return "preferences";
    }


    @RequestMapping(method = RequestMethod.POST)
    public String add(Preferences preferences, BindingResult result) {
        if (result.hasErrors()) {
            return "preferences";
        }
        System.out.println("In POST, preferences = " + preferences);
        return "redirect:preferences";
        //return "preferences";
    }
}