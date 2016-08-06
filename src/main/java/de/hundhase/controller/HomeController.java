package de.hundhase.controller;

import de.hundhase.models.Picture;
import de.hundhase.models.PictureDao;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
class HomeController {

    @Autowired
    private PictureDao pictureDao;

    @RequestMapping("/")
    String index(Model model) {
        String msg = "<h2>Herzlich willkommen</h2>\n";
        msg += "<p>Der Fotobereich ist passwortgeschützt. Bei Interesse wird dies aber gern weitergegeben. Einfach eine E-Mail schicken.</p>";
        msg += "<p><b>Es wurde mal Zeit für ein neues Kennwort. Einfach eine Mail schicken.</b></p>";
        msg += "<div id=\"footer\">";
        msg += "<p>quick access: <a href=\"#top\">Back to top</a> | <a href=\"impressum.html\">Impressum</a></p>";
        model.addAttribute("msg", msg);
        model.addAttribute("template", "welcome");
        return "main";
    }


    @RequestMapping(value = "/showThumbnails")
    public String showThumbnails(Model model) {
        int year = 2015;
        int month = 12;
        return showThumbnails(model, year, month);
    }


    @RequestMapping(value = "/showThumbnails/{year}/")
    public String showThumbnails(Model model, @PathVariable("year") int year) {
        int month = 12;

        DateTime now = DateTime.now();
        if (year == now.getYear()) {
            month = now.getMonthOfYear();
        }

        return showThumbnails(model, year, month);
    }

    @RequestMapping(value = "/showThumbnails/{year}/{month}/")
    public String showThumbnails(Model model, @PathVariable("year") int year, @PathVariable("month") int month) {
        List<Picture> pictures = pictureDao.findPictures(year, month).stream().collect(Collectors.toList());
        model.addAttribute("template", "gallery");
        model.addAttribute("pictures", pictures);
        return "main";
    }

    @RequestMapping(value = "/showPicture/{pictureId}/")
    public String showPicture(Model model, @PathVariable("pictureId") long pictureId) {
        Picture picture = pictureDao.findOne(pictureId);

        model.addAttribute("template", "picture");
        model.addAttribute("picture", picture);
        return "main";
    }

}