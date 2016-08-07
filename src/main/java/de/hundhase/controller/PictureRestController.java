package de.hundhase.controller;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.catalog.model.CatalogService;
import com.fasterxml.jackson.annotation.JsonView;
import de.hundhase.models.Picture;
import de.hundhase.models.PictureDao;
import de.hundhase.util.ConsulUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/v1/pictures")
class PictureRestController implements IRestController {

    @Autowired
    private ConsulClient consulClient;

    @Autowired
    private PictureDao pictureDao;

    @JsonView(Picture.Views.Extended.class)
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getPictures(WebRequest webRequest) {
        try {
            Optional<Pageable> pageable = getPageable(webRequest);
            Strategy strategy = pageable.isPresent() ? new PageableStrategy(pageable.get()) : new DefaultStrategy();

            Optional<Integer> month = getIntParameter("month", webRequest);
            Optional<Integer> year = getIntParameter("year", webRequest);

            Response<List<CatalogService>> listResponse = consulClient.getCatalogService("application", QueryParams.DEFAULT);
            for (CatalogService cs : listResponse.getValue()) {
                URL url = ConsulUtil.getURL(cs, "/v1/health");
                System.out.println(url);
            }

            List<?> pictures;
            if (month.isPresent() && year.isPresent()) {
                pictures = strategy.getPictures(year.get(), month.get());
            } else {
                pictures = strategy.getAll();
            }

            return ResponseEntity.ok(pictures);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @RequestMapping(value = "/{pictureId}")
    public ResponseEntity<?> showPicture(Model model, @PathVariable("pictureId") long pictureId, WebRequest webRequest) {
        try {
            Picture picture = pictureDao.findOne(pictureId);
            Optional<String> select = getParameter("select", webRequest);
            return ResponseEntity.ok(partializeObject(picture, select));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private interface Strategy {
        List<Picture> getPictures(Integer year, Integer month);

        List<Picture> getAll();
    }

    private class PageableStrategy implements Strategy {

        private Pageable pageable;

        public PageableStrategy(Pageable pageable) {
            this.pageable = pageable;
        }

        @Override
        public List<Picture> getPictures(Integer year, Integer month) {
            return pictureDao.findPictures(year, month, pageable).stream().collect(Collectors.toList());
        }

        @Override
        public List<Picture> getAll() {
            List<Picture> pictures = new ArrayList<>();
            pictureDao.findAll(pageable).forEach(pictures::add);
            return pictures;
        }
    }

    private class DefaultStrategy implements Strategy {

        @Override
        public List<Picture> getPictures(Integer year, Integer month) {
            return pictureDao.findPictures(year, month).stream().collect(Collectors.toList());
        }

        @Override
        public List<Picture> getAll() {
            List<Picture> pictures = new ArrayList<>();
            pictureDao.findAll().forEach(pictures::add);
            return pictures;
        }
    }


}